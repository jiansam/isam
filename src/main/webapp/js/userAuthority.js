$(function(){
	var temp;
	/*A.使用者權限*/
	/*A-1.監聽修改使用者權限*/
	$("#more").click(function(){
		$("#editRange").dialog({
			width: 500,
			modal:true,
			title:'修改使用者編輯查閱權限',
			closeOnEscape: false,
			open: function(event, ui) { 
				$(".ui-dialog-titlebar-close").hide();
				}, 
			buttons: {
		        "確認": function() {
		        	changeDefaultSelect();
		        	var flagTF = getDefaultSelect();
		        	if(flagTF){
		        	  getRangeChecked();
			          $("#more").prop("title","您已經完成進階設定，如需修改請再次點擊");
			          $( this ).dialog( "close" );
		        	}else{
		        		alert("請選取一項以上之權限項目");
		        	}
		        },
		        "取消": function() {
		        	getDefaultSelect();
		        	$("#more").prop("title","您已經取消設定，如需修改請再次點擊");
		          $( this ).dialog( "close" );
		        }
		      }
		});
	});
	
	$(".selectNext").click(function(){
		selectNextLevel($(this));
	});
	
	$(".beSelectedNext").click(function(){
		beSelectedNext($(this));
		checkEditPlusAdd($(this));
	});
});
/*修改權限包含新增*/
function checkEditPlusAdd($item){
	var value=$item.val();
	var flag=$item.prop("checked");
	if(startsWith(value,"E")){
		var name= $item.prop("name");
		var aVal= "A"+value.substring(1,5);
		var $target=$("input[name='"+name+"'][value='"+aVal+"']");
		if(flag){
			$target.prop("checked",flag);
			beSelectedNext($target);
		}
	}else if(startsWith(value,"A")){
		var name= $item.prop("name");
		var aVal= "E"+value.substring(1,5);
		var $target=$("input[name='"+name+"'][value='"+aVal+"']");
		if($target.prop("checked")){
			$item.prop("checked",$target.prop("checked"));
			beSelectedNext($item);
		}
	}
}
/*A-1-1.設定使用者權限預設值*/
function getDefaultSelectBoth(range){
	var text = range.substring(1,range.length-1);
	if(text.length==0){
		getDefaultSelect();
	}else{
		var rangeAry=text.split(", ");
		elDefaultSelect(rangeAry);
	}
	getRangeChecked();
}
function getDefaultSelect(){
	var flagTF=false;
	$("#editRange").find("input:checkbox").each(function(){
		if($(this).hasClass("defaultSelect")){
			$(this).prop("checked",true);
		}else{
			$(this).prop("checked",false);
		}
		var tf=$(this).prop("checked");
		if(tf){
			flagTF=true;
		}
	});
	return flagTF;
}
function getRangeChecked(){
	var rangeStr="";
	$("input[name='range']:checked").each(function(){
		rangeStr+=$(this).val()+",";
	});
	if(rangeStr.length>0){
		rangeStr=rangeStr.substring(0, rangeStr.length-1);
		$("#rangeStr").val(rangeStr);
	}
	return rangeStr;
}
/*A-1-2.修改後，重設使用者權限預設值*/
function changeDefaultSelect(){
//	console.log("changeDefaultSelect");
	$("#editRange").find("input:checkbox").each(function(){
		if($(this).hasClass("defaultSelect")){
			$(this).removeClass("defaultSelect");
		}
		if($(this).is(":checked")){
			$(this).addClass("defaultSelect");
		}
	});
}
/*A-1-3.EL使用者權限預設值*/
function elDefaultSelect(rangeAry){
//	console.log("elDefaultSelect");
	$("#editRange").find("input:checkbox").each(function(){
		var val=$(this).val();
		if($.inArray(val,rangeAry)!=-1){
			$(this).prop("checked",true);
			if($(this).hasClass("beSelectedNext")){
				beSelectedNext($(this));
			}
		}
	});
	changeDefaultSelect();	
}

/*A-2.連動使用者權限項目的下上層*/
/*A-2-1.上層全選全刪連動下層結果*/
function selectNextLevel($selectNext){
//	console.log("selectNextLevel");
	var tdNum=parseInt($selectNext.closest("tr").children("td").index($selectNext.parent("td")));
	var trNum=parseInt($("#editRange tr").index($selectNext.closest("tr")));
	var checkTF=$selectNext.prop("checked");
	var nextNum=parseInt($selectNext.val());
	var trEnd=trNum+nextNum;
	for(var i=trNum+1;i<=trEnd;i++){
		var $target=$("#editRange tr:eq("+i+") td:eq("+tdNum+") :input");
		$target.prop("checked",checkTF);
		checkEditPlusAdd($target);
	}
}

/*A-2-2.下層更動修改上層顯示結果*/
function beSelectedNext($beSelectedNext){
//	console.log("beSelectedNext");
	if(!$beSelectedNext.hasClass("firstItem")){
		var levelTF=$beSelectedNext.prop("checked");
		var tdNum=parseInt($beSelectedNext.closest("tr").children("td").index($beSelectedNext.parent("td")));
		$beSelectedNext.closest("tr").prevAll("tr:has(.selectNext)").each(function(index){
			if(index===0){
				checkSelectNextTF($(this).children("td:eq("+tdNum+")").children("input"),levelTF);
				return;
			}
		});
	}
}
function checkSelectNextTF($selectNext,levelTF){
//	console.log("checkSelectNextTF");
	var tdNum=parseInt($selectNext.closest("tr").children("td").index($selectNext.parent("td")));
	var trNum=parseInt($("#editRange tr").index($selectNext.closest("tr")));
	var checkTF=$selectNext.prop("checked");
	var flagTF=true;
	var nextNum=parseInt($selectNext.val());
	var trEnd=trNum+nextNum;
	for(var i=trNum+1;i<=trEnd;i++){
		flagTF = $("#editRange tr:eq("+i+") td:eq("+tdNum+") :input").prop("checked");
		if(flagTF!=levelTF){
			$selectNext.prop("checked",false);
			return;
		}
	}
	if(flagTF!=checkTF){
		$selectNext.prop("checked",flagTF);
		selectNextLevel($selectNext);
	}
}
function clearAuthority(){
	$("#editRange .datatableApproval input[type='checkbox']").prop("checked",false);
}
function setTeamOne(){
	clearAuthority();
	$("#editRange input[name='range']").each(function(){
		var valStr=$(this).val();
		if(endsWith(valStr,"0401")||endsWith(valStr,"0303")||valStr==="R0301"){
			$(this).prop("checked",true);
			beSelectedNext($(this));
		}
	});
}
function setTeamTwo(){
	clearAuthority();
	$("#editRange input[name='range']").each(function(){
		var valStr=$(this).val();
		if(endsWith(valStr,"0101")||endsWith(valStr,"0102")||endsWith(valStr,"0302")){
			if(startsWith(valStr,"A")||startsWith(valStr,"R")){
				$(this).prop("checked",true);
				beSelectedNext($(this));
			}
		}else if(valStr==="R0301"){
			$(this).prop("checked",true);
			beSelectedNext($(this));
		}
	});	
}
function setTeamFour(){
	clearAuthority();
	$("#editRange input[name='range']").each(function(){
		var valStr=$(this).val();
		if(valStr==="R0301"||valStr==="R0201"){
			$(this).prop("checked",true);
			beSelectedNext($(this));
		}
	});	
}
