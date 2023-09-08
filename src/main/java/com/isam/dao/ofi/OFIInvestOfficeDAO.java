package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestOffice;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dasin.tools.dTools;


import com.isam.bean.UserMember;
import com.isam.helper.SQL;

import Lara.Utility.ToolsUtil;

public class OFIInvestOfficeDAO {
	
	
	public static OFIInvestOffice select(String banno){
		 System.out.println("0");
		SQL sqltool = new SQL();
		OFIInvestOffice bean = new OFIInvestOffice();
		
		try {
			 //System.out.println("1"+":"+banno);
			PreparedStatement stmt = sqltool.prepare(" SELECT * FROM OFI_InvestOffice WHERE Ban_no = ?  ");
			stmt.setString(1, dTools.leadingZero(banno,8));
		//	 System.out.println("2");
			ResultSet rs = stmt.executeQuery();
	//		 System.out.println("3");
			 
			while(rs.next()){
		//		 System.out.println("1");
				bean.setBanNo(rs.getString("Ban_no"));
		//		System.out.println("2");
		//		System.out.println(rs.getString("Ban_no")+" -:- "+bean.getBanNo());
				bean.setCompname(rs.getString("COMP_CHTNAME"));
				bean.setStatus(rs.getString("Status"));
				bean.setLocation(rs.getString("Location"));
				bean.setSetupdate(dTools.isEmpty(rs.getString("setupdate").trim())?"": dTools.leadingZero(rs.getString("setupdate").trim(), 7) );
			
				bean.setSdate(dTools.isEmpty(rs.getString("sdate").trim())?"":dTools.leadingZero(rs.getString("sdate").trim(),7));
				bean.setEdate(dTools.isEmpty(rs.getString("edate").trim())?"":dTools.leadingZero(rs.getString("edate").trim(),7));
				bean.setAgent(rs.getString("agent"));
			
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
	
	public static ArrayList<OFIInvestOffice> list(){
		ArrayList<OFIInvestOffice> result = new ArrayList<OFIInvestOffice>();
		
		SQL sqltool = new SQL();
		try {
			ResultSet rs = sqltool.query("SELECT * FROM OFI_InvestOffice ORDER BY ban_no ");
			
			while(rs.next()) {
				
				result.add(select(rs.getString("Ban_No")));
			}
			
			rs.close();
		}catch (Exception e) {
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
	private static String statusMapping(String status) {
		String result = "";
		switch(status) {
		case "營業中":
			result = "核准設立";
			break;
		case "停業中":
			result = "停業日期";
			break;
		case "解散或撤銷":
			result = "";
			break;
		}
		return result;
	}
	public static List<Map<String,String>> select(Map<String,String> terms){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		String banno= DataUtil.nulltoempty( terms.get("banno"));
		String company=DataUtil.nulltoempty(terms.get("company"));
		String status=DataUtil.nulltoempty(terms.get("status"));
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * from OFI_InvestOffice where 1=1 ");
		
		if(!banno.isEmpty()) {
			sb.append("and ban_no = '").append(banno).append("' ");
		}
		if(!company.isEmpty()) {
			sb.append("and COMP_CHTNAME like '").append( company).append("' ");
		}
		if(!status.isEmpty()) {
			if(status.equals("1")) {
			sb.append("and Status = '核准設立' and ( edate = '0000000' or edate = '' ) ");
			}
			else if(status.equals("2")) {
				sb.append("and ( edate <> '0000000' and edate <> '' )  and  ( Status not like '%廢止%' and  Status not like '%撤回%'  and Status not like '%清算完結%' and Status  not like '%解散%' and  Status not like '%撤銷%' and Status  not like '%註銷%' )  ");
			}
			else if(status.equals("3")) {
				sb.append(" and ( Status like '%廢止%' or Status like '%撤回%'  or Status like '%清算完結%' or Status like '%解散%' or Status like '%撤銷%' or Status like '%註銷%' )");
			}
		}
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
		
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),rs.getString(i));
				}
				result.add(sub);
			}
			rs.close();
			stmt.close();
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
	
	public static HashMap<String, OFIInvestOffice> mapCode(){
		HashMap<String, OFIInvestOffice> result = new HashMap<String, OFIInvestOffice>();
		for(OFIInvestOffice twsic : list()) {
			result.put(twsic.getBanNo(), twsic);
		}
		
		return result;
	}
	static int Row_Chtname_Index  =0;
	static int Row_Banno_Index = 0 ;
	static int Row_Status_Index = 0 ;
	static int Row_Location_Index = 0 ;
	static int Row_Setupdate_Index = 0 ;
	static int Row_sdate_Index = 0 ;
	static int Row_edate_Index = 0 ;
	static int Row_agent_Index = 0 ;
	public static HashMap<String, String> upload(HttpServletRequest request){
		HashMap<String, String> parameters = new HashMap<String, String>();
		HashMap<String, OFIInvestOffice> code_map = mapCode();
		SQL sqltool = new SQL();
		
		HttpSession session = request.getSession();
		UserMember user = (UserMember)session.getAttribute("userInfo");
		
		ArrayList<OFIInvestOffice> insert_list = new ArrayList<OFIInvestOffice>();
		ArrayList<OFIInvestOffice> update_list = new ArrayList<OFIInvestOffice>();
		
		if(ServletFileUpload.isMultipartContent(request)){
	
			try{
				ServletFileUpload sfu = new ServletFileUpload();
				sfu.setHeaderEncoding("UTF-8");

				FileItemIterator iter = sfu.getItemIterator(request);
				PreparedStatement statement_i = sqltool.prepare("INSERT INTO OFI_InvestOffice (ban_no, comp_chtname, status, location, setupdate, "
						+ "sdate, edate, agent) "
						+ "VALUES (?, ?, ?, ?, ?, ?,  ?,  ?) ");
				
				PreparedStatement statement_u = sqltool.prepare("UPDATE OFI_InvestOffice SET "
						+ "comp_chtname = ?, status = ?, location = ?, setupdate= ?, sdate=?, edate=?, agent=? "
						+ "WHERE ban_no = ? ");

				while(iter.hasNext()){
					FileItemStream fis = iter.next();
					InputStream is = fis.openStream();

					// Note: normal form fields MUST be placed before file input.
					if(fis.isFormField()){
						parameters.put(fis.getFieldName(), Streams.asString(is, "utf-8"));
					}else{
						Workbook wb = WorkbookFactory.create(is);
						Sheet sheet = wb.getSheetAt(0);
						DataFormatter formatter = new DataFormatter();
					
						Row currentRow = sheet.getRow(0);
						currentRow.forEach(a->{
					
							switch(dTools.trim(a.getStringCellValue())) {
							case "公司名稱":
								Row_Chtname_Index = a.getColumnIndex();
								break;
							case "統一編號":
								Row_Banno_Index = a.getColumnIndex();
								break;
							case "公司狀況":
								Row_Status_Index = a.getColumnIndex();
								break;
							case "辦事處所在地":
								Row_Location_Index = a.getColumnIndex();
								break;
							case "核准登記日期":
								Row_Setupdate_Index = a.getColumnIndex();
								break;
							case "停業日期(起)":
								Row_sdate_Index = a.getColumnIndex();
								break;
							case "停業日期(迄)":
								Row_edate_Index = a.getColumnIndex();
								break;
							case "訴訟及非訴訟代理人":
								Row_agent_Index = a.getColumnIndex();
								break;
							}
							
						});
						for (int i = 1;i <= sheet.getLastRowNum();i++){
							Cell tempCell = sheet.getRow(i).getCell(Row_Banno_Index);
							tempCell.setCellType(Cell.CELL_TYPE_STRING);
							if(sheet.getRow(i) == null || tempCell == null ||
								dTools.isEmpty(tempCell.getStringCellValue())){
							
								continue;
							}
							tempCell = sheet.getRow(i).getCell(Row_Banno_Index);
							if(tempCell!=null) {
								tempCell.setCellType(Cell.CELL_TYPE_STRING);
							}
							String banno = dTools.leadingZero(dTools.trim(DataUtil.nulltoempty(tempCell!=null?tempCell.getStringCellValue():"")), 8);
							
							tempCell = sheet.getRow(i).getCell(Row_Chtname_Index);
							if(tempCell!=null) {
								tempCell.setCellType(Cell.CELL_TYPE_STRING);
							}
							String chtname = dTools.trim(tempCell!=null?tempCell.getStringCellValue():"");
							
							tempCell = sheet.getRow(i).getCell(Row_Status_Index);
							if(tempCell!=null) {
								tempCell.setCellType(Cell.CELL_TYPE_STRING);
							}
							String status = dTools.trim(tempCell!=null?tempCell.getStringCellValue():"");
							
							tempCell = sheet.getRow(i).getCell(Row_Location_Index);
							if(tempCell!=null) {
								tempCell.setCellType(Cell.CELL_TYPE_STRING);
							}
							String location = dTools.trim(tempCell!=null?tempCell.getStringCellValue():"");
							
							tempCell = sheet.getRow(i).getCell(Row_Setupdate_Index);
							tempCell.setCellType(Cell.CELL_TYPE_STRING);
							String setupdate =dTools.leadingZero( dTools.trim(DataUtil.nulltoempty(tempCell!=null?tempCell.getStringCellValue():"")),7);
							
							tempCell = sheet.getRow(i).getCell(Row_sdate_Index);
							if(tempCell!=null) {
								tempCell.setCellType(Cell.CELL_TYPE_STRING);
							}
							
							String sdate =dTools.leadingZero( dTools.trim(DataUtil.nulltoempty( tempCell!=null?tempCell.getStringCellValue():"")),7);
							
							tempCell = sheet.getRow(i).getCell(Row_edate_Index);
							if(tempCell!=null) { 
								tempCell.setCellType(Cell.CELL_TYPE_STRING);
							}
							String edate = dTools.leadingZero(dTools.trim(DataUtil.nulltoempty(tempCell!=null?tempCell.getStringCellValue():"")),7);
							
							tempCell = sheet.getRow(i).getCell(Row_agent_Index);
							if(tempCell!=null) {
								tempCell.setCellType(Cell.CELL_TYPE_STRING);
							}
							String agent = dTools.trim(tempCell!=null?tempCell.getStringCellValue():"");
							
							if(code_map.get(banno)!=null) {
							//	System.out.println("update:"+banno);
								OFIInvestOffice twsic = code_map.get(banno);
								
							
									statement_u.setString(1, chtname);
									statement_u.setString(2, status);
									statement_u.setString(3, location);
									statement_u.setString(4, setupdate.trim().replaceAll("0000000", "").replaceAll("          ", ""));
									statement_u.setString(5, sdate.trim().replaceAll("0000000", "").replaceAll("          ", ""));
									statement_u.setString(6, edate.trim().replaceAll("0000000", "").replaceAll("          ", ""));
									statement_u.setString(7, agent);
									statement_u.setString(8, banno);
									statement_u.executeUpdate();
									
									twsic.setBanNo(banno);
									twsic.setCompname(chtname);
								
									update_list.add(twsic);
							
							}else {
							//	System.out.println("insert:"+banno);
								OFIInvestOffice twsic = new OFIInvestOffice();
								
								
								
								statement_i.setString(1, banno);
								statement_i.setString(2, chtname);
								statement_i.setString(3, status);
								statement_i.setString(4, location);
								statement_i.setString(5, setupdate.trim().replaceAll("0000000", "").replaceAll("          ", ""));
								statement_i.setString(6, sdate.trim().replaceAll("0000000", "").replaceAll("          ", ""));
								statement_i.setString(7, edate.trim().replaceAll("0000000", "").replaceAll("          ", ""));
								statement_i.setString(8, agent);
								statement_i.executeUpdate();
								
								twsic.setBanNo(banno);
								twsic.setCompname(chtname);
								
								insert_list.add(twsic);
							}
						}
					}

					is.close();
				}
				
				statement_i.close();
				statement_u.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					sqltool.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			request.setAttribute("insert_list", insert_list);
			request.setAttribute("update_list", update_list);
		}

		return parameters;
	}
	
	public int update(OFIInvestOffice bean) {
		int result = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE OFI_InvestOffice SET COMP_CHTNAME=?,Status=?,Location=?,setupdate=?,");
		sb.append("sdate=?,edate=?,agent=? ");
		sb.append("where ban_no=?");
		String forStmt = sb.toString();
//	
		System.out.println(forStmt);
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, bean.getCompname());
			pstmt.setString(2, bean.getStatus());
			pstmt.setString(3, bean.getLocation());
			pstmt.setString(4, bean.getSetupdate());
			pstmt.setString(5, bean.getSdate());
			pstmt.setString(6, bean.getEdate());
			pstmt.setString(7, bean.getAgent());
		
			pstmt.setString(8, bean.getBanNo());
			result=pstmt.executeUpdate();
			pstmt.close();
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
