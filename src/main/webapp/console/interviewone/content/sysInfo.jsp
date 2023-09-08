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
					<div id='filelist' style="display: none;">
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
		<span style="float: right;"><input type="button" class="btn_class_opener" value="返回列表" onclick='window.location.href="${pageContext.request.contextPath}/console/interviewone/showfollowing.jsp"'></span>
	</legend>
	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
		<table>
			<tr>
				<td class="trRight"  style="width: 20%;">調查年度：</td>
				<td><span>${IObean.year}年</span></td>
				<td class="trRight" style="width: 20%;">事業名稱：</td>
				<td colspan="5">${IOBaseInfo.cname}</td>
			</tr>
			<tr>
				<td class="trRight">陸資案號：</td>
				<td>
				<c:forEach var="item" items="${fn:split(IOBaseInfo.investNo,'、')}" varStatus="i">
					<c:if test="${i.index>0}">、</c:if>
					<c:choose>
						<c:when test="${fn:startsWith(item,'4')}">(陸分)</c:when>
						<c:otherwise>(陸)</c:otherwise>
					</c:choose>
					${item}
				</c:forEach>
				<input type="hidden" maxlength="6" name="investNo" value="${IOBaseInfo.investNo}"/>
				</td>
				<td class="trRight">統一編號：</td>
				<td><span>${IOBaseInfo.idno}</span></td>
			</tr>
			<%-- <tr>
				<th class="trRight" style="width: 15%;">前次修改人：</th>
				<td>${userName[IObean.updateuser]}</td>
				<th class="trRight">前次修改時間：</th>
				<td><span>${IOBaseInfo.lastUpdate}</span></td>
			</tr> --%>
		</table>
	</div>
</fieldset>				