package com.isam.service;

import java.util.List;

import com.isam.bean.CommitXOffice;
import com.isam.dao.CommitXOfficeDAO;

public class CommitXOfficeService {
	CommitXOfficeDAO dao = null;
	public CommitXOfficeService(){
		dao = new CommitXOfficeDAO();
	}
	
	public boolean isExists(String IDNO){
		List<CommitXOffice> list = dao.select(IDNO);
		if(list.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	public List<CommitXOffice> select(String IDNO){
		return dao.select(IDNO);
	}
	public String getOfficeStr(String IDNO){
		StringBuilder sb=new StringBuilder();
		List<CommitXOffice> list = dao.select(IDNO);
		for(int i=0;i<list.size();i++){
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append(list.get(i).getType());
		}
		return sb.toString();
	}
	public int insert(CommitXOffice bean) {
		return dao.insert(bean);
	}
	public void insertBatch(String[] restrainTypes,String IDNO,java.sql.Timestamp ctime,String cuser) {
		dao.insertBatch(restrainTypes, IDNO,ctime,cuser);
	}
	public void delete(String IDNO){
		dao.delete(IDNO);
	}
}
