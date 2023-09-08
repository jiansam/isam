<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>

<script type="text/javascript" src="<c:url value='/js/jquery.ui.widget.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.iframe-transport.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.fileupload.js'/>"></script>

<script type="text/javascript">
$(function() {
	//$(".fileDown").menu();
	$("#tabs").tabs();
	if("${dyear}".length>0){
		 setSelectedToDefalut("nowyear","${dyear}");
	}
	$("select[name='nowyear']").change(function(){
		reloadIOlist();
	});
	$(".delItem").click(function( ){
		 var no=$(this).prop("alt");
		 if(no.length>0){
			 removeIOItem($(this),no);
		 }
	});
	$(".uploadItem").click(function(){
		var item=$(this).prop("alt");
		var tmp=item.split("_");
		managefiles($(this),tmp[1],tmp.length>2?tmp[2]:'',tmp[0]);
	});
	var y=$.extend(sdInitDataTableSetting(),sdSortChinese(),{
		"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2,4,5,6 ] },
		                 {"sSortDataType":"chinese", "aTargets": [3,5,6]},
		                 {"bSortable": false, "aTargets": [0,1,4]}
		              ],
		              "aaSorting": [[ 5, "desc" ],[2,"asc"]],
		             /* "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
					   "iDisplayLength": 20, */
				   "fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	});
	var oTable=$("#example").dataTable(y);
	$("#dialogupload").click(function(){
		$.post( "${pageContext.request.contextPath}/console/interviewone/content/uploadlist.jsp", function(data){
			$( "#uploadform" ).html(data); 
			$( "#uploadform" ).dialog({
			      height:400,
			      width:800,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      buttons: {
			        "新增": function() {
					  $("#addinvestno").submit();
			          $( this ).dialog( "close" );
			        },
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
			      },
			      close: function( event, ui ) {
			    	  $( "#uploadform" ).html(""); 
			      }
		});
		},"html");
	});
	if("${action}"==="delete"){
		alert("資料已移除。");
	}
	if("${fn:contains(memberUrls,'E0303')}"==="false"){
		oTable.fnSetColumnVis( 1, false); 
	}
});
function managefiles($item,investNo,reInvestNo,qNo){
	$.post( "${pageContext.request.contextPath}/includes/fileupload.jsp",{
			'investNo':investNo,
			'year':$("select[name='nowyear'] option:selected").val(),
			'reInvestNo':reInvestNo,
			'qNo':qNo
		}, function(data){
		$( "#uploadform" ).html(data); 
		$( "#uploadform" ).dialog({
		      height:400,
		      width:800,
		      modal: true,
		      resizable: false,
		      draggable: false,
		      title:'檔案管理_'+$item.parents("tr").children("td:eq(3)").text()+"("+investNo+")",
		      close: function( event, ui ) {
		    	  $( "#uploadform" ).html(""); 
		    	  reloadIOlist();
		      }
	});
	},"html");
}
function removeIOItem($no,no){
	$("<div style='font-size='12px;''>您即將移除陸資案號["+$no.parents("td").next("td").text()+"]，移除後將無法復原，請確認是否繼續?</div>").dialog({
		width: 350,
		modal:true,
		title:'確認刪除',
		buttons: {
	        "移除": function() {
			  postUrlByForm('/console/interviewone/deleteitem.jsp',{'serno':no,'year':$("select[name='nowyear'] option:selected").val()});
	          $( this ).dialog( "close" );
	        },
	        "取消": function() {
	          $( this ).dialog( "close" );
	        }
		}
	});
}
function reloadIOlist(){
	 postUrlByForm('/console/interviewone/showiolist.jsp',{'action':'manage','year':$("select[name='nowyear'] option:selected").val()});
}

</script>
<style>
  .ui-widget input, .ui-widget select, .ui-widget textarea, .ui-widget button{
  	font-size: 12px;
  }
</style>
<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;管理年度訪視清單&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/console/interviewone/content/surveymenu.jsp" flush="true">
				<jsp:param value="4" name="pos"/>
			</jsp:include>
		</div>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;年度訪視清單&nbsp;]</span>
				<span><input id="dialogupload" class="btn_class_opener" type="button" value="上傳訪視清單"></span>
			</legend>
			<div style="margin-bottom: 10xp;">
			<c:if test="${not empty yearlist}">
				<label>調查年度：</label>
				<select name="nowyear">
					<c:forEach var="ioSer" items="${yearlist}">
						<option value="${ioSer}">${ioSer}年</option>
					</c:forEach>
				</select>
			</c:if>
			</div><br>
			<div id="tabs">
			  <ul>
			    <li><a href="#tabs-1">訪視清單列表</a></li>
			    <li><a href="#tabs-2">選取轉投資事業</a></li>
			  </ul>
				<div id="tabs-1">
					<table id="example">
						<thead>
						<tr>
							<th style="width: 7%;">序號</th>
							<th style="width: 7%;">移除</th>
							<th style="width: 12%;">陸資案號</th>
							<th>國內事業名稱/轉投資事業名稱</th>
							<th style="width: 10%;">管理檔案</th>
							<th style="width: 12%;">訪視紀錄</th>
							<th style="width: 12%;">營運問卷</th>
						</tr>
						</thead>
						<tbody>
							<c:forEach var="bean" items="${Interviewone}">
								<tr>
									<td></td>
									<td>
										<c:if test="${fn:contains(memberUrls,'E0303')}"><input type="button" value="移除" class="delItem btn_class_opener" alt="${bean.qNo}"></c:if>
									</td>
									<td><c:forTokens items="${bean.investNo}" delims="、" var="inStr">
										   ${inStr}
										</c:forTokens>
									</td>
									<td>${bean.reInvestNo eq 0?cninfo[bean.investNo].cname : reInvestBase[bean.reInvestNo].cname}</td>
									<td>
										<input type="button" value="管理檔案" class="uploadItem btn_class_opener" alt="${bean.qNo}_${bean.investNo}_${bean.reInvestNo}">
									</td>
									<td>${iCount[bean.qNo] eq 0?'未上傳':'已上傳'}</td>
									<td>${sCount[bean.qNo] eq 0?'未上傳':'已上傳'}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div style="margin-bottom: 20px;"></div>
				</div>
				<div id="tabs-2">
					<jsp:include page="/console/interviewone/content/reinvestlist.jsp" flush="true" />
				</div>
			
			</div>
			<div id="uploadform"></div>
		</fieldset>
	</fieldset>
</div>
