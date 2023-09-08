package com.isam.servlet.ofi;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.bean.Interviewone;
import com.isam.bean.InterviewoneContent;
import com.isam.bean.InterviewoneItem;
import com.isam.bean.OFIInvestList;
import com.isam.bean.OFIInvestNoXFinancial;
import com.isam.bean.OFINTBTDatas;
import com.isam.bean.OFIPDFItem;
import com.isam.bean.OFIReInvestList;
import com.isam.helper.DataUtil;
import com.isam.helper.OrderUtil;
import com.isam.helper.PDFUtil;
import com.isam.service.InterviewoneContentService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneService;
import com.isam.service.ofi.OFIAllDataService;
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestNoXAuditService;
import com.isam.service.ofi.OFIInvestNoXFContentService;
import com.isam.service.ofi.OFIInvestNoXFinancialService;
import com.isam.service.ofi.OFIInvestNoXTWSICService;
import com.isam.service.ofi.OFINTBTDatasService;
import com.isam.service.ofi.OFIReInvestListService;
import com.isam.service.ofi.OFIReInvestNoXFContentService;
import com.isam.service.ofi.OFIReInvestNoXFinancialService;
import com.isam.service.ofi.OFIReInvestXTWSICService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
 
public class OFIInvestPDFServlet extends HttpServlet{
	private PDFUtil pf;
	private OFIInvestNoXTWSICService sicSer;
	private OFIReInvestXTWSICService reSic;
	private OFIInvestNoXAuditService auditSer;
	private OFIAllDataService baseSer;
	private FontSelector  styletitle;
	private FontSelector  styleName;
	private FontSelector  styleContent;
	private FontSelector  styleTop;
	private Map<String,List<OFIPDFItem>> items;
	private Map<String,String> sicMap;
	Map<String, String> nMap;
	/*訪視、財報*/
	private InterviewoneService ioSer;
	private InterviewoneContentService iocSer;
	private OFIReInvestListService reSer;
	private OFIInvestNoXFinancialService finSer;
	private OFIInvestNoXFContentService fcSer;
	private OFIReInvestNoXFinancialService rfinSer;
	private OFIReInvestNoXFContentService rfcSer;
	private OFINTBTDatasService NTBTser;
	Map<String, InterviewoneItem> frtOpt;
	private InterviewoneHelp opt;
	private FontSelector  styleName2;
	private FontSelector  styleContent2;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		baseSer=new OFIAllDataService();
		items=baseSer.getPDFItemMap();		
		sicSer=new OFIInvestNoXTWSICService();
		reSic=new OFIReInvestXTWSICService();
		auditSer=new OFIInvestNoXAuditService();
		sicMap=sicSer.getTWSICMap();
		pf=new PDFUtil();
		setFontSelector();
		setNameMap();
		/*訪視、財報*/
		ioSer = new InterviewoneService();
		iocSer = new InterviewoneContentService();
		reSer = new OFIReInvestListService();
		
		finSer = new OFIInvestNoXFinancialService(); 
		fcSer = new OFIInvestNoXFContentService();
		
		rfinSer = new OFIReInvestNoXFinancialService();
		rfcSer = new OFIReInvestNoXFContentService();
		
		NTBTser = new OFINTBTDatasService();
		opt = new InterviewoneHelp();
		frtOpt= opt.getqTypeF();
	}
	/** Inner class to add a header and a footer. */
    class HeaderFooter extends PdfPageEventHelper {
        int pagenumber;
        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
        }
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
			ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(14, String.format("%d", pagenumber), FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)),
                    (rect.getLeft()+rect.getRight())/2, rect.getBottom()-36, 0);
        }
    }
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String prxfix="";
		if(request.getServletPath().startsWith("/console")){
			 prxfix="/console";
		}
		// 接收資料
		String investNo = DataUtil.nulltoempty(request.getParameter("investNo"));
		List<String> pdfitems=DataUtil.StrArytoList(request.getParameterValues("pdfitem"));
		OFIInvestListService listSer=new OFIInvestListService();
		OFIInvestList bean=listSer.select(investNo);
		if(investNo.isEmpty()||bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+prxfix+"/cnfdi/listapproval.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		
		Map<String,String> sysinfo=baseSer.getPDFBaseData(investNo);
		String prefix=investNo.substring(0, 1).equals("4")?"（陸分）":"（陸）";
		String str=prefix+investNo;
		String cname=sysinfo.get("cname");
		
		Map<String,List<OFIPDFItem>> subitems=baseSer.getPDFSubItemMap();
		
		Document document = new Document(PageSize.A4,50,50,50,40);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String date = DataUtil.getStrUDate();
		String doctitle =cname+"相關資料";
		String dfilename =date.replace("-", "")+"_("+investNo+")"+doctitle+".pdf";
		try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			HeaderFooter event = new HeaderFooter();
		    writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		    writer.setPageEvent(event);
			//設定文件作者
			document.addAuthor("ISAMAdmin");
			//設定文件標題
			document.addTitle(dfilename.substring(0, dfilename.length()-4));
			//設定創建日期
			document.addCreationDate();
		    
		    //開啟文件
			document.open();
			
			Paragraph p=pf.getParagraph(doctitle, styletitle);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			document.add(getTopTable(str, date));
			int tNum=1;
			
			/*基本資料*/
			if(pdfitems.contains("0")){
				p=pf.getParagraph(OrderUtil.getOrderName(tNum)+"、"+nMap.get("0"), styleName);
				//p.setLeading(15,0);
				document.add(p);
				tNum++;
				document.add(getBriefTable(sysinfo));
			}
			float tFloat=2.5f;
			/*陸資投資人資訊 */
			if(pdfitems.contains("1")){
				int sNum=1;
				p=pf.getParagraph(OrderUtil.getOrderName(tNum)+"、"+nMap.get("1"), styleName);
				p.setLeading(0,tFloat);
				document.add(p);
				tNum++;
				List<OFIPDFItem> list=items.get("1");
				Map<String,Map<String,String>> investors=baseSer.getInvestorData(investNo);
				Map<String,List<List<String>>> relates=baseSer.getInvestorXRelatedData(investNo);
				Map<String,List<List<String>>> agents=baseSer.getPDFCNInvestorXAgentData(investNo);
				if(investors.isEmpty()){
					p=pf.getParagraph("無陸資投資人", styleContent);
					p.setLeading(0,2);
					p.setIndentationLeft(10f);
					document.add(p);
				}
				for (Entry<String, Map<String, String>> m:investors.entrySet()) {
					String investorSeq=m.getKey();
					Map<String, String> ibase=m.getValue();
					ibase.put("faceValue", sysinfo.get("faceValue"));
					p=pf.getParagraph("("+OrderUtil.getOrderName(sNum)+")"+ibase.get("investor"), styleName);
					p.setLeading(0,2);
					document.add(p);
					sNum++;
					for (int i = 0; i < list.size(); i++) {
						String iNo=list.get(i).getiNo();
						if(subitems.containsKey(iNo)){
							p=pf.getParagraph((i+1)+"."+list.get(i).getName(), styleContent);
							p.setLeading(20f,0);
							document.add(p);
							List<OFIPDFItem> subs=subitems.get(iNo);
							if(subs.get(0).getClassify().equals("1")){
								document.add(getInvestorTable(ibase,subs));
							}
							if(iNo.equals("32")){
								document.add(getInvestorRelatedListTable(relates.get(investorSeq),subs));
							}
							if(iNo.equals("35")){
								document.add(getInvestorAgentsListTable(agents.get(investorSeq),subs));
							}
						}else{
							String x=DataUtil.nulltoempty(ibase.get(list.get(i).getItem()));
							p=pf.getParagraph((i+1)+"."+list.get(i).getName()+(x.isEmpty()?"無":x), styleContent);
							p.setLeading(20f,0);
							document.add(p);
						}
						
					}
				}
			}
			
			/*稽核資訊 */
			if(pdfitems.contains("2")){
				p=pf.getParagraph(OrderUtil.getOrderName(tNum)+"、"+nMap.get("2"), styleName);
				p.setLeading(0,tFloat);
				document.add(p);
				tNum++;
				Map<String,String> map_first=auditSer.getAuditAll(investNo);
				
				/* 107-11-08 因為 07的重大投資案列表要做日期排序 ==================================== */
				//先把不是07的都先取出來
				Map<String,String> map = new HashMap<>();
				for(String code : map_first.keySet()) {
					String temp = "";
					if(code.indexOf("_") != -1) {
						temp = code.substring(code.indexOf("_")+1); //去除1_0701, 2_0701前面的底線跟數字
					}
					if(temp.startsWith("07") && temp.length()>2) {
						continue;
					}
					map.put(code, map_first.get(code));
				}
				//取出07的重大投資案列表
				Map<String,Map<String,String>> map07 = auditSer.getAudit07Details(investNo);
				Map<String,Map<String,String>> map07_new = new HashMap<>(); //先把key=seq改成用 key=0701
				ArrayList<String> templist = new ArrayList<>();
				
				for(String seq : map07.keySet()) {
					Map<String,String> temp = map07.get(seq);
					String date0701 = DataUtil.addZeroForNum(temp.get("0701"), 7); //檢查日期沒有左補0的補上
					temp.put("0701", date0701); 
					
					map07_new.put(date0701+"_"+temp.get("0702"), temp); //加文號是因為日期可能有相同的
					templist.add(date0701+"_"+temp.get("0702"));
				}
				
				String[] ary07 = templist.toArray(new String[0]);
//				Comparator<String> comp = Collections.reverseOrder(); //遞減排序
				Arrays.sort(ary07);
				for(int i=0; i<ary07.length; i++) {
					Map<String,String> teMap = map07_new.get(ary07[i]);
					map.put((i+1)+"_"+"0701", teMap.get("0701"));
					map.put((i+1)+"_"+"0702", teMap.get("0702"));
					map.put((i+1)+"_"+"0703", teMap.get("0703"));
				}
				
				Map<String,Map<String,String>> auditOpt=baseSer.getAuditCodeList(investNo);
				int check=0;
				if(!map.containsKey("0601")){
					map.put("0601", "暫無訪查資料 ");
				}else{
					check=map.get("0601").contains("-")?1:0;
				}
				if(!map.containsKey("0602")){
					map.put("0602", "暫無訪查資料 ");
				}else{
					check=map.get("0602").contains("-")?1:0;
				}
				Map<String,String> a02=auditOpt.get("02");
				Map<String,String> newa02=new LinkedHashMap<String,String>();
				if(a02!=null){
					for (Entry<String, String> m:a02.entrySet()) {
						String key=m.getKey();
						if(map.containsKey(key)){
							newa02.put(key, m.getValue());
						}
					}
					auditOpt.put("02", newa02);
				}
				Map<String,String> a07=auditOpt.get("07");
				Map<String,String> newa07=new LinkedHashMap<String,String>();
				if(a07!=null){
					for (Entry<String, String> m:a07.entrySet()) {
						String key=m.getKey();
						if(map.containsKey(key)){
							newa07.put(key, m.getValue());
						}
					}
					auditOpt.put("07", newa07);
				}
				List<OFIPDFItem> list=items.get("2");
				Map<String,String> tmp= new HashMap<String,String>();
				for (int i = 0; i < list.size(); i++) {
					OFIPDFItem item=list.get(i);
					String auditcode=item.getItem();
					String strTmp=auditcode.equals("06")?"未確認":"未填";
					tmp.put(auditcode, "("+OrderUtil.getOrderName(i+1)+")"+item.getName()+(map.get(auditcode)==null?strTmp:map.get(auditcode)));
				}
				int c=1;
				int c07=1;
				for (Entry<String, Map<String, String>> m:auditOpt.entrySet()) {
					String k=m.getKey();
					p=pf.getParagraph(tmp.get(k), styleName);
					p.setLeading(0,2);
					document.add(p);
					Map<String, String> sub=m.getValue();
					String a06=tmp.get(k);
					if(a06.endsWith("是")||(check==1&&k.startsWith("06"))||(a06.endsWith("未確認")&&k.startsWith("06"))){
						PdfPTable table = new PdfPTable(2);
						table.setWidths(new float[]{1,3});
						table.setWidthPercentage(100);
						table.setSpacingAfter(15f);
						table.setSpacingBefore(10f);
						PdfPCell cell;
						for (Entry<String, String> s:sub.entrySet()) {
							String code=s.getKey();
							String name=s.getValue();
							if(code.length()==2){
								continue;
							}
							if(code.endsWith("0201")){
								cell = pf.getPdfPCell(OrderUtil.getOrderName(c)+"、公文附加附款",styleContent);
								cell.setColspan(2);
								cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
								table.addCell(cell);
								c++;
							}
							if(code.endsWith("0701")){
								cell = pf.getPdfPCell(OrderUtil.getOrderName(c07)+"、委員會核准之重大投資案",styleContent);
								cell.setColspan(2);
								cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
								table.addCell(cell);
								c07++;
							}
							cell = pf.getPdfPCell(name+"：",styleContent,Element.ALIGN_RIGHT);
							table.addCell(cell);
							cell = pf.getPdfPCell(map.get(code),styleContent);
							table.addCell(cell);
						}
						document.add(table);
					}
				}
				
			}
			
			/*國內轉投資*/
			if(pdfitems.contains("3")){
				p=pf.getParagraph(OrderUtil.getOrderName(tNum)+"、"+nMap.get("3"), styleName);
				p.setLeading(0,tFloat);
				document.add(p);
				tNum++;
				List<Map<String,String>> list=baseSer.getReInvestmentBase(investNo);
				for (int i = 0; i < list.size(); i++) {
					Map<String,String> data=list.get(i);
					p=pf.getParagraph("("+OrderUtil.getOrderName(i+1)+")"+data.get("reinvestment"), styleName);
					p.setLeading(0,2);
					document.add(p);
					document.add(getReInvestmentTable(data));
				}
			}
			
			/*聯絡資訊*/
			if(pdfitems.contains("4")){
				p=pf.getParagraph(OrderUtil.getOrderName(tNum)+"、"+nMap.get("4"), styleName);
				p.setLeading(0,tFloat);
				document.add(p);
				tNum++;
				sysinfo.putAll(baseSer.getPDFContactData(investNo));
				document.add(getContactsTable(sysinfo));
			}
			
			
			
			/*訪查資料 -- 如本公司未訪，轉投資已訪，本公司就顯示未訪查*/
			if(pdfitems.contains("5")){
				/* 1.標題  =====================================================================================*/
				p=pf.getParagraph(OrderUtil.getOrderName(tNum)+"、"+nMap.get("5"), styleName);
				p.setLeading(0,tFloat);
				document.add(p);
				tNum++;
				
				/* 2.取資料  =====================================================================================*/
				//本公司 ＆ 轉投資公司 訪視狀態 [0或轉投資id , LIST[訪視狀態資料]]
				Map<String, List<Interviewone>> singlelist = ioSer.selectByInvestNo(investNo); 
				List<OFIReInvestList> OFIReInvestList = reSer.select(investNo);
				
				
				if(singlelist.isEmpty() && OFIReInvestList.isEmpty()){ //兩個都空，就顯示未訪查
					p=pf.getParagraph("　　尚未訪查", pf.getFontSelector(14, Font.NORMAL));
					p.setLeading(0, 1.5f);
					document.add(p);
				}else{
					
					//公司＆轉投資名稱
					Map<String, String> comName = new TreeMap<>();
					comName.put("0", cname);
					for(OFIReInvestList OFIReInvest : OFIReInvestList){ //轉投資公司資料
						comName.put(OFIReInvest.getReInvestNo(), "(轉投資)"+OFIReInvest.getReinvestment());
					}
					//訪視情形、問卷情形
					InterviewoneHelp h = new InterviewoneHelp();
					Map<String, Map<String, String>> ioOpt = h.getOptionValName();
					Map<String, String> itv_StatusS = ioOpt.get("interviewStatus");
					Map<String, String> fina_StatusS = ioOpt.get("surveyStatus");
					//訪視異常列表
					List<String> itvErr = iocSer.getInterviewErrorList();
					//財務異常列表
					List<String> finaErr = iocSer.getFinancialErrorList();
					
				
				/* 3.定標題  =====================================================================================*/
					String[] headers = {"序號","調查年度","訪視情形","問卷繳交","訪視異常","財務異常"};
					
					PdfPTable table = new PdfPTable(headers.length);
					table.setWidthPercentage(100);
					table.setSpacingAfter(15f);
					table.setSpacingBefore(10f);
					try {
						table.setWidths(new float[]{1, 1.6f, 1.2f, 1.2f, 1, 1});
					} catch (DocumentException e) {
						e.printStackTrace();
					}
					
					//標題列
					PdfPCell cell;
					for (int i = 0; i < headers.length; i++) {
						cell = pf.getPdfPCell(headers[i], styleContent, Element.ALIGN_CENTER);
						cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
						table.addCell(cell);
					}
					
				/* 4.表格內容  ====================================================================================*/
					
					for(String key : comName.keySet()){ //依照0,轉投資公司依序在singlelist取資料，如果沒有就顯示尚未訪查
						
						//一、公司列
						cell = pf.getPdfPCell(comName.get(key), styleContent);
						cell.setColspan(headers.length); //合併儲存格
						cell.setBackgroundColor(new BaseColor(224, 242, 247));
						table.addCell(cell); 
						
						
						//二、資料列
						List<Interviewone> list = singlelist.get(key);
						if(list == null){
							cell = pf.getPdfPCell("尚未訪查", styleContent);
							cell.setColspan(headers.length); //合併儲存格
							table.addCell(cell); 
							
						}else{
							
							for(int b=0; b<list.size(); b++){
								
								Interviewone itvone = list.get(b);
								String qNo = String.valueOf(itvone.getqNo());
								//1.序號
								cell = pf.getPdfPCell("("+String.valueOf(b+1)+")", styleContent, Element.ALIGN_CENTER);
								table.addCell(cell);
								//2.調查年度
								cell = pf.getPdfPCell(itvone.getYear(), styleContent, Element.ALIGN_CENTER);
								table.addCell(cell);
								//3.訪視情形
								String itv_Status = itvone.getInterviewStatus();
								cell = pf.getPdfPCell(itv_StatusS.get(itv_Status), styleContent, Element.ALIGN_CENTER);
								table.addCell(cell);
								//4.問卷繳交
								String fina_Status = itvone.getSurveyStatus();
								cell = pf.getPdfPCell(fina_StatusS.get(fina_Status), styleContent, Element.ALIGN_CENTER);
								table.addCell(cell);
								
								
								//5.訪視異常
								String itv_situation = "";
								if(!itvErr.contains(qNo) && "1".equalsIgnoreCase(itv_Status)){ //無異常 已訪視
									itv_situation = "正常";
								}else if(!itvErr.contains(qNo) && "0".equalsIgnoreCase(itv_Status)){ //無異常 待訪視
									itv_situation = "";
								}else if(!itvErr.contains(qNo) && "9".equalsIgnoreCase(itv_Status)){ //無異常 未訪視
									itv_situation = "";
								}else if(itvErr.contains(qNo) && "1".equalsIgnoreCase(itv_Status)){ //有異常 已訪視
									itv_situation = "異常";
								}else if(itvErr.contains(qNo) && "9".equalsIgnoreCase(itv_Status)){ //有異常 未訪視
									itv_situation = "異常";
								}
								cell = pf.getPdfPCell(itv_situation, styleContent, Element.ALIGN_CENTER);
								table.addCell(cell);
								
								
								//6.財務異常
								String fina_situation = "";
								if(!finaErr.contains(qNo) && "1".equalsIgnoreCase(fina_Status)){ //無異常 已繳回
									fina_situation = "正常";
								}else if(!finaErr.contains(qNo) && "0".equalsIgnoreCase(fina_Status)){ //無異常 未繳回
									fina_situation = "";
								}else if(finaErr.contains(qNo) && "1".equalsIgnoreCase(fina_Status)){ //有異常 已繳回
									fina_situation = "異常";
								}else if(finaErr.contains(qNo) && "0".equalsIgnoreCase(fina_Status)){ //有異常 未繳回
									fina_situation = "異常";
								}								
								cell = pf.getPdfPCell(fina_situation, styleContent, Element.ALIGN_CENTER);
								table.addCell(cell);
								
								
								//四、訪視異常列
								if("異常".equals(itv_situation)){
									//1.序號
									cell = pf.getPdfPCell("", styleContent, Element.ALIGN_CENTER);
									table.addCell(cell);
									//2.異常項目
									cell = pf.getPdfPCell("訪視異常原因", styleContent, Element.ALIGN_CENTER);
									table.addCell(cell);
									//3.異常內容（合併儲存格4格）
									StringBuilder reason = new StringBuilder(); 
									int count = 1;
									for(InterviewoneContent error : iocSer.getInterviewError(qNo)){
										
										if(reason.length() > 0){
											reason.append("\r\n");
										}
										reason.append(count+". "+error.getOptionName()+"："+error.getValue());
										++count;
									}
									cell = pf.getPdfPCell(reason.toString(), styleContent, Element.ALIGN_LEFT);
									cell.setColspan(4);
									cell.setPaddingLeft(3);
									table.addCell(cell);
								}
								
								
								//五、財務異常列
								if("異常".equals(fina_situation)){
									//1.序號
									cell = pf.getPdfPCell("", styleContent, Element.ALIGN_CENTER);
									table.addCell(cell);
									//2.異常項目
									cell = pf.getPdfPCell("財務異常原因", styleContent, Element.ALIGN_CENTER);
									table.addCell(cell);
									//3.異常內容（合併儲存格4格）
									StringBuilder reason = new StringBuilder(); 
									int count = 1;
									for(InterviewoneContent error : iocSer.getFinancialError(qNo)){
										
										if(reason.length() > 0){
											reason.append("\r\n");
										}
										reason.append(count+". "+error.getOptionName()+"："+error.getValue());
										++count;
									}
									cell = pf.getPdfPCell(reason.toString(), styleContent, Element.ALIGN_LEFT);
									cell.setColspan(4);
									cell.setPaddingLeft(3);
									table.addCell(cell);
								}							
							}
						} //end if 三、資料列 
					} //end for
					document.add(table);
				}
			}
			
			
			
			
			
			/*財務資料*/
			if(pdfitems.contains("6")){
				
				/* 1.標題  =====================================================================================*/
				p=pf.getParagraph(OrderUtil.getOrderName(tNum)+"、"+nMap.get("6"), styleName);
				p.setLeading(0,tFloat);
				document.add(p);
				tNum++;
				

				/* 2.取資料（財報申報情形）  ========================================================================*/
				List<OFIInvestNoXFinancial> financial = finSer.select(investNo,"0"); //公司財報
				List<OFIReInvestList> reInvests=reSer.select(investNo); //轉投資公司
				Map<String,List<OFIInvestNoXFinancial>> refinancial = rfinSer.select(investNo);  //轉投資公司財報
				
				if(financial.isEmpty() && refinancial.isEmpty()){
					p = pf.getParagraph("(一)財報申請情形：尚無資料", styleName);
					p.setLeading(0,tFloat);
					document.add(p);
					
				}else{
					p = pf.getParagraph("(一)財報申請情形：", styleName);
					p.setLeading(0,tFloat);
					document.add(p);
					
					
				/* 3.表格內容  ====================================================================================
				 *  因為前面已經判斷過 本公司財報 ＆ 轉投資財報，已經有一個不是空的了，所以進到這裡就一定顯示財報，沒有就寫沒有資料。
				 *  轉投資公司財報，先判斷有沒有轉投資公司，若有轉投資公司，就一一列出，沒有的也寫沒有資料。 */
					
					/* 一、本公司：標題    [文筆網路科技有限公司(099/09/13成立)] [文筆網路科技有限公司(新設成立)]
					 * 判斷法1：  isNew 是新設就顯示；但，若遇到 setupdate 有資料就改顯示日期；也有機會空白（不是新社 且 沒有設立日期）
					 * 判斷法2：  如果header 有內容，就 公司名 + 包括弧 + 成立。
					 */
					
					int company_num = 1;
					String header = "";
					if("新設".equals(sysinfo.get("isNew"))){  //是否新設立？
						header = sysinfo.get("isNew");
					}
					if(sysinfo.get("setupdate").trim().length() != 0){  //是否有設立日期？
						header = sysinfo.get("setupdate");
						
					}
					if(header.trim().length() > 0){
						header = cname + "(" + header + "成立)";
					}
					
					
					if(financial.isEmpty()){
						p = pf.getParagraph(company_num + "." + header + ":尚無資料", styleName);
						p.setLeading(0,tFloat);
						document.add(p);
						++company_num; //財報公司報數
					}else{
						p = pf.getParagraph(company_num + "." + header, styleName);
						p.setLeading(0,tFloat);
						document.add(p);	
						++company_num; //財報公司報數
						
					//二、本公司：內容
						int count = 1;
						for(OFIInvestNoXFinancial fbean : financial){
							
							String lastyear=DataUtil.addZeroForNum(String.valueOf(Integer.valueOf(fbean.getReportyear())-1),3);
							OFIInvestNoXFinancial fbeanlast=finSer.selectbean(investNo, lastyear, "0");
							Map<String, String> fcbean=fcSer.selectBySerno(fbean.getSerno());
							Map<String, String> fcbeanlast = null;
							if(fbeanlast!=null){
								fcbeanlast=fcSer.selectBySerno(fbeanlast.getSerno());
							}
							
							document.add(getTopTable2("("+count+"). "+fbean.getReportyear()+"年度（填報日期："+DataUtil.addSlashToTWDate(fbean.getReportdate())+"）"));
							document.add(getFSTable(fbean,fcbean, fcbeanlast,fbean.getReportyear(), lastyear));
							++count;
						}
						
						
					} //end if financial not empty
					
					
					//三、轉投資
					if(reInvests != null && !reInvests.isEmpty()){
						//轉投資名稱
//						Map<String, String> comName = new TreeMap<>();
						for(OFIReInvestList OFIReInvest : reInvests){ //轉投資公司資料
							
							/* 轉投資標題    [文筆網路科技有限公司(099/09/13成立)] [文筆網路科技有限公司(新設成立)]
							 * 判斷法1：  isNew 是新設就顯示；但，若遇到 setupdate 有資料就改顯示日期；也有機會空白（不是新社 且 沒有設立日期）
							 * 判斷法2：  如果header 有內容，就 公司名 + 包括弧 + 成立。
							 */
							
							header = "";
							if("2".equals(OFIReInvest.getIsNew())){  //是否新設立？
								header = "新設";
							}
							if(OFIReInvest.getSetupdate().trim().length() != 0){  //是否有設立日期？
								header = DataUtil.addSlashToTWDate(OFIReInvest.getSetupdate());
							}
							if(header.trim().length() > 0){
								header = "(轉投資)"+OFIReInvest.getReinvestment() + "(" + header + "成立)";
							}
							
//							comName.put(OFIReInvest.getReInvestNo(), "(轉投資)"+OFIReInvest.getReinvestment());
							
							List<OFIInvestNoXFinancial> refinancial_one = refinancial.get(OFIReInvest.getReInvestNo()); //取出一轉投資公司所有財報資料
							if(refinancial_one == null || refinancial_one.isEmpty()){
								p = pf.getParagraph(company_num + "." + header + ":尚無資料", styleName);
								p.setLeading(0,tFloat);
								document.add(p);
								++company_num; //財報公司報數
							}else{
								p = pf.getParagraph(company_num + "." + header, styleName);
								p.setLeading(0,tFloat);
								document.add(p);	
								++company_num; //財報公司報數
								
								
							//二、轉投資：內容
								int count = 1;
								for(OFIInvestNoXFinancial fbean : refinancial_one){

									String reinvestNo = fbean.getInvestNo();
									String lastyear=DataUtil.addZeroForNum(String.valueOf(Integer.valueOf(fbean.getReportyear())-1),3);
									OFIInvestNoXFinancial fbeanlast=rfinSer.selectbean(reinvestNo, lastyear);
									Map<String, String> fcbean=rfcSer.selectBySerno(fbean.getSerno());
									Map<String, String> fcbeanlast = null;
									if(fbeanlast!=null){
										fcbeanlast=rfcSer.selectBySerno(fbeanlast.getSerno());
									}
									
									document.add(getTopTable2("("+count+"). "+fbean.getReportyear()+"年度（填報日期："+DataUtil.addSlashToTWDate(fbean.getReportdate())+"）"));
									document.add(getFSTable(fbean,fcbean, fcbeanlast,fbean.getReportyear(), lastyear));
									++count;
								}
								
								
							} //end if financial not empty
						} // end for 轉投資公司
					}//end if轉投資LIST不是空
				} //end if 有內容
				
				
				
				
				
				
				
				
				/* 國稅局 ==========================================================================================*/
				
				
				/* 1.取資料（財務資料下載）  ========================================================================*/	
				ArrayList<OFINTBTDatas> NTBTS = NTBTser.list(investNo);
				if(NTBTS == null || NTBTS.isEmpty()){
					p = pf.getParagraph("(二)財務資料下載：尚無資料", styleName);
					p.setLeading(0,tFloat);
					document.add(p);
					
				}else{
					p = pf.getParagraph("(二)財務資料下載：", styleName);
					p.setLeading(0,tFloat);
					document.add(p);
					
					
				/* 2.定標題（財務資料下載）  =========================================================================*/
					String[] headers = {"序號","說明","建立日期"};
					
					PdfPTable table = new PdfPTable(headers.length);
					table.setWidthPercentage(100);
					table.setSpacingAfter(15f);
					table.setSpacingBefore(10f);
					try {
						table.setWidths(new float[]{0.8f, 2, 1.3f});
					} catch (DocumentException e) {
						e.printStackTrace();
					}				
				
					//標題列
					PdfPCell cell;
					for (int i = 0; i < headers.length; i++) {
						cell = pf.getPdfPCell(headers[i], styleContent, Element.ALIGN_CENTER);
						cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
						table.addCell(cell);
					}
					
				/* 3.表格內容(財務資料下載)  =========================================================================*/
					
					int b = 0;
					for(OFINTBTDatas NTBTbean : NTBTS){
						//1.序號
						cell = pf.getPdfPCell("("+String.valueOf(b+1)+")", styleContent, Element.ALIGN_CENTER);
						table.addCell(cell);
						//2.說明
						cell = pf.getPdfPCell(NTBTbean.getTitle(), styleContent, Element.ALIGN_CENTER);
						table.addCell(cell);
						//3.建立日期
						cell = pf.getPdfPCell(NTBTbean.getCreatetime_ROC(), styleContent, Element.ALIGN_CENTER);
						table.addCell(cell);
						++b;
					}					
					
					document.add(table);
				}//end if 國稅局有資料
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally {
			document.close();
		}
		
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(dfilename, "utf-8") + "\"");
		response.setContentType("application/pdf");
		response.setContentLength(baos.size());
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		baos.writeTo(out);
		out.flush();
		out.close();
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	private PdfPTable getTopTable(String investNo,String date) {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(20f);
		PdfPCell cell;
		cell = pf.getPdfPCell("案號："+investNo,styleTop);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setFixedHeight(25f);
		cell.setBorder(0);
		table.addCell(cell);
		cell = pf.getPdfPCell("下載日期："+date,styleTop);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setFixedHeight(25f);
		cell.setBorder(0);
		table.addCell(cell);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setSpacingAfter(10f);
		return table;
	}
	private PdfPTable getBriefTable(Map<String,String> sysinfo) {
		Map<String,List<String>> sics=sicSer.select(sysinfo.get("investNo"));
		List<OFIPDFItem> list=items.get("0");
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		PdfPCell cell;
		try {
			table.setWidths(new float[]{1,2.5f});
			for (int i = 0; i < list.size(); i++) {
				OFIPDFItem bean=list.get(i);
				String t=bean.getName();
				String txt=sysinfo.get(bean.getItem());
				if(bean.getColspan()==0){
					cell = pf.getPdfPCell(t,styleContent,Element.ALIGN_RIGHT);
					table.addCell(cell);
					cell = pf.getPdfPCell(txt,styleContent);
					table.addCell(cell);
				}else{
					cell = pf.getPdfPCell(t,styleContent);
					cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					cell.setColspan(bean.getColspan());
					table.addCell(cell);
					List<String> slist=sics.get(bean.getItem());
					if(slist==null||slist.isEmpty()){
						cell = pf.getPdfPCell("尚無資料",styleContent);
						cell.setColspan(bean.getColspan());
						table.addCell(cell);
					}else{
						for (int j = 0; j < slist.size(); j++) {
							cell = pf.getPdfPCell(slist.get(j),styleContent,Element.ALIGN_RIGHT);
							table.addCell(cell);
							cell = pf.getPdfPCell(sicMap.get(slist.get(j)),styleContent);
							table.addCell(cell);
						}
					}
				}
			}
			table.setSpacingAfter(15f);
			table.setSpacingBefore(10f);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return table;
	}
	private PdfPTable getContactsTable(Map<String,String> sysinfo) {
		List<OFIPDFItem> list=items.get("4");
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingAfter(15f);
		table.setSpacingBefore(20f);
		PdfPCell cell;
		try {
			table.setWidths(new float[]{1,3});
			for (int i = 0; i < list.size(); i++) {
				OFIPDFItem bean=list.get(i);
				String t=bean.getName();
				String txt=sysinfo.get(bean.getItem());
				cell = pf.getPdfPCell(t,styleContent,Element.ALIGN_RIGHT);
				table.addCell(cell);
				cell = pf.getPdfPCell(txt,styleContent);
				table.addCell(cell);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return table;
	}
	private PdfPTable getInvestorTable(Map<String,String> sysinfo,List<OFIPDFItem> list) {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingAfter(15f);
		table.setSpacingBefore(10f);
		PdfPCell cell;
		try {
			table.setWidths(new float[]{1,3});
			for (int i = 0; i < list.size(); i++) {
				OFIPDFItem bean=list.get(i);
				String t=bean.getName();
				String txt=sysinfo.get(bean.getItem());
				if((bean.getItem().equals("BG1")||bean.getItem().equals("BG2"))&&txt==null){
					txt="未填";
				}
				cell = pf.getPdfPCell(t,styleContent,Element.ALIGN_RIGHT);
				table.addCell(cell);
				cell = pf.getPdfPCell(txt,styleContent);
				table.addCell(cell);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return table;
	}
	private PdfPTable getInvestorRelatedListTable(List<List<String>> data,List<OFIPDFItem> list) {
		PdfPTable table = new PdfPTable(list.size());
		table.setWidthPercentage(100);
		table.setSpacingAfter(15f);
		table.setSpacingBefore(10f);
		try {
			table.setWidths(new float[]{1,5,2.5f});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		PdfPCell cell;
		for (int i = 0; i < list.size(); i++) {
			OFIPDFItem bean=list.get(i);
			String t=bean.getName();
			cell = pf.getPdfPCell(t,styleContent,Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell);
		}
		if(data!=null&&data.size()>0){
			for (int i = 0; i < data.size(); i++) {
				List<String> content=data.get(i);
				for (int j = 0; j < content.size(); j++) {
					if(j==0){
						cell = pf.getPdfPCell("("+String.valueOf(i+1)+")",styleContent,Element.ALIGN_CENTER);
						table.addCell(cell);
					}
					cell = pf.getPdfPCell(content.get(j),styleContent);
					table.addCell(cell);
				}
			
			}
		}else{
			cell = pf.getPdfPCell("尚無資料",styleContent);
			cell.setColspan(list.size());
			table.addCell(cell);
		}
		return table;
	}
	private PdfPTable getInvestorAgentsListTable(List<List<String>> data,List<OFIPDFItem> list) {
		PdfPTable table = new PdfPTable(list.size());
		table.setWidthPercentage(100);
		table.setSpacingAfter(15f);
		table.setSpacingBefore(10f);
		try {
			table.setWidths(new float[]{1,1.3f,1.7f,2.5f,1.5f,2.5f});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		PdfPCell cell;
		for (int i = 0; i < list.size(); i++) {
			OFIPDFItem bean=list.get(i);
			String t=bean.getName();
			cell = pf.getPdfPCell(t,styleContent,Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell);
		}
		if(data!=null&&data.size()>0){
			for (int i = 0; i < data.size(); i++) {
				List<String> content=data.get(i);
				for (int j = 0; j < content.size(); j++) {
					if(j==0){
						cell = pf.getPdfPCell("("+String.valueOf(i+1)+")",styleContent,Element.ALIGN_CENTER);
						table.addCell(cell);
					}
					cell = pf.getPdfPCell(content.get(j),styleContent);
					if(j<2){
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					}
					table.addCell(cell);
				}	
			}
		}else{
			cell = pf.getPdfPCell("尚無資料",styleContent);
			cell.setColspan(list.size());
			table.addCell(cell);
		}
		return table;
	}
	private PdfPTable getReInvestmentTable(Map<String,String> sysinfo) {
		Map<String,List<String>> sics=reSic.getReInvestXTWSIC(sysinfo.get("reInvestNo"));
		List<OFIPDFItem> list=items.get("3");
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		PdfPCell cell;
		try {
			table.setWidths(new float[]{1,3});
			for (int i = 0; i < list.size(); i++) {
				OFIPDFItem bean=list.get(i);
				String t=bean.getName();
				String txt=sysinfo.get(bean.getItem());
				if(bean.getColspan()==0){
					cell = pf.getPdfPCell(t,styleContent,Element.ALIGN_RIGHT);
					table.addCell(cell);
					cell = pf.getPdfPCell(txt,styleContent);
					table.addCell(cell);
				}else{
					cell = pf.getPdfPCell(t,styleContent);
					cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					cell.setColspan(bean.getColspan());
					table.addCell(cell);
					List<String> slist=sics.get(bean.getItem());
					if(slist==null||slist.isEmpty()){
						cell = pf.getPdfPCell("尚無資料",styleContent);
						cell.setColspan(bean.getColspan());
						table.addCell(cell);
					}else{
						for (int j = 0; j < slist.size(); j++) {
							cell = pf.getPdfPCell(slist.get(j),styleContent,Element.ALIGN_RIGHT);
							table.addCell(cell);
							cell = pf.getPdfPCell(sicMap.get(slist.get(j)),styleContent);
							table.addCell(cell);
						}
					}
				}
			}
			table.setSpacingAfter(15f);
			table.setSpacingBefore(10f);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return table;
	}
	
	
	/*本公司財報-----------------------------------------------------------------------------------------*/
	private PdfPTable getFSTable(OFIInvestNoXFinancial fbean,Map<String, String> fcbean,Map<String, String> fcbeanlast,String year,String lastyear) {
		PdfPTable table=new PdfPTable(9);
		table = setFSTableTitleLayout(getFSTableTitle(table,year,lastyear),2);
		table.setWidthPercentage(100);
		table.setSpacingAfter(5f);
		table = setFSTableContentLayout(getFSTableContent(table,fbean, fcbean, fcbeanlast),2);
		try {
			table.setWidths(new float[]{1.1f,1.0f,1.0f,0.7f,1.2f,1.0f,1.0f,0.8f,0.7f});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return table;
	}
	private PdfPTable setFSTableTitleLayout(PdfPTable table,int header) {
		for (int i = 0; i < header; i++) {
			for (PdfPCell cell:table.getRow(i).getCells()) {
				if(cell!=null){
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setPaddingBottom(5f);
					cell.setBackgroundColor(new BaseColor(243, 243, 243));
				}
			}
		}
		return table;
	}
	private PdfPTable setFSTableContentLayout(PdfPTable table,int header) {
		List<PdfPRow> list=table.getRows();
		for (int i = header; i < list.size(); i++) {
			PdfPCell[] cells=table.getRow(i).getCells();
			for (int j = 0; j < cells.length; j++) {
				PdfPCell cell=cells[j];
				if(cell!=null){
					if(i<6){
						if(j==0||j==4){
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						}else{
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setPaddingRight(5f);
						}
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setPaddingBottom(5f);
					}else if(i== list.size()-1){
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setPaddingBottom(5f);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						cell.setBorder(0);
					}else if(i==7){ //財簽保留意見 靠左
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setPaddingTop(2.5f);
						cell.setPaddingBottom(5f);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						cell.setBorder(0);
					}else{
						if(j==0||j==5){
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						}else{
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						}
						cell.setPaddingTop(2.5f);
						if(i==6){
							cell.setPaddingTop(15f);
						}
						cell.setPaddingBottom(5f);
						cell.setVerticalAlignment(Element.ALIGN_TOP);
						cell.setBorder(0);
					}

				}
			}
		}
		return table;
	}
	private PdfPTable getFSTableContent(PdfPTable table,OFIInvestNoXFinancial fbean,Map<String, String> fcbean,Map<String, String> fcbeanlast) {
		String idown= DataUtil.nulltoempty(fcbean.get("61"));
		for (int i = 58; i <= 60; i++) {
			String k1=String.valueOf(i);
			String k2=String.valueOf(i+3);
			table.addCell(pf.getPdfPCell(frtOpt.get(k1).getcName(),styleContent2));
			table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbeanlast.get(k1)),styleContent2));
			table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get(k1)),styleContent2));
			table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getGrowthRate(DataUtil.nulltoempty(fcbean.get(k1)), DataUtil.nulltoempty(fcbeanlast.get(k1)))),styleContent2));
			table.addCell(pf.getPdfPCell(frtOpt.get(k2).getcName(),styleContent2));
			table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbeanlast.get(k2)),styleContent2));
			table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get(k2)),styleContent2));
			table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getPercent(DataUtil.nulltoempty(fcbean.get(k2)), idown)),styleContent2));
			table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getGrowthRate(DataUtil.nulltoempty(fcbean.get(k2)), DataUtil.nulltoempty(fcbeanlast.get(k2)))),styleContent2));
		}
		PdfPCell cell;
		cell =pf.getPdfPCell("", styleContent2);
		cell.setColspan(4);
		table.addCell(cell);
		table.addCell(pf.getPdfPCell(frtOpt.get("64").getcName(),styleContent2));
		table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbeanlast.get("64")),styleContent2));
		table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get("64")),styleContent2));
		table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getPercent(DataUtil.nulltoempty(fcbean.get("64")), idown)),styleContent2));
		table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getGrowthRate(DataUtil.nulltoempty(fcbean.get("64")), DataUtil.nulltoempty(fcbeanlast.get("64")))),styleContent2));
		
		table.addCell(pf.getPdfPCell(frtOpt.get("65").getcName()+"：",styleContent2)); //事務所
		cell=pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get("65")),styleContent2);
		cell.setColspan(4);
		table.addCell(cell);
		
		table.addCell(pf.getPdfPCell(frtOpt.get("66").getcName()+"：",styleContent2)); //會計師
		cell=pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get("66")),styleContent2);
		cell.setColspan(3);
		table.addCell(cell);
		
		//財簽保留意見，改合併所有儲存格
		cell=pf.getPdfPCell(frtOpt.get("67").getcName()+"：" + DataUtil.nulltoempty(fcbean.get("67")), styleContent2);
		cell.setColspan(9);
		table.addCell(cell);
//		table.addCell(pf.getPdfPCell(frtOpt.get("67").getcName()+"：",styleContent2)); 
//		cell=pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get("67")),styleContent2);
//		cell.setColspan(8);
//		table.addCell(cell);
		
		table.addCell(pf.getPdfPCell("備註：",styleContent2));
		cell=pf.getPdfPCell(DataUtil.nulltoempty(fbean.getNote()),styleContent2);
		cell.setColspan(8);
		table.addCell(cell);
		
		StringBuilder sb=new StringBuilder();
		double tt=DataUtil.toDouble(fcbean.get("74"))+DataUtil.toDouble(fcbean.get("75"))+DataUtil.toDouble(fcbean.get("76"));
		if(fcbean.get("74")==null&&fcbean.get("75")==null&&fcbean.get("76")==null){
			sb.append("國內事業員工人數（含派駐管理職）：共計").append("--").append("人（其中");
		}else{
			sb.append("國內事業員工人數（含派駐管理職）：共計").append(DataUtil.formatIntString(tt)).append("人（其中");
		}
		sb.append(frtOpt.get("74").getcName()).append(fcbean.get("74")==null?"--":fcbean.get("74")).append("人、");
		sb.append(frtOpt.get("75").getcName()).append(fcbean.get("75")==null?"--":fcbean.get("75")).append("人、");
		sb.append(frtOpt.get("76").getcName()).append(fcbean.get("76")==null?"--":fcbean.get("76")).append("人）。");
		cell=pf.getPdfPCell(sb.toString(),styleContent2);
		sb.setLength(0);
		cell.setColspan(9);
		table.addCell(cell);
		return table;
	}
	private PdfPTable getFSTableTitle(PdfPTable table,String year,String lastyear) {
		PdfPCell cell;
		cell = pf.getPdfPCell("科目",styleName2);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("金額",styleName2);
		cell.setColspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("年度\r\n成長率",styleName2);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("科目",styleName2);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("金額",styleName2);
		cell.setColspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("占營收\r\n比率",styleName2);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("年度\r\n成長率",styleName2);
		cell.setRowspan(2);
		table.addCell(cell);
		table.addCell(pf.getPdfPCell(lastyear,styleName2));
		table.addCell(pf.getPdfPCell(year,styleName2));
		table.addCell(pf.getPdfPCell(lastyear,styleName2));
		table.addCell(pf.getPdfPCell(year,styleName2));
		return table;
	}
	private PdfPTable getTopTable2(String year) {
		PdfPTable table=new PdfPTable(2);
		try {
			table.setWidths(new float[]{1,1});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		table.setWidthPercentage(100);
		table.setSpacingAfter(5f);
		table.setSpacingBefore(10f);
		PdfPCell cell;
		cell = pf.getPdfPCell(year,styleContent);
		cell.setBorder(0);
		table.addCell(cell);
		cell = pf.getPdfPCell("單位：新台幣元；%",styleContent);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(0);
		table.addCell(cell);
		return table;
	}
	
	private void setFontSelector() {
		styletitle = pf.getFontSelector(18, Font.BOLD);
		styleName = pf.getFontSelector(14, Font.BOLD);
		styleName2 = pf.getFontSelector(12, Font.BOLD);
		styleContent = pf.getFontSelector(12, Font.NORMAL);
		styleContent2 = pf.getFontSelector(10, Font.NORMAL);
		styleTop = pf.getFontSelector(14, Font.NORMAL);
	}
	private void setNameMap() {
		nMap=new LinkedHashMap<String, String>();
		nMap.put("0","基本資料");
		nMap.put("1","陸資投資人資訊");
		nMap.put("2","稽核資訊");
		nMap.put("3","國內轉投資");
		nMap.put("4","聯絡資訊");
		nMap.put("5","訪查資料");
		nMap.put("6","財務資料");
	}
}
