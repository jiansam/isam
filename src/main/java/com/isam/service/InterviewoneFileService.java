package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.InterviewoneFile;
import com.isam.dao.InterviewoneFileDAO;

public class InterviewoneFileService{
	InterviewoneFileDAO dao = null;
	
	public InterviewoneFileService(){
		dao = new InterviewoneFileDAO();
	}
	public int insert(InterviewoneFile bean) {
		return dao.insert(bean);
	}
	public void insert(List<InterviewoneFile> beans) {
		dao.insert(beans);
	}
	public int delete(int fNo) {
		return dao.delete(fNo);
	}
	public int deleteByQNo(String qNo) {
		return dao.deleteByQNo(qNo);
	}
	public List<List<Integer>> getISFileCount(String year){
		return dao.getISFileCount(year);
	}
	public List<InterviewoneFile> select(String investNo,String reInvestNo){
		return dao.select(investNo,reInvestNo,null);
	}
	public List<InterviewoneFile> select(String investNo,String reInvestNo,String year){
		return dao.select(investNo,reInvestNo,year);
	}
	public Map<String,Integer> countByYear(String investNo,String reInvestNo){
		return dao.countByYear(investNo, reInvestNo);
	}
//	public int countByYear(String investNo,String year){
//		return dao.countByYear(investNo, year);
//	}
	public InterviewoneFile select(int fNo){
		return dao.select(fNo);
	}
} 
