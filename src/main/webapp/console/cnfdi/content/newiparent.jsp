<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*,com.isam.service.ofi.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>

<script>
$(function() {
	$("#nationD").select2();
	$("#cnCodeD").select2();
	$("#nationD").on("change",function(){
		if(parseInt($(this).val(),10)===142){
			$(".cnSelect").show();
		}else{
			$("#cnCodeD").select2("val","");
			$(".cnSelect").hide();
		}
	});
	if("${param.cnCodeDef}".length>0||parseInt("${param.nationDef}",10)===142){
		$(".cnSelect").show();
		$("#cnCodeD").select2("val","${param.cnCodeDef}");
	}
	if("${param.nationDef}".length>0){
		$("#nationD").select2("val","${param.nationDef}");
	}
	$("#myInsert").click(function(){
		if($.trim($("input[name='relatedname']").val()).length===0){
			alert("名稱為必填欄位");
			$("input[name='relatedname']").focus();
			return false;
		}else if($("#nationD").select2("val").length===0){
			alert("國別為必選欄位");
			return false;
		}else{
			$("#myform").submit();
		}
	});
});
</script>
<%
	OFIInvestOptionService ser= new OFIInvestOptionService();
	Map<String, Map<String, String>> map=ser.select();
	request.setAttribute("nation", map.get("nation"));
	request.setAttribute("cnCode", map.get("cnCode"));
%>
	<form id="myform" action="<c:url value='/console/updateinvestorrelated.jsp'/>" method="post">
		<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			<div class='tbtitle'>母公司或關連企業及受益人資訊</div>
			<table style="width: 98%;font-size: 16px;border-spacing: 10px;  border-collapse: separate;" class="tchange">
				<tr>
					<td style="text-align: right;">名稱：</td>
					<td colspan="3">
						<input type="text" name="relatedname" value="${param.relatedname}" style="width: 90%;font-size: 16px;">
						<input type="hidden" name="type" value="${param.type}" >
						<input type="hidden" name="serno" value="${param.serno}" >
						<input type="hidden" name="investorSeq" value="${param.investorSeq}" >
					</td>			
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;">國別：</td>				
					<td style="width: 20%;">
						<select id="nationD" name="nation" style="width: 250px;">
							<option value="">不拘</option>
							<c:forEach var="item" items="${nation}">
								<option value="${item.key}">${item.value}</option>
							</c:forEach>
						</select>
					</td>				
					<td style="text-align: right;width: 10%;">
						<div class="cnSelect" style="display: none;">省分：</div>
					</td>				
					<td>
						<div class="cnSelect" style="display: none;">
							<select id="cnCodeD" name="cnCode" style="width: 250px;">
								<option value="">不拘</option>
								<c:forEach var="item" items="${cnCode}">
									<option value="${item.key}">${item.value}</option>
								</c:forEach>
							</select>
						</div>
					</td>				
				</tr>
			</table>
			<div style="text-align: center;margin-top: 10px;">
				<input type="button" id="myInsert" class="btn_class_Approval" value="儲存"/>
			</div>					
		</div>
	</form>