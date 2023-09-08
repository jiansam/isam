<!-- header.jsp -->
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="content-language" content="zh-tw" />
<meta name="robots" content="noindex,nofollow">
<meta name="google-site-verification" content="KQpEMN4-vlxPlBDP1HLioZpkSZ6qTY-xuGfMzgAyQi4" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache"> 
<meta http-equiv="Expires" content="${nowdatetime}"> 
<meta name="SKYPE_TOOLBAR" content="SKYPE_TOOLBAR_PARSER_COMPATIBLE" />
<title>歡迎使用投資調查與管理資料庫</title>
<jsp:useBean id="nowdate" class="java.util.Date"></jsp:useBean>
<c:set var="nowdatetime"><fmt:formatDate value="${nowdate}" type="both"/></c:set>
<jsp:include page="/includes/jQueryInculdes.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/webStyle.css'/>" >
<script type="text/javascript" src="<c:url value='/js/isamhelper.js'/>"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/pure-min.css"/>
</head>