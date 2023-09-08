<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>
<style>
.numpadding td{
/* 	margin-right: 10px; */
	padding-right: 20px;
}
</style>
<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<script>
$(function(){
	$( "#tabs" ).tabs();
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
$(function(){
	if("${bean.type}"==="01"){
		$("select[name='year']").change(function(){
			changeNowCount();
		});
		$("input[name='repType']").change(function(){
			changeNowCount();
		});
		$(".countReach").keyup(function(){
			changeReachPercent($(this).closest("tr"));
			if(startsWith($(this).prop("name"),"RsType")){
				getSumAndPercent($(this));
			}
		});
	}
	if("edit"==="edit"){
		$(".countReach").each(function(){
			if(startsWith($(this).prop("name"),"RsType")){
				getSumAndPercent($(this));
			}
		});
	}
});
function changeNowCount(){
	var nowCount;
	var repType = "${bean.repType}";
	var countRep=$("input[name='repType']:checked").val()==="0101"?1:2;
	var year=$("select[name='year']").val();
	if(repType==="01"){
		nowCount=(year-"${bean.startYear}")*2+countRep;
	}else{
		nowCount=(year-"${bean.startYear}")+1;
	}
	$("#nowCountRN").text(nowCount);
}
function getSumAndPercent($thisItem){
	var typeCode = $thisItem.prop("name").replace("RsType","").substr(0,4);
	var sumVal=0;
	$(".countSRC input[name^='RsType"+typeCode+"']").each(function(){
		var singleVal=getRemoveCommaVal($(this).val());
		if(singleVal!=="undefined"&&singleVal.length>0&&typeof(singleVal)!=="undefined"){
			sumVal+=parseFloat(singleVal);
		}
	});
	if(sumVal===0){
		$("#reach"+typeCode).text("");
	}else{
		$("#reach"+typeCode).text(getInsertComma(sumVal));
	}
	getDivideText($("#reach"+typeCode),$("#pdown"+typeCode),$("#reachRate"+typeCode),"%");
}
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
	<c:set var="typeName"  value="${typeStr}" />
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="text-align:center;padding:5px;width:200px;white-space:nowrap;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
		<span style="color:#F30;"><strong>&nbsp;${crBean.year}年執行情形&nbsp;</strong>&nbsp;</span>
	</legend>
	<form  id="CRForm" action="<c:url value='/console/commit/addcommitreport.jsp'/>" method="post">
		<div id="tabs" class="tabs-bottom" style="font-size: 16px;margin-top: 20px;">
			<ul>
			    <li><a href="#tabs-1">${typeName}填報項目</a></li>
			    <li><a href="#tabs-2">檢視涉及文號</a></li>
			    <li><a href="#tabs-3">檢視涉及大陸事業</a></li>
			    <li style="float: right"><input type="button" class="btn_class_opener" onclick="javascript:window.history.go(-1)" value="回上一頁" /></li>
			  </ul>
			 <div class="tabs-spacer"></div>
			 <div id="tabs-1">
				<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
					<legend style="border-top:1px solid #E6E6E6;margin-bottom: 0px;">
						<span style="color:#F30;">[&nbsp;Step1&nbsp;]</span>
						<strong style="color:#222;">${typeName}-填報執行情形</strong>
					</legend>
						<span class="basetable">
							統一編號：${param.idno}
						</span>
						<span class="basetable">
							投資人名稱：${param.investor}
						</span>
						<span class="basetable">
							收文文號：${crBean.keyinNo}
						</span><br/>
						<span class="basetable">
							列管期間：${bean.startYear}年~${bean.endYear}年
						</span>
						<c:if  test="${bean.type eq '01'}">
						<span class="basetable">
						<c:if test="${bean.repType eq '01'}">
							<c:set var="tempC" value="${crBean.repType eq '0101' ?1:2}" />
							<c:set var="conutRN" value="${((bean.endYear-bean.startYear)+1)*2}" />
							列管期數：${((crBean.year-bean.startYear))*2+tempC}/${conutRN}
						</c:if>
						<c:if test="${bean.repType eq '0103'}">
							<c:set var="conutRN" value="${(bean.endYear-bean.startYear)+1}" />
							列管期數：<fmt:parseNumber value="${(crBean.year-bean.startYear)+1}" />/${conutRN}
						</c:if>
						</span>
						</c:if>
						<span class="basetable">
						<c:if test="${not empty bean.type}">
							<c:choose>
								<c:when test="${bean.type eq '01'}">
										填報類型：
										<c:if test="${bean.repType eq '01'}">
											<c:if test="${crBean.repType eq '0101'}">上半年</c:if>
											<c:if test="${crBean.repType eq '0102'}">下半年</c:if>
										</c:if>
										<c:if test="${bean.repType != '01'}">
												年報
										</c:if>
								</c:when>
								<c:when test="${bean.type eq '02'}">
									列管財務類型：${codeStr}
								</c:when>
								<c:when test="${bean.type eq '03'}">
									資金回饋幣別：${codeStr}
								</c:when>
							</c:choose>
						</c:if>
						</span>
						<span class="basetable">
							填報年度：${crBean.year}<c:if test="${not empty crBean.year}">年</c:if>
						</span>
						<c:if test="${not empty office}">
						<br/>
						<span class="basetable">
							涉及機關：${office}
						</span>
						</c:if>
						<c:if  test="${bean.type eq '01'}">
						<br/>
							<span class="basetable">
							轉換幣別：<c:if test="${crBean.isConversion eq 'N'}">否</c:if><c:if test="${crBean.isConversion eq 'Y'}">是</c:if>
							</span><br/>
							<c:if test="${not empty crBean.note}">
							<span class="basetable">
							轉換備註：<pre><c:out value="${crBean.note}" /></pre>
							</span></c:if>
						</c:if>
						<br/><br/>
						<strong style="margin:10px 10px;color:#222;">${typeName}-${crBean.year}年執行情形</strong>
						<div style="height: 10px;"></div>
						<c:if test="${not empty bean.type}">
							<c:choose>
								<c:when test="${bean.type eq '01'}">
									<c:if test="${not empty detailBean}">
										<table class="formProj countSRC">
											<tr style="text-align: center;">
												<th style="width: 20%;">承諾事項執行項目</th>
												<th style="width: 32%;">企業承諾數值</th>
												<th style="width: 32%;">實際執行情形</th>
												<th style="width: 16%;">達成率</th>
											</tr>
											<c:forEach var="listBean" items="${crAry}">
												<tr class="trRight numpadding">
													<td>${CRType[listBean.type]}</td>
													<td><span class="numberFmt">${listBean.corpvalue}</span><input type="hidden" name="ORsType${listBean.type}" value="${listBean.corpvalue}" class="countReach"></td>
													<td><span class="numberFmt">${listBean.repvalue}</span><input type="hidden" name="RsType${listBean.type}" value="${listBean.repvalue}" class="countReach"></td>
													<td>
														<c:if test="${listBean.repvalue!=0&&listBean.corpvalue!=0}">
															<span class="numberFmt">${(listBean.repvalue/listBean.corpvalue)*100}</span>%
														</c:if>
													</td>
												</tr>
											</c:forEach>
										</table>
									</c:if>
								</c:when>
								<c:when test="${bean.type eq '02'}">
									<table class="formProj" style="text-align: center;">
										<tr>
										<c:forEach var="listBean" items="${crAry}">
											<th>${CRType[listBean.type]}</th>
										</c:forEach>
										</tr>
										<tr>
										<c:forEach var="listBean" items="${crAry}">
											<td><span class="numberFmt">${listBean.repvalue}</span>%</td>
										</c:forEach>
										</tr>
									</table>
								</c:when>
								<c:when test="${bean.type eq '03'}">
									<table class="formProj" style="width: 50%;text-align: center;">
										<c:forEach var="listBean" items="${crAry}">
										<tr>
											<th>${CRType[listBean.type]}</th>
											<td style="width: 50%;">
												<span class="numberFmt">${listBean.repvalue}</span>
											</td>
										</tr>
										</c:forEach>
									</table>
								</c:when>
							</c:choose>
						</c:if>
						<c:if test="${not empty detailBean}">
						<div style="height: 10px;"></div>
							<c:choose>
<c:when test="${bean.type eq '01'}">
								<strong style="margin:10px 10px;color:#222;">${typeName}-原承諾事項</strong>
								<div style="height: 10px;"></div>
									<table class="formProj" style="text-align: right;">
										<tr style="text-align: center;">
											<th style="width: 20%;">原承諾項目</th>
											<th style="width: 20%;">原承諾總計</th>
											<th style="width: 20%;">原承諾期間平均</th>
											<th style="width: 20%;">本期實際執行情形</th>
											<th style="width: 20%;">期間平均達成率</th>
										</tr>
										<c:forEach var="datailb" items="${detailBean}">
										<tr class="numpadding countTable">
											<td style="width: 20%;">
											<c:choose>
												<c:when test="${datailb.type eq '0101'}">國內投資計畫</c:when>
												<c:when test="${datailb.type eq '0102'}">機器設備及原物料採購</c:when>
												<c:when test="${datailb.type eq '0103'}">研發經費投入</c:when>
												<c:when test="${datailb.type eq '0104'}">人員聘僱</c:when>
											</c:choose>
											</td>
											<td><span class="numberFmt" id="tdown${datailb.type}">${datailb.value}</span></td>
											<td><span class="numberFmt" id="pdown${datailb.type}">${datailb.value/conutRN}</span></td>
											<td><span class="numberFmt" id="reach${datailb.type}"></span></td>
											<td><span class="numberFmt" id="reachRate${datailb.type}"></span></td>
										</tr>
										</c:forEach>
									</table>
</c:when>
<c:when test="${bean.type eq '02'}">
								<strong style="margin:10px 10px;color:#222;">${typeName}-列管年財務比率</strong>
								<div style="height: 10px;"></div>
								<table class="formProj" style="text-align: center;">
									<tr>
										<th>列入年度</th>
										<c:forEach var="datailb" items="${detailBean}">
											<c:if test="${datailb.isFinancial eq '1'}">
												<c:choose>
													<c:when test="${datailb.type eq '0201'}"><th>流動比率</th></c:when>
													<c:when test="${datailb.type eq '0202'}"><th>速動比率</th></c:when>
													<c:when test="${datailb.type eq '0203'}"><th>負債比率</th></c:when>
												</c:choose>
											</c:if>
										</c:forEach>
									</tr>									
									<tr>
										<c:forEach var="datailb" items="${detailBean}">
											<c:if test="${datailb.isFinancial eq '1'}">
												<c:if test="${datailb.type eq '0201'}"><td>${datailb.year}</td></c:if>
												<td><span class="numberFmt">${datailb.value}</span>%</td>
											</c:if>
										</c:forEach>
									</tr>									
								</table>
								<c:if test="${not empty detailEx}">
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
													<td>${dEx[0]}</td>
													<td><span class="numberFmt">${dEx[1]}</span>%</td>
													<td><span class="numberFmt">${dEx[2]}</span>%</td>
													<td><span class="numberFmt">${dEx[3]}</span>%</td>
											</tr>
											</c:forEach>
									</table>
								</c:if>
</c:when>
<c:when test="${bean.type eq '03'}">
								<strong style="margin:10px 10px;color:#222;">${typeName}-原承諾事項</strong>
								<div style="height: 10px;"></div>
								<table class="formProj" style="text-align: center;width: 50%;">
									<tr>
										<c:forEach var="datailb" items="${detailBean}">
											<c:if test="${datailb.type eq '03'}">
												<th style="width: 50%;">資金回饋總金額</th>
												<td style="width: 50%;"><span class="numberFmt">${datailb.value}</span></td>
											</c:if>
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
	</form>
	</fieldset>
</div>