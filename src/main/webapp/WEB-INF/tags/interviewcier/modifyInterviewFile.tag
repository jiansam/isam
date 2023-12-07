<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ tag import="java.util.*" %>
<%@ tag import="java.text.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>

<%
	int identifier = -1;

	try{
		identifier = Integer.parseInt(request.getParameter("identifier"));
	}catch(Exception e){}
	
%>

<div  align="center" style="width:100%">
	<span>請選取上傳檔案： </span>
	<input id="fileupload" type="file" name="files[]" multiple />
</div>

<div id="progress" style="width:100%;">
    <div class="bar" style="width: 0%;text-align: center;background-image: url('<c:url value='/images/progressbar.gif'/>');"></div>
</div>

<script type="text/javascript" src="<c:url value='/js/jquery.ui.widget.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.iframe-transport.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/jquery.fileupload.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/interviewFile.js'/>"></script>

<script>
	$(function() {
		$('#fileupload').fileupload({
			url: 'uploadInterviewFile.jsp',
			dataType : 'html',
			done : function(e, data) {
				$('#fileTable').html(data.result);
				$('#progress .bar').css('width', '0%').text('');
			},
			progressall : function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$('#progress .bar').css('width', progress + '%').text(progress + '%');
			},
			sequentialUploads: true,
			formData: { identifier: '<%= identifier %>', Purpose: 'moea'}
		}).prop('disabled', !$.support.fileInput).parent().addClass(
				$.support.fileInput ? undefined : 'disabled');
	});
</script>

<div id="fileTable" style="width:100%">
	<itag:listInterviewFile identifier="<%= String.valueOf(identifier) %>" purpose="moea" />
</div>