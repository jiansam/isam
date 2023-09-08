<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" language="javascript" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/opener.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/survey.js'/>"></script>
<div id="mainContent">
<form class="myForm" action='<c:url value="/survey/showsurvayhtml.jsp" />' method="post">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;請設定問卷顯示欄位&nbsp;</strong>&nbsp;</span>
		</legend>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 1&nbsp;]</span><strong style="color:#222;">&nbsp;問卷查詢條件&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
				${surveyterms}
				<input type="hidden" value="${surveyterms}" name="terms">
				<input type="hidden" value="${surveyqType}" name="qType">
				<input type="hidden" value="${surveyYear}" name="year">
			</div>
		</fieldset>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 2&nbsp;]</span><strong style="color:#222;">&nbsp;選擇公司&nbsp;</strong>&nbsp;
				<input type="button" class="clearAll btn_class_opener" value="清空"/>				
				<input type="button" class="selectThispage btn_class_opener" value="本頁全選"/>				
				<input type="button" class="unselectThispage btn_class_opener" value="本頁全刪"/>				
				<input type="button" class="selectedCompany btn_class_opener" value="檢視所選"/>${qNosErr}
				<span>(Max:<span class="myCount">0</span>/<span class="myLimitCount">80</span>家)</span>
			</legend>
			<div style="height: 250px;overflow: auto;">
			<table id="example" class="display" style="width: 98%;"> 
				<thead>
					<tr>
						<th>NO.</th>
						<th>選擇</th>
						<th>公司名稱</th>
						<th>統一編號</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${not empty qNoList}">
						<c:forEach var="qNo" items="${qNoList}">
							<tr>
								<td></td>
								<td><input type="checkbox" name="qNoTemp" class="companyClear" value="${qNo.qNo}"></td>
								<td>${qNo.company}</td>
								<td>${qNo.IDNO}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
			</div>
		</fieldset>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 3&nbsp;]</span><strong style="color:#222;">&nbsp;選擇問卷題目&nbsp;</strong>&nbsp;
				<input type="button" value="清空" class="clearMItem btn_class_opener">
				<input type="button" class="testCheckm btn_class_opener" value="檢視所選" alt="問卷題目"/>
				<img title="此圖示表未選時，該選項為不拘" alt="此圖示表未選時，該選項為不拘" src="<c:url value="/images/unselectedmeansall.png"/>">
				<span style="float: right;">搜尋&nbsp;<input type="text" class="filterMItem"></span>
				<span class="filterMsg"></span>
			</legend>
			<div class="MItem" style="clear: both;">
				<jsp:include page="/survey/content/surveytopic.jsp" flush="true" />
			</div>
		</fieldset>
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;Step 4&nbsp;]</span><strong style="color:#222;">&nbsp;展示結果&nbsp;</strong>&nbsp;
			</legend>
			<div style="margin-bottom:10px; margin-top:10px; margin-left:5px;">
				<img src="<c:url value='/images/sub/icon_dot.jpg'/>" width="18" height="18" />進行比對：
				<img class="mySent" src="<c:url value='/images/sub/button_search.jpg'/>" width="60" height="22" style="cursor:pointer;"/>
			</div>
		</fieldset>
	</fieldset>
	</form>
</div>		