<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.isam.service.*,com.isam.bean.*,java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	InterviewoneService ser = new InterviewoneService();
	List<String> ylist=ser.getYearList(request.getParameter("investNo"),request.getParameter("reInvestNo"));
	request.setAttribute("ylist",ylist);
%>
<%-- <script type="text/javascript" src="<c:url value='/js/jquery.ui.widget.js'/>"></script> --%>
<%-- <script type="text/javascript" src="<c:url value='/js/jquery.iframe-transport.js'/>"></script> --%>
<%-- <script type="text/javascript" src="<c:url value='/js/jquery.fileupload.js'/>"></script> --%>

<script>
	$(function() {
		getfilelist();
		$("#listTool").tooltip({
	          content: function () {
	              return $(this).prop('title');
	          }
	      });
		$("#listTool").hide();
		//$("#trFilename").hide();
		$("#uploadBtn").click(function(){
			//$("#trFilename").hide();
			$(".uploadallfiles").each(function(){
				$(this).click().remove();
			});
			//$("#fileTemp").html("");
			$("#listTool").hide();
			$("#listTool").prop("title","尚未選取");
		});
		$('#fileupload').fileupload({
			dataType : 'html',
			done : function(e, data) {
				getfilelist();
				$('#progress .bar').css('width', '0%').text('');
			},
			progressall : function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$('#progress .bar').css('width', progress + '%').text(progress + '%');
			},
	        add: function (e, data) {
	        	//$("#trFilename").show();
	        	//$("#fileTemp").append(data.files[0].name).append("<br>");
	        	var str=$("#listTool").prop("title");
	        	if(str==="尚未選取"){
	        		str=data.files[0].name;
	        	}else{
	        		str=str+"<br/>"+data.files[0].name;
	        	}
	        	$("#listTool").prop("title",str);
	        	str="";
	        	$("#listTool").show();
 	            data.context = $('<button class="uploadallfiles" style="display:none;"/>')
	                .appendTo($("#myDiv"))
	                .click(function () {
	                    data.submit();
	                    data=null;
	                });
	        },
			sequentialUploads: true			
		}) 
		/*.prop('disabled', !$.support.fileInput).parent().addClass(
				$.support.fileInput ? undefined : 'disabled')*/;
	});
	function getfilelist(){
		$.post( "${pageContext.request.contextPath}/includes/filelist.jsp",
			{'investNo':"${param.investNo}",'reInvestNo':'${param.reInvestNo}'},
		function(data){
			$( "#fileTable" ).html(data); 
		},"html");
	}
	function deletefile(fNo){
		$.post( "${pageContext.request.contextPath}/console/interviewone/deleteinterviewonefiles.jsp",
			{'fNo':fNo}).done(function(){
				getfilelist();
		});
	}
</script>
<div id="myDiv" style="width:100%;">
	<form id="myForm" action='<c:url value="/console/interviewone/uploadqfiles.jsp" />' method="POST" enctype="multipart/form-data">
	<table class="formProj" >
		<tr><th colspan="4">新增檔案</th></tr>
		<tr>
			<td class="trRight">年度：</td>
			<td>
				<select name='year'>
					<c:forEach var="year" items="${ylist}">
						<option value="${year}" ${param.year==year?'selected="selected"':''}>${year}&nbsp;</option>
					</c:forEach>
				</select>
			</td>
			<td class="trRight">檔案類型：</td>
			<td>
				<input id="qTypeI" type="radio" value="I" name="qType" checked="checked"><label for="qTypeI">訪視紀錄</label>
				<input id="qTypeS" type="radio" value="S" name="qType"><label for="qTypeS">營運問卷</label>
				<input type="hidden" value="${param.qNo}" name="qNo">
				<input type="hidden" value="${param.investNo}" name="investNo">
				<input type="hidden" value="${param.reInvestNo}" name="reInvestNo">
			</td>
		</tr>
		<tr>
			<td class="trRight">上傳檔案：</td>
			<td colspan="3">
				<input id="fileupload" type="file" name="files[]" multiple="multiple"/>
				<input id="uploadBtn" type="button" value="上傳"/>
				<a id="listTool" href="#" title="尚未選取">已選取檔案列表</a> 
			</td>
		</tr>
	</table>
	</form>
</div>
<div id="progress" style="width:100%;">
    <div class="bar" style="width: 0%;text-align: center;background-image: url('<c:url value='/images/progressbar.gif'/>');"></div>
</div>
<div style="margin-top: 10px;"></div>
<div id="fileTable" style="width:100%">
</div>

