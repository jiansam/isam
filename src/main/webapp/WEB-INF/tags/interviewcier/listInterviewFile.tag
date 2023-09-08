<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ tag import="java.util.*" %>
<%@ tag import="java.text.*" %>
<%@ tag import="java.net.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<%@ attribute name="identifier" required="true" %>
<%@ attribute name="purpose" required="true" %>
<%
	int interviewIdentifier = -1;

	try{
		interviewIdentifier = Integer.parseInt(identifier);
	}catch(Exception e){}

	ArrayList<InterviewFile> interviewfiles = InterviewFileDAO.select(interviewIdentifier, purpose);	
	if(interviewfiles.size() == 0){
		return;
	}
%>
		<table style="width:100%">
			<tr>
				<td class="colheader">檔案名稱</td>
				<td class="colheader">檔案大小</td>
				<td class="colheader">上傳日期</td>
				<td class="colheader del"></td>
			</tr>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("y/MM/dd");
	Calendar cal = Calendar.getInstance();

	for(InterviewFile file : interviewfiles){
		String dateString;
		if(file.getUploadDate() != null){
			cal.setTime(file.getUploadDate());
			cal.add(Calendar.YEAR, -1911);
			dateString = sdf.format(cal.getTime());
		}else{
			dateString = "無上傳日期";
		}
%>
			<tr>
				<td>
					<a href="download.jsp?identifier=<%= URLEncoder.encode(ThreeDes.getEncryptString(String.valueOf(file.getIdentifier())), "utf-8") %>">
					<%= file.getFilename() %></a>
				</td>
				<td><%= file.getFileSize() %></td>
				<td><%= dateString %></td>
				<td class="del"><img src="<c:url value='/images/action_delete.gif'/>" style="border: 0px" alt="刪除檔案" title="刪除檔案"
					onclick="deleteInterviewFile(this, '<%= ThreeDes.getEncryptString(String.valueOf(file.getIdentifier())) %>');"></td>
			</tr>
<%
	}
%>
		</table>