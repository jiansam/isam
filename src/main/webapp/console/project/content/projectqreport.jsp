<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/css/textareafont.css'/>" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="<c:url value='/js/project.js'/>"></script>
<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/projectreport.js'/>"></script>

<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;新增季報資料&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/console/project/content/projectmenu.jsp" flush="true">
				<jsp:param value="2" name="pos"/>
			</jsp:include>
		</div>
		<div>
			<form id="projRForm" action="<c:url value='/console/project/projectreportedit.jsp'/>" method="post">
				<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;新增季報基本資料&nbsp;]</span>
				</legend>
				<span class="basetable">
					投資案號：<input type="text" maxlength="6" name="investNo" class="pInvestNo"/>
					<img class="myWait" src="<c:url value='/images/loading.gif' />" title="please wait" style="width: 20px;display: none;"/>
				</span>
				<span class="basetable">
					收文文號：<input type="text" maxlength="11" name="pNo"/><span id="reNoCount"></span>
				</span>
				<span class="basetable">
					填報季度：
					<select id="pYear" style="width: 80px;" name="pYear">
					</select>
					<select name="pSeason" id="pSeason" style="width: 80px;">
						<option value="1">第1季</option>
						<option value="2">第2季</option>
						<option value="3">第3季</option>
					</select>
				</span>
				<br/>				
				<span class="basetable setCnName" style="display: none;">
				</span>
				<span class="basetable setInvestor" style="display: none;">					
				</span>
				<br/>
				<span class="basetable">
				本次免申報：
						<input type="radio" name="noNeed" id="noNeed1" value="1"/>
						<label for="noNeed1">免申報</label>
						<input type="radio" name="noNeed" id="noNeed0" value="0" checked="checked"/>
						<label for="noNeed0">應申報</label>
				</span><br/>
				<span class="basetable">
				免申報備註：<textarea name="noNeedNote" style="width: 65%;vertical-align: text-top;" cols="50" rows="5">${crBean.note}</textarea>
				</span>
				<br/>
				<span class="basetable">
				轉換幣別：<input type="radio" name="isConversion" id="isConversionY" value="Y"/>
						<label for="isConversionY">是</label>
						<input type="radio" name="isConversion" id="isConversionN" value="N" checked="checked"/>
						<label for="isConversionN">否</label>
				</span><br/>
				<span class="basetable">
				轉換備註：<textarea name="note" style="width: 65%;vertical-align: text-top;" cols="50" rows="5">${crBean.note}</textarea>
				</span>
			</fieldset>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;新增文號&nbsp;]</span>
					<input type="button" class="selectThispage btn_class_opener" value="本頁全選"/>				
					<input type="button" class="unselectThispage btn_class_opener" value="本頁全刪"/>	
				</legend>
				<div id="help">
					<table class="receiveTable" class="display" style="width: 95%;margin-left: 15px;"> 
						<thead>
							<tr>
								<th>選擇</th>
								<th>核准日期</th>
								<th>文號</th>
								<th>案由</th>
							</tr>
						</thead>
						<tbody class="SetReceive">
						</tbody>
					</table>
				</div>
			</fieldset>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;新增季報執行情形&nbsp;]</span>
				</legend>
				<div style="margin:5px 10px;color:#222;">1.資金匯出、核備情形（金額：美元）</div>
				<table class="formProj">
					<tr>
						<th>本季匯出投資金額</th>
						<th>累計核准投資金額</th>
						<th>累計已核備投資金額</th>
						<th>累計已實行投資比例</th>								
					</tr>
					<tr  class="trRight">
						<td>
							<input type="text" name="outwardMoney" style="width: 95%;" class="numberFmt"/>
						</td>
						<td>
							<input type="text" name="approvalMoney" style="width: 95%;" class="numberFmt pDown"/>
						</td>
						<td>
							<input type="text" name="approvedMoney" style="width: 95%;" class="numberFmt pUp"/>
						</td>
						<td>
							<span class="pPercent"></span>
							<input type="hidden" name="isOnline" value="N"/>
							<input type="hidden" name="repType" value="Q"/>
							<input type="hidden" name="edittype" value="insert"/>
							<input type="hidden" name="url" value="${pageContext.request.requestURI}"/>
						</td>
					</tr>
						<tr>
						<th>本季匯出投資金額_備註</th>
						<th>累計核准投資金額_備註</th>
						<th>累計已核備投資金額_備註</th>
						<th></th>								
					</tr>
					<tr>
						<td>
							<textarea rows="3" style="width: 95%;" name="outwardNote"></textarea>
						</td>
						<td>
							<textarea rows="3" style="width: 95%;" name="approvalNote"></textarea>
						</td>
						<td>
							<textarea rows="3" style="width: 95%;" name="approvedNote"></textarea>
						</td>
						<td></td>
					</tr>
				</table>
			</fieldset>
			<div style="text-align: center;">
				<input type="button" id="myInsert" class="btn_class_Approval" value="確認新增"/>
			</div>
			</form>
		</div>
	</fieldset>
</div>