<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/opener.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/interview.js'/>"></script>

<%
	String[] InvestmentType = request.getParameterValues("InvestmentType");
%>
<div id="mainContent">
<form id="interviewform2" name="interviewform2" action="interviewresult.jsp" method="post">
	<fieldset>
		<legend>
			<span class="legendTitle"><strong>&nbsp;請選擇訪查企業及訪談大綱&nbsp;</strong>&nbsp;</span>
		</legend>
		<fieldset>
			<legend>
				<span class="legendStepTitle">[&nbsp;Step 1&nbsp;]</span><strong style="color:#222;">&nbsp;訪查企業查詢條件&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
				<itag:selections />
			</div>
		</fieldset>
		<fieldset>
			<itag:interviewlist />
		</fieldset>
	
		<fieldset>
			<legend>
				<span  class="legendStepTitle">[&nbsp;Step 3&nbsp;]</span><strong style="color:#222;">&nbsp;選擇訪談大綱&nbsp;</strong>&nbsp;
				<input type="button" value="清空" class="clearMItem btn_class_opener">
				<input type="button" class="testCheckm btn_class_opener" value="檢視所選"/>
				<img title="此圖示表未選時，該選項為不拘" alt="此圖示表未選時，該選項為不拘" src="<c:url value="/images/unselectedmeansall.png"/>">
				<span style="float: right;">搜尋&nbsp;<input type="text" class="filterMItem"></span>
				<span class="filterMsg"></span>
			</legend>
			<itag:outlinetree code="" />
		</fieldset>
		<fieldset>
			<legend>
				<span  class="legendStepTitle">[&nbsp;Step 4&nbsp;]</span><strong style="color:#222;">&nbsp;顯示結果&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
				<img src="<c:url value='/images/sub/icon_dot.jpg'/>" width="18" height="18" />進行查詢：
				<input type="image"  style="width: 60px;height: 22px;cursor:pointer;vertical-align: middle;" src="<c:url value='/images/sub/button_search.jpg'/>" />
			</div>
		</fieldset>
	</fieldset>
</form>	
</div>
