<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>
<style>
	.tdTxtLine{
		border-top:1px solid #c6c6c6;
		padding: 5px 0px;
	}
</style>
<script>
$(document).ready(function() {
	$("#txtLine").find("tr:gt(0)").each(function(){
		$(this).find("td").each(function(){
			$(this).addClass("tdTxtLine");
		});
	});
	$(".txtCheck").first().find("td").each(function(){
		$(this).removeClass("tdTxtLine");
	});
});
</script>
<div class='tbtitle'>員工僱用概況</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<c:choose>
		<c:when test="${empty SMap.twXemp && empty SMap.foreignXemp && empty SMap.cnXemp}">
			<tr><td colspan="6">無</td></tr>
		</c:when>
		<c:otherwise>
		<tr>
			<td style="text-align: right;width: 18%;">員工總人數：</td>
			<td colspan="5"><span class="numberFmt">${ibfn:SToI(SMap.twXemp)+ibfn:SToI(SMap.foreignXemp)+ibfn:SToI(SMap.cnXemp)}</span>&nbsp;&nbsp;人</td>
		</tr>
		<tr>
			<td style="text-align: right;">台灣員工：</td>
			<td colspan="5">
			<span class="numberFmt">${ibfn:SToI(SMap.twXemp)}</span>&nbsp;&nbsp;人；&nbsp;
			外國(含港澳)員工：
			<span class="numberFmt">${ibfn:SToI(SMap.foreignXemp)}</span>&nbsp;&nbsp;人；&nbsp;
			陸籍駐台人員：
			${ibfn:SToI(SMap.cnXemp)}&nbsp;&nbsp;人&nbsp;(含投資經營管理甲類 ${ibfn:SToI(SMap.cnXempA)} 人、乙類 ${ibfn:SToI(SMap.cnXempB)} 人)</td>
		</tr>
		</c:otherwise>
	</c:choose>
</table>
<div class='tbtitle'>營運場所概況</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">營業場所：</td>
		<td colspan="5">
			${optionValName.operatingPlace[IMap.operatingPlace]}<c:if test="${not empty IMap.OPNote}">；${IMap.OPNote}</c:if>
		</td>
	</tr>
	<tr>
		<td style="text-align: right;">分支機構與門市：</td>
		<td colspan="5">
		<c:if test="${empty SMap.branch}">無</c:if>
		${optionValName.branch[SMap.branch]}<c:if test="${SMap.branch eq 1}">，分支機構及門市數：<span class="numberFmt">${SMap.branchcount}</span></c:if>
		</td>
	</tr>
	<c:choose>
		<c:when test="${empty SMap.rentOS && empty SMap.ownOS}">
			<tr><td style="text-align: right;">營業總坪數：</td><td colspan="5">無</td></tr>
		</c:when>
		<c:otherwise>
		<tr>
			<td style="text-align: right;">營業總坪數：</td>
			<td colspan="5"><span class="numberFmt">${ibfn:SToD(SMap.ownOS)+ibfn:SToD(SMap.rentOS)}</span>&nbsp;&nbsp;坪&nbsp;&nbsp;(包含：辦公室、分支機構、門市、事務所、工廠、倉庫等)</td>
		</tr>
		<tr>
			<td style="text-align: right;">租賃坪數：</td>
			<td style="width: 10%;"><span class="numberFmt">${SMap.rentOS}</span>&nbsp;&nbsp;坪</td>
			<td style="text-align: right;width: 20%;">購買自有坪數：</td>
			<td colspan="3"><span class="numberFmt">${SMap.ownOS}</span>&nbsp;&nbsp;坪</td>
		</tr>
		</c:otherwise>
	</c:choose>
</table>
<div class='tbtitle'>營運模式</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<tr>
		<td style="text-align: right;width: 18%;">營運模式：</td>
		<td colspan="5">
			<c:if test="${empty SMap.OSMode}">無</c:if>
			<c:forEach var="item" items="${fn:split( SMap.OSMode,',')}" varStatus="i">
				<c:if test="${i.index !=0}"><br></c:if>${optionValName.OSMode[item]}
			</c:forEach>
		</td>
	</tr>
</table>
<div class='tbtitle'>營運情形</div>
<% 
request.setAttribute("vEnter", "\n"); 
%>
<table style="width: 100%;font-size: 16px;" class="tchange" id="txtLine">
<c:if test="${empty IMap.mainproject && empty IMap.marketing && empty IMap.newproject && empty IMap.errorMsg && empty IMap.iProgress && empty IMap.iMotivation && empty IMap.iBarrier && empty IMap.tSrc}">
	<tr><td colspan="3">無</td></tr>
</c:if>
	<c:if test="${not empty IMap.iProgress}">
		<tr>
			<td style="text-align: right;width: 18%;">投資進度：</td>
			<td colspan="2">
				<c:forEach var="item" items="${fn:split(IMap.iProgress,',')}" varStatus="i">
					<c:if test="${i.index !=0}">；</c:if>${optionValName.iProgress[item]}
				</c:forEach>
			</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.iMotivation}">
		<tr>
			<td style="text-align: right;width: 18%;">投資動機：</td>
			<td colspan="2">
				<c:forEach var="item" items="${fn:split(IMap.iMotivation,',')}" varStatus="i">
					<c:if test="${i.index !=0}">；</c:if>${optionValName.iMotivation[item]}
				</c:forEach><c:if test="${not empty IMap.iMotivationNote}">(${IMap.iMotivationNote})</c:if>
			</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.iBarrier}">
		<tr>
			<td style="text-align: right;width: 18%;">投資障礙：</td>
			<td colspan="2">
				<c:forEach var="item" items="${fn:split(IMap.iBarrier,',')}" varStatus="i">
					<c:if test="${i.index !=0}"><br></c:if>${optionValName.iBarrier[item]}
					<c:if test="${item eq 2}">-
						<c:forEach var="sub" items="${fn:split(IMap.rBarrier,',')}" varStatus="j">
							<c:if test="${j.index !=0}">；</c:if>${optionValName.rBarrier[sub]}
						</c:forEach><c:if test="${not empty IMap.rBarrierNote}">(${IMap.rBarrierNote})</c:if>
					</c:if>
					<c:if test="${item eq 3}">-
						<c:forEach var="sub" items="${fn:split(IMap.eBarrier,',')}" varStatus="j">
							<c:if test="${j.index !=0}">；</c:if>${optionValName.eBarrier[sub]}
						</c:forEach>
					</c:if>
				</c:forEach><c:if test="${not empty IMap.iBarrierNote}">(${IMap.iBarrierNote})</c:if>
			</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.tSrc}">
		<tr>
			<td style="text-align: right;width: 18%;">國內事業技術來源：</td>
			<td colspan="2">
				<c:forEach var="item" items="${fn:split(IMap.tSrc,',')}" varStatus="i">
					<c:if test="${i.index !=0}">；</c:if>${optionValName.tSrc[item]}
					<c:if test="${item eq 1 && not empty IMap.tSrcExist}">
						(<c:forEach var="sub" items="${fn:split(IMap.tSrcExist,',')}" varStatus="j">
							<c:if test="${j.index !=0}">；</c:if>${optionValName.tSrcExist[sub]}
						</c:forEach>)
					</c:if>
					<c:if test="${item eq 2 && not empty IMap.tSrcEmp}">
						(<c:forEach var="sub" items="${fn:split(IMap.tSrcEmp,',')}" varStatus="j">
							<c:if test="${j.index !=0}">；</c:if>${optionValName.tSrcEmp[sub]}
						</c:forEach>)
					</c:if>
					<c:if test="${item eq 3 && not empty IMap.tSrcMA}">
						(<c:forEach var="sub" items="${fn:split(IMap.tSrcMA,',')}" varStatus="j">
							<c:if test="${j.index !=0}">；</c:if>${optionValName.tSrcMA[sub]}
						</c:forEach>)
					</c:if>
				</c:forEach><c:if test="${not empty IMap.tSrcNote}">(${IMap.tSrcNote})</c:if>
			</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.mainproject || not empty IMap.marketing}">
	<tr>
		<td style="text-align: right;width: 18%;" rowspan="3">主要營業活動：</td>
		<td colspan="2"></td>
	</tr>
	<c:if test="${not empty IMap.mainproject}">
	<tr class="txtCheck">
		<td style="text-align: right;width: 12%;">主要產品：</td>
		<td>${fn:replace(IMap.mainproject,vEnter,"<br>")}</td>
	</tr>
	</c:if>
	<c:if test="${not empty IMap.marketing}">
	<tr class="txtCheck">
		<td style="text-align: right;width: 12%;">產銷狀況：</td>
		<td >${fn:replace(IMap.marketing,vEnter,"<br>")}</td>
	</tr>
	</c:if>
	</c:if>
	<c:if test="${not empty IMap.newproject}">
	<tr>
		<td style="text-align: right;width: 12%;">新事項規劃：</td>
		<td  colspan="2">${fn:replace(IMap.newproject,vEnter,"<br>")}</td>
	</tr>
	</c:if>
	<c:if test="${not empty IMap.errorMsg}">
	<tr>
		<td style="text-align: right;width: 18%;">異常狀況彙總：</td>
		<td  colspan="2">${fn:replace(IMap.errorMsg,vEnter,"<br>")}</td>
	</tr>
	</c:if>
</table>