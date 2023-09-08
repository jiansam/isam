<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/js/jquery.ui.ympicker-zh-TW.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/css/ympicker.css'/>" rel="stylesheet"/>
<script>
$(function() {
	setYMFromTo($("input[name='MaxR']"),$("input[name='MinR']"),"${rmm.MaxR}","${rmm.MinR}");
	setYMFromTo($("input[name='MaxI']"),$("input[name='MinI']"),"${rmm.MaxI}","${rmm.MinI}");
});
</script>
<script>
$(function() {
	if("${rInfo.rejectType}".length>0){
		setCheckboxToDefalut("rejectType","${rInfo.rejectType}");
	}else{
		$("input[name='rejectType']").prop("checked",true);
	}
	$("#clearbtn").click(function(){
		$("input[name='MaxR']").val("${ibfn:addSlash(rmm.MaxR)}".substring(0, 6));
		$("input[name='MinR']").val("${ibfn:addSlash(rmm.MinR)}".substring(0, 6));
		$("input[name='MaxI']").val("${ibfn:addSlash(rmm.MaxI)}".substring(0, 6));
		$("input[name='MinI']").val("${ibfn:addSlash(rmm.MinI)}".substring(0, 6));
		$("input[name='rejectType']").prop("checked",true);
	});
	$("#mySearch").click(function(){
		if(checkTextBox()){
			if($("input[name='rejectType']:checked").length===0){
				alert("請至少選擇一個駁回類型");
			}else{
				$("#searchForm").submit();
			}
		}
	});
	$("#doloadExcel").click(function(){
		downloadRejects();
	});
});
function downloadRejects(){
	var rType="";
	$("input[name='rejectType']:checked").each(function(){
		if(rType.length>0){
			 rType+=","
		}
		 rType+=$(this).val();
	});
	var tmp=0;
	$("input[type='text']").each(function(){
		if($.trim($(this).val()).length==0){
			alert("本欄不可為空值");
			$(this).focus();
			tmp=1;
			return false;
		};
	});
	if(tmp===0){
		postUrlByForm('/downloadsummary.jsp',{
			'rType':rType,
			"MinR":$("input[name='MinR']").val(),
			"MaxR":$("input[name='MaxR']").val(),
			"MinI":$("input[name='MinI']").val(),
			"MaxI":$("input[name='MaxI']").val()
		});
	}
}
function checkTextBox(){
	var x=true;
	$("input[type='text']").each(function(){
		if($.trim($(this).val()).length==0){
			alert("年月為必填欄位，請選擇。")
			$(this).focus();
			x=false;
			return false;
		}
	});
	return x;
}
</script>

<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;查詢條件&nbsp;]</span>
	</legend>
<div>
	<div style="float: left;width: 98%;">
		<form id="searchForm" action='<c:url value="/reject/showsummary.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 5px;">
					<tr>
						<td class="tdRight">申請年月：</td>				
						<td colspan="3">
							<input style="width: 15%;" type="text" name="MinR" value="${rInfo.MinR}">~<input type="text" name="MaxR"  style="width: 15%;" value="${rInfo.MaxR}">
						</td>				
					</tr>
					<tr>
						<td class="tdRight">發文年月：</td>				
						<td colspan="3">
							<input  style="width: 15%;" type="text" name="MinI" value="${rInfo.MinI}">~<input type="text" name="MaxI"  style="width: 15%;" value="${rInfo.MaxI}">
						</td>				
					</tr>
					<tr>
						<td class="tdRight">駁回類型：</td>				
						<td colspan="3">
							<c:forEach var="newitem" items="${opt.rejectType}" varStatus="i">
								<input type="checkbox" name="rejectType" value="${newitem.key}" id="rejectType${i.index}"><label for="rejectType${i.index}">${newitem.value}</label><br>
							</c:forEach>
						</td>				
					</tr>
					<tr>
						<td class="tdRight"  colspan="6" style="text-align: right;">
							<input type="hidden" name="fbtype" value="b"/>
							<input type="button" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input type="button" id="doloadExcel" class="btn_class_opener" value="直接下載"/>
							<input type="button" id="clearbtn" class="btn_class_opener" value="重設"/>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
	<div style="clear: both;"></div>	
</div>
</fieldset>