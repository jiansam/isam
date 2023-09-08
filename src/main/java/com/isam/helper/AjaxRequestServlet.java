package com.isam.helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.isam.bean.InterviewoneContent;
import com.isam.service.InterviewoneContentService;

public class AjaxRequestServlet extends HttpServlet
{
	InterviewoneContentService itvOneContentSV = null;
	Gson gson = null;
	
	@Override
	public void init() throws ServletException
	{
		itvOneContentSV = new InterviewoneContentService();
		gson = new Gson();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String doThing = request.getParameter("doThing");
		
		//取出訪視公司 訪視異常項目
		if(doThing != null && "error_Itv".equals(doThing)){
			getInterviewError(request, response);
		}
		//取出訪視公司 財務異常項目
		else if(doThing != null && "error_fia".equals(doThing)){
			getFinancialError(request, response);
		}
	}
	
	
	//取出訪視公司 訪視異常項目
	private void getInterviewError(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String qNo = request.getParameter("id");
		ArrayList<InterviewoneContent> list = itvOneContentSV.getInterviewError(qNo);
		
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.write(gson.toJson(list));
		out.close();
		return;
	}
	
	//取出訪視公司 財務異常項目
	private void getFinancialError(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String qNo = request.getParameter("id");
		ArrayList<InterviewoneContent> list = itvOneContentSV.getFinancialError(qNo);
		
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.write(gson.toJson(list));
		out.close();
		return;
	}



	
}
