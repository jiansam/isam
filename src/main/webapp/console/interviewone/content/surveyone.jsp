<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<%@ taglib prefix="console" tagdir="/WEB-INF/tags/console" %>

<fieldset>
	<legend>
		<span style="color:#F30;">[填寫問卷資料]</span>&nbsp;
		<input type="hidden" name='qType' value="S">
	</legend>
	<div style="width: 100%;">
	<table class="formProj">
		<tr>
			<th colspan="4">經濟部<span>${IOyear}</span>年陸資在台事業營運調查問卷</th>
		</tr>
		<tr>
			<td class="trRight"><span style="color: red;">*</span>填表日期：</td>
			<td colspan="3">
			<c:if test="${not empty ioclists.reportdate}">
				<c:set var="date">${fn:substring(ioclists.reportdate,0,3)}/${fn:substring(ioclists.reportdate,3,5)}/${fn:substring(ioclists.reportdate,5,7)}</c:set>
			</c:if>
				<input id="singledate" type="text" name="reportdate" value="${date}"/>	
				<input type="hidden" name="surveyStatus" value="1"/>
			</td>
			<!-- <td class="trRight" style="white-space: nowrap;">問卷狀態：</td>
			<td>
				<input type="radio" name="surveyStatus" value="1" id="ss0"/><label for="ss0">已完整</label>
				<input type="radio" name="surveyStatus" value="2" id="ss1" checked="checked"/><label for="ss1">未完整</label>
			</td> -->
		</tr>
		<tr>
			<td class="trRight"><span style="color: red;">*</span>填表人姓名：</td>
			<td ><input type="text" name="reporter" style="width: 250px;" value="${ioclists.reporter}"/></td>
			<td class="trRight">職稱：</td>
			<td><input type="text" name="position" style="width: 350px;" value="${ioclists.position}"/></td>
		</tr>
		<tr>
			<td class="trRight">聯絡電話：</td>
			<td>
				<c:set var="tel" value="${fn:split(ioclists.telNo,'-')}"></c:set>
				<c:set var="telLast" value="${tel[2]}"></c:set>
				<c:if test="${fn:indexOf(telLast,'#')!=-1}">
					<c:set var="telEx" value="${fn:substringAfter(telLast,'#')}"></c:set>
					<c:set var="telLast" value="${fn:substringBefore(telLast,'#')}"></c:set>
				</c:if>
				<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${tel[0]}"/><label>-</label>
				<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${tel[1]}"/><label>-</label>
				<input type="text" style="width: 50px;" maxlength="4" class="telNo" value="${telLast}"/><label>&nbsp;分機&nbsp;</label>
				<input type="text" style="width: 50px;" class="telNo optionalText" value="${telEx}"/>
				<input type="hidden" value="" name="telNo" />
			</td>
			<td class="trRight">手機：</td>
			<td>
				<c:set var="cellphone" value="${fn:split(ioclists.cellphone,'-')}"></c:set>
				<input type="text" style="width: 50px;" maxlength="4" class="cellphone" value="${cellphone[0]}"/><label>-</label>
				<input type="text" style="width: 50px;" maxlength="3" class="cellphone" value="${cellphone[1]}"/><label>-</label>
				<input type="text" style="width: 50px;" maxlength="3" class="cellphone" value="${cellphone[2]}"/>
				<input type="hidden" value="" name="cellphone" />
			</td>
		</tr>
		<tr>
			<td class="trRight">事業所在地：</td>
			<td colspan="3">
				<jsp:include page="/includes/twaddr.jsp" flush="true">
					<jsp:param value="City" name="City"/>
					<jsp:param value="Town" name="Town"/>
					<jsp:param value="Addr" name="Addr"/>
					<jsp:param value="${ioclists.Addr}" name="AddrStr"/>
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td class="trRight" style="width:180px;" nowrap>營所稅營業收入業別：</td>
			<td colspan="3">
				<console:BusinessIncomeTaxCode selected="${ioclists.businessIncomeTaxCode}" />
			</td>
		</tr>
		<tr>
			<th colspan="4">營業場所概況</th>
		</tr>
		<tr>
			<td class="trRight" style="white-space: nowrap;">分支機構及門市：</td>
			<td colspan="3">
				<input type="radio" name="branch" id="b0" value="0" checked="checked"/><label for="b0">1.無</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="radio" name="branch" id="b1" value="1"/><label for="b1">2.有，分支機構及門市數：</label>
				<input type="text"  name="branchcount" style="width: 150px;" class="numberFmt" value="${ioclists.branchcount}"/>
			</td>
		</tr>
		<tr>
			<td class="trRight" rowspan="3">營業總坪數：</td>
			<td colspan="3">
				&nbsp;1.租賃，<input type="text" name="rentOS" style="width: 90px;" class="numberFmt" value="${ioclists.rentOS}"/>&nbsp;坪；&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				2.購買自有，<input type="text" name="ownOS" style="width: 90px;" class="numberFmt" value="${ioclists.ownOS}"/>&nbsp;坪。
			</td>
		</tr>
		<tr>
			<td colspan="3">
				&nbsp;合計總坪數：<span id="TextOS" style="width: 85px; display: -moz-inline-box;display: inline-block;" class="numberFmt">${ibfn:SToD(ioclists.rentOS)+ibfn:SToD(ioclists.ownOS)}</span>&nbsp;坪。
			</td>
		</tr>
		<tr>
			<td colspan="3">
				(包含：辦公室、分支機構、門市、事務所、工廠、倉庫等)
			</td>
		</tr>
		<tr>
			<th colspan="4">專職員工僱用概況</th>
		</tr>
		<tr>
			<td class="trRight">員工總人數：</td>
			<td colspan="3">
				&nbsp;<span id="TextXemp" style="width: 85px; display: -moz-inline-box;display: inline-block;" class="numberFmt">${ibfn:SToD(ioclists.twXemp)+ibfn:SToD(ioclists.foreignXemp)+ibfn:SToD(ioclists.cnXemp)}</span>&nbsp;人。
			</td>
		</tr>
		<tr>
			<td class="trRight">包含：</td>
			<td colspan="3">
				&nbsp;1.台灣員工：<input type="text" name="twXemp" style="width: 90px;" class="numberFmt" value="${ioclists.twXemp}"/>&nbsp;人；&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				2.外國(含港澳)員工：<input type="text" name="foreignXemp" style="width: 90px;" class="numberFmt" value="${ioclists.foreignXemp}"/>&nbsp;人；&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<br />
				&nbsp;3.陸籍駐台人員：<input type="text" name="cnXemp" style="width: 90px;" class="numberFmt" value="${ioclists.cnXemp}"/>&nbsp;人；
				(含投資經營管理甲類 <input type="text" name="cnXempA" style="width: 90px;" class="numberFmt" value="${ioclists.cnXempA}"/> 人、
				乙類 <input type="text" name="cnXempB" style="width: 90px;" class="numberFmt" value="${ioclists.cnXempB}"/> 人)
			</td>
		</tr>
		<tr>
			<th colspan="4">簡明財務概況(${IOlastyear}年)</th>
		</tr>
		<tr>
			<td class="trRight">資料來源：</td>
			<td colspan="4">
				<input type="checkbox" name="dsFinancial" class="tabBox" id="tBox0" value="0"/><label for="tBox0">1.會計師財務簽證報告書</label>
				<input type="checkbox" name="dsFinancial" class="tabBox" id="tBox1" value="1"/><label for="tBox1">2.營利事業所得稅結算申報書</label>
				<input type="checkbox" name="dsFinancial" class="tabBox" id="tBox2" value="2"/><label for="tBox2">3.暫結報表</label>
				<input type="checkbox" name="dsFinancial" class="tabBox" id="tBox3" value="3"/><label for="tBox3">4.新設立尚未跨年度營業</label><br/>
				<input type="checkbox" name="dsFinancial" class="tabBox" id="tBox4" value="4"/><label for="tBox4">5.查定稅額小規模營業人</label>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<jsp:include page="/console/interviewone/content/financialRP.jsp" flush="true" />
			</td>
		</tr>
		<tr>
			<th colspan="4">營運模式</th>
		</tr>
		<tr>
			<td class="trRight">營運模式(可複選)：</td>
			<td colspan="4">
				<input type="checkbox" name="OSMode" id="OSMode0" value="1"/><label for="OSMode0">1.生產製造</label>
				<input type="checkbox" name="OSMode" id="OSMode1" value="2"/><label for="OSMode1">2.技術服務(如維修、研發設計、檢測等)</label>
				<input type="checkbox" name="OSMode" id="OSMode2" value="3"/><label for="OSMode2">3.非技術服務</label>
			</td>
		</tr>
		<tr>
			<th colspan="4">備註</th>
		</tr>
		<tr>
			<td colspan="4" style="text-align: center;">
				<textarea rows="10" style="width: 99%;" name="surveynote">${ioclists.surveynote}</textarea>
			</td>
		</tr>
	</table>
	</div>
</fieldset>