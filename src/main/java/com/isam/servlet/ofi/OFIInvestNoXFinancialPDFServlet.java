package com.isam.servlet.ofi;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.bean.InterviewoneItem;
import com.isam.bean.OFIInvestNoXFinancial;
import com.isam.helper.DataUtil;
import com.isam.helper.PDFUtil;
import com.isam.service.InterviewoneHelp;
import com.isam.service.ofi.OFIInvestNoXFContentService;
import com.isam.service.ofi.OFIInvestNoXFinancialService;
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
 
public class OFIInvestNoXFinancialPDFServlet extends HttpServlet{
	private PDFUtil pf;
	private InterviewoneHelp opt;
	private OFIInvestNoXFContentService fcSer;
	private OFIInvestNoXFinancialService fSer;
	private FontSelector  styletitle;
	private FontSelector  styleName;
	private FontSelector  styleContent;
	private FontSelector  styleNote;
	Map<String, InterviewoneItem> frtOpt;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		opt = new InterviewoneHelp();
		frtOpt= opt.getqTypeF();
		fcSer= new OFIInvestNoXFContentService();
		fSer = new OFIInvestNoXFinancialService();
		pf=new PDFUtil();
		setFontSelector();
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
                    (rect.getLeft()+rect.getRight())/2, rect.getBottom() - 18, 0);
        }
    }
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// 接收資料
		String serno=DataUtil.nulltoempty(request.getParameter("serno"));
		String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
		String tt=DataUtil.nulltoempty(request.getParameter("tt"));
		
		OFIInvestNoXFinancial fbean=fSer.selectBySerno(serno);
		String lastyear=DataUtil.addZeroForNum(String.valueOf(Integer.valueOf(fbean.getReportyear())-1),3);
		OFIInvestNoXFinancial fbeanlast=fSer.selectbean(investNo, lastyear, "0");
		Map<String, String> fcbean=fcSer.selectBySerno(serno);
		Map<String, String> fcbeanlast = null;
		if(fbeanlast!=null){
			fcbeanlast=fcSer.selectBySerno(fbeanlast.getSerno());
		}
		
		if(serno.isEmpty()||investNo.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('無法取得資料，請重新選取！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/cnfdi/listapproval.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
//		String str=investNo.substring(0,1).equals("4")?"（陸分）"+investNo:"（陸）"+investNo;
		
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String date = DataUtil.getStrUDate();
		String dfilename ="財務資訊簡表("+investNo+").pdf";
		try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			HeaderFooter event = new HeaderFooter();
			writer.setBoxSize("art", new Rectangle(25, 40, 788, 788));
		    writer.setPageEvent(event);
			//設定文件作者
			document.addAuthor("ISAMAdmin");
			//設定文件標題
			document.addTitle(dfilename.substring(0, dfilename.length()-4));
			//設定創建日期
			document.addCreationDate();
			document.setPageSize(PageSize.A4.rotate());
			document.setMargins(50,50,30,30);
		    //開啟文件
			document.open();
			
			Paragraph p;
			p=pf.getParagraph("下載日期:"+DataUtil.toTWDateStr(DataUtil.convertDate(date)), styleNote);
			p.setAlignment(Element.ALIGN_RIGHT);
			p.setSpacingAfter(5f);
			document.add(p);
			p=pf.getParagraph(tt, styletitle);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			document.add(getTopTable("填報日期："+DataUtil.addSlashToTWDate(fbean.getReportdate())));
			document.add(getFSTable(fbean,fcbean, fcbeanlast,fbean.getReportyear(), lastyear));
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
	private PdfPTable getFSTable(OFIInvestNoXFinancial fbean,Map<String, String> fcbean,Map<String, String> fcbeanlast,String year,String lastyear) {
		PdfPTable table=new PdfPTable(9);
		table = setFSTableTitleLayout(getFSTableTitle(table,year,lastyear),2);
		table.setWidthPercentage(100);
		table.setSpacingAfter(5f);
		table = setFSTableContentLayout(getFSTableContent(table,fbean, fcbean, fcbeanlast),2);
		try {
			table.setWidths(new float[]{1.3f,1.0f,1.0f,0.7f,1.2f,1.0f,1.0f,0.8f,0.7f});
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
					cell.setBackgroundColor(WebColors.getRGBColor("#F3F3F3"));
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
			table.addCell(pf.getPdfPCell(frtOpt.get(k1).getcName(),styleContent));
			table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbeanlast.get(k1)),styleContent));
			table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get(k1)),styleContent));
			table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getGrowthRate(DataUtil.nulltoempty(fcbean.get(k1)), DataUtil.nulltoempty(fcbeanlast.get(k1)))),styleContent));
			table.addCell(pf.getPdfPCell(frtOpt.get(k2).getcName(),styleContent));
			table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbeanlast.get(k2)),styleContent));
			table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get(k2)),styleContent));
			table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getPercent(DataUtil.nulltoempty(fcbean.get(k2)), idown)),styleContent));
			table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getGrowthRate(DataUtil.nulltoempty(fcbean.get(k2)), DataUtil.nulltoempty(fcbeanlast.get(k2)))),styleContent));
		}
		PdfPCell cell;
		cell =pf.getPdfPCell("", styleContent);
		cell.setColspan(4);
		table.addCell(cell);
		table.addCell(pf.getPdfPCell(frtOpt.get("64").getcName(),styleContent));
		table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbeanlast.get("64")),styleContent));
		table.addCell(pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get("64")),styleContent));
		table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getPercent(DataUtil.nulltoempty(fcbean.get("64")), idown)),styleContent));
		table.addCell(pf.getPdfPCell(DataUtil.formatString(DataUtil.getGrowthRate(DataUtil.nulltoempty(fcbean.get("64")), DataUtil.nulltoempty(fcbeanlast.get("64")))),styleContent));
		
		table.addCell(pf.getPdfPCell(frtOpt.get("65").getcName()+"：",styleContent));
		cell=pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get("65")),styleContent);
		cell.setColspan(4);
		table.addCell(cell);
		table.addCell(pf.getPdfPCell(frtOpt.get("66").getcName()+"：",styleContent));
		cell=pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get("66")),styleContent);
		cell.setColspan(3);
		table.addCell(cell);
		table.addCell(pf.getPdfPCell(frtOpt.get("67").getcName()+"：",styleContent));
		cell=pf.getPdfPCell(DataUtil.nulltoempty(fcbean.get("67")),styleContent);
		cell.setColspan(8);
		table.addCell(cell);
		table.addCell(pf.getPdfPCell("備註：",styleContent));
		cell=pf.getPdfPCell(DataUtil.nulltoempty(fbean.getNote()),styleContent);
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
		cell=pf.getPdfPCell(sb.toString(),styleContent);
		sb.setLength(0);
		cell.setColspan(9);
		table.addCell(cell);
		return table;
	}
	private PdfPTable getFSTableTitle(PdfPTable table,String year,String lastyear) {
		PdfPCell cell;
		cell = pf.getPdfPCell("科目",styleName);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("金額",styleName);
		cell.setColspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("年度\r\n成長率",styleName);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("科目",styleName);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("金額",styleName);
		cell.setColspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("占營收\r\n比率",styleName);
		cell.setRowspan(2);
		table.addCell(cell);
		cell = pf.getPdfPCell("年度\r\n成長率",styleName);
		cell.setRowspan(2);
		table.addCell(cell);
		table.addCell(pf.getPdfPCell(lastyear,styleName));
		table.addCell(pf.getPdfPCell(year,styleName));
		table.addCell(pf.getPdfPCell(lastyear,styleName));
		table.addCell(pf.getPdfPCell(year,styleName));
		return table;
	}
	private PdfPTable getTopTable(String reportdate) {
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
		cell = pf.getPdfPCell(reportdate,styleNote);
		cell.setBorder(0);
		table.addCell(cell);
		cell = pf.getPdfPCell("單位：新台幣元；%",styleNote);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(0);
		table.addCell(cell);
		return table;
	}
	private void setFontSelector() {
		styletitle = pf.getFontSelector(18, Font.BOLD);
		styleName = pf.getFontSelector(14, Font.BOLD);
		styleContent = pf.getFontSelector(12, Font.NORMAL);
		styleNote = pf.getFontSelector(12, Font.NORMAL);
	}
}
