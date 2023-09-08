<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*"%>
<%
	InterviewoneService ser = new InterviewoneService();
	String qNo=request.getParameter("qNo");
	Interviewone bean = ser.select(null, qNo).get(0);
	request.setAttribute("bean",bean);
	InterviewoneHelp iohelper = new InterviewoneHelp();
	Map<String, String> map = iohelper.getOptionValName().get("errMsg");
	request.setAttribute("map",map);
%>
<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script>
	$(function() {
		setSelectedToDefalut("msg","${bean.msg}");
	});
</script>
<div id="myDiv" style="width:100%;">
	<form id="myForm" action='<c:url value="/console/interviewone/editerrmsg.jsp" />' method="POST">
	<table class="formProj" >
		<tr><th >特殊稽核</th></tr>
		<tr>
			<td>
				<select name='msg'>
					<c:forEach var="msg" items="${map}">
						<option value="${msg.key}">${msg.value}&nbsp;</option>
					</c:forEach>
				</select>
				<input type="hidden" name="qNo" value="${bean.qNo}">
			</td>
		</tr>
		<tr>
			<td style="text-align: center;">
				<input type="submit" value="更新"/>
			</td>
		</tr>
	</table>
	</form>
</div>

