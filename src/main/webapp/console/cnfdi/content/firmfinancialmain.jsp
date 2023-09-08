<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<%-- <script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/> --%>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1,2,3,4,5] },
		                 { "bSortable": false, "aTargets": [ 0,3,4,5 ] }],
		                 "bFilter": false, "bInfo": false,"bPaginate": false
				   ,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting());
	var oTable=$("#financial").dataTable(y);
	if("${fn:contains(memberUrls,'E0401')}"==="false"){
		oTable.fnSetColumnVis( 3, false); 
		//oTable.fnSetColumnVis( 4, false); 
	}
	$(".fDel").click(function(){
		var $item =$(this);
		$( "<div>確定刪除本筆資料？</div>" ).dialog({
		      resizable: false,
		      height:200,
		      modal: true,
		      buttons: {
		      "確定": function() {
		         deleteContact($item);
		          $( this ).dialog( "close" );
		        },
		       "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
	});
	$("#addF").click(function(){
		toAddContact();
	});
	$(".fEdit").click(function(){
		toEditContact($(this));
	});
	$(".fShow").click(function(){
		toShowContact($(this));
	});
	$(".fPdf").click(function(){
		todownload($(this))
	});
	function toShowContact($item){
		var reportyear =$item.parents("tr").find("td :eq(1)").text();
		var reportdate =$item.parents("tr").find("td :eq(2)").text();
		var serno =$item.parents("tr").find("td :eq(3) span").text();
			$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newfreport.jsp",{
					'type':'show','investNo':"${sysinfo.INVESTMENT_NO}",'serno':serno
				}, function(data){
				$( "#tmp" ).html(data); 
				$( "#tmp" ).dialog({
				      height:600,
				      width:980,
				      modal: true,
				      resizable: false,
				      draggable: false,
				      title:'檢視財報',
				      close: function( event, ui ) {
				    	  $( "#tmp" ).html(""); 
				      }
			});
			},"html");
	}
	function toEditContact($item){
		var reportyear =$item.parents("tr").find("td :eq(1)").text();
		var reportdate =$item.parents("tr").find("td :eq(2)").text();
		var serno =$item.parents("tr").find("td :eq(3) span").text();
			$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newfreport.jsp",{
					'type':'edit','investNo':"${sysinfo.INVESTMENT_NO}",'serno':serno
				}, function(data){
				$( "#tmp" ).html(data); 
				$( "#tmp" ).dialog({
				      height:600,
				      width:980,
				      modal: true,
				      resizable: false,
				      draggable: false,
				      title:'編輯財報',
				      close: function( event, ui ) {
				    	  $( "#tmp" ).html(""); 
				      }
			});
			},"html");
	}
	function deleteContact($item){
		var serno =$item.next("span").text();
		postUrlByForm('/console/cnfdi/updatefinancial.jsp',{'serno':serno,'type':'delete','investNo':"${sysinfo.INVESTMENT_NO}"});
	}
	function todownload($item){
		var x="";
		if("${opbean.isNew}"==="2"){
			x="新設";
		}
		if('${ibfn:addSlash(opbean.setupdate)}'.length>0){
			x+="${ibfn:addSlash(opbean.setupdate)}";
		}
		if(x.length>0){
			x="("+x+"成立)";
		}
		var repYear =$item.parents("tr").find("td :eq(1)").text();
		var tt="${sysinfo.COMP_CHTNAME}"+repYear+'年度財務資訊簡表'+x;
		var serno =$item.next("span").text();
		postUrlByForm('/cnfdi/donwloadfs.jsp',{'serno':serno,'investNo':"${sysinfo.INVESTMENT_NO}",'tt':tt});
	}
	function toAddContact(){
			$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newfreport.jsp",{
					'type':'add','investNo':"${sysinfo.INVESTMENT_NO}"
				}, function(data){
				$( "#tmp" ).html(data); 
				$( "#tmp" ).dialog({
				      height:600,
				      width:980,
				      modal: true,
				      resizable: false,
				      draggable: false,
				      title:'新增財報',
				      close: function( event, ui ) {
				    	  $( "#tmp" ).html(""); 
				      }
			});
			},"html");
	}
});
</script>
<div id="tmp"></div>
<div style="text-align: right;"><input type="button" class="btn_class_opener" id="addF" value="新增填報紀錄"></div>
<div class='tbtitle' style="margin-bottom: 5px;">財報申報情形</div>
<div style="height: 5px;"></div>
<div style="width: 98%;padding-left: 15px;">
<table style="width: 100%;font-size: 16px;" id="financial">
	<thead>
		<tr>
			<th>序號</th>
			<th>財報年度</th>
			<th>填報日期</th>
			<th>刪除</th>
			<th>
			<c:choose>
				<c:when test="${fn:contains(memberUrls,'E0401')}">編輯</c:when>
				<c:otherwise>檢視</c:otherwise>
			</c:choose>
			</th>
			<th>列印</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="f" items="${financial}">
			<tr>
				<td></td>
				<td>${f.reportyear}</td>
				<td>${ibfn:addSlash(f.reportdate)}</td>
				<td><c:if test="${fn:contains(memberUrls,'E0401')}"><input type="button" class="fDel btn_class_opener" value="刪除"><span style="display: none;">${f.serno}</span></c:if></td>
				<td>
					<c:choose>
						<c:when test="${fn:contains(memberUrls,'E0401')}"><input type="button" class="fEdit btn_class_opener" value="編輯"><span style="display: none;">${f.note}</span></c:when>
						<c:otherwise><input type="button" class="fShow btn_class_opener" value="檢視"><span style="display: none;">${f.serno}</span></c:otherwise>
					</c:choose>
				</td>
				<td>
					<input type="button" class="fPdf btn_class_opener" value="列印"><span style="display: none;">${f.serno}</span>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<!-- <div style="margin: 10px;"></div> -->