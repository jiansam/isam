<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%
	InterviewoneService ser = new InterviewoneService();
	Map<String,String> list=ser.getReInvestNoByYearInvestNo(request.getParameter("year"),request.getParameter("investNo"));
	request.setAttribute("xlist",list);
%>
<script type="text/javascript">
$(function(){
	if("${fn:length(xlist)}"==0){
		alert("查無符合條件轉投資事業，請重新確認年度及案號。");
		$("input[name='company'][value='0']").prop("checked",true);
	}
});
</script>
<c:if test="${not empty xlist}">
	<select style="width: 250px;" name="reinvestNo">
		<c:forEach var="sub" items="${xlist}">
			<option value="${sub.key}">${sub.value}</option>
		</c:forEach>
	</select>
</c:if>	

