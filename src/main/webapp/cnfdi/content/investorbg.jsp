<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class='tbtitle'>背景資料</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
		<tr>
			<td style="text-align: right;width: 20%;vertical-align: text-top;">黨政軍案件：</td>
			<td colspan="3">
				<c:if test="${empty bgs.BG1}">${optmap.BG2['']}</c:if>
				<c:forEach var="item" items="${bgs.BG1}" varStatus="i">
					<c:if test="${i.index>0}">、</c:if>${optmap.BG1[item]}
				</c:forEach>
			</td>
		</tr>
		<tr>
			<td style="text-align: right;width: 20%;vertical-align: text-top;">備註：</td>
			<td colspan="3">${bgs.BG1Note[0]}</td>
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
			<td colspan="3">${bgs.BG2Note[0]}</td>
		</tr>
</table>
