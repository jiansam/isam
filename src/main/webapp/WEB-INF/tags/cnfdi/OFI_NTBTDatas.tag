<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="console" tagdir="/WEB-INF/tags/console/" %>
<!-- firmdetail.jsp/財務資料下載    -->

<div class='tbtitle' style="margin-bottom: 5px;">財務資料下載</div>
<div style="height: 5px;"></div>

<div style="width: 98%;margin-left: 15px;">
	<table style="width: 98%;font-size: 16px;" id="NTBT">
		<thead>
			<tr>
				<th style="width: 10%;">序號</th>
				<th style="width: 45%;">標題</th>
				<th style="width: 10%;">建立日期</th>
				<th style="width: 10%;">下載</th>
			</tr>
		</thead>
		<tbody id="rows">
			<c:forEach var="data" items="${NTBTDatas}">	
				<tr>
					<td></td>
					<td>${data.title}</td>
					<td>${data.createtime_ROC}</td>
					<td>
						<a href='${pageContext.request.contextPath}/downloadNTBTDatas.view?id=${data.id}' style="font-size: 1em;font-family: Lucida Grande,Lucida Sans,Arial,sans-serif;color: rgb(119, 119, 119);" 
							class="btn_class_opener" target="_blank">下載</a></td>
				</tr>
			</c:forEach>		
		</tbody>
		
		<tfoot>
			<tr style="display:none;" id="template">
				<td class="no"></td>
				<td class="title"></td>
				<td class="createtime_ROC"></td>
				<td class="down">
					<a href='' style="font-size: 1em;font-family: Lucida Grande,Lucida Sans,Arial,sans-serif;color: rgb(119, 119, 119);" 
						class="fDown btn_class_opener" target="_blank">下載</a>
			</tr>
		</tfoot>
	</table>
</div>
<div style="height: 10px;"></div>	
<div id="upfile"></div>
<console:uploadDiv />	

<script src="<c:url value='/js/edit-OFINTBTDatas.js'/>" type="text/javascript" charset="utf-8"></script>
<script>
	var context = "${pageContext.request.contextPath}";
	var isUserCanEdit = "${fn:contains(memberUrls,'E0401')}";
	var investNo = "${sysinfo.INVESTMENT_NO}";
	var isConsole = false; //js判斷使用
	
	$(document).ready(function() {
		//方法一：直接用ajax取資料
		//getNTBTDatasList(investNo);
		
		//方法二：搭配EL （OFIShowApprovalFormServlet裡面的  setAttribute("NTBTDatas") 要打開
		var setting = setDataTable_NTBT();
		var table = $('#NTBT').DataTable(setting);
	});
</script>
