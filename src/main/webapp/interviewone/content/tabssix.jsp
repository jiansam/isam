<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% 
request.setAttribute("vEnter", "\n"); 
%>
<div class='tbtitle'>關注項目</div>
<table style="width: 100%;font-size: 16px;padding-left: 15px;" class="tchange">
	<c:if test="${not empty IMap.focusItems}">
		<tr>
<!-- 			<td style="text-align: right;width: 18%;">關注項目：</td> -->
			<td colspan="2">${fn:replace(IMap.focusItems,vEnter,"<br>")}</td>
		</tr>
	</c:if>
</table>
