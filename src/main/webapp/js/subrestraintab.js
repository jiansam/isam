$(function() {
	/*test*/
	$("select[name='from']").change(function(){
		var thisVal=parseInt($(this).val(), 10);
		var first=parseInt($("select[name='from'] option:first").val(), 10);
		var last=parseInt($("select[name='from'] option:last").val(), 10);
		if(first-thisVal===0||last-thisVal===0){
			setYearOption('from',thisVal);
			setYearOption('to',thisVal);
			changeYearEditOption(thisVal);
		}
	});
	$("select[name='to']").change(function(){
		var thisVal=parseInt($(this).val(), 10);
		var first=parseInt($("select[name='to'] option:first").val(), 10);
		var last=parseInt($("select[name='to'] option:last").val(), 10);
		var fromVal=$("select[name='from']").val();
		if(first-thisVal===0||last-thisVal===0){
			setYearOption('to',thisVal);
			changeYearEditOption(fromVal);
		}
	});
	/*test end*/
	$( "#tabs" ).tabs();
	$(".nextStep").click(function(){
		var nowStep=$( "#tabs" ).tabs("option", "active");
		var num = parseInt(nowStep, 10)+1;
		$( "#tabs" ).tabs({active:num});
	});
	$(".prevStep").click(function(){
		var nowStep=$( "#tabs" ).tabs("option", "active");
		var num = parseInt(nowStep, 10)-1;
		$( "#tabs" ).tabs({active:num});
	});
	$(".nextFocus").keyup(function(event){
		if(event.keyCode===13){
			if($(this).parent().nextAll('td').has(".nextFocus").length){
				$(this).parent().nextAll('td').first().children(".nextFocus").focus();
				return;
			}
		}
	});
	setYearFromOption();
	setYearToOption();
	setYearFromToOptionDefalut(getYear());
	$("select[name='from']").change(function(){
		var max=parseInt($(this).val(), 10);
		changeYearEditOption(max);		
	});
	$("#myInsert").click(function(){
		if(checkbeforeSubmit()){
			getReadyForm();
			$("#projRForm").submit();
		}
	});
  });

function checkbeforeSubmit(){
	if(checkdatatableChecked($("#receiveTable"),"請至少選擇一個文號",1)&&
	    checkdatatableChecked($("#CNTable"),"請至少選擇一個案號",2)){
		getReadyTable($("#CNTable"));
		getReadyTable2($("#receiveTable"));
		return true;	
	};
}
/*function checkbeforeSubmit(){
	$(".addtable").find("input:text").each(function(i){
		var x = $(this).prop("name","");
	});
	$(".insertTable").find("input:text").each(function(i){
		var value =$(this).prop("value");
		var name =$(this).prop("name");
		$("form").append("<div style='display:none;'><input type='checkbox' checked='checked' name='"+name+"' value='"+value+"'></div>");
		$(this).remove();
//		$(this).prop("readonly",false);
//		var x =$(this).prop("value");
//		$(this).prop("type","checkbox");
//		$(this).prop("checked",true);
//		$(this).prop("value",x);
	});
	if(checkdatatableChecked($("#receiveTable"),"請至少選擇一個文號",1)&&
			checkdatatableChecked($("#CNTable"),"請至少選擇一個案號",2)){
		getReadyTable($("#CNTable"));
		getReadyTable2($("#receiveTable"));
		return true;	
	};
}
*/
function getReadyForm(){
	$(".addtable").find("input:text").each(function(i){
		var x = $(this).prop("name","");
	});
	$(".insertTable").find("input:text").each(function(i){
		var value =$(this).prop("value");
		var name =$(this).prop("name");
		$("form").append("<div style='display:none;'><input type='checkbox' checked='checked' name='"+name+"' value='"+value+"'></div>");
		$(this).remove();
	});
}
function getReadyTable($dataT){
	var oTable=$dataT.dataTable();
	$('input:checked', oTable.fnGetNodes()).each(function(i,ele){
		ele.style.display="none";
		$("#projRForm").append(ele);
	});
}
function getReadyTable2($dataT){
	var oTable=$dataT.dataTable();
	var nNodes = oTable.fnGetNodes();
	$("input[name='receiveNo']:checked", nNodes).each(function(){
		var ntr= $(this).parents('tr')[0];
		var aData=oTable.fnGetData(ntr);
		var finalStr=aData[2]+"&&&"+aData[1]+"&&&"+aData[3];
		var str = "<input type='checkbox' name='receiveNoAdd' value='"+finalStr+"' checked='checked' />";
		$("#tempRecevice").append(str);
	});
}
function checkdatatableChecked($dataT,StrAlert,act){
	var oTable=$dataT.dataTable();
	var checkedbox = $('input:checked', oTable.fnGetNodes());
	if(checkedbox.length == 0){
		alert(StrAlert);
		$( "#tabs" ).tabs("option","active",act);
		return false;
	}else{
		return true;
	}
}
function getYear(){
	var date = new Date();
	var year = date.getFullYear()-1911;
	return year;	
}
function setYearOption(name,dYear){
	var year = parseInt(dYear, 10)+5;
	var start = parseInt(dYear, 10)-5;
	var defYear = parseInt(dYear, 10);
	var sel="";
	for(var i=year;i>=start;i--){
		var str=""+i;
		if(i<100){
			str="0"+str;
		}
		sel+="<option value='"+i+"'>"+str+"年</option>";
	}
	$("select[name='"+name+"']").html(sel);	
	$("select[name='"+name+"']").find("option[value='"+defYear+"']").prop("selected",true);
}
function setYearFromOption(){
	var year = parseInt(getYear(), 10)+5;
	var start = parseInt(getYear(), 10)-5;
	var sel="";
	for(var i=year;i>=start;i--){
		var str=""+i;
		if(i<100){
			str="0"+str;
		}
		sel+="<option value='"+i+"'>"+str+"年</option>";
	}
	$("select[name='from']").html(sel);	
}
function setYearToOption(){
	var year = parseInt(getYear(), 10)+5;
	var start = parseInt(getYear(), 10)-5;
	var sel="";
	for(var i=year;i>=start;i--){
		var str=""+i;
		if(i<100){
			str="0"+str;
		}
		sel+="<option value='"+i+"'>"+str+"年</option>";
	}
	$("select[name='to']").html(sel);
}
function setYearFromToOptionDefalut(year){
	$("select[name='from']").find("option[value='"+year+"']").prop("selected",true);
	changeYearEditOption(year);
}
function setYearFromToDefalut(year,to){
	if(year.length>0&&to.length>0){
		year = parseInt(year, 10);
		to = parseInt(to, 10);
		$("select[name='from']").find("option[value='"+year+"']").prop("selected",true);
		changeYearEditOption(year);
		$("select[name='to']").find("option[value='"+to+"']").prop("selected",true);
	}
}
function changeYearEditOption(max){
	$("select[name='to']").find("option").each(function(){
		if($(this).prop("class")==="hideOptions"){
			$(this).removeClass("hideOptions");
			$(this).unwrap("<span style='display:none;'></span>");
		}
	});
	$("select[name='to']").find("option").each(function(){
		var comp=parseInt($(this).val(), 10);
		if(comp<max){
			$(this).addClass("hideOptions");
			$(this).wrap("<span style='display:none;'></span>");
		}
	});
}
function setSelectedToDefalut(name,value){
	if(value.length>0){
		$("select[name='"+name+"']").find("option[value='"+value+"']").prop("selected",true);
	}
}
function setSelectedToText(name,value){
	if(value.length>0){
		var text = $("select[name='"+name+"']").find("option[value='"+value+"']").text();
		var hiddenStr ="<input type='hidden' name='repType' value='"+value+"'/>"+text;
		$("select[name='"+name+"']").parent().append(hiddenStr);
		$("select[name='"+name+"']").remove();
	}
}
function setCheckboxToDefalut(name,valueStr){
	if(valueStr.length>0){
		  var ary=valueStr.split(",");
		  $("input[name='"+name+"']").each(function(){
			  var v= $(this).val();
			  if($.inArray(v,ary)!=-1){
				  $(this).prop("checked",true);
			  }
		  });
	  }
}
function deleteCommit(serno,idno){
	$("<div style='font-size='12px;''>您即將刪除本筆資料，並將同步移除其所屬執行情形報表，請確認是否繼續?</div>").dialog({
		width: 350,
		modal:true,
		title:'刪除此筆資料',
		buttons: {
	        "確定": function() {
	    	  postUrlByForm('/console/commit/commitdelete.jsp',{'serno':serno,'idno':idno});
	          $( this ).dialog( "close" );
	        },
	        "取消": function() {
	          $( this ).dialog( "close" );
	        }
		}
	});
}
