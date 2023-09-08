package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.helper.SQL;

public class ApprovalDAO {
	
	public List<Map<String,String>> getApprovalMapList(String IDNO,String investor,String investNo,String cName,String state,String com){
		SQL sqltool = new SQL();
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (select ISNULL(c.IDNO,d.IDNO) idno,ISNULL(c.investNo,d.investNo) investNo,d.state,isnull(d.PC,0) pc,ISNULL(c.CC,0) cc ,isnull(isC,'0') isC,isnull(isP,'0')isP	from ");
		sb.append("(select i.IDNO,i.investNo,sum(k.CC) cc,'1' isC from (select a.IDNO,b.investNo,a.[serno] from ");
		sb.append("(SELECT  [serno],[IDNO] FROM [Commit] where [enable]='1')a left join (SELECT [serno],[investNo] FROM [CommitXInvestNo])b ");
		sb.append("on a.serno=b.serno group by a.IDNO,b.investNo,a.serno )i left join (SELECT [serno] x,count(*) as CC FROM [CommitReport] where enable='1' group by [serno]) k ");
		sb.append("on k.x=i.serno group by i.IDNO,i.investNo) c full join (select op.[IDNO],op.[investNo],op.state,k.PC PC,'1' isP from ");
		sb.append("(SELECT [serno],[IDNO],[investNo],state FROM [Project] where state in (").append(state).append(") group by [serno],[investNo],[IDNO],[state]) op ");
		sb.append("left join (SELECT [serno] serno,count(*) as PC FROM [ProjectReport] where enable='1' group by  [serno]) k ");
		sb.append("on k.serno=op.serno )d on c.IDNO=d.IDNO and c.investNo=d.investNo)rs ");
		sb.append("where idno in (SELECT IDNO FROM CDataInvestor where INVESTOR_CHTNAME like ? and (oIDNO like ? or IDNO like ?)) ");
		//sb.append("and investNo in (SELECT investNo FROM CDataInvestment where INVESTMENT_NO like ? and COMP_CHTNAME like ?) ");
		sb.append("AND investNo IN (SELECT investNo FROM ("
			+ "SELECT investNo, investNo INVESTMENT_NO, [name] COMP_CHTNAME FROM CDataException "
			+ "UNION ALL SELECT investNo, INVESTMENT_NO, COMP_CHTNAME FROM CDataInvestment "
			+ ") cdataex WHERE INVESTMENT_NO like ? AND COMP_CHTNAME like ?) ");
		sb.append("and isP=ISNULL(?,isP) and isC=ISNULL(?,isC)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investor);
			stmt.setString(2, IDNO);
			stmt.setString(3, IDNO);
			stmt.setString(4, investNo);
			stmt.setString(5, cName);
			if(com.equals("1")){
				stmt.setString(6, null);
				stmt.setString(7, "1");
			}else if(com.equals("2")){
				stmt.setString(6, "1");
				stmt.setString(7, null);
			}else{
				stmt.setString(6, null);
				stmt.setString(7, null);
			}
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta= rs.getMetaData();
			while(rs.next()){
				Map<String,String> row=new HashMap<String,String>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.put(meta.getColumnName(i),rs.getString(i)==null?"":rs.getString(i));
				}
				result.add(row);
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
