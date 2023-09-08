package com.isam.bean;

import org.dasin.tools.dTools;

public class ProjectContact {
	String contact_name;
	String contact_tel_no;
	String COUNTY_NAME;
	String TOWN_NAME;
	String contact_ADDRESS;
	
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getContact_tel_no() {
		return contact_tel_no;
	}
	public void setContact_tel_no(String contact_tel_no) {
		this.contact_tel_no = contact_tel_no;
	}
	public String getCOUNTY_NAME() {
		return COUNTY_NAME;
	}
	public void setCOUNTY_NAME(String cOUNTY_NAME) {
		COUNTY_NAME = cOUNTY_NAME;
	}
	public String getTOWN_NAME() {
		return TOWN_NAME;
	}
	public void setTOWN_NAME(String tOWN_NAME) {
		TOWN_NAME = tOWN_NAME;
	}
	public String getContact_ADDRESS() {
		return contact_ADDRESS;
	}
	public void setContact_ADDRESS(String contact_ADDRESS) {
		this.contact_ADDRESS = contact_ADDRESS;
	}
	
	public String getComplete_Address() {
		return dTools.trim(COUNTY_NAME) + dTools.trim(TOWN_NAME) + dTools.trim(contact_ADDRESS);
	}
}
