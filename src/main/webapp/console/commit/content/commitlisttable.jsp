<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	var oTable=$("#example").dataTable({
		"bAutoWidth" : false, //自適應寬度
		/* "bJQueryUI": true, */
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
		    { "aTargets": [0],"bSortable": false }
		],  
		"aaSorting": [[ 1, 'asc' ]],
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
		"aoColumns":[
			{"sWidth":"10%", "bSortable": false,'sClass':'center'},
			{"sWidth":"15%"},
			{"sSortDataType":"chinese","sWidth":"35%"},
			{"sWidth":"10%"},
			{"sWidth":"10%"}
		]
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
});
$(function() {
	//監測tr click
	$("body").on('click',"#example tbody tr",function(){
		var nTds=$('td',this);
		var idno=$(nTds[1]).text();
		var ary={"serno":idno};
 		var url="/console/commit/showcommitdetail.jsp";
 		postUrlByForm(url,ary);
	});
});
</script>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;結果列表&nbsp;]</span>&nbsp;&nbsp;
				</legend>
				<div>
					<table id="example" class="display" style="width: 98%;"> 
						<thead>
							<tr>
								<th>No</th>
								<th>統編</th>
								<th>投資人名稱</th>
								<th>修改人</th>
								<th>更新日</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty commitList}">
								<c:forEach var="proj" items="${commitList}">
									<tr>
										<td></td>
										<td>${proj[0]}</td>
										<td>${proj[1]}</td>
										<td>${proj[2]}</td>
										<td>${proj[3]}</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</fieldset>
		</div>