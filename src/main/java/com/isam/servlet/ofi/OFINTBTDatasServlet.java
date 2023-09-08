package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import com.isam.bean.OFINTBTDatas;
import com.isam.bean.UserMember;
import com.isam.service.ofi.OFINTBTDatasService;

import Lara.Utility.ToolsUtil;

public class OFINTBTDatasServlet extends HttpServlet
{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/* 3種情況進入此Servlet（搭配edit-OFINTBTDatas.js
		 * 1.刪除 財務資料下載。用form表單的submit送出，isMultipartContent = false，參數用request取出
		 * 2.新增 財務資料下載，isMultipartContent = true，使用 parameters map 儲存所有參數
		 * 3.編輯 財務資料下載，isMultipartContent = true，使用 parameters map 儲存所有參數
		 * */
		
		OFINTBTDatasService service = new OFINTBTDatasService();
		
		//設定儲存上傳檔案的 map-------------------------------------------------------------------------------------------
		HashMap<String, String> parameters = new HashMap<>();
		//HashMap<String, byte[]> files = new HashMap<>(); //這裡只會有一個附檔，所以不用map，直接用JavaBean裝
		OFINTBTDatas bean = new OFINTBTDatas();
		boolean isTrue = ServletFileUpload.isMultipartContent(request);
		
		if(isTrue == true){
		
			ServletFileUpload sfu = new ServletFileUpload();
			sfu.setHeaderEncoding("UTF-8");
			
			FileItemIterator iterator = null;
			FileItemStream fis = null;
			InputStream inputStream = null;
			try {
				
				iterator = sfu.getItemIterator(request);
				
				//一個一個項目檢查
				while(iterator.hasNext()){
					fis = iterator.next();
					inputStream = fis.openStream();
					
					if(fis.isFormField()){
						parameters.put(fis.getFieldName(), Streams.asString(inputStream,"UTF-8"));
						
					}else{
						//System.out.println("filename="+fis.getName());
						
						
						if(fis.getName()==null || fis.getName().trim().length()==0){
							//files = null;
							bean.setfName(null);
							bean.setfContent(null);
						}else{
							//files.put(fis.getName(), IOUtils.toByteArray(inputStream));
							bean.setfName(fis.getName());
							bean.setfContent(IOUtils.toByteArray(inputStream));
							
							/* 轉bytes[]方法二
							int len = 0;
							byte[] b = new byte[1024];
							output = new ByteArrayOutputStream();
							while ((len = stream.read(b, 0, b.length)) != -1) {
								output.write(b, 0, len);
							}
							byte[] buffer = output.toByteArray();
							*/
						}
					}
					
					inputStream.close();
				}
				
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}//end if
		
		
		
		//取出user資料--------------------------------------------------------------------------------------------
		HttpSession session = request.getSession();
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();
		
		
		/* 判斷insert or edit or del -----------------------------------------------------------------------------
		 * insert、edit 傳入：enctype="multipart/form-data"。  type可用map取出
		 * del傳入，是普通form：enctype="application/x-www-form-urlencoded"。  type可用getParameter取出
		 */
		
		String type = parameters.get("type")==null? request.getParameter("type") : parameters.get("type");
		String investNo = parameters.get("investNo")==null? request.getParameter("investNo") : parameters.get("investNo");
		String msg = "";
		
		if("add".equals(type)){
			bean.setId(ToolsUtil.parseInt(parameters.get("id")));
			bean.setInvestNo(investNo);
			bean.setTitle(parameters.get("title"));
			bean.setNote(parameters.get("note"));
			bean.setUpdatetime(new Date());
			bean.setUpdateuser(updateuser);
			bean.setCreatetime(new Date()); //第一次新增，之後都要取舊的
			bean.setCreateuser(updateuser);
			bean.setEnable(true);
			service.insert(bean);
			msg = "資料已經新增";
		}else if("edit".equals(type)){
			int id = ToolsUtil.parseInt(parameters.get("id"));
			bean.setId(id);
			bean.setTitle(parameters.get("title"));
			bean.setNote(parameters.get("note"));
			bean.setUpdatetime(new Date());
			bean.setUpdateuser(updateuser);
			if(bean.getfContent() == null){
				OFINTBTDatas olDatas = service.get(id);
				bean.setfName(olDatas.getfName());
				bean.setfContent(olDatas.getfContent());
			}
			service.update(bean);
			msg = "資料已經修改";
		}else if("del".equals(type)){
			int id = ToolsUtil.parseInt(request.getParameter("id"));
			service.unable(id, updateuser);
			msg = "資料已經刪除";
		}
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('"+msg+"!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
		out.flush();
		out.close();
		return;
	}
	
}
