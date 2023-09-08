<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>

<script>
$(function() {
	$("#cnCode").select2();
	if(parseInt("${ofiiobean.nation}",10)===142){
		$(".cnSelect").show();
		$("#cnCode").select2("val","${ofiiobean.cnCode}");
	}
});
</script>
<c:if test="${!fn:contains(memberUrls,'E0401') && ofiiobean.updateuser!='admin'}">
<!-- view version -->
<div class='tbtitle'>基本資料</div>
<table style="width: 98%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;">資料狀態：</td>
		<td colspan="3">${optmap.isFilled[ofiiobean.isFilled]}</td>			
	</tr>
	<tr>
		<td style="text-align: right;width: 20%;">投資人國別：</td>				
		<td style="width: 20%;">${optmap.nation[ofiiobean.nation]}</td>				
		<td style="text-align: right;width: 10%;"><div class="cnSelect" style="display: none;">省分：</div></td>				
		<td>${optmap.cnCode[ofiiobean.cnCode]}</td>			
	</tr>
</table>
<div class='tbtitle'>備註</div>
<div style="width: 98%;margin-left: 15px;">
	<pre>${ofiiobean.note}</pre>
</div>
</c:if>
<c:if test="${fn:contains(memberUrls,'E0401') || ofiiobean.updateuser=='admin'}">
<!-- edit version -->
<form id="Updatedetail" action='<c:url value="/console/updateinvestor.jsp"/>' method="post">
<input type="hidden" name="investorSeq" value="${ofiiobean.investorSeq}">
<div class='tbtitle'>基本資料</div>
<table style="width: 98%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;">資料狀態：</td>
		<td colspan="3">
			<c:forEach var="newitem" items="${optmap.isFilled}" varStatus="i">
				<input type="radio" name="isFilled" id="isFilled${i.index}" value="${newitem.key}"><label for="isFilled${i.index}">${newitem.value}</label>
			</c:forEach>
		</td>			
	</tr>
	<tr>
		<td style="text-align: right;width: 20%;">投資人國別：</td>				
		<td style="width: 20%;">${optmap.nation[ofiiobean.nation]}</td>				
		<td style="text-align: right;width: 10%;"><div class="cnSelect" style="display: none;">省分：</div></td>				
		<td>
			<div class="cnSelect" style="display: none;">
			<select id="cnCode" name="cnCode" style="width: 250px;">
				<option value="">不拘</option>
				<c:forEach var="item" items="${optmap.cnCode}">
					<option value="${item.key}">${item.value}</option>
				</c:forEach>
			</select>
			</div>
		</td>				
	</tr>
</table>
<div class='tbtitle'>投資情形</div>

<table  class="formProj">
<tr>
<th width="40%">
國內事業名稱
</th>
<th>
核准金額</th>
<th>
審定金額
</th>
<th>審定股數
</th>
</tr>
<c:forEach var="item" items="${icase}" varStatus="i">
<tr>
<td>
${item.cname}
</td>
<td>
<input type="hidden" name="ids" value="${item.investNo }"/>
<input type="text" name="ilmoney1" value="${item.ilmoney1} "/>

</td>
<td>
<input type="text" name="ilmoney2" value="${item.ilmoney2} "/>

</td>
<td>
<input type="text" name="ilstockimp" value="${item.ilstockimp} "/>

</td>
</tr>
</c:forEach>


<tr>
<td colspan="4">
原系統資料庫資料
</td>
</tr>

<tr>
<th>
國內事業名稱
</th>
<th>
核准金額</th>
<th>
審定金額
</th>
<th>審定股數
</th>
</tr>
<c:forEach var="item" items="${icase}" varStatus="i">
<tr>
<td>
${item.cname}
</td>
<td>

<label>${item.money1}</label>


</td>
<td>
<label>${item.money2}</label>


</td>
<td>
<label>${item.stockimp}</label>


</td>
</tr>
</c:forEach>

</table>
<div class='tbtitle'>備註</div>
<div style="width: 98%;margin-left: 15px;">
	<textarea name="note" style="width: 70%;" rows="10">${ofiiobean.note}</textarea>
</div>
</form>
<div style="margin-top: 10px;text-align: center;"><input type="button" value="儲存" id="saveI" class="btn_class_Approval"></div>
</c:if>
<div style="height: 10px;"></div>
