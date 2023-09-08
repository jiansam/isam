<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>

<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<script>
$(function(){
	$( "#tabs" ).tabs();
	$( "#accordion" ).accordion({collapsible: true,active:false});
	$(".myTable").dataTable(getOption());
	$("#reNoTable").dataTable().$("td:first-child").css('text-align','center');
	$("#invNoTable").dataTable().$("td:first-child").css('text-align','center');
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
function getOption(){
	  var option={
			"bAutoWidth" : false, //自適應寬度
			"bLengthChange": false,
			"fnDrawCallback": function ( oSettings ) {
					if ( oSettings.bSorted || oSettings.bFiltered ){
						for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
							$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
						} 
					}
				},
			"aaSorting": [[ 1, 'asc' ]],
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
	<legend style="text-align:center;padding:5px;width:200px;white-space:nowrap;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
		<span style="color:#F30;"><strong>&nbsp;檢視管制項目&nbsp;</strong>&nbsp;</span>
	</legend>
	<c:set var="typeName"  value="${CRType[bean.type]}" />
		<div id="tabs" class="tabs-bottom" style="font-size: 16px;margin-top: 20px;">
			<ul>
			    <li><a href="#tabs-1">${typeName}填報項目</a></li>
			    <li><a href="#tabs-2">檢視涉及文號</a></li>
			    <li><a href="#tabs-3">檢視涉及大陸事業</a></li>
		    	<li style="float: right"><input type="button" class="btn_class_opener" onclick="postUrlByForm('/approval/showocommit.jsp',{'serno':'${oInfo.idno}'})" value="回上一頁" /></li>
			  </ul>
			 <div class="tabs-spacer"></div>
			 <div id="tabs-1">
			 <fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
					<legend style="width:100%;border-top:1px solid #E6E6E6">
						<span style="color:#F30;">[&nbsp;Step1&nbsp;]</span>
						<strong style="color:#222;">${typeName}-原始承諾金額 </strong>
					</legend>
						<span class="basetable">
							統一編號：${oInfo.idno}
						</span>
						<span class="basetable">
							投資人名稱：${oInfo.investor}
						</span><br/>
						<span class="basetable">
							修改人：${oInfo.editor}
						</span>
						<span class="basetable">
							修改時間：${fn:substring(bean.updatetime,0,16)}
						</span><br/>
						<span class="basetable">
							管制狀態：${bean.state eq 'Y'?'管制':'解除管制'}
						</span>
							<c:choose>
								<c:when test="${bean.type eq '01'}">
									<span class="basetable">
										申報類型：${AoCode[bean.repType]}
									</span>
									<span class="basetable">
										申報期間：${bean.startYear}年~${bean.endYear}年
									</span>
									<c:if test="${not empty office}">
										<br/>
										<span class="basetable">
											涉及機關：${fn:replace(office,',','、')}
										</span>							
									</c:if>
								</c:when>
								<c:when test="${bean.type eq '02'}">
									<span class="basetable">
										列管類型：${AoCode[bean.repType]}
									</span>
									<span class="basetable">
										改善期限：${bean.startYear}年~${bean.endYear}年
									</span>
								</c:when>
								<c:when test="${bean.type eq '03'}">
									<span class="basetable">
										資金回饋期間：${bean.startYear}年~${bean.endYear}年
									</span>
								</c:when>
								<c:when test="${bean.type eq '04'}">
									<span class="basetable">
										管制期間：${bean.startYear}年~${bean.endYear}年
									</span>
								</c:when>
							</c:choose>
						<c:if test="${not empty bean.note}">
								<br/>
								   <div class="basetable">
									   <div style="float: left; width: 6%;">說明：</div>
									   <div style="float: left;width: 90%;">
									   		${fn:substring(bean.note,0,100)}
									   		<c:if test="${fn:length(bean.note)>100}">...</c:if>
									   </div>
									   <div style="clear: both;"></div>
									   <c:if test="${fn:length(bean.note)>100}">
									   <div id="accordion">
									   	<h3>詳細說明</h3>
									   		<div><pre>${bean.note}</pre></div>
									   </div>
									   </c:if>
								   </div>
						</c:if>
						<c:if test="${bean.type != '04'}">
							<c:if test="${not empty detailTotal}">
							<br/><br/>
							<c:choose>
								<c:when test="${bean.type eq '01'}">
									<strong style="margin:5px 10px;color:#222;">${typeName}-總原始承諾金額（新台幣元）</strong>
									<table class="formProj" style="table-layout:fixed;">
										<tr>
											<c:forEach var="i" items="${detailTotal}">
												<c:if test="${empty CRType[i.key]}">
													<th>年度</th>
												</c:if>
												<c:if test="${not empty CRType[i.key]}">
													<th>${CRType[i.key]}</th>
												</c:if>
											</c:forEach>
										</tr>
										<tr>
											<c:forEach var="i" items="${detailTotal}">
												<td class="trRight"><span class="numberFmt">${i.value}</span></td>
											</c:forEach>
										</tr>
									</table>
									<c:if test="${not empty detailEx}">
										<div style="height: 10px;"></div>
										<strong style="margin:10px 10px;color:#222;">${typeName}-原承諾事項分年資料（新台幣元）</strong>
										<div style="height: 10px;"></div>
										<table class="formProj" style="text-align: center;table-layout:fixed;">
											<tr>
												<th>年度</th>
												<th>國內投資計畫</th>
												<th>機器設備及原物料採購</th>
												<th>研發經費投入</th>								
												<th>人員聘僱</th>								
											</tr>
												<c:forEach var="dEx" items="${detailEx}">
												<tr style="text-align: center;">
													<c:forEach var="dExSub" items="${dEx}" varStatus="i">
														<c:if test="${i.index eq 0}">
															<td>${dExSub}</td>													
														</c:if>
														<c:if test="${i.index !=0}">
															<td class="trRight"><span class="numberFmt">${dExSub}</span></td>												
														</c:if>
													</c:forEach>
												</tr>
												</c:forEach>
										</table>
									</c:if>
								</c:when>
								<c:when test="${bean.type eq '02'}">
									<strong style="margin:5px 10px;color:#222;">${typeName}（被列入改善年度之財報資訊）</strong>
									<table class="formProj" style="table-layout:fixed;">
										<tr>
											<c:forEach var="i" items="${detailTotal}">
												<c:if test="${empty CRType[i.key]}">
													<th>年度</th>
												</c:if>
												<c:if test="${not empty CRType[i.key]}">
													<th>${CRType[i.key]}</th>
												</c:if>
											</c:forEach>
										</tr>
										<tr>
											<c:forEach var="i" items="${detailTotal}" varStatus="j">
												<td style="text-align: center;">
													<c:if test="${not empty i.value}">
														<span class="numberFmt">${i.value}</span>&nbsp;%
													</c:if>
												</td>
											</c:forEach>
										</tr>
									</table>
									<c:if test="${not empty detailEx}">
										<div style="height: 10px;"></div>
										<strong style="margin:10px 10px;color:#222;">${typeName}-原承諾事項分年資料</strong>
										<div style="height: 10px;"></div>
										<table class="formProj" style="text-align: center;">
											<tr>
												<th>年度</th>
												<th>流動比率</th>
												<th>速動比率</th>
												<th>負債比率</th>								
											</tr>
												<c:forEach var="dEx" items="${detailEx}">
												<tr style="text-align: center;">
													<c:forEach var="dExSub" items="${dEx}" varStatus="i">
														<c:if test="${i.index eq 0}">
															<td>${dExSub}</td>													
														</c:if>
														<c:if test="${i.index !=0}">
															<td><span class="numberFmt">${dExSub}</span><c:if test="${not empty dExSub}">%</c:if></td>												
														</c:if>
													</c:forEach>
												</tr>
												</c:forEach>
										</table>
									</c:if>
								</c:when>
								<c:when test="${bean.type eq '03'}">
									<strong style="margin:5px 10px;color:#222;">資金回饋總額</strong>
									<table class="formProj">
										<c:forEach var="i" items="${detailTotal}" varStatus="j">
											<tr>
												<th>${CRType[i.key]}</th>
												<td>
													<span>幣別：${AoCode[bean.repType]}&nbsp;&nbsp;&nbsp;總金額：</span><span class="numberFmt">${i.value}</span>元
												</td>
											</tr>
										</c:forEach>
									</table>
									<c:if test="${not empty detailEx}">
										<div style="height: 10px;"></div>
										<strong style="margin:10px 10px;color:#222;">${typeName}-原承諾事項分年資料</strong>
										<div style="height: 10px;"></div>
											<table class="formProj" style="text-align: center;">
												<tr>
													<th>年度</th>
													<th>資金回饋金額</th>
													<th>累積資金回饋金額</th>
												</tr>
													<c:forEach var="dEx" items="${detailEx}">
													<tr>
															<td>${dEx[0]}</td>
															<td><span class="numberFmt">${dEx[1]}</span></td>
															<td><span class="numberFmt">${dEx[2]}</span></td>
													</tr>
													</c:forEach>
											</table>
									</c:if>
								</c:when>
							</c:choose>
							</c:if>
						</c:if>
				</fieldset>
			 </div>
			 <div id="tabs-2">
			 	<c:if test="${not empty receiveNo}">
					<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
							<legend style="width:100%;border-top:1px solid #E6E6E6">
								<span style="color:#F30;">[&nbsp;Step2&nbsp;]</span>
								<strong style="color:#222;">檢視涉及文號</strong>
							</legend>
						<div style="margin-top: 20px;">
							<table  class="myTable" id="reNoTable" style="width: 95%;margin-left: 10px;"> 
								<thead>
									<tr>
										<th>No</th>
										<th>核准日期</th>
										<th>文號</th>
										<th>案由</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="reNo" items="${receiveNo}">
										<tr>
											<td></td>
											<td>${reNo.respDate}</td>
											<td>${reNo.receiveNo}</td>
											<td>${reNo.note}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</fieldset>
				</c:if>
			</div>
			<div id="tabs-3">
				<c:if test="${not empty investNo}">
					<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
						<legend style="width:100%;border-top:1px solid #E6E6E6">
							<span style="color:#F30;">[&nbsp;Step3&nbsp;]</span>
							<strong style="color:#222;">檢視涉及大陸投資事業</strong>
						</legend>
						<div style="margin-top: 20px;">
							<table class="myTable" id="invNoTable" style="width: 95%;margin-left: 15px;"> 
								<thead>
									<tr>
										<th>No</th>
										<th>案號</th>
										<th>大陸事業名稱</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="invNo" items="${investNo}">
										<tr>
											<td></td>
											<td>${invNo.key}</td>
											<td>${invNo.value}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</fieldset>
				</c:if>
			</div>
		</div>
	</fieldset>
</div>