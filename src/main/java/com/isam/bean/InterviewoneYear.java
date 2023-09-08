package com.isam.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InterviewoneYear implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int qNo;
	private String investNo;
	private String companyname;
	private String banno;
	private String dataStatus;
	private String operationStatus;
	private int money1; 
	private int money2;
	private int stockimp;
	private String approvalDate;
	private String setupDate;
	private String item;
	private String type;
	private String isFilled;
	private String nation;
	private String cnCode;
	private String isOpearted;
	private String investorseq;
	private List<Interviewone> interviewone = new ArrayList<Interviewone>();

	private List<OFIInvestNoXAudit> audit = new ArrayList<OFIInvestNoXAudit>();
	private List<OFIInvestorXBG> xbg = new ArrayList<OFIInvestorXBG>();
	private List<OFIInvestNoXTWSIC> twsic = new ArrayList<OFIInvestNoXTWSIC>();
	private List<OFIInvestorXRelated> related = new ArrayList<OFIInvestorXRelated>();
	private String enable;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	private String msg;
	private int fileCount; //106-11-29新增 紀錄[下載檔案的數量]
	private String businessIncomeTaxCode; //2018.7.13.dasin : 新增行業別顯示
	
	public String getBusinessIncomeTaxCode() {
		return businessIncomeTaxCode;
	}
	public void setBusinessIncomeTaxCode(String businessIncomeTaxCode) {
		this.businessIncomeTaxCode = businessIncomeTaxCode;
	}
	public  List<OFIInvestNoXTWSIC> getTwsic(){
		twsic.forEach((_item)->{
			if( _item.getType().equals("0")) {
				type = "O";
				
			}
			
			if( _item.getItem().startsWith("I3")) {
				item = "O";
			}
			
			
		});
	return twsic;
	}
	
	public  List<Interviewone> getInterviewone(){
		return interviewone;
	}
	public  List<OFIInvestorXRelated> getRelated(){
		return related;
	}
	public  List<OFIInvestorXBG> getXBG(){
		return xbg;
	}
	public  List<OFIInvestNoXAudit> getAudit(){
		return audit;
	}
	public int getqNo() {
		return qNo;
	}
	public void setqNo(int qNo) {
		this.qNo = qNo;
	}
	
	public String getInvestorSeq() {
		return investorseq;
	}
	public void setInvestorSeq(String investorseq) {
		this.investorseq = investorseq;
	}
	
	public String getType() {
		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsOperated() {
		return isOpearted;
	}
	public void setIsOperated(String isOpearted) {
		this.isOpearted = isOpearted;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	
	public String getCnCode() {
		return cnCode;
	}
	public void setCnCode(String cnCode) {
		this.cnCode = cnCode;
	}
	public String getIsFilled() {
		return isFilled;
	}
	public void setIsFilled(String isFilled) {
		this.isFilled = isFilled;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getSetupDate() {
		return setupDate;
	}
	public void setSetupDate(String setupDate) {
		this.setupDate = setupDate;
	}
	public String getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	
	public String getOperationStatus() {
		return operationStatus;
	}
	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}
	
	public String getCompanyName() {
		return companyname;
	}
	public void setCompanyName(String companyname) {
		this.companyname = companyname;
	}
	
	public String getBanNo() {
		return banno;
	}
	public void setBanNo(String banno) {
		this.banno = banno;
	}
	
	public int getMoney1() {
		return money1;
	}
	public void setMoney1(int money1) {
		this.money1 = money1;
	}
	
	public int getMoney2() {
		return money2;
	}
	public void setMoney2(int money2) {
		this.money2 = money2;
	}
	
	public int getStockimp() {
		return stockimp;
	}
	public void setStockimp(int stockimp) {
		this.stockimp = stockimp;
	}
	
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}

	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getFileCount()
	{
		return fileCount;
	}
	public void setFileCount(int fileCount)
	{
		this.fileCount = fileCount;
	}
	
	
}
