package com.isam.bean;

public class InterviewoneContent{
	private int qNo;
	private int optionId;
	private String value;
	private int seq;
	private String optionName;
	
	
	@Override
	public String toString()
	{
		return "InterviewoneContent [value=" + value + ", optionName=" + optionName + "]";
	}
	public int getqNo() {
		return qNo;
	}
	public void setqNo(int qNo) {
		this.qNo = qNo;
	}
	public int getOptionId() {
		return optionId;
	}
	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getOptionName()
	{
		return optionName;
	}
	public void setOptionName(String optionName)
	{
		this.optionName = optionName;
	}
	
}
