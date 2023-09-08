<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:if test="${not empty indLevel1}">
	<c:forEach var="ind" items="${indLevel1}">
		<div>
			<img src='<c:url value="/images/action_add.gif"/>' class="opener" alt="open"><input type="checkbox" class="topBox" value="${ind.value[0].code}"><span>${ind.value[0].name}</span><br/>
			<c:choose>
				<c:when test="${not empty indLevel2[ind.key]}">
					<div class="closed" style="padding-left: 30px;">
						<c:forEach var="ind2" items="${indLevel2[ind.key]}">
							<div><img src='<c:url value="/images/action_remove.gif"/>'><input type="checkbox" value="${ind2.code}" name="ind"><span>${ind2.code}${ind2.name}</span></div>
						</c:forEach>
					</div>
				</c:when>
			</c:choose>
		</div>
	</c:forEach>
</c:if>