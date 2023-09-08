function setSelectRange(name,y,maxyear,maxQ,minyear,minQ){
	var $selectitem=$("select[name='"+name+"']");
	if(y===maxyear){
		$selectitem.find("option").each(function(){
			removeSpan($(this));
			if($(this).val()>maxQ){
				addSpan($(this));
			}
		});
	}else if(y===minyear){
		$selectitem.find("option").each(function(){
			removeSpan($(this));
			if($(this).val()<minQ){
				addSpan($(this));
			}
		});
	}else{
		$selectitem.find("option").each(function(){
			removeSpan($(this));
		});
	}
} 
function setYearSelectRange($syear,$eyear){
	$syear.change(function(){
		var dyear = parseInt($(this).val(),10);
		 hideYearRange($eyear,dyear);
	});
} 
function hideYearRange($year,dyear){
	$year.find("option").each(function(){
		removeSpan($(this));
		var nowyear = parseInt($(this).val(),10);
		if(nowyear<dyear){
			addSpan($(this));
		}
	});
} 
function removeSpan($item){
	 if ( $item.parent().is( "span" ) ) {
		 $item.unwrap("<span style='display:none'></span>");
      }
}
function addSpan($item){
	$item.wrap("<span style='display:none'></span>");
}
function setTWAddrSpan($TWlev1,$TWlev2){
	$TWlev1.change(function(){
		var classNo="."+$(this).val();
		setTWAddrChangeSpan($TWlev2,classNo);
	});
}
function setTWAddrSpan($TWlev1,$TWlev2,fin){
	$TWlev1.change(function(){
		var classNo="."+fin+$(this).val();
		setTWAddrChangeSpan($TWlev2,classNo);
	});
}
function setTWAddrChangeSpan($TWlev2,classNo){
	$TWlev2.find("option").each(function(){
		removeSpan($(this));
	});
	$TWlev2.find("option").not(classNo).each(function(){
		addSpan($(this));
	});
	//107-08-29 變更城市選項後，讓select2 選第一個
	$TWlev2.select2("val", "0");
}
