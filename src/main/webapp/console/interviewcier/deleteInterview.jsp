<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.bean.*" %>
<%@ page import="com.isam.dao.*" %>
<%@ page import="com.isam.helper.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<%
	int identifier = -1;
	try{
		identifier = Integer.parseInt(request.getParameter("identifier"));
	}catch(Exception e){}
	
	if(identifier > 0){
		InterviewCierDAO.delete(identifier);
	}
	//response.sendRedirect("listInterview.jsp");
%>
<script type="text/javascript">
alert("完成刪除，返回訪查總表。");
window.location.href = "listInterview.jsp";
</script>
