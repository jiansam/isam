<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<%
	response.setHeader("Cache-Control", "no-cache, no-store");
	response.setHeader("Pragma", "no-cache");
	long time = System.currentTimeMillis();
	response.setDateHeader("Last-Modified", time);
	response.setDateHeader("Date", time);
	response.setDateHeader("Expires", 0);
%>
<link href="<c:url value='/css/jquery.checktree/jquery.checktree.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/js/jquery.checktree.js'/>" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript">
	$(function(){
		$("input[name='pdfitem']").prop("checked",true);
		$("#btnPDF").click(function(){
			if($("input[name='pdfitem']:checked").length>0){
				$("#toPDF").submit();
			}else{
				alert("請至少選擇一項匯出項目，謝謝！");
			}
		})
		$( "#pdf-accordion" ).accordion({
			heightStyle: "content"
		}); 
		$('ul.tree').checkTree({
			labelAction: "check"
		});
		$("#btnFile").click(function(){
			if($(".zip:checked").length>0){
				$("#toFile").submit();
			}else{
				alert("請至少選擇一項匯出項目，謝謝！");
			}
		})
	});
</script>
<style>
.tbtitle0{
	background-color:#ececec;
	color:#222;
	font-weight:bold;
	font-size: 16px;
}
</style>
<div id="pdf-accordion">
	 <h3 class='tbtitle0'>匯出PDF報表</h3>
	 <div>
	 	 <div class='tbtitle'>選擇匯出項目</div>
		 <form id="toPDF" action='<c:url value="/downloadinvestdata.jsp"/>' method="post">
			<input type="hidden" name="investNo" value="${sysinfo.INVESTMENT_NO}">
			<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;"><input type="checkbox" name="pdfitem" value="0" id="pdfitem0"></td>
					<td><label for="pdfitem0">基本資料（包含執行情形、設立情形、設立日期、初次審定或備查日期、經營狀況、營業項目）</label></td>
				</tr>
				<tr>
					<td style="text-align: right;"><input type="checkbox" name="pdfitem" value="1" id="pdfitem1"></td>
					<td><label for="pdfitem1">陸資投資人資訊</label></td>
				</tr>
				<tr>
					<td style="text-align: right;"><input type="checkbox" name="pdfitem" value="2" id="pdfitem2"></td>
					<td><label for="pdfitem2">稽核資訊</label></td>
				</tr>
			<c:if test="${not empty reInvests}">
				<tr>
					<td style="text-align: right;"><input type="checkbox" name="pdfitem" value="3" id="pdfitem3"></td>
					<td><label for="pdfitem3">國內轉投資</label></td>
				</tr>
			</c:if>
				<tr>
					<td style="text-align: right;"><input type="checkbox" name="pdfitem" value="4" id="pdfitem4"></td>
					<td><label for="pdfitem4">聯絡資訊</label></td>
				</tr>
				<tr>
					<td style="text-align: right;"><input type="checkbox" name="pdfitem" value="5" id="pdfitem5"></td>
					<td><label for="pdfitem5">訪查資料</label></td>
				</tr>
				<tr>
					<td style="text-align: right;"><input type="checkbox" name="pdfitem" value="6" id="pdfitem6"></td>
					<td><label for="pdfitem6">財務資料</label></td>
				</tr>
			</table>
			<div style="margin-top: 10px;text-align: center;">
				<input type="button" value="匯出PDF" id="btnPDF" class="btn_class_Approval">
			</div>
		</form>
	</div>
	
	<h3 class='tbtitle0'>附件下載</h3>
	<div>
		<div class='tbtitle'>選擇匯出項目</div>
		
		<c:choose>
		
			<c:when test="${not empty noData}">${noData}</c:when>
			<c:otherwise>
			<form id="toFile" action='<c:url value="/console/cnfdi/OFIInvestZIP.view"/>' method="post">
	
				<ul class="tree" style="margin-left: 15px;">
					<c:if test="${not empty investorsZip}">
						<li>
							<input type="checkbox">
							<label>架構圖</label>
							<ul>
							<c:forEach var="data" items="${investorsZip}">
								<li><input class="zip" type="checkbox" name="investorFile" value="${data.INVESTOR_SEQ}"><label>${data.INVESTOR_CHTNAME}</label></li>
							</c:forEach>
							</ul>
							
						</li>
					</c:if>
					
					
					<c:if test="${not empty NTBTDatasZip}">
						<li>
							<input class="zip" type="checkbox" name="NTBTDatas" value="NTBT" >
							<label>財務資料下載</label>
						</li>
					</c:if>
					
					
					
					<c:if test="${not empty financialZip}">
						<li>
							<input type="checkbox">
							<label>財報申報資料-${sysinfo.COMP_CHTNAME}</label>
							<ul>
							<c:forEach var="data" items="${financialZip}">
								<li><input class="zip" type="checkbox" name="financial" value="${data.serno}" ><label>${data.reportyear}</label></li>
							</c:forEach>
							</ul>
						</li>
					</c:if>
					
					
					<c:if test="${not empty refinancialZip}">
						<c:forEach var="re" items="${reInvests}">
						
						<c:if test="${not empty refinancialZip[re.reInvestNo]}">
						<li>
							<input type="checkbox">
							<label>財報申報資料-(轉投資)${re.reinvestment}</label>
							<ul>
							<c:forEach var="data" items="${refinancialZip[re.reInvestNo]}">
								<li><input class="zip" type="checkbox" name="refinancial-${re.reInvestNo}" value="${data.serno}" ><label>${data.reportyear}</label></li>
							</c:forEach>
							</ul>
						</li>
						</c:if>
						
						</c:forEach>
					</c:if>				
					
				</ul>
				
				<div style="margin-top: 10px;text-align: center;">
					<input type="hidden" name="doTh" value="getFile">
					<input type="hidden" name="investNo" value="${sysinfo.INVESTMENT_NO}">
					<input type="hidden" name="cname" value="${sysinfo.COMP_CHTNAME}">
					<input type="button" value="下載檔案" id="btnFile" class="btn_class_Approval">
				</div>			
			</form>
			</c:otherwise>
		</c:choose>
        
	</div>
	

</div>