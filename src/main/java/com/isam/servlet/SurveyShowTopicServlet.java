package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.service.SurveyService;

public class SurveyShowTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Map<String, String> errors=new HashMap<String, String>();
		session.removeAttribute("qNoList");
		session.removeAttribute("surveyterms");
		session.removeAttribute("surveyYear");
		session.removeAttribute("surveyqType");

		Map<String, String> qTypeMap= (Map<String, String>) session.getAttribute("qTypeName");
		Map<String, List<String>> qTypeYear= ( Map<String, List<String>>) session.getAttribute("qTypeYear");
		String qType = request.getParameter("qType");
		String year = request.getParameter("year");
		String[] inds = request.getParameterValues("ind");
		String[] regions = request.getParameterValues("region");
		String terms = request.getParameter("terms");
		String ind="";
		String region="";
		
		if(qType==null||!qTypeMap.containsKey(qType)){
			errors.put("qTypeErr", "問卷類型錯誤");
		}
		if(year==null||!qTypeYear.get(qType).contains(year)){
			errors.put("qYearErr", "調查年度錯誤");
		}
		
		SurveyService ser = new SurveyService();
		if(inds!=null&&inds.length!=0){
			ind=ser.getStrItem(inds,"Industry");
		}
		if(regions!=null&&regions.length!=0){
			region=ser.getStrItem(regions,"distraction");
		}
		if(!errors.isEmpty()){
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("/survey/surveyterms.jsp").forward(request, response);
		}
		List<Map<String,String>> list = ser.getQNoResult(qType, year, ind, region);
		
		if(list.size()==0){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('查無符合條件廠商，請重新選取條件!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/survey/surveyterms.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		session.setAttribute("qNoList", list);
		session.setAttribute("surveyterms", terms);
		session.setAttribute("surveyYear", year);
		session.setAttribute("surveyqType", qType);
		String path = request.getContextPath();
		response.sendRedirect(path + "/survey/surveyshowitem.jsp");
	}
}
