package com.isam.bean;

import java.io.Serializable;
import java.util.Date;

import Lara.Utility.DateUtil;

public class OFIInvestorXFile implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int fNo;
	private String investorSeq;
	private String title;
	private String fName;
	private byte[] fContent;
	private String note;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	private String enable;
	private String INVESTOR_CHTNAME; //106-08-21新增供ZIP下載使用
	private String createtime_str; //107-07-04 供投資人、投資案 列表頁 出 架構圖用
	
	
	@Override
	public String toString()
	{
		return "[fNo=" + fNo + ", investorSeq=" + investorSeq + ", title=" + title + ", fName=" + fName
				+ "]";
	}
	public int getfNo() {
		return fNo;
	}
	public void setfNo(int fNo) {
		this.fNo = fNo;
	}
	public String getInvestorSeq() {
		return investorSeq;
	}
	public void setInvestorSeq(String investorSeq) {
		this.investorSeq = investorSeq;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public byte[] getfContent() {
		return fContent;
	}
	public void setfContent(byte[] fContent) {
		this.fContent = fContent;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public java.sql.Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(java.sql.Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	public java.sql.Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(java.sql.Timestamp createtime) {
		this.createtime = createtime;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getINVESTOR_CHTNAME()
	{
		return INVESTOR_CHTNAME;
	}
	public void setINVESTOR_CHTNAME(String iNVESTOR_CHTNAME)
	{
		INVESTOR_CHTNAME = iNVESTOR_CHTNAME;
	}
	public void setCreatetime_str(java.sql.Timestamp createtime) {
		createtime_str = DateUtil.dateToChangeROC(new Date(createtime.getTime()), "yyyy/MM/dd", "EN");
	}
	public String getCreatetime_str() {
		return createtime_str;
	}
}
