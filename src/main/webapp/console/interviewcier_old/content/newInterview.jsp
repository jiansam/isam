<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/opener.js'/>"></script>

<script type="text/javascript">
$(function() {
  $( "#tabs" ).tabs();
});
</script>
<style type="text/css">
	.colheader{
		background-color: lightsteelblue;
	}
	
	.ui-datepicker{
		Font-size:13px !important;
		padding: 0.2em 0.2em 0;
	}
</style>

<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;新增訪查紀錄&nbsp;</strong>&nbsp;</span>
		</legend>
	
		<form name="newform" action="newInterview-2.jsp" method="post">
		<div id="tabs" style="font-size: 14px;margin-top: 20px;">
			<ul>
				<li><a href="#tabs-1">基本資料</a></li>
				<li><a href="#tabs-2">訪談內容</a></li>
				<li style="float: right">
					<input type="button" class="btn_class_opener" value="返回列表" onclick='window.location.href="listInterview.jsp"'>
				</li>
			</ul>
			
			<div id="tabs-1">
				<itag:modifyInterview action="new" />
			</div>
		
			<div id="tabs-2">
				<itag:modifyInterviewContent action="new" />
			</div>
		</div>
		
		<div style="text-align: center; margin-top: 10px;font-size: 14px;">
			<input type="submit" style="font-size: 16px;" class="btn_class_Approval" value="儲存新增">
		</div>
		</form>
	</fieldset>
</div>
