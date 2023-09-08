<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/css/textareafont.css'/>" type="text/css" rel="stylesheet"/>

<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/projectreport.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script>
$(function(){
	$( document ).tooltip({
		  disabled: true
	});

	var repType="${PRUBean.repType}";
	if(repType==="Y"){
		$("input[name='financial']").each(function(){
			if($(this).val()==="${PRUBean.financial}"){
				$(this).prop("checked",true);
			}
		});
	}
	if("${PRUBean.isConversion}".length>0){
		$("input[name='isConversion'][value='${PRUBean.isConversion}']").prop("checked",true);
	}
	setRedioToDefalut("noNeed","${PRUBean.noNeed}")
	getPercentText();
 	var oTable=$("#changeReceive").dataTable({
		"bAutoWidth" : false, //自適應寬度
		"bLengthChange": false,
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
			<span style="color:#F30;"><strong>&nbsp;修改<c:if test="${PRUBean.repType eq 'Q'}">季</c:if><c:if test="${PRUBean.repType eq 'Y'}">年</c:if>報&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/console/project/content/projectmenu.jsp" flush="true">
				<jsp:param value="4" name="pos"/>
			</jsp:include>
		</div>
		<div>
			<%-- <c:url value="/console/project/projectreportedit.jsp" var="deleteUrl">
				<c:param name="edittype" value="delete" />
				<c:param name="repserno" value="${PRUBean.repSerno}" />
				<c:param name="investNo" value="${PRUName.investNo}" />
			</c:url>
			<input type="hidden" value="${deleteUrl}" id="myDeleteUrl"/> --%>
			<form  id="projRForm" action="<c:url value='/console/project/projectreportedit.jsp'/>" method="post">
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;填報基本資料&nbsp;]</span>
					<input type="button" id="mydelete" class="btn_checkout" value="刪除" style="float: right;"/>
				</legend>
					<span class="basetable">
						投資案號：<span>${PRUName.investNo}</span>
						<input type="hidden" name="investNo" value="${PRUName.investNo}"/>
						<input type="hidden" name="investor" value="${PRUName.IDNO}"/>
					</span>
					<span class="basetable">
						收文文號：<input type="text" maxlength="11" name="pNo" value="${PRUBean.keyinNo}"/><span id="reNoCount">(${fn:length(PRUBean.keyinNo)})</span>
					</span>
					<span class="basetable">
						填報時點：${PRUBean.year}年&nbsp;
						<c:if test="${PRUBean.repType eq 'Q'}">
						第${PRUBean.quarter}季
						</c:if>
						<input type="hidden" name="pYear" value="${PRUBean.year}"/>
						<input type="hidden" name="pSeason" value="${PRUBean.quarter}"/>
					</span>
					<br/>
					<c:if test="${PRUBean.repType eq 'Y'}">
						<span class="basetable">
						已繳交財報：<input id="financialY" name="financial" type="radio" value="Y"/><label for="financialY">是</label>&nbsp;
							   <input id="financialN" name="financial" type="radio" value="N"/><label for="financialN">否</label>&nbsp;
							   <input id="financialL" name="financial" type="radio" value="L"/><label for="financialL">當年度不需繳交</label>&nbsp;
						</span><br/>	
					</c:if>
					<span class="basetable">
						大陸投資事業名稱：${PRUName.cnName}
					</span>
					<span class="basetable">
						投資人名稱：${PRUName.investor}
					</span>
					<br/>
					<span class="basetable">
						修改人：
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
					<br/>
					<span class="basetable">
					本次免申報：
							<input type="radio" name="noNeed" id="noNeed1" value="1"/>
							<label for="noNeed1">免申報</label>
							<input type="radio" name="noNeed" id="noNeed0" value="0" checked="checked"/>
							<label for="noNeed0">應申報</label>
					</span><br/>
					<span class="basetable">
					免申報備註：<textarea name="noNeedNote" style="width: 65%;vertical-align: text-top;" cols="50" rows="5">${PRUBean.noNeedNote}</textarea>
					</span>
					<br/>
					<span class="basetable">
					轉換幣別：<input type="radio" name="isConversion" id="isConversionY" value="Y"/>
							<label for="isConversionY">是</label>
							<input type="radio" name="isConversion" id="isConversionN" value="N" checked="checked"/>
							<label for="isConversionN">否</label>
					</span><br/>
					<span class="basetable">
					轉換備註：<textarea name="note" style="width: 65%;vertical-align: text-top;" cols="50" rows="5">${PRUBean.note}</textarea>
					</span>
			</fieldset>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;修改文號&nbsp;]</span>
					<input type="button" class="selectThispage btn_class_opener" value="本頁全選"/>				
					<input type="button" class="unselectThispage btn_class_opener" value="本頁全刪"/>
				</legend>
				<div id="help">
					<table id="changeReceive" class="display" style="width: 95%;margin-left: 15px;"> 
						<thead>
							<tr>
								<th>選擇</th>
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
										<td>
											<input type="checkbox" name="receviceNoTemp" value="${reno[1]}" <c:if test="${reno[3] eq '1'}">checked="checked"</c:if> />
										</td>	
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
					<span style="color:#F30;">[&nbsp;修改執行情形&nbsp;]</span>
				</legend>
				<div style="margin:5px 10px;color:#222;">1.資金匯出、核備情形（金額：美元）</div>
				<table class="formProj">
					<tr>
						<th>本季匯出投資金額</th>
						<th>累計核准投資金額</th>
						<th>累計已核備投資金額</th>
						<th>累計已實行投資比例</th>								
					</tr>
					<tr  class="trRight">
						<td>
							<input type="text" name="outwardMoney" value="${PRUBean.outwardMoney}" style="width: 95%;" class="numberFmt noCheckPY"/>
						</td>
						<td>
							<input type="text" name="approvalMoney" value="${PRUBean.approvalMoney}" style="width: 95%;" class="numberFmt pDown noCheckPY"/>
						</td>
						<td>
							<input type="text" name="approvedMoney" value="${PRUBean.approvedMoney}" style="width: 95%;" class="numberFmt pUp noCheckPY"/>
						</td>
						<td>
							<span class="pPercent"></span>
							<input type="hidden" name="isOnline" value="${PRUBean.isOnline}"/>
							<input type="hidden" name="repType" value="${PRUBean.repType}"/>
							<input type="hidden" name="repserno" value="${PRUBean.repSerno}"/>
							<input type="hidden" name="edittype" value="update"/>
						</td>
					</tr>
					<tr>
						<td  style="text-align: center;font-weight: bold;">原系統資料庫資料</td>
						<td style="text-align: right;"><span class="numberFmt" style='margin: 5px;<c:if test="${PRUList.k1 - PRUList.d1!=0}">color:red;</c:if>' >${PRUList.d1}</span></td>
						<td style="text-align: right;"><span class="numberFmt" style='margin: 5px;<c:if test="${PRUList.k2 - PRUList.d2!=0}">color:red;</c:if>' >${PRUList.d2}</span></td>
						<td></td>
					</tr>
					<tr>
						<th>本季匯出投資金額_備註</th>
						<th>累計核准投資金額_備註</th>
						<th>累計已核備投資金額_備註</th>
						<th></th>								
					</tr>
					<tr>
						<td>
							<textarea rows="3" style="width: 95%;" name="outwardNote">${PRUBean.outwardNote}</textarea>
						</td>
						<td>
							<textarea rows="3" style="width: 95%;" name="approvalNote">${PRUBean.approvalNote}</textarea>
						</td>
						<td>
							<textarea rows="3" style="width: 95%;" name="approvedNote">${PRUBean.approvedNote}</textarea>
						</td>
						<td></td>
					</tr>
				</table>
				<c:if test="${PRUBean.repType eq 'Q'}">
					<span style="color: red;margin: 5px;font-size: 14px;">註：累計核准投資金額與累計已核備投資金額此兩項，於102年第2季前的數值皆由系統匯入。</span>
				</c:if>
				<c:if test="${PRUBean.repType eq 'Y'}">
					<div style="margin:5px 10px;color:#222;">2.本年度對國內相對投資情形（新台幣千元）</div>
					<table class="formProj" style="width: 500px;">
						<tr  class="trRight">
							<th>本年度在國內新增投資金額</th>
							<td><input type="text" name="investMoney" value="${PRUBean.investMoney}" style="width: 95%;" class="numberFmt lastNumberFmt noCheckPY"/></td>
						</tr>
					</table>
				</c:if>
			</fieldset>
			<div style="text-align: center;">
				<input type="button" id="myupdate" class="btn_class_Approval" value="確認修改"/>
			</div>
			</form>
		</div>
	</fieldset>
</div>