<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
<!--
table td{
	text-align: center;
}
.tdLeft{
	text-align: left;
}
.tdRight{
	text-align: right;
}
.errorMsg{
	color: red;
	font-size: 12px;
}
-->
</style>
<script type="text/javascript" src="<c:url value='/js/isam.js'/>"></script>
<script type="text/javascript">
$(function(){
	loginRecordError();
	$("#sent").click(function(){
		var oldStr=$("#oldStr").val();
		var newStr=$("#newStr").val();
		var checkStr=$("#checkStr").val();
		if(checkIsEmpty(oldStr)||checkIsEmpty(newStr)||checkIsEmpty(checkStr)){
			if(checkIsEmpty(oldStr)){
				alert("請輸入原本設定的密碼。");
				$("#oldStr").focus();
			}else if(checkIsEmpty(newStr)){
				alert("請輸入新密碼。");
				$("#newStr").focus();
			}else if(checkIsEmpty(checkStr)){
				alert("請再次輸入新密碼。");
				$("#checkStr").focus();
			}
		}else{
			if(!checkIsEqual(newStr,checkStr)){
				alert("您兩次輸入的新密碼不一致，請重新輸入。");
				$("#checkStr").val("");
				$("#newStr").val("").focus();
			}else{
				if(!checkPWD(newStr)){
					alert("您輸入的新密碼不符合密碼的格式，請以6個以上英數字或符號組成新密碼。");
					$("#checkStr").val("");
					$("#newStr").val("").focus();
				}else if(checkIsEqual(newStr,oldStr)){
					alert("新密碼不可與原密碼相同，請重新輸入。");
					$("#checkStr").val("");
					$("#newStr").val("").focus();
				}else{
					$("#myForm").submit();
				}
			}
		}
	});
});
</script>

<div id="pagecont">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;歡迎使用本資料庫&nbsp;</strong>&nbsp;</span>
		</legend>
	<div id="leftbody">
		<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
			<legend style="width:100%;border-top:1px solid #E6E6E6">
				<span style="color:#F30;">[&nbsp;${userInfo.idMember}最近10次登入紀錄&nbsp;]</span>
			</legend>
		<div>
		<c:if test="${not empty loginRecord}">
		   <table>
		   		<c:forEach var="record" items="${loginRecord}" varStatus="i">
					<tr>
						<td>(${i.index+1})</td>
						<td class="tdLeft">${fn:substring(record.loginTime,0,fn:indexOf(record.loginTime,":")+3)}</td>
						<td><input type="hidden" class="yn" value="${record.loginResult}">${record.loginResult}</td>
					</tr>
		   		</c:forEach>
		   </table>
		</c:if>
		</div>
		</fieldset>
	</div>
	<div id="rightbody">
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;個人帳號資訊&nbsp;]</span>
				</legend>
			<div>
				<table>
						<tr>
							<td class="tdRight">帳號：</td>
							<td class="tdLeft">${userInfo.idMember}</td>
						</tr>
						<tr>
							<td class="tdRight">所有人：</td>
							<td class="tdLeft">${userInfo.username}</td>
						</tr>
						<tr>
							<td class="tdRight">E-mail：</td>
							<td class="tdLeft">${userInfo.userEmail}</td>
						</tr>
						<tr>
							<td class="tdRight">帳號最後更新時間：</td>
							<td class="tdLeft">${fn:substring(userInfo.updtime,0,fn:indexOf(userInfo.updtime,":")+3)}</td>
						</tr>
					</table>
				</div>
			</fieldset>
		</div>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;修改密碼&nbsp;]</span>
				</legend>
			<div>
				<form action="<c:url value='/changePwd.jsp'/>" method="post" id="myForm">
					<input type="hidden" name="account" value="${userInfo.idMember}">
					<table>
						<tr>
							<td class="tdRight">原密碼：</td>
							<td class="tdLeft"><input type="password" id="oldStr" name="oldStr" size="25" title="請輸入原密碼"><span class="errorMsg">${errors.emptyOld}${errors.oldError}</span></td>
						</tr>
						<tr>
							<td class="tdRight">新密碼：</td>
							<td class="tdLeft"><input type="password" id="newStr" name="newStr" size="25" title="請以6個以上英數字或符號組成新密碼"><span class="errorMsg">${errors.emptyNewStr}${errors.noMatch}${errors.notEqual}</span></td>
						</tr>
						<tr>
							<td class="tdRight">再次輸入新密碼：</td>
							<td class="tdLeft"><input type="password" id="checkStr" name="checkStr" size="25" title="請再次輸入新密碼"><span class="errorMsg">${errors.emptyCheckStr}</span></td>
						</tr>
						<tr>
							<td colspan="2">
								<input id="sent" type="button" value="送出">
								<input type="reset" value="清空">
							</td>
						</tr>
					</table>
				</form>
			</div>
			</fieldset>
		</div>
	</div>
	</fieldset>
	<div style="height: 20px;clear: both;"></div>
</div>
