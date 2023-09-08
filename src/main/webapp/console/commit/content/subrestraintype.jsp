<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>

<script src="<c:url value='/js/project.js'/>" type="text/javascript" ></script>
<script src="<c:url value='/js/addRow.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<%-- <script src="<c:url value='/js/hideOptionBySpan.js'/>" type="text/javascript" charset="utf-8"></script>
 <script src="<c:url value='/js/restraintab.js'/>" type="text/javascript"></script>
 --%>
<script>
$(function() {
	setYearOption('from',"${subbean.startYear}");
	setYearOption('to',"${subbean.endYear}");
	changeYearEditOption("${subbean.startYear}");
	setYearFromToDefalut("${subbean.startYear}","${subbean.endYear}");
	
	setSelectedToDefalut("state","${subbean.state}");
	setCheckboxToDefalut("subReceive","${subinfo.receive}")
	setCheckboxToDefalut("institution","${subinfo.office}")
	
/* 	setYearSelectRange($("select[name='from']"),$("select[name='to']"));
	setSelectedToDefalut('from',parseInt("${subbean.startYear}",10))
	setSelectedToDefalut('to',parseInt("${subbean.endYear}",10));
	setSelectedToDefalut('state',"${subbean.state}");
	
	setCheckboxToDefalut("subReceive","${subinfo.receive}")
	setCheckboxToDefalut("institution","${subinfo.office}")
	hideYearRange($("select[name='to']"),$("select[name='from']").val()); */
	if("${not empty SDetail}"==="true"){
		$("#insertShow").show();
		sumRowByTRsType();
	}
});
function deleteCommit(){
	 $("<div style='font-size='12px;''>您即將刪除本筆資料，並將同步移除其所屬執行情形報表，請確認是否繼續?</div>").dialog({
		width: 350,
		modal:true,
		title:'刪除此筆資料',
		buttons: {
	        "確定": function() {
	    	  postUrlByForm('/console/commit/subcommitact.jsp',{
	    		  'subserno':'${subinfo.subserno}','restrainType':'${cbean.type}','editType':'delete','sernoStr':'${subinfo.serno}','IDNO':'${cbean.IDNO}'
	    	  });
	          $( this ).dialog( "close" );
	        },
	        "取消": function() {
	          $( this ).dialog( "close" );
	        }
		}
	});
}
</script>
<script>
$(function(){
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
function changeYearEditOption(max){
	$("select[name='to']").find("option").each(function(){
		if($(this).prop("class")==="hideOptions"){
			$(this).removeClass("hideOptions");
			$(this).unwrap("<span style='display:none;'></span>");
		}
	});
	var m=parseInt(max, 10);
	$("select[name='to']").find("option").each(function(){
		var comp=parseInt($(this).val(), 10);
		if(comp<m){
			$(this).addClass("hideOptions");
			$(this).wrap("<span style='display:none;'></span>");
		}
	});
}
function setYearFromToDefalut(year,to){
	if(year.length>0&&to.length>0){
		year = parseInt(year, 10);
		to = parseInt(to, 10);
		$("select[name='from']").find("option[value='"+year+"']").prop("selected",true);
		changeYearEditOption(year);
		$("select[name='to']").find("option[value='"+to+"']").prop("selected",true);
	}
}
</script>
<div>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="text-align:center;padding:5px;width:200px;white-space:nowrap;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
		<c:if test="${subinfo.editType eq 'add'}"><c:set var="typeEdit"  value="新增" /></c:if>
		<c:if test="${subinfo.editType eq 'edit'}"><c:set var="typeEdit"  value="修改" /></c:if>
		<c:if test="${subinfo.editType eq 'show'}"><c:set var="typeEdit"  value="檢視" /></c:if>
		<span style="color:#F30;"><strong>&nbsp;${typeEdit}管制項目明細&nbsp;</strong>&nbsp;</span>
	</legend>
	<form  id="projRForm" action="<c:url value='/console/commit/subcommitact.jsp'/>" method="post">
		<div id="tabs" class="tabs-bottom" style="font-size: 16px;margin-top: 20px;">
			<ul>
			    <li><a href="#tabs-1">國內相對投資計劃編輯項目</a></li>
			    <li><a href="#tabs-2">選擇涉及文號</a></li>
<%-- 		    	<li style="float: right"><input type="button" class="btn_class_opener" onclick="postUrlByForm('/console/commit/showcommitdetail.jsp',{'serno':'${cbean.IDNO}'})" value="回企業編輯" /></li> --%>
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
						<strong style="color:#222;">國內相對投資計劃-原始承諾金額</strong>
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
							申報類型：${AoCode[cbean.repType]}
						</span>
						<span class="basetable">
							<c:choose>
								<c:when test="${subinfo.editType eq 'show'}">
									申報期間：${subbean.startYear}年~${subbean.endYear}年
								</c:when>
								<c:otherwise>
									申報期間：
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
							<input type="hidden" name="restrainType" value="01"/>
							<input type="hidden" name="IDNO" value="${cbean.IDNO}"/>
							<input type="hidden" name="investNo" value="${subinfo.investNo}"/>
							<input type="hidden" name="editType" value="${subinfo.editType}"/>
							<input type="hidden" name="sernoStr" value="${subinfo.serno}"/>
							<input type="hidden" name="subserno" value="${subinfo.subserno}"/>
						</span>
						<br/>
						<span class="basetable" style="vertical-align: top;">
						<c:choose>
								<c:when test="${subinfo.editType eq 'show'}">
									涉及機關：
									<c:forEach var="item" items="${suboffice}" varStatus="i">
										<c:if test="${i.index>0}">、</c:if>
									 	${CRType[item.type]}
									</c:forEach>
								</c:when>
								<c:otherwise>
									 涉及機關：
									 	<c:forEach var="item" items="${suboffice}">
										 	<input type='checkbox' value='${item.type}' name='institution' id='I${item.type}' /><label for='I${item.type}'>${CRType[item.type]}</label>
									 	</c:forEach>
								</c:otherwise>
							</c:choose>
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
						<strong style="margin:5px 10px;color:#222;">國內相對投資計劃-總原始承諾金額（新台幣元）</strong>
					<table class="formProj">
						<tr>
							<c:forEach var="item" items="${cRsType}">
								<th>${CRType[item.type]}</th>
							</c:forEach>							
						</tr>
						<tr class="trRight">
							<c:choose>
								<c:when test="${subinfo.editType eq 'show'}">
									<c:forEach var="item" items="${cRsType}">
										<td><span class="numberFmt trRight">${TDetail[item.type]}</span></td>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<c:forEach var="item" items="${cRsType}">
										<td><input type="text" name="TRsType${item.type}" style="width: 95%;" class="numberFmt trRight" value="${TDetail[item.type]}"/></td>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tr>
					</table>
					<c:if test="${subinfo.editType eq 'add' || subinfo.editType eq 'edit'}">
						<br/><br/>
						<strong style="margin:5px 10px;color:#222;">國內相對投資計劃-新增分年原始承諾金額（新台幣元）</strong>
					<table class="formProj addtable">
						<tr>
							<th>年度</th>
							<c:forEach var="item" items="${cRsType}">
								<th>${CRType[item.type]}</th>
							</c:forEach>									
							<th>確認新增</th>								
						</tr>
						<tr class="trRight">
							<td style="width: 10%;"><input type="text" name="atypeYear" style="width: 95%;" class="nextFocus numberFmt" maxlength="3" title="請輸入三位碼的民國年"/></td>
							<c:forEach var="item" items="${cRsType}">
								<td><input type="text" name="aRsType${item.type}" style="width: 95%;" class="numberFmt trRight" value=""/></td>
							</c:forEach>
							<td style="width: 10%;"><input type="button" id="addRowCalculate" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="自行新增" /></td>
						</tr>
					</table>
					</c:if>
					<div id="insertShow" style="display: none;">
					<br/>
					<c:choose>
						<c:when test="${subinfo.editType eq 'show'}">
							<strong style="margin:5px 10px;color:#222;">分年原始承諾金額</strong>
							<table class="formProj insertTable">
								<tr>
									<th>序號</th>
									<th>年度</th>
									<c:forEach var="item" items="${cRsType}">
										<th>${CRType[item.type]}</th>
									</c:forEach>
								</tr>
	 						<c:if test="${not empty SDetail}">
								<c:forEach var="ex" items="${SDetail}" varStatus="i">
									<tr style="text-align: center;">
										<td>(${i.index})</td>
										<td>${ex.key}</td>
										<c:forEach var="item" items="${cRsType}">
											<td><span class="numberFmt trRight">${ex.value[item.type]}</span></td>
										</c:forEach>
									</tr>
								</c:forEach>
							</c:if> 
							</table>
						</c:when>
						<c:otherwise>
						<strong style="margin:5px 10px;color:#222;">分年原始承諾金額</strong>
						<table class="formProj insertTable">
							<tr>
								<th>刪除</th>
								<th>年度</th>
								<c:forEach var="item" items="${cRsType}">
									<th>${CRType[item.type]}</th>
								</c:forEach>
							</tr>
 						<c:if test="${not empty SDetail}">
							<c:forEach var="ex" items="${SDetail}">
								<tr style="text-align: center;">
									<td><input type="button" onclick="delRowInput(this);" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="刪除" /></td>
									<td><input readonly="readonly" type="text" name="atypeYear" style="width: 95%;" maxlength="3" value="${ex.key}"/></td>
									<c:forEach var="item" items="${cRsType}">
										<td><input type="text" name="aRsType${item.type}" style="width: 95%;" class="numberFmt trRight" value="${ex.value[item.type]}"/></td>
									</c:forEach>
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
			
			
			
		
