package com.isam.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.isam.bean.Industry;
import com.isam.bean.SurveyRegion;
import com.isam.bean.SurveyTopic;
import com.isam.dao.SurveyDAO;


public class SurveyHelp {
	SurveyDAO dao=null;
	private Map<String,List<SurveyRegion>> regionLev1= null;
	private Map<String,List<SurveyRegion>> regionLev2= null;
	private Map<String,List<SurveyRegion>> regionLev3= null;
	private Map<String,List<Industry>> indLevel1= null;
	private Map<String,List<Industry>> indLevel2= null;
	private Map<String,List<SurveyTopic>> topicLev1= null;
	private Map<String,List<SurveyTopic>> topicLev2= null;
	private Map<String,Map<String,List<SurveyTopic>>> topicMap= null;
	private Map<String,List<String>> qTypeXYear= null;
	private Map<String,String> qType= null;
	
	public SurveyHelp(){
		dao = new SurveyDAO();
		initial();
		read();
	}
	private void initial(){
		regionLev1= new LinkedHashMap<String, List<SurveyRegion>>();
		regionLev2= new LinkedHashMap<String, List<SurveyRegion>>();
		regionLev3= new LinkedHashMap<String, List<SurveyRegion>>();
		indLevel1=new LinkedHashMap<String, List<Industry>>();
		indLevel2=new LinkedHashMap<String, List<Industry>>();
		topicLev1=new LinkedHashMap<String, List<SurveyTopic>>();
		topicLev2=new LinkedHashMap<String, List<SurveyTopic>>();
		topicMap =new HashMap<String, Map<String,List<SurveyTopic>>>();
		qType=new LinkedHashMap<String, String>();
	}
	private void read(){
		getSurveyRegion();
		getIndustry();
		getQTypeMap();
		getQTypeXYear();
		getSurveyTopic();
		setTopicMap();
	}
	private void getQTypeMap(){
		qType.put("Abroad", "對海外投資事業營運狀況調查表");
		qType.put("Service", "華僑及外國人投資事業營運狀況調查表 (服務業)");
		qType.put("NonService", "華僑及外國人投資事業營運狀況調查表 (非服務業)");
		qType.put("CN", "陸資投資事業營運狀況調查表");
	}
	private void  getQTypeXYear(){
		qTypeXYear = dao.getQTypeXYear();
	}
	private void getSurveyRegion(){
		List<SurveyRegion> beans = dao.getSurveyRegion();
		for(int i=0;i<beans.size();i++){
			SurveyRegion bean=beans.get(i);
			String lev = bean.getLevel();
			String parent=bean.getParent();
			if(lev.equals("1")){
				List<SurveyRegion> list=null;
				if(!regionLev1.containsKey(bean.getParent())){
					list= new ArrayList<SurveyRegion>();
				}else{
					list=regionLev1.get(parent);
				}
				list.add(bean);
				regionLev1.put(bean.getCode(), list);
			}else if(lev.equals("2")){
				List<SurveyRegion> list=null;
				if(!regionLev2.containsKey(bean.getParent())){
					list= new ArrayList<SurveyRegion>();
				}else{
					list=regionLev2.get(parent);
				}
				list.add(bean);
				regionLev2.put(parent, list);
			}else if(lev.equals("3")){
				List<SurveyRegion> list=null;
				if(!regionLev3.containsKey(bean.getParent())){
					list= new ArrayList<SurveyRegion>();
				}else{
					list=regionLev3.get(parent);
				}
				list.add(bean);
				regionLev3.put(parent, list);
			}
		}
	}
	private void getIndustry(){
		List<Industry> beans = dao.getIndustry();
		for(int i=0;i<beans.size();i++){
			Industry bean=beans.get(i);
			String lev = bean.getLevel();
			String parent=bean.getParent();
			if(lev.equals("1")){
				List<Industry> list=null;
				if(!indLevel1.containsKey(bean.getParent())){
					list= new ArrayList<Industry>();
				}else{
					list=indLevel1.get(parent);
				}
				list.add(bean);
				indLevel1.put(bean.getCode(), list);
			}else if(lev.equals("2")){
				List<Industry> list=null;
				if(!indLevel2.containsKey(bean.getParent())){
					list= new ArrayList<Industry>();
				}else{
					list=indLevel2.get(parent);
				}
				list.add(bean);
				indLevel2.put(parent, list);
			}
		}
	}
	private void getSurveyTopic(){
		List<SurveyTopic> beans = dao.getSurveyTopic();
		for(int i=0;i<beans.size();i++){
			SurveyTopic bean=beans.get(i);
			String parent=bean.getParent();
			if(parent.equals("")){
				List<SurveyTopic> list=null;
				if(!topicLev1.containsKey(bean.getParent())){
					list= new ArrayList<SurveyTopic>();
				}else{
					list=topicLev1.get(parent);
				}
				list.add(bean);
				topicLev1.put(bean.getTopic(), list);
			}else{
				List<SurveyTopic> list=null;
				if(!topicLev2.containsKey(bean.getParent())){
					list= new ArrayList<SurveyTopic>();
				}else{
					list=topicLev2.get(parent);
				}
				list.add(bean);
				topicLev2.put(parent, list);
			}
		}
	}
	private void setTopicMap(){
		for(Entry<String, List<SurveyTopic>> m:topicLev2.entrySet()){
			List<SurveyTopic> beans=m.getValue();
			Map<String, List<SurveyTopic>> map= null;
			List<SurveyTopic> list=null;
			for(int i=0;i<beans.size();i++){
				SurveyTopic bean= beans.get(i);
				String parent = bean.getParent();
				String qType=bean.getqType();
				if(!topicMap.containsKey(qType)){
					map=new LinkedHashMap<String, List<SurveyTopic>>();
					if(!map.containsKey(parent)){
						list= new ArrayList<SurveyTopic>();
					}else{
						list=map.get(parent);
					}					
					list.add(bean);
					map.put(parent, list);
					topicMap.put(qType, map);
				}else{
					map=topicMap.get(qType);
					if(!map.containsKey(parent)){
						list= new ArrayList<SurveyTopic>();
					}else{
						list=map.get(parent);
					}					
					list.add(bean);
					map.put(parent, list);
					topicMap.put(qType, map);
				}
			}
		}
	}
	public Map<String, List<SurveyRegion>> getRegionLev1() {
		return regionLev1;
	}
	public Map<String, List<SurveyRegion>> getRegionLev2() {
		return regionLev2;
	}
	public Map<String, List<SurveyRegion>> getRegionLev3() {
		return regionLev3;
	}
	public Map<String, List<Industry>> getIndLevel1() {
		return indLevel1;
	}
	public Map<String, List<Industry>> getIndLevel2() {
		return indLevel2;
	}
	public Map<String, String> getqType() {
		return qType;
	}
	public Map<String, List<String>> getqTypeXYear() {
		return qTypeXYear;
	}
	public Map<String, List<SurveyTopic>> getTopicLev1() {
		return topicLev1;
	}
	public Map<String, List<SurveyTopic>> getTopicLev2() {
		return topicLev2;
	}
	public Map<String, Map<String, List<SurveyTopic>>> getTopicMap() {
		return topicMap;
	}
}
