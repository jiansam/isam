<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="<c:url value='/js/opener.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/survey.js'/>"></script>
<div id="mainContent">
<form action="<c:url value='/survey/showsurveyitem.jsp'/>" method="post" class="myForm">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;請選擇問卷查詢條件&nbsp;</strong>&nbsp;</span>
		</legend>
	
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;Step 1&nbsp;]</span><strong style="color:#222;">&nbsp;選擇問卷調查表&nbsp;</strong>&nbsp;
		</legend>
	<div>
		問卷種類:
		<select style="margin:2px 0px;" name="qType" class="qType">
			<c:forEach var="q" items="${qTypeName}">
				<option value="${q.key}">${q.value}</option>
			</c:forEach>
		</select><br/>
		調查年度:
		<select style="margin:2px 0px;" name="year" class="qTypeYear">
			<c:forEach var="qType" items="${qTypeYear}">
				<c:forEach var="year" items="${qType.value}">
					<option class="${qType.key}" value="${year}">${year-1911}</option>
				</c:forEach>
			</c:forEach>
		</select><br/>
	</div>
	</fieldset>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;Step 2&nbsp;]</span><strong style="color:#222;">&nbsp;選擇投資業別&nbsp;</strong>&nbsp;
			<input type="button" value="清空" class="clearMItem btn_class_opener">
			<input type="button" class="testCheckm btn_class_opener" value="檢視所選" alt="投資業別"/>
			<img title="此圖示表未選時，該選項為不拘" alt="此圖示表未選時，該選項為不拘" src="<c:url value="/images/unselectedmeansall.png"/>">
			<span style="float: right;">搜尋&nbsp;<input type="text" class="filterMItem"></span>
			<span class="filterMsg"></span>
		</legend>
		<div class="MItem" style="clear: both;">
			<jsp:include page="/survey/content/surveyindustry.jsp" flush="true" />
		</div>
	</fieldset>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;Step 3&nbsp;]</span><strong style="color:#222;">&nbsp;選擇投資地區/來源&nbsp;</strong>&nbsp;
			<input type="button" value="清空" class="clearMItem btn_class_opener">
			<input type="button" class="testCheckm btn_class_opener" value="檢視所選" alt="投資地區"/>
			<img title="此圖示表未選時，該選項為不拘" alt="此圖示表未選時，該選項為不拘" src="<c:url value="/images/unselectedmeansall.png"/>">
			<span style="float: right;">搜尋&nbsp;<input type="text" class="filterMItem"></span>
			<span class="filterMsg"></span>
		</legend>
		<div class="MItem testHide" style="clear: both;">
			<jsp:include page="/survey/content/surveyregion.jsp" flush="true" />
		</div>
		<div class="testHideTxt" style="display: none;">
			<span style="color:#F30;">投資地區/來源皆為中國大陸</span>
		</div>
	</fieldset>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;Step 4&nbsp;]</span><strong style="color:#222;">&nbsp;查詢符合條件之廠商&nbsp;</strong>&nbsp;
		</legend>
		<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
			<img src="<c:url value='/images/sub/icon_dot.jpg'/>" width="18" height="18" />進行查詢：
			<img class="mySent" src="<c:url value='/images/sub/button_search.jpg'/>" width="60" height="22" style="cursor:pointer;" />
		</div>
	</fieldset>
	</fieldset>
	</form>
</div>
