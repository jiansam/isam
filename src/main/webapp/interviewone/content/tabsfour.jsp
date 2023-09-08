<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<c:if test="${empty SMap.dsFinancial}">暫無資料</c:if>
<c:if test="${not empty SMap.dsFinancial}">
<script>
  $(function() {
    $( "#accordion" ).accordion({ heightStyle: "fill" });
    setFormatInputDefault(".numberFmt",2);
  });
</script>
<div id="accordion">
	<c:forEach var="item" items="${fn:split(SMap.dsFinancial,',')}" varStatus="i">
		<c:set var="SE" value="SE_${item}" />
		<c:set var="asset" value="asset_${item}" />
		<c:set var="NI" value="NI_${item}" />
		<c:set var="cost" value="cost_${item}" />
		<c:set var="expense" value="expense_${item}" />
		<c:set var="NP" value="NP_${item}" />
		<fmt:parseNumber var="SEr" type="number" value="${SMap[SE]}"/>
		<fmt:parseNumber var="assetr" type="number" value="${SMap[asset]}"/>
		<fmt:parseNumber var="NIr" type="number" value="${SMap[NI]}"/>
		<fmt:parseNumber var="costr" type="number" value="${SMap[cost]}"/>
		<fmt:parseNumber var="expenser" type="number" value="${SMap[expense]}"/>
		<fmt:parseNumber var="NPr" type="number" value="${SMap[NP]}"/>
		
		<fmt:parseNumber var="SElr" type="number" value="${LastMap.SE}"/>
		<fmt:parseNumber var="assetlr" type="number" value="${LastMap.asset}"/>
		<fmt:parseNumber var="NIlr" type="number" value="${LastMap.NI}"/>
		<fmt:parseNumber var="costlr" type="number" value="${LastMap.cost}"/>
		<fmt:parseNumber var="expenselr" type="number" value="${LastMap.expense}"/>
		<fmt:parseNumber var="NPlr" type="number" value="${LastMap.NP}"/>
		<c:if test="${SEr<0}">
			<c:set var="lessZero" value="1"/>
		</c:if>
		<h3>${optionValName.dsFinancial[item]}</h3>
		<div>
			<table class="formProj">
				<tr>
					<th colspan="7">資產負債表部分(填寫之數字為該科目之合計數)</th>
				</tr>
				<tr class="trRight">
					<td style="width: 30%;">年底股東(業主)權益：</td>
					<td style="width: 15%;">新台幣</td>
					<td style="width: 20%;">
					<span class="numberFmt" <c:if test="${lessZero eq 1}">style="color:red;"</c:if> >${SMap[SE]}</span></td>
					<td class="trLeft">元</td>
					<td>成長率：</td>
					<td>
						<c:choose>
							<c:when test="${not empty SElr&&SElr>0}"><span class="numberFmt">${(SEr-SElr)/cfn:abs(SElr)*100}</span></c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class="trLeft">%</td>
				</tr>
				<tr class="trRight">
					<td>年底資產總額：</td>
					<td>新台幣</td>
					<td><span class="numberFmt">${SMap[asset]}</span></td>
					<td class="trLeft">元</td>
					<td>成長率：</td>
					<td>
						<c:choose>
							<c:when test="${not empty assetlr&&assetlr>0}"><span class="numberFmt">${(assetr-assetlr)/cfn:abs(assetlr)*100}</span></c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class="trLeft">%</td>
				</tr>
				<tr>
					<th colspan="7">損益表部分(填寫之數字為該科目之合計數)</th>
				</tr>
				<tr class="trRight">
					<td style="width: 30%;">年度營業收入淨額：</td>
					<td style="width: 8%;">新台幣</td>
					<td style="width: 20%;"><span class="numberFmt">${SMap[NI]}</span></td>
					<td class="trLeft">元</td>
					<td>成長率：</td>
					<td>
						<c:choose>
							<c:when test="${not empty NIlr&&NIlr>0}"><span class="numberFmt">${(NIr-NIlr)/cfn:abs(NIlr)*100}</span></c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class="trLeft">%</td>
				</tr>
				<tr class="trRight">
					<td>年度營業成本：</td>
					<td>新台幣</td>
					<td><span class="numberFmt">${SMap[cost]}</span></td>
					<td class="trLeft">元</td>
					<td>成長率：</td>
					<td>
						<c:choose>
							<c:when test="${not empty costlr&&costlr>0}">
								<span class="numberFmt">${(costr-costlr)/cfn:abs(costlr)*100}</span>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class="trLeft">%</td>
				</tr>
				<tr class="trRight">
					<td>年度營業費用：</td>
					<td>新台幣</td>
					<td><span class="numberFmt">${SMap[expense]}</span></td>
					<td class="trLeft">元</td>
					<td>成長率：</td>
					<td>
						<c:choose>
							<c:when test="${not empty expenselr && expenselr>0}">
								<span class="numberFmt">${(expenser-expenselr)/cfn:abs(expenselr)*100}</span>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class="trLeft">%</td>
				</tr>
				<tr class="trRight">
					<td>年度稅後損益：</td>
					<td><c:choose>
						<c:when test="${empty SMap[NP]}">無</c:when>
						<c:when test="${fn:indexOf(SMap[NP],'-')!=-1}">虧損，</c:when>
						<c:otherwise>獲利，</c:otherwise>						
					</c:choose>新台幣</td>
					<td><span class="numberFmt">${SMap[NP]}</span></td>
					<td class="trLeft">元</td>
					<td>成長率：</td>
					<td>
						<c:choose>
							<c:when test="${not empty NPlr&&NPlr>0}">
								<span class="numberFmt">${(NPr-NPlr)/cfn:abs(NPlr)*100}</span>
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</td>
					<td class="trLeft">%</td>
				</tr>
				<tr>
					<th colspan="7">財務比率</th>
				</tr>
				<tr class="trRight">
					<td>淨值報酬率：</td>
				<c:if test="${not empty SMap[NP] && lessZero != 1}">
					<td colspan="2">
						<span class="numberFmt">${NPr/cfn:abs(SEr) *100}</span>
					</td>
					<td class="trLeft" colspan="4">%</td>
				</c:if>
				<c:if test="${lessZero eq 1}">
					<td class="trLeft" colspan="6">年底股東(業主)權益為負值</td>
				</c:if>
				</tr>
				<tr class="trRight">
					<td>負債比率：</td>
					<td colspan="2">
						<c:if test="${not empty SMap[asset] && not empty SMap[SE]}">
							<span class="numberFmt">${(assetr-SEr)/assetr *100}</span>
						</c:if>
					</td>
					<td class="trLeft" colspan="4">%</td>
				</tr>
				<tr class="trRight">
					<td>總資產週轉率：</td>
					<td colspan="2">
						<c:if test="${not empty SMap[NI] && not empty SMap[asset]}">
							<span class="numberFmt">${NIr/assetr *100 }</span>
						</c:if>
					</td>
					<td class="trLeft" colspan="4">%</td>
				</tr>
			</table>
		</div>
	</c:forEach>
</div>
</c:if>				