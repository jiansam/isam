package com.isam.service.ofi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestorXBG;
import com.isam.dao.ofi.OFIInvestorXBGDAO;

public class OFIInvestorXBGService{
	OFIInvestorXBGDAO dao = null;
	public OFIInvestorXBGService(){
		dao = new OFIInvestorXBGDAO();
		
	}
	public List<OFIInvestorXBG> select(String investorSeq){
		return dao.select(investorSeq);
	}
	public Map<String,Map<String,String>> getBGByInvestNo(String investNo){
		return dao.getBGByInvestNo(investNo);
	}
	public Map<String,List<String>> getBGMap(String investorSeq){
		List<OFIInvestorXBG> list=dao.select(investorSeq);
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		List<String> sub;
		for (int i = 0; i < list.size(); i++) {
			OFIInvestorXBG bean= list.get(i);
			String bgType=bean.getBgType();
			if(map.containsKey(bgType)){
				sub=map.get(bgType);
			}else{
				sub=new ArrayList<String>();
			}
			sub.add(bean.getValue());
			map.put(bgType, sub);
		}
		return map;
	}
	public void insert(List<OFIInvestorXBG> beans) {
		dao.insert(beans);
	}
	public void delete(String investorSeq){
		dao.delete(investorSeq);
	}
	public Map<String,String> getBGMapStr(String investorSeq){
		List<OFIInvestorXBG> list=dao.select(investorSeq);
		Map<String,String> map = new HashMap<String, String>();
		String sub="";
		StringBuffer sb= new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			OFIInvestorXBG bean= list.get(i);
			String bgType=bean.getBgType();
			if(map.containsKey(bgType)){
				sub=map.get(bgType);
			}else{
				sub="";
			}
			if(!sub.isEmpty()){
				sb.append(sub).append(",").append(bean.getValue());
			}else{
				sb.append(bean.getValue());
			}
			sub=sb.toString();
			sb.setLength(0);
			map.put(bgType, sub);
		}
		return map;
	}
	public String isEditable(String investorSeq){
		return dao.isEditable(investorSeq);
	}
} 
