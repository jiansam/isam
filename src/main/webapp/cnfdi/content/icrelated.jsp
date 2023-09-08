<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,2,3 ] },
		                 {"sSortDataType":"chinese", "aTargets": [ 1]}
		                 ],
		           "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
				   "iDisplayLength": 10
				   ,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$(".dbtable").dataTable(y);
	$(".dbtable tr,.dbtable td").css("cursor","none");
	/* $("#ibtn").click(function(){
		 alert($(this).prop("alt"))
		postOpenUrlByForm('/showinvestor.jsp',{'investorSeq':$(this).prop("alt")});
	})
	 $("#cbtn").click(function(){
		 alert($(this).prop("alt"))
		 postOpenUrlByForm('/showapproval.jsp',{'investNo':$(this).prop("alt")});
	}) */
});
</script>
<div style="text-align: right;">
	<a href="${pageContext.request.contextPath}/showinvestor.jsp?investorSeq=${icase.investorSeq}" target="_blank">
		<input type="button" value="投資人" id="ibtn" class="btn_class_opener">
	</a>
	<a href="${pageContext.request.contextPath}/showapproval.jsp?investNo=${icase.investNo}" target="_blank">
		<input type="button" value="國內事業" class="btn_class_opener">
	</a>
</div>
<div class='tbtitle'>投資情形</div>
	<table style="width: 100%;font-size: 16px;" class="tchange">
		<tr>
			<td style="text-align: right;width: 20%;">核准日期：</td>
			<td style="width: 20%;">${ibfn:addSlash(icase.respdate)}</td>
			<td style="text-align: right;width: 20%;">投資金額：</td>
			<td><span class="numberFmt">${icase.investvalue}</span></td>
		</tr>
		<tr>
			<td style="text-align: right;width: 20%;">持有股權或出資額：</td>
			<td style="width: 20%;">
				<c:choose>
					<c:when test="${empty icase.investedcapital}">尚無資料</c:when>
					<c:otherwise><span class="numberFmt" >${icase.investedcapital}</span></c:otherwise>
				</c:choose>
			</td>
			<td style="text-align: right;width: 20%;">面額：</td>
			<td>
				<c:choose>
					<c:when test="${icase.orgType != '01' && icase.orgType != '02'}">
						無須填寫
					</c:when>
					<c:when test="${empty icase.faceval}">
						尚無資料
					</c:when>
					<c:otherwise><span class="numberFmt" >${icase.faceval}</span></c:otherwise>
				</c:choose>	
			</td>
		</tr>
		<tr>
			<td style="text-align: right;width: 20%;">持有股權比率：</td>
			<td colspan="3">
				<c:choose>
					<c:when test="${empty icase.sp}">異常(尚無資料)</c:when>
					<c:when test="${ibfn:SToD(icase.sp)>100||ibfn:SToD(icase.sp)<0}">異常(<span class="numberFmt" >${icase.sp}</span>%)</c:when>
					<c:otherwise><span class="numberFmt" >${icase.sp}</span>%</c:otherwise>
				</c:choose>
			</td>			
		</tr>
	</table>
  	<div class='tbtitle'>投資人基本資料</div>
	<table style="width: 100%;font-size: 16px;" class="tchange">
		<tr>
			<td style="text-align: right;width:20%;">代理人：</td>
			<td style="width: 20%;">${agent[icase.caseNo].IN_AGENT}&nbsp;${agent[icase.caseNo].POSITION_NAME}</td>
			<td style="text-align: right;width:20%;">聯絡電話：</td>
			<td >${agent[icase.caseNo].TEL_NO}</td>
		</tr>
		<c:if test="${not empty contacts[icase.investNo]}">
			<c:forEach var="citem" items="${contacts[icase.investNo]}">
				<tr>
					<td style="text-align: right;">聯絡人：</td>
					<td>${citem.name}</td>
					<td style="text-align: right;">聯絡電話：</td>
					<td>${citem.telNo}</td>
				</tr>
			</c:forEach>
		</c:if>
	</table>
  <c:if test="${not empty relateds}">	
 <div class='tbtitle' style="margin-bottom: 5px;">母公司或關連企業及受益人資訊</div>
<div style="height: 5px;"></div>
<div style="width: 98%;margin-left: 15px;">
<table style="width: 98%;font-size: 16px;" class="dbtable">
	<thead>
		<tr>
			<th>序號</th>
			<th>名稱</th>
			<th>國別</th>
			<th>建立日期</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="r" items="${relateds}">	
			<tr>
				<td></td>
				<td>${r.relatedname}</td>
				<td>${optmap.nation[r.nation]}${optmap.cnCode[r.cnCode]}</td>
				<td>${ibfn:toTWDateStr(r.createtime)}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="height: 50px;"></div>
</c:if>	
<div class='tbtitle'>背景資料</div>
	<table style="width: 100%;font-size: 16px;" class="tchange">
			<tr>
				<td style="text-align: right;width: 20%;">黨政軍案件：</td>
				<td colspan="3">
					<c:if test="${empty bgs.BG1}">否</c:if>
					<c:forEach var="item" items="${bgs.BG1}" varStatus="i">
						<c:if test="${i.index>0}">、</c:if>${optmap.BG1[item]}
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;width: 20%;">備註：</td>
				<td colspan="3">${bgs.BG1Note[0]}</td>
			</tr>
			<tr class="tbuborder">
				<td style="text-align: right;width: 20%;">央企國企案件：</td>
				<td colspan="3">
					<c:if test="${empty bgs.BG2}">否</c:if>
					<c:forEach var="item" items="${bgs.BG2}" varStatus="i">
						<c:if test="${i.index>0}">、</c:if>${optmap.BG2[item]}
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;width: 20%;">備註：</td>
				<td colspan="3">${bgs.BG2Note[0]}</td>
			</tr>
	</table>
<div class='tbtitle'>備註</div>
<div style="width: 98%;margin-left: 15px;">
<pre>${ofiiobean.note}</pre>
</div> 