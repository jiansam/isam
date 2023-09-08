<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:choose>
	<c:when test="${fn:contains(memberUrls,'E0401') || isbg !='1'}">
		<!-- edit version -->
		<form id="Updatebg" action='<c:url value="/console/updateinvestorbg.jsp"/>' method="post">
		<input type="hidden" name="investorSeq" value="${ofiiobean.investorSeq}">
		<div class='tbtitle'>背景資料</div>
		<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;width: 20%;vertical-align: text-top;">黨政軍案件：</td>
					<td colspan="3">
						<c:forEach var="newitem" items="${optmap.BG1}" varStatus="i">
							<input type="checkbox" name="BG1" id="BG1${i.index}" value="${newitem.key}"><label for="BG1${i.index}">${newitem.value}</label>&nbsp;&nbsp;
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;vertical-align: text-top;">備註：</td>
					<td colspan="3"><textarea name="BG1Note" style="width: 70%;" rows="10">${bgs.BG1Note}</textarea></td>
				</tr>
				<tr class="tbuborder">
					<td style="text-align: right;width: 20%;vertical-align: text-top;">央企政府出資案件：</td>
					<td colspan="3">
						<c:forEach var="newitem" items="${optmap.BG2}" varStatus="i">
							<input type="checkbox" name="BG2" id="BG2${i.index}" value="${newitem.key}"><label for="BG2${i.index}">${newitem.value}</label>&nbsp;&nbsp;
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;vertical-align: text-top;">備註：</td>
					<td colspan="3"><textarea name="BG2Note" style="width: 70%;" rows="10">${bgs.BG2Note}</textarea></td>
				</tr>
		</table>
		</form>
		<div style="margin-top: 10px;text-align: center;"><input type="button" value="儲存" id="saveBG" class="btn_class_Approval"></div>
	</c:when>
	<c:otherwise>
		<!-- view version -->
		<div class='tbtitle'>背景資料</div>
		<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;width: 20%;vertical-align: text-top;">黨政軍案件：</td>
					<td colspan="3">
						<c:if test="${empty bgs.BG1}">${optmap.BG1['']}</c:if>
						<c:forEach var="item" items="${bgs.BG1}" varStatus="i">
							<c:if test="${i.index>0}">、</c:if>${optmap.BG1[item]}
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;vertical-align: text-top;">備註：</td>
					<td colspan="3">${bgs.BG1Note}</td>
				</tr>
				<tr class="tbuborder">
					<td style="text-align: right;width: 20%;vertical-align: text-top;">央企政府出資案件：</td>
					<td colspan="3">
						<c:if test="${empty bgs.BG2}">${optmap.BG2['']}</c:if>
						<c:forEach var="item" items="${bgs.BG2}" varStatus="i">
							<c:if test="${i.index>0}">、</c:if>${optmap.BG2[item]}
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;vertical-align: text-top;">備註：</td>
					<td colspan="3">${bgs.BG2Note}</td>
				</tr>
		</table>
	</c:otherwise>
</c:choose>
<div style="height: 10px;"></div>
