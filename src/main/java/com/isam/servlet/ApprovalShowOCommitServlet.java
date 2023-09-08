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

import com.isam.bean.Commit;
import com.isam.bean.CommitXInvestNo;
import com.isam.bean.CommitXReceiveNo;
import com.isam.dao.ApprovalOptionDAO;
import com.isam.dao.CommitRestrainTypeDAO;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitService;
import com.isam.service.CommitXInvestNoService;
import com.isam.service.CommitXReceiveNoService;
import com.isam.service.CommitXRestrainOfficeService;
import com.isam.service.ProjectKeyHelp;

public class ApprovalShowOCommitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitXRestrainOfficeService officeSer;
	private CommitXReceiveNoService receNoSer;
	private CommitXInvestNoService investNoSer;
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
		commitSer = new CommitService();
		investNoSer = new CommitXInvestNoService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		UserName=help.getUserToName();
		CRType = CommitRestrainTypeDAO.getTypeMap();
		AoCode = ApprovalOptionDAO.getOptionMapByType("Commit");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		String serno=request.getParameter("serno")==null?"":request.getParameter("serno").trim();
		String idno=request.getParameter("idno")==null?"":request.getParameter("idno").trim();
		String investor=request.getParameter("investor")==null?"":request.getParameter("investor").trim();
		
		Map<String,String> oInfo=new HashMap<String, String>();
		oInfo.put("serno", serno);
		oInfo.put("idno", idno);
		oInfo.put("investor", investor);
//		System.out.println(serno);
		Commit bean= commitSer.select(serno);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/approval/showocommit.jsp?serno="+idno+"';</script>");
			out.flush();
			out.close();
			return;
		}
		request.setAttribute("bean", bean);
		oInfo.put("editor", UserName.get(bean.getUpdateuser()));
		request.setAttribute("oInfo", oInfo);

		if(!bean.getType().equals("04")){
			Map<String, String> detailTotal = detailSer.selectTotalValue(serno);
			request.setAttribute("detailTotal", detailTotal);
			List<List<String>> detailEx =  detailSer.select(serno,bean.getType());
			if(detailEx!=null&&!detailEx.isEmpty()){
				request.setAttribute("detailEx", detailEx);
			}
		}
		
		String office = officeSer.selectStrName(serno);
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
		request.setAttribute("CRType", CRType);
		request.setAttribute("AoCode", AoCode);
		
		request.getRequestDispatcher("/approval/seecommitrestrain.jsp").forward(request, response);
	}
}
