package com.isam.helper;

import java.util.HashMap;

import javax.servlet.ServletContext;

import com.isam.bean.Investment;
import com.isam.dao.InvestmentDAO;

public class ApplicationAttributeHelper {
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getInvestNoToName(ServletContext context){
		HashMap<String, String> result;
		
		if(context.getAttribute("InvestNoToName") == null) {
			result = new HashMap<String, String>();
			
			for(Investment bean : new InvestmentDAO().select()){
				result.put(bean.getInvestNo(), bean.getCompName());
			}
		}else {
			result = (HashMap<String, String>)context.getAttribute("InvestNoToName");
		}
		
		return result;
	}
}
