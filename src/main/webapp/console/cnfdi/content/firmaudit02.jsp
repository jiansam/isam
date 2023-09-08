<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript">
$(function(){
	if("${audit02[param.seq]['0205']}".length>0){
		$("input[name='0205'][value='${audit02[param.seq]['0205']}']").prop("checked",true);
	}else{
		$("input[name='0205'][value='']").prop("checked",true);
	}
	$("#saveAudit02").click(function(){
		 if(checkUpdateAudit()){
			 $("#UpdateAudit02").submit();
		  }
	});
});
function checkUpdateAudit(){
	var x="";
	$("input[type='text']").each(function(){
		x+=$(this).val();
	});
	$("textarea").each(function(){
		x+=$(this).val();
	});
	if($("input[name='0202']").val().length!==11 && $("input[name='0202']").val().length>0){
		alert("如填寫文號，文號須為11位碼。");
		$("input[name='0202']").focus();
		return false;
	}else if(x.length==0){
		alert("資料不可全為空值。");
		$("input[name='0202']").focus();
		return false;
	}else{
		return true;
	}
}
</script>
<c:choose>
	<c:when test="${param.type eq 'show'}">
		<c:choose>
			<c:when test="${not empty audit02[param.investNo][param.seq]}">
				<table style="width: 100%;" class="formProj">
					<c:forEach var="opt" items="${auditOpt}">
						<c:if test="${fn:startsWith(opt.auditCode,'02') && fn:length(opt.auditCode)>2}">
							<tr>
								<td style="text-align: right;width: 20%;">${opt.description}：</td>
								<td>
									<c:choose>
										<c:when test="${opt.selectName =='date'}">
											${ibfn:addSlash(audit02[param.investNo][param.seq][opt.auditCode])}
										</c:when>
										<c:when test="${opt.selectName =='YN' && opt.autoDef != '1'}">
											<c:set var="tmp" value="${audit02[param.investNo][param.seq][opt.auditCode]}" /> ${optmap.YN[tmp]}
										</c:when>
										<c:otherwise>${audit02[param.investNo][param.seq][opt.auditCode]}</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise>
				<table style="width: 100%;" class="formProj">
					<c:forEach var="opt" items="${auditOpt}">
						<c:if test="${fn:startsWith(opt.auditCode,'02') && fn:length(opt.auditCode)>2}">
							<tr>
								<td style="text-align: right;width: 20%;">${opt.description}：</td>
								<td>
									<c:choose>
										<c:when test="${opt.selectName =='date'}">
											${ibfn:addSlash(audit02[param.seq][opt.auditCode])}
										</c:when>
										<c:when test="${opt.selectName =='YN' && opt.autoDef != '1'}">
											<c:set var="tmp" value="${audit02[param.seq][opt.auditCode]}" /> ${optmap.YN[tmp]}
										</c:when>
										<c:otherwise>${audit02[param.seq][opt.auditCode]}</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<form id="UpdateAudit02" action='<c:url value="/console/cnfdi/updateaudit02.jsp"/>' method="post">
		<table style="width: 100%;">
			<c:forEach var="opt" items="${auditOpt}">
				<c:if test="${fn:startsWith(opt.auditCode,'02') && fn:length(opt.auditCode)>2}">
					<tr>
						<td style="text-align: right;width: 20%;">${opt.description}：</td>
						<td>
							<c:choose>
								<c:when test="${opt.selectName =='date'}">
									<input id="date02" type="text" name="${opt.auditCode}" value="${ibfn:addSlash(audit02[param.seq][opt.auditCode])}" >
								</c:when>
								<c:when test="${opt.selectName =='YN' && opt.autoDef != '1'}">
									<c:forEach var="r" items="${optmap.YN}">
										<input type="radio" name="${opt.auditCode}" value="${r.key}" id="YN${opt.auditCode}${r.key}"><label for="YN${opt.auditCode}${r.key}">${r.value}</label>
									</c:forEach>
								</c:when>
								<c:when test="${opt.auditCode=='0204' || opt.auditCode=='0203'}">
									<textarea name="${opt.auditCode}" style="width: 95%;" rows="5">${audit02[param.seq][opt.auditCode]}</textarea>
								</c:when>
								<c:otherwise><input autofocus="autofocus" type="text" name="${opt.auditCode}" value="${audit02[param.seq][opt.auditCode]}"></c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
		<input type="hidden" name="investNo" value="${param.investNo}">
		<input type="hidden" name="seq" value="${param.seq}">
		<input type="hidden" name="type" value="${param.type}">
		<input type="hidden" name="tabsNum" value="${param.tabsNum}">
		<div style="margin-top: 10px;text-align: center;"><input type="button" value="儲存" id="saveAudit02" class="btn_class_Approval"></div>
		</form>
	</c:otherwise>
</c:choose>