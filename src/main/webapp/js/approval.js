
/* Formating function for row details */
function fnFormatDetailsProj (oTable,ntr)
{
	var aData=oTable.fnGetData(ntr);
	var investNo = aData[3];
	var idNO = aData[2];
	var url=getRootPath();
	var imgurl=url+'/images/yes.png';
	 var dateTest=new Date();
	 var dtime=dateTest.getTime();
	 var sOut= '<table class="datatableProj" cellpadding="5" cellspacing="0" border="0">';
	     sOut+= '<tr><th colspan="7">專案審查資料填報狀況</th></tr>';
	$.getJSON(url+"/getprpivot.jsp",{"idNO":idNO,"investNo":investNo,"test":dtime},function(data){
		sOut+= '<tr><th>年度</th><th>第一季</th><th>第二季</th><th>第三季</th><th>年報</th><th>財報</th></tr>';
		var keys =$.map(data[0].noNeed,function( value, key ){
			return key;	
		});
		var values =$.map(data[0].noNeed,function( value, key ){
			return value;	
		});
			if(parseInt(data[1].length,10)===0){
				sOut+= '<tr><td colspan="7">尚無資料</td></tr>';
			}else{
				for(var i=0;i<data[1].length;i++){
					sOut+="<tr>";
					for(var j=0;j<data[1][i].length;j++){
						sOut+="<td>";
						var xItem=data[1][i][j];
						if(xItem!=""&&!isNaN(xItem)&&j!=0){
							var x = "postUrlByForm('/approval/seeprojct.jsp',{'repserno':'"+xItem+"'});";
							var pos=$.inArray(xItem,keys);
							if(pos==-1||values[pos]==="0"){
								sOut+="<input type='button' class='btn_class_opener updateReport' value='檢視' onclick=\""+x+"\" />";
							}else{
								sOut+="<input type='button' class='btn_class_opener updateReport' value='本次免申報' onclick=\""+x+"\" />";
							}
						}else{
							sOut+=xItem;
						}
						sOut+="</td>";
					}
					sOut+="</tr>";
				}
			}
			sOut+= '</table>';
			$("#tmpStr").html(sOut);
			$("#tmpStr").dialog({
				width: 650,
				modal:true,
				title:aData[4]
			});
    });
}
function fnFormatDetailsAgree (oTable,ntr)
{
	var aData=oTable.fnGetData(ntr);
	var investNo = aData[3];
	var idNO = aData[2];
	var url=getRootPath();
	var dateTest=new Date();
	var dtime=dateTest.getTime();
	var comurl = "postUrlByForm('/approval/showocommit.jsp',{'serno':'"+idNO+"'});";
    var sOut = "<div style='text-align: right;border:0px;'><a href='#' onclick=\""+comurl+"\" class='btn_class_opener' style='outline: none;'>檢視原承諾</a></div>";
    sOut += '<table class="datatableProj" cellpadding="5" cellspacing="0" border="0">';
    sOut += '<tr><th colspan="6">承諾事項填報狀況</th></tr>';
    sOut += '<tr><th>年度</th><th>上半年</th><th>下半年</th><th>年報</th><th>改善財務</th><th>資金回饋</th></tr>';
    $.getJSON(url+"/getcommitpivot.jsp",{"idNO":idNO,"test":dtime},function(data){
    	if(parseInt(data.length,10)===0){
    		sOut+= '<tr><td colspan="6">尚無資料</td></tr>';
    	}else{
    		for(var i=0;i<data.length;i++){
    			sOut+="<tr>";
    			for(var j=0;j<data[i].length;j++){
    				sOut+="<td>";
    				var xItem=data[i][j];
    				if(xItem!=""&&!isNaN(xItem)&&j!=0){
    					var x = "postUrlByForm('/approval/seecommit.jsp',{'repserno':'"+xItem+"','idno':'"+idNO+"','investor':'"+aData[1]+"'});";
    					sOut+="<input type='button' class='btn_class_opener updateReport' value='檢視' onclick=\""+x+"\" />";
    				}else{
    					sOut+=xItem;
    				}
    				sOut+="</td>";
    			}
    			sOut+="</tr>";
    		}
    	}
    	sOut+= '</table>';
    	$("#tmpStr").html(sOut);
    	$("#tmpStr").dialog({
    		width: 650,
    		modal:true,
    		title:aData[1]
    	});
    });
}
function season(oldurl,name,year,Q){
	var urlroot=getRootPath();
	var dateTest=new Date();
	var dtime=dateTest.getTime();
	var url=urlroot+'/approval/content/reportseason.jsp';
	$("#forseason").load(url,{'cnName':name,'year':year,'Q':Q,'date':dtime},function(){
		$(this).dialog({
			width: 750,
			modal:true,
			title:'專案審查'
		});
	});
}
function commitee(oldurl,name,year,Q){
	var urlroot=getRootPath();
	var dateTest=new Date();
	var dtime=dateTest.getTime();
	var url=urlroot+'/approval/content/reportcommit.jsp';
	$("#forseason").load(url,{'cnName':name,'year':year,'Q':Q,'date':dtime},function(){
		$(this).dialog({
			width: 950,
			modal:true,
			title:'承諾事項'
		});
		 $( "#tabs" ).tabs();
	});
}
$(function() {
	var oTable=$("#example").dataTable();
	$('.proj',oTable.fnGetNodes()).on('click',function () {
		var ntr = $(this).parents('tr')[0];
		fnFormatDetailsProj (oTable,ntr);
	});
	$('.agree',oTable.fnGetNodes()).on('click',function () {
		var ntr = $(this).parents('tr')[0];
		fnFormatDetailsAgree(oTable,ntr);
	});
	$('.info',oTable.fnGetNodes()).on('click',function () {
		var ntr = $(this).parents('tr')[0];
		var aData=oTable.fnGetData(ntr);
		postUrlByForm('/approval/seeapproval.jsp',{'idno': aData[2],'investNo': aData[3],'investor':aData[1],'cnName':aData[4]});
	});
	
});
