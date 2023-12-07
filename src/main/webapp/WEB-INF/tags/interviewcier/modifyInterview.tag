<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ tag import="java.util.*" %>
<%@ tag import="java.text.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<%@ attribute name="action" %>
<%
	int identifier = -1;
	try{
		identifier = Integer.parseInt(request.getParameter("identifier"));
	}catch(Exception e){}
	
	ArrayList<InterviewCier> interviewbriefs = InterviewCierDAO.select(identifier);
	if("new".equalsIgnoreCase(action)){
		interviewbriefs.add(new InterviewCier());
	}
	
	if(interviewbriefs.size() == 0){
		return;
	}
	
	InterviewCier interviewbrief = interviewbriefs.get(0);
%>
<script type="text/javascript" src="<c:url value='/ckeditor/ckeditor.js'/>"></script>
<script type="text/javascript" src="<c:url value='/ckeditor/config.js'/>"></script>
		<table>
			<tr>
				<td class="colheader">訪查年度</td>
				<td><input type="text" name="year" size="100" value="<%= interviewbrief.getYear() %>"></td>
			</tr>
			
		 
			<tr>
				<td class="colheader">企業類別</td>
				<td>
					<input type="checkbox" name="type1" value="1" <%= "1".equals(interviewbrief.getType1()) ? "checked='checked'" :""%> >僑外資在臺事業 &nbsp;  
					<input type="checkbox" name="type2" value="1" <%= "1".equals(interviewbrief.getType2()) ? "checked='checked'" :""%> >台元科技園區 &nbsp;
					<input type="checkbox" name="type3" value="1" <%= "1".equals(interviewbrief.getType3()) ? "checked='checked'" :""%> >陸資在臺辦事處
				</td>
			</tr>
			
			 
			<tr>
 
				<td class="colheader">企業名稱</td>
				<td><input type="text" name="company" size="100" value="<%= DataUtil.trim(interviewbrief.getCompany()) %>"></td>
			</tr>
			
			<tr>
				<td class="colheader">摘要說明</td>
				<td>
					<textarea name="description" cols="100" rows="5" class="ckeditor"><%= DataUtil.trim(interviewbrief.getDescription()) %></textarea>
				</td>
			</tr>
		</table>
