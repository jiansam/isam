package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import com.isam.bean.Commit;
import com.isam.bean.CommitDetail;
import com.isam.bean.CommitReport;
import com.isam.bean.CommitXInvestNo;
import com.isam.bean.CommitXReceiveNo;
import com.isam.dao.ApprovalOptionDAO;
import com.isam.dao.CommitRestrainTypeDAO;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitReportDetailService;
import com.isam.service.CommitReportService;
import com.isam.service.CommitService;
import com.isam.service.CommitXInvestNoService;
import com.isam.service.CommitXReceiveNoService;
import com.isam.service.CommitXRestrainOfficeService;
import com.isam.service.ProjectKeyHelp;

public class CommitShowAddReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitXRestrainOfficeService officeSer;
	private CommitXReceiveNoService receNoSer;
	private CommitXInvestNoService investNoSer;
	private CommitDetailService detailSer;
	private CommitService commitSer;
	private CommitReportDetailService crdSer;
	private CommitReportService crSer;
	private Map<String, String> CRType;
	private Map<String,String> AoCode;
	private Map<String, String> UserName;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		officeSer = new CommitXRestrainOfficeService();
		receNoSer = new CommitXReceiveNoService();
		detailSer = new CommitDetailService();
		commitSer = new CommitService();
		investNoSer = new CommitXInvestNoService();
		crdSer = new CommitReportDetailService();
		crSer = new CommitReportService();
		CRType = CommitRestrainTypeDAO.getTypeMap();
		AoCode = ApprovalOptionDAO.getOptionMapByType("Commit");
		ProjectKeyHelp help = new ProjectKeyHelp();
		UserName=help.getUserToName();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		request.removeAttribute("repserno");
		request.removeAttribute("crBean");
		request.removeAttribute("crAry");
		request.removeAttribute("editor");
		request.removeAttribute("serno");
		request.removeAttribute("editType");
		request.removeAttribute("idno");
		request.removeAttribute("investor");
		request.removeAttribute("bean");
		request.removeAttribute("detailBean");
		request.removeAttribute("detailEx");
		request.removeAttribute("office");
		request.removeAttribute("receiveNo");
		request.removeAttribute("investNo");
		request.removeAttribute("codeStr");
		String repserno=request.getParameter("repserno")==null?"":request.getParameter("repserno").trim();
		
		String serno=request.getParameter("serno")==null?"":request.getParameter("serno").trim();
		String editType=request.getParameter("editType")==null?"":request.getParameter("editType").trim();
		String idno=request.getParameter("idno")==null?"":request.getParameter("idno").trim();
		String investor=request.getParameter("investor")==null?"":request.getParameter("investor").trim();
		CommitReport crBean;
		if(editType.equals("edit")){
			crBean = crSer.select(repserno);
			serno = String.valueOf(crBean.getSerno());
			JSONArray crAry = crdSer.getJsonFmt(repserno);
			request.setAttribute("repserno", repserno);
			request.setAttribute("crBean", crBean);
			request.setAttribute("crAry", crAry);
			request.setAttribute("editor", UserName.get(crBean.getUpdateuser()));
		}
		request.setAttribute("serno", serno);
		request.setAttribute("editType", editType);
		request.setAttribute("idno", idno);
		request.setAttribute("investor", investor);
//		System.out.println("commitShowAdd:"+serno+":"+editType+":"+idno+":"+investor);
		Commit bean= commitSer.select(serno);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您要新增的填報執行情形所屬的管制項目已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommitdetail.jsp?serno="+idno+"';</script>");
			out.flush();
			out.close();
			return;
		}
		request.setAttribute("bean", bean);
		
		List<CommitDetail> detailBean = detailSer.select(serno);
		request.setAttribute("detailBean", detailBean);
		if(bean.getType().equals("02")||bean.getType().equals("03")){
			List<List<String>> detailEx =  detailSer.select(serno,bean.getType());
			if(detailEx!=null&&!detailEx.isEmpty()){
				request.setAttribute("detailEx", detailEx);
			}
		}else if(bean.getType().equals("01")){
			Map<String,Map<String,String>> exMap = detailSer.selectWithoutTotal(serno, bean.getType());
			if(exMap!=null&&!exMap.isEmpty()){
				request.setAttribute("detailEx", exMap);
			}
		}
		String office = officeSer.selectStr(serno);
		if(office!=null&&!office.isEmpty()){
			request.setAttribute("office", office);
		}
		
		List<CommitXReceiveNo> receiveNo =receNoSer.select(serno);
		request.setAttribute("receiveNo", receiveNo);
		List<CommitXInvestNo> investNotemp = investNoSer.select(serno);
		Map<String,String> investNo = new HashMap<String, String>();
		for (CommitXInvestNo b:investNotemp) {
			investNo.put(b.getInvestNo(),InvestNoToName.get(b.getInvestNo()));
		}
		request.setAttribute("investNo", investNo);
		
		String typeStr=CRType.get(bean.getType());
		request.setAttribute("typeStr", typeStr);
		if(!bean.getRepType().isEmpty()){
			String codeStr=AoCode.get(bean.getRepType());
			request.setAttribute("codeStr", codeStr);
		}
		
		request.getRequestDispatcher("/console/commit/commitreport.jsp").forward(request, response);
//		String path = request.getContextPath();
//		response.sendRedirect(path + "/console/commit/commitreport.jsp");
	}
}
