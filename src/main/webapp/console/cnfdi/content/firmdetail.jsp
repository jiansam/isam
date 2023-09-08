<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<%@ taglib prefix="console" tagdir="/WEB-INF/tags/console/" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/setDatatable.js'/>"></script>
<%-- <script type="text/javascript" src="<c:url value='/js/setDatatableOld.js'/>"></script> --%>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<%-- <link href="<c:url value='/css/select2fix.css'/>" rel="stylesheet"/> --%>

<script>
$(function() {
    $("#tabs").tabs({
    	active:"${sysinfo.tabsNum}",
    	cache: false
    });
    setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
	setRedioToDefalut("isNew","${opbean.isNew}");
	setRedioToDefalut("isOperated","${opbean.isOperated}");
	setRedioToDefalut("active","${opbean.active}");
	setRedioToDefalut("isFilled","${opbean.isFilled}");
	
	$("#saveI").click(function(){
		if(checkUpdatedetail()){
			$("#Updatedetail").submit();
		}
	});
	$("#saveAudit").click(function(){
		$("#UpdateAudit").submit();
	});
});
function checkUpdatedetail(){
	if($("input[name=isNew]:checked").val()==="1" && $("#singledate").val().length===0){
		alert("非新設，需填寫設立日期。");
		$("#singledate").focus();
		return false;
	}else if($("input[name=isOperated]:checked").val()==="2" && ($("#fromL").val().length===0 || $("#toL").val().length===0)){
		alert("停業中，需填寫停業起訖。");
		$("#from").focus();
		return false;
	}else{
		return true;
	}
}
</script>
<style>
.tbtitle{
	background-color:#ececec;color:#222;margin-left: 15px;margin-bottom: 5px;margin-top: 5px;;font-weight:bold;
}
.select2-container-multi .select2-choices .select2-search-choice{
	padding:0px 5px 0px 15px !important;;
}
.ui-tabs .ui-tabs-nav li a{
	padding: .7em .7em;
}
</style>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;編輯國內事業資料&nbsp;]</span>&nbsp;&nbsp;
					<span style="float: right ;">
						<a href="<c:url value='/cnfdi/listapproval.jsp?fbtype=b'/>" class="btn_class_opener">返回列表</a>
					</span>
				</legend>
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
					<!-- <div style="color:#222;margin-left: 15px;margin-bottom: 5px;">基本資料</div> -->
					<table style="width: 100%;">
						<tr>
							<td style="text-align: right;width: 15%;">陸資案號：</td>
							<td>${sysinfo.prefix}${sysinfo.INVESTMENT_NO}</td>
							<td style="text-align: right;width: 15%;">公司名稱：</td>
							<td colspan="3" style="width: 55%;">${sysinfo.COMP_CHTNAME}</td>
						</tr>
						<tr>
							<td style="text-align: right;">統一編號：</td>
							<td >${sysinfo.BAN_NO}</td>
							<td style="text-align: right;">組織型態：</td>
							<td >${sysinfo.OrgTypeName}</td>
							<td style="text-align: right;">發行方式：</td>
							<td>${sysinfo.issueTypeName}</td>
						</tr>
						<tr>
							<td style="text-align: right;">核准日期：</td>
							<td >${ibfn:addSlash(opbean.respdate)}</td>
							<td style="text-align: right;">核准文號：</td>
							<td>${IReceiveNo.receiveNo}</td>
							<td style="text-align: right;">承辦人：</td>
							<td style="width: 20%;">${IReceiveNo.pname}</td>
						</tr>
						 <tr>
							<td style="text-align: right;">陸資投資事業：</td>
							<td>${optmap.isCNFDI[opbean.isCNFDI]}</td>
							<td style="text-align: right;">國內轉投資：</td>
							<td >${empty reInvests?'否':'是'}</td>
							<td style="text-align: right;">經營身分：</td>
							<td>${sysinfo.inSrc}</td>
						</tr> 
					</table>					
				</div>
				<div id="tabs" style="font-size: 16px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">基本資料</a></li>
					    <li><a href="#tabs-2">編輯聯絡資訊</a></li>
					    <li><a href="#tabs-6">稽核資訊</a></li>
					    <c:if test="${not empty reInvests}">
					    	<li><a href="#tabs-4">國內轉投資</a></li>
					    </c:if>
					    <li><a href="#tabs-3">股東名冊</a></li>
					    <li><a href="#tabs-7">財務簡表</a></li>
					    <li><a href="#tabs-10">財務資料下載</a></li>
					    <li><a href="#tabs-5">訪查資料</a></li>
					    <li><a href="#tabs-8">管理密度</a></li>
<!-- 					    <li><a href="#tabs-9">匯出PDF</a></li> -->
					    <li><a href="${pageContext.request.contextPath}/console/cnfdi/OFIInvestZIP.view?doTh=getItem&investNo=${sysinfo.INVESTMENT_NO}">匯出PDF</a></li>
					  </ul>
					  <div id="tabs-1">
					  	<c:if test="${!fn:contains(memberUrls,'E0401') && opbean.updateuser!='admin'}"><jsp:include page="/console/cnfdi/content/rfirmbase.jsp" flush="true" /></c:if>
					  	<c:if test="${fn:contains(memberUrls,'E0401') || opbean.updateuser=='admin'}">
						  	<form id="Updatedetail" action='<c:url value="/console/cnfdi/updateinvest.jsp"/>' method="post">
								<jsp:include page="/console/cnfdi/content/firmbase.jsp" flush="true" />
								<input type="hidden" name="investNo" value="${sysinfo.INVESTMENT_NO}">
								<div style="margin-top: 10px;text-align: center;"><input type="button" value="儲存" id="saveI" class="btn_class_Approval"></div>
							</form>
						</c:if>
					  </div>
					  <div id="tabs-2">
						<jsp:include page="/console/cnfdi/content/contact.jsp" flush="true" />
					  </div>
					  <div id="tabs-6">
					  	<c:choose>
					  		<c:when test="${fn:contains(memberUrls,'E0401') || sysinfo.isAudit !='1'}">
					  			<form id="UpdateAudit" action='<c:url value="/console/cnfdi/updateaudit.jsp"/>' method="post">
								<jsp:include page="/console/cnfdi/content/firmaudit.jsp" flush="true" />
									<input type="hidden" name="investNo" value="${sysinfo.INVESTMENT_NO}">
									<div style="margin-top: 10px;text-align: center;"><input type="button" value="儲存" id="saveAudit" class="btn_class_Approval"></div>
								</form>
					  		</c:when>
					  		<c:otherwise>
					  			<jsp:include page="/console/cnfdi/content/rfirmaudit.jsp" flush="true" />
					  		</c:otherwise>
					  	</c:choose>
					  </div>
					  <c:if test="${not empty reInvests}">
						  <div id="tabs-4">
							<jsp:include page="/console/cnfdi/content/firmreinvestor.jsp" flush="true" />
						  </div>
					  </c:if>
					  <div id="tabs-3">
						<jsp:include page="/console/cnfdi/content/firminvestor.jsp" flush="true" />
					  </div>
					  <div id="tabs-7">
						<jsp:include page="/console/cnfdi/content/firmfinancial.jsp" flush="true" />
					  </div>
					  <div id="tabs-10">
						<console:OFI_NTBTDatas />
					  </div>
					  <div id="tabs-5">
						<jsp:include page="/console/cnfdi/content/firmwarning.jsp" flush="true" />
					  </div>
					  <div id="tabs-8">
						<jsp:include page="/console/cnfdi/content/firmscore.jsp" flush="true" />
					  </div>
<!-- 					  <div id="tabs-9"> -->
<%-- 						<jsp:include page="/console/cnfdi/content/firmpdf.jsp" flush="true" /> --%>
<!-- 					  </div> -->
				</div>
			</fieldset>
		</div>
<console:uploadDiv />	
