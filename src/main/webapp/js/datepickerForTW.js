/*載入jQuery UI i18n 的zh-TW*/
jQuery(function($){
	$.datepicker.regional['zh-TW'] = {
		closeText: '關閉',
		prevText: '&#x3C;上月',
		nextText: '下月&#x3E;',
		currentText: '今天',
		monthNames: ['一月','二月','三月','四月','五月','六月',
		'七月','八月','九月','十月','十一月','十二月'],
		monthNamesShort: ['一月','二月','三月','四月','五月','六月',
		'七月','八月','九月','十月','十一月','十二月'],
		dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
		dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
		dayNamesMin: ['日','一','二','三','四','五','六'],
		weekHeader: '周',
		dateFormat: 'yy/mm/dd',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: true
		/*yearSuffix: '年'*/};
	$.datepicker.setDefaults($.datepicker.regional['zh-TW']);
});

/*設定jQuery UI datepicker*/
$(function() {
	 var old_generateMonthYearHeader = $.datepicker._generateMonthYearHeader;
     var old_get = $.datepicker._get;
     var old_CloseFn = $.datepicker._updateDatepicker;
     $.extend($.datepicker, {
         _generateMonthYearHeader:function (a,b,c,d,e,f,g,h) {
             var htmlYearMonth = old_generateMonthYearHeader.apply(this, [a, b, c, d, e, f, g, h]);
             if ($(htmlYearMonth).find(".ui-datepicker-year").length > 0) {
                 htmlYearMonth = $(htmlYearMonth).find(".ui-datepicker-year").find("option").each(function (i, e) {
                	 if(Number(e.value) - 1911 > 0){
                		 $(e).text(e.value).text(Number(e.value) - 1911);
//                		 $(e).text(e.value).text(Number(e.innerText) - 1911);
                	}
//                	 if(Number(e.value) - 1911 > 0){$(e).text(Number(e.innerText) - 1911);}
               }).end().end().get(0).outerHTML;
             }
             return htmlYearMonth;
         },

        _get:function (a, b) {
             a.selectedYear = a.selectedYear - 1911 < 0 ? a.selectedYear + 1911 : a.selectedYear;
             a.drawYear = a.drawYear - 1911 < 0 ? a.drawYear + 1911 : a.drawYear;
             a.curreatYear = a.curreatYear - 1911 < 0 ? a.curreatYear + 1911 : a.curreatYear;
             return old_get.apply(this, [a, b]);
         },

        _updateDatepicker:function (inst) {
             old_CloseFn.call(this, inst);
             $(this).datepicker("widget").find(".ui-datepicker-buttonpane").children(":last")
                    .click(function (e) {
                         inst.input.val("");
                     });
         },

        _setDateDatepicker: function (a, b) {
             if (a = this._getInst(a)) { this._setDate(a, b); this._updateDatepicker(a); this._updateAlternate(a);}
         },

        _widgetDatepicker: function () {
//        	console.log( "_widgetDatepicker");
             return this.dpDiv;
         },
         parseDate: function (format, value, settings) {  
        	 var v = new String(value);
        	 var vAry = v.split("/");
        	 var Y,M,D;  
        	 if(v.length>0){ 
        		 Y = parseInt(vAry[0],10)+1911;  
                 M = parseInt(vAry[1],10)-1;  
                 D = parseInt(vAry[2],10);
                 if(isNaN(parseInt(new Date(Y,M,D).getFullYear(),10))){
                	 return (new Date());
                 }else{
                	 return (new Date(Y,M,D));  
                 }
        	 }  
        	 return (new Date());  
         } 
  });

		var dates = $( "#from, #to" ).datepicker({
			 yearSuffix: "", //將年改為空白
		        changeYear: true, //手動修改年
		        changeMonth: true, //手動修改月
		        firstDay: 1, //0為星期天
		       /* showOtherMonths: true, //在本月中顯示其他月份
		        selectOtherMonths: true, //可以在本月中選擇其他月份
		        showButtonPanel: true, //顯示bottom bar*/
		         dateFormat: "yy/mm/dd",
		         maxDate: "+0",
		         onSelect: function (dateText, inst) {
		        	var dateFormate = inst.settings.dateFormat == null ? "yy/mm/dd" : inst.settings.dateFormat; //取出格式文字
		            var reM = /m+/g;
		            var reD = /d+/g;
		            var year=inst.selectedYear - 1911 < 0 ? inst.selectedYear : inst.selectedYear - 1911;
		            if(String(year).length<3){
		            	year="0"+year;
		            }
		            var month=parseInt(inst.selectedMonth,10) >8  ? inst.selectedMonth + 1 : "0" + String(inst.selectedMonth + 1);
		            var date=String(inst.selectedDay).length != 1 ? inst.selectedDay : "0" + String(inst.selectedDay);
		            
		             inst.input.val(year+"/"+month+"/"+date);
		         }
		});
		var singledate = $( "#singledate").datepicker({
			dateFormat:"yy/mm/dd",
			changeYear: true, //手動修改年
	        changeMonth: true, //手動修改月			
			/*showButtonPanel:true,*/ 
	        maxDate: "+0",
	        onSelect: function (dateText, inst) {
	        	var dateFormate = inst.settings.dateFormat == null ? "yy/mm/dd" : inst.settings.dateFormat; //取出格式文字
	            var reM = /m+/g;
	            var reD = /d+/g;
	            var year=inst.selectedYear - 1911 < 0 ? inst.selectedYear : inst.selectedYear - 1911;
	            if(String(year).length<3){
	            	year="0"+year;
	            }
	            var month=parseInt(inst.selectedMonth,10) >8  ? inst.selectedMonth + 1 : "0" + String(inst.selectedMonth + 1);
	            var date=String(inst.selectedDay).length != 1 ? inst.selectedDay : "0" + String(inst.selectedDay);
	            
	             inst.input.val(year+"/"+month+"/"+date);
	         }
		});
		var singledate = $( ".singledate").datepicker({
			dateFormat:"yy/mm/dd",
			changeYear: true, //手動修改年
			changeMonth: true, //手動修改月			
			/*showButtonPanel:true,*/ 
			maxDate: "+0",
			onSelect: function (dateText, inst) {
				var dateFormate = inst.settings.dateFormat == null ? "yy/mm/dd" : inst.settings.dateFormat; //取出格式文字
				var reM = /m+/g;
				var reD = /d+/g;
				var year=inst.selectedYear - 1911 < 0 ? inst.selectedYear : inst.selectedYear - 1911;
				if(String(year).length<3){
					year="0"+year;
				}
				var month=parseInt(inst.selectedMonth,10) >8  ? inst.selectedMonth + 1 : "0" + String(inst.selectedMonth + 1);
				var date=String(inst.selectedDay).length != 1 ? inst.selectedDay : "0" + String(inst.selectedDay);
				
				inst.input.val(year+"/"+month+"/"+date);
			}
		});
		var noLimitdate = $( "#fromL, #toL").datepicker({
			 yearSuffix: "", //將年改為空白
		        changeYear: true, //手動修改年
		        changeMonth: true, //手動修改月
		        firstDay: 1, //0為星期天
		         dateFormat: "yy/mm/dd",
		         onSelect: function (dateText, inst) {
		        	var dateFormate = inst.settings.dateFormat == null ? "yy/mm/dd" : inst.settings.dateFormat; //取出格式文字
		            var reM = /m+/g;
		            var reD = /d+/g;
		            var year=inst.selectedYear - 1911 < 0 ? inst.selectedYear : inst.selectedYear - 1911;
		            if(String(year).length<3){
		            	year="0"+year;
		            }
		            var month=parseInt(inst.selectedMonth,10) >8  ? inst.selectedMonth + 1 : "0" + String(inst.selectedMonth + 1);
		            var date=String(inst.selectedDay).length != 1 ? inst.selectedDay : "0" + String(inst.selectedDay);
		            
		             inst.input.val(year+"/"+month+"/"+date);
		         }
		});
	});
function fmtDateTo2 (txt){
	var ary = txt.split("/");
	var year = parseInt(ary[0], 10);
	year = year.length>3?year-1911:year;
	return year+"/"+ary[1]+"/"+ary[2];
}