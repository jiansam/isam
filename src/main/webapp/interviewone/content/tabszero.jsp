<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	$(".cShow").click(function(){
		var serno=$(this).prop("id").replace("show","");
		toShowFollowing(serno);
	  });
	var y=$.extend({
		"aaSorting": [[0, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,2 ] },
		                 {"sSortDataType":"chinese", "aTargets": [1,3,4,5 ]},
		                 {"sWidth":"10%", "aTargets": [0,2,4,5]},
		                 {"sWidth":"22%", "aTargets": [1,3]}
		                 ] 
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$("#example").dataTable(y);
	var x=$.extend({
		"aaSorting": [[1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1 ] },
		                 {"sSortDataType":"chinese", "aTargets": [2,3 ]},
		                 {"sWidth":"10%", "aTargets": [0]}
		                 ] 
		            ,"aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
				   "iDisplayLength": 10, 
				   "fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese(),sdSortNumric());
	$("#sf").dataTable(x);
	$("body").on('click',"#example tbody tr td",function(){
  		var ntds= $(this).parent().find("td");
  			var serno=$('td:eq(0)',$(this).parent()).find("span").text();
  		if(serno.length>0){
  			toShowFollowing(serno);
  		}
 	});
	$("body").on('click',"#sf tbody tr td",function(){
  		var ntds= $(this).parent().find("td");
  		var serno=$('td:eq(1)',$(this).parent()).find("span").text();
  		if(serno.length>0){
  			toShowFollowing(serno);
  		}
 	});
});
function toShowFollowing(serno){
	$.post( "${pageContext.request.contextPath}/console/interviewone/content/following.jsp",{
		'serno':serno,'type':'show','qNo':"${IObean.qNo}"
		}, function(data){
		$( "#EditFollowing" ).html(data); 
		$( "#EditFollowing" ).dialog({
		      height:550,
		      width:800,
		      modal: true,
		      resizable: false,
		      draggable: false,
		      title:'訪查處理紀錄',
		      close: function( event, ui ) {
		    	  $( "#EditFollowing" ).html(""); 
		      }
	});
	},"html");
}
</script>
<div id="EditFollowing"></div>
<div class='tbtitle'>匯總情形</div>
<div>
<table style="width: 98%;">
	<tr>
		<td style="text-align: right;width: 12%;">查訪日期：</td>
		<td style="width: 13%;">${ibfn:addSlash(IMap.reportdate)}</td>
		<td style="text-align: right;">財務異常：</td>
<%-- 		<td>${IOBaseInfo.isFError}${IOBaseInfo.isIError}${spNeed}</td> --%>
		<td style="width: 10%;">
			<c:choose>
				<c:when test="${IObean.interviewStatus eq 1}">${IOBaseInfo.isFError}</c:when>
				<c:otherwise>
					${optionValName.interviewStatus[IObean.interviewStatus]}
				</c:otherwise>
			</c:choose>
		</td>
		<td style="text-align: right;">訪視異常：</td>
		<td style="width: 10%;">
<%-- 			<c:choose>
				<c:when test="${IObean.interviewStatus eq 1}">${IOBaseInfo.isIError}</c:when>
				<c:otherwise>
					${optionValName.interviewStatus[IObean.interviewStatus]}
				</c:otherwise>
			</c:choose> --%>
			<c:choose>
				<c:when test="${IObean.interviewStatus eq 1 ||IOBaseInfo.isIError eq '異常'}">${IOBaseInfo.isIError}</c:when>
				<c:otherwise>
					${optionValName.interviewStatus[IObean.interviewStatus]}
				</c:otherwise>
			</c:choose>
		</td>
		<c:if test="${IObean.reInvestNo eq '0'}">
		<td style="text-align: right;">特殊需要：</td>
		<td style="width: 10%;"><c:if test="${empty spNeed}">無</c:if><c:if test="${ not empty spNeed}">有</c:if></td>
		</c:if>
		<td style="text-align: right;">追蹤情形：</td>
		<td>
			<c:choose>
				<c:when test="${empty IOBaseInfo.canadd}">尚無</c:when>
				<c:otherwise>${optionValName.following[IOBaseInfo.canadd]}</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
	</tr>	 
</table>
</div>

<div class='tbtitle'>處理紀錄列表</div>
<div>
<c:if test="${not empty sflist}">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend><span style="color:#F30;">[投審會訪視紀錄]</span>&nbsp;</legend>
		<div>
		<table id="sf" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>序號</th>
					<th>查訪日期</th>
					<th>處理狀態</th>
					<th>追蹤情形</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="bean" items="${sflist}">
					<tr>
						<td></td>
						<td>${ibfn:addSlash(bean.receiveDate)}<span style="display: none;">${bean.serno}</span></td>
						<td>${optionValName.progress[bean.optionValue]}</td>
						<td>${optionValName.following[bean.following]}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
</c:if>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend><span style="color:#F30;">[收文處理紀錄]</span>&nbsp;</legend>
		<div>
<table id="example" class="display" style="width: 98%;"> 
	<thead>
		<tr>
			<th>收文日期</th>
			<th>收文字號</th>
			<th>發文日期</th>
			<th>發文文號</th>
			<th>處理狀態</th>
			<th>追蹤情形</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="bean" items="${followinglist}">
			<tr>
				<td>${ibfn:addSlash(bean.receiveDate)}<span style="display: none;">${bean.serno}</span></td>
				<td>${bean.receiveNo}</td>
				<td>${ibfn:addSlash(bean.issueDate)}</td>
				<td>${bean.issueNo}</td>
				<td>${optionValName.progress[bean.optionValue]}</td>
				<td>${optionValName.following[bean.following]}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
</fieldset>
</div>
