package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyMessageListModel {
	public ArrayList<MyMessageModel> list;
	public int page;
	public int total;
	public MyMessageListModel(){
		list = new ArrayList<MyMessageModel>();
	}
	public void JSonToBean(JSONObject json, String userid) throws JSONException{
		page = json.getInt("page");
		total = json.getInt("total");
		if (page == 1) {
			list.clear();
		}
		JSONArray array = json.getJSONArray("datalist");
		int m = array.length();
		for (int i = 0; i < m; i++) {
			MyMessageModel model = new MyMessageModel(array.getJSONObject(i), userid);
			list.add(model);
		}
	}
}
