<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;管理未核准案件&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<c:choose>
				<c:when test="${infoMap.editType eq 'edit'||infoMap.editType eq 'show'}">
					<c:set var="posNo">4</c:set>
				</c:when>
				<c:otherwise><c:set var="posNo">2</c:set></c:otherwise>
			</c:choose>
			<jsp:include page="/console/reject/content/menu.jsp" flush="true">
				<jsp:param value="${posNo}" name="pos"/>
			</jsp:include>
		</div>
		<div>
	   		<c:choose>
				<c:when test="${infoMap.editType eq 'show'}">
					<jsp:include page="/console/reject/content/reject4read.jsp" flush="true" />
				</c:when>
				<c:otherwise>
					<jsp:include page="/console/reject/content/reject.jsp" flush="true" />
				</c:otherwise>
			</c:choose>
		</div>
	</fieldset>
</div>