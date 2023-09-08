<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="console" tagdir="/WEB-INF/tags/console/" %>
<!-- firmdetail.jsp/財務資料下載    -->

<div align="right">
<!-- 	<input type="button" class="btn_class_opener" id="addP" value="新增資料" onclick="toAddSFile()"> -->
	<a href='#' style="font-size: 1em;font-family: Lucida Grande,Lucida Sans,Arial,sans-serif;color: rgb(119, 119, 119);" 
		class="btn_class_opener" onclick="toAddSFile()">新增資料</a>	
	</div>

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
				<th style="width: 10%;">刪除</th>
				<th style="width: 10%;">編輯</th>
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
					<td>
						<c:if test="${fn:contains(memberUrls,'E0401')}">
							<input type="button" class="btn_class_opener" alt="${data.id}" value="刪除" onclick="toDelFile(this)">
						</c:if>
					</td>
					<td>
						<c:if test="${fn:contains(memberUrls,'E0401')}">
							<input type="button" class="btn_class_opener" alt="${data.id}" value="編輯" onclick="toEditSFile(this)">
						</c:if>
					</td>
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
				<td class="del">
					<input type="button" name="del" class="fDel btn_class_opener" alt="" value="刪除" onclick="toDelFile(this)"></td>
				<td class="edit">
					<input type="button" name="edit" class="fEdit btn_class_opener" alt="" value="編輯" onclick="toEditSFile(this)"></td>
					
			</tr>
		</tfoot>
	</table>
</div>
<div style="height: 10px;"></div>	
<div id="upfile"></div>


<script src="<c:url value='/js/edit-OFINTBTDatas.js'/>" type="text/javascript" charset="utf-8"></script>
<script>
	var context = "${pageContext.request.contextPath}";
	var isUserCanEdit = "${fn:contains(memberUrls,'E0401')}";
	var investNo = "${sysinfo.INVESTMENT_NO}";
	var isConsole = true; //js判斷使用
	
	$(document).ready(function() {
		//方法一：直接用ajax取資料 ie9不支援FormData，所以改回EL轉頁
		//getNTBTDatasList(investNo);
		
		//方法二：搭配EL （OFIShowApprovalFormServlet裡面的  setAttribute("NTBTDatas") 要打開
		var setting = setDataTable_NTBT();
		var table = $('#NTBT').DataTable(setting);
		if(isUserCanEdit === "false"){
			table.fnSetColumnVis( 4, false); //jQuery隱藏欄位
			table.fnSetColumnVis( 5, false); 
		}
	});
</script>
