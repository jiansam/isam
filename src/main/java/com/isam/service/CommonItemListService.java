package com.isam.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.isam.bean.CommonItemList;
import com.isam.dao.CommonItemListDAO;

public class CommonItemListService {
	CommonItemListDAO dao = null;
	public CommonItemListService(){
		dao = new CommonItemListDAO();
	}
	public Map<String,List<CommonItemList>> getCommonItemMap(){
		return dao.getCommonItemMap();
	}
	public int insert(CommonItemList bean) {
		return dao.insert(bean);
	}
	public int update(CommonItemList bean) {
		return dao.update(bean);
	}
	public int unable(String idno) {
		return dao.unable(idno);
	}
	public int select(String idno){
		return dao.select(idno);
	}
	public ArrayList<String> getCommonItemString()
	{
		ArrayList<String> mStrings = new ArrayList<>();
		Map<String,List<CommonItemList>> map = dao.getCommonItemMap();
		if(map != null && !map.isEmpty()) {
			for(String key : map.keySet()) {
				for(CommonItemList bean : map.get(key)) {
					mStrings.add(bean.getCname());
				}
			}
		}
		return mStrings;
	}
	public ArrayList<CommonItemList> get(){
		return dao.get();
	}
}
