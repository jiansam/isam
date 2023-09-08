package com.isam.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.InterviewoneContent;
import com.isam.dao.InterviewoneContentDAO;

public class InterviewoneContentService{
	InterviewoneContentDAO dao = null;
	public InterviewoneContentService(){
		dao = new InterviewoneContentDAO();
	}
	public List<String> getInterviewErrorList(){
		return dao.getInterviewErrorList();
	}
	public String getAuditInterviewError(String investNo){
		return dao.getInterviewErrorYear(investNo);
	}
	public String getErrorExceptXYear(String investNo){
		return dao.getErrorExceptXYear(investNo);
	}
/*	public String getAuditInterviewError(String investNo){
		return dao.getInterviewErrorList(investNo).isEmpty()?"0":"1";
	}
*/	public List<String> getInterviewErrorList(String investNo){
		return dao.getInterviewErrorList(null,investNo);
	}
	public boolean isInterviewError(String qNo){
		boolean result=false;
		if(!dao.getInterviewErrorList(qNo,null).isEmpty()){
			result=true;
		}
		return result;
	}
	public ArrayList<InterviewoneContent> getInterviewError(String qNo){
		ArrayList<InterviewoneContent> result = new ArrayList<>();
		ArrayList<InterviewoneContent> list = dao.getInterviewError(qNo);
		if(list != null){
			
			/*因為營業狀況會有複數，所以有兩個bean，預計先取出所有異常的OptionName，再組合複數的Optionvalue，放入新的list*/
			ArrayList<String> names = new ArrayList<>(); 
			Map<String, ArrayList<String>> temp = new HashMap<>();
			for(InterviewoneContent bean : list){
				
				String optionName = bean.getOptionName();
				
				//取出所有OptionName
				if(!names.contains(optionName)){
					names.add(optionName);
				}
				
				//以相同的 OptionName，組成map
				if(temp.get(optionName) == null){
					temp.put(optionName, new ArrayList<String>());
				}
				temp.get(optionName).add(bean.getValue());
			}
			//以names跑迴圈把 map裡面的值取出，再放回javabean裡面
			for(String optionName : names){
				StringBuilder valueStr = new StringBuilder();
				for(String value : temp.get(optionName)){
					if(valueStr.length() > 0){
						valueStr.append("、");
					}
					valueStr.append(value);
				}
				
				InterviewoneContent bean = new InterviewoneContent();
				bean.setOptionName(optionName);
				bean.setValue(valueStr.toString());
				result.add(bean);
			}
		}
		return result;
	}
	
/*	public String getAuditFinancialError(String investNo){
		return dao.getFinancialErrorList(investNo).isEmpty()?"0":"1";
	}*/
	public String getAuditFinancialError(String investNo){
		return dao.getFinancialErrorYear(investNo);
	}
	public List<String> getFinancialErrorList(){
		return dao.getFinancialErrorList();
	}
	public List<String> getFinancialErrorList(String investNo){
		return dao.getFinancialErrorList(null,investNo);
	}
	public boolean isFinancialError(String qNo){
		boolean result=false;
		if(!dao.getFinancialErrorList(qNo,null).isEmpty()){
			result=true;
		}
		return result;
	}
	public ArrayList<InterviewoneContent> getFinancialError(String qNo){
		return dao.getFinancialError(qNo);
	}
	
	public Map<String,String> getLastYearFinancial(int qNo,String year){
		return dao.getLastYearFinancial(qNo, year);
	}
	public Map<String,String> select(int qNo,String qType){
		return dao.select(qNo, qType);
	}
	public boolean isExist(int qNo,String qType){
		return dao.isExist(qNo,qType);
	}
	public void insert(List<InterviewoneContent> beans) {
		dao.insert(beans);
	}
	public int delete(int qNo,String qType) {
		return dao.delete(qNo,qType);
	}
} 
