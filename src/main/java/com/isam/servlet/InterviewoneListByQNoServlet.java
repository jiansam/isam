package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Interviewone;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneContentService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneManageService;
import com.isam.service.InterviewoneService;
import com.isam.service.MoeaicDataService;
import com.isam.service.ofi.OFIInvestNoXAuditService;

public class InterviewoneListByQNoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MoeaicDataService MSer;
	private InterviewoneService ioSer;
	private InterviewoneContentService iocSer;
	private InterviewoneManageService imSer;
	private OFIInvestNoXAuditService audSer;
	private InterviewoneHelp help;
	private Map<String, Map<String, String>> ioOpt;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		MSer = new MoeaicDataService();
		ioSer = new InterviewoneService();
		iocSer = new InterviewoneContentService();
		imSer = new InterviewoneManageService();
		audSer = new OFIInvestNoXAuditService();
		help = new InterviewoneHelp();
		ioOpt=help.getOptionValName();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("IOBaseInfo");
		session.removeAttribute("singlelist");
		session.removeAttribute("errorIlist");
		session.removeAttribute("errorFlist");
		session.removeAttribute("spNeed");
		session.removeAttribute("ioOpt");
		session.removeAttribute("fsMap");
		
		String qNo=DataUtil.nulltoempty(request.getParameter("qNo"));
		//String reInvestNo=DataUtil.nulltoempty(request.getParameter("reInvestNo"));
		Interviewone tmp=ioSer.selectByQNo(qNo);
		if(qNo.isEmpty()||tmp==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('尚無詳細資料，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/interviewone/showiolist.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		Map<String,String> baseinfo;
		String reInvestNo=tmp.getReInvestNo();
//		System.out.println(reInvestNo);
//		System.out.println(tmp);
		if(reInvestNo.equals("0")){
			baseinfo=MSer.getCNSysBaseInfo(null, tmp.getInvestNo());
			session.setAttribute("spNeed", audSer.getSPNeed(tmp.getInvestNo()));
		}else{
			baseinfo=ioSer.getReInvestNoBaseInfo("", reInvestNo);
		}
		session.setAttribute("IOBaseInfo", baseinfo);
		session.setAttribute("singlelist", ioSer.selectByQNo(qNo,reInvestNo));
		session.setAttribute("errorIlist", iocSer.getInterviewErrorList());
		session.setAttribute("errorFlist", iocSer.getFinancialErrorList());
		session.setAttribute("fsMap", imSer.getFollowingMap(qNo,reInvestNo));
		session.setAttribute("ioOpt", ioOpt);
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/interviewone/singlelist.jsp");		
	}
}
