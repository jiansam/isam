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
    <input type="radio" id="radio1" name="pMenu" value="<c:url value="/survey/filledSurveyFile.management" ><c:param name="doThing" value="front"/></c:url>"/><label for="radio1">問卷內容下載</label>
    <input type="radio" id="radio2" name="pMenu" value="<c:url value="/survey/surveyFile.management" ><c:param name="doThing" value="front"/></c:url>" /><label for="radio2">空白問卷下載</label>
</div>

