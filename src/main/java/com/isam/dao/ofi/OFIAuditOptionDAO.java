package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.OFIAuditOption;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIAuditOptionDAO {
	public List<OFIAuditOption> getAuditOption(){
		List<OFIAuditOption> result= new ArrayList<OFIAuditOption>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_AuditOption order by auditCode";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				OFIAuditOption bean = new OFIAuditOption();
				bean.setAuditCode(rs.getString("auditCode"));
				bean.setDescription(rs.getString("Description"));
				bean.setSelectName(DataUtil.nulltoempty(rs.getString("selectName")));
				bean.setIsMultiple(rs.getString("isMultiple"));
				bean.setAutoDef(rs.getString("autoDef"));
				result.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
