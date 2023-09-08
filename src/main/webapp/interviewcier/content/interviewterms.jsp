<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>

<script type="text/javascript" src="<c:url value='/js/opener.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/survey.js'/>"></script>
<div id="mainContent">
<form name="interviewform1" action="interviewshowitem.jsp" method="post">
	<fieldset>
		<legend>
			<span class="legendTitle"><strong>&nbsp;請選擇訪查企業查詢條件&nbsp;</strong>&nbsp;</span>
		</legend>
		<fieldset>
			<legend>
				<span class="legendStepTitle">[&nbsp;Step 1&nbsp;]</span><strong style="color:#222;">&nbsp;選擇訪查資料&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin: 0px auto;">
				<div style="float:left;width: 30%;">
						<div>
							<span style="color:#FF5000;vertical-align: middle;">選擇訪查年度&nbsp;</span>
							<input type="button" value="清空" class="clearMSelect btn_class_opener">
							<input type="button" class="checkMSelect btn_class_opener" value="檢視所選"/>
							<img title="此圖示表未選時，該選項為不拘" alt="此圖示表未選時，該選項為不拘" src="<c:url value="/images/unselectedmeansall.png"/>">
						</div>
						<div>
							<select name="year" style="margin:2px 0px;width: 90%;" multiple="multiple" class="selectBox">
								<itag:options selectName="year" />
							</select>
						</div>	
				</div>
				<div style="float:left;width: 30%;">
						<div>
							<span style="color:#FF5000;vertical-align: middle;">選擇訪查企業投資類型&nbsp;</span>
							<input type="button" value="清空" class="clearMSelect btn_class_opener">
							<img title="此圖示表未選時，該選項為不拘" alt="此圖示表未選時，該選項為不拘" src="<c:url value="/images/unselectedmeansall.png"/>">
							<!-- <input type="button" class="testCheckm btn_class_opener" value="檢視所選"/> -->
						</div>
						<div>
							<select name="InvestmentType" multiple="multiple" class="selectBox">
 								<itag:options selectName="InvestmentType" />
							</select>
						</div>	
				</div>
			</div>
			<div style="clear: both;"></div>
		</fieldset>
		<fieldset>
			<legend>
				<span class="legendStepTitle">[&nbsp;Step 2&nbsp;]</span><strong style="color:#222;">&nbsp;選擇投資業別&nbsp;</strong>&nbsp;
				<input type="button" value="清空" class="clearMItem btn_class_opener">
				<input type="button" class="testCheckm btn_class_opener" value="檢視所選"/>
				<img title="此圖示表未選時，該選項為不拘" alt="此圖示表未選時，該選項為不拘" src="<c:url value="/images/unselectedmeansall.png"/>">
				<span style="float: right;">搜尋&nbsp;<input type="text" class="filterMItem"></span>
				<span class="filterMsg"></span>
			</legend>
			<itag:industrytree code="" />
		</fieldset>
		<fieldset >
			<legend>
				<span class="legendStepTitle">[&nbsp;Step 3&nbsp;]</span><strong style="color:#222;">&nbsp;選擇投資地區&nbsp;</strong>&nbsp;
				<input type="button" value="清空" class="clearMItem btn_class_opener">
				<input type="button" class="testCheckm btn_class_opener" value="檢視所選"/>
				<img title="此圖示表未選時，該選項為不拘" alt="此圖示表未選時，該選項為不拘" src="<c:url value="/images/unselectedmeansall.png"/>">
				<span style="float: right;">搜尋&nbsp;<input type="text" class="filterMItem"></span>
				<span class="filterMsg" ></span>
			</legend>
			<itag:regiontree code="" />
		</fieldset>
		<fieldset>
			<legend>
				<span class="legendStepTitle">[&nbsp;Step 4&nbsp;]</span><strong style="color:#222;">&nbsp;查詢符合條件之企業&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
				<img src="<c:url value='/images/sub/icon_dot.jpg'/>" width="18" height="18" />進行查詢：
				 <input type="image"  style="width: 60px;height: 22px;cursor:pointer;vertical-align: middle;" src="<c:url value='/images/sub/button_search.jpg'/>" />
			</div>
		</fieldset>
	</fieldset>
</form>
</div>
