<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ attribute name="selectName" required="true"%>
<%@ attribute name="selectedOption" %>
<%
	Vector<InterviewOption> options = (Vector<InterviewOption>)session.getAttribute(selectName);
	if(options == null){
		options = InterviewOptionDAO.select(selectName);
		session.setAttribute(selectName, options);
	}
	
	for(int i = 0;i < options.size();i++){
		InterviewOption option = options.get(i);
%>	<option value="<%= option.getOptionValue() %>" <%=  option.getOptionValue().equalsIgnoreCase(selectedOption) ? "selected" : "" %>>
			<%= option.getCDescription() %>
		</option>
<%
	}
%>