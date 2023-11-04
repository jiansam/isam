<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="java.text.*, java.net.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<style>
*{
	font-family: 微軟正黑體,sans-serif !important;
}
</style>

<%
	request.setCharacterEncoding("utf-8");
	String interviewIdentifiers[] = request.getParameterValues("interview") == null ? new String[0] :  request.getParameterValues("interview");
	String outlineCodes[] = request.getParameterValues("outlines") == null ? new String[0] :  request.getParameterValues("outlines");
	
	//Load notShow outlines.
	Hashtable<String, InterviewOutline> OutlineTable = (Hashtable<String, InterviewOutline>)session.getAttribute("InterviewcierOutlineTable");
	ArrayList<String> outlines = new ArrayList<String>(Arrays.asList(outlineCodes));
	for(String s : OutlineTable.keySet()){
		InterviewOutline outline = OutlineTable.get(s);
		if(outlines.contains(outline.getParent()) && !outlines.contains(outline.getCode())){
			outlines.add(outline.getCode());
		}
	}
	outlineCodes = outlines.toArray(outlineCodes);
	
	// Load interviews and contents
	Vector<Interview> interviews = InterviewDAO.select(interviewIdentifiers);
	PairHashtable<Integer, String, String> interviewContent = InterviewContentDAO.select(interviewIdentifiers, outlineCodes);
	
	// Sort and keep valid outlines.
	outlines.clear();
	outlines.addAll(interviewContent.secondKeySet());
	Collections.sort(outlines);
	
	// Load Investment Type
	//Hashtable<String, String> optionsTable = (Hashtable<String, String>)session.getAttribute("InvestmentTypeTable");
	request.setAttribute("InterviewXInvestment", InterviewXInvestmentDAO.createTable());
	
	// Load Industry
	Hashtable<String, Industry> IndustryTable = (Hashtable<String, Industry>)session.getAttribute("InterviewcierIndustryTable");
	HashMap<Integer, ArrayList<String>> interviewXindustry = InterviewXIndustryDAO.createTable();
	
	// Load Region
	Hashtable<String, InterviewRegion> RegionTable = (Hashtable<String, InterviewRegion>)session.getAttribute("InterviewcierRegionTable");
	HashMap<Integer, ArrayList<String>> interviewXregion = InterviewXRegionDAO.createTable();
%>
<table  id="dtable"  border="0" cellpadding="0" cellspacing="0" style="margin:0px;max-width: 450px;">

				<tr class="topTrtest">
<%

for(int i = 0;i < interviews.size();i++){
		ArrayList<InterviewFile> interviewfiles = InterviewFileDAO.select(interviews.get(i).getIdentifier(), "cier"); //2021/09/23 新增檔案下載鈕
%>
					<td class="trCompany">
						<div style="width:450px;" class="dtips">
							<%= interviews.get(i).getCompany() %>
							<Input Type="checkbox" Name="interview" value="<%=  interviews.get(i).getIdentifier() %>" checked="checked">
<%-- 2021/09/23 新增檔案下載鈕開始 --%>
<%
	if(interviewfiles.size() > 0){
%>
							<img src="${pageContext.request.contextPath}/images/sub/icon_filedownload.png" style="width: 18px;cursor: pointer;vertical-align: sub;" />
							<div>
<%
		for(InterviewFile file : interviewfiles){
%>
								<a href="../console/interviewcier/download.jsp?identifier=<%= URLEncoder.encode(ThreeDes.getEncryptString(String.valueOf(file.getIdentifier())), "utf-8") %>">
									<%= file.getFilename() %></a><br />
<%
		}
%>
							</div>
<%
	}
%>
<%-- 2021/09/23 新增檔案下載鈕結束 --%>
						</div>
						</td>
<%
	}
%>
					
				</tr>
<%
	if(outlineCodes.length == 0 || Arrays.asList(outlineCodes).contains("01")){
%>
				<tr class="topTrtest">
					<td colspan="<%= interviews.size() %>" class="qSubject" title="訪查記錄檔">
						訪查記錄檔<Input Type="checkbox" Name="outline" value="4" checked="checked">
					</td>
				</tr>
				
				<tr class="topTrtest">
<%
	for(int i = 0;i < interviews.size();i++){
		Interview interview = interviews.get(i);
%>
					<td valign="top" align="center">
						<div class="textDiv" align="left" style="width: 98%;">
						<div class="qTitle">企業訪查資料</div>
						<div>
							<table  style="width: 100%;">
								<tr>
									<td>企業類別</td>
									<td><itag:showInvestment identifier="<%= interview.getIdentifier() %>" /></td>
								</tr>
<%
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
									<td>產業大類</td>
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
									<td>產業別</td>
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
								<tr>
									<td>企業名稱</td>
									<td>
										<%= DataUtil.trim(interview.getCompany()) %><br>
<%
	if(DataUtil.trim(interview.getCompanyEnglish()).length() > 0){
%>
										<span><%= DataUtil.trim(interview.getCompanyEnglish()) %></span>
<%
	}
%>
									</td>
								</tr>
<%
	if(DataUtil.trim(interview.getParentCompany()).length() > 0){
%>
								<tr>
									<td>母公司</td>
									<td><%= DataUtil.trim(interview.getParentCompany()) %></td>
								</tr>
<%
	}
%>
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
									<td>對外投資地點</td>
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
<%
	if(DataUtil.trim(interview.getInterviewee()).length() > 0){
%>
								<tr>
									<td>受訪者</td>
									<td><%= DataUtil.trim(interview.getInterviewee()).replaceAll("\n", "<br>") %></td>
								</tr>
<%
	}
%>
								<tr>
									<td>受訪時間</td>
									<td>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("y年M月d日");
	Calendar cal = Calendar.getInstance();
	cal.setTime(interview.getInterviewDate());
	cal.add(Calendar.YEAR, -1911);
%>
											<%= sdf.format(cal.getTime()) %>
									</td>
								</tr>
<%
	if(DataUtil.trim(interview.getInterviewer()).length() > 0){
%>
								<tr>
									<td>受訪地點</td>
									<td><%= DataUtil.trim(interview.getInterviewPlace()).replaceAll("\n", "<br>") %></td>
								</tr>
<%
	}
%>
<%
	if(DataUtil.trim(interview.getInterviewer()).length() > 0){
%>
								<tr>
									<td>訪問者</td>
									<td><%= DataUtil.trim(interview.getInterviewer()).replaceAll("\n", "<br>") %></td>
								</tr>
<%
	}
%>
<%
	if(DataUtil.trim(interview.getNote()).length() > 0){
%>
								<tr>
									<td>附註</td>
									<td><%= DataUtil.trim(interview.getNote()).replaceAll("\n", "<br>").replaceAll("\n", "<br>") %></td>
								</tr>
<%
	}
%>
							</table>
						</div>
						</div>
					</td>
<%
	}
%>
				</tr>
<%
	}
%>
				
<%
	for(int i = 0;i < outlines.size();i++){
		String title = OutlineTable.get(outlines.get(i)).getName() + "("
				+ OutlineTable.get(OutlineTable.get(outlines.get(i)).getParent()).getName() + ")";
%>
				<tr class="topTrtest">
					<td colspan="<%= interviews.size() %>" class="qSubject" title="<%= title %>">
						<%= title %>
						<Input Type="checkbox" Name="outline" value="<%= outlines.get(i) %>" checked="checked">
					</td>
				</tr>

				<tr class="topTrtest">
<%
		for(int j = 0;j < interviews.size();j++){
			String text = interviewContent.get(interviews.get(j).getIdentifier(), outlines.get(i));
			if(text == null){
				text = "";
			}
%>
					<td valign="top" align="center" style="width:450px;">
						<div class="textDiv" align="left" style="width: 98%;">
							<div class="qContent">
							<%= HTMLUtil.clean(text)%>
							</div>
						</div>
					</td>
<% 
		}
%>
				</tr>
<%
	}
%>
</table>