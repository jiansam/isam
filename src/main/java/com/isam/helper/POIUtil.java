package com.isam.helper;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;

public class POIUtil {
	
	public Font getFontCaption( Workbook wb) {
		Font fontTitle =wb.createFont();
		fontTitle.setFontHeightInPoints((short) 16);
		fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontTitle.setFontName("新細明體");
		return fontTitle;		
	}
	public Font getFontContext( Workbook wb) {
		Font font =wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("新細明體");
		return font;		
	}
	public CellStyle cellStyleCaption(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(getFontCaption(wb));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setWrapText(false);
		return cellStyle;
	}
	public CellStyle cellStyleTH(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(getFontContext(wb));
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
//		是否自動換行
		cellStyle.setWrapText(false);
		return cellStyle;
	}
	public CellStyle cellStyle(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(getFontContext(wb));
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setWrapText(true);
		return cellStyle;
	}
	public CellStyle cellStyleTD(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(getFontContext(wb));
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
//		是否自動換行
//		cellStyle.setWrapText(true);
		return cellStyle;
	}
	public CellStyle cellStyleSum(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(getFontContext(wb));
		cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderTop((short) 3);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
//		是否自動換行
		cellStyle.setWrapText(true);
		return cellStyle;
	}
//	public static void main(String args[]){
//		
//	}
}
