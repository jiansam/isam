<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,3 ] },
		                 {"sSortDataType":"chinese", "aTargets": [1,2]},
		                 { "bSortable": false, "aTargets": [ 0,3 ] }
		                 ],
		                 "bFilter": false, "bInfo": false,
		                 "bPaginate": false
				   ,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$("#strctural").dataTable(y);
	$(".fDown").click(function(){
		toDownloadSFile($(this));
	});
});
function toDownloadSFile($item){
	var serno =$item.prop("alt");
	postUrlByForm('/downloadStructrual.jsp',{'serno':serno});
}
</script>
<div class='tbtitle' style="margin-bottom: 5px;">母公司或關連企業及受益人架構圖</div>
<div style="height: 5px;"></div>
<div style="width: 98%;margin-left: 15px;">
<table style="width: 98%;font-size: 16px;" id="strctural">
	<thead>
		<tr>
			<th style="width: 10%;">序號</th>
			<th style="width: 45%;">標題</th>
			<th style="width: 10%;">建立日期</th>
			<th style="width: 10%;">下載</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="r" items="${ofifiles}">	
			<tr>
				<td></td>
				<td>${r.title}</td>
				<td>${ibfn:toTWDateStr(r.createtime)}</td>
				<td><input type="button" class="fDown btn_class_opener" title="${r.fName}" alt="${r.fNo}" value="下載"></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="height: 10px;"></div>
