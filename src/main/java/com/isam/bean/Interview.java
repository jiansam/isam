package com.isam.bean;

import java.io.Serializable;
import java.util.Date;

public class Interview implements Serializable{
	private static final long serialVersionUID = 1L;
	private int identifier;
	private int year;
	private String company;
	private boolean enable;

	private int project;
	private boolean publicity;
	private String companyEnglish;
	private String parentCompany;
	private String interviewee;
	private String interviewer;
	private Date interviewDate;
	private String interviewPlace;
	private String note;
	private String updatetime;

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
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getProject() {
		return project;
	}
	public void setProject(int project) {
		this.project = project;
	}
	public boolean isPublicity() {
		return publicity;
	}
	public void setPublicity(boolean publicity) {
		this.publicity = publicity;
	}
	public String getCompanyEnglish() {
		return companyEnglish;
	}
	public void setCompanyEnglish(String companyEnglish) {
		this.companyEnglish = companyEnglish;
	}
	public String getParentCompany() {
		return parentCompany;
	}
	public void setParentCompany(String parentCompany) {
		this.parentCompany = parentCompany;
	}
	public String getInterviewee() {
		return interviewee;
	}
	public void setInterviewee(String interviewee) {
		this.interviewee = interviewee;
	}
	public String getInterviewer() {
		return interviewer;
	}
	public void setInterviewer(String interviewer) {
		this.interviewer = interviewer;
	}
	public Date getInterviewDate() {
		return interviewDate;
	}
	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}
	public String getInterviewPlace() {
		return interviewPlace;
	}
	public void setInterviewPlace(String interviewPlace) {
		this.interviewPlace = interviewPlace;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public Interview(){
		setInterviewDate(new Date());
		setPublicity(true);
		setParentCompany("");
	}
	
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
}
