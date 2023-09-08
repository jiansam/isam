<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	var oTable=$("#resultTable").dataTable({
		"bAutoWidth" : false, //自適應寬度
		"aoColumnDefs": [
		    { "bSortable": false,'sClass':'center', "aTargets": [ 0 ] }
		],
		"aaSorting": [[ 2, 'asc' ],[ 1, 'asc' ]],
		//"bFilter": false,
		//多語言配置
		"oLanguage" : {
			"sProcessing" : "正在載入中......",
			"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
			"sZeroRecords" : "對不起，查詢不到相關資料！",
			"sEmptyTable" : "本分類目前尚無資料！",
			"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共有_TOTAL_ 筆資料<br/>大陸事業，共${projTerms.countCN}筆；投資人，共${projTerms.countIDNO}筆；總件數為_MAX_筆",
			"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料，大陸事業，共${projTerms.countCN}筆；投資人，共${projTerms.countIDNO}筆；總件數為_MAX_筆",
			"sInfoFiltered" : "",
			"sSearch" : "搜尋",
			"oPaginate" : {
				"sFirst" : "最前頁",
				"sPrevious" : "上一頁",
				"sNext" : "下一頁",
				"sLast" : "最末頁"
			}
		},
		"aoColumns":[
			{"sWidth":"7%"},
			{"sSortDataType":"chinese","sWidth":"26%"},
			{"sWidth":"8%"},
			{"sSortDataType":"chinese","sWidth":"31%"},
			{"sSortDataType":"chinese","sWidth":"10%"},
			{"sSortDataType":"chinese","sWidth":"9%"},
			{"sWidth":"9%"}
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
	$(".myChange").click(function(){
		getStateConfirmStr($(this));		
	});

	$("body").on('click',"#resultTable tbody tr td:not(td:first-child)",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		var url="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/console/project/showprojectdetail.jsp?";
		url+="serno="+$(nTds[0]).find(":input").val();
		window.location=url;
	});	
});
function getStateConfirmStr($myState){
	var changeTo = $(".myChange").index($myState)+1;
	var oTable=$("#resultTable").dataTable();
	var nNodes = oTable.fnGetNodes();
	var sData = $("input[name='no']", nNodes).serialize();
	var titlestate=$myState.text();
	if(sData.length>0){
		var forConfirm="<h3>確定將下列管制狀態更改為<"+titlestate+"></h3><h5>即使在此未勾選全部投資人，同專案之管制項目狀態也將連動修改</h5><table class='formProj'>";
		forConfirm+="<tr><th>投資人</th><th>案號</th><th>大陸事業名稱</th><th>狀態</th></tr>";
		$("input[name='no']:checked", nNodes).each(function(){
			var ntr=$(this).parents('tr')[0];
			var aData=oTable.fnGetData(ntr);
			forConfirm+="<tr><td>"+aData[1]+"</td><td>"+aData[2]+"</td><td>"+aData[3]+"</td><td>"+aData[4]+"</td></tr>"; 
		});
		forConfirm+="</table>";
		$("<div style='font-size='14px;''>"+forConfirm+"</div>").dialog({
			width: 950,
			modal:true,
			title:'確認修改狀態',
			buttons: {
		        "確定修改": function() {
		          var url ="/console/project/changestate.jsp?changeTo=0"+changeTo+"&"+sData;
		    	  window.location="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"+url;
		          $( this ).dialog( "close" );
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
			}
		});
	}else{
		alert("請至少選擇一筆專案，方能進行專案狀態修改");
	}
}
$(function() {
	$("#changeState").hide();
	$("#changebtn").button({ icons:{primary: "ui-icon-gear",secondary:"ui-icon-triangle-1-s"}})
			.click(function(){
			var menu = $("#changeState").show().position({
				            my: "left top",
				            at: "left bottom",
				            of: this
				          });
			$( document ).one( "click", function() {
	            menu.hide();
	        });
	        return false;
	});
	$("#changeState li").hover(function(){
		$(this).toggleClass("active");
	});
});
</script>
		<div id="projConfirm"></div>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;margin-right: 20px;">[&nbsp;結果列表&nbsp;]</span>&nbsp;&nbsp;
					<c:if test="${fn:contains(memberUrls,'E0101')}">
					<span id="changeDiv" style="float:right;margin-right:10px;">
						<button id="changebtn" style="font-size: 14px;">點此修改列管狀態</button>
						<ul id="changeState" >
						    <li><div class="myChange"><span class="triangle"></span>列管</div></li>
						    <li><div class="myChange"><span class="triangle"></span>解除季報</div></li>
						    <li><div class="myChange"><span class="triangle"></span>待確認</div></li>
						    <li><div class="myChange"><span class="triangle"></span>解除列管</div></li>
						</ul>
					</span>
					</c:if>
				</legend>
				<div style="color:#F30;text-align: center;">${projTerms.changeDone}</div>
				<div>
					<table id="resultTable" class="display" style="width: 98%;"> 
						<thead>
							<tr>
								<th>選擇</th>
								<th>投資人</th>
								<th>案號</th>
								<th>大陸事業名稱</th>
								<th>狀態</th>
								<th>修改人</th>
								<th>更新日</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty projList}">
								<c:forEach var="proj" items="${projList}">
									<tr>
										<td><input type="checkbox" name="no" value="${proj[0]}"/></td>
										<td>${proj[1]}</td>
										<td>${proj[2]}</td>
										<td>${proj[3]}</td>
										<td>${proj[4]}</td>
										<td>${proj[5]}</td>
										<td>${proj[6]}</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</fieldset>
		</div>