package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.Commit;
import com.isam.dao.CommitDAO;

public class CommitService {
	CommitDAO dao = null;
	public CommitService(){
		dao = new CommitDAO();
	}
	public Map<String,String> getInvestNOList(String IDNO){
		return dao.getInvestNOList(IDNO);
	}
	public Map<String,List<String>> getReceviceNOList(String IDNO){
		return dao.getReceviceNOList(IDNO);
	}
	public int checkYearRange(String IDNO,String type,String startYear,String endYear){
		return dao.checkYearRange(IDNO, type, startYear, endYear);
	}
	public int checkYearRange(String IDNO,String type,String startYear,String endYear,int serno){
		return dao.checkYearRange(IDNO, type, startYear, endYear,serno);
	}
	public int getSerno(String IDNO,String type,String startYear,String endYear){
		return dao.getSerno(IDNO, type, startYear, endYear);
	}
	public List<Commit> select(){
		return dao.select(null);
	}
	public Commit select(String serno){
		List<Commit> list = dao.select(serno);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}
	public List<Commit> selectByIDNO(String IDNO){
		return dao.selectByIDNO(IDNO);
	}
	public String getMaxMinYear(String IDNO){
		return dao.getMaxMinYear(IDNO);
	}
	public int insert(Commit bean) {
		return dao.insert(bean);
	}
	public int update(Commit bean) {
		return dao.update(bean);
	}
	public int unable(String serno) {
		return dao.unable(serno);
	}
	public int updateNeedAlert(String serno,String needAlert) {
		return dao.updateNeedAlert(serno, needAlert);
	}
}
