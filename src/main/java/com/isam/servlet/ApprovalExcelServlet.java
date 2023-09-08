package com.isam.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.isam.helper.DataUtil;
import com.isam.service.MoeaicDataService;

public class ApprovalExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String idno = request.getParameter("idno");
		String investNo = request.getParameter("investNo");
		
		
		MoeaicDataService ser = new MoeaicDataService();
		List<List<String>> list = ser.selectExcelSRC(idno, investNo);
		String dfilename ="核准資料"+DataUtil.getStrUDate()+".xlsx";
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		
		wb.createSheet("核准資料");
		Sheet sheet = wb.getSheetAt(0);
		for(int i=0;i<list.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<list.get(i).size();k++){
				String str = list.get(i).get(k);
				if(k>9&&k<21&&i!=0){
					double x = Double.valueOf(str); 
					row.createCell(k).setCellValue(x);
				}else{
					row.createCell(k).setCellValue(createHelper.createRichTextString(str));
				}
			}
		}
//		response.setContentType("application/vnd.openxmlformats;charset=UTF-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(dfilename, "utf-8") + "\"");
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		wb.write(out);
		out.close();
	}
	
}
