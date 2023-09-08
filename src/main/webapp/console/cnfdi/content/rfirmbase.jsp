<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript">
	$(function(){
	});
</script>

<div class='tbtitle'>營運狀態</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;">資料狀態：</td>
		<td>
			${optmap.isFilled[opbean.isFilled]}
		</td>
	</tr>
	<tr>
		<td style="text-align: right;">執行情形：</td>
		<td>${optmap.active[opbean.active]}
		</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 20%;">設立情形：</td>
		<td colspan="3">${optmap.isNew[opbean.isNew]}</td>
	</tr>
		<tr>
	<c:if test="${opbean.isNew eq '1'}">
			<td style="text-align: right;width: 20%;">設立日期：</td>
			<td style="width: 20%;">
				${ibfn:addSlash(opbean.setupdate)}
			</td>
	</c:if>
			<td style="text-align: right;width: 20%;">初次審定或備查日期：</td>
			<td>
				${ibfn:addSlash(opbean.approvaldate)}
			</td>
		</tr>
	<tr>
		<td style="text-align: right;width: 20%;">設立備註：</td>
		<td colspan="3">
			${opbean.setupnote}
		</td>
	</tr>
	<tr>
		<td style="text-align: right;">經營狀況：</td>
		<td colspan="3">${optmap.isOperated[opbean.isOperated]}
		</td>
	</tr>
	<c:if test="${opbean.isOperated eq '2'}">
		<tr>
			<td style="text-align: right;">停業起迄：</td>
			<td colspan="3">
				${ibfn:addSlash(opbean.sdate)}~${ibfn:addSlash(opbean.edate)}
			</td>
		</tr>
	</c:if>
</table>
<div class='tbtitle'>營業項目</div>
<table style="width: 100%;font-size: 16px;vertical-align: text-top;" class="tchange">
	<tr>
		<td style="text-align: right;width: 20%;">主要營業項目：</td>
		<td colspan="5">
			<c:forEach var="item" items="${sic['1']}" varStatus="i">
				<c:if test="${i.index>0}"><br></c:if>${item}-${sicMap[item]}
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;vertical-align: text-top;">次要營業項目：</td>
		<td colspan="5">
			<c:forEach var="item" items="${sic['2']}" varStatus="i">
				<c:if test="${i.index>0}"><br></c:if>${item}-${sicMap[item]}
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;vertical-align: text-top;">涉及特許與特殊項目：</td>
		<td colspan="5">
			<c:forEach var="item" items="${sic['0']}" varStatus="i">
				<c:if test="${i.index>0}"><br></c:if>${item}-${sicMap[item]}
			</c:forEach>
		</td>
	</tr>
</table>

<div class='tbtitle'>國稅局或財報登記營業項目</div>
<div style="padding-left: 15px;"><pre style="margin-left: 15px;">${opbean.firmXNTBTSic}</pre></div>

<div class='tbtitle'>備註</div>
<div style="padding-left: 15px;">
	<pre style="margin-left: 15px;">${opbean.note}</pre>
</div>
