<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>


<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>

<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<script src="<c:url value='/js/addRow.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/hideOptionBySpan.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/js/fmtNumber.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<script>
$(function(){
	setYearSelectRange($("select[name='from']"),$("select[name='to']"));
		setSelectedToDefalut('from',"${subbean.startYear}");
		setSelectedToDefalut('to',"${subbean.endYear}");
		setSelectedToDefalut("state","${subbean.state}");
		setCheckboxToDefalut("subReceive","${subinfo.receive}")
		//setCheckboxToDefalut("institution","${subinfo.office}");
	hideYearRange($("select[name='to']"),$("select[name='from']").val());
	
		if("${not empty SDetail}"==="true"){
			$("#insertShow").show();
		}
		setFormatInputDefault(".number",2);
	$( "#tabs" ).tabs();
	$(".nextStep").click(function(){
		var nowStep=$( "#tabs" ).tabs("option", "active");
		var num = parseInt(nowStep, 10)+1;
		$( "#tabs" ).tabs({active:num});
	});
	$(".prevStep").click(function(){
		var nowStep=$( "#tabs" ).tabs("option", "active");
		var num = parseInt(nowStep, 10)-1;
		$( "#tabs" ).tabs({active:num});
	});
	$(".nextFocus").keyup(function(event){
		if(event.keyCode===13){
			if($(this).parent().nextAll('td').has(".nextFocus").length){
				$(this).parent().nextAll('td').first().children(".nextFocus").focus();
				return;
			}
		}
	});
	var y;
	if("${subinfo.editType}"!=='show'){
		y=$.extend(sdInitDataTableSetting(),sdSortChinese(),{
			"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2] },
			                 {"sSortDataType":"chinese", "aTargets": [3]},
			                 {"bSortable": false, "aTargets": [0]}
			              ]
		});
	}else{
		y=$.extend(sdInitDataTableSetting(),sdSortChinese(),{
			"aaSorting": [[ 1, "desc" ]],"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1,2] },
			                 {"sSortDataType":"chinese", "aTargets": [3]},
			                 {"bSortable": false, "aTargets": [0]}
			              ],"fnDrawCallback": function ( oSettings ) {
								/* Need to redo the counters if filtered or sorted */
								if ( oSettings.bSorted || oSettings.bFiltered ){
									for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
										$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
									}
								}
							}
		});
		
	}
	var oTable=$("#receiveTable").dataTable(y);
	$("#myInsert").click(function(){
		if(checkbeforeSubmit()){
			getReadyForm();
			getReadyTable2(oTable);
			getReadyTable(oTable);
			$("#projRForm").submit();
		}
	});
});
function checkbeforeSubmit(){
	if(checkdatatableChecked($("#receiveTable"),"請至少選擇一個文號",1)){
		return true;	
	};
}
function checkdatatableChecked($dataT,StrAlert,act){
	var oTable=$dataT.dataTable();
	var checkedbox = $('input:checked', oTable.fnGetNodes());
	if(checkedbox.length == 0){
		alert(StrAlert);
		$( "#tabs" ).tabs("option","active",act);
		return false;
	}else{
		return true;
	}
}
function getReadyTable2($dataT){
	var oTable=$dataT.dataTable();
	var nNodes = oTable.fnGetNodes();
	$("input[name='receiveNo']:checked", nNodes).each(function(){
		var ntr= $(this).parents('tr')[0];
		var aData=oTable.fnGetData(ntr);
		var finalStr=aData[2]+"&&&"+aData[1]+"&&&"+aData[3];
		var str = "<input type='checkbox' name='receiveNoAdd' value='"+finalStr+"' checked='checked' />";
		$("#projRForm").append(str);
	});
}
function getReadyForm(){
	$(".addtable").find("input:text").each(function(i){
		var x = $(this).prop("name","");
	});
	$(".insertTable").find("input:text").each(function(i){
		var value =$(this).prop("value");
		var name =$(this).prop("name");
		$("form").append("<div style='display:none;'><input type='checkbox' checked='checked' name='"+name+"' value='"+value+"'></div>");
		$(this).remove();
	});
}
function getReadyTable($dataT){
	var oTable=$dataT.dataTable();
	$('input:checked', oTable.fnGetNodes()).each(function(i,ele){
		ele.style.display="none";
		$("#projRForm").append(ele);
	});
}
</script>
<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<c:if test="${info.editType eq 'add'}"><c:set var="typeEdit"  value="新增" /></c:if>
			<c:if test="${info.editType eq 'edit'}"><c:set var="typeEdit"  value="修改" /></c:if>
			<span style="color:#F30;"><strong>&nbsp;${typeEdit}管制項目&nbsp;</strong>&nbsp;</span>
		</legend>
		<form  id="projRForm" action="<c:url value='/console/commit/subcommitact.jsp'/>" method="post">
		<div id="tabs" class="tabs-bottom" style="font-size: 16px;margin-top: 20px;">
			<ul>
			    <li><a href="#tabs-1">資金回饋編輯項目</a></li>
 				<li><a href="#tabs-2">選擇涉及文號</a></li>
			    <li style="float: right"><input type="button" class="btn_class_opener" onclick="postUrlByForm('/console/commit/commitviewo.jsp',{'serno':${cbean.serno},'editType':'show','idno':'${cbean.IDNO}','investor':'${subinfo.investor}','tabNo':'2'});" value="回企業編輯明細" /></li>
			    <c:if test="${subinfo.editType eq 'edit'}">
			    	<li style="float: right"><input type="button" class="btn_class_opener" onclick="deleteCommit()" value="刪除紀錄" /></li>
			    </c:if>
			  </ul>
			 <div class="tabs-spacer"></div>
			 <div id="tabs-1">
				<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
					<legend style="border-top:1px solid #E6E6E6;margin-bottom: 0px;">
						<span style="color:#F30;">[&nbsp;Step1&nbsp;]</span>
							<strong style="color:#222;">資金回饋-原始承諾金額</strong>
					</legend>
						<span class="basetable">
							統一編號：${cbean.IDNO}
						</span>
						<span class="basetable">
							投資人名稱：${subinfo.investor}
						</span><br/>
						<span class="basetable">
							投資案號：${subinfo.investNo}
						</span>
						<span class="basetable">
							大陸事業名稱：${subinfo.investName}
						</span>
						  <c:if test="${subinfo.editType eq 'edit'}">
							<br/>
							<span class="basetable">
								修改人：${subinfo.editor}
							</span>
							<span class="basetable">
								修改時間：${fn:substring(bean.updatetime,0,16)}
							</span>
						</c:if>
						<br/>
						<span class="basetable">
							<c:choose>
								<c:when test="${subinfo.editType eq 'show'}">管制狀態：${AoCode[cbean.state]}</c:when>
								<c:otherwise>
									管制狀態：<select style="width: 120px;" name="state">
									<option value="Y">管制</option>
									<option value="N">解除管制</option>
									</select>
								</c:otherwise>
							</c:choose>							
						</span>
						<span class="basetable">
							<c:choose>
								<c:when test="${subinfo.editType eq 'show'}">
									資金回饋期間：${subbean.startYear}年~${subbean.endYear}年
								</c:when>
								<c:otherwise>
									資金回饋期間：
									<select style="width: 80px;" name="from">
										<c:forEach var="y" begin="${cbean.startYear}" end="${cbean.endYear}" varStatus="1">
											<option value="${cbean.endYear-y+cbean.startYear}">${cbean.endYear-y+cbean.startYear}&nbsp;</option>
										</c:forEach>
									</select>年~
									<select style="width: 80px;" name="to">
										<c:forEach var="y" begin="${cbean.startYear}" end="${cbean.endYear}" varStatus="1">
											<option value="${cbean.endYear-y+cbean.startYear}">${cbean.endYear-y+cbean.startYear}&nbsp;</option>
										</c:forEach>
									</select>年
								</c:otherwise>
							</c:choose>
							<input type="hidden" name="restrainType" value="03"/>
							<input type="hidden" name="IDNO" value="${cbean.IDNO}"/>
							<input type="hidden" name="investNo" value="${subinfo.investNo}"/>
							<input type="hidden" name="editType" value="${subinfo.editType}"/>
							<input type="hidden" name="sernoStr" value="${subinfo.serno}"/>
							<input type="hidden" name="subserno" value="${subinfo.subserno}"/>
						</span><br/>
						
						<span class="basetable">
							<c:choose>
								<c:when test="${subinfo.editType eq 'show'}">
									說明：${subbean.note}									
								</c:when>
								<c:otherwise>
									說明：<textarea name="notes" style="width: 88%;vertical-align: top;" rows="6" cols="150">${subbean.note}</textarea>
								</c:otherwise>
							</c:choose>
						</span>
						<br/><br/>
						<strong style="margin:5px 10px;color:#222;">資金回饋總額</strong>
						<table class="formProj">
							<tr>
								<th>資金回饋總額</th>
								<td>
									<span>幣別：${AoCode[cbean.repType]}
									</span>
									&nbsp;&nbsp;&nbsp;總金額
									<c:choose>
										<c:when test="${subinfo.editType eq 'show'}">
											<span class="numberFmt trRight">${TDetail['03']}</span>元
										</c:when>
										<c:otherwise>
											<input type="text" name="RsType03" class="numberFmt trRight" value="${TDetail['03']}"/>元
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</table><br/>
						<c:if test="${subinfo.editType eq 'add' || subinfo.editType eq 'edit'}">
						<strong style="margin:5px 10px;color:#222;">資金回饋</strong>
							<table class="formProj addtable">
								<tr>
									<th>年度</th>
									<td><input type="text" name="atypeYear" style="width: 95%;" class="nextFocus" maxlength="3" title="請輸入三位碼的民國年"/></td>
									<th>承諾金額</th>
									<td><input type="text" name="aRsType0301" style="width: 95%;" class="nextFocus numberFmt trRight"/></td>
									<td><input type="button" id="addRowCount" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="自行新增" /></td>
								</tr>
							</table>
					</c:if>
					<div id="insertShow" style="display: none;">
					<br/>
					<c:choose>
						<c:when test="${subinfo.editType eq 'show'}">
							<strong style="margin:5px 10px;color:#222;">分年資金回饋</strong>
							<table class="formProj insertTable">
								<tr>
									<th>序號</th>
									<th>年度</th>
									<th>承諾金額</th>
									<th>累計承諾金額</th>
								</tr>
	 						<c:if test="${not empty SDetail}">
								<c:forEach var="ex" items="${SDetail}" varStatus="i">
									<tr style="text-align: center;">
										<td>(${i.index+1})</td>
										<td>${ex.key}</td>
										<td><span class="numberFmt trRight">${ex.value['0301']}</span></td>
										<td><span class="number trRight">${ex.value['0302']}</span></td>
									</tr>
								</c:forEach>
							</c:if> 
							</table>
						</c:when>
						<c:otherwise>
						<strong style="margin:5px 10px;color:#222;">分年資金回饋</strong>
						<table class="formProj insertTable">
							<tr>
								<th>刪除</th>
								<th>年度</th>
								<th>承諾金額</th>
								<th>累計承諾金額</th>
							</tr>
 						<c:if test="${not empty SDetail}">
							<c:forEach var="ex" items="${SDetail}">
								<tr style="text-align: center;">
									<td><input type="button" onclick="delRowRs03Input(this);" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="刪除" /></td>
									<td><input readonly="readonly" type="text" name="atypeYear" style="width: 95%;" maxlength="3" value="${ex.key}"/></td>
									<td><input type="text" name="aRsType0301" style="width: 95%;" class="numberFmt trRight" value="${ex.value['0301']}"/></td>
									<td><input type="text" name="aRsType0302" style="width: 95%;" class="number trRight" value="${ex.value['0302']}"/></td>
								</tr>
							</c:forEach>
						</c:if> 
						</table>
						</c:otherwise>
					</c:choose>
					</div>
				</fieldset>	
				<c:if test="${subinfo.editType != 'show'}">
					<div style="text-align: center;margin: 10px 0px 5px 0px;">
						<input type="button" class="btn_class_opener nextStep" style="color: #777777;text-align: center;font-size: 12px;" value="下一步" />
					</div>
				</c:if>
			 </div>
				 <div id="tabs-2">
					<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
							<legend style="width:100%;border-top:1px solid #E6E6E6">
								<span style="color:#F30;">[&nbsp;Step2&nbsp;]</span>
								<strong style="color:#222;">選擇涉及文號</strong>
							</legend>
						<div>
							<table id="receiveTable" style="width: 95%;margin-left: 10px;"> 
								<thead>
									<tr>
										<c:if test="${subinfo.editType != 'show'}"><th>選擇</th></c:if>
										<c:if test="${subinfo.editType eq 'show'}"><th>序號</th></c:if>
										<th>核准日期</th>
										<th>文號</th>
										<th>案由</th>
									</tr>
								</thead>
								<tbody id="SetReceive">
									<c:choose>
										<c:when test="${subinfo.editType eq 'show'}">
											<c:forEach var="item" items="${receiveNo}" varStatus="i">
												<tr>
													<td></td>
													<td>${ibfn:addSlash(item.respDate)}</td>
													<td>${item.receiveNo}</td>
													<td>${item.note}</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<c:forEach var="item" items="${subReceiveNo}">
												<tr>
													<td><input type="checkbox" name="subReceive" value="${item.receiveNo}"></td>
													<td>${ibfn:addSlash(item.respDate)}</td>
													<td>${item.receiveNo}</td>
													<td>${item.note}</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
					</fieldset>
				<c:if test="${subinfo.editType != 'show'}">
					<div style="text-align: center;margin: 10px 0px 5px 0px;">
						<input type="button" class="btn_class_opener prevStep" style="color: #777777;text-align: center;font-size: 12px;" value="上一步" />
						<input type="button" id="myInsert" class="btn_class_Approval" style="font-size: 13px;" value="確認${typeEdit}"/>
					</div>
				</c:if>
			</div>
		</div>
	</form>
	</fieldset>
</div>	