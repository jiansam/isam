<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>


<script>
$(function() {
	$("#myInsert").click(function(){
		if(checkUpdatedetail()){
			$("#myform").submit();
		}
	});
});
function checkUpdatedetail(){
	if($("input[name='isNew']:checked").val()==="1" && $("#singledate").val().length===0){
		alert("非新設，需填寫設立日期。");
		$("#singledate").focus();
		return false;
	}else if($("input[name='isOperated']:checked").val()==="2" && ($("#fromL").val().length===0 || $("#toL").val().length===0)){
		alert("停業中，需填寫停業起訖。");
		$("#from").focus();
		return false;
	}else if(($("input[name='orgType']:checked").val()==="1" || $("input[name='orgType']:checked").val()==="2")){
		var x=0;
		$("input").each(function(){
			if($.trim($(this).val()).length===0&&$(this).hasClass("numberFmt")){
				alert("組織型態為股份有限公司或股份有限公司(上市櫃)，需填寫實收資本額、股數及面額。");
				$(this).focus();
				x++;
				return false;
			}
		});
		if(x===0){
			return true;
		}
	}else{
		return true;
	}
}
</script>
<style>
.tbtitle{
	background-color:#ececec;color:#222;margin-left: 15px;margin-bottom: 5px;margin-top: 5px;;font-weight:bold;
}
</style>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;國內轉投資事業資料&nbsp;]</span>&nbsp;&nbsp;
					<span style="float: right ;">
						<a href="<c:url value='/console/showinvest.jsp?investNo=${sysinfo.INVESTMENT_NO}'/>" class="btn_class_opener">返回國內事業</a>
					</span>
				</legend>
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
					<div class='tbtitle'>國內事業基本資料</div>
					<table style="width: 100%;">
						<tr>
							<td style="text-align: right;width: 15%;">陸資案號：</td>
							<td>${sysinfo.prefix}${sysinfo.INVESTMENT_NO}</td>
							<td style="text-align: right;width: 15%;">國內事業名稱：</td>
							<td>${sysinfo.COMP_CHTNAME}</td>
						</tr>
						<tr>
							<td style="text-align: right;">統一編號：</td>
							<td >${sysinfo.BAN_NO}</td>
							<td style="text-align: right;">組織型態：</td>
							<td >${sysinfo.OrgTypeName}</td>
						</tr>
					</table>					
				</div>
					<div>
					<c:if test="${!fn:contains(memberUrls,'E0401') && reBean.updateuser!='admin'}">
						<jsp:include page="/console/cnfdi/content/rreinvest.jsp" flush="true" />
					</c:if>
					<c:if test="${fn:contains(memberUrls,'E0401') || reBean.updateuser=='admin'}">
						<!-- edit version -->
						<form id="myform" action="<c:url value='/console/editreinvest.jsp'/>" method="post">
							<jsp:include page="/console/cnfdi/content/reinvest.jsp" flush="true" />
							<input type="hidden" value="${reBean.reInvestNo}" name="reInvestNo">
							<input type="hidden" value="${sysinfo.INVESTMENT_NO}" name="investNo">
							<div style="text-align: center;margin-top: 10px;">
								<input type="button" id="myInsert" class="btn_class_Approval" value="儲存"/>
							</div>
						</form>						
					</c:if>
					</div>
			</fieldset>
		</div>