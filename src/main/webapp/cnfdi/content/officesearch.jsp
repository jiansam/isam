<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function() {
	setDefalutOptionByClass($(".df"));
	var status = "${Info.status}";
	setRedioToDefalut("status",status);
	if(status===''){
		$("#status_4").prop("checked",true);
	}

	$("#clearbtn").click(function(){
		setDefalutOptionByClass($(".df"));
		$("form :input[type='text']").val("");
		$("form :input[type='checkbox']").prop("checked",false);
	
		if(parseInt($(this).val(),10)===142){
		
		}else{
			
		}
		setRedioToDefalut("status",status);
		$("#status_4").prop("checked",true);
	});
	$("#loadbtn").click(function(){
		$(".searchForm #doThing").html('<input type="text" name="doThing" value="loadExcel"/>');
		$(".searchForm").submit();
		$(".searchForm #doThing").empty();
	});
});
</script>
<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;查詢條件&nbsp;]</span>
	</legend>
<div>
	<div style="float: left;width: 100%;">
		<form class="searchForm" action='<c:url value="/cnfdi/showofficelist.jsp"/>' method="post">
		
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
							
						<td class="tdLeft">統一編號：</td>				
						<td><input type="text" name="banno" maxlength="8"  value="${Info.banno }"/></td>
							<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;國內事業名稱：</td>				
						<td><input type="text" name="company" style="width:230px;" value="${Info.company }"/></td>					
					</tr>
					<tr>
							<td class="tdLeft" colspan="4">公司狀況(單選)：
							<input type="radio" name="status" value="1" id="status_1"><label for="status_1">營業中</label>
							<input type="radio" name="status" value="2" id="status_2"><label for="status_2">停業中</label>
							<input type="radio" name="status" value="3" id="status_3"><label for="status_3">解散或撤銷</label>
							<input type="radio" name="status" value="" id="status_4"><label for="status_4">全部</label>
								
							</td>			
						
					</tr>
				
					
					
					<tr>
						<td class="tdRight"  colspan="4" style="text-align: right;">
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input id="clearbtn" type="button" class="btn_class_opener" value="清空"/>
							
						</td>
					</tr>
					<tr><td style="display:none;" id="doThing"></td></tr>
			   	</table>
		   	</div>
	   </form>
	</div>
	<div style="clear: both;"></div>	
</div>
</fieldset>