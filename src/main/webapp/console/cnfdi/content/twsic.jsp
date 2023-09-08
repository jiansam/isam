<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.bean.*" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;管理營業項目&nbsp;</strong>&nbsp;</span>
		</legend>
		
		<form action="upload_twsic.jsp" method="post" enctype="multipart/form-data">
			1. 請先下載目前資料庫內的營業項目清單：
			<a href="download_twsic.view" class="btn_class_opener" target="_blank">下載營業項目清單</a>
			<br/><br/>
			
			2. 請上傳更新後的營業項目清單：
			<input type="file" name="upload" style="background-color:#ededed;" />
			<input type="submit" value="上傳檔案" class="btn_class_opener" />
		</form>
	</fieldset><br/>
	
<%
	ArrayList<OFITWSIC> insert_list = (ArrayList<OFITWSIC>)request.getAttribute("insert_list");
	ArrayList<OFITWSIC> update_list = (ArrayList<OFITWSIC>)request.getAttribute("update_list");
	
	if(insert_list != null && insert_list.size() > 0){
%>
	本次新增以下項目：<br/><br/>
	<table class="formProj">
<%
		for(OFITWSIC twsic : insert_list){
%>
		<tr>
			<td width="40%"><%= twsic.getCode() %></td>
			<td><%= twsic.getCodename() %></td>
		</tr>
<%
		}
%>
	</table><br/><br/>
<%
	}
	
	if(update_list != null && update_list.size() > 0){
%>
	本次修改以下項目：<br/><br/>
	<table class="formProj">
<%
		for(OFITWSIC twsic : update_list){
%>
		<tr>
			<td width="40%"><%= twsic.getCode() %></td>
			<td><%= twsic.getCodename() %></td>
		</tr>
<%
		}
%>
	</table><br/>
<%
	}
%>
</div>
