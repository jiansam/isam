<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<div class='tbtitle'>營運狀態</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 20%;">設立日期：</td>
		<td><c:if test="${opbean.isNew eq '2'}">新設</c:if>${ibfn:addSlash(opbean.setupdate)}</td>
		<td style="text-align: right;width: 20%;">初次審定或備查日期：</td>
		<td>${ibfn:addSlash(opbean.approvaldate)}</td>
	</tr>
	<c:if test="${not empty opbean.setupnote}">
	<tr>
		<td style="text-align: right;width: 20%;">設立備註：</td>
		<td colspan="3">${opbean.setupnote}</td>
	</tr>
	</c:if>
	<tr>
		<td style="text-align: right;width: 20%;">核准日期：</td>
		<td style="width: 20%;" >${ibfn:addSlash(opbean.respdate)}</td>
		<td style="text-align: right;width: 20%;">核准文號：</td>
		<td>${opbean.receiveNo}</td>
	</tr>
	<tr>
		<td style="text-align: right;">經營狀況：</td>
		<td colspan="3">
			${optmap.isOperated[opbean.isOperated]}
		<c:if test="${not empty ibfn:addSlash(opbean.sdate)}">，${ibfn:addSlash(opbean.sdate)}~${ibfn:addSlash(opbean.edate)}			
		</c:if>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;">經營身分：</td>
		<td>${sysinfo.inSrc}</td>
	</tr>
	<tr>
		<td style="text-align: right;">執行情形：</td>
		<td>${optmap.active[opbean.active]}</td>
	</tr>
</table>
<div class='tbtitle'>營業項目</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 20%;vertical-align: text-top;">主要營業項目：</td>
		<td colspan="5">
			<c:forEach var="item" items="${sic['1']}" varStatus="i">
				<c:if test="${i.index>0}"><br></c:if>${item}
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;vertical-align: text-top;">次要營業項目：</td>
		<td colspan="5">
			<c:forEach var="item" items="${sic['2']}" varStatus="i">
				<c:if test="${i.index>0}"><br></c:if>${item}
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;vertical-align: text-top;">涉及特許與特殊項目：</td>
		<td colspan="5">
			<c:forEach var="item" items="${sic['0']}" varStatus="i">
				<c:if test="${i.index>0}"><br></c:if>${item}
			</c:forEach>
		</td>
	</tr>
</table>

<c:if test="${not empty opbean.firmXNTBTSic}">
<div class='tbtitle'>國稅局或財報登記營業項目</div>
<div style="margin-left: 15px;"><pre style="margin-left: 15px;">${opbean.firmXNTBTSic}</pre></div>
</c:if>

<c:if test="${not empty opbean.note}">
<div class='tbtitle'>備註</div>
<div style="margin-left: 15px;"><pre style="margin-left: 15px;">${opbean.note}</pre></div>
</c:if>