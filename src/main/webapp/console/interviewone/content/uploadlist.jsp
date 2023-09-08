<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript" src="<c:url value='/js/yearmonthhelper.js'/>"></script>
<script type="text/javascript">
$(function(){
	setYearOption($("select[name='surveyyear']"));
	setYearUpperDownBound($("select[name='surveyyear']"),10);
});
</script>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;上傳年度附加清單&nbsp;]</span>
	</legend>
<div>
	<div style="float: left;width: 100%;">
		<form id="addinvestno" action='<c:url value="/console/interviewone/updateiolist.jsp"/>' method="post" enctype="multipart/form-data">
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;">
					<tr>
						<td class="tdRight" style="width: 15%;">調查年度：</td>				
						<td class="tdLeft">
							<select name="surveyyear">
							</select>
						</td>				
					</tr>
					<tr>
						<td class="tdRight">附加檔案：</td>
						<td class="tdLeft">
							<input type="file" name="addfile" style="width: 85%;">
						</td>
					</tr>
					<tr>
						<td colspan="2" class="tdLeft">
							<span style="color: red;font-size: 12px;">
								註：<br>
								1.請選擇調查年度，並使用
								<a href="<c:url value="/forexample/uploadcnno.xlsx" />"  style="color: blue;font-size: 12px;">
							   		範例格式<img style="width: 15px;height: 15px;" alt="下載範例格式" src='<c:url value="/images/sub/icon_excel.png" />'>
							   	</a>上傳案號。<br>
								2.上傳案號後，將依所選年度將案號附加在該年度訪視清單內，若該年該案號已存在則將直接剔除，不另新增。<br>
								3.若要刪除某年特定案號，請使用年度訪視清單的刪除功能。								
							</span>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
</div>
</fieldset>