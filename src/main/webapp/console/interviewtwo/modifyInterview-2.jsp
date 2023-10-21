<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.isam.bean.*" %>
<%@ page import="com.isam.dao.*" %>
<%@ page import="com.isam.helper.*" %>
<%
	InterviewBrief interviewbrief = new InterviewBrief();
	int identifier = -1;
	try{
		identifier = Integer.parseInt(request.getParameter("identifier"));
	}catch(Exception e){}
	
	if(identifier == -1){
		response.sendRedirect("listInterview.jsp");
	}

	interviewbrief.setIdentifier(identifier);

	interviewbrief.setYear(Integer.parseInt(request.getParameter("year")));
	interviewbrief.setCompany(DataUtil.trim(request.getParameter("company")));
	interviewbrief.setDescription(DataUtil.trim(request.getParameter("description")));
	
	UserMember bean = (UserMember) session.getAttribute("userInfo");
	interviewbrief.setUpdateuser(bean.getUsername());
	
	InterviewBriefDAO.update(interviewbrief);
	
	response.sendRedirect("modifyInterview.jsp?identifier=" + identifier);
%>