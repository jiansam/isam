<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% 
request.setAttribute("vEnter", "\n"); 
%>
<div class='tbtitle'>訪查說明</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">訪查初視情形：</td>
		<td>
	<c:if test="${empty IMap.operating}">無
	</c:if>
			<c:forEach var="item" items="${fn:split(IMap.operating,',')}" varStatus="i">
				<c:if test="${i.index !=0}">、</c:if>${optionValName.operating[item]}<c:if test="${item eq 999}">(${IMap.operatingTypeNote})</c:if>
			</c:forEach>
		</td>
	</tr>
		<tr>
			<td style="text-align: right;border-top: 1px solid #c6c6c6;">訪視備註：</td>
			<td style="border-top: 1px solid #c6c6c6;">
	<c:if test="${ empty IMap.interviewnote}">無
	</c:if>
			${fn:replace(IMap.interviewnote,vEnter,"<br>")}
			</td>
		</tr>
</table>
<div class='tbtitle'>財務資料總評說明</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">營業收入：</td>
		<td style="width: 20%;">${optionValName.income[IMap.income]}</td>
		<td style="text-align: right;width: 20%;">營業成本或費用：</td>
		<td>${optionValName.income[IMap.costexpend]}</td>
	</tr>
</table>
<div class='tbtitle'>異常狀態</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">財務異常：</td>
		<td>
			<c:choose>
				<c:when test="${IObean.interviewStatus eq 1}">${IOBaseInfo.isFError}</c:when>
				<c:otherwise>
					${optionValName.interviewStatus[IObean.interviewStatus]}
				</c:otherwise>
			</c:choose></td>
	</tr>
	<c:if test="${IObean.interviewStatus eq 1 && IOBaseInfo.isFError eq '異常'}">
	<tr>
		<td style="text-align: right;vertical-align: top;">原因：</td>
		<td>${IOBaseInfo.isFError_reason}</td>
	</tr>
	</c:if>
	<tr>
		<td style="text-align: right;">訪視異常：</td>
		<td>
			<c:choose>
				<c:when test="${IObean.interviewStatus eq 1 ||IOBaseInfo.isIError eq '異常'}">${IOBaseInfo.isIError}</c:when>
				<c:otherwise>
					${optionValName.interviewStatus[IObean.interviewStatus]}
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<c:if test="${IObean.interviewStatus eq 1 && IOBaseInfo.isFError eq '異常' || IOBaseInfo.isIError eq '異常'}">
	<tr>
		<td style="text-align: right;vertical-align: top;">原因：</td>
		<td>${IOBaseInfo.isIError_reason}</td>
	</tr>
	</c:if>	
	<tr>
		<td style="text-align: right;">特殊需要：</td>
		<td colspan="3"><c:if test="${empty spNeed}">無</c:if>${spNeed}</td>
	</tr>
</table>
<c:if test="${not empty IMap.note || not empty SMap.surveynote}">
<div class='tbtitle'>備註</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<c:if test="${not empty IMap.note}">
		<tr>
			<td style="text-align: right;width: 18%;">訪視紀錄：</td>
			<td>${fn:replace(IMap.note,vEnter,"<br>")}</td>
		</tr>
	</c:if>
	<c:if test="${not empty SMap.surveynote}">
		<tr>
			<td style="text-align: right;width: 18%;border-top: 1px solid #c6c6c6;">營運問卷：</td>
			<td style="border-top: 1px solid #c6c6c6;">${fn:replace(SMap.surveynote,vEnter,"<br>")}</td>
		</tr>
	</c:if>
</table>
</c:if>