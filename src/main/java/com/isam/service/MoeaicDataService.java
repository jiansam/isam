package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.MoeaicData;
import com.isam.dao.MoeaicDataDAO;

public class MoeaicDataService extends ProjectKeyHelp{
	MoeaicDataDAO dao = null;
	public MoeaicDataService(){
		dao = new MoeaicDataDAO();
	}
	public List<String> getCNNameStrs(String IDNO){
		return dao.getCNNameStrs(IDNO);
	}
	public List<MoeaicData> selectByInvestNo(String investNo,String IDNO){
		return dao.selectByInvestNo(investNo,IDNO);
	}
	public List<MoeaicData> selectByInvestNoIDNO(String investNo,String IDNO){
		return dao.selectByInvestNoIDNO(investNo,IDNO);
	}
//	public List<List<String>> selectByInvestNoIDNONew(String investNo,String IDNO){
//		return dao.selectByInvestNoIDNONew(investNo,IDNO);
//	}
	public List<MoeaicData> selectByInvestNoIDNO(String investNo,String IDNO,int repserno){
		return dao.selectByInvestNoIDNO(investNo,IDNO,repserno);
	}
	public List<MoeaicData> selectByInvestNo(String investNo){
		return dao.selectByInvestNo(investNo);
	}
	public List<String> selectIDNOByInvestNo(String investNo){
		return dao.selectIDNOByInvestNo(investNo);
	}
	public List<String> selectSumMoney(String idno,String investNo){
		return dao.selectSumMoney(idno, investNo);
	}
	public List<List<String>> selectExcelSRC(String idno,String investNo){
		return dao.selectExcelSRC(idno, investNo);
	}
	public List<List<String>> selectWebSRC(String idno,String investNo){
		return dao.selectWebSRC(idno, investNo);
	}
	/*取得陸資基本資料*/
	public Map<String,String> getCNSysBaseInfo(String idno,String investNo){
		return dao.getCNSysBaseInfo(idno, investNo);
	}
	public Map<String,Map<String,String>> getCNSysBaseInfo(){
		return dao.getCNSysBaseInfo();
	}
	public boolean isCNInvestNoInRange(String investNo){
		boolean result = false;
		if(!this.getCNSysBaseInfo(null, investNo).isEmpty()){
			result=true;
		}
		return result;
	}
} 
