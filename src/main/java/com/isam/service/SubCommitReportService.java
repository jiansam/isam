package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.CommitReport;
import com.isam.dao.SubCommitReportDAO;

public class SubCommitReportService {
	SubCommitReportDAO dao = null;
	public SubCommitReportService(){
		dao = new SubCommitReportDAO();
	}
	public int isExists(String subserno,String year,String restrainType){
		return dao.isExists(subserno, year,restrainType);
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
	public List<List<String>> getReportPivot(String idno){
		return dao.getReportPivot(idno);
	}
	public void unableBySerno(String subserno) {
		dao.unableBySerno(subserno);
	}
	public void deleteAllSub(String serno) {
		dao.deleteAllSub(serno);
	}
	public Map<String,Map<String,String>> getMaxMinYearQuarter(){
		return dao.getMaxMinYearQuarter();
	}
}
