package com.isam.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.service.InterviewoneFileService;

public class InterviewonwFileDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String temp = request.getParameter("fNo");
		int fNo =-1;
		if(temp!=null&&temp.trim().length()!=0){
			fNo = Integer.valueOf(temp);
		}
		InterviewoneFileService ser= new InterviewoneFileService();
		if(fNo>0){
			ser.delete(fNo);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
