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
	checkSHV();
	checkOrgType();
});

function checkSHV(){
	var val=parseInt("${reBean.orgType}",10);
	var up = getRemoveCommaVal($(".pUp").text());
	var pDown = getRemoveCommaVal($(".pDown").text());
	var d = getRemoveCommaVal("${reBean.regiCapital}");
	var fv = getRemoveCommaVal("${reBean.faceValue}");
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
function checkOrgType(){
	var val=parseInt("${reBean.orgType}",10);
	if(val===1||val===2){
		$(".notEqRg").show();
		$(".eqRg").hide();
	}else{
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
		<h3>轉投資事業項目</h3>
		<div id="myNext" class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			<div class='tbtitle'>基本資料</div>
			<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;">轉投資事業名稱：</td>
					<td colspan="3">${reBean.reinvestment}</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;">統一編號：</td>
					<td style="width: 20%;">${reBean.idno}</td>
					<td style="text-align: right;">資料狀態：</td>
					<td>${optmap.isFilled[reBean.isFilled]}</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;">組織型態：</td>
					<td colspan="3">${optmap.orgType[reBean.orgType]}</td>
				</tr>
				<tr>
					<td style="text-align: right;">轉投資事業地址：</td>
					<td colspan="3">
						${IOLV1[reBean.city]}${IOLV2[reBean.town]}${reBean.addr}
					</td>
				</tr>
			</table>
			<div class='tbtitle'>轉投資營運狀態</div>
			<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;width: 20%;">設立情形：</td>
					<td colspan="3">${optmap.isNew[reBean.isNew]}</td>
				</tr>
				<c:if test="${not empty reBean.setupdate}">
				<tr>
					<td style="text-align: right;width: 20%;">設立日期：</td>
					<td colspan="3">${ibfn:addSlash(reBean.setupdate)}</td>
				</tr>
				</c:if>
				<tr>
					<td style="text-align: right;width: 20%;">設立備註：</td>
					<td colspan="3">${reBean.setupnote}</td>
				</tr>
				<tr>
					<td style="text-align: right;">經營狀況：</td>
					<td colspan="3">${optmap.isOperated[reBean.isOperated]}</td>
				</tr>
				<c:if test="${not empty reBean.sdate}">
					<tr>
						<td style="text-align: right;">停業起訖：</td>
						<td colspan="3">${ibfn:addSlash(reBean.sdate)}&nbsp;~&nbsp;${ibfn:addSlash(reBean.edate)}
						</td>
					</tr>
				</c:if>
				<tr>
					<td style="text-align: right;">登記資本額：</td>
					<td><span class="numberFmt">${reBean.regiCapital}</span></td>
					
					<td style="text-align: right;"><span>實收資本額：</span></td>
					<td><span class="notEqRg"><span class="numberFmt pDown">${reBean.paidCapital}</span></span><span class="eqRg">同登記資本額</span></td>
				</tr>
				<tr>
					<td style="text-align: right;">登記股數：</td>
					<td><span class="notEqRg"><span class="numberFmt">${reBean.stockNum}</span></span><span class="eqRg">無須填寫</span></td>
					<td style="text-align: right;">面額：</td>
					<td><span class="notEqRg"><span class="numberFmt">${reBean.faceValue}</span></span><span class="eqRg">無須填寫</span></td>
				</tr>
				<tr>
					<td style="text-align: right;">持有股數：</td>
					<td><span class="numberFmt psh">${reBean.shareholding}</span></td>
					<td style="text-align: right;">持有股權/出資額：</td>
					<td><span class="numberFmt pUp">${reBean.shareholdingValue}</span></td>
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
					<td style="text-align: right;width: 20%;vertical-align: text-top;">主要營業項目：</td>
					<td colspan="5">
						<c:forEach var="item" items="${sic['1']}" varStatus="i">
							<c:if test="${i.index>0}"><br></c:if>${item}-${sicMap[item]}
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;vertical-align: text-top;">次要營業項目：</td>
					<td colspan="5">
						<c:forEach var="item" items="${sic['2']}" varStatus="i">
							<c:if test="${i.index>0}"><br></c:if>${item}-${sicMap[item]}
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;vertical-align: text-top;">涉及特許與特殊項目：</td>
					<td colspan="5">
						<c:forEach var="item" items="${sic['0']}" varStatus="i">
							<c:if test="${i.index>0}"><br></c:if>${item}-${sicMap[item]}
						</c:forEach>
					</td>
				</tr>
			</table>
			<div class='tbtitle'>備註</div>
			<div style="padding-left: 20px;">
				<pre>${reBean.note}</pre>
			</div>
		</div>
