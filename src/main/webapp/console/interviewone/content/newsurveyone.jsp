<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/yearmonthhelper.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/telfmthelper.js'/>"></script>

<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>

<script type="text/javascript" src="<c:url value='/js/jquery.ui.widget.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.iframe-transport.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.fileupload.js'/>"></script>

<script type="text/javascript">
$(function(){
	setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
	//setFormatInputBlur(".numberFmt",2);
	setYearOption($("select[name='year']"));
	setYearUpperDownBound($("select[name='year']"),10);
	inputTextNext("#myform",".nextInput",".skip");
	$( "#tabs" ).tabs({disabled:[0,1,2,3,4], collapsible: true, active: false});
	if("${IOBaseInfo.editType}".length>0){
		setRedioToDefalut("branch","${ioclists.branch}");
		setCheckboxToDefalut("dsFinancial","${ioclists.dsFinancial}");
		setCheckboxToDefalut("OSMode","${ioclists.OSMode}");
		if("${ioclists.dsFinancial}".length>0){
			var ary=[0,1,2,3,4];
			var x ="${ioclists.dsFinancial}".split(",");
			for ( var i = 0; i < x.length; i++) {
				var v=parseInt(x[i],10);
				if($.inArray(v,ary)!=-1){
					ary.splice($.inArray(v,ary), 1);
				}
			}
			$( "#tabs" ).tabs("option","disabled",ary).tabs("option", "active", x[0]);
		}
		$(".RTCheckText").each(function(){
			if($(this).find("input[type='text']").val().length>0){
				$(this).show();
			}
		});
	}
	$(".tabBox").click(function(){
		setTabHidden($(this),$( "#tabs" ));
	});
	setListenerForSum($("input[type='text'][name$='OS']"),$("#TextOS"));
	setListenerForSum($("input[type='text'][name$='Xemp']"),$("#TextXemp"));
	setListenerForBranch();
	
	$("input[type='text'][name^='NP_']").each(function(){
		checkPLStatus($(this));
	});
	$("input[type='text'][name^='NP_']").blur(function(){
		checkPLStatus($(this));
	});
	
	$("#myInsert").click(function(){
		var code_list = "";
		var selected_code = $("#businessIncomeTaxCode").select2("data");
		if(selected_code != null){
			for(var i = 0;i < selected_code.length;i++){
				if(code_list != ""){
					code_list = code_list + "；"; 
				}
				
				code_list = code_list + selected_code[i].text; 
			}
		}
		$("input[name='businessIncomeTaxCode']").val(code_list);
		
		var date=$.trim($("#singledate").val());
		if(date.length<=0||/(\d){3}\/(\d){2}\/(\d){2}/.test(date)===false){
			alert("日期為必填欄位，且格式須為民國年/月/日，如103/01/01。");
			$("#singledate").focus();
		}else if(date.substring(0,3)!=="${IOyear}"){
			alert("日期年度須為${IOyear}年");
			$("#singledate").focus();
		}else if($.trim($("input[name='reporter']").val()).length<=0){
			alert("填表人姓名欄位為必填欄位。");
			$("input[name='reporter']").focus();
		}else if(telFmtToText($(".telNo"),$("input[name='telNo']"))===false||telFmtToText($(".cellphone"),$("input[name='cellphone']"))===false){
			return false;
		}else{
			setTabItemEmpty($( "#tabs" ));
			//telFmtToText($(".telNo"),$("input[name='telNo']"));
			//telFmtToText($(".cellphone"),$("input[name='cellphone']"));
			if($.trim($("input[name='Addr']").val()).length<=0){
				$("select[name='City']").remove();
				$("select[name='Town']").remove();
			}
			$("form").submit();
		}
	});
	
	$("#businessIncomeTaxCode").select2({
		 language: "zh-TW",
		 placeholder: "尚未選擇收入業別",
		 closeOnSelect: false,
		 width: '95%'
	});
	
 	$(".uploadItem").click(function(){
 		var item=$(this).prop("alt");
 		var tmp=item.split("_");
	 	managefiles($(this),tmp[1],tmp.length>2?tmp[2]:'',tmp[0]);
 	});
});
function managefiles($item,investNo,reInvestNo,qNo){
	$.post( "${pageContext.request.contextPath}/includes/fileupload.jsp",
			{
				'investNo':investNo,
				'year':"${IOyear}",
				'reInvestNo':reInvestNo,
				'qNo':qNo
			}, 
			function(data){
						$( "#uploadform" ).html(data); 
						$( "#uploadform" ).dialog({
						      height:400,
						      width:800,
						      modal: true,
						      resizable: false,
						      draggable: false,
						      title:'檔案管理_'+'${IOBaseInfo.cname}'+"("+'${IOBaseInfo.investNo}'+")",
						      close: function( event, ui ) {
						    	  $( "#uploadform" ).html(""); 
						      }
						});
			},"html");
}
</script>

<script type="text/javascript">
function checkPLStatus($item){
	var item=$.trim($item.val());
	if($.trim(item).length>0){
			item=getRemoveCommaVal($item.val()); 
		if(!$.isNumeric(item)){
			$item.val("");
			alert("本欄位必須為數值。");
		}else{
			var x="無";
			if(item<0){
				x="虧損";
			}else if(item>0){
				x="獲利";
			}
			$item.parents("tr").next().find(".PLStatus").text(x);
		}
	}else{
		$item.parents("tr").next().find(".PLStatus").text("無");
	}
}
function setTabHidden($item,$tabs){
	var ary=[0,1,2,3,4];
	var nowtab=parseInt($item.val(),10);
	var TF=$item.prop("checked");
	$("input[name='dsFinancial']:checked").each(function(){
		var x=parseInt($(this).val(),10);
		if($.inArray(x,ary)!=-1){
			ary.splice($.inArray(x,ary), 1);
		}
	});
	if($("input[name='dsFinancial']:checked").length==0){
		$tabs.tabs("option","collapsible",true).tabs("option", "active", false).tabs("option","disabled",ary);
	}else if(TF){
		$tabs.tabs("option","disabled",ary).tabs("option", "active", nowtab).tabs("option","collapsible",false);
	}else{
		$tabs.tabs("option","disabled",ary).tabs("option", "active", $("input[name='dsFinancial']:checked").val()).tabs("option","collapsible",false);
	}
}
function setTabItemEmpty($tabs){
	$("input[name='dsFinancial']").not(":checked").each(function(){
		var x=parseInt($(this).val(),10)+1;
		$tabs.find("#tab-"+x).find("input[type=text]").val("");
	});
}
function setListenerForSum($item,$itemTxt){
	$item.blur(function(){
		var sum=0.00;
		$item.each(function(){
			var item=$.trim($(this).val());
			if($.trim(item).length>0){
 				item=getRemoveCommaVal($(this).val()); 
				if(!$.isNumeric(item)){
					$(this).val("");
					alert("本欄位必須為數值。");
				}else{
					sum+=parseFloat(item,10);
				}
			}
		});
		$itemTxt.text(parseFloat(sum,10).toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,"));
	});
}
function setListenerForBranch(){
	$("input[name='branchcount']").blur(function(){
		var item=$.trim($(this).val());
		if($.trim(item).length>0){
			item=getRemoveCommaVal($(this).val()); 
			if(!$.isNumeric(item)||item<=0){
				$(this).val("");
				alert("本欄位必須填寫大於0的數值。");
				$("#b0").prop("checked",true);
			}else{
				$("#b1").prop("checked",true);
			}
		}else if(!$("#b0").prop("checked")){
			alert("本欄位為空白或0時，請選擇無。");
			$("#b0").prop("checked",true);			
		}
	});
	$("input[name='branch']").click(function(){
		if($(this).val()==0){
			$("input[name='branchcount']").val("");
		}else{
			$("input[name='branchcount']").focus();
		}
	});
}
</script>

<div>
<c:choose>
		<c:when test="${IOBaseInfo.editType eq 'edit'}">
			<c:set var="typeStr" scope="page">編輯</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="typeStr" scope="page">新增</c:set>
		</c:otherwise>
	</c:choose>
	<fieldset>
		<legend class="legendTitle">
			<span style="color:#F30;"><strong>&nbsp;${typeStr}營運問卷&nbsp;</strong>&nbsp;</span>
		</legend>
		<c:if test="${empty IOBaseInfo.editType}">
			<div>
				<jsp:include page="/console/interviewone/content/surveymenu.jsp" flush="true">
					<jsp:param value="3" name="pos"/>
				</jsp:include>
			</div>
		</c:if>
		<div>
			<form id="myform" action="<c:url value='/console/interviewone/edititem.jsp'/>" method="post">
				<!-- 基本資料 -->
				<jsp:include page="/console/interviewone/content/sysBaseInfo.jsp" flush="true" />
				<!-- 問卷表單 -->
				<jsp:include page="/console/interviewone/content/surveyone.jsp" flush="true" />
				<input type="hidden" value="${IOBaseInfo.editType}" name="editType">
				<div style="text-align: center;">
					<input type="button" style="margin-right:15px;" value="管理檔案" class="uploadItem btn_class_Approval" 
						   alt="${IOBaseInfo.qNo}_${IOBaseInfo.investNo}_${IOBaseInfo.reinvestNo==null?'0':IOBaseInfo.reinvestNo}" >
					<input type="button" id="myInsert" class="btn_class_Approval" value="確認${typeStr}"/>
				</div>
			</form>
		</div>
	</fieldset>
	<div id="uploadform"></div>
</div>