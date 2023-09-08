<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ attribute name="code" required="true"%>

<%
	Hashtable<String, Vector<Industry>> industrytree = (Hashtable<String, Vector<Industry>>)session.getAttribute("InterviewcierIndustryTree");
	if(industrytree == null){
		industrytree = InterviewIndustryDAO.loadIndustryTree();
		session.setAttribute("InterviewcierIndustryTree", industrytree);
	}
	
	ArrayList<String> checkedlist = (ArrayList<String>)request.getAttribute("checkedIndustry");
	if(checkedlist == null){
		checkedlist = new ArrayList<String>();
	}
	
	Vector<Industry> industries = industrytree.get(code);
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
		for(int i = 0;i < industries.size();i++){
			Industry industry =  industries.get(i);
%>
		<div>
<%
			if(industrytree.get(industry.getCode()) != null){
%>
			<img src='<c:url value="/images/action_add.gif"/>' class="opener" alt="open">
			<input type="checkbox" class="topBox" name="Industry" value="<%= industry.getCode() %>" <%= checkedlist.contains(industry.getCode()) ? "checked" : "" %>>
			<span><%= industry.getName() %></span><br/>
			<itag:industrytree code="<%= industry.getCode() %>"></itag:industrytree>
<%
			}else{
%>
			<img src='<c:url value="/images/action_remove.gif"/>'>
			<input type="checkbox" name="Industry" value="<%= industry.getCode() %>" <%= checkedlist.contains(industry.getCode()) ? "checked" : "" %>>
			<span><%= industry.getCode() %><%= industry.getName() %></span><br/>
<%
			}
%>
		</div>
<%
		}
%>
		</div>
