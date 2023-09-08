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
<script type="text/javascript" src="<c:url value='/js/userAuthority.js'/>"></script>
<script type="text/javascript">
$(function(){
	loginRecordError();
	checkDefaultVal("${userbean.company}","${userbean.groupId}","${userbean.enable}");
	$("#sent").click(function(){
		var ref = checkEditValue();
		if(ref){
			ref = checkUser();
		}
		if(ref){
			$("#myForm").submit();
		}
	});
	$("#clear").click(function(){
		location.reload();
	});
	var range = "${webFolder}";
	getDefaultSelectBoth(range);
});
</script>

<div id="pagecont">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;編輯使用者帳號&nbsp;</strong>&nbsp;</span>
		</legend>
		<div id="leftbody">
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;最近10次登入紀錄&nbsp;]</span>
				</legend>
			<div>
			<c:if test="${not empty recordlogin}">
			   <table>
			   		<c:forEach var="record" items="${recordlogin}" varStatus="i">
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
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;編輯使用者帳號&nbsp;]</span>
				</legend>
					<div>
						<form action="<c:url value='/edituser.jsp'/>" method="post" id="myForm">
							<input type="hidden" name="editor" value="${userInfo.idMember}">
							<input type="hidden" name="type" value="edit">
							<input type="hidden" name="rangeStr" value="${param.rangeStr}" id="rangeStr">
							<table id="mytable">
								<tr>
									<td colspan="2"></td>
								</tr>
								<tr>
									<td class="tdRight">使用者帳號：</td>
									<td class="tdLeft">${userbean.idMember}<input type="hidden" id="user" name="user" maxlength="12" size="25" value="${userbean.idMember}"><span class="errorMsg">${errors.emptyidMember}</span></td>
								</tr>
																<tr>
									<td class="tdRight">修改密碼：</td>
									<td class="tdLeft"><input type="password" id="newStr" name="newStr" size="27" title="請以6個以上英數字或符號組成新密碼"><span class="errorMsg">${errors.emptyNewStr}${errors.noMatch}${errors.notEqual}</span></td>
								</tr>
								<tr>
									<td class="tdRight">再次輸入密碼：</td>
									<td class="tdLeft"><input type="password" id="checkStr" name="checkStr" size="27"  title="請再次輸入密碼"><span class="errorMsg">${errors.emptyCheckStr}</span></td>
								</tr>
								<tr>
									<td class="tdRight">單位：</td>
									<td class="tdLeft">
										<select id="company" name="company" style="width: 190px;">
											<option value="moea">投資審議委員會</option>
											<option value="cier">中華經濟研究院</option>
											<option value="ibtech">鐵橋數位科技</option>
										</select>
										<span class="errorMsg">${errors.emptycompany}</span>
									</td>
								</tr>
								<tr>
									<td class="tdRight">姓名：</td>
									<td class="tdLeft"><input type="text" id="owner" name="owner" size="25" value="${userbean.username}"><span class="errorMsg">${errors.emptyusername}${errors.notTWWord}</span></td>
								</tr>
								<tr>
									<td class="tdRight">E-mail：</td>
									<td class="tdLeft"><input type="text" id="mail" name="mail" size="45" value="${userbean.userEmail}"><span class="errorMsg">${errors.emptyuserEmail}${errors.notMail}</span></td>
								</tr>
								<tr>
									<td class="tdRight">是否為網站管理者：</td>
									<td class="tdLeft">
										<input type="radio" name="authority" value="super"><label>是</label> 
										<input type="radio" name="authority" value="user" checked="checked"><label>否</label>
										<span class="errorMsg">${errors.emptyauthority}</span>
									</td>
								</tr>
								<tr>
									<td class="tdRight">使用者編輯查閱權限：</td>
									<td class="tdLeft">
										<input id="more" type="button" class="btn_class_opener" value="進階設定" title="預設為可查閱不可編輯，如需調整請點選此按鈕">
										<span class="errorMsg">${errors.emptyRange}</span>
									</td>
								</tr>
								<tr>
									<td class="tdRight">帳號狀態：</td>
									<td class="tdLeft">
										<input type="radio" name="able" value="0"><label>已停用</label> 
										<input type="radio" name="able" value="1"><label>已啟用</label>
										<span class="errorMsg">${errors.emptyable}</span>
									</td>
								</tr>
								<tr>
									<td colspan="2"></td>
								</tr>
								<tr>
									<td colspan="2">
										<input id="sent" type="button" value="送出">
										<input id="clear" type="button" value="重設">
									</td>
								</tr>
							</table>
							<jsp:include page="/console/admin/content/userauthority.jsp" flush="true"/>
						</form>
					</div>
			</fieldset>
		</div>
	</fieldset>
	<div style="height: 20px;clear: both;"></div>
</div>
