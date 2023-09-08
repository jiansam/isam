<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
$(function() {
	$("#mySearch").click(function(){
		toListCompany();
	});
});
function toListCompany(){
	var cname =$.trim($("input[name='investName']").val());
	var idno ="";
	if("${userInfo.company eq 'cier' || userInfo.company eq 'ibtech'}"==="true"){
		idno =$.trim($("input[name='IDNO']").val());
	}
	if(cname.length+idno.length>0){
		$.post( "${pageContext.request.contextPath}/console/reject/content/listmatch.jsp",{
				'cname':cname,'idno':idno
			}, function(data){
			$( "#divExam" ).html(data); 
		},"html");
	}else{
		alert("請填入國內事業名稱關鍵字！");
		$("input[name='investName']").focus();
	}
}
</script>
<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;管理未核准案件&nbsp;</strong>&nbsp;</span>
		</legend>
		<div>
			<jsp:include page="/console/reject/content/menu.jsp" flush="true">
				<jsp:param value="2" name="pos"/>
			</jsp:include>
		</div>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6;padding-top: 10px;">
					<span style="color:#F30;">[&nbsp;Step 1&nbsp;]</span>
				   	<strong style="color:#222;">&nbsp;查詢國內事業名稱&nbsp;</strong>
				</legend>
			   	<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;margin:5px;font-size: 16px;">
			   	<form action="">
				    <table style="width: 95%;padding-left: 20px;">
						<tr>
							<td class="trRight">&nbsp;&nbsp;&nbsp;&nbsp;國內事業名稱：</td>				
							<td class="trLeft"><input type="text" name="investName" style="width:350px;" /></td>	
							<c:if test="${userInfo.company eq 'cier' || userInfo.company eq 'ibtech'}">			
							<td class="trRight">統一編號：</td>				
							<td><input type="text" name="IDNO" maxlength="8"/></td>	
							</c:if>			
							<td>
								<input type="button" id="mySearch" class="btn_class_opener" value="查詢"/>
								<input type="reset" class="btn_class_opener" value="清空"/>
							</td>					
						</tr>					
				   	</table>
				</form>
			   	</div>
			</fieldset>
			<div id="divExam">
				
			</div>
		</div>
	</fieldset>
</div>
