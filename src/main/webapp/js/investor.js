$(function() {
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
	$("#mySearch").click(function(){
		 getCompanyList();
	});
});
function getCompanyList(){
	var oTable=$("#example").dataTable();
	var idno=$.trim($("input[name='IDNO']").val());
	var investor=$.trim($("input[name='investName']").val());
	if((idno+investor).length===0){
		alert("請輸入查詢條件");
		return false;		
	}else if(isNaN(idno)){
		alert("統編為數字，請輸入數值");
		return false;
	}
	 var dateTest=new Date();
	 var option={
			 //"bAutoWidth" : false,自適應寬度
	   			"fnDrawCallback": function ( oSettings ) {
	   				if ( oSettings.bSorted || oSettings.bFiltered ){
	   					for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
	   						$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
	   					}
	   				}
	   			},
	   			"aoColumnDefs": [
	   			    { "aTargets": [0],"bSortable": false,'sClass':'center'  }
	   			 	/*{ "bSearchable":false,"bVisible":false, "aTargets": [3]},*/
	   			],
	   			"aaSorting": [[ 1, 'asc' ]],
	   			/* "sPaginationType" : "full_numbers", */  //顯示全部的分頁器
	   			//多語言配置
	   			"oLanguage" : {
	   				"sProcessing" : "正在載入中......",
	   				"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
	   				"sZeroRecords" : "對不起，查詢不到相關資料！",
	   				"sEmptyTable" : "目前尚無符合條件之資料！",
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
	   				{"sWidth":"15%"},
	   				{"sWidth":"20%"},
	   				{"sSortDataType":"chinese"}
	   				/*{"sWidth":"25%"}*/
	   			]	   			
	   		};
	 var url=getRootPath()+'/console/commit/getinvestors.jsp';
	 $.getJSON(url,{
		 "investor":encodeURI(investor),
		 "IDNO":idno,
		 "test":dateTest.getTime()},function(data){
	   	var pjpivot="";
	   	for(var i=0;i<data.length;i++){
	   		pjpivot+="<tr><td></td><td>";
	   		pjpivot+=data[i][0];
	   		pjpivot+="</td><td><span id='myInvestor'>"+data[i][1]+"</span><span id='myFlag' style='display:none;'>"+data[i][2];
	   		pjpivot+="</span></td></tr>";
	   	}
	   	oTable.fnDestroy();
	   	$(".SetInvestors").html(pjpivot);
	   	$("#divExam").show();
	   	oTable.dataTable(option);
	});
}
$(function() {
	//監測tr click
	$("body").on('click',"#example tbody tr",function(){
		var nTds=$('td',this);
		var idno=$(nTds[1]).text();
		var investor=$(nTds[2]).find("#myInvestor").text();
		var flag=$(nTds[2]).find("#myFlag").text();
		if(flag==="N"){
			var forConfirm="請確認新增下列這筆資料：<br/>統編："+idno+"<br/>投資人名稱："+investor;
			$("<div style='font-size='14px;''>"+forConfirm+"</div>").dialog({
				width: 400,
				modal:true,
				title:'確認新增這筆企業',
				buttons: {
			        "確定": function() {
			        	var ary={"serno":idno};
			        	var url ="/console/commit/addcommitinvestor.jsp";
			        	postUrlByForm(url,ary);
			         $( this ).dialog( "close" );
			        },
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
				}
			});
		}else{
			var forConfirm="該筆資料已經存在，請點選確定轉往該頁面：<br/>統編："+idno+"<br/>投資人名稱："+investor+"<br/>";
			$("<div style='font-size='14px;''>"+forConfirm+"</div>").dialog({
				width: 400,
				modal:true,
				title:'轉往修改頁面',
				buttons: {
			        "確定": function() {
			        	var ary={"serno":idno};
			     		var url="/console/commit/showcommitdetail.jsp";
			     		postUrlByForm(url,ary);
			        	$( this ).dialog( "close" );
			        },
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
				}
			});
		}
	});
});