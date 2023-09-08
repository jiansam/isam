<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,4,5 ] },
		                 {"sSortDataType":"chinese", "aTargets": [1,2]},
		                 { "bSortable": false, "aTargets": [ 0,4,5 ] }
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
	var oTable=$(".dbtable").dataTable(y);
	if("${fn:contains(memberUrls,'E0401')}"==="false"){
		oTable.fnSetColumnVis( 4, false); 
		oTable.fnSetColumnVis( 5, false); 
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
});
function toEditContact($item){
	var serno =$item.prop("id").replace("edit","");
	var nationDef =$item.parent("td").children(".nationNo").text();
	var cnCodeDef =$item.parent("td").children(".cnNo").text();
	var relatedname =$item.parents("tr").find("td :eq(1)").text();
		$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newiparent.jsp",{
				'type':'edit','cnCodeDef':cnCodeDef,"nationDef":nationDef,'relatedname':relatedname,'serno':serno,'investorSeq':"${ofiiobean.investorSeq}"
			}, function(data){
			$( "#errmsg" ).html(data); 
			$( "#errmsg" ).dialog({
			      height:250,
			      width:800,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      title:'編輯母公司或關連企業及受益人資訊',
			      close: function( event, ui ) {
			    	  $( "#errmsg" ).html(""); 
			      }
		});
		},"html");
}
function deleteContact(serno){
	postUrlByForm('/console/updateinvestorrelated.jsp',{'serno':serno,'type':'delete','investorSeq':"${ofiiobean.investorSeq}"});
}
function toAddContact(){
		$.post( "${pageContext.request.contextPath}/console/cnfdi/content/newiparent.jsp",{
				'type':'add','investorSeq':"${ofiiobean.investorSeq}"
			}, function(data){
			$( "#errmsg" ).html(data); 
			$( "#errmsg" ).dialog({
			      height:250,
			      width:800,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      title:'新增母公司或關連企業及受益人資訊',
			      close: function( event, ui ) {
			    	  $( "#errmsg" ).html(""); 
			      }
		});
		},"html");
}
</script>
<div id="errmsg"></div>
<div style="text-align: right;"><input type="button" class="btn_class_opener" id="addC" value="新增"></div>
<div class='tbtitle' style="margin-bottom: 5px;">母公司或關連企業及受益人資訊</div>
<div style="height: 5px;"></div>
<div style="width: 98%;margin-left: 15px;">
<table style="width: 98%;font-size: 16px;" class="dbtable">
	<thead>
		<tr>
			<th>序號</th>
			<th>名稱</th>
			<th>國別</th>
			<th>建立日期</th>
			<th>刪除</th>
			<th>編輯</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="r" items="${relateds}">	
			<tr>
				<td></td>
				<td>${r.relatedname}</td>
				<td>${optmap.nation[r.nation]}${optmap.cnCode[r.cnCode]}</td>
				<td>${ibfn:toTWDateStr(r.createtime)}</td>
				<td>
				<c:if test="${fn:contains(memberUrls,'E0401')}"><input type="button" class="cDel btn_class_opener" id="delete${r.serno}" value="刪除"></c:if></td>
				<td>
				<c:if test="${fn:contains(memberUrls,'E0401')}"><input type="button" class="cEdit btn_class_opener" id="edit${r.serno}" value="編輯">
					<span class="nationNo" style="display: none;">${r.nation}</span>
					<span class="cnNo" style="display: none;">${r.cnCode}</span></c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</div>
<div style="height: 10px;"></div>
