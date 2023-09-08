<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.isam.service.*,com.isam.bean.*,java.util.*,com.isam.helper.*,com.isam.service.ofi.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<%
	session.removeAttribute("fbean");
	session.removeAttribute("fcbean");
	session.removeAttribute("frtOpt");
	session.removeAttribute("fbeanlast");
	session.removeAttribute("fcbeanlast");
	session.removeAttribute("lastyear");
	InterviewoneHelp opt = new InterviewoneHelp();
	OFIInvestNoXFContentService fcSer= new OFIInvestNoXFContentService();
	OFIInvestNoXFinancialService fSer = new OFIInvestNoXFinancialService();
	session.setAttribute("frtOpt", opt.getqTypeF());
	String serno=DataUtil.nulltoempty(request.getParameter("serno"));
	String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
	OFIInvestNoXFinancial fbean=fSer.selectBySerno(serno);
	String lastyear = DataUtil.addZeroForNum(String.valueOf(Integer.valueOf(fbean.getReportyear())-1),3);
	OFIInvestNoXFinancial fbeanlast=fSer.selectbean(investNo, lastyear, "0");
	session.setAttribute("fbean", fbean);
	session.setAttribute("fcbean", fcSer.selectBySerno(serno));
	session.setAttribute("fbeanlast", fbeanlast);
	session.setAttribute("lastyear", lastyear);
	if(fbeanlast!=null){
		session.setAttribute("fcbeanlast", fcSer.selectBySerno(fbeanlast.getSerno()));
	}
%>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<script type="text/javascript">
$(function(){
	setFormatInputDefault(".numberFmt",0);
	setFormatInputDefault(".numberPercent",2);
	countemp();
	if("${param.type}"==="show"){
		$(".numberFmt").each(function(){
			var v=$.trim($(this).text());
			if(v.length===0){
				$(this).text("-");
			}
		});
		$(".numberPercent").each(function(){
			var v=$.trim($(this).text());
			if(v.length===0){
				$(this).text("-");
			}
		});
	}
	$("#fPdf").click(function(){
		postUrlByForm('/cnfdi/donwloadfs.jsp',{'serno':"${param.serno}",'investNo':"${param.investNo}",'tt':"${param.tt}"});
	});
});
function countemp(){
	var c=0;
	var f=0;
	$(".emp .empsub").each(function(){
		var x=getValueText($(this));
		if(x.length>0){
			c+=parseFloat(getValueText($(this)));
			f++;
		}
	});
	if(f!=0){
		$(".emp .tt").text(getInsertComma(c,0));
	}
	$(".emp .numberFmt").each(function(){
		var v=$.trim($(this).text());
		if(v.length===0){
			$(this).text(" --");
		}
	});
}
</script>
<div style="text-align: right;padding-right: 15px;"><input type="button" id="fPdf" class="btn_class_loaddata" style="padding: 2 5 2 5;font-size: 14px;" value="下載"><span style="display: none;">${fbean.serno}</span></div>
<div style="font-size: 16x;text-align: center;font-weight: bold;">${param.tt}</div>
<div>
	<div class="trRight" style="font-size: 14px;padding-left:10px;float: left;">填報日期：${ibfn:addSlash(fbean.reportdate)}</div>
	<div class="trRight" style="padding-right: 10px;font-size: 14px;">單位：新台幣元；%</div>
</div>
<table class="formProj">
	<tr>
		<th rowspan="2" style="width: 14%;">科目</th>
		<th colspan="2" style="width: 26%;">金額</th>
		<th rowspan="2" style="width: 6%;">年度<br>成長率</th>
		<th rowspan="2" style="width: 14%;">科目</th>
		<th colspan="2" style="width: 26%;">金額</th>
		<th rowspan="2" style="width: 8%;">占營收<br>比率</th>
		<th rowspan="2" style="width: 6%;">年度<br>成長率</th>
	</tr>
	<tr>
		<th style="width: 13%;">${lastyear}</th>
		<th style="width: 13%;">${fbean.reportyear}</th>
		<th style="width: 13%;">${lastyear}</th>
		<th style="width: 13%;">${fbean.reportyear}</th>
	</tr>
	<tr class="trRight">
		<td>${frtOpt['58'].cName}：</td>
		<td><span class="numberFmt">${fcbeanlast['58']}</span></td>
		<td><span class="numberFmt">${fcbean['58']}</span></td>
		<td><span class="numberPercent">${ibfn:getGrowthRate(fcbean['58'],fcbeanlast['58'])}</span></td>
		<td>${frtOpt['61'].cName}：</td>
		<td><span class="numberFmt">${fcbeanlast['61']}</span></td>
		<td><span class="numberFmt">${fcbean['61']}</span></td>
		<td><span class="numberPercent">${ibfn:getPercent(fcbean['61'],fcbean['61'])}</span></td>
		<td><span class="numberPercent">${ibfn:getGrowthRate(fcbean['61'],fcbeanlast['61'])}</span></td>
	</tr>
	<tr class="trRight">
		<td>${frtOpt['59'].cName}：</td>
		<td><span class="numberFmt">${fcbeanlast['59']}</span></td>
		<td><span class="numberFmt">${fcbean['59']}</span></td>
		<td><span class="numberPercent">${ibfn:getGrowthRate(fcbean['59'],fcbeanlast['59'])}</span></td>
		<td>${frtOpt['62'].cName}：</td>
		<td><span class="numberFmt">${fcbeanlast['62']}</span></td>
		<td><span class="numberFmt">${fcbean['62']}</span></td>
		<td><span class="numberPercent">${ibfn:getPercent(fcbean['62'],fcbean['61'])}</span></td>
		<td><span class="numberPercent">${ibfn:getGrowthRate(fcbean['62'],fcbeanlast['62'])}</span></td>
	</tr>
	<tr class="trRight">
		<td>${frtOpt['60'].cName}：</td>
		<td><span class="numberFmt">${fcbeanlast['60']}</span></td>
		<td><span class="numberFmt">${fcbean['60']}</span></td>
		<td><span class="numberPercent">${ibfn:getGrowthRate(fcbean['60'],fcbeanlast['60'])}</span></td>
		<td>${frtOpt['63'].cName}：</td>
		<td><span class="numberFmt">${fcbeanlast['63']}</span></td>
		<td><span class="numberFmt">${fcbean['63']}</span></td>
		<td><span class="numberPercent">${ibfn:getPercent(fcbean['63'],fcbean['61'])}</span></td>
		<td><span class="numberPercent">${ibfn:getGrowthRate(fcbean['63'],fcbeanlast['63'])}</span></td>
	</tr>
	<tr class="trRight">
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td>${frtOpt['64'].cName}：</td>
		<td><span class="numberFmt">${fcbeanlast['64']}</span></td>
		<td><span class="numberFmt">${fcbean['64']}</span></td>
		<td><span class="numberPercent">${ibfn:getPercent(fcbean['64'],fcbean['61'])}</span></td>
		<td><span class="numberPercent">${ibfn:getGrowthRate(fcbean['64'],fcbeanlast['64'])}</span></td>
	</tr>
	<tr>
		<td class="trRight">${frtOpt['65'].cName}：</td>
		<td colspan="4">${fcbean['65']}</td>
		<td class="trRight">${frtOpt['66'].cName}：</td>
		<td colspan="3">${fcbean['66']}</td>
	</tr>
	<tr>
		<td class="trRight">${frtOpt['67'].cName}：</td>
		<td colspan="8"><pre>${fcbean['67']}</pre></td>
	</tr>
	<tr>
		<td class="trRight">${frtOpt['68'].cName}：</td>
		<td colspan="8"><pre>${fbean.note}</pre></td>
	</tr>
	<tr class="emp">
		<td class="trRight">國內事業員工人數：<br>（含派駐管理職）</td>
		<td colspan="8" style="text-align: left;">共計<span class="numberFmt tt"></span>&nbsp;人
		（其中${frtOpt['74'].cName}<span class="numberFmt empsub">${fcbean['74']}</span>&nbsp;人
		、${frtOpt['75'].cName}<span class="numberFmt empsub">${fcbean['75']}</span>&nbsp;人
		、${frtOpt['76'].cName}<span class="numberFmt empsub">${fcbean['76']}</span>&nbsp;人）。</td>
	</tr>
</table>
