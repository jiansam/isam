<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/yearmonthhelper.js'/>"></script>
<script type="text/javascript">
$(function(){
	setYearOption($("select[name='year']"));
	setYearUpperDownBound($("select[name='year']"),10);
	inputTextNext("#myform",".nextInput",".skip");
	$("#newone").click(function(){
		if(checkTable()){
			$("#myform").submit();
		}
	});
	$("#getold").click(function(){
		if(checkTable()){
			$("#myform").append("<input type='hidden' name='getOld' value='1'>").submit();
		}
	});
	$("input[name='company']").change(function(){
		if($(this).val()==="1"){
			if(!checkTable()){
				$("input[name='company'][value='0']").prop("checked",true);
				$( "#reInvestmentList" ).html(""); 
			}else{
				$.post("${pageContext.request.contextPath}/console/interviewone/content/getreinvestnolist.jsp",
						{'year':$("select[name='year']").val(),'investNo':$.trim($("input[name='investNo']").val())},
				function(data){
						$( "#reInvestmentList" ).html(data); 
				},"html");
			}
		}else{
			$( "#reInvestmentList" ).html(""); 
		}
	});
});
function checkTable(){
	var rs=true;
	var investNO=$.trim($("input[name='investNo']").val());
	//var idno=$.trim($("input[name='idno']").val());
	if(investNO.length==0/*&&idno.length==0*/){
		rs=false;
		alert("請填寫陸資案號，以進行查詢。");
	}else{
		if(investNO.length>0){
			if(investNO.length!=6){
				alert("請填寫完整的六位碼案號，以進行查詢。");
				rs=false;
			}else if(!startsWith(investNO,"4")&&!startsWith(investNO,"5")){
				alert("陸資案號開頭必須為4或5，請檢查您的案號是否正確。");
				rs=false;
			}
		}
		/* else if(idno.length!=8){
			alert("請填寫完整的八位碼統一編號，以進行查詢。");
			rs=false;
		} */
	}
	return rs;
}
</script>
<div>
	<fieldset>
		<legend class="legendTitle">
			<span style="color:#F30;"><strong>&nbsp;新增${param.formtypeName}&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/console/interviewone/content/surveymenu.jsp" flush="true">
				<jsp:param value="${param.index}" name="pos"/>
			</jsp:include>
		</div>
		<div>
			<form id="myform" action="<c:url value='/console/interviewone/getform.jsp'/>" method="post">
			<fieldset>
				<legend>
					<span style="color:#F30;">[調閱資料]</span>&nbsp;
				</legend>
				<span class="basetable">
					填報年度：
					<select style="width: 80px;" name="year">
					</select>
				</span><br/>
				<span class="basetable" >
					陸資案號：<input type="text" maxlength="6" name="investNo"/>					
				</span><br/>
				<span class="basetable" >
					選擇填報事業：
					<input id="company0" type="radio" name="company" value="0" checked="checked"><label for="company0">國內事業</label>
					<input id="company1" type="radio" name="company" value="1"><label for="company1">轉投資事業</label>	
					<span id="reInvestmentList"></span>				
				</span><br/>
<!-- 				<span class="basetable">
					統一編號：<input type="text" maxlength="8" name="idno"/>
				</span><br/> -->
				<input type="hidden" value="${param.formtype}" name="formtype">
				<div class="basetable" style="text-align: left;margin-top: 10px;">
					<input id="newone" type="button" value="填寫空白${param.STR}" class="btn_class_Approval" style="font-size: 13px;">
					<input id="getold" type="button" value="帶入去年資料" class="btn_class_loaddata" style="font-size: 13px;">				
				</div>				
				<br/>
			</fieldset>
			</form>
		</div>
	</fieldset>
</div>