<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 3, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,3,4] },
		                 {"sSortDataType":"chinese", "aTargets": [2,5,6]}
		                 ]
				   ,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$("#agentList").dataTable(y);
});
</script>
<div class='tbtitle' style="margin-bottom: 5px;">代理人資料</div>
<div style="height: 5px;"></div>
<div style="width: 98%;margin-left: 15px;">
<table style="width: 98%;font-size: 16px;" id="agentList">
	<thead>
		<tr>
			<th style="width: 8%;">序號</th>
			<th style="width: 10%;">案號</th>
			<th style="width: 27%;">國內事業</th>
			<th style="width: 12%;">核准日期</th>
			<th style="width: 15%;">文號</th>
			<th style="width: 18%;">案由</th>
			<th style="width: 10%;">代理人</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="r" items="${agentList}">	
			<tr>
				<td></td>
				<c:forEach var="item" items="${r}">
					<td>${item}</td>
				</c:forEach>	
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="height: 10px;"></div>
