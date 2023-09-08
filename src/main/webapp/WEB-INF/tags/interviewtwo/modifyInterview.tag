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
	
	ArrayList<InterviewBrief> interviewbriefs = InterviewBriefDAO.select(identifier);
	if("new".equalsIgnoreCase(action)){
		interviewbriefs.add(new InterviewBrief());
	}
	
	if(interviewbriefs.size() == 0){
		return;
	}
	
	InterviewBrief interviewbrief = interviewbriefs.get(0);
%>
<script type="text/javascript" src="<c:url value='/ckeditor/ckeditor.js'/>"></script>
<script type="text/javascript" src="<c:url value='/ckeditor/config.js'/>"></script>
		<table>
			<tr>
				<td class="colheader">訪查年度</td>
				<td><input type="text" name="year" size="100" value="<%= interviewbrief.getYear() %>"></td>
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
