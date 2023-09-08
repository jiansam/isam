<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({ "bLengthChange": false,
		"aaSorting": [[ 1, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1,3,4] },
		                 {"sSortDataType":"chinese", "aTargets": [ 1]},
		                 { "bSortable": false, "aTargets": [ 0,3,4 ] }],"bFilter": false, "bInfo": false,
		                 "bPaginate": false,
						"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese(),sdNumberFmt());
	var oTable=$("#contacts").dataTable(y);
	if("${fn:contains(memberUrls,'E0401')}"==="false"){
		oTable.fnSetColumnVis( 3, false); 
		oTable.fnSetColumnVis( 4, false); 
	}
	$(".cDel").click(function(){
		var serno =$(this).prop("id").replace("delete","");
		$( "<div>確定刪除本筆資料？</div>" ).dialog({
		      resizable: false,
		      height:200,
		      modal: true,
		      buttons: {
		      "確定": function() {
		         deleteContact(serno);
		          $( this ).dialog( "close" );
		        },
		       "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
	});
	$("#addC").click(function(){
		toAddContact();
	});
	$(".cEdit").click(function(){
		toEditContact($(this));
	});
	function toEditContact($item){
		var serno =$item.prop("id").replace("edit","");
		var name =$item.parents("tr").find("td :eq(1)").text();
		var telNo =$item.parents("tr").find("td :eq(2)").text();
			$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newcontact.jsp",{
					'type':'edit','investNo':"${sysinfo.INVESTMENT_NO}","name":name,'telNo':telNo,'serno':serno
				}, function(data){
				$( "#errmsg" ).html(data); 
				$( "#errmsg" ).dialog({
				      height:250,
				      width:800,
				      modal: true,
				      resizable: false,
				      draggable: false,
				      title:'編輯聯絡人',
				      close: function( event, ui ) {
				    	  $( "#errmsg" ).html(""); 
				      }
			});
			},"html");
	}
	function deleteContact(serno){
		postUrlByForm('/console/cnfdi/updateinvestcontact.jsp',{'serno':serno,'type':'delete','investNo':"${sysinfo.INVESTMENT_NO}"});
	}
	function toAddContact(){
			$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newcontact.jsp",{
					'type':'add','investNo':"${sysinfo.INVESTMENT_NO}"
				}, function(data){
				$( "#errmsg" ).html(data); 
				$( "#errmsg" ).dialog({
				      height:250,
				      width:800,
				      modal: true,
				      resizable: false,
				      draggable: false,
				      title:'新增聯絡人',
				      close: function( event, ui ) {
				    	  $( "#errmsg" ).html(""); 
				      }
			});
			},"html");
	}
});
</script>
<div id="errmsg"></div>
<div class='tbtitle' style="margin-bottom: 5px;">聯絡人列表</div>
<div style="height: 5px;"></div>
<div style="text-align: right;"><input type="button" class="btn_class_opener" id="addC" value="新增聯絡人"></div>
<div style="width: 98%;padding-left: 15px;">
<table style="width: 100%;font-size: 16px;" id="contacts">
	<thead>
		<tr>
			<th></th>
			<th>聯絡人</th>
			<th>聯絡人電話</th>
			<th>刪除</th>
			<th>編輯</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${contactsName}">
			<tr>
				<td></td>
				<td>${item.name}</td>
				<td>${item.telNo}</td>
				<td><c:if test="${fn:contains(memberUrls,'E0401')}"><input type="button" class="cDel btn_class_opener" id="delete${item.serno}" value="刪除"></c:if></td>
				<td><c:if test="${fn:contains(memberUrls,'E0401')}"><input type="button" class="cEdit btn_class_opener" id="edit${item.serno}" value="編輯"></c:if></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="height: 10px;"></div>
<div class='tbtitle'>系統資訊</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 20%;">負責人：</td>
		<td style="width: 20%;">${sysinfo.CHARGE_PERSON}</td>
		<td style="text-align: right;width: 20%;">電話：</td>
		<td>${sysinfo.TEL_NO}</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 20%;">地址：</td>
		<td colspan="3">${sysinfo.COUNTY_NAME}${sysinfo.TOWN_NAME}${sysinfo.ADDRESS}</td>
	</tr>	
<%-- 	<c:forEach var="item" items="${agents}">
		<tr>
			<td style="text-align: right;">代理人：</td>
			<td >${item.IN_AGENT}&nbsp;${item.POSITION_NAME}</td>
			<td style="text-align: right;">代理人電話：</td>
			<td>${item.TEL_NO}</td>
		</tr>
	</c:forEach> --%>
</table>
<div class='tbtitle'>聯絡資訊</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 20%;">問卷電話：</td>
		<td>${contacts.S_telNo}</td>
	</tr>
	<tr>
		<td style="text-align: right;">訪視電話：</td>
		<td>${contacts.I_telNo}</td>
	</tr>
	<tr>
		<td style="text-align: right;">問卷地址：</td>
		<td>${IOLV1[contacts.S_City]}${IOLV2[contacts.S_Town]}${contacts.S_Addr}</td>
	</tr>
	<tr>
		<td style="text-align: right;">訪視地址：</td>
		<td>${IOLV1[contacts.I_City]}${IOLV2[contacts.I_Town]}${contacts.I_Addr}</td>
	</tr>
</table>
