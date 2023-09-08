<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="com.isam.dao.*, com.isam.bean.*" %>
<%@ tag import="java.util.*" %>
<%@ tag import="org.dasin.tools.*" %>
<%@ attribute name="selected" %>

<select id="businessIncomeTaxCode" multiple="multiple">
	<option></option>
<%
	ArrayList<String> code_list = new ArrayList<String>();
	for(String s : dTools.trim(selected).split("；")){
		if(s!=null && !s.isEmpty()){
			code_list.add(s.substring(0, s.indexOf(" ")));
		}
	}
	
	for(BusinessIncomeTaxCode code : BusinessIncomeTaxCodeDAO.list()){
%>
		<option value="<%= code.getCode() %>" <%= code_list.contains(code.getCode()) ? "selected" : "" %>><%= code.getCode() + " " + code.getName() %></option>
<%
	}
%>
</select>
<input type="hidden" name="businessIncomeTaxCode" value="<%= selected %>" />