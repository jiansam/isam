<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewtwo" %>
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
<script type="text/javascript">
	function deleteInterviewFile(o, id){
		if(confirm("確定刪除這個檔案？（刪除的檔案無法回復）")){
			$.post("download.jsp", {identifier: id, action: 'delete'});
			var tr = $(o).closest('tr');
			tr.remove();
		}else{
			return false;
		}		
	}
</script>


		<table style="width:100%">
			<tr>
				<td class="colheader">檔案名稱</td>
				<td class="colheader">檔案大小</td>
				<td class="colheader">上傳日期</td>
				<td class="colheader"></td>
			</tr>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("y/MM/dd");
	Calendar cal = Calendar.getInstance();

	for(InterviewFile file : interviewfiles){
		cal.setTime(file.getUploadDate());
		cal.add(Calendar.YEAR, -1911);
%>
			<tr>
				<td>
					<a href="download.jsp?identifier=<%= URLEncoder.encode(ThreeDes.getEncryptString(String.valueOf(file.getIdentifier())), "utf-8") %>">
					<%= file.getFilename() %></a>
				</td>
				<td><%= file.getFileSize() %></td>
				<td><%= sdf.format(cal.getTime()) %></td>
				<td><img src="<c:url value='/images/action_delete.gif'/>" style="border: 0px" alt="刪除檔案" title="刪除檔案"
					onclick="deleteInterviewFile(this, '<%= ThreeDes.getEncryptString(String.valueOf(file.getIdentifier())) %>');"></td>
			</tr>
<%
	}
%>
		</table>