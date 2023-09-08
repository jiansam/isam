<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.bean.*" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;匯入陸資辦事處資料&nbsp;</strong>&nbsp;</span>
		</legend>
		
		<form action="import_office.jsp" method="post" enctype="multipart/form-data">
		
		
			
			
			請選擇檔案：
			<input type="file" name="upload" style="background-color:#ededed;" required />
			<input type="submit" value="送出" class="btn_class_opener" />
				<a href="<c:url value="/forexample/1100302_匯入商業司陸資資料範本.xls" />" class="btn_class_opener" target="_blank">範例檔下載</a>
				<br/><br/>
				<font style="color:red;">
				說明： <br/>
				1.匯入前，請先檢查欄位名稱是否與「範例檔」相同，若不同(例如：多1空格、半型、全型等)， 則會匯入失敗。 <br/>
				2.當統一編號相同時，資料匯入後會自動取代原系統資料。
				</font>
				
		</form>
	</fieldset><br/>
	
</div>
