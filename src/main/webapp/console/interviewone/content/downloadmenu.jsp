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
    <input type="radio" id="radio1" name="pMenu" value="<c:url value="/console/interviewone/datadownload.jsp" />"/><label for="radio1">陸資資料查詢</label>
    <input type="radio" id="radio2" name="pMenu" value="<c:url value="/console/interviewone/downloadbycompany.jsp" />" /><label for="radio2">依公司下載</label>
    <input type="radio" id="radio3" name="pMenu" value="<c:url value="/console/interviewone/downloadbyyear.jsp" />" /><label for="radio3">依年度下載</label>
    <input type="radio" id="radio4" name="pMenu" value="<c:url value="/console/interviewone/getdatareport.jsp" />" /><label for="radio4">訪查資料匯出</label>

</div>
