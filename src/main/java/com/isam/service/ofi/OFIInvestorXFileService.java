package com.isam.service.ofi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestorXFile;
import com.isam.dao.ofi.OFIInvestorXFileDAO;

public class OFIInvestorXFileService{
	OFIInvestorXFileDAO dao = null;
	public OFIInvestorXFileService(){
		dao = new OFIInvestorXFileDAO();
		
	}
	public int insert(OFIInvestorXFile bean) {
		return dao.insert(bean);
	}
	public int unable(int fNo,String updateuser) {
		return dao.unable(fNo,updateuser);
	}
	public void update(OFIInvestorXFile bean) {
		dao.update(bean);
	}
	public List<OFIInvestorXFile> select(String investorSeq){
		return dao.select(investorSeq);
	}
	public OFIInvestorXFile select(int fNo){
		return dao.select(fNo);
	}
	
	//106-08-22 下載ZIP
	public List<Map<String,String>> getInvestorHasfile(String investNo){
		return dao.getInvestorHasfile(investNo);
	}
	public List<OFIInvestorXFile> uploadFile(ArrayList<String> investorSeqS){
		return dao.uploadFile(investorSeqS);
	}
	
	//107-07-03  有架構圖的 InvestorSeq
	public ArrayList<String> selectInvestorSeqS_hasFile(){
		return dao.selectInvestorSeqS_hasFile();
	}
	
} 
