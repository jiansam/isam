package com.isam.servlet.ofi;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.helper.DataUtil;
import com.isam.helper.OrderUtil;
import com.isam.helper.PDFUtil;
import com.isam.service.ofi.OFIAuditOptionService;
import com.isam.service.ofi.OFIDepartmentService;
import com.isam.service.ofi.OFIInvestCaseService;
import com.isam.service.ofi.OFIInvestNoXAuditService;
import com.isam.service.ofi.OFIInvestNoXTWSICService;
import com.isam.service.ofi.OFIInvestorXBGService;
import com.isam.service.ofi.OFIReInvestListService;
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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
 
public class OFIInvestXAuditPDFServlet extends HttpServlet{
	private PDFUtil pf;
	private OFIInvestCaseService icSer;
	private OFIInvestorXBGService bgSer;
	private OFIInvestNoXAuditService auditSer;
	private OFIInvestNoXTWSICService sicSer;
	private OFIDepartmentService deptSer;
	private OFIReInvestListService reSer;
	private OFIAuditOptionService aoptSer;
	private FontSelector  styletitle;
	private FontSelector  styleName;
	private FontSelector  styleContent;
	private FontSelector  styleNote;
	Map<String, String> nMap;
	Map<String, String> aopt;
	Map<String, String> dept;
	
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		icSer=new OFIInvestCaseService();
		bgSer=new OFIInvestorXBGService();
		auditSer=new OFIInvestNoXAuditService();
		sicSer=new OFIInvestNoXTWSICService();
		reSer=new OFIReInvestListService();
		deptSer=new OFIDepartmentService();
		dept=deptSer.getCodeNameMap();
		aoptSer=new OFIAuditOptionService();
		aopt=aoptSer.getAuditOptionMap();
		pf=new PDFUtil();
		setFontSelector();
		setNameMap();
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
		
		// 接收資料
		String investNo = DataUtil.nulltoempty(request.getParameter("investNo"));
		//國內事業名稱
		String cname=icSer.getInvestName(investNo);
		//國內事業名稱事後管理項目Count
		Map<String,String> countMap=icSer.getRemarkCount(investNo);
		
		//國內事業名稱涉及投資人股權比例和名稱
		Map<String,Map<String,String>> isnames=icSer.getCNInvestorNameSp(investNo);
		//國內事業名稱投資人涉及的背景，<investorSeq,<bgType,value>>
		Map<String,Map<String,String>> bg=bgSer.getBGByInvestNo(investNo);

		//國內事業名稱涉及的稽核項目
		List<Map<String,String>> audit=auditSer.selectByInvestNo(investNo);
		List<Map<String,String>> a02=auditSer.classifyByAudit(audit,"02");
		List<Map<String,String>> a01=auditSer.classifyByAudit(audit,"01");
		List<Map<String,String>> a03=auditSer.classifyByAudit(audit,"03");
		Map<String, Map<String, String>> a02s=auditSer.getAudit02Details(investNo);
		
		//轉投資名稱Map<reInvestNo,reinvestment>
		Map<String,String> reMap=reSer.getReinvestNoNameMap(investNo);
		//取得轉投資特許項目<reInvestNo,List<item>>
		String type="0";
		Map<String,List<String>>relist=reSer.getReinvestNoItems(investNo, type); //106-08-28 追加SQL條件，只取出仍未廢止的轉投資公司
		//取得特殊及特許項目
		Map<String,List<String>> spMap=sicSer.select(investNo);
		List<String> splist=spMap.get(type);
		//取得特殊及特許項目名稱		
		 Map<String,String> sicmap=sicSer.getTWSICMap();
		 
		if(cname.isEmpty()||investNo.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('無法取得資料，請重新選取！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/cnfdi/listapproval.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		String str=investNo.substring(0,1).equals("4")?"（陸分）"+investNo:"（陸）"+investNo;
		
		Document document = new Document(PageSize.A4,50,50,50,40);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String date = DataUtil.getStrUDate();
		String dfilename ="事後管理應注意事項表("+investNo+").pdf";
		try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			HeaderFooter event = new HeaderFooter();
		    writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
//		    writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		    writer.setPageEvent(event);
			//設定文件作者
			document.addAuthor("ISAMAdmin");
			//設定文件標題
			document.addTitle(dfilename.substring(0, dfilename.length()-4));
			//設定創建日期
			document.addCreationDate();
		    
		    //開啟文件
			document.open();
			
			Paragraph p=pf.getParagraph("事後管理應注意事項表", styletitle);
			p.setAlignment(Element.ALIGN_CENTER);
			p.setSpacingAfter(3.5f);
			document.add(p);
			p=pf.getParagraph("下載日期:"+date, styleContent);
			p.setAlignment(Element.ALIGN_RIGHT);
			document.add(p);
			document.add(pf.getParagraph("案號："+str, styleContent));
			document.add(pf.getParagraph("國內事業名稱："+cname, styleContent));
			int sd=0;
			for(Entry<String, String> m:countMap.entrySet()){
				sd+=Integer.valueOf(m.getValue());
			}
			
			countMap.putAll(checkCountMap(countMap,isnames, bg, spMap, a01,a02,a03));
			document.add(getBriefTable(countMap));
			if(sd!=0){
				document.add(getParagraph("詳細資料：",styleName));
				int i=1;
				for (Entry<String, String> m:nMap.entrySet()) {
					String o=OrderUtil.addComma(OrderUtil.getOrderName(i))+m.getValue();
					document.add(getParagraph(o,styleName));
					String k=countMap.get(m.getKey());
					if(i==1){
						document.add(getBGTable(bg, isnames,k));
					}else if(i==2){
						document.add(getSPTable(sicmap, splist, reMap, relist,k));
					}else if(i==3){
						document.add(getTermsYNTable(k));
						int j=1;
						for (Entry<String, Map<String, String>> x:a02s.entrySet()) {
							document.add(getTermsTable(x.getValue(),j));
							j++;
						}
					}else if(i==4){
						document.add(getAuthorityTable(a01,a03,k));
					}
					i++;
				}
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();
		
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
	private Map<String,String> checkCountMap(Map<String,String> countMap,Map<String,Map<String,String>> isnames,
			Map<String,Map<String,String>> bg,Map<String,List<String>> spMap,List<Map<String,String>> a01
			,List<Map<String,String>> a02,List<Map<String,String>> a03) {
		Map<String,String> result=new LinkedHashMap<String, String>();
		result.putAll(countMap);
		if(spMap.isEmpty()){
			result.put("sp", "");
		}
		if(a02==null||a02.isEmpty()){
			result.put("a02", "");
		}
		if((a01==null||a01.isEmpty())&&(a03==null||a03.isEmpty())){
			result.put("a0103", "");
		}
		for(Entry<String, String> m:result.entrySet()){
			String v=m.getValue();
//			System.out.println(m.getKey()+"="+v);
			if(v.isEmpty()){
				v="未填";
			}else{
				v=v.equals("0")?"無":"有";
			}
			result.put(m.getKey(), v);
		}
		Set<String> tmp=new HashSet<String>();
		for (Entry<String, Map<String, String>> m:bg.entrySet()) {
			Map<String, String> v=m.getValue();
			for (Entry<String, String> sub:v.entrySet()) {
				if(!sub.getKey().endsWith("Note")){
					tmp.add(sub.getValue());
//					System.out.println(sub.getValue());
				}
			}
		}
//		System.out.println(tmp.size());
		if(tmp.contains("否")&&tmp.size()==1){
			result.put("bg", "無");
		}else if(tmp.contains("未填")){
			result.put("bg", "未填");
		}else{
			result.put("bg", "有");
		}
		if(isnames.isEmpty()){
			result.put("bg", "無*");
		}
		for (Entry<String, Map<String, String>> m:isnames.entrySet()) {
			if(!bg.containsKey(m.getKey())){
				result.put("bg", "未填");
			}
		}
		return result;
	}
	private Paragraph getParagraph(String Str, FontSelector f) {
		Paragraph p= pf.getParagraph(Str, f);
		p.setSpacingBefore(10f);
		p.setSpacingAfter(10f);
		return p;
	}
	private PdfPTable getBriefTable(Map<String, String> countMap) {
		PdfPTable table = new PdfPTable(countMap.size());
		table.setWidthPercentage(100);
		table.setSpacingBefore(20f);
		PdfPCell cell;
		for(Entry<String, String> m:countMap.entrySet()){
			String k = m.getKey();
			cell = pf.getPdfPCell(nMap.get(k),styleName);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(25f);
			table.addCell(cell);
		}
		for(Entry<String, String> m:countMap.entrySet()){
			String v=m.getValue();
			cell = pf.getPdfPCell(v,styleContent);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(25f);
			table.addCell(cell);
		}
		if(countMap.get("bg").endsWith("*")){
			cell = pf.getPdfPCell("註:星號(*)表示無陸資投資人。",styleNote);
			cell.setFixedHeight(25f);
			cell.setBorder(0);
			cell.setColspan(4);
			table.addCell(cell);
		}
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		return table;
	}
	private PdfPTable getBGTable(Map<String,Map<String,String>> bg,Map<String,Map<String,String>> isnames,String ifEmpty) {
		PdfPTable table=new PdfPTable(2);
		try {
			table.setWidths(new float[]{1,2});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		table.setWidthPercentage(90);
		table.getDefaultCell().setMinimumHeight(25f);
		PdfPCell cell;
		int num=1;
		StringBuilder sb=new StringBuilder();
		if(ifEmpty.startsWith("無")){
			cell = pf.getPdfPCell(ifEmpty.replace("*", "陸資投資人"), styleContent);
			cell.setBorder(0);
			cell.setFixedHeight(25f);
			cell.setColspan(2);
			table.addCell(cell);
		}else{
			for (Entry<String, Map<String, String>> n:isnames.entrySet()) {
				sb.append(OrderUtil.addSquare(OrderUtil.getOrderName(num)));
				sb.append(n.getValue().get("iscname"));
				String name=sb.toString();
				sb.setLength(0);
				cell = pf.getPdfPCell(name, styleName);
				cell.setBorder(0);
				cell.setPaddingBottom(10f);
				cell.setColspan(2);
				table.addCell(cell);
				if(!bg.containsKey(n.getKey())){
					cell=pf.getPdfPCell("未填", styleContent);
					cell.setPaddingBottom(5f);
					cell.setColspan(2);
					table.addCell(cell);
				}else{
					for (Entry<String, String> s:bg.get(n.getKey()).entrySet()) {
						if(s.getKey().endsWith("Note")){
							cell=pf.getPdfPCell("備註：", styleContent);
						}else{
							String tmp=s.getKey();
							if(tmp.equals("BG1")){
								tmp="1.黨政軍案件：";
							}else{
								tmp="2.央企政府出資案件：";
							}
							cell=pf.getPdfPCell(tmp, styleContent);
						}
						cell.setPaddingBottom(5f);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						table.addCell(cell);
						cell = pf.getPdfPCell(s.getValue(), styleContent);
						cell.setPaddingBottom(5f);
						table.addCell(cell);
					}
				}
				num++;
			}
		}
		return table;
	}
	private PdfPTable getSPTable(Map<String,String> sicmap,List<String> splist,Map<String,String> reMap,Map<String,List<String>>relist,String ifEmpty) {
		PdfPTable table=new PdfPTable(2);
//		table.getDefaultCell().setLeading(3f, 1.2f);
		try {
			table.setWidths(new float[]{1,6});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		table.setWidthPercentage(90);
		PdfPCell cell;
		if(!ifEmpty.startsWith("有")){
			cell = pf.getPdfPCell(ifEmpty,styleContent);
			cell.setBorder(0);
			cell.setColspan(2);
			table.addCell(cell);
		}else{
			if(splist!=null&&!splist.isEmpty()){
				cell = pf.getPdfPCell("國內事業涉及特許或特殊事項列表",styleName);
				cell.setColspan(2);
				cell.setBackgroundColor(WebColors.getRGBColor("#F3F3F3"));
				cell.setPaddingBottom(5f);
				table.addCell(cell);
				for (int i = 0; i < splist.size(); i++) {
					String code=splist.get(i);
					cell = pf.getPdfPCell(code,styleContent);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setPaddingBottom(5f);
					table.addCell(cell);
					cell = pf.getPdfPCell(sicmap.get(code),styleContent);
					cell.setPaddingBottom(5f);
					table.addCell(cell);
				}
			}
			if(relist!=null&&!relist.isEmpty()){
				cell = pf.getPdfPCell("國內事業轉投資事業涉及特許或特殊事項列表",styleName);
				cell.setColspan(2);
				cell.setPaddingBottom(5f);
				cell.setBackgroundColor(WebColors.getRGBColor("#F3F3F3"));
				table.addCell(cell);
				int num=1;
				StringBuilder sb=new StringBuilder();
				for (Entry<String, List<String>> m :relist.entrySet()) {
					String k=m.getKey();
					List<String> v=m.getValue();
					String n=reMap.get(k);
					sb.append(OrderUtil.addSquare(OrderUtil.getOrderName(num)));
					sb.append(n);
					String name=sb.toString();
					sb.setLength(0);
					cell = pf.getPdfPCell(name,styleName);
					cell.setPaddingBottom(5f);
					cell.setColspan(2);
					table.addCell(cell);
					for (int i = 0; i < v.size(); i++) {
						String code=v.get(i);
						cell = pf.getPdfPCell(code,styleContent);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setPaddingBottom(5f);
						table.addCell(cell);
						cell = pf.getPdfPCell(sicmap.get(code),styleContent);
						cell.setPaddingBottom(5f);
						table.addCell(cell);
					}
					num++;
				}
			}
		}
		return table;
	}
	private PdfPTable getTermsTable(Map<String,String> map,int seq) {
		PdfPTable table=new PdfPTable(3);
		try {
			table.setWidths(new float[]{1,2,8});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		table.setWidthPercentage(90);
		PdfPCell cell;
		cell = pf.getPdfPCell(OrderUtil.addSquare(OrderUtil.getOrderName(seq))+aopt.get("02"),styleName);
		cell.setColspan(3);
		cell.setBorder(0);
		cell.setPaddingBottom(10f);
		table.addCell(cell);
		for (Entry<String, String> m:map.entrySet()) {
			String k=m.getKey();
			String v=DataUtil.nulltoempty(m.getValue());
			if(aopt.containsKey(k)){
				cell = pf.getPdfPCell(aopt.get(k)+"：",styleContent);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setPaddingBottom(5f);
				cell.setColspan(2);
				table.addCell(cell);
				if(k.equals("0201")){
					v=DataUtil.addSlashToTWDate(v);
				}else if(k.equals("0205")){
					if(v.equals("0")){
						v="否";
					}else if(v.equals("1")){
						v="是";
					}else{
						v="未確認";
					}
				}
				cell = pf.getPdfPCell(v	,styleContent);
				cell.setPaddingBottom(5f);
				table.addCell(cell);
			}
		}
		return table;
	}
	private PdfPTable getTermsYNTable(String ifEmpty) {
		PdfPTable table=new PdfPTable(3);
		try {
			table.setWidths(new float[]{1,2,8});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		table.setWidthPercentage(90);
		PdfPCell cell;
		if(!ifEmpty.startsWith("有")){
			cell = pf.getPdfPCell(ifEmpty,styleContent);
			cell.setBorder(0);
			cell.setColspan(3);
			table.addCell(cell);
		}
		return table;
	}
	private PdfPTable getAuthorityTable(List<Map<String,String>> a01,List<Map<String,String>> a03,String ifEmpty) {
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		PdfPTable table=new PdfPTable(3);
		try {
			table.setWidths(new float[]{1,2,8});
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		table.setWidthPercentage(90);
		PdfPCell cell;
		if(a01!=null&&!a01.isEmpty()){
			list.add(auditSer.getAuditsName(a01, dept));
		}
		if(a03!=null&&!a03.isEmpty()){
			list.add(auditSer.getAuditsName(a03, dept));
		}
		if(!ifEmpty.startsWith("有")){
			cell = pf.getPdfPCell(ifEmpty,styleContent);
			cell.setBorder(0);
			cell.setColspan(3);
			table.addCell(cell);
		}else{
			for (int i=0;i<list.size();i++) {
				for (Entry<String, String> m:list.get(i).entrySet()) {
					String k=m.getKey();
					if(k.length()==2){
						cell = pf.getPdfPCell(OrderUtil.addSquare(OrderUtil.getOrderName(i+1))+aopt.get(k),styleName);
						cell.setColspan(3);
						cell.setBorder(0);
						cell.setPaddingBottom(10f);
						table.addCell(cell);
						if(m.getValue().equals("0")){
							cell = pf.getPdfPCell(" ",styleContent);
							cell.setBorder(0);
							table.addCell(cell);
							cell = pf.getPdfPCell("無",styleContent);
							cell.setBorder(0);
							cell.setColspan(2);
							table.addCell(cell);
						}
					}else{
						if(m.getValue().length()>0){
							cell = pf.getPdfPCell(aopt.get(k)+"：",styleContent);
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							cell.setPaddingBottom(5f);
							cell.setColspan(2);
							table.addCell(cell);
							cell = pf.getPdfPCell(m.getValue(),styleContent);
							cell.setPaddingBottom(5f);
							table.addCell(cell);
						}
					}
					
				}
			}
		}
		return table;
	}
	private void setFontSelector() {
		styletitle = pf.getFontSelector(18, Font.BOLD);
		styleName = pf.getFontSelector(14, Font.BOLD);
		styleContent = pf.getFontSelector(14, Font.NORMAL);
		styleNote = pf.getFontSelector(10, Font.NORMAL);
	}
	private void setNameMap() {
		nMap=new LinkedHashMap<String, String>();
		nMap.put("bg","投資人身份");
		nMap.put("sp","業務特殊性");
		nMap.put("a02","附款要求");
		nMap.put("a0103","審查機關控管要求");
	}
}
