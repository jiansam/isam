<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<%@ attribute name="identifier" required="true" type="Integer"%>
<%
	Hashtable<String, String> optionsTable = (Hashtable<String, String>)session.getAttribute("InvestmentTypeTable");
	HashMap<Integer, ArrayList<String>> interviewXinvestment = (HashMap<Integer, ArrayList<String>>)request.getAttribute("InterviewXInvestment");

	ArrayList<String> investmentList =  interviewXinvestment.get(identifier) == null ? 
			new ArrayList<String>() : interviewXinvestment.get(identifier);
			
	for(int i = 0;i < investmentList.size();i++){
%>
		<%= DataUtil.trim(optionsTable.get(investmentList.get(i))) %><%= i < investmentList.size() - 1 ? "<br>" : "" %>
<%
	}
%>