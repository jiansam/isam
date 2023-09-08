<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var opt=$.extend({
		"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1,2,3] }
		             ],		                
	},sdInitDataTableSetting());
	var roTable=$("#contacts").dataTable(opt);
	$(".cShow").click(function(){
		var cID=$(this).prop("alt");
		var ntr = $(this).parents('tr')[0];
		var aData=roTable.fnGetData(ntr);
			$.post( "${pageContext.request.contextPath}/console/commit/content/newcontact.jsp",{
					'type':'show'
					,'contact':aData[0],'tel':aData[1]
					,'reNos':aData[2] 
				}, function(data){
				$( "#Rtmp" ).html(data); 
				$( "#Rtmp" ).dialog({
				      height:550,
				      width:980,
				      modal: true,
				      resizable: false,
				      draggable: false,
				      title:'檢視聯絡人',
				      buttons: {
					       "關閉": function() {
					          $( this ).dialog( "close" );
					        }
					  },
				      close: function( event, ui ) {
				    	  $( "#Rtmp" ).html(""); 
				      }
			});
			},"html");
	});
});
</script>
<div id="Rtmp"></div>
<div>
	  	<c:if test="${not empty cContacts}">
	  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">聯絡人資訊</strong>
			<table id="contacts" style="font-size: 16px;width: 100%;">
				<thead>
				<tr>
					<th>聯絡人</th>
					<th>電話</th>
					<th>文號</th>
					<th style="width: 8%;">檢視</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="cContact" items="${cContacts}">
					<tr>
						<td>${cContact.contact}</td>										
						<td>${cContact.tel}</td>										
						<td>${cXr[cContact.cid]}</td>										
						<td><input type="button" class="btn_class_opener cShow" value="檢視" alt="${cContact.cid}"></td>										
					</tr>
				</c:forEach>
				</tbody>
			</table>
	  	</c:if>
	  	<c:if test="${empty cContacts}">
	  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
	  	</c:if>
</div>
<div style="margin: 10px;"></div>