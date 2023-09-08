<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
$(function(){
	setFinancialYearOption($("select[name='year']"))
	$("select[name='year']").change(function(){
		var date = new Date();
		var year = date.getFullYear()-1911;
		var date =$(this).find("option:selected").val();
		if(date!=year){
			$("#note").text("註:資料統計至"+date+"/12/31");
		}else{
			$("#note").text("註:資料統計至前一天。");
		}
	});
	$("#mySent").click(function(){
		$("form").submit();
	});
});
function setFinancialYearOption($year){
	var date = new Date();
	var year = date.getFullYear()-1911;
	var sel="";
	for(var i=year;i>=98;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$year.html(sel);
	$year.find("option:first").prop("selected",true);
	$("#note").text("註:資料統計至前一天。");
}
</script>
<div id="mainContent">
<form action="<c:url value='/cnfdi/downloadfinancial.jsp'/>" method="post" class="myForm">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;下載財報申報情形&nbsp;</strong>&nbsp;</span>
		</legend>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 1&nbsp;]</span><strong style="color:#222;">&nbsp;選擇查詢年度&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin: 0px auto;">
				<div style="float:left;width: 80%;">
					<div class="basetable">
						<span>年度：</span>
						<select name="year">
						</select>
					</div>
				</div>
				<div style="float:left;width: 80%;">
					<div class="basetable">
						<span>申報情形：</span>
						<input type="radio" name="YN" value="1" id="YN1"><label for="YN1">已申報</label>
						<input type="radio" name="YN" value="0" id="YN0"><label for="YN0">未申報</label>
						<input type="radio" name="YN" value="" id="YN" checked="checked"><label for="YN">不拘</label>
					</div>
				</div>
				<div style="float:left;width: 80%;">
					<div class="basetable">
						<span id="note" style="font-size: 12px;color: red;"></span>
					</div>
				</div>
				<div style="clear: left;"></div>	
			</div>
		</fieldset>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 2&nbsp;]</span><strong style="color:#222;">&nbsp;下載檔案&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
				<img src="<c:url value='/images/sub/icon_dot.jpg'/>" width="18" height="18" />進行下載：
				<img id="mySent" src="<c:url value='/images/sub/button_search.jpg'/>" width="60" height="22" style="cursor:pointer;" />
			</div>
		</fieldset>
	</fieldset>
	</form>
</div>