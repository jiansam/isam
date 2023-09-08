function sdInitDataTableSetting(){
	var opt= {
		"bAutoWidth" : false, //自適應寬度
		"bRetrieve": true,
		"bDestroy": true,
		"oLanguage" : {
			"sProcessing" : "正在載入中......",
			"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
			"sZeroRecords" : "對不起，查詢不到相關資料！",
			"sEmptyTable" : "目前尚無資料！",
			"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共有_TOTAL_ 筆資料",
			"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料",
			"sInfoFiltered" : "<br/>原始資料為 _MAX_ 筆資料",
			"sSearch" : "搜尋",
			"oPaginate" : {
				"sFirst" : "最前頁",
				"sPrevious" : "上一頁",
				"sNext" : "下一頁",
				"sLast" : "最末頁"
			}
		}
		};
	return opt;	
}
function sdSortInput(){
	/* Create an array with the values of all the input boxes in a column */
	$.fn.dataTableExt.afnSortData['dom-text'] = function  ( oSettings, iColumn )
	{
		return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
			var rs = $('td:eq('+iColumn+') input', tr).val();
			 var deformatted = rs.replace(/[^\d\-\.\/a-zA-Z]/g,'');
		     if ( $.isNumeric( deformatted ) || deformatted === "-" ) {
	           return deformatted;
		     }
			return rs;
		} );
	};
}
function sdNumberFmt(){
	/* Create an array with the values of all the input boxes in a column */
	$.fn.dataTableExt.afnSortData['numberFmt-text'] = function  ( oSettings, iColumn )
	{
		return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
			var rs = $('td:eq('+iColumn+') span', tr).text();
			var deformatted = rs.replace(/,/g,'');
			if ( $.isNumeric( deformatted ) || deformatted === "-" ) {
				return deformatted;
			}
			return rs;
		} );
	};
}
function sdSortBtn(){
	/* Create an array with the values of all the input boxes in a column */
	$.fn.dataTableExt.afnSortData['dom-btn'] = function  ( oSettings, iColumn )
	{
		return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
			if($('td:eq('+iColumn+') input', tr).length>0){
				return $('td:eq('+iColumn+') input', tr).val();
			}else{
				return "";
			}
		} );
	};
}
function sdSortCheckbox(){
	/* Create an array with the values of all the checkboxes in a column */
	$.fn.dataTableExt.afnSortData['dom-checkbox'] = function  ( oSettings, iColumn )
	{
		return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
			return $('td:eq('+iColumn+') input', tr).prop('checked') ? '1' : '0';
		} );
	};
}
function sdSortChinese(){
	jQuery.fn.dataTableExt.oSort['chinese-asc']  = function(x,y) {
		var x1=x.replace(/(<([^>]+)>)/ig,"");
		var y1=y.replace(/(<([^>]+)>)/ig,"");
	    return x1.localeCompare(y1);    };     
	jQuery.fn.dataTableExt.oSort['chinese-desc']  = function(x,y) { 
		var x1=x.replace(/(<([^>]+)>)/ig,"");
		var y1=y.replace(/(<([^>]+)>)/ig,"");
	   return y1.localeCompare(x1);    };
	jQuery.fn.dataTableExt.aTypes.push(function(sData) {
	        var reg =/^[\u2e80-\u9fff]{0,}$/;
	        if(reg.test(sData)){
	            return 'chinese';
	        }
	        return null;

	});
}
function sdSortNumric(){
	jQuery.fn.dataTableExt.aTypes.unshift(function ( sData ){
	     var deformatted = sData.replace(/[^\d\-\.\/a-zA-Z]/g,'');
	     if ( $.isNumeric( deformatted ) || deformatted === "-" ) {
	           return 'formatted-num';
	     }
		 return null;
	});
	jQuery.extend( jQuery.fn.dataTableExt.oSort, {
	    "formatted-num-pre": function ( a ) {
	    	a = (a === "-" || a === "") ? 0 : a.replace( /[^\d\-\.]/g, "" );
	        return parseFloat( a,10);
	    },
	    "formatted-num-asc": function(a,b){return a - b;},	 
	    "formatted-num-desc": function(a,b){return b - a;}
	});
}
function setfnGetColumnData(){
	/*Author:   Benedikt Forchhammer <b.forchhammer /AT\ mind2.de>*/
	$.fn.dataTableExt.oApi.fnGetColumnData = function ( oSettings, iColumn, bUnique, bFiltered, bIgnoreEmpty ) {
	    if ( typeof iColumn == "undefined" ) return new Array();
	    if ( typeof bUnique == "undefined" ) bUnique = true;
	    if ( typeof bFiltered == "undefined" ) bFiltered = true;
	    if ( typeof bIgnoreEmpty == "undefined" ) bIgnoreEmpty = true;
	    var aiRows;
	    if (bFiltered == true) aiRows = oSettings.aiDisplay; 
	    else aiRows = oSettings.aiDisplayMaster;
	    var asResultData = new Array();
	    for (var i=0,c=aiRows.length; i<c; i++) {
	        iRow = aiRows[i];
	        var aData = this.fnGetData(iRow);
	        var sValue = aData[iColumn];
	        if (bIgnoreEmpty == true && sValue.length == 0) continue;
	        else if (bUnique == true && jQuery.inArray(sValue, asResultData) > -1) continue;
	        else asResultData.push(sValue);
	    }
	    return asResultData;
	};
}
function checkedFilteredData(posNum){
	$.fn.dataTableExt.oApi.checkedFilteredData = function ( oSettings ) {
	        for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ) {
	            $('td:eq('+posNum+') input', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).prop("checked",true);
	        }
	}
	$.fn.dataTableExt.oApi.uncheckedFilteredData = function ( oSettings ) {
		for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ) {
			$('td:eq('+posNum+') input', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).prop("checked",false);
		}
	}
	$.fn.dataTableExt.oApi.uncheckedAllData = function ( oSettings ) {
		for ( var i=0, iLen=oSettings.aiDisplayMaster.length ; i<iLen ; i++ ) {
			$('td:eq('+posNum+') input', oSettings.aoData[ oSettings.aiDisplayMaster[i] ].nTr ).prop("checked",false);
		}
	}
}
function fnCreateSelect( aData )
{
    var r='<select><option value=""></option>', i, iLen=aData.length;
    for ( i=0 ; i<iLen ; i++ )
    {
        r += '<option value="'+aData[i]+'">'+aData[i]+'</option>';
    }
    return r+'</select>';
}
function setTFoot(oTable,inputCname,selectCname){
	var asInitVals = new Array();
	$("tfoot input").keyup( function () {
		oTable.fnFilter( this.value, $(this).closest("th").index());
	} );
	$("tfoot input").each( function (i) {
		asInitVals[i] = this.value;
	} );
	$("tfoot input").focus( function () {
		if ( this.className == inputCname ){
			this.className = "";
			this.value = "";
		}
	} );
	$("tfoot input").blur( function (i) {
		if ( this.value == "" ){
			this.className = inputCname;
			this.value = asInitVals[$("tfoot input").index(this)];
		}
	} );
	/* Add a select menu for each TH element in the table footer */
	$("tfoot th").each( function ( i ) {
		if($(this).hasClass(selectCname)){
			this.innerHTML = fnCreateSelect( oTable.fnGetColumnData(i) );
			$('select', this).change( function () {
				if($(this).val().length>0){
					oTable.fnFilter("^"+$(this).val()+"$",i,true);
				}else{
					oTable.fnFilter( $(this).val(), i );
				}
/*				oTable.fnFilter( $(this).val(), i );
*/			} );
		}
	} );
}
function fnHideColumns ( oTable, ary )
{
	$.each(ary,function(index,value){
		oTable.fnSetColumnVis(value,false);
	});
}