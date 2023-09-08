/*驗證正規條件*/
function checkStr(instr,ptn){
	return ptn.test(instr);
}

function checkPWD(instr){
	/*	return checkStr(instr,/[A-Za-z0-9]{6,12}/);
	 */
	return checkStr(instr,/[A-Za-z0-9~!@#$%^&*()_+|}{\/*?><}[;":`]{6,}/);
}
function checkIsEqual(a1,a2){
	if(a1===a2){
		return true;
	}else{
		return false;
	}
}
function checkIsEmpty(a1){
	if(a1.length==0){
		return true;
	}else{
		return false;
	}
}

function checkIsTWWord(instr){
	return checkStr(instr,/^[\u4E00-\u9FA5]+/);
}

function checkIsEmail(instr){
	return checkStr(instr,/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z]+$/);
}
function checkEditValue(){
	var flag=true;
	$("#mytable :input:not(:password)").each(function(){
		var tempStr = $(this).val();
		if($.trim(tempStr).length==0){
			alert("本欄位為必填欄位，請勿輸入空格或未填。");
			$(this).focus();
			flag=false;
			return false;
		}
	});
	return flag;
}
function checkValue(){
	var flag=true;
	$("#mytable :input").each(function(){
		var tempStr = $(this).val();
		if($.trim(tempStr).length==0){
			alert("本欄位為必填欄位，請勿輸入空格或未填。");
			$(this).focus();
			flag=false;
			return false;
		}
	});
	return flag;
}

function checkUser(){
	var flag=true;
	var account=$("#user").val();
	var owner=$("#owner").val();
	var mail=$("#mail").val();
	var newStr=$("#newStr").val();
	var checkStr=$("#checkStr").val();

	if(!checkIsEmpty(newStr)){
		if(!checkIsEqual(newStr,checkStr)){
				alert("您兩次輸入的密碼不一致，請重新輸入。");
				$("#checkStr").val("");
				$("#newStr").val("").focus();
				flag=false;
		}else if(!checkPWD(newStr)){
			alert("您輸入的密碼不符合密碼的格式，請以6個以上英數字或符號組成新密碼。");
			$("#checkStr").val("");
			$("#newStr").val("").focus();
			flag=false;
		}else if(checkIsEqual(newStr,account)){
			alert("密碼不可與帳號相同，請重新輸入。");
			$("#checkStr").val("");
			$("#newStr").val("").focus();
			flag=false;
		}
	}
	if(!checkIsEmail(mail)){
		alert("email格式錯誤，請重新輸入。");
		$("#mail").val("").focus();
		flag=false;
	}else if(!checkIsTWWord(owner)){
		alert("請輸入中文姓名，請重新輸入。");
		$("#owner").val("").focus();
		flag=false;
	}
	return flag;
}

function loginRecordError(){
	$(".yn").each(function(){
		var checkStr = $(this).val();
		if(checkStr==='N'){
			$(this).parent().parent().children().css("color","red");
		}
	});
}

function checkDefaultVal(company,group,able){
	$("#company").children().each(function(){
		if($(this).val()===company){
			$(this).prop("selected","selected");		
		}
	});
	$("input[name='authority']").each(function(){
		if($(this).val()===group){
			$(this).prop("checked","checked");		
		}
	});
	$("input[name='able']").each(function(){
		if($(this).val()===able){
			$(this).prop("checked","checked");		
		}
	});
}
function enterSubmit(){
	$("input[name='account']").focus();
	$("input[name='pWD']").keydown(function(event){
		if(event.keyCode===13){
			$("#sent").click();		
		}
	});
}
$(function() {
	  $( document ).tooltip({track:true});
});