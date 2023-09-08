package com.isam.service.ofi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.isam.bean.OFIInvestNoXTWSIC;
import com.isam.dao.ofi.OFIInvestNoXTWSICDAO;

public class OFIInvestNoXTWSICService{
	OFIInvestNoXTWSICDAO dao = null;
	public OFIInvestNoXTWSICService(){
		dao = new OFIInvestNoXTWSICDAO();
	}
	
	public Map<String,List<String>> selectFrontFmt(String investNo){
		return dao.selectFrontFmt(investNo);
	}
	public Map<String,String> getTWSICSelected(String investNo){
		Map<String,String> map = new HashMap<String, String>();
		Map<String,List<String>> tmp= this.select(investNo);
		if(!tmp.isEmpty()){
			StringBuffer sb = new StringBuffer();
			for (Entry<String, List<String>> m:tmp.entrySet()) {
				String k=m.getKey();
				List<String> v= m.getValue();
				for (int i = 0; i < v.size(); i++) {
					if(i>0){
						sb.append(",");
					}
					sb.append(v.get(i));
				}
				map.put(k, sb.toString());
				sb.setLength(0);
			}
		}
		return map;
	}
	public Map<String,List<String>> select(String investNo){
		return dao.select(investNo);
	}
	public List<String> select(String investNo,String type){
		Map<String,List<String>> m= this.select(investNo);
		List<String> reuslt=null;
		if(m.containsKey(type)){
			 reuslt=m.get(type);
		}
		return reuslt;
	}
	public List<Map<String,String>> getTWSICList(String investNo){
		return dao.getTWSICList(investNo);
	}
	public Map<String,String> getTWSICMap(){
		return dao.getTWSICMap();
	}
	public void delete(String investNo){
		dao.delete(investNo);
	}
	public void insert(List<OFIInvestNoXTWSIC> beans) {
		dao.insert(beans);
	}
} 
