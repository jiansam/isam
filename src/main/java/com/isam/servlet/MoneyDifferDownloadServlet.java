package com.isam.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectReportService;

public class MoneyDifferDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, String> IDNOToName;
	private ProjectReportService prSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName=help.getIDNOToName();
		prSer = new ProjectReportService();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		String year = request.getParameter("year");
		String quarter = request.getParameter("quarter");
		String type = request.getParameter("type");
		
		StringBuffer sb= new StringBuffer();
		List<Map<String,String>> temp=null;
		List<List<String>> list=new ArrayList<List<String>>();
		String projDate =DataUtil.addZeroForNum(year,3);
		String season ="";
		sb.append("投資金額差異累計至");
		if(quarter.equals("1")){
			projDate+="0331";
			season ="第一季";
		}else if(quarter.equals("2")){
			season ="第二季";
			projDate+="0630";
		}else if(quarter.equals("3")){
			season ="第三季";
			projDate=projDate+"0930";
		}else{
			season ="年報";
			projDate=projDate+"1231";
		}
		sb.append(projDate);
			temp=prSer.getDifferList(year, quarter, null,projDate);
			int count=0;
			for (int i = 0; i < temp.size(); i++) {
				Map<String,String> map=temp.get(i);
				List<String> sublist=new ArrayList<String>();
				if(i==0){
					List<String> title=new ArrayList<String>();
					title.add("序號");
					title.add("年度");
					title.add("季度");
					title.add("投資人");
					title.add("統編");
					title.add("大陸事業名稱");
					title.add("案號");
					title.add("廠商填報累計核准投資金額");
					title.add("原系統累計核准投資金額");
					title.add("廠商填報累計已核備投資金額");
					title.add("原系統累計已核備投資金額");
					list.add(title);
				}
				if(map.get("s1").isEmpty()&&map.get("s2").isEmpty()){
					continue;
				}else if(type.equals("1")&&map.get("s1").isEmpty()){
					continue;
				}else if(type.equals("2")&&map.get("s2").isEmpty()){
					continue;
				}else{
					count++;
				}
				sublist.add(String.valueOf(count));
				sublist.add(year);
				sublist.add(season);
				sublist.add(IDNOToName.get(map.get("IDNO")));
				sublist.add(map.get("IDNO"));
				sublist.add(InvestNoToName.get(map.get("investNo")));
				sublist.add(map.get("investNo"));
				sublist.add(map.get("k1"));
				sublist.add(map.get("d1"));
				sublist.add(map.get("k2"));
				sublist.add(map.get("d2"));
				list.add(sublist);
			}
		if(temp.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('查無符合條件資料，請重新選取！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/project/dbdifferdownload.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		wb.createSheet("投資金額差異");
		Sheet sheet = wb.getSheetAt(0);
		for(int i=0;i<list.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<list.get(i).size();k++){
				String str = list.get(i).get(k);
				if(i!=0&&k>6&&!str.isEmpty()){
					double x = Double.valueOf(str); 
					row.createCell(k).setCellValue(x);
				}else{
					row.createCell(k).setCellValue(createHelper.createRichTextString(str));
				}
			}
		}
		sheet.setColumnWidth(0, 8*256);
		sheet.setColumnWidth(1, 8*256);
		sheet.setColumnWidth(2, 8*256);
		sheet.setColumnWidth(3, 20*256);
		sheet.setColumnWidth(4, 10*256);
		sheet.setColumnWidth(5, 20*256);
		sheet.setColumnWidth(6, 10*256);
		sheet.setColumnWidth(7, 28*256);
		sheet.setColumnWidth(8, 28*256);
		sheet.setColumnWidth(9, 28*256);
		sheet.setColumnWidth(10, 28*256);
		
		String dfilename=sb.append(".xlsx").toString();
		sb.setLength(0);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" +DataUtil.encodeFileName(request,dfilename) + "\"");
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		wb.write(out);
		out.close();
	}
}

