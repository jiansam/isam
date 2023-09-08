<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	 var y=$.extend({
			"aaSorting": [[ 1, "desc" ]],
			"aoColumnDefs": [
                     		  {'sClass':'center', "aTargets": [ 0,1,2,3] },
			                  {"sSortDataType":"chinese", "aTargets": [ 2,3 ]},
			                  {"sType":"string", "aTargets": [ 1 ]},
                          	  {"bSortable": false, "aTargets": [0]}
			                ],
			"bFilter": false, 
			"bPaginate": false, 
			"bInfo": false,
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
	$.post( "${pageContext.request.contextPath}/console/cnfdi/content/firmaudit02.jsp",{
		'type':'show','investNo':"${sysinfo.INVESTMENT_NO}","seq":seq,"tabsNum":$("#tabs").tabs( "option", "active" )
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
<div class='tbtitle'>稽核</div>
<table style="width: 95%;font-size: 16px;margin-left: 15px;">
	<c:forEach var="opt" items="${auditOpt}">
		<c:choose>
			<c:when test="${fn:startsWith(opt.auditCode,'02') && fn:length(opt.auditCode)>2}">
			</c:when>
			<c:when test="${fn:startsWith(opt.auditCode,'07') && fn:length(opt.auditCode)>2}">
			</c:when>			
			<c:otherwise>
			<c:if test="${fn:length(opt.auditCode)==2||not empty audit[opt.auditCode] || (fn:startsWith(opt.auditCode,'06')&&!fn:endsWith(opt.auditCode,'99'))}">
				<tr <c:if test="${fn:length(opt.auditCode)==2&&opt.auditCode!='01'}"> class="tbuborder"</c:if>>
					<td style="text-align: right;width: 30%;vertical-align: text-top;">
						<c:if test="${fn:length(opt.auditCode)==2}">(${opt.auditCode})</c:if>${opt.description}：
					</td>
					<td style="word-break: break-all;">
						<c:choose>
							<c:when test="${opt.selectName =='date'}">${ibfn:addSlash(audit[opt.auditCode])}</c:when>
							<c:when test="${opt.selectName =='YN' && opt.autoDef == '1'}">
<%-- 								<c:choose>
									<c:when test="${audit[opt.auditCode]=='0'||opt.auditCode=='04'}">${optmap.YN[audit[opt.auditCode]]}</c:when>
									<c:when test="${fn:length(audit[opt.auditCode])==3}">${optmap.YN['1']}&nbsp;（${audit[opt.auditCode]}年）</c:when>
									<c:otherwise>${optmap.YN['0']}&nbsp;（${audit[opt.auditCode]}）</c:otherwise>
								</c:choose>							 --%>
								<c:choose>
									<c:when test="${audit[opt.auditCode]=='0'||opt.auditCode=='04'}">${optmap.YN[audit[opt.auditCode]]}</c:when>
									<c:when test="${empty audit[opt.auditCode]}">暫無訪查資料</c:when>
									<c:otherwise>${audit[opt.auditCode]}</c:otherwise>
								</c:choose>							
							</c:when>
							<c:when test="${opt.selectName =='YN'}">${empty optmap.YN[audit[opt.auditCode]]?optmap.YN['']:optmap.YN[audit[opt.auditCode]]}</c:when>
							<c:when test="${opt.selectName =='dept'}">
								<c:forEach var="x" items="${fn:split(audit[opt.auditCode],',')}" varStatus="i">
									<c:if test="${i.index>0}">、</c:if>
									${deptOpt[x]}
								</c:forEach>
							</c:when>
							<c:when test="${opt.selectName =='city'}">
								<c:forEach var="x" items="${fn:split(audit[opt.auditCode],',')}" varStatus="i">
									<c:if test="${i.index>0}">、</c:if>
									${IOLV1[fn:substring(x,0,5)]}${IOLV2[x]}
								</c:forEach>
							</c:when>
							<c:otherwise>${audit[opt.auditCode]}</c:otherwise>
						</c:choose>
					</td>
				</tr>
				</c:if>
				</c:otherwise>
			</c:choose>
	<c:if test="${opt.auditCode=='02'}">
			<tr>
				<td colspan="2">
					<div style="margin-left: 70px;">
					<table class="audit02table" style="width: 90%;text-align: center;">
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
							<c:forEach var="x" items="${audit02}">
								<tr>
									<td></td>
									<td>${ibfn:addSlash(x.value['0201'])}</td>
									<td>${x.value['0202']}</td>
									<td>${optmap.YN[x.value['0205']]}</td>
									<td align="center"><input class="show02 btn_class_opener" type="button" value="檢視" alt="${x.key}"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
				</td>
			</tr>
		</c:if>
		<c:if test="${opt.auditCode=='07'}">
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
							<c:forEach var="x" items="${audit07}">
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
