/*
function getNTBTDatasList(investNo){
	var data = new FormData();
	data.append("investNo", investNo);
	data.append("type", "list");
	ajaxByForm(data, context + "/console/cnfdi/NTBTDatas.view", "list")
	
}
*/

function toAddSFile(){   //$.post( url, [data], [success], [dataType] )
	$.post( 
		context+"/console/cnfdi/content/edit-OFINTBTDatas.jsp",
		
		{ "type" : "add",
		  "investNo" : investNo,
		  "id" : "0",
		  "title": ""},
		
		function(response){
			$( "#upfile" ).html(response); 
			setDialog("#upfile", "800", true, "新增財務資料下載", true);
		},

		"html" 
	);
}

function toEditSFile(index){
	var id = $(index).prop("alt");
	var title = $(index).parents("tr").find("td:eq(1)").text();
	$.post( 
			context+"/console/cnfdi/content/edit-OFINTBTDatas.jsp",
			
			{ "type" : "edit",
			  "investNo" : investNo, 
			  "id" : id,
			  "title": title },
			
			function(response){
				$( "#upfile" ).html(response); 
				setDialog("#upfile", "800", true, "編輯財務資料下載", true); //setDialog(id, width, isModal, header, isAutoOpen)
			},

			"html" 
		);
}

/*
function toUploadFile(){
	if(confirm("是否上傳檔案") == false){
		return;
	}
	var id = $("#myform input[name='id']").val();
	
	//開啟upload轉圈圈
//	$("#uploadDiv").ajaxStart(
//			function(){ 
//				$(this).show(); 
//			}); 
//	$("#uploadDiv").ajaxStop(
//			function(){ 
//				$(this).hide(); 
//			});
	$("#uploadDiv").show();
	$.ajax({
		url : context + "/console/cnfdi/NTBTDatas.view",
		type: "POST",
		data: new FormData($('#myform')[0]),
		cache: false, //上傳文件不需暫存
	    processData: false, //data值是FormData對象，不需要對數據做處理
	    contentType: false, //由<form>表單構成的FormData，已經聲明屬性enctype="multipart/form-data"，
		success : function(response) {
			
			//關閉dialog
			$("#upfile").dialog("close");
			
			//有資料進行
			if(response != ""){
				
				//清除舊的Datable設定 1.9版本寫法   ＊id要改＊
				$("#NTBT #rows").html("");
				var table = $('#NTBT').dataTable();
				table.fnClearTable();
				table.fnDestroy();
				
				//放入新的列表
				setNTBTDatasTable(response);
				$("#uploadDiv").hide();
				setTimeout(
						function(){
							if(id == 0){
								alert("資料已經新增");
							}else{
								alert("資料已經修改");
							}
						}, 500);
				
			}//end if
		}
	});
}
*/

/*
function toDelFile(index){
	var data = new FormData();
	data.append("investNo", investNo);
	data.append("type", "del");
	data.append("id", $(index).prop("alt"));
	
	$.ajax({
		url : context + "/console/cnfdi/NTBTDatas.view",
		type: "POST",
		data: data,
		cache: false, //上傳文件不需暫存
	    processData: false, //data值是FormData對象，不需要對數據做處理
	    contentType: false, //由<form>表單構成的FormData，已經聲明屬性enctype="multipart/form-data"， 原預設"application/x-www-form-urlencoded"
		error : function(xhr) {
		},
		success : function(response) {
			//有資料進行
			if(response != ""){
				
				$("#NTBT #rows").html("");
				//清除舊的Datable設定 1.9版本寫法   ＊id要改＊
				var table = $('#NTBT').dataTable();
				table.fnClearTable();
				table.fnDestroy();
				//要是刪到最後一筆，會沒有列表可列，所以出字串
				if(response != "delSucess"){
					
					//放入新的列表
					setNTBTDatasTable(response);
				}
				
				setTimeout(
						function(){		
							alert("資料已經刪除");
						}, 500);
			}//end if
		}
	});
}
*/

function toDelFile(index){
	$( "<div>確定刪除本筆資料？</div>" ).dialog({
	      resizable: false,
	      height:200,
	      modal: true,
	      title:'即將刪除',
	      buttons: {
	      "確定": function() {
	    	  forwardByForm(context + "/console/cnfdi/NTBTDatas.view" , {"investNo":investNo, "type":"del", "id":$(index).prop("alt")});
	          $( this ).dialog( "close" );
	        },
	       "取消": function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    })

}


function forwardByForm(servletUrl,values){
	var $form = $("<form></form>" , {"method":"post", "action":servletUrl} );
	var params = "";
	$.each(values, function(key,value){
					params += "<input type='hidden' value='"+value+"' name='"+key+"' />";
				   }
	);
	$form.append(params);
	$('body').append($form);
	$form.submit();
}

/* ================================================================================================ */
/* DataTable */
/* ================================================================================================ */
function setDataTable_NTBT(){
	var columnSet;
	if(isConsole == true){
		columnSet = {"aoColumnDefs": [
						{ "aTargets": [0,2,3,4,5],   "sClass":"center" },
						{ "aTargets": [1,2],       "sSortDataType":"chinese" },
						{ "aTargets": [0,3,4,5],   "bSortable": false } ],
					}
	}else{
		columnSet = {"aoColumnDefs": [
						{ "aTargets": [0,2,3],   "sClass":"center" },
						{ "aTargets": [1,2],   "sSortDataType":"chinese" },
						{ "aTargets": [0,3],   "bSortable": false } ],
					}
	}
	
	//$.extend() 將所有屬性設定不重複的相加合成一個新的
	var setting = 
		$.extend( 
				{	"aaSorting" : [[ 1, "asc" ]],
					"bFilter": false,
					"bInfo": false,
					"bPaginate": false,
					"fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
				},
				columnSet,
				sdInitDataTableSetting(),
				sdSortChinese()
		)
	return setting;
}

function setNTBTDatasTable(jsonStr){
	if(jsonStr == ""){
		return;
	}
	var url = ""
	var datas = JSON.parse(jsonStr);
	for(var n=0; n<datas.length; n++){
		$("#NTBT #template").clone().removeAttr("id").appendTo("#NTBT #rows").show();
		//$("#NTBT #rows tr:last td.no ").html("("+(n+1)+")");
		$("#NTBT #rows tr:last td.title").html(datas[n].title);
		$("#NTBT #rows tr:last td.createtime_ROC").html(datas[n].createtime_ROC);
		$("#NTBT #rows tr:last td.down a").attr("href", context+"/downloadNTBTDatas.view?id="+datas[n].id);
		if(isConsole == true){ //後台
			$("#NTBT #rows tr:last td.del input[name='del']").attr("alt", datas[n].id);
			$("#NTBT #rows tr:last td.edit input[name='edit']").attr("alt", datas[n].id);
		}
		
		
	}
	var setting = setDataTable_NTBT();
	var obj = $('#NTBT').DataTable(setting); 
	//1.9 ----------------------------------------
	//大寫DataTable，可以建立，可以取物件（回傳API物件）
	//小寫dataTable，可以建立，可以取物件（回傳jQuery物件）
	//1.10----------------------------------------
	//大寫傳回DataTable物件，小寫取回jQuery物件
	//結論-----------------------------------------
	//1.9  大小寫沒差別 dataTable
	//1.10以後一律寫DataTable
	if(isConsole == true && isUserCanEdit === "false"){ //後台+沒有編輯權限（隱藏 刪除+編輯）
		obj.fnSetColumnVis( 4, false); //jQuery隱藏欄位
		obj.fnSetColumnVis( 5, false);
	}
}


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
		draggable : true, //滑鼠按住標題列拖曳，預設開啟(true)
		//dialogClass : "dlg-no-close", //隱藏右上角的close button X
		closeOnEscape : true, //按"Esc"關閉，預設開啟(true)。
		resizable : false, //設定dialog box縮放功能(滑鼠按住視窗右下角拖曳)，預設關閉(false)。
		title : header, //標題列的標題，預設null
		beforeClose : function(){ //關閉前做的事情
			$(id).html("");
		},
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
	$("#"+id).dialog("open");
}
/* ================================================================================================ */
/* ajax */
/* ================================================================================================ */
function ajaxByForm(formData, servletURL, id){
	$.ajax({
		url : servletURL,
		data: formData,
		method: "POST",
		cache: false, //上傳文件不需暫存 （預設是true）
	    processData: false, //data值是FormData對象，不需要對數據做處理
	    contentType: false, //由<form>表單構成的FormData，已經聲明屬性enctype="multipart/form-data"， 原預設"application/x-www-form-urlencoded"
		success : function(response) {
			if(id == "list"){
				setNTBTDatasTable(response)
			}
		}
	});
}


function ajaxByData(data, servletURL, id){
	$.ajax({
		url : servletURL ,
		data : data,
		method : 'POST',
		cache : false, //IE不要cache （預設是true）
		async : true, //是否採用非同步（預設是true）
		contentType : "application/x-www-form-urlencoded" (預設),
		success : function(response) {
		}
	});
}


function ajaxByDialog(servletURL, id){
	$.ajax({
		url : url,
		method: "POST",
		cache: false,
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
