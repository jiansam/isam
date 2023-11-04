<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<itag:setattributes />
<jsp:include page="/includes/header.jsp" flush="true" />
<jsp:include page="/includes/bodytop.jsp" flush="true" />
<c:choose>
	<c:when test="${fn:contains(memberUrls,'E0301')}">
		<jsp:include page="/console/interviewcier/content/modifyInterview.jsp" flush="true" />
	</c:when>
	<c:otherwise>
		<jsp:include page="/console/interviewcier/content/showInterview.jsp" flush="true" />
	</c:otherwise>
</c:choose>
<jsp:include page="/includes/footer.jsp" flush="true" />
