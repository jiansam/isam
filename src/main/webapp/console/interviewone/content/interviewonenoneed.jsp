<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript">
$(function(){
	/*設定選項預設值*/
	setDefalutOptionByClass($(".defClass"));
	/*設定選擇其他時，出現的文字框*/
	setListenerRT($(".RTCheckRadio"));
	setListenerRTText($(".RTCheckText input[type='text']"));
	if("${IOBaseInfo.editType}".length>0){
		setCheckboxToDefalut("noNeedReason","${ioclists.noNeedReason}");
	}
	$(".RTCheckText").each(function(){
		if($(this).find("input[type='text']").val().length>0){
			$(this).show();
		}
	});
});
</script>
<style>
.formProj a:hover{
color:#777777;
text-decoration: none;
}
.formProj a:link{
color:#777777;
text-decoration: none;
}
.formProj a:visited{
color:#777777;
text-decoration: none;
}
</style>
<div style="width: 100%;">
	<table class="formProj">
		<tr>
			<th colspan="4">經濟部<span>${IOyear}</span>年陸資在台事業訪視紀錄表</th>
		</tr>
		<tr>
			<td class="trRight" style="width: 18%;"><span style="color: red;">*</span>查訪日期：</td>
			<td style="width: 52%;">
				<input id="singledate" type="text" name="reportdate" value="${ibfn:addSlash(ioclists.reportdate)}"/>	
			</td>
			<td class="trRight" style="width: 12%;">經營狀態：</td>
			<td>
				<c:choose>
					<c:when test="${IOBaseInfo.reinvestNo==0 || empty IOBaseInfo.reinvestNo}">
						<a href='<c:url value='/console/showinvest.jsp?investNo=${IOBaseInfo.investNo}'/>' target="_blank" class='btn_class_opener'>${IOBaseInfo.isOperated}</a>
					</c:when>
					<c:otherwise>
						<a href='<c:url value='/console/showreinvest.jsp?investNo=${IOBaseInfo.investNo}&&reinvest=${IOBaseInfo.reinvestNo}'/>' target="_blank" class='btn_class_opener'>${IOBaseInfo.isOperated}</a>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<th colspan="4">未進行實地訪查原因</th>
		</tr>
		<tr>
			<td class="trRight">未訪視原因：</td>
			<td colspan="3">
				<span class="basetable"><input type="checkbox" name="noNeedReason" value="1" id="NNR1"/><label for="NNR1">1.在台無營業活動但未辦理停業</label></span>
				<span class="basetable"><input type="checkbox" name="noNeedReason" value="2" id="NNR2"/><label for="NNR2">2.停業中</label></span>
				<span class="basetable"><input type="checkbox" name="noNeedReason" value="3" id="NNR3"/><label for="NNR3">3.解散或撤銷或廢止或歇業</label></span><br>
				<span class="basetable"><input type="checkbox" name="noNeedReason" value="4" id="NNR4"/><label for="NNR4">4.實收資本額NT$3000萬元以上且已繳交財報</label></span>
				<span class="basetable"><input type="checkbox" name="noNeedReason" value="5" id="NNR5"/><label for="NNR5">5.投審會表示不需要訪查</label></span><br>
				<span class="basetable">
					<input type="checkbox" class="RTCheckRadio" name="noNeedReason" value="99" id="NNR99"/><label for="NNR99">6.其他</label>&nbsp;
					<span  class="RTCheckText" style="display: none;"><input type="text" name="noNeedOther" style="width: 400px;" value="${ioclists.noNeedOther}"/></span>
				</span>
			</td>
		</tr>
		<tr>
			<td class="trRight">未訪視說明：</td>
			<td colspan="3" style="text-align: center;">
				<textarea rows="5" style="width: 99%;" name="noNeed">${ioclists.noNeed}</textarea>
			</td>
		</tr>
	</table>		
</div>