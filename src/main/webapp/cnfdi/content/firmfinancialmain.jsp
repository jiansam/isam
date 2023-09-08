<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<%-- <script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
 --%>
 <script>
$(function() {
	$(".fShow").click(function(){
		toShowContact($(this));
	});
	$(".fPdf").click(function(){
		todownload($(this));
	});
	var y=$.extend({
		"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1,2,3,4] }],
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
	},sdInitDataTableSetting(),sdSortChinese(),sdNumberFmt());
	var oTable=$("#financial").dataTable(y);
	//$("#financial tr,#financial td").css("cursor","none");
});
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
	var serno =$item.parents("tr").find("td :eq(3) span").text();
	postUrlByForm('/cnfdi/donwloadfs.jsp',{'serno':serno,'investNo':"${sysinfo.INVESTMENT_NO}",'tt':tt});
}
function toShowContact($item){
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
	var serno =$item.parents("tr").find("td :eq(3) span").text();
		$.post( "${pageContext.request.contextPath}/cnfdi/content/newfreport.jsp",{
				'type':'show','investNo':"${sysinfo.INVESTMENT_NO}",'serno':serno,'tt':tt
			}, function(data){
			$( "#tmp" ).html(data); 
			$( "#tmp" ).dialog({
			      height:550,
			      width:1100,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      title:tt,
			      close: function( event, ui ) {
			    	  $( "#tmp" ).html(""); 
			      }
		});
		},"html");
}
</script>
<div id="tmp"></div>
<div class='tbtitle' style="margin-bottom: 5px;">財報申報情形</div>
<div style="width: 98%;padding-left: 15px;">
<table style="width: 100%;font-size: 16px;" id="financial" class="display">
	<thead>
		<tr>
			<th style="width: 15%;">序號</th>
			<th style="width: 20%;">財報年度</th>
			<th style="width: 20%;">填報日期</th>
			<th>檢視</th>
			<th>下載</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="f" items="${financial}">
			<tr>
				<td></td>
				<td>${f.reportyear}</td>
				<td>${ibfn:addSlash(f.reportdate)}</td>
				<td><input type="button" class="fShow btn_class_opener" value="檢視"><span style="display: none;">${f.serno}</span></td>
				<td><input type="button" class="fPdf btn_class_opener" value="下載"><span style="display: none;">${f.serno}</span></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="margin: 10px;"></div>