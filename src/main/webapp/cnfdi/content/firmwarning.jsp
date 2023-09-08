<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- <script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>"  type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
 --%>
<script type="text/javascript">
var context = "${pageContext.request.contextPath}";
$(function(){
	$( ".msgTip" ).tooltip();
	$(".getFiles").click(function(){
		var year = $(this).attr("year");
		var reNo = $(this).attr("reNo");
		var companyName = $(this).closest("div.ui-accordion-content").prev("h3").text();
		getFlies(year, reNo, companyName);
	});	
		function getFlies(year, reNo, companyName){
			$.post( "${pageContext.request.contextPath}/console/cnfdi/content/interviewfilebyyear.jsp",{
				'year':year,'investNo':'${sysinfo.INVESTMENT_NO}','reInvestNo':reNo,'cname':companyName
				}, function(data){
				$( "#showfiles" ).html(data); 
				$( "#showfiles" ).dialog({
				      width:600,
				      modal: true,
				      title:year+'年'+companyName+'檔案列表',
				      close: function( event, ui ) {
				    	  $( "#showfiles" ).html(""); 
				      }
			});
			},"html");
		}	
	var y=$.extend({
		"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2,3,4,5,6,7,8 ] },
		                 {"sSortDataType":"chinese", "aTargets": [2,3,4,5,6,7]},
		                 {"bSortable": false, "aTargets": [0]}
		                 ],
		            "fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$(".example").dataTable(y);
 	$("body").on('click',".example tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		var x = 4;
		var local = nTds.index($(this));
		if( local < 4 || (5 < local && local < 8) ){
			var q=$('td:eq(1)',ntr).find("span").text();
			if(q.length>0){
				postUrlByForm('/interviewone/listbyqno.jsp',{'qNo':q});
			}
		}
	}); 
 	$("#myrbase").accordion({ heightStyle: "content"});
});
</script>
<script src="<c:url value='/js/ajaxRequest.js'/>" type="text/javascript" charset="utf-8"></script>
<div id="error-dialog" ></div>

<div class='tbtitle'>特殊需要</div>
<div style="width: 98%;padding-left: 15px;">
	<c:if test="${empty spNeed}">無</c:if>
	<pre>${spNeed}</pre>
</div>
<div class='tbtitle'>訪查列表</div>
<div id="showfiles"></div>
<div id="myrbase">
	<c:forEach  var="m" items="${singlelist}">
		<h3>${m.key eq '0'?'':'(轉投資)'}${m.key eq '0'?sysinfo.COMP_CHTNAME:reInvestNoXName[m.key]}</h3>
		<div>
			<div class='tbtitle'>訪查資料</div>
			<div style="width: 98%;padding-left: 15px;">
					<table class="display example" style="width: 100%;"> 
						<thead>
							<tr>
								<th rowspan="2">序號</th>
								<th rowspan="2">調查年度</th>
								<th rowspan="2">訪視情形</th>
								<th rowspan="2">問卷情形</th>
								<th colspan="2">異常情形</th>
								<th rowspan="2">處理狀態</th>
								<th rowspan="2">追蹤情形</th>
								<th rowspan="2">檔案下載</th>
							</tr>
							<tr>
								<th>訪視異常</th>
								<th>財務異常</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="bean" items="${m.value}">
								<tr>
								<td></td>
								<td>${bean.year}<span style="display: none;">${bean.qNo}</span></td>
								<td>${ioOpt.interviewStatus[bean.interviewStatus]}</td>
								<td>${ioOpt.surveyStatus[bean.surveyStatus]}</td>
								
								<c:set var="errI" value="-1"></c:set>
								<td><c:forEach var="errorI" items="${errorIlist}">
										<c:if test="${errorI == bean.qNo}">
											<c:set var="errI" value="0"></c:set>
											<input type="button" value="異常原因" onclick="sendAjax('error_Itv',this)" 
												class="btn_class_opener" style="font-size: 14px;">
										</c:if>
									</c:forEach></td>
								
								<c:set var="errF" value="-1"></c:set>
								<td><c:forEach var="errorF" items="${errorFlist}">
										<c:if test="${errorF == bean.qNo}">
											<c:set var="errF" value="0"></c:set>
											<input type="button" value="異常原因" onclick="sendAjax('error_fia',this)"
												class="btn_class_opener" style="font-size: 14px;">
										</c:if>
							</c:forEach></td>
								
								<c:set var="xqNo"><c:out value="${bean.qNo}"/></c:set>
								<td>${ioOpt.progress[fsMap[xqNo].optionValue]}</td>
								<td>
									<c:if test="${empty fsMap[xqNo].following && (errI!=-1 || errF!=-1 || not empty spNeed)}">
										${optionValName.following['']}
									</c:if>
									${optionValName.following[fsMap[xqNo].following]}
								</td>
								<td>
									<c:choose>
										<c:when test="${bean.fileCount>0}">
											<input type="button" class="btn_class_opener getFiles" style="font-size: 14px;" value="下載"  year="${bean.year}" reNo="${bean.reInvestNo}"/>
										</c:when>
									</c:choose>
								</td>								
							</tr>
							</c:forEach>
						</tbody>
					</table>
			</div>
		</div>	
	</c:forEach>
</div>
<%-- <div>
			<div style="width: 98%;padding-left: 15px;">
				<table id="example" class="display" style="width: 100%;cursor: pointer;"> 
					<thead>
						<tr>
							<th rowspan="2">序號</th>
							<th rowspan="2">調查年度</th>
							<th rowspan="2">訪視情形</th>
							<th rowspan="2">問卷情形</th>
							<th colspan="2">異常情形</th>
							<th rowspan="2">處理狀態</th>
							<th rowspan="2">追蹤情形</th>
						</tr>
						<tr>
							<th>訪視異常</th>
							<th>財務異常</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="bean" items="${singlelist}">
							<tr>
								<td></td>
								<td>${bean.year}<span style="display: none;">${bean.qNo}</span></td>
								<td>${ioOpt.interviewStatus[bean.interviewStatus]}</td>
								<td>${ioOpt.surveyStatus[bean.surveyStatus]}</td>
								<td><c:if test="${fn:indexOf(errorIlist, bean.qNo)!=-1}">異常</c:if></td>
								<td><c:if test="${fn:indexOf(errorFlist, bean.qNo)!=-1}">異常</c:if></td>
								<c:set var="xqNo"><c:out value="${bean.qNo}"/></c:set>
								<td>${ioOpt.progress[fsMap[xqNo].optionValue]}</td>
								<td>
								<c:if test="${empty fsMap[xqNo].following && (fn:indexOf(errorIlist, bean.qNo)!=-1 || fn:indexOf(errorFlist, bean.qNo)!=-1 || not empty spNeed)}">
									${optionValName.following['']}
								</c:if>
								${optionValName.following[fsMap[xqNo].following]}
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</div>
</div> --%>
<div style="margin: 10px;"></div>