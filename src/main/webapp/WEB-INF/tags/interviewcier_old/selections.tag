<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>

<%
	final String nothingSelected = "不拘";
	final String separator = "、";

	// 1. Create year string.
	String year[] = request.getParameterValues("year");
	StringBuilder yearString = new StringBuilder();
	
	if(year == null || year.length == 0){
		yearString.append("不拘");
	}else{
		for(int i = 0;i < year.length;i++){
			yearString.append(yearString.length() == 0 ? "" :  separator)
				.append(Integer.parseInt(year[i]) - 1911);
			;	
		}
		
		yearString.append("年");
	}

	// 2. Create investment type string.
	Hashtable<String, String> optionsTable = (Hashtable<String, String>)session.getAttribute("InvestmentTypeTable");
	
	String InvestmentType[] = request.getParameterValues("InvestmentType");
	StringBuilder InvestmentTypeString = new StringBuilder();
	if(InvestmentType == null || InvestmentType.length == 0){
		InvestmentTypeString.append("不拘");
	}else{
		for(int i = 0;i < InvestmentType.length;i++){
			InvestmentTypeString.append(InvestmentTypeString.length() == 0 ? "" :  separator)
				.append(optionsTable.get(InvestmentType[i]));
			;	
		}
	}
	
	// 3. Create industry string.
	Hashtable<String, Industry> IndustryTable = (Hashtable<String, Industry>)session.getAttribute("InterviewcierIndustryTable");
	
	String Industries[] = request.getParameterValues("Industry");
	StringBuilder IndustryString = new StringBuilder();
	
	if(Industries == null || Industries.length == 0){
		IndustryString.append("不拘");
	}else{
		Vector<String> vec = new Vector<String>();
		for(String industry : Industries){
			vec.add(industry);
		}
		
		for(int i = 0;i < Industries.length;i++){
			Industry industry = IndustryTable.get(Industries[i]);
			if(vec.contains(industry.getParent())){
				continue;
			}
			
			IndustryString.append(IndustryString.length() == 0 ? "" :  separator)
				.append(industry.getName());
			;	
		}
	}
	
	// 4. Create region string.
	Hashtable<String, InterviewRegion> RegionTable = (Hashtable<String, InterviewRegion>)session.getAttribute("InterviewcierRegionTable");
		
	String Regions[] = request.getParameterValues("Region");
	StringBuilder RegionString = new StringBuilder();
	
	if(Regions == null || Regions.length == 0){
		RegionString.append("不拘");
	}else{
		Vector<String> vec = new Vector<String>();
		for(String region : Regions){
			vec.add(region);
		}
		
		for(int i = 0;i < Regions.length;i++){
			InterviewRegion region = RegionTable.get(Regions[i]);
			if(vec.contains(region.getParent())){
				continue;
			}
			
			RegionString.append(RegionString.length() == 0 ? "" :  separator)
				.append(region.getName());
			;	
		}
	}
%>
				訪查年度：<%= yearString.toString() %><br>
				企業投資類型：<%= InvestmentTypeString.toString() %> <br>
				投資業別：<%= IndustryString.toString() %><br>
				投資地區：<%= RegionString.toString() %>