<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend(sdInitDataTableSetting(),{
		"oLanguage" : {
			"sProcessing" : "正在載入中......",
			"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
			"sZeroRecords" : "對不起，查詢不到相關資料！",
			"sEmptyTable" : "目前尚無資料！",
			"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共 _TOTAL_筆;已填報${terms.totalCount-terms.noCount}筆，待填報${terms.noCount}筆，訪視清單共有${terms.totalCount}筆<br/>已訪視${cMap.I_1}筆；未訪視${cMap.I_9}筆；待訪視${cMap.I_0+terms.noCount}筆；問卷已繳回${cMap.S_1}筆；未繳回${cMap.S_0+terms.noCount}筆",
			"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料，已訪視0筆；不須訪視0筆；待訪視0筆；<br/>問卷已繳回0筆；未繳回0筆；",
			"sInfoFiltered" : "",
			"sSearch" : "搜尋",
			"oPaginate" : {
				"sFirst" : "最前頁",
				"sPrevious" : "上一頁",
				"sNext" : "下一頁",
				"sLast" : "最末頁"
			}
		},
		"aaSorting": [[0, "desc" ],[1, "asc"]],
		"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2,5,6 ] },
		                 {"sSortDataType":"chinese", "aTargets": [4,5,6]},
		                 {"sSortDataType":"formatted", "aTargets": [0,1,2]},		                 
// 		                 {"bSortable": false, "aTargets": [0]}
		                 ],
		"aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
		"iDisplayLength": 10,
// 				   "fnDrawCallback": function ( oSettings ) {
// 						/* Need to redo the counters if filtered or sorted */
// 						if ( oSettings.bSorted || oSettings.bFiltered ){
// 							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
// 								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
// 							}
// 						}
// 					}
	},sdSortChinese(),sdSortNumric());
	var oTable=$("#example").dataTable(y);
	$("body").on('click',"#example tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		postUrlByForm('/interviewone/listbyqno.jsp',{'qNo':$('td:eq(1)',ntr).find("span").prop("id"),'reInvestNo':$('td:eq(2)',ntr).find("span").text()});
	});
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
<!-- 					<th>序號</th> -->
					<th>年度</th>
					<th>陸資案號</th>
					<th>統編</th>
					<th>事業名稱/轉投資事業名稱</th>
					<th>行業別</th>
					<th>訪視狀態</th>
					<th>問卷狀態</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="bean" items="${Interviewone}">
					<tr>
<!-- 						<td></td> -->
						<td>${bean.year}</td>
						<td><span id="${bean.qNo}">${bean.investNo}</span></td>
						<td>${bean.reInvestNo eq 0?cninfo[bean.investNo].idno : reInvestBase[bean.reInvestNo].idno}<span style="display: none;">${bean.reInvestNo}</span></td>
						<td>${bean.reInvestNo eq 0?cninfo[bean.investNo].cname : reInvestBase[bean.reInvestNo].cname}</td>
						<td>${fn:replace(bean.businessIncomeTaxCode, "；", "<br/>")}</td>
						<td>${iStatus[bean.interviewStatus]}</td>
						<td>${sStatus[bean.surveyStatus]}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
</div>
