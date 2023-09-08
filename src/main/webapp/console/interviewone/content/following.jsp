<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.isam.service.*,com.isam.bean.*,java.util.*,com.isam.helper.*,com.isam.service.ofi.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="ibfn" uri="http://www.ibtech.idv.tw/functions" %>

<%
	InterviewoneHelp opt = new InterviewoneHelp();
	session.setAttribute("fOpt", opt.getOptionValName());
	OFIDepartmentService odt= new OFIDepartmentService();
	session.setAttribute("odt", odt.getCodeNameMap());
	String serno=DataUtil.nulltoempty(request.getParameter("serno"));
	if(!serno.isEmpty()&&DataUtil.nulltoempty(request.getParameter("type")).equals("show")){
		InterviewoneManageService imSer= new InterviewoneManageService();
		InterviewoneManage bean= imSer.select(serno);
		session.removeAttribute("imBean");
		session.setAttribute("imBean", bean);
	}
%>
<div>
	<c:if test="${imBean.flag eq '0'}">
		<table class="formProj" style="font-size: 16;">
			<tr>
				<td class="trRight" style="width: 12%;">追蹤類型：</td>
				<td colspan="3">收文處理紀錄</td>
			</tr>
			<tr>
				<td class="trRight" style="width: 12%;">收文文號：</td>
				<td>${imBean.receiveNo }</td>
				<td class="trRight" style="width: 12%;">收文日期：</td>
				<td>${ibfn:addSlash(imBean.receiveDate)}</td>
			</tr>	
			<tr>
				<td class="trRight">發文文號：</td>
				<td>${imBean.issueNo}</td>
				<td class="trRight">發文日期：</td>
				<td>${ibfn:addSlash(imBean.issueDate)}</td>
			</tr>
			<tr>
				<td class="trRight">來文單位：</td>
				<td colspan="3">${odt[imBean.issueby]}</td>
			</tr>		
			<tr>
				<td class="trRight" style="width: 12%;">處理狀態：</td>
				<td colspan="3">${fOpt.progress[imBean.optionValue]}
				</td>
			</tr>		
			<tr>
				<td class="trRight" style="width: 12%;">追蹤情形：</td>
				<td colspan="3">${fOpt.following[imBean.following]}
				</td>
			</tr>		
			<tr>
				<td class="trRight">備註：</td>
				<td colspan="3"><pre>${imBean.note}</pre></td>
			</tr>	
		</table>
	</c:if>
	<c:if test="${imBean.flag eq '1'}">
		<table class="formProj" style="font-size: 16;">
			<tr>
				<td class="trRight" style="width: 12%;">追蹤類型：</td>
				<td colspan="3">投審會訪視紀錄</td>
			</tr>		
			<tr>
				<td class="trRight" style="width: 12%;">處理狀態：</td>
				<td colspan="3">${fOpt.progress[imBean.optionValue]}
				</td>
			</tr>		
			<tr>
				<td class="trRight" style="width: 12%;">追蹤情形：</td>
				<td colspan="3">${fOpt.following[imBean.following]}
				</td>
			</tr>		
			<tr>
				<td class="trRight" style="width: 12%;">訪查日期：</td>
				<td>${ibfn:addSlash(imBean.receiveDate)}</td>
			</tr>
			<tr>
				<td class="trRight">訪視人員：</td>
				<td colspan="3"><pre>${imBean.interviewer}</pre></td>
			</tr>	
			<tr>
				<td class="trRight">受訪人員：</td>
				<td colspan="3"><pre>${imBean.interviewee}</pre></td>
			</tr>	
			<tr>
				<td class="trRight">備註：</td>
				<td colspan="3"><pre>${imBean.note}</pre></td>
			</tr>	
		</table>
	</c:if>
</div>
