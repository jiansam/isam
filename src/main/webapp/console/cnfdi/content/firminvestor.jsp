<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,2,3] },
		                 {"sSortDataType":"chinese", "aTargets": [ 1,2]},
		                 {"sSortDataType":"numberFmt-text", "aTargets": [ 4,5,6]},
		                 { 'sClass':'trRight', "aTargets": [4,5,6] }],
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
	},sdInitDataTableSetting(),sdSortChinese(),sdNumberFmt());
	var oTable=$(".dbtable").dataTable(y);
	$("body").on('click',".dbtable tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
 		postUrlByForm('/console/showinvestform.jsp',{'investorSeq':$('td:eq(1)',ntr).find("span").text()});
	});
 	$("#adddash tr td").each(function(){
		if($.trim($(this).text()).length==0){
			$(this).text("--");
		}
		if($(this).parent("tr").children("td").index($(this))==3){
		var numStr=$.trim($(this).text());
			if(numStr=="--"){
				var xStr="${fn:startsWith(sysinfo.OrgTypeName,'股份有限')}";
				if(("${sysinfo.FACE_VALUE}".length==0 && xStr==="true")|| "${sysinfo.PAID_CAPITAL}".length==0){
					$(this).text("異常(無資料)");
				}
			}else{
				var num=parseFloat(getRemoveCommaVal(numStr),10);
				if(num>100||num<0){
					$(this).text("異常("+numStr+")");
				}
			}
		}
	});
});
</script>
<div class='tbtitle'>基本資料</div>
<table style="width: 100%;font-size: 16px;">
	<tr>
		<td style="text-align: right;width: 15%;">登記資本額：</td>
		<td>
			<c:choose>
				<c:when test="${empty sysinfo.REGI_CAPITAL}">尚無資料</c:when>
				<c:otherwise><span class="numberFmt" >${sysinfo.REGI_CAPITAL}</span></c:otherwise>
			</c:choose>
		</td>
		<td style="text-align: right;width: 15%;">實收資本額：</td>
		<td>
			<c:choose>
				<c:when test="${sysinfo.OrgTypeName eq '分公司'}">
					同登記資本額
				</c:when>
				<c:when test="${empty sysinfo.PAID_CAPITAL}">
					尚無資料
<%-- 					<c:choose>
						<c:when test="${!fn:startsWith(sysinfo.OrgTypeName,'股份有限')}">同登記資本額</c:when>
						<c:otherwise>尚無資料</c:otherwise>
					</c:choose> --%>
				</c:when>
				<c:otherwise><span class="numberFmt" >${sysinfo.PAID_CAPITAL}</span></c:otherwise>
			</c:choose>
		</td>
		<td style="text-align: right;width: 15%;">面額：</td>
		<td>
			<c:choose>
				<c:when test="${!fn:startsWith(sysinfo.OrgTypeName,'股份有限')}">
					無須填寫
				</c:when>
				<c:when test="${empty sysinfo.FACE_VALUE && fn:startsWith(sysinfo.OrgTypeName,'股份有限')}">
					尚無資料
				</c:when>
				<c:otherwise><span class="numberFmt">${sysinfo.FACE_VALUE}</span></c:otherwise>
			</c:choose>			
		</td>
	</tr>
</table>
<div class='tbtitle'>彙整資料</div>
<table style="width: 100%;font-size: 16px;" id="adddash">
	<tr>
		<th class="trRight" style="width: 20%;">資金類型</th>
		<th class="trRight" style="width: 30%;padding-right: 20px;">投資金額</th>
		<th class="trRight" style="width: 30%;padding-right: 20px;">持有股權(出資額)</th>	
		<th class="trRight" style="width: 20%;padding-right: 20px;">股權比例</th>	
	</tr>
			<c:set var="tMoney2" value="0" />
			<c:set var="tival2" value="0" />
			<c:set var="tsp2" value="0" />
		<c:forEach var="sitem" items="${opsummary}" varStatus="i">
			<c:choose>
			<c:when test="${i.index>0}">
				<tr class="trRight">
					<td>${sitem.gname}</td>
					<td style="padding-right: 20px;"><span class="numberFmt" >${sitem.money2}</span></td>
					<td style="padding-right: 20px;"><span class="numberFmt" >${sitem.ival}</span></td>
					<td style="padding-right: 20px;"><span class="numberFmt" >${sitem.sp}</span><c:if test="${not empty sitem.sp}">&nbsp;%</c:if></td>
				</tr>
				<c:set var="tMoney2" value="${tMoney2+sitem.money2}" />
				<c:set var="tival" value="${tival+sitem.ival}" />
				<c:set var="tsp" value="${tsp+sitem.sp}" />
			</c:when>
			<c:otherwise>
				<tr class="trRight">
					<td>${sitem.gname}</td>
					<td style="padding-right: 20px;"><span class="numberFmt" >${sitem.money2}</span></td>
					<td style="padding-right: 20px;"><span class="numberFmt" >${sitem.ival}</span></td>
					<td style="padding-right: 20px;">
					<c:if test="${empty sitem.sp && (not empty opsummary[1].sp ||not empty  opsummary[2].sp)}">
						<span class="numberFmt">${100-opsummary[1].sp-opsummary[2].sp}</span>&nbsp;%
						<c:set var="tsp" value="${tsp+100-opsummary[1].sp-opsummary[2].sp}" />
					</c:if>
					<c:if test="${not empty sitem.sp}">
						<span class="numberFmt">${sitem.sp}</span>
						<c:set var="tsp" value="${tsp+sitem.sp}" />
					</c:if>
					</td>
				</tr>
				<c:set var="tMoney2" value="${tMoney2+sitem.money2}" />
				<c:set var="tival" value="${tival+sitem.ival}" />
			</c:otherwise>
			</c:choose>
		</c:forEach>
		<tr class="trRight">
			<td>小計</td>
			<td style="padding-right: 20px;"><span class="numberFmt" >${tMoney2}</span></td>
			<td style="padding-right: 20px;"><span class="numberFmt" >${tival}</span></td>
			<td style="padding-right: 20px;"><span class="numberFmt" >${tsp}</span><c:if test="${not empty tsp}">&nbsp;%</c:if></td>
		</tr>
</table>
<div class='tbtitle' style="margin-bottom: 5px;">投資人列表</div>
<div style="height: 5px;"></div>
<div style="width: 98%;padding-left: 15px;">
<table style="width: 100%;font-size: 16px;" class="dbtable">
	<thead>
		<tr>
			<th>序號</th>
			<th style="width: 25%;">投資人</th>
			<th>資金類型</th>
			<th>核准日期</th>
			<th>投資金額</th>
			<th>持有股權(出資額)</th>	
			<th>股權比例</th>	
		</tr>
	</thead>
	<tbody>
		<c:forEach var="investor" items="${investors}">
			<tr>
				<td></td>
				<td>${investor.INVESTOR_CHTNAME}<span class="serno" style="display: none;">${investor.INVESTOR_SEQ}</span></td>
				<td>${optmap.inSrc[investor.INVE_ROLE_CODE]}</td>
				<td>${ibfn:addSlash(investor.RESP_DATE)}</td>
				<td><span class="numberFmt" >${investor.MONEY2}</span></td>
				<td><span class="numberFmt" >${investor.investvalue}</span></td>
				<td>
					<c:choose>
						<c:when test="${not empty investor.pt &&(ibfn:SToD(investor.pt)>100||ibfn:SToD(investor.pt)<0)}">
							異常(<span class="numberFmt" >${investor.pt}</span>%)
						</c:when>
						<c:when test="${empty investor.pt &&(empty sysinfo.PAID_CAPITAL||(empty sysinfo.FACE_VALUE && fn:startsWith(sysinfo.OrgTypeName,'股份有限')))}">異常(無資料)</c:when>
						<c:when test="${empty investor.pt}">0.00%</c:when>
						<c:otherwise><span class="numberFmt" >${investor.pt}</span>%</c:otherwise>
					</c:choose>
<%-- 					<c:if test="${ibfn:SToD(investor.pt)>100||ibfn:SToD(investor.pt)<0}">異常(</c:if>
					<span class="numberFmt" >${investor.pt}</span><c:if test="${not empty investor.pt}">%</c:if>
					<c:if test="${ibfn:SToD(investor.pt)>100||ibfn:SToD(investor.pt)<0}">)</c:if> 
					<c:if test="${empty investor.pt &&(empty sysinfo.PAID_CAPITAL||(empty sysinfo.FACE_VALUE && fn:startsWith(sysinfo.OrgTypeName,'股份有限')))}">異常(無資料)</c:if>	--%>		
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="margin: 10px;"></div>