<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class='tbtitle'>現場確認項目</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">企業招牌：</td>
		<td style="width: 10%;">${optionValName.signboard[IMap.signboard]}</td>
		<td style="text-align: right;width: 20%;">陸資企業群聚現象：</td>
		<td>${optionValName.cluster[IMap.cluster]}<c:if test="${IMap.cluster eq 1}">；
			<c:forEach var="item" items="${fn:split(IMap.clustereffect,',')}" varStatus="i">
				<c:if test="${i.index !=0}">、</c:if>${optionValName.clustereffect[item]}
			</c:forEach>
		</c:if>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;">員工上班情形：</td>
		<td>${optionValName.staffAttendance[IMap.staffAttendance]}</td>
		<td style="text-align: right;">營運設備：</td>
		<td>${optionValName.OE[IMap.OE]}<c:if test="${IMap.OE eq 1}">；${IMap.OENote}</c:if></td>
	</tr>
</table>
<div class='tbtitle'>書面確認項目</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">訪視狀態：</td>
		<td style="width: 10%;">${optionValName.interviewStatus[IObean.interviewStatus]}</td>
		<td style="text-align: right;width: 20%;">問卷狀態：</td>
		<td>${optionValName.surveyStatus[IObean.surveyStatus]}</td>
	</tr>
	<tr>
		<td style="text-align: right;">書面確認事項：</td>
		<td colspan="3">
			<c:if test="${not empty IMap.WC}">
			<c:forEach var="item" items="${fn:split(IMap.WC,',')}" varStatus="i">
				<c:if test="${i.index !=0}"><br></c:if>${optionValName.WC[item]}
			</c:forEach>
			</c:if>
			<c:if test="${empty IMap.WC}">
				未繳交
			</c:if>
		</td>
	</tr>
</table>
<div class='tbtitle'>特殊身份</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">經營業務為I3類：</td>
		<td>${optionValName.spI3[IMap.spI3]}</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 18%;">委員會核准之&nbsp;&nbsp;&nbsp;&nbsp;<br/>重大投資案：</td>
		<td>${optionValName.spImportant[IMap.spImportant]}</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 18%;">投資人背景為國企：</td>
		<td>${optionValName.spSOE[IMap.spSOE]}</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 18%;">公函附帶條件：</td>
		<td>${optionValName.spClause[IMap.spClause]}</td>
	</tr>
	<c:if test="${IMap.spClause == '1'}">
	<tr>
		<td style="text-align: right;width: 18%;vertical-align: top;">附帶條件內容1：</td>
		<td style="border-bottom:1px dotted silver;">${IMap.spClauseContent1}</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 18%;vertical-align: top;">落實程度1：</td>
		<td style="border-bottom:1px dotted silver;">${IMap.spImplementation1}</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 18%;vertical-align: top;">附帶條件內容2：</td>
		<td style="border-bottom:1px dotted silver;">${IMap.spClauseContent2}</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 18%;vertical-align: top;">落實程度2：</td>
		<td>${IMap.spImplementation2}</td>
	</tr>
	</c:if>
</table>
<div class='tbtitle'>國內投資事業為費用中心</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">是否為費用中心：</td>
		<td>
			${optionValName.costCenter[IMap.costCenter]}
			<c:if test="${IMap.costCenter == '3'}">
			<span>(${optionValName.costCenterStatus[IMap.costCenterStatus]})</span>
			</c:if>
		</td>
	</tr>
	<c:if test="${IMap.costCenter == '3' && not empty IMap.costCenterNote}">
	<tr>
		<td style="text-align: right;width: 18%;vertical-align: top;">說明：</td>
		<td>${IMap.costCenterNote}</td>
	</tr>
	</c:if>
</table>