package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.dao.ofi.OFIManageScoreDAO;

public class OFIManageScoreService {
	OFIManageScoreDAO dao = null;
	public OFIManageScoreService(){
		dao = new OFIManageScoreDAO();
	}
	public Map<String,List<Map<String,String>>> select(String investNo){
		return dao.select(investNo);
	}
}
