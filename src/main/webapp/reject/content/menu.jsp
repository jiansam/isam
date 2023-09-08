<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	#radio{
		Font-size:13px !important;
		padding: 0.2em 0.2em 0;
		/* margin-left:5px; */
		/* text-align: right; */
	}	
</style>
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
    <input type="radio" id="radio1" name="pMenu" value="<c:url value="/reject/showlist.jsp" />"/><label for="radio1">未核准列表</label>
    <c:if test="${param.pos eq '2'}">
    	<input type="radio" id="radio2" name="pMenu" value="<c:url value="/reject/showform.jsp" />" /><label for="radio2">檢視未核准案件</label>
    </c:if>
    <input type="radio" id="radio3" name="pMenu" value="<c:url value="/reject/showsummary.jsp" />" /><label for="radio3">駁回案統計資料</label>
</div>

