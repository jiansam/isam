package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.helper.DataUtil;
import com.isam.service.COMTBDataService;
import com.isam.service.InterviewoneContentService;
import com.isam.service.InterviewoneService;
import com.isam.service.MoeaicDataService;
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIReInvestListService;

public class InterviewoneShowFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MoeaicDataService MSer;
	private COMTBDataService CSer;
	private InterviewoneService ioSer;
	private InterviewoneContentService iocSer;
	private OFIInvestOptionService opt;
	private OFIInvestListService ofiSer;
	private OFIReInvestListService ofiRSer;
	private Map<String, String> levelone;
	private Map<String, String> leveltwo;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		MSer = new MoeaicDataService();
		CSer = new COMTBDataService();
		ioSer = new InterviewoneService();
		iocSer = new InterviewoneContentService();
		ofiSer=new OFIInvestListService();
		ofiRSer=new OFIReInvestListService();
		opt=new OFIInvestOptionService();
		Map<Integer, Map<String, String>> mapTW=CSer.getTWADDRCode();
		levelone = mapTW.get(1);
		leveltwo = mapTW.get(2);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		List<String> memberUrls=(List<String>) session.getAttribute("memberUrls");
		session.removeAttribute("IOBaseInfo");
		session.removeAttribute("IOLV1");
		session.removeAttribute("IOLV2");
		session.removeAttribute("IOyear");
		session.removeAttribute("IOlastyear");
		session.removeAttribute("ioclists");
		
//		String idno=request.getParameter("idno")==null?"":request.getParameter("idno").trim();
		String investNo=request.getParameter("investNo")==null?"":request.getParameter("investNo").trim();
//		String company=DataUtil.nulltoempty(request.getParameter("company"));
		String reinvestNo=request.getParameter("reinvestNo");
//		System.out.println(reinvestNo);
		String year=request.getParameter("year")==null?"":DataUtil.addZeroForNum(request.getParameter("year").trim(), 3);
		String lastyear=DataUtil.addZeroForNum(String.valueOf(Integer.valueOf(year)-1),3);
		/*interview or survey*/
		String formtype=request.getParameter("formtype")==null?"":request.getParameter("formtype").trim();
		String editType=request.getParameter("editType")==null?"":request.getParameter("editType").trim();
		String getOld=request.getParameter("getOld")==null?"":request.getParameter("getOld").trim();
		
		String url="";
		String reurl="";
		if(formtype.equals("I")){
			url="/console/interviewone/getinterviewone.jsp";
			reurl="/console/interviewone/newinterviewone.jsp";
		}else if(formtype.equals("S")){
			url="/console/interviewone/getsurveyone.jsp";
			reurl="/console/interviewone/newsurveyone.jsp";
		}
		if(investNo.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('陸資案號為必填欄位!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+url+"';</script>");
			out.flush();
			out.close();
			return;
		}
		/*取得基本資料，並驗證資料國內事業是否存在*/
		Map<String,String> baseinfo=MSer.getCNSysBaseInfo(null, investNo.isEmpty()?null:investNo);
//		for (Entry<String, String> m:baseinfo.entrySet()) {
//			System.out.println(m.getKey()+"="+m.getValue());
//		}
		if(reinvestNo!=null&&!reinvestNo.equals("0")){
//			System.out.println("reInfo");
			baseinfo=ioSer.getReInvestNoBaseInfo(year, reinvestNo);
		}
		int qNo=0;
		if(getOld.equals("1")){
			qNo=ioSer.getQNoByYear(lastyear, (reinvestNo!=null&&!reinvestNo.equals("0"))?null:baseinfo.get("investNo"),reinvestNo!=null?reinvestNo:null);
			int qNoCheck=ioSer.getQNoByYear(year, (reinvestNo!=null&&!reinvestNo.equals("0"))?null:baseinfo.get("investNo"),reinvestNo!=null?reinvestNo:null);
//			System.out.println("qNo="+qNo);
			if(qNoCheck==0){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('非年度訪視清單所屬國內事業，請重新確認!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+url+"';</script>");
				out.flush();
				out.close();
				return;
			}
			if(!iocSer.isExist(qNo,formtype)){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				StringBuffer sb = new StringBuffer();
				sb.append("<script language='javascript'>alert('無去年度資料，請直接填寫空白表單!'); window.location.href='http://");
				sb.append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath());
				sb.append("/console/interviewone/getform.jsp?investNo=").append(baseinfo.get("investNo"));
				sb.append("&year=").append(year).append("&formtype=").append(formtype);
				if(reinvestNo!=null){
//					System.out.println("test="+qNo);
					sb.append("&reinvestNo=").append(reinvestNo);
				}
				sb.append("';</script>");
				out.print(sb.toString());
				sb.setLength(0);
				out.flush();
				out.close();
				return;
			}
		}else{
			qNo=ioSer.getQNoByYear(year, (reinvestNo!=null&&!reinvestNo.equals("0"))?null:baseinfo.get("investNo"),reinvestNo!=null?reinvestNo:null);
//			System.out.println("year="+year);
//			System.out.println((reinvestNo!=null&&!reinvestNo.equals("0"))?null:baseinfo.get("investNo"));
		}
//		System.out.println("qNo="+qNo);
		/*驗證訪視紀錄是否已經存在，存在則轉往編輯頁面*/
			if(qNo!=0){
				baseinfo.put("qNo",String.valueOf(qNo));
				if(iocSer.isExist(qNo,formtype)&&editType.isEmpty()&&!getOld.equals("1")){
					if(memberUrls.contains("E0303")){
						request.setCharacterEncoding("UTF-8");
						response.setContentType("text/html;charset=UTF-8");
						PrintWriter out = response.getWriter();
						StringBuffer sb = new StringBuffer();
						sb.append("<script language='javascript'>alert('資料已經存在，即將轉往編輯頁面!'); window.location.href='http://");
						sb.append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath());
						sb.append("/console/interviewone/getform.jsp?investNo=").append(baseinfo.get("investNo"));
						sb.append("&year=").append(year).append("&formtype=").append(formtype);
						if(reinvestNo!=null){
							sb.append("&reinvestNo=").append(reinvestNo);
						}
						sb.append("&editType=edit';</script>");
						out.print(sb.toString());
						sb.setLength(0);
						out.flush();
						out.close();
						return;
					}else{
						request.setCharacterEncoding("UTF-8");
						response.setContentType("text/html;charset=UTF-8");
						PrintWriter out = response.getWriter();
						out.print("<script language='javascript'>alert('資料已經存在，但您無編輯權限，請重新查閱!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+url+"';</script>");
						out.flush();
						out.close();
						return;
					}
				}else if(editType.equals("edit")){
					baseinfo.put("editType","edit");
					session.setAttribute("ioclists", iocSer.select(qNo, formtype));
					baseinfo.put("interviewStatus",ioSer.select(year, String.valueOf(qNo)).get(0).getInterviewStatus());
				}else if(getOld.equals("1")){
					baseinfo.put("editType","add");
					session.setAttribute("ioclists", iocSer.select(qNo, formtype));
					baseinfo.put("qNo",String.valueOf(ioSer.getQNoByYear(year,reinvestNo!=null?null:baseinfo.get("investNo"),reinvestNo!=null?reinvestNo:null)));
				}
				if(reinvestNo==null||reinvestNo.equals("0")){
					baseinfo.put("isOperated", opt.select().get("isOperated").get(ofiSer.select(baseinfo.get("investNo")).getIsOperated()));
				}else{
					baseinfo.put("isOperated", opt.select().get("isOperated").get(ofiRSer.selectbean(baseinfo.get("investNo"),reinvestNo).getIsOperated()));
				}
				baseinfo.put("reinvestNo", reinvestNo);
			}else{
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('非年度訪視清單所屬國內事業，請重新確認!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+url+"';</script>");
				out.flush();
				out.close();
				return;
			}
			
		session.setAttribute("IOBaseInfo", baseinfo);
		session.setAttribute("IOLV1", levelone);
		session.setAttribute("IOLV2", leveltwo);
		session.setAttribute("IOyear", year);
		session.setAttribute("IOlastyear", lastyear);
		String path = request.getContextPath();
		response.sendRedirect(path + reurl);		
	}
}
