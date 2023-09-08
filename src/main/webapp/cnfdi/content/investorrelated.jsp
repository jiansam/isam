<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>"  type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,2,3 ] },
		                 {"sSortDataType":"chinese", "aTargets": [ 1]}
		                 ],
		           "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
				   "iDisplayLength": 10
				   ,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$(".dbtable").dataTable(y);
	$(".dbtable tr,.dbtable td").css("cursor","none");
});
</script>

<div class='tbtitle' style="margin-bottom: 5px;">母公司或關連企業及受益人資訊</div>
<div style="height: 5px;"></div>
<div style="width: 98%;margin-left: 15px;">
<table style="width: 98%;font-size: 16px;" class="dbtable">
	<thead>
		<tr>
			<th>序號</th>
			<th>名稱</th>
			<th>國別</th>
			<th>建立日期</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="r" items="${relateds}">	
			<tr>
				<td></td>
				<td>${r.relatedname}</td>
				<td>${optmap.nation[r.nation]}${optmap.cnCode[r.cnCode]}</td>
				<td>${ibfn:toTWDateStr(r.createtime)}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="height: 50px;"></div>
<div class='tbtitle'>備註</div>
<div style="width: 98%;margin-left: 15px;">
<pre>${ofiiobean.note}</pre>
</div>