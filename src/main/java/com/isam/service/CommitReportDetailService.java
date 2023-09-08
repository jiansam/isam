package com.isam.service;

import java.util.List;

import org.json.simple.JSONArray;

import com.isam.bean.CommitReportDetail;
import com.isam.dao.CommitReportDetailDAO;

public class CommitReportDetailService {
	CommitReportDetailDAO dao = null;
	public CommitReportDetailService(){
		dao = new CommitReportDetailDAO();
	}
	
	public JSONArray getJsonFmt(String repSerno){
		return dao.getJsonFmt(repSerno);
	}
	public List<CommitReportDetail> select(String repSerno){
		return dao.select(repSerno);
	}
	public void insert(List<CommitReportDetail> beans) {
		dao.insert(beans);
	}
	public void delete(int repSerno){
		dao.delete(repSerno);
	}
	public int unable(String repserno) {
		return dao.unable(repserno);
	}
	public List<List<String>> getSummaryReport(String IDNO,String type){
		if(type.equals("02")){
			return getSummary02Report(IDNO);
		}else{
			return dao.getSummaryReport(IDNO, type);
		}
	}
	public List<List<String>> getSummary02Report(String IDNO){
		return dao.getSummary02Report(IDNO);
	}
	public List<List<String>> getSummary03Report(String IDNO){
		return dao.getSummary03Report(IDNO);
	}
	public void updateCommitReportDetailsBySub(String serno,String type){
		if(type.equals("01")){
			dao.updateCommitReportDetailsBySub(serno);
		}else if(type.equals("03")){
			dao.updateCommitReportDetailsBySub03(serno);
		}
	}
}
