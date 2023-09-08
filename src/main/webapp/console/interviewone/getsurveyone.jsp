<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp" flush="true" />
<jsp:include page="/includes/bodytop.jsp" flush="true" />
<jsp:include page="/console/interviewone/content/getform.jsp" flush="true">
	<jsp:param value="營運問卷" name="formtypeName"/>
	<jsp:param value="3" name="index"/>
	<jsp:param value="S" name="formtype"/>
	<jsp:param value="問卷" name="STR"/>
</jsp:include>
<jsp:include page="/includes/footer.jsp" flush="true" />
