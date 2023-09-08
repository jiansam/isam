<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.isam.bean.*" %>
<%@ page import="com.isam.dao.*" %>
<%@ page import="com.isam.helper.*" %>
<%
	Interview interview = new Interview();
	int identifier = -1;
	try{
		identifier = Integer.parseInt(request.getParameter("identifier"));
	}catch(Exception e){}
	
	if(identifier == -1){
		response.sendRedirect("listInterview.jsp");
	}

	interview.setIdentifier(identifier);
	interview.setCompany(DataUtil.trim(request.getParameter("company")));
	interview.setCompanyEnglish(DataUtil.trim(request.getParameter("companyEnglish")));
	if(Boolean.parseBoolean(request.getParameter("noParentcompany"))){
		interview.setParentCompany("");
	}else{
		interview.setParentCompany(DataUtil.trim(request.getParameter("parentCompany")));
	}
	
	interview.setInterviewee(DataUtil.trim(request.getParameter("interviewee")));
	interview.setInterviewer(DataUtil.trim(request.getParameter("interviewer")));
	interview.setPublicity(Boolean.parseBoolean(request.getParameter("publicity")));
	interview.setInterviewPlace(DataUtil.trim(request.getParameter("interviewPlace")));
	interview.setNote(DataUtil.trim(request.getParameter("note")));
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	Date interviewDate = sdf.parse(DataUtil.trim(request.getParameter("interviewDate")));
	Calendar cal = Calendar.getInstance();
	cal.setTime(interviewDate);
	cal.add(Calendar.YEAR, 1911);
	interview.setInterviewDate(cal.getTime());
	
	InterviewDAO.update(interview);
	
	//Update InterviewXRegion
	String[] RegionCode = request.getParameterValues("Region") == null ? new String[0] : request.getParameterValues("Region");
	InterviewXRegionDAO.update(identifier, RegionCode);
	
	//Update InterviewXIndustry
	String[] IndustryCode = request.getParameterValues("Industry") == null ? new String[0] : request.getParameterValues("Industry");
	InterviewXIndustryDAO.update(identifier, IndustryCode);
	
	//Update InterviewXInvestment
	String[] InvestmentType = request.getParameterValues("InvestmentType") == null ? new String[0] : request.getParameterValues("InvestmentType");
	InterviewXInvestmentDAO.update(identifier, InvestmentType);
	
	//Update Interview Content Table
	PairHashtable<Integer, String, String> interviewContentTable = new PairHashtable<Integer, String, String>();
	Hashtable<String, InterviewOutline> OutlineTable = (Hashtable<String, InterviewOutline>)session.getAttribute("InterviewcierOutlineTable");
	for(String outlineCode : OutlineTable.keySet()){
		if(request.getParameter(outlineCode) != null){
			interviewContentTable.put(identifier, outlineCode, request.getParameter(outlineCode));
		}
	}
	
	InterviewContentDAO.update(interviewContentTable);
	
	response.sendRedirect("modifyInterview.jsp?identifier=" + identifier);
%>