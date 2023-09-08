package com.isam.service.ofi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestNoXFinancial;
import com.isam.dao.ofi.OFIReInvestNoXFinancialDAO;

public class OFIReInvestNoXFinancialService{
	OFIReInvestNoXFinancialDAO dao = null;
	public OFIReInvestNoXFinancialService(){
		dao = new OFIReInvestNoXFinancialDAO();
	}
	public int isExist(String reInvestNo,String year){
		return dao.isExist(reInvestNo, year);
	}
	public Map<String,List<OFIInvestNoXFinancial>> select(String investNo){
		return dao.select(investNo);
	}
	public OFIInvestNoXFinancial selectBySerno(String serno){
		return dao.selectBySerno(serno);
	}
	public OFIInvestNoXFinancial selectbean(String reInvestNo,String year){
		return dao.selectbean(reInvestNo, year);
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
