<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script src="<c:url value='/media/js/jquery.dataTables.js'/>" type="text/javascript"></script>
<script src="<c:url value='/js/setDatatable.js'/>" type="text/javascript" charset="utf-8"></script>
<link href="<c:url value='/media/css/jquery.dataTables.css'/>" type="text/css" rel="stylesheet"/>
<script>
$(function() {
	$(".toVTList").each(function(){
		var $nth=$(this).parents("tr").find("th");
		var num=$nth.index($(this).parents("th"));
		var sum=0;
		$("#example tr").find("td:eq("+num+")").each(function(){
			sum+=parseInt($(this).text(), 10);
		});	
		$(this).text(sum);
	});
	$(".toVTList").click(function(){
		toListVRejects($(this))
	});
	$(".toList").click(function(){
		toListRejects($(this))
	});
	$("#toExcel").click(function(){
		toDownloadRejects()
	});
});
function toDownloadRejects(){
	var rType="";
	$("#example tr").find("td:eq(0)").each(function(){
		if(rType.length>0){
			 rType+=","
		}
		 rType+=$(this).find("span").text();
	});
	postUrlByForm('/downloadsummary.jsp',{
		'rType':rType,
		"MinR":"${rInfo.MinR}",
		"MaxR":"${rInfo.MaxR}",
		"MinI":"${rInfo.MinI}",
		"MaxI":"${rInfo.MaxI}"
	});
}
function toListVRejects($item){
	var $ntds=$item.parents("tr").find("th");
	var num=$ntds.index($item.parents("th"));
	var rType="";
	$("#example tr").find("td:eq(0)").each(function(){
		if(rType.length>0){
			 rType+=","
		}
		 rType+=$(this).find("span").text();
	});
	var tmp="";
	var miny="${rInfo.MinI}".substring(0,3);
	var maxy="${rInfo.MaxI}".substring(0,3);
	var MinI="${rInfo.MinI}";
	var MaxI="${rInfo.MaxI}";
	if($("#example tr").find("th:eq("+num+")").find("span").length>0){
		tmp=$("#example tr").find("th:eq("+num+")").find("span").text();
		if(tmp===miny&&tmp!==maxy){
			MaxI=tmp+"/12";
		}else if(tmp!==miny&&tmp===maxy){
			MinI=tmp+"/01";
		}else if(tmp!=miny&&tmp!=maxy){
			MinI=tmp+"/01";
			MaxI=tmp+"/12";
		}
	}
	postOpenUrlByForm('/reject/showlist.jsp',{
	    'rType':rType,
		"MinR":"${rInfo.MinR}",
		"MaxR":"${rInfo.MaxR}",
		"MinI":MinI,
		"MaxI":MaxI
	});
}
function toListRejects($item){
	var $ntds=$item.parents("tr").find("td");
	var num=$ntds.index($item.parents("td"));
	var rType=$ntds.first().find("span").text();
	var tmp="";
	var miny="${rInfo.MinI}".substring(0,3);
	var maxy="${rInfo.MaxI}".substring(0,3);
	var MinI="${rInfo.MinI}";
	var MaxI="${rInfo.MaxI}";
	if($("#example tr").find("th:eq("+num+")").find("span").length>0){
		tmp=$("#example tr").find("th:eq("+num+")").find("span").text();
		if(tmp===miny&&tmp!==maxy){
			MaxI=tmp+"/12";
		}else if(tmp!==miny&&tmp===maxy){
			MinI=tmp+"/01";
		}else if(tmp!=miny&&tmp!=maxy){
			MinI=tmp+"/01";
			MaxI=tmp+"/12";
		}
	}
	postOpenUrlByForm('/reject/showlist.jsp',{
	    'rejectType':rType,
		"MinR":"${rInfo.MinR}",
		"MaxR":"${rInfo.MaxR}",
		"MinI":MinI,
		"MaxI":MaxI
	});
}
</script>
<style>
#example a:hover{
	color:#F30;
	cursor: pointer; 
}
a:hover{
	color:#F30;
	cursor: pointer; 
}
</style>
<div>
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="width:100%;border-top:1px solid #E6E6E6">
			<span style="color:#F30;">[&nbsp;結果顯示&nbsp;]</span>
			<div style="float: right;padding: 3px;margin-right: 15px;">
				<a id="toExcel" style="color: blue;">
			   		<img alt="下載" src='<c:url value="/images/sub/icon_excel.png" />'>&nbsp;輸出至EXCEL
			   	</a>
			</div>
		</legend>
		<div style="width: 98%;">
		<c:if test="${empty rCounts}">查無符合條件
		</c:if>
		<c:if test="${not empty rCounts}">
		<table id="example" class="formProj"> 
			<thead>
				<tr>
					<c:forEach var="item" items="${rCounts[0]}" varStatus="i">
						<c:choose>
							<c:when test="${i.index eq 0}">
								<th style="width: 25%;">駁回類型</th>
							</c:when>
							<c:when test="${i.index eq 1}">
							</c:when>
							<c:otherwise>
								<th><span>${item.key}</span>年</th>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<th>合計</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="list" items="${rCounts}">
					<tr>
						<c:set var="hSum" value="0" />
						<c:forEach var="item" items="${list}" varStatus="i">
							<c:choose>
								<c:when test="${i.index eq 0}">
									<td style="width: 25%;"><span style="display: none;">${item.value}</span>${fn:substring(opt.rejectType[item.value],0,fn:indexOf(opt.rejectType[item.value],"("))}</td>
								</c:when>
								<c:when test="${i.index eq 1}">
								</c:when>
								<c:when test="${empty item.value}">
									<td style="text-align: right;"><a class="toList">0</a></td>
								</c:when>
								<c:otherwise>
									<c:set var="hSum" value="${item.value+hSum}" />
									<td style="text-align: right;"><a class="toList">${item.value}</a></td>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<td style="text-align: right;"><a class="toList">${hSum}</a></td>
					</tr>
				</c:forEach>
				<tr>
					<th>加總</th>
					<c:forEach begin="0" end="${fn:length(rCounts[0])-2}">
						<th><a class="toVTList">0</a></th>
					</c:forEach>
				</tr>
			</tbody>
		</table>
		<span style="color:red;font-size: 12px;padding-left: 20px;">備註：依發文年月進行資料統計。</span>
		</c:if>
		</div>
	</fieldset>
</div>
