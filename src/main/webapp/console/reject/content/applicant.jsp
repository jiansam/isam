<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.isam.helper.*,com.isam.ofi.reject.service.*,com.isam.ofi.reject.bean.*,java.util.*"%>

<%
	session.removeAttribute("APL");
	session.removeAttribute("APLAgents");
	OFIRejectXApplicantService ser = new OFIRejectXApplicantService();
	String applyNo=DataUtil.nulltoempty(request.getParameter("applyNo"));
	OFIRejectXAgentService agser = new OFIRejectXAgentService();
	if(!applyNo.isEmpty()){
		OFIRejectXApplicant bean=ser.select(applyNo);
		session.setAttribute("APL", bean);
		List<OFIRejectXAgent> agents=agser.select(applyNo);
		session.setAttribute("APLAgents", agents);
	}
%>


<script>
$(function() {
	$("input[name='postion']:first").prop("checked",true);
	$("#myAdd").click(function(){
		addApplicant();
	});
		removeApplicant();
});
function addApplicant(){
	 var aStr=$.trim($("input[name='agenttmp']").val());
	 if(aStr.length===0){
		 alert("代理人不可為空值");
		 $("input[name='agenttmp']").focus();
	 }else{
		 var str1="<tr><td class='trRight'>代理人：</td><td>";
		 if($("#myDT").find("tr").length==6){
			 str1="<tr><th colspan='5'>代理人列表</th></tr>"+str1;
		 }
		 var str2="</td><td>職業：</td><td>";
		 var str3=$("input[name='postion']:checked").next("label").text()+"</td><td>";
		 var str4="<input type='hidden' value='"+aStr+"' name='agent'>";
		 var str5="<input type='hidden' value='"+$("input[name='postion']:checked").val()+"' name='pos'><input type='button' value='移除' class='mydel'></td></tr>";
		 $("#myDT").append(str1+aStr+str2+str3+str4+str5);
		 $("input[name='agenttmp']").val("");
	 }
	removeApplicant();
}
function removeApplicant(){
	$(".mydel").click(function(){
		if($("#myDT").find("tr").length==8){
			$(this).parents("tr").prev().remove();
		}
		$(this).parents("tr").remove();
	});
}
</script>
<style>
.formProj a:link{
text-decoration: none;
}
</style>
<div>
	<form id="DTForm">
	<table style="width: 98%;font-size: 16px;" class="formProj" id="myDT">
		<tr>
			<th colspan="5">申請人資料</th>
		</tr>
		<tr>
			<td class="trRight"><span style="color: red;">*</span>申請人姓名/公司名稱：</td>
   			<td colspan="4"><input type="text" name="cApplicant" value="${APL.cApplicant}" style="width: 85%;"></td>
		</tr>
		<tr>
			<td class="trRight">申請人英文名稱：</td>
   			<td colspan="4"><input type="text" name="eApplicant" value="${APL.eApplicant}" style="width: 85%;"></td>
		</tr>
		<tr>
			<td class="trRight">申請人國別：</td>
   			<td colspan="4">
   				<jsp:include page="/includes/countryoption.jsp" flush="true">
					<jsp:param value="${APL.nation}" name="nationDef"/>
					<jsp:param value="${APL.cnCode}" name="cnCodeDef"/>
				</jsp:include>
   			</td>
		</tr>
		<tr>
			<td class="trRight">申請人備註：</td>
   			<td colspan="4">
   				<textarea name="note" rows="2" style="width: 95%;">${APL.note}</textarea>
   			</td>
		</tr>
		<tr>
			<th colspan="5">新增代理人資料</th>
		</tr>
		<tr id="tmp">
			<td class="trRight">代理人：</td>
   			<td><input type="text" name="agenttmp" value=""></td>
			<td class="trRight">職業：</td>
   			<td>
   				<c:forEach var="newitem" items="${opt.postion}" varStatus="i">
					<input type="radio" name="postion" id="postion${i.index}" value="${newitem.key}"><label for="postion${i.index}">${newitem.value}</label>
				</c:forEach>
   			</td>
   			<td><input type="button" value="新增" id="myAdd"></td>
		</tr>
		<c:if test="${not empty APLAgents}">
			<tr><th colspan='5'>代理人列表</th></tr>
		</c:if>
		<c:forEach var="item" items="${APLAgents}">
			<tr>
				<td class='trRight'>代理人：</td>
				<td>${item.agent}</td>
				<td>職業：</td>
				<td>${opt.postion[item.postion]}</td>
				<td>
					<input type='hidden' value='${item.agent}' name='agent'>
					<input type='hidden' value='${item.postion}' name='pos'>
					<input type='button' value='移除' class='mydel'>
				</td>
			</tr>
		</c:forEach>
	</table>
	<input type="hidden" name="cNo" value="${param.cNo}" style="width: 85%;">
	<input type="hidden" name="serno" value="${param.serno}" style="width: 85%;">
	<input type="hidden" name="applyNo" value="${param.applyNo}" style="width: 85%;">
	</form>
</div>

