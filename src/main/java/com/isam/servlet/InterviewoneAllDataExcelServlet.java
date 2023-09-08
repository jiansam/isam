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
import com.isam.service.ofi.OFIAllDataService;

public class InterviewoneAllDataExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//	public void init(ServletConfig config) throws ServletException {
//		super.init(config);
//	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		OFIAllDataService allSer=new OFIAllDataService();
		
		String dfilename =DataUtil.getStrUDate().replace("-", "")+"_陸資實地訪查.xlsx";
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		
		List<List<String>> olist = allSer.getAllInterviewoneData();
		Sheet sheet = wb.createSheet("陸資實地訪查");
		for(int i=0;i<olist.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<olist.get(i).size();k++){
				String str = olist.get(i).get(k);
				row.createCell(k).setCellValue(createHelper.createRichTextString(str));
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
