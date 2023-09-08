$(function(){
	var option={
			"bAutoWidth" : false, //自適應寬度
			"bLengthChange": false,
			"aoColumnDefs": [
			    { "bSortable": false,'sClass':'center', "aTargets": [ 0 ] }
			],
			"bDestroy": true,
			"aaSorting": [[ 1, 'desc' ]],
			//"bFilter": false,
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
			}
	};
 	$("input[name='pNo']").keyup(function(){
 		var count= $(this).val().length;
 		if(count!=0){
	 		$("#reNoCount").text("("+count+")");
 		}else{
 			$("#reNoCount").text("");
 		}
 	});
 	$("#myInsert").click(function(){
 		if($("input[name='noNeed']:checked").val()==="1"){
 			//$("#projRForm").submit();
 			getDatatableParam(".receiveTable",$("#projRForm"));
 		}else if(checkPRValue()){
 			getDatatableParam(".receiveTable",$("#projRForm"));
 		}		
 	});
 	$("input[name='noNeed']").click(function(){
 		if($(this).val()==="1"&&$("input[name='financial']").length>0){
 			$("input[name='financial'][value='L']").prop("checked",true);
 		}
 	});
 	$("#myupdate").click(function(){
 		if($("input[name='noNeed']:checked").val()==="1"){
 			//$("#projRForm").submit();
 			getDatatableParam("#changeReceive",$("#projRForm"));
 		}else if(checkPRValue()){
 			getDatatableParam("#changeReceive",$("#projRForm"));
 		}		
 	});
 	$("#mydelete").click(function(){
 		var result=0;
 		//console.log($("input[name='repserno']").val())
 		$.post( getRootPath()+"/console/project/checkprcomfirm.jsp",{
			'repserno':$("input[name='repserno']").val()
		}, function(data){
			//console.log(data)
			result=data[0];
		},"json").done(function(){
			if(result>0){
				$( "<div title='轉往線上申報待確認'>於線上申報待確認列表裡有相同填報時點資料，無法進行刪除，即將轉往線上申報待確認頁面!</div>" ).dialog({
				     modal: true,
				     close: function( event, ui ) {
				    	 postUrlByForm('/console/project/listrpconfirm.jsp',{'investNo':$("input[name='investNo']").val()});
				     }
				})
			}else{
				$("<div style='font-size='12px;''>您即將刪除本筆資料，請確認是否繼續?</div>").dialog({
					width: 350,
					modal:true,
					title:'刪除此筆資料',
					buttons: {
				        "確定": function() {
				          postUrlByForm('/console/project/projectreportedit.jsp',{'investNo':$("input[name='investNo']").val(),'repserno':$("input[name='repserno']").val(),'edittype':'delete'});	
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
 	var oTable=$(".receiveTable").dataTable(option); 
 	listenKeyUp(oTable,option);
});
function listenKeyUp(oTable,option){
	$(".pInvestNo").keyup(function(){
		var investNo=$(this).val();
		listenKeyUp2(oTable,option,investNo);
	});
	listenChange(oTable,option);
}
function listenChange(oTable,option){
	$(".pInvestor").change(function(){
		listenChange2(oTable,option,$(".pInvestNo").val())
	});
}
function listenChange2(oTable,option,investNo){
	if(!startsWith(investNo,"6")&&investNo.length===6){
		alert("專案必須以6開頭");
		$(this).val("");
	}else if(investNo.length===6){
		oTable.fnDestroy();
//		clearExceptInvestNO();
		$(".myWait").show();
//		$("input[name='pNo']").focus();
		var d=new Date();
		var idno="";
//		var url=getRootPath()+'/console/project/getmdbyinvestno.jsp';
		var url=getRootPath()+'/console/project/getprojectreceive.jsp';
		if($(".pInvestor").length>0){
			idno=$(".pInvestor").val();
		}
		$.getJSON(url, {
			"investNo" : investNo,
			"date":d.getTime(),
			"IDNO":idno
		}, function(data) {
			if(data!=null&&data.length!=0){
				var recevice="";
				if(data!=null){
					for(var i=0;i<data.length;i++){
						recevice+="<tr><td style='text-align: center;'><input type='checkbox' name='receviceNoTemp' value='"+data[i].receiveNo+"'></td>";
						recevice+="<td>"+data[i].respDate+"</td><td>"+data[i].receiveNo+"</td><td>"+data[i].appName+"</td></tr>";
					}
					$("div:has(.SetReceive)").show();
					$(".SetReceive").html(recevice);
					$(".receiveTable").dataTable(option);							
				}
			}else{
				alert("查無此案號");
				listenChange(oTable,option);
				clearJsonResult();
			}
			$(".myWait").hide();
		}).done(function(){
			var d=new Date();
			listenChange(oTable,option);
		});
	}
}
function listenKeyUp2(oTable,option,investNo){
		if(!startsWith(investNo,"6")&&investNo.length===6){
			alert("專案必須以6開頭");
			$(this).val("");
		}else if(investNo.length===6){
			oTable.fnDestroy();
			clearExceptInvestNO();
			$(".myWait").show();
			$("input[name='pNo']").focus();
			var d=new Date();
			var idno="";
			var url=getRootPath()+'/console/project/getmdbyinvestno.jsp';
			$.getJSON(url, {
				"investNo" : investNo,
				"date":d.getTime(),
				"idno":idno
			}, function(data) {
				if(data!=null&&data.length!=0){
					var cnName="大陸投資事業："+data[0].cnName;
					$(".setCnName").show().html(cnName);
					var lenCompany = data[1].length;
					if(lenCompany==0){
						clearJsonResult();
						alert("此案號非專案資料，請重新輸入");
						listenKeyUp(oTable,option);
					}else{
						var investorJson="投資人：<select class='pInvestor' style='width: 250px;' name='investor'>";
						for(var i=0;i<lenCompany;i++){
							investorJson+="<option value='"+data[1][i].idno+"'>"+data[1][i].investor+"</option>";
						}
						investorJson+="</select>";
						$(".setInvestor").show().html(investorJson);						
						var recevice="";
						if(data[2]!=null){
							for(var i=0;i<data[2].length;i++){
								recevice+="<tr><td style='text-align: center;'><input type='checkbox' name='receviceNoTemp' value='"+data[2][i].receiveNo+"'></td>";
								recevice+="<td>"+data[2][i].respDate+"</td><td>"+data[2][i].receiveNo+"</td><td>"+data[2][i].appName+"</td></tr>";
							}
							$("div:has(.SetReceive)").show();
							$(".SetReceive").html(recevice);
							$(".receiveTable").dataTable(option);							
						}
					}
				}else{
					alert("查無此案號");
					listenKeyUp(oTable,option);
					clearJsonResult();
				}
				$(".myWait").hide();
			}).done(function(){
				var d=new Date();
				listenKeyUp(oTable,option);
			});
		}
}
function checkPRValue(){
	
	var x = $("input[name='pNo']").val().length;
	var y = $("#pYear :selected").val();
	var z=true;
	if(x!=11&&y>=100){
		alert("100年後文號須為11碼");
		$("input[name='pNo']").focus();
		z=false;
		return false;
	}
	if($("input[name='repType']").val()==="Y"){
		$("#projRForm").find("input:text").not("#help :input").not(".noCheckPY").each(function(){
			var len=$(this).val().length;
			if(len===0){
				alert("此欄為必填欄位");
				$(this).focus();
				z=false;
				return false; 				
			}
		});	
	}else{
		$("#projRForm").find("input:text").not("#help :input").each(function(){
			var len=$(this).val().length;
			if(len===0){
				alert("此欄為必填欄位");
				$(this).focus();
				z=false;
				return false; 				
			}
		});	
	}
	return z;
}
function clearJsonResult(){
	$(".pInvestNo").val("");
	clearExceptInvestNO();
}
function clearExceptInvestNO(){
	$(".setCnName").html("").hide();
	$(".setInvestor").html("").hide();
	$(".SetReceive").html("");
}
$(function() {
	$(".selectThispage").click(function(){
		$(this).closest("fieldset").find(":checkbox").each(function(){
			$(this).prop("checked",true);
		});
	});
	$(".unselectThispage").click(function(){
		$(this).closest("fieldset").find(":checkbox").each(function(){
			$(this).prop("checked",false);
		});
	});
});