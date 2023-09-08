<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- <link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script> --%>
<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>

<script>
$(function() {
	 $( "#accordion" ).accordion({heightStyle: "content",collapsible: true });
});
</script>
<div style="font-size: 14px;">
	
	<strong style="margin:5px 10px;color:#222;font-size: 16px;">執行情形彙總</strong>
	<c:if test="${not empty summary}">
		<div id="accordion">
			<h3>執行情形填報情形</h3>
			<div>
				<table class="dtWithInput" style="font-size: 16px;text-align: center;width: 100%;">
					<thead><tr>
						<th rowspan="2">年度</th>
						<th colspan="3">國內相對投資計畫</th>
						<th rowspan="2" class="sortInput">改善財務</th>								
						<th rowspan="2" class="sortInput">資金回饋</th>								
					</tr>
					<tr>
						<th class="sortInput">上半年</th>
						<th class="sortInput">下半年</th>								
						<th  class="sortInput">年報</th>								
					</tr></thead><tbody>
					<c:forEach var="povL" items="${povCR}">
						<tr>
							<c:forEach var="repserno" items="${povL}" varStatus="i">
							<td>
								<c:if test="${i.index eq '0' }">${repserno}</c:if>
								<c:if test="${i.index != '0' && repserno!='' }">
									<input type="button" onclick="toEditReport('${repserno}','${cdbean.IDNO}','${cdInfo.investor}')" class="myEdit btn_class_opener" style="text-align: center;font-size: 12px;" value="${param.EditType}"/>
								</c:if>
							</td>
							</c:forEach>
						</tr>
					</c:forEach></tbody>
				</table>	
			</div>
			<c:forEach var="sumItem" items="${summary}">
				<h3>${CRType[sumItem.key]}</h3>
				<div style="min-height: 300px;">
					<c:if test="${sumItem.key eq '02' }">
							<table class="formProj">
								<tr>
									<th rowspan="2">審查類型</th>
									<th colspan="2">改善期間</th>
									<th colspan="4">列入改善年之財務資訊</th>
								</tr>
								<tr>
									<th>起始年</th>
									<th>截止年</th>
									<th>列入年度</th>
									<th>流動比率</th>
									<th>速動比率</th>
									<th>負債比率</th>
								</tr>
						<c:if test="${not empty mapExDetail}">
									<c:forEach var="exItem02" items="${mapExDetail[sumItem.key]}">
								<tr>
										<c:forEach var="item02" items="${exItem02}" varStatus="i">
											<c:choose>
												<c:when test="${i.index eq 0}"><td>${repTypeMap[item02]}</td></c:when>
												<c:when test="${i.index < 4}"><td>${item02}</td></c:when>
												<c:otherwise><td class="trRight"><span class="numberFmt">${item02}</span>%</td></c:otherwise>
											</c:choose>
										</c:forEach>
								</tr>
									</c:forEach>
						</c:if>
						</table><br/><br/>
					</c:if>
					<table class="formProj">
					<thead>
					<c:if test="${sumItem.key != '02' }">
						<tr>
							<th rowspan="2">填報項目</th>
							<th rowspan="2">
								<c:if test="${sumItem.key eq '01' }">申報類型</c:if>
								<c:if test="${sumItem.key eq '03' }">幣別</c:if>
							</th>
							<th colspan="2">管制期間</th>
							<th colspan="2">計算期數</th>
							<th rowspan="2">原始總承諾金額</th>
							<th rowspan="2">累計執行承諾金額</th>
							<th rowspan="2">累計達成率</th>
						</tr>
						<tr>
							<th>起始年</th>
							<th>截止年</th>
							<th>累計</th>
							<th>總期數</th>
						</tr>
					</c:if>
					<c:if test="${sumItem.key eq '02' }">
						<tr>
							<th rowspan="2">年度</th>
							<th colspan="3">分年承諾</th>
							<th colspan="3">填報執行情形</th>
						</tr>
						<tr>
							<th>流動比率</th>
							<th>速動比率</th>
							<th>負債比率</th>
							<th>流動比率</th>
							<th>速動比率</th>
							<th>負債比率</th>
						</tr>
					</c:if>
					</thead>
					<tbody>
					<c:if test="${sumItem.key != '02' }">
					<c:forEach var="item" items="${sumItem.value}">
						<tr>
							<c:forEach var="item" items="${item}" varStatus="i">
								<c:choose>
									<c:when test="${i.index eq 0}"><td>${CRType[item]}</td></c:when>
									<c:when test="${i.index eq 1}"><td>${repTypeMap[item]}</td></c:when>
									<c:when test="${i.index >5}"><td class="trRight"><span class="numberFmt"><fmt:parseNumber value="${item}" /></span><c:if test="${i.index eq 8 && not empty item}">%</c:if></td></c:when>
									<c:otherwise><td><span>${item}</span></td></c:otherwise>
								</c:choose>
							</c:forEach>
						</tr>
					</c:forEach>
					</c:if>
					<c:if test="${sumItem.key eq '02' }">
					<c:forEach var="item" items="${sumItem.value}">
						<tr>
							<c:forEach var="item" items="${item}" varStatus="i">
								<c:choose>
									<c:when test="${i.index eq 0}"><td>${item}</td></c:when>
									<c:when test="${i.index >0}"><td class="trRight"><span class="numberFmt"><fmt:parseNumber value="${item}" /></span><c:if test="${not empty item}">%</c:if></td></c:when>
								</c:choose>
							</c:forEach>
						</tr>
					</c:forEach>
					</c:if>
					</tbody>
					</table>
					<c:if test="${not empty mapExDetail}">
						<c:if test="${sumItem.key eq '03' && not empty mapExDetail[sumItem.key]}">
					<br/><br/>
							<table class="formProj">
								<tr>
									<th rowspan="2">年度</th>
									<th colspan="2">分年承諾</th>
									<th rowspan="2">填報執行情形</th>
								</tr>
								<tr>
									<th>承諾金額</th>
									<th>累積承諾金額</th>
								</tr>
								<c:forEach var="exItem03" items="${mapExDetail[sumItem.key]}">
									<tr>
										<c:forEach var="item03" items="${exItem03}" varStatus="i">
											<c:choose>
												<c:when test="${i.index eq 0}"><td>${item03}年</td></c:when>
												<c:otherwise><td class="trRight"><span class="numberFmt">${item03}</span></td></c:otherwise>
											</c:choose>
										</c:forEach>
									</tr>
								</c:forEach>
							</table>
						</c:if>
					</c:if>
				</div>
			</c:forEach>
		</div>
	</c:if>		
</div>