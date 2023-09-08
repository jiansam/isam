package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestBaseData;
import com.isam.bean.OFIInvestorXBG;
import com.isam.bean.OFIInvestorXRelated;
import com.isam.dao.ofi.OFIInvestorExcelDAO;
import com.isam.helper.PairHashtable;

public class OFIInvestorExcelService
{

	public Map<String, List<OFIInvestorXRelated>> getRelateds(List<String> investors){ //取出所有投資人的母公司資訊
		return OFIInvestorExcelDAO.getRelateds(investors);
	}
	
	public Map<String, List<OFIInvestorXBG>> getBGs(List<String> investors){ //取出所有投資人的背景資訊
		return OFIInvestorExcelDAO.getBGs(investors);
	}
	
	public Map<String, List<String>> getFiles(List<String> investors){ //取出所有投資人的架構圖ID
		return OFIInvestorExcelDAO.getFiles(investors);
	}
	
	public PairHashtable<String, String, OFIInvestBaseData> getInvestDatas(List<String> investors){ //取出所有投資人的投資案資料
		return OFIInvestorExcelDAO.getInvestDatas(investors);
	}
	
	public Map<String, OFIInvestBaseData> getMoeaicDatas(List<String> investNos){ //取出所有陸資案號的組織型態、發行方式
		return OFIInvestorExcelDAO.getMoeaicDatas(investNos);
	}
}
