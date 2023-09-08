<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>

<%-- <script type="text/javascript" src="<c:url value='/js/approval.js'/>"></script> --%>
<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<script type="text/javascript">
$(function(){
	var option={
			"bAutoWidth" : false, //自適應寬度
			"bLengthChange": false,
			"aoColumnDefs": [
			    { "bSortable": false,'sClass':'center', "aTargets": [ 0 ] }
			],
			"aaSorting": [[ 1, 'desc' ]],
			//"bFilter": false,
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
			}
	};
 	var oTable=$(".datatables").dataTable(option); 
 });
</script>
<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;核准資料&nbsp;</strong>&nbsp;</span>
		</legend>
		<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="padding-left: 20px; ">
					<tr>
						<td class="trRight" style="width: 10%;">投資案號：</td>				
						<td style="width: 5%;">${apInfo.investNo}</td>				
						<td style="width: 15%;" class="trRight">大陸事業名稱：</td>				
						<td>${apInfo.cnName}</td>				
					</tr>
					<tr>
						<td class="trRight" >統一編號：</td>				
						<td>${apInfo.idno}</td>				
						<td class="trRight">投資人名稱：</td>				
						<td>${apInfo.investor}</td>				
					</tr>
			</table>
		</div>
		<div style="height: 20px;"></div>
		<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			<c:if test="${not empty approvalDetail}">
				<table style="font-size: 14px;width: 98%;" class="datatables">
					<c:forEach	var="beans" items="${approvalDetail}" varStatus="p">
						<c:forEach	var="Row" items="${beans}"  varStatus="i">
							<c:choose>
								<c:when test="${i.index eq 0}">
									<c:if test="${p.index eq 0}"><thead><tr style="text-align: left;"><th>${Row}</th></c:if>
									<c:if test="${p.index eq 1}"><tbody><tr style="text-align: left;"><td>${Row}</td></c:if>
									<c:if test="${p.index > 1}"><tr style="text-align: left;"><td>${Row}</td></c:if>
								</c:when>
								<c:when test="${i.index eq 7}">
									<td>${Row}</td></tr>
								</c:when>
								<c:when test="${i.index eq 8}">
									<tr style="text-align: right;"><td>
									<c:if test="${p.index>0}"><span class="numberFmt">${Row}</span></c:if>
									<c:if test="${p.index eq 0}">${Row}</c:if>
								</td>
								</c:when>
								<c:when test="${i.index eq 18}">
								<td>
									<c:if test="${p.index>0}"><span class="numberFmt">${Row}</span></c:if>
									<c:if test="${p.index eq 0}">${Row}</c:if>
								</td></tr>
									<c:if test="${p.index eq 0}"></thead></c:if>
									<c:if test="${p.index eq fn:length(beans[0])}"></tbody></c:if>
								</c:when>
								<c:otherwise>
									<td>
										<c:choose>
											<c:when test="${i.index > 7 && p.index>0}"><span class="numberFmt">${Row}</span></c:when>
											<c:otherwise>${Row}</c:otherwise>
										</c:choose>
									</td>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:forEach>
				</table>
			</c:if>
		</div>
	</fieldset>
</div>
