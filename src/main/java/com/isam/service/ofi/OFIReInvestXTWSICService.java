package com.isam.service.ofi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.isam.bean.OFIReInvestXTWSIC;
import com.isam.dao.ofi.OFIReInvestXTWSICDAO;

public class OFIReInvestXTWSICService{
	OFIReInvestXTWSICDAO dao = null;
	public OFIReInvestXTWSICService(){
		dao = new OFIReInvestXTWSICDAO();
	}
	public Map<String,Map<String,List<String>>> getReInvestFrontTWSICs(String investNo){
		return dao.getReInvestFrontTWSICs(investNo);
	}
	public Map<String,List<String>> getReInvestXTWSIC(String reInvestNo){
		return dao.getReInvestXTWSIC(reInvestNo);
	}
	public List<Map<String,String>> getTWSICList(){
		return dao.getTWSICList();
	}
	public Map<String,String> getTWSICSelected(String reInvestNo){
		Map<String,String> map = new HashMap<String, String>();
		Map<String,List<String>> tmp= this.getReInvestXTWSIC(reInvestNo);
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
	public void delete(String reInvestNo){
		dao.delete(reInvestNo);
	}
	public void insert(List<OFIReInvestXTWSIC> beans) {
		dao.insert(beans);
	}
} 
