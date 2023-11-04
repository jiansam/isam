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
	String[] identifiers = request.getParameterValues("identifier") == null ? new String[0] : request.getParameterValues("identifier");
	if("new".equalsIgnoreCase(action)){
		identifiers = new String[1];
		identifiers[0] = "-1";
	}
	
	if(identifiers.length == 0){
		return;
	}
	
	PairHashtable<Integer, String, String> interviewContent = InterviewContentDAO.select(identifiers, new String[0]);
	int identifier = Integer.parseInt(identifiers[0]);
	
	Hashtable<String, InterviewOutline> OutlineTable = (Hashtable<String, InterviewOutline>)session.getAttribute("InterviewcierOutlineTable");
	Vector<String> level1 = new Vector<String>();
	Vector<String> level2 = new Vector<String>();
	for(InterviewOutline outline: OutlineTable.values()){
		if(outline.getLevel() == 1){
			level1.add(outline.getCode());
		}
		
		if(outline.getLevel() == 2){
			level2.add(outline.getCode());
		}
	}
	
	Collections.sort(level1);
	Collections.sort(level2);
%>
<script type="text/javascript" src="<c:url value='/ckeditor/ckeditor.js'/>"></script>
<script type="text/javascript" src="<c:url value='/ckeditor/config.js'/>"></script>
<script type="text/javascript">
var editor;

function edit(e){
	if(editor){
		editor.destroy();
	}
	
	 editor = CKEDITOR.replace(e, {
		 on: {
			 instanceReady : function(ev){ this.focus(); }
		}
	 });
}

$(function() {
	$( ".accordion" ).accordion({
		heightStyle: "content", 
		collapsible: true,
		//active: false,
		animate:false,
		activate: function( event, ui ) {
			if(ui.newPanel.children("textarea").length > 0){
				edit(ui.newPanel.children("textarea")[0].name);
			}else if(ui.newPanel.find("div textarea").length > 0){
				var active = ui.newPanel.accordion( "option", "active" );
				edit(ui.newPanel.find("div textarea")[active].name);
			}
		},
		create: function( event, ui ) {
			if(!editor && ui.panel.find("textarea").length > 0){
				edit(ui.panel.find("textarea")[0].name);
			}
		}
	});
});
</script>
 <div class="accordion">
 <%
 	int count_level_1 = 0;
	for(String outlineCode1 : level1){
		if("01".equalsIgnoreCase(outlineCode1)){
			continue;
		}
		
		InterviewOutline outline1 = OutlineTable.get(outlineCode1);
%>
 	<h3><%= ++count_level_1 %>.<%=  outline1.getName() %></h3>
 	<div class="accordion">
 <%
 	int count_level_2 = 0;
 	for(String outlineCode2 : level2){
		if(!outlineCode2.startsWith(outlineCode1)){
			continue;
		}
		InterviewOutline outline = OutlineTable.get(outlineCode2);
%>
	<h3><%= count_level_1 %>.<%= ++count_level_2 %>.<%=  outline.getName() %></h3>
	<div>
		<textarea name="<%= outlineCode2 %>" cols="100" rows="5" onfocus="edit('<%= outlineCode2 %>')"><%= DataUtil.trim(interviewContent.get(identifier, outlineCode2)) %></textarea>
	</div>
<%
	}
%>
	</div>
<%
	}
%>
 </div>