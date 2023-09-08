<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/setDefaultChecked.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/yearmonthhelper.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/telfmthelper.js'/>"></script>

<script type="text/javascript" src="<c:url value='/js/jquery.ui.widget.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.iframe-transport.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.fileupload.js'/>"></script>

<script type="text/javascript">
$(function(){
	$("#myInsert").click(function(){
		var date=$.trim($("#singledate").val());
		if($("input[name='rBarrier']:checked").length>0){
			$("input[name='iBarrier'][value='2']").prop("checked",true)
		}
		if($("input[name='eBarrier']:checked").length>0){
			$("input[name='iBarrier'][value='3']").prop("checked",true);
		}
		if(date.length<=0||/(\d){3}\/(\d){2}\/(\d){2}/.test(date)===false){
			alert("日期為必填欄位，且格式須為民國年/月/日，如103/01/01。");
			$("#singledate").focus();
		}else if(date.substring(0,3)!=="${IOyear}"){
			alert("日期年度須為${IOyear}年");
			$("#singledate").focus();
		}else if(parseInt($("input[name='cluster']:checked").val(),10)===1 && $(".RCBox input[name='clustereffect']:checked").length===0){
			alert("群聚現象選有時，必須至少選擇一項群聚方式選項。");
			$("input[name='cluster'][value='1']").focus();
		}else if($("input[name='iBarrier'][value='2']").prop("checked") && $("input[name='rBarrier']:checked").length===0){
			alert("勾選法規、行政障礙時，必須至少選擇所屬之一選項。");
			$("input[name='iBarrier'][value='2']").focus();
		}else if($("input[name='iBarrier'][value='3']").prop("checked") && $("input[name='eBarrier']:checked").length===0){
			alert("勾選投資環境障礙時，必須至少選擇所屬之一選項。");
			$("input[name='iBarrier'][value='3']").focus();
		}else if(telFmtToText($(".telNo"),$("input[name='telNo']"))===false||telFmtToText($(".cellphone"),$("input[name='cellphone']"))===false){
			return false;
		}else{
			$("input[name^='intervieweeName']").each(function(){
				var $checkitem=$(this).parents("td").find("input[name^='intervieweeType']:checked");
				if($.trim($(this).val()).length<=0&&$checkitem.val()!="999"){
					$checkitem.prop("checked",false);
				}
			});
			if($.trim($("input[name='Addr']").val()).length<=0){
				$("select[name='City']").remove();
				$("select[name='Town']").remove();
			}
			if($.trim($("input[name='baddr']").val()).length<=0){
				$("select[name='bcity']").remove();
				$("select[name='btown']").remove();
			}
			if(parseInt($("input[name='cluster']:checked").val(),10)===1){
				$("input[name='clustereffect'][value='0']").prop("checked",false);
			}
			$("form").submit();
		}
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
function setListenerRT($item){
	/*選擇其他時，Show出後面的TEXT*/
	$item.each(function(){
		var name=$(this).prop("name");
		var $gp=$("input[name='"+name+"']");
		$gp.click(function(){
			if($(this).hasClass("RTCheckRadio")){
				if($(this).prop("checked")){
					showRTText($(this));
				}else{
					$(this).parents("span").find(".RTCheckText").children().val("");
					$(this).parents("span").find(".RTCheckText").hide();
					$(this).prop("checked",false);
				}
			}else{
				if($gp.is("input[type=radio]")){
					$(this).parents("span").find(".RTCheckText").children().val("");
					$(this).parents("span").find(".RTCheckText").hide();
				}
			}
		});
	});
}
function setListenerRTText($item){
	/*$(".RTCheckText input[type='text']")*/
	/*驗證其他後面的TEXT是否有值*/
	$item.each(function(){
		var name=$(this).parents("span").find(".RTCheckRadio").prop("name");
		var $gp=$(this).parents("span").find(".RTCheckRadio");
		$(this).blur(function(){
			if($.trim($(this).val()).length==0){
				alert("選擇此選項時，本欄須填值，請重新選取。");
				if($gp.is("input[type=radio]")){
					$("input[name='"+name+"']:first").prop("checked",true);
				}else{
					$gp.prop("checked",false);
				}
				$(this).parent().hide();
			}
		});
	});
}
function showRTText($item){
	$item.parent().find(".RTCheckText").show();
	var $RTText=$item.parent().find(".RTCheckText").children("input");
	$RTText.focus();
}
function setListenerCEF(){
	/*設定陸資企業群聚現象*/
	$(".CEF").click(function(){
		var val=$(this).val();
		checkCEF(val);
	});
}
function checkCEF(val){
	/*設定陸資企業群聚現象*/
		if(val!=0){
			$(".RCBox").show();
			$("#CEF0").prop("checked",false);
		}else{
			$(".RCBox").hide();
			$("input[name='clustereffect']").prop("checked",false);
			$("#CEF0").prop("checked",true);
		}
}

function check_spClause(){
	if($('#myform input[name=spClause]:checked').val() == '1'){
		$(".spClause").show();
	}else{
		$(".spClause").hide();
		$(".spClause textarea").val('');
	}
}

function check_costCenter(){
	if($('#myform input[name=costCenter]:checked').val() == '2' ||
		$('#myform input[name=costCenter]:checked').val() == '3' ||
		$('#myform input[name=costCenter]:checked').val() == '4'){
		$(".costCenter").show();
	}else{
		$(".costCenter").hide();
		$(".costCenter textarea").val('');
	}
	
	if($('#myform input[name=costCenter]:checked').val() == '3'){
		$(".costCenterStatus").show();
	}else{
		$("#myform input[name=costCenterStatus]").attr('checked', false);
		$(".costCenterStatus").hide();		
	}
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
			<span style="color:#F30;"><strong>&nbsp;${typeStr}訪視紀錄&nbsp;</strong>&nbsp;</span>
		</legend>
		<c:if test="${empty IOBaseInfo.editType}">
			<div>
				<jsp:include page="/console/interviewone/content/surveymenu.jsp" flush="true">
					<jsp:param value="2" name="pos"/>
				</jsp:include>
			</div>
		</c:if>
		<div>
			<form id="myform" action="<c:url value='/console/interviewone/edititem.jsp'/>" method="post">
				<!-- 基本資料 -->
				<jsp:include page="/console/interviewone/content/sysBaseInfo.jsp" flush="true" />
				<!-- 問卷表單 -->
				<jsp:include page="/console/interviewone/content/interviewone.jsp" flush="true" />
				<input type="hidden" value="${IOBaseInfo.editType}" name="editType">
				<div style="text-align: center;">
					<input type="button" style="margin-right:15px;" value="管理檔案" class="uploadItem btn_class_Approval"
						   alt="${IOBaseInfo.qNo}_${IOBaseInfo.investNo}_${IOBaseInfo.reinvestNo==null?'0':IOBaseInfo.reinvestNo}" >
					<input type="button" id="myInsert" class="btn_class_Approval" value="確認${typeStr}"/>
				</div>
			</form>
		</div>
	</fieldset>
	<div id="test"></div>
	<div id="uploadform"></div>
</div>