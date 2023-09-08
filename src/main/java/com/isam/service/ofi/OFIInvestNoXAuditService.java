package com.isam.service.ofi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.isam.bean.OFIInvestNoXAudit;
import com.isam.dao.ofi.OFIInvestNoXAuditDAO;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneContentService;

public class OFIInvestNoXAuditService{
	OFIInvestNoXAuditDAO dao = null;
	InterviewoneContentService iocSer=null;
	OFIInvestCaseService icSer =null;
	public OFIInvestNoXAuditService(){
		dao = new OFIInvestNoXAuditDAO();
		iocSer = new InterviewoneContentService();
		icSer = new OFIInvestCaseService();
	}
	public Map<String,Map<String,String>> select(String investorSeq){
		return dao.select(investorSeq);
	}
	public Map<String,String> getAduitByInvestNo(String investNo){
		return dao.getAduitByInvestNo(investNo);
	}
	public List<Map<String,String>> selectByInvestNo(String investNo){
		return dao.selectByInvestNo(investNo);
	}
	public List<Map<String,String>> classifyByAudit(List<Map<String,String>> audit,String auditCode){
		List<Map<String,String>>  result= new ArrayList<Map<String,String>>();
		for (int i = 0; i < audit.size(); i++) {
			Map<String,String> m =audit.get(i);
			if(audit.get(i).get("auditCode").startsWith(auditCode)){
				result.add(m);
			}
		}
		return result;
	}
//	public Map<String,Map<String,List<String>>> classifyByAuditCode(List<Map<String,String>> audit){
//		Map<String,Map<String,List<String>>>  result= new HashMap<String,Map<String,List<String>>>();
//		Map<String,List<String>> sub;
//		List<String> list;
//		for(Map<String,String> rs:audit){
//			String key2=rs.get("auditCode");
//			String key1=key2.substring(0, 2);
//			String value=DataUtil.nulltoempty(rs.get("value"));
//			if(result.containsKey(key1)){
//				sub= result.get(key1);
//			}else{
//				sub=new HashMap<String,List<String>>();
//			}
//			if(sub.containsKey(key2)){
//				list=sub.get(key2);
//			}else{
//				list=new ArrayList<String>();
//			}
//			list.add(value);
//			sub.put(key2,list);
//			result.put(key1, sub);
//		}
//		return result;
//	}
	public Map<String,String> getAuditsName(List<Map<String,String>> audit,Map<String, String> itemName) {
		Map<String,String> result=new TreeMap<String, String>();
		StringBuilder sb= new StringBuilder();
		for (Map<String,String> m:audit) {
			String k=m.get("auditCode");
			String v=DataUtil.nulltoempty(m.get("value"));
			if(result.containsKey(k)){
				sb.append(result.get(k)).append("、");
			}
			if(!k.endsWith("99")&&k.length()==4){
				sb.append(itemName.containsKey(v)?itemName.get(v):v);
			}else{
				sb.append(v);
			}
			result.put(k, sb.toString());
//			System.out.println(k+"="+sb.toString());
			sb.setLength(0);
		}
		return result;
	}
//	public Map<String,String> getAuditsName(Map<String,Map<String,List<String>>> audit,String auditCode,Map<String, String> itemName) {
//		Map<String,String> result=new TreeMap<String, String>();
//		if(audit.containsKey(auditCode)){
//			Map<String, List<String>> a=audit.get(auditCode);
//			StringBuilder sb= new StringBuilder();
//			for (Entry<String, List<String>> m:a.entrySet()) {
//				String k=m.getKey();
//				List<String> list=m.getValue();
//				for (String s:list) {
//					if(sb.length()>0){
//						sb.append("、");
//					}
//					if(!k.endsWith("99")&&k.length()==4){
//						sb.append(itemName.containsKey(s)?itemName.get(s):s);
//					}else{
//						sb.append(s);
//					}
//				}
//				result.put(k, sb.toString());
//				sb.setLength(0);
//			}
//		}
//		return result;
//	}
	public Map<String,Map<String,String>> getAduitsByInvestorSeq(String investorSeq,List<Map<String,String>> icase){
		Map<String,Map<String,String>> audits=dao.select(investorSeq);
		String investNo="";
		 for (int i = 0; i < icase.size(); i++) {
				investNo=icase.get(i).get("investNo");
				Map<String, String> sub;
				if(audits.containsKey(investNo)){
					sub=audits.get(investNo);
				}else{
					sub=new HashMap<String, String>();
				}
				/*String exp=iocSer.getErrorExceptXYear(investNo);
				String i0602=iocSer.getAuditInterviewError(investNo);
				String i0601=iocSer.getAuditFinancialError(investNo);
				String a0602=i0602.isEmpty()?"0":"1";
				String a0601=i0601.isEmpty()?"0":"1";
				sub.put("0602",i0602.isEmpty()?"0":i0602);
				sub.put("0601",i0601.isEmpty()?"0":i0601);
				if(!exp.isEmpty()){
					i0602=exp+"年-解散或撤銷或廢止或歇業";
					i0601=exp+"年-解散或撤銷或廢止或歇業";
					a0602="0";
					a0601="0";
					sub.put("0602",i0602);
					sub.put("0601",i0601);
				}*/
				Map<String,String> temp = dao.getAudit06(investNo);
				if(temp.containsKey("06")){
					String a06=temp.get("06");
					temp.put("06", a06.equals("是")?"1":"0");
				}
				sub.putAll(temp);
				sub.put("04",icSer.isSignificantInvest(investNo));
				/*if(!a0602.equals("1")&&!a0601.equals("1")&&(!sub.containsKey("0603")||sub.get("0603").isEmpty())){
					sub.put("06","0");
				}else{
					sub.put("06","1");
				}*/
				if(!sub.containsKey("0603")||sub.get("0603").isEmpty()){
					sub.put("0603","無");
				}
				audits.put(investNo, sub);
			}
		return audits;
	}
	public Map<String,String> getAduitsByInvestNo(String investNo){
		Map<String,String> sub = dao.getAduitByInvestNo(investNo);
		Map<String,String> temp = dao.getAudit06(investNo);
		if(temp.containsKey("06")){
			String a06=temp.get("06");
			temp.put("06", a06.equals("是")?"1":"0");
		}
		sub.putAll(temp);
		sub.put("04",icSer.isSignificantInvest(investNo));
		return sub;
	}
/*	public Map<String,String> getAduitsByInvestNo(String investNo){
		Map<String,String> sub = dao.getAduitByInvestNo(investNo);
		String i0602=iocSer.getAuditInterviewError(investNo);
		String i0601=iocSer.getAuditFinancialError(investNo);
		String a0602=i0602.isEmpty()?"0":"1";
		String a0601=i0601.isEmpty()?"0":"1";
		String exp=iocSer.getErrorExceptXYear(investNo);
		if(!exp.isEmpty()){
			i0602=exp+"年-解散或撤銷或廢止或歇業";
			i0601=exp+"年-解散或撤銷或廢止或歇業";
			a0602="0";
			a0601="0";
			sub.put("0602",i0602);
			sub.put("0601",i0601);
		}
		sub.put("0602",i0602.isEmpty()?"0":i0602);
		sub.put("0601",i0601.isEmpty()?"0":i0601);
		sub.put("04",icSer.isSignificantInvest(investNo));
		if(!a0602.equals("1")&&!a0601.equals("1")&&(!sub.containsKey("0603")||sub.get("0603").isEmpty())){
			sub.put("06","0");
		}else{
			sub.put("06","1");
		}
		return sub;
	}
*/	public String isEditable(String investNo){
		return dao.isEditable(investNo);
	}
//	public List<OFIAuditOption> getAuditOption(){
//		return dao.getAuditOption();
//	}
	public String getSPNeed(String investNo){
		return dao.getSPNeed(investNo);
	}
	public void delete(String investNo){
		dao.delete(investNo);
	}
	public void insert(List<OFIInvestNoXAudit> beans) {
		dao.insert(beans);
	}
	public void insertAudit02(List<OFIInvestNoXAudit> beans,String seqStr) {
		dao.insertAudit02(beans, seqStr);
	}
	public void deleteAudit02(String investNo,String seq){
		dao.deleteAudit02(investNo, seq);
	}
	public Map<String,Map<String,String>> getAudit02Details(String investNo){
		return dao.getAudit02Details(investNo);
	}
	public Map<String,Map<String,Map<String,String>>> getAudit02ByInvestorSeq(String investorSeq){
		return dao.getAudit02ByInvestorSeq(investorSeq);
	}
	public Map<String,String> getAuditAll(String investNo){
		return dao.getAuditAll(investNo);
	}
	
	public Map<String,Map<String,String>> getAudit07Details(String investNo){
		return dao.getAudit07Details(investNo);
	}
	public void insertAudit07(List<OFIInvestNoXAudit> beans,String seqStr) {
		dao.insertAudit07(beans, seqStr);
	}
	public void deleteAudit07(String investNo,String seq){
		dao.deleteAudit07(investNo, seq);
	}
	public Map<String,Map<String,Map<String,String>>> getAudit07ByInvestorSeq(String investorSeq){
		return dao.getAudit07ByInvestorSeq(investorSeq);
	}	
} 
