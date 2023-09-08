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
			/*edit 20140219*/
			e.preventDefault();
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
    };
})(jQuery);
/*$(function() {
	setNewAddFormatInput(".numberFmt");
});*/
function setNewAddFormatInput(className){
	$(className).formatInput();
	$(className).focus(function(event){
		var x = $(this).getCursorPosition()-1;
		var result=getRemoveCommaVal($(this).val());
		$(this).val(result);
		$(this).setCursorPosition(x);
	});
}
function setFormatInputDefault(className,scale){
	$(className).each(function(){
		var checkNumberFmt;
		if($(this).is("input")){
			checkNumberFmt=$(this).val();
			if(checkNumberFmt.length>0){
				var num=checkNumberFmt.replace(/,/g, "");
				num=parseFloat(num,10).toFixed(scale).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
				$(this).val(num);
			}
		}else if($(this).is("span")){
			checkNumberFmt=$(this).text();
			if(checkNumberFmt.length>0){
				var num=checkNumberFmt.replace(/,/g, "");
				num=parseFloat(num,10).toFixed(scale).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
				$(this).text(num);
			}
		}
	});
}
function setFormatInputBlur(className,scale){
	$(className).blur(function(){
		var checkNumberFmt;
		if($(this).is("input")){
			checkNumberFmt=$(this).val();
			if(checkNumberFmt.length>0){
				var num=checkNumberFmt.replace(/,/g, "");
				num=parseFloat(num,10).toFixed(scale).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
				$(this).val(num);
			}
		}else if($(this).is("span")){
			checkNumberFmt=$(this).text();
			if(checkNumberFmt.length>0){
				var num=checkNumberFmt.replace(/,/g, "");
				num=parseFloat(num,10).toFixed(scale).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
				$(this).text(num);
			}
		}
	});
}
function getRemoveCommaVal(number){
	return number.replace(/,/g, "");
}
function getInsertComma(number,scale){
	return parseFloat(number,10).toFixed(scale).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
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
function getValueText($Src){
	var result="";
	if($Src.is("input")){
		result=getRemoveCommaVal($Src.val());
	}else{
		result=getRemoveCommaVal($Src.text());
	}
	return result;
}
function getDivideText($up,$down,$target,Suffix,scale){
	var down = getValueText($down);
	var up = getValueText($up);
	var divideR ="";
	if(down.length>0&&up.length>0&&parseFloat(down,10)!=0&&parseFloat(up,10)!=0){
		var result;
		if(Suffix==="%"){
			result = ((up/down)*100).toFixed(scale);
			divideR=result+" %";
		}else{
			result = (up/down).toFixed(scale);
			divideR=result;
		}
	}
	$target.text(divideR);
}