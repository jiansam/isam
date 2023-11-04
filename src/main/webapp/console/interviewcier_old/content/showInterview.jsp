<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.isam.bean.*" %>
<%@ page import="com.isam.dao.*" %>
<%@ page import="com.isam.helper.*" %>
<script type="text/javascript" src="<c:url value='/js/datepickerForTW.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/opener.js'/>"></script>

<script type="text/javascript">
$(function() {
  $( "#tabs" ).tabs();
});
</script>
<style type="text/css">
	.colheader{
		background-color: lightsteelblue;
	}
	
	.ui-datepicker{
		Font-size:13px !important;
		padding: 0.2em 0.2em 0;
	}
	.del{
		display: none;
	}
</style>
<%
	String identifier = request.getParameter("identifier");
	if(identifier == null){
		return;
	}
%>
<div id="mainContent">
	<fieldset style="-moz-border-radius:6px; -moz-border-top-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-right-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-left-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc; -moz-border-bottom-colors:#fff #ccc #ccc #ccc #ccc #ccc #ccc #ccc #ccc;">
		<legend style="text-align:center;padding:5px;width:200px;border-top:1px solid #E6E6E6;margin-bottom: 10px;background-color: #66cccc;-webkit-border-radius: 30px; -moz-border-radius: 30px;border-radius: 30px;">
			<span style="color:#F30;"><strong>&nbsp;檢視訪查紀錄&nbsp;</strong>&nbsp;</span>
		</legend>
 	
		<div id="tabs" style="font-size: 14px;margin-top: 20px;">
			<ul>
				<li><a href="#tabs-1">基本資料</a></li>
				<li><a href="#tabs-2">訪談內容</a></li>
				<li><a href="#tabs-3">附加檔案</a></li>
				<li style="float: right">
					<input type="button" class="btn_class_opener" value="返回列表" onclick='window.location.href="listInterview.jsp"'>
				</li>
			</ul>
			
			<div id="tabs-1">
<%
	String[] identifiers = new String[1];
	identifiers[0] = identifier;
	Vector<Interview> interviews = InterviewDAO.select(identifiers);
	
	if(interviews.size() == 0){
		return;
	}
	
	Interview interview = interviews.get(0);
	
	// Load Investment Type
	request.setAttribute("InterviewXInvestment", InterviewXInvestmentDAO.createTable());
%>
		<table>
			<tr>
				<td class="colheader">企業類別</td>
				<td>
					<itag:showInvestment identifier="<%= interview.getIdentifier() %>" />
				</td>
			</tr>
		
			<tr>
				<td class="colheader">企業名稱(中文)</td>
				<td><%= DataUtil.trim(interview.getCompany()) %></td>
			</tr>
			
			<tr>
				<td class="colheader">企業名稱(英文)</td>
				<td><%= DataUtil.trim(interview.getCompanyEnglish()) %></td>
			</tr>
			
			<tr>
				<td class="colheader">集團/母公司名稱</td>
				<td><%= DataUtil.trim(interview.getParentCompany()) %></td>
			</tr>
			
			<tr>
				<td class="colheader">公開企業/個人資訊</td>
				<td><%= interview.isPublicity() ? "是" : "否" %></td>
			</tr>
			
			<tr>
				<td class="colheader">受訪者</td>
				<td><%= DataUtil.trim(interview.getInterviewee()).replaceAll("\\n", "<br>") %></td>
			</tr>
			
			<tr>
				<td class="colheader">受訪時間</td>
				<td>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("y/MM/dd");
	Calendar cal = Calendar.getInstance();
	cal.setTime(interview.getInterviewDate());
	cal.add(Calendar.YEAR, -1911);
%>
					<%= sdf.format(cal.getTime()) %>
				</td>
			</tr>
			
			<tr>
				<td class="colheader">受訪地點</td>
				<td><%= DataUtil.trim(interview.getInterviewPlace()).replaceAll("\\n", "<br>") %></td>
			</tr>
			
			<tr>
				<td class="colheader">訪問者</td>
				<td><%= DataUtil.trim(interview.getInterviewer()).replaceAll("\\n", "<br>") %></td>
			</tr>
			
			<tr>
				<td class="colheader">附註</td>
				<td><%= DataUtil.trim(interview.getNote()).replaceAll("\\n", "<br>") %></td>
			</tr>
<%
	//Load Industry
	Hashtable<String, Industry> IndustryTable = (Hashtable<String, Industry>)session.getAttribute("InterviewcierIndustryTable");
	HashMap<Integer, ArrayList<String>> interviewXindustry = InterviewXIndustryDAO.createTable();
	
	// Load Region
	Hashtable<String, InterviewRegion> RegionTable = (Hashtable<String, InterviewRegion>)session.getAttribute("InterviewcierRegionTable");
	HashMap<Integer, ArrayList<String>> interviewXregion = InterviewXRegionDAO.createTable();
	
	ArrayList<String> industrylist = interviewXindustry.get(interview.getIdentifier()) == null ? 
			new ArrayList<String>() : interviewXindustry.get(interview.getIdentifier());
	ArrayList<Industry> parentIndustryType = new ArrayList<Industry>();
	ArrayList<Industry> industryType = new ArrayList<Industry>();
	for(String s : industrylist){
		Industry industry = IndustryTable.get(s);
		
		if(DataUtil.isEmpty(industry.getParent())){
			if(!parentIndustryType.contains(industry)){
				parentIndustryType.add(industry);
			}
		}else{
			if(!parentIndustryType.contains(IndustryTable.get(industry.getParent()))){
				parentIndustryType.add(IndustryTable.get(industry.getParent()));
			}
			
			if(!industryType.contains(industry)){
				industryType.add(industry);
			}
		}
	}
%>
			<tr>
				<td class="colheader">產業大類</td>
				<td>
<%
		for(Industry industry : parentIndustryType){
%>
										<%= industry.getName() %></br>
<%
		}
%>
				</td>
			</tr>
			
			<tr>
				<td class="colheader">產業別</td>
				<td>
<%
		for(Industry industry : industryType){
%>
										<%= industry.getCode() %><%= industry.getName() %></br>
<%
		}
%>
				</td>
			</tr>

<%
	ArrayList<String> regioncodes = interviewXregion.get(interview.getIdentifier()) == null ? new ArrayList<String>() : interviewXregion.get(interview.getIdentifier());
	ArrayList<InterviewRegion> regionlist = new ArrayList<InterviewRegion>();
	for(String s : regioncodes){
		InterviewRegion region = RegionTable.get(s);
		
		if(!regioncodes.contains(region.getParent())){
			regionlist.add(region);
		}
	}
%>		
			<tr>
				<td class="colheader">投資地點</td>
				<td>
<%
	for(InterviewRegion region : regionlist){
%>
										<%= region.getName() %></br>
<%
	}
%>
				</td>
			</tr>
		</table>
			</div>
		
			<div id="tabs-2">
<%
	PairHashtable<Integer, String, String> interviewContent = InterviewContentDAO.select(identifiers, new String[0]);
	
	Hashtable<String, InterviewOutline> OutlineTable = (Hashtable<String, InterviewOutline>)session.getAttribute("InterviewcierOutlineTable");
	Vector<String> level1 = new Vector<String>();
	Vector<String> level2 = new Vector<String>();
	for(InterviewOutline outline: OutlineTable.values()){
		if(outline.getLevel() == 1){
			level1.add(outline.getCode());
		}
		
		if(outline.getLevel() == 2){
			level2.add(outline.getCode());
		}
	}
	
	Collections.sort(level1);
	Collections.sort(level2);
%>
<script type="text/javascript" src="<c:url value='/ckeditor/config.js'/>"></script>
<script type="text/javascript">
$(function() {
	$( ".accordion" ).accordion({
		heightStyle: "content", 
		collapsible: true,
		animate:false
	});
});
</script>
 <div class="accordion">
 <%
	for(String outlineCode1 : level1){
		if("01".equalsIgnoreCase(outlineCode1)){
			continue;
		}
		
		InterviewOutline outline1 = OutlineTable.get(outlineCode1);
%>
 	<h3><%=  outline1.getName() %></h3>
 	<div class="accordion">
 <%
	for(String outlineCode2 : level2){
		if(!outlineCode2.startsWith(outlineCode1)){
			continue;
		}
		InterviewOutline outline = OutlineTable.get(outlineCode2);
%>
	<h3><%=  outline.getName() %></h3>
	<div>
		<%= DataUtil.trim(interviewContent.get(Integer.parseInt(identifier), outlineCode2)) %>
	</div>
<%
	}
%>
	</div>
<%
	}
%>
 </div>
			</div>
			
			<div id="tabs-3">
				<div id="fileTable" style="width:100%">
					<itag:listInterviewFile identifier="<%= String.valueOf(identifier) %>" purpose="cier" />
				</div>
			</div>
		</div>
	</fieldset>
</div>
