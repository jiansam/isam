package com.isam.bean;

import java.io.Serializable;
import java.util.Date;

import Lara.Utility.DateUtil;

public class OFINTBTDatas implements Serializable
{

	int id;
	String investNo;
	String title;
	String fName;
	byte[] fContent;
	String note;
	Date updatetime;
	String updateuser;
	Date createtime;
	String createuser;
	boolean enable;
	String createtime_ROC;
	String updatetime_ROC;
	
	
	
	@Override
	public String toString()
	{
		return "OFINTBTDatas [id=" + id + ", investNo=" + investNo + ", title=" + title + ", fName=" + fName
				+ ", createtime=" + createtime + "]";
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getInvestNo()
	{
		return investNo;
	}
	public void setInvestNo(String investNo)
	{
		this.investNo = investNo;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getfName()
	{
		return fName;
	}
	public void setfName(String fName)
	{
		this.fName = fName;
	}
	public byte[] getfContent()
	{
		return fContent;
	}
	public void setfContent(byte[] fContent)
	{
		this.fContent = fContent;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public Date getUpdatetime()
	{
		return updatetime;
	}
	public void setUpdatetime(Date updatetime)
	{
		this.updatetime = updatetime;
	}
	public String getUpdateuser()
	{
		return updateuser;
	}
	public void setUpdateuser(String updateuser)
	{
		this.updateuser = updateuser;
	}
	public Date getCreatetime()
	{
		return createtime;
	}
	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}
	public String getCreateuser()
	{
		return createuser;
	}
	public void setCreateuser(String createuser)
	{
		this.createuser = createuser;
	}
	public boolean isEnable()
	{
		return enable;
	}
	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}
	
	public void setUpdatetime_ROC(Date updatetime)
	{
		this.updatetime_ROC = DateUtil.dateToChangeROC(updatetime, "yyyy/MM/dd", "EN");
	}
	public String getUpdatetime_ROC()
	{
		return updatetime_ROC;
	}

	public void setCreatetime_ROC(Date createtime)
	{
		this.createtime_ROC = DateUtil.dateToChangeROC(createtime, "yyyy/MM/dd", "EN");
	}
	public String getCreatetime_ROC()
	{
		return createtime_ROC;
	}
}
