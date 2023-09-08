package com.isam.service.ofi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestNoXFinancial;
import com.isam.dao.ofi.OFIInvestNoXFinancialDAO;

public class OFIInvestNoXFinancialService{
	OFIInvestNoXFinancialDAO dao = null;
	public OFIInvestNoXFinancialService(){
		dao = new OFIInvestNoXFinancialDAO();
	}
	public List<OFIInvestNoXFinancial> select(String investNo,String seq){
		return dao.select(investNo,null,seq);
	}
	public List<OFIInvestNoXFinancial> select(String investNo,String year,String seq){
		return dao.select(investNo,year,seq);
	}
	public OFIInvestNoXFinancial selectbean(String investNo,String reportyear,String seq){
		return dao.selectbean(investNo, reportyear, seq);
	}
//	public List<List<String>> getFinancialReport(String year){
//		String yearp1=DataUtil.addZeroForNum(String.valueOf(Integer.valueOf(year)+1),3);
//		return dao.getFinancialReport(year,yearp1);
//	}
	public List<List<String>> getFinancialReport(String year,String YN){
//		String yearp1=DataUtil.addZeroForNum(String.valueOf(Integer.valueOf(year)+1),3);
		return dao.getFinancialReport(year,YN);
	}
	public OFIInvestNoXFinancial selectBySerno(String serno){
		return dao.selectBySerno(serno);
	}
	public int insert(OFIInvestNoXFinancial bean) {
		return dao.insert(bean);
	}
	public void update(OFIInvestNoXFinancial bean) {
		dao.update(bean);
	}
	public void delete(String serno){
		dao.delete(serno);
	}
	public List<Map<String, String>> selectBySernoS(ArrayList<String> sernoS){
		return dao.selectBySernoS(sernoS);
	}
} 
