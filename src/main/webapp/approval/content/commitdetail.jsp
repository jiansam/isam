<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/setDatatableOld.js'/>"></script>
<script>
$(function() {
	$(".datatable").dataTable(getOption());
	$(".DTRestrain").dataTable(getOption2([3]));
    $( "#tabs" ).tabs();
});
function toCheckPage(serno,idno,investor){
	postUrlByForm('/approval/showodetail.jsp',{'serno':serno,'editType':'show','idno':idno,'investor':investor});
}
function toEditReport(repserno,idno,investor){
	postUrlByForm('/approval/seecommit.jsp',{'repserno':repserno,'idno':idno,'investor':investor});
}
  function getOption(){
	  var option={
				"bAutoWidth" : false, //自適應寬度
				"bLengthChange": false,
				//"bFilter": false,
				//多語言配置
				"oLanguage" : {
					"sProcessing" : "正在載入中......",
					"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
					"sZeroRecords" : "對不起，查詢不到相關資料！",
					"sEmptyTable" : "目前尚無資料！",
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
	  return option;
  }
</script>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;檢視承諾&nbsp;]</span>&nbsp;&nbsp;
					<span style="margin-left: 830px;">
						<a href="<c:url value='/approval/showapproval.jsp'/>" class="btn_class_opener">返回列表</a>
					</span>
				</legend>
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
					<strong style="color:#222;margin-left: 15px;margin-bottom: 5px;">企業基本資料</strong><br/>
					<table style="width: 100%;">
						<tr>
							<td style="width: 25%;text-align: right;">投資人名稱(統一編號)：</td>
							<td>${cdInfo.investor}（${cdbean.IDNO}）</td>
						</tr>
						<c:if test="${not empty cdInfo.minmaxyear}">
						<tr>
							<td style="width: 25%;text-align: right;">彙總申報起始年：</td>
							<td>${cdInfo.minmaxyear}</td>
						</tr>
						</c:if>
						<c:if test="${not empty cdInfo.restrainOffice}">
						<tr>
							<td style="width: 25%;text-align: right;">涉及機關：</td>
							<td>${cdInfo.restrainOffice}</td>
						</tr>
						</c:if>
						<tr>
							<td style="width: 25%;text-align: right;">最後修改人(日期)：</td>
							<td>${cdInfo.lastEditor}（${cdInfo.lastUpdate}）</td>
						</tr>
					</table>					
				</div>
				<div id="tabs" style="font-size: 14px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">承諾企業編輯項目</a></li>
					    <li><a href="#tabs-2">管制項目狀態</a></li>
					    <li><a href="#tabs-3">執行情形彙總</a></li>
					    <li><a href="#tabs-4">涉及大陸事業</a></li>
					    <li><a href="#tabs-5">涉及文號</a></li>
					    <li><a href="#tabs-6">聯絡人資訊</a></li>
					  </ul>
					  <div id="tabs-1">
					        <div style="font-size: 16px;margin-top: 5px;">
									<c:if test="${empty cdInfo.officstr && empty cdbean.note}">
										<strong style="color:#222;margin-left: 15px;margin-bottom: 5px;">無特殊列管項目且無備註</strong><br/>
									</c:if>
									<c:if test="${not empty cdInfo.officstr}">
										<div style="margin-bottom: 10px;margin-left: 15px;">
											<div style="margin-bottom: 5px;">
											<c:set var="offic" value="${fn:split(cdInfo.officstr,',')}"/>
							        		<strong style="color:#222;">特殊列管項目：</strong><br/>
							        		</div> 
							        		<div>
							        		<c:forEach var="itemOffic" items="${offic}">
							        			<c:if test="${not empty CRType[fn:substring(itemOffic,0,4)]&&fn:length(itemOffic)>4}">${CRType[fn:substring(itemOffic,0,4)]}-</c:if>${CRType[itemOffic]}<br/>
							        		</c:forEach>
							        		</div>
							    		</div>
						    		</c:if>
						    		<c:if test="${not empty cdbean.note}">
										<div style="margin-left: 15px;">
								        		<strong style="color:#222;margin-bottom: 0px;">備註：</strong> 
										    	<pre style="margin-top: 10px;">${cdbean.note}</pre>
										</div>
									</c:if>
							</div>
					  </div>
					  <div id="tabs-2">
						<c:if test="${empty crlist}">
							<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
						</c:if>
						<c:if test="${not empty crlist}">
							<strong style="margin:5px 10px;color:#222;font-size: 16px;">管制項目狀態彙總</strong>
							<table class="DTRestrain" style="font-size: 16px;text-align: center;width: 100%;">
								<thead>
								<tr>
									<th>項目</th>
									<th>期間</th>
									<th>狀態</th>
									<th>檢視管制項目</th>
								</tr>
								</thead>
								<tbody>
								<c:forEach var="crbean" items="${crlist}">
									<tr>
										<td>${crbean.type}</td>
										<td>${crbean.startYear}~${crbean.endYear}</td>
										<td>${crbean.state}</td>
										<td style="text-align: center;">
											<input type="button" onclick="toCheckPage('${crbean.serno}','${cdbean.IDNO}','${cdInfo.investor}')" class="btn_class_opener" style="text-align: center;font-size: 12px;" value="檢視"/>
										</td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</c:if>
					  </div>
					  <div id="tabs-3">
					  	<c:if test="${empty povCR}">
							<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
						</c:if>
						<c:if test="${not empty povCR}">
							<jsp:include page="/approval/content/commitdetailreport.jsp" flush="true" >
								<jsp:param value="檢視" name="EditType"/>
							</jsp:include>
						</c:if>
					  </div>
					  <div id="tabs-4">
					  	<c:if test="${not empty investList}">
					  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">涉及大陸事業</strong>
							<table class="datatable" style="font-size: 16px;width: 100%;">
								<thead>
								<tr>
									<th>案號</th>
									<th>大陸事業名稱</th>
									<th>管制項目</th>
								</tr>
								</thead>
								<tbody>
								<c:forEach var="invest" items="${investList}">
									<tr>
										<td>${invest[0]}</td>										
										<td>${invest[1]}</td>										
										<td>${invest[2]}</td>										
									</tr>
								</c:forEach>
								</tbody>
							</table>
					  	</c:if>
					  	<c:if test="${empty investList}">
					  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
					  	</c:if>
					  </div>
					  <div id="tabs-5">
					  	<c:if test="${not empty creNOList}">
					  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">涉及文號</strong>
							<table class="datatable" style="font-size: 16px;width: 100%;">
								<thead>
								<tr>
									<th>核准日</th>
									<th>文號</th>
									<th>事由</th>
									<th>管制項目</th>
								</tr></thead><tbody>
								<c:forEach var="reNo" items="${creNOList}">
									<tr>
										<td>${reNo[0]}</td>										
										<td>${reNo[1]}</td>										
										<td>${reNo[2]}</td>										
										<td>${reNo[3]}</td>										
									</tr>
								</c:forEach></tbody>
							</table>
					  	</c:if>
					  	<c:if test="${empty creNOList}">
					  		<strong style="margin:5px 10px;color:#222;font-size: 16px;">尚無資料</strong>
					  	</c:if>
					  </div>
					  <div id="tabs-6" style="margin-bottom: 20px;">
					  	<jsp:include page="/approval/content/contacts.jsp" flush="true" />
					  </div>
				</div>
			</fieldset>
		</div>