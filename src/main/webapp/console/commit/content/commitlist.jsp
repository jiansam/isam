<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;承諾事項資料維護&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/console/commit/content/commitmenu.jsp" flush="true">
				<jsp:param value="1" name="pos"/>
			</jsp:include>
		</div>
		<div>
			<jsp:include page="/console/commit/content/commitsearch.jsp" flush="true" />
		</div>
		<div>
			<jsp:include page="/console/commit/content/commitlisttable.jsp" flush="true" />
		</div>
	</fieldset>
</div>
