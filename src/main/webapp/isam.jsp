<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-language" content="zh-tw" />
<meta http-equiv="Content-Script-Type" content="text/javascript" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
<title>歡迎使用投資調查與管理資料庫</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/webStyle.css'/>" />
<jsp:include page="/includes/jQueryInculdes.jsp" flush="true" />
<link href="<c:url value='/css/start/jquery-ui-1.10.3.custom.min.css' />" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<c:url value='/js/isam.js'/>"></script>
<script>
	$(function() {
		enterSubmit();
		$( "#dialog" ).dialog({
			autoOpen: false,
			width: 320,
			show: {
				effect: "blind",
				duration: 1000
			},
			hide: {
				effect: "blind",
				duration: 1000
			}
		});
		$( "#opener" ).click(function() {
			$( "#dialog" ).dialog( "open" );
		});
		/*送出檢視用 */
		$("#sent").click(function(){
			var a1=$("#myform :text").val();
			var a2=$("#myform :password").val();
			if(checkStr(a1,/[A-Za-z0-9]{2,12}/) && checkPWD(a2)){
				$("#myform").submit();
			}else{
				alert("您的帳號或密碼格式錯誤，請重新輸入。");				
			}
		});
	});
	</script>
</head>
<body>
<div id="welcome">
	<div style="height: 220px;"></div>
	<div id="pagecontent">
		<div id="leftbody">
		</div>
		<div id="rightbody">
			<div id="loginForm">
				<form action='<c:url value="/isamCheck.jsp"/>' method="post" id="myform">
					<h2 style="margin-bottom:15px;">歡迎登入投資調查與管理資料庫<span style="margin-left: 7px;"><img alt="我有疑問" title="我有疑問" id="opener" src='<c:url value="/images/question2.png"/>' style="width: 20px;height: auto;"></span></h2>
						<div id="dialog" title="Content us" style="font-size: 15px;">
							<p>使用上如有任何疑問，歡迎與我們聯絡</p>
							中華經濟研究院&nbsp;區域發展研究中心<br/>
							聯絡人：蔡小姐<br/>
							Tel：02-2735-6006 #231<br/>
							E-mail: <a href="mailto:ihsinya@cier.edu.tw?subject=%5B%A7%EB%BCf%B7%7C%5D%BD%D0%A8%F3%A7U%A8%FA%A6%5E%B1K%BDX">ihsinya@cier.edu.tw</a>
						</div>
						<div style="margin-top:5px;color:#0046f8; font-weight:bold;">帳號：<input type="text" name="account" style="width: 200px;" /> </div>
						<div style="margin-top:5px;color:#0046f8; font-weight:bold;">密碼：<input type="password" name="pWD"  style="width: 200px;"/> </div>
						<div style="margin-top:10px; text-align:right; width:255px;">
							<span style="color: red;font-size: 13px;">${errors.inputER}&nbsp;</span>
							<span style="color: red;font-size: 13px;">${errors.noER}&nbsp;</span>
							<input id="sent" type="button" value="登入" />
							<input type="reset" value="重填" />
						</div>
				</form>
			</div>
		</div>
	</div>
	<div style="background-color: #6699CC;padding-top:5px;height:55px ;text-align: center;font-size: 12px;font-weight: bold;">
	經濟部投資審議委員會「全球投資趨勢及政策研究計畫」專案委辦<br/>
	建議使用IE7.0以上版本瀏覽　最佳觀看解析度1280x768<br/>
Copyright©2013 Investment Commission, Ministry of Economic Affairs , All Rights reserved.
	</div>
</div>
</body>
</html>