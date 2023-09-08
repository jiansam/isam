<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
$(function() {
	var y=$.extend(sdInitDataTableSetting(),sdSortChinese(), sdSortCheckbox(),{
		"aoColumnDefs": [{ 'sClass':'center', "aTargets": [ 0,1] },
		                 {"sSortDataType":"chinese", "aTargets": [4]},
		                 {"sSortDataType":"dom-checkbox", "aTargets": [1]},
		                 {"bSortable": false, "aTargets": [0]}
		              ],
		              "aaSorting": [[ 2, "desc" ]],
				   "fnDrawCallback": function ( oSettings ) {
						/* Need to redo the counters if filtered or sorted */
						if ( oSettings.bSorted || oSettings.bFiltered ){
							for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ ){
								$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html("("+(i+1)+")");
							}
						}
					}
	});
	var oTable=$("#reInvestList").dataTable(y);
	$("body").on('click',"#reInvestList tbody tr td",function(){
		var ntr= $(this).parent();
		$('td:eq(1)',ntr).find("input[name='reinvestNo']").click();
	});
	$("#selectAll").click(function(){
		$("input[name='reinvestNo']", oTable.fnGetNodes()).each(function(){
			$(this).prop("checked",true);
		});
	});
	$("#clearAll").click(function(){
		$("input[name='reinvestNo']", oTable.fnGetNodes()).each(function(){
			$(this).prop("checked",false);
		});
	});
	$("#submitAll").click(function(){
		var params="";
		params+="<input type='hidden' value='"+$("select[name='nowyear']").val()+"' name='year' />";
		$("input[name='reinvestNo']:checked", oTable.fnGetNodes()).each(function(){
			params+="<input type='hidden' value='"+$(this).val()+"' name='reinvestNo' />";
		});
		var actionUrl=getRootPath()+'/console/interviewone/addreinvestment.jsp';
		var $form=$("<form></form>",{"method":"post","action":actionUrl});
		$form.append(params);
		$('body').append($form);
		$form.submit();
	});
});
</script>
<div>
	<div style="float: right;margin-bottom: 20px;">
		<input id="selectAll" class="btn_class_opener" type="button" value="全選">
		<input id="clearAll" class="btn_class_opener" type="button" value="清空">
	</div>
		<table id="reInvestList">
			<thead>
			<tr>
				<th>序號</th>
				<th>選取</th>
				<th>陸資案號</th>
				<th>統編</th>
				<th>轉投資事業名稱</th>
			</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${reInvestList}">
					<tr>
						<td></td>
						<td>
							<input type="checkbox" name="reinvestNo" value="${reInvestBase[item].reInvestNo}">
						</td>
						<td>${reInvestBase[item].investNo}</td>
						<td>${reInvestBase[item].idno}</td>
						<td>${reInvestBase[item].cname}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</div>
<div style="margin-bottom: 5px;margin-top: 45px;text-align: center;"><input id="submitAll" class="btn_class_opener" type="button" value="送出"></div>
			
