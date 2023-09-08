<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="tabs">
	<ul>
		<li><a href="#tab-1">會計師財務簽證報告書</a></li>
		<li><a href="#tab-2">營利事業所得稅結算申報書</a></li>
		<li><a href="#tab-3">暫結報表</a></li>
		<li><a href="#tab-4">新設立尚未跨年度營業</a></li>
		<li><a href="#tab-5">查定稅額小規模營業人</a></li>
	</ul>
	<div id="tab-1">
		<table class="formProj">
			<tr>
				<th colspan="4">資產負債表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年底股東(業主)權益：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="SE_0" style="width: 200px;" class="numberFmt" value="${ioclists.SE_0}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年底資產總額：</td>
				<td>新台幣</td>
				<td><input type="text" name="asset_0" style="width: 200px;" class="numberFmt" value="${ioclists.asset_0}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr>
				<th colspan="4">損益表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年度營業收入淨額：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="NI_0" style="width: 200px;" class="numberFmt" value="${ioclists.NI_0}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業成本：</td>
				<td>新台幣</td>
				<td><input type="text" name="cost_0" style="width: 200px;" class="numberFmt" value="${ioclists.cost_0}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業費用：</td>
				<td>新台幣</td>
				<td><input type="text" name="expense_0" style="width: 200px;" class="numberFmt" value="${ioclists.expense_0}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益：</td>
				<td>新台幣</td>
				<td><input type="text" name="NP_0" style="width: 200px;" class="numberFmt" value="${ioclists.NP_0}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益狀況：</td>
				<td class="trLeft" colspan="3"><span class="PLStatus" style="margin-left: 5px;">無</span></td>
			</tr>
		</table>
	</div>
	<div id="tab-2">
		<table class="formProj">
			<tr>
				<th colspan="4">資產負債表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年底股東(業主)權益：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="SE_1" style="width: 200px;" class="numberFmt" value="${ioclists.SE_1}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年底資產總額：</td>
				<td>新台幣</td>
				<td><input type="text" name="asset_1" style="width: 200px;" class="numberFmt" value="${ioclists.asset_1}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr>
				<th colspan="4">損益表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年度營業收入淨額：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="NI_1" style="width: 200px;" class="numberFmt" value="${ioclists.NI_1}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業成本：</td>
				<td>新台幣</td>
				<td><input type="text" name="cost_1" style="width: 200px;" class="numberFmt" value="${ioclists.cost_1}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業費用：</td>
				<td>新台幣</td>
				<td><input type="text" name="expense_1" style="width: 200px;" class="numberFmt" value="${ioclists.expense_1}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益：</td>
				<td>新台幣</td>
				<td><input type="text" name="NP_1" style="width: 200px;" class="numberFmt" value="${ioclists.NP_1}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益狀況：</td>
				<td class="trLeft" colspan="3"><span class="PLStatus" style="margin-left: 5px;">無</span></td>
			</tr>
		</table>
	</div>
	<div id="tab-3">
	<table class="formProj">
			<tr>
				<th colspan="4">資產負債表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年底股東(業主)權益：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="SE_2" style="width: 200px;" class="numberFmt" value="${ioclists.SE_2}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年底資產總額：</td>
				<td>新台幣</td>
				<td><input type="text" name="asset_2" style="width: 200px;" class="numberFmt" value="${ioclists.asset_2}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr>
				<th colspan="4">損益表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年度營業收入淨額：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="NI_2" style="width: 200px;" class="numberFmt" value="${ioclists.NI_2}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業成本：</td>
				<td>新台幣</td>
				<td><input type="text" name="cost_2" style="width: 200px;" class="numberFmt" value="${ioclists.cost_2}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業費用：</td>
				<td>新台幣</td>
				<td><input type="text" name="expense_2" style="width: 200px;" class="numberFmt" value="${ioclists.expense_2}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益：</td>
				<td>新台幣</td>
				<td><input type="text" name="NP_2" style="width: 200px;" class="numberFmt" value="${ioclists.NP_2}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益狀況：</td>
				<td class="trLeft" colspan="3"><span class="PLStatus" style="margin-left: 5px;">無</span></td>
			</tr>
		</table>
	</div>
	<div id="tab-4">
	<table class="formProj">
			<tr>
				<th colspan="4">資產負債表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年底股東(業主)權益：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="SE_3" style="width: 200px;" class="numberFmt" value="${ioclists.SE_3}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年底資產總額：</td>
				<td>新台幣</td>
				<td><input type="text" name="asset_3" style="width: 200px;" class="numberFmt" value="${ioclists.asset_3}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr>
				<th colspan="4">損益表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年度營業收入淨額：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="NI_3" style="width: 200px;" class="numberFmt" value="${ioclists.NI_3}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業成本：</td>
				<td>新台幣</td>
				<td><input type="text" name="cost_3" style="width: 200px;" class="numberFmt" value="${ioclists.cost_3}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業費用：</td>
				<td>新台幣</td>
				<td><input type="text" name="expense_3" style="width: 200px;" class="numberFmt" value="${ioclists.expense_3}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益：</td>
				<td>新台幣</td>
				<td><input type="text" name="NP_3" style="width: 200px;" class="numberFmt" value="${ioclists.NP_3}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益狀況：</td>
				<td class="trLeft" colspan="3"><span class="PLStatus" style="margin-left: 5px;">無</span></td>
			</tr>
		</table>
	</div>
	
	<div id="tab-5">
	<table class="formProj">
			<tr>
				<th colspan="4">資產負債表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年底股東(業主)權益：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="SE_4" style="width: 200px;" class="numberFmt" value="${ioclists.SE_4}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年底資產總額：</td>
				<td>新台幣</td>
				<td><input type="text" name="asset_4" style="width: 200px;" class="numberFmt" value="${ioclists.asset_4}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr>
				<th colspan="4">損益表部分(填寫之數字為該科目之合計數)</th>
			</tr>
			<tr class="trRight">
				<td style="width: 30%;">年度營業收入淨額：</td>
				<td style="width: 8%;">新台幣</td>
				<td style="width: 20%;"><input type="text" name="NI_4" style="width: 200px;" class="numberFmt" value="${ioclists.NI_4}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業成本：</td>
				<td>新台幣</td>
				<td><input type="text" name="cost_4" style="width: 200px;" class="numberFmt" value="${ioclists.cost_4}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度營業費用：</td>
				<td>新台幣</td>
				<td><input type="text" name="expense_4" style="width: 200px;" class="numberFmt" value="${ioclists.expense_4}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益：</td>
				<td>新台幣</td>
				<td><input type="text" name="NP_4" style="width: 200px;" class="numberFmt" value="${ioclists.NP_4}"></td>
				<td class="trLeft">元</td>
			</tr>
			<tr class="trRight">
				<td>年度稅後損益狀況：</td>
				<td class="trLeft" colspan="3"><span class="PLStatus" style="margin-left: 5px;">無</span></td>
			</tr>
		</table>
	</div>
</div>