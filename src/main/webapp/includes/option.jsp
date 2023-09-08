<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	CommonItemListService dao = new CommonItemListService();
	Map<String,List<CommonItemList>> map = dao.getCommonItemMap();
	request.setAttribute("map",map.get(request.getParameter("tt")));
	request.setAttribute("type",request.getParameter("tt"));
%>
<script type="text/javascript">
$(function(){
	setCheckCLIListener($("#CLITable .trDataRow").find("td:lt(3)"));
	setDeleteCLIListener($(".delCLI"));
	setEditCLIListener($(".editCLI"));
	setAddCLIListener();
});
function setCheckCLIListener($range){
	$range.click(function(){
		var $item=$(this).parents("tr").find(".CLI");
		if($item.is(":checked")){
			$item.prop("checked",false);
		}else{
			$item.prop("checked",true);
		}
	});
}
function setDeleteCLIListener($range){
	$range.click(function(){
		var $input=$(this).parents("tr");
		$("<div style='font-size='12px;'>您即將刪除本筆資料，刪除後將無法回復，請確認是否繼續?</div>").dialog({
			width: 350,
			modal:true,
			title:'刪除此筆資料',
			buttons: {
		        "確定": function() {
		        	$.post( "${pageContext.request.contextPath}/commonlist.jsp",
							{"edittype": "unable","type":"${type}","idno":$input.find(".CLI").val()},
						function(data){
							$input.remove();
							reRankCLITable();
							alert("刪除完成。");
					},"json");
		          $( this ).dialog( "close" );
		        },
		        "取消": function() {
		          $( this ).dialog( "close" );
		        }
			}
		});
	});
}
function setEditCLIListener($range){
	$range.click(function(){
		var $input=$(this).parents("tr").find("td:eq(2)");
		var content;
		if($(this).val()==="編輯"){
			content="<input type='text' value='"+$input.text()+"' name='content' class='CLIedit' style='width:95%;'/>";
			$input.html(content);
			$(this).val("儲存");	
		}else if($(this).val()==="儲存"){
			content=$input.find(".CLIedit").val();
			if($.trim(content).length>0){
				$.post( "${pageContext.request.contextPath}/commonlist.jsp",
						{"edittype": "edit","type":"${type}","content":content,"idno":$(this).parents("tr").find(".CLI").val()},
					function(data){
						alert("編輯完成。");
				},"json");
				$input.html(content);
				$(this).parents("tr").find(".CLI").prop("checked",true);
				$(this).val("編輯");	
			}else{
				alert("不可為空白，如不須此選項，請改點選刪除。");
			}
		}
	});
}
function setAddCLIListener(){
	$(".AddCLI").click(function(){
		var $newCLI=$("input[name='newCLI']");
		var content=$newCLI.val();
		var len=$(".CLI").length+1;
		if($.trim(content).length>0){
			$.post( "${pageContext.request.contextPath}/commonlist.jsp",
					{"edittype": "add","type":"${type}","content":content},
				function(data){
					var str='';
					str+='<tr class="trCenter trDataRow"><td>('+len+')</td>';
					str+='<td><input type="checkbox" value="'+data[0].idno+'" name="itemStr" class="CLI"></td>';
					str+='<td style="width: 70%;" class="trLeft" >'+data[0].content+'</td>';
					str+='<td><input type="button" class="editCLI" value="編輯"></td>';
					str+='<td><input type="button" class="delCLI" value="刪除"></td></tr>';
					$("#CLITable").append(str);
					$newCLI.val("");
					setCheckCLIListener($("#CLITable tr:last").find("td:lt(3)"));
					setDeleteCLIListener($("#CLITable tr:last").find(".delCLI"));
					setEditCLIListener($("#CLITable tr:last").find(".editCLI"));
					$("#CLITable tr:last").find(".CLI").prop("checked",true);
					alert("新增完成。");
			},"json");
		}else{
			alert("不可新增空白選項。");
		}
	});
}
function reRankCLITable(){
	$("#CLITable .trDataRow").find("td:eq(0)").each(function(index){
		var len = parseInt(index)+1;
		$(this).html("("+len+")");
	});
}
</script>
	<table class="formProj" id="CLITable">
		<tr>
			<th colspan="5">新增為常用清單</th>
		</tr>
		<tr>
			<td colspan="4"><input type="text" style="width: 95%;" name="newCLI"/></td>
			<td class="trCenter"><input type="button" class="AddCLI" value="加入"></td>
		</tr>
		<tr>
			<th colspan="5">常用清單</th>
		</tr>
		<c:forEach var="x" items="${map}" varStatus="i">
			<tr class="trCenter trDataRow">
				<td>(${i.index+1})</td>
				<td><input type="checkbox" value="${x.idno}" name="itemStr" class="CLI"></td>
				<td style="width: 70%;" class="trLeft" >${x.cname}</td>
				<td><input type="button" class="editCLI" value="編輯"></td>
				<td><input type="button" class="delCLI" value="刪除"></td>
			</tr>
		</c:forEach>
	</table>