<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.isam.bean.*" %>
<%@ page import="com.isam.dao.*" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewtwo" %>
<%
	Hashtable<String, String> parameters = InterviewFileDAO.upload(request);
	String identifier = parameters.get("identifier");
%>
<itag:listInterviewFile identifier="<%=  identifier %>" purpose="moea" />