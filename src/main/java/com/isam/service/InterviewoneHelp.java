package com.isam.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.isam.bean.InterviewoneItem;
import com.isam.bean.InterviewoneOption;
import com.isam.dao.InterviewoneHelperDAO;


public class InterviewoneHelp {
	private InterviewoneHelperDAO iohelper;
	private Map<String,Map<String,String>> optionValName= null;
	private Map<String,InterviewoneItem> qTypeS= null;
	private Map<String,InterviewoneItem> qTypeI= null;
	private Map<String,InterviewoneItem> qTypeF= null;
	private Map<String,Map<String,Integer>> optionValNo= null;
	private List<InterviewoneItem> itemlist;
	private List<InterviewoneOption> optionlist;
	
	public InterviewoneHelp(){
		initial();
		read();
	}
	private void initial(){
		iohelper= new InterviewoneHelperDAO();
		optionValName =new HashMap<String, Map<String,String>>();
		optionValNo =new HashMap<String, Map<String,Integer>>();
		itemlist = new ArrayList<InterviewoneItem>();
		optionlist = new ArrayList<InterviewoneOption>();
		qTypeS = new HashMap<String, InterviewoneItem>();
		qTypeI = new HashMap<String, InterviewoneItem>();
		qTypeF = new TreeMap<String, InterviewoneItem>();
	}
	private void read(){
		getInterviewoneOption();
		getInterviewoneItem();
	}
	private void getInterviewoneOption(){
		List<InterviewoneOption> list=iohelper.getOptions();
		Map<String,String> temp;
		Map<String,Integer> temp2;
		for (int i = 0; i < list.size(); i++) {
			InterviewoneOption bean = list.get(i);
			String key=bean.getParamName();
			if(optionValName.containsKey(key)){
				temp=optionValName.get(key);
			}else{
				temp=new LinkedHashMap<String, String>();
			}
			if(optionValNo.containsKey(key)){
				temp2=optionValNo.get(key);
			}else{
				temp2=new LinkedHashMap<String, Integer>();
			}
			temp.put(bean.getOptionValue(), bean.getCname());
			temp2.put(bean.getOptionValue(), bean.getOptionNo());
			optionValName.put(key, temp);
			optionValNo.put(key, temp2);
		}
	}
	private void getInterviewoneItem(){
		List<InterviewoneItem> items=iohelper.getItems();
		itemlist.addAll(items);
		for (int i = 0; i < items.size(); i++) {
			InterviewoneItem bean = items.get(i);
			if(bean.getqType().equals("I")){
				qTypeI.put(bean.getParamName(), bean);
			}else if(bean.getqType().equals("S")){
				qTypeS.put(bean.getParamName(), bean);
			}else if(bean.getqType().equals("F")){
				qTypeF.put(String.valueOf(bean.getOptionId()), bean);
			}
		}
		optionlist.addAll(iohelper.getOptions());
	}
	
	public Map<String, Integer> getoptidbyparam(Map<String,InterviewoneItem> map) {
		Map<String, Integer> result=new HashMap<String, Integer>();
		for (Entry<String, InterviewoneItem> m :map.entrySet()) {
			InterviewoneItem bean=m.getValue();
			result.put(bean.getParamName(), bean.getOptionId());
		}
		return result;
	}
	public Map<String, Map<String, String>> getOptionValName() {
		return optionValName;
	}
	public List<InterviewoneItem> getItemlist() {
		return itemlist;
	}
	public List<InterviewoneOption> getOptionlist() {
		return optionlist;
	}
	public Map<String, Map<String, Integer>> getOptionValNo() {
		return optionValNo;
	}
	public Map<String, InterviewoneItem> getqTypeS() {
		return qTypeS;
	}
	public Map<String, InterviewoneItem> getqTypeI() {
		return qTypeI;
	}
	public Map<String, InterviewoneItem> getqTypeF() {
		return qTypeF;
	}

}
