<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/css/textareafont.css'/>" type="text/css" rel="stylesheet"/>
<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<style>
	.ui-datepicker{
		Font-size:13px !important;
		padding: 0.2em 0.2em 0;
	}
</style>
<script>
$(function() {
	var firstTab="${projInfo.done}";
	if(firstTab.length==0){
		firstTab=0;
	}
	 var updateOk="${projInfo.updateOk}";
	    if(updateOk.length!=0){
	    	alert(updateOk);
	    }
    $( "#tabs" ).tabs({
    	active:firstTab
    });
   	var y = "${pjbean.state}";
    $(".myState option").each(function(){
    	var x = $(this).val();
    	if(x===y){
    		$(this).prop("selected",true);
    	}else{
    		$(this).prop("selected",false);
    	}
    });
    $("#myUpdate").click(function(){
    	if($("#singledate").val().replace(/\//g, "").length!=7){
	    	alert("日期格式錯誤，請將格式改為如0##/0#/0#，或用日曆選取。");
    	}else{
	    	$("#Updatedetail").submit();
    	}
    });
    var dateTest=new Date();
    $.getJSON('<c:url value="/getprpivot.jsp"/>',{"serno":"${pjbean.serno}","test":dateTest.getTime()},function(data){
/*     $.getJSON('<c:url value="/console/project/getprpivot.jsp"/>',{"serno":"${pjbean.serno}","test":dateTest.getTime()},function(data){ */
    	var pjpivot="";
    	var urlTemp1="";
    	var urlTemp2="";
    	var urlTemp3="</a>";
    	var urlTemp4="edit";
		if("${fn:contains(memberUrls,'E0101')}"==="true"){
			urlTemp1="<a href='<c:url value='/console/project/updatepr.jsp?repserno=";
			urlTemp2="'/>' class='btn_checkout updateReport'>";
		}else{
			urlTemp1="<a href='<c:url value='/approval/seeprojct.jsp?repserno=";
			urlTemp2="&type=1'/>' class='btn_checkout updateReport'>";
			urlTemp4="檢視"
		}
		var keys =$.map(data[0].noNeed,function( value, key ){
			return key;	
		});
		var values =$.map(data[0].noNeed,function( value, key ){
			return value;	
		});
    	for(var i=0;i<data[1].length;i++){
    		pjpivot+="<tr style='text-align:center;'>";
    		for(var j=0;j<data[1][i].length;j++){
    			pjpivot+="<td>";
    			var tmpSerno=data[1][i][j];
    			if(tmpSerno!=""&&!isNaN(tmpSerno)&&j!=0){
    				pjpivot+=urlTemp1+tmpSerno+urlTemp2;
    				var pos=$.inArray(tmpSerno,keys);
    				if(pos==-1){
    					pjpivot+=urlTemp4+urlTemp3;
    				}else{
    					if(values[pos]==="0"){
    						pjpivot+=urlTemp4+urlTemp3;
    					}else{
    						pjpivot+="本次免申報"+urlTemp3;
    					}
    				}
    			}else{
    				pjpivot+=tmpSerno;
    			}
    			pjpivot+="</td>";
    		}
    		pjpivot+="</tr>";
    	}
    	$(".SetPR").html(pjpivot);
    	var option =getOption();
    	$(".PivotTable").dataTable(option);
    });
    $(".receiveTable").hide();
    $.getJSON('<c:url value="/console/project/getprojectreceive.jsp"/>', {
		"investNo" : "${pjbean.investNo}",
		"IDNO" : "${pjbean.IDNO}"
	}).done(function(data) {
		   	var recevice="";
		   	for(var i=0;i<data.length;i++){
				recevice+="<tr><td>"+data[i].respDate+"</td><td>"+data[i].receiveNo+"</td><td>"+data[i].appName+"</td></tr>";
		   	}
		   	$(".myWait").hide();
		   	$(".receiveTable").show();
			$(".SetReceive").html(recevice);
			var option =getOption();
			$(".receiveTable").dataTable(option);
	});
  });
  function getOption(){
	  var option={
				"bAutoWidth" : false, //自適應寬度
				"bLengthChange": false,
				"aaSorting": [[ 0, 'desc' ]],
				"aoColumnDefs": [
				    { 'sClass':'center', "aTargets": [ 0 ] }
				 ],
				//"bFilter": false,
				//多語言配置
				"oLanguage" : {
					"sProcessing" : "正在載入中......",
					"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
					"sZeroRecords" : "對不起，查詢不到相關資料！",
					"sEmptyTable" : "目前尚無資料！",
					"sInfo" : "目前顯示 _START_ 到 _END_ 筆，共有_TOTAL_ 筆資料",
					"sInfoEmpty": "目前顯示 0  到 0 筆，共有 0 筆資料",
					"sInfoFiltered" : "<br/>原始資料為 _MAX_ 筆資料",
					"sSearch" : "搜尋",
					"oPaginate" : {
						"sFirst" : "最前頁",
						"sPrevious" : "上一頁",
						"sNext" : "下一頁",
						"sLast" : "最末頁"
					}
				}
		};
	  return option;
  }
</script>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;專案編輯&nbsp;]</span>&nbsp;&nbsp;
					<span style="float: right;margin-right: 5px;">
						<a href="<c:url value='/console/project/showproject.jsp'/>" class="btn_class_opener">返回列表</a>
					</span>
				</legend>
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
					<strong style="color:#222;margin-left: 15px;margin-bottom: 5px;">專案基本資料</strong><br/>
					<table style="width: 100%;">
						<tr>
							<td style="width: 25%;text-align:right;">大陸投資事業名稱(投資案號)：</td>
							<td>${projInfo.cnName}（${pjbean.investNo}）</td>
						</tr>
						<tr>
							<td style="width: 25%;text-align: right;">投資人名稱(統一編號)：</td>
							<td>${projInfo.investor}（${pjbean.IDNO}）</td>
						</tr>
						<tr>
							<td style="width: 25%;text-align: right;">前次修改人(日期)：</td>
							<td>${projInfo.lastEditor}（${projInfo.lastUpdate}）</td>
						</tr>
					</table>
				</div>
				<div id="tabs" style="font-size: 14px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">專案編輯項目</a></li>
					    <li><a href="#tabs-2">專案文號列表</a></li>
					    <li><a href="#tabs-3">企業申報狀態</a></li>
					  </ul>
					  <div id="tabs-1">
					  <c:if test="${not empty pjbean.porjDate}">
						<c:set var="porjDate">${fn:substring(pjbean.porjDate,0,3)}/${fn:substring(pjbean.porjDate,3,5)}/${fn:substring(pjbean.porjDate,5,7)}</c:set>
					  </c:if>
					  <c:choose>
					  <c:when test="${fn:contains(memberUrls,'E0101')}">
					    <form id="Updatedetail" action='<c:url value="/console/project/updateprojectdetail.jsp"/>' method="post">
							<%-- <span style="color:#F30;font-size: 16px;text-align: center;">${projInfo.updateOk}</span> --%>
					        <div style="font-size: 16px;margin-top: 5px;">
					        	狀態：
					        	<select name="state" style="width: 130px;" class="myState">
					        		<option value="01">列管</option>
					        		<option value="02">解除季報</option>
					        		<option value="03">待確認</option>
					        		<option value="04">解除列管</option>
					        	</select>
					        	<span style="margin-left: 15px;">專案核准日期：</span>
					        	<input id="singledate" type="text" name="projDate" maxlength="9" value="${porjDate}">
					        	<c:if test="${pjbean.isSysDate eq '1'  && not empty pjbean.porjDate}"><span style="color: red;">(此日期為系統預設值，僅供參考。)</span></c:if>
							</div>
					        <div style="font-size: 16px;margin-top: 5px;">
					        	備註：
						    	<textarea name="notes" rows="15" style="width: 70%;vertical-align: text-top;">${pjbean.note}</textarea>
						    	<input type="hidden" name="serno" value="${pjbean.serno}">
						    	<div style="text-align: center; margin-top: 10px;font-size: 14px;">
									<input type="button" id="myUpdate" class="btn_class_Approval" value="修改"/>
								</div> 
							</div>
					    </form>
					  </c:when>
					  <c:otherwise>
					  	  <div style="font-size: 16px;margin-top: 5px;">
					        	狀態：${projState[pjbean.state]}
							</div>
					  	  <div style="font-size: 16px;margin-top: 5px;">
					        	專案核准日期：${porjDate}
					        	<c:if test="${pjbean.isSysDate eq '1' && not empty pjbean.porjDate}"><span style="color: red;">(此日期為系統預設值，僅供參考。)</span></c:if>
							</div>
					        <div style="font-size: 16px;margin-top: 5px;">
					        	<c:if test="${not empty pjbean.note}">
						        	<div style="float: left;">備註：</div>
						        	<div style="float: left;">${pjbean.note}</div>
						        	<div style="clear: left;"></div>
					        	</c:if>
							</div>
					  </c:otherwise>
					   </c:choose>
					  </div>
					  <div id="tabs-2">
						  <div style="font-size: 16px;margin-bottom: 20px;">
					  	  <img class="myWait" src="<c:url value='/images/loading.gif' />" title="please wait" style="width: 50px;"/>
						    <table class="receiveTable" class="display" style="width: 95%;"> 
								<thead>
									<tr>
										<th>核准日期</th>
										<th>文號</th>
										<th>案由</th>
									</tr>
								</thead>
								<tbody class="SetReceive" style="color: #fff;">
								</tbody>
							</table>
						</div>
					  </div>
					  <div id="tabs-3">
					    <div style="font-size: 16px;margin-bottom: 20px;">
						    <table class="PivotTable" class="display" style="width: 95%;padding: 3px;"> 
								<thead>
									<tr>
										<th>年度</th>
										<th>第一季</th>
										<th>第二季</th>
										<th>第三季</th>
										<th>年報</th>
										<th>財報</th>
									</tr>
								</thead>
								<tbody class="SetPR">
								</tbody>
							</table>
						</div>
						<div>財報：Y表示已繳交；N表示未繳交；L表示當年度不需繳交</div>
					  </div>
				</div>
			</fieldset>
		</div>