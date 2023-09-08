<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<link href="<c:url value='/media/css/TableTools.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>
<script src="<c:url value='/media/js/TableTools.min.js'/>" language="javascript" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/media/js/ZeroClipboard.js'/>" language="javascript" type="text/javascript" charset="utf-8"></script>
<script>
/*核准資料的datatable*/
function getOptionTable(aoColumnsary,notbSortable){
	var option={
			"bAutoWidth" : false, //自適應寬度
			/*"bJQueryUI": true,*/
			"aLengthMenu": [[5, 10, 20], [5, 10, 20]],
			"iDisplayLength": 5,
			//使用fnDrawCallback、aoColumnDefs和aaSorting設定直接給予第一個<td>動態序號
			"fnDrawCallback": function ( oSettings ) {
				/* Need to redo the counters if filtered or sorted */
				if ( oSettings.bSorted || oSettings.bFiltered ){
					for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
						$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
					}
				}
			},
			"aoColumnDefs": [
			    { "bSortable": false,'sClass':'center', "aTargets": notbSortable }
			    
			],
			"aaSorting": [[ 3, 'asc' ],[1, 'asc']],
			/* "sPaginationType" : "full_numbers", */  //顯示全部的分頁器
			//多語言配置
			"oLanguage" : {
				"sProcessing" : "正在載入中......",
				"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
				"sZeroRecords" : "對不起，查詢不到相關資料！",
				"sEmptyTable" : "本分類目前尚無資料！",
				"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共有_TOTAL_ 筆資料",
				"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料",
				"sInfoFiltered" : "<br/>原始資料為 _MAX_ 筆資料",
				"sSearch" : "搜尋",
				"oPaginate" : {
					"sFirst" : "最前頁",
					"sPrevious" : "上一頁",
					"sNext" : "下一頁",
					"sLast" : "最末頁"
				}
			},
			"aoColumns":aoColumnsary			
		};
	return option;
}
function getAoColumnsary(){
	var aoColumnsary=[
		{"sWidth":"3%"},
		{"sSortDataType":"chinese","sWidth":"22%"},
		{"sWidth":"6%"},
		{"sWidth":"6%"},
		{"sSortDataType":"chinese","sWidth":"27%"},
		{"sWidth":"8%"}
	];
	if("${fn:contains(memberUrls,'R0101')}"==="true"){
		aoColumnsary.push({"sSortDataType":"chinese","sWidth":"10%"});
		aoColumnsary.push({"sWidth":"6%"});
	}
	if("${fn:contains(memberUrls,'R0102')}"==="true"){
		aoColumnsary.push({"sWidth":"6%"});
	}	
	return aoColumnsary;
}
function getNotbSortable(){
	var notbSortable=[0,5];
	if("${fn:contains(memberUrls,'R0101')}"==="true"){
		notbSortable.push(7);
		if("${fn:contains(memberUrls,'R0102')}"==="true"){
			notbSortable.push(8);
		}
	}else{
		notbSortable.push(6);
	}
	return notbSortable;
}
$(function() {
	var aoColumnsary=getAoColumnsary();
	var notbSortable=getNotbSortable();
	var x=getOptionTable(aoColumnsary,notbSortable);
	$("#example").dataTable();
	$(".clearAll").click(function(){
		 var nNodes = $("#example").dataTable().fnGetNodes();
		 $('input:checkbox', nNodes).each( function() {
			 $(this).attr("checked",false);
		 });
	});
});

jQuery.fn.dataTableExt.oSort['chinese-asc']  = function(x,y) {
    return x.localeCompare(y);    };     
jQuery.fn.dataTableExt.oSort['chinese-desc']  = function(x,y) { 
   return y.localeCompare(x);    };
jQuery.fn.dataTableExt.aTypes.push(function(sData) {
        var reg =/^[\u2e80-\u9fff]{0,}$/;
        if(reg.test(sData)){
            return 'chinese';
        }
        return null;
});

</script>
<script type="text/javascript" src="<c:url value='/js/approval.js'/>"></script>
<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;結果顯示&nbsp;]</span><strong style="color:#222;">&nbsp;檢視符合條件列表&nbsp;</strong>&nbsp;
		</legend>
		<div>
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>選擇</th>
					<th>訪視年度</th>
					<th>陸資案號</th>
					<th>公司名稱</th>
					<th>統一編號</th>
					<th>訪視紀錄表</th>
					<th>問卷</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="bean" items="${Interviewone}">
					<tr>
					<td></td>
					<td>${bean.year}<span style="display: none;">${bean.year}</span></td>
						<td>${bean.investNo}<span style="display: none;">${bean.investNo}</span></td>
						
						<td>${bean.companyName}<span style="display: none;">${bean.companyName}</span></td>
							<td>${bean.banNo}<span style="display: none;">${bean.banNo}</span></td>
							
								
									<td>
									
								<c:if test="${not empty bean.interviewFNo}">
										<a href="<c:url value="/getinterviewonefiles.jsp" ><c:param name="fNo">${bean.interviewFNo}</c:param>	</c:url>" >
										<img title="下載檔案" src="${pageContext.request.contextPath}/images/download.png" width="22" ></a>
										
											</c:if>
									</td>
							
								
								
								
							<td>
							
							<c:if test="${not empty bean.surveyFNo}">
										<a href="<c:url value="/getinterviewonefiles.jsp" ><c:param name="fNo">${bean.surveyFNo}</c:param>	</c:url>" >
										<img title="下載檔案" src="${pageContext.request.contextPath}/images/download.png" width="22" ></a>
										
											</c:if>
						
								</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
</div>
