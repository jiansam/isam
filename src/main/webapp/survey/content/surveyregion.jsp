<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty regionLev1}">
	<c:forEach var="lev1" items="${regionLev1}">
		<c:choose>
			<c:when test="${empty regionLev2[lev1.key]}">
				<c:if test="${lev1.value[0].code != 'A'}">
					<div>
						<img src='<c:url value="/images/action_remove.gif"/>'><input type="checkbox" class="topBox" value="${lev1.value[0].code}" name="region"><span>${lev1.value[0].name}</span><br/>
					</div>
				</c:if>
			</c:when>
			<c:when test="${not empty regionLev2[lev1.key]}">
				<c:set value="${regionLev2[lev1.key]}" var="lev2Src" />
				<div>
					<img src='<c:url value="/images/action_add.gif"/>' class="opener" alt="open"><input type="checkbox" class="topBox" value="${lev1.value[0].code}"  name="region"><span>${lev1.value[0].name}</span><br/>
					<div class="closed" style="padding-left: 30px;">
					<c:forEach var="lev2" items="${lev2Src}">
						<c:choose>
							<c:when test="${empty regionLev3[lev2.code]}">
								<div><img src='<c:url value="/images/action_remove.gif"/>'><input type="checkbox" value="${lev2.code}"  name="region"><span><a title="${lev2.note}" class="ahreftip">${lev2.name}</a></span></div>
							</c:when>
							<c:when test="${not empty regionLev3[lev2.code]}">
								<c:set value="${regionLev3[lev2.code]}" var="lev3Src" />
								<div>
									<img src='<c:url value="/images/action_add.gif"/>' class="opener" alt="open"><input type="checkbox" class="topBox" value="${lev2.code}" name="region"><span><a title="${lev2.note}" class="ahreftip">${lev2.name}</a></span><br/>
									<div class="closed" style="padding-left: 30px;">
									<c:forEach var="lev3" items="${lev3Src}">
										<div><img src='<c:url value="/images/action_remove.gif"/>'><input type="checkbox" value="${lev3.code}" name="region"><span>${lev3.name}</span></div>
									</c:forEach>
									</div>
								</div>
							</c:when>
						</c:choose>
					</c:forEach>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div>Oops!資料庫出現問題，請通知網站管理員！</div>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</c:if>