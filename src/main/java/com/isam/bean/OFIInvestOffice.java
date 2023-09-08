package com.isam.bean;

import java.io.Serializable;

public class OFIInvestOffice implements Serializable{
	private static final long serialVersionUID = 1L;
	private String banno;
	private String compname; //
	private String status;
	private String location;
	private String setupdate;


	private String sdate;
	private String edate;
	private String agent;

	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String Location) {
		this.location = Location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String Status) {
		this.status = Status;
	}
	public String getBanNo() {
		return banno;
	}
	public void setBanNo(String Ban_No) {
		this.banno = Ban_No;
	}
	
	public String getCompname()
	{
		return compname;
	}
	public void setCompname(String cOMP_CHTNAME)
	{
		compname = cOMP_CHTNAME;
	}

	public String getSetupdate() {
		return setupdate;
	}
	public void setSetupdate(String setupdate) {
		this.setupdate = setupdate;
	}
	
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	
	
}
