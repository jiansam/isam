<%@page import="com.isam.helper.DataUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.isam.ofi.reject.service.*,com.isam.ofi.reject.bean.*,java.util.*"%>
<%
	session.removeAttribute("NewAppls");
	OFIRejectXApplicantService ser = new OFIRejectXApplicantService();
	String serno=DataUtil.nulltoempty(request.getParameter("serno"));
	List<Map<String,String>> NewAppls=new ArrayList<Map<String,String>>();
	List<Map<String,String>> temp=null;
	if(!serno.isEmpty()){
		temp=ser.getRejectApplicant(serno, "");
		if(temp!=null){
			NewAppls.addAll(temp);
		}
	}
	session.setAttribute("NewAppls", NewAppls);
%>
   <table style="width: 98%;font-size: 16px;" class="formProj"  id="ApplicantForm">
   		<tr>
   			<th style="width: 5%;">序號</th>
   			<th style="width: 45%;">申請人姓名或公司名稱</th>
   			<th style="width: 20%;">國別</th>
   			<th>代理人(職業)</th>
   			<th>刪除</th>
   			<th>編輯</th>
   		</tr>
   		<c:if test="${empty NewAppls}">
   			<tr class="emptyData">
   				<td colspan="6">尚無資料</td>
   			</tr>
   		</c:if>
   		<c:forEach var="appls" items="${NewAppls}" varStatus="i">
   			<tr>
   				<td style="text-align: center;">(${i.index+1})</td>
   				<td>${appls.cApplicant}<c:if test="${not empty appls.eApplicant}"><br>${appls.eApplicant}</c:if></td>
   				<td>${appls.nation}<c:if test="${not empty appls.cnCode}">（${appls.cnCode}）</c:if></td>
   				<td>${appls.agent}</td>
   				<td style="text-align: center;"><input type="button" class="delAPL btn_class_opener" value="刪除" alt="${appls.applyNo}"></td>
   				<td style="text-align: center;"><input type="button" class="editAPL btn_class_opener" value="編輯" alt="${appls.applyNo}"></td>
   			</tr>
   		</c:forEach>
	</table>