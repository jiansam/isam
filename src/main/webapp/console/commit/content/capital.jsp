<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<script src="<c:url value='/js/restraintab.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/addRow.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/fmtNumber.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/ajaxRequest.js'/>" type="text/javascript"></script>
<script>
$(function(){
	
	if("${info.editType}"==="edit"){
		//setYearFromToDefalut("${bean.startYear}","${bean.endYear}");
		setYearOption('from',"${bean.startYear}");
		setYearOption('to',"${bean.endYear}");
		changeYearEditOption("${bean.startYear}");
		setSelectedToDefalut("state","${bean.state}");
		setSelectedToText("repType","${bean.repType}");
		setCheckboxToDefalut("institution","${office}");
	
		var obj = $.parseJSON('${detailBean}');
		$.each(obj,function(i,ele){
			var name="RsType"+obj[i].type;
			var cont= getInsertComma(parseFloat(obj[i].value,10),2)
			$("input[name='"+name+"']").val(cont);
		});
		if("${not empty detailEx}"==="true"){
			$("#insertShow").show();
		}

		var state_original = $("select[name='state']").val();
		$("#saveVal").click(function(){
			getReadyForm();
			//107-01-04 新增是否編輯聯絡人選擇
			var state_now = $("select[name='state']").val();
			if(state_original != state_now && "${cContacts}" != "0"){
				open_dialog("<div id='isEditContact'>請問您修改管制項目狀態後，是否繼續編輯聯絡人？</div>", 450, false, "儲存管制項目狀態", true);
			}else{
				$("#projRForm").submit();
			}
		});
		$("#roadDiv").one("click",function(){
			$(".reNoDiv").show();
			$("#saveVal").remove();
			$("input[name='DelreNo']").remove();
			$(this).val("下一步");
			var obj1 = $.parseJSON('${receiveNo}');
			var obj2 = $.parseJSON('${investNo}');
			getRestrainTables("${info.idno}","restrain","${info.editType}",obj1,obj2);
		});
		setFormatInputDefault(".number",2);
	}
});
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
</script>
<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<c:if test="${info.editType eq 'add'}"><c:set var="typeEdit"  value="新增" /></c:if>
			<c:if test="${info.editType eq 'edit'}"><c:set var="typeEdit"  value="修改" /></c:if>
			<span style="color:#F30;"><strong>&nbsp;${typeEdit}管制項目&nbsp;</strong>&nbsp;</span>
		</legend>
		<form  id="projRForm" action="<c:url value='/console/commit/editcommit.jsp'/>" method="post">
		<div id="tabs" class="tabs-bottom" style="font-size: 16px;margin-top: 20px;">
			<ul>
			    <li><a href="#tabs-1">資金回饋編輯項目</a></li>
			    <li class="reNoDiv" <c:if test="${info.editType eq 'edit'}">style="display: none;"</c:if>><a href="#tabs-2">選擇涉及文號</a></li>
			    <li class="reNoDiv" <c:if test="${info.editType eq 'edit'}">style="display: none;"</c:if>><a href="#tabs-3">選擇涉及大陸事業</a></li>
			    <li style="float: right"><input type="button" class="btn_class_opener" onclick="postUrlByForm('/console/commit/showcommitdetail.jsp',{'serno':'${info.idno}'})" value="回企業編輯" /></li>
			    <c:if test="${info.editType eq 'edit'}">
			    	<li style="float: right"><input type="button" class="btn_class_opener" onclick="postUrlByForm('/console/commit/commitviewo.jsp',{'serno':'${info.serno}','editType':'show','idno':'${info.idno}','investor':'${info.investor}','tabNo':'2'});" value="編輯明細" /></li>
			    	<li style="float: right"><input type="button" class="btn_class_opener" onclick="deleteCommit('${info.serno}','${info.idno}')" value="刪除紀錄" /></li>
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
						統一編號：${info.idno}
					</span>
					<span class="basetable">
						投資人名稱：${info.investor}
					</span>
					 <c:if test="${info.editType eq 'edit'}">
					 	<br/>
						<span class="basetable">
							修改人：${info.editor}
						</span>
						<span class="basetable">
							修改時間：${fn:substring(bean.updatetime,0,16)}
						</span>
						</c:if>
					<br/>
					<span class="basetable">
						管制狀態：<select style="width: 120px;" name="state">
						<option value="Y">管制</option>
						<option value="N">解除管制</option>
						</select>
					</span>
					<span class="basetable">
						資金回饋期間：
						<select class="cYear" style="width: 80px;" name="from">
						</select>~
						<select class="cYear" style="width: 80px;" name="to">
						</select>
						<input type="hidden" name="restrainType" value="03"/>
						<input type="hidden" name="IDNO" value="${info.idno}"/>
						<input type="hidden" name="editType" value="${info.editType}"/>
						<input type="hidden" name="sernoStr" value="${info.serno}"/>
					</span><br/>
					
						<span class="basetable">
						說明：<textarea name="notes" style="width: 88%;vertical-align: top;margin-top: 5px;" rows="6" cols="150">${bean.note}</textarea>
					</span>
					<br/><br/>
					<strong style="margin:5px 10px;color:#222;">資金回饋總額</strong>
					<table class="formProj">
						<tr>
							<th>資金回饋總額</th>
							<td>
								<span>幣別：<select name="repType" style="width: 100px;">
									<option value=""></option>
									<option value="NTD">新台幣</option>
									<option value="RMB">人民幣</option>
									<option value="USD">美元</option>
									<option value="HKD">港幣</option>
								</select></span>&nbsp;&nbsp;&nbsp;總金額
								<input type="text" name="RsType03" class="numberFmt trRight"/>元
							</td>
						</tr>
					</table><br/>
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
				<div id="insertShow" style="display: none;">
					<br/>	
					<strong style="margin:5px 10px;color:#222;">分年資金回饋</strong>
						<table class="formProj insertTable">
							<tr>
								<th>刪除</th>
								<th>年度</th>
								<th>承諾金額</th>
								<th>累計承諾金額</th>
							</tr>
						<c:if test="${not empty detailEx}">
							<c:forEach var="ex" items="${detailEx}">
								<tr style="text-align: center;">
									<td><input type="button" onclick="delRowRs03Input(this);" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="刪除" /></td>
									<td><input readonly="readonly" type="text" name="atypeYear" style="width: 95%;" maxlength="3" value="${ex[0]}"/></td>
									<td><input type="text" name="aRsType0301" style="width: 95%;" class="numberFmt trRight" value="${ex[1]}"/></td>
									<td><input type="text" name="aRsType0302" style="width: 95%;" class="number trRight" value="${ex[2]}"/></td>
								</tr>
							</c:forEach>
						</c:if>
						</table>
					</div>
			</fieldset>	
					<div style="text-align: center;margin: 10px 0px 5px 0px;">
						<c:if test="${info.editType eq 'add'}">
							<input type="button" class="btn_class_opener nextStep" style="color: #777777;text-align: center;font-size: 12px;" value="下一步" />
						</c:if>
						<c:if test="${info.editType eq 'edit'}">
							<input type="hidden" name="DelreNo" value="0"/>
							<input id="saveVal" type="button" class="btn_class_Approval" style="font-size: 13px;" value="儲存" />
							<input id="roadDiv" type="button" class="btn_class_opener nextStep" style="color: #777777;text-align: center;font-size: 12px;" value="繼續編輯文號" />
						</c:if>
					</div>
			 </div>
			<div class="reNoDiv" <c:if test="${info.editType eq 'edit'}">style="display: none;"</c:if>>
			 <jsp:include page="/console/commit/content/recevice.jsp" flush="true" >
				<jsp:param value="${info.idno}" name="idno"/>
				<jsp:param value="${info.editType}" name="editType"/>
				<jsp:param value="restrain" name="type"/>
				<jsp:param value="${typeEdit}" name="typeEdit"/>
			</jsp:include>
			</div>
		</div>
	</form>
	</fieldset>
</div>	