<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.isam.service.*,com.isam.bean.*,java.util.*,com.isam.helper.*,com.isam.service.ofi.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<%

	String year=DataUtil.nulltoempty(request.getParameter("year"));
	String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
	String reInvestNo=DataUtil.nulltoempty(request.getParameter("reInvestNo"));
	String cname=DataUtil.nulltoempty(request.getParameter("cname"));
	InterviewoneFileService iofSer=new InterviewoneFileService();
	session.removeAttribute("Filelist");
	session.setAttribute("Filelist", iofSer.select(investNo, reInvestNo, year));
%>
<div>
<table class="formProj" >
		<caption>${param.year}年&nbsp;&nbsp;${param.cname}&nbsp;&nbsp;檔案列表</caption>
		<tr>
			<th>序號</th>
			<th>類型</th>
			<th>檔案名稱</th>
		</tr>
		<c:forEach var="bean" items="${Filelist}" varStatus="i">
			<tr style="text-align: center;">
				<td>${i.index+1}</td>
				<td>${bean.qType eq 'I'?'訪視紀錄':'營運問卷'}</td>
				<c:url value="/getinterviewonefiles.jsp" var="download">
					<c:param name="fNo">${bean.fNo}</c:param>
				</c:url>
				<td><a href="${download}">${bean.fName}</a></td>
			</tr>
		</c:forEach>
	</table>
</div>
