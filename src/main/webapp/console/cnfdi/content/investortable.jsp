<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/js/ajaxRequest.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
var contextPath = "${pageContext.request.contextPath}";
$(function() {
	
	$(".iTooltip").tooltip();
	var y=$.extend({
		"aaSorting" : [[ 1, "asc" ]],
		"aoColumnDefs" : [ { "sClass" : "center", "aTargets": [0,5,6] }, 
		                   { "sSortDataType" : "chinese", "aTargets": [2,3,4,5,6] }
	 					 ],
        "aoColumns" : [
	                      { "sWidth": "8%" }, 
	                      { "sWidth": "17%" }, 
	                      { "sWidth": "34%" },
	                      { "sWidth": "15%" },
	                      { "sWidth": "8%" },
	                      { "sWidth": "8%" },
	                      { "sWidth": "10%" }  
            		  ],
         "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
		   "iDisplayLength": 10,
		   "fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$("#example").dataTable(y);
	
	
	$("body").on('click',"#example tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		var local = nTds.index($(this));
		if(local == 5){
			if($(this).text().trim() == "無"){
				return;
			}
 		//	ajaxByDataToDialog($(this))
			opendialog($(this))
		}else{
	 		postUrlByForm('/console/showinvestform.jsp',{'investorSeq':$('td:eq(2)',ntr).find("span").text()});
		}
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
					<th>陸資案號</th>
					<th>投資人</th>
					<th>國別</th>
					<th>資金<br/>類型</th>
					<th>架構圖</th>
					<th>資料狀態</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${ofiInvestors}">
				
					<tr>
					<td></td>
					<td data-investorseq="${item.investorSeq}">${item.investNoStrs}</td>
					<td>${ibfn:shortenTooltip(item.INVESTOR_CHTNAME,40,iTooltip)}<span style="display: none;">${item.investorSeq}</span></td>
					<td>${item.country}${item.cn}</td>
					<td>${optmap.inSrc[item.inrole]}</td>
					<td>
					<c:choose>
						<c:when test="${item.file=='有'}"><input type="button" value="${item.file}" style="width:40px;" 
									class="dialogbtn btn_class_opener" /></c:when>
						<c:otherwise>${item.file}</c:otherwise>
					</c:choose></td>
					
					<td>${optmap.isFilled[item.isFilled]}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
	<div id="listDialog"></div>
</div>
