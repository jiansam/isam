<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<%@ attribute name="action" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fieldset>
	<legend>
		<span class="legendStepTitle">[&nbsp;訪查紀錄總表&nbsp;]</span>
		<c:if test="${'modify' == fn:toLowerCase(action) && (fn:contains(memberUrls,'A0302') || fn:contains(memberUrls,'E0302'))}">
			<input type="button" class="btn_class_opener" value="新增訪查紀錄" onclick='window.location.href="newInterview.jsp"'/>
		</c:if>
	</legend>
	
	<div align="right"><a href='download-interviewlist.jsp' target="_blank" class="btn_class_opener">下載Excel清單</a></div>

	<div style="min-height: 280px;overflow: auto;margin-top: 10px;">
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>NO.</th>
					<th>年度</th>
					<th>訪查企業名稱</th>
					<th>企業類別</th>
					<th>修改時間</th>
				</tr>
			</thead>
			
			<tbody>
<%
	String pageurl = "modifyInterview";
	if("show".equalsIgnoreCase(action)){
		pageurl = "showInterview";
	}
	
	Calendar cal = Calendar.getInstance();
	List<InterviewCier> list = InterviewCierDAO.select();
	
	
	for(InterviewCier interviewbrief : list){
		                               
		cal.setTime(interviewbrief.getUpdatetime()); 
%>
	<tr>
		<td></td>
		<td><%= interviewbrief.getYear() %></td>
		<td>
			 <a href="<%= pageurl %>.jsp?identifier=<%= interviewbrief.getIdentifier() %>" style="text-decoration: underline;">
				<%= DataUtil.trim(interviewbrief.getCompany()).length() == 0 ? "(未輸入名稱)" : interviewbrief.getCompany() %>
			 </a>
		</td>
		
			<td>
				<%=  "1".equals(interviewbrief.getType1()) ? "僑外資在臺事業<br>":"" %>
				<%=  "1".equals(interviewbrief.getType2()) ? "台元科技園區<br>":"" %>
				<%=  "1".equals(interviewbrief.getType3()) ? "陸資在臺辦事處":"" %>
			</td>
		
		
		<td><%=  cal.get(Calendar.YEAR) - 1911 %>/<%=  cal.get(Calendar.MONTH) + 1 %>/<%=  cal.get(Calendar.DAY_OF_MONTH) %></td>
	</tr>
<%
	}
%>
			</tbody>

			<tfoot>
				<tr>
					<th></th>
					<th class="mFilter"></th>
					<th><input type="text" name="search_company" value="搜尋企業" class="search_init" /></th>
					<th><input type="text" name="search_type" value="搜尋企業類別" class="search_init" /></th>
					<th></th>
				</tr>
			</tfoot>
		</table>
	</div>
</fieldset>