package com.isam.bean;

import java.io.Serializable;
import java.util.Date;

public class InterviewFile implements Serializable{
	private static final long serialVersionUID = 1L;
	private int identifier;
	private int interviewIdentifier;
	private String filename;
	private int fileSize;
	private Date uploadDate;
	private String purpose;

	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public int getInterviewIdentifier() {
		return interviewIdentifier;
	}
	public void setInterviewIdentifier(int interviewIdentifier) {
		this.interviewIdentifier = interviewIdentifier;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
}
