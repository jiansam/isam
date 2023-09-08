package com.isam.servlet.ofi;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
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
import com.isam.service.InterviewoneHelp;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIManageClassifyService;

public class OFIMScoreExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestOptionService optSer;
	private OFIManageClassifyService mcSer;
	private Map<String,Map<String,String>> optmap;
//	private Map<String,String> ioMsgOpt;
	private Map<String, Map<String, String>> ioOpt;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		mcSer = new OFIManageClassifyService();
		optSer= new OFIInvestOptionService();
		optmap=optSer.select();
		InterviewoneHelp h = new InterviewoneHelp();
		ioOpt=h.getOptionValName();
//		ioMsgOpt=ioOpt.get("errMsg");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		List<List<String>> list = mcSer.getMSReport();
		
		String dfilename ="管理密度報表_"+DataUtil.getStrUDate()+".xlsx";
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		
		wb.createSheet("管理密度");
		Sheet sheet = wb.getSheetAt(0);
		for(int i=0;i<list.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<list.get(i).size();k++){
				String str = list.get(i).get(k);
				if(i>0){
					if(k==3){
						str=optmap.get("isFilled").get(str);
					}else if(k==4){
						str=optmap.get("active").get(str);
					}else if(k==5){
						str=optmap.get("isOperated").get(str);
					}else if(k==10){
						if(!str.isEmpty()){
							str=str.equals("0")?"N（"+ioOpt.get("interviewStatus").get(str)+"）":"Y（"+ioOpt.get("interviewStatus").get(str)+"）";
						}else{
							str="N（非預定訪視企業）";
						}
					}else if(k==11){
						if(!str.isEmpty()){
							str=str.equals("0")?"N（"+ioOpt.get("surveyStatus").get(str)+"）":"Y（"+ioOpt.get("surveyStatus").get(str)+"）";
						}else{
							str="N（非預定訪視企業）";
						}
//					}else if(k>=12&&k<=13){
//						str=str.equals("1")?"Y":"N";
//					}else if(k==15){
//						str =ioMsgOpt.get(str);
					}
					row.createCell(k).setCellValue(createHelper.createRichTextString(str));
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
