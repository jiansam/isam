package com.isam.bean;

import java.io.Serializable;

public class ProjectReportName extends ProjectReport implements Serializable{
	private static final long serialVersionUID = 1L;
	private String idno;
	private String investor;
	private String investNo;
	private String investment;
	
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getInvestor() {
		return investor;
	}
	public void setInvestor(String investor) {
		this.investor = investor;
	}
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}
	public String getInvestment() {
		return investment;
	}
	public void setInvestment(String investment) {
		this.investment = investment;
	}
}
