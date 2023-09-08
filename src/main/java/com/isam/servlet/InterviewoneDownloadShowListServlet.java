package com.isam.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.isam.bean.CommonItemList;
import com.isam.bean.Interviewone;
import com.isam.bean.InterviewoneCI;
import com.isam.bean.OFIInvestNoXAudit;
import com.isam.bean.OFIInvestorXRelated;
import com.isam.helper.DataUtil;
import com.isam.service.CommonItemListService;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneService;
import com.isam.service.MoeaicDataService;
import static java.util.Comparator.comparing;

public class InterviewoneDownloadShowListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneService ioSer;
	private InterviewoneFileService iofSer;
	private MoeaicDataService mdSer;
	private InterviewoneHelp iohelper;
	private Map<String,Map<String,String>> cninfo;
	private Map<String,Map<String,String>> optionValName;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ioSer = new InterviewoneService();
		iofSer = new InterviewoneFileService();
		mdSer = new MoeaicDataService();
		cninfo=mdSer.getCNSysBaseInfo();
		iohelper = new InterviewoneHelp();
		optionValName=iohelper.getOptionValName();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	//	this.doPost(req, resp);
	}
	 String a="";
	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
	
		
		List<String> yearlist=ioSer.getYearList();
		
		Map<String,String> map=new HashMap<String, String>();

		String action=request.getParameter("action")==null?"":request.getParameter("action");
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String investName=DataUtil.fmtSearchItem(request.getParameter("investName"),"%");

	
		String abnormal=request.getParameterValues("abnormal")==null?"":DataUtil.addTokenToItem(request.getParameterValues("abnormal"),",");
		//107-08-01 新增異常狀況條件查詢(來源來自訪視問卷裡的 異常狀況彙總 & 訪視備註)
		String errMsgXnote=request.getParameter("errMsgXnote")==null?"":request.getParameter("errMsgXnote");
		Map<String,String> maxYM=ioSer.getMaxInterviewDateYM();
		
		System.out.println(abnormal);
		
		map.put("investNo", investNo);
		map.put("IDNO", IDNO);
		map.put("investName", investName);
	
		map.put("abnormal", abnormal);
		map.put("errMsgXnote", errMsgXnote);
		
	
	//	List<InterviewoneCI> beans;
		
		//	beans = ioSer.selectCI(map);
	

		
		//107-08-01 新增異常狀況條件查詢(來源來自訪視問卷裡的 異常狀況彙總 & 訪視備註)
		Gson gson = new Gson();
		CommonItemListService dao = new CommonItemListService();
//		ArrayList<String> errMsgXnoteS = dao.getCommonItemString();
//		session.setAttribute("errMsgXnote", gson.toJson(errMsgXnoteS));
		//107-08-22
		ArrayList<CommonItemList> errMsgXnoteS = dao.get();
		session.setAttribute("errMsgXnote", gson.toJson(errMsgXnoteS));
		
		
		String dfilename ="訪查資料"+DataUtil.getStrUDate()+".xlsx";
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		
		wb.createSheet("國內事業列表");
		wb.createSheet("陸資投資人列表");
		int currentRow = 0;
		Sheet sheet1 = wb.getSheetAt(0);
	
		List<List<String>> ilist = ioSer.getTIInvestorData(map);
		Sheet sheet = wb.getSheetAt(0);
		for(int i=0;i<ilist.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<ilist.get(i).size();k++){
				String str = ilist.get(i).get(k);
				row.createCell(k).setCellValue(createHelper.createRichTextString(str));
			}
		}
		
	//	Sheet sheet2 = wb.getSheetAt(1);
		
		ilist = ioSer.getCIInvestorData(map);
		 sheet = wb.getSheetAt(1);
		for(int i=0;i<ilist.size();i++){
			Row row = sheet.createRow((short)i);
			for(int k=0;k<ilist.get(i).size();k++){
				String str = ilist.get(i).get(k);
				row.createCell(k).setCellValue(createHelper.createRichTextString(str));
			}
		}
		
		/*
		 currentRow = 0;
		  titleRow = sheet2.createRow(currentRow++);
			 maxNumAudit = 0;
			 currentTitleCell = 0;
			titleRow.createCell(currentTitleCell++).setCellValue("陸資案號");
			titleRow.createCell(currentTitleCell++).setCellValue("國內事業名稱");
			titleRow.createCell(currentTitleCell++).setCellValue("統一編號");
			titleRow.createCell(currentTitleCell++).setCellValue("投資人");
			titleRow.createCell(currentTitleCell++).setCellValue("資料狀態");
			titleRow.createCell(currentTitleCell++).setCellValue("投資人國別");
			titleRow.createCell(currentTitleCell++).setCellValue("投資人省分");
			titleRow.createCell(currentTitleCell++).setCellValue("核准金額");
			titleRow.createCell(currentTitleCell++).setCellValue("審定金額");
			titleRow.createCell(currentTitleCell++).setCellValue("審定股數");
			for(int i=0;i<  maxNumRelated;i++) {
				titleRow.createCell(currentTitleCell++).setCellValue((i+1)+"_母公司（或關連企業）及受益人名稱");
				titleRow.createCell(currentTitleCell++).setCellValue((i+1)+"_母公司（或關連企業）及受益人國別");
			
			}
			titleRow.createCell(currentTitleCell++).setCellValue("背景1-黨政軍案件");
			titleRow.createCell(currentTitleCell++).setCellValue("背景1-備註");
			titleRow.createCell(currentTitleCell++).setCellValue("背景2-黨政軍案件");
			titleRow.createCell(currentTitleCell++).setCellValue("背景2-備註");
			titleRow.createCell(currentTitleCell++).setCellValue("架構圖");
			titleRow.createCell(currentTitleCell++).setCellValue("備註");
			
			for(InterviewoneCI bean :beans.stream().filter(item->item.getIsCNFDI()).collect(Collectors.toList())) {
				
				
				if(abnormal.contains("1")&&!bean.getTwsic().stream().anyMatch(item->item.getItem().startsWith("I3"))) {
					continue;
				}
				if(abnormal.contains("2")&&!bean.getTwsic().stream().anyMatch(item->item.getType().equals("0"))) {
					continue;
				}
				if(abnormal.contains("3")&&!bean.getAudit().stream().anyMatch(item->item.getAuditCode().equals("02")&& item.getValue().equals("1"))) {
					continue;
				}
				if(abnormal.contains("4")&&!bean.getAudit().stream().anyMatch(item->item.getAuditCode().equals("07")&& item.getValue().equals("1"))) {
					continue;
				}
				
				if(abnormal.contains("5")&&!bean.getXBG().stream().anyMatch(item->item.getBgType().equals("BG1")&& (item.getValue().equals("1")||item.getValue().equals("2")||item.getValue().equals("3")))) {
					continue;
				}
				if(abnormal.contains("6")&&!bean.getXBG().stream().anyMatch(item->item.getBgType().equals("BG2")&& (item.getValue().equals("1")||item.getValue().equals("2")))) {
					continue;
				}
				int currentCell = 0;
				Row row = sheet2.createRow(currentRow++);
				row.createCell(currentCell++).setCellValue(bean.getInvestNo());
				row.createCell(currentCell++).setCellValue(bean.getCompanyName());
				row.createCell(currentCell++).setCellValue(bean.getBanNo());
				row.createCell(currentCell++).setCellValue(bean.getInvestorName());
				row.createCell(currentCell++).setCellValue(bean.getIsFilled());
				row.createCell(currentCell++).setCellValue(bean.getNation());
				row.createCell(currentCell++).setCellValue(bean.getCnCode());
				row.createCell(currentCell++).setCellValue(bean.getMoney1());
				row.createCell(currentCell++).setCellValue(bean.getMoney2());
				row.createCell(currentCell++).setCellValue(bean.getStockimp());
				 List<OFIInvestorXRelated> ate= bean.getRelated();
				for(OFIInvestorXRelated audit : ate ) {
					
					row.createCell(currentCell++).setCellValue(audit.getRelatedname());
					row.createCell(currentCell++).setCellValue(audit.getNation()+audit.getCnCode());
			
					}
				 for(tmp = ate.size() ; tmp < maxNumRelated;tmp++) {
					 
				
						
							currentCell+=2;
					
						
							
					}
				 
				 if( bean.getXBG().stream().anyMatch(item->item.getBgType().equals("BG1"))) {
						
						
						
						Set<String> result = new HashSet<>();
						bean.getXBG().stream().filter(item->item.getBgType().equals("BG1")).forEach(item-> result.add(BG1ToString(item.getValue())));
						row.createCell(currentCell++).setCellValue(String.join("、", result));
					}
					else {
						row.createCell(currentCell++).setCellValue("否");
					}
				 
				 if( bean.getXBG().stream().anyMatch(item->item.getBgType().equals("BG1Note"))) {
						
						
						
						Set<String> result = new HashSet<>();
						bean.getXBG().stream().filter(item->item.getBgType().equals("BG1Note")).forEach(item-> result.add(BG1ToString(item.getValue())));
						row.createCell(currentCell++).setCellValue(String.join("、", result));
					}
					else {
						row.createCell(currentCell++).setCellValue("");
					}
				 
				 if( bean.getXBG().stream().anyMatch(item->item.getBgType().equals("BG2"))) {

						Set<String> result = new HashSet<>();
						bean.getXBG().stream().filter(item->item.getBgType().equals("BG2")).forEach(item-> result.add(BG2ToString(item.getValue())));
						row.createCell(currentCell++).setCellValue(String.join("、", result));
					}
					else {
						row.createCell(currentCell++).setCellValue("否");
					}
				 
				 if( bean.getXBG().stream().anyMatch(item->item.getBgType().equals("BG2Note"))) {
						
						
						
						Set<String> result = new HashSet<>();
						bean.getXBG().stream().filter(item->item.getBgType().equals("BG2Note")).forEach(item-> result.add(BG1ToString(item.getValue())));
						row.createCell(currentCell++).setCellValue(String.join("、", result));
					}
					else {
						row.createCell(currentCell++).setCellValue("");
					}
			
				
			}
			*/
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(dfilename, "utf-8") + "\"");
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		wb.write(out);
		out.close();
	}
	
	private String BG1ToString(String val) {
		if(val.equals("0")) 
		{
			return "否";
		}
		else 
		if(val.equals("1")) {
			return "黨";
		}
		else if (val.equals("2")) {
			return "政";
		}
		else if (val.equals("3")) {
			return "軍";
		}
		else {
			return val;
		}
	
		
	}
	private String BG2ToString(String val) {
		if(val.equals("0")) 
		{
			return "否";
		}
		else 
		if(val.equals("1")) {
			return "央企";
		}
		else if (val.equals("2")) {
			return "政府出資";
		}
	
		else {
			return val;
		}
	
		
	}
	int tmp=0;
}
