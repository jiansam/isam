function telFmtToText($phone,$result){
	var telNo="";
	$phone.each(function(){
		var val=$.trim($(this).val());
		if($.isNumeric(val)){
			if($(this).hasClass("optionalText")){
				if(val.length>0){
					telNo=telNo.substring(0,telNo.length-1)+"#"+val;
				}else{
					telNo=telNo.substring(0,telNo.length-1);
				}
			}else{
				telNo+=val+"-";
			}
		}
	});
	if(telNo.length>0&&endsWith(telNo,"-")){
		telNo=telNo.substring(0,telNo.length-1);
	}
	var regex = /\d{2,4}-\d{3,4}-\d{3,4}[#\d+]?/;
	if($phone.find(".optionalText").length==0){
		regex = /\d{2,4}-\d{3,4}-\d{3,4}/;
	}
	if(regex.test(telNo)){
		$result.val(telNo);
		return true;
	}else if(telNo.length>0){
		alert("電話不符合格式。");
		$phone.focus();
		return false; 
	}
}