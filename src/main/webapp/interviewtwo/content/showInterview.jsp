<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="com.isam.bean.*" %>
<%@ page import="com.isam.dao.*" %>
<%@ page import="com.isam.helper.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewtwo" %>

<script type="text/javascript">
$(function() {
  $( "#tabs" ).tabs();
});
</script>
<style type="text/css">
	.colheader{
		background-color: lightsteelblue;
		text-align: center;
		padding:5px 0px 5px 0px;
		min-width: 100px;
		vertical-align: text-top;
	}
	
	.filelist:hover{
		text-decoration: underline;
	}
</style>

<%
	int identifier = -1;
	try{
		identifier = Integer.parseInt(request.getParameter("identifier"));
	}catch(Exception e){}
	
	ArrayList<InterviewBrief> interviewbriefs = InterviewBriefDAO.select(identifier);
	
	if(interviewbriefs.size() == 0){
		return;
	}
	
	InterviewBrief interviewbrief = interviewbriefs.get(0);
%>
<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;訪查內容：<%= DataUtil.trim(interviewbrief.getCompany()) %>&nbsp;</strong>&nbsp;</span>
		</legend>
	
		<div id="tabs" style="font-size: 15px;margin-top: 20px;">
			<ul>
				<li><a href="#tabs-1">基本資料</a></li>
				<li style="float: right">
					<input type="button" class="btn_class_opener" value="返回列表" onclick='window.location.href="listInterview.jsp"'>
				</li>
			</ul>
			
			<div id="tabs-1">
				<table style="width: 95%">
					<tr>
						<td class="colheader" width="15%">訪查年度</td>
						<td><%= interviewbrief.getYear() %></td>
					</tr>
<<<<<<< HEAD
					<tr>
						<td class="colheader">企業類別</td>
						<td>
							<%=  "1".equals(interviewbrief.getType1()) ? "僑外資在臺事業<br>":"" %>
							<%=  "1".equals(interviewbrief.getType1()) ? "台元科技園區<br>":"" %>
							<%=  "1".equals(interviewbrief.getType1()) ? "陸資在臺辦事處":"" %>
						</td>
					</tr>
=======
					
>>>>>>> parent of 07f76a2 (新增 企業類別)
					<tr>
						<td class="colheader">企業名稱</td>
						<td><%= DataUtil.trim(interviewbrief.getCompany()) %></td>
					</tr>
					
					<tr>
						<td class="colheader">摘要說明</td>
						<td>
							<%= DataUtil.trim(interviewbrief.getDescription()).replaceAll("\n", "<br>") %>
						</td>
					</tr>
					
					<tr>
						<td class="colheader">檔案列表</td>
						<td>
							<ul>
<%
	ArrayList<InterviewFile> interviewfiles = InterviewFileDAO.select(identifier, "moea");
	
	for(InterviewFile file : interviewfiles){
%>
							<li style="list-style: decimal inside;">
							<a class="filelist" href="download.jsp?identifier=<%= URLEncoder.encode(ThreeDes.getEncryptString(String.valueOf(file.getIdentifier())), "utf-8") %>">
							<%= file.getFilename() %></a><br/>
							</li>
<%
	}
%>
							</ul>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</fieldset>
</div>
