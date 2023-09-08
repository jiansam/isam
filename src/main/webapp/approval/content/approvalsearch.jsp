<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style>
	.ui-datepicker{
		Font-size:13px !important;
		padding: 0.2em 0.2em 0;
	}
	.ui-menu { 
		width: 200px;
		padding: 0.2em 0.2em 0; 
	}
</style>
<script>
  $(function() {
    $( "#quickmenu" ).menu();
    if("${ApprovalTerms.com}"==="2"){
	    setDefaultChecked("${ApprovalTerms.state}","state");
    }
    setDefaultChecked("${ApprovalTerms.com}","com");
    if("${ApprovalTerms.state}".length===0&&"${ApprovalTerms.com}".length===0){
    	$(".stateCheck input:checkbox").prop("checked",true);
    }
    $("#myReset").click(function(){
    	$(".searchForm input[type=text]").each(function(){
    		$(this).val("");
    	});
    	$(".childCheck").prop("checked",false);
    	setDefaultChecked("0","com");
    });
  });
  $(function() {
/* 	  $(".parentCheck").change(function(){
			checkChild($(this));
			$("input[name='com']:checked").prop("checked",false);
			$("#com2").prop("checked",true);
		}); */
		$(".childCheck").change(function(){
			checkMustHave($(this));
			$("input[name='com']:checked").prop("checked",false);
			$("#com2").prop("checked",true);
		});
		$("input[name='com']").change(function(){
			var x =parseInt($(this).val(),10);
			if(x===2){
				$(".childCheck").prop("checked",true);
				$("#State00").prop("checked",true);
			}else{
				$(".childCheck").prop("checked",false);
				$("#State00").prop("checked",false);
			}
		});
  });
  function checkMustHave($thisObj){
		var flag=true;
		var thisCheck=$thisObj.prop("checked");
		if($thisObj.parent().children(".childCheck:checked").length==0){
			alert("請至少選擇列管或解除季報，以免查無資料！");
			$(".childCheck").prop("checked",true);
		}
  }
  /*  function checkChild($thisObj){
		var flag=$thisObj.prop("checked");
		if(flag){
			$thisObj.parent().children(".childCheck").prop("checked",flag);
		}else{
			$thisObj.prop("checked",true);
			alert("至少需選擇列管或解除季報");
		}
  }
   function checkParent($thisObj){
	var flag=true;
	var thisCheck=$thisObj.prop("checked");
	var $parentObj=$thisObj.parent().children(".parentCheck");
	$thisObj.parent().children(".childCheck").each(function(){
		if($(this).prop("checked")!==thisCheck){
			flag=false;
			$parentObj.prop("checked",false);
			return;
		}
	});
	if(flag){
		$parentObj.prop("checked",thisCheck);
	}
  } */
  function setDefaultChecked(checkStr,name){
	  if(checkStr.length>0){
		  var ary=checkStr.split(",");
		  $("input[name='"+name+"']").each(function(){
			  var v= $(this).val();
			  if($.inArray(v,ary)!=-1){
				  $(this).prop("checked",true);
			  }
		  });
	  }	  
}
</script>

<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;查詢條件&nbsp;]</span>
	</legend>
<div>
	<div style="float: left;width: 70%;">
		<form class="searchForm" action='<c:url value="/approval/showapproval.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td class="tdRight">投資案號：</td>				
						<td><input type="text" maxlength="6" name="investNo" value="${ApprovalTerms.investNo}"/></td>				
						<td class="tdRight" >&nbsp;&nbsp;&nbsp;&nbsp;大陸事業名稱：</td>				
						<td><input type="text" name="cnName" style="width:230px;"  value="${ApprovalTerms.cnName}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">統一編號：</td>				
						<td><input type="text" name="IDNO" maxlength="8"  value="${ApprovalTerms.IDNO}"/></td>				
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;投資人名稱：</td>				
						<td><input type="text" name="investName" style="width:230px;" value="${ApprovalTerms.investor}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">管制狀態：</td>				
						<td colspan="3" class="stateCheck">
						<c:if test="${fn:contains(memberUrls,'R0101')&&fn:contains(memberUrls,'R0102')}">
							<input type="radio"  name="com" value="0" id="com0"/><label for="com0">不拘</label>
						</c:if>
						<c:if test="${fn:contains(memberUrls,'R0101')}">
							<input type="radio"  name="com" value="2" id="com2"/><label for="com2">專案</label>（
							<!-- <input type="checkbox" id="State00" class="parentCheck"/><label for="State00">不拘</label>&nbsp; -->
							<input type="checkbox" name="state" value="01" id="State01" class="childCheck"/><label for="State01">列管</label>&nbsp;
							<input type="checkbox" name="state" value="02" id="State02" class="childCheck"/><label for="State02">解除季報</label>）&nbsp;
						</c:if>
						<c:if test="${fn:contains(memberUrls,'R0102')}">
							<input type="radio" name="com" value="1" id="com1"/><label for="com1">承諾</label>
						</c:if>
						</td>				
					</tr>
					<tr>
						<%-- <td class="tdRight">核准期間：</td>
						<td colspan="2">
							<input type="text" id="from" name="from"  value="${projTerms.from}" size="13"/>
							<label for="to">~</label>
							<input type="text" id="to" name="to"  value="${projTerms.to}"  size="13"/>
						</td> --%>
						<td class="tdRight"  colspan="4" style="text-align: right;">
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input id="myReset" type="button" class="btn_class_opener" value="清空"/>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
  	<div style="float: left;width: 25%;padding-left: 15px;">
		<strong style="color:#222;">&nbsp;快速查詢&nbsp;</strong>
		<ul id="quickmenu" style="width: 270px;">
			<c:if test="${fn:contains(memberUrls,'R0101')&&fn:contains(memberUrls,'R0102')}">
		      	<li><a href="<c:url value='/approval/showapproval.jsp'/>"><span class="ui-icon ui-icon-play"></span>資料總表</a></li>
      		</c:if>
      		<c:if test="${fn:contains(memberUrls,'R0101')}">
		      	<li><a href="<c:url value='/approval/showapproval.jsp?state=01&state=02&com=2'/>"><span class="ui-icon ui-icon-play"></span>專案列表</a></li>
      		</c:if>
      		<c:if test="${fn:contains(memberUrls,'R0102')}">
		      	<li><a href="<c:url value='/approval/showapproval.jsp?com=1'/>"><span class="ui-icon ui-icon-play"></span>承諾列表</a></li>
      		</c:if>
		</ul>
	</div>
	<div style="clear: both;"></div>	
</div>
</fieldset>