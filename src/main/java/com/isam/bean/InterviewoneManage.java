package com.isam.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class InterviewoneManage implements Serializable{
	private static final long serialVersionUID = 1L;
	private int serno;
	private int qNo;
	private String receiveNo;
	private String receiveDate;
	private String issueNo;
	private String issueDate;
	private String issueby;
	private String optionValue;
	private String following;
	private String interviewer;
	private String interviewee;
	private String note;
	private String enable;
	private String flag;
	private Timestamp updatetime;
	private String updateuser;
	private Timestamp createtime;
	private String createuser;
	public int getSerno() {
		return serno;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
	public int getqNo() {
		return qNo;
	}
	public void setqNo(int qNo) {
		this.qNo = qNo;
	}
	public String getReceiveNo() {
		return receiveNo;
	}
	public void setReceiveNo(String receiveNo) {
		this.receiveNo = receiveNo;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getIssueby() {
		return issueby;
	}
	public void setIssueby(String issueby) {
		this.issueby = issueby;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public String getFollowing() {
		return following;
	}
	public void setFollowing(String following) {
		this.following = following;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getInterviewer() {
		return interviewer;
	}
	public void setInterviewer(String interviewer) {
		this.interviewer = interviewer;
	}
	public String getInterviewee() {
		return interviewee;
	}
	public void setInterviewee(String interviewee) {
		this.interviewee = interviewee;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
