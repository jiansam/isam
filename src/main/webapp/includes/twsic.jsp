<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*,com.isam.service.ofi.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
$(function() {
	var y=$.extend({
		"aaSorting": [[ 1, "desc" ],[2,"asc"]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2] },
		                 {"sSortDataType":"chinese", "bFilter":false,"aTargets": [3]}
		                 , {"sSortDataType":"dom-checkbox", "aTargets": [1]}
		                 , {"bFilter":false,"bSortable":false, "aTargets": [0]}		                 
		                 ],
		           "aLengthMenu": [[10, 20, 50,-1], [10, 20, 50,'All']],
				   "iDisplayLength": 10,
				   "aoColumns": [
				                 { sWidth: '10%' },
				                 { sWidth: '10%' },
				                 { sWidth: '15%' },
				                 { sWidth: '65%' },
				                 { sWidth: '0%' }
				                ]
					,"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	},sdInitDataTableSetting(),sdSortChinese(),sdSortCheckbox(),checkedFilteredData("1"));
	$("#optItems tbody tr").each(function() {
		var sTitle="";
		 var nTds = $('td', this);
		 sTitle=$(nTds[4]).text();
		 $(this).prop("title",sTitle);
	});
	$("body").on('click',"#optItems tbody tr",function(){
		var ntd=$('td:eq(1) input',this);
		if(ntd.prop( "checked" )){
			ntd.prop( "checked",false );
		}else{
			ntd.prop( "checked",true );
		}
	});
	var oTable=$("#optItems").dataTable(y);
	oTable.fnSetColumnVis(4,false);
	
	$("#selectAll").click(function(){
		oTable.checkedFilteredData();
		oTable.fnSort( [[ 1, "desc" ],[2,"asc"] ]);
		 oTable.fnFilter( "" );
	});
	$("#unselect").click(function(){
		oTable.uncheckedFilteredData();
		oTable.fnSort( [[ 1, "desc" ],[2,"asc"] ]);
		 oTable.fnFilter( "" );
	});
	$("#clearAll").click(function(){
		oTable.uncheckedAllData();
		oTable.fnSort( [[ 1, "desc" ],[2,"asc"] ]);
		oTable.fnFilter( "" );
	});
});
</script>
<%
	OFIInvestNoXTWSICService ser= new OFIInvestNoXTWSICService();
	request.setAttribute("twsicOpt", ser.getTWSICMap());
%>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;查詢營業項目&nbsp;]</span>
		<input type="button" id="clearAll" class="btn_class_opener" value="清空"/>				
		<input type="button" id="selectAll" class="btn_class_opener" value="全選"/>				
		<input type="button" id="unselect" class="btn_class_opener" value="取消選取"/>	
	</legend>
<div>
	
	<div style="width: 95%;margin-left: 20px;">
	<table id="optItems" style="width: 100%; ">
		<thead>
			<tr>
				<th>序號</th>
				<th>選項</th>
				<th>細類編號</th>
				<th>細類項目</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${twsicOpt}">
				<tr>
					<td></td>
					<td><input type="checkbox" value="${item.key}"></td>
					<td>${item.key}</td>
					<td>
						<c:choose>
							<c:when test="${fn:length(item.value)>20}">${fn:substring(item.value,0,20)}...</c:when>
							<c:otherwise>${item.value}</c:otherwise>
						</c:choose>
					</td>
					<td>
						<span style="font-size: 16px;">${item.value}</span>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
</div>
</fieldset>