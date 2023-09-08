<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
  $(function() {
    $( "#radio" ).buttonset();
    var nowRadio="#radio"+"${param.pos}";
    $(nowRadio).prop("checked",true);
    $( "#radio" ).buttonset("refresh");
    $( "input[name='pMenu']").change(function(){
    	var hrefPmenu=$(this).val();
    	window.location="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}"+hrefPmenu;
    });
  });
</script>

<div id="radio">
    <input type="radio" id="radio1" name="pMenu" value="<c:url value="/console/project/showproject.jsp" />"/><label for="radio1">專案列表</label>
    <input type="radio" id="radio2" name="pMenu" value="<c:url value="/console/project/projectqreport.jsp" />" /><label for="radio2">新增季報</label>
    <input type="radio" id="radio3" name="pMenu" value="<c:url value="/console/project/projectyreport.jsp" />" /><label for="radio3">新增年報</label>
    <c:if test="${param.pos eq '4'}">
    	<input type="radio" id="radio4" name="pMenu" value="<c:url value="/console/project/projectreport.jsp" />" /><label for="radio4">編輯報表</label>
    </c:if>
   	<input type="radio" id="radio5" name="pMenu" value="<c:url value="/console/project/listrpconfirm.jsp" />" /><label for="radio5">線上申報待確認</label>
</div>

