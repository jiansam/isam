/**/
$(function() {
	$(".mySent").click(function(){
		var confirmStr = getConfirmStr();
		if($(".myCount").length){
			if($(".myCount").text()==0){
				alert("請至少選擇一家公司！");
			}else{
				getDatatableParam("#example",$(".myForm"));
			}
		}else{
			$(".myForm").append("<input type='hidden' name='terms' value='"+confirmStr+"'>");
			$(".myForm").submit();
		}
	});
	 optionAlt();
	 
	 $(".qType").change(function(){
		$(".qTypeYear").children("span").each(function(i,e){
		    $(this).children().clone().replaceAll($(this));
		 });
		$(".qTypeYear option:selected").each(function(i,e){ 
			$(this).prop("selected",false);
		});
		$(".MTopicMap :checkbox").prop("checked",false);
		$(".noUsed").show().removeClass("noUsed");
//		$(".notCount").prop("type","checkbox").removeClass("notCount");
		if($(this).val()==="CN"){
			$(".testHide :checkbox").prop("checked",false);
			$(".testHide").hide();
			$(".testHideTxt").show();
		}else{
			$(".testHideTxt").hide();
			$(".testHide").show();
		}
		 optionAlt();
	 }); 
});
function optionAlt(){
	 var onSelectNo =$(".qType").val();
	 $(".qTypeYear").find("option:not(."+onSelectNo+")").each(function(i,e){
		 $(this).wrap("<span style='display:none'></span>");
	 });
	 $(".selectBox").children("option:first").prop("selected",true);
	 $(".MTopicMap").children("div").each(function(i,e){
		var optNo=$(this).prop("class");			
		if(optNo!=onSelectNo){
			$(this).addClass("noUsed");
//			$(this).children(":checkbox").addClass("notCount");
		}
	  });
	 $(".noUsed").hide();
//	 $(".notCount").prop("type","hidden");
}
function getConfirmStr(){
	var confirmStr = "";
	var checkStr=showSelectedConfirm();
	var confirmStr = "問卷種類："+$(".qType option:selected").text();
	var year="";
	var onSelectNo =$(".qType").val();
	$(".qTypeYear option:selected").each(function(){
		if($(this).prop("class")===onSelectNo){
			year+=$(this).html()+",";
		}
	});
	confirmStr=confirmStr+"<br/>調查年度："+year.substring(0, year.length-1)+"年<br/>"+checkStr;
	return confirmStr;
}

/*問卷公司的datatable*/
$(function() {
	if($("#example").length>0){
		
		var oTable=$("#example").dataTable({
			"bAutoWidth" : false, //自適應寬度
			/* "bJQueryUI": true, */
			"aLengthMenu": [[5, 10, 20], [5, 10, 20]],
			"iDisplayLength": 5,
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
			                 { "bSortable": false,'sClass':'center', "aTargets": [ 0,1 ] }
			                 ],
			                 "aaSorting": [[ 2, 'desc' ]],
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
			                	 "sSearch" : "搜尋",
			                	 "oPaginate" : {
			                		 "sFirst" : "最前頁",
			                		 "sPrevious" : "上一頁",
			                		 "sNext" : "下一頁",
			                		 "sLast" : "最末頁"
			                	 }
			                 },
			                 "aoColumns":[
			                              null,
			                              null,
			                              {"sSortDataType":"chinese"},
			                              null
			                              ]			
		});
		$(".clearAll").click(function(){
			var nNodes = oTable.fnGetNodes();
			$('input:checkbox', nNodes).each( function() {
				$(this).prop("checked",false);
			});
			setSelectedCount();
		});
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
	}
});
