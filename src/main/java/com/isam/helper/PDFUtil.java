package com.isam.helper;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;


public class PDFUtil {
	private BaseFont bf = null;
	
	public PDFUtil(){
		try {
			bf = BaseFont.createFont("C:\\WINDOWS\\Fonts\\KAIU.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public FontSelector getFontSelector( float size, int style) {
		FontSelector f = new FontSelector();
		if(bf!=null){
			Font Content = new Font(bf, size,style);
			Font ENG = FontFactory.getFont(FontFactory.TIMES_ROMAN, size);
			f.addFont(ENG);
			f.addFont(Content);
		}
		return f;
	}
	public PdfPCell getPdfPCell(String Str, FontSelector f) {
		PdfPCell p= new PdfPCell(new Phrase(f.process(DataUtil.nulltoempty(Str))));
		p.setVerticalAlignment(Element.ALIGN_MIDDLE);
		p.setMinimumHeight(5);
		p.setPaddingBottom(5);
		return p;
	}
	public PdfPCell getPdfPCell(String Str, FontSelector f,int hAlign) {
		PdfPCell p= new PdfPCell(new Phrase(f.process(DataUtil.nulltoempty(Str))));
		p.setHorizontalAlignment(hAlign);
		p.setVerticalAlignment(Element.ALIGN_MIDDLE);
		p.setMinimumHeight(5);
		p.setPaddingBottom(5);
		return p;
	}
	public Phrase getPhrase(String Str, FontSelector f) {
		Phrase p= new Phrase(f.process(Str));
		return p;
	}
	public Paragraph getParagraph(String Str, FontSelector f) {
		Paragraph p= new Paragraph(f.process(DataUtil.nulltoempty(Str)));
		return p;
	}
	public static void main(String args[]){
		
	}
}
