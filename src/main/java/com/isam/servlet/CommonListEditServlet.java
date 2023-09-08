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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.bean.CommonItemList;
import com.isam.bean.UserMember;
import com.isam.service.CommonItemListService;

public class CommonListEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommonItemListService ciSer;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ciSer=new CommonItemListService();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String edittype=request.getParameter("edittype")==null?"":request.getParameter("edittype").trim();
		String type=request.getParameter("type")==null?"":request.getParameter("type").trim();
		String content=request.getParameter("content")==null?"":request.getParameter("content").trim();
		String idno=request.getParameter("idno")==null?"":request.getParameter("idno").trim();
		
		HttpSession session = request.getSession();
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();
		
		Map<String,List<CommonItemList>> map = ciSer.getCommonItemMap();
		JSONArray jarray = new JSONArray();
		JSONObject obj=new JSONObject();
		if(map.containsKey(type)){
			CommonItemList bean = new CommonItemList();
			bean.setCname(content);
			bean.setType(type);
			bean.setUpdateuser(updateuser);
			if(edittype.equals("add")){
				idno=String.valueOf(ciSer.insert(bean));
			}else if(edittype.equals("edit")||edittype.equals("unable")){
				bean.setIdno(idno);
				if(ciSer.select(idno)!=0){
					if(edittype.equals("edit")){
						ciSer.update(bean);
					}else{
						ciSer.unable(idno);
					}
				}
			}
		}
		obj.put("idno", idno);
		obj.put("edittype", edittype);
		obj.put("content", content);
		jarray.add(obj);
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();
		return;
	}
}
