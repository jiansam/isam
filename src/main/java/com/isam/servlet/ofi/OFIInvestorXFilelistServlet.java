package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.isam.bean.OFIInvestorXFile;
import com.isam.service.ofi.OFIInvestorXFileService;

public class OFIInvestorXFilelistServlet extends HttpServlet
{
	/**
	 * 用於取得單一 investorSeq 的所有架構圖  File List
	 */
	
	private OFIInvestorXFileService fSer;
	
	@Override
	public void init() throws ServletException
	{
		fSer = new OFIInvestorXFileService();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String investorSeq = request.getParameter("investorSeq");
		List<OFIInvestorXFile> list = fSer.select(investorSeq);
		Gson gson = new Gson();
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.write(gson.toJson(list));
		out.close();
		return;
	}



	
	
}
