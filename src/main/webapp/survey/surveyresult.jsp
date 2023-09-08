<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="/includes/header.jsp" flush="true" />
<body>
<%-- <script type="text/javascript" src="<c:url value='/js/tab.js'/>"></script> --%>
<script type="text/javascript">
$(function(){
	$("#Print").parent().hide();
	$("#testPrint").click(function(){
		var strC=$("#testPrint").parent().find("span:visible").text();
		if(strC==="預覽列印"){
			$("input[name='companyName']:not(:checked)").each(function(i,ele){
				var x=$("input[name='companyName']").index(ele);
				var plusOne =x+1;
				$("#dtable tr td:eq("+x+")").hide();
				$("#dtable tr:first").nextAll().addClass("tr123");
				$(".tr123").each(function(){
					if($(this).find('td').length==1){
						var num= $(this).attr("colspan")-1;
						 $(this).attr("colspan",num);
					}else{
						$(this).children("td:nth-child("+plusOne+")").hide();
					}
			});
			});
			$("input[name='chapter']:not(:checked)").each(function(i,ele){
				$(this).parent().parent().next().hide();
				$(this).parent().parent().hide();
			});
			$(".textDiv").css("height","auto");
			$(".qSubject").css("background-color","#ececec");
			$(".trCompany").css("background-color","#ececec");
			var temptable = "";
			var lenC=$("#dtable tr").find(".trCompany").length;
			$("#pageC").children("div:not('#tabs')").hide();
			$("#dtable tr input:checkbox").each(function(){
				$(this).hide();
			});
			$("#Print").parent().show();
		}else{
			$(".textDiv").css("height","200px");
			$(".qSubject").css("background-color","#9999CC");
			$(".trCompany").css("background-color","#CCCCFF");
			$("#pageC").children("div:not('#tabs')").show();
			$("#dtable tr input:checkbox").each(function(){
				$(this).show();
			});
			$("#dtable tr td").show();
			$("#dtable tr").show();
			$("#Print").parent().hide();
		}		
			$("#testPrint").parent().find("span").toggle();
	});
	$("#Print").click(function(){
		window.print();
	});
	 $( document ).tooltip({
		 track:true,
		 content:function(){
			return $(this).prop("title");
		 }
	 });
});

</script>

<div id="pageC" style="margin-left: 20px; ">
	<div>
		<img alt="" src='<c:url value="/images/WebsiteLogoBlue.png"/>'>
	</div>
	<div style="margin: 5px 0px;height: 20px;">
		<img src='<c:url value="/images/arrow_blue.png"/>'>&nbsp;<a href='<c:url value="/survey/surveyterms.jsp"/>'>營運狀況調查</a><img src='<c:url value="/images/arrow_blue.png"/>'>&nbsp;<a href='<c:url value="/survey/surveyterms.jsp"/>'>問卷查閱</a>
		<img src='<c:url value="/images/arrow_blue.png"/>'>&nbsp;顯示結果
		<span>(<a title="${surveyterms}" style="font-size: small;color: blue;text-decoration: underline;">查詢條件</a>)</span>
	</div>
	<div id="tabs" >
		<ul class="tabs">
			<li><a href="#" id="testPrint" class="btn_class_opener"><span>預覽列印</span><span style="display: none;">瀏覽模式</span></a></li>
			<li><a href="#" id="Print" class="btn_class_opener"><span>列印</span></a></li>
			<li><a href="<c:url value='/survey/surveyterms.jsp'/>" class="btn_class_opener"><span>重新查詢</span></a></li>
		</ul>
		<div id="tab1" style="margin: 10px;">
			<table  id="dtable"  border="0" cellpadding="0" cellspacing="0" style="margin:0px;max-width: 450px;">
				<c:if test="${not empty htmlList}">
					<c:forEach var="Row" items="${htmlList}" varStatus="i" step="1" begin="0">
						<c:choose>
							<c:when test="${i.index==0}">
								<tr class="topTrtest">
								<c:forEach var="cont" items="${Row}" varStatus="j"  step="1" begin="1">
										<td class="trCompany">
											<div style="width:450px;">
												${cont}
												<Input Type="checkbox" Name="companyName" value="${j.index}" checked="checked">
											</div>
										</td>
								</c:forEach>
								</tr>
							</c:when>
							<c:otherwise>
								<tr class="topTrtest">
									<td colspan="${fn:length(Row)-1}" class="qSubject">
										${Row[0]}<Input Type="checkbox" Name="chapter" value="4" checked="checked">
									</td>
								</tr>
								<tr class="topTrtest">									
									<c:forEach var="cont" items="${Row}" varStatus="j"  step="1" begin="1">
										<td><div class="textDiv">${cont}</div></td>
									</c:forEach>
								</tr>								
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:if>			
			</table>
		</div>
	</div>
</div>
</body>

