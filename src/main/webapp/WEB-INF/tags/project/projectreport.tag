<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/projectreport.js'/>"></script>
<script>
$(function(){
 	var oTable=$("#changeReceive").dataTable({
		"bAutoWidth" : false, //自適應寬度
		"bLengthChange": false,
		"fnDrawCallback": function ( oSettings ) {
			/* Need to redo the counters if filtered or sorted */
			if ( oSettings.bSorted || oSettings.bFiltered ){
				for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
					$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
				} 
			}
		},
		"aoColumnDefs": [
		    { "bSortable": false,'sClass':'center', "aTargets": [ 0 ]},
		    { "bSearchable":false,"bVisible":false, "aTargets": [ 4 ]}
		],
		"aaSorting": [[ 4, 'desc' ]],
		//"bFilter": false,
		//多語言配置
		"oLanguage" : {
			"sProcessing" : "正在載入中......",
			"sLengthMenu" : "每頁顯示 _MENU_ 筆資料",
			"sZeroRecords" : "對不起，查詢不到相關資料！",
			"sEmptyTable" : "本分類目前尚無資料！",
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
	});
}); 
</script>
<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>${PRUBean.year}年&nbsp;<c:if test="${PRUBean.repType eq 'Q'}">第${PRUBean.quarter}季季</c:if><c:if test="${PRUBean.repType eq 'Y'}">年</c:if>報&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<form  id="projRForm" action="<c:url value='/console/project/projectreportedit.jsp'/>" method="post">
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;基本資料&nbsp;]</span>
					 <span style="float: right"><input type="button" class="btn_class_opener" onclick="javascript:window.history.go(-1)" value="回上一頁" /></span>
				</legend>
					<span class="basetable">
						投資案號：<span>${PRUName.investNo}</span>
					</span>
					<span class="basetable">
						大陸投資事業名稱：${PRUName.cnName}
					</span>
					<span class="basetable">
						收文文號：${PRUBean.keyinNo}
					</span>
					<br/>
					<span class="basetable">
						投資人統編：${PRUName.IDNO}
					</span>
					<span class="basetable">
						投資人名稱：${PRUName.investor}
					</span><br/>
					<c:if test="${PRUBean.repType eq 'Y'}">
						<span class="basetable">
							已繳交財報：<c:choose>
								<c:when test="${PRUBean.financial eq 'Y'}">是</c:when>
								<c:when test="${PRUBean.financial eq 'N'}">否</c:when>
								<c:when test="${PRUBean.financial eq 'L'}">當年度不需繳交</c:when>
							</c:choose>
						</span><br/>	
					</c:if>
					<span class="basetable">
						修改人：<%-- ${PRUName.editor} --%>
						<c:choose>
							<c:when test="${PRUBean.isOnline eq 'N' }">${PRUName.editor}</c:when>
							<c:when test="${PRUBean.isOnline eq 'Y' && PRUName.editor != 'admin'}">${PRUName.editor}(線上申報)</c:when>
							<c:when test="${PRUBean.isOnline eq 'Y'}">線上申報</c:when>
							<c:otherwise>${PRUName.editor}</c:otherwise>
						</c:choose>
					</span>
					<span class="basetable">
						修改日：${fn:substring(PRUBean.updatetime,0,16)}
					</span>
					<span class="basetable">
						填報時點：${PRUBean.year}年&nbsp;
						<c:if test="${PRUBean.repType eq 'Q'}">
						第${PRUBean.quarter}季
						</c:if>
					</span>
			</fieldset>
			<c:if test="${PRUBean.noNeed eq '1'}">
				<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
					<legend style="width:100%;border-top:1px solid #E6E6E6">
						<span style="color:#F30;">[&nbsp;本次免申報&nbsp;]</span>
					</legend>
					<span class="basetable">
						免申報備註：
					</span>
					<div style="padding-left: 15px;">
						<pre>${PRUBean.noNeedNote}</pre>						
					</div>
				</fieldset>
			</c:if>
			<c:if test="${PRUBean.isConversion eq 'Y'}">
				<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
					<legend style="width:100%;border-top:1px solid #E6E6E6">
						<span style="color:#F30;">[&nbsp;幣別轉換：是&nbsp;]</span>
					</legend>
					<c:if test="${not empty PRUBean.note}">
						<span class="basetable">
							轉換幣別備註：
						</span>
						<div style="padding-left: 15px;">
							<pre>${PRUBean.note}</pre>						
						</div>
					</c:if>
				</fieldset>
			</c:if>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;涉及文號&nbsp;]</span>
				</legend>
				<div id="help">
					<table id="changeReceive" class="display" style="width: 95%;margin-left: 15px;"> 
						<thead>
							<tr>
								<th>No</th>
								<th>核准日期</th>
								<th>文號</th>
								<th>案由</th>
								<th>flag</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty PRUReceive}">
								<c:forEach var="reno" items="${PRUReceive}">
									<tr>
										<td></td>	
										<td>${reno[0]}</td>	
										<td>${reno[1]}</td>	
										<td>${reno[2]}</td>	
										<td>${reno[3]}</td>	
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</fieldset>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;執行情形&nbsp;]</span>
				</legend>
				<div style="margin:5px 10px;color:#222;">1.資金匯出、核備情形（金額：美元）</div>
				<table class="formProj">
					<tr>
						<th style="width: 25%;">本季匯出投資金額</th>
						<th style="width: 25%;">累計核准投資金額</th>
						<th style="width: 25%;">累計已核備投資金額</th>
						<th style="width: 25%;">累計已實行投資比例</th>								
					</tr>
					<tr  class="trRight">
						<td>
							<c:if test="${not empty PRUBean.outwardMoney}">
								<span class="numberFmt"><fmt:parseNumber value="${PRUBean.outwardMoney}"/></span>
							</c:if>
						</td>
						<td>
							<c:if test="${not empty PRUBean.approvalMoney}">
								<span class="numberFmt pUp"><fmt:parseNumber value="${PRUBean.approvalMoney}"/></span>
							</c:if>
						</td>
						<td>
							<c:if test="${not empty PRUBean.approvedMoney}">
								<span class="numberFmt pDown"><fmt:parseNumber value="${PRUBean.approvedMoney}"/></span>
							</c:if>
						</td>
						<td>
							<c:if test="${not empty PRUBean.completeMoney}">
								<span class="numberFmt"><fmt:parseNumber value="${PRUBean.completeMoney * 100}"/></span>&nbsp;%
							</c:if>
						</td>
					</tr>
					<tr>
						<th>本季匯出投資金額_備註</th>
						<th>累計核准投資金額_備註</th>
						<th>累計已核備投資金額_備註</th>
						<th></th>								
					</tr>
					<tr>
						<td>
							<pre>${PRUBean.outwardNote}</pre>
						</td>
						<td>
							<pre>${PRUBean.approvalNote}</pre>
						</td>
						<td>
							<pre>${PRUBean.approvedNote}</pre>
						</td>
						<td></td>
					</tr>
				</table>
				<c:if test="${PRUBean.repType eq 'Y'}">
					<div style="margin:5px 10px;color:#222;">2.本年度對國內相對投資情形（新台幣千元）</div>
					<table class="formProj" style="width: 500px;">
						<tr  class="trRight">
							<th style="width: 50%;">本年度在國內新增投資金額</th>
							<td>
								<c:if test="${not empty PRUBean.investMoney}">
									<span class="numberFmt"><fmt:parseNumber value="${PRUBean.investMoney}"/></span>
								</c:if>
							</td>
						</tr>
					</table>
				</c:if>
			</fieldset>
			</form>
		</div>
	</fieldset>
</div>