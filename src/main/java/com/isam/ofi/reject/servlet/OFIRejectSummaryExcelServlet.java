package com.isam.ofi.reject.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.isam.helper.DataUtil;
import com.isam.helper.POIUtil;
import com.isam.ofi.reject.service.OFIRejectService;
import com.isam.service.ofi.OFIInvestOptionService;

public class OFIRejectSummaryExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIRejectService rSer;
	private OFIInvestOptionService optSer;
	private POIUtil POIUtil;
	private Map<String,Map<String,String>> opt;
	private Map<String,String> detailTMap;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		rSer = new OFIRejectService();
		optSer = new OFIInvestOptionService();
		opt=optSer.select();
		POIUtil=new POIUtil();
		detailTMap=getDetailTitleMap();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		Map<String,String> info=new HashMap<String, String>();
		String MaxR=DataUtil.paramToTWYM(request.getParameter("MaxR"));
		String MinR=DataUtil.paramToTWYM(request.getParameter("MinR"));
		String MaxI=DataUtil.paramToTWYM(request.getParameter("MaxI"));
		String MinI=DataUtil.paramToTWYM(request.getParameter("MinI"));
		String[] rejectTypeTmp=request.getParameterValues("rejectType");
		String rejectType=DataUtil.nulltoempty(request.getParameter("rType"));
		if(rejectTypeTmp!=null){
			rejectType=DataUtil.fmtStrAryItem(rejectTypeTmp);
		}else if(!rejectType.isEmpty()){
			rejectType=DataUtil.fmtStrAryItem(rejectType.split(","));
		}
		Map<String,String> rmm=rSer.getMAXMINDay();
		int MinY=Integer.valueOf(MinI.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MinI")).substring(0, 3):MinI.substring(0, 3));
		int MaxY=Integer.valueOf(MaxI.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MaxI")).substring(0, 3):MaxI.substring(0, 3));
		List<String> tmp=new ArrayList<String>();
		for (int i = MinY; i <= MaxY; i++) {
			tmp.add(DataUtil.addZeroForNum(String.valueOf(i),3));
		}
		info.put("MaxR", DataUtil.addRigthNineForNum(MaxR,7));
		info.put("MinR", DataUtil.addRigthZeroForNum(MinR,7));
		info.put("MaxI", DataUtil.addRigthNineForNum(MaxI,7));
		info.put("MinI", DataUtil.addRigthZeroForNum(MinI,7));
		info.put("rejectType", rejectType.replaceAll("'", ""));
		info.put("yearPvt", DataUtil.fmtPvtItem(tmp));
		
		List<Map<String, String>> rCounts=rSer.getRejectsCounts(info);
		Map<String,List<List<String>>> context=rSer.getRejectContext(info);
		info.put("MaxR", MaxR.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MaxR")).substring(0, 6):DataUtil.addSlashToTWYM(MaxR));
		info.put("MinR", MinR.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MinR")).substring(0, 6):DataUtil.addSlashToTWYM(MinR));
		info.put("MaxI", MaxI.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MaxI")).substring(0, 6):DataUtil.addSlashToTWYM(MaxI));
		info.put("MinI", MinI.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MinI")).substring(0, 6):DataUtil.addSlashToTWYM(MinI));
		

		String dfilename =DataUtil.toTWDateStr(DataUtil.getNowTimestamp()).replaceAll("/", "")+"未核准資料.xlsx";
		Workbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		CellStyle cellCaption=POIUtil.cellStyleCaption(wb);
		CellStyle cellTh=POIUtil.cellStyleTH(wb);
		CellStyle cellTD=POIUtil.cellStyleTD(wb);
		CellStyle celldf=POIUtil.cellStyle(wb);
		fmtSummeryContext(wb, createHelper, rCounts,cellCaption,cellTh,cellTD,celldf);
		
		for (Entry<String, List<List<String>>> m:context.entrySet()) {
			createDetailSheet(wb,opt.get("rejectType").get(m.getKey()).split("\\(")[0],createHelper,m.getValue());
		}
		
		Sheet sheet = wb.createSheet("查詢條件");
		Row row = sheet.createRow((short)0);
		row.createCell(0).setCellValue("申請年月："+info.get("MinR")+"~"+info.get("MaxR"));
		row = sheet.createRow((short)1);
		row.createCell(0).setCellValue("發文年月："+info.get("MinI")+"~"+info.get("MaxI"));
		row = sheet.createRow((short)2);
		row.createCell(0).setCellValue("駁回類型：");
		int flag=3;
		for (String s:info.get("rejectType").split(",")) {
			row = sheet.createRow((short)flag);
			row.createCell(0).setCellValue(opt.get("rejectType").get(s));
			flag++;
		}
//		response.setContentType("application/vnd.openxmlformats;charset=UTF-8");
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(dfilename, "utf-8") + "\"");
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		wb.write(out);
		out.close();
	}
	private void createDetailSheet(Workbook wb,String sheetName,CreationHelper createHelper,List<List<String>> list){
		Sheet sheet = wb.createSheet(sheetName);
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		CellStyle styleP = wb.createCellStyle();
		styleP.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
		for(int i=0;i<list.size();i++){
			Row row = sheet.createRow((short)i);
			if(i==0){
				row.createCell(0).setCellValue("序號");
			}else{
				Cell cellx=row.createCell(0);
				cellx.setCellType(Cell.CELL_TYPE_NUMERIC);
				cellx.setCellValue(i);
			}
			for(int k=1;k<=list.get(i).size();k++){
				String str = list.get(i).get(k-1);
				Cell cell=row.createCell(k);
				if(i==0){
					String key=str.indexOf("_")!=-1?str.substring(0, str.indexOf("_")+1):str;
					String vAdd=str.indexOf("_")!=-1?"_"+str.split("_")[1]:"";
					if(detailTMap.containsKey(key)){
						cell.setCellValue(detailTMap.get(key)+vAdd);
					}
				}else{
					if(k==9){
					
						if(!row.getCell(8).getStringCellValue().equals("其他")) {
					
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							cell.setCellStyle(cellStyle);
							if(str.isEmpty()){
								cell.setCellValue("");
							}else{
								double x = DataUtil.SToD(str);
								cell.setCellValue(x);
							}
						}
						
					}
					else if(k==10){
						
						if(row.getCell(8).getStringCellValue().equals("其他")) {
							if(str.isEmpty()){
								cell.setCellValue("");
							}else{
								
								cell.setCellValue(str);
							}
						}
					
							
						
					}
					
					else if(k==11){
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellStyle(styleP);
						if(str.isEmpty()){
							cell.setCellValue("");
						}else{
							double x = DataUtil.SToD(str)/DataUtil.SToD("100");
							cell.setCellValue(x);
						}
					}else{
						cell.setCellValue(createHelper.createRichTextString(str));
					}
					
				}
			}
		}
		sheet.setColumnWidth(9, 20*256);
	}
	private Map<String,String> getDetailTitleMap(){
		Map<String,String> map=new HashMap<String, String>();
		map.put("rejectType","駁回類型");
		map.put("cName","國內事業名稱");
		map.put("isNew","設立情形");
		map.put("setupdate","設立日期");
		map.put("orgType","組織型態");
		map.put("receiveDate","申請日期");
		map.put("receiveNo","收文文號");
		map.put("currency","申請事項");
		map.put("money","申請投資金額");
		map.put("moneyother","其他事項");
		map.put("shareholding","持股比例");
		map.put("otherSic","其他業別項目");
		map.put("item","業別項目");
		map.put("issueDate","發文日期");
		map.put("issueNo","發文文號");
		map.put("decision","駁回決議");
		map.put("explanation","駁回決議說明");
		map.put("reason","駁回理由");
		map.put("note","備註");
		map.put("cApplicant_","申請人姓名或公司名稱");
		map.put("eApplicant_","申請人英文名稱");
		map.put("nation_","申請人國別");
		map.put("cnCode_","申請人省份");
		map.put("note_","申請人備註");
		map.put("agent_","代理人/職業");
		return map;
	}
	private void fmtSummeryContext(Workbook wb,CreationHelper createHelper,List<Map<String, String>> rCounts,CellStyle cellCaption,CellStyle cellTh,CellStyle cellTD,CellStyle celldf){
		Sheet sheet=wb.createSheet("案件統計");
		List<List<String>> list=fmtSummeryContext(rCounts);
		Row rTitle = sheet.createRow((short)0);
		rTitle.setHeightInPoints((short)35);
		Cell t1=rTitle.createCell(0);
		t1.setCellValue("陸 資 來 臺 投 資 駁 回 案 件 統 計 資 料 ");
		t1.setCellStyle(cellCaption);
		for(int i=0;i<list.size();i++){
			Row row = sheet.createRow((short)i+1);
			row.setHeightInPoints((short)35);
			for(int k=0;k<list.get(i).size();k++){
				String str = list.get(i).get(k);
				Cell cell=row.createCell(k);
				if(i>1&&k>1){
					cell.setCellStyle(cellTD);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(DataUtil.SToD(str));
				}else{
					if(i==list.size()-1 ){
						if(k==0){
							cell.setCellStyle(cellTD);
						}else{
							cell.setCellStyle(celldf);
						}
					}else if(i<=1||k==0){
						cell.setCellStyle(cellTh);
					}else{
						cell.setCellStyle(celldf);
					}
					cell.setCellValue(createHelper.createRichTextString(str));
				}
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, list.get(0).size()-1));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, list.get(0).size()-1));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(list.size(), list.size(), 0, 1));
		sheet.setColumnWidth(1, 80*256);
	}
	
	private List<List<String>> fmtSummeryContext(List<Map<String, String>> rCounts){
		List<List<String>> list=new ArrayList<List<String>>();
		Map<String,String> t=rCounts.get(0);
		List<String> title1=new ArrayList<String>();
		List<String> title=new ArrayList<String>();
		int c=0;
		for (Entry<String, String> m:t.entrySet()) {
			String name=m.getKey();
			if(DataUtil.isMatchFmtNumber(name)){
				title.add(Integer.valueOf(name)+"年度");
			}else{
				title.add("");
			}
			if(c==1){
				title1.add("類型");
			}else if(c==2){
				title1.add("件數");
			}else{
				title1.add("");
			}
			c++;
		}
		title.add("合計");
		title1.add("");
		list.add(title1);
		list.add(title);
		List<String> cont;
		Map<String,Integer> vSum=new LinkedHashMap<String, Integer>();
		for (int i = 0; i < rCounts.size(); i++) {
			Map<String,String> tmp=rCounts.get(i);
			cont=new ArrayList<String>();
			int j=0;
			int hSum=0;
			for (Entry<String, String> m:tmp.entrySet()) {
				String k= m.getKey();
				String val= m.getValue();
				if(j==0){
					String[] x=opt.get("rejectType").get(val).split("\\.");
					cont.add(x[0]);
					cont.add(x[1]);
				}else if(j!=1){
					val=val.isEmpty()?"0":val;
					int iVal =Integer.valueOf(val);
					hSum+=iVal;
					cont.add(val);
					vSum.put(k, vSum.containsKey(k)?vSum.get(k)+iVal:iVal);
					if(j==tmp.size()-1){
						cont.add(String.valueOf(hSum));
						vSum.put("vSum", vSum.containsKey("vSum")?vSum.get("vSum")+hSum:hSum);
						hSum=0;
					}
				}
				j++;
			}
			list.add(cont);
		}
		cont=new ArrayList<String>();
		cont.add("合計");
		cont.add("");
		for (Entry<String, Integer> m:vSum.entrySet()) {
			cont.add(String.valueOf(m.getValue()));
		}
		list.add(cont);
		return list;
	}
}
