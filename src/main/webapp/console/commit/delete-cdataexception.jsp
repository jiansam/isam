<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.dao.CDataExceptionDAO" %>
<%@ page import="com.isam.helper.ApplicationAttributeHelper" %>
<%
	boolean success = CDataExceptionDAO.delete(request.getParameter("InvestNo"), Integer.parseInt(request.getParameter("serno")));

	if(success){
		ApplicationAttributeHelper.getInvestNoToName(application).remove(request.getParameter("InvestNo"));
	}
%>