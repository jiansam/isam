<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
  $(function() {
    $( "#radio" ).buttonset();
    var nowRadio="#radio"+"${param.pos}";
    $(nowRadio).prop("checked",true);
    $( "#radio" ).buttonset("refresh");
    $( "input[name='pMenu']").click(function(){
    	var hrefPmenu=$(this).val();
    	window.location="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}"+hrefPmenu;
    });
  });
</script>

<div id="radio">
    <input type="radio" id="radio1" name="pMenu" value="<c:url value="/console/interviewone/showiolist.jsp" />"/><label for="radio1">陸資實地訪查列表</label>
    <input type="radio" id="radio2" name="pMenu" value="<c:url value="/console/interviewone/getinterviewone.jsp" />" /><label for="radio2">新增訪視紀錄</label>
    <input type="radio" id="radio3" name="pMenu" value="<c:url value="/console/interviewone/getsurveyone.jsp" />" /><label for="radio3">新增營運問卷</label>
    <input type="radio" id="radio4" name="pMenu" value="<c:url value="/console/interviewone/showiolist.jsp?action=manage" />" /><label for="radio4">管理年度訪視清單及上傳附檔</label>
    <input type="radio" id="radio5" name="pMenu" value="<c:url value="/console/interviewone/showfollowing.jsp" />" /><label for="radio5">後續追蹤列表</label>
</div>
