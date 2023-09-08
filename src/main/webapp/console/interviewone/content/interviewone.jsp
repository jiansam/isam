<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript">
$(function(){
	if("${IOBaseInfo.interviewStatus}".length===0||'${IOBaseInfo.interviewStatus}'==1){
		$("input[name='interviewStatus'][value='1']").prop("checked",true);
		getINeedContext();
	}else{
		$("input[name='interviewStatus'][value='${IOBaseInfo.interviewStatus}']").prop("checked",true);
		getINoNeedContext();
	}
	$("input[name='interviewStatus']").change(function(){
		var ist=parseInt($(this).val(),10);
		if(ist===1){
			getINeedContext();
		}else if(ist===9){
			getINoNeedContext();
		}
	});
});
function getINeedContext(){
	$.post( "${pageContext.request.contextPath}/console/interviewone/content/interviewoneneed.jsp",{
	}, function(data){
	$( "#iContext" ).html(data); 
},"html");
}
function getINoNeedContext(){
	$.post( "${pageContext.request.contextPath}/console/interviewone/content/interviewonenoneed.jsp",{
	}, function(data){
		$( "#iContext" ).html(data); 
	},"html").done(function(){
		var $Y=$("input[name='reportdate']");
		if($Y.val().length===0){
			var d = new Date();
			var yTW=d.getFullYear() -1911;
			$Y.val(yTW+"/01/01");
		}
	});
}
</script>
<fieldset>
	<legend>
		<span style="color:#F30;">[填寫訪視紀錄]</span>&nbsp;
		<input type="hidden" name='qType' value="I">
	<c:if test="${IOBaseInfo.editType eq 'edit'}">
		<span style="float: right;">
			<input type="button" class="btn_class_opener" value="刪除訪視記錄表" onclick='window.location.href="${pageContext.request.contextPath}/console/interviewone/showiolist.jsp"'>
		</span>

	</c:if>
	</legend>
	<div style="width: 100%;">
	<table class="formProj">
		<tr>
			<td style="width: 15%;" class="trRight">訪視處理情形：</td>
			<td colspan="2">
				<input type="radio" name="interviewStatus" value="1" id="is1"/><label for="is1">已訪視</label>
				<input type="radio" name="interviewStatus" value="9" id="is9"/><label for="is9">未訪視</label>
			</td>
		</tr>
	</table>
	</div>
	<div style="padding-top: 10px;"></div>
	<div id="iContext">
	</div>
</fieldset>