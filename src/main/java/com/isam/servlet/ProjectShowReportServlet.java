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
import com.isam.helper.DataUtil;
import com.isam.service.MoeaicDataService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectReportService;
import com.isam.service.ProjectService;
import com.isam.service.ProjectXReciveNoService;

public class ProjectShowReportServlet extends HttpServlet {
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
		session.removeAttribute("PRUList");
		String temp = request.getParameter("repserno");
		
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
			out.print("<script language='javascript'>alert('本筆資料已經不存在，即將轉往專案列表頁面!');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/project/showproject.jsp';</script>");
			out.flush();
			out.close();
			return;
		}else if(bean.getEnable().equals("0")){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('資料已經不存在，即將轉往修改頁面!');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/project/showprojectdetail.jsp?serno="+bean.getSerno()+"';</script>");
			out.flush();
			out.close();
			return;
		}
		int serno=bean.getSerno();
		ProjectService proj= new ProjectService();
//		System.out.println(String.valueOf(serno));
		Project project= proj.getProjectBySerno(String.valueOf(serno));
		
		/*取得全部的文號包含沒有被選取的*/
		MoeaicDataService moeaic = new MoeaicDataService();
		List<MoeaicData> relist = moeaic.selectByInvestNoIDNO(project.getInvestNo(),project.getIDNO());
		/*取得被選取的文號*/
		ProjectXReciveNoService pXRNo=new ProjectXReciveNoService();
		List<String> checkList=pXRNo.select(repSerno);
		
		List<List<String>> receiveNOs=new ArrayList<List<String>>();
		for(MoeaicData s:relist){
			List<String> receive = new ArrayList<String>();
			receive.add(s.getRespDate());
			receive.add(s.getReceiveNo());
			receive.add(s.getAppName());
			if(checkList.contains(s.getReceiveNo())){
				receive.add("1");
			}else{
				receive.add("0");
			}
			receiveNOs.add(receive);
		}
		Map<String,String> PRUName= new HashMap<String, String>();
		PRUName.put("investNo", project.getInvestNo());
		PRUName.put("IDNO", project.getIDNO());
		PRUName.put("cnName", InvestNoToName.get(project.getInvestNo()));
		PRUName.put("investor", IDNOToName.get(project.getIDNO()));
		PRUName.put("editor", UserName.get(bean.getUpdateuser()));
		StringBuilder sb = new StringBuilder();
		String respdate="";
		sb.append(DataUtil.addZeroForNum(bean.getYear(), 3));
		String quarter=bean.getQuarter();
		if(quarter.equals("4")){
			sb.append("1231");
		}else if(quarter.equals("3")){
			sb.append("0930");
		}else if(quarter.equals("2")){
			sb.append("0630");
		}else if(quarter.equals("1")){
			sb.append("0331");
		}
		respdate=sb.toString();
		sb.setLength(0);
		List<Map<String,String>> PRUList=prSer.getDifferList(bean.getYear(),quarter, String.valueOf(serno),respdate);
		
		session.setAttribute("PRUBean", bean);
		if(!PRUList.isEmpty()){
			session.setAttribute("PRUList", PRUList.get(0));	
		}
		session.setAttribute("PRUReceive", receiveNOs);	
		session.setAttribute("PRUName", PRUName);	
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/project/projectreport.jsp");
	}

}
