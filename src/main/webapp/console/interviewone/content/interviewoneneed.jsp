<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>

<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript">
$(function(){
	/*設定enter 下一個text*/
	inputTextNext("#myform",".nextInput",".skip");
	/*設定選項預設值*/
	setDefalutOptionByClass($(".defClass"));
	/*設定選擇其他時，出現的文字框*/
	setListenerRT($(".RTCheckRadio"));
	setListenerRTText($(".RTCheckText input[type='text']"));
	/*設定陸資企業群聚現象*/
	setListenerCEF();
	$(".getDialog").click(function(){
		var classify= $(this).prop("id");
		$.post( "${pageContext.request.contextPath}/includes/option.jsp", {"tt": classify},function(data){
			$( "#test" ).html(data); 
			$( "#test" ).dialog({
			      height:500,
			      width:800,
			      modal: true,
			      resizable: false,
			      draggable: false,
			      buttons: {
			        "加入評語": function() {
			        	var str=$.trim($("textarea[name='"+classify+"']").val());
			        	$( this ).find(".CLI:checked").each(function(){
			        		str+=$(this).parents("tr").find("td:eq(2)").text()+"；";
			        	});
			        	$("textarea[name='"+classify+"']").val(str);
			          $( this ).dialog( "close" );
			        },
			        "取消": function() {
			          $( this ).dialog( "close" );
			        }
			      },
			      close: function( event, ui ) {
			    	  $( "#test" ).html(""); 
			      }
		});
		},"html");
	});
	

});

//107-07-31
function setAddr() {
	var bcity = $("#getAddr select[name='bcity']").val()
	var btown = $("#getAddr select[name='btown']").val()
	var baddr = $("#getAddr input[name='baddr']").val()
	
	$("#setAddr select[name='City']").select2("val", bcity);
	
	//變更 實際營運地址 城市選項
	var fin = 'x'; //要改的是實際營運地址
	var classNo="."+fin+bcity;
	$("#setAddr select[name='Town']").find("option").each(function(){
		removeSpan($(this));
	});
	$("#setAddr select[name='Town']").find("option").not(classNo).each(function(){
		addSpan($(this));
	});
	
	//變更選中的城市
	$("#setAddr select[name='Town']").select2("val", btown);
	$("#setAddr input[name='Addr']").val(baddr);
}

function setTWAddrChangeSpan($TWlev2,classNo){

}
</script>

<script type="text/javascript">
$(function(){
	$("input[name='rBarrier']").click(function(){
		if($("input[name='rBarrier']:checked").length>0){
			$("input[name='iBarrier'][value='2']").prop("checked",true);
		}
	});
	$("input[name='eBarrier']").click(function(){
		if($("input[name='eBarrier']:checked").length>0){
			$("input[name='iBarrier'][value='3']").prop("checked",true);
		}
	});
	$("input[name='iBarrier'][value='2'],input[name='iBarrier'][value='3']").click(function(){
		if(!$(this).prop("checked")){
			$(this).parents(".groupitem").find(".RTCheckText").children().val("");
			$(this).parents(".groupitem").find(".RTCheckText").hide();
			$(this).parents(".groupitem").find("input[type='checkbox']").prop("checked",false);
		}
	});
	$("input[name='tSrc']").click(function(){
		if(!$(this).prop("checked")){
			$(this).parents("span").find("input[type='checkbox']").prop("checked",false);
		}
	});
	$(".tsrcCheck").click(function(){
		var n=$(this).prop("name");
		if($("input[name='"+n+"']:checked").length>0){
			$(this).parents("span").children("input[name='tSrc']").prop("checked",true);
		}
	});
	$("input[name='spClause']").click(function(){
		check_spClause();
	});
	$("input[name='costCenter']").click(function(){
		check_costCenter();
	});
	if("${IOBaseInfo.editType}".length>0){
		setRedioToDefalut("signboard","${ioclists.signboard}");
		setRedioToDefalut("cluster","${ioclists.cluster}");
		setRedioToDefalut("signboard","${ioclists.signboard}");
		if("${ioclists.clustereffect}".length>0){
			 checkCEF("${ioclists.cluster}");
		}
		setCheckboxToDefalut("clustereffect","${ioclists.clustereffect}");
		setRedioToDefalut("staffAttendance","${ioclists.staffAttendance}");
		setRedioToDefalut("operatingPlace","${ioclists.operatingPlace}");
		setRedioToDefalut("OE","${ioclists.OE}");
		setRedioToDefalut("income","${ioclists.income}");
		setRedioToDefalut("costexpend","${ioclists.costexpend}");
		setRedioToDefalut("intervieweeType_1","${ioclists.intervieweeType_1}");
		setRedioToDefalut("intervieweeType_2","${ioclists.intervieweeType_2}");
		setRedioToDefalut("intervieweeType_3","${ioclists.intervieweeType_3}");
		setRedioToDefalut("intervieweeType_4","${ioclists.intervieweeType_4}");
		setRedioToDefalut("intervieweeType_5","${ioclists.intervieweeType_5}");
		setRedioToDefalut("intervieweeType_6","${ioclists.intervieweeType_6}");
		setCheckboxToDefalut("operating","${ioclists.operating}");
		setCheckboxToDefalut("WC","${ioclists.WC}");
		
		setCheckboxToDefalut("iProgress","${ioclists.iProgress}");
		setCheckboxToDefalut("iMotivation","${ioclists.iMotivation}");
		setCheckboxToDefalut("iBarrier","${ioclists.iBarrier}");
		setCheckboxToDefalut("rBarrier","${ioclists.rBarrier}");
		setCheckboxToDefalut("eBarrier","${ioclists.eBarrier}");
		setCheckboxToDefalut("tSrc","${ioclists.tSrc}");
		setCheckboxToDefalut("tSrcExist","${ioclists.tSrcExist}");
		setCheckboxToDefalut("tSrcEmp","${ioclists.tSrcEmp}");
		setCheckboxToDefalut("tSrcMA","${ioclists.tSrcMA}");
		
		setRedioToDefalut("spI3", "${ioclists.spI3}");
		setRedioToDefalut("spImportant", "${ioclists.spImportant}");
		setRedioToDefalut("spSOE", "${ioclists.spSOE}");
		setRedioToDefalut("spClause", "${ioclists.spClause}");
		
		setRedioToDefalut("costCenter", "${ioclists.costCenter}");
		setRedioToDefalut("costCenterStatus", "${ioclists.costCenterStatus}");
		
		$(".RTCheckText").each(function(){
			if($(this).find("input[type='text']").val().length>0){
				$(this).show();
			}
		});
	}
	
	check_spClause();
	check_costCenter();
});
</script>
	<div style="width: 100%;">
	<table class="formProj">
		<tr>
			<th colspan="4">經濟部<span>${IOyear}</span>年陸資在台事業訪視紀錄表</th>
		</tr>
		<tr>
			<td class="trRight" style="width: 14%;"><span style="color: red;">*</span>查訪日期：</td>
			<td colspan="3">
				<input id="singledate" type="text" name="reportdate" value="${ibfn:addSlash(ioclists.reportdate)}"/>	
				<input type="hidden" name="interviewStatus" value="1"/>
			</td>
		</tr>
		<tr>
			<td class="trRight">聯絡電話：${fn:substringAfter(telLast,'#')}</td>
			<td style="width: 40%;">
				<c:set var="tel" value="${fn:split(ioclists.telNo,'-')}"></c:set>
				<c:set var="telLast" value="${tel[2]}"></c:set>
				<c:if test="${fn:indexOf(tel[2],'#')!=-1}">
					<c:set var="telLast" value="${fn:substringBefore(tel[2],'#')}"></c:set>
					<c:set var="telEx" value="${fn:substringAfter(tel[2],'#')}"></c:set>
				</c:if>
				<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${tel[0]}"/><label>-</label>
				<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${tel[1]}"/><label>-</label>
				<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${telLast}"/><label>&nbsp;分機&nbsp;</label>
				<input type="text" style="width: 50px;" class="telNo optionalText" value="${telEx}"/>
				<input type="hidden" value="" name="telNo" />
			</td>
			<td class="trRight">手機：</td>
			<td>
				<c:set var="cellphone" value="${fn:split(ioclists.cellphone,'-')}"></c:set>
				<input type="text" style="width: 50px;" maxlength="4" class="cellphone" value="${cellphone[0]}"/><label>-</label>
				<input type="text" style="width: 50px;" maxlength="3" class="cellphone" value="${cellphone[1]}"/><label>-</label>
				<input type="text" style="width: 50px;" maxlength="3" class="cellphone" value="${cellphone[2]}"/>
				<input type="hidden" value="" name="cellphone" />
			</td>
		</tr>
		<tr>
			<td class="trRight">商業司登記：</td>
			<td colspan="3" id="getAddr">
				<jsp:include page="/includes/twaddr.jsp" flush="true">
					<jsp:param value="bcity" name="City"/>
					<jsp:param value="btown" name="Town"/>
					<jsp:param value="baddr" name="Addr"/>
					<jsp:param value="a" name="fin"/>
					<jsp:param value="${ioclists.baddr}" name="AddrStr"/>
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td class="trRight">實際營運地址：</td>
			<td colspan="3" id="setAddr">
				<jsp:include page="/includes/twaddr.jsp" flush="true">
					<jsp:param value="City" name="City"/>
					<jsp:param value="Town" name="Town"/>
					<jsp:param value="Addr" name="Addr"/>
					<jsp:param value="x" name="fin"/>
					<jsp:param value="${ioclists.Addr}" name="AddrStr"/>
				</jsp:include>
				<br>
				<div style="text-align: right;">
					<input type="button" value="帶入商業司地址" class="btn_class_opener" onclick="setAddr()">
				</div>
			</td>
		</tr>
		<tr>
			<th colspan="4">訪查初視情形</th>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">營運狀況：</td>
			<td colspan="3">
				<span class="basetable"><input type="checkbox" name="operating" value="1" id="OP1"/><label for="OP1">1.營業中</label></span>
				<span class="basetable"><input type="checkbox" name="operating" value="2" id="OP2"/><label for="OP2">2.新設立</label></span>
				<span class="basetable"><input type="checkbox" name="operating" value="3" id="OP3"/><label for="OP3">3.尚未開業</label></span>
				<span class="basetable"><input type="checkbox" name="operating" value="4" id="OP4"/><label for="OP4">4.停業中</label></span>
				<br/><span class="basetable"><input type="checkbox" name="operating" value="5" id="OP5"/><label for="OP5">5.已變更登記遷入，惟現場無所營事業必須之設備及存貨</label></span>
				<br/><span class="basetable"><input type="checkbox" name="operating" value="6" id="OP6"/><label for="OP6">6.該地址無此公司</label></span>
				<span class="basetable"><input type="checkbox" name="operating" value="7" id="OP7"/><label for="OP7">7.異常營業</label></span>
				<span class="basetable">
					<input type="checkbox" name="operating" value="999" id="OP999" class="RTCheckRadio"/><label for="OP999">8.其他</label>&nbsp;
					<span  class="RTCheckText" style="display: none;"><input type="text" name="operatingTypeNote" style="width: 400px;" value="${ioclists.operatingTypeNote}"/></span>
				</span>
			</td>
		</tr>
		<tr>
			<td class="trRight">招牌：</td>
			<td colspan="3">
				<span class="basetable"><input type="radio" name="signboard" value="0" id="SB1" class="defClass"/><label for="SB1">1.無</label></span>
				<span class="basetable"><input type="radio" name="signboard" value="1" id="SB2"/><label for="SB2">2.有</label></span>
			</td>
		</tr>
		<tr>
			<td class="trRight">群聚：</td>
			<td colspan="3">
				<span class="basetable">陸資企業群聚現象：<input type="radio" name="cluster" value="0" id="CEFa" class="defClass CEF"/><label for="CEFa">1.無</label></span>
				<span class="basetable">
					<input type="radio" name="cluster" value="1" id="CEFb" class="CEF"/><label for="CEFb">2.有</label>
					<input type="checkbox" name="clustereffect" value="0" id="CEF0" style="display: none;" class="defClass"/>
					<span class="basetable RCBox" style="display: none;">
						<input type="checkbox" name="clustereffect" value="1" id="CEF1"/><label for="CEF1">a.同址</label>
						<input type="checkbox" name="clustereffect" value="2" id="CEF2"/><label for="CEF2">b.同大樓</label>
						<input type="checkbox" name="clustereffect" value="3" id="CEF3"/><label for="CEF3">c.隔壁</label>
					</span>
				</span>
			</td>
		</tr>
		<tr>
			<td class="trRight">員工上班情形：</td>
			<td colspan="3">
				<span class="basetable"><input type="radio" name="staffAttendance" value="0" id="SA1" class="defClass"/><label for="SA1">1.無</label></span>
				<span class="basetable"><input type="radio" name="staffAttendance" value="1" id="SA2"/><label for="SA2">2.有</label></span>
			</td>
		</tr>
		<tr>
			<th colspan="4">書面確認事項</th>
		</tr>
		<tr>
			<td class="trRight">與問卷勾稽：</td>
			<td colspan="3">
			<span class="basetable" style="line-height: 20px;">
				<input type="checkbox" value="0" name="WC" id="WC0"/>
				<label for="WC0">前一年度會計師財務簽證報告書或營利事業所得稅結算申報書，如新設無前述資料，<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;則為最近一期國稅局營業人銷售額與稅額申報書（401）。					
				</label>
			</span><br>
			<span class="basetable">
				<input type="checkbox" value="1" name="WC" id="WC1"/>
				<label for="WC1">台、陸籍員工人數及員工投保勞健保總數資料。</label>
			</span>
			</td>
		</tr>
		<tr>
			<th colspan="4">營業場所</th>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">營運場所：</td>
			<td colspan="3">
				<span class="basetable"><input type="radio" name="operatingPlace" value="1" id="OPP1" class="defClass"/><label for="OPP1">1.辦公室（含廠房）</label></span>
				<span class="basetable"><input type="radio" name="operatingPlace" value="2" id="OPP2"/><label for="OPP2">2.住家（兼辦公室）</label></span>
				<span class="basetable"><input type="radio" name="operatingPlace" value="3" id="OPP3"/><label for="OPP3">3.門市（含辦公室）</label></span>
				<span class="basetable"><input type="radio" name="operatingPlace" value="4" id="OPP4"/><label for="OPP4">4.會計師事務所內</label></span>
				<br/><span class="basetable"><input type="radio" name="operatingPlace" value="5" id="OPP5"/><label for="OPP5">5.商務中心內</label></span>
				<span class="basetable">
					<input type="radio" name="operatingPlace" value="999" id="OPP999" class="RTCheckRadio"/><label for="OPP999">6.其他</label>&nbsp;
					<span class="RTCheckText" style="display: none;"><input type="text" name="OPNote" style="width: 400px;" value="${ioclists.OPNote}"/></span>
				</span>
			</td>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">營運設備：</td>
			<td colspan="3">
				<span class="basetable"><input type="radio" name="OE" value="0" id="OE1" class="defClass"/><label for="OE1">1.無</label></span>
				<span class="basetable">
					<input type="radio" name="OE" value="1" id="OE2" class="RTCheckRadio"/><label for="OE2">2.有</label>
					<span class="RTCheckText" style="display: none;">：<input type="text" name="OENote" style="width: 450px;" value="${ioclists.OENote}"/></span>
				</span>
			</td>
		</tr>
		
		<tr>
			<th colspan="4">特殊身份</th>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">經營業務為I3類：</td>
			<td colspan="3">
				<span class="basetable"><input type="radio" name="spI3" value="1" id="spI31" /><label for="spI31">1.是</label></span>
				<span class="basetable"><input type="radio" name="spI3" value="0" id="spI32" class="defClass"/><label for="spI32">2.否</label></span>
			</td>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">委員會核准之重大投資案：</td>
			<td colspan="3">
				<span class="basetable"><input type="radio" name="spImportant" value="1" id="spImportant1" /><label for="spImportant1">1.是</label></span>
				<span class="basetable"><input type="radio" name="spImportant" value="0" id="spImportant2" class="defClass"/><label for="spImportant2">2.否</label></span>
			</td>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">投資人背景為國企：</td>
			<td colspan="3">
				<span class="basetable"><input type="radio" name="spSOE" value="1" id="spSOE1" /><label for="spSOE1">1.是</label></span>
				<span class="basetable"><input type="radio" name="spSOE" value="0" id="spSOE2" class="defClass"/><label for="spSOE2">2.否</label></span>
			</td>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">公函附帶條件：</td>
			<td colspan="3">
				<span class="basetable"><input type="radio" name="spClause" value="1" id="spClause1" /><label for="spClause1">1.是</label></span>
				<span class="basetable"><input type="radio" name="spClause" value="0" id="spClause2" class="defClass"/><label for="spClause2">2.否</label></span>
			</td>
		</tr>
		<tr class="spClause">
			<td class="trRight" style="white-space: nowrap;">附帶條件內容1：</td>
			<td colspan="3">
				<textarea rows="5" style="width: 99%;" name="spClauseContent1">${ioclists.spClauseContent1}</textarea>
			</td>
		</tr>
		<tr class="spClause">
			<td class="trRight" style="white-space: nowrap;">落實程度1：</td>
			<td colspan="3">
				<textarea rows="5" style="width: 99%;" name="spImplementation1">${ioclists.spImplementation1}</textarea>
			</td>
		</tr>
		<tr class="spClause">
			<td class="trRight" style="white-space: nowrap;">附帶條件內容2：</td>
			<td colspan="3">
				<textarea rows="5" style="width: 99%;" name="spClauseContent2">${ioclists.spClauseContent2}</textarea>
			</td>
		</tr>
		<tr class="spClause">
			<td class="trRight" style="white-space: nowrap;">落實程度2：</td>
			<td colspan="3">
				<textarea rows="5" style="width: 99%;" name="spImplementation2">${ioclists.spImplementation2}</textarea>
			</td>
		</tr>
		
		<tr>
			<th colspan="4">國內投資事業為費用中心</th>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">是否為費用中心：</td>
			<td colspan="3">
				<span class="basetable" style="padding:0;"><input type="radio" name="costCenter" value="1" id="costCenter1" class="defClass"/><label for="costCenter1">1.非費用中心</label></span>
				<span class="basetable" style="padding:0;"><input type="radio" name="costCenter" value="2" id="costCenter2" /><label for="costCenter2">2.否(今年度已改善)</label></span>
				<span class="basetable" style="padding:0;"><input type="radio" name="costCenter" value="3" id="costCenter3" /><label for="costCenter3">3.是</label></span>
				<span class="costCenterStatus">
					(<input type="radio" name="costCenterStatus" value="1" id="costCenterStatus1" /><label for="costCenterStatus1">a.持續維持</label>
				</span>
				<span class="costCenterStatus">
					<input type="radio" name="costCenterStatus" value="2" id="costCenterStatus2" /><label for="costCenterStatus2">b.今年度新增</label>)
				</span>
				<span class="basetable"><input type="radio" name="costCenter" value="4" id="costCenter4" /><label for="costCenter4">4.無法確認</label></span>
			</td>
		</tr>
		<tr class="costCenter">
			<td class="trRight" style="white-space: nowrap;">說明：</td>
			<td colspan="3">
				<textarea rows="5" style="width: 99%;" name="costCenterNote">${ioclists.costCenterNote}</textarea>
			</td>
		</tr>
		
		<tr>
			<th colspan="4">訪問營運概況</th>
		</tr>
		<tr>
			<td class="trRight">投資進度：</td>
			<td colspan="3">
				<span class="basetable"><input type="checkbox" name="iProgress" value="1" id="iProgress1"/><label for="iProgress1">尋找辦公室／據點／建廠</label></span>
				<span class="basetable"><input type="checkbox" name="iProgress" value="2" id="iProgress2"/><label for="iProgress2">尋找商機</label></span>
				<span class="basetable"><input type="checkbox" name="iProgress" value="3" id="iProgress3"/><label for="iProgress3">已正式營運</label></span>
			</td>
		</tr>
		<tr>
			<td class="trRight">投資動機：</td>
			<td colspan="3">
				<span class="basetable"><input type="checkbox" name="iMotivation" value="1" id="iMotivation1"/><label for="iMotivation1">來臺試水溫、尋找投資商機</label></span>
				<span class="basetable"><input type="checkbox" name="iMotivation" value="2" id="iMotivation2"/><label for="iMotivation2">臺灣產品、技術優勢</label></span>
				<span class="basetable"><input type="checkbox" name="iMotivation" value="3" id="iMotivation3"/><label for="iMotivation3">臺灣人才</label></span><br>
				<span class="basetable"><input type="checkbox" name="iMotivation" value="4" id="iMotivation4"/><label for="iMotivation4">臺灣為國際接軌跳板</label></span>
				<span class="basetable"><input type="checkbox" name="iMotivation" value="5" id="iMotivation5"/><label for="iMotivation5">著眼大陸市場商機</label></span>
				<span class="basetable"><input type="checkbox" name="iMotivation" value="6" id="iMotivation6"/><label for="iMotivation6">大陸母公司在臺服務窗口</label></span>
				<span class="basetable"><input type="checkbox" name="iMotivation" value="7" id="iMotivation7"/><label for="iMotivation7">雙方策略聯盟</label></span><br>
				<span class="basetable">
					<input type="checkbox" name="iMotivation" value="8" id="iMotivation8" class="RTCheckRadio"/><label for="iMotivation8">其他</label>
					<span class="RTCheckText" style="display: none;">：<input type="text" name="iMotivationNote" style="width: 450px;" value="${ioclists.iMotivationNote}"/></span>
				</span>
			</td>
		</tr>
		<tr>
			<td class="trRight">投資障礙：</td>
			<td colspan="3">
				<span class="basetable"><input type="checkbox" name="iBarrier" value="1" id="iBarrier1"/><label for="iBarrier1">（1）暫無問題</label></span><br>
				<span class="groupitem">
					<span class="basetable"><input type="checkbox" name="iBarrier" value="2" id="iBarrier2"/><label for="iBarrier2">（2）法規、行政障礙</label></span><br>
						<span style="margin-left: 25px;">
							<span class="basetable"><input type="checkbox" name="rBarrier" value="1" id="rBarrier1"/><label for="rBarrier1">行政程序繁冗</label></span>
							<span class="basetable"><input type="checkbox" name="rBarrier" value="2" id="rBarrier2"/><label for="rBarrier2">陸資限制</label></span>
							<span class="basetable"><input type="checkbox" name="rBarrier" value="3" id="rBarrier3"/><label for="rBarrier3">兩岸法規調和（認證標準不一）</label></span>
							<span class="basetable"><input type="checkbox" name="rBarrier" value="4" id="rBarrier4"/><label for="rBarrier4">人員來臺簽證問題</label></span><br>
						</span>
						<span style="margin-left: 25px;">
							<span class="basetable">
								<input type="checkbox" name="rBarrier" value="5" id="rBarrier5" class="RTCheckRadio"/><label for="rBarrier5">生活不便利</label>
								<span class="RTCheckText" style="display: none;">：<input type="text" name="rBarrierNote" style="width: 450px;" value="${ioclists.rBarrierNote}"/></span>
							</span>
						</span>
				</span>
					<br>
				<span class="groupitem">
					<span class="basetable"><input type="checkbox" name="iBarrier" value="3" id="iBarrier3"/><label for="iBarrier3">（3）投資環境障礙</label></span><br>
					<span style="margin-left: 25px;">
						<span class="basetable"><input type="checkbox" name="eBarrier" value="1" id="eBarrier1"/><label for="eBarrier1">國內反中情緒</label></span>
						<span class="basetable"><input type="checkbox" name="eBarrier" value="2" id="eBarrier2"/><label for="eBarrier2">人力素質</label></span>
						<span class="basetable"><input type="checkbox" name="eBarrier" value="3" id="eBarrier3"/><label for="eBarrier3">勞工聘僱</label></span>
						<span class="basetable"><input type="checkbox" name="eBarrier" value="4" id="eBarrier4"/><label for="eBarrier4">融（增）資</label></span>
					</span><br>
					<span style="margin-left: 25px;">
						<span class="basetable"><input type="checkbox" name="eBarrier" value="5" id="eBarrier5"/><label for="eBarrier5">土地、廠房、辦公室難取得</label></span>
						<span class="basetable"><input type="checkbox" name="eBarrier" value="6" id="eBarrier6"/><label for="eBarrier6">水電問題</label></span>
						<span class="basetable"><input type="checkbox" name="eBarrier" value="7" id="eBarrier7"/><label for="eBarrier7">環評或水土保持</label></span>
					</span>
				</span><br>
					<span class="basetable">
						<input type="checkbox" name="iBarrier" value="4" id="iBarrier4" class="RTCheckRadio"/><label for="iBarrier4">（4）其他</label>
						<span class="RTCheckText" style="display: none;">：<input type="text" name="iBarrierNote" style="width: 450px;" value="${ioclists.iBarrierNote}"/></span>
					</span>
			</td>
		</tr>
		<tr>
			<td class="trRight">國內事業技術來源：</td>
			<td colspan="3">
				<span class="basetable">
					<input type="checkbox" name="tSrc" value="1" id="tSrc1"/><label for="tSrc1">投資方既有技術</label>
					（<input type="checkbox" name="tSrcExist" value="1" id="tSrcExist1" class="tsrcCheck"/><label for="tSrcExist1">陸資</label>
					／<input type="checkbox" name="tSrcExist" value="2" id="tSrcExist2" class="tsrcCheck"/><label for="tSrcExist2">台商</label>）
				</span><br>
				<span class="basetable">
					<input type="checkbox" name="tSrc" value="2" id="tSrc2"/><label for="tSrc2">聘僱人才取得</label>
					（<input type="checkbox" name="tSrcEmp" value="1" id="tSrcEmp1" class="tsrcCheck"/><label for="tSrcEmp1">台灣</label>
					／<input type="checkbox" name="tSrcEmp" value="2" id="tSrcEmp2" class="tsrcCheck"/><label for="tSrcEmp2">大陸</label>
					／<input type="checkbox" name="tSrcEmp" value="3" id="tSrcEmp3" class="tsrcCheck"/><label for="tSrcEmp3">國外</label>）
				</span><br>
				<span class="basetable">
					<input type="checkbox" name="tSrc" value="3" id="tSrc3"/><label for="tSrc3">併購其他企業取得</label>
					（<input type="checkbox" name="tSrcMA" value="1" id="tSrcMA1" class="tsrcCheck"/><label for="tSrcMA1">台商</label>
					／<input type="checkbox" name="tSrcMA" value="2" id="tSrcMA2" class="tsrcCheck"/><label for="tSrcMA2">僑外資</label>
					／<input type="checkbox" name="tSrcMA" value="3" id="tSrcMA3" class="tsrcCheck"/><label for="tSrcMA3">其他海外併購案取得</label>）
				</span><br>
				<span class="basetable">
					<input type="checkbox" name="tSrc" value="4" id="4" class="RTCheckRadio"/><label for="4">其他</label>
					<span class="RTCheckText" style="display: none;">：<input type="text" name="tSrcNote" style="width: 450px;" value="${ioclists.tSrcNote}"/></span>
				</span>
			</td>
		</tr>
		<tr>
			<td class="trRight">主要營業活動：</td>
			<td colspan="3">
				<span class="basetable">主要產品</span>
				<textarea rows="5" style="width: 99%;" name="mainproject">${ioclists.mainproject}</textarea>
				<span class="basetable">產銷狀況：</span>
				<textarea rows="5" style="width: 99%;" name="marketing">${ioclists.marketing}</textarea>
			</td>
		</tr>
		<tr>
			<td class="trRight">新事項規劃：</td>
			<td colspan="3">
				<textarea rows="5" style="width: 99%;" name="newproject">${ioclists.newproject}</textarea>
			</td>
		</tr>
		<tr>
			<td class="trRight">異常狀況彙總：</td>
			<td colspan="3">
				<input id="errorMsg"  class="getDialog" type="button" value="常用異常清單">
				<textarea rows="5" style="width: 99%;" name="errorMsg" >${ioclists.errorMsg}</textarea>
			</td>
		</tr>
		<tr>
			<th colspan="4">訪視備註</th>
		</tr>
		<tr>
			<td colspan="4" style="text-align: center;">
				<div class="basetable" style="text-align: left;"><input id="interviewnote" class="getDialog" type="button" value="常用評語清單"></div>
				<textarea rows="5" style="width: 99%;" name="interviewnote">${ioclists.interviewnote}</textarea>
			</td>
		</tr>
		<tr>
			<th colspan="4">財務資料總評說明</th>
		</tr>
		<tr>
			<td class="trRight">營業收入：</td>
			<td>
				<span class="basetable"><input type="radio" name="income" value="1" id="income1" class="defClass"/><label for="income1">1.有</label></span>
				<span class="basetable"><input type="radio" name="income" value="0" id="income2"/><label for="income2">2.無</label></span>
			</td>
			<td class="trRight">營業成本或費用：</td>
			<td>
				<span class="basetable"><input type="radio" name="costexpend" value="1" id="costexpend1" class="defClass"/><label for="costexpend1">1.有</label></span>
				<span class="basetable"><input type="radio" name="costexpend" value="0" id="costexpend2"/><label for="costexpend2">2.無</label></span>
			</td>
		</tr>
		<tr>
			<th colspan="4">受訪人</th>
		</tr>
		<tr>
			<td class="trRight">受訪人1：</td>
			<td colspan="3">
				<span class="basetable">
					<input type="radio" name="intervieweeType_1" value="1" id="IT_1_1" class="defClass"/><label for="IT_1_1">1.負責人</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_1" value="2" id="IT_1_2"/><label for="IT_1_2">2.員工</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_1" value="999" id="IT_1_999" class="RTCheckRadio"/><label for="IT_1_999">3.其他</label>&nbsp;
					<span class="RTCheckText" style="display: none;"><input type="text" name="intervieweeTypeNote_1" style="width: 400px;" value="${ioclists.intervieweeTypeNote_1}"/></span>
				</span>
				<br/>
				<span class="basetable">姓名：<input type="text" name="intervieweeName_1" style="width: 150px;" value="${ioclists.intervieweeName_1}"/></span>
				<span class="basetable">職稱：<input type="text" name="intervieweeTitle_1" style="width: 150px;" value="${ioclists.intervieweeTitle_1}"/></span>
			</td>
		</tr>
		<tr>
			<td class="trRight">受訪人2：</td>
			<td colspan="3">
				<span class="basetable">
					<input type="radio" name="intervieweeType_2" value="1" id="IT_2_1" class="defClass"/><label for="IT_2_1">1.負責人</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_2" value="2" id="IT_2_2"/><label for="IT_2_2">2.員工</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_2" value="999" id="IT_2_999" class="RTCheckRadio"/><label for="IT_2_999">3.其他</label>&nbsp;
					<span class="RTCheckText" style="display: none;"><input type="text" name="intervieweeTypeNote_2" style="width: 150px;" value="${ioclists.intervieweeTypeNote_2}"/></span>
				</span>
				<br/>
				<span class="basetable">姓名：<input type="text" name="intervieweeName_2" style="width: 150px;" value="${ioclists.intervieweeName_2}"/></span>
				<span class="basetable">職稱：<input type="text" name="intervieweeTitle_2" style="width: 150px;" value="${ioclists.intervieweeTitle_2}"/></span>
			</td>
		</tr>
		<tr>
			<td class="trRight">受訪人3：</td>
			<td colspan="3">
				<span class="basetable">
					<input type="radio" name="intervieweeType_3" value="1" id="IT_3_1" class="defClass"/><label for="IT_3_1">1.負責人</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_3" value="2" id="IT_3_2"/><label for="IT_3_2">2.員工</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_3" value="999" id="IT_3_999" class="RTCheckRadio"/><label for="IT_3_999">3.其他</label>&nbsp;
					<span class="RTCheckText" style="display: none;"><input type="text" name="intervieweeTypeNote_3" style="width: 150px;" value="${ioclists.intervieweeTypeNote_3}"/></span>
				</span>
				<br/>
				<span class="basetable">姓名：<input type="text" name="intervieweeName_3" style="width: 150px;" value="${ioclists.intervieweeName_3}"/></span>
				<span class="basetable">職稱：<input type="text" name="intervieweeTitle_3" style="width: 150px;" value="${ioclists.intervieweeTitle_3}"/></span>
			</td>
		</tr>
		<tr>
			<td class="trRight">受訪人4：</td>
			<td colspan="3">
				<span class="basetable">
					<input type="radio" name="intervieweeType_4" value="1" id="IT_4_1" class="defClass"/><label for="IT_4_1">1.負責人</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_4" value="2" id="IT_4_2"/><label for="IT_4_2">2.員工</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_4" value="999" id="IT_4_999" class="RTCheckRadio"/><label for="IT_4_999">3.其他</label>&nbsp;
					<span class="RTCheckText" style="display: none;"><input type="text" name="intervieweeTypeNote_4" style="width: 150px;" value="${ioclists.intervieweeTypeNote_4}"/></span>
				</span>
				<br/>
				<span class="basetable">姓名：<input type="text" name="intervieweeName_4" style="width: 150px;" value="${ioclists.intervieweeName_4}"/></span>
				<span class="basetable">職稱：<input type="text" name="intervieweeTitle_4" style="width: 150px;" value="${ioclists.intervieweeTitle_4}"/></span>
			</td>
		</tr>
		<tr>
			<td class="trRight">受訪人5：</td>
			<td colspan="3">
				<span class="basetable">
					<input type="radio" name="intervieweeType_5" value="1" id="IT_5_1" class="defClass"/><label for="IT_5_1">1.負責人</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_5" value="2" id="IT_5_2"/><label for="IT_5_2">2.員工</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_5" value="999" id="IT_5_999" class="RTCheckRadio"/><label for="IT_5_999">3.其他</label>&nbsp;
					<span class="RTCheckText" style="display: none;"><input type="text" name="intervieweeTypeNote_5" style="width: 150px;" value="${ioclists.intervieweeTypeNote_5}"/></span>
				</span>
				<br/>
				<span class="basetable">姓名：<input type="text" name="intervieweeName_5" style="width: 150px;" value="${ioclists.intervieweeName_5}"/></span>
				<span class="basetable">職稱：<input type="text" name="intervieweeTitle_5" style="width: 150px;" value="${ioclists.intervieweeTitle_5}"/></span>
			</td>
		</tr>
		<tr>
			<td class="trRight">受訪人6：</td>
			<td colspan="3">
				<span class="basetable">
					<input type="radio" name="intervieweeType_6" value="1" id="IT_6_1" class="defClass"/><label for="IT_6_1">1.負責人</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_6" value="2" id="IT_6_2"/><label for="IT_6_2">2.員工</label>
					&nbsp;&nbsp;<input type="radio" name="intervieweeType_6" value="999" id="IT_6_999" class="RTCheckRadio"/><label for="IT_6_999">3.其他</label>&nbsp;
					<span class="RTCheckText" style="display: none;"><input type="text" name="intervieweeTypeNote_6" style="width: 150px;" value="${ioclists.intervieweeTypeNote_6}"/></span>
				</span>
				<br/>
				<span class="basetable">姓名：<input type="text" name="intervieweeName_6" style="width: 150px;" value="${ioclists.intervieweeName_6}"/></span>
				<span class="basetable">職稱：<input type="text" name="intervieweeTitle_6" style="width: 150px;" value="${ioclists.intervieweeTitle_6}"/></span>
			</td>
		</tr>
		
		<tr>
			<th colspan="4">關注項目</th></tr>
		<tr>
			<td colspan="4" style="text-align: center;">
				<textarea rows="5" style="width: 99%;" name="focusItems">${ioclists.focusItems}</textarea></td></tr>		
		
		<tr>
			<th colspan="4">備註</th>
		</tr>
		<tr>
			<td colspan="4" style="text-align: center;">
				<textarea rows="5" style="width: 99%;" name="note">${ioclists.note}</textarea>
			</td>
		</tr>
		<tr>
			<th colspan="4">訪視人員</th>
		</tr>
		<tr>
			<td colspan="4" style="text-align: center;">
				<textarea rows="5" style="width: 99%;" name="interviewer">${ioclists.interviewer}</textarea>
			</td>
		</tr>
	</table>
</div>
