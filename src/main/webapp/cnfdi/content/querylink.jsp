<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
  $(function() {
    $( "#quickmenu" ).menu();
  });
</script>
<div style="float: left;width: 18%;padding-left: 15px;">
	<strong style="color:#222;">&nbsp;國內事業查詢&nbsp;</strong>
	<ul id="quickmenu" style="width: 180px;">
      	<li><a href="<c:url value='/cninvestment/approvallist.jsp'/>"><span class="ui-icon ui-icon-play"></span>已確認列表</a></li>
      	<li><a href="<c:url value='/cninvestment/nonapprovallist.jsp'/>"><span class="ui-icon ui-icon-play"></span>未確認列表</a></li>
	</ul>
</div>
