package com.isam.bean;

import java.io.Serializable;
import java.util.*;

public class InterviewBrief implements Serializable{
	private static final long serialVersionUID = 1L;
	private int identifier;
	private int year;
	private String company;
	private String description;
	private String updateuser;
	private Date updatetime;

	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public InterviewBrief(){
		setYear(Calendar.getInstance().get(Calendar.YEAR) - 1911);
	}
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
}
