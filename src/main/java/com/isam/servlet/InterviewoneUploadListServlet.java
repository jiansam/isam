package com.isam.servlet;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.isam.bean.Interviewone;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneService;
import com.isam.service.MoeaicDataService;


public class InterviewoneUploadListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();
		Map<String, String> errors = new HashMap<String, String>();
		
		String year="";
		Set<String> set=new HashSet<String>();
		InterviewoneService ser = new InterviewoneService();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = null;
			try { 
				iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					InputStream stream = item.openStream();
					if (item.isFormField()) {
						String value = Streams.asString(stream,"UTF-8");
						 if(name.equals("surveyyear")){
							 year=DataUtil.addZeroForNum(value, 3);
						 }
					}else{
						Workbook wb = WorkbookFactory.create(stream);
					 	try {
							Sheet sheet = wb.getSheetAt(0); // 取得workbook的第一個sheet
							int rowa = sheet.getPhysicalNumberOfRows(); // 含欄位名稱
							if (rowa == 0) {
								errors.put("num", "您上傳的檔案無內容，請確定您的檔案第一個分頁是否有值!");
							}
							// 依序將每欄資料讀出，放進map
							for (int i = 1; i < rowa; i++) {
								Row row = sheet.getRow(i);
								if(row!=null){
									Cell temp = row.getCell(0,Row.CREATE_NULL_AS_BLANK);
									String cell = "";
									if (temp != null&&!temp.equals("")) {
										temp.setCellType(Cell.CELL_TYPE_STRING);
										cell = temp.getStringCellValue();
									}
									if (cell != null&&!cell.equals("")) {
//											取代強制斷行及不分行符號
										cell = cell.trim().replaceAll("\\^l|\\^s"," ");
										set.add(cell);
									}									
								} 		
							} 		
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							stream.close();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		System.out.println("year="+year);
		Timestamp time=DataUtil.getNowTimestamp();
		List<Interviewone> beans=new ArrayList<Interviewone> ();
		MoeaicDataService MSer = new MoeaicDataService();
		List<String> list=new ArrayList<String>();
		list.addAll(set);
		for (int i = 0; i < list.size(); i++) {
			String investNo=list.get(i);
			if(!ser.isExists(year, investNo,null)){
				if(MSer.isCNInvestNoInRange(investNo)){
					Interviewone bean = new Interviewone();
					bean.setInvestNo(investNo);
					bean.setReInvestNo("0");
					bean.setYear(year);
					bean.setInterviewStatus("0");
					bean.setSurveyStatus("0");
					bean.setEnable("1");
					bean.setUpdatetime(time);
					bean.setUpdateuser(updateuser);
					bean.setCreatetime(time);
					bean.setCreateuser(updateuser);
					bean.setMsg("");
					beans.add(bean);
				}
			}else{
				//errors
			}
		}
		if(beans.size()>0){
			ser.insert(beans);
		}else{
			//errors
		}
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/interviewone/showiolist.jsp?action=manage&year="+year);
	}
	
}
