package com.isam.service.ofi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.dao.ofi.OFIReceiveNoListDAO;

public class OFIReceiveNoListService{
	OFIReceiveNoListDAO dao = null;
	public OFIReceiveNoListService(){
		dao = new OFIReceiveNoListDAO();
	}
	public List<Map<String,String>> getIsFilledList(String pname,String receiveNo){
		return dao.getIsFilledList(pname,receiveNo);
	}
	public List<Map<String,String>> select(String type,String serno,String investNo){
		return dao.select(type, serno, investNo);
	}
	public Map<String,List<Map<String,String>>> getReceiveNo(String investNo){
		List<Map<String,String>> list= this.select(null, null, investNo);
		Map<String,List<Map<String,String>>> map=new HashMap<String, List<Map<String,String>>>();
		List<Map<String,String>> sub;
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> bean = list.get(i);
			String type=bean.get("type");
			if(map.containsKey(type)){
				sub = map.get(type);
			}else{
				sub = new ArrayList<Map<String,String>>();
			}
			sub.add(bean);
			map.put(type, sub);
		}
		return map;
	}
	public Map<String,String> getinvestReceiveNo(String investNo){
		List<Map<String,String>> list= this.select("invest", investNo, investNo);
		Map<String,String> map=new HashMap<String, String>();
		if(!list.isEmpty()){
			map=list.get(0);
		}
		return map;
	}
	public Map<String,Map<String,String>> getinvestorReceiveNo(String investorSeq){
		List<Map<String,String>> list= this.select("invest", investorSeq,null);
		Map<String,Map<String,String>> map=new HashMap<String, Map<String,String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> sub =list.get(i);
			map.put(sub.get("investNo"),sub);
		}
		return map;
	}
	public Map<String,Map<String,String>> getReInvestReceiveNo(String investNo){
		List<Map<String,String>> list= this.select("reInvest",null,investNo);
		Map<String,Map<String,String>> map=new HashMap<String, Map<String,String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> sub =list.get(i);
			map.put(sub.get("serno"),sub);
		}
		return map;
	}
} 
