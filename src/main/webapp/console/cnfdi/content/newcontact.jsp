<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/telfmthelper.js'/>"></script>
<script type="text/javascript">
$(function(){
	/*設定enter 下一個text*/
	inputTextNext("#myform",".nextInput",".skip");
	$("#myInsert").click(function(){
		if($("input[name='name']").val().length==0){
			alert("聯絡人名稱為必填欄位");
			$("input[name='name']").focus();
			return false;
		}else if(telFmtToText($(".telNo"),$("input[name='telNo']"))===false&&$("input[name='telNo']").val().length>0){
			return false;
		}else{
			$("form").submit();
		}
	});
});
</script>

<div>
	<form id="myform" action="<c:url value='/console/cnfdi/updateinvestcontact.jsp'/>" method="post">
		<table class="formProj">
			<tr>
				<td style="width: 20%;" class="trRight">聯絡人：</td>
				<td><input type="text" value="${param.name}" name="name" /></td>
			</tr>	
			<tr>
				<td class="trRight">聯絡電話：</td>
				<td >
					<c:set var="tel" value="${fn:split(param.telNo,'-')}"></c:set>
					<c:set var="telLast" value="${tel[2]}"></c:set>
					<c:if test="${fn:indexOf(tel[2],'#')!=-1}">
						<c:set var="telLast" value="${fn:substringBefore(tel[2],'#')}"></c:set>
						<c:set var="telEx" value="${fn:substringAfter(tel[2],'#')}"></c:set>
					</c:if>
					<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${tel[0]}"/><label>-</label>
					<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${tel[1]}"/><label>-</label>
					<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${telLast}"/><label>&nbsp;分機&nbsp;</label>
					<input type="text" style="width: 50px;" class="telNo optionalText" value="${telEx}"/>
					<input type="hidden" value="" name="telNo" />
				</td>
			</tr>	
		</table>
		<input type="hidden" value="${param.investNo}" name="investNo">
		<input type="hidden" value="${param.serno}" name="serno">
		<input type="hidden" value="${param.type}" name="type">
		<div style="text-align: center;margin-top: 10px;">
			<input type="button" id="myInsert" class="btn_class_Approval" value="儲存"/>
		</div>
	</form>
</div>
