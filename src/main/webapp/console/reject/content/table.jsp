<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	var y=$.extend({
		"aaSorting": [[ 2, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2 ] },
                         { "sWidth": "7%", "aTargets": [ 0 ] }, { "sWidth": "10%", "aTargets": [ 1,2] },
                         { "sWidth": "27%", "aTargets": [4] },
		                 {"sSortDataType":"chinese", "aTargets": [ 3,4]}],
		           "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
				   "iDisplayLength": 10
				   ,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese());
	$(".mytip").tooltip();
	var oTable=$("#example").dataTable(y);
	$("body").on('click',"#example tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		var t="show";
		if("${fn:contains(memberUrls,'E0401')}"==="true"){
			t="edit";
		}
 		postUrlByForm('/console/reject/showform.jsp',{'editType':t
 			,"serno":$('td:eq(1)',ntr).find("span").text(),
 			"cNo":$('td:eq(2)',ntr).find("span").text()
 		});
	});
});
</script>

<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;結果顯示&nbsp;]</span><strong style="color:#222;">&nbsp;檢視符合條件列表&nbsp;</strong>&nbsp;
		</legend>
		<div>
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>序號</th>
					<th>申請日期</th>
					<th>發文日期</th>
					<th>國內事業名稱</th>
					<th>駁回類型</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="list" items="${rejects}">
					<tr>
						<td></td>
						<td>${ibfn:addSlash(list.receiveDate)}<span style="display: none;">${list.serno}</span></td>
						<td>${ibfn:addSlash(list.issueDate)}<span style="display: none;">${list.cNo}</span></td>
						<td>
							<c:choose>
								<c:when test="${fn:length(list.cName)>25}"><a class="mytip" href="#" title="${list.cName}">${ibfn:shorten(list.cName,25)}</a></c:when>
								<c:otherwise>${list.cName}</c:otherwise>
							</c:choose>
						</td>
						<td>${fn:substring(list.Description,0,fn:indexOf(list.Description,"("))}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
</div>
