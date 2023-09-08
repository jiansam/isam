function getOption2(ary){
	  var option={
				"bAutoWidth" : false, //自適應寬度
				"bLengthChange": false,
				//"bFilter": false,
				//多語言配置
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
				},
				"aoColumnDefs": [{ "bSortable": false,  "aTargets": ary}]
		};
	  return option;
}
function getOptionBTN(ary){
	var option={
			"bAutoWidth" : false, //自適應寬度
			"bLengthChange": false,
			//"bFilter": false,
			//多語言配置
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
			},
			"aoColumnDefs": [{ "sSortDataType": "dom-btn",  "aTargets": ary}]
	};
	return option;
}
$(function(){
	/* Create an array with the values of all the input boxes in a column */
	$.fn.dataTableExt.afnSortData['dom-text'] = function  ( oSettings, iColumn )
	{
		return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
			return $('td:eq('+iColumn+') input', tr).val();
		} );
	};
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
	/* Create an array with the values of all the select options in a column */
//	$.fn.dataTableExt.afnSortData['dom-select'] = function  ( oSettings, iColumn )
//	{
//		return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
//			return $('td:eq('+iColumn+') select', tr).val();
//		} );
//	};

	/* Create an array with the values of all the checkboxes in a column */
	$.fn.dataTableExt.afnSortData['dom-checkbox'] = function  ( oSettings, iColumn )
	{
		return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
			return $('td:eq('+iColumn+') input', tr).prop('checked') ? '1' : '0';
		} );
	};
	$(".dtWithInput").dataTable(getOptionBTN([ 1,2,3,4,5 ]));
});	