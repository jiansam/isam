package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.dao.ApprovalDAO;

public class ApprovalService {
	ApprovalDAO dao = null;
	public ApprovalService(){
		dao = new ApprovalDAO();
	}
	public List<Map<String,String>> getApprovalMapList(String IDNO,String investor,String investNo,String cName,String state,String com){
		return dao.getApprovalMapList(IDNO, investor, investNo, cName, state,com);
	}
}
