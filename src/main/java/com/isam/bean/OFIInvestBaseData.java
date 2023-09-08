package com.isam.bean;

import java.io.Serializable;

public class OFIInvestBaseData implements Serializable
{
	//陸資公司資料
	String investNo; //陸資案號
	String COMP_CHTNAME; //公司名稱
	String ID_NO; //統一編號
	String orgTypeName; //公司類型
	String ISSUE_TYPE_NAME; //發行方式

	//投資人資料
	String investorSeq;
	String INVESTOR_CHTNAME;
	String REGI_CAPITAL; //登記資本額
	String PAID_CAPITAL; //實收資本額（分公司就 同登記資本額）
	String FACE_VALUE; //面額（若是股份有限公司就 無須填寫）小數點到6位
	String investvalue; //投資金額
	String investedcapital; //持有股權
	String sp; //股權比例
	String inrole; //資金類型 INVE_ROLE_CODE
	String country; //國家
	String cnCode; //城市
	boolean isFilled; //待確認 investor  0未確認 1已確認 
	String note; //備註
	
	
	
	
	
	@Override
	public String toString()
	{
		return "[investNo=" + investNo + ", COMP_CHTNAME=" + COMP_CHTNAME + ", ID_NO=" + ID_NO
				+ ", investorSeq=" + investorSeq + ", INVESTOR_CHTNAME=" + INVESTOR_CHTNAME + ", REGI_CAPITAL="
				+ REGI_CAPITAL + ", PAID_CAPITAL=" + PAID_CAPITAL + ", FACE_VALUE=" + FACE_VALUE + ", investvalue="
				+ investvalue + ", investedcapital=" + investedcapital + ", sp=" + sp + ", inrole=" + inrole
				+ ", country=" + country + ", cnCode=" + cnCode + ", isFilled=" + isFilled + ", note=" + note + "]";
	}
	public String getInvestNo()
	{
		return investNo;
	}
	public void setInvestNo(String investNo)
	{
		this.investNo = investNo;
	}
	public String getCOMP_CHTNAME()
	{
		return COMP_CHTNAME;
	}
	public void setCOMP_CHTNAME(String cOMP_CHTNAME)
	{
		COMP_CHTNAME = cOMP_CHTNAME;
	}
	public String getID_NO()
	{
		return ID_NO;
	}
	public void setID_NO(String iD_NO)
	{
		ID_NO = iD_NO;
	}
	public String getInvestorSeq()
	{
		return investorSeq;
	}
	public void setInvestorSeq(String investorSeq)
	{
		this.investorSeq = investorSeq;
	}
	public String getINVESTOR_CHTNAME()
	{
		return INVESTOR_CHTNAME;
	}
	public void setINVESTOR_CHTNAME(String iNVESTOR_CHTNAME)
	{
		INVESTOR_CHTNAME = iNVESTOR_CHTNAME;
	}
	public String getREGI_CAPITAL()
	{
		return REGI_CAPITAL;
	}
	public void setREGI_CAPITAL(String rEGI_CAPITAL)
	{
		REGI_CAPITAL = rEGI_CAPITAL;
	}
	public String getPAID_CAPITAL()
	{
		return PAID_CAPITAL;
	}
	public void setPAID_CAPITAL(String pAID_CAPITAL)
	{
		PAID_CAPITAL = pAID_CAPITAL;
	}
	public String getFACE_VALUE()
	{
		return FACE_VALUE;
	}
	public void setFACE_VALUE(String fACE_VALUE)
	{
		FACE_VALUE = fACE_VALUE;
	}
	public String getInvestvalue()
	{
		return investvalue;
	}
	public void setInvestvalue(String investvalue)
	{
		this.investvalue = investvalue;
	}
	public String getInvestedcapital()
	{
		return investedcapital;
	}
	public void setInvestedcapital(String investedcapital)
	{
		this.investedcapital = investedcapital;
	}
	public String getSp()
	{
		return sp;
	}
	public void setSp(String sp)
	{
		this.sp = sp;
	}
	public String getInrole()
	{
		return inrole;
	}
	public void setInrole(String inrole)
	{
		this.inrole = inrole;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
	public String getCnCode()
	{
		return cnCode;
	}
	public void setCnCode(String cnCode)
	{
		this.cnCode = cnCode;
	}
	public boolean isFilled()
	{
		return isFilled;
	}
	public void setFilled(boolean isFilled)
	{
		this.isFilled = isFilled;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public String getOrgTypeName()
	{
		return orgTypeName;
	}
	public void setOrgTypeName(String orgTypeName)
	{
		this.orgTypeName = orgTypeName;
	}
	public String getISSUE_TYPE_NAME()
	{
		return ISSUE_TYPE_NAME;
	}
	public void setISSUE_TYPE_NAME(String iSSUE_TYPE_NAME)
	{
		ISSUE_TYPE_NAME = iSSUE_TYPE_NAME;
	}
	
	
}
