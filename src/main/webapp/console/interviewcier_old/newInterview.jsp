<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<itag:setattributes />
<jsp:include page="/includes/header.jsp" flush="true" />
<jsp:include page="/includes/bodytop.jsp" flush="true" />
<c:choose>
	<c:when test="${fn:contains(memberUrls,'A0301') || fn:contains(memberUrls,'E0301')}">
		<jsp:include page="/console/interviewcier/content/newInterview.jsp" flush="true" />
	</c:when>
	<c:otherwise>
		<div style="width: 100%;text-align:center;">很抱歉，使用者無新增權限。</div>
	</c:otherwise>
</c:choose>
<jsp:include page="/includes/footer.jsp" flush="true" />
