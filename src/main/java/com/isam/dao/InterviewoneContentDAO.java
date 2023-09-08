package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.InterviewoneContent;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class InterviewoneContentDAO {
	
	public List<String> getInterviewErrorList(){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT qNo FROM OFI_InterviewErrorList('')";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString("qNo"));
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
	public List<String> getInterviewErrorList(String qNo,String investNo){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ( select qNo from ( SELECT qNo,optionId,value FROM InterviewoneContent where ");
		sb.append("qNo=ISNULL(?,qNo) and optionId in (SELECT optionId FROM InterviewoneXGroup ");
		sb.append("where groupNo='1' and enable='1' and optionValue is not null))a,");
		sb.append("(SELECT optionId,optionValue FROM InterviewoneXGroup where groupNo='1' ");
		sb.append("and enable='1' and optionValue is not null and groupNo='1')b ");
		sb.append("where a.value=b.optionValue and a.optionId=b.optionId group by qNo union ");
		sb.append("SELECT qNo FROM InterviewoneContent where qNo=ISNULL(?,qNo) and optionId in ");
		sb.append("(SELECT optionId FROM InterviewoneXGroup where groupNo='1' and enable='1' and optionValue is null)");
		sb.append(") i where i.qNo in (SELECT qNo FROM Interviewone where investNo=isnull(?,investNo) and enable='1') ");
		sb.append(" and qNo not in (SELECT [qNo] FROM InterviewoneContent where optionId='78' and value='3' )");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, qNo);
			stmt.setString(2, qNo);
			stmt.setString(3, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString("qNo"));
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
	
	public ArrayList<InterviewoneContent> getInterviewError(String qNo)
	{
		ArrayList<InterviewoneContent> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare(
					"SELECT f.*, B.cName AS reasonName, C.cname AS reason FROM  "
				  + "( "
				  + "	select qNo,a.optionId,value,seq from "
				  + "	( "
				  + "		SELECT qNo,optionId,value,seq "
				  + "		FROM InterviewoneContent "
				  + "		where qNo=ISNULL(?,qNo) "
				  + "		and optionId in "
				  + "		( "
				  + "			SELECT optionId " //去除異常彙總，取出訪視異常項目id，供該公司比對取出紀錄中該id的value
				  + "			FROM InterviewoneXGroup "
				  + "			where groupNo='1' "
				  + "			and enable='1' "
				  + "			and optionValue is not null "
				  + "		) "
				  + "	)a, "
				  + "	("
				  + "		SELECT optionId,optionValue " //去除異常彙總，取出預設 每個id ＆ 異常value
				  + "		FROM InterviewoneXGroup "
				  + "		where groupNo='1' "
				  + "		and enable='1' "
				  + "		and optionValue is not null "
				  + "		and groupNo='1' "
				  + "	)b"
				  + "	where a.value=b.optionValue " //如果記錄中各id填的value與異常表相同，秀出
				  + "	and a.optionId=b.optionId "
				  
				  + "	union " //將兩個SQL 語句的結果合併起來
				  
				  + "	SELECT qNo,optionId,value,seq "
				  + "	FROM InterviewoneContent "
				  + "	where qNo=ISNULL(?,qNo) "
				  + "	and optionId in "
				  + "	( "
				  + "		SELECT optionId " //取出異常彙總id
				  + "		FROM InterviewoneXGroup "
				  + "		where groupNo='1' "
				  + "		and enable='1' "
				  + "		and optionValue is null "
				  + "	)"
				  + ")f "
				  + "left outer join InterviewoneItem B ON f.optionId = B.optionId " 
				  + "left outer join InterviewoneOption C ON B.paramName = C.paramName and f.value = C.optionValue "
				  
				  /* 106-07-21：佳慧要求，若遇到未訪視，要加上未訪視說明，在不把[未訪視說明 optionId=77]加入InterviewoneXGroup群組之下，
				   * 改用 union方式，把未訪視說明加入，因為若有未訪視78，就一定會有77或者空白。
				   * 但因為 
				   * 未訪視說明 optionId=77
				   * 未訪視原因 optionId=78 
				   * 若以optionId 排序，會先出說明才出原因，顯示會顛倒，所以多加seq欄位，將77的sqe*99 變成大的，這樣排序就會變成 77在第二個*/
				  
				  + "union "
				  + "SELECT D.qNo, D.optionId, D.value, case D.seq when 1 then D.seq * 99 end 'seq', "
				  + "		B.cName AS reasonName, C.cname AS reason "
				  + "FROM InterviewoneContent D "
				  + "left outer join InterviewoneItem B ON D.optionId = B.optionId "
				  + "left outer join InterviewoneOption C ON B.paramName = C.paramName and D.value = C.optionValue "
				  + "where qNo=ISNULL(?,qNo) "
				  + "and D.optionId = 77 "
				  + "and (D.value IS NOT NULL AND LEN(D.value)>0) "
				  + "ORDER BY seq, f.optionId ");
			int column = 1;
			pstmt.setString(column++, qNo);
			pstmt.setString(column++, qNo);
			pstmt.setString(column++, qNo);
			rs = pstmt.executeQuery();
			result = new ArrayList<>();
			while (rs.next()){
				InterviewoneContent bean = new InterviewoneContent();
				String optionId = rs.getString("optionId");
				if("22".equals(optionId)){
					bean.setOptionName(rs.getString("reasonName"));
					bean.setValue(rs.getString("value"));
				}else if("77".equals(optionId)){
						bean.setOptionName(rs.getString("reasonName"));
						bean.setValue(rs.getString("value"));				
				}else{
					bean.setOptionName(rs.getString("reasonName"));
					bean.setValue(rs.getString("reason"));
				}
				result.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltools.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public String getFinancialErrorYear(String investNo){
		String result = "";
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select case when ic.qNo is null then '' else i.year end year from (");
		sb.append("select * from Interviewone where investNo=? and enable='1' and (interviewStatus='1' or interviewStatus='9') ");
		sb.append("and year=(SELECT Max(year) FROM Interviewone where investNo=? and enable='1') )i left join ( ");
		sb.append("SELECT a.qNo FROM (select qNo,optionId,value from InterviewoneContent where optionId in(");
		sb.append("SELECT optionId FROM InterviewoneXGroup where enable='1' and groupNo='2') and qNo not in (");
		sb.append("select qNo from InterviewoneContent	where optionId='8' and value='2')) a,");
		sb.append("(SELECT optionId,optionValue FROM InterviewoneXGroup where enable='1' and groupNo='2')b ");
		sb.append("where a.optionId=b.optionId and a.value=b.optionValue group by qNo) ic on i.qNo=ic.qNo");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=DataUtil.nulltoempty(rs.getString("year"));
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
/*	public List<String> getFinancialErrorList(String investNo){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT a.qNo FROM (select qNo,optionId,value from InterviewoneContent ");
		sb.append("where optionId in(SELECT optionId FROM InterviewoneXGroup ");
		sb.append("where enable='1' and groupNo='2') and qNo not in (");
		sb.append("select qNo from InterviewoneContent	where optionId='8' and value='2') ");
		sb.append(") a,(SELECT optionId,optionValue ");
		sb.append("FROM InterviewoneXGroup where enable='1' and groupNo='2')b ");
		sb.append("where a.optionId=b.optionId and a.value=b.optionValue group by qNo");
		sb.append(" ) f where f.qNo in (SELECT qNo FROM Interviewone where investNo=? and enable='1' ");
		sb.append("and year=(SELECT Max(year) FROM Interviewone where investNo=? and enable='1') )");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString("qNo"));
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
*/	public String getInterviewErrorYear(String investNo){
		String result = "";
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select case when ic.qNo is null then '' else i.year end year from (");
		sb.append("select * from Interviewone where investNo=? and enable='1' and (interviewStatus='1' or interviewStatus='9') ");
		sb.append("and year=(SELECT Max(year) FROM Interviewone where investNo=? and enable='1') )i left join ( ");
		sb.append("select qNo from ( SELECT qNo,optionId,value FROM InterviewoneContent ");
		sb.append("where optionId in (SELECT optionId FROM InterviewoneXGroup where groupNo='1' and enable='1' ");
		sb.append("and optionValue is not null))a,(SELECT optionId,optionValue FROM InterviewoneXGroup ");
		sb.append("where groupNo='1' and enable='1' and optionValue is not null and groupNo='1')b ");
		sb.append("where a.value=b.optionValue and a.optionId=b.optionId group by qNo union ");
		sb.append("SELECT qNo FROM InterviewoneContent where optionId in (SELECT optionId FROM InterviewoneXGroup ");
		sb.append("where groupNo='1' and enable='1' and optionValue is null)) ic on i.qNo=ic.qNo");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=DataUtil.nulltoempty(rs.getString("year"));
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
		public String getErrorExceptXYear(String investNo){
			String result = "";
			SQL sqltool = new SQL();
			String forStmt = "SELECT year from Interviewone where interviewStatus='9' and enable='1' and qNo in (SELECT qNo FROM InterviewoneContent where optionId='78' and value='3') and investNo=?";
			try {
				PreparedStatement stmt = sqltool.prepare(forStmt);
				stmt.setString(1, investNo);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
					result=rs.getString(1);
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
//	public List<String> getInterviewErrorList(String investNo){
//		List<String> result = new ArrayList<String>();
//		SQL sqltool = new SQL();
//		StringBuilder sb = new StringBuilder();
//		sb.append("select * from ( select qNo from ( SELECT qNo,optionId,value FROM InterviewoneContent where ");
//		sb.append("optionId in (SELECT optionId FROM InterviewoneXGroup ");
//		sb.append("where groupNo='1' and enable='1' and optionValue is not null))a,");
//		sb.append("(SELECT optionId,optionValue FROM InterviewoneXGroup where groupNo='1' ");
//		sb.append("and enable='1' and optionValue is not null and groupNo='1')b ");
//		sb.append("where a.value=b.optionValue and a.optionId=b.optionId group by qNo union ");
//		sb.append("SELECT qNo FROM InterviewoneContent where optionId in ");
//		sb.append("(SELECT optionId FROM InterviewoneXGroup where groupNo='1' and enable='1' and optionValue is null)");
//		sb.append(") i where i.qNo in (SELECT qNo FROM Interviewone where investNo=? and enable='1' ");
//		sb.append("and year=(SELECT Max(year) FROM Interviewone where investNo=? and enable='1')) ");
//		String forStmt = sb.toString();
////		System.out.println(forStmt);
//		sb.setLength(0);
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, investNo);
//			stmt.setString(2, investNo);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				result.add(rs.getString("qNo"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try{
//				sqltool.close();
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
		public List<String> getFinancialErrorList(){
			List<String> result = new ArrayList<String>();
			SQL sqltool = new SQL();
			String forStmt = "SELECT qNo FROM OFI_FinancialErrorList ('')";
			try {
				PreparedStatement stmt = sqltool.prepare(forStmt);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
					result.add(rs.getString("qNo"));
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
	public List<String> getFinancialErrorList(String qNo,String investNo){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT a.qNo FROM (select qNo,optionId,value from InterviewoneContent ");
		sb.append("where optionId in(SELECT optionId FROM InterviewoneXGroup ");
		sb.append("where enable='1' and groupNo='2') and qNo not in (");
		sb.append("select qNo from InterviewoneContent	where optionId='8' and value='2') ");
		sb.append("and qNo = ISNULL(?,qNo)) a,(SELECT optionId,optionValue ");
		sb.append("FROM InterviewoneXGroup where enable='1' and groupNo='2')b ");
		sb.append("where a.optionId=b.optionId and a.value=b.optionValue group by qNo");
		sb.append(" ) f where f.qNo in (SELECT qNo FROM Interviewone where investNo=isnull(?,investNo) and enable='1')");
		sb.append(" and f.qNo not in (SELECT qNo FROM InterviewoneContent where optionId='78' and value='3' )");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, qNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString("qNo"));
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
	
	public ArrayList<InterviewoneContent> getFinancialError(String qNo)
	{
		ArrayList<InterviewoneContent> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare(
					"select A.qNo, A.optionId, A.value, B.cName AS reasonName, C.cname AS reason "
				  + "from InterviewoneContent A "
				  + "left outer join InterviewoneItem B ON A.optionId = B.optionId "
				  + "left outer join InterviewoneOption C ON B.paramName = C.paramName and A.value = C.optionValue "
				  + "where A.optionId in "
				  + "( "
				  + "	SELECT optionId FROM InterviewoneXGroup " //找出第二群組：財務異常，並仍生效的
				  + "	where enable='1' and groupNo='2' "
				  + ") "
				  + "and A.qNo not in "
				  + "( "
				  + "	select qNo from InterviewoneContent " //找出營運狀況（id=8）選到新設立（2）的
				  + "	where optionId='8' and value='2' "
				  + ") "
				  + "and A.value = " //問卷所填的值 ＝ 財務異常群組預設設定值（該答案代表異常）
				  + "( "
				  + "	select optionValue FROM InterviewoneXGroup " 
				  + "	where enable='1' and groupNo='2' "
				  + " 	and A.optionId = optionId "
				  + ") "
				  + "and A.qNo = ISNULL(?,qNo)");
			pstmt.setString(1, qNo);
			rs = pstmt.executeQuery();
			result = new ArrayList<>();
			while (rs.next()){
				InterviewoneContent bean = new InterviewoneContent();
				bean.setOptionName(rs.getString("reasonName"));
				bean.setValue(rs.getString("reason")+"（非新設立）");
				result.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltools.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public Map<String,String> select(int qNo,String qType){
		Map<String,String> result = new HashMap<String, String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select t1.pname,(STUFF((select ','+value from (SELECT case when isText='1' ");
		sb.append("then b.paramName+'_'+convert(nvarchar,seq) else b.paramName end pname,value ");
		sb.append("FROM InterviewoneContent a,InterviewoneItem b where b.optionId=a.optionId and a.qNo=? and b.qType=?)t2 ");
		sb.append("WHERE  T2.pname = T1.pname for XML path('')),1,1,'')) as value from ( ");
		sb.append("SELECT case when isText='1' then b.paramName+'_'+convert(nvarchar,seq) else b.paramName end pname,value ");
		sb.append("FROM InterviewoneContent a,InterviewoneItem b where b.optionId=a.optionId and a.qNo=? and b.qType=? )t1 ");
		sb.append("group by pname");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, qNo);
			stmt.setString(2, qType);
			stmt.setInt(3, qNo);
			stmt.setString(4, qType);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString(1), rs.getString(2));
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
	public Map<String,String> getLastYearFinancial(int qNo,String year){
		Map<String,String> result = new HashMap<String, String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select paramName,convert(float,replace(value,',','')) from (");
		sb.append("SELECT optionId,value,rank() OVER (order by seq asc) seq FROM InterviewoneContent ");
		sb.append("where qNo = (SELECT qNo FROM Interviewone where enable ='1' and year=? and investNo=(");
		sb.append("select investNo FROM Interviewone where qNo=? )) and optionId in (");
		sb.append("SELECT optionId FROM InterviewoneXGroup where groupNo='9' and enable='1')");
		sb.append(")a,InterviewoneItem b where a.seq='1' and a.optionId=b.optionId");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			stmt.setInt(2, qNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString(1), rs.getString(2));
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
	
	public boolean isExist(int qNo,String qType){
		boolean result = false;
		SQL sqltool = new SQL();
		String forStmt = "SELECT COUNT(*) FROM InterviewoneContent where qNo=? and optionId in (SELECT optionId FROM InterviewoneItem where qType=? and enable='1')";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, qNo);
			stmt.setString(2, qType);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				if(rs.getInt(1)!=0){
					result = true;
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
	
	public void insert(List<InterviewoneContent> beans) {
		String forpstmt = "insert into InterviewoneContent values(?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				InterviewoneContent bean = beans.get(i);
				pstmt.setInt(1, bean.getqNo());
				pstmt.setInt(2, bean.getOptionId());
				pstmt.setString(3, bean.getValue());
				pstmt.setInt(4, bean.getSeq());
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
	
	public int delete(int qNo,String qType) {
		int result = 0;
		String forpstmt = "delete from InterviewoneContent WHERE qNo=? and optionId in (SELECT optionId FROM InterviewoneItem where qType=isnull(?,qType) and enable='1')";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setInt(1, qNo);
			pstmt.setString(2,qType);
			result=pstmt.executeUpdate();			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
