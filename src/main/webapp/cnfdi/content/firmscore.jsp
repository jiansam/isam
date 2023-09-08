<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<%-- <script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
 --%>
<script type="text/javascript">
	$(function(){
		 $( "#accordion1" ).accordion({
		      collapsible: true, heightStyle: "content",active:false
		 });
		 
		 var y=$.extend({
				"aaSorting": [[ 1, "asc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [0,1] },
				                 {"sSortDataType":"numberFmt-text", "aTargets": [ 2,3,4]},
				                 { 'sClass':'trRight', "aTargets": [2,3,4] }],
				           "aLengthMenu": [[5, 10, 15,-1], [5, 10, 15,"All"]],
						   "iDisplayLength": 5
						   ,"fnDrawCallback": function ( oSettings ) {
								/* Need to redo the counters if filtered or sorted */
								if ( oSettings.bSorted || oSettings.bFiltered ){
									for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
										$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
									}
								}
							}
			},sdInitDataTableSetting(),sdSortChinese(),sdNumberFmt());
			var oTable=$("#mScore").dataTable(y);
			$("#mScore tr,#mScore td").css("cursor","none");
			
			setDialog('#note-dialog', '950', true, '計算說明', false);			
	});
	function setDialog(id, width, isModal, header, isAutoOpen){
		$(id).dialog({
			autoOpen : isAutoOpen,
			show : { effect : "blind", duration : 300},
			hide : { effect : "explode", duration : 300},
			width : width,
			modal : isModal, //鎖定頁面功能，預設關閉(false)
			draggable : true, //滑鼠按住標題列拖曳，預設開啟(true)
			//dialogClass : "dlg-no-close", //隱藏右上角的close button X
			closeOnEscape : true, //按"Esc"關閉，預設開啟(true)。
			resizable : false, //設定dialog box縮放功能(滑鼠按住視窗右下角拖曳)，預設關閉(false)。
			title : header, //標題列的標題，預設null
		});
	}
	function open_Dialog(id){
		$(id).dialog("open");
	}	
</script>
<c:if test="${empty manage}">尚無資料</c:if>
<c:if test="${not empty manage}">
	 <div align="right" style="background:#fff;padding:5px">
	 	<input type="button" class="btn_class_opener" style="font-size: 14px;" value="計算說明" onclick="open_Dialog('#note-dialog')"/></div>	 
	 <div class='tbtitle'>管理等級</div>
	 <div style="font-size: medium;width: 95%;margin-left: 15px;">
	 <table id='mScore' style="width: 95%;font-size: 16px;margin-left: 15px;" class="display">
		 <thead>
			<tr>
				<th>序號</th>
				<th>年度</th>
				<th style="text-align: center;">系統評分總分</th>
				<th style="text-align: center;">系統管理等級</th>
				<th style="text-align: center;">調整後管理等級</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${manage}" var="mitem">
			 	<tr>
			 		<td></td>
			 		<td>${mitem.value.year}</td>
			 		<td>${mitem.value.score}</td>
			 		<td>${mitem.value.oclassify}</td>
			 		<td>${mitem.value.nclassify}</td>
			 	</tr>
	 		</c:forEach>
		</tbody>
	 </table>
	 </div>
	 <div style="margin: 10px;height: 20px;"></div>
	 <div class='tbtitle'>系統評分項目</div>
	 <div id="accordion1" style="font-size: medium;width: 95%;margin-left: 15px;">
		<c:forEach var="ms" items="${mscore}">
		<h3>${ms.key}年</h3>
		<div>
			 <table style="width: 95%;font-size: 16px;margin-left: 15px;" class="formProj">
				<tr>
					<th>序號</th>
					<th>系統自動評分項目</th>
					<th>分數</th>
				</tr>
				<c:forEach var="score" items="${ms.value}" varStatus="i">
					<tr>
						<td style="text-align: center;">(${i.index+1})</td>
						<td>${optmap.manage[score.item]}</td>
						<td style="text-align: center;">${score.score}</td>
					</tr>
				</c:forEach>
			 </table>
		</div>
		</c:forEach>
	</div>
	 <div style="margin: 10px;"></div>
</c:if>

<div id="note-dialog">
	<div>(1) 分數計算：
		<table class="pure-table pure-table-bordered" style="margin: 10px;width:99%;">
			<thead>
				<tr>
					<th>序號</th>
					<th>系統自動評分項目</th>
					<th>條件/說明</th>
					<th>分數</th></tr>
			</thead>
			
			<tbody>
				<tr><td rowspan="3">1</td>
					<td rowspan="3">投資金額</td>
					<td>∑(投資金額) < 新台幣600萬元</td>
					<td>1</td></tr>
				<tr><td>新台幣600萬元 ≦ ∑(投資金額) < 新台幣3,000萬元</td>
					<td>2</td></tr>
				<tr><td>∑(投資金額) ≧ 新台幣3,000萬元</td>
					<td>3</td></tr>
					
				<tr><td>2</td>
					<td>持有國內事業股權</td>
					<td>股權比例 ≧ 33.33%</td>
					<td>1</td></tr>			
		
				<tr><td>3</td>
					<td>涉及特許及特殊項目</td>
					<td>國內事業營業項目，涉及特許或特殊項目</td>
					<td>2</td></tr>
					
				<tr><td rowspan="2">4</td>
					<td rowspan="2">持有轉投資事業</td>
					<td>持有國內轉投資事業(多筆只算一次)</td>
					<td>1</td></tr>
				<tr><td>．轉投資金額 ≧ 3千萬，或涉及特許/特殊項目<br>．各轉投資事業分別計算加總</td>
					<td>2</td></tr>
					
				<tr><td rowspan="2">5</td>
					<td>背景1-黨政軍案件</td>
					<td>多筆只算一次</td>
					<td>1</td></tr>
				<tr><td>背景2-央企政府出資案件</td>
					<td>多筆只算一次</td>
					<td>1</td></tr>							
		
				<tr><td rowspan="2">6</td>
					<td>稽核1-主管機關專案核准案件</td>
					<td>多筆只算一次</td>
					<td>1</td></tr>
				<tr><td>稽核2-公文附加附款案件</td>
					<td>多筆只算一次</td>
					<td>1</td></tr>
		
				<tr><td rowspan="2">7</td>
					<td>稽核3-協力機關要求標註關切案件</td>
					<td>多筆只算一次</td>
					<td>1</td></tr>
				<tr><td>稽核4-重大投資案件</td>
					<td>多筆只算一次</td>
					<td>1</td></tr>
		
				<tr><td rowspan="2">8</td>
					<td>稽核5-取得台灣地區不動產案件</td>
					<td>多筆只算一次</td>
					<td>1</td></tr>
				<tr><td>涉及稽核6為異常(含轉投資事業)</td>
					<td>若國內事業與轉投資事業皆異常時，也只算一次</td>
					<td>4</td></tr>
			</tbody>
		</table>
	</div>
	<div style="margin-bottom: 10px;">(2) 管理等級：系統評分總分1-4分相對等級列為1-4級，系統評分總分 ≧ 5一律列為第5級。</div>
	<div>(3) 當未訪視原因或國內事業之經營狀況為「解散或撤銷或廢止或歇業」時，從該年度開始後的「系統評分與系統管理等級」全部歸0，但該年度以前的「系統評分與系統管理等級」則維持不變。</div>
</div>