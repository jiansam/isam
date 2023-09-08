package com.isam.servlet.ofi;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.isam.bean.OFITWSIC;
import com.isam.dao.ofi.OFI_TWSICDAO;
import com.isam.helper.DataUtil;

public class OFIDownloadTWSICServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Workbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet("營業項目清單");
		
		int rownumber = 0;
		int colnumber = 0;

		Row header = sheet.createRow(rownumber++);
		header.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue("代碼");
		header.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue("說明");
		header.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue("isSP");
		
		for(OFITWSIC twsic : OFI_TWSICDAO.list()) {
			colnumber = 0;
			Row r = sheet.createRow(rownumber++);
			
			r.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue(twsic.getCode());
			r.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue(twsic.getCodename());
			r.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue(twsic.getIsSP());
		}
		
		sheet.autoSizeColumn(1);
		sheet.setColumnWidth(1, Math.max(sheet.getColumnWidth(1)*2, 25600));
		
		createNoteSheet(wb);
		
		response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=twsic_list_" + DataUtil.getStrUDate() +".xlsx");

		ServletOutputStream out = response.getOutputStream();
		wb.write(out);

		out.flush();
		out.close();
	}
	
	private void createNoteSheet(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom((short)1);		
		style.setBorderTop((short)1);
		style.setBorderLeft((short)1);
		style.setBorderRight((short)1);
		 
		Sheet sheet = wb.createSheet("說明");
		
		int rownumber = 0;
		Row row = sheet.createRow(rownumber++);
		row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("1. isSP填寫說明：");
		
		row = sheet.createRow(rownumber++);
		row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("代碼");
		row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("說明");
		row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("範例");
		
		row = sheet.createRow(rownumber++);
		row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("1");
		row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("代碼最後為1");
		row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("A101011");
		
		row = sheet.createRow(rownumber++);
		row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("2");
		row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("代碼開頭為I3");
		row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("I301010");
		
		row = sheet.createRow(rownumber++);
		row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("3");
		row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("只F109070、F209060");
		row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("");
		
		row = sheet.createRow(rownumber++);
		row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("2. 新增7碼營業代碼後，需確認是否已有4碼資料，若沒有請一併新增。");
		
		row = sheet.createRow(rownumber++);
		row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("3. 調整「代碼」欄資料，但說明不變，則視為新增資料。");
		
		row = sheet.createRow(rownumber++);
		row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("4. 調整「說明」欄資料，但代碼不變，則視為修改資料。");
		
		row = sheet.createRow(rownumber++);
		row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("5. 不可刪除資料。");
		
		for(int i = 1;i < 5;i++) {
			row = sheet.getRow(i);
			
			for(int j = 1;j < 4;j++) {
				row.getCell(j).setCellStyle(style);
			}
		}
		
		sheet.setColumnWidth(2, 5120);
	}
}
