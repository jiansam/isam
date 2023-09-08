package com.isam.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.InterviewoneManage;
import com.isam.dao.InterviewoneManageDAO;

public class InterviewoneManageService{
	InterviewoneManageDAO dao = null;
	public InterviewoneManageService(){
		dao = new InterviewoneManageDAO();
	}
	public List<Map<String,String>> getIMList(Map<String,String> terms){
		return dao.getIMList(terms);
	}
	public String checkMaxReceiveDate(String qNo,String serno){
		return dao.checkMaxReceiveDate(qNo, serno);
	}
	public String checkFowllowing(String qNo,String serno){
		return dao.checkFowllowing(qNo, serno);
	}
	public Map<String,Map<String,String>> getFollowingMap(String investNo){
		return dao.getFollowingMap(investNo);
	}
	public Map<String,Map<String,String>> getFollowingMap(String qNo,String reInvestNo){
		return dao.getFollowingMap(qNo,reInvestNo);
	}
	public Map<String,List<InterviewoneManage>> selectByQNoFlag(List<InterviewoneManage> list){
		 Map<String,List<InterviewoneManage>> map=new LinkedHashMap<String, List<InterviewoneManage>>();
		 List<InterviewoneManage> sub;
		 String flag;
		 for (int i = 0; i < list.size(); i++) {
			 InterviewoneManage b=list.get(i);
			 flag=b.getFlag();
			 if(map.containsKey(flag)){
				 sub=map.get(flag);
			 }else{
				 sub=new ArrayList<InterviewoneManage>();
			 }
			 sub.add(b);
			 map.put(flag, sub);
		}
		return map;
	}
	public List<InterviewoneManage> selectByQNo(String qNo){
		return dao.selectByQNo(qNo);
	}
	public InterviewoneManage select(String serno){
		return dao.select(serno);
	}
	public int insert(InterviewoneManage bean) {
		return dao.insert(bean);
	}
	public int update(InterviewoneManage bean) {
		return dao.update(bean);
	}
	public int unable(String serno,String updateuser) {
		return dao.unable(serno, updateuser);
	}
} 
