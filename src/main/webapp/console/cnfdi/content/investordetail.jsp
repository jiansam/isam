<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>

<script>
$(function() {
    $("#tabs").tabs();
    setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
	setRedioToDefalut("isFilled","${ofiiobean.isFilled}");
	setCheckboxToDefalutWithEmpty("BG1","${bgs.BG1}");
	setCheckboxToDefalutWithEmpty("BG2","${bgs.BG2}");
	$("#saveI").click(function(){
		$("#Updatedetail").submit();
	});
	$("#saveBG").click(function(){
		$("#Updatebg").submit();
	});
	$("input[name='BG1']").change(function(){
		checkBG("BG1",$(this));
	});
	$("input[name='BG2']").change(function(){
		checkBG("BG2",$(this));
	});
});
function checkBG(name,$item){
	var val=$item.val();
	if($item.is(":checked")&&val!=""&&val!="0"){
		$("input[name='"+name+"'][value=''],input[name='"+name+"'][value='0']").prop("checked",false);
	}else if($item.is(":checked")&&(val=="" || val=="0")){
		$("input[name='"+name+"']").prop("checked",false);
		$("input[name='"+name+"'][value='"+val+"']").prop("checked",true);
	}else if($("input[name='"+name+"']:checked").length==0){
		alert("至少須選擇一項，如要修改，請直接選擇未填之外的選項。");
		$("input[name='"+name+"'][value='']").prop("checked",true);
	}
}

</script>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;投資人資料&nbsp;]</span>&nbsp;&nbsp;
					<span  style="float: right ;">
						<a href="<c:url value='/console/cnfdi/listinvestor.jsp?fbtype=b'/>" class="btn_class_opener">返回列表</a>
					</span>
				</legend>
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
					<table style="width: 100%;">
						<tr>
							<td style="text-align: right;width: 10%;">投資人：</td>
							<td colspan="5">${ofiiobean.cname}</td>
						</tr>
						<tr>
							<td style="text-align: right;width: 10%;">英文名：</td>
							<td colspan="5">${ofiiobean.ename}</td>
						</tr>
						<tr>
							<td style="text-align: right;width: 10%;">資金類型：</td>
							<td colspan="5">${optmap.inSrc[ofiiobean.inrole]}</td>
						</tr>
					</table>					
				</div>
				<div id="tabs" style="font-size: 16px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">基本資料</a></li>
					    <li><a href="#tabs-3">母公司或關聯企業及受益人資訊</a></li>
					    <li><a href="#tabs-2">背景資料</a></li>
					    <li><a href="#tabs-4">架構圖</a></li>
					    <li><a href="#tabs-5">投資案</a></li>
					    <li><a href="#tabs-6">代理人資料</a></li>
					  </ul>
					  <div id="tabs-1">
							<jsp:include page="/console/cnfdi/content/investorbase.jsp" flush="true" />
					  </div>
					  <div id="tabs-3">
						<jsp:include page="/console/cnfdi/content/investorrelated.jsp" flush="true" />
					  </div>
					  <div id="tabs-2">
							<jsp:include page="/console/cnfdi/content/investorbg.jsp" flush="true" />
					  </div>
					  <div id="tabs-4">
						<jsp:include page="/console/cnfdi/content/investorstructural.jsp" flush="true" />
					  </div>
					  <div id="tabs-5">
						<jsp:include page="/console/cnfdi/content/investorcase.jsp" flush="true" />
					  </div>
					  <div id="tabs-6">
						<jsp:include page="/console/cnfdi/content/investoragentinfos.jsp" flush="true" />
					  </div>
				</div>
			</fieldset>
		</div>