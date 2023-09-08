function inputTextNext(container,className,notCheck){
	var noDot=className.replace(".","");
	var setInputR;
	if(container.jquery===undefined){
		setInputR=container+" input[type='text']";
		$(setInputR).not("input[aria-controls]").not(notCheck).addClass(noDot);
	}else{
		container.find("input[type='text']").not(notCheck).addClass(noDot);
	}	
	$(className).keydown(function(event){
		if(event.which===13){
			event.preventDefault();
			var $contain=$(this).parents(container);
			var $pitem=$contain.find(className);
			if($pitem.length!=0){
				var x=$pitem.index($(this))+1;
				var $focasItem=$contain.find(className+":eq("+x+")");
				if($focasItem.length>0&&$focasItem.is(":visible")){
					$focasItem.focus();
				}else{
					$(this).blur();
					/*if($contain.find("#btnAddSent").length>0&&container.jquery===undefined){
						$contain.find("#btnAddSent").click(getCheckFNByType(no));
					}*/
				}
			}else{
				$(this).blur();
			}
		}
	});
}