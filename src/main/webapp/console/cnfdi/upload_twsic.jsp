<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.isam.dao.ofi.*" %>
<%
	OFI_TWSICDAO.upload(request);
	request.getRequestDispatcher("twsic.jsp").forward(request, response);
%>

