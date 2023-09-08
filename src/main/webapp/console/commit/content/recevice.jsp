<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/restrain.js'/>" type="text/javascript"></script>
<style>
.ui-datepicker{
	font-size: 14px;
}
.ui-datepicker-title{
	white-space: nowrap;
}
</style>
<script>
$(function() {
	if("${param.editType}"==="add"){
		getRestrainTables("${param.idno}","${param.type}","${param.editType}");
	}
});
</script>

<div id="tabs-2">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step2&nbsp;]</span>
				<strong style="color:#222;">選擇涉及文號</strong>
			</legend>
			<img class="myWait" src="<c:url value='/images/loading.gif' />" title="please wait" style="width: 50px;"/>
		<div class="help">
			<table id="receiveTable" style="width: 95%;margin-left: 10px;"> 
				<thead>
					<tr>
						<th>選擇</th>
						<th>核准日期</th>
						<th>文號</th>
						<th>案由</th>
					</tr>
				</thead>
				<tbody id="SetReceive">
				</tbody>
			</table>
			<div id="formAdd" style="margin-top: 30px;">
				<table class="formProj" style="width:95%;font-size: 16px;">
					<tr>
						<th>核准日期</th>
						<td><input type="text" name="ARespDate" maxlength="7" style="width: 95%;font-size: 14px;" class="nextFocus"/></td>
						<th>文號</th>
						<td><input type="text" name="AReceviceNo" style="width: 95%;" maxlength="11" class="nextFocus"/></td>
						<th>案由</th>
						<td><input type="text" name="AAppName" style="width: 95%;" class="nextFocus"/></td>
						<td><input type="button" id="addRecevice" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="自行新增" /></td>
					</tr>
				</table>
			</div>
			<div id="tempRecevice" style="display: none;"></div>
		</div>
	</fieldset>
		<div style="text-align: center;margin: 10px 0px 5px 0px;">
			<input type="button" class="btn_class_opener prevStep" style="color: #777777;text-align: center;font-size: 12px;" value="上一步" />
			<input type="button" class="btn_class_opener lastNumberFmt nextStep" style="color: #777777;text-align: center;font-size: 12px;" value="下一步" />
		</div>
</div>
<div id="tabs-3">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;Step3&nbsp;]</span>
			<strong style="color:#222;">選擇涉及大陸投資事業</strong>
		</legend>
		<img class="myWait" src="<c:url value='/images/loading.gif' />" title="please wait" style="width: 50px;"/>
		<div class="help">
			<table id="CNTable" style="width: 95%;margin-left: 15px;"> 
				<thead>
					<tr>
						<th>選擇</th>
						<th>案號</th>
						<th>大陸事業名稱</th>
					</tr>
				</thead>
				<tbody id="SetCNName">
				</tbody>
			</table>
			<div id="formAddCN" style="margin-top: 30px;">
				<table class="formProj" style="width:95%;font-size: 16px;">
					<tr>
						<th style="width: 10%;">案號</th>
						<td style="width: 15%;"><input type="text" name="AInvestNo" maxlength="6" style="width: 95%;font-size: 14px;" class="nextFocus" title="請輸入六碼案號"/></td>
						<td><input type="button" id="addCN" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="自行新增" /></td>
					</tr>
				</table>
			</div>
		</div>
	</fieldset>
	<div style="text-align: center;margin: 10px 0px 5px 0px;">
		<input type="button" class="btn_class_opener prevStep" style="color: #777777;text-align: center;font-size: 12px;" value="上一步" />
		<input type="button" id="myInsert" class="btn_class_Approval" style="font-size: 13px;" value="確認${param.typeEdit}"/>
	</div>
	
	<div id="addCN-dialog" title="新增對外案號" style="font-size: 15px;">
		<table class="formProj" style="width:95%;font-size: 16px;">
			<tr>
				<th style="width: 10%;">案號</th>
				<td style="width: 15%;"><input type="text" name="ExInvestNo" id="ExInvestNo" maxlength="6" style="width: 95%;font-size: 14px;" title="請輸入六碼案號"/></td>
			</tr>
			<tr>
				<th style="width: 10%;">大陸事業名稱</th>
				<td style="width: 15%;"><input type="text" name="ExcnName" id="ExcnName" style="width: 95%;font-size: 14px;" autofocus="autofocus" /></td>
			</tr>
			<tr>
			<td colspan="2" align="center">
				<input type="button" id="ExaddCN" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="自行新增" 
					onclick="addExCNNameToTable()" />
				<input type="hidden" name="Exidno" id="Exidno" value="${info.idno}" />
				<input type="hidden" name="Exserno" id="Exserno" value="${info.serno}" />
			</td>
			</tr>
		</table>
	</div>
</div>
