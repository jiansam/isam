jQuery(function($){
	$.fn.extend({
		formatInput: function(settings) {
			var $elem = $(this);
			settings = $.extend({errback: null}, settings);
			$elem.bind("blur.filter_input", $.fn.formatEvent);
		},
		formatEvent: function(e) {
			var elem = $(this);
			var initial_value = elem.val();
			var afterVal="";
			var indexP=initial_value.indexOf(".");
			if(indexP!=-1&&initial_value.length>indexP){
				afterVal=initial_value.substring(initial_value.indexOf(".")+1);
				initial_value=initial_value.substring(0,initial_value.indexOf(".")+1);
			}
			var fmtStr=$.fn.insertCommas(initial_value);
			if(afterVal!=""){
				 fmtStr+=afterVal;
			}
			elem.val(fmtStr);
		},
		insertCommas: function(number) {
			return number.replace(/,/g, "").replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
		}
	});
});
new function($) {
	  $.fn.setCursorPosition = function(pos) {
	    if ($(this).get(0).setSelectionRange) {
	      $(this).get(0).setSelectionRange(pos, pos);
	    } else if ($(this).get(0).createTextRange) {
	      var range = $(this).get(0).createTextRange();
	      range.collapse(true);
	      range.moveEnd('character', pos);
	      range.moveStart('character', pos);
	      range.select();
	    }
	  }
}(jQuery);
(function($) {
    $.fn.getCursorPosition = function() {
        var input = this.get(0);
        if (!input) return; // No (input) element found
        if ('selectionStart' in input) {
            // Standard-compliant browsers
            return input.selectionStart;
        } else if (document.selection) {
            // IE
            input.focus();
            var sel = document.selection.createRange();
            var selLen = document.selection.createRange().text.length;
            sel.moveStart('character', -input.value.length);
            return sel.text.length - selLen;
        }
    }
})(jQuery);
$(function() {
	setYearOption();
	$( document ).tooltip({
		 track:true
	 });
	$(".numberFmt").each(function(){
		var checkNumberFmt;
		if($(this).is("input")){
			checkNumberFmt=$(this).val();
			if(checkNumberFmt.length>0){
				var num=checkNumberFmt.replace(/,/g, "");
				num=parseFloat(num,10).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
				$(this).val(num);
			}
		}else if($(this).is("span")){
			checkNumberFmt=$(this).text();
			if(checkNumberFmt.length>0){
				var num=checkNumberFmt.replace(/,/g, "");
				num=parseFloat(num,10).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
				$(this).text(num);
			}
		}
	});
	$(".numberFmt").formatInput();
	$(".numberFmt").focus(function(event){
		var x = $(this).getCursorPosition()-1;
		var result=getRemoveCommaVal($(this).val());
		$(this).val(result);
		$(this).setCursorPosition(x);
	});
	$(".numberFmt").keyup(function(event){
		if(event.keyCode===13){
			if($(this).parent().next().has(".numberFmt").length>0){
				$(this).parent().next().children(".numberFmt").focus();
			}else{
				if($(this).hasClass("countReach")){
//					if($(this).prop("class").indexOf("countReach")!=-1){
					$(this).closest("tr").next().find("input[class*='lastNumberFmt']").focus();
				}else{
					$(this).blur();
					$(".lastNumberFmt").first().focus();
				}
			}
		}
	});
	$(".pUp,.pDown").keyup(function(){
		getPercentText();
	});
});
function getYear(){
	var date = new Date();
	var year = date.getFullYear()-1911;
	return year;	
}
function setYearOption(){
	var year = getYear();
	var sel="";
	for(var i=year;i>=89;i--){
		sel+="<option value='"+i+"'>"+i+"年</option>";
	}
	$("#pYear").html(sel);
}
function getRemoveCommaVal(number){
	return number.replace(/,/g, "");
}
function getInsertComma(number){
	return parseFloat(number,10).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
}
function getPercentText(){
	var down = getRemoveCommaVal($(".pDown").val());
	var up = getRemoveCommaVal($(".pUp").val());
	if(down.length>0&&up.length>0&&parseFloat(down,10)!=0&&parseFloat(up,10)!=0){
		var result = ((up/down)*100).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
		$(".pPercent").text(result+" %");
	}else{
		$(".pPercent").text("");
	}
}
/*for commit 01*/
function getValueText($Src){
	var result="";
	if($Src.is("input")){
		result=getRemoveCommaVal($Src.val());
	}else{
		result=getRemoveCommaVal($Src.text());
	}
	return result;
}
function getDivideText($up,$down,$target,Suffix){
	var down = getValueText($down);
	var up = getValueText($up);
	var divideR ="";
	if(down.length>0&&up.length>0&&parseFloat(down,10)!=0&&parseFloat(up,10)!=0){
		var result;
		if(Suffix==="%"){
			result = ((up/down)*100).toFixed(2);
			divideR=result+" %";
		}else{
			result = (up/down).toFixed(2);
			divideR=result;
		}
	}
	$target.text(divideR);
}