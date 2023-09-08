<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var opt=$.extend({
		"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1,2,3,4,5] }
		             ],		                
	},sdInitDataTableSetting());
	var roTable=$("#contacts").dataTable(opt);
	if("${fn:contains(memberUrls,'E0102')}"==="false"){
		roTable.fnSetColumnVis( 4, false); 
		roTable.fnSetColumnVis( 5, false); 
	}
	$("#cNew").click(function(){
		$.post( "${pageContext.request.contextPath}/console/commit/content/newcontact.jsp",{
				'type':'add'
			}, function(data){
			$( "#Rtmp" ).html(data); 
			$( "#Rtmp" ).dialog({
			      height:550,
			      width:980,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      title:'新增聯絡人',
			      buttons: {
				      "儲存": function() {
				    	  if(editContact('add',"")){
					          $( this ).dialog( "close" );
				    	  }
				        },
				       "取消": function() {
				          $( this ).dialog( "close" );
				        }
				      },
			      close: function( event, ui ) {
			    	  $( "#Rtmp" ).html(""); 
			      }
		});
		},"html");
	});
	$(".cDel").click(function(){
		var $item =$(this);
		$( "<div>確定刪除本筆資料？</div>" ).dialog({
		      resizable: false,
		      height:200,
		      modal: true,
		      buttons: {
		      "確定": function() {
		    	var cID =$item.prop("alt");
		  		postUrlByForm('/console/commit/editcommitcontact.jsp'
		  				,{'cID':cID,'type':'delete','idno':"${cdbean.IDNO}"});
		          $( this ).dialog( "close" );
		        },
		       "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
	});
	$(".cEdit").click(function(){
		var cID=$(this).prop("alt");
		var ntr = $(this).parents('tr')[0];
		var aData=roTable.fnGetData(ntr);
		$.post( "${pageContext.request.contextPath}/console/commit/content/newcontact.jsp",{
					'type':'edit'
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
			      title:'編輯聯絡人',
			      buttons: {
				      "儲存": function() {
				    	  if(editContact('edit',cID)){
					          $( this ).dialog( "close" );
				    	  }
				        },
				       "取消": function() {
				          $( this ).dialog( "close" );
				        }
				      },
			      close: function( event, ui ) {
			    	  $( "#Rtmp" ).html(""); 
			      }
			});
		},"html");
	});
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
	function editContact(type,cID){
		var contact=$("#crform input[name='contact']").val();
   	 	var tel=$("#crform input[name='tel']").val();
   		var reNos="";
		$("input[name='reNos']:checked",$("#reNoList").dataTable().fnGetNodes()).each(function(idx, tr){
			if(reNos.length!=0){
				reNos+=",";
			}
			reNos+=$.trim($(this).val());
		});
		if($.trim(contact).length+$.trim(tel).length>0){
 	 		postUrlByForm('/console/commit/editcommitcontact.jsp'
			,{'cID':cID,'idno':"${cdbean.IDNO}",'type':type,'contact':contact,'tel':tel,'reNos':reNos});
 	 		return true;
		}else{
			alert("至少需填寫一項聯絡人或電話欄位，請檢查謝謝！");
			$("#crform input[name='contact']").focus();
			return false;
		}
	}
});
</script>
<div id="Rtmp"></div>
<div>
	<div style="float: right;margin:0px 5px 10px 5px;"><input type="button" class="btn_class_opener" id="cNew" value="新增" style="font-size: 16px;"></div>
	  	<c:if test="${not empty cContacts}">
	  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">聯絡人資訊</strong>
			<table id="contacts" style="font-size: 16px;width: 100%;">
				<thead>
				<tr>
					<th>聯絡人</th>
					<th>電話</th>
					<th>文號</th>
					<th style="width: 8%;">檢視</th>
					<th style="width: 8%;">編輯</th>
					<th style="width: 8%;">刪除</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="cContact" items="${cContacts}">
					<tr>
						<td>${cContact.contact}</td>										
						<td>${cContact.tel}</td>										
						<td>${cXr[cContact.cid]}</td>										
						<td><input type="button" class="btn_class_opener cShow" value="檢視" alt="${cContact.cid}"></td>										
						<td><input type="button" class="btn_class_opener cEdit" value="編輯" alt="${cContact.cid}"></td>										
						<td><input type="button" class="btn_class_opener cDel" value="刪除" alt="${cContact.cid}"></td>										
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