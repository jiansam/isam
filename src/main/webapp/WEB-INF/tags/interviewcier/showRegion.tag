<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ attribute name="identifier" required="true" type="Integer"%>
<%
	if(request.getAttribute("InterviewXRegion") == null){
		request.setAttribute("InterviewXRegion", InterviewXRegionDAO.createTable());
	}

	Hashtable<String, InterviewRegion> RegionTable = (Hashtable<String, InterviewRegion>)session.getAttribute("InterviewcierRegionTable");
	HashMap<Integer, ArrayList<String>> interviewXregion = (HashMap<Integer, ArrayList<String>>)request.getAttribute("InterviewXRegion");

	ArrayList<String> regions =  interviewXregion.get(identifier) == null ? new ArrayList<String>() : interviewXregion.get(identifier);
	StringBuilder sb = new StringBuilder(); 
	for(int i = 0;i < regions.size();i++){
		String regionCode = regions.get(i);
		InterviewRegion region = RegionTable.get(regionCode);
		if(region == null || regions.contains(region.getParent())){
			continue;
		}
		
		sb.append(region.getName()).append("、");
	}
%>
<%= sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "" %>