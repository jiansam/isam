function getYear(){
	var date = new Date();
	var year = date.getFullYear()-1911;
	return year;	
}
function setYearOption($year){
	var year = getYear();
	var sel="";
	for(var i=year;i>=year-10;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$year.html(sel);
	$year.find("option:first").prop("selected",true);
}
function backwardYearOption($year,selectY,minusY,limitUY,limitDY){
	var pY=parseInt(minusY/2, 10);
	var dY=parseInt(selectY,10);
	var maxyear = dY+pY;
	var minY=dY-pY;
	if(minY<=limitDY){
		maxyear=limitDY+parseInt(minusY, 10);
		if(maxyear>limitUY){
			maxyear=limitUY;
		}
		minY=limitDY;
	}
	var sel="";
	for(var i=maxyear;i>=minY;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$year.html(sel);
	$year.find("option[value='"+dY+"']").prop("selected",true);
}
function forwardYearOption($year,selectY,minusY,limitUY,limitDY){
	var pY=parseInt(minusY/2, 10);
	var dY=parseInt(selectY,10);
	var maxyear = dY+pY;
	var minY=dY-pY;
	if(maxyear>=limitUY){
		maxyear=limitUY;
		minY=limitUY-parseInt(minusY, 10);
		if(minY<limitDY){
			minY=limitDY;
		}
	}
	var sel="";
	for(var i=maxyear;i>=minY;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$year.html(sel);
	$year.find("option[value='"+dY+"']").prop("selected",true);
}
function setYearUpperDownBound($year,pY){
	var limitUY = parseInt(getYear(),10);
	$year.change(function(){
		var nowyear=$(this).val();
		var downyear=$year.find("option:last").val();
		var upyear=$year.find("option:first").val();
		if(nowyear===downyear){
			backwardYearOption($year,nowyear,pY,limitUY,80);
		}else if(nowyear==upyear){
			forwardYearOption($year,nowyear,pY,limitUY,80);
		}
	});
}