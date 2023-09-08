<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.service.ofi.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript">
$(function(){
	$("#nation").select2();
	$("#cnCode").select2();
	$("#nation").on("change",function(){
		if(parseInt($(this).val(),10)===142){
			$("#cnSelect").show();
		}else{
			$("#cnCode").select2("val","");
			$("#cnSelect").hide();
		}
	});
	if("${param.cnCodeDef}".length>0||parseInt("${param.nationDef}",10)===142){
		$("#cnSelect").show();
		$("#cnCode").select2("val","${param.cnCodeDef}");
	}
	if("${param.nationDef}".length>0){
		$("#nation").select2("val","${param.nationDef}");
	}
});
</script>
<%
	OFIInvestOptionService ser= new OFIInvestOptionService();
	Map<String, Map<String, String>> map=ser.select();
	request.setAttribute("nation", map.get("nation"));
	request.setAttribute("cnCode", map.get("cnCode"));
%>
<select id="nation" name="nation" style="width: 250px;">
	<option value="">不拘</option>
	<c:forEach var="item" items="${nation}">
		<option value="${item.key}">${item.value}</option>
	</c:forEach>
</select>
<span id="cnSelect" style="display: none;">
<select id="cnCode" name="cnCode" style="width: 250px;">
	<option value="">不拘</option>
	<c:forEach var="item" items="${cnCode}">
		<option value="${item.key}">${item.value}</option>
	</c:forEach>
</select>
</span>
