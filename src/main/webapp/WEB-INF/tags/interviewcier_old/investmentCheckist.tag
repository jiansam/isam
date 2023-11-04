<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ attribute name="identifier" type="Integer" %>
<table>
	<tr>
<%
	Vector<InterviewOption> options = (Vector<InterviewOption>)session.getAttribute("InvestmentType");
	ArrayList<String> investmentList = InterviewXInvestmentDAO.createTable(identifier).get(identifier);
	if(investmentList == null){
		investmentList = new ArrayList<String>() ;
	}
	
	for(int i = 0;i < options.size();i++){
		InterviewOption option = options.get(i);
%>
		<td>
			<input type="checkbox" name="InvestmentType" value="<%= option.getOptionValue() %>" <%= investmentList.contains(option.getOptionValue()) ? "checked" : "" %>>
			<%= option.getCDescription() %>
		</td>
<%
		if(i % 2 == 1){
%>
		</tr>
		<tr>
<%
		}
%>
<%
	}
%>
	</tr>
</table>