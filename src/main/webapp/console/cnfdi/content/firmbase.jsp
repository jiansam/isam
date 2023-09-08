<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript">
	$(function(){
		checkActive($("input[name=isNew]:checked"),"1",$("tr").has("#singledate").children("td:lt(2)"));
		checkActive($("input[name=isOperated]:checked"),"2",$("tr").has("#fromL"));
		$("input[name=isNew]").change(function(){
			checkActive($("input[name=isNew]:checked"),"1",$("tr").has("#singledate").children("td:lt(2)"));
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
	});
	function checkActive($item,hideValue,$hideItem){
		var val=$item.val();
		if(val===hideValue){
			$hideItem.show();
		}else{
			$hideItem.find("input").val("");
			$hideItem.hide();
		}
	}
</script>

<div class='tbtitle'>營運狀態</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;">資料狀態：</td>
		<td>
			<c:forEach var="newitem" items="${optmap.isFilled}" varStatus="i">
				<input type="radio" name="isFilled" id="isFilled${i.index}" value="${newitem.key}"><label for="isFilled${i.index}">${newitem.value}</label>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;">執行情形：</td>
		<td colspan="3">
			<c:forEach var="newitem" items="${optmap.active}" varStatus="i">
				<input type="radio" name="active" id="active${i.index}" value="${newitem.key}"><label for="active${i.index}">${newitem.value}</label>
			</c:forEach>
		</td>
	</tr>
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
		<td style="width: 20%;">
			<input id="singledate" class="singledate" type="text" name="setupdate" value="${ibfn:addSlash(opbean.setupdate)}">
		</td>
		<td style="text-align: right;width: 20%;">初次審定或備查日期：</td>
		<td>
			<input class="singledate" type="text" name="approvaldate" value="${ibfn:addSlash(opbean.approvaldate)}">
		</td>
	</tr>
	<tr>
		<td style="text-align: right;width: 20%;">設立備註：</td>
		<td colspan="3">
			<input type="text" name="setupnote" value="${opbean.setupnote}" style="width: 80%;">
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
		<td style="text-align: right;">停業起迄：</td>
		<td colspan="3">
			<label>起日：</label><input id="fromL" type="text" name="sdate" value="${ibfn:addSlash(opbean.sdate)}"><label>&nbsp;~&nbsp;迄日：</label>	
			<input id="toL" type="text" name="edate" value="${ibfn:addSlash(opbean.edate)}">
		</td>
	</tr>
	
</table>
<div class='tbtitle'>營業項目</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 20%;">主要營業項目：</td>
		<td colspan="5">
			<c:set var="sysdate" value="0"/>
			<select name="mainSic" id="mainSic" style="width: 100%;">
				<option value="">無</option>
				<c:forEach var="item" items="${sicOption}" varStatus="i">
					<c:if test="${sysdate eq '0' && i.index eq 0}">
						<optgroup label="系統資料">
					</c:if>
					<c:if test="${item.isSysData != '1' && sysdate eq '0'}">
						<c:if test="${i.index >0}"></optgroup></c:if>
						<c:set var="sysdate" value="1"/>
						<optgroup label="其他">
					</c:if>
						<option value="${item.code}">${item.code}-${item.codename}</option>
				</c:forEach>
				</optgroup>
			</select>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;">次要營業項目：</td>
		<td colspan="5">
			<c:set var="sysdate" value="0"/>
			<select name="secondary" id="secondary" multiple="multiple" style="width: 100%;">
				<c:forEach var="item" items="${sicOption}" varStatus="i">
					<c:if test="${sysdate eq '0' && i.index eq 0}">
						<optgroup label="系統資料">
					</c:if>
					<c:if test="${item.isSysData != '1' && sysdate eq '0'}">
						<c:if test="${i.index >0}"></optgroup></c:if>
						<c:set var="sysdate" value="1"/>
						<optgroup label="其他">
					</c:if>
						<option value="${item.code}">${item.code}-${item.codename}</option>
				</c:forEach>
				</optgroup>
			</select>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;">涉及特許與特殊項目：</td>
		<td colspan="5">
			<c:set var="sysdate" value="0"/>
			<c:set var="isSPC" value="0"/>
			<select name="spSic" id="spSic" multiple="multiple" style="width: 100%;">
				<c:forEach var="item" items="${sicOption}" varStatus="i">
					<c:if test="${fn:length(item.isSP)>0}">
						<c:if test="${sysdate == '0' && isSPC=='0'}">
							<optgroup label="系統資料">
							<c:set var="isSPC" value="1"/>
						</c:if>
						<c:if test="${item.isSysData != '1' && sysdate eq '0'}">
							<c:if test="${i.index >0 && isSPC=='1'}"></optgroup></c:if>
							<c:set var="sysdate" value="1"/>
							<optgroup label="其他">
						</c:if>
						<option value="${item.code}">${item.code}-${item.codename}</option>
					</c:if>
				</c:forEach>
				</optgroup>
			</select>
		</td>
	</tr>
</table>

<div class='tbtitle'>國稅局或財報登記營業項目</div>
<textarea name="firmXNTBTSic" style="width:70%; margin-left:15px;" rows="5">${opbean.firmXNTBTSic}</textarea>

<div class='tbtitle'>備註</div>
<textarea name="note" style="width: 70%;margin-left: 15px;" rows="15">${opbean.note}</textarea>
