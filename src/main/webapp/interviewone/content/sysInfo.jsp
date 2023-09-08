<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
$(function() {
  $("#filelist").dialog({
	  autoOpen:false,
	  title:"${IObean.year}年${IOBaseInfo.cname}檔案列表",
	  width:600
  });
  $("#view").click(function(){
	  $("#filelist").dialog("open");
  });
});
</script>
<fieldset>
	<legend>
		<span style="color:#F30;">[系統基本資料]</span>&nbsp;
		<c:if test="${not empty IOFilelist}">
					<span id='view' class='btn_class_opener'>檢視掃描檔案</span>
					<div id='filelist'>
							<table class="formProj" >
								<caption>${IObean.year}年&nbsp;&nbsp;${IOBaseInfo.cname}&nbsp;&nbsp;檔案列表</caption>
								<tr>
									<th>序號</th>
									<th>類型</th>
									<th>檔案名稱</th>
								</tr>
								<c:forEach var="bean" items="${IOFilelist}" varStatus="i">
									<tr style="text-align: center;">
										<td>${i.index+1}</td>
										<td>${bean.qType eq 'I'?'訪視紀錄':'營運問卷'}</td>
										<c:url value="/getinterviewonefiles.jsp" var="download">
											<c:param name="fNo">${bean.fNo}</c:param>
										</c:url>
										<td><a href="${download}">${bean.fName}</a></td>
									</tr>
								</c:forEach>
							</table>
					</div>
				</c:if>
		<span style="float: right;"><input type="button" class="btn_class_opener" value="返回列表" onclick='window.location.href="${pageContext.request.contextPath}/interviewone/showiolist.jsp"'></span>
	</legend>
	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
		<table>
			<tr>
				<th class="trRight">調查年度：</th>
				<td><span>${IObean.year}年</span></td>
				<th class="trRight">事業名稱：</th>
				<td colspan="5">${IOBaseInfo.cname}</td>
			</tr>
			<tr>
				<th class="trRight" style="width: 13%;">陸資案號：</th>
				<td><span>(${IOBaseInfo.bNo})&nbsp;${IOBaseInfo.investNo}</span><input type="hidden" maxlength="6" name="investNo" value="${IOBaseInfo.investNo}"/></td>
				<th class="trRight">統一編號：</th>
				<td><span>${IOBaseInfo.idno}</span></td>
			</tr>
			<tr>
				<th class="trRight" style="width: 15%;">前次修改人：</th>
				<td>${userName[IObean.updateuser]}</td>
				<th class="trRight">前次修改時間：</th>
				<td><span>${IOBaseInfo.lastUpdate}</span></td>
			</tr>
			<%-- <tr>
				<th class="trRight" >特殊稽核：</th>
				<td colspan="3">${optionValName.errMsg[IObean.msg]}</td>
			</tr>
 --%>
		</table>
	</div>
</fieldset>				