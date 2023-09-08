<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<link href="<c:url value='/css/select2fix.css'/>" rel="stylesheet"/>

<script>
$(function() {
	$("#pname").select2();
	if("${filledterms.pname}".length>0){
		$("#pname").select2("val","${filledterms.pname}");
	}
	$("#clearbtn").click(function(){
		$("#pname").select2("val","");
		$("form :input[type='text']").val("");
	});
});
</script>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;查詢條件&nbsp;]</span>
	</legend>
<div>
	<div style="float: left;width: 100%;">
		<form class="searchForm" action='<c:url value="/console/listnotfilled.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td class="tdRight">承辦人：</td>				
						<td>
						<select name="pname" id="pname" style="width: 100%;">
							<option value="">不拘</option>
							<c:forEach var="item" items="${filledpnames}">
								<option value="${item}">${item}</option>
							</c:forEach>
						</select>
						</td>				
						<td class="tdRight">文號：</td>				
						<td><input type="text" name="receiveNo" maxlength="11"  value="${filledterms.receiveNo}"/></td>				
					</tr>
					<tr>
						<td class="tdRight"  colspan="4" style="text-align: right;">
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input id="clearbtn" type="button" class="btn_class_opener" value="清空"/>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
	<div style="clear: both;"></div>	
</div>
</fieldset>