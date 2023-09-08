<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/includes/header.jsp" flush="true" />
<jsp:include page="/includes/bodytop.jsp" flush="true" />
<c:choose>
	<c:when test="${fn:contains(memberUrls,'E0302')}">
		<jsp:include page="/console/interviewtwo/content/modifyInterview.jsp" flush="true" />
	</c:when>
	<c:otherwise>
		<jsp:include page="/interviewtwo/content/showInterview.jsp" flush="true" />
	</c:otherwise>
</c:choose>
<jsp:include page="/includes/footer.jsp" flush="true" />
