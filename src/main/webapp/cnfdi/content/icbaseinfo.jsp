<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script>
$(function() {
	 $("#cbtn").click(function(){
		 postUrlByForm('/showapproval.jsp',{'investNo':'${icase.investNo}'});
	})
});
</script>
  	<div>
  	<div class='tbtitle'>投資事業基本資料</div>
	<table style="width: 100%;font-size: 16px;" class="tchange">
		<c:if test="${not empty agent[icase.caseNo]}">
		<tr>
			<td style="text-align: right;">代理人：</td>
			<td>${agent[icase.caseNo].IN_AGENT}&nbsp;${agent[icase.caseNo].POSITION_NAME}</td>
			<td style="text-align: right;">聯絡電話：</td>
			<td>${agent[icase.caseNo].TEL_NO}</td>
		</tr>
		</c:if>
		<c:if test="${not empty contacts[icase.caseNo]}">
			<c:forEach var="citem" items="${contacts[icase.caseNo]}">
				<tr>
					<td style="text-align: right;">聯絡人：</td>
					<td>${citem.name}</td>
					<td style="text-align: right;">聯絡電話：</td>
					<td>${citem.telNo}</td>
				</tr>
			</c:forEach>
		</c:if>
	</table>
  		<input type="button" value="國內事業詳細資料" id="cbtn" class="btn_class_opener">
	</div>
