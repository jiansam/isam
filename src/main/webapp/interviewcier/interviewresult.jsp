<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<itag:setattributes />
<jsp:include page="/includes/header.jsp" flush="true" />

<%-- 2021/09/23 新增檔案下載鈕開始 --%>  
<script src="${pageContext.request.contextPath}/js/jquery-1.10.2.min.js" type="text/javascript"></script>  
<script src="${pageContext.request.contextPath}/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<%-- 2021/09/23 新增檔案下載鈕結束 --%>

<link href="<c:url value='/css/resultTable.css' />" rel="stylesheet" type="text/css">

<%-- 2021/09/23 新增檔案下載鈕開始 --%>
<script type="text/javascript" src="<c:url value='/js/isamhelper.js'/>"></script>

<style>
.sf-menu a, #path a{
	font-size: 12px !important;
}
</style>
<%-- 2021/09/23 新增檔案下載鈕結束 --%>

<body>
<script type="text/javascript">
$(function(){
	$("#Print").parent().hide();
	$("#testPrint").click(function(){
		var strC=$("#testPrint").parent().find("span:visible").text();
		if(strC==="預覽列印"){
			//$("input[name='companyName']:not(:checked)").each(function(i,ele){
			$("input[name='interview']:not(:checked)").each(function(i,ele){
				var x=$("input[name='interview']").index(ele);
				var plusOne =x+1;
				$("#dtable tr td:eq("+x+")").hide();
				$("#dtable tr:first").nextAll().addClass("tr123");
				$(".tr123").each(function(){
					if($(this).find('td').length==1){
						/* alert($(this).html()); */
						var num= $(this).attr("colspan")-1;
						 $(this).attr("colspan",num);
					}else{
						/* alert($(this).children("td:nth-child("+plusOne+")").html()); */
						$(this).children("td:nth-child("+plusOne+")").hide();
					}
			});
			});
			$("input[name='outline']:not(:checked)").each(function(i,ele){
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
	$("p").css("text-indent","0em");	
	$("p").not("p:has(strong)").css("text-indent","2em");
});

<%-- 2021/09/23 新增檔案下載鈕開始 --%>
$(function(){
	$(".dtips").each(function(){
		var timeout;
		//var first_div = $(this).next("div").first(); 
		var first_div = $(this).children("div").first();
		
		$(this).hover(
			function(){
				clearTimeout(timeout);
				first_div.show();
			},
			function(){
				timeout = setTimeout(
					function (){
						first_div.hide();
					}, 500
				);
			}
		);
	});
});
</script>
<style>
.dtips{
	/* display: inline; */
	position: relative;
	/* z-index: 99; */
}

.dtips div {
	position: absolute;
	display: none;
	z-index: 100;
	margin:0px;
	padding: 10px 20px;
/* 	font-size:14px; */
	width:auto !important;
	text-align: left;
	white-space: nowrap;
	background-color:#fff;
	-moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius: 5px;
	border:1px #000 solid;
	box-shadow:2px 2px 3px #aaaaaa;
	/*left: -100px;*/
	right: 0;
}

.dtips div a:hover{
	color: red;
}	
	
</script>
<%-- 2021/09/23 新增檔案下載鈕結束 --%>

<div id="pageC" style="margin-left: 20px;">
	<div>
		<img alt="" src='<c:url value="/images/WebsiteLogoBlue.png"/>'>
	</div>
	<div style="margin: 5px 0px;height: 20px;">
		<img src='<c:url value="/images/home.png"/>' style="width: 16px;">&nbsp;<img src='<c:url value="/images/arrow_blue.png"/>'>&nbsp;訪查資料
		<img src='<c:url value="/images/arrow_blue.png"/>'>&nbsp;顯示結果
	</div>
	<div id="tabs" >
		<ul class="tabs">
			<li><a href="#" id="testPrint" class="btn_class_opener"><span>預覽列印</span><span style="display: none;">瀏覽模式</span></a></li>
			<li><a href="#" id="Print" class="btn_class_opener"><span>列印</span></a></li>
			<li><a href="<c:url value='/interviewcier/interviewterms.jsp'/>" class="btn_class_opener"><span>重新查詢</span></a></li>
		</ul>
		<div id="tab1" style="margin: 10px;">
			<itag:resulttable />
		</div>
	</div>
</div>
</body>

