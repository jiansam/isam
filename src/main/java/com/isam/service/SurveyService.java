package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.dao.SurveyDAO;

public class SurveyService{
	Map<String,List<String>> getCheckMap=null;
	SurveyDAO dao = null;
	public SurveyService(){
		dao = new SurveyDAO();
		getCheckMap = dao.getCheckMap();
	}
	public String getStrItem(String[] ary,String type){
		StringBuffer sb = new StringBuffer();
		List<String> list = getCheckMap.get(type);
		if(list!=null&&!list.isEmpty()){
			for(String s:ary){
				if(list.contains(s)){
//					System.out.println("s:"+s);
					if(sb.length()==0){
						sb.append(s);
					}else{
						sb.append(",");
						sb.append(s);
					}
				}
			}
		}
		String str=sb.toString();
		sb.setLength(0);
		return str;
	}
	public List<List<String>> getSurveyResult(String qType,String year,String topic,String ind,String region){
		return dao.getSurveyResult(qType, year, topic, ind, region);
	}
	public List<Map<String,String>> getQNoResult(String qType,String year,String ind,String region){
		return dao.getQNoResult(qType, year, ind, region);
	}
	public List<List< String>> getSurveyHtmlResult(String qType,String year,String topic,String qNo){
		return dao.getSurveyHtmlResult(qType, year, topic, qNo);
	}
} 
