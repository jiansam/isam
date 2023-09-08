<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link href="<c:url value='/media/css/demo_table.css'/>" type="text/css" rel="stylesheet"/>
<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value='/js/setDatatableOld.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script>
$(function() {
    $("#tabs").tabs();
    setNewAddFormatInput(".numberFmt");
	setFormatInputDefault(".numberFmt",2);
});

</script>
		<div>
			<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
				<legend style="width:100%;border-top:1px solid #E6E6E6">
					<span style="color:#F30;">[&nbsp;投資人資料&nbsp;]</span>&nbsp;&nbsp;
					<span  style="float: right ;">
						<a href="<c:url value='/cnfdi/listinvestor.jsp'/>" class="btn_class_opener">返回列表</a>
					</span>
				</legend>
				<c:if test="${ofiiobean!=null}">
				<div class="ui-widget ui-widget-content ui-corner-all" style="padding: 5px;font-size: 16px;" >
					<!-- <div style="color:#222;margin-left: 15px;margin-bottom: 5px;">基本資料</div> -->
					<table style="width: 100%;">
						<tr>
							<td style="text-align: right;width: 10%;">投資人：</td>
							<td colspan="5">${ofiiobean.cname}</td>
						</tr>
						<tr>
							<td style="text-align: right;width: 10%;">英文名：</td>
							<td colspan="5">${ofiiobean.ename}</td>
						</tr>
						<tr>
							<td style="text-align: right;width: 10%;">國別：</td>
							<td style="width: 30%;">${optmap.nation[ofiiobean.nation]}${optmap.cnCode[ofiiobean.cnCode]}</td>
							<td style="text-align: right;width: 10%;">資金類型：</td>
							<td style="width: 20%;" >${optmap.inSrc[ofiiobean.inrole]}</td>
							<td style="text-align: right;width: 10%;">資料狀態：</td>
							<td >${optmap.isFilled[ofiiobean.isFilled]}</td>
						</tr>
					</table>					
				</div>
				</c:if>
				
				
				<div id="tabs" style="font-size: 16px;margin-top: 20px;">
					<ul>
					    <li><a href="#tabs-1">詳細資料</a></li>
					    <c:if test="${ofiiobean!=null}">
					    <li><a href="#tabs-3">背景資料</a></li>
					    <li><a href="#tabs-2">架構圖</a></li>
					    <li><a href="#tabs-4">投資案</a></li>
					    <li><a href="#tabs-5">代理人資料</a></li>
					    </c:if>
					  </ul>
					  <div id="tabs-1">
					    <c:if test="${ofiiobean!=null}">
						<jsp:include page="/cnfdi/content/investorrelated.jsp" flush="true" />
						  </c:if>
						    <c:if test="${ofiiooffice!=null}">
						    	<jsp:include page="/cnfdi/content/investoroffice.jsp" flush="true" />
						    </c:if>
						   
					  </div>
					     <c:if test="${ofiiobean!=null}">
					  <div id="tabs-3">
						<jsp:include page="/cnfdi/content/investorbg.jsp" flush="true" />
					  </div>
					  <div id="tabs-2">
						<jsp:include page="/cnfdi/content/investorstructural.jsp" flush="true" />
					  </div>
					  <div id="tabs-4">
						<jsp:include page="/cnfdi/content/investorcase.jsp" flush="true" />
					  </div>
					  <div id="tabs-5">
						<jsp:include page="/cnfdi/content/investoragentinfos.jsp" flush="true" />
					  </div>
					     </c:if>
				</div>
			</fieldset>
		</div>