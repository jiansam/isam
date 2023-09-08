<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function() {
	setDefalutOptionByClass($(".df"));
	setCheckboxToDefalutWithEmpty("companytype","${ofiCaseterms.companytype}");
	setCheckboxToDefalut("aduit","${ofiCaseterms.aduit}");
	setCheckboxToDefalut("AndOr","${ofiCaseterms.AndOr}");
	$("#nation").select2("val","");
	$("#clearbtn").click(function(){
		setDefalutOptionByClass($(".df"));
		setCheckboxToDefalutWithEmpty("companytype","");
		$("form :input[type='text']").val("");
		$("form :input[type='checkbox']").prop("checked",false);
		$("#cnCode2").select2("val","");
		$("#nation2").select2("val","");
	
		
		if(parseInt($(this).val(),10)===142){
			$("#cnSelect").show();
			$("#cnSelect2").show();
		}else{
			$("#cnCode").select2("val","");
			$("#cnSelect").hide();
			$("#cnCode2").select2("val","");
			$("#cnSelect2").hide();
		}
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
		<form class="searchForm" action='<c:url value="/cnfdi/listinvestcase.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td class="tdRight">陸資案號：</td>				
						<td><input type="text" maxlength="6" name="investNo" value="${ofiCaseterms.investNo}"/></td>				
						<td class="tdRight">統一編號(含辦事處)：</td>				
						<td><input type="text" name="IDNO" maxlength="8"  value="${ofiCaseterms.IDNO }"/></td>				
					</tr>
					<tr>
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;國內事業名稱(含辦事處)：</td>				
						<td><input type="text" name="company" style="width:230px;" value="${ofiCaseterms.company}"/></td>				
						<td class="tdRight"  width="200px" >&nbsp;&nbsp;&nbsp;&nbsp;投資人名稱(含辦事處)：</td>				
						<td><input type="text" name="investor" style="width:230px;"  value="${ofiCaseterms.investor}"/></td>			
					</tr>
					<tr>
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;投資型態：</td>				
						<td colspan="3">
							<input type="radio" name="companytype" value="1" id="companytypex1" ><label for="companytypex1">分公司或子公司</label>
							<input type="radio" name="companytype" value="2" id="companytypex2"><label for="companytypex2">辦事處</label>
							<input type="radio" name="companytype" value="" id="companytypex3"><label for="companytypex3">全部</label>
						</td>
							
					</tr>
					<tr>
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;母公司或關連企業及受益人名稱(含辦事處)：</td>				
						<td><input type="text" name="investorXRelated" style="width:230px;" value="${ofiCaseterms.investorXRelated}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">母公司或關連企業及受益人國別：</td>				
						<td colspan="3">
							<jsp:include page="/includes/countryoption2.jsp" flush="true">
								<jsp:param value="${ofiCaseterms.relatedNation}" name="nationDef"/>
								<jsp:param value="${ofiCaseterms.relatedCnCode}" name="cnCodeDef"/>
							</jsp:include>
						</td>				
					</tr>					
					<tr>
						<td class="tdRight" rowspan="2">稽核：</td>				
						<td colspan="3">
							<input type="checkbox" name="aduit" value="01" id="aduit01"><label for="aduit01">主管機關專案核准案件</label>
							<input type="checkbox" name="aduit" value="02" id="aduit02"><label for="aduit02">公文附加附款案件</label>
							<input type="checkbox" name="aduit" value="03" id="aduit03"><label for="aduit03">協力機關要求標註關切案件</label>
						</td>			
					</tr>					
					<tr>
						<td colspan="3">
							<input type="checkbox" name="aduit" value="04" id="aduit04"><label for="aduit04">重大投資案件</label>
							<input type="checkbox" name="aduit" value="05" id="aduit05"><label for="aduit05">取得台灣地區不動產案件</label>
							<input type="checkbox" name="aduit" value="06" id="aduit06"><label for="aduit06">異常案件</label>
							<input type="checkbox" name="aduit" value="07" id="aduit07"><label for="aduit07">委員會核准之重大投資案</label>
							<br>
							（<input type="radio" name="AndOr" value="1" id="AndOr1"><label for="AndOr1">AND</label>
							<input class="df" type="radio" name="AndOr" value="" id="AndOr"><label for="AndOr">OR</label>）
						</td>			
					</tr>
					<tr>
						<td class="tdRight"  colspan="4" style="text-align: right;">
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input id="clearbtn" type="button" class="btn_class_opener" value="清空"/>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
	<div style="clear: both;"></div>	
</div>
</fieldset>