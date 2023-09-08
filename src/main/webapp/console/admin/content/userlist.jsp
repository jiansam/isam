<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
<!--
table td{
	text-align: center;
}
.tdLeft{
	text-align: left;
}
.tdRight{
	text-align: right;
}
.errorMsg{
	color: red;
	font-size: 12px;
}
-->
</style>
<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/isam.js'/>"></script>
<script type="text/javascript">
$(function() {
	loginRecordError();	
	var oTable=$("#example").dataTable({
		"bAutoWidth" : false, //自適應寬度
		/* "bJQueryUI": true, */
		//使用fnDrawCallback、aoColumnDefs和aaSorting設定直接給予第一個<td>動態序號
		"fnDrawCallback": function ( oSettings ) {
			/* Need to redo the counters if filtered or sorted */
			if ( oSettings.bSorted || oSettings.bFiltered ){
				for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
					$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
				}
			}
		},
		"aoColumnDefs": [
		    { "bSortable": false,'sClass':'center', "aTargets": [ 0,6 ] }
		],
		"aaSorting": [[ 1, 'asc' ]],
		/* "sPaginationType" : "full_numbers", */  //顯示全部的分頁器
		//多語言配置
		"oLanguage" : {
			"sProcessing" : "正在載入中......",
			"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
			"sZeroRecords" : "對不起，查詢不到相關資料！",
			"sEmptyTable" : "本分類目前尚無資料！",
			"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共有_TOTAL_ 筆資料",
			"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料",
			"sInfoFiltered" : "<br/>原始資料為 _MAX_ 筆資料",
			"sSearch" : "搜尋",
			"oPaginate" : {
				"sFirst" : "最前頁",
				"sPrevious" : "上一頁",
				"sNext" : "下一頁",
				"sLast" : "最末頁"
			}
		},
		"aoColumns":[
			null,
			null,
			{"sSortDataType":"chinese"},
			{"sSortDataType":"chinese"},
			{"sSortDataType":"chinese"},
			null,
			null
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
});
</script>

<div id="pagecont">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;管理網站使用者&nbsp;</strong>&nbsp;</span>
		</legend>
		<div style="text-align: right;margin-right: 13px;margin-bottom:5px;font-size: 14px;">
			<img alt="新增" src='<c:url value="/images/action_add.gif"/>' >
			<a href="<c:url value="/console/admin/adduser.jsp"/>" style="color: #F30;">新增使用者</a>
		</div>
	<div id="leftbody" >
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;最近10次登入紀錄&nbsp;]</span>
			</legend>
		<div>
		<c:if test="${not empty listrecord}">
		   <table>
		   		<c:forEach var="record" items="${listrecord}" varStatus="i">
					<tr>
						<td>(${i.index+1})</td>
						<td class="tdLeft">${fn:substring(record.loginTime,0,fn:indexOf(record.loginTime,":")+3)}</td>
						<td class="tdLeft"><input type="hidden" class="yn" value="${record.loginResult}">${record.idMember}</td>
					</tr>
		   		</c:forEach>
		   </table>
		</c:if>
		</div>
		</fieldset>
	</div>
	<div id="rightbody" >
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;網站使用者列表&nbsp;]</span>&nbsp;&nbsp;
				</legend>
				<div>
					<table id="example" class="display" style="width: 98%;"> 
						<thead>
							<tr>
								<th>NO.</th>
								<th>帳號</th>
								<th>單位</th>
								<th>姓名</th>
								<th>權限</th>
								<th>狀態</th>
								<th>異動</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty listuser}">
								<c:forEach var="user" items="${listuser}">
									<tr>
										<td></td>
										<td>${user.idMember}</td>
										<td>${companyMap[user.company]}</td>
										<td>${user.username}</td>
										<td>
											<c:if test="${user.groupId eq 'super'}">管理者</c:if>
											<c:if test="${user.groupId eq 'user'}">使用者</c:if>
										</td>
										<td>
											<c:if test="${user.enable eq '0'}">N</c:if>
											<c:if test="${user.enable eq '1'}">Y</c:if>
										</td>
										<c:url var="urlUser" value="/getedituser.jsp">
											<c:param name="user">${user.idMember}</c:param>
										</c:url>
										<td><a href="${urlUser}"><img src="<c:url value="/images/edit.png" />" alt="修改" style="border: 0;"></a></td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</fieldset>
		</div>
	</div>
	</fieldset>
	<div style="height: 20px;clear: both;"></div>
</div>
