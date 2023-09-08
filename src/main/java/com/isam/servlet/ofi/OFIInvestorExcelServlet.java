package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dasin.tools.dTools;

import com.isam.bean.OFIInvestBaseData;
import com.isam.bean.OFIInvestOffice;
import com.isam.bean.OFIInvestorXBG;
import com.isam.bean.OFIInvestorXRelated;
import com.isam.helper.DataUtil;
import com.isam.helper.PairHashtable;
import com.isam.service.ofi.OFIAuditOptionService;
import com.isam.service.ofi.OFIInvestOfficeService;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIInvestorExcelService;
import com.isam.service.ofi.OFIInvestorListService;

import Lara.Utility.DateUtil;
import Lara.Utility.ExcelUtil;
import Lara.Utility.ToolsUtil;
import Lara.Utility.WebTools;

public class OFIInvestorExcelServlet extends HttpServlet
{

	private OFIInvestOfficeService officeSer;
	private OFIInvestorListService listSer;
	private OFIInvestOptionService optSer;
	private OFIInvestorExcelService service;
	private Map<String,Map<String,String>> optmap;
	private ArrayList<String> header;
	private ArrayList<String> header1;
	private ArrayList<String> header2;
	private List<OFIInvestBaseData> baseData;
	private Map<String, List<OFIInvestorXRelated>> related;
	private Map<String, List<OFIInvestorXBG>> bgs;
	private Map<String, List<String>> files;
	private Map<String, OFIInvestBaseData> moeaic_datas;
	private PairHashtable<String, String, OFIInvestBaseData> invest;
	int relatedLen = 0;

	@Override
	public void init() throws ServletException
	{
		officeSer = new OFIInvestOfficeService();
		listSer = new OFIInvestorListService();
		optSer = new OFIInvestOptionService();
		service = new OFIInvestorExcelService();
		optmap = optSer.select();
		
		header1 = new ArrayList<>();
		header1.add("母公司（或關連企業）及受益人名稱");
		header1.add("母公司（或關連企業）及受益人國別");
		
		header2 = new ArrayList<>();
		header2.add("背景1-黨政軍案件");
		header2.add("背景1-備註");
		header2.add("背景2-央企政府出資案件");
		header2.add("背景2-備註");
		header2.add("架構圖");
		header2.add("備註");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Map<String,String> terms=new HashMap<String, String>();		
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String companytype=request.getParameter("companytype")==null?"":request.getParameter("companytype");
		String company=DataUtil.fmtSearchItem(request.getParameter("company"), "%");
		String investor=DataUtil.fmtSearchItem(request.getParameter("investor"), "%");
		String investorXRelated=DataUtil.fmtSearchItem(request.getParameter("investorXRelated"), "%");
		String relatedNation=DataUtil.nulltoempty(request.getParameter("nation2"));
		String relatedCnCode=DataUtil.nulltoempty(request.getParameter("cnCode2"));
		String nation=DataUtil.nulltoempty(request.getParameter("nation"));
		String cnCode=DataUtil.nulltoempty(request.getParameter("cnCode"));
		String BG1=request.getParameterValues("BG1")==null?"":DataUtil.addTokenToItem(request.getParameterValues("BG1"),",");
		String BG2=request.getParameterValues("BG2")==null?"":DataUtil.addTokenToItem(request.getParameterValues("BG2"),",");
		String AndOr1=DataUtil.nulltoempty(request.getParameter("AndOr1"));
		String AndOr2=DataUtil.nulltoempty(request.getParameter("AndOr2"));
		String fbtype=DataUtil.nulltoempty(request.getParameter("fbtype"));
		terms.put("investNo", investNo);
		terms.put("IDNO", IDNO); //統一編號
		terms.put("companytype",companytype);
		terms.put("company", company);
		terms.put("investor", investor);
		terms.put("investorXRelated", investorXRelated);
		terms.put("relatedNation", relatedNation);
		terms.put("relatedCnCode", relatedCnCode);
		terms.put("nation", nation); 
		terms.put("cnCode", cnCode);
		terms.put("BG1", BG1); //背景1
		terms.put("BG2", BG2); //背景2
		terms.put("AndOr1", AndOr1);
		terms.put("AndOr2", AndOr2);
		

		terms.put("fbtype", fbtype);
		
		/* 取出投資人列表 */
		List<Map<String,String>> list=listSer.select(terms);
		if(list == null || list.isEmpty()){ //取無資料便轉回
			String url="/cnfdi/investorlist.jsp";
			if(fbtype.equals("b")){
				url="/console/cnfdi/investorlist.jsp";
			}
			
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"
						+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+url+"';</script>");
			out.flush();
			out.close();
			return;
		}

		
		List<String> investors = new ArrayList<>();
		List<String> investNos = new ArrayList<>();
		baseData = new ArrayList<>();
		for(Map<String,String> temp : list){
			
			String investorSeq = temp.get("investorSeq");
			String investNoStrs = temp.get("investNoStrs");

			String cn = temp.get("cn");
			if(cn!=null ) {
				String investorname = temp.get("INVESTOR_CHTNAME");
				if(  cn.isEmpty()) {
					if(investorname.indexOf("香港商")>=0) {
						temp.replace("cn", "香港");
					}
					else if(investorname.indexOf("日商")>=0) {
						temp.replace("cn", "日本");
					}
					else if(investorname.indexOf("大陸商")>=0) {
						temp.replace("cn", "中國大陸");
					}
					else if(investorname.indexOf("薩摩亞商")>=0) {
						temp.replace("cn", "薩摩亞");
					}
					else if(investorname.indexOf("英商")>=0) {
						temp.replace("cn", "英國");
					}
					else if(investorname.indexOf("新加坡商")>=0) {
						temp.replace("cn", "新加坡");
					}
					else if(investorname.indexOf("英屬維京群島商")>=0) {
						temp.replace("cn", "英屬維京群島");
					}
					else if(investorname.indexOf("貝里斯商")>=0) {
						temp.replace("cn", "貝里斯");
					}
					else if(investorname.indexOf("=賽席爾商")>=0) {
						temp.replace("cn", "賽席爾");
					}
				}
			}
			
			if(investNoStrs.isEmpty()) {
				OFIInvestBaseData bean = new OFIInvestBaseData();
				bean.setCOMP_CHTNAME("");
				bean.setInvestNo(temp.get("note"));
	
				bean.setInvestorSeq(investorSeq);
				
				bean.setINVESTOR_CHTNAME(temp.get("INVESTOR_CHTNAME"));
				bean.setInrole(temp.get("inrole"));
				bean.setNote(temp.get("note"));
				bean.setCountry(temp.get("country"));
				bean.setCnCode(temp.get("cn"));
				bean.setFilled( ("0".equals(temp.get("isFilled")) ? false:true) );
				baseData.add(bean);
			}
			else {
				for(String ivtNo : ToolsUtil.getValueToList(investNoStrs, "、")){
					OFIInvestBaseData bean = new OFIInvestBaseData();
					
					bean.setInvestNo(ivtNo);
					bean.setInvestorSeq(investorSeq);
					bean.setINVESTOR_CHTNAME(temp.get("INVESTOR_CHTNAME"));
					bean.setInrole(temp.get("inrole"));
					bean.setNote(temp.get("note"));
					bean.setCountry(temp.get("country"));
					bean.setCnCode(temp.get("cn"));
					bean.setFilled( ("0".equals(temp.get("isFilled")) ? false:true) );
					baseData.add(bean);
					if(!investNos.contains(ivtNo)){
						investNos.add(ivtNo); //取moeaic資料使用
					}
				}
			}
			if(!investors.contains(investorSeq)){
				investors.add(investorSeq);
			}
		}

		
		/* 取出相關資料 */
		related = service.getRelateds(investors); //取出所有投資人的母公司資訊
		bgs = service.getBGs(investors); //取出所有投資人的背景資訊
		files = service.getFiles(investors); //取出所有投資人的架構圖ID
		invest = service.getInvestDatas(investors); //取出所有投資人的投資案資料
		moeaic_datas = service.getMoeaicDatas(investNos); //取出所有陸資案號的組織型態、發行方式

		header = new ArrayList<>();
		header.add("陸資案號");		header.add("國內事業名稱");		header.add("統一編號");		header.add("組織型態");
		header.add("發行方式");		header.add("登記資本額");		header.add("實收資本額");		header.add("面額");
		header.add("投資人");		header.add("資料狀態");		header.add("投資人國別");		header.add("投資人省分");
		header.add("資金類型");		header.add("投資金額");		header.add("持有股權(出資額)");		header.add("股權比例");

		/* 檢查母公司最多有幾個 */
		for(String bgKey : related.keySet()){
			int length = related.get(bgKey).size();
			if(length > relatedLen){
				relatedLen = length;
			}
		}
		/* header因為不確定母公司最多有幾組，所以先分成三組，現在知道母公司組數後，再組合到header裡面
		 * 1_母公司（或關連企業）及受益人名稱	、1_母公司（或關連企業）及受益人國別 */
		for(int i=1; i<=relatedLen; i++){ 
			for(String name : header1){
				header.add(i+"_"+name);
			}
		}
		header.addAll(header2);

		creatExcel(request, response);
	}

	private void creatExcel(HttpServletRequest request, HttpServletResponse response)
	{
		ServletOutputStream out = null;
		try{
			
		/* 1.產生一個EXCEL ====================================================================================*/
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("投資人列表");
			int rowNum = 0;
			
		/* 2.設定 字型、欄位背景色、框線色 ===========================================================================*/
			/* 設定欄位字型Font  - (自寫method) setFont(int style, Workbook wb, int font_size, String font_name); */
			Font hearder_font = ExcelUtil.setFont("header", wb, 12, "微軟正黑體");; 
			Font data_font = ExcelUtil.setFont("data", wb, 12, "微軟正黑體");
			/* 設定儲存格欄位背景  */
			Map<String, Integer> yellow = new HashMap<>();yellow.put("r", 255);yellow.put("g", 244);yellow.put("b", 213); //淺橘色
			/* 設定框線顏色  */
			Map<String, Integer> gray = new HashMap<>();gray.put("r", 191);gray.put("g", 191);gray.put("b", 191); //灰色-191,191,191
			
			/* 設定欄位格式Cellstyle - (自寫method) setCellStyle(style, wb, cellColor, font, borderStyle, borderColor); */
			CellStyle header_center = ExcelUtil.setCellStyle("center", wb, yellow, hearder_font, "thin", gray); //黑字 灰框
			CellStyle header_left = ExcelUtil.setCellStyle("left", wb, yellow, hearder_font, "thin", gray); //黑字 灰框
			CellStyle data_left = ExcelUtil.setCellStyle("left", wb, null, data_font, "thin", gray); //黑字 灰框
			CellStyle data_center = ExcelUtil.setCellStyle("center", wb, null, data_font, "thin", gray); //黑字 灰框
			CellStyle data_right = ExcelUtil.setCellStyle("right", wb, null, data_font, "thin", gray); //黑字 灰框
			
			
			
		/* 3.內容 ==============================================================================================*/
			
			/* 3-1.標題 -----------------------------------------------------------------------------------*/
			Row header_row = sheet.createRow(rowNum++);
			int headerColNum = 0;
			for(String hr : header){
				CellStyle hStyle = null;
				if(hr.indexOf("母公司") == -1){
					hStyle = header_center;
				}else{
					hStyle = header_left;
				}
				ExcelUtil.createCell("str", headerColNum++, header_row, hStyle, hr);
			}
			
			/* 3-2.內容 -----------------------------------------------------------------------------------*/
			for(OFIInvestBaseData bean : baseData){
				Row row = sheet.createRow(rowNum++);
				int column = 0;
				String investor = bean.getInvestorSeq(); //投資人
				String investNo = bean.getInvestNo(); //陸資案號
			
				
				if(investor.equals("0")) {
					
					OFIInvestOffice officeObj = officeSer.select(investNo);
					ExcelUtil.createCell("str", column++, row, data_center, ""); //陸資案號
					ExcelUtil.createCell("str", column++, row, data_left,officeObj.getCompname()); //公司名稱
					ExcelUtil.createCell("str", column++, row, data_center,officeObj.getBanNo()); //統一編號
					ExcelUtil.createCell("str", column++, row, data_center, "辦事處");           //組織型態
				ExcelUtil.createCell("str", column++, row, data_center, "");					//發行方式
					ExcelUtil.createCell("str", column++, row, data_center, "");				//登記資本額
					ExcelUtil.createCell("str", column++, row, data_center, "");				//實收資本額
					ExcelUtil.createCell("str", column++, row, data_center, "");				//面額
					ExcelUtil.createCell("str", column++, row, data_center, officeObj.getCompname());				//投資人
					ExcelUtil.createCell("str", column++, row, data_center, bean.isFilled()==true?"已確認":"未確認");				//資料狀態
					ExcelUtil.createCell("str", column++, row, data_center, bean.getCnCode());				//投資人國別
					ExcelUtil.createCell("str", column++, row, data_center, "");				//投資人省分
					ExcelUtil.createCell("str", column++, row, data_center,"陸資" );				//資金類型
					ExcelUtil.createCell("str", column++, row, data_center,"" );				//投資金額
					ExcelUtil.createCell("str", column++, row, data_center,"" );				//持有股權
					ExcelUtil.createCell("str", column++, row, data_center,"" );				//股權比例
					ExcelUtil.createCell("str", column++, row, data_center,"" );				//背景1-黨政軍案件
					ExcelUtil.createCell("str", column++, row, data_center,"" );				//背景1-備註
					ExcelUtil.createCell("str", column++, row, data_center,"" );				//背景2-央企政府出資案件
					ExcelUtil.createCell("str", column++, row, data_center,"" );				//背景2-備註
					ExcelUtil.createCell("str", headerColNum-2, row, data_center,"無" );				//架構圖
					ExcelUtil.createCell("str", headerColNum-1, row, data_center,"" );				//備註


				}
				else {
					System.out.println("investor="+investor + ", investNo="+investNo+", getCOMP_CHTNAME="+bean.getCOMP_CHTNAME());
					
					if(dTools.isEmpty(investor) || investor.equals("0")|| dTools.isEmpty(investNo)) {
						continue;
					}
				OFIInvestBaseData baseData = invest.get(investor, investNo); //[投資人,陸資案號,投資資料]
				OFIInvestBaseData moeaicData = moeaic_datas.get(investNo); //陸資公司的組織型態、發行方式
				List<OFIInvestorXRelated> parents = related.get(investor); //母公司資訊
				List<OFIInvestorXBG> bg = bgs.get(investor); //背景資料
				List<String> file = files.get(investor); //架構圖檔案ID
				
				ExcelUtil.createCell("str", column++, row, data_center, investNo); //陸資案號
				ExcelUtil.createCell("str", column++, row, data_left, baseData.getCOMP_CHTNAME()); //公司名稱
				ExcelUtil.createCell("str", column++, row, data_center, baseData.getID_NO()); //統一編號
				
				String orgTypeName = "";  
				String issue = "";
				if(moeaicData != null){
					orgTypeName = moeaicData.getOrgTypeName();
					issue = moeaicData.getISSUE_TYPE_NAME();
				}
				ExcelUtil.createCell("str", column++, row, data_center, orgTypeName.trim()); //組織型態
				ExcelUtil.createCell("str", column++, row, data_center, issue); //發行方式
				
				ExcelUtil.createCell("str", column++, row, data_center, baseData.getREGI_CAPITAL() == null ? "尚無資料" : ToolsUtil.parseNumToFinancial(baseData.getREGI_CAPITAL(), "#,###,###,###,###,##0")); //登記資本額
				String temp = "";
				if("分公司".equals(orgTypeName)){
					temp = "同登記資本額";
				}else{
					temp = bean.getPAID_CAPITAL() == null ? "尚無資料" : ToolsUtil.parseNumToFinancial(baseData.getPAID_CAPITAL(), "#,###,###,###,###,##0");
				}
				ExcelUtil.createCell("str", column++, row, data_center, temp); //實收資本額
				
				if(!"股份有限".equals(orgTypeName)){
					temp = "無須填寫";
				}else{
					temp = bean.getFACE_VALUE() == null ? "尚無資料" : ToolsUtil.parseNumToFinancial(baseData.getFACE_VALUE(), "#,###,###,###,###,##0.0#####");//面額
				}
				ExcelUtil.createCell("str", column++, row, data_center, temp); //面額

				ExcelUtil.createCell("str", column++, row, data_center, bean.getINVESTOR_CHTNAME()); //投資人
				ExcelUtil.createCell("str", column++, row, data_center, bean.isFilled()==true?"已確認":"未確認"); //資料狀態
				ExcelUtil.createCell("str", column++, row, data_center, bean.getCountry()); //投資人國別
				ExcelUtil.createCell("str", column++, row, data_center, bean.getCnCode()); //投資人省分
				ExcelUtil.createCell("str", column++, row, data_center, optmap.get("inSrc").get(bean.getInrole())); //資金類型

				ExcelUtil.createCell("str", column++, row, data_right, baseData.getInvestvalue() == null ? "" : 
							ToolsUtil.parseNumToFinancial(baseData.getInvestvalue(), "#,###,###,###,###,##0")); //投資金額
				ExcelUtil.createCell("str", column++, row, data_right, baseData.getInvestedcapital() == null ? "" : 
							ToolsUtil.parseNumToFinancial(baseData.getInvestedcapital(), "#,###,###,###,###,##0")); //持有股權
				ExcelUtil.createCell("str", column++, row, data_right, baseData.getSp() == null ? "" : 
							ToolsUtil.parseNumToFinancial(baseData.getSp(), "#,###,###,###,###,##0.00")+"%"); //股權比例
				
				//母公司欄位是取最多的數量，所以如果該筆母公司筆數不是最大數，要補足欄位
				if(parents != null){
					for(OFIInvestorXRelated p : parents){
						String con = optmap.get("nation").get(p.getNation());
						if(p.getCnCode() != null && p.getCnCode().trim().length() != 0){
							con = con + optmap.get("cnCode").get(p.getCnCode());
						}
						ExcelUtil.createCell("str", column++, row, data_left, p.getRelatedname()); //母公司（或關連企業）及受益人名稱
						ExcelUtil.createCell("str", column++, row, data_left, con); //母公司（或關連企業）及受益人國別
					}
					column = column + ((relatedLen - parents.size())*2); //目前欄位，補足母公司總欄位，就到下一起始格(因為前一個用完時都已經+1)
				}else{
					column = column + (relatedLen*2); //-1(開頭) 加16欄，加母公司欄位，就到下一起始格(因為前一個用完時都已經+1)
				}
				
				Map<String, StringBuilder> bgMap = null;
				if(bg != null){
					bgMap = new HashMap<>();
					bgMap.put("BG1", new StringBuilder());
					bgMap.put("BG1Note", new StringBuilder());
					bgMap.put("BG2", new StringBuilder());
					bgMap.put("BG2Note", new StringBuilder());
					for(OFIInvestorXBG b : bg){ //ListBG1 BG1Note BG2 BG2Note
						String type = b.getBgType();
						String val = b.getValue();
						if("BG1".equalsIgnoreCase(type) || "BG2".equalsIgnoreCase(type)){
							if(bgMap.get(type).length() > 0){
								bgMap.get(type).append("；");
							}
							bgMap.get(type).append(optmap.get(type).get(val));
						}else{
							bgMap.get(type).append(val);
						}
					}
				}
				ExcelUtil.createCell("str", column++, row, data_center, bgMap==null?"":bgMap.get("BG1").toString()); //背景1
				ExcelUtil.createCell("str", column++, row, data_left, bgMap==null?"":bgMap.get("BG1Note").toString()); //背景1-備註
				ExcelUtil.createCell("str", column++, row, data_center, bgMap==null?"":bgMap.get("BG2").toString()); //背景2
				ExcelUtil.createCell("str", column++, row, data_left, bgMap==null?"":bgMap.get("BG2Note").toString()); //背景2-備註
				
				temp = "無";
				if(file != null){
					temp = file.size()>0 ? "有":"無";
				}
				ExcelUtil.createCell("str", column++, row, data_center, temp); //架構圖
				ExcelUtil.createCell("str", column++, row, data_left, bean.getNote()); //備註
				
				
				}
				
				
			}
			
			
			
		/* 4.處理格式設定 =========================================================================================*/			
			
			/* 凍結列 int colSplit, int rowSplit：左側從第0開始切割，上方從地2列開始切割 （index從0開始） */
			sheet.createFreezePane(0, 1);
			/* 調整欄寬：取標題欄的欄位數，一個一個autoSize(速度比較慢)，再設定成autoSize後的兩倍寬（因為中文字是兩倍）*/
			for(int i = 0;i < header_row.getLastCellNum();i++){
				sheet.autoSizeColumn(i);
				//sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
				//sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i)*2, 30*256)); //欄寬1倍 或 欄寬60 取最小
				/* 或者不autoSize 直接設定欄位寬度 */
				sheet.setColumnWidth(i, 25*256); 
			}
			/* 標題設定列高 */
			header_row.setHeightInPoints((short)28);			
			/* 有中文字的檔名要先處理 */
			String h1 = "投資人";
			/* 輸出 設定瀏覽器不要cache舊資料 */
			ExcelUtil.setResponseNoCache(response);
//			response.setContentType("application/vnd.ms-excel; charset=utf-8"); //這種寫法在iphone會開啟失敗，改下方寫法
			response.setContentType("application/x-download; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" 
													+ DateUtil.dateToChangeROC(new Date(), "yyyyMMdd ", "CH")
													+ URLEncoder.encode(h1, "utf-8") + ".xlsx" + "\"");
			out = response.getOutputStream();
			wb.write(out);
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out!=null){
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}


	
}
