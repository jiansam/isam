<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ attribute name="code" required="true" %>

<%
	Hashtable<String, Vector<InterviewRegion>> regiontree = (Hashtable<String, Vector<InterviewRegion>>)session.getAttribute("InterviewcierRegionTree");
	if(regiontree == null){
		regiontree = InterviewRegionDAO.loadRegionTree();
		session.setAttribute("InterviewcierRegionTree", regiontree);
	}
	
	ArrayList<String> checkedlist = (ArrayList<String>)request.getAttribute("checkedRegion");
	if(checkedlist == null){
		checkedlist = new ArrayList<String>();
	}
	
	Vector<InterviewRegion> regions = regiontree.get(code);
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
		for(int i = 0;i < regions.size();i++){
			InterviewRegion region =  regions.get(i);
%>
		<div>
<%
			if(regiontree.get(region.getCode()) != null){
%>
			<img src='<c:url value="/images/action_add.gif"/>' class="opener" alt="open" />
			<input type="checkbox" <%= region.getParent().length() == 0 ? "class='topBox'" : "" %> 
				name="Region" value="<%= region.getCode() %>" <%= checkedlist.contains(region.getCode()) ? "checked" : "" %>>
			<span><%= region.getName() %></span><br/>
			<itag:regiontree code="<%= region.getCode() %>"></itag:regiontree>
<%
			}else{
%>
			<img src='<c:url value="/images/action_remove.gif"/>' />
			<input type="checkbox" name="Region" <%= region.getParent().length() == 0 ? "class='topBox'" : "" %> 
				value="<%= region.getCode() %>" <%= checkedlist.contains(region.getCode()) ? "checked" : "" %>>
			<span><%= region.getName() %></span><br/>
<%
			}
%>
		</div>
<%
		}
%>
		</div>
