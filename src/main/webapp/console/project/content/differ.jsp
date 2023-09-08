<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/js/hideOptionBySpan.js'/>" language="javascript" type="text/javascript"></script>
<script type="text/javascript">
<!--
$(function(){
	$(".mySent").click(function(){
		$("FORM").submit();
	});
	setYearRPRange("100");
});
function setYearRPRange(min){
	var date = new Date();
	var maxY = date.getFullYear()-1911;
	var Q = Math.ceil((date.getMonth()+1/3)-1);
	if(Q===0){
		maxY=maxY-1;
	}
	var startY = parseInt(min, 10);
	var endY = parseInt(maxY, 10);
	var sel="";
	for(var i=endY;i>=startY;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$("select[name='year']").html(sel);
}
//-->
</script>
<div id="mainContent">
<form action="<c:url value='/console/project/downloaddiffer.jsp'/>" method="post" class="myForm">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;下載差異金額&nbsp;</strong>&nbsp;</span>
		</legend>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 1&nbsp;]</span><strong style="color:#222;">&nbsp;設定查詢條件&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin: 0px auto;">
				<div class="basetable">
					<label>累計截至：</label>
					<select style="margin:2px 0px;width: 15%;" name="year">
					</select>
					<select style="margin:2px 0px;width: 15%;" name="quarter">
						<option value="1">第一季</option>
						<option value="2">第二季</option>
						<option value="3">第三季</option>
						<option value="4">年報</option>
					</select>
				</div>
				<div class="basetable">
					<label>差異項目：</label>
					<select style="margin:2px 0px;width: 15%;" name="type">
						<option value="0">不拘</option>
						<option value="1">累計核准投資金額</option>
						<option value="2">累計已核備投資金額</option>
					</select>
				</div>
			</div>
		</fieldset>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 2&nbsp;]</span><strong style="color:#222;">&nbsp;下載檔案&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
				<img src="<c:url value='/images/sub/icon_dot.jpg'/>" width="18" height="18" />進行下載：
				<img class="mySent" src="<c:url value='/images/sub/button_search.jpg'/>" width="60" height="22" style="cursor:pointer;" />
			</div>
		</fieldset>
	</fieldset>
	</form>
</div>