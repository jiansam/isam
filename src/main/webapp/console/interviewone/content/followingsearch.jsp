<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<%-- <script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script> --%>
<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<%-- <link href="<c:url value='/css/select2fix.css'/>" rel="stylesheet"/> --%>
<style>
	.ui-datepicker{
		Font-size:13px !important;
		padding: 0.2em 0.2em 0;
	}
	.ui-menu { 
		width: 200px;
		padding: 0.2em 0.2em 0; 
	}
	.select2-search-choice{
	 line-height:16px !important; 
	}
</style>
<script>
  $(function() {
	  $("select[name='progress']").select2({
		 language: "zh-TW",
		 placeholder: "預設不拘，或點此選擇",
		 closeOnSelect: false
	  });
	setFormDF();
    $( "#quickmenu" ).menu();
    $("#mySearch").click(function(){
    	if($("input[name='following']:checked").length==0){
    		alert("追蹤情形未選擇時，系統自動全選。");
    		checkedAll("following");
    	}
    	if($("input[name='abnormal']:checked").length==0){
	    	alert("請至少選擇一項異常事項。");
	    	return false;
    	}else{
    		$(".searchForm").submit();
    	}
    });
    $("#myReset").click(function(){
    	clearAllItems();
    });
  });
  function setFormDF(){
	setDefalutOptionByClass($(".df"));
	if("${terms.progress}".length>0){
		var ary="${terms.progress}".split(",");
		$("select[name='progress']").select2("val",ary);
	}
	if("${terms.abnormal}".length>0){
		setCheckboxToDefalut("abnormal","${terms.abnormal}");
	}else{
		checkedAll("abnormal");
	}
	if("${terms.following}".length>0){
		setCheckboxToDefalutWithEmpty("following","${terms.following}");
	}else{
		checkedAll("following");
	}
	if("${terms.AndOr}".length>0){
		setRedioToDefalut("AndOr","${terms.AndOr}");
	}
	if("${terms.gap}".length>0){
		setRedioToDefalut("gap","${terms.gap}");
	}
  }
  function checkedAll(name){
	$("input[name='"+name+"']").each(function(){
		 $(this).prop("checked",true);
	});
  }
  function clearAllItems(){
	 $("select[name='progress']").select2("val","");
	 $("input[name='investName']").val("");
	 $("input[name='investNo']").val("");
	 $("input[name='IDNO']").val("");
	 setRedioToDefalutWithEmpty("AndOr","");
	 setRedioToDefalutWithEmpty("gap","");
	 $("input[name='abnormal']").each(function(){
		 $(this).prop("checked",false);
	 });
	 $("input[name='following']").each(function(){
		 $(this).prop("checked",false);
	 });
  }
</script>
<script type="text/javascript">
$(function() {
	checkmonth();
	if("${terms.year}${terms.smonth}${terms.emonth}".length==0){
		setSelectedToDefalut("year","${terms.maxY}");
		//setSelectedToDefalut("smonth",parseInt("${terms.maxM}",10)+"");
		setSelectedToDefalut("emonth",parseInt("${terms.maxM}",10)+"");
	}else{
		//setSelectedToDefalut("smonth",parseInt("${terms.smonth}",10)+"");
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
  
});
function checkmonth(){
	if($("select[name='year']").val()=="${terms.maxY}"){
		//setSelectedToDefalut("smonth",parseInt("${terms.maxM}",10)+"");
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
	<div style="float: left;width: 80%;">
		<form class="searchForm" action='<c:url value="/console/interviewone/showfollowing.jsp"/>' method="post">
		   	<strong style="color:#222;">&nbsp;自訂條件&nbsp;</strong>
		   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;">
			    <table style="width: 95%;padding-left: 20px;">
					<tr>
						<td style="width: 15%;padding: 5px 0px;" class="tdRight">陸資案號：</td>				
						<td style="width: 35%;padding: 5px 0px;"><input type="text" maxlength="6" name="investNo" value="${fn:replace(terms.investNo,'%','')}"/></td>				
						<td style="width: 15%;padding: 5px 0px;" class="tdRight">統一編號：</td>				
						<td style="width: 35%;padding: 5px 0px;"><input type="text" name="IDNO" maxlength="8"  value="${fn:replace(terms.IDNO,'%','')}"/></td>				
					</tr>
					<tr>
						<td class="tdRight">公司名稱：</td>				
						<td ><input type="text" name="investName" style="width:90%;" value="${fn:replace(terms.investName,'%','')}"/></td>				
						<td class="tdRight">調查年度：</td>				
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
					</tr>
					<tr>
						<td class="tdRight">異常事項：</td>				
						<td colspan="3">
							<input type="checkbox" name="abnormal" value="1" id="abnormal1"><label for="abnormal1">訪視異常</label>
							<input type="checkbox" name="abnormal" value="2" id="abnormal2"><label for="abnormal2">財務異常</label>
							<input type="checkbox" name="abnormal" value="3" id="abnormal3"><label for="abnormal3">特殊需要</label>
							（<input type="radio" name="AndOr" value="1" id="AndOr1"><label for="AndOr1">AND</label>
							<input class="df" type="radio" name="AndOr" value="" id="AndOr2"><label for="AndOr2">OR</label>）
						</td>
					</tr>
					<tr>
						<td class="tdRight">處理狀態：</td>				
						<td colspan="3">
							<select name="progress" multiple="multiple" style="width: 95%;">
								<c:forEach var="item" items="${fOpt['progress']}">
									<option value="${item.key}">${item.value}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td class="tdRight">追蹤情形 ：</td>				
						<td>
							<c:forEach var="item" items="${fOpt['following']}">
								<input type="checkbox" name="following" value="${item.key}" id="following${item.key}">
								<label for="following${item.key}">${item.value}</label>
							</c:forEach>
						</td>
						<td class="tdRight">經營狀況 ：</td>	
						<td>
							<input class="df" type="radio" name="gap" value="" id="gap"><label for="gap">不拘</label>
							<input type="radio" name="gap" value="1" id="gap1"><label for="gap1">應追蹤</label>
						</td>
					</tr>
					<tr>
						<td class="tdRight"  colspan="6" style="text-align: right;">
							<input type="submit" id="mySearch" class="btn_class_opener" value="查詢"/>
							<input type="button" id="myReset"class="btn_class_opener" value="清空"/>
						</td>
					</tr>
			   	</table>
		   	</div>
	   </form>
	</div>
   	<div style="float: left;padding-left: 15px;width: 15%;">
		<strong style="color:#222;">&nbsp;快速查詢&nbsp;</strong>
		<ul id="quickmenu" style="width: 180px;">
			<li><a href="<c:url value="/console/interviewone/showfollowing.jsp?gap=1&priority=1"/>"><span class="ui-icon ui-icon-play"></span>經營狀況追蹤</a></li>
			<%-- <li><a href="<c:url value="/console/interviewone/showfollowing.jsp?priority=1"/>"><span class="ui-icon ui-icon-play"></span>明年優先訪查</a></li> --%>
		</ul>
	</div>
	<div style="clear: both;"></div>
</div>
</fieldset>