<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<div class='tbtitle'>系統資訊</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 20%;">負責人：</td>
		<td style="width: 20%;">${sysinfo.CHARGE_PERSON}</td>
		<td style="text-align: right;width: 20%;">電話：</td>
		<td>${sysinfo.TEL_NO}</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 20%;">地址：</td>
		<td colspan="3">${sysinfo.COUNTY_NAME}${sysinfo.TOWN_NAME}${sysinfo.ADDRESS}</td>
	</tr>	
	<%-- <c:forEach var="item" items="${agents}">
		<tr>
			<td style="text-align: right;">代理人：</td>
			<td >${item.IN_AGENT}&nbsp;${item.POSITION_NAME}</td>
			<td style="text-align: right;">代理人電話：</td>
			<td>${item.TEL_NO}</td>
		</tr>
	</c:forEach> --%>
	<c:forEach var="item" items="${contactsName}">
		<tr>
			<td style="text-align: right;">聯絡人：</td>
			<td>${item.name}</td>
			<td style="text-align: right;">聯絡人電話：</td>
			<td>${item.telNo}</td>
		</tr>
	</c:forEach>
</table>
<div class='tbtitle'>聯絡資訊</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 20%;">問卷電話：</td>
		<td>${contacts.S_telNo}</td>
	</tr>
	<tr>
		<td style="text-align: right;">訪視電話：</td>
		<td>${contacts.I_telNo}</td>
	</tr>
	<tr>
		<td style="text-align: right;">問卷地址：</td>
		<td>${IOLV1[contacts.S_City]}${IOLV2[contacts.S_Town]}${contacts.S_Addr}</td>
	</tr>
	<tr>
		<td style="text-align: right;">訪視地址：</td>
		<td>${IOLV1[contacts.I_City]}${IOLV2[contacts.I_Town]}${contacts.I_Addr}</td>
	</tr>
</table>
