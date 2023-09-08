package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.MoeaicData;
import com.isam.bean.Project;
import com.isam.bean.ProjectReport;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.service.MoeaicDataService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectReportService;
import com.isam.service.ProjectService;

public class ApprovalShowProjServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, String> IDNOToName;
	private Map<String, String> UserName;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName=help.getIDNOToName();
		UserName=help.getUserToName();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		session.removeAttribute("PRUBean");
		session.removeAttribute("PRUReceive");
		session.removeAttribute("PRUName");
		String temp = request.getParameter("repserno");
		String type = request.getParameter("type");
		
		if(temp==null){
//			response.sendError(404);
			return;
		}
		int repSerno=Integer.valueOf(temp);
		ProjectReportService prSer= new ProjectReportService();
		ProjectReport bean = prSer.selectByRepSerno(repSerno,"1");
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('本筆資料已經不存在，即將轉往專案列表頁面!');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/approval/showapproval.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		int serno=bean.getSerno();
		ProjectService proj= new ProjectService();
//		System.out.println(String.valueOf(serno));
		Project project= proj.getProjectBySerno(String.valueOf(serno));
		
		MoeaicDataService moeaic = new MoeaicDataService();
		List<MoeaicData>  relist=moeaic.selectByInvestNoIDNO(project.getInvestNo(),project.getIDNO(),repSerno);
		
		List<List<String>> receiveNOs=new ArrayList<List<String>>();
		for(MoeaicData s:relist){
			List<String> receive = new ArrayList<String>();
			receive.add(s.getRespDate());
			receive.add(s.getReceiveNo());
			receive.add(s.getAppName());
			receive.add("1");
			receiveNOs.add(receive);
		}
		
		Map<String,String> PRUName= new HashMap<String, String>();
		PRUName.put("investNo", project.getInvestNo());
		PRUName.put("IDNO", project.getIDNO());
		PRUName.put("cnName", InvestNoToName.get(project.getInvestNo()));
		PRUName.put("investor", IDNOToName.get(project.getIDNO()));
		PRUName.put("editor", UserName.get(bean.getUpdateuser()));
		
		session.setAttribute("PRUBean", bean);	
		session.setAttribute("PRUReceive", receiveNOs);	
		session.setAttribute("PRUName", PRUName);	
		
		String path = request.getContextPath();
		if(type==null){
			response.sendRedirect(path + "/approval/projectreport.jsp");
		}else{
			response.sendRedirect(path + "/console/project/seeprojectreport.jsp");
		}
	}

}
