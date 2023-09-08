<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="java.util.*,com.isam.service.*"%>
<script type="text/javascript">
$(function(){
	$("#myInsert").click(function(){
		if($.trim($("input[name='title']").val()).length==0){
			$("input[name='title']").focus();
			alert("標題為必填項目。");
		}else if($.trim($("input[name='addfile']").val()).length==0&&"${param.type}"==='add'){
			$("input[name='addfile']").focus();
			alert("請選擇檔案");
		}else{
			$("#myform").submit();
		}
	});
	if("${param.type}"  ==='add'){
		$("input[name='addfile']").change(function(){
			var str=$(this).val();
			if(str.length>0){
				var type=str.split(/(\\|\/)/g).pop();
				str=type.substring(0,type.lastIndexOf("."));
				$("input[name='title']").val(str);
			}
		});
	}
});
</script>

<div>
	<form id="myform" action="<c:url value='/console/cnfdi/uploadStructural.jsp'/>" method="post" enctype="multipart/form-data">
		<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
		<div class='tbtitle'>母公司或關連企業及受益人架構圖</div>
		<table style="width: 98%;font-size: 16px;border-spacing: 10px;  border-collapse: separate;" class="tchange">
			<tr>
				
				<td class="trRight"><c:if test="${param.type =='add'}">上傳檔案：</c:if><c:if test="${param.type =='edit'}">檔案變更：</c:if></td>
				<td>
					<input type="file" name="addfile" style="width: 85%;">
				</td>
			</tr>
			<tr>
				<td style="width: 20%;" class="trRight">標題名稱：</td>
				<td><input type="text" name="title"  style="width: 85%;" value="${param.title}"/></td>
			</tr>	
			<!-- <tr>
				<td class="trRight">備註：</td>
				<td><textarea name="note" style="width: 85%;" rows="10"></textarea></td>
			</tr> -->
			<tr>
				<td colspan="2" style="text-align: center;"><input type="button" id="myInsert" class="btn_class_Approval" value="送出"/></td>
			</tr>
		</table>
		<input type="hidden" name="fNo" value="${param.serno}">
		<input type="hidden" name="type" value="${param.type}">
		<input type="hidden" name="investorSeq" value="${param.investorSeq}">
		</div>
	</form>
	<div id="fileTable">
	</div>
</div>
