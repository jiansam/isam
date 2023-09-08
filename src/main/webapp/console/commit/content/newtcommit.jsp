<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.isam.service.*,com.isam.bean.*,java.util.*,com.isam.helper.*,java.util.Map.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<script type="text/javascript" src="<c:url value='/js/fmtNumber.js'/>"></script>
<script type="text/javascript">
$(function(){
	setFormatInputDefault(".numberFmt",2);
	setFormatInputDefault(".intFmt",0);
});
</script>
<%
   SubCommitDetailService scdSer = new SubCommitDetailService();
   Map<String,Map<String,Double>> map=scdSer.getDetailSummary(request.getParameter("serno"),request.getParameter("type"));
   session.removeAttribute("sumdetail");
   session.setAttribute("sumdetail", map) ; 
   Map<String, Double> t=new HashMap<String, Double>();
   for(Entry<String, Map<String, Double>> m:map.entrySet()){
	   for(Entry<String, Double> sub:m.getValue().entrySet()){
		   String k=sub.getKey();
		   double v=t.containsKey(k)?sub.getValue()+t.get(k):sub.getValue();
		   t.put(k, v);
	   }
   }
   session.removeAttribute("tsumdetail");
   session.setAttribute("tsumdetail", t) ; 
%>
	<form id="mytform" method="post" action='<c:url value="/console/commit/updatetypedetail.jsp"/>'>
	<div style="margin:10px 10px;color:#222;font-weight: bold;padding-bottom: 10px;">${oInfo.investor}(${oInfo.idno})</div>
		<c:if test="${not empty tsumdetail}">
			<strong style="margin:10px 10px;color:#222;">${CRType[bean.type]}-總原始承諾金額（新台幣元）</strong>
			<div style="height: 10px;"></div>
			<table class="formProj" style="text-align: center;table-layout:fixed;">
				<tr>
					<th>國內投資計畫</th>
					<th>機器設備及原物料採購</th>
					<th>研發經費投入</th>								
					<th>人員聘僱</th>								
				</tr>
				<tr>
					<td class="trRight"><span class="numberFmt"><fmt:formatNumber value="${tsumdetail['0101']}" type="number"  maxFractionDigits="2"/></span></td>													
					<td class="trRight"><span class="numberFmt"><fmt:formatNumber value="${tsumdetail['0102']}" type="number"  maxFractionDigits="2"/></span></td>													
					<td class="trRight"><span class="numberFmt"><fmt:formatNumber value="${tsumdetail['0103']}" type="number"  maxFractionDigits="2"/></span></td>													
					<td class="trRight"><span class="numberFmt"><fmt:formatNumber value="${tsumdetail['0104']}" type="number"  maxFractionDigits="2"/></span></td>													
				</tr>
			</table>
		</c:if>
		<c:if test="${not empty sumdetail}">
			<div style="height: 10px;"></div>
			<strong style="margin:10px 10px;color:#222;">${CRType[bean.type]}-原承諾事項分年資料（新台幣元）</strong>
			<div style="height: 10px;"></div>
			<table class="formProj" style="text-align: center;table-layout:fixed;">
				<tr>
					<th>年度</th>
					<th>國內投資計畫</th>
					<th>機器設備及原物料採購</th>
					<th>研發經費投入</th>								
					<th>人員聘僱</th>								
				</tr>
					<c:forEach var="dEx" items="${sumdetail}">
					<tr>
						<td>${dEx.key}</td>													
						<td class="trRight"><span class="numberFmt"><fmt:formatNumber value="${dEx.value['0101']}" type="number"  maxFractionDigits="2"/></span></td>													
						<td class="trRight"><span class="numberFmt"><fmt:formatNumber value="${dEx.value['0102']}" type="number"  maxFractionDigits="2"/></span></td>													
						<td class="trRight"><span class="numberFmt"><fmt:formatNumber value="${dEx.value['0103']}" type="number"  maxFractionDigits="2"/></span></td>													
						<td class="trRight"><span class="numberFmt"><fmt:formatNumber value="${dEx.value['0104']}" type="number"  maxFractionDigits="2"/></span></td>													
					</tr>
					</c:forEach>
			</table>
		</c:if>
		<div style="display: none;">
			<input type="text" name="serno" value="${oInfo.serno}">
			<input type="text" name="idno" value="${oInfo.idno}">
			<input type="text" name="parentType" value="${bean.type}">
			<input type="text" name="investor" value="${oInfo.investor}">
			
			<c:set var="tt" value="${bean.repType eq '01'?2:1}" />
			<input type="text" name="total"  class="intFmt" value="${(ibfn:SToD(bean.endYear)-ibfn:SToD(bean.startYear)+1)*tt}">
			<c:if test="${not empty tsumdetail}">
				<c:forEach var="dEx" items="${tsumdetail}">
					<input type="hidden" name="tRType${dEx.key}" value="<fmt:formatNumber value='${dEx.value}' type='number'  maxFractionDigits='2'/>">
				</c:forEach>
			</c:if>
			<c:if test="${not empty sumdetail}">
				<c:forEach var="dEx" items="${sumdetail}">
					<c:forEach var="sub" items="${dEx.value}">
						<input name="sRType${sub.key}-${dEx.key}" type="hidden" value="<fmt:formatNumber value='${sub.value}' type='number'  maxFractionDigits='2'/>">
					</c:forEach>
				</c:forEach>
			</c:if>
		</div>
	</form>
