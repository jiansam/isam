package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.isam.bean.OFIInvestorXBG;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIInvestorXBGDAO {
	
	public Map<String,Map<String,String>> getBGByInvestNo(String investNo){
		Map<String,Map<String,String>> map= new TreeMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb= new StringBuilder();
		sb.append("SELECT investorSeq,bgType,case when o.SelectName is null then value else o.Description end value FROM OFI_InvestorXBG b ");
		sb.append("left join (SELECT Description,SelectName,OptionValue FROM OFI_WebOption where enable='1' and SelectName like 'BG%') o ");
		sb.append("on o.SelectName=b.bgType and o.OptionValue=b.value  where investorSeq in (SELECT INVESTOR_SEQ ");
		sb.append("FROM OFI_InvestCase where investNo=? and INVE_ROLE_CODE='3' and isVisible='1' ) order by bgType,seq");
		String forStmt =sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			Map<String,String> sub;
			while(rs.next()){
				String investorSeq=rs.getString("investorSeq");
				String bgType=rs.getString("bgType");
				String v=rs.getString("value");
				if(map.containsKey(investorSeq)){
					sub=map.get(investorSeq);
				}else{
					sub=new TreeMap<String, String>();
				}
				if(sub.containsKey(bgType)){
					sb.append(sub.get(bgType)).append("、").append(v);
					sub.put(bgType, sb.toString());
				}else{
					sub.put(bgType, v);
				}
				sb.setLength(0);
				map.put(investorSeq, sub);
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
		return map;
	}
	public List<OFIInvestorXBG> select(String investorSeq){
		List<OFIInvestorXBG> list= new ArrayList<OFIInvestorXBG>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestorXBG where investorSeq=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				OFIInvestorXBG bean= new OFIInvestorXBG();
				bean.setInvestorSeq(rs.getString("investorSeq"));
				bean.setBgType(rs.getString("bgType"));
				bean.setValue(rs.getString("value"));
				bean.setSeq(rs.getInt("seq"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				list.add(bean);
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
		return list;
	}
	public void delete(String investorSeq){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_InvestorXBG WHERE investorSeq = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, investorSeq);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void insert(List<OFIInvestorXBG> beans) {
		String forpstmt = "insert into OFI_InvestorXBG values(?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIInvestorXBG bean= beans.get(i);
				pstmt.setString(1, bean.getInvestorSeq());
				pstmt.setString(2, bean.getBgType());
				pstmt.setString(3, bean.getValue());
				pstmt.setInt(4, bean.getSeq());
				pstmt.setTimestamp(5, bean.getCreatetime());
				pstmt.setString(6, bean.getCreateuser());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			sqltool.commit();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public String isEditable(String investorSeq){
		String result="0";
		SQL sqltool = new SQL();
		String forStmt = "select distinct createuser from OFI_InvestorXBG where investorSeq=?";
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				String user=DataUtil.nulltoempty(rs.getString("createuser"));
				if(!user.isEmpty()&&!user.equals("admin")){
					result="1";
				}
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
