<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="<c:url value='/css/textareafont.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	$( "#tabs" ).tabs();
	if("${type}"==="0"){
		showInfo("資料已成功刪除");
	}else if("${type}"==="1"){
		showInfo("資料已成功覆蓋");
	}else if("${type}"==="3"){
		showInfo("資料已成功刪除");
		$( "#tabs" ).tabs("option", "active", 1);
	}
});
</script>
<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;專案審查資料維護&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/console/project/content/projectmenu.jsp" flush="true">
				<jsp:param value="5" name="pos"/>
			</jsp:include>
		</div>
		<div id="tabs">
		<ul>
			<li><a href="#tabs-1">待確認列表</a></li>
		    <c:if test="${not empty notMatchlist}"><li><a href="#tabs-2">匯入錯誤</a></li></c:if>
		    <li><a href="update_status.jsp">資料介接情形</a></li>
		</ul>
		<div id="tabs-1">
			<jsp:include page="/console/project/content/prlisttable.jsp" flush="true" />
		</div>
		<c:if test="${not empty notMatchlist}">
		<div id="tabs-2">
			<jsp:include page="/console/project/content/noserno.jsp" flush="true" />
		</div>
		</c:if>
		</div>
	</fieldset>
</div>
