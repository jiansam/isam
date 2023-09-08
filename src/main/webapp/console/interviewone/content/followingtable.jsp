<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/js/hideOptionBySpan.js'/>" type="text/javascript" charset="utf-8"></script>
<script>
$(function() {
	$(".nameTip").tooltip({ track: true});
	var y=$.extend(sdInitDataTableSetting()
			,{
			/*
		"oLanguage" : {
			"sProcessing" : "正在載入中......",
			"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
			"sZeroRecords" : "對不起，查詢不到相關資料！",
			"sEmptyTable" : "目前尚無資料！",
			"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共有_TOTAL_ 筆資料<br/>已訪視${cMap.I_1}筆；不需訪視${cMap.I_9}筆；待訪視${cMap.I_0}筆；問卷已繳回${cMap.S_1}筆；未繳回${cMap.S_0}筆；",
			"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料，已訪視0筆；不須訪視0筆；待訪視0筆；<br/>問卷已繳回0筆；未繳回0筆；",
			"sInfoFiltered" : "<br/>原始資料為 _MAX_ 筆資料",
			"sSearch" : "搜尋",
			"oPaginate" : {
				"sFirst" : "最前頁",
				"sPrevious" : "上一頁",
				"sNext" : "下一頁",
				"sLast" : "最末頁"
			}
		},*/
		"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2,3] },
		                 {"sSortDataType":"chinese", "aTargets": [4,6]},
		                 {"sSortDataType":"dom-btn", "aTargets": [5]},
		                 {"bSortable": false, "aTargets": [0]},
		                 { "sWidth": "10%", "aTargets": [ 1,2,3,5,6,7] },
		                 { "sWidth": "33%", "aTargets": [ 4 ] },
		                 { "sWidth": "7%", "aTargets": [ 0 ] }
		                 ],		                 
				   "fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdSortChinese(),sdSortBtn());
	var oTable=$("#example").dataTable(y);
	/* $("body").on('click',"#example tbody tr td:eq(2)",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		postUrlByForm('/console/showinvest.jsp',{'investNo':$('td:eq(2)',ntr).text()});
	}); */
	$("body").on('click',"#example tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		var cid=nTds.index($(this));
		if(cid===5){
			var reInvestNo=$('td:eq(1)',ntr).find("span").text();
			if(reInvestNo==="0"){
				if($('td:eq(2)',ntr).text().length!=0){
					postUrlByForm('/console/showinvest.jsp',{'investNo':$('td:eq(2)',ntr).text()});
				}
			}else{
				if(reInvestNo.length!=0){
					postUrlByForm('/console/showreinvest.jsp',{'investNo':'','reinvest':reInvestNo});
				}
			}
		}else{
			/*alert($('td:eq(3)',ntr).find("span").prop("id"))*/
			if($('td:eq(3)',ntr).find("span").prop("id").length!=0){
				postUrlByForm('/console/interviewone/showfollowingbyqno.jsp',{'qNo':$('td:eq(3)',ntr).find("span").prop("id")});
			}
		}
	});
});
</script>
<style>
  .ui-tooltip {
    font-size: 16px;    
  }
  #example .btn_class_opener,.btn_class_loaddata{
  	font-size: 15px; 
	width: 88px;
  }
</style>

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
					<th>查訪日期</th>
					<th>陸資案號</th>
					<th>統編</th>
					<th>事業名稱</th>
					<th>經營狀態</th>
					<th>處理狀態</th>
					<th>追蹤情形</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="bean" items="${imList}" varStatus="i">
					<tr>
						<td></td>
						<td>${ibfn:addSlash(bean.iDate)}<span style="display:none;">${bean.reInvestNo}</span></td>
						<td>
							<c:forTokens items="${bean.investNo}" delims="、" var="inStr">
							   ${inStr}
							</c:forTokens>
						</td>
						<td><span id="${bean.qNo}">${bean.idno}</span></td>
						<td>${ibfn:shortenTooltip(bean.cname,18,'nameTip')}</td>
						<td>
							<c:choose>
								<c:when test="${not empty bean.gap && bean.isOperated != '3'}"><input type="button" class="btn_class_loaddata" value="${opMap[bean.isOperated]}"/></c:when>
								<c:otherwise><input type="button" class="btn_class_opener" value="${opMap[bean.isOperated]}"/></c:otherwise>
							</c:choose>
						</td>
						<td>${fOpt.progress[bean.progress]}</td>
						<td>${fOpt.following[bean.following]}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
</div>
