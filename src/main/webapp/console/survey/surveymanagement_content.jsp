<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;問卷下載&nbsp;</strong>&nbsp;</span>
		</legend>
		
		<fieldset>
			<legend>
				<span class="legendStepTitle">[&nbsp;管理問卷資料&nbsp;]</span>
				<c:if test="${fn:contains(memberUrls,'A0201') || fn:contains(memberUrls,'E0201')}">
					<input type="button" class="btn_class_opener" value="上傳問卷資料" onclick="surveyFile.show()"/>
				</c:if>
			</legend>
			<div style="margin: 0px auto;">
				<table class="formProj" style="text-align: center;font-size: 16px;">
					<tr>
						<th rowspan="2">年度/類型</th>
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
									<td>
									
									
										&nbsp;
										<c:url value="/console/survey/filledSurveyFileLoad.view" var="dnd" scope="page">
											<c:param name="id"  value="${file.id}"/>
											
										</c:url>
										<c:if test="${fn:contains(memberUrls,'E0201')}">
										<a href="${dnd}" >
										<img title="下載檔案" src="${pageContext.request.contextPath}/images/download.png" width="22"></a>
										
										
										<c:url value="/console/survey/filledSurveyFile.management" var="rmv" scope="page">
											<c:param name="id"  value="${file.id}"/>
											<c:param name="doThing"  value="remove"/>
										</c:url>
										
										<a href="${rmv}" >
										<img title="刪除檔案" src="${pageContext.request.contextPath}/images/action_delete.gif" width="22" onclick="return confirm('確認要刪除此問卷？')"></a>
										</c:if>									
									
									</td>
								</c:if>
							
							</c:forEach>
						</tr>
					</c:forEach>
						
				</table>	
			</div>
			<div id="editFile" style="display:none;width:100%;">
			<br/><br/><br/>
			<hr>
				<form action="${pageContext.request.contextPath}/console/survey/filledSurveyFileUpload.view" method="post" onsubmit="return surveyFile.checkData();" enctype="multipart/form-data"  style="width:50%; margin:0 auto;">
					<table class="formProj" style="font-size: 16px;">
						<tr><td colspan="2" style="font-weight:bold; size:1em; text-align:center;">新增/編輯問卷</td></tr>
						<tr>
							<td>&nbsp;年度</td>
							<td>&nbsp;<select name="year" id="surveyYear">
											<option value="">--請選擇年度--</option>
											<c:forEach var="y" items="${yearRange}" >
												<option value="${y}">${y}年&nbsp;</option>
											</c:forEach>
									  </select>							
							</td>
						</tr>
						<tr>
							<td>&nbsp;類型</td>
							<td>&nbsp;<select name="type" id="surveyName"></select>							
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap">上傳檔案</td>
							<td>&nbsp;<input type="file" name="file" ></td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<input type="hidden" name="id" value="">
								<input type="hidden" name="doThing" value="">
								<input type="submit" value="送出" >
							</td>
						</tr>
					</table>
				</form>
			</div>
		</fieldset>
	</fieldset>
</div>

<script type="text/javascript">

$(document).ready(function(){
	if("${result}" != ""){
		alert("${result}");
		<% session.setAttribute("result", "");%>
	}
	
});

var surveyFile = surveyFile || {};
surveyFile.type = ["Abroad","Service","NonService","mainland"];
surveyFile.typeName = ["對海外投資事業營運狀況調查表","華僑及外國人投資(服務業)","華僑及外國人投資(非服務業)","陸資投資事業營運狀況調查表"];
surveyFile.show = function(){
	surveyFile.setSurveyName();
	$("#editFile input[name='doThing']").val("new");
	$("#editFile").show();
}
surveyFile.setSurveyName = function(){
	$("#surveyName").empty();
	$("#surveyName").append("<option value=''>--請選擇問卷類型--</option>");
	for(var i=0; i<surveyFile.type.length; i++){
		$("#surveyName").append("<option value='"+surveyFile.type[i]+"'>"+surveyFile.typeName[i]+"</option>");
	}
}
surveyFile.checkData = function(){
	if( $("#editFile #surveyYear").val() == "" ){
		alert("請選擇問卷年度。");
		return false;
	}
	if( $("#editFile #surveyName").val() == "" ){
		alert("請選擇問卷類型。");
		return false;
	}
	if( $("#editFile input[name='file']").val() == "" ){
		alert("請選擇問卷檔案。");
		return false;
	}
	return confirm('確定送出資料？');
}
</script>
