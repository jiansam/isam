<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 2, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,3,5] },
		                 {"sSortDataType":"chinese", "aTargets": [2,4]}
		                 /* ,{"sSortDataType":"dom-btn", "aTargets": [3,4,5]} */],
		           "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
				   "iDisplayLength": 10
	},sdInitDataTableSetting(),sdSortChinese(),sdSortBtn());
	$("#example tbody").on('click',"td:not(.1stTd)", function() {
		 var nTr = $(this).parents('tr')[0];
		var $item=$('td:eq(0)',nTr).find("input:checkbox");
		$item.prop("checked",!$item.prop("checked"));
    });
	$("input[name='no']").bind('click', function() {
		$(this).parent(".1stTd").trigger('click');
    });
	$(".1stTd").bind('click', function() {
		$(this).find("input[name='no']").trigger('click');
    });
	var o1Table=$("#example").dataTable(y);
	$("#bDel").on("click",function(){
		 if($("input[name='no']:checked",o1Table.fnGetNodes()).length>0){
			$("<div title='刪除提示'>請問是否確定要刪除?</div>" ).dialog({
		      resizable: false,
		      modal: true,
		      buttons: {
		        "確定": function() {
		        	var serno="";
					 $("input[name='no']:checked",o1Table.fnGetNodes()).each(function(idx, tr){
						 if(serno.length!=0){
							 serno+=",";
						 }
						 serno+=$(this).val();
					  });
					  postUrlByForm('/console/project/editnomatch.jsp',{'no':serno});
					  $( this ).dialog( "close" );
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		 }else{
			 $("<div title='請選擇'>至少須選擇一項以上，才能進行動作，謝謝！</div>" ).dialog();
		 }
	});
	
});
$(function() {
	$(".selectThispage").click(function(){
		$(this).closest("fieldset").find(":checkbox").each(function(){
			$(this).prop("checked",true);
		});
	});
	$(".unselectThispage").click(function(){
		$(this).closest("fieldset").find(":checkbox").each(function(){
			$(this).prop("checked",false);
		});
	});
});
</script>
<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;匯入錯誤&nbsp;]</span><strong style="color:#222;">&nbsp;系統無對應專案資料&nbsp;</strong>&nbsp;
			<span style="float: right ;">
				<a href="#" id="selectThispage" class="selectThispage btn_class_opener">本頁全選</a>
				<a href="#" id="unselectThispage" class="unselectThispage btn_class_opener">本頁取消</a>
				<a href="#" id="bDel" class="btn_class_opener">刪除</a>
			</span>
		</legend>
		<div>
		<div style="width: 98%;">
		<table id="example" class="display" style="width: 100%;"> 
			<thead>
				<tr>
					<th style="width: 8%;">選擇</th>
					<th>統編</th>
					<th>投資人</th>
					<th>投資<br/>案號</th>
					<th>大陸事業名稱</th>
					<th>填報<br/>年季</th>
					<th nowrap>錯誤原因</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${notMatchlist}">
					<tr>
						<c:forEach var="sub" items="${item}" varStatus="i">
							<c:choose>
								<c:when test="${i.index==0}"><td class="1stTd"><input type="checkbox" name="no" value="${sub}">&nbsp;</td></c:when>
								<c:otherwise><td>${sub}</td></c:otherwise>
							</c:choose>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		</div>
	</fieldset>
</div>
