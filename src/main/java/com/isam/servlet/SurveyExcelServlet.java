package com.isam.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.isam.helper.DataUtil;
import com.isam.service.SurveyService;

public class SurveyExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Map<String, String> qTypeMap= (Map<String, String>) session.getAttribute("qTypeName");
		Map<String, List<String>> qTypeYear= ( Map<String, List<String>>) session.getAttribute("qTypeYear");
		String qType = request.getParameter("qType");
		String[] years = request.getParameterValues("year");
		String[] inds = request.getParameterValues("ind");
		String[] regions = request.getParameterValues("region");
		String[] topics = request.getParameterValues("topic");
		String ind="";
		String region="";
		String topic="";
		
		System.out.println(new Date());
		
		if(qType==null||!qTypeMap.containsKey(qType)){
			request.setCharacterEncoding("UTF-8");
	    	response.setContentType("text/html;charset=UTF-8");
	    	PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('問卷代碼錯誤!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/survey/surveydownloaditem.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		String yearError="";
		if(years==null){
			yearError="請選擇訪查年度";
		}else{
			for(String s:years){
				if(!qTypeYear.get(qType).contains(s)){
					yearError="訪查年度錯誤，請重新選取";
				}
			}
		}
		if(!yearError.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('"+yearError+"!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/survey/surveydownloaditem.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		SurveyService ser = new SurveyService();
		if(inds!=null&&inds.length!=0){
			ind=ser.getStrItem(inds,"Industry");
		}
		if(regions!=null&&regions.length!=0){
			region=ser.getStrItem(regions,"distraction");
		}
		if(topics!=null&&topics.length!=0){
			topic=ser.getStrItem(topics,qType);
		}
		String dfilename =qTypeMap.get(qType)+DataUtil.getStrUDate()+".xlsx";
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		int flag=0;
		for(int i=0;i<years.length;i++){
			wb.createSheet(years[i]);
			Sheet sheet = wb.getSheetAt(i);
			List<List<String>> list = ser.getSurveyResult(qType, years[i], topic, ind, region);
			for(int j=0;j<list.size();j++){
				Row row = sheet.createRow((short)j);
				for(int k=0;k<list.get(j).size();k++){
					row.createCell(k).setCellValue(createHelper.createRichTextString(list.get(j).get(k)));
				}
			}
			if(sheet.getLastRowNum()!=0){
				flag++;
			}
		}
		if(flag==0){
			request.setCharacterEncoding("UTF-8");
	    	response.setContentType("text/html;charset=UTF-8");
	    	PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('查無符合條件資料!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/survey/surveydownloaditem.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
//		response.setContentType("application/vnd.openxmlformats;charset=UTF-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(dfilename, "utf-8") + "\"");
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		wb.write(out);
		out.close();
		
		System.out.println(new Date());
	}
	
}
