<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.isam.service.*,com.isam.service.ofi.*,com.isam.bean.*,java.util.*,com.isam.helper.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<%
	session.removeAttribute("fbean");
	session.removeAttribute("fcbean");
	session.removeAttribute("frtOpt");
	session.removeAttribute("ibean");
	InterviewoneHelp opt = new InterviewoneHelp();
	InterviewoneService iSer=new InterviewoneService();
	OFIReInvestNoXFContentService fcSer= new OFIReInvestNoXFContentService();
	OFIReInvestNoXFinancialService fSer = new OFIReInvestNoXFinancialService();
	session.setAttribute("frtOpt", opt.getqTypeF());
	String serno=DataUtil.nulltoempty(request.getParameter("serno"));
	String type=DataUtil.nulltoempty(request.getParameter("type"));
	String reInvestNo=DataUtil.nulltoempty(request.getParameter("reInvestNo"));
	session.setAttribute("ibean", iSer.getMaxYearEMPCountRx(reInvestNo));
	if(!serno.isEmpty()&&!DataUtil.nulltoempty(request.getParameter("type")).equals("add")){
		session.setAttribute("fbean", fSer.selectBySerno(serno));
		session.setAttribute("fcbean", fcSer.selectBySerno(serno));
	}
%>
<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/yearmonthhelper.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script type="text/javascript">
$(function(){
	setYearOption($("select[name='reportyear']"));
	setYearUpperDownBound($("select[name='reportyear']"),10);
	setSelectedToDefalut("reportyear", parseInt("${fbean.reportyear}").toString());
	setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",0);
	inputTextNext("#myform",".nextInput",".skip");
	if("${param.type}"==="show"){
		$(".numberFmt").not(".emp .numberFmt").each(function(){
			var v=$.trim($(this).text());
			if(v.length===0){
				$(this).text("-");
			}
		});
	}
	$( "input[name='reportdate']").datepicker({
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
	$("#myInsert").click(function(){
		var rd=$("input[name='reportdate']").val();
		if(rd.length==0){
			alert("填報日期為必填欄位");
			$("input[name='reportdate']").focus();
			return false;
		}else{
			var x=parseInt(rd.substring(0,3),10);
			var y=parseInt($("select[name='reportyear']").val(),10);
			var z=x-y;
			if(z!=1){
				$( "<div>填報日期年度("+y+")應與申報年度("+x+")相差一年，請確認是否繼續？</div>" ).dialog({
				      resizable: false,
				      height:200,
				      width:600,
				      modal: true,
				      buttons: {
				      "確定": function() {
						$("#myform").submit();
				          $( this ).dialog( "close" );
				        },
				       "取消": function() {
				          $( this ).dialog( "close" );
				        }
				      }
				    });
			}else{
				$("#myform").submit();
			}
		}
	});
	$(".emp input").blur(function(){
		countemp();
	});
	countemp();
	$("#checkemp").click(function(){
		$(".emp input").each(function(){
			var $tr=$(this).parents("tr");
			var $trNext=$(this).parents("tr").next();
			var $td=$(this).parents("td");
			var num=$tr.find("td").index($td);
			$(this).val($trNext.find("td:eq("+num+")").find("span").text());
		});
		countemp();
	});
});
function countemp(){
	var c=0;
	var f=0;
	$(".emp .empsub").each(function(){
		var x=getValueText($(this));
		if(x.length>0){
			c+=parseFloat(getValueText($(this)));
			f++;
		}
	});
	if(f!=0){
		$(".emp .tt").text(getInsertComma(c,0));
	}
	$(".emp .numberFmt").each(function(){
		var v=$.trim($(this).text());
		if(v.length===0){
			$(this).text(" --");
		}
	});
}
</script>
<c:choose>
	<c:when test="${param.type eq 'show'}">
		<table class="formProj">
			<tr>
				<td class="trRight" style="width: 18%;">財報年度：</td>
				<td style="width: 32%;">${fbean.reportyear}</td>
				<td class="trRight" style="width: 18%;">填報日期：</td>
				<td>${ibfn:addSlash(fbean.reportdate)}</td>
			</tr>
				<tr>
					<td class="trRight">${frtOpt['58'].cName}：</td>
					<td>
						新台幣&nbsp;<span class="numberFmt">${fcbean['58']}</span>&nbsp;元
					</td>
					<td class="trRight">${frtOpt['61'].cName}：</td>
					<td >
						新台幣&nbsp;<span class="numberFmt">${fcbean['61']}</span>&nbsp;元
					</td>
				</tr>
				<tr>
					<td class="trRight">${frtOpt['59'].cName}：</td>
					<td>
						新台幣&nbsp;<span class="numberFmt">${fcbean['59']}</span>&nbsp;元
					</td>
					<td class="trRight">${frtOpt['62'].cName}：</td>
					<td >
						新台幣&nbsp;<span class="numberFmt">${fcbean['62']}</span>&nbsp;元
					</td>
				</tr>
				<tr>
					<td class="trRight">${frtOpt['60'].cName}：</td>
					<td>
						新台幣&nbsp;<span class="numberFmt">${fcbean['60']}</span>&nbsp;元
					</td>
					<td class="trRight">${frtOpt['63'].cName}：</td>
					<td >
						新台幣&nbsp;<span class="numberFmt">${fcbean['63']}</span>&nbsp;元
					</td>
				</tr>
				<tr>
					<td class="trRight"></td>
					<td></td>
					<td class="trRight">${frtOpt['64'].cName}：</td>
					<td >
						新台幣&nbsp;<span class="numberFmt">${fcbean['64']}</span>&nbsp;元
					</td>
				</tr>
				<tr>
					<td class="trRight">${frtOpt['65'].cName}：</td>
					<td colspan="3"><span>${fcbean['65']}</span></td>
				</tr>
				<tr>
					<td class="trRight">${frtOpt['66'].cName}：</td>
					<td colspan="3"><span>${fcbean['66']}</span></td>
				</tr>
				<tr>
					<td class="trRight">${frtOpt['67'].cName}：</td>
					<td colspan="3"><pre>${fcbean['67']}</pre></td>
				</tr>
				<tr>
					<td class="trRight">${frtOpt['68'].cName}：</td>
					<td colspan="3"><pre>${fbean.note}</pre></td>
				</tr>
				<tr class="emp">
					<td class="trRight">國內事業員工人數：<br>（含派駐管理職）</td>
					<td colspan="3" style="text-align: left;">共計<span class="numberFmt tt"></span>&nbsp;人
					（其中${frtOpt['74'].cName}<span class="numberFmt empsub">${fcbean['74']}</span>&nbsp;人
					、${frtOpt['75'].cName}<span class="numberFmt empsub">${fcbean['75']}</span>&nbsp;人
					、${frtOpt['76'].cName}<span class="numberFmt empsub">${fcbean['76']}</span>&nbsp;人）。</td>
				</tr>
		</table>
	</c:when>
	<c:when test="${param.type eq 'add' ||param.type eq 'edit'}">
		<div>
			<form id="myform" action="<c:url value='/console/cnfdi/updateRxfinancial.jsp'/>" method="post">
				<table class="formProj">
					<tr>
						<td class="trRight" style="width: 20%;">財報年度：</td>
						<td >
							<select style="width: 80px;" name="reportyear">
							</select>
						</td>
						<td class="trRight" style="width: 25%;">填報日期：</td>
						<td >
							<input type="text" name="reportdate" value="${ibfn:addSlash(fbean.reportdate)}">
						</td>
					</tr>
						<tr>
							<td class="trRight">${frtOpt['58'].cName}：</td>
							<td>
								新台幣&nbsp;<input class="numberFmt" type="text" name="${frtOpt['58'].paramName}" value="${fcbean['58']}">&nbsp;元
							</td>
							<td class="trRight">${frtOpt['61'].cName}：</td>
							<td >
								新台幣&nbsp;<input class="numberFmt" type="text" name="${frtOpt['61'].paramName}" value="${fcbean['61']}">&nbsp;元
							</td>
						</tr>
						<tr>
							<td class="trRight">${frtOpt['59'].cName}：</td>
							<td>
								新台幣&nbsp;<input class="numberFmt" type="text" name="${frtOpt['59'].paramName}" value="${fcbean['59']}">&nbsp;元
							</td>
							<td class="trRight">${frtOpt['62'].cName}：</td>
							<td >
								新台幣&nbsp;<input class="numberFmt" type="text" name="${frtOpt['62'].paramName}" value="${fcbean['62']}">&nbsp;元
							</td>
						</tr>
						<tr>
							<td class="trRight">${frtOpt['60'].cName}：</td>
							<td>
								新台幣&nbsp;<input class="numberFmt" type="text" name="${frtOpt['60'].paramName}" value="${fcbean['60']}">&nbsp;元
							</td>
							<td class="trRight">${frtOpt['63'].cName}：</td>
							<td >
								新台幣&nbsp;<input class="numberFmt" type="text" name="${frtOpt['63'].paramName}" value="${fcbean['63']}">&nbsp;元
							</td>
						</tr>
						<tr>
							<td class="trRight"></td>
							<td></td>
							<td class="trRight">${frtOpt['64'].cName}：</td>
							<td >
								新台幣&nbsp;<input class="numberFmt" type="text" name="${frtOpt['64'].paramName}" value="${fcbean['64']}">&nbsp;元
							</td>
						</tr>
						<tr>
							<td class="trRight">${frtOpt['65'].cName}：</td>
							<td colspan="3">
								<input type="text" name="${frtOpt['65'].paramName}" value="${fcbean['65']}" style="width: 90%;">
							</td>
						</tr>
						<tr>
							<td class="trRight">${frtOpt['66'].cName}：</td>
							<td colspan="3">
								<input type="text" name="${frtOpt['66'].paramName}" value="${fcbean['66']}" style="width: 90%;">
							</td>
						</tr>
						<tr>
							<td class="trRight">${frtOpt['67'].cName}：</td>
							<td colspan="3">
								<textarea name="${frtOpt['67'].paramName}" rows="5" style="width: 90%;">${fcbean['67']}</textarea>
							</td>
						</tr>
						<tr>
							<td class="trRight">${frtOpt['68'].cName}：</td>
							<td colspan="3">
								<textarea name="${frtOpt['68'].paramName}" rows="5" style="width: 90%;">${fbean.note}</textarea>
							</td>
						</tr>
						<tr>
						<c:choose>
							<c:when test="${empty ibean}">
								<th colspan="4">國內事業員工（含派駐管理職）</th>
							</c:when>
							<c:otherwise>
									<th colspan="3">國內事業員工（含派駐管理職）</th>
									<th><input type="button" id="checkemp" class="btn_class_loaddata" value="同問卷人數"/></th>
							</c:otherwise>
						</c:choose>
						</tr>
						<tr>
							<th>員工總人數</th>
							<th>${frtOpt['74'].cName}</th>
							<th>${frtOpt['75'].cName}</th>
							<th>${frtOpt['76'].cName}</th>
						</tr>
						<tr class="emp">
							<td class="trRight"><span class="numberFmt tt"></span>&nbsp;人</td>
							<td class="trRight"><input class="numberFmt empsub" type="text" name="${frtOpt['74'].paramName}" value="${fcbean['74']}">&nbsp;人</td>
							<td class="trRight"><input class="numberFmt empsub" type="text" name="${frtOpt['75'].paramName}" value="${fcbean['75']}">&nbsp;人</td>
							<td class="trRight"><input class="numberFmt empsub" type="text" name="${frtOpt['76'].paramName}" value="${fcbean['76']}">&nbsp;人</td>
						</tr>
						<c:choose>
							<c:when test="${empty ibean}">
								<tr><th colspan="4">尚無問卷資料</th></tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td class="trRight"><span class="numberFmt">${ibean.tt}</span>&nbsp;人&nbsp;(${ibean.year}年問卷)</td>
									<td class="trRight"><span class="numberFmt">${ibean['45']}</span>&nbsp;人</td>
									<td class="trRight"><span class="numberFmt">${ibean['46']}</span>&nbsp;人</td>
									<td class="trRight"><span class="numberFmt">${ibean['47']}</span>&nbsp;人</td>
								</tr>
							</c:otherwise>
						</c:choose>
				</table>
				<input type="hidden" value="${param.reInvestNo}" name="reInvestNo">
				<input type="hidden" value="${param.investNo}" name="investNo">
				<input type="hidden" value="${param.type}" name="type">
				<input type="hidden" value="${param.serno}" name="serno">
				<div style="text-align: center;margin-top: 10px;">
					<input type="button" id="myInsert" class="btn_class_Approval" value="儲存"/>
				</div>
			</form>
		</div>
	</c:when>
</c:choose>
