package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import com.google.gson.Gson;
import com.isam.bean.OFINTBTDatas;
import com.isam.bean.UserMember;
import com.isam.service.ofi.OFINTBTDatasService;

import Lara.Utility.ToolsUtil;

public class OFINTBTDatasServlet_bak extends HttpServlet
{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/* 四種情況進入此Servlet（搭配edit-OFINTBTDatas.js
		 * 1.開啟國內事業資料頁面，財務資料下載tag會發動Ajax取出所有資料放回頁面。用ajax傳回FormData，並把contentType拿掉，但也會進入迴圈，也使用parameters map 儲存所有參數
		 * 2.刪除 財務資料下載。用ajax傳回FormData，並把contentType拿掉，但也會進入迴圈，也使用parameters map 儲存所有參數
		 * 3.新增 財務資料下載，isMultipartContent = true，使用 parameters map 儲存所有參數
		 * 4.編輯 財務資料下載，isMultipartContent = true，使用 parameters map 儲存所有參數
		 * */
		
		OFINTBTDatasService service = new OFINTBTDatasService();
		
		//設定儲存上傳檔案的 map-------------------------------------------------------------------------------------------
		HashMap<String, String> parameters = new HashMap<>();
		//HashMap<String, byte[]> files = new HashMap<>(); //這裡只會有一個附檔，所以不用map，直接用JavaBean裝
		OFINTBTDatas bean = new OFINTBTDatas();
		
		if(ServletFileUpload.isMultipartContent(request)){
		
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
		
		
		/* 判斷insert or edit or del or list-----------------------------------------------------------------------------
		 * insert、edit 傳入，type可用map取出
		 * del、list 傳入，是普通form，type可用getParameter取出
		 */
		
		String type = parameters.get("type");
		String investNo = parameters.get("investNo");

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
			
		}else if("del".equals(type)){
			int id = ToolsUtil.parseInt(parameters.get("id"));
			service.unable(id, updateuser);
			
		}else if("list".equals(type)){
		}
		
		ArrayList<OFINTBTDatas> list = service.list(investNo);
		String outstr = "";
		if(list != null && !list.isEmpty()){
			Gson gson = new Gson();
			outstr = gson.toJson(list);
		}else if(list != null && list.isEmpty() && "del".equals(type)){ //刪到最後一筆會沒有list，JS必須有內容才會出alert
			outstr = "delSucess";
		}
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(outstr);
		out.close();
		return;
	}
	
}
