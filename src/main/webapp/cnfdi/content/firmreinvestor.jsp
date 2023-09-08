<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<script>
$(function() {
	 $( "#accordion" ).accordion({
	      collapsible: true, heightStyle: "content"
	 });
});
</script>
<c:if test="${not empty reInvests}">
	<div id="accordion" style="font-size: medium;">
	<c:forEach var="re" items="${reInvests}">
		<h3>${re.reinvestment}</h3>
		<div>
			<div class='tbtitle'>基本資料</div>
			<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;">轉投資事業名稱：</td>
					<td colspan="5">${re.reinvestment}</td>
				</tr>
				<tr>
					<td style="text-align: right;width: 20%;">統一編號：</td>
					<td style="width: 15%;">${re.idno}</td>
					<td style="text-align: right;width: 10%;">組織型態：</td>
					<td style="width: 20%;">${optmap.orgType[re.orgType]}</td>
					<td style="text-align: right;width: 10%;">資料狀態：</td>
					<td>${optmap.isFilled[re.isFilled]}</td>
				</tr>
				<tr>
					<td style="text-align: right;">轉投資事業地址：</td>
					<td colspan="5">${IOLV1[re.city]}${IOLV2[re.town]}${re.addr}</td>
				</tr>
			</table>
			<div class='tbtitle'>轉投資營運狀態</div>
		<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;width: 20%;">設立日期：</td>
					<td style="width: 20%;"><c:if test="${re.isNew eq '2'}">新設</c:if>${ibfn:addSlash(re.setupdate)}<span>&nbsp;&nbsp;${re.setupnote}</span></td>
					<td style="text-align: right;width: 20%;">經營狀況：</td>
					<td>
						${optmap.isOperated[re.isOperated]}
						<c:if test="${not empty ibfn:addSlash(re.sdate)}">，${ibfn:addSlash(re.sdate)}~${ibfn:addSlash(re.edate)}			
						</c:if>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">登記資本額：</td>
					<td>
						<c:choose>
							<c:when test="${empty re.regiCapital}">尚無資料</c:when>
							<c:otherwise><span class="numberFmt" >${re.regiCapital}</span></c:otherwise>
						</c:choose>					
					</td>
					<td style="text-align: right;">實收資本額：</td>
					<td>
						<c:choose>
							<c:when test="${re.orgType eq '5'}">
								同登記資本額
							</c:when>
							<c:when test="${empty re.paidCapital}">
								尚無資料
								<%-- <c:choose>
									<c:when test="${re.orgType eq '1' || re.orgType eq '2'}">尚無資料</c:when>
									<c:otherwise>同登記資本額</c:otherwise>
								</c:choose> --%>
							</c:when>
							<c:otherwise><span class="numberFmt" >${re.paidCapital}</span></c:otherwise>
						</c:choose>		
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">登記股數：</td>
					<td>
						<c:choose>
							<c:when test="${empty re.stockNum}">
								<c:choose>
									<c:when test="${re.orgType eq '1' || re.orgType eq '2'}">尚無資料</c:when>
									<c:otherwise>無須填寫</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise><span class="numberFmt" >${re.stockNum}</span></c:otherwise>
						</c:choose>	
					</td>
					<td style="text-align: right;">面額：</td>
					<td>
						<c:choose>
							<c:when test="${empty re.faceValue}">
								<c:choose>
									<c:when test="${re.orgType eq '1' || re.orgType eq '2'}">尚無資料</c:when>
									<c:otherwise>無須填寫</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise><span class="numberFmt" >${re.faceValue}</span></c:otherwise>
						</c:choose>	
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">持有股數：</td>
					<td>
						<c:choose>
							<c:when test="${empty re.shareholding}">
								<c:choose>
									<c:when test="${re.orgType eq '1' || re.orgType eq '2'}">尚無資料</c:when>
									<c:otherwise>無須填寫</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise><span class="numberFmt" >${re.shareholding}</span></c:otherwise>
						</c:choose>
					</td>
				<c:set var="x" value=""/>
				<c:set var="xPercent" value=""/>
				<c:choose>
					<c:when test="${re.orgType ==1 || re.orgType==2}">
						<c:set var="x" value="${ibfn:toDouble(re.shareholding) *ibfn:toDouble(re.faceValue)}"/>
						<c:if test="${not empty re.paidCapital&& ibfn:toDouble(re.paidCapital)>0}">
							<c:set var="xPercent" value="${(ibfn:toDouble(re.shareholding) *ibfn:toDouble(re.faceValue))/ibfn:toDouble(re.paidCapital)*100}"/>
						</c:if>
					</c:when>
					<c:otherwise>
						<c:set var="x" value="${ibfn:toDouble(re.reinvestMoney)}"/>
						<c:if test="${not empty re.regiCapital && ibfn:toDouble(re.regiCapital)>0}">
							<c:set var="xPercent" value="${ibfn:toDouble(re.reinvestMoney)/ibfn:toDouble(re.regiCapital)*100}"/>
						</c:if>
					</c:otherwise>
				</c:choose>
					<td style="text-align: right;">持有股權/出資額：</td>
					<td>
						<c:choose>
							<c:when test="${empty x}">-</c:when>
							<c:otherwise><span class="numberFmt" >${x}</span></c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;">轉投資金額：</td>
					<td>
						<c:choose>
							<c:when test="${empty re.reinvestMoney}">尚無資料</c:when>
							<c:otherwise><span class="numberFmt prm" >${re.reinvestMoney}</span></c:otherwise>
						</c:choose>
					</td>
					<td style="text-align: right;width: 20%;">持有股權比例：</td>
					<td colspan="3">
						<c:choose>
							<c:when test="${empty xPercent}">異常(無資料)</c:when>
							<c:otherwise><span class="numberFmt" >${xPercent}</span>%</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			<div class='tbtitle'>營業項目</div>
			<table style="width: 100%;font-size: 16px;" class="tchange">
				<tr>
					<td style="text-align: right;width: 20%;vertical-align: text-top;">主要營業項目：</td>
					<td>
						<c:forEach var="item" items="${reInvestItems[re.reInvestNo]['1']}" varStatus="i">
							<c:if test="${i.index>0}"><br></c:if>${item}
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;vertical-align: text-top;">次要營業項目：</td>
					<td>
						<c:forEach var="item" items="${reInvestItems[re.reInvestNo]['2']}" varStatus="i">
							<c:if test="${i.index>0}"><br></c:if>${item}
						</c:forEach>
					</td>
				</tr>
				<tr>
					<td style="text-align: right;vertical-align: text-top;">涉及特許與特殊項目：</td>
					<td>
						<c:forEach var="item" items="${reInvestItems[re.reInvestNo]['0']}" varStatus="i">
							<c:if test="${i.index>0}"><br></c:if>${item}
						</c:forEach>
					</td>
				</tr>
			</table>
			<div class='tbtitle'>備註</div>
			<div style="padding-left: 20px;">
			<pre>${re.note}</pre>
			</div>
		</div>
	</c:forEach>
	</div>
</c:if>