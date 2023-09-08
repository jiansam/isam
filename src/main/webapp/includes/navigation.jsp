<!-- navigation.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<style>
.triangle {
	margin-left:2px;
	margin-right:5px;
    border-top: 8px solid white;
    border-left: 12px solid #2D89EF;
    border-bottom:8px solid white;
    display: inline-block;
}
</style>
<c:set var="urlForParse">${fn:replace(pageContext.request.requestURI, pageContext.request.contextPath,'')}</c:set>
<c:set var="url">${pageContext.request.requestURL}</c:set>
<c:set var="baseURL" value="${fn:replace(url, pageContext.request.requestURI, pageContext.request.contextPath)}" />
<script>
	$(function(){
		var urlForParse="${urlForParse}";
		var strs = urlForParse.split("/");
		var navigationCont="";
		var arrows="<span class='triangle'></span>";
		for(var i=1;i<strs.length;i++){
			if(i==1){
				var ahref=getRUrl(strs[i]);
				navigationCont+=arrows+ahref;
			}
		}
		$(".navigationCont").append(navigationCont);
	});
// 	function getRUrl(Str){
// 		var x = "";
// 		if(Str==="approval"){			
// 			x="<a href='${baseURL}/approval/approval.jsp'>核准資料</a>";
// 		}else if(Str==="survey"){
// 			x="<a href='${baseURL}/survey/surveyterms.jsp'>營運狀況調查</a>";
// 		}else if(Str==="interviewtwo"||Str==="interviewcier"){
// 			x="<a href='${baseURL}/interviewcier/interviewterms.jsp'>訪查資料</a>";
// 		}else if(Str==="console"||endsWith(Str,".jsp")){
// 			x="<a href='${baseURL}/useredit.jsp'>資料維護</a>";
// 		}else if(Str==="cnfdi"){
			
// 			alert($(".navigation>li").has("a[href*='/"+Str+"/']:first").children("a").text());
// 			alert($("#menu a[href*='/"+Str+"/']:first").text());
// 			x="<a href='${baseURL}/useredit.jsp'>資料維護</a>";
// 		}
// 		return x;
// 	}	
	function getRUrl(Str){
		var x = "";
		if(Str==="console"||endsWith(Str,".jsp")){
			x="<a href='${baseURL}/useredit.jsp'>後台管理</a>";
		}else{
			var $topitem=$(".navigation>li").has("a[href^='${pageContext.request.contextPath}/"+Str+"/']:first").children("a");
			var name=$topitem.text();
			var url =$topitem.next().children("li").find("a[href]:first").prop("href");
			x="<a href='"+url+"'>"+name+"</a>";
		}
		return x;
	}	
</script>
<div style="margin: 10px;height: 20px;text-align: right;" class="navigationCont">
	<%-- <img src='<c:url value="/images/home.png"/>' style="width: 16px;">&nbsp; --%>	
</div>