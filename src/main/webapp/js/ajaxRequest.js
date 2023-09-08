/* ================================================================================================ */
/* dialog：
 * 1.如果<div>內已有內容，要一載入頁面就先setDialog（才能隱藏div），等要使用再打開 ；
 * 2.若是使用他頁jsp，可以等response放入後setDialog並自動打開*/
/* ================================================================================================ */
function setDialog(id, width, isModal, header, isAutoOpen){
	$(id).dialog({
		autoOpen : isAutoOpen,
		show : { effect : "blind", duration : 300},
		hide : { effect : "explode", duration : 300},
		width : width,
		modal : isModal, //鎖定頁面功能，預設關閉(false)
		//dialogClass : "dlg-no-close", //隱藏右上角的close button X
		closeOnEscape : true, //按"Esc"關閉，預設開啟(true)。
		draggable : true,  //滑鼠按住標題列拖曳，預設開啟(true)
		resizable : false, //設定dialog box縮放功能(滑鼠按住視窗右下角拖曳)，預設關閉(false)。
		title : header, //標題列的標題，預設null
		//beforeClose : function(){ //關閉前做的事情
		//	$(id).html("");
		//},
	});
}

function setDialog2(id, width, isModal, header, isAutoOpen, isEmptyDiv){
	$(id).dialog({
		autoOpen : isAutoOpen,
		show : { effect : "blind", duration : 300},
		hide : { effect : "explode", duration : 300},
		width : width,
		modal : isModal, //鎖定頁面功能，預設關閉(false)
		//dialogClass : "dlg-no-close", //隱藏右上角的close button X
		closeOnEscape : true, //按"Esc"關閉，預設開啟(true)。
		draggable : true,  //滑鼠按住標題列拖曳，預設開啟(true)
		resizable : false, //設定dialog box縮放功能(滑鼠按住視窗右下角拖曳)，預設關閉(false)。
		title : header, //標題列的標題，預設null
	});
	
	if(isEmptyDiv){
		$(id).dialog({
			beforeClose : function(){ //關閉前做的事情
				$(id).html("");
			},
		});
	}
}

//自動打開對話視窗並出 雙button - 承諾事項解除管制時使用
function open_dialog(id, width, isModal, header, isAutoOpen){
	$(id).dialog({
		autoOpen : isAutoOpen,
		show : { effect : "blind", duration : 300},
		hide : { effect : "explode", duration : 300},
		width : width,
		modal : isModal,                //鎖定頁面功能，僅能點選dialog box的元件，預設關閉(false)
		draggable : true,               //滑鼠按住標題列拖曳，預設開啟(true)
		title : header,                 //標題列的標題，預設null
		//dialogClass : "dlg-no-close", //隱藏右上角的close button X
		//closeOnEscape : true,         //按"Esc"關閉，預設開啟(true)。
		//draggable : false,            //設定dialog box拖曳功能(滑鼠按住標題列拖曳)，預設開啟(true)。
		//resizable : false,            //設定dialog box縮放功能(滑鼠按住視窗右下角拖曳)，預設關閉(false)。
		//close: function() {}
		buttons: [	
					{ 	text : "是",
						click: function() {
							$("input[name='isEditContact']").val("Y");
							$("#projRForm").submit();	
							$( this ).dialog( "close" );
						}
					},
					{ 	text : "否",
						click: function() {
							$("#projRForm").submit();
							$( this ).dialog( "close" );
						}
					}
				 ]
		
	});
}

function openDialog(id, url){
	$.ajax({
		url : url,
		cache: false,
		type: "POST",
		error : function(xhr) {
			$(id).html("資料讀取失敗，請稍後重試！");
			$(id).dialog("open");
		},
		success : function(response) {
			$(id).html(response);
			$(id).dialog("open");
		},
	});
}

function open_Dialog(id){
	$(id).dialog("open");
}
/* ================================================================================================ */
/* ajax */
/* ================================================================================================ */
function ajaxByForm(servletURL, formData, doThing){
	$.ajax({
		url : servletURL,
		data: formData,
		method: "POST",
		cache: false, //上傳文件不需暫存 （預設是true）
	    processData: false, //data值是FormData對象，不需要對數據做處理
	    contentType: false, //由<form>表單構成的FormData，已經聲明屬性enctype="multipart/form-data"， 原預設"application/x-www-form-urlencoded"
		success : function(response) {
			if(doThing == "list"){
				setNTBTDatasTable(response)
			}
		}
	});
}


function ajaxByData(servletURL, data, doThing){
	$.ajax({
		url : servletURL ,
		data : data,
		method : 'POST',
		cache : false, //IE不要cache （預設是true）
		async : true, //是否採用非同步（預設是true）
		contentType : "application/x-www-form-urlencoded", //(預設)
		success : function(response) {
			
			if(doThing == "error_Itv" || doThing == "error_fia"){
				if(response == ""){
					return;
				}
				ajaxByDialog(context+"/includes/ajaxRequest_itvOneError.jsp", "#error-dialog", doThing, response)
				
			}
		}
	});
}


function ajaxByDialog(servletURL, id, doThing, jsonStr){
	$.ajax({
		url : servletURL,
		method: "POST",
		cache: false,
		error : function(xhr) {
			$(id).html("資料讀取失敗，請稍後重試！");
			$(id).dialog("open");
		},
		success : function(response) {
			$(id).html(response);
			$(id).dialog("open");
			
			if(doThing == "error_Itv" || doThing == "error_fia"){
				setItvDialog(doThing, jsonStr);
			}
		},
	});
}

/* ================================================================================================ */
/* 國內事業/訪查資料tag - 財務異常、訪查異常 */
/* ================================================================================================ */
function sendAjax(doThing, index){
	var data;
	if(doThing == "error_Itv" || doThing == "error_fia"){
		var id = $(index).closest("tr").find("td:eq(1) > span").text();
		var year = $(index).closest("tr").find("td:eq(1)").text();
		year = year.substring(0, year.indexOf(id));
		var companyName = $(index).closest("div.ui-accordion-content").prev("h3").text();
		data = {"id":id, "doThing":doThing};
		var dialog_header = year + "年" + companyName + " - 異常原因";
		setDialog('#error-dialog', '600', true, dialog_header, false);
	}
	ajaxByData(context+"/ajaxRequest.view", data, doThing);
}
function setItvDialog(doThing, jsonStr){
	var json = JSON.parse(jsonStr);
	var header;
	if(doThing == "error_Itv"){
		header = "訪視異常原因";
	}else{
		header = "財務異常原因";
	}
	$("#error-dialog #errHeader").html("<b>"+header+"</b>")
	for(var i=0; i<json.length; i++){
		$("#error-dialog #errList").append(
				"<span style='display:block;padding-bottom:10px;'><b>"+json[i].optionName+"</b>："+json[i].value+"</span>");
	}
}



/* ================================================================================================ */
/* 投資人、投資案列表  列表頁出架構圖 Dialog */
/* ================================================================================================ */
function ajaxByDataToDialog($index){
	var ntr= $($index).parent(); //因為這個selector是 td，所以要往上一個parnet是tr
	var investorSeq = $('td:eq(2)',ntr).find("span").text();
 	console.log("investorSeq="+investorSeq)
	
	var investor = $('td:eq(2)',ntr).text();
	investor = investor.substring(0, investor.indexOf(investorSeq))
	var investNo = $('td:eq(1)',ntr).text();
	var header = "架構圖列表_"+investor+"("+investNo+")";
	var servletURL = contextPath + "/console/cnfdi/OFIInvestorXFileList.view";
	var jspURL = contextPath + "/includes/investorstructural2.jsp";
	var dialogId = "#listDialog";
	var data = {"investorSeq" : investorSeq};
	setDialog("#listDialog", 1000, true, header, false)
	
	$.ajax({
		url : servletURL ,
		data : data,
		method : 'POST',
		cache : false, //IE不要cache （預設是true）
		async : true, //是否採用非同步（預設是true）
		contentType : "application/x-www-form-urlencoded", //(預設)
		success : function(responseJson) { //回傳JSON
// 			console.log(responseJson) //[[fNo=292, investorSeq=105728, title=104/10/13, fName=400108_1041013.jpg]]
			var json = JSON.parse(responseJson); 
			
			
			var id = dialogId;
			$.ajax({
				url : jspURL,
				cache: false,
				type: "POST",
				error : function(xhr) {
					$(id).html("資料讀取失敗，請稍後重試！");
					$(id).dialog("open");
				},
				success : function(response) {
					$(id).html(response);
					$("#strctural #rows").html(""); //清空tbody內容
					for(var n=0 ; n<json.length ; n++){
						$('#strctural #template').clone().removeAttr("id").appendTo("#strctural #rows").show();
						$('#strctural #rows tr:last > .title').text( json[n].title ); //檔案標題
						$('#strctural #rows tr:last > .createtime').text( json[n].createtime_str ); //建立日期
						$('#strctural #rows tr:last > .fName input[name="btn"]').prop("title", json[n].title)
																				.prop("alt", json[n].fNo);    //檔案下載
					}
					$(id).dialog("open");
				},
			});
		}
	});
}

/* ================================================================================================ */
/* 投資人、投資案列表  列表頁出架構圖 Dialog */
/* ================================================================================================ */
function ajaxByDataToDialog_office($index){
	var ntr= $($index).parent(); //因為這個selector是 td，所以要往上一個parnet是tr
	var investorSeq = $('td:eq(3)',ntr).find("span").text();
 	console.log("investorSeq="+investorSeq)
	
	var investor = $('td:eq(4)',ntr).text();
	investor = investor.substring(0, investor.indexOf(investorSeq))
	var investNo = $('td:eq(2)',ntr).text();
	var header = "架構圖列表_"+investor+"("+investNo+")";
	var servletURL = contextPath + "/console/cnfdi/OFIInvestorXFileList.view";
	var jspURL = contextPath + "/includes/investorstructural.jsp";
	var dialogId = "#listDialog";
	var data = {"investorSeq" : investorSeq};
	setDialog("#listDialog", 1000, true, header, false)
	
	$.ajax({
		url : servletURL ,
		data : data,
		method : 'POST',
		cache : false, //IE不要cache （預設是true）
		async : true, //是否採用非同步（預設是true）
		contentType : "application/x-www-form-urlencoded", //(預設)
		success : function(responseJson) { //回傳JSON
// 			console.log(responseJson) //[[fNo=292, investorSeq=105728, title=104/10/13, fName=400108_1041013.jpg]]
			var json = JSON.parse(responseJson); 
			
			
			var id = dialogId;
			$.ajax({
				url : jspURL,
				cache: false,
				type: "POST",
				error : function(xhr) {
					$(id).html("資料讀取失敗，請稍後重試！");
					$(id).dialog("open");
				},
				success : function(response) {
					$(id).html(response);
					$("#strctural #rows").html(""); //清空tbody內容
					for(var n=0 ; n<json.length ; n++){
						$('#strctural #template').clone().removeAttr("id").appendTo("#strctural #rows").show();
						$('#strctural #rows tr:last > .title').text( json[n].title ); //檔案標題
						$('#strctural #rows tr:last > .createtime').text( json[n].createtime_str ); //建立日期
						$('#strctural #rows tr:last > .fName input[name="btn"]').prop("title", json[n].title)
																				.prop("alt", json[n].fNo);    //檔案下載
					}
					$(id).dialog("open");
				},
			});
		}
	});
}

function opendialog($index){
	
	var ntr= $($index).parent(); //因為這個selector是 td，所以要往上一個parnet是tr
	//var investorSeq = $('td:eq(2)',ntr).find("span").text();
	var investorSeq = $('td:eq(1)',ntr).data("investorseq");
	var investor = $('td:eq(2)',ntr).text();
	investor = investor.substring(0, investor.indexOf(investorSeq))
	var investNo = $('td:eq(1)',ntr).text();
	var header = "架構圖列表_"+investor+"("+investNo+")";
 	console.log("investorSeq="+investorSeq)
	
	$.post( contextPath + "/includes/investorstructural.jsp",
			{
				'investorSeq':investorSeq
			}, 
			function(data){
						$( "#listDialog" ).html(data); 
						$( "#listDialog" ).dialog({
//						      height:420,
						      width:800,
						      modal: true,
						      resizable: false,
						      draggable: false,
						      title : header,
						      close : function( event, ui ) {
						    	  $( "#listDialog" ).html(""); 
						      }
						});
			},"html");
		
}


function opendialog2($index){
	
	var ntr= $($index).parent(); //因為這個selector是 td，所以要往上一個parnet是tr
//var investorSeq = $('td:eq(2)',ntr).find("span").text();
	var investorSeq = $('td:eq(1)',ntr).data("investorseq");
	var investor = $('td:eq(3)',ntr).text();
	investor = investor.substring(0, investor.indexOf(investorSeq))
	var investNo = $('td:eq(2)',ntr).text();
	var header = "架構圖列表_"+investor+"("+investNo+")";
 	console.log("investorSeq="+investorSeq)
	
	$.post( contextPath + "/includes/investorstructural.jsp",
			{
				'investorSeq':investorSeq
			}, 
			function(data){
						$( "#listDialog" ).html(data); 
						$( "#listDialog" ).dialog({
						      height:420,
						      width:800,
						      modal: true,
						      resizable: false,
						      draggable: false,
						      title : header,
						      close : function( event, ui ) {
						    	  $( "#listDialog" ).html(""); 
						      }
						});
			},"html");
		
}

/* ================================================================================================ */
/* 107-08-22 陸資訪查/大陸投資實地訪查/訪查列表  search異常狀況出Dialog */
/* ================================================================================================ */

function openDialog_errMsgXnote(id){
	if(errlist == ''){
		return;
	}
	var arr_json = JSON.parse(errlist);
	
//	console.log(arr_json)
	$.ajax({
		url : contextPath + "/includes/errMsgXnoteList.jsp",
		cache: false,
		type: "POST",
		error : function(xhr) {
			$(id).html("資料讀取失敗，請稍後重試！");
			$(id).dialog("open");
		},
		success : function(response) {
			$(id).html(response);
			
//			//清除舊的Datable跟資料
//			var table = $('#errMsg_table').DataTable();//大寫傳回DataTable物件，小寫取回jQuery物件
//			table.destroy();
			$("#errMsg_table").show();
			$("#errMsg_table #rows").html("");
			//有資料進行
			for(var n=0; n<arr_json.length; n++){
				$("#errMsg_table #template").clone().removeAttr("id").appendTo("#errMsg_table #rows").show();
				$("#errMsg_table #rows tr:last td.ckbox input[name='err-box']").val(arr_json[n].cname).prop("id",arr_json[n].idno);
				$("#errMsg_table #rows tr:last td.name").append(
																  $('<label></label>')
																	.prop("for", arr_json[n].idno)
																	.text(arr_json[n].cname)
																);
				$("#errMsg_table #rows tr:last td.type").html(arr_json[n].type);
			}
			
			var errMsgXnote = $("input[name='errMsgXnote']").val();
			setCheckboxValue($("#errMsg_table input[name='err-box']"), errMsgXnote);
			
			//前面已經移除舊有的DataTable設定，這裡再賦予一次
			$('#errMsg_table').DataTable({
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
				},
				"iDisplayLength" : 10,
				"lengthMenu" : [[5], ['全部']],
				"aaSorting" : [[2, 'asc']],
				"aoColumnDefs": [
			 				   {"aTargets": [2], "bVisible": false},
			 				  ]
			});
			
			//btn_選取時
			$("input[name='getErrbtn']").on('click', function(){
				var table = $('#errMsg_table').DataTable();
				table.fnDestroy();
				var errMsg_choice = $('input:checkbox:checked[name="err-box"]').map(function() {
					return $(this).val(); }).get().join(",");
				
				$("input[name='errMsgXnote']").val(errMsg_choice);
				$("input[name='errMsgXnote']").attr("readonly", true); //選了清單，就不能編輯，除非清除選擇
				$(id).dialog("close");
			})
			
			$(id).dialog("open");
		},
	});
}

function setCheckboxValue(tag, value){  //ex: tag="#itvQAForm input[name='industry']"
	if(value.length == 0){
		return;
	}
	
	var ary;
	if(value.indexOf(",") != -1){
		ary = value.split(",");
	}else {
		ary = [value]; //使用inArry但是value不是Array所以不能比對
	}
	$(tag).each(function(){
		var v= $(this).val();
		if($.inArray(v,ary)!=-1){
			$(this).prop("checked",true);
		}else{
			$(this).prop("checked",false);
		}
	});
}


