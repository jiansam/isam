/********************
 * 營運狀況調查問卷後台上傳功能
 * 
 ********************/

package com.isam.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.SurveyFile;
import com.isam.dao.FilledSurveyFileDAO;

import Lara.Utility.DateUtil;
import Lara.Utility.ToolsUtil;

public class FilledSurveyFileManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	String doThing;
	FilledSurveyFileDAO dao;
	HttpSession session;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		session = request.getSession();
		session.removeAttribute("yearS");
		session.removeAttribute("year_files");
		session.removeAttribute("yearRange");
		
		doThing = request.getParameter("doThing");
		dao = new FilledSurveyFileDAO();
		
		if(doThing == null || "front".equals(doThing)) {
			doIndex(request, response);
		}
		else if("remove".equals(doThing)) {
			doRemove(request, response);
		}
	}


	private void doRemove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int id = ToolsUtil.parseInt(request.getParameter("id"));
		dao.deleteFile(id);
		session.setAttribute("result", "檔案已移除");
		doIndex(request, response);
	}

	private void doIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Map<Integer, Map<String, SurveyFile>> map = dao.getAll();
		ArrayList<String> surveyType = new ArrayList<>(Arrays.asList("Abroad","Service","NonService","mainland"));
		Map<String, ArrayList<SurveyFile>> year_files = new HashMap<>();
		
		ArrayList<String> yearS = new ArrayList<>();
		if(!map.isEmpty()) {
			for(int year : map.keySet()) {
				yearS.add(0, String.valueOf(year));;
				Map<String, SurveyFile> type_file = map.get(year);
				ArrayList<SurveyFile> files = new ArrayList<>();
				for(String type : surveyType) {
					files.add(type_file.get(type));
				}
				year_files.put(String.valueOf(year), files);
			}
			
		}
		session.setAttribute("yearS", yearS);
		session.setAttribute("year_files", year_files);
		session.setAttribute("yearRange", DateUtil.getChineseYearRange(98));
		
		String path = request.getContextPath();
		
		if("front".equals(doThing)) {
			//前台轉頁
			response.sendRedirect(path + "/survey/surveymanagement.jsp");
		}else {
			//後台轉頁
			response.sendRedirect(path + "/console/survey/surveymanagement.jsp");
		}
		return;
	}
}
