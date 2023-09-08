<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.isam.service.*,com.isam.service.ofi.*,com.isam.bean.*,java.util.*,com.isam.helper.*"%>
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
	$("select[name='progress']").select2({
		 language: "zh-TW"
	  });
	setDFRp();
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
			if($("input[name='following']:checked").val()==="1"){
				var w=0;
				$.getJSON(getRootPath()+"/console/interviewone/checkFollowing.jsp",{ "serno": "${param.serno}", "qNo": "${param.qNo}"},function(data){
					var d=data[0].receiveDate;
					var f=data[0].following;
					if(f==="1"){
						alert("同一訪查不可有兩筆以上的已完成紀錄，請重新確認。");
						w=1;
					}
					if(rd<d){
						alert("已完成的日期必須比所有其他紀錄的日期大，請重新確認包含收文處理紀錄之日期。");
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
// 				if(rd>"${MAXReceive}"&&"${followingStr}"==="1"){
// 					alert("查訪日期不可以大於已完成紀錄的日期。");					
// 				}else{
// 					$("#myform").submit();
// 				}
					$.getJSON(getRootPath()+"/console/interviewone/checkFollowing.jsp",{ "serno": "${param.serno}", "qNo": "${param.qNo}"},function(data){
					var d=data[0].receiveDate;
					var f=data[0].following;
					if(rd>d&&f==="1"){
						alert("收文日期不可以大於已完成紀錄的收文日期。");
					}else{
						$("#myform").submit();
					}
				})
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
	return b;
}
function setDFRp(){
	$("input[name='following'][value='0']").prop("checked",true);
	if("${param.type}"==="edit"){
		$("select[name='progress']").select2("val","${imBean.optionValue}");
		setRedioToDefalut('following', "${imBean.following}");
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
							<input type="radio" name="flag" value="0" id="flag0"><label for="flag0">收文處理紀錄</label>
							<input type="radio" name="flag" value="1" id="flag1" checked="checked"><label for="flag1">投審會訪視紀錄</label>
						</c:when>
						<c:otherwise>
							投審會訪視紀錄
							<input name="flag" value="${imBean.flag}" type="hidden">
						</c:otherwise>
					</c:choose>
				</td>
			</tr>	
			<tr>
				<td class="trRight" style="width: 12%;"><span style="color: red;">*</span>處理狀態：</td>
				<td colspan="3">
					<select name="progress" style="width: 70%;">
						<c:forEach var="item" items="${fOpt['progress']}">
							<c:if test="${item.key eq '1' ||item.key eq '2' ||item.key eq '99' }">
							<option value="${item.key}">${item.value}</option>
							</c:if>
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
				<td class="trRight" style="width: 12%;"><span style="color: red;">*</span>訪查日期：</td>
				<td colspan="3"><input type="text" name="receiveDate" value="${ibfn:addSlash(imBean.receiveDate)}"  class="singledate notEmpty"></td>
			</tr>		
			<tr>
				<td class="trRight"><span style="color: red;">*</span>訪視人員：</td>
				<td colspan="3"><textarea name="interviewer" style="width: 80%;" rows="3" class="notEmpty">${imBean.interviewer}</textarea></td>
			</tr>	
			<tr>
				<td class="trRight">受訪人員：</td>
				<td colspan="3"><textarea name="interviewee" style="width: 80%;" rows="3">${imBean.interviewee}</textarea></td>
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
