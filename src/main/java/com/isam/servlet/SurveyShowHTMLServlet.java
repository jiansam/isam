package com.isam.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.service.SurveyService;

public class SurveyShowHTMLServlet extends HttpServlet {
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
		Map<String, String> qTypeMap= (Map<String, String>) session.getAttribute("qTypeName");
		Map<String, List<String>> qTypeYear= ( Map<String, List<String>>) session.getAttribute("qTypeYear");
		String qType = request.getParameter("qType");
		String year = request.getParameter("year");
		String terms = request.getParameter("terms");
		String[] topics = request.getParameterValues("topic");
		String[] qNos = request.getParameterValues("qNo");
//		System.out.println("qType:"+qType);
//		System.out.println("year:"+year);

		String qNo="";
		String topic="";
		
		if(qType==null||!qTypeMap.containsKey(qType)){
			errors.put("qTypeErr", "問卷類型錯誤");
		}
		if(year==null||!qTypeYear.get(qType).contains(year)){
			errors.put("qYearErr", "調查年度錯誤");
		}
		if(qNos==null){
			errors.put("qNosErr", "請選擇欲進行比較的企業");
		}else if(qNos.length>80){
			errors.put("qNosErr", "比較企業不可超過80筆");
		}
		SurveyService ser = new SurveyService();
		if(topics!=null&&topics.length!=0){
			topic=ser.getStrItem(topics,qType);
		}
		if(!errors.isEmpty()){
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("/survey/surveyshowitem.jsp").forward(request, response);
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		for(String s:qNos){
			if(sb.length()==0){
				sb.append(s);
			}else{
				sb.append(",");
				sb.append(s);
			}
		}
		qNo=sb.toString();
		sb.setLength(0);
		
		
		 
		List<List< String>>  list = ser.getSurveyHtmlResult(qType, year, topic, qNo);
		
		session.removeAttribute("htmlList");
		session.removeAttribute("surveyterms");
		session.removeAttribute("surveyYear");
		session.removeAttribute("surveyqType");
		
		session.setAttribute("htmlList", list);
		session.setAttribute("surveyterms", terms);
		session.setAttribute("surveyYear", year);
		session.setAttribute("surveyqType", qType);
		String path = request.getContextPath();
		response.sendRedirect(path + "/survey/surveyresult.jsp");
	}
}
