<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/js/ajaxRequest.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>"  type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>"  type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>

<script type="text/javascript">
	//107-08-22
	var errlist = '${errMsgXnote}';
	var contextPath = "${pageContext.request.contextPath}";
$(function() {
	setDefalutOptionByClass($(".df"));
	setSelectedToDefalut("year","${ofiterms.dyear}");
	setSelectedToDefalut("iyear","${ofiterms.iyear}");
	setSelectedToDefalut("itvOneYear","${ofiterms.itvOneYear}"); //107-08-03
	setRedioToDefalut("CNFDI","${ofiterms.CNFDI}");
	setRedioToDefalut("reInvest","${ofiterms.reInvest}");
	setRedioToDefalut("sp","${ofiterms.sp}");
	setRedioToDefalut("AndOr","${ofiterms.AndOr}");
	setCheckboxToDefalut("abnormal","${ofiterms.abnormal}");
	setCheckboxToDefalut("issueType","${ofiterms.issueType}");
	$("#clearbtn").click(function(){
		setDefalutOptionByClass($(".df"));
		$("form :input[type='checkbox']").prop("checked",false);
		$("form :input[type='text']").val("");
		$("form textarea").val("");
	});
	
	//107-08-01
// 	var availableTags = JSON.parse('${errMsgXnote}'); // ["A","B"] 
// 	$( "#errMsgXnote" ).autocomplete({
// 	    source: availableTags,
// 	    minLength: 1
// 	});		

	//107-08-22
	  setDialog2("#errMsgXnoteList", 1000, true, "常用清單", false, true)
});

//107-08-22
function empty(id) {
	$(id).val("");
	$(id).attr("readonly", false); //清除選擇，清單可編輯
}

$(function() {
	$("#twsic").click(function(){
		var items=$(this).val();
		var oTable = $("#optItems").dataTable();
		var nNodes = oTable.fnGetNodes();
		if(items.length>0){
			var ary=items.split(",");
			$("input:checked", nNodes).prop("checked",false);
			for(var i=0;i<ary.length;i++){
				$("input[value='"+ary[i]+"']", nNodes).prop("checked",true);
			}
		}
		oTable.fnSort( [[ 1, "desc" ],[2,"asc"] ]);
		$( "#twTmp" ).dialog({
			      height:610,
			      width:800,
			      modal: true,
			      title:"選取營業項目",
			      resizable: false,
			      draggable: false,
			      buttons: {
			        "確定": function() {
			        	var str="";
						$("input:checked", nNodes).each(function(){
							if(str.length>0){
								str+=",";
							}
							str+=$(this).val();
						});
						$("#twsic").text(str);
			          $( this ).dialog( "close" );
			        },
			        "取消": function() {
						$("#twsic").text(items);
						if(items.length>0){
							var ary=items.split(",");
							//$("input:checked", nNodes).prop("checked",false);
							for(var i=0;i<ary.length;i++){
								$("input[value='"+ary[i]+"']", nNodes).prop("checked",true);
							}
						}
			          $( this ).dialog( "close" );
			        }
			      },close:function(event,ui){
			    	  oTable.fnFilter( "" );
			    	  $("input:checked", nNodes).prop("checked",false);
			      }
		});
	});
});
</script>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;查詢條件&nbsp;]</span>
	</legend>
<div>
	<div style="float: left;width: 98%;">
		<form class="searchForm" action='<c:url value="/cnfdi/listapproval.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table  style="width: 98%;padding-left: 2px;">
					<tr>
						<td class="tdRight" style="width: 15%;">陸資案號：</td>				
						<td><input type="text" maxlength="6" name="investNo" value="${ofiterms.investNo}"/></td>				
						<td class="tdRight">統一編號：</td>				
						<td><input type="text" name="IDNO" maxlength="8"  value="${ofiterms.IDNO}"/></td>				
						<td class="tdRight">公司名稱：</td>				
						<td  style="width: 15%;"><input type="text" name="company" style="width:180px;" value="${ofiterms.company}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">核准年度：</td>				
						<td>
							<select name="year">
								<option value="">不拘</option>
								<c:forEach var="y" begin="${ofiterms.syear}" end="${ofiterms.eyear}" varStatus="1">
									<option value="${ofiterms.eyear-y+ofiterms.syear}">${ofiterms.eyear-y+ofiterms.syear}&nbsp;</option>
								</c:forEach>
							</select>
						</td>				
						<td class="tdRight" >&nbsp;&nbsp;&nbsp;&nbsp;投資人名稱：</td>				
						<td colspan="3"><input type="text" name="investor" style="width:150px;"  value="${ofiterms.investor}"/></td>			
					</tr>
					<tr>
						<td class="tdRight">國內轉投資：</td>				
						<td>
							<input type="radio" id="reI1" name="reInvest" value="99" class="df"><label for="reI1">不拘</label>
							<input type="radio" id="reI2" name="reInvest" value="1"><label for="reI2">有</label>
							<input type="radio" id="reI3" name="reInvest" value="0"><label for="reI3">無</label>
						</td>				
						<td class="tdRight">涉及特許/特殊：</td>				
						<td colspan="3">
							<input type="radio" name="sp" value="99" id="sp99" class="df"><label for="sp99">不拘</label>
							<input type="radio" name="sp" value="1" id="sp1"><label for="sp1">是</label>
							<input type="radio" name="sp" value="0" id="sp0"><label for="sp0">否</label>
						</td>				
					</tr>

					<tr>
						<td class="tdRight">陸資投資事業：</td>				
						<td colspan="5">
							<input type="radio" name="CNFDI" value="99" id="CNFDI99" class="df"><label for="CNFDI99">不拘</label>
							<input type="radio" name="CNFDI" value="0" id="CNFDI0"><label for="CNFDI0">否</label>
							<input type="radio" name="CNFDI" value="1" id="CNFDI1"><label for="CNFDI1">是</label>
							<input type="radio" name="CNFDI" value="9" id="CNFDI9"><label for="CNFDI9">尚無資料</label>
						</td>		
					</tr>
					<tr>
						<td class="tdRight">異常事項：</td>				
						<td colspan="5">
							<select name="iyear">
								<option value="-1">不拘</option>
								<c:forEach  var="y" items="${iyearlist}" >
									<option value="${y}">${y}&nbsp;</option>
								</c:forEach>
							</select><label>年</label>
							<input type="checkbox" name="abnormal" value="1" id="abnormal1"><label for="abnormal1">訪視異常</label>
							<input type="checkbox" name="abnormal" value="2" id="abnormal2"><label for="abnormal2">財務異常/</label>
							<input type="checkbox" name="abnormal" value="3" id="abnormal3"><label for="abnormal3">特殊需要</label>
							（<input type="radio" name="AndOr" value="1" id="AndOr1"><label for="AndOr1">AND</label>
							<input class="df" type="radio" name="AndOr" value="" id="AndOr"><label for="AndOr">OR</label>）
						</td>				
					</tr>
					
					<tr>
						<td class="tdRight">異常狀況：</td>
						<td colspan="5">
							<div class="ui-widget">
								<select name="itvOneYear" >
									<option value="-1">不拘</option>
									<c:forEach  var="y" items="${iyearlist}" >
										<option value="${y}">${y}&nbsp;</option>
									</c:forEach>
								</select><label>年</label>						
								<label for="errMsgXnote"></label>
								<input type="text" id="errMsgXnote" name="errMsgXnote" style="width:60% !important" value="${ofiterms.errMsgXnote}" >
								<input type="button" class="btn_class_opener" value="常用清單" onclick="openDialog_errMsgXnote('#errMsgXnoteList')">
								<input type="button" class="btn_class_opener" value="清除選擇" onclick="empty('#errMsgXnote')">							  
							</div>							
						</td>
					</tr>					
					
					
					<tr>
						<td class="tdRight">發行方式：</td>				
						<td colspan="5">
							<input type="checkbox" name="issueType" value="01" id="issueType01"><label for="issueType01">非公開發行</label>
							<input type="checkbox" name="issueType" value="02" id="issueType02"><label for="issueType02">公開發行</label>
							<input type="checkbox" name="issueType" value="03" id="issueType03"><label for="issueType03">上市發行</label>
							<input type="checkbox" name="issueType" value="04" id="issueType04"><label for="issueType04">上櫃發行</label>
							<input type="checkbox" name="issueType" value="05" id="issueType05"><label for="issueType05">興櫃發行</label>
							<input type="checkbox" name="issueType" value="99" id="issueType99"><label for="issueType99">其他發行方式</label>
						</td>				
					</tr>
					<tr>
						<td class="tdRight">營業項目：</td>
						<td colspan="5">
							<textarea  id="twsic" name="twsic" rows="3" style="width: 80%;" readonly="readonly">${ofiterms.twsic}</textarea>
						</td>
					</tr>
					<tr>
						<td class="tdRight"  colspan="6" style="text-align: right;">
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input type="button" id="clearbtn" class="btn_class_opener" value="清空"/>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
	<div style="clear: both;"></div>	
</div>
</fieldset>
<div id="twTmp" style="display: none;">
	<jsp:include page="/includes/twsic.jsp" flush="true" />
</div>
<div id="errMsgXnoteList"></div>