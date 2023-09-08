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
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;

import com.isam.bean.Commit;
import com.isam.bean.CommitDetail;
import com.isam.bean.CommitReport;
import com.isam.bean.SubCommit;
import com.isam.dao.ApprovalOptionDAO;
import com.isam.dao.CommitRestrainTypeDAO;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.CommitService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.SubCommitDetailService;
import com.isam.service.SubCommitReportDetailService;
import com.isam.service.SubCommitReportService;
import com.isam.service.SubCommitService;
import com.isam.service.SubCommitXReceiveNoService;

public class SubCommitShowAddReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitService commitSer;
	private SubCommitService subSer;
	private SubCommitReportService crSer;
	private SubCommitReportDetailService crdSer;
//	private SubCommitXRestrainOfficeService subOSer;
	private SubCommitXReceiveNoService subRnSer;
	private SubCommitDetailService detailSer;
	private Map<String, String> CRType;
	private Map<String,String> AoCode;
	private Map<String, String> UserName;
	private Map<String, String> IDNOToName;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
//		subOSer = new SubCommitXRestrainOfficeService();
		subRnSer = new SubCommitXReceiveNoService();
		detailSer = new SubCommitDetailService();
		subSer=new SubCommitService();
		commitSer = new CommitService();
		crSer =new SubCommitReportService();
		crdSer = new SubCommitReportDetailService();
		CRType = CommitRestrainTypeDAO.getTypeMap();
		AoCode = ApprovalOptionDAO.getOptionMapByType("Commit");
		ProjectKeyHelp help = new ProjectKeyHelp();
		UserName=help.getUserToName();
		IDNOToName=help.getIDNOToName();
		
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		session.removeAttribute("subbean");
		session.removeAttribute("receiveNo");
		session.removeAttribute("subinfo");
		session.removeAttribute("AoCode");
		session.removeAttribute("detailBean");
		session.removeAttribute("CRType");
		session.removeAttribute("crBean");
		session.removeAttribute("crAry");
		session.removeAttribute("codeStr");
		
		String repserno=DataUtil.nulltoempty(request.getParameter("repserno"));
		
		String subserno=DataUtil.nulltoempty(request.getParameter("subserno"));
		String editType=DataUtil.nulltoempty(request.getParameter("editType"));
		Map<String,String> info=new HashMap<String, String>();
		CommitReport crBean;
		if(editType.equals("edit")){
			crBean = crSer.select(repserno);
			subserno = String.valueOf(crBean.getSerno());
			JSONArray crAry = crdSer.getJsonFmt(repserno);
			info.put("repserno", repserno);
			info.put("editor", UserName.get(crBean.getUpdateuser()));
			session.setAttribute("crBean", crBean);
			session.setAttribute("crAry", crAry);
		}
		
		SubCommit subbean=subSer.select(subserno);
		if(subbean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您要新增的填報執行情形所屬的管制項目已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit';</script>");
			out.flush();
			out.close();
			return;
		}
		session.setAttribute("subbean", subbean);
		
		String serno= subbean.getSerno();
		String investNo=subbean.getInvestNo();
		Commit cbean =commitSer.select(serno);
		String idno=cbean.getIDNO();
		
		info.put("serno", serno);
		info.put("investName", InvestNoToName.get(investNo));
		info.put("editType", editType);
		info.put("idno", idno);
		info.put("investor", IDNOToName.get(idno));
		info.put("typeStr", CRType.get(subbean.getType()));
		
		List<CommitDetail> detailBean = detailSer.select(subserno);	
		session.setAttribute("detailBean", detailBean);
		session.removeAttribute("detailEx");
		
		if(subbean.getType()!=null && subbean.getType().equals("01")){
			Map<String,Map<String,String>> exMap = detailSer.selectWithoutTotal(subserno, subbean.getType());
			if(exMap!=null&&!exMap.isEmpty()){
				session.setAttribute("detailEx", exMap);
			}
		}
		
//		info.put("office", subOSer.selectStr(subserno));
		info.put("receive", subRnSer.selectStr(subserno));
		
		session.setAttribute("receiveNo", subRnSer.select(subserno));
		
		session.setAttribute("subinfo", info);
//		session.setAttribute("AoCode", AoCode);
//		session.setAttribute("CRType", CRType);
		if(!subbean.getRepType().isEmpty()){
			String codeStr=AoCode.get(subbean.getRepType());
			session.setAttribute("codeStr", codeStr);
		}
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/commit/subcommitreport.jsp");
	}
}
