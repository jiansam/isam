<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/isamhelper.js'/>"></script>
<script type="text/javascript">
$(function(){
	$("#delBtn").click(function(){
		$("<div style='font-size='12px;''>您即將刪除本筆資料，刪除後將無法復原，請確認是否刪除?</div>").dialog({
			width: 350,
			modal:true,
			title:'確認刪除',
			buttons: {
		        "確定": function() {
				  postUrlByForm('/console/interviewone/edititem.jsp',{'qNo':'${IOBaseInfo.qNo}','editType':'delete','qType':$("input[name='qType']").val()});
		          $( this ).dialog( "close" );
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
			}
		});
	});
});
</script>
<fieldset>
	<legend>
		<span style="color:#F30;">[系統基本資料]</span>&nbsp;
		<span style="float: right;">
			<input type="button" class="btn_class_opener" value="返回列表" onclick='window.location.href="${pageContext.request.contextPath}/console/interviewone/showiolist.jsp"'>
		</span>
	</legend>
	<table class="formProj">
		<tr>
			<th class="trRight" style="width: 13%;">陸資案號：</th>
			<td style="width: 27%;">
				<span>
				<c:forEach var="item" items="${fn:split(IOBaseInfo.investNo,'、')}" varStatus="i">
					<c:if test="${i.index>0}">、</c:if>
					<c:choose>
						<c:when test="${fn:startsWith(item,'4')}">(陸分)</c:when>
						<c:otherwise>(陸)</c:otherwise>
					</c:choose>
					${item}
				</c:forEach>
				</span>
				<input type="hidden" maxlength="6" name="investNo" value="${IOBaseInfo.investNo}"/>
			</td>
			<th class="trRight" style="width: 10%;">統一編號：</th>
			<td><span>${IOBaseInfo.idno}</span></td>
			<th class="trRight" style="width: 10%;">聯絡電話：</th>
			<td style="width: 20%;"><span>${IOBaseInfo.tel}</span></td>
		</tr>
		<tr>
			<th class="trRight">事業名稱：</th>
			<td colspan="5">${IOBaseInfo.cname}</td>
		</tr>
		<tr>
			<th class="trRight">事業所在地：</th>
			<td colspan="5">${IOBaseInfo.addr}</td>
		</tr>
	</table>
	<input type="hidden" name="qNo" value="${IOBaseInfo.qNo}"/>
	<input type="hidden" name='year' value="${IOyear}">
</fieldset>				