<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/js/jquery.ui.ympicker-zh-TW.js'/>" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<link href="<c:url value='/css/ympicker.css'/>" rel="stylesheet"/>

<script>
$(function() {
	setYMFromTo($("input[name='MaxR']"),$("input[name='MinR']"),"${rmm.MaxR}","${rmm.MinR}");
	setYMFromTo($("input[name='MaxI']"),$("input[name='MinI']"),"${rmm.MaxI}","${rmm.MinI}");
});
</script>
<script>
$(function() {
	setDefalutOptionByClass($(".df"));
	setRedioToDefalut("decision","${rInfo.decision}");
	setSelectedToDefalut("rejectType","${rInfo.rejectType}");
	$("#clearbtn").click(function(){
		$("input[name='MaxR']").val("${ibfn:addSlash(rmm.MaxR)}".substring(0, 6));
		$("input[name='MinR']").val("${ibfn:addSlash(rmm.MinR)}".substring(0, 6));
		$("input[name='MaxI']").val("${ibfn:addSlash(rmm.MaxI)}".substring(0, 6));
		$("input[name='MinI']").val("${ibfn:addSlash(rmm.MinI)}".substring(0, 6));
		$("input[name='cName']").val("");
		$("input[name='cApplicant']").val("");
		setDefalutOptionByClass($(".df"));
	});
	 $("select[name='rejectType']").select2({
		 language: "zh-TW",
		 placeholder: "預設不拘，或點此選擇",
		 closeOnSelect: false
	  });
	 if("${rInfo.rejectType}".length>0){
			var ary="${rInfo.rejectType}".split(",");
			$("select[name='rejectType']").select2("val",ary);
	 }
});
</script>

<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;查詢條件&nbsp;]</span>
	</legend>
<div>
	<div style="float: left;width: 98%;">
		<form class="searchForm" action='<c:url value="/reject/showlist.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 5px;">
					<tr>
						<td class="tdRight" style="width: 15%;">國內事業名稱：</td>				
						<td style="width: 35%;"><input type="text" name="cName" style="width: 95%;" value="${rInfo.cName}"/></td>				
						<td class="tdRight">申請人名稱：</td>				
						<td style="width: 30%;"><input type="text" name="cApplicant" style="width: 95%;" value="${rInfo.cApplicant}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">駁回類型：</td>				
						<td colspan="3">
							<select name="rejectType" style="width: 90%;" multiple="multiple">
								<!-- <option  class='df' value="">不拘</option> -->
								<c:forEach var="newitem" items="${opt.rejectType}" varStatus="i">
									<option value="${newitem.key}">${newitem.value}</option>
								</c:forEach>
							</select>
						</td>				
					</tr>
					<tr>
						<td class="tdRight">駁回決議：</td>				
						<td colspan="3">
							<input type="radio" name="decision" id="decision" value="" class='df'><label for="decision">不拘</label>
							<c:forEach var="newitem" items="${opt.decision}" varStatus="i">
									<input type="radio" name="decision" id="decision${i.index}" value="${newitem.key}"><label for="decision${i.index}">${newitem.value}</label>
							</c:forEach>
						</td>				
					</tr>
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
						<td class="tdRight"  colspan="6" style="text-align: right;">
							<input type="hidden" name="fbtype" value="b"/>
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
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