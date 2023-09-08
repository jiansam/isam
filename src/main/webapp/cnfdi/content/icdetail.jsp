<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/setDatatableOld.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
    $("#tabs").tabs();
    setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
});

</script>
<script>
$(function() {
	 var y=$.extend({
			"aaSorting": [[ 1, "desc" ]],
			"aoColumnDefs": [
                             {'sClass':'center', "aTargets": [ 0,1,2,3 ] },
			                 {"sSortDataType":"chinese", "aTargets": [ 2,3 ]},
			                 {"sType":"string", "aTargets": [ 1 ]},
                             {"bSortable": false, "aTargets": [0]}
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
		'type':'show','investNo':investNo,"seq":seq,"tabsNum":$("#tabs").tabs( "option", "active" )
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
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;投資案資料&nbsp;]</span>&nbsp;&nbsp;
					<span  style="float: right ;">
						<a href="<c:url value='/cnfdi/listinvestcase.jsp'/>" class="btn_class_opener">返回列表</a>
					</span>
				</legend>
				<c:if test="${ofiiobean!=null}">
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
					<table style="width: 100%;">
						<tr>
							<td style="text-align: right;">陸資案號：</td>
							<td>${fn:substring(icase.investNo,0,1)=='4'?'（陸分）':'（陸）'}&nbsp;${icase.investNo}</td>
							<td style="text-align: right;">執行情形：</td>
							<td>${optmap.active[ibean.active]}</td>
							<td style="text-align: right;">國內事業資料狀態：</td>
							<td >${optmap.isFilled[icase.isFilled]}</td>
						</tr>
						<tr>
							<td style="text-align: right;width: 15%;">國內事業名稱：</td>
							<td colspan="3">${icase.cname}</td>
							<td style="text-align: right;">統一編號：</td>
							<td>${icase.idno}</td>
						</tr>
						<tr>
							<td style="text-align: right;">投資人：</td>
							<td colspan="5">${ofiiobean.cname}</td>
						</tr>
						<tr>
							<td style="text-align: right;">英文名：</td>
							<td colspan="5">${ofiiobean.ename}</td>
						</tr>
						<tr>
							<td style="text-align: right;">國別：</td>
							<td >${optmap.nation[ofiiobean.nation]}${optmap.cnCode[ofiiobean.cnCode]}</td>
							<td style="text-align: right;width: 10%;">資金類型：</td>
							<td >${optmap.inSrc[ofiiobean.inrole]}</td>
							<td style="text-align: right;width: 15%;">投資人資料狀態：</td>
							<td >${optmap.isFilled[ofiiobean.isFilled]}</td>
						</tr>
					</table>					
				</div>
				</c:if>
				
				<div id="tabs" style="font-size: 16px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">詳細資料</a></li>
					    	<c:if test="${ofiiobean!=null}">
					    <li><a href="#tabs-2">稽核</a></li>
					    	</c:if>
					  </ul>
					  <div id="tabs-1">
					     <c:if test="${ofiiobean!=null}">
						<jsp:include page="/cnfdi/content/icrelated.jsp" flush="true" />
						</c:if>
						<c:if test="${ofiiooffice!=null}">
						    	<jsp:include page="/cnfdi/content/investoroffice.jsp" flush="true" />
						    </c:if>
					  </div>
					   	<c:if test="${ofiiobean!=null}">
					  <div id="tabs-2">
							  <div class='tbtitle'>稽核</div>
								<table style="width: 95%;font-size: 16px;margin-left: 15px;" class="tchange">
									<c:forEach var="opt" items="${auditOpt}">
									<c:choose>
											<c:when test="${fn:startsWith(opt.auditCode,'02') && fn:length(opt.auditCode)>2}">
											</c:when>
											<c:when test="${fn:startsWith(opt.auditCode,'07') && fn:length(opt.auditCode)>2}">
											</c:when>												
											<%-- <c:when  test="${fn:length(opt.auditCode)!=2 && empty audits[icase.investNo][opt.auditCode]}"></c:when> --%>
											<c:otherwise>
												<tr <c:if test="${fn:length(opt.auditCode)==2&&opt.auditCode!='01'}"> class="tbuborder"</c:if>>
													<td style="text-align: right;width: 30%;vertical-align: text-top;">
														<c:if test="${fn:length(opt.auditCode)==2}">(${opt.auditCode})</c:if>${opt.description}：
													</td>
													<td style="word-break: break-all;">
														<c:choose>
														<c:when test="${opt.selectName =='date'}">${ibfn:addSlash(audits[icase.investNo][opt.auditCode])}</c:when>
														<c:when test="${opt.auditCode=='04'}">
																${optmap.YN[audits[icase.investNo][opt.auditCode]]}
														</c:when>
														<c:when test="${opt.selectName =='YN' && opt.autoDef == '1'}">
															<c:choose>
																<c:when test="${audits[icase.investNo][opt.auditCode]=='0'}">${optmap.YN[audits[icase.investNo][opt.auditCode]]}</c:when>
																<c:when test="${empty audits[icase.investNo][opt.auditCode]}">暫無訪查資料</c:when>
																<c:otherwise>${audits[icase.investNo][opt.auditCode]}</c:otherwise>
															</c:choose>							
														</c:when>
														<c:when test="${opt.selectName =='YN'}">
															${empty optmap.YN[audits[icase.investNo][opt.auditCode]]?optmap.YN['']:optmap.YN[audits[icase.investNo][opt.auditCode]]}
														</c:when>
														<c:when test="${opt.selectName =='dept'}">
															<c:forEach var="x" items="${fn:split(audits[icase.investNo][opt.auditCode],',')}" varStatus="i">
																<c:if test="${i.index>0}">、</c:if>
																${deptOpt[x]}
															</c:forEach>
														</c:when>
														<c:when test="${opt.selectName =='city'}">
															<c:forEach var="x" items="${fn:split(audits[icase.investNo][opt.auditCode],',')}" varStatus="i">
																<c:if test="${i.index>0}">、</c:if>
																${levelone[fn:substring(x,0,5)]}${leveltwo[x]}
															</c:forEach>
														</c:when>
														<c:otherwise>${audits[icase.investNo][opt.auditCode]}</c:otherwise>
													</c:choose>
													</td>
												</tr>
											</c:otherwise>
										</c:choose>
										<c:if test="${opt.auditCode=='02'}">
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
							<c:forEach var="x" items="${audit02}">
								<tr>
									<td></td>
									<td>${ibfn:addSlash(x.value['0201'])}</td>
									<td>${x.value['0202']}</td>
									<td>${optmap.YN[x.value['0205']]}</td>
									<td align="center"><input class="show02 btn_class_opener" type="button" value="檢視" alt="${x.key}" name="${icase.investNo}"></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
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
						</div>
						</c:if>
					  </div>
			</fieldset>
		</div>