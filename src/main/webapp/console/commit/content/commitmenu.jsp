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
    <input type="radio" id="radio1" name="pMenu" value="<c:url value="/console/commit/showcommit.jsp" />"/><label for="radio1">承諾列表</label>
    <input type="radio" id="radio2" name="pMenu" value="<c:url value="/console/commit/commitaddinvestor.jsp" />" /><label for="radio2">新增承諾企業</label>
    <%-- <input type="radio" id="radio3" name="pMenu" value="<c:url value="/console/commit/commitreport.jsp" />" /><label for="radio3">新增執行情形</label> --%>
</div>

