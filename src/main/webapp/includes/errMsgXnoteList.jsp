<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div style="background:#fff;" >

	<form >
		<table id="errMsg_table" style="width:100%;display:none;">
			<thead>
				<tr>
					<th nowrap="nowrap" align="center" width="25%">選擇</th>
					<th align="center" >常用清單</th>
					<th align="center">清單類型</th>
				</tr>
			</thead>
		
			<tbody id="rows">
			</tbody>
			
			<tfoot>
				<tr style="display:none;" id="template">
					<td class="ckbox" align="center" >
						<input name="err-box" type="checkbox" value="" style="height:17px;width:17px;"></td>
					<td class="name"></td>
					<td class="type"></td>
				</tr>
			</tfoot>
		</table>
	</form>
</div>
<br><br>
		<div style="text-align: center;"><input name="getErrbtn" type="button" value="選擇" style="width:30%;" class="btn_class_opener">
		</div>