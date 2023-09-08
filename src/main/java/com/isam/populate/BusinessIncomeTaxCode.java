package com.isam.populate;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dasin.tools.dTools;

import com.isam.helper.SQL;

public class BusinessIncomeTaxCode {
	public static void populate(File file) {
		SQL sqltool = new SQL();
		
		try{
			sqltool.execute("TRUNCATE TABLE BusinessIncomeTaxCode ");
			PreparedStatement statement = sqltool.prepare("INSERT INTO BusinessIncomeTaxCode (code, name) VALUES (?, ?) ");
			
			Workbook wb = WorkbookFactory.create(new FileInputStream(file));
			Sheet sheet = wb.getSheet("Q26");
			
			for(int i = 2;i <= sheet.getLastRowNum();i++){
				Row row = sheet.getRow(i);System.out.println(i);
				if(row == null || row.getCell(15) == null) {
					continue;
				}
				
				row.getCell(15).setCellType(Cell.CELL_TYPE_STRING);
				
				String code = dTools.trim(row.getCell(15).getStringCellValue());
				String name = dTools.trim(row.getCell(16).getStringCellValue());
				
				statement.setString(1, code);
				statement.setString(2, name);
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
}
