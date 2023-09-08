package com.isam.servlet;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.isam.bean.Interviewone;
import com.isam.bean.InterviewoneFile;
import com.isam.bean.OFIInvestNoXFinancial;
import com.isam.bean.OFIInvestorXFile;
import com.isam.bean.OFINTBTDatas;
import com.isam.bean.OFIReInvestList;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneService;
//import com.isam.service.ofi.OFIInvestCaseService;
import com.isam.service.ofi.OFIInvestNoXFinancialService;
import com.isam.service.ofi.OFIInvestorXFileService;
import com.isam.service.ofi.OFINTBTDatasService;
import com.isam.service.ofi.OFIReInvestListService;
import com.isam.service.ofi.OFIReInvestNoXFinancialService;

import Lara.Utility.ToolsUtil;

public class InterviewoneZipServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestorXFileService fSer; //取出投資人的架構圖
	private OFINTBTDatasService NTBTser; //取出財務資料下載
	private OFIInvestNoXFinancialService finSer; //取出本公司財報資料
	private OFIReInvestListService reSer; //取出轉投資公司
	private OFIReInvestNoXFinancialService rfinSer; //取出轉投資公司財報
	
	@Override
	public void init() throws ServletException
	{
//		icSer = new OFIInvestCaseService();
		fSer = new OFIInvestorXFileService();
		NTBTser = new OFINTBTDatasService();
		finSer= new OFIInvestNoXFinancialService();
		reSer=new OFIReInvestListService();
		rfinSer= new OFIReInvestNoXFinancialService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	//	this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		goToGetFile(request, response);
	
		
		
	}

	private void goToGetFile(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Map<String , String> params = new TreeMap<>();
		Enumeration<String> names = request.getParameterNames();
		
		
		String[] investNoList = request.getParameterValues("investNo");
		
	//	System.out.println( String.join(",", investNoList));
		while(names.hasMoreElements()){
			String name = names.nextElement();
			System.out.println( name);
			//利用name 取出每一個value並放入MAP內使用
			String[] values = request.getParameterValues(name);
			params.put(name, ToolsUtil.getListToValue(new ArrayList<>(Arrays.asList(values)), ","));
		}
		

		/* 取出登入資訊 */
		String sessionId = "";
		for(Cookie cookie : request.getCookies()){
			if("JSESSIONID".equalsIgnoreCase(cookie.getName())){
				sessionId = cookie.getValue();
				break;
			}
		}
		/* 取出根目錄之前的網址
		 * request.getScheme（） ：取得到通訊協定  http
		 * request.getServerName（） ：取得IP位置  localhost
		 * request.getServerPort（） ：取得埠號  8080
		 * request.getContextPath（） ：取得應用程式名稱  /isam
		 * request.getServletPath（） ：傳回網頁路徑  /console/cnfdi/OFIInvestZIP.view */
		//String urlHead = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		//String urlHead = "http://localhost:"+request.getServerPort()+request.getContextPath();
		
		
		/* 架構圖、財務資料下載：取資料庫byte[]
		 * 公司財報、轉投資公司財報：使用URLConnect取自動生成PDF，轉成byte[]放入 */
		ServletOutputStream out = null;
		ByteArrayOutputStream baos = null;
		ZipOutputStream zos = null;

		InterviewoneFileService ser= new InterviewoneFileService();
		try {
			baos = new ByteArrayOutputStream();
			zos = new ZipOutputStream(baos, Charset.forName("Cp950")); 

			String investNo = params.get("investNo");
		//	String com_name = params.get("cname");
			
		
			
			for(String param : investNoList){
				
		  String interviewFno = 	param.split("\\|")[0];
		  String surveyFNo = (param.split("\\|")).length==2?(param.split("\\|"))[1]:null;

		  
	//	  System.out.println(interviewFno+":"+surveyFNo+":"+param);
				 int ifNo =-1;
				if(interviewFno!=null&&interviewFno.trim().length()!=0){
					for(String fno : interviewFno.split(",")) {
					ifNo = Integer.valueOf(fno);
					InterviewoneFile bean =  ser.select(ifNo);
					String directory = "訪視紀錄表/";
					//開啟新entry，把檔案放入ZipOutput
					try {
						zos.putNextEntry(new ZipEntry(new String((directory+bean.getfName()).getBytes(),"Cp950")));
						zos.write(bean.getfContent());
						zos.closeEntry();
						}
						finally {
							
						}
				}
				}
				 int sfNo =-1;

				if(surveyFNo!=null&&surveyFNo.trim().length()!=0){
					for(String fno : surveyFNo.split(",")) {
					sfNo = Integer.valueOf(fno);
					InterviewoneFile bean =  ser.select(sfNo);
					String directory = "問卷/";
					//開啟新entry，把檔案放入ZipOutput
					try {
					zos.putNextEntry(new ZipEntry(new String((directory+bean.getfName()).getBytes(),"Cp950")));
					zos.write(bean.getfContent());
					zos.closeEntry();
					}
					finally {
						
					}
				}
}
				//財務資料下載：用 investNo 取所有財務資料下載
			
					
						

						
					
			}
				
			
			
			
			zos.flush();
			baos.flush();
			zos.close();
			byte[] zipBytes = baos.toByteArray();
			baos.close();
			
			/* Write attachment to JSP. */
			String dfilename = DataUtil.getStrUDate().replace("-", "") + "_陸資訪查" +  "相關資料.zip";
	        out = response.getOutputStream();
	        response.setContentType("application/zip");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(dfilename, "utf-8") + "\"");
	        out.write(zipBytes);
	        out.flush();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(zos != null){
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(baos != null){
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	//連線URL取資料，存到硬碟
	public byte[] doURLConnect(String sURL, String sessionId){
		/* HttpUrlConnection，把需要的request標頭放進RequestProperty
		 * 1.建立URL：遇到中文字要 URLEncoder
		 * 2.RequestProperty：這裡不需要模仿瀏覽器發送請求設定，但是需要登入的sessionID
		 * 3.用 BufferedInputStream 接資料回傳 
		 */
		byte[] fileBytes = null;
		HttpURLConnection URLconn = null;
		BufferedInputStream bis = null;
		try {
			URL url = new URL(sURL);
			URLconn = (HttpURLConnection)url.openConnection();
			URLconn.setRequestProperty("Cookie","JSESSIONID=" + sessionId);
			URLconn.connect();
			bis = new BufferedInputStream(URLconn.getInputStream());
			
			//System.out.println(URLconn.getContentLength());
			//System.out.println(new String(IOUtils.toByteArray(is), "utf-8")); 可以印出byte[]資料
			fileBytes = IOUtils.toByteArray(bis);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(URLconn != null){
				try {
					URLconn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return fileBytes;
	}
	
	
	//取得匯出PDF目錄表
	private void goToGetItem(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String investNo = request.getParameter("investNo");
		
		/* 取出架構圖
		 * 一：取出股東名冊 + 架構圖
		 *
		 * 取出架構圖
		 * for(Map<String,String> investorOne : investors){
		 *		String investorSeq = investorOne.get("INVESTOR_SEQ");
		 *      List<OFIInvestorXFile> ofifiles = fSer.select(investorSeq);
		 * }
		   
		 * 二：自寫method join上述兩表 
		 * SELECT A.INVESTOR_SEQ, A.INVESTOR_CHTNAME, B.fNo, B.title
		 * FROM OFI_InvestCase A  --投資人列表
		 * LEFT OUTER JOIN OFI_InvestorXFile B --架構圖列表
									ON A.INVESTOR_SEQ = B.investorSeq --投資人ID
		 * WHERE A.investNo = 500480 --國內事業ID
		 * AND B.enable='1' 
		 * order by B.createtime desc
		 */

		
		request.setAttribute("test", "test");
		
		// 取出架構圖 
		List<Map<String,String>> investors = fSer.getInvestorHasfile(investNo);
		request.setAttribute("investorsZip", investors);
		
		// 取出財務資料下載
		ArrayList<OFINTBTDatas> NTBTS = NTBTser.list(investNo);
		request.setAttribute("NTBTDatasZip", NTBTS);

		// 取出財報資料
		List<OFIInvestNoXFinancial> financial = finSer.select(investNo,"0"); //公司財報
		request.setAttribute("financialZip", financial);
		
		//取出轉投資財報
		List<OFIReInvestList> reInvests=reSer.select(investNo); //轉投資公司
		Map<String,List<OFIInvestNoXFinancial>> refinancial = null;
		if(reInvests != null && !reInvests.isEmpty()){
			refinancial = rfinSer.select(investNo); //轉投資公司財報
			request.setAttribute("reInvestsZip", reInvests);
			request.setAttribute("refinancialZip", refinancial);
		}
		
		//確定是否全部為空
		boolean hasData = false;
		if(investors != null && !investors.isEmpty()){
			hasData = true; 
		}
		if(NTBTS != null && !NTBTS.isEmpty()){
			hasData = true; 
		}
		if(financial != null && !financial.isEmpty()){
			hasData = true; 
		}
		if(refinancial != null && !refinancial.isEmpty()){
			hasData = true;
		}
		if(hasData == false){
			request.setAttribute("noData", "　尚無附件資料");
		}
		
		request.getRequestDispatcher("/console/cnfdi/content/firmpdf.jsp").forward(request, response);
		
	}
	
}
