<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>"  type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>

<script type="text/javascript" src="<c:url value='/js/jquery.ui.widget.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.iframe-transport.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.fileupload.js'/>"></script>

<script type="text/javascript">
var context = "${pageContext.request.contextPath}";
$(function(){
	$(".msgTip").tooltip();
	var y=$.extend({
		"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2,3,4,5,6,7 ] },
		                 {"sSortDataType":"chinese", "aTargets": [4,5 ]},
		                 {"sSortDataType":"formatted", "aTargets": [1]},
		                 {"bSortable": false, "aTargets": [0]}
		                 ],
		           /*"aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
				   "iDisplayLength": 20, */
				   "fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese(),sdSortNumric());
	var oTable=$("#example").dataTable(y);
 	if("${fn:contains(memberUrls,'E0303')}"==="false"){
	 	$("body").on('click',"#example tbody tr td",function(){
	 		var ntds= $(this).parent().find("td");
			var local = nTds.index($(this));
			if( local < 4 || (5 < local && local < 7) ){
	 			var qNo=$('td:eq(1)',$(this).parent()).find("span").text();
	 			postUrlByForm('/console/interviewone/showbyqno.jsp',{'qNo':qNo,'type':'/console'});
	 		}
	 	});
	}
 	
 	$(".uploadItem").click(function(){
 		var item=$(this).prop("alt");
 		var tmp=item.split("_");
	 	managefiles($(this),tmp[1],tmp.length>2?tmp[2]:'',tmp[0]);
 	});
});



function managefiles($item,investNo,reInvestNo,qNo){
	var year = $($item).closest("tr").find("td:eq(1)").html();
	year = year.substring(0, year.indexOf("<span"));
	
	$.post( "${pageContext.request.contextPath}/includes/fileupload.jsp",
			{
				'investNo':investNo,
				'year':year,
				'reInvestNo':reInvestNo,
				'qNo':qNo
			}, 
			function(data){
						$( "#uploadform" ).html(data); 
						$( "#uploadform" ).dialog({
						      height:400,
						      width:800,
						      modal: true,
						      resizable: false,
						      draggable: false,
						      title:'檔案管理_'+'${IOBaseInfo.cname}'+"("+'${IOBaseInfo.investNo}'+")",
						      close: function() {
						    	  $( "#uploadform" ).html(""); 
						      }
						});
			},"html");
}

</script>

<script src="<c:url value='/js/ajaxRequest.js'/>" type="text/javascript" charset="utf-8"></script>
<div id="error-dialog" ></div>

<div>
<div id="errmsg"></div>
	<fieldset>
		<legend class="legendTitle">
			<span style="color:#F30;"><strong>&nbsp;陸資實地訪查&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
		<!-- 基本資料 -->
		<jsp:include page="/console/interviewone/content/sysBaseInfo.jsp" flush="true" />
		<!-- 問卷表單 -->
		<div>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;填報情形&nbsp;]</span>
			</legend>
			<c:if test="${not empty singlelist && !fn:startsWith(IOBaseInfo.cname,'(轉投資)')}">
				<div class='tbtitle'>特殊需要</div>
				<div style="width: 98%;padding-left: 15px;">
					<c:if test="${empty spNeed}">無</c:if>
					<pre>${spNeed}</pre>
				</div>
			</c:if>
			<div class='tbtitle'>訪查列表</div>
			<div style="width: 95%;padding-left: 15px;">
				<table id="example" class="display" style="width: 98%;"> 
					<thead>
						<tr>
							<th rowspan="2">序號</th>
							<th rowspan="2">調查年度</th>
							<th rowspan="2">訪視情形</th>
							<th rowspan="2">問卷情形</th>
							<th colspan="2">異常情形</th>
							<th rowspan="2">處理狀態</th>
							<th rowspan="2">追蹤情形</th>
							<th rowspan="2">管理檔案</th>
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
								<td>
									<c:if test="${bean.interviewStatus eq 0}">待訪視</c:if>
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
												<a  href="${iurl}" class="btn_class_opener">編輯</a>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${bean.interviewStatus eq 1}">已訪視</c:when>
													<c:when test="${bean.interviewStatus eq 9}">未訪視</c:when>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</c:if>
								</td>
								<td>
									<c:if test="${bean.surveyStatus eq 0}">未繳回</c:if>
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
										<a  href="${surl}" class="btn_class_opener">編輯</a>
											</c:when>
											<c:otherwise>已繳回
											</c:otherwise>
										</c:choose>
									</c:if>
								</td>
<%-- 								<td><c:if test="${fn:indexOf(errorIlist, bean.qNo)!=-1}" --%>
<%-- 										><input type="button" value="異常原因" onclick="sendAjax('error_Itv',this)"  --%>
<%-- 											class="btn_class_opener" style="font-size: 14px;"></c:if></td> --%>
<%-- 								<td><c:if test="${fn:indexOf(errorFlist, bean.qNo)!=-1}" --%>
<%-- 										><input type="button" value="異常原因" onclick="sendAjax('error_fia',this)" --%>
<%-- 											class="btn_class_opener" style="font-size: 14px;"></c:if></td> --%>
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
									<c:choose>
										<c:when test="${not empty fsMap[xqNo].following}">
											<c:set var="qurl" scope="page">
												<c:url value='/console/interviewone/showfollowingbyqno.jsp'>
													<c:param name="qNo">${bean.qNo}</c:param>
												</c:url>
											</c:set>
											<a href="${qurl}" class="btn_class_opener">${ioOpt.following[fsMap[xqNo].following]}</a>
										</c:when>
<%-- 									<c:when test="${empty fsMap[xqNo].following && (fn:indexOf(errorIlist, bean.qNo)!=-1 || fn:indexOf(errorFlist, bean.qNo)!=-1 || not empty spNeed)}"> --%>
										<c:when test="${empty fsMap[xqNo].following   &&   (errI!=-1 || errF!=-1 || not empty spNeed)}">
											<c:set var="qurl" scope="page">
												<c:url value='/console/interviewone/showfollowingbyqno.jsp'>
													<c:param name="qNo">${bean.qNo}</c:param>
												</c:url>
											</c:set>
											<a href="${qurl}" class="btn_class_opener">${ioOpt.following['']}</a>
										</c:when>
									</c:choose>
								</td>
								<td>
									<input type="button" value="管理檔案" class="uploadItem btn_class_opener" alt="${bean.qNo}_${bean.investNo}_${bean.reInvestNo}"></td>
							</tr>
						</c:forEach>
							
					</tbody>
				</table>
				</div>
			</fieldset>
		</div>
		</div>
	</fieldset>
	<div id="uploadform"></div>
</div>