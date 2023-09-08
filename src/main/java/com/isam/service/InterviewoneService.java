package com.isam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.isam.bean.Interviewone;
import com.isam.bean.InterviewoneCI;
import com.isam.bean.InterviewoneCompany;
import com.isam.bean.InterviewoneYear;
import com.isam.dao.InterviewoneDAO;

public class InterviewoneService{
	InterviewoneDAO dao = null;
	public InterviewoneService(){
		dao = new InterviewoneDAO();
	}
	public Map<String,String> getMaxInterviewDateYM(){
		return dao.getMaxInterviewDateYM();
	}
	public List<String> getYearList(){
		return dao.getYearList();
	}
	public List<String> getYearList(String investNo,String reInvestNo){
		return dao.getYearList(investNo,reInvestNo);
	}
	public List<List<String>> getTIInvestorData(Map<String,String> terms){
		return dao.getTIInvestorData(terms);
	}
	public List<List<String>> getCIInvestorData(Map<String,String> terms){
		return dao.getCIInvestorData(terms);
	}
	public Map<String,String> getContacts(String investNo){
		return dao.getContacts(investNo);
	}
	public Map<String,String> getMaxYearEMPCount(String investNo){
		return dao.getMaxYearEMPCount(investNo);
	}
	public Map<String,String> getMaxYearEMPCountRx(String reInvestNo){
		return dao.getMaxYearEMPCountRx(reInvestNo);
	}
	public int getQNoByYear(String year,String investNo,String reInvestNo){
		return dao.getQNoByYear(year, investNo,reInvestNo);
	}
	public boolean isExists(String year,String investNo,String reInvestNo){
		boolean result=false;
		if(this.getQNoByYear(year, investNo,reInvestNo)!=0){
			result=true;
		}
		return result;
	}
	public int getTCountByYear(String year){
		return dao.countByYear(year,null,null);
	}
	public int getNotFilledCountByYear(String year){
		return dao.countByYear(year,"0","0");
	}
	public List<Interviewone> select(Map<String,String> terms){
		return dao.select(terms);
	}
	
	public List<InterviewoneCompany> selectByCompany(Map<String,String> terms){
		return dao.selectByCompany(terms);
	}
	
//	public List<InterviewoneYear> selectByYear(Map<String,String> terms){
//		return dao.selectByYear(terms);
//	}
//	public List<Interviewone> select(String year,String survey,String interview,String investName,String investNo,String IDNO){
//		return dao.select(year, survey, interview, investName, investNo, IDNO);
//	}
//	public List<Interviewone> select(String syear,String eyear,String survey,String interview,String investName,String investNo,String IDNO){
//		return dao.select(syear, eyear,survey,interview, investName, investNo, IDNO);
//	}
	public List<Interviewone> select(String year){
		return dao.select(year, null);
	}
	public List<Interviewone> select(String year,String qNo){
		return dao.select(year, qNo);
	}
//	public List<Interviewone> selectByInvestNo(String investNo){
//		return dao.selectByInvestNo(investNo);
//	}	
	public Map<String,List<Interviewone>> selectByInvestNo(String investNo){
		return dao.selectByInvestNo(investNo);
	}
	public Map<String,List<Interviewone>> selectByInvestNo2(String investNo){ //106-11-29新增下載檔案數量欄位
		return dao.selectByInvestNo2(investNo);
	}
	public List<Interviewone> selectByQNo(String qNo,String reInvestNo){
		return dao.selectByQNo(qNo,reInvestNo);
	}	
	public Interviewone selectByQNo(String qNo){
		return dao.selectByQNo(qNo);
	}	
//	public int insert(Interviewone bean) {
//		return dao.insert(bean);
//	}
	public void insert(List<Interviewone> beans) {
		dao.insert(beans);
	}
	public int updateMsg(String msg,String updateuser,String qNo) {
		return dao.updateMsg(msg, updateuser, qNo);
	}
	public int update(Interviewone bean) {
		return dao.update(bean);
	}
	public int unable(String qNo,String updateuser) {
		return dao.unable(qNo,updateuser);
	}
	/* 選取轉投資清單*/
	public List<String> getReInvestNoByYear(String year){
		return dao.getReInvestNoByYear(year);
	}
	public Map<String,Map<String,String>> getReInvestNoBaseInfo(String year){
		return dao.getReInvestNoBaseInfo(year);
	}
	public Map<String,String> getInvestNoByReInvestNo(String year){
		Map<String,Map<String,String>> map=dao.getReInvestNoBaseInfo(year);
		Map<String,String> result=new HashMap<String,String>();
		for (Entry<String, Map<String, String>> m : map.entrySet()) {
			result.put(m.getKey(), m.getValue().get("investNo"));
		}
		return result;
	}
	public Map<String,String> getReInvestNoByYearInvestNo(String year,String investNo){
		return dao.getReInvestNoByYearInvestNo(year, investNo);
	}
	public Map<String,String> getReInvestNoBaseInfo(String year,String reInvestNo){
		return dao.getReInvestNoBaseInfo(year, reInvestNo);
	}
} 
