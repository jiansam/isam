package com.isam.service.ofi;

import java.util.ArrayList;
import java.util.List;

import com.isam.bean.OFINTBTDatas;
import com.isam.dao.ofi.OFINTBTDatasDAO;

public class OFINTBTDatasService
{
	OFINTBTDatasDAO dao = null;

	public OFINTBTDatasService(){
		dao = new OFINTBTDatasDAO();
	}
	
	public int insert(OFINTBTDatas bean) {
		return dao.insert(bean);
	}
	public int unable(int id,String updateuser) {
		return dao.unable(id,updateuser);
	}
	public void update(OFINTBTDatas bean) {
		dao.update(bean);
	}
	public ArrayList<OFINTBTDatas> list(String investNo){
		return dao.list(investNo);
	}
	public ArrayList<OFINTBTDatas> uploadFile(String investNo){
		return dao.uploadFile(investNo);
	}
	public OFINTBTDatas get(int id){
		return dao.get(id);
	}
}
