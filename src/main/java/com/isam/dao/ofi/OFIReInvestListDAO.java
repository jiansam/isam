package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIReInvestList;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIReInvestListDAO {
	public Map<String,String> getReinvestNoNameMap(String investNo){
		Map<String,String> result = new HashMap<String,String>();
		SQL sqltool = new SQL();
		String forStmt ="SELECT reInvestNo,reinvestment from OFI_ReInvestmentList where enable='1' and reInvestNo in (select reInvestNo from OFI_ReInvestXInvestNo where investNO=?)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("reInvestNo"),rs.getString("reinvestment"));
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
	public Map<String,List<String>> getReinvestNoItems(String investNo,String type){
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		SQL sqltool = new SQL();
		String forStmt ="SELECT reInvestNo,item from OFI_ReInvestXTWSIC where reInvestNo in (select reInvestNo from OFI_ReInvestXInvestNo where investNO=? and enable='1') and type=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, type);
			ResultSet rs = stmt.executeQuery();
			List<String> sub;
			String serno;
			while(rs.next()){
				serno=rs.getString("reInvestNo");
				if(result.containsKey(serno)){
					sub=result.get(serno);
				}else{
					sub=new ArrayList<String>();
				}
				sub.add(rs.getString("item"));
				result.put(serno,sub);
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
	public List<OFIReInvestList> select(String investNo,String reInvestNo){
		List<OFIReInvestList> list= new ArrayList<OFIReInvestList>();
		SQL sqltool = new SQL();
		StringBuilder sb= new StringBuilder();
		sb.append("SELECT a.* ,b.investNo,note,reinvestMoney,shareholding,shareholdingValue,shareholdingRatio ");
		sb.append("FROM OFI_ReInvestmentList a,OFI_ReInvestXInvestNo b where a.reInvestNo=isnull(?,a.reInvestNo) ");
		sb.append("and investNo=isnull(?,investNo) and a.enable='1' and a.reInvestNo=b.reInvestNo");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, reInvestNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				OFIReInvestList bean= new OFIReInvestList();
				bean.setReInvestNo(rs.getString("reInvestNo"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReinvestment(rs.getString("reinvestment"));
				bean.setOrgType(rs.getString("orgType"));
				bean.setIdno(rs.getString("idno"));
				bean.setCity(rs.getString("city"));
				bean.setTown(rs.getString("town"));
				bean.setAddr(rs.getString("addr"));
				bean.setIsNew(rs.getString("isNew"));
				bean.setSetupdate(rs.getString("setupdate"));
				bean.setSetupnote(rs.getString("setupnote"));
				bean.setStockNum(DataUtil.nulltoempty(rs.getString("stockNum")));
				bean.setFaceValue(DataUtil.nulltoempty(rs.getString("faceValue")));
				bean.setRegiCapital(DataUtil.nulltoempty(rs.getString("regi_capital")));
				bean.setPaidCapital(DataUtil.nulltoempty(rs.getString("paid_capital")));
				bean.setIsOperated(DataUtil.nulltoempty(rs.getString("isOperated")));
				bean.setSdate(rs.getString("sdate"));
				bean.setEdate(rs.getString("edate"));
				bean.setNote(rs.getString("note"));
				bean.setReinvestMoney(DataUtil.nulltoempty(rs.getString("reinvestMoney")));
				bean.setShareholding(DataUtil.nulltoempty(rs.getString("shareholding")));
				bean.setShareholdingValue(DataUtil.nulltoempty(rs.getString("shareholdingValue")));
				bean.setShareholdingRatio(DataUtil.nulltoempty(rs.getString("shareholdingRatio")));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setIsFilled(rs.getString("isFilled"));
				bean.setEnable(rs.getString("enable"));
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
	public void update(OFIReInvestList bean) {
		StringBuffer sb = new StringBuffer();
		sb.append("update OFI_ReInvestmentList set orgType=?,idno=?,city=?,town=?,addr=?,isNew=?,setupdate=?,setupnote=?");
		sb.append(",stockNum=?,faceValue=?,regi_capital=?,paid_capital=?,isOperated=?,sdate=?,edate=?,");
		sb.append("updatetime=?,updateuser=?,isFilled=? Where reInvestNo=?");
		String forpstmt =sb.toString();
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getOrgType());
			pstmt.setString(2, bean.getIdno());
			pstmt.setString(3, bean.getCity());
			pstmt.setString(4, bean.getTown());
			pstmt.setString(5, bean.getAddr());
			pstmt.setString(6, bean.getIsNew());
			pstmt.setString(7, bean.getSetupdate());
			pstmt.setString(8, bean.getSetupnote());
			pstmt.setString(9, bean.getStockNum());
			pstmt.setString(10, bean.getFaceValue());
			pstmt.setString(11, bean.getRegiCapital());
			pstmt.setString(12, bean.getPaidCapital());
			pstmt.setString(13, bean.getIsOperated());
			pstmt.setString(14, bean.getSdate());
			pstmt.setString(15, bean.getEdate());
			pstmt.setTimestamp(16, bean.getUpdatetime());
			pstmt.setString(17, bean.getUpdateuser());
			pstmt.setString(18, bean.getIsFilled());
			pstmt.setString(19, bean.getReInvestNo());
			pstmt.executeUpdate();
			pstmt = sqltool.prepare("update OFI_ReInvestXInvestNo set note=?,updatetime=?,updateuser=? Where reInvestNo=? and investNo=?");
			pstmt.setString(1, bean.getNote());
			pstmt.setTimestamp(2, bean.getUpdatetime());
			pstmt.setString(3, bean.getUpdateuser());
			pstmt.setString(4, bean.getReInvestNo());
			pstmt.setString(5, bean.getInvestNo());
			pstmt.executeUpdate();
			sb.append("update OFI_ReInvestXInvestNo set shareholdingValue=(case when (orgType='01' or orgType='02') then x.shareholding*faceValue ");
			sb.append("else case when (orgType!='01' and orgType!='02' and orgType!='0') then x.reinvestMoney else null end ");
			sb.append("end),shareholdingRatio=(case when (orgType='01' or orgType='02') then x.shareholding*faceValue/PAID_CAPITAL*100 ");
			sb.append("else case when (orgType!='01' and orgType!='02' and orgType!='0') then x.reinvestMoney/PAID_CAPITAL*100 else null end end) ");
			sb.append("from (SELECT a.reInvestNo,a.shareholding,a.reinvestMoney,b.orgType,b.faceValue,b.paid_capital ");
			sb.append("FROM OFI_ReInvestXInvestNo a,OFI_ReInvestmentList b where a.reInvestNo=b.reInvestNo and  a.reInvestNo=? and a.investNo=?)x ");
			forpstmt =sb.toString();
			pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getReInvestNo());
			pstmt.setString(2, bean.getInvestNo());
			sb.setLength(0);
			pstmt.executeUpdate();
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
//	public void updateShareholdingRatio(String reInvestNo,String investNo) {
//		StringBuffer sb = new StringBuffer();
//		sb.append("update OFI_ReInvestXInvestNo set shareholdingValue=(case when (orgType='01' or orgType='02') then shareholding*faceValue ");
//		sb.append("else case when (orgType!='01' and orgType!='02' and orgType!='0') then reinvestMoney else null end ");
//		sb.append("end),shareholdingRatio=(case when (orgType='01' or orgType='02') then shareholding*faceValue/PAID_CAPITAL*100 ");
//		sb.append("else case when (orgType!='01' and orgType!='02' and orgType!='0') then reinvestMoney/PAID_CAPITAL*100 else null end end) ");
//		sb.append("Where reInvestNo=?");
//		String forpstmt =sb.toString();
//		sb.setLength(0);
//		SQL sqltool = new SQL();
//		try {
//			PreparedStatement pstmt = sqltool.prepare(forpstmt);
//			pstmt.setString(1, reInvestNo);
//			pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				sqltool.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
