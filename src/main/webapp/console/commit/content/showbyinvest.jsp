<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;Step3&nbsp;]</span>
		<strong style="color:#222;">編輯${CRType[bean.type]}明細</strong>
	</legend>
	<div id="mytCommit"></div>
	<div style="margin-top: 20px;">
		<table class="myTable" id="invNoTable" style="width: 95%;margin-left: 15px;"> 
			<thead>
				<tr>
					<th>No</th>
					<th>案號</th>
					<th>大陸事業名稱</th>
					<th>期間</th>
					<th>狀態</th>
					<th>明細</th>
					<c:if test="${fn:contains(memberUrls,'E0102')}">
						<th>編輯明細</th>
					</c:if>
					<th>填報執行情形</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="invNo" items="${investNo}">
					<tr>
						<td></td>
						<td>${invNo.investNo}</td>
						<td>${InvestNoToName[invNo.investNo]}</td>
						<td>${invNo.startYear}~${invNo.endYear}</td>
						<td>${AoCode[invNo.state]}</td>
						<td>
							<c:if test="${empty invNo.subserno}">
								<a class="btn_class_opener" href="<c:url value='/console/commit/subcommitedit.jsp?subserno=${invNo.subserno}&serno=${oInfo.serno}&investNo=${invNo.investNo}&editType=add' />">新增</a>
							</c:if>
							<c:if test="${not empty invNo.subserno}">
								<a class="btn_class_opener" href="<c:url value='/console/commit/subcommitedit.jsp?subserno=${invNo.subserno}&serno=${oInfo.serno}&investNo=${invNo.investNo}&editType=show' />">檢視</a>
							</c:if>
						</td>
						<c:if test="${fn:contains(memberUrls,'E0102')}">
							<td>
								<c:if test="${not empty invNo.subserno}">
									<a class="btn_class_opener" href="<c:url value='/console/commit/subcommitedit.jsp?subserno=${invNo.subserno}&serno=${oInfo.serno}&investNo=${invNo.investNo}&editType=edit' />">編輯</a>
								</c:if>
							</td>
						</c:if>
						<td>
						<c:if test="${not empty invNo.subserno}">
							<a class="btn_class_opener" href="<c:url value='/console/commit/showsubcommitreport.jsp?subserno=${invNo.subserno}&editType=add' />">新增</a>
						</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</fieldset>