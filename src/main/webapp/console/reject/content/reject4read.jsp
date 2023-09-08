<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script type="text/javascript">

$(function() {
	setFormatInputDefault(".numberFmt",2);
	//checkCurrencyLayout();
	
});
</script>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6;padding-top: 10px;">
					<span style="color:#F30;">[&nbsp;國內事業基本資料&nbsp;]</span>
				</legend>
				    <table style="width: 98%;font-size: 16px;" class="formProj">
						<tr>
							<th colspan="4">國內事業基本資料</th>
						</tr>
						<tr>
							<c:choose>
								<c:when test="${userInfo.company eq 'cier' || userInfo.company eq 'ibtech'}">
									<td class="trRight" style="width: 15%;">國內事業名稱：</td>				
									<td class="trLeft">${rejbean.cname}</td>
									<td class="trRight" style="width: 10%;">統一編號：</td>				
									<td style="width: 20%;">${rejbean.idno}</td>	
								</c:when>
								<c:otherwise>
									<td class="trRight">國內事業名稱：</td>				
									<td class="trLeft" colspan="3">${rejbean.cname}</td>
								</c:otherwise>
							</c:choose>	
						</tr>
						<tr>
							<td class="trRight">設立情形：</td>
							<td>${opt.isNew[rejbean.isNew]}</td>
							<td class="trRight">設立日期：</td>
							<td>${ibfn:addSlash(rejbean.setupdate)}</td>
						</tr>					
						<tr>
							<td class="trRight">組織型態：</td>
							<td colspan="3">${opt.orgType[rejbean.orgType]}</td>
						</tr>					
				   	</table>
			</fieldset>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6;">
					<span style="color:#F30;">[&nbsp;申請人與代理人資料&nbsp;]</span>
				</legend>
				   	<table style="width: 98%;font-size: 16px;" class="formProj"  id="ApplicantForm">
				   		<tr>
				   			<th style="width: 5%;">序號</th>
				   			<th style="width: 45%;">申請人姓名或公司名稱</th>
				   			<th style="width: 20%;">國別</th>
				   			<th>代理人(職業)</th>
				   			
				   		</tr>
				   		<c:if test="${empty appls}">
				   			<tr>
				   				<td colspan="4">尚無資料</td>
				   			</tr>
				   		</c:if>
				   		<c:forEach var="appls" items="${appls}" varStatus="i">
				   			<tr>
				   				<td rowspan="2" style="text-align: center;">(${i.index+1})</td>
				   				<td>${appls.cApplicant}<c:if test="${not empty appls.eApplicant}"><br>${appls.eApplicant}</c:if></td>
				   				<td>${appls.nation}<c:if test="${not empty appls.cnCode}">（${appls.cnCode}）</c:if></td>
				   				<td>${appls.agent}</td>
				   			</tr>
				   			<tr>
				   				<td colspan="3">備註：${appls.note}</td>
				   			</tr>
				   		</c:forEach>
				   	</table>
			</fieldset>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6;padding-top: 10px;">
					<span style="color:#F30;">[&nbsp;相關資料&nbsp;]</span>
				</legend>
				    <table style="width: 98%;font-size: 16px;" class="formProj">
						<tr>
							<th colspan="4">申請相關資料</th>
						</tr>
						<tr>
							<td class="trRight">申請日期：</td>
							<td>${ibfn:addSlash(rejectObj.receiveDate)}</td>
							<td class="trRight">收文文號：</td>
							<td>${rejectObj.receiveNo}</td>
						</tr>					
						<tr>
							<td class="trRight">申請事項：</td>
							<td>${opt.currency[rejectObj.currency]}&nbsp;<span class="numberFmt">${rejectObj.money}</span>
								<c:if test="${not empty rejectObj.money}">&nbsp;元</c:if>
							</td>
							<td class="trRight">持股比例：</td>
							<td><span class="numberFmt">${rejectObj.shareholding}</span>&nbsp;%
							</td>
						</tr>
						<tr>
							<td class="trRight">業別項目：</td>
							<td colspan="3">
								<c:forEach var="item" items="${fn:split(rSic,',')}" varStatus="i">
									<c:if test="${i.index >0}"><br></c:if>
									<c:if test="${not empty item}">${item}-${twsic[item]}</c:if>
								</c:forEach>
							</td>
						</tr>
						<tr>
							<td class="trRight">其他業別項目：</td>
							<td colspan="3">
								<pre>${rejectObj.otherSic}</pre>
							</td>
						</tr>
						<tr>
							<th colspan="4">審議相關資料</th>
						</tr>
						<tr>
							<td class="trRight" style="width: 15%;">發文日期：</td>
							<td style="width: 40%;">
								${ibfn:addSlash(rejectObj.issueDate)}
							</td>
							<td class="trRight" style="width: 10%;">發文文號：</td>
							<td>
								${rejectObj.issueNo}
							</td>
						</tr>					
						<tr>
							<td class="trRight">駁回類型：</td>
							<td colspan="3">${opt.rejectType[rejectObj.rejectType]}
							</td>
						</tr>
						<tr>
							<td class="trRight">駁回決議：</td>
							<td colspan="3">${opt.decision[rejectObj.decision]}
							</td>
						</tr>
						<tr>
							<td class="trRight">駁回決議說明：</td>
							<td colspan="3">
								<pre>${rejectObj.explanation}</pre>
							</td>
						</tr>
						<tr>
							<td class="trRight">駁回理由：</td>
							<td colspan="3">
								<pre>${rejectObj.reason }</pre>
							</td>
						</tr>
						<tr>
							<td class="trRight">備註：</td>
							<td colspan="3">
								<pre>${rejectObj.note}</pre>
							</td>
						</tr>
				   	</table>
			</fieldset>
