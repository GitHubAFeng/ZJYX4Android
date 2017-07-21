package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAccountsListModel {
	public ArrayList<MyAccountRecordModel> list;
	public int page;
	public int total;
	
	public MyAccountsListModel()
	{
		list = new ArrayList<MyAccountRecordModel>();
	}
	
	public void JSonToBean(JSONObject json) throws JSONException{
		page = json.getInt("page");
		if (page == 1) {
			list.clear();
		}
		total = json.getInt("total");
		
		JSONArray array = json.getJSONArray("datalist");
		int m = array.length();
		for (int i = 0; i < m; i++) {
			MyAccountRecordModel model = new MyAccountRecordModel(array.getJSONObject(i));
			list.add(model);
		}
	}
	
}
