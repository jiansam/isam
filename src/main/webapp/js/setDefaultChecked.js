function setSelectedToDefalut(name,value){
	if(value.length>0){
		$("select[name='"+name+"']").find("option[value='"+value+"']").prop("selected",true);
	}
}
function setMutilSelectedToDefalut(name,value){
	if(value.length>0){
		var ary=value.split(",");
		for(var i=0;i<ary.length;i++){
			$("select[name='"+name+"']").find("option[value='"+ary[i]+"']").prop("selected",true);
		}
	}
}
function setRedioToDefalutWithEmpty(name,value){
		$("input[name='"+name+"'][value='"+value+"']").prop("checked",true);
}
function setRedioToDefalut(name,value){
	if(value.length>0){
		$("input[name='"+name+"'][value='"+value+"']").prop("checked",true);
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
function setCheckboxToDefalutWithEmpty(name,valueStr){
		var ary=valueStr.split(",");
		$("input[name='"+name+"']").each(function(){
			var v= $(this).val();
			if($.inArray(v,ary)!=-1){
				$(this).prop("checked",true);
			}
		});
}
function setDefalutOptionByClass($defClass){
	$defClass.each(function(){
		var target=$(this);
		if(target.is("option")){
			target.prop("selected",true);
		}else if(target.is("input[type='radio']")||target.is("input[type='checkbox']")){
			target.prop("checked",true);
		}
	});
	
}