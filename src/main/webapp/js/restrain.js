$(function() {
	$.ajaxSetup({ cache: false });
	
	/* Create an array with the values of all the checkboxes in a column */
	$.fn.dataTableExt.afnSortData['dom-checkbox'] = function  ( oSettings, iColumn )
	{
		return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
			return $('td:eq('+iColumn+') input', tr).prop('checked') ? '1' : '0';
		} );
	};
	$(".help").hide();
	$("#addRecevice").click(function(){
		if(checkRowToTable()){
			addRowToTable();
		}
	});
	$("#addCN").click(function(){
		var a1=$("input[name='AInvestNo']").val();
		if(a1.length===6&&startsWith(a1,'6')){
			if($("input[name='investNo'][value='"+a1+"']").length>0){
				$("input[name='investNo'][value='"+a1+"']").prop("checked",true);
				var oTable=$("#CNTable").dataTable();
				oTable.fnSort([[0,'desc'],[1,'asc']]);
				$("input[name='AInvestNo']").val("");
			}else{
				addCNNameToTable($("input[name='AInvestNo']").val());
			}
		}else if(a1.length === 6){
			$("#ExInvestNo").val($("input[name='AInvestNo']").val());
			$("#addCN-dialog").dialog("open");
		}else{
			alert("案號須為六位碼。");
		}
	});
	
	$("#addCN-dialog").dialog({
		autoOpen: false,
		width: 320,
		show: {
			effect: "blind",
			duration: 1000
		},
		hide: {
			effect: "blind",
			duration: 1000
		}
	});
});

function addExCNNameToTable(){
	var oTable = $("#CNTable").dataTable();
	var ExInvestNo = $("#ExInvestNo").val();
	var ExcnName = $("#ExcnName").val();
	
	var temp2 = ExcnName;
	if(!ExInvestNo.startsWith("6")){
   		temp2 = temp2 + " <img src='" + getRootPath() + "/images/action_delete.gif' onclick='deleteCNName(this)' />";
   	}
	
	$.ajax({
		url : 'add-cdataexception.jsp',
		type: "POST",
		error : function(xhr) {
			$("#addCN-dialog").dialog("close");
			alert("資料寫入失敗，請稍後重試！");
		},
		success : function(response) {
			oTable.fnAddData(["<input type='checkbox' name='investNo' value='" + ExInvestNo + "' checked='checked' />",
				ExInvestNo, temp2]);
			oTable.fnSort([[0,'desc'],[1,'asc']]);
			oTable.$("td:first-child").css('text-align','center');
			
			$("#ExInvestNo").val("");
			$("#ExcnName").val("");
			$("input[name='AInvestNo']").val("");
			$("#addCN-dialog").dialog("close");
		},
		data: {
			InvestNo: ExInvestNo,
			CNName: ExcnName,
			IdNo: $("#Exidno").val(),
			serno: $("#Exserno").val()
		},
		cache: false
	});
}

function deleteCNName(anchor){
	if(!confirm('確認刪除這筆資料？')){
		return false;
	}
	
	var oTable = $("#CNTable").dataTable();
	var row = $(anchor).closest('tr');
	
	$.ajax({
		url : 'delete-cdataexception.jsp',
		type: "POST",
		error : function(xhr) {
			alert("資料刪除失敗，請稍後重試！");
		},
		success : function(response) {
			oTable.fnDeleteRow(row[0]);
		},
		data: {
			InvestNo: row.find("input[name=investNo]").eq(0).val(),
			serno: $("#Exserno").val()
		},
		cache: false
	});
}

function addCNNameToTable(a1){
//	var a1=$("input[name='AInvestNo']").val();
	var url=getRootPath()+"/console/project/getinvestNoName.jsp";
	var dateTest=new Date();
	$.getJSON(url, {
		"investNo" : a1,
		"datetime":dateTest.getTime()
	},function(data) {
		if(data!=null&&data.length>0){
			var temp1 = data[0].investNo;
		   	var temp2 = data[0].cnName;
		   	
		   	if(!temp1.startsWith("6")){
		   		temp2 = temp2 + " <img src='" + getRootPath() + "/images/action_delete.gif' onclick='deleteCNName(this)' />";
		   	}
		   	
			var a0 = "<input type='checkbox' name='investNo' value='"+temp1+"' checked='checked' />";
			var oTable=$("#CNTable").dataTable();
			oTable.fnAddData([a0,temp1,temp2]);
			oTable.fnSort([[0,'desc'],[1,'asc']]);
			oTable.$("td:first-child").css('text-align','center');
			$("input[name='AInvestNo']").val("");
		}else{
			alert(a1);
			alert("查無此案號資料，請重新輸入!");
			$("input[name='AInvestNo']").val("");
		}
	});
}

function addRowToTable(){
	var a1=$("input[name='ARespDate']").val();
	var a2=$("input[name='AReceviceNo']").val();
	var a3=$("input[name='AAppName']").val().replace(/,/g, "、");
	var a0 = "<input type='checkbox' name='receiveNo' value='"+a2+"' checked='checked' />";
	var oTable=$("#receiveTable").dataTable();
	if($("input[name='receiveNo'][value='"+a2+"']").length>0){
		$("input[name='receiveNo'][value='"+a2+"']").prop("checked",true);
	}else{
		oTable.fnAddData([a0,a1,a2,a3]);
	}
	oTable.fnSort([[0,'desc'],[1,'desc']]);
	oTable.$("td:first-child").css('text-align','center');
	$("#formAdd :text").each(function(){
		$(this).val("");
	});
}
function checkRowToTable(){
	var x=true;
	$("#formAdd input:text").each(function(){
		if($.trim($(this).val()).length===0){
			alert("此欄位不得為空白");
			$(this).focus();
			x=false;
			return false;
		}
	});
	return x;
}
function getRestrainTables(idno,type,editType,obj1,obj2){
	var dateTest=new Date();
	var url = getRootPath()+"/console/commit/getrestraindata.jsp";
	$.getJSON(url, {
		"serno" : idno,
		"type":type,
		"datetime":dateTest.getTime()
	},function(data) {
		   	var receviceStr="";
		   	var cname="";
		   	var temp1=data[0].receive;
		   	var temp2=data[0].cnnames;
		   	for(var i=0;i<temp1.length;i++){
		   		receviceStr+="<tr><td><input type='checkbox' name='receiveNo' value='"+temp1[i][1]+"' /></td><td>"+temp1[i][0]+"</td><td>"+temp1[i][1]+"</td><td>"+temp1[i][2]+"</td></tr>";
		   	}
		   	for(var i=0;i<temp2.length;i++){
		   		cname+="<tr><td><input type='checkbox' name='investNo' value='"+temp2[i][0]+"' /></td><td>"+temp2[i][0]+"</td><td>"+temp2[i][1]+"</td></tr>";
		   	}
		   	
			var option =getOptionByType([0]);
			$("#SetReceive").html(receviceStr);
			$("#SetCNName").html(cname);
			$("#receiveTable").dataTable(option);
			$("#receiveTable").dataTable().$("td:first-child").css('text-align','center');
			/*$("#receiveTable").dataTable().fnSetColumnVis(4,false,false);*/
			$("#CNTable").dataTable(option);
			$("#CNTable").dataTable().$("td:first-child").css('text-align','center');
			/*$("#CNTable").dataTable().fnSetColumnVis(3,false,false);*/
			if(editType==="edit"){
				getRestrainEditTables(obj1,obj2);
			}
			$(".myWait").hide();
			$(".help").show();
	});
	
	function getRestrainEditTables(obj1,obj2){
		$.each(obj1,function(i,ele){
				var oTable=$("#receiveTable").dataTable();
				var reNo=obj1[i].receiveNo;
				var reNoInput="input[name='receiveNo'][value='"+reNo+"']";
				if(oTable.$(reNoInput).length>0){
					oTable.$(reNoInput).prop("checked",true);
					/*var pos = oTable.fnGetPosition(oTable.$(reNoInput).parents('tr')[0]);
					oTable.fnUpdate('0',pos,4);*/
				}else{
					var a1=obj1[i].respDate;
					var a3=obj1[i].note;
					var a0 = "<input type='checkbox' name='receiveNo' value='"+reNo+"' checked='checked' />";
					oTable.fnAddData([a0,a1,reNo,a3]);
				}
				oTable.fnSort([[0,'desc'],[1,'desc']]);
				oTable.$("td:first-child").css('text-align','center');
		});
		$.each(obj2,function(i,ele){
			var oTable=$("#CNTable").dataTable();
			var investNo=obj2[i].investNo;
			var investNoInput="input[name='investNo'][value='"+investNo+"']";
			if(oTable.$(investNoInput).length>0){
				oTable.$(investNoInput).prop("checked",true);
				/*var pos = oTable.fnGetPosition(oTable.$(investNoInput).parents('tr')[0]);
				oTable.fnUpdate('0',pos,3);*/
			}else{
				addCNNameToTable(investNo);
			}
			oTable.fnSort([[0,'desc'],[1,'asc']]);
			oTable.$("td:first-child").css('text-align','center');
		});
	}
}
function getOptionByType(ary){

  var option={
			"bAutoWidth" : false, //自適應寬度
			"bLengthChange": false,
			"aaSorting": [[ 0, 'desc' ]],
			"aoColumnDefs": [
			   { 'sClass':'center', "aTargets": [ 0 ] },
			   { "sSortDataType": "dom-checkbox",  "aTargets": ary}
			 ],
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
			}
	};
  return option;
}
