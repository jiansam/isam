package com.isam.servlet.ofi;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.bean.OFINTBTDatas;
import com.isam.service.ofi.OFINTBTDatasService;

import Lara.Utility.ToolsUtil;

public class OFINTBTDatasLoadServlet extends HttpServlet
{

	OFINTBTDatasService service = null;
	
	
	@Override
	public void init() throws ServletException
	{
		super.init();
		service = new OFINTBTDatasService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		ServletOutputStream out = null;
		try {
			
			//檔案id
			int id = ToolsUtil.parseInt(request.getParameter("id"));
			if(id > 0){
				
				//設定cache
				if(request.getProtocol().equalsIgnoreCase("HTTP/1.0")){
					response.setHeader("Pragma", "no-cache");
				}else if(request.getProtocol().equalsIgnoreCase("HTTP/1.1")){
					response.setHeader("Cache-Control", "no-cache");
				}
				
				//取資料
				OFINTBTDatas bean = service.get(id);
				byte[] fileBytes = new byte[0];
				String filename = "";
				if(bean != null){
					filename = bean.getfName();
					fileBytes = bean.getfContent();
				}
				
				//IO
				out = response.getOutputStream();
				if(fileBytes.length > 0){
					response.setContentType("application/x-download");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(filename, "utf-8") + "\"" );
					response.setContentLength(fileBytes.length);
					out.write(fileBytes);
				}
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out!=null){
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
}
