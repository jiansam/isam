package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;
import com.isam.dao.WebFolderAuthority;
import com.isam.helper.DataUtil;
import com.isam.helper.ThreeDes;
import com.isam.service.UserHelp;

public class EditUserServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Map<String, String> errors=new HashMap<String, String>();
	    String editor=request.getParameter("editor");	
	    String type=request.getParameter("type");	
	    String idMember=request.getParameter("user");	
		String newStr=request.getParameter("newStr");
		String checkStr=request.getParameter("checkStr");
		String company=request.getParameter("company");
		String username=request.getParameter("owner");
		String userEmail=request.getParameter("mail");
		String authority=request.getParameter("authority");
		String able=request.getParameter("able");
		String rangeStr = request.getParameter("rangeStr");
		String[] rangeTemp = rangeStr.split(",");
		List<String> range = new ArrayList<String>();
		if(rangeTemp!=null){
			for (String code:rangeTemp) {
				range.add(code.trim());
			}
		}
		
		/*檢查用MAP*/
		Map<String,String> paramMap=new HashMap<String,String>();
		paramMap.put("idMember",idMember);
		paramMap.put("newStr",newStr);
		paramMap.put("checkStr",checkStr);
		paramMap.put("company",company);
		paramMap.put("username",username);
		paramMap.put("userEmail",userEmail);
		paramMap.put("authority",authority);
		paramMap.put("able",able);
		
		UserHelp user = new UserHelp();
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		if(DataUtil.isEmpty(type)||DataUtil.isEmpty(editor)||!editor.equals(bean.getIdMember())){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('請確認您有修改此帳號的權限!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"';</script>");
			out.flush();
			out.close();
			return;
		}
		if(type.equals("edit")&&DataUtil.isEmpty(newStr)){
			paramMap.remove("newStr");
			paramMap.remove("checkStr");
		}
		for(Map.Entry<String, String> s:paramMap.entrySet()){
			if(DataUtil.isEmpty(s.getValue())){
				errors.put("empty"+s.getKey(),"此欄位不可為空白。");
			}
		}
		
		WebFolderAuthority webFolder=new WebFolderAuthority();
		List<String> urlCode= webFolder.getCodeList();
		if(range.size()==0){
			errors.put("emptyRange","請選取一項以上之權限項目");
		}else{
			for (String code:range) {
				if(!urlCode.contains(code)){
					errors.put("emptyRange","包含不存在的權限項目");
				}
			}
		}
		if(errors.size()==0){
			if(!DataUtil.isEmpty(newStr)){
				if(!newStr.equals(checkStr)){
					errors.put("notEqual", "您兩次輸入的新密碼不一致，請重新輸入。");
				}else if(!DataUtil.isMatchPtn(newStr,"[A-Za-z0-9\\p{Punct}]{6,}")){
					errors.put("noMatch", "您輸入的新密碼不符合密碼的格式，請以6個以上英數字或符號組成新密碼。");
				}
			}
			if(!DataUtil.isMatchEmail(userEmail)){
				errors.put("notMail", "請輸入正確格式的Email。");
			}
			if(!DataUtil.isMatchTWWord(username)){
				errors.put("notTWWord", "請輸入中文姓名。");
			}
		}
		if(!authority.equals("super")){
			authority="user";
		}
		if(!able.equals("1")){
			able="0";
		}
		if(!company.equals("moea")&&!company.equals("cier")){
			company="ibtech";
		}
		if(!type.equals("add")){
			type="edit";
		}
		UserMember newBean =user.isIdMemberExist(idMember);
		if(type.equals("add")&&newBean!=null){
			errors.put("sameId", "此帳號已有人使用，請重新輸入。");
		}else if(type.equals("edit")&&newBean==null){
			errors.put("notExistId", "此帳號不存在，請檢查後重新輸入。");
		}
		if(type.equals("add")){
			if(!DataUtil.isMatchPtn(idMember,"\\w*")){
				errors.put("accRule", "帳號請以英文大小寫，數字和底線組合，大小寫視為不同文字。");
			}
		}
//		for(Map.Entry<String, String> m:errors.entrySet()){
//			System.out.println(m.getKey()+":"+m.getValue());
//		}
		if(errors.size()>0){
			request.setAttribute("errors", errors);
			if(type.equals("add")){
				request.getRequestDispatcher("/console/admin/adduser.jsp").forward(request, response);
				return;
			}else{
				request.getRequestDispatcher("/getedituser.jsp?user="+idMember).forward(request, response);
				return;
			}
		}
		if(type.equals("add")&&newBean==null){
			newBean= new UserMember();
			newBean.setCreatetime(DataUtil.getNowTimestamp());
			newBean.setIdCreatetor(editor);
		}
		if(type.equals("edit")&&DataUtil.isEmpty(newStr)){
			newBean.setUserPwd(newBean.getUserPwd());
		}else{
			newBean.setUserPwd(ThreeDes.toMD5(newStr));
		}
		newBean.setIdMember(idMember);
		newBean.setCompany(company);
		newBean.setUsername(username);
		newBean.setUserEmail(userEmail);
		newBean.setGroupId(authority);
		newBean.setIdEditor(editor);
		newBean.setUpdtime(DataUtil.getNowTimestamp());
		newBean.setEnable(able);
		
		if(type.equals("edit")){
			user.updateUserMember(newBean);
		}else{
			user.createUserMember(newBean);
		}
		int count=0;
		webFolder.delete(idMember);
		for (String code:range) {
			count+=webFolder.insert(idMember, code);
		}
		if(editor.equals(idMember)){
			List<String> passList = webFolder.getURLByIdMember(idMember);
			List<String> checkUrls = webFolder.checkList(passList);
			session.setAttribute("checkUrls", checkUrls);
			List<String> memberUrls=webFolder.getMenuItemByIdMember(idMember);
			session.setAttribute("memberUrls", memberUrls);
		}
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('帳號已經編輯完成，即將返回列表。'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/userlist.jsp';</script>");
		out.flush();
		out.close();
		return;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
