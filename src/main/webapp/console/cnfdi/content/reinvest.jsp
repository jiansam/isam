<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<link href="<c:url value='/css/select2fix.css'/>" rel="stylesheet"/>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/hideOptionBySpan.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>

<script>
$(function() {
	setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
	inputTextNext("#myNext",".nextInput",".skip");
	setRedioToDefalut("isNew","${reBean.isNew}");
	setRedioToDefalut("isOperated","${reBean.isOperated}");
	setRedioToDefalut("isFilled","${reBean.isFilled}");
	setRedioToDefalut("orgType","${reBean.orgType}");
/* 	if("${reBean.city}".length>0){
		setSelectedToDefalut("city","${reBean.city}");
		setTWAddrChangeSpan($("#lev2"),".${reBean.city}");
		setSelectedToDefalut("town","${reBean.town}");
	} */
	checkActive($("input[name=isNew]:checked"),"1",$("tr").has("#singledate"));
	checkActive($("input[name=isOperated]:checked"),"2",$("tr").has("#fromL"));
	$("input[name=isNew]").change(function(){
		checkActive($("input[name=isNew]:checked"),"1",$("tr").has("#singledate"));
	});
	$("input[name=isOperated]").change(function(){
		checkActive($("input[name=isOperated]:checked"),"2",$("tr").has("#fromL"));
	});
	$("#mainSic").select2();
	$("#spSic").select2();
	$("#secondary").select2();
	$("#mainSic").select2("val","${sic['1']}");
	if("${sic['2']}".length>0){
		var ary2 ="${sic['2']}".split(",");
		 $("#secondary").select2("val",ary2);
	}
	if("${sic['0']}".length>0){
		var ary ="${sic['0']}".split(",");
		 $("#spSic").select2("val",ary);
	}
	
	var classNo="."+$("#lev1").find("option:eq(0)").val();
	setTWAddrChangeSpan($("#lev2"),classNo);
	setTWAddrSpan($("#lev1"),$("#lev2"));
	//getPercentText();
	$(".pDown,input[name=regicapital],input[name=faceValue]").blur(function(){
		checkSHV();
	}); 
	checkOrgType($("input[name=orgType]:checked"))
	checkSHV();
	$("input[name=orgType]").change(function(){
		checkOrgType($("input[name=orgType]:checked"));
		checkSHV();
	});
});

function checkSHV(){
	var val=parseInt($("input[name=orgType]:checked").val(),10);
	var up = getRemoveCommaVal($(".pUp").text());
	var pDown = getRemoveCommaVal($(".pDown").val());
	var d = getRemoveCommaVal($("input[name=regicapital]").val());
	var fv = getRemoveCommaVal($("input[name=faceValue]").val());
	var psh = getRemoveCommaVal($(".psh").text());
	var prm = getRemoveCommaVal($(".prm").text());
	psh=$.isNumeric(psh)?psh:"";
	var x="-";
	var xPercent="-";
	if(val===1||val===2){
		x= psh*fv;
		if(pDown!=0){
			xPercent=((x/pDown)*100).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
		}
		if(psh.length==0){
			$(".psh").text("尚無資料");
		}
	}else{
		x=prm;
		if(d!=0){
			xPercent=((x/d)*100).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
		}
		if(psh.length==0){
			$(".psh").text("無須填寫 ");
		}
	}
		$(".pUp").text(parseFloat(x,10).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"));
		if(xPercent==="-"){
			$(".pPercent").text("異常(無資料)");
		}else{
			$(".pPercent").text(xPercent+" %");
		}
		
}
function checkOrgType($item){
	var val=parseInt($item.val(),10);
	if(val===1||val===2){
		$(".notEqRg").show();
		$(".eqRg").hide();
	}else{
		$(".notEqRg").find("input").val("");
		$(".notEqRg").hide();
		$(".eqRg").show();
	}
}
function checkActive($item,hideValue,$hideItem){
	var val=$item.val();
	if(val===hideValue){
		$hideItem.show();
	}else{
		$hideItem.find("input").val("");
		$hideItem.hide();
	}
}
function getPercentText(){
	var down = getRemoveCommaVal($(".pDown").val());
	var up = getRemoveCommaVal($(".pUp").text());
	if(down.length>0&&up.length>0&&parseFloat(down,10)!=0&&parseFloat(up,10)!=0){
		var result = ((up/down)*100).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
		$(".pPercent").text(result+" %");
	}else{
		$(".pPercent").text("");
	}
}
</script>
		<h3>轉投資事業編輯項目</h3>
		<div id="myNext" class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			<div class='tbtitle'>基本資料</div>
			<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;">轉投資事業名稱：</td>
					<td colspan="3">${reBean.reinvestment}</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;">統一編號：</td>
					<td style="width: 20%;"><input type="text" name="idno" value="${reBean.idno}"></td>
					<td style="text-align: right;">資料狀態：</td>
					<td>
						<c:forEach var="newitem" items="${optmap.isFilled}" varStatus="i">
							<input type="radio" name="isFilled" id="isFilled${i.index}" value="${newitem.key}"><label for="isFilled${i.index}">${newitem.value}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;">組織型態：</td>
					<td colspan="3">
						<c:forEach var="newitem" items="${optmap.orgType}" varStatus="i">
							<input type="radio" name="orgType" id="orgType${i.index}" value="${newitem.key}"><label for="orgType${i.index}">${newitem.value}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td class="trRight">轉投資事業地址：</td>
					<td colspan="3">
						<jsp:include page="/includes/twaddr.jsp" flush="true">
							<jsp:param value="city" name="City"/>
							<jsp:param value="town" name="Town"/>
							<jsp:param value="addr" name="Addr"/>
							<jsp:param value="${reBean.addr}" name="AddrStr"/>
						</jsp:include>
					</td>
				</tr>
			</table>
			<div class='tbtitle'>轉投資營運狀態</div>
			<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;width: 20%;">設立情形：</td>
					<td colspan="3">
						<c:forEach var="newitem" items="${optmap.isNew}" varStatus="i">
							<input type="radio" name="isNew" id="isNew${i.index}" value="${newitem.key}"><label for="isNew${i.index}">${newitem.value}</label>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;">設立日期：</td>
					<td colspan="3">
						<input id="singledate" type="text" name="setupdate" value="${ibfn:addSlash(reBean.setupdate)}">
					</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;">設立備註：</td>
					<td colspan="3">
						<input type="text" name="setupnote" value="${reBean.setupnote}" style="width: 80%;">
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">經營狀況：</td>
					<td colspan="3">
						<c:forEach var="newitem" items="${optmap.isOperated}" varStatus="i">
							<input type="radio" name="isOperated" id="isOperated${i.index}" value="${newitem.key}"><label for="isOperated${i.index}">${newitem.value}</label>
						</c:forEach>			
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">停業起訖：</td>
					<td colspan="3">
						<label>起日：</label><input id="fromL" type="text" name="sdate" value="${ibfn:addSlash(reBean.sdate)}"><label>&nbsp;~&nbsp;迄日：</label>	
						<input id="toL" type="text" name="edate" value="${ibfn:addSlash(reBean.edate)}">
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">登記資本額：</td>
					<td><input class="numberFmt" type="text" name="regicapital" value="${reBean.regiCapital}"></td>
					
					<td style="text-align: right;"><span>實收資本額：</span></td>
					<td><span class="notEqRg"><input class="numberFmt pDown" type="text" name="paidcapital" value="${reBean.paidCapital}"></span><span class="eqRg">同登記資本額</span></td>
				</tr>
				<tr>
					<td style="text-align: right;">登記股數：</td>
					<td><span class="notEqRg"><input class="numberFmt" type="text" name="stockNum" value="${reBean.stockNum}"></span><span class="eqRg">無須填寫</span></td>
					<td style="text-align: right;">面額：</td>
					<td><span class="notEqRg"><input class="numberFmt" type="text" name="faceValue" value="${reBean.faceValue}"></span><span class="eqRg">無須填寫</span></td>
				</tr>
				<tr>
					<td style="text-align: right;">持有股數：</td>
					<td><span class="numberFmt psh" >${reBean.shareholding}</span></td>
					<td style="text-align: right;">持有股權/出資額：</td>
					<td><span class="numberFmt pUp" >${reBean.shareholdingValue}</span></td>
				</tr>
				<tr>
					<td style="text-align: right;">轉投資金額：</td>
					<td><span class="numberFmt prm" >${reBean.reinvestMoney}</span></td>
					<td style="text-align: right;width: 20%;">持有股權比例：</td>
					<td><span class="numberFmt pPercent"></span></td>
				</tr>
			</table>
			<div class='tbtitle'>營業項目</div>
			<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;width: 20%;">主要營業項目：</td>
					<td>
						<select name="mainSic" id="mainSic" style="width: 80%;">
							<option value="">無</option>
							<c:forEach var="item" items="${sicOption}" varStatus="i">
									<option value="${item.code}">${item.code}-${item.codename}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">次要營業項目：</td>
					<td>
						<select name="secondary" id="secondary" multiple="multiple" style="width: 80%;">
							<c:forEach var="item" items="${sicOption}" varStatus="i">
								<option value="${item.code}">${item.code}-${item.codename}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">涉及特許與特殊項目：</td>
					<td>
						<select name="spSic" id="spSic" multiple="multiple" style="width: 80%;">
							<c:forEach var="item" items="${sicOption}" varStatus="i">
								<c:if test="${item.isSP != ''}">
									<option value="${item.code}">${item.code}-${item.codename}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
			<div class='tbtitle'>備註</div>
			<div style="padding-left: 20px;">
				<textarea name="note" style="width: 70%;" rows="15">${reBean.note}</textarea>
			</div>
		</div>
