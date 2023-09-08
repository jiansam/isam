<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/telfmthelper.js'/>"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<link href="<c:url value='/css/webStyle.css'/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
$(function(){
	/*設定enter 下一個text*/
	inputTextNext("#crform",".nextInput",".skip");
	var y=$.extend({
			"aaSorting": [[ 0, "desc" ],[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1,2] },
			                 {"sSortDataType":"chinese", "aTargets": [1]},
			                 {"sSortDataType":"dom-checkbox", "aTargets": [0]}
			                ],
			           "aLengthMenu": [[5, 10, 20,-1], [5, 10, 20,"All"]],
					   "iDisplayLength": 5
		},sdInitDataTableSetting(),sdSortChinese(),sdSortCheckbox());
	var roTable=$("#reNoList").dataTable(y);
	$("#reNoList tbody").on('click',"tr",function(){
		 $(this).find("input[name='reNos']").trigger( "click" );
	});
	var x=$.extend({
		"aaSorting": [[ 0, "desc" ],[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1,2] },
		                 {"sSortDataType":"chinese", "aTargets": [1]}
		                ],
		           "aLengthMenu": [[5, 10, 20,-1], [5, 10, 20,"All"]],
				   "iDisplayLength": 5
				   ,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese());
	$("#reNoListShow").dataTable(x);
	$("#reNoListShow tr,#reNoListShow td").css("cursor","none");
});
</script>

<div>
<c:choose>
	<c:when test="${param.type eq 'show'}">
		<fieldset>
				<legend class="legendTitle" style="font-weight: bold;">聯絡人資訊</legend>
				<table class="formProj">
					<tr>
						<td style="width: 20%;" class="trRight">聯絡人：</td>
						<td style="width: 30%;">${param.contact}</td>
						<td style="width: 20%;" class="trRight">聯絡電話：</td>
						<td>${param.tel}</td>
					</tr>	
				</table>
				<div style="margin: 10px;background-color: #ececec;font-weight: bold;">選擇文號</div>
				<div style="margin: 5px 10px;">
					<table id="reNoListShow" style="font-size: 16px;width: 95%;">
						<thead>
						<tr>
							<th>序號</th>
							<th>核准日</th>
							<th>文號</th>
							<th>案由</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach var="reNo" items="${creNOList}" varStatus="i">
							<c:if test="${fn:contains(param.reNos,reNo[1])}">
								<tr>
									<td>${i.index}</td>										
									<td>${reNo[0]}</td>										
									<td>${reNo[1]}</td>										
									<td>${reNo[2]}</td>											
								</tr>
							</c:if>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</fieldset>
	</c:when>
	<c:when test="${param.type eq 'edit' || param.type eq 'add'}">
		<form id="crform" action="<c:url value=''/>" method="post">
			<fieldset>
				<legend class="legendTitle" style="font-weight: bold;">聯絡人資訊</legend>
				<table class="formProj">
					<tr>
						<td style="width: 20%;" class="trRight">聯絡人：</td>
						<td><input type="text" value="${param.contact}" name="contact" style="width: 85%;font-size: 16px;"/></td>
						<td class="trRight">聯絡電話：</td>
						<td><input type="text" value="${param.tel}" name="tel" style="width: 85%;font-size: 16px;"/></td>
					</tr>	
				</table>
				<div style="margin: 10px;background-color: #ececec;font-weight: bold;">選擇文號</div>
				<div style="margin: 5px 10px;">
					<table id="reNoList" style="font-size: 16px;width: 95%;">
						<thead>
						<tr>
							<th>選擇</th>
							<th>核准日</th>
							<th>文號</th>
							<th>案由</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach var="reNo" items="${creNOList}">
							<tr>
								<td><input type="checkbox" class="btn_class_opener fisrtTd" name="reNos" value="${reNo[1]}" ${fn:contains(param.reNos,reNo[1])?"checked='checked'":""}></td>										
								<td>${reNo[0]}</td>										
								<td>${reNo[1]}</td>										
								<td>${reNo[2]}</td>											
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</fieldset>
		</form>
	</c:when>
</c:choose>
</div>
