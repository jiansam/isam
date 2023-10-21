<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.bean.*" %>
<%@ page import="com.isam.dao.*" %>
<%@ page import="com.isam.helper.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%
	InterviewBrief interviewbrief = new InterviewBrief();
 
	interviewbrief.setYear(Integer.parseInt(request.getParameter("year")));
 
	interviewbrief.setCompany(DataUtil.trim(request.getParameter("company")));
	interviewbrief.setDescription(DataUtil.trim(request.getParameter("description")));
	
	UserMember bean = (UserMember) session.getAttribute("userInfo");
	interviewbrief.setUpdateuser(bean.getUsername());
	
	int identifier = InterviewBriefDAO.insert(interviewbrief);
	
	if(identifier != -1){
		response.sendRedirect("modifyInterview.jsp?identifier=" + identifier);
	}
%>
