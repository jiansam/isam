package com.isam.populate;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dasin.tools.dTools;

import com.isam.helper.SQL;

public class OFI_TWSIC {
	public static void populate(File file) {
		SQL sqltool = new SQL();
		ArrayList<String> code_list = list_code();
		
		try{
			PreparedStatement statement = sqltool.prepare(
				"INSERT INTO OFI_TWSIC (code, codename, isSP, parent, level, enable, updatetime, updateuser, createtime, createuser) "
				+ "VALUES (?, ?, ?, ?, ?, 1, GETDATE(), 'admin', GETDATE(), 'admin') "
			);
			
			Workbook wb = WorkbookFactory.create(new FileInputStream(file));
			Sheet sheet = wb.getSheetAt(0);
			
			for(int i = 1;i <= sheet.getLastRowNum();i++){
				Row row = sheet.getRow(i);System.out.println(i);
				
				String code = dTools.trim(row.getCell(0).getStringCellValue());
				if(code_list.contains(code)) {
					System.out.println(code + " exists!");
					continue;
				}else {
					if(code.length() != 4 && code.length() != 7) {
						System.out.println(code + "'s length is wrong!");
						continue;
					}
					
					code_list.add(code);
				}
				
				int count = 1;
				statement.setString(count++, code);
				statement.setString(count++, dTools.trim(row.getCell(1).getStringCellValue()));
				statement.setString(count++, row.getCell(2) == null ? "" : 
						row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC ? 
						String.valueOf((int)row.getCell(2).getNumericCellValue())
						: dTools.trim(row.getCell(2).getStringCellValue()));
				statement.setString(count++, row.getCell(3) == null ? "" : 
						dTools.trim(row.getCell(3).getStringCellValue()));
				statement.setInt(count++, code.length() == 4 ? 1 : 2);
				statement.addBatch();
			}
			
			statement.executeBatch();
			statement.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	static ArrayList<String> list_code(){
		ArrayList<String> result = new ArrayList<String>();
		
		SQL sqltool = new SQL();
		
		try{
			ResultSet rs = sqltool.query("SELECT * FROM OFI_TWSIC ");
			while(rs.next()) {
				result.add(rs.getString("code"));
			}
			
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
