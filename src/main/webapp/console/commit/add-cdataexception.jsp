<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.dao.CDataExceptionDAO" %>
<%@ page import="com.isam.helper.ApplicationAttributeHelper" %>
<%
	boolean success = CDataExceptionDAO.insert(request.getParameter("InvestNo"), request.getParameter("CNName"), request.getParameter("IdNo"),
		Integer.parseInt(request.getParameter("serno")));

	if(success){
		ApplicationAttributeHelper.getInvestNoToName(application).put(request.getParameter("InvestNo"), request.getParameter("CNName"));
	}
%>