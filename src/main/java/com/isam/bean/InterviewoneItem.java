package com.isam.bean;

import java.io.Serializable;


public class InterviewoneItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int optionId;
	private String qType;
	private String cName;
	private String paramName;
	private String isText;
	private String enable;
	
	public int getOptionId() {
		return optionId;
	}
	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}
	public String getqType() {
		return qType;
	}
	public void setqType(String qType) {
		this.qType = qType;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getIsText() {
		return isText;
	}
	public void setIsText(String isText) {
		this.isText = isText;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
}
