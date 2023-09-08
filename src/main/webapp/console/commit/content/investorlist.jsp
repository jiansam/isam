<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/investor.js'/>" type="text/javascript"></script>

<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;新增承諾企業&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/console/commit/content/commitmenu.jsp" flush="true">
				<jsp:param value="2" name="pos"/>
			</jsp:include>
		</div>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;新增承諾企業&nbsp;]</span>&nbsp;&nbsp;
				</legend>
			   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;margin:5px;font-size: 16px;">
			   	<strong style="color:#222;">&nbsp;請輸入查詢條件&nbsp;</strong>
			   	<form action="">
				    <table style="width: 95%;padding-left: 20px;">
						<tr>
							<td class="tdRight">統一編號：</td>				
							<td><input type="text" name="IDNO" maxlength="8" /></td>				
							<td class="tdRight">&nbsp;&nbsp;&nbsp;&nbsp;投資人名稱：</td>				
							<td><input type="text" name="investName" style="width:230px;" /></td>				
							<td>
								<input type="button" id="mySearch" class="btn_class_opener" value="查詢"/>
								<input type="reset" class="btn_class_opener" value="清空"/>
							</td>					
						</tr>					
				   	</table>
				</form>
			   	</div>
				<div style="display: none;" id="divExam">
					<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
						<legend style="width:100%;border-top:1px solid #E6E6E6">
							<span style="color:#F30;">[&nbsp;可新增選項&nbsp;]</span>&nbsp;&nbsp;
						</legend>
							<table id="example" class="display" style="width: 980px;padding: 5 10px; "> 
								<thead>
									<tr>
										<th>No</th>
										<th>統編</th>
										<th>投資人名稱</th>										
									</tr>
								</thead>
								<tbody class="SetInvestors">
								</tbody>
							</table>
					</fieldset>
				</div>
			</fieldset>
		</div>
	</fieldset>
</div>
