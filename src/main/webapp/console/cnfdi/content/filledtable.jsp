<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 2, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2] },
		                 {"sSortDataType":"chinese", "aTargets": [1,3,4,5]}
		                 /* ,{"sSortDataType":"dom-btn", "aTargets": [3,4,5]} */],
		           "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
				   "iDisplayLength": 10,"sPaginationType":"full_numbers",
				   
				   "aoColumns": [
				                 { sWidth: '8%' },
				                 { sWidth: '10%' },
				                 { sWidth: '13%' },
				                 { sWidth: '23%' },
				                 { sWidth: '23%' },
				                 { sWidth: '23%' } ]

				   ,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese(),sdSortBtn());
// 	$(".editIN").click(function(){
// 		var investNo=$(this).prop("id").replace("investNo","");
//  		postUrlByForm('/console/showinvest.jsp',{'investNo':investNo,"type":"isFilled"});
// 	});
// 	$(".editIO").click(function(){
// 		var investorSeq=$(this).prop("id").replace("investor","");
//  		postUrlByForm('/console/showinvestform.jsp',{'investorSeq':investorSeq,"type":"isFilled"});
// 	});
// 	$(".editRI").click(function(){
// 		var reinvest=$(this).prop("id").replace("reinvest","");
//  		postUrlByForm('/console/showreinvest.jsp',{'reinvest':reinvest,"type":"isFilled"});
// 	});
	$(".mytip").tooltip();
	var oTable=$("#example").dataTable(y);
});
</script>
<style>
.dataTables_paginate a{
	font-size: 14px;
}
</style>
<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;結果顯示&nbsp;]</span><strong style="color:#222;">&nbsp;檢視符合條件列表&nbsp;</strong>&nbsp;
		</legend>
		<div>
		<div style="width: 98%;">
		<table id="example" class="display" style="width: 100%;"> 
			<thead>
				<tr>
					<th style="width: 8%;">序號</th>
					<th>承辦人</th>
					<th>文號</th>
					<th>國內事業</th>
					<th>投資人</th>
					<th>轉投資事業</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${filledlist}">
					<tr>
						<td></td>
						<td>${item.pname}</td>
						<td>${item.receiveNo}</td>
						<td>
						<c:if test="${not empty item.iNo}">
							<c:url value="/console/showinvest.jsp" var="editIN">
								<c:param name="type">isFilled</c:param>
								<c:param name="investNo">${item.iNo}</c:param>
							</c:url>
							<a class="mytip" href="${editIN}" title="${item.iName}" target="_blank">
							${fn:substring(item.iName,0,10)}<c:if test="${fn:length(item.iName)>10}">...</c:if>
							</a>
							<%-- <input class="btn_class_opener editIN" id="investNo${item.iNo}" type="button" value="編輯"> --%>
						</c:if>
						</td>
						<td>
						<c:if test="${not empty item.inNo}">
							<c:url value="/console/showinvestform.jsp" var="editIN">
								<c:param name="type">isFilled</c:param>
								<c:param name="investorSeq">${item.inNo}</c:param>
							</c:url>
							<a class="mytip" href="${editIN}" title="${item.inName}" target="_blank">
								${fn:substring(item.inName,0,10)}<c:if test="${fn:length(item.inName)>10}">...</c:if>
							</a>
							<%-- <input class="btn_class_opener editIO" id="investor${item.investor}" type="button" value="編輯"> --%>
						</c:if>
						</td>
						<td>
						<c:if test="${not empty item.reNo}">
							<c:url value="/console/showreinvest.jsp" var="editIN">
								<c:param name="type">isFilled</c:param>
								<c:param name="reinvest">${item.reNo}</c:param>
								<c:param name="investNo">${item.iNo}</c:param>
							</c:url>
							<a class="mytip" href="${editIN}" title="${item.reName}" target="_blank">
								${fn:substring(item.reName,0,10)}<c:if test="${fn:length(item.reName)>10}">...</c:if>
							</a>
							<%-- <input class="btn_class_opener editRI" id="reinvest${item.reinvest}" type="button" value="編輯"> --%>
						</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		</div>
	</fieldset>
</div>
