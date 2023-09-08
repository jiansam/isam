<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.isam.helper.*,com.isam.ofi.reject.service.*,com.isam.ofi.reject.bean.*,java.util.*"%>
<%
	OFIRejectXApplicantService ser = new OFIRejectXApplicantService();
	String applyNo=DataUtil.nulltoempty(request.getParameter("applyNo"));
	ser.delete(applyNo);
	OFIRejectXAgentService agser = new OFIRejectXAgentService();
	agser.delete(applyNo);
%>
