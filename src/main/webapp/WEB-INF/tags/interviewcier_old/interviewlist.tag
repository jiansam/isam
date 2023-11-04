<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
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
<%
	//1. select Interview
	Vector<Interview> result = InterviewDAO.select(request.getParameterValues("year"),
			request.getParameterValues("InvestmentType"), 
			request.getParameterValues("Industry"), 
			request.getParameterValues("Region"));
%>
	<legend>
		<span  class="legendStepTitle">[&nbsp;Step 2&nbsp;]</span><strong style="color:#222;">&nbsp;選擇訪查企業&nbsp;</strong>&nbsp;
		<input type="button" class="clearAll btn_class_opener" value="清空"/>				
		<input type="button" class="selectThispage btn_class_opener" value="本頁全選"/>				
		<input type="button" class="unselectThispage btn_class_opener" value="本頁全刪"/>				
		<input type="button" class="selectedCompany btn_class_opener" value="檢視所選"/>
		<span>(Max:<span class="myCount">0</span>/<span class="myLimitCount"><%= result.size() %></span>家)</span>
	</legend>
	
	<div style="min-height: 280px;overflow: auto;margin-top: 10px;">
		<table id="example" class="display" style="width: 98%;"> 
			<thead>
				<tr>
					<th>NO.</th>
					<th>選擇</th>
					<th>訪查企業名稱</th>
					<th>投資業別</th>
					<th>投資地區</th>
					<th>企業投資類型</th>
				</tr>
			</thead>
			
			<tbody>
<%
	//2. Create investment type string.
	request.setAttribute("InterviewXInvestment", InterviewXInvestmentDAO.createTable());
	
	// 3. Create industry string.
	Hashtable<String, Industry> IndustryTable = (Hashtable<String, Industry>)session.getAttribute("InterviewcierIndustryTable");
	HashMap<Integer, ArrayList<String>> interviewXindustry = InterviewXIndustryDAO.createTable();
	
	for(Interview interview : result){
%>
	<tr>
		<td></td>
		<td><input type="checkbox" name="interview" class="companyClear" value="<%= interview.getIdentifier() %>"></td>
		<td><%= interview.getCompany() %></td>
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
		<td><itag:showInvestment identifier="<%= interview.getIdentifier() %>" /></td>
	</tr>
<%
	}
%>
			</tbody>

			<tfoot>
				<tr>
					<th></th>
					<th></th>
					<th><input type="text" value="搜尋企業" class="search_init" /></th>
					<th><input type="text" value="搜尋業別" class="search_init" /></th>
					<th><input type="text" value="搜尋地區" class="search_init" /></th>
					<th class="mFilter" id="investmentFilter"></th>
				</tr>
			</tfoot>
		</table>
	</div>