package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.CommitReport;
import com.isam.dao.CommitReportDAO;

public class CommitReportService {
	CommitReportDAO dao = null;
	public CommitReportService(){
		dao = new CommitReportDAO();
	}
	public int isExists(String serno,String year,String restrainType){
		return dao.isExists(serno, year,restrainType);
	}
	public int getRepSerno(String serno,String year,String restrainType){
		return dao.getRepSerno(serno, year,restrainType);
	}
	public CommitReport select(String repserno){
		return dao.select(repserno);
	}
	public int insert(CommitReport bean) {
		return dao.insert(bean);
	}
	public int update(CommitReport bean) {
		return dao.update(bean);
	}
	public int unable(String repserno) {
		return dao.unable(repserno);
	}
	public List<List<String>> getReportPivot(String IDNO){
		return dao.getReportPivot(IDNO);
	}
	public void unableBySerno(String serno) {
		dao.unableBySerno(serno);
	}
	public Map<String,Map<String,String>> getMaxMinYearQuarter(){
		return dao.getMaxMinYearQuarter();
	}
	public void updateCommitReportBySub(String serno,String updateuser){
		dao.updateCommitReportBySub(serno, updateuser);
	}
	public void checkCommitReportHasSubData(String serno,String updateuser){
		dao.checkCommitReportHasSubData(serno, updateuser);
	}
	/*產出EXCEL用*/
	public List<Map<String,String>> getReportList(String year,String type,String repType,String repState){
		return dao.getReportList(year, type, repType, repState);
	}
	public List<Map<String,String>> getReportList(String syear,String eyear,String type,String repType,String repState,String idno,String investment){
		return dao.getReportList(syear,eyear, type, repType, repState,idno,investment);
	}
}
