<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<script src="<c:url value='/js/hideOptionBySpan.js'/>" type="text/javascript" charset="utf-8"></script>

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
	setCheckboxToDefalut("abnormal","${terms.abnormal}");
	setRedioToDefalut("AndOr","${terms.AndOr}");
  $( "#myReset" ).click(function(){
	  setDefalutOptionByClass($(".df"));
	  $(".searchForm").find("input[type='text']").val("");
	  $(".searchForm select").each(function(){
		  $(this).find("option:first").prop("selected",true);
	  });
	  $("input[name='abnormal']").each(function(){
		  $(this).prop("checked",false);
	  });
  });
	checkmonth();
	if("${terms.year}${terms.smonth}${terms.emonth}".length==0){
		setSelectedToDefalut("year","${terms.maxY}");
		setSelectedToDefalut("smonth","1");
		setSelectedToDefalut("emonth",parseInt("${terms.maxM}",10)+"");
	}else{
		setSelectedToDefalut("smonth",parseInt("${terms.smonth}",10)+"");
		setSelectedToDefalut("emonth",parseInt("${terms.emonth}",10)+"");
		setSelectedToDefalut("year","${terms.year}");
	}
	hideBeforemonth($("select[name='emonth']"),$("select[name='smonth']").val());
  $("select[name='smonth'],select[name='emonth']").change(function(){
		hideBeforemonth($("select[name='emonth']"),$("select[name='smonth']").val());
  });
  $("select[name='year']").change(function(){
	  checkmonth();
  });
  
  //107-08-01
//   var availableTags = JSON.parse('${errMsgXnote}'); // ["A","B"] 
//   $( "#errMsgXnote" ).autocomplete({
//       source: availableTags,
//       minLength: 0
//   });
  
  //107-08-22
  setDialog2("#errMsgXnoteList", 1000, true, "常用清單", false, true)
  
});

//107-08-22
function empty(id) {
	$(id).val("");
	$(id).attr("readonly", false); //清除選擇，清單可編輯
}

function checkmonth(){
	if($("select[name='year']").val()=="${terms.maxY}"){
		setSelectedToDefalut("smonth",parseInt("${terms.maxM}",10)+"");
		setSelectedToDefalut("emonth",parseInt("${terms.maxM}",10)+"");
		hideAftermonth($("select[name='smonth']"),"${terms.maxM}");
		hideAftermonth($("select[name='emonth']"),"${terms.maxM}");
	}else{
		hideAftermonth($("select[name='smonth']"),12);
		hideAftermonth($("select[name='emonth']"),12);
	}
}
function hideAftermonth($mOpt,maxM){
	$mOpt.find("option").each(function(){
		removeSpan($(this));
		var nM=parseInt($(this).val(),10);
		if(nM>parseInt(maxM,10)){
			addSpan($(this));
		}
	});
}
function hideBeforemonth($mOpt,maxM){
	$mOpt.find("option").each(function(){
		removeSpan($(this));
		var nM=parseInt($(this).val(),10);
		if(nM<parseInt(maxM,10) || ( $("select[name='year']").val()=="${terms.maxY}"&&nM>parseInt("${terms.maxM}",10))){
			addSpan($(this));
		}
	});
}
</script>
<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
	<legend style="width:100%;border-top:1px solid #E6E6E6">
		<span style="color:#F30;">[&nbsp;查詢條件&nbsp;]</span>
	</legend>
<div>
	<div style="float: left;width: 100%;">
		<form class="searchForm" action='<c:url value="/console/interviewone/showiolist.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td style="width: 10%;padding: 5px 0px;" class="tdRight">陸資案號：</td>				
						<td style="width: 30%;padding: 5px 0px;"><input type="text" maxlength="6" name="investNo" value="${fn:replace(terms.investNo,'%','')}"/></td>				
						<td style="width: 10%;padding: 5px 0px;" class="tdRight">統一編號：</td>				
						<td style="width: 15%;padding: 5px 0px;"><input type="text" name="IDNO" maxlength="8"  value="${fn:replace(terms.IDNO,'%','')}"/></td>				
						<td style="width: 15%;padding: 5px 0px;" class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;公司名稱：</td>				
						<td style="width: 25%;padding: 5px 0px;"><input type="text" name="investName" style="width:100%;" value="${fn:replace(terms.investName,'%','')}"/></td>				
					</tr>
					<tr>
						<td class="tdRight" nowrap="nowrap">調查年度：</td>				
						<td>
							<select name="year">
								<c:forEach var="ioSer" items="${yearlist}">
									<option value="${ioSer}" <c:if test="${ioSer eq terms.year}">selected="selected" </c:if>>${ioSer}年&nbsp;</option>
								</c:forEach>
							</select>
							<select name="smonth">
								<c:forEach var="i" begin="1" end="12">
									<option value="${i}">${i}月&nbsp;</option>
								</c:forEach>
							</select>
							<label>~</label>
							<select name="emonth">
								<c:forEach var="i" begin="1" end="12">
									<option value="${i}">${i}月&nbsp;</option>
								</c:forEach>
							</select>
						</td>				
						<td class="tdRight" nowrap="nowrap">訪視狀態：</td>				
						<td>
							<select name="interview">
								<option value="-1">不拘</option>
								<c:forEach var="i" items="${iStatus}">
									<option value="${i.key}" <c:if test="${i.key eq terms.interview}">selected="selected" </c:if>>${i.value}</option>
								</c:forEach>
							</select>
						</td>
						<td class="tdRight">問卷狀態：</td>				
						<td>
							<select name="survey">
								<option value="-1">不拘</option>
								<c:forEach var="i" items="${sStatus}">
									<option value="${i.key}" <c:if test="${i.key eq terms.survey}">selected="selected" </c:if>>${i.value}</option>
								</c:forEach>
							</select>
						</td>				
					</tr>
					<tr>
						<td class="tdRight">異常事項：</td>				
						<td colspan="5">
							<input type="checkbox" name="abnormal" value="1" id="abnormal1"><label for="abnormal1">訪視異常</label>
							<input type="checkbox" name="abnormal" value="2" id="abnormal2"><label for="abnormal2">財務異常</label>
							<input type="checkbox" name="abnormal" value="3" id="abnormal3"><label for="abnormal3">特殊需要</label>
							（<input type="radio" name="AndOr" value="1" id="AndOr1"><label for="AndOr1">AND</label>
							<input class="df" type="radio" name="AndOr" value="" id="AndOr2"><label for="AndOr2">OR</label>）
						</td>
					</tr>
					<tr>
						<td class="tdRight">異常狀況：</td>
						<td colspan="5">
							<div class="ui-widget">
								<label for="errMsgXnote"></label>
								<input type="text" id="errMsgXnote" name="errMsgXnote" style="width:72% !important" value="${terms.errMsgXnote}" >
								<input type="button" class="btn_class_opener" value="常用清單" onclick="openDialog_errMsgXnote('#errMsgXnoteList')">
								<input type="button" class="btn_class_opener" value="清除選擇" onclick="empty('#errMsgXnote')">							  
							</div>		
						</td>
					</tr>
					<tr>
						<td></td>
						<td colspan="5" style="color:blue;">說明：多筆條件請用半形逗號「,」隔開，查詢關係為交集(and)。</td>
					</tr>
					<tr>
						<td class="tdRight" colspan="6" style="text-align: right;">
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input type="button" id="myReset" class="btn_class_opener" value="清空"/>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
</div>
</fieldset>
<div id="errMsgXnoteList"></div>