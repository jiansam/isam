<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.isam.service.*,com.isam.bean.*,java.util.*,com.isam.helper.*,com.isam.service.ofi.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript" src="<c:url value='/js/select2.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/select2_locale_zh-TW.js'/>"></script>
<link href="<c:url value='/css/select2/select2.css'/>" rel="stylesheet"/>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/setInputTextNext.js'/>"></script>
<script src="<c:url value='/js/setDefaultChecked.js'/>" type="text/javascript" charset="utf-8"></script>
<%
	InterviewoneHelp opt = new InterviewoneHelp();
	session.setAttribute("fOpt", opt.getOptionValName());
	OFIDepartmentService odt= new OFIDepartmentService();
	session.setAttribute("odt", odt.getCodeNameMap());
	String serno=DataUtil.nulltoempty(request.getParameter("serno"));
	String qNo=DataUtil.nulltoempty(request.getParameter("qNo"));
	InterviewoneManageService imSer= new InterviewoneManageService();
	session.removeAttribute("followingStr");
	session.removeAttribute("MAXReceive");
	session.setAttribute("followingStr", imSer.checkFowllowing(qNo, serno));
	session.setAttribute("MAXReceive", imSer.checkMaxReceiveDate(qNo, serno));
	
	session.removeAttribute("imBean");
	if(!serno.isEmpty()&&DataUtil.nulltoempty(request.getParameter("type")).equals("edit")){
		InterviewoneManage bean= imSer.select(serno);
		session.setAttribute("imBean", bean);
	}
%>
<script type="text/javascript">
$(function(){
	inputTextNext("#myform",".nextInput",".skip");
	$("select[name='issueby'],select[name='progress']").select2({
		 language: "zh-TW"
	  });
	setDFRp();
	$("input[name='issueNo']").focus(function(){
		checkIssueNo();
	}).blur(function(){
		checkIssueNo();
	});
	$("input[name='flag']").change(function(){
		if($(this).val()==="1"){
			toISAMFollowing( "${param.serno}", "${param.type}");
		}else{
			toEditFollowing("${param.serno}", "${param.type}");
		}
	});
	$("#myInsert").click(function(){
		var rd=$("input[name='receiveDate']").val().replace(/\//g,"");
		if(checkDFRp()){
			if(parseInt($("input[name='following']:checked").val(),10)===1){
				var w=0;
				$.getJSON(getRootPath()+"/console/interviewone/checkFollowing.jsp",{ "serno": "${param.serno}", "qNo": "${param.qNo}"},function(data){
					var d=data[0].receiveDate;
					var f=data[0].following;
					if(f==="1"){
						alert("同一訪查不可有兩筆以上的已完成紀錄，請重新確認。");
						w=1;
					}
					if(rd<d){
						alert("已完成的收文日期或查訪日期必須比所有其他紀錄的日期大，請重新確認。");
						w=1;
					}
				}).done(function(){
					if(w===0){
						var x="<div><span class='ui-icon ui-icon-alert' style='float:left; margin:0 7px 20px 0;'></span>";
						x+="追蹤情形如選擇已完成，則不可再新增訪查處理紀錄，請確認是否已完成追蹤！</div>";
						var $d= $(x).dialog({
							 resizable: false,
						      modal: true,
						      width:380,
						      title:"確定已完成追蹤流程",
						      buttons: {
						          "確定": function() {
						        	  $("#myform").submit();
						            $( this ).dialog( "close" );
						          },
						          "取消": function() {
						            $( this ).dialog( "close" );
						          }
						      }
						});
					}
				});
			}else{
 				$.getJSON(getRootPath()+"/console/interviewone/checkFollowing.jsp",{ "serno": "${param.serno}", "qNo": "${param.qNo}"},function(data){
					var d=data[0].receiveDate;
					var f=data[0].following;
					if(rd>d&&f==="1"){
						alert("收文日期不可以大於已完成紀錄的收文日期。");
					}else{
						$("#myform").submit();
					}
				})
				/* if(rd>"${MAXReceive}"&&"${followingStr}"==="1"){
					alert("收文日期不可以大於已完成紀錄的收文日期。");					
				}else{
					$("#myform").submit();
				} */
			}
		}
	});
});
function checkDFRp(){
	var b=true;
	$(".formProj .notEmpty").each(function(){
		if($.trim($(this).val()).length==0){
			alert("本欄位為必填欄位");
			$(this).focus();
			b=false;
			return false;
		}
	});
	if($("input[name='receiveDate']").val()<$("input[name='issueDate']").val()){
		alert("收文日期須大於等於發文日期");
		b=false;
		return false;
	}
	return b;
}
function setDFRp(){
	$("input[name='following'][value='0']").prop("checked",true);
	if("${param.type}"==="edit"){
		$("select[name='issueby']").select2("val","${imBean.issueby}");
		$("select[name='progress']").select2("val","${imBean.optionValue}");
		setRedioToDefalut('following', "${imBean.following}");
	}
	checkIssueNo();
}
function checkIssueNo(){
	var ino=$("input[name='issueNo']").val();
	if(ino==="無"&&"${param.type}"==="add"){
		$("input[name='issueNo']").val("");
	}else if(ino.length===0){
		$("input[name='issueNo']").val("無");
	}	
}
</script>
<div>
	<form id="myform" action="<c:url value='/console/interviewone/editFollowing.jsp'/>" method="post">
		<table class="formProj" style="font-size: 16;">
			<tr>
				<td class="trRight" style="width: 12%;"><span style="color: red;">*</span>追蹤類型：</td>
				<td colspan="3">
					<c:choose>
						<c:when test="${param.type eq 'add' }">
							<input type="radio" name="flag" value="0" id="flag0" checked="checked"><label for="flag0">收文處理紀錄</label>
							<input type="radio" name="flag" value="1" id="flag1"><label for="flag1">投審會訪視紀錄</label>
						</c:when>
						<c:otherwise>
							收文處理紀錄
							<input name="flag" value="${imBean.flag}" type="hidden">
						</c:otherwise>
					</c:choose>
				</td>
			</tr>	
			<tr>
				<td class="trRight" style="width: 12%;"><span style="color: red;">*</span>收文文號：</td>
				<td><input type="text" name="receiveNo" style="width: 85%;" value="${imBean.receiveNo }" class="notEmpty"></td>
				<td class="trRight" style="width: 12%;"><span style="color: red;">*</span>收文日期：</td>
				<td><input type="text" name="receiveDate" value="${ibfn:addSlash(imBean.receiveDate)}"  class="singledate notEmpty"></td>
			</tr>	
			<tr>
				<td class="trRight"><span style="color: red;">*</span>發文文號：</td>
				<td><input type="text" name="issueNo"  style="width: 85%;" value="${imBean.issueNo}" class="notEmpty"></td>
				<td class="trRight"><span style="color: red;">*</span>發文日期：</td>
				<td><input type="text" name="issueDate" value="${ibfn:addSlash(imBean.issueDate)}"  class="singledate notEmpty"></td>
			</tr>
			<tr>
				<td class="trRight">來文單位：</td>
				<td colspan="3">
					<select name="issueby" style="width: 70%;">
						<option value="">無</option>
					<c:forEach var="item" items="${odt}" varStatus="i">
						<c:if test="${fn:endsWith(item.key,'00')}">
							<c:if test="${i.index > 0 }">
								</optgroup>
							</c:if>
							<optgroup label="${item.value}">
						</c:if>
						<option value="${item.key}">${item.value}</option>
					</c:forEach>
					</optgroup>
					</select>
				</td>
			</tr>		
			<tr>
				<td class="trRight" style="width: 12%;"><span style="color: red;">*</span>處理狀態：</td>
				<td colspan="3">
					<select name="progress" style="width: 70%;">
						<c:forEach var="item" items="${fOpt['progress']}">
							<option value="${item.key}">${item.value}</option>
						</c:forEach>
					</select>
				</td>
			</tr>		
			<tr>
				<td class="trRight" style="width: 12%;"><span style="color: red;">*</span>追蹤情形：</td>
				<td colspan="3">
					<c:forEach var="item" items="${fOpt['following']}">
						<c:if test="${not empty item.key}">
							<c:if test="${!(followingStr == 1&& item.key==1)||imBean.following==1}">
								<input type="radio" name="following" value="${item.key}" id="following${item.key}">
								<label for="following${item.key}">${item.value}</label>
							</c:if>
						</c:if>
					</c:forEach>
				</td>
			</tr>		
			<tr>
				<td class="trRight">備註：</td>
				<td colspan="3"><textarea name="note" style="width: 80%;" rows="10">${imBean.note}</textarea></td>
			</tr>	
			<tr>
				<td colspan="4">星號(<span style="color: red;">*</span>)為必填欄位。</td>
			</tr>
		</table>
		<input type="hidden" value="${param.serno}" name="serno">
		<input type="hidden" value="${param.qNo}" name="qNo">
		<input type="hidden" value="${param.type}" name="type">
		<div style="text-align: center;margin-top: 10px;">
			<input type="button" id="myInsert" class="btn_class_Approval" value="儲存"/>
		</div>
	</form>
</div>
