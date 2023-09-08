package com.isam.bean;

import java.io.Serializable;

public class CommitDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	private int serno;
	private String type;
	private String year;
	private double value;
	private String total;
	private String isFinancial;
	private String enable;
	
	public int getSerno() {
		return serno;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getIsFinancial() {
		return isFinancial;
	}
	public void setIsFinancial(String isFinancial) {
		this.isFinancial = isFinancial;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
}
