<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.service.ofi.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript">
$(function(){
	$("#nation2").select2();
	$("#cnCode2").select2();
	$("#nation2").on("change",function(){
		if(parseInt($(this).val(),10)===142){
			$("#cnSelect2").show();
		}else{
			$("#cnCode2").select2("val","");
			$("#cnSelect2").hide();
		}
	});
// 	console.log('cnCodeDef='+"${param.cnCodeDef}")
// 	console.log('nationDef='+"${param.nationDef}")
	if("${param.cnCodeDef}".length>0||parseInt("${param.nationDef}",10)===142){
		$("#cnSelect2").show();
		$("#cnCode2").select2("val","${param.cnCodeDef}");
	}
	if("${param.nationDef}".length>0){
		$("#nation2").select2("val","${param.nationDef}");
	}
});
</script>
<%
	OFIInvestOptionService ser= new OFIInvestOptionService();
	Map<String, Map<String, String>> map=ser.select();
	request.setAttribute("nation", map.get("nation"));
	request.setAttribute("cnCode", map.get("cnCode"));
%>
<select id="nation2" name="nation2" style="width: 250px;">
	<option value="">不拘</option>
	<c:forEach var="item" items="${nation}">
		<option value="${item.key}">${item.value}</option>
	</c:forEach>
</select>
<span id="cnSelect2" style="display: none;">
<select id="cnCode2" name="cnCode2" style="width: 250px;">
	<option value="">不拘</option>
	<c:forEach var="item" items="${cnCode}">
		<option value="${item.key}">${item.value}</option>
	</c:forEach>
</select>
</span>
