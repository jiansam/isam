package com.isam.bean;

public class Investor {
	private String IDNO;
	private String oIDNO;
	private String investor;
	
	public String getIDNO() {
		return IDNO;
	}
	public void setIDNO(String iDNO) {
		IDNO = iDNO;
	}
	public String getoIDNO() {
		return oIDNO;
	}
	public void setoIDNO(String oIDNO) {
		this.oIDNO = oIDNO;
	}
	public String getInvestor() {
		return investor;
	}
	public void setInvestor(String investor) {
		this.investor = investor;
	}
}
