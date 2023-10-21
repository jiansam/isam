package com.isam.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.isam.bean.CommonItemList;
import com.isam.bean.Interviewone;
import com.isam.bean.InterviewoneCompany;
import com.isam.bean.InterviewoneFile;
import com.isam.bean.InterviewoneYear;
import com.isam.helper.DataUtil;
import com.isam.service.CommonItemListService;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneService;
import com.isam.service.MoeaicDataService;


public class InterviewoneDownloadByYearServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneService ioSer;
	private InterviewoneFileService iofSer;
	private MoeaicDataService mdSer;
	private InterviewoneHelp iohelper;
	private Map<String,Map<String,String>> cninfo;
	private Map<String,Map<String,String>> optionValName;
	  private CharsetEncoder enc;
	  private Charset cs = StandardCharsets.UTF_8;
	   private boolean isUTF8;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ioSer = new InterviewoneService();
		iofSer = new InterviewoneFileService();
		mdSer = new MoeaicDataService();
		cninfo=mdSer.getCNSysBaseInfo();
		iohelper = new InterviewoneHelp();
		optionValName=iohelper.getOptionValName();
	}
	 private CharsetEncoder encoder() {
	        if (enc == null) {
	            enc = cs.newEncoder()
	              .onMalformedInput(CodingErrorAction.REPORT)
	              .onUnmappableCharacter(CodingErrorAction.REPORT);
	        }
	        return enc;
	    }
	public boolean isValidName(String name) {
		  CharsetEncoder ce = encoder().reset();
	        char[] ca = name.toCharArray();
	        int len = (int)(ca.length * ce.maxBytesPerChar());
	        byte[] ba = new byte[len];
	        if (len == 0)
	            return true;
	        // UTF-8 only for now. Other ArrayDeocder only handles
	        // CodingErrorAction.REPLACE mode.
	        /*
	        if (isUTF8 && ce instanceof ArrayEncoder) {
	            int blen = ((ArrayEncoder)ce).encode(ca, 0, ca.length, ba);
	            if (blen == -1)    // malformed
	               return false;
	            return true;
	        }*/
	        return true;
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		session.removeAttribute("yearlist");
		session.removeAttribute("Interviewone");
		List<String> yearlist=ioSer.getYearList();
		session.setAttribute("yearlist", yearlist);
		
		String url="/interviewone/downloadbyyear.jsp";
		String servletPath= req.getServletPath();
		if(servletPath.indexOf("console")==-1){
			url=url.replace("/console", "");
		}
		String path = req.getContextPath();
		resp.sendRedirect(path + url);
	//	this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
	

		Map<String,String> map=new HashMap<String, String>();
		String startYear=request.getParameter("startyear")==null?"":DataUtil.addZeroForNum(request.getParameter("startyear").trim(), 3);
		String endYear=request.getParameter("endyear")==null?"":DataUtil.addZeroForNum(request.getParameter("endyear").trim(), 3);
	
		
		String type=request.getParameter("type")==null?"":request.getParameter("type");
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String investName=DataUtil.fmtSearchItem(request.getParameter("investName"),"%");
		String survey=request.getParameter("survey");
		String interview=request.getParameter("interview");
		String AndOr=request.getParameter("AndOr")==null?"":request.getParameter("AndOr");
		String abnormal=request.getParameterValues("abnormal")==null?"":DataUtil.addTokenToItem(request.getParameterValues("abnormal"),",");
		//107-08-01 新增異常狀況條件查詢(來源來自訪視問卷裡的 異常狀況彙總 & 訪視備註)
		String errMsgXnote=request.getParameter("errMsgXnote")==null?"":request.getParameter("errMsgXnote");
		Map<String,String> maxYM=ioSer.getMaxInterviewDateYM();
		map.put("startyear", startYear);
		map.put("endyear", endYear);
	
		map.put("maxM", maxYM.get("month"));
		map.put("maxY", maxYM.get("year"));
		map.put("investNo", investNo);
		map.put("IDNO", IDNO);
		map.put("investName", investName);
		map.put("survey", survey);
		map.put("interview", interview);
		map.put("AndOr", AndOr);
		map.put("abnormal", abnormal);
		map.put("errMsgXnote", errMsgXnote);
		String start=map.get("maxY")+"0100";
//		String start=map.get("maxY")+map.get("maxM")+"00";
		String end=map.get("maxY")+map.get("maxM")+"99";
	
		map.put("start",start);
		map.put("end",end);
		List<InterviewoneCompany> beans;System.out.println(startYear);
		
	//	beans = ioSer.selectByYear(map);
		beans = ioSer.selectByCompany(map);
		
		
		
		ServletOutputStream out = null;
		ByteArrayOutputStream baos = null;
		ZipOutputStream zos = null;
		InterviewoneService ivService = new InterviewoneService();
		InterviewoneFileService ser= new InterviewoneFileService();
		try {
			baos = new ByteArrayOutputStream();
			zos = new ZipOutputStream(baos, Charset.forName("Cp950")); 

	//		String investNo = params.get("investNo");
		//	String com_name = params.get("cname");
			
		if(beans.size()!=0) {
			
		
	
			
			for(InterviewoneCompany param : beans){
				
	

		  String interviewFno = param.getInterviewFNo();
		  String surveyFNo = param.getSurveyFNo();

				 int ifNo =-1;
				if((type.equals("1")||type.equals("3"))&&interviewFno!=null&&interviewFno.trim().length()!=0){
					for(String strFno : interviewFno.split(",")) {
					ifNo = Integer.valueOf(strFno);
					InterviewoneFile bean =  ser.select(ifNo);
					
					String directory = "訪視紀錄表/";
					//System.out.println(directory+bean.getfName());
					//開啟新entry，把檔案放入ZipOutput
					try {
						zos.putNextEntry(new ZipEntry(new String((directory+bean.getfName()).getBytes(),"Cp950")));
								zos.write(bean.getfContent());
					zos.closeEntry();
					
					}
					catch(java.util.zip.ZipException exception) {}
					catch(java.lang.IllegalArgumentException exception) {
						System.out.println(new String((directory+bean.getfName()).getBytes(),"Cp950")+":"+ isValidName(new String((directory+bean.getfName()).getBytes(),"Cp950")));
					}
					finally{
						
					}
				}
				}
				 int sfNo =-1;
				if((type.equals("2")||type.equals("3"))&&surveyFNo!=null&&surveyFNo.trim().length()!=0){
for(String strFno : surveyFNo.split(",")) {
					sfNo = Integer.valueOf(strFno);

					InterviewoneFile bean =  ser.select(sfNo);
					String	 directory = "問卷/";
				
				
						//開啟新entry，把檔案放入ZipOutput
					try {
						zos.putNextEntry(new ZipEntry(new String((directory+bean.getfName()).getBytes(),"Cp950")));
						zos.write(bean.getfContent());
						zos.closeEntry();
					}
					catch(java.util.zip.ZipException exception) {}
					catch(java.lang.IllegalArgumentException exception) {
						System.out.println(new String((directory+bean.getfName()).getBytes(),"Cp950")+":"+ isValidName(new String((directory+bean.getfName()).getBytes(),"Cp950")));
							}
					finally{
						
					}
				}
				}
				//財務資料下載：用 investNo 取所有財務資料下載
			
				
						

					
			}
		}
		else {
			System.out.println("無資料");
		}
			
			
			
			zos.flush();
			baos.flush();
			zos.close();
			byte[] zipBytes = baos.toByteArray();
			baos.close();
		
			
			/* Write attachment to JSP. */
			String dfilename = DataUtil.getStrUDate().replace("-", "") + "_陸資訪查" +  "相關檔案.zip";
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
					zos.flush();
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
}
