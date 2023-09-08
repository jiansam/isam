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

import com.isam.bean.Commit;
import com.isam.bean.CommitXInvestNo;
import com.isam.bean.CommitXReceiveNo;
import com.isam.dao.ApprovalOptionDAO;
import com.isam.dao.CommitRestrainTypeDAO;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitService;
import com.isam.service.CommitXInvestNoService;
import com.isam.service.CommitXReceiveNoService;
import com.isam.service.CommitXRestrainOfficeService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.SubCommitService;

public class CommitShowOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitXRestrainOfficeService officeSer;
	private CommitXReceiveNoService receNoSer;
	private CommitXInvestNoService investNoSer;
	private SubCommitService SubSer;
	private CommitDetailService detailSer;
	private CommitService commitSer;
	private Map<String, String> UserName;
	private Map<String, String> CRType;
	private Map<String,String> AoCode;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		officeSer = new CommitXRestrainOfficeService();
		receNoSer = new CommitXReceiveNoService();
		detailSer = new CommitDetailService();
		investNoSer=new CommitXInvestNoService();
		commitSer = new CommitService();
		SubSer = new SubCommitService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		UserName=help.getUserToName();
		CRType = CommitRestrainTypeDAO.getTypeMap();
		AoCode = ApprovalOptionDAO.getOptionMapByType("Commit");
		
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		session.removeAttribute("receiveNo");
		session.removeAttribute("investNo");
		session.removeAttribute("InvestNoToName");
		session.removeAttribute("CRType");
		session.removeAttribute("AoCode");
		session.removeAttribute("bean");
		session.removeAttribute("oInfo");
		session.removeAttribute("detailTotal");
		session.removeAttribute("detailEx");
		session.removeAttribute("office");
		
		String serno=request.getParameter("serno")==null?"":request.getParameter("serno").trim();
		String idno=request.getParameter("idno")==null?"":request.getParameter("idno").trim();
		String investor=request.getParameter("investor")==null?"":request.getParameter("investor").trim();
		String tabNo=DataUtil.nulltoempty(request.getParameter("tabNo"));

		Map<String,String> oInfo=new HashMap<String, String>();
		oInfo.put("serno", serno);
		oInfo.put("idno", idno);
		oInfo.put("investor", investor);
		oInfo.put("tabNo", tabNo);
		
		Commit bean= commitSer.select(serno);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommitdetail.jsp?serno="+idno+"';</script>");
			out.flush();
			out.close();
			return;
		}
		session.setAttribute("bean", bean);
		oInfo.put("editor", UserName.get(bean.getUpdateuser()));
		session.setAttribute("oInfo", oInfo);

		if(!bean.getType().equals("04")){
			Map<String, String> detailTotal = detailSer.selectTotalValue(serno);
			session.setAttribute("detailTotal", detailTotal);
			List<List<String>> detailEx =  detailSer.select(serno,bean.getType());
			if(detailEx!=null&&!detailEx.isEmpty()){
				session.setAttribute("detailEx", detailEx);
			}
		}
		
		String office = officeSer.selectStrName(serno);
		if(office!=null&&!office.isEmpty()){
			session.setAttribute("office", office);
		}
		String type=bean.getType();
		if(type.equals("01")||type.equals("03")){
			session.setAttribute("investNo", SubSer.getInvestNOXSubList(serno,type));
		}else{
			List<CommitXInvestNo> investNotemp = investNoSer.select(serno);
			Map<String,String> investNo = new HashMap<String, String>();
			for (CommitXInvestNo b:investNotemp) {
				investNo.put(b.getInvestNo(),InvestNoToName.get(b.getInvestNo()));
			}
			session.setAttribute("investNo", investNo);
		}
		
		List<CommitXReceiveNo> receiveNo =receNoSer.select(serno);
		session.setAttribute("receiveNo", receiveNo);
		session.setAttribute("InvestNoToName", InvestNoToName);
		session.setAttribute("CRType", CRType);
		session.setAttribute("AoCode", AoCode);
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/commit/seecommitrestrain.jsp");
		//request.getRequestDispatcher("/console/commit/seecommitrestrain.jsp").forward(request, response);
	}
}
