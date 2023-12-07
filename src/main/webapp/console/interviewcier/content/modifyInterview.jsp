<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>

<script type="text/javascript">
$(function() {
  $( "#tabs" ).tabs();
});

function deleteInterview(id){
	if(confirm("確定刪除這筆紀錄？（刪除的紀錄無法回復）")){
		window.location.href = "deleteInterview.jsp?identifier=" + id;
	}else{
		return false;
	}
}
</script>
<style type="text/css">
	.colheader{
		background-color: lightsteelblue;
		text-align: center;
		padding:5px 0px 5px 0px;
		min-width: 100px;
		vertical-align: text-top;
	}
	
	.ui-datepicker{
		Font-size:13px !important;
		padding: 0.2em 0.2em 0;
	}
</style>
<%
	String identifier = request.getParameter("identifier");
	if(identifier == null){
		return;
	}
%>
<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;修改訪查紀錄&nbsp;</strong>&nbsp;</span>
		</legend>
 	
 		<form name="modifyform" action="modifyInterview-2.jsp" method="post">
		<div id="tabs" style="font-size: 15px;margin-top: 20px;">
			<ul>
				<li><a href="#tabs-1">基本資料</a></li>
				<li><a href="#tabs-3">附加檔案</a></li>
				<li style="float: right">
					<input type="button" class="btn_class_opener" onclick="deleteInterview(<%= identifier %>)" value="刪除紀錄" />
					<input type="button" class="btn_class_opener" value="返回列表" onclick='window.location.href="listInterview.jsp"'>
				</li>
			</ul>
			
			<div id="tabs-1">
				<itag:modifyInterview />
			</div>
		
			<div id="tabs-3">
				<itag:modifyInterviewFile />
			</div>
		</div>
		
		<div style="text-align: center; margin-top: 10px;font-size: 14px;">
			<input type="hidden" name="identifier" value="<%= identifier %>" />
			<input type="submit" style="font-size: 16px;" class="btn_class_Approval" value="儲存修改">
		</div>
		</form>
	</fieldset>
</div>
