package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.bean.OFIReInvestList;
import com.isam.dao.ofi.OFIReInvestListDAO;

public class OFIReInvestListService{
	OFIReInvestListDAO dao = null;
	public OFIReInvestListService(){
		dao = new OFIReInvestListDAO();
	}
	public Map<String,String> getReinvestNoNameMap(String investNo){
		return dao.getReinvestNoNameMap(investNo);
	}
	public Map<String,List<String>> getReinvestNoItems(String investNo,String type){
		return dao.getReinvestNoItems(investNo, type);
	}
	public List<OFIReInvestList> select(String investNo){
		return dao.select(investNo,null);
	}
	public OFIReInvestList selectbean(String investNo,String rinvestNo){
		OFIReInvestList bean = new OFIReInvestList();
		List<OFIReInvestList> list = dao.select(investNo,rinvestNo);
		if(!list.isEmpty()){
			bean=list.get(0);
		}
		return bean;
	}
	public void update(OFIReInvestList bean) {
		dao.update(bean);
		//dao.updateShareholdingRatio(bean.getReInvestNo(),investNo);
	}
} 
