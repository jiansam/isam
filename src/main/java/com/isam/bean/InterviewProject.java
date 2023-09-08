package com.isam.bean;

import java.io.Serializable;

public class InterviewProject implements Serializable{
	private static final long serialVersionUID = 1L;
	private int identifier;
	private int year;
	private String Name;
	private String SubName;

	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getSubName() {
		return SubName;
	}
	public void setSubName(String subName) {
		SubName = subName;
	}

}
