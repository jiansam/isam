<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script type="text/javascript">
$(function(){
	$("#myInsert").click(function(){
		var x=$.trim($("input[name='nscore']").val());
		if(!$.isNumeric(x) && x.length!==0){
			alert("調整後等級應為數值，請填寫數值！");
			$("input[name='nscore']").focus();
			return false;
		}else{
			$("#myform").submit();
		}
	});
});
</script>

<div>
	<form id="myform" action="<c:url value='/console/cnfdi/updatemscore.jsp'/>" method="post">
		<table class="formProj">
			<tr>
				<td class="trRight">年度：</td>
				<td>${param.year}</td>
			</tr>	
			<tr>
				<td class="trRight">系統評分總分：</td>
				<td>${param.score}</td>
			</tr>	
			<tr>
				<td class="trRight">系統管理等級：</td>
				<td>${param.oscore}</td>
			</tr>	
			<tr>
				<td class="trRight">調整後等級：</td>
				<td><input type="text" value="${param.nscore}" name="nscore"></td>
			</tr>	
		</table>
		<input type="hidden" value="${param.investNo}" name="investNo">
		<input type="hidden" value="${param.year}" name="year">
		<div style="text-align: center;margin-top: 10px;">
			<input type="button" id="myInsert" class="btn_class_Approval" value="儲存"/>
		</div>
	</form>
</div>
