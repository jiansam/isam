package com.isam.service;

import java.util.List;

import com.isam.bean.LoginRecord;
import com.isam.bean.UserMember;
import com.isam.dao.UserLoginDAO;
import com.isam.helper.DataUtil;
import com.isam.helper.ThreeDes;


public class UserHelp {
	UserLoginDAO dao=null;
	public UserHelp(){
		dao = new UserLoginDAO();
	}
	public List<UserMember> select(String idMember,String enable){
		return dao.select(idMember,enable);
	}
	public UserMember isIdMemberExist(String idMember){
		UserMember bean=null;
		List<UserMember> list =dao.select(idMember,null);
		if(list.size()!=0){
			bean = list.get(0);
		}
		return bean;
	}
	public boolean checkLogin(String password,String pWD){
		boolean result=false;
		if(!DataUtil.isEmpty(pWD)){
			result=ThreeDes.checkMD5(password,ThreeDes.toMD5(pWD));
		}
		return result;
	}
	public boolean updateUserMember(UserMember bean){
		boolean result=false;
		if(bean!=null){
			int no = dao.update(bean);
			if(no!=0){
				result=true;
			}
		}
		return result;
	}
	public boolean createUserMember(UserMember bean){
		boolean result=false;
		if(bean!=null){
			int no = dao.insert(bean);
			if(no!=0){
				result=true;
			}
		}
		return result;
	}
	public void loginRecord(LoginRecord bean) {
		dao.insert(bean);
	}
	public List<LoginRecord> getloginRecord(String idMember) {
		return dao.selectTopTen(idMember);
	}
}
