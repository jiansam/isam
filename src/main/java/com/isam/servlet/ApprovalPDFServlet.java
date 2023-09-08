package com.isam.servlet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.helper.DataUtil;
import com.isam.service.MoeaicDataService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
 
public class ApprovalPDFServlet extends HttpServlet{
	/** Inner class to add a header and a footer. */
    class HeaderFooter extends PdfPageEventHelper {
        /** Alternating phrase for the header. */
//        Phrase[] header = new Phrase[1];
//        Phrase[] header = new Phrase[2];
        /** Current page number (will be reset for every chapter). */
        int pagenumber;
        
        /**
         * Initialize one of the headers.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
//        public void onOpenDocument(PdfWriter writer, Document document) {
//            header[0] = new Phrase("Movie history");
//        }
        
        /**
         * Initialize one of the headers, based on the chapter title;
         * reset the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, float,
         *      com.itextpdf.text.Paragraph)
         */
//        public void onChapter(PdfWriter writer, Document document,
//                float paragraphPosition, Paragraph title) {
//            header[1] = new Phrase(title.getContent());
//            pagenumber = 1;
//        }

        /**
         * Increase the page number.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onStartPage(PdfWriter writer, Document document) {
            pagenumber++;
        }
        
        /**
         * Adds the header and the footer.
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            BaseFont bf = null;
			try {
				bf = BaseFont.createFont("MSungStd-Light","UniCNS-UCS2-H",BaseFont.NOT_EMBEDDED);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_CENTER, new Phrase(14, String.format("頁次: %d", pagenumber), new Font(bf, 10, Font.NORMAL)),
                    (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
        }
    }
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// 接收資料
		String idno = request.getParameter("idno");
		String investNo = request.getParameter("investNo");
		String investor = request.getParameter("investor");
		String cnName = request.getParameter("cnName");
		
		// model先new出來備用
		MoeaicDataService ser = new MoeaicDataService();
		List<List<String>> approvalDetail = ser.selectWebSRC(idno, investNo);
		List<String> approvalSum = ser.selectSumMoney(idno, investNo);

		if(approvalDetail.size()==0){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('無法取得資料，請重新選取！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/approval/approval.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		List<List<String>> result = new ArrayList<List<String>>();
		for (int i = 0; i < approvalDetail.size(); i++) {
			List<String> subListtemp1 = approvalDetail.get(i).subList(0, 8);
			List<String> subListtemp2 = approvalDetail.get(i).subList(8, 19);
			List<String> subList1 = new ArrayList<String>();
			subList1.addAll(subListtemp1);
			subList1.add("");
			subList1.add("");
			subList1.add("");
			List<String> subList2 = new ArrayList<String>();
			for(String s:subListtemp2){
				if(i!=0){
					subList2.add(DataUtil.formatString(s));
				}else{
					subList2.add(s);
				}
			}
			subList2.addAll(subListtemp2);
			result.add(subList1);
			result.add(subList2);
		}
		Document document = new Document();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String date = DataUtil.getStrUDate();
		String dfilename ="大陸投資明細資料列表.pdf";
		try {
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			HeaderFooter event = new HeaderFooter();
		    writer.setBoxSize("art", new Rectangle(25, 40, 788, 788));
//		    writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		    writer.setPageEvent(event);
			//設定文件作者
			document.addAuthor("ISAMAdmin");
			//設定文件標題
			document.addTitle(dfilename.substring(0, dfilename.length()-4));
			//設定創建日期
			document.addCreationDate();
			document.setPageSize(PageSize.A4.rotate());
			document.setMargins(30,30,30,30);

		    BaseFont bf = BaseFont.createFont("MSungStd-Light","UniCNS-UCS2-H",BaseFont.NOT_EMBEDDED);
		    Font styletitle = new Font(bf, 18, Font.NORMAL);
		    Font styleName = new Font(bf, 10, Font.BOLD);
		    Font styleContent = new Font(bf, 10, Font.NORMAL);
			
		    
		    //開啟文件
			document.open();
			
			PdfPTable topTable = new PdfPTable(5);
			topTable.setWidthPercentage(98);
			topTable.setKeepTogether(true);
			topTable.setSpacingBefore(5);
			topTable.setSpacingAfter(5);
			
			Paragraph adddate = new Paragraph("下載日期:"+date,styleContent);
			PdfPCell topDateCell = new PdfPCell(adddate);
			topDateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topDateCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			topDateCell.setBorder(0);
			topDateCell.setColspan(2);
			topTable.addCell(topDateCell);
			
			Paragraph top = new Paragraph("大陸投資明細資料列表",styletitle);
			PdfPCell topTextCell = new PdfPCell(top);
			topTextCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			topTextCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			topTextCell.setBorder(0);
			topTextCell.setColspan(3);
			topTable.addCell(topTextCell);
			
			String x1="案號："+investNo;
			String x2="大陸投資事業名稱："+cnName;
			String x3="統編："+idno;
			String x4="投資人名稱："+investor;
			
			Paragraph datax1 = new Paragraph(x1,styleContent);
			PdfPCell topCellx1 = new PdfPCell(datax1);
			topCellx1.setHorizontalAlignment(Element.ALIGN_LEFT);
			topCellx1.setVerticalAlignment(Element.ALIGN_BOTTOM);
			topCellx1.setBorder(0);
			topCellx1.setMinimumHeight(30);
			topTable.addCell(topCellx1);
			Paragraph datax2 = new Paragraph(x2,styleContent);
			PdfPCell topCellx2 = new PdfPCell(datax2);
			topCellx2.setHorizontalAlignment(Element.ALIGN_LEFT);
			topCellx2.setVerticalAlignment(Element.ALIGN_BOTTOM);
			topCellx2.setBorder(0);
			topCellx2.setColspan(4);
			topTable.addCell(topCellx2);
			
			Paragraph datax3 = new Paragraph(x3,styleContent);
			PdfPCell topCellx3 = new PdfPCell(datax3);
			topCellx3.setHorizontalAlignment(Element.ALIGN_LEFT);
			topCellx3.setVerticalAlignment(Element.ALIGN_BOTTOM);
			topCellx3.setBorder(0);
			topTable.addCell(topCellx3);
			
			Paragraph datax4 = new Paragraph(x4,styleContent);
			PdfPCell topCellx4 = new PdfPCell(datax4);
			topCellx4.setHorizontalAlignment(Element.ALIGN_LEFT);
			topCellx4.setVerticalAlignment(Element.ALIGN_BOTTOM);
			topCellx4.setBorder(0);
			topCellx4.setColspan(4);
			topTable.addCell(topCellx4);
			document.add(topTable);
			
			PdfPTable table = new PdfPTable(11);
			table.setWidthPercentage(98);
			table.setSpacingBefore(5);
			table.setHeaderRows(2);
			for(int i=0;i<result.size();i++){
				for(int j=0;j<11;j++){
					String ans = result.get(i).get(j);
					PdfPCell cell;
					int flag = i%2;
					if(flag==0){
						if(i==0){
							cell = new PdfPCell (new Paragraph(ans,styleName));
						}else{
							cell = new PdfPCell (new Paragraph(ans,styleContent));
						}
						cell.setBorder(1);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					}else{
						if(i==1){
							cell = new PdfPCell (new Paragraph(ans,styleName));
						}else{
							cell = new PdfPCell (new Paragraph(ans,styleContent));
						}
						cell.setBorder(0);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					}
					cell.setMinimumHeight(20);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setLeading(1f,1.2f);
					table.addCell(cell);
				}
			}
			
			PdfPCell space = new PdfPCell (new Paragraph(""));
			space.setBorder(1);
			space.setColspan(11);
			table.addCell(space);
			table.addCell(space);
			
			String sum1="資金加總："+DataUtil.formatString(approvalSum.get(0));
			Paragraph data = new Paragraph(sum1,styleContent);
			PdfPCell topCell = new PdfPCell(data);
			topCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			topCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			topCell.setBorder(0);
			topCell.setColspan(8);
			table.addCell(topCell);
			
			String sum2="審定加總："+DataUtil.formatString(approvalSum.get(1));
			Paragraph data2 = new Paragraph(sum2,styleContent);
			PdfPCell topCell2 = new PdfPCell(data2);
			topCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			topCell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
			topCell2.setBorder(0);
			topCell2.setColspan(3);
			table.addCell(topCell2);
			
			String sum3="投資案審定資金比例："+approvalSum.get(2);
			Paragraph data3 = new Paragraph(sum3,styleContent);
			PdfPCell topCell3 = new PdfPCell(data3);
			topCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
			topCell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
			topCell3.setBorder(0);
			topCell3.setColspan(11);
			table.addCell(topCell3);
			
			document.add(table);

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
}
