<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<script>
	var projects = [
<%
	Vector<InterviewProject> projects = InterviewProjectDAO.select();
	
	ArrayList<Integer> years = new ArrayList<Integer>();
	for(int i = 0;i < projects.size();i++){
		InterviewProject project  = projects.get(i);
		if(!years.contains(project.getYear())){
			years.add(project.getYear());
		}
		
		String name = project.getName();
		if(DataUtil.trim(project.getSubName()).length() > 0){
			name = name + " - " + DataUtil.trim(project.getSubName());
		}
%>
		{year: <%= project.getYear() %>, name: '<%= name  %>', id: <%= project.getIdentifier() %>}<%= i < projects.size() - 1 ? "," : "" %>
<%
	}
%>
];
	
	function setProject(){
		var year = $('#ProjectYear').val();

		$('#project').children().remove().end();
		for(var i = 0;i < projects.length;i++){
			var p = projects[i];
			if(p.year == year){
				$('#project').append($('<option>', {value : p.id, title: p.name}).text(p.name.length > 40 ? p.name.substr(0, 40) + '...' : p.name));
			}
		}
	}
	
	$(function(){
		setProject();
	});
</script>
<select id="ProjectYear" name="ProjectYear" onchange="setProject();">
<%
	Collections.sort(years);
	for(int i = years.size() - 1;i >= 0 ;i--){
%>
		<option value="<%= years.get(i) %>"><%= years.get(i) %></option>
<%
	}
%>
</select>
<select id="project" name="project"></select>