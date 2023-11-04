<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
$(function(){
	$("#investmentFilter").find("option").each(function(index){
		if($(this).html().indexOf("\n") != -1){
			var newopts =  $(this).html().split("\n");
			$(this).remove();
			
			for(var i = 0;i < newopts.length;i++){
				opts = $("#investmentFilter").find("option");
				newopts[i] = $.trim(newopts[i]);
				
				var exists = false;
				for(var j = 0;j < opts.length;j++){
					if(newopts[i] == opts[j].innerText){
						exists = true;
						break;
					}
				}
				
				if(!exists){
					$("#investmentFilter").find("select").append($('<option>').text(newopts[i]));
				}
			}
		}
	});
});
</script>
<fieldset>
	<legend>
		<span class="legendStepTitle">[&nbsp;修改訪查紀錄&nbsp;]</span>
		<c:if test="${fn:contains(memberUrls,'A0301') || fn:contains(memberUrls,'E0301')}">
			<input type="button" class="btn_class_opener" value="新增訪查紀錄" onclick='window.location.href="newInterview.jsp"'/>
		</c:if>
	</legend>
			
	<div style="min-height: 280px;overflow: auto;margin-top: 10px;">
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>NO.</th>
					<th>年度</th>
					<th>訪查企業名稱</th>
					<th>投資業別</th>
					<th>投資地區</th>
					<th>企業投資類型</th>
					<th>修改日期</th>
				</tr>
			</thead>
			
			<tbody>
<%
	//Create investment type string.
	request.setAttribute("InterviewXInvestment", InterviewXInvestmentDAO.createTable());
	
	//Create industry string.
	Hashtable<String, Industry> IndustryTable = (Hashtable<String, Industry>)session.getAttribute("InterviewcierIndustryTable");
	HashMap<Integer, ArrayList<String>> interviewXindustry = InterviewXIndustryDAO.createTable();
	
	for(Interview interview : InterviewDAO.select(request.getParameterValues("year"),
			request.getParameterValues("InvestmentType"), 
			request.getParameterValues("Industry"), 
			request.getParameterValues("Region"))){
%>
	<tr>
		<td></td>
		<td align="center"><%= interview.getYear() - 1911 %></td>
		<td>
			<a href="modifyInterview.jsp?identifier=<%= interview.getIdentifier() %>" style="text-decoration: underline;">
				<%= DataUtil.trim(interview.getCompany()).length() == 0 ? "(未輸入名稱)" : interview.getCompany() %>
			</a>
		</td>
		<td>
<%
			for(String industryCode : interviewXindustry.get(interview.getIdentifier()) == null ? 
					new ArrayList<String>() : interviewXindustry.get(interview.getIdentifier())){
%>
				<%= industryCode %><%= IndustryTable.get(industryCode).getName() %><br />
<%
			}
%>
		</td>
		<td>
			<itag:showRegion identifier="<%= interview.getIdentifier() %>" />
		</td>
		<td>
			<itag:showInvestment identifier="<%= interview.getIdentifier() %>" />
		</td>
		<td nowrap><%= interview.getUpdatetime().split("\\s")[0] %></td>
	</tr>
<%
	}
%>
			</tbody>

			<tfoot>
				<tr>
					<th></th>
					<th class="mFilter"><input style="display: none;" type="text" class="search_init" /></th>
					<th><input type="text" name="search_company" value="搜尋企業" class="search_init" /></th>
					<th><input type="text" name="search_IND" value="搜尋業別" class="search_init" /></th>
					<th><input type="text" name="search_area" value="搜尋地區" class="search_init" /></th>
					<th class="mFilter" id="investmentFilter"></th>
					<th></th>
				</tr>
			</tfoot>
		</table>
	</div>
</fieldset>