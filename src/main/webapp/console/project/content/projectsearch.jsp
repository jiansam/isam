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
    	if($("input[name='state']:checked").length>0){
    		$(".searchForm").submit();
    	}else{
	    	alert("至少需選擇一種以上的管制狀態");
    	}
    });
     setProjStateChecked();
  });
  function setProjStateChecked(){
	  var stateDef="${projTerms.state}";
	  if(stateDef.length>0){
		  var ary=stateDef.split(",");
		  $("input[name='state']").each(function(){
			  var v= $(this).val();
			  if($.inArray(v,ary)!=-1){
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
		<form class="searchForm" action='<c:url value="/console/project/showproject.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td class="tdRight">投資案號：</td>				
						<td><input type="text" maxlength="6" name="investNo" value="${projTerms.investNo}"/></td>				
						<td class="tdRight" >&nbsp;&nbsp;&nbsp;&nbsp;大陸事業名稱：</td>				
						<td><input type="text" name="cnName" style="width:230px;"  value="${projTerms.cnName}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">統一編號：</td>				
						<td><input type="text" name="IDNO" maxlength="8"  value="${projTerms.IDNO}"/></td>				
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;投資人名稱：</td>				
						<td><input type="text" name="investName" style="width:230px;" value="${projTerms.investor}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">管制狀態：</td>				
						<td colspan="3">
							<input type="checkbox" name="state" value="01" id="State01"/><label for="State01">列管</label>&nbsp;
							<input type="checkbox" name="state" value="02" id="State02"/><label for="State02">解除季報</label>&nbsp;
							<input type="checkbox" name="state" value="03" id="State03"/><label for="State03">待確認</label>&nbsp;
							<input type="checkbox" name="state" value="04" id="State04"/><label for="State04">解除列管</label>&nbsp;
						</td>				
					</tr>
					<tr>
						<td class="tdRight">核准期間：</td>
						<td colspan="2">
							<input type="text" id="from" name="from"  value="${projTerms.from}" size="13"/>
							<label for="to">~</label>
							<input type="text" id="to" name="to"  value="${projTerms.to}"  size="13"/>
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
			<li><a href="<c:url value="/console/project/showproject.jsp"/>"><span class="ui-icon ui-icon-play"></span>資料總表</a></li>
			<li><a href="<c:url value="/console/project/showproject.jsp?state=01&state=02"/>"><span class="ui-icon ui-icon-play"></span>專案控管列表</a></li>
			<li><a href="<c:url value="/console/project/showproject.jsp?state=03"/>"><span class="ui-icon ui-icon-play"></span>待確認列表</a></li>
			<li><a href="<c:url value="/console/project/showproject.jsp?alert=Y"/>"><span class="ui-icon ui-icon-play"></span>解除季報提示列表</a></li>
		</ul>
	</div>
	<div style="clear: both;"></div>
	
</div>
</fieldset>