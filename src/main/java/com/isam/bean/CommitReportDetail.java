package com.isam.bean;


public class CommitReportDetail{
	private int repserno;
	private String Year;
	private String type;
	private double corpvalue;
	private double repvalue;
	private int count;
	private String enable;
	
	public int getRepserno() {
		return repserno;
	}
	public void setRepserno(int repserno) {
		this.repserno = repserno;
	}
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getCorpvalue() {
		return corpvalue;
	}
	public void setCorpvalue(double corpvalue) {
		this.corpvalue = corpvalue;
	}
	public double getRepvalue() {
		return repvalue;
	}
	public void setRepvalue(double repvalue) {
		this.repvalue = repvalue;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
}
