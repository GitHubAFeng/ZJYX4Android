package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPointsListModel {
	public ArrayList<MyPoint> list;
	public int page;
	public int total;
	
	public MyPointsListModel()
	{
		list = new ArrayList<MyPoint>();
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
			MyPoint model = new MyPoint(array.getJSONObject(i));
			list.add(model);
		}
	}
	
}
