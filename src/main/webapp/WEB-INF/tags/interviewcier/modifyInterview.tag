<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itag" tagdir="/WEB-INF/tags/interviewcier" %>
<%@ tag import="java.util.*" %>
<%@ tag import="java.text.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ tag import="com.isam.helper.*" %>
<%@ attribute name="action" %>
<%
	String[] identifier = request.getParameterValues("identifier") == null ? new String[0] : request.getParameterValues("identifier");
	Vector<Interview> interviews = InterviewDAO.select(identifier);
	if("new".equalsIgnoreCase(action)){
		interviews.add(new Interview());
	}
	
	if(interviews.size() == 0){
		return;
	}
	
	Interview interview = interviews.get(0);
%>
		<table>
			<tr>
				<td class="colheader">企業類別</td>
				<td>
					<itag:investmentCheckist identifier="<%= interview.getIdentifier() %>" />
				</td>
			</tr>
		
			<tr>
				<td class="colheader">企業名稱(中文)</td>
				<td><input type="text" name="company" size="100" value="<%= DataUtil.trim(interview.getCompany()) %>"></td>
			</tr>
			
			<tr>
				<td class="colheader">企業名稱(英文)</td>
				<td><input type="text" name="companyEnglish" size="100" value="<%= DataUtil.trim(interview.getCompanyEnglish()) %>"></td>
			</tr>
			
			<tr>
				<td class="colheader">集團/母公司名稱</td>
				<td>
					<input type="radio" name="noParentCompany" value="true" <%= DataUtil.trim(interview.getParentCompany()).length() == 0 ? "checked" : "" %>> 同上
					<input type="radio" id="noParentCompanyFalse" name="noParentCompany" 
						value="false" <%= DataUtil.trim(interview.getParentCompany()).length() > 0 ? "checked" : "" %>> 名稱：
					<input type="text" name="parentCompany" size="80" value="<%= DataUtil.trim(interview.getParentCompany()) %>"
						onfocus="document.getElementById('noParentCompanyFalse').checked=true;">
				</td>
			</tr>
			
			<tr>
				<td class="colheader">公開企業/個人資訊</td>
				<td>
					<input type="radio" name="publicity" value="true" <%= interview.isPublicity() ? "checked" : "" %>>  是
					<input type="radio" name="publicity" value="false" <%= !interview.isPublicity() ? "checked" : "" %>>  否
				</td>
			</tr>
			
			<tr>
				<td class="colheader">受訪者</td>
				<td>
					<textarea name="interviewee" cols="100" rows="3"><%= DataUtil.trim(interview.getInterviewee()) %></textarea>
				</td>
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
					<input type="text" id="from" name="interviewDate" value="<%= sdf.format(cal.getTime()) %>">
				</td>
			</tr>
			
			<tr>
				<td class="colheader">受訪地點</td>
				<td>
					<textarea name="interviewPlace" cols="100" rows="3"><%= DataUtil.trim(interview.getInterviewPlace()) %></textarea>
				</td>
			</tr>
			
			<tr>
				<td class="colheader">訪問者</td>
				<td>
					<textarea name="interviewer" cols="100" rows="3"><%= DataUtil.trim(interview.getInterviewer()) %></textarea>
				</td>
			</tr>
			
			<tr>
				<td class="colheader">附註</td>
				<td>
					<textarea name="note" cols="100" rows="5"><%= DataUtil.trim(interview.getNote()) %></textarea>
				</td>
			</tr>
		</table>
<%
	HashMap<Integer, ArrayList<String>> interviewXindustry = InterviewXIndustryDAO.createTable();
	if(interviewXindustry != null){
		request.setAttribute("checkedIndustry", interviewXindustry.get(interview.getIdentifier()));
	}
%>
		<fieldset>
			<legend>
				<strong style="color:#222;">&nbsp;選擇投資業別&nbsp;</strong>&nbsp;
				<input type="button" value="清空" class="clearMItem btn_class_opener">
				<input type="button" class="testCheckm btn_class_opener" value="檢視所選"/>
				<span style="float: right;">搜尋&nbsp;<input type="text" class="filterMItem"></span>
				<span class="filterMsg"></span>
			</legend>
			<itag:industrytree code="" />
		</fieldset>

<%
	HashMap<Integer, ArrayList<String>> interviewXregion = InterviewXRegionDAO.createTable();
	if(interviewXregion != null){
		request.setAttribute("checkedRegion", interviewXregion.get(interview.getIdentifier()));
	}
%>
		<fieldset >
			<legend>
				<strong style="color:#222;">&nbsp;選擇投資地區&nbsp;</strong>&nbsp;
				<input type="button" value="清空" class="clearMItem btn_class_opener">
				<input type="button" class="testCheckm btn_class_opener" value="檢視所選"/>
				<span style="float: right;">搜尋&nbsp;<input type="text" class="filterMItem"></span>
				<span class="filterMsg" ></span>
			</legend>
			<itag:regiontree code="" />
		</fieldset>