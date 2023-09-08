<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<style>
	#resultTable td.details{
		background-color:#f3f7fb;
		border:2px solid #A19B9E
	}
	.prtable {
		border-collapse:collapse;
		padding-left:10px;
		width:100%;
		table-layout: fixed;
	}
	.prtable tr td{
		border: 1px solid #000000;	
	}
	.prtable tr th{
		border: 1px solid #000000;	
	}	
	.ui-button-text {
    	font-size: 14px;
	}
</style>
<script>
$(function() {
	$(".mytip").tooltip();
	var y=$.extend({
		"aaSorting": [[ 2, "desc" ]],
		           "aLengthMenu": [[10, 20, 50,100], [10, 20, 50,100]],
				   "iDisplayLength": 10,				   
				   "aoColumns":[
				    			{"sSortDataType":"dom-checkbox","sWidth":"8%"},
				    			{"sWidth":"7%"},
				    			{"sSortDataType":"chinese","sWidth":"27%"},
				    			{"sSortDataType":"chinese","sWidth":"11%"},
				    			{"sSortDataType":"chinese","sWidth":"27%"},
				    			{"sSortDataType":"chinese","sWidth":"12%"},
				    			{"sWidth":"8%"},{},{},{},{},{},{},{}			    			
				    		]
		    		 , "oSearch": {"sSearch": "${searchStr}"}
	},sdInitDataTableSetting(),sdSortChinese(),sdSortCheckbox());
	
	$("input[name='repSerno']").bind('click', function() {
		$(this).parent(".fisrtTd").trigger('click');
    });
	$(".fisrtTd").bind('click', function() {
		$(this).find("input[name='repSerno']").trigger('click');
    });
	var oTable=$("#resultTable").dataTable(y);
    fnHideColumns(oTable,[6,7,9,10,11,13]);
    $('#resultTable tbody').on("click","td:not(.fisrtTd)",function () {
        var nTr = $(this).parents('tr')[0];
        if ( oTable.fnIsOpen(nTr) ){
        	oTable.fnClose( nTr );
        }else{
            oTable.fnOpen( nTr, fnFormatDetails(oTable, nTr), 'details' );
        }
    } );
	$("#aopen").on("click",function(){
		 $(oTable.fnGetNodes()).each(function(idx, tr){
			 if (!oTable.fnIsOpen(tr) ){
				 oTable.fnOpen( tr, fnFormatDetails(oTable, tr), 'details' );
		     }
		  });
	});
	$("#aclose").on("click",function(){
		 $(oTable.fnGetNodes()).each(function(idx, tr){
			 if (oTable.fnIsOpen(tr) ){
				 oTable.fnClose( tr );
		     }
		  });
	});	
	$("#aDel").on("click",function(){
		 if($("input[name='repSerno']:checked",oTable.fnGetNodes()).length>0){
			$("<div title='刪除提示'>相同資料不會重覆進行資料匯入，請問是否確定要刪除?</div>" ).dialog({
		      resizable: false,
		      modal: true,
		      buttons: {
		        "確定": function() {
		        	var serno="";
					 $("input[name='repSerno']:checked",oTable.fnGetNodes()).each(function(idx, tr){
						 if(serno.length!=0){
							 serno+=",";
						 }
						 serno+=$(this).val();
					  });
					  postUrlByForm('/console/project/editprcomfirm.jsp',{'repSerno':serno,'type':'0'});
					  $( this ).dialog( "close" );
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		 }else{
			 $("<div title='請選擇'>至少須選擇一項以上，才能進行動作，謝謝！</div>" ).dialog();
		 }
	});	
	$("#aOverwrite").on("click",function(){
		 if($("input[name='repSerno']:checked",oTable.fnGetNodes()).length>0){
			$("<div title='覆蓋資料'>覆蓋後，將以匯入資料取代原資料，是否繼續覆蓋動作？</div>" ).dialog({
		      resizable: false,
		      modal: true,
		      buttons: {
		        "確定": function() {
		        	var serno="";
					 $("input[name='repSerno']:checked",oTable.fnGetNodes()).each(function(idx, tr){
						 if(serno.length!=0){
							 serno+=",";
						 }
						 serno+=$(this).val();
					  });
					  postUrlByForm('/console/project/editprcomfirm.jsp',{'repSerno':serno,'type':'1'});
			          $( this ).dialog( "close" );
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		 }else{
			 $("<div title='請選擇'>至少須選擇一項以上，才能進行動作，謝謝！</div>" ).dialog();
		 }
	});	
});
function showInfo ( str )
{
	$("<div>"+str+"</div>" ).dialog();
}
function fnFormatDetails ( oTable, nTr )
{
    var aData = oTable.fnGetData( nTr );
    var num= aData[7].split(/\/(?![s])/);
    var nNodes = oTable.fnGetNodes( );
    var investor=aData[2];
    var investment=aData[4];
    if($(nTr, nNodes).find("td:eq(4) a").length>0){
    	 investment=$(nTr, nNodes).find("td:eq(4) a").prop("title");
    }
    if($(nTr, nNodes).find("td:eq(2) a").length>0){
    	investor=$(nTr, nNodes).find("td:eq(2) a").prop("title");
    }
    var sOut = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:10px;width:100%;">';
    sOut += '<tr><td class="trRight">投資人:</td><td>'+investor+'('+aData[1]+')</td>';
    sOut += '<td class="trRight">填報時點:</td><td>'+aData[5]+'</td></tr>';
    sOut += '<tr><td class="trRight">投資案號:</td><td>'+aData[3]+'</td>';
    sOut += '<td class="trRight">發文日期:</td><td>'+aData[13]+'</td></tr>';
    sOut += '<tr><td class="trRight">大陸事業名稱:</td><td>'+investment+'</td>';
    sOut += '<td class="trRight">更新日期:</td><td>'+aData[6]+'</td></tr>';
    sOut += '</table>';
    sOut += '<table class="prtable" cellpadding="5" cellspacing="0" border="0"><tr><th>本季匯出投資金額</th><th>累積核准投資金額</th><th>累積已核備投資金額</th><th>本年度在國內新增投資金額</th></tr>';
    sOut += '<tr><td class="trRight">'+num[0]+'</td><td class="trRight">'+num[1]+'</td><td class="trRight">'+num[2]+'</td><td class="trRight">';
    if(num.length>3){
    	sOut += num[3]+'</td></tr>';
    }else{
    	sOut +='</td></tr>';
    }
    if((aData[9]+aData[10]+aData[11]).length>0){
    	sOut += '<tr><td>'+aData[9]+'</td><td>'+aData[10]+'</td><td>'+aData[11]+'</td><td></td></tr>';
    }
    
    sOut += '</table>';
    return sOut;
}
</script>

<div>
	<fieldset style="-moz-border-radius: 6px; -moz-border-top-colors: #fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors: #fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors: #fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors: #fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width: 100%; border-top: 1px solid #E6E6E6">
			<span style="color: #F30; margin-right: 20px;">[&nbsp;待確認列表&nbsp;(紅字表示原系統資料庫資料)&nbsp;]</span>&nbsp;&nbsp;
			<c:if test="${fn:contains(memberUrls,'E0101')}">
				<span style="float: right;"> <a href="#" id="aopen"
					class="btn_class_opener">全部展開</a> <a href="#" id="aclose"
					class="btn_class_opener">全部收合</a> <a href="#" id="aDel"
					class="btn_class_opener">刪除</a> <a href="#" id="aOverwrite"
					class="btn_class_opener">覆蓋</a>
				</span>
			</c:if>
		</legend>
		
		<div>
			<table id="resultTable" class="display" style="width: 98%;">
				<thead>
					<tr>
						<th>選擇</th>
						<th>統編</th>
						<th>投資人</th>
						<th>投資<br>案號</th>
						<th>大陸事業名稱</th>
						<th>填報<br>時點</th>
						<th>更新日</th>
						<th>本季匯出投資金額/累積核准投資金額/累積已核備投資金額</th>
						<th>文號</th>
						<th>本季匯出投資金額備註</th>
						<th>累積核准投資金額備註</th>
						<th>累積已核備投資金額備註</th>
						<th>收文<br>日期</th>
						<th>發文日期</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${not empty prclist}">
						<c:forEach var="proj" items="${prclist}">
							<tr>
								<td class="fisrtTd"><input type="checkbox" name="repSerno" value="${proj.repSerno}"></td>
								<td>${proj.idno}</td>
								<td>${ibfn:shortenTooltip(proj.investor,12,"mytips")}</td>
								<td>${proj.investNo}</td>
								<td>${ibfn:shortenTooltip(proj.investment,12,"mytips")}</td>
								<td>${proj.year}Q${proj.quarter}</td>
								<td>${ibfn:toTWDateStr(proj.updatetime)}</td>
<%-- 								<td>${ibfn:formatString(proj.outwardMoney)}/${ibfn:formatString(proj.approvalMoney)}/${ibfn:formatString(proj.approvedMoney)}/${ibfn:formatString(proj.investMoney)}</td> --%>
								<td>${proj.outwardMoney}/${proj.approvalMoney}/${proj.approvedMoney}/${ibfn:formatString(proj.investMoney)}</td>
								<td>${proj.keyinNo}</td>
								<td>${proj.outwardNote}</td>
								<td>${proj.approvalNote}</td>
								<td>${proj.approvedNote}</td>
								<td>${proj.receiveDate}</td>
								<td>${proj.sendDate}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	</fieldset>
</div>