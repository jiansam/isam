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
	$("#example").dataTable(x);
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
			<span style="color:#F30;">[&nbsp;結果顯示&nbsp;]</span><strong style="color:#222;">&nbsp;檢視符合條件案件列表&nbsp;</strong>&nbsp;
		</legend>
		<div>
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>NO</th>
					<th>投資人名稱</th>
					<th>統編</th>
					<th>案號</th>
					<th>大陸事業名稱</th>
					<th>核准資料</th>
				<c:if test="${fn:contains(memberUrls,'R0101')}">
					<th>列管</th>
					<th>專案</th>
				</c:if>
				<c:if test="${fn:contains(memberUrls,'R0102')}">
					<th>承諾</th>
				</c:if>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty ApprovalList}">
				<c:forEach var="approval" items="${ApprovalList}">
					<tr>
						<td></td>
						<td>${approval.investor}</td>
						<td>${approval.idno}</td>
						<td>${approval.investNo}</td>
						<td>${approval.cnName}</td>
						<td>
							<c:if test="${fn:startsWith(approval.investNo,'6')}">
							<img src='<c:url value="/images/pdf.png"/>' class="info" style="cursor: pointer;">
							</c:if>
						</td>
<%-- 						<td><img src='<c:url value="/images/info.png"/>' onclick="postUrlByForm('/approval/downloadExcel.jsp',{'idno':'${approval.idno}','investNo':'${approval.investNo}'});"></td> --%>
					<c:if test="${fn:contains(memberUrls,'R0101')}">
						<td>${approval.stateName}</td>
						<td>
							<c:choose>
							<c:when test="${approval.state != '' && approval.pc!=0}">
								<img src='<c:url value="/images/check.png"/>' style="cursor: pointer;" class="proj" alt="">
							</c:when>
							<c:when test="${approval.state != ''}">
								<img src='<c:url value="/images/checkB.png"/>' alt="">
							</c:when>
							</c:choose>
						</td>
					</c:if>
					<c:if test="${fn:contains(memberUrls,'R0102')}">
						<td>
							<c:choose>
								<c:when test="${approval.isC != '' && approval.cc!=0}">
									<img src='<c:url value="/images/check.png"/>' style="cursor: pointer;" class="agree" alt="">
								</c:when>
								<c:when test="${approval.isC != ''}">
									<img src='<c:url value="/images/checkB.png"/>' alt="">
								</c:when>
							</c:choose>
						</td>
					</c:if>
					</tr>
				</c:forEach>
				</c:if>
			</tbody>
		</table>
		</div>
	</fieldset>
	<div id="forseason"></div>
</div>
