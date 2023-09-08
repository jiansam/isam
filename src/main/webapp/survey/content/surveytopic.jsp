<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${not empty topicMap}">
	<c:set var="url" value="${pageContext.request.requestURL}" />
	<c:set var="download" value="surveydownloaditem"  />
	<c:choose>
		<c:when test="${fn:indexOf(url,download)!=-1}">
			<c:forEach var="topic" items="${topicMap}">
				<c:forEach var="topic1" items="${topic.value}">
					<div class="${topic.key}">
						<img src='<c:url value="/images/action_add.gif"/>' class="opener" alt="open"><input type="checkbox" class="topBox" value="${topic1.key}"><span>${topicLev1[topic1.key][0].title}</span><br/>
						<div class="closed" style="padding-left: 30px;">
							<c:forEach var="topic2" items="${topic1.value}">
								<div><img src='<c:url value="/images/action_remove.gif"/>'><input type="checkbox" value="${topic2.topic}" name="topic"><span>${topic2.title}</span></div>
							</c:forEach>
						</div>
					</div>
				</c:forEach>
			</c:forEach>
		</c:when>
		<c:when test="${fn:indexOf(url,download)==-1 && not empty surveyqType}">
			<c:forEach var="topic1" items="${topicMap[surveyqType]}">
				<div>
					<img src='<c:url value="/images/action_add.gif"/>' class="opener" alt="open"><input type="checkbox" class="topBox" value="${topic1.key}"><span>${topicLev1[topic1.key][0].title}</span><br/>
					<div class="closed" style="padding-left: 30px;">
						<c:forEach var="topic2" items="${topic1.value}">
							<div><img src='<c:url value="/images/action_remove.gif"/>'><input type="checkbox" value="${topic2.topic}" name="topic"><span>${topic2.title}</span></div>
						</c:forEach>
					</div>
				</div>
			</c:forEach>
		</c:when>	
	</c:choose>
</c:if>