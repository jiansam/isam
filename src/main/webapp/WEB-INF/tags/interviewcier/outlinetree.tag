<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ attribute name="code" required="true"%>

<%
	Hashtable<String, Vector<InterviewOutline>> outlinetree = (Hashtable<String, Vector<InterviewOutline>>)session.getAttribute("InterviewcierOutlineTree");
	
	Vector<InterviewOutline> outlines = outlinetree.get(code);
	if("".equalsIgnoreCase(code)){
%>
		<div class="MItem" style="clear: both;">
<%
	}else{
%>
		<div class="closed" style="padding-left: 30px;">
<%
	}
%>
<%
		for(int i = 0;i < outlines.size();i++){
			InterviewOutline outline =  outlines.get(i);
%>
		<div>
<%
			if(outlinetree.get(outline.getCode()) != null){
%>
			<img src='<c:url value="/images/action_add.gif"/>' class="opener" alt="open">
			<input type="checkbox" class="topBox" name="outlines" value="<%= outline.getCode() %>"><span><%= outline.getName() %></span><br/>
			<itag:outlinetree code="<%= outline.getCode() %>"></itag:outlinetree>
<%
			}else{
%>
			<img src='<c:url value="/images/action_remove.gif"/>'>
			<input type="checkbox" name="outlines" value="<%= outline.getCode() %>"><span><%= outline.getName() %></span><br/>
<%
			}
%>
		</div>
<%
		}
%>
		</div>
