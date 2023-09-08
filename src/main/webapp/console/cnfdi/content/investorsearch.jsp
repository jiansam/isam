<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(function() {
	setDefalutOptionByClass($(".df"));
	setCheckboxToDefalut("BG1","${ofiIOterms.BG1}");
	setCheckboxToDefalut("BG2","${ofiIOterms.BG2}");
	setCheckboxToDefalut("AndOr1","${ofiIOterms.AndOr1}");
	setCheckboxToDefalut("AndOr2","${ofiIOterms.AndOr2}");
	$("#clearbtn").click(function(){
		setDefalutOptionByClass($(".df"));
		$("form :input[type='text']").val("");
		$("form :input[type='checkbox']").prop("checked",false);
		$("#cnCode").select2("val","");
		$("#nation").select2("val","");
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
		<form class="searchForm" action='<c:url value="/console/cnfdi/listinvestor.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td class="tdRight">陸資案號：</td>				
						<td><input type="text" maxlength="6" name="investNo" value="${ofiIOterms.investNo }"/></td>				
						<td class="tdRight">統一編號：</td>				
						<td><input type="text" name="IDNO" maxlength="8"  value="${ofiIOterms.IDNO }"/></td>				
					</tr>
					<tr>
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;國內事業名稱：</td>				
						<td><input type="text" name="company" style="width:230px;" value="${ofiIOterms.company}"/></td>				
						<td class="tdRight" >&nbsp;&nbsp;&nbsp;&nbsp;投資人：</td>				
						<td><input type="text" name="investor" style="width:230px;"  value="${ofiIOterms.investor}"/></td>			
					</tr>
					<tr>
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;背景1-黨政軍：</td>				
						<td>
							<input type="checkbox" name="BG1" value="1" id="bg1x1"><label for="bg1x1">黨</label>
							<input type="checkbox" name="BG1" value="2" id="bg1x2"><label for="bg1x2">政</label>
							<input type="checkbox" name="BG1" value="3" id="bg1x3"><label for="bg1x3">軍</label>
							<input type="checkbox" name="BG1" value="0" id="bg1x0"><label for="bg1x0">否</label>
							<input type="checkbox" name="BG1" value="99" id="bg1x99"><label for="bg1x99">未填</label>
						</td>
						<td colspan="2">
							（<input type="radio" name="AndOr1" value="1" id="AndOr11"><label for="AndOr11">AND</label>
							<input class="df" type="radio" name="AndOr1" value="" id="AndOr1"><label for="AndOr1">OR</label>）
						</td>			
					</tr>
					<tr>
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;背景2-央企政府出資：</td>				
						<td>
							<input type="checkbox" name="BG2" value="1" id="bg2x1"><label for="bg2x1">央企</label>
							<input type="checkbox" name="BG2" value="2" id="bg2x2"><label for="bg2x2">政府出資</label>
							<input type="checkbox" name="BG2" value="0" id="bg2x0"><label for="bg2x0">否</label>
							<input type="checkbox" name="BG2" value="99" id="bg2x99"><label for="bg2x99">未填</label>
						</td>
						<td colspan="2">
							（<input type="radio" name="AndOr2" value="1" id="AndOr21"><label for="AndOr21">AND</label>
							<input class="df" type="radio" name="AndOr2" value="" id="AndOr2"><label for="AndOr2">OR</label>）
						</td>			
					</tr>
					<tr>
						<td class="tdRight">國別：</td>				
						<td colspan="3">
							<jsp:include page="/includes/countryoption.jsp" flush="true">
								<jsp:param value="${ofiIOterms.nation}" name="nationDef"/>
								<jsp:param value="${ofiIOterms.cnCode}" name="cnCodeDef"/>
							</jsp:include>
						</td>				
					</tr>
				</table>
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;母公司或關連企業及受益人名稱：</td>				
						<td><input type="text" name="investorXRelated" style="width:700px;" value="${ofiIOterms.investorXRelated}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">母公司或關連企業及受益人國別：</td>				
						<td colspan="3">
							<jsp:include page="/includes/countryoption2.jsp" flush="true">
								<jsp:param value="${ofiIOterms.relatedNation}" name="nationDef"/>
								<jsp:param value="${ofiIOterms.relatedCnCode}" name="cnCodeDef"/>
							</jsp:include>
						</td>				
					</tr>	
					
					<tr>
						<td class="tdRight"  colspan="4" style="text-align: right;">
							<input type="hidden" name="fbtype" value="b"/>
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input id="clearbtn" type="button" class="btn_class_opener" value="清空"/>
							<input id="loadbtn" type="button" class="btn_class_opener" value="下載"/>
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