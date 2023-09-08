<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp" flush="true" />
<jsp:include page="/includes/bodytop.jsp" flush="true" />
<jsp:include page="/console/interviewone/content/getform.jsp" flush="true">
	<jsp:param value="訪視紀錄" name="formtypeName"/>
	<jsp:param value="2" name="index"/>
	<jsp:param value="I" name="formtype"/>
	<jsp:param value="紀錄" name="STR"/>
</jsp:include>
<jsp:include page="/includes/footer.jsp" flush="true" />
