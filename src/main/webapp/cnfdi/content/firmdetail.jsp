<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cnfdi" tagdir="/WEB-INF/tags/cnfdi/" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/setDatatable.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script>
$(function() {
    $("#tabs").tabs();
    setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
});

</script>
<style>
.tbtitle{
	background-color:#ececec;color:#222;margin-left: 15px;margin-bottom: 5px;margin-top: 5px;;font-weight:bold;
}
.ui-tabs .ui-tabs-nav li a{
	padding: .7em .7em;
}
</style>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;國內事業資料&nbsp;]</span>&nbsp;&nbsp;
					<span style="float: right ;">
						<a href="<c:url value='/cnfdi/listapproval.jsp'/>" class="btn_class_opener">返回列表</a>
					</span>
				</legend>
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
					<!-- <div style="color:#222;margin-left: 15px;margin-bottom: 5px;">基本資料</div> -->
					<table style="width: 100%;">
						<tr>
							<td style="text-align: right;width: 13%;">投資案號：</td>
							<td>${sysinfo.prefix}${sysinfo.INVESTMENT_NO}</td>
							<td style="text-align: right;width: 10%;">公司名稱：</td>
							<td colspan="3" style="width: 60%;">${sysinfo.COMP_CHTNAME}</td>
						</tr>
						<tr>
							<td style="text-align: right;">統一編號：</td>
							<td>${sysinfo.BAN_NO}</td>
							<td style="text-align: right;">組織型態：</td>
							<td>${sysinfo.OrgTypeName}</td>
							<td style="text-align: right;">發行方式：</td>
							<td>${sysinfo.issueTypeName}</td>
						</tr>
						<tr>
							<td style="text-align: right;">陸資投資事業：</td>
							<td>${optmap.isCNFDI[opbean.isCNFDI]}</td>
							<td style="text-align: right;">國內轉投資：</td>
							<td>${empty reInvests?'否':'是'}</td>
							<td style="text-align: right;width: 15%;">資料狀態：</td>
							<td>${optmap.isFilled[opbean.isFilled]}</td>
						</tr>
					</table>					
				</div>
				<div id="tabs" style="font-size: 16px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">營運資料</a></li>
					    <li><a href="#tabs-2">聯絡資訊</a></li>
					    <li><a href="#tabs-6">稽核</a></li>
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
						<jsp:include page="/cnfdi/content/firmbase.jsp" flush="true" />
					  </div>
					  <div id="tabs-2">
						<jsp:include page="/cnfdi/content/contact.jsp" flush="true" />
					  </div>
					  <div id="tabs-6">
						<jsp:include page="/cnfdi/content/firmaudit.jsp" flush="true" />
					  </div>
					   <c:if test="${not empty reInvests}">
					  <div id="tabs-4">
						<jsp:include page="/cnfdi/content/firmreinvestor.jsp" flush="true" />
					  </div>
					  </c:if>
					  <div id="tabs-3">
						<jsp:include page="/cnfdi/content/firminvestor.jsp" flush="true" />
					  </div>
					  <div id="tabs-7">
						<jsp:include page="/cnfdi/content/firmfinancial.jsp" flush="true" />
					  </div>
					  <div id="tabs-10">
						<cnfdi:OFI_NTBTDatas />
					  </div>
					  <div id="tabs-5">
						<jsp:include page="/cnfdi/content/firmwarning.jsp" flush="true" />
					  </div>
					  <div id="tabs-8">
						<jsp:include page="/cnfdi/content/firmscore.jsp" flush="true" />
					  </div>
<!-- 					   <div id="tabs-9"> -->
<%-- 						<jsp:include page="/console/cnfdi/content/firmpdf.jsp" flush="true" /> --%>
<!-- 					  </div> -->
				</div>
			</fieldset>
		</div>