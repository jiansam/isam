<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.*" %>
<%@ tag import="com.isam.bean.*" %>
<%@ tag import="com.isam.dao.*" %>
<%@ attribute name="refresh" %>
<%
	//1. Create investment type table.
	String selectName = "InvestmentType";
	Vector<InterviewOption> options = (Vector<InterviewOption>)session.getAttribute(selectName);
	if(options == null){
		options = InterviewOptionDAO.select(selectName);
		session.setAttribute(selectName, options);
	}
	
	Hashtable<String, String> optionsTable = (Hashtable<String, String>)session.getAttribute("InvestmentTypeTable");
	if(optionsTable == null){
		optionsTable = new Hashtable<String, String>();
		
		for(InterviewOption option : options){
			optionsTable.put(option.getOptionValue(), option.getCDescription());
		}
		
		session.setAttribute("InvestmentTypeTable", optionsTable);
	}
	
	// 2. Create industry tree & table.
	Hashtable<String, Industry> IndustryTable = (Hashtable<String, Industry>)session.getAttribute("InterviewcierIndustryTable");
	if(IndustryTable == null ){
		Hashtable<String, Vector<Industry>> industrytree = (Hashtable<String, Vector<Industry>>)session.getAttribute("InterviewcierIndustryTree");
		if(industrytree == null){
			industrytree = InterviewIndustryDAO.loadIndustryTree();
			session.setAttribute("InterviewcierIndustryTree", industrytree);
		}
		
		IndustryTable = new Hashtable<String, Industry>();
		for(String key :  industrytree.keySet()){
			Vector<Industry> vec = industrytree.get(key);
			
			for(Industry industry : vec){
				IndustryTable.put(industry.getCode(), industry);
			}
		}
		
		session.setAttribute("InterviewcierIndustryTable", IndustryTable);
	}
	
	// 3. Create region tree & table.
	Hashtable<String, InterviewRegion> RegionTable = (Hashtable<String, InterviewRegion>)session.getAttribute("InterviewcierRegionTable");
	if(RegionTable == null){
		Hashtable<String, Vector<InterviewRegion>> regiontree = (Hashtable<String, Vector<InterviewRegion>>)session.getAttribute("InterviewcierRegionTree");
		if(regiontree == null){
			regiontree = InterviewRegionDAO.loadRegionTree();
			session.setAttribute("InterviewcierRegionTree", regiontree);
		}
		
		RegionTable = new Hashtable<String, InterviewRegion>();
		for(String key :  regiontree.keySet()){
			Vector<InterviewRegion> vec = regiontree.get(key);
			
			for(InterviewRegion region : vec){
				RegionTable.put(region.getCode(), region);
			}
		}
		
		session.setAttribute("InterviewcierRegionTable", RegionTable);
	}
	
	// 4. Create outline tree & table.
	Hashtable<String, InterviewOutline> OutlineTable = (Hashtable<String, InterviewOutline>)session.getAttribute("InterviewcierOutlineTable");
	if(OutlineTable == null){
		Hashtable<String, Vector<InterviewOutline>> outlinetree = (Hashtable<String, Vector<InterviewOutline>>)session.getAttribute("InterviewcierOutlineTree");
		if(outlinetree == null){
			outlinetree = InterviewOutlineDAO.loadOutlineTree();
			session.setAttribute("InterviewcierOutlineTree", outlinetree);
		}
		
		OutlineTable = InterviewOutlineDAO.loadOutlineTable();
		session.setAttribute("InterviewcierOutlineTable", OutlineTable);
	}
%>
