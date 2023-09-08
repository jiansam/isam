<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
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
    $("#mySearch").click(function(){
    	$(".searchForm").submit();
    });
    setCommitStateChecked();
  });
   function setCommitStateChecked(){
	  var stateDef="${commitTerms.state}";
	  if(stateDef.length>0){
		  var ary=stateDef.split(",");
		  $("input[name='state']").each(function(){
			  var v= $(this).val();
			  if($.inArray(v,ary)>-1){
				  $(this).prop("checked","checked");
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
		<form class="searchForm" action='<c:url value="/console/commit/showcommit.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td class="tdRight">統一編號：</td>				
						<td><input type="text" name="IDNO" maxlength="8"  value="${commitTerms.IDNO}"/></td>				
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;投資人名稱：</td>				
						<td><input type="text" name="investName" style="width:230px;" value="${commitTerms.investor}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">管制類型：</td>				
						<td colspan="3">
							<input type="checkbox" name="state" value="01" id="state01"/><label for="state01">國內相對投資計畫</label>&nbsp;
							<input type="checkbox" name="state" value="02" id="state02"/><label for="state02">改善財務</label>&nbsp;
							<input type="checkbox" name="state" value="03" id="state03"/><label for="state03">資金回饋</label>&nbsp;
							<input type="checkbox" name="state" value="04" id="state04"/><label for="state04">金融服務</label>&nbsp;
							<input type="checkbox" name="state" value="05" id="state05"/><label for="state05">特殊列管</label>&nbsp;
						</td>				
					</tr>
					<tr>
						<td class="tdRight">核准期間：</td>
						<td colspan="2">
							<input type="text" id="from" name="from"  value="${commitTerms.from}" size="13"/>
							<label for="to">~</label>
							<input type="text" id="to" name="to"  value="${commitTerms.to}"  size="13"/>
						</td>
						<td class="tdRight">
							<input type="button" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input type="reset" class="btn_class_opener" value="清空"/>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
   	<div style="float: left;width: 25%;padding-left: 15px;">
		<strong style="color:#222;">&nbsp;快速查詢&nbsp;</strong>
		<ul id="quickmenu" style="width: 270px;">
			<li><a href="<c:url value="/console/commit/showcommit.jsp"/>"><span class="ui-icon ui-icon-play"></span>資料總表</a></li>
			<li><a href="<c:url value="/console/commit/showcommit.jsp?alert=Y"/>"><span class="ui-icon ui-icon-play"></span>提示解除國內相對投資計畫</a></li>
		</ul>
	</div>
	<div style="clear: both;"></div>
	
</div>
</fieldset>