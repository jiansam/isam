<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
$(function() {
  $( "#tabs" ).tabs();
  $("#addRF").click(function(){
	  toAddFollowing();
  });
});
function toAddFollowing(){
	$.post( "${pageContext.request.contextPath}/console/interviewone/content/newfollowing.jsp",{
			'type':'add','qNo':"${IObean.qNo}"
		}, function(data){
		$( "#EditFollowing" ).html(data); 
		$( "#EditFollowing" ).dialog({
		      height:550,
		      width:800,
		      modal: true,
		      resizable: false,
		      draggable: false,
		      title:'訪查處理紀錄',
		      close: function( event, ui ) {
		    	  $("select[name='issueby'],select[name='progress']").select2('destroy');
		    	  $( "#EditFollowing" ).html(""); 
		      }
	});
	},"html");
}
</script>
<div id="EditFollowing"></div>
<!-- <div id="addFollowing"></div> -->
<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>後續追蹤管理</strong>&nbsp;</span>
		</legend>
		<jsp:include page="/console/interviewone/content/sysInfo.jsp" />
		<fieldset>
			<legend>
				<span style="color:#F30;">[處理情形]</span>&nbsp;
				<c:if test="${empty IOBaseInfo.canadd || IOBaseInfo.canadd!='1'}">
				<span><input id="addRF" type="button" class="btn_class_loaddata" value="新增訪查處理紀錄"></span>
				</c:if>
			</legend>
			<c:choose>
				<c:when test="${empty IMap && empty SMap }">
					尚無資料
				</c:when>
				<c:otherwise>
				<div id="tabs" style="font-size: 15px;margin-top: 20px;">
					<ul>
						<li><a href="#tabs-0">訪查處理紀錄</a></li>
						<li><a href="#tabs-1">基本資料</a></li>
						<li><a href="#tabs-2">確認事項</a></li>
						<li><a href="#tabs-3">營運狀態</a></li>
						<li><a href="#tabs-4">財務資料</a></li>
						<li><a href="#tabs-5">總評說明</a></li>
					</ul>
					<div id="tabs-0">
						<jsp:include page="/console/interviewone/content/tabszero.jsp"></jsp:include>
					</div>
					<div id="tabs-1">
						<jsp:include page="/interviewone/content/tabsone.jsp"></jsp:include>
					</div>
					<div id="tabs-2">
						<jsp:include page="/interviewone/content/tabstwo.jsp"></jsp:include>
					</div>
					<div id="tabs-3">
						<jsp:include page="/interviewone/content/tabsthree.jsp"></jsp:include>
					</div>
					<div id="tabs-4">
						<jsp:include page="/interviewone/content/tabsfour.jsp"></jsp:include>
					</div>
					<div id="tabs-5">
						<jsp:include page="/interviewone/content/tabsfive.jsp"></jsp:include>
					</div>
				</div>
				</c:otherwise>
			</c:choose>
		</fieldset>
	</fieldset>
</div>
