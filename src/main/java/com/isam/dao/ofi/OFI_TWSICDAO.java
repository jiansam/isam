package com.isam.dao.ofi;

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
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dasin.tools.dTools;

import com.isam.bean.OFITWSIC;
import com.isam.bean.UserMember;
import com.isam.helper.SQL;

public class OFI_TWSICDAO {
	public static ArrayList<OFITWSIC> list(){
		ArrayList<OFITWSIC> result = new ArrayList<OFITWSIC>();
		
		SQL sqltool = new SQL();
		try {
			ResultSet rs = sqltool.query("SELECT * FROM OFI_TWSIC ORDER BY code ");
			
			while(rs.next()) {
				OFITWSIC twsic = new OFITWSIC();
				twsic.setCode(rs.getString("code"));
				twsic.setCodename(rs.getString("codename"));
				twsic.setIsSP(dTools.trim(rs.getString("isSP")));
				
				result.add(twsic);
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
	
	public static HashMap<String, OFITWSIC> mapCode(){
		HashMap<String, OFITWSIC> result = new HashMap<String, OFITWSIC>();
		for(OFITWSIC twsic : list()) {
			result.put(twsic.getCode(), twsic);
		}
		
		return result;
	}
	
	public static HashMap<String, String> upload(HttpServletRequest request){
		HashMap<String, String> parameters = new HashMap<String, String>();
		HashMap<String, OFITWSIC> code_map = mapCode();
		SQL sqltool = new SQL();
		
		HttpSession session = request.getSession();
		UserMember user = (UserMember)session.getAttribute("userInfo");
		
		ArrayList<OFITWSIC> insert_list = new ArrayList<OFITWSIC>();
		ArrayList<OFITWSIC> update_list = new ArrayList<OFITWSIC>();
		
		if(ServletFileUpload.isMultipartContent(request)){
			try{
				ServletFileUpload sfu = new ServletFileUpload();
				sfu.setHeaderEncoding("UTF-8");

				FileItemIterator iter = sfu.getItemIterator(request);
				PreparedStatement statement_i = sqltool.prepare("INSERT INTO OFI_TWSIC (code, codename, isSP, parent, level, "
						+ "enable, updatetime, updateuser, createtime, createuser) "
						+ "VALUES (?, ?, ?, ?, ?, '1', GETDATE(), ?, GETDATE(), ?) ");
				
				PreparedStatement statement_u = sqltool.prepare("UPDATE OFI_TWSIC SET "
						+ "codename = ?, isSP = ?, updatetime = GETDATE(), updateuser = ? "
						+ "WHERE code = ? ");

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
						
						for (int i = 1;i <= sheet.getLastRowNum();i++){
							if(sheet.getRow(i) == null || sheet.getRow(i).getCell(0) == null ||
								dTools.isEmpty(sheet.getRow(i).getCell(0).getStringCellValue())){
								continue;
							}
							
							String code = dTools.trim(sheet.getRow(i).getCell(0).getStringCellValue());
							String codename = dTools.trim(sheet.getRow(i).getCell(1).getStringCellValue());
							String isSP = sheet.getRow(i).getCell(2) == null ? "" :
								dTools.trim(formatter.formatCellValue(sheet.getRow(i).getCell(2)));
							if(code_map.get(code) != null) {
								OFITWSIC twsic = code_map.get(code);
								
								if(!twsic.getCodename().equalsIgnoreCase(codename) ||
									!twsic.getIsSP().equalsIgnoreCase(isSP)) {
									statement_u.setString(1, codename);
									statement_u.setString(2, isSP);
									statement_u.setString(3, user.getIdMember());
									statement_u.setString(4, code);
									
									statement_u.executeUpdate();
									
									twsic.setCodename(codename);
									update_list.add(twsic);
								}
							}else {
								OFITWSIC twsic = new OFITWSIC();
								twsic.setCode(code);
								twsic.setCodename(codename);
								
								statement_i.setString(1, code);
								statement_i.setString(2, codename);
								statement_i.setString(3, isSP);
								statement_i.setString(4, twsic.getParent());
								statement_i.setString(5, twsic.getLevel());
								statement_i.setString(6, user.getIdMember());
								statement_i.setString(7, user.getIdMember());
								
								statement_i.executeUpdate();
								
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
}
