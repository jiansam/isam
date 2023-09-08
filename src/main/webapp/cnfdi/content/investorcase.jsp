<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	 $( "#accordion" ).accordion({
	      collapsible: true, heightStyle: "content"
	 });
	 var y=$.extend({
			"aaSorting": [[ 1, "desc" ]],
			"aoColumnDefs": [
            	              { 'sClass':'center', "aTargets": [ 0,1,2,3] },
			                  {"sSortDataType":"chinese", "aTargets": [ 2,3 ]},
			                  {"sType":"string", "aTargets": [ 1 ]},
                 	          {"bSortable": false, "aTargets": [0]}
			                // ,{ "bVisible": false, "aTargets": [6,7,8] }
			              ],
			"bFilter": false, "bPaginate": false, "bInfo": false,
			"fnDrawCallback": function ( oSettings ) {
							/* Need to redo the counters if filtered or sorted */
							if ( oSettings.bSorted || oSettings.bFiltered ){
								for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
									$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
								}
							}
						}
		},sdInitDataTableSetting(),sdSortChinese());
		$(".audit02table").dataTable(y);
		$(".show02").click(function(){
			showTwo($(this));
		});
		$(".audit07table").dataTable(y);
		$(".show07").click(function(){
			showSeven($(this));
		});			
});
function showTwo($item){
	var seq=$item.prop("alt");
	var investNo=$item.prop("name");
	$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit02.jsp",{
		'type':'show','investNo':investNo,"seq":seq
	}, function(data){
		$( "#audit02hide").html(data); 
		$("#audit02hide").dialog({
			height: 430,
			width: 650,
			modal: true,
			draggable: false,
			title:"稽核二 公文附加附款案件"
	});
	},"html");
}
function showSeven($item){
	var seq=$item.prop("alt");
	var investNo=$item.prop("name");
	$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit07.jsp",{
		'type':'show','investNo':investNo,"seq":seq
	}, function(data){
		$( "#audit07hide").html(data); 
		$("#audit07hide").dialog({
			height: 430,
			width: 650,
			modal: true,
			draggable: false,
			title:"稽核七 委員會核准之重大投資案"
	});
	},"html");
}
</script>
<div id="audit02hide"></div>
<div id="audit07hide"></div>
<div id="accordion" style="font-size: medium;">
  <c:forEach var="item" items="${icase}">
  	<h3>${item.cname}</h3>
  	<div>
  	<div style="text-align: right;"><a href="<c:url value='/showapproval.jsp?investNo=${item.investNo}'/>" class="btn_class_opener">檢視國內事業</a></div>
  	<div class='tbtitle'>投資事業基本資料</div>
	<table style="width: 100%;font-size: 16px;" class="tchange">
		<tr>
			<td style="text-align: right;width: 20%;">投資案號：</td>
			<td>${fn:substring(item.investNo,0,1) =='4'?'（陸分）':'（陸）'}${item.investNo}</td>
			<td style="text-align: right;width: 20%;">統一編號：</td>
			<td style="width: 40%;">${item.idno}</td>
		</tr>
		<tr>
			<td style="text-align: right;">公司名稱：</td>
			<td colspan="3">${item.cname}</td>
		</tr>
		<c:if test="${not empty agent[item.caseNo]}">
		<tr>
			<td style="text-align: right;">代理人：</td>
			<td>${agent[item.caseNo].IN_AGENT}&nbsp;${agent[item.caseNo].POSITION_NAME}</td>
			<td style="text-align: right;">聯絡電話：</td>
			<td>${agent[item.caseNo].TEL_NO}</td>
		</tr>
		</c:if>
		<c:if test="${not empty contacts[item.investNo]}">
			<c:forEach var="citem" items="${contacts[item.investNo]}">
				<tr>
					<td style="text-align: right;">聯絡人：</td>
					<td>${citem.name}</td>
					<td style="text-align: right;">聯絡電話：</td>
					<td>${citem.telNo}</td>
				</tr>
			</c:forEach>
		</c:if>
	</table>
  	<div class='tbtitle'>投資人投資情形</div>
	<table style="width: 100%;font-size: 16px;" class="tchange">
		<tr>
			<td style="text-align: right;width: 20%;">核准日期：</td>
			<td style="width: 20%;">${ibfn:addSlash(item.respdate)}</td>
			<td style="text-align: right;width: 20%;">投資金額：</td>
			<td><span class="numberFmt">${item.investvalue}</span></td>
		</tr>
		<tr>
			<td style="text-align: right;width: 20%;">持有股權或出資額：</td>
			<td style="width: 20%;">
				<c:choose>
					<c:when test="${empty item.investedcapital}">尚無資料</c:when>
					<c:otherwise><span class="numberFmt" >${item.investedcapital}</span></c:otherwise>
				</c:choose>
			</td>
			<td style="text-align: right;width: 20%;">面額：</td>
			<td>
				<c:choose>
					<c:when test="${item.orgType != '01' && item.orgType!= '02'}">
						無須填寫
					</c:when>
					<c:when test="${empty item.faceval && (item.orgType eq '01' || item.orgType eq '02')}">
						尚無資料
					</c:when>
					<c:otherwise><span class="numberFmt">${item.faceval}</span></c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td style="text-align: right;width: 20%;">持有股權比率：</td>
			<td style="width: 20%;">
				<c:if test="${ibfn:SToD(item.sp)>100||ibfn:SToD(item.sp)<0}">異常(</c:if>
					<span class="numberFmt" >${item.sp}</span><c:if test="${not empty item.sp}">%</c:if>
					<c:if test="${ibfn:SToD(item.sp)>100||ibfn:SToD(item.sp)<0}">)</c:if>
					<c:if test="${empty item.sp}">異常(無資料)</c:if>
			</td>
			<td style="text-align: right;width: 20%;">投資案資料狀態：</td>
			<td>${optmap.isFilled[item.isFilled]}</td>
		</tr>
	</table>
	<div class='tbtitle'>稽核</div>
	<table style="width: 95%;font-size: 16px;margin-left: 15px;" class="tchange">
		<c:forEach var="opt" items="${auditOpt}">
			<c:choose>
			<c:when test="${fn:startsWith(opt.auditCode,'02') && fn:length(opt.auditCode)>2}">
			</c:when>
			<c:when test="${fn:startsWith(opt.auditCode,'07') && fn:length(opt.auditCode)>2}">
			</c:when>				
			<c:otherwise>
				<c:if test="${fn:length(opt.auditCode)==2||not empty audits[item.investNo][opt.auditCode] || (fn:startsWith(opt.auditCode,'06')&&!fn:endsWith(opt.auditCode,'99'))}">
					<tr <c:if test="${fn:length(opt.auditCode)==2&&opt.auditCode!='01'}"> class="tbuborder"</c:if>>
						<td style="text-align: right;width: 30%;vertical-align: text-top;">
							<c:if test="${fn:length(opt.auditCode)==2}">(${opt.auditCode})</c:if>${opt.description}：
						</td>
						<td style="word-break: break-all;">
							<c:choose>
							<c:when test="${opt.selectName =='date'}">${ibfn:addSlash(audits[item.investNo][opt.auditCode])}</c:when>
							<c:when test="${opt.auditCode=='04'}">
								${optmap.YN[audits[item.investNo][opt.auditCode]]}
							</c:when>
							<c:when test="${opt.selectName =='YN' && opt.autoDef == '1'}">
								<c:choose>
									<c:when test="${audits[item.investNo][opt.auditCode]=='0'}">${optmap.YN[audits[item.investNo][opt.auditCode]]}</c:when>
									<c:when test="${empty audits[item.investNo][opt.auditCode]}">暫無訪查資料</c:when>
									<c:otherwise>${audits[item.investNo][opt.auditCode]}</c:otherwise>
								</c:choose>							
							</c:when>
							<c:when test="${opt.selectName =='YN'}">
								${empty optmap.YN[audits[item.investNo][opt.auditCode]]?optmap.YN['']:optmap.YN[audits[item.investNo][opt.auditCode]]}
							</c:when>
							<c:when test="${opt.selectName =='dept'}">
								<c:forEach var="x" items="${fn:split(audits[item.investNo][opt.auditCode],',')}" varStatus="i">
									<c:if test="${i.index>0}">、</c:if>
									${deptOpt[x]}
								</c:forEach>
							</c:when>
							<c:when test="${opt.selectName =='city'}">
								<c:forEach var="x" items="${fn:split(audits[item.investNo][opt.auditCode],',')}" varStatus="i">
									<c:if test="${i.index>0}">、</c:if>
									${levelone[fn:substring(x,0,5)]}${leveltwo[x]}
								</c:forEach>
							</c:when>
							<c:otherwise>${audits[item.investNo][opt.auditCode]}</c:otherwise>
						</c:choose>
						</td>
					</tr>
					</c:if>
				</c:otherwise>
			</c:choose>
		<c:if test="${opt.auditCode=='02' && not empty audit02[item.investNo]}">
			<tr>
				<td colspan="2">
					<table class="audit02table" style="width: 90%;">
						<thead>
							<tr>
								<th>序號</th>
								<th>發文日期</th>
								<th>文號</th>
								<th>財務簡報</th>
								<th>檢視</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="x" items="${audit02[item.investNo]}">
								<tr>
									<td></td>
									<td>${ibfn:addSlash(x.value['0201'])}</td>
									<td>${x.value['0202']}</td>
									<td>${optmap.YN[x.value['0205']]}</td>
									<td align="center"><input class="show02 btn_class_opener" type="button" value="檢視" alt="${x.key}" name="${item.investNo}"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>
		</c:if>
		<c:if test="${opt.auditCode=='07' && not empty audit07[item.investNo]}">
			<tr>
				<td colspan="2">
					<table class="audit07table" style="width: 90%;">
						<thead>
							<tr>
								<th>序號</th>
								<th>核准日期</th>
								<th>核准文號</th>
								<th>說明</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="x" items="${audit07[item.investNo]}">
								<tr>
									<td></td>
									<td>${ibfn:addSlash(x.value['0701'])}</td>
									<td>${x.value['0702']}</td>
									<td>${x.value['0703']}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>
		</c:if>		
		</c:forEach>
	</table>
	</div>
  </c:forEach>
  
</div>
