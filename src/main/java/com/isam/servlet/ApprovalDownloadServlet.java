package com.isam.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.dasin.tools.dPairHashMap;
import org.dasin.tools.dTools;

import com.isam.bean.ProjectContact;
import com.isam.dao.ApprovalOptionDAO;
import com.isam.dao.CommitRestrainTypeDAO;
import com.isam.dao.ProjectReportDAO;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.CommitReportService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectReportService;

public class ApprovalDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, String> IDNOToName;
	private CommitReportService crSer;
	private ProjectReportService prSer;
	private Map<String, String> CRType;
	private Map<String,String> AoCode;
	private Map<String,String> ProjectState;
	private Map<String,String> financialState;
	private Map<String,String> repStateMap;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName=help.getIDNOToName();
		ProjectState =help.getProjectState();
		financialState=help.getFinancialState();
		crSer = new CommitReportService();
		prSer = new ProjectReportService();
		CRType = CommitRestrainTypeDAO.getTypeMap();
		AoCode = ApprovalOptionDAO.getOptionMapByType("Commit");
		repStateMap=new HashMap<String, String>();
		repStateMap.put("0", "未申報");
		repStateMap.put("1", "已申報");
		repStateMap.put("99", "本次免申報");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		String approval = request.getParameter("approval");
		String year = request.getParameter("year");
		String quarter = request.getParameter("quarter");
		String type = request.getParameter("type");
		String repType = request.getParameter("repType");
		String declare = DataUtil.nulltoempty(request.getParameter("declare")).equals("-1")?null:request.getParameter("declare");
	
		String idno = request.getParameter("idno");
		String investment = DataUtil.nulltoempty(request.getParameter("investment"));
		String from = request.getParameter("from");
		String to = request.getParameter("to");
		
		Map<String,String> terms=new HashMap<String, String>();
		terms.put("approval", approval);
		terms.put("year",year );
		terms.put("quarter", quarter);
		terms.put("type",type );
		terms.put("repType", repType);
		terms.put("declare", request.getParameter("declare"));
		terms.put("idno", idno);
		terms.put("investment", investment);
		terms.put("from", from);
		terms.put("to", to);
		
		String paramStr=DataUtil.toParamStr(terms);
		StringBuffer sb= new StringBuffer();
//		System.out.println(approval+":"+year+":"+quarter+":"+type+":"+repType+":"+declare);
		List<Map<String,String>> temp=null;
		List<List<String>> list=new ArrayList<List<String>>();
		String projDate =DataUtil.addZeroForNum(year,3);
		if(approval.equals("0101")){
			sb.append("專案").append(year).append("年");
			if(!quarter.equals("4")){
				sb.append("第").append(quarter).append("季申報執行情形");
				if(quarter.equals("1")){
					projDate=DataUtil.addZeroForNum(String.valueOf((Integer.valueOf(projDate)-1)),3)+"1231";
				}else if(quarter.equals("2")){
					projDate=projDate+"0331";
				}else{
					projDate=projDate+"0630";
				}
			}else{
				sb.append("年報申報執行情形");
				projDate=projDate+"1231";
			}
			
			temp = prSer.getReportList(year, quarter, declare, projDate);
			dPairHashMap<String, String, ProjectContact> latest_contact_map = ProjectReportDAO.mapLatestContact();
			
			for (int i = 0; i < temp.size(); i++) {
				Map<String,String> map=temp.get(i);
				List<String> sublist=new ArrayList<String>();
				if(i==0){
					List<String> title=new ArrayList<String>();
					title.add("序號");
					title.add("年度");
					title.add("投資人");
					title.add("統編");
					title.add("大陸事業名稱");
					title.add("案號");
					title.add("管制狀態");
					title.add("申報狀態");
					title.add("免申報備註");
					title.add("核准日期");
					if(quarter.equals("4")){
						title.add("財報");
					}
					
					//2017.11.22.dasin : 新增聯絡人資料
					title.add("聯絡人");
					title.add("聯絡電話");
					title.add("聯絡地址");
					
					list.add(title);
				}
				
				sublist.add(String.valueOf(i+1));
				sublist.add(year);
				sublist.add(IDNOToName.get(map.get("IDNO")));
				sublist.add(map.get("IDNO"));
				sublist.add(InvestNoToName.get(map.get("investNo")));
				sublist.add(map.get("investNo"));
				sublist.add(ProjectState.get(map.get("state")));
				sublist.add(repStateMap.get(map.get("repState")));
				sublist.add(map.get("noNeedNote"));
				sublist.add(map.get("porjDate"));
				if(quarter.equals("4")){
					sublist.add(financialState.get(map.get("financial")));
				}
				
				//2018.2.8.dasin : 若未申報，則取用前一次申報之聯絡人
				if(!dTools.isEmpty(map.get("contact_name"))) {
					sublist.add(DataUtil.trim(map.get("contact_name")));
					sublist.add(DataUtil.trim(map.get("contact_tel_no")));
					sublist.add(DataUtil.trim(map.get("COUNTY_NAME")) 
						+ DataUtil.trim(map.get("TOWN_NAME")).replace("　", "") 
						+ DataUtil.trim(map.get("contact_ADDRESS")));
				}else{
					ProjectContact contact = latest_contact_map.get(map.get("investNo"), map.get("IDNO"));
					if(contact != null) {
						sublist.add(DataUtil.trim(contact.getContact_name()));
						sublist.add(DataUtil.trim(contact.getContact_tel_no()));
						sublist.add(DataUtil.trim(contact.getComplete_Address()));
					}else {
						sublist.add("");
						sublist.add("");
						sublist.add("");
					}
				}
				
				list.add(sublist);
			}
		}else if(approval.equals("0102")){
			sb.append("承諾").append(from).append("年-").append(to).append("年_").append(CRType.get(type)).append("申報執行情形");
			temp=crSer.getReportList(from,to, type, repType, declare,idno,investment);
			
			for (int i = 0; i < temp.size(); i++) {
				Map<String,String> map=temp.get(i);
				List<String> sublist=new ArrayList<String>();
				if(i==0){
					List<String> title=new ArrayList<String>();
					title.add("序號");
					title.add("年度");
					if(type.equals("01")){
						title.add("申報類型");
					}
					title.add("投資人");
					title.add("統編");
					title.add("管制狀態");
					title.add("申報狀態");
					title.add("文號");
					title.add("聯絡人資訊");
					list.add(title);
				}
				sublist.add(String.valueOf(i+1));
				sublist.add(map.get("year"));
				if(type.equals("01")){
					sublist.add(AoCode.get(repType));
				}
				sublist.add(IDNOToName.get(map.get("IDNO")));
				sublist.add(map.get("IDNO"));
				sublist.add(map.get("state").equals("Y")?"管制":"解除管制");
				sublist.add(map.get("repState").equals("0")?"未申報":"已申報");
				sublist.add(removeDuplicates(map.get("reNos")));	//2018.1.11.dasin : remove duplicates.
				sublist.add(map.get("contact"));
				list.add(sublist);
			}
		}
		if(temp.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('查無符合條件資料，請重新選取！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/approval/showdownload.jsp?"+paramStr+"';</script>");
			out.flush();
			out.close();
			return;
		}
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		wb.createSheet("核准資料");
		Sheet sheet = wb.getSheetAt(0);
		for(int i=0;i<list.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<list.get(i).size();k++){
				String str = list.get(i).get(k);
				row.createCell(k).setCellValue(createHelper.createRichTextString(str));
			}
		}
		String dfilename=sb.append(".xlsx").toString();
		sb.setLength(0);
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" +DataUtil.encodeFileName(request,dfilename) + "\"");
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		wb.write(out);
		out.close();
	}
	
	String removeDuplicates(String inString) {
		inString = inString == null ? "" : inString.trim();
		ArrayList<String> list = new ArrayList<String>();
		
		for(String s : inString.split("、")) {
			if(!list.contains(s)) {
				list.add(s);
			}
		}
		
		Collections.sort(list);
		
		return String.join("、", list);
	}
}

