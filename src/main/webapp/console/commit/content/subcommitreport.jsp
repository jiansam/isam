<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<style>
.numpadding td{
/* 	margin-right: 10px; */
	padding-right: 20px;
}
</style>
<script>
$(function(){
	$( "#tabs" ).tabs({heightStyle: "auto"});
	$( "#accordion" ).accordion({collapsible: true,active:false});
	setYearRPRange("${subbean.startYear}","${subbean.endYear}");
 	$(".myTable").dataTable(getOption());
	$("#reNoTable").dataTable().$("td:first-child").css('text-align','center');
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
	if("${subbean.type}"==="01"){
		changeOcommit("${subbean.endYear}");
		$("select[name='year']").change(function(){
			changeNowCount();
			changeOcommit($(this).val());
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
	$("#SentRP").click(function(){
		$("#CRForm").append("<input type='hidden' name='count' value='"+$("#nowCountRN").text()+"'/>");
		var flag=true;
		$("#CRForm input[type='text']").each(function(){
			if(isNaN(getRemoveCommaVal($(this).val()))){
				alert("請填數值");$(this).focus();
				flag=false;
				return false;
			};
		});
		if(flag){
			$("#CRForm").submit();
		}
	});
	if("${subinfo.editType}"==="edit"){
		var obj = $.parseJSON('${crAry}');
		$.each(obj,function(i,ele){
			var name="ORsType"+obj[i].type;
			var nameR="RsType"+obj[i].type;
			var cont=parseFloat(obj[i].corpvalue,10).toFixed(2).toString();
			cont = getInsertComma(cont);
			var contR=parseFloat(obj[i].repvalue,10).toFixed(2).toString();
			contR = getInsertComma(contR);
			$("input[name='"+name+"']").val(cont);
			$("input[name='"+nameR+"']").val(contR);
		});
		var yearxnow=parseInt("${crBean.year}",10);
		$("select[name='year']").find("option[value='"+yearxnow+"']").prop("selected",true);
		$("input[name='repType'][value='${crBean.repType}']").prop("checked",true);
		$("input[name='isConversion'][value='${crBean.isConversion}']").prop("checked",true);
		changeNowCount();
		$(".countReach").each(function(){
			changeReachPercent($(this).closest("tr"));
			if(startsWith($(this).prop("name"),"RsType")){
				getSumAndPercent($(this));
			}
		});
		changeOcommit("${crBean.year}");
		$("select[name='year']").hide();
		var x =$("label[for='repType${crBean.repType}']").text();
		$("#pleaseHide").empty().text(x);
	}
});
function changeNowCount(){
	var nowCount;
	var repType = "${subbean.repType}";
	var countRep=$("input[name='repType']:checked").val()==="0101"?1:2;
	var year=$("select[name='year']").val();
	if(repType==="01"){
		nowCount=(year-"${subbean.startYear}")*2+countRep;
	}else{
		nowCount=(year-"${subbean.startYear}")+1;
	}
	$("#nowCountRN").text(nowCount);
}
function changeReachPercent($thisItem){
	var $up = $thisItem.find("input[name^='RsType']");
	var $down = $thisItem.find("input[name^='ORsType']");
	var $target = $thisItem.find(".cpercent");
	getDivideText($up,$down,$target,"%");
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
function setYearRPRange(max,min){
	var startY = parseInt(max, 10);
	var endY = parseInt(min, 10);
	var sel="";
	for(var i=endY;i>=startY;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$("select[name='year']").html(sel);
}
function goToEditPage(serno,idno,investor){
	$("<div style='font-size='12px;''>您即將離開本頁面，未儲存項目將不會保存，請確認是否繼續?</div>").dialog({
		width: 350,
		modal:true,
		title:'確認離開',
		buttons: {
	        "確定": function() {
			  postUrlByForm('/console/commit/subcommitedit.jsp',{
				  'serno':'${subinfo.serno}','subserno':'${subbean.subserno}','investNo':'${subbean.investNo}','editType':'edit'});
	          $( this ).dialog( "close" );
	        },
	        "取消": function() {
	          $( this ).dialog( "close" );
	        }
		}
	});
}
function deleteSubCommitRP(){
	$("<div style='font-size='12px;''>您即將刪除本筆資料，請確認是否繼續?</div>").dialog({
		width: 350,
		modal:true,
		title:'確認刪除',
		buttons: {
	        "確定": function() {
			  postUrlByForm('/console/commit/editcommitreport.jsp',{'repSerno':'${subinfo.repserno}','idno':'${subinfo.idno}','editType':'delete','serno':'${subinfo.serno}','investor':'${subinfo.investor}','subserno':'${subbean.subserno}'});
	          $( this ).dialog( "close" );
	        },
	        "取消": function() {
	          $( this ).dialog( "close" );
	        }
		}
	});
}
function changeOcommit(year){
	$("#changeRepYear").text(year);
	var detailEx="${detailEx}";
	$(".countTable .RepYearC").each(function(){
		var type="#Year"+year+" .ref"+$(this).prop("id").replace("RepYear","");
		$(this).text($(type).text());
	});
}
</script>
<div>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="text-align:center;padding:5px;width:200px;white-space:nowrap;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
		<c:if test="${subinfo.editType eq 'add'}"><c:set var="typeEdit"  value="新增" /></c:if>
		<c:if test="${subinfo.editType eq 'edit'}"><c:set var="typeEdit"  value="修改" /></c:if>
		<span style="color:#F30;"><strong>&nbsp;${typeEdit}執行情形明細&nbsp;</strong>&nbsp;</span>
	</legend>
	<c:set var="typeName"  value="${subinfo.typeStr}" />
	<form  id="CRForm" action="<c:url value='/console/commit/editcommitreport.jsp'/>" method="post">
		<div id="tabs" class="tabs-bottom" style="font-size: 16px;margin-top: 20px;">
			<ul>
			    <li><a href="#tabs-1">${typeName}填報項目</a></li>
			    <li><a href="#tabs-2">檢視涉及文號</a></li>
		    	<li style="float: right">
		    		<input type="button" class="btn_class_opener" onclick="postUrlByForm('/console/commit/showcommitdetail.jsp',{'serno':'${subinfo.idno}'})" value="回企業明細編輯" />
		    	</li>
 			    <c:if test="${fn:contains(memberUrls,'E0102')}">
			    	<li style="float: right"><input type="button" onclick="goToEditPage()" class="myEdit btn_class_opener" value="編輯原承諾明細"/></li>
				</c:if>
			    <c:if test="${subinfo.editType eq 'edit'}">
			    	<li style="float: right"><input type="button" class="btn_class_opener" onclick="deleteSubCommitRP();" value="刪除紀錄" /></li>
			    </c:if>
			  </ul>
			 <div class="tabs-spacer"></div>
			 <div id="tabs-1">
				<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
					<legend style="border-top:1px solid #E6E6E6;margin-bottom: 0px;">
						<span style="color:#F30;">[&nbsp;Step1&nbsp;]</span>
						<strong style="color:#222;">${typeName}-填報執行情形</strong>
					</legend>
						<span class="basetable">
							統一編號：${subinfo.idno}
						</span>
						<span class="basetable">
						投資人名稱：
						<c:choose>
							<c:when test="${fn:length(subinfo.investor)>14}"><a title="${subinfo.investor}">${fn:substring(subinfo.investor,0,13)}...</a></c:when>
							<c:otherwise>${subinfo.investor}</c:otherwise>
						</c:choose>
						</span>
						<span class="basetable">
							收文文號：<input type="text" name="keyinNo" maxlength="11" value="${crBean.keyinNo}"/>
						</span><br/>
						<span class="basetable">
							投資案號：${subbean.investNo}
						</span>
						<span class="basetable">
							大陸事業名稱：${subinfo.investName}
						</span><br/>
						 <c:if test="${subinfo.editType eq 'edit'}">
						 	<span class="basetable">
								修改人：${subinfo.editor}
							</span>
							<span class="basetable">
								修改時間：${fn:substring(crBean.updatetime,0,16)}
							</span><br/>
						 </c:if>
						<span class="basetable">
							列管期間：${subbean.startYear}年~${subbean.endYear}年
						</span>
						<c:if  test="${subbean.type eq '01'}">
						<span class="basetable">
						<c:if test="${subbean.repType eq '01'}">
							<c:set var="conutRN" value="${((subbean.endYear-subbean.startYear)+1)*2}" />
							列管期數：<span id="nowCountRN">${(((empty crBean.year?subbean.endYear:crBean.year)-subbean.startYear)+1)*2}</span>/${((subbean.endYear-subbean.startYear)+1)*2}
						</c:if>
						<c:if test="${subbean.repType eq '0103'}">
							<c:set var="conutRN" value="${(subbean.endYear-subbean.startYear)+1}" />
							列管期數：<span id="nowCountRN">${((empty crBean.year?subbean.endYear:crBean.year)-subbean.startYear)+1}</span>/${(subbean.endYear-subbean.startYear)+1}
						</c:if>
						</span>
						</c:if>
						<span class="basetable">
						<c:if test="${not empty subbean.type}">
							<c:choose>
								<c:when test="${subbean.type eq '01'}">
										填報類型：
										<c:if test="${subbean.repType eq '01'}">
												<span id="pleaseHide">
												<input  name="repType" type="radio" value="0101" id="repType0101" checked="checked"/>
												<label for="repType0101">上半年</label>
												<input  name="repType"  type="radio" value="0102" id="repType0102"/>
												<label for="repType0102">下半年</label>
												</span>
										</c:if>
										<c:if test="${subbean.repType != '01'}">
												<input  name="repType" type="hidden" value="0103" id="repType0103" checked="checked"/>
												<label for="repType0103">年報</label>
										</c:if>
								</c:when>
								<c:when test="${subbean.type eq '02'}">
									列管財務類型：${codeStr}
								</c:when>
								<c:when test="${subbean.type eq '03'}">
									資金回饋幣別：${codeStr}
								</c:when>
							</c:choose>
						</c:if>
						</span>
						<span class="basetable">
							填報年度：
							<c:if test="${subinfo.editType != 'add'}">${crBean.year}年</c:if>
							<select style="width: 80px;" name="year">
							</select>
							<input type="hidden" name="restrainType" value="${subbean.type}"/>
							<input type="hidden" name="IDNO" value="${subinfo.idno}"/>
							<input type="hidden" name="editType" value="${subinfo.editType}"/>
							<input type="hidden" name="sernoStr" value="${subinfo.serno}"/>
							<input type="hidden" name="subserno" value="${subbean.subserno}"/>
							<input type="hidden" name="investor" value="${subinfo.investor}"/>
							<input type="hidden" name="repSerno" value="${subinfo.repserno}"/>
						</span>
						<c:if  test="${subbean.type eq '01'}">
						<br/>
							<span class="basetable">
							轉換幣別：<input type="radio" name="isConversion" id="isConversionY" value="Y"/>
									<label for="isConversionY">是</label>
									<input type="radio" name="isConversion" id="isConversionY" value="N" checked="checked"/>
									<label for="isConversionN">否</label>
							</span><br/>
							<span class="basetable">
							轉換備註：<textarea name="note" style="width: 65%;vertical-align: text-top;" cols="50" rows="5">${crBean.note}</textarea>
							</span>
						</c:if>
						<c:if test="${not empty subbean.note}">
						<br/>
						   <div class="basetable">
							   <div style="float: left; width: 6%;">說明：</div>
							   <div style="float: left;width: 90%;">
							   		${fn:substring(subbean.note,0,100)}
							   		<c:if test="${fn:length(subbean.note)>100}">...</c:if>
							   </div>
							   <div style="clear: both;"></div>
							   <c:if test="${fn:length(subbean.note)>100}">
							   <div id="accordion">
							   	<h3>詳細說明</h3>
							   		<div><pre>${subbean.note}</pre></div>
							   </div>
							   </c:if>
						   </div>
						</c:if>
						<br/><br/>
						<c:if test="${empty detailBean && subbean.type eq '01'}"><span class="basetable">必須先有管制項目，方可建置執行情形</span></c:if>
						<c:if test="${not empty detailBean}"><strong style="margin:10px 10px;color:#222;">${typeName}-填報年執行情形<c:if test="${subbean.type eq '01'}">（新台幣元）</c:if></strong></c:if>
						<div style="height: 10px;"></div>
						<c:if test="${not empty subbean.type}">
							<c:choose>
								<c:when test="${subbean.type eq '01'}">
									<c:if test="${not empty detailBean}">
										<table class="formProj countSRC">
											<tr style="text-align: center;">
												<th style="width: 20%;">承諾事項執行項目</th>
												<th style="width: 32%;">企業承諾數值</th>
												<th style="width: 32%;">實際執行情形</th>
												<th style="width: 16%;">達成率</th>
											</tr>
											<c:forEach var="datailb" items="${detailBean}">
												<c:if test="${datailb.total != '0'}">
												<c:choose>
													<c:when test="${datailb.type eq '0101'}">
														<tr class="trRight">
															<td style="padding-right: 10px;width: 20%;">長期投資</td>
															<td><input type="text" name="ORsType${datailb.type}01" style="width: 95%;" class="numberFmt countReach trRight"/></td>
															<td><input type="text" name="RsType${datailb.type}01" style="width: 95%;" class="numberFmt countReach trRight"/></td>
															<td><span class="cpercent"></span></td>
														</tr>
														<tr class="trRight">
															<td style="padding-right: 10px;width: 20%;">固定資產投資</td>
															<td><input type="text" name="ORsType${datailb.type}02" style="width: 95%;" class="lastNumberFmt numberFmt countReach trRight"/></td>
															<td><input type="text" name="RsType${datailb.type}02" style="width: 95%;" class="numberFmt countReach trRight"/></td>
															<td><span class="cpercent"></span></td>
														</tr>
													</c:when>
													<c:when test="${datailb.type eq '0102'}">
														<tr class="trRight">
															<td style="padding-right: 10px;width: 20%;">機器設備及原物料採購</td>
															<td><input type="text" name="ORsType${datailb.type}" style="width: 95%;" class="lastNumberFmt numberFmt countReach trRight"/></td>
															<td><input type="text" name="RsType${datailb.type}" style="width: 95%;" class="numberFmt countReach trRight"/></td>
															<td><span class="cpercent"></span></td>
														</tr>
													</c:when>
													<c:when test="${datailb.type eq '0103'}">
														<tr class="trRight">
															<td style="padding-right: 10px;width: 20%;">研發經費投入</td>
															<td><input type="text" name="ORsType${datailb.type}" style="width: 95%;" class="lastNumberFmt numberFmt countReach trRight"/></td>
															<td><input type="text" name="RsType${datailb.type}" style="width: 95%;" class="numberFmt countReach trRight"/></td>
															<td><span class="cpercent"></span></td>
														</tr>
													</c:when>
													<c:when test="${datailb.type eq '0104'}">
														<tr class="trRight">
															<td style="padding-right: 10px;width: 20%;">新增聘研發人力</td>
															<td><input type="text" name="ORsType${datailb.type}01" style="width: 95%;" class="lastNumberFmt numberFmt countReach trRight"/></td>
															<td><input type="text" name="RsType${datailb.type}01" style="width: 95%;" class="numberFmt countReach trRight"/></td>
															<td><span class="cpercent"></span></td>
														</tr>
														<tr class="trRight">
															<td style="padding-right: 10px;width: 20%;">新增聘工作機會</td>
															<td><input type="text" name="ORsType${datailb.type}02" style="width: 95%;" class="lastNumberFmt numberFmt countReach trRight"/></td>
															<td><input type="text" name="RsType${datailb.type}02" style="width: 95%;" class="numberFmt countReach trRight"/></td>
															<td><span class="cpercent"></span></td>
														</tr>
													</c:when>
												</c:choose>
												</c:if>
											</c:forEach>								
										</table>
									</c:if>
								</c:when>
								<c:when test="${bean.type eq '03'}">
									<table class="formProj" style="width: 50%;text-align: center;">
										<tr>
											<th style="width: 50%;">資金回饋金額</th>
											<td style="width: 50%;">
												<input type="text" name="RsType0301" class="numberFmt trRight"/>元
											</td>
										</tr>
									</table>
								</c:when>
							</c:choose>
						</c:if>
						<c:if test="${not empty detailBean}">
						<div style="height: 10px;"></div>
							<c:choose>
<c:when test="${subbean.type eq '01'}">
								<strong style="margin:10px 10px;color:#222;">${typeName}-原承諾事項（新台幣元）</strong>
								<div style="height: 10px;"></div>
									<table class="formProj" style="text-align: right;">
										<tr style="text-align: center;">
											<th style="width: 20%;">原承諾項目</th>
											<th style="width: 15%;">原承諾總計</th>
											<c:if test="${not empty detailEx}">
												<th style="width: 15%;">原承諾金額(<span id="changeRepYear">${subbean.endYear}</span>)</th>
											</c:if>
											<th style="width: 15%;">原承諾期間平均金額</th>
											<th style="width: 15%;">本期實際執行情形</th>
											<th style="width: 20%;">期間平均達成率</th>
										</tr>
										<c:forEach var="datailb" items="${detailBean}">
										<tr class="numpadding countTable">
											<c:if test="${datailb.total != '0'}">
											<td style="padding-right: 10px;width: 20%;">
												<c:choose>
													<c:when test="${datailb.type eq '0101'}">國內投資計畫</c:when>
													<c:when test="${datailb.type eq '0102'}">機器設備及原物料採購</c:when>
													<c:when test="${datailb.type eq '0103'}">研發經費投入</c:when>
													<c:when test="${datailb.type eq '0104'}">人員聘僱</c:when>
												</c:choose>
											</td>
											<td><span class="numberFmt" id="tdown${datailb.type}">${datailb.value}</span></td>
											<c:if test="${not empty detailEx}">
												<c:if test="${subbean.repType eq '01'}">
													<td>
														<span class="numberFmt RepYearC" id="RepYear${datailb.type}">
														</span>
													</td>
												</c:if>
												<c:if test="${subbean.repType != '01'}">
													<td>
														<span class="numberFmt RepYearC" id="RepYear${datailb.type}">.
														</span>
													</td>
												</c:if>
											</c:if>
											<td><span class="numberFmt" id="pdown${datailb.type}">${datailb.value/conutRN}</span></td>
											<td><span class="numberFmt" id="reach${datailb.type}"></span></td>
											<td><span class="numberFmt" id="reachRate${datailb.type}"></span></td>
											</c:if>
										</tr>
										</c:forEach>
										<c:if test="${subbean.repType eq '01'}">
											<tfoot><tr><th colspan="6" style="text-align: left;padding: 10px 5px 8px 5px;">註：填報類型為半年報時，顯示分年原承諾金額時，其值為該年度分年承諾金額之一半</th></tr></tfoot>
										</c:if>
									</table>
									<c:if test="${not empty detailEx}">
										<div style="display: none;">
											<c:forEach items="${detailEx}" var="byYear">
													<div id="Year${byYear.key}">
												<c:forEach items="${byYear.value}" var="byYearVal">
														<c:if test="${not empty byYearVal.value}">
														<c:if test="${subbean.repType eq '01'}">
															<span class="numberFmt ref${byYearVal.key}">${byYearVal.value/2}</span>
														</c:if>
														<c:if test="${subbean.repType != '01'}">
															<span class="numberFmt ref${byYearVal.key}">${byYearVal.value}</span>
														</c:if>
														</c:if>
												</c:forEach>	
													</div>
											</c:forEach>
										</div>
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
		</div>
			<div style="text-align: center;margin: 10px 0px 5px 0px;">
				<input type="button" id="SentRP" class="btn_class_Approval" style="font-size: 13px;" value="確認${typeEdit}" />
			</div>
	</form>
	</fieldset>
</div>