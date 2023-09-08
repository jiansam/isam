<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/js/ajaxRequest.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
var contextPath = "${pageContext.request.contextPath}";
$(function() {
	var y=$.extend({
		"aaSorting" : [[ 1, "asc" ]],
		"aoColumnDefs" : [ { "sClass":"center" , "aTargets":[0,1] },
		                   { "sSortDataType":"chinese" , "aTargets":[2,3,4]}
						 ],
		"aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
		"iDisplayLength": 10,
	    "fnDrawCallback": function ( oSettings ) {
							  /* Need to redo the counters if filtered or sorted */
							  if ( oSettings.bSorted || oSettings.bFiltered ){
							  	  for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								  	  $('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
								  	if($('td:eq(2)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).text().length>0){
										switch($('td:eq(2)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).text()[0]){
										case '4':
											$('td:eq(1)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html('分公司');
											break;
										case '5':
											$('td:eq(1)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html('子公司');
											break;
											
										}
									}
									else{
										$('td:eq(1)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html('辦事處');
									}
								  }
							  }
			              }
	},sdInitDataTableSetting(),sdSortChinese());
	var oTable=$("#example").dataTable(y);
	$("body").on('click',"#example tbody tr td",function(){
		var ntr= $(this).parent();
		var nTds=$('td',ntr);
		var banno = $("td[data-banno]",ntr).data('banno');
		var investorseq = $("td[data-investorseq]",ntr).data('investorseq');
		var local = nTds.index($(this));
	
		if(!banno){
		if(local == 5){
			if($(this).text().trim() == "無"){
				return;
			}
// 			ajaxByDataToDialog($(this))
			opendialog2($(this))
		}else{
	 		postUrlByForm('/showinvestcase.jsp',{
	 			'caseNo':$('td:eq(3)',ntr).find("span").text(),
	 			'investorSeq':investorseq});
		}
		}else{
			postUrlByForm('/showinvestcase.jsp',{'banno':banno});
		}
	});
});
</script>

<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;結果顯示&nbsp;]</span><strong style="color:#222;">&nbsp;檢視符合條件列表&nbsp;</strong>&nbsp;
		</legend>
		<div style="padding: 5px;font-size: 16px;">
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th style="width: 8%;">序號</th>
						<th  style="width: 10%;">投資型態</th>
					<th style="width: 10%;">陸資案號</th>
					<th>國內事業名稱</th>
					<th>投資人名稱</th>
					<th nowrap="nowrap">架構圖</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${ofiCase}">
					<tr>
						<td></td>
							<td 	<c:if test="${item.investNo==''}">data-banno="${item.caseNo}"</c:if> data-investorseq="${item.investorSeq}">${item.companytype}</td>
						<td>${item.investNo}</td>
						<td>${item.COMP_CHTNAME}<span style="display: none;">${item.caseNo}</span></td>
						<td>${item.INVESTOR_CHTNAME}<span style="display: none;">${item.investorSeq}</span></td>
						<td>
						<c:choose>
							<c:when test="${item.file=='有'}"><input type="button" value="${item.file}" style="width:40px;" 
										class="dialogbtn btn_class_opener" /></c:when>
							<c:otherwise>${item.file}</c:otherwise>
						</c:choose></td>						
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</fieldset>
	<div id="listDialog"></div>
</div>
