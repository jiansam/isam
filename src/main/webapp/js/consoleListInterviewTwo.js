/*有下拉選單的datatable*/
(function($) {
/*
 * Function: fnGetColumnData
 * Purpose:  Return an array of table values from a particular column.
 * Returns:  array string: 1d data array 
 * Inputs:   object:oSettings - dataTable settings object. This is always the last argument past to the function
 *           int:iColumn - the id of the column to extract the data from
 *           bool:bUnique - optional - if set to false duplicated values are not filtered out
 *           bool:bFiltered - optional - if set to false all the table data is used (not only the filtered)
 *           bool:bIgnoreEmpty - optional - if set to false empty values are not filtered from the result array
 * Author:   Benedikt Forchhammer <b.forchhammer /AT\ mind2.de>
 */
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
}}(jQuery));
 
 
function fnCreateSelect( aData )
{
    var r='<select><option value=""></option>', i, iLen=aData.length;
    for ( i=0 ; i<iLen ; i++ )
    {
        r += '<option value="'+aData[i]+'">'+aData[i]+'</option>';
    }
    return r+'</select>';
}

var asInitVals = new Array();

/*訪查企業列表*/ 
$(function() {
	var oTable=$("#example").dataTable({
		"bAutoWidth" : false, //自適應寬度
		/* "bJQueryUI": true, */
		"aLengthMenu": [[10, 20, 50, -1], [10, 20, 50, "全部"]],
		"iDisplayLength": 20,
		//使用fnDrawCallback、aoColumnDefs和aaSorting設定直接給予第一個<td>動態序號
		"fnDrawCallback": function ( oSettings ) {
			/* Need to redo the counters if filtered or sorted */
			if ( oSettings.bSorted || oSettings.bFiltered ){
				for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
					$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
				}
			}
		},
		"aoColumnDefs": [
		    { "bSortable": false,'sClass':'center', "aTargets": [0] }
		],
		"aaSorting": [[ 1, 'desc' ]],
		/* "sPaginationType" : "full_numbers", */  //顯示全部的分頁器
		//多語言配置
		"oLanguage" : {
			"sProcessing" : "正在載入中......",
			"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
			"sZeroRecords" : "對不起，查詢不到相關資料！",
			"sEmptyTable" : "本分類目前尚無資料！",
			"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共有_TOTAL_ 筆資料",
			"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料",
			"sInfoFiltered" : "<br/>原始資料為 _MAX_ 筆資料",
			"sSearch" : "搜尋全部欄位",
			"oPaginate" : {
				"sFirst" : "最前頁",
				"sPrevious" : "上一頁",
				"sNext" : "下一頁",
				"sLast" : "最末頁"
			}
		},
		"aoColumns":[
			null,
			{"sSortDataType":"dom-text", "sType": "numeric"},
			{"sSortDataType":"chinese"},
			{"sSortDataType":"chinese"},
			null
		],
//		"oTableTools": {
//			"sSwfPath": getRootPath()+"/media/swf/copy_csv_xls_pdf.swf",
//			"aButtons": [
//				"print"
//				,"xls"
//			]
//		},
		"sDom": 'T<"clear">lfrtip'
	});
	$(".clearAll").click(function(){
		 var nNodes = oTable.fnGetNodes();
		 $('input:checkbox', nNodes).each( function() {
			 $(this).attr("checked",false);
		 });
	});
	$("tfoot input").keyup( function () {
        /* Filter on the column (the index) of this element */
		/* Use 'th' index instead of 'input' index. */
        oTable.fnFilter( this.value, $(this).closest("th").index());
    } );
	 $("tfoot input").each( function (i) {
	        asInitVals[i] = this.value;
	    } );
	     
	    $("tfoot input").focus( function () {
	        if ( this.className == "search_init" )
	        {
	            this.className = "";
	            this.value = "";
	        }
	    } );
	     
	    $("tfoot input").blur( function (i) {
	        if ( this.value == "" )
	        {
	            this.className = "search_init";
	            this.value = asInitVals[$("tfoot input").index(this)];
	        }
	    } );

jQuery.fn.dataTableExt.oSort['chinese-asc']  = function(x,y) {
    return x.localeCompare(y);    };     
jQuery.fn.dataTableExt.oSort['chinese-desc']  = function(x,y) { 
   return y.localeCompare(x);    };
jQuery.fn.dataTableExt.aTypes.push(function(sData) {
        var reg =/^[\u2e80-\u9fff]{0,}$/;
        if(reg.test(sData)){
            return 'chinese';
        }
        return null;
});
     
    /* Add a select menu for each TH element in the table footer */
    $("tfoot th").each( function ( i ) {
    	if($(this).hasClass('mFilter')){
    		this.innerHTML = fnCreateSelect( oTable.fnGetColumnData(i) );
    		$('select', this).change( function () {
    			oTable.fnFilter( $(this).val(), i );
    		} );
    	}
    } );
});