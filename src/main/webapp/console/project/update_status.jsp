<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.isam.bean.*, com.isam.dao.*" %>

<style>
th.mFilter select{
	width: 60%;
}
</style>
<script>
$(function() {
	/* Add a select menu for each TH element in the table footer */
	$.fn.dataTableExt.oApi.fnGetColumnData = function ( oSettings, iColumn, bUnique, bFiltered, bIgnoreEmpty ) {
	    // check that we have a column id
	    if ( typeof iColumn == "undefined" ) return new Array();
	     
	    // by default we only want unique data
	    if ( typeof bUnique == "undefined" ) bUnique = true;
	     
	    // by default we do want to only look at filtered data
	    if ( typeof bFiltered == "undefined" ) bFiltered = true;
	     
	    // by default we do not want to include empty values
	    if ( typeof bIgnoreEmpty == "undefined" ) bIgnoreEmpty = true;
	     
	    // list of rows which we're going to loop through
	    var aiRows;
	     
	    // use only filtered rows
	    if (bFiltered == true) aiRows = oSettings.aiDisplay; 
	    // use all rows
	    else aiRows = oSettings.aiDisplayMaster; // all row numbers
	 
	    // set up data array    
	    var asResultData = new Array();
	     
	    for (var i=0,c=aiRows.length; i<c; i++) {
	        iRow = aiRows[i];
	        var aData = this.fnGetData(iRow);
	        var sValue = aData[iColumn];
	         
	        // ignore empty values?
	        if (bIgnoreEmpty == true && sValue.length == 0) continue;
	 
	        // ignore unique values?
	        else if (bUnique == true && jQuery.inArray(sValue, asResultData) > -1) continue;
	         
	        // else push the value onto the result data array
	        else asResultData.push(sValue);
	    }
	     
	    return asResultData;
	}
	
	var settings = $.extend({
		"aaSorting": [[ 0, "desc" ]],
		"aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
		"iDisplayLength": 20,				   
	   "aoColumns":[
			{}, {"sSortDataType":"chinese"}
		]
	}, sdInitDataTableSetting(), sdSortChinese());
	
	var oTable= $("#update_status_table").dataTable(settings);
	
    $("tfoot th").each( function ( i ) {
    	if($(this).hasClass('mFilter')){
    		this.innerHTML = "篩選條件：&nbsp;" + fnCreateSelect( oTable.fnGetColumnData(i) );
    		$('select', this).change( function () {
    			oTable.fnFilter( $(this).val(), i );
    		});
    	}
    } );
	
// 	$("#statusFilter").find("option").each(function(index){
// 		if($(this).html().indexOf("\n") != -1){
// 			var newopts =  $(this).html().split("\n");
// 			$(this).remove();
			
// 			for(var i = 0;i < newopts.length;i++){
// 				opts = $("#statusFilter").find("option");
// 				newopts[i] = $.trim(newopts[i]);
				
// 				var exists = false;
// 				for(var j = 0;j < opts.length;j++){
// 					if(newopts[i] == opts[j].innerText){
// 						exists = true;
// 						break;
// 					}
// 				}
				
// 				if(!exists){
// 					$("#statusFilter").find("select").append($('<option>').text(newopts[i]));
// 				}
// 			}
// 		}
// 	});
});
</script>

<fieldset style="-moz-border-radius: 6px; -moz-border-top-colors: #fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors: #fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors: #fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors: #fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
<legend style="width: 100%; border-top: 1px solid #E6E6E6">
<span style="color: #F30; margin-right: 20px;">[&nbsp;資料介接情形&nbsp;]</span>&nbsp;&nbsp;
<!-- <span style="float: right;">  -->
<!-- 	<a href="javascript: void(0);" id="show_all" class="btn_class_opener">顯示全部</a>  -->
<!-- 	<a href="javascript: void(0);" id="show_exception" class="btn_class_opener">顯示異常</a> -->
<!-- </span> -->
</legend>
		
<div>
<span style="color: blue;">當發生介接異常時，請聯繫系統工程師</span><br /><br />
<table id="update_status_table" class="display" style="width: 98%;">
	<thead>
		<tr>
			<th>介接完成時間</th>
			<th>介接情形</th>
		</tr>
	</thead>
	
	<tbody>
<%
		for(UpdateStatus us : UpdateStatusDAO.list()){
%>
		<tr>
			<td><%= us.getUpdate_date().substring(0, us.getUpdate_date().lastIndexOf(":")) %></td>
			<td><%= us.isStatus() ? "成功" : "異常" %></td>
		</tr>
<%
		}
%>
	</tbody>
	
	<tfoot>
		<tr>
			<th></th>
			<th class="mFilter" id="statusFilter"></th>
		</tr>
	</tfoot>
</table>
<!-- <div style="clear:both"></div> -->
</div>
</fieldset>