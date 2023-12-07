package com.isam.servlet.interviewcier;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.isam.bean.InterviewCier;
import com.isam.dao.InterviewCierDAO;
import com.isam.helper.DataUtil;

public class DownloadInterviewListServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		Workbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet("訪查紀錄總表");
		
		int rownumber = 0;
		int colnumber = 0;

		Row header = sheet.createRow(rownumber++);
		header.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue("年度");
		header.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue("訪查企業名稱");
		header.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue("企業類別");
		header.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue("修改時間");
		
		Calendar cal = Calendar.getInstance();
		for(InterviewCier interviewbrief : InterviewCierDAO.select()){
			cal.setTime(interviewbrief.getUpdatetime());
			String type = "";
			 
			if ("1".equals(interviewbrief.getType1())) type += "僑外資在臺事業、";
		    if ("1".equals(interviewbrief.getType2())) type += "台元科技園區、";
			if ("1".equals(interviewbrief.getType3())) type += "陸資在臺辦事處、";
			
			type =  StringUtils.removeEnd(type, "、");
		
			colnumber = 0;
			Row r = sheet.createRow(rownumber++);
			
			r.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue(interviewbrief.getYear());
			r.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue(
				DataUtil.trim(interviewbrief.getCompany()).length() == 0 ? "(未輸入名稱)" : interviewbrief.getCompany());
			r.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue(type);
			
			r.createCell(colnumber++, Cell.CELL_TYPE_STRING).setCellValue(
					(cal.get(Calendar.YEAR) - 1911) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +  cal.get(Calendar.DAY_OF_MONTH));
		}
		
		for(int i = 0;i < 4;i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i)*2, 25600));
		}
		
		response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=interviewCierList_" + DataUtil.getStrUDate() +".xlsx");

		ServletOutputStream out = response.getOutputStream();
		wb.write(out);

		out.flush();
		out.close();
	}
}
