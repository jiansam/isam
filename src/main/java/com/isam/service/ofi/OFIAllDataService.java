package com.isam.service.ofi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.isam.bean.OFIPDFItem;
import com.isam.dao.ofi.OFIAllDataDAO;
import com.isam.service.COMTBDataService;
import com.isam.service.InterviewoneService;

public class OFIAllDataService{
	OFIAllDataDAO dao = null;
	public OFIAllDataService(){
		dao = new OFIAllDataDAO();
	}
	
	public Map<String,Map<String,String>> getAuditCodeList(String investNo){
		return dao.getAuditCodeList(investNo);
	}
	public List<List<String>> getAllInvestmentData(Map<String, String> auditOpt){
		return dao.getAllInvestmentData(auditOpt);
	}
	public List<List<String>> getAllReinvestmentData(){
		return dao.getAllReinvestmentData();
	}
	public List<List<String>> getAllInvestorData(){
		return dao.getAllInvestorData();
	}
	public List<List<String>> getAllInterviewoneData(){
		return dao.getAllInterviewoneData();
	}
	public List<List<String>> getAllAgentXReceiveNoData(){
		return dao.getAllAgentXReceiveNoData();
	}
	public List<List<String>> getAllOfficeData(){
		return dao.getAllOfficeData();
	}
	/*toPDF*/
	public Map<String,String> getPDFBaseData(String investNo){
		return dao.getPDFBaseData(investNo);
	}
	public Map<String,List<OFIPDFItem>> getPDFItemMap(){
		return dao.getPDFItemMap();
	}
	public Map<String,List<OFIPDFItem>> getPDFSubItemMap(){
		return dao.getPDFSubItemMap();
	}
	public Map<String,String> getPDFContactData(String investNo){
		Map<String,String> tmp=dao.getPDFContactData(investNo);
		InterviewoneService ser =new InterviewoneService();
		COMTBDataService CSer=new COMTBDataService();
		Map<Integer, Map<String, String>> mapTW=CSer.getTWADDRCode();
		Map<String, String> levelone = mapTW.get(1);
		Map<String, String> leveltwo = mapTW.get(2);
		Map<String,String> isItems=ser.getContacts(investNo);
		tmp.put("iTel",isItems.containsKey("I_telNo")?isItems.get("I_telNo"):"");
		tmp.put("sTel",isItems.containsKey("S_telNo")?isItems.get("S_telNo"):"");
		tmp.put("iAddr", isItems.containsKey("I_Addr")?levelone.get(isItems.get("I_City"))+leveltwo.get(isItems.get("I_Town"))+isItems.get("I_Addr"):"");
		tmp.put("sAddr", isItems.containsKey("S_Addr")?levelone.get(isItems.get("S_City"))+leveltwo.get(isItems.get("S_Town"))+isItems.get("S_Addr"):"");
		return tmp;
	}
	public Map<String,Map<String,String>> getInvestorData(String investNo){
		Map<String,Map<String,String>> map=new HashMap<String,Map<String,String>>();
		Map<String,Map<String,String>> base=dao.getPDFInvestorData(investNo);
		Map<String,Map<String,String>> other=dao.getPDFInvestorXOtherData(investNo);
		map.putAll(base);
		Map<String,String> sub;
		for (Entry<String, Map<String, String>> m:other.entrySet()) {
			String iNo=m.getKey();
			if(map.containsKey(iNo)){
				sub=map.get(iNo);
				sub.putAll(m.getValue());
				map.put(iNo, sub);
			}
		}
		return map;
	}
	public Map<String,List<List<String>>>  getInvestorXRelatedData(String investNo){
		return dao.getPDFCNInvestorXRelatedData(investNo);
	}
	public Map<String,List<List<String>>>  getPDFCNInvestorXAgentData(String investNo){
		return dao.getPDFCNInvestorXAgentData(investNo);
	}
	public List<Map<String,String>> getReInvestmentBase(String investNo){
		return dao.getReInvestmentBase(investNo);
	}
} 
