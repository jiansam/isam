package com.isam.bean;

import org.dasin.tools.dTools;

public class OFITWSIC {
	String code;
	String codename;
	String isSP;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodename() {
		return codename;
	}
	public void setCodename(String codename) {
		this.codename = codename;
	}
	public String getIsSP() {
		return isSP;
	}
	public void setIsSP(String isSP) {
		this.isSP = isSP;
	}
	
	public String getParent() {
		String result = dTools.trim(code); 
		if(result.length() > 4) {
			return result.substring(0, 4);
		}else {
			return "";
		}
	}
	
	public String getLevel() {
		return dTools.trim(code).length() > 4 ? "2" : "1";
	}
}
