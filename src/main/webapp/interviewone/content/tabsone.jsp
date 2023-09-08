<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="org.dasin.tools.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<div class='tbtitle'>基本資料</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<c:if test="${not empty IMap.reportdate}">
	<tr>
		<td style="text-align: right;width: 18%;">查訪日期：</td>
		<td colspan="3">${ibfn:addSlash(IMap.reportdate)}</td>
	</tr>
	</c:if>
	<c:if test="${not empty IMap.noNeedReason}">
	<tr>
		<td style="text-align: right;width: 18%;">未訪視原因：</td>
		<td colspan="3">
			<c:forTokens items="${IMap.noNeedReason}" delims="," var="item" varStatus="i">
				<c:if test="${i.index >0}">,</c:if>
				${optionValName.noNeedReason[item]}
			</c:forTokens>
		<c:if test="${not empty IMap.noNeedOther}">(${IMap.noNeedOther})</c:if>
		</td>
	</tr>
	</c:if>
	<c:if test="${not empty IMap.noNeed}">
	<tr>
		<td style="text-align: right;width: 18%;">未訪視說明：</td>
		<td colspan="3">${IMap.noNeed}</td>
	</tr>
	</c:if>
	<c:if test="${not empty IMap.intervieweeType_1}">
		<tr>
			<td style="text-align: right;width: 18%;">受訪人1：</td>
			<td colspan="3">${IMap.intervieweeName_1}&nbsp;&nbsp;${IMap.intervieweeTitle_1}(${optionValName.intervieweeType[IMap.intervieweeType_1]}<c:if test="${IMap.intervieweeType_1 eq 999}">：</c:if>${IMap.intervieweeTypeNote_1})</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.intervieweeType_2}">
		<tr>
			<td style="text-align: right;">受訪人2：</td>
			<td colspan="3">${IMap.intervieweeName_2}&nbsp;&nbsp;${IMap.intervieweeTitle_2}(${optionValName.intervieweeType[IMap.intervieweeType_2]}<c:if test="${IMap.intervieweeType_2 eq 999}">：</c:if>${IMap.intervieweeTypeNote_2})</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.intervieweeType_3}">
		<tr>
			<td style="text-align: right;">受訪人3：</td>
			<td colspan="3">${IMap.intervieweeName_3}&nbsp;&nbsp;${IMap.intervieweeTitle_3}(${optionValName.intervieweeType[IMap.intervieweeType_3]}<c:if test="${IMap.intervieweeType_3 eq 999}">：</c:if>${IMap.intervieweeTypeNote_3})</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.intervieweeType_4}">
		<tr>
			<td style="text-align: right;">受訪人4：</td>
			<td colspan="3">${IMap.intervieweeName_4}&nbsp;&nbsp;${IMap.intervieweeTitle_4}(${optionValName.intervieweeType[IMap.intervieweeType_4]}<c:if test="${IMap.intervieweeType_4 eq 999}">：</c:if>${IMap.intervieweeTypeNote_4})</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.intervieweeType_5}">
		<tr>
			<td style="text-align: right;">受訪人5：</td>
			<td colspan="3">${IMap.intervieweeName_5}&nbsp;&nbsp;${IMap.intervieweeTitle_5}(${optionValName.intervieweeType[IMap.intervieweeType_5]}<c:if test="${IMap.intervieweeType_5 eq 999}">：</c:if>${IMap.intervieweeTypeNote_5})</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.intervieweeType_6}">
		<tr>
			<td style="text-align: right;">受訪人6：</td>
			<td colspan="3">${IMap.intervieweeName_6}&nbsp;&nbsp;${IMap.intervieweeTitle_6}(${optionValName.intervieweeType[IMap.intervieweeType_6]}<c:if test="${IMap.intervieweeType_6 eq 999}">：</c:if>${IMap.intervieweeTypeNote_6})</td>
		</tr>
	</c:if>
	<c:if test="${not empty IMap.interviewer}">
		<tr>
			<td style="text-align: right;width: 18%;">訪視人員：</td>
			<% 
    			request.setAttribute("vEnter", "\n"); 
			%>
			<td colspan="3">${fn:replace(IMap.interviewer,vEnter,"<br>")}</td>
		</tr>
	</c:if>
	<c:if test="${not empty SMap.reportdate}">
	<tr>
		<td style="text-align: right;width: 18%;border-top: 1px solid #c6c6c6;">問卷填表日期：</td>
		<td style="border-top: 1px solid #c6c6c6" colspan="3">${fn:substring(SMap.reportdate,0,3)}/${fn:substring(SMap.reportdate,3,5)}/${fn:substring(SMap.reportdate,5,7)}</td>
	</tr>
	</c:if>
	
	<c:if test="${not empty SMap.reporter}">
		<tr>
			<td style="text-align: right;width: 18%;">問卷填表人：</td>
			<td colspan="3">${SMap.reporter}&nbsp;&nbsp;${SMap.position}</td>
		</tr>
	</c:if>
	
	<c:if test="${not empty SMap.businessIncomeTaxCode}">
	<tr>
		<td style="text-align: right;vertical-align: top;border-top: 1px solid #c6c6c6;" nowrap>營所稅營業收入業別：</td>
		<td style="border-top: 1px solid #c6c6c6;">
<%
	for(String s : dTools.trim(((Map<String,String>)session.getAttribute("SMap")).get("businessIncomeTaxCode")).split("；")){
%>
			<%= s %><br/>
<%
	}
%>
		</td>
	</tr>
	</c:if>
</table>
<div class='tbtitle'>聯絡資訊</div>
<table style="width: 100%;font-size: 16px;" class="tchange">
	<c:if test="${ not empty IOBaseInfo.tel}">
		<tr>
			<td style="text-align: right;width: 18%;">系統聯絡電話：</td>
			<td colspan="3">${IOBaseInfo.tel}</td>
		</tr>
	</c:if>
	<c:if test="${ not empty IMap.telNo ||  not empty IMap.cellphone}">
		<tr>
			<c:if test="${ not empty IMap.telNo}">
			<td style="text-align: right;width: 18%;">訪視聯絡電話：</td>
			<td style="width: 20%;">${IMap.telNo}</td>
			</c:if>
			<c:if test="${ not empty IMap.cellphone}">
			<td style="text-align: right;width: 18%;">訪視手機：</td>
			<td style="width: 20%;">${IMap.cellphone}</td>
			</c:if>
			<c:if test="${ empty IMap.cellphone || empty IMap.telNo}">
			<td style="text-align: right;width: 18%;"></td>
			<td></td>
			</c:if>
		</tr>
	</c:if>
		<tr>
			<c:if test="${ not empty SMap.telNo}">
				<td style="text-align: right;width: 18%;">問卷聯絡電話：</td>
				<td>${SMap.telNo}</td>
			</c:if>
			<c:if test="${ not empty SMap.cellphone}">
				<td style="text-align: right;width: 18%;">問卷手機：</td>
				<td>${SMap.cellphone}</td>
			</c:if>
		</tr>
	<c:if test="${ not empty IOBaseInfo.addr}">
		<tr>
			<td style="text-align: right;width: 18%;">系統事業地址：</td>
			<td colspan="3">${IOBaseInfo.addr}</td>
		</tr>
	</c:if>
	<c:if test="${ not empty IMap.baddr}">
		<tr>
			<td style="text-align: right;width: 18%;">商業司登記：</td>
			<td colspan="3">${IOLV1[IMap.bcity]}${IOLV2[IMap.btown]}${IMap.baddr}</td>
		</tr>
	</c:if>
	<c:if test="${ not empty IMap.Addr}">
		<tr>
			<td style="text-align: right;width: 18%;">訪視事業地址：</td>
			<td colspan="3">${IOLV1[IMap.City]}${IOLV2[IMap.Town]}${IMap.Addr}</td>
		</tr>
	</c:if>
	<c:if test="${ not empty SMap.Addr}">
		<tr>
			<td style="text-align: right;width: 18%;">問卷事業地址：</td>
			<td colspan="3">${IOLV1[SMap.City]}${IOLV2[SMap.Town]}${SMap.Addr}</td>
		</tr>
	</c:if>
</table>
