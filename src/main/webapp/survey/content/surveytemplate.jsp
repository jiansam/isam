<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;問卷下載&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/survey/content/surveymenu.jsp" flush="true">
				<jsp:param value="2" name="pos"/>
			</jsp:include>
		</div>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			
			<div style="margin: 0px auto;">
				<table class="formProj" style="text-align: center;font-size: 16px;">
					<tr>
						<th rowspan="2">年度/種類</th>
						<th rowspan="2">對海外投資事業營運狀況調查表</th>
						<th colspan="2">華僑及外國人投資事業營運狀況調查表</th>
						<th rowspan="2">陸資投資事業營運狀況調查表</th>
					</tr>
					<tr>
						<th>服務業</th>
						<th>非服務業</th>
					</tr>
					<c:forEach var="year" items="${yearS}">
						<tr>
							<td align="center">${year}年</td>
							<c:forEach var="file" items="${year_files[year]}">
								<c:if test="${empty file}">
									<td></td>
								</c:if>
								
								<c:if test="${not empty file}">
									<td><a href="<c:url value="/console/survey/surveyFileLoad.view?id=${file.id}" />" target="_blank">
										<img src='<c:url value="/images/pdf.png" />' width="22" title="${file.file_title}" alt="${file.file_title}"/></a></td>
								</c:if>
							
							</c:forEach>
						</tr>
					</c:forEach>
				</table>	
			</div>
		</fieldset>	
	</fieldset>
</div>
