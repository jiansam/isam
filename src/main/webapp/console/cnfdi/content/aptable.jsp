<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "desc" ],[2,"asc"]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2,3,5,6 ] },
		                 {"sSortDataType":"chinese", "aTargets": [ 4,5,6 ]}],
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
	var oTable=$("#example").dataTable(y);
	$("body").on('click',"#example tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
 		postUrlByForm('/console/showinvest.jsp',{'investNo':$('td:eq(2)',ntr).text()});
	});
// 	$("body").on('click',"#example tbody tr td:last-child",function(){
// 		var ntr= $(this).parent();
// 		var nTds=$('td',ntr);
// 		postUrlByForm('/console/interviewone/listbyqno.jsp',{'investNo':$('td:eq(1)',ntr).text()});
// 	});
});
</script>

<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;結果顯示&nbsp;]</span><strong style="color:#222;">&nbsp;檢視符合條件列表&nbsp;</strong>&nbsp;
		</legend>
		<div>
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>序號</th>
					<th>年度</th>
					<th>案號</th>
					<th>統編</th>
					<th>公司名稱</th>
					<th>狀態</th>
					<th>陸資事業</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="list" items="${ofilist}">
					<tr>
						<td></td>
						<td>${list.year}</td>
						<td>${list.investNo}</td>
						<td>${list.idno}</td>
						<td>${list.company}</td>
						<td>${optmap.isFilled[list.isFilled]}</td>
						<td>${optmap.isCNFDI[list.isCNFDI]}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
</div>
