<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,3,4,5 ] },
		                 {"sSortDataType":"chinese", "aTargets": [1,2]},
		                 { "bSortable": false, "aTargets": [ 0,3,4,5 ] }
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
	if("${fn:contains(memberUrls,'E0401')}"==="false"){
		oTable.fnSetColumnVis( 4, false); 
		oTable.fnSetColumnVis( 5, false); 
	}
	$(".fDel").click(function(){
		var serno =$(this).prop("alt");
		$( "<div>確定刪除本筆資料？</div>" ).dialog({
		      resizable: false,
		      height:200,
		      modal: true,
		      title:'即將刪除',
		      buttons: {
		      "確定": function() {
		    	  deleteSFile(serno);
		          $( this ).dialog( "close" );
		        },
		       "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
	});
	$("#addP").click(function(){
		toAddSFile();
	});
	$(".fEdit").click(function(){
		toEditSFile($(this));
	});
	$(".fDown").click(function(){
		toDownloadSFile($(this));
	});
});
function toDownloadSFile($item){
	var serno =$item.prop("alt");
	postUrlByForm('/downloadStructrual.jsp',{'serno':serno});
}
function toEditSFile($item){
	var serno =$item.prop("alt");
	var title =$item.parents("tr").find("td :eq(1)").text();
		$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newstructural.jsp",{
				'type':'edit','investorSeq':"${ofiiobean.investorSeq}","serno":serno,"title":title
			}, function(data){
			$( "#upfile" ).html(data); 
			$( "#upfile" ).dialog({
			      height:250,
			      width:800,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      title:'編輯架構圖',
			      close: function( event, ui ) {
			    	  $( "#upfile" ).html(""); 
			      }
		});
		},"html");
}
function deleteSFile(serno){
	postUrlByForm('/console/cnfdi/uploadStructural.jsp',{'serno':serno,'type':'del','investorSeq':"${ofiiobean.investorSeq}"});
}
function toAddSFile(){
		$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newstructural.jsp",{
				'type':'add','investorSeq':"${ofiiobean.investorSeq}"
			}, function(data){
			$( "#upfile" ).html(data); 
			$( "#upfile" ).dialog({
			      height:250,
			      width:800,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      title:'新增架構圖',
			      close: function( event, ui ) {
			    	  $( "#upfile" ).html(""); 
			      }
		});
		},"html");
}
</script>
<div id="upfile"></div>
<div style="text-align: right;"><input type="button" class="btn_class_opener" id="addP" value="新增架構圖"></div>
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
			<th style="width: 10%;">刪除</th>
			<th style="width: 10%;">編輯</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="r" items="${ofifiles}">	
			<tr>
				<td></td>
				<td>${r.title}</td>
				<td>${ibfn:toTWDateStr(r.createtime)}</td>
				<td><input type="button" class="fDown btn_class_opener" title="${r.fName}" alt="${r.fNo}" value="下載"></td>
				<td>
					<c:if test="${fn:contains(memberUrls,'E0401')}">
						<input type="button" class="fDel btn_class_opener" alt="${r.fNo}" value="刪除">
					</c:if>
				</td>
				<td>
					<c:if test="${fn:contains(memberUrls,'E0401')}">
						<input type="button" class="fEdit btn_class_opener" alt="${r.fNo}" value="編輯">
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="height: 10px;"></div>
