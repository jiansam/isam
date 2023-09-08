<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div style="display: none;" id="editRange">
	<div style="font-size: 14px;text-align: right;margin-bottom: 5px;">
		<input type="button" class="btn_class_opener" value="一組預設" onclick="setTeamOne();">
		<input type="button" class="btn_class_opener" value="二組預設" onclick="setTeamTwo();">
		<input type="button" class="btn_class_opener" value="四組預設" onclick="setTeamFour();">
		<input type="button" class="btn_class_opener" value="清空" onclick="clearAuthority();">
	</div>
	<table class="datatableApproval">
		<tr>
			<th rowspan="2">權限項目</th>
			<th rowspan="2">前台查閱</th>
			<th colspan="2">後台</th>
		</tr>
		<tr>
			<th>新增</th>
			<th>修改</th>
		</tr>
		<tr>
			<td class="tdLeft">大陸投資核准資料</td>
			<td><input type="checkbox" class="selectNext defaultSelect" value="2"></td>
			<td><input type="checkbox" class="selectNext"  value="2"></td>
			<td><input type="checkbox" class="selectNext"  value="2"></td>
		</tr>
		<tr>
			<td class="tdLeft">&nbsp;&nbsp;&nbsp;&nbsp;專案審查</td>
			<td><input type="checkbox" name="range" value="R0101" class="beSelectedNext defaultSelect"></td>
			<td><input type="checkbox" name="range" value="A0101" class="beSelectedNext"></td>
			<td><input type="checkbox" name="range" value="E0101" class="beSelectedNext"></td>
		</tr>
		<tr>
			<td class="tdLeft">&nbsp;&nbsp;&nbsp;&nbsp;承諾事項</td>
			<td><input type="checkbox" name="range" value="R0102" class="beSelectedNext defaultSelect"></td>
			<td><input type="checkbox" name="range" value="A0102" class="beSelectedNext"></td>
			<td><input type="checkbox" name="range" value="E0102" class="beSelectedNext"></td>
		</tr>
		<tr>
			<td class="tdLeft">陸資管理</td>
			<td><input type="checkbox" name="range" value="R0401" class="beSelectedNext firstItem defaultSelect"></td>
			<td><input type="checkbox" name="range" value="A0401" class="beSelectedNext firstItem"></td>
			<td><input type="checkbox" name="range" value="E0401" class="beSelectedNext firstItem"></td>
		</tr>
		<tr>
			<td class="tdLeft">營運狀況調查</td>
			<td><input type="checkbox" name="range" value="R0201" class="defaultSelect"></td>
			<td><input type="checkbox" name="range" value="A0201" class="beSelectedNext firstItem"></td>
			<td><input type="checkbox" name="range" value="E0201" class="beSelectedNext firstItem"></td>
		</tr>
		<tr>
			<td class="tdLeft">訪查資料</td>
			<td><input type="checkbox" class="selectNext defaultSelect" value="3"></td>
			<td><input type="checkbox" class="selectNext" value="3"></td>
			<td><input type="checkbox" class="selectNext" value="3"></td>
		</tr>
		<tr>
			<td class="tdLeft">&nbsp;&nbsp;&nbsp;&nbsp;其他實地訪查</td>
			<td><input type="checkbox" name="range" value="R0301" class="beSelectedNext defaultSelect"></td>
			<td><input type="checkbox" name="range" value="A0301" class="beSelectedNext"></td>
			<td><input type="checkbox" name="range" value="E0301" class="beSelectedNext"></td>
		</tr>
		<tr>
			<td class="tdLeft">&nbsp;&nbsp;&nbsp;&nbsp;大陸投資實地訪查</td>
			<td><input type="checkbox" name="range" value="R0302" class="beSelectedNext defaultSelect"></td>
			<td><input type="checkbox" name="range" value="A0302" class="beSelectedNext"></td>
			<td><input type="checkbox" name="range" value="E0302" class="beSelectedNext"></td>
		</tr>
		<tr>
			<td class="tdLeft">&nbsp;&nbsp;&nbsp;&nbsp;陸資實地訪查</td>
			<td><input type="checkbox" name="range" value="R0303" class="beSelectedNext defaultSelect"></td>
			<td><input type="checkbox" name="range" value="A0303" class="beSelectedNext"></td>
			<td><input type="checkbox" name="range" value="E0303" class="beSelectedNext"></td>
		</tr>
	</table>
</div>
