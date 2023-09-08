package com.isam.servlet.ofi;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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
import com.isam.service.ofi.OFIAuditOptionService;

public class OFIAllDataExcelServlet extends HttpServlet {
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
		OFIAuditOptionService auditSer=new OFIAuditOptionService();
		OFIAllDataService allSer=new OFIAllDataService();
		Map<String, String> auditOpt=auditSer.getAuditOptionMap();
		

		
		String dfilename =DataUtil.getStrUDate().replace("-", "")+"_陸資已核准資料.xlsx";
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		
		List<List<String>> list = allSer.getAllInvestmentData(auditOpt);
		Sheet sheet = wb.createSheet("國內事業基本資料");
		for(int i=0;i<list.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<list.get(i).size();k++){
				String str = list.get(i).get(k);
					row.createCell(k).setCellValue(createHelper.createRichTextString(str));
			}
		}
		
		List<List<String>> rlist = allSer.getAllReinvestmentData();
		sheet = wb.createSheet("國內轉投資資料");
		for(int i=0;i<rlist.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<rlist.get(i).size();k++){
				String str = rlist.get(i).get(k);
				row.createCell(k).setCellValue(createHelper.createRichTextString(str));
			}
		}
		List<List<String>> ilist = allSer.getAllInvestorData();
		sheet = wb.createSheet("投資人");
		for(int i=0;i<ilist.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<ilist.get(i).size();k++){
				String str = ilist.get(i).get(k);
				row.createCell(k).setCellValue(createHelper.createRichTextString(str));
			}
		}
		List<List<String>> olist = allSer.getAllInterviewoneData();
		sheet = wb.createSheet("陸資實地訪查");
		for(int i=0;i<olist.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<olist.get(i).size();k++){
				String str = olist.get(i).get(k);
				row.createCell(k).setCellValue(createHelper.createRichTextString(str));
			}
		}
		
		List<List<String>> aglist = allSer.getAllAgentXReceiveNoData();
		sheet = wb.createSheet("代理人資料");
		for(int i=0;i<aglist.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<aglist.get(i).size();k++){
				String str = aglist.get(i).get(k);
				row.createCell(k).setCellValue(createHelper.createRichTextString(str));
			}
		}
		
		List<List<String>> alist = allSer.getAllOfficeData();
		sheet = wb.createSheet("辦事處資料");
		for(int i=0;i<alist.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<alist.get(i).size();k++){
				String str = alist.get(i).get(k);
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
