package com.isam.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
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
import org.dasin.tools.dPairHashMap;

import com.isam.bean.SurveyFile;
import com.isam.dao.FilledSurveyFileDAO;

import Lara.Utility.DateUtil;
import Lara.Utility.ToolsUtil;

public class FilledSurveyFileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	FilledSurveyFileDAO dao;
	SurveyFile bean;
	HttpSession session;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
		InputStream is = null;
		HashMap<String, String> parameters = new HashMap<>();
		String file_title = "";
		byte[] file_content = null;
		
		if(ServletFileUpload.isMultipartContent(request)){
			ServletFileUpload sfu = new ServletFileUpload();
			sfu.setHeaderEncoding("UTF-8");
			FileItemIterator iterator;
			try {
				iterator = sfu.getItemIterator(request);
				//一個一個項目檢查
				while(iterator.hasNext()){
					FileItemStream fis = iterator.next();
					InputStream inputStream = fis.openStream();
					if(fis.isFormField()){
						parameters.put(fis.getFieldName(), Streams.asString(inputStream,"UTF-8"));
					}else{
//						System.out.println("filename="+fis.getName());
						if(fis.getName()==null || fis.getName().trim().length()==0){
							file_title = "";
						}else{
							file_title = fis.getName();
							file_content = IOUtils.toByteArray(inputStream);
						}
					}
					inputStream.close();
				}
				
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
		
		
		String doThing = parameters.get("doThing");
		bean = new SurveyFile();
		bean.setId(ToolsUtil.parseInt(parameters.get("id")));
		bean.setYear(ToolsUtil.parseInt(parameters.get("year")));
		bean.setType(ToolsUtil.trim(parameters.get("type")));
		bean.setFile_title(file_title);
		bean.setFile_content(file_content);
		bean.setDisable(false);
		
		dao = new FilledSurveyFileDAO();
		int year = bean.getYear();
		String type = bean.getType();

		session = request.getSession();
		if( dao.isExist(year, type) ) {
			// 該年度、該問卷種類已存在
			dao.updateSurveyFile(bean);
			session.setAttribute("result", "該年度該問卷種類已存在。");
		}else{
			dao.insertSurveyFile(bean);
			session.setAttribute("result", "已成功新增問卷。");
		}
		
		getServletContext().getNamedDispatcher("FilledSurveyFileServlet").forward(request, response);
	}

}
