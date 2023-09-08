<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
var context = "${pageContext.request.contextPath}";
$(function(){
	$(".toqurl").click(function(){
		postUrlByForm("/console/interviewone/showfollowingbyqno.jsp", {"qNo":$(this).prop("alt")});
	});
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
		                 {"sSortDataType":"chinese", "aTargets": [2,3,4,5,6]},
		                 {"sSortDataType":"formatted", "aTargets": [7]},
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
	},sdInitDataTableSetting(),sdSortChinese(),sdSortNumric());
	var oTable=$(".istable").dataTable(y);
	//$("#istable tr,#istable td").css("cursor","none");
	if("${fn:contains(memberUrls,'E0303')}"==="false"){
	 	$("body").on('click',".istable tbody tr td",function(){
	 		var ntds= $(this).parent().find("td");
	 		if(ntds.index($(this))<7){
	 			var qNo=$('td:eq(1)',$(this).parent()).find("span").text();
	 			postUrlByForm('/console/interviewone/showbyqno.jsp',{'qNo':qNo,'type':'/console'});
	 		}
	 	});
 	}
	$("#myrbase").accordion({ heightStyle: "content"});
	
});
</script>
<script src="<c:url value='/js/ajaxRequest.js'/>" type="text/javascript" charset="utf-8"></script>
<div id="error-dialog" ></div>

<c:if test="${empty singlelist}"><div class='tbtitle'>訪查資料</div><div>暫無訪查資料</div></c:if>
<div id="showfiles"></div>
<div id="myrbase">
	<c:forEach  var="m" items="${singlelist}">
		<h3>${m.key eq '0'?'':'(轉投資)'}${m.key eq '0'?sysinfo.COMP_CHTNAME:reInvestNoXName[m.key]}</h3>
		<div>
			<div class='tbtitle'>訪查資料</div>
			<div style="width: 98%;padding-left: 15px;">
					<table class="display istable" style="width: 100%;"> 
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
									<td>
										<c:if test="${bean.interviewStatus eq 0}">${ioOpt.interviewStatus[bean.interviewStatus]}</c:if>
										<c:if test="${bean.interviewStatus != 0}">
											<c:choose>
												<c:when test="${fn:contains(memberUrls,'E0303')}">
													<c:set var="iurl" scope="page">
														<c:url value='/console/interviewone/getform.jsp'>
															<c:param name="investNo">${bean.investNo}</c:param>
															<c:param name="qNo">${bean.qNo}</c:param>
														<c:param name="reinvestNo">${bean.reInvestNo}</c:param>
															<c:param name="year">${bean.year}</c:param>
															<c:param name="formtype">I</c:param>
															<c:param name="editType">edit</c:param>
														</c:url>
													</c:set>
													<a  href="${iurl}" class="btn_class_opener" style="color: #777777;font-size: 14px;">${ioOpt.interviewStatus[bean.interviewStatus]}</a>
												</c:when>
												<c:otherwise>
														${ioOpt.interviewStatus[bean.interviewStatus]}
												</c:otherwise>
											</c:choose>
										</c:if>
									</td>
									<td>
										<c:if test="${bean.surveyStatus eq 0}">${ioOpt.surveyStatus[bean.surveyStatus]}</c:if>
										<c:if test="${bean.surveyStatus != 0}">
											<c:choose>
												<c:when test="${fn:contains(memberUrls,'E0303')}">
													<c:set var="surl" scope="page">
													<c:url value='/console/interviewone/getform.jsp'>
														<c:param name="investNo">${bean.investNo}</c:param>
														<c:param name="qNo">${bean.qNo}</c:param>
														<c:param name="reinvestNo">${bean.reInvestNo}</c:param>
														<c:param name="year">${bean.year}</c:param>
														<c:param name="formtype">S</c:param>
														<c:param name="editType">edit</c:param>
													</c:url>
													</c:set>
													<a href="${surl}" class="btn_class_opener" style="color: #777777;font-size: 14px;">${ioOpt.surveyStatus[bean.surveyStatus]}</a>
												</c:when>
												<c:otherwise>${ioOpt.surveyStatus[bean.surveyStatus]}
												</c:otherwise>
											</c:choose>
										</c:if>
									</td>
									
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
									<td><c:choose>
											<c:when test="${not empty fsMap[xqNo].following}">
	 											<input type="button" class="btn_class_opener toqurl" style="font-size: 14px;" value="${ioOpt.following[fsMap[xqNo].following]}"  alt="${bean.qNo}"/> 
											</c:when>
											<c:when test="${empty fsMap[xqNo].following   &&   (errI!=-1 || errF!=-1 || not empty spNeed)}">
	 											<input type="button" class="btn_class_opener toqurl" style="font-size: 14px;" value="${ioOpt.following['']}" alt="${bean.qNo}"/> 
											</c:when>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${bean.fileCount>0}">
												<input type="button" class="btn_class_opener getFiles" style="font-size: 14px;" value="下載" year="${bean.year}" reNo="${bean.reInvestNo}"/>
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
<div style="margin: 10px;"></div>
