<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
$(function() {
	$(".removefile").click(function(){
		var fNo=$(this).prop("alt");
		$("<div style='font-size='12px;''>檔案移除後將無法復原，請確認是否繼續?</div>").dialog({
			width: 350,
			modal:true,
			title:'確認刪除',
			buttons: {
		        "移除": function() {
		          deletefile(fNo);
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
	<%
		InterviewoneFileService ser = new InterviewoneFileService();
		String reInvestNo=request.getParameter("reInvestNo");
		List<InterviewoneFile> list=ser.select(reInvestNo.equals("0")?request.getParameter("investNo"):null,request.getParameter("reInvestNo"));
		request.setAttribute("filelist",list);
	%>
<div id="fileTable" style="width:100%">
	<c:if test="${not empty filelist}">
		<table class="formProj" >
			<caption>檔案列表</caption>
			<tr>
				<th nowrap="nowrap">序號</th>
				<th nowrap="nowrap">年度</th>
				<th>類型</th>
				<th>檔案名稱</th>
				<c:if test="${fn:contains(memberUrls,'E0303')}"><th>刪除</th></c:if>
			</tr>
			<c:forEach var="bean" items="${filelist}" varStatus="i">
				<tr style="text-align: center;">
					<td>${i.index+1}</td>
					<td>${bean.year}</td>
					<td nowrap="nowrap">${bean.qType eq 'I'?'訪視紀錄':'營運問卷'}</td>
					<c:url value="/getinterviewonefiles.jsp" var="download">
						<c:param name="fNo">${bean.fNo}</c:param>
					</c:url>
					<td><a href="${download}">${bean.fName}</a></td>
					<c:if test="${fn:contains(memberUrls,'E0303')}"><td><input type="button" value="移除" alt="${bean.fNo}" class="removefile"></td></c:if>
				</tr>
			</c:forEach>
		</table>
	</c:if>	
</div>

