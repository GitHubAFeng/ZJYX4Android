package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFriendsList {
	public ArrayList<MyFriends> list;
	public int page;
	public int total;
	public MyFriendsList(){
		list = new ArrayList<MyFriends>();
	}
	
	public void jsonToBean(JSONObject json) throws JSONException{
		page = json.getInt("page");
		total = json.getInt("total");
		JSONArray array = json.getJSONArray("datalist");
		if (page == 1) {
			list.clear();
		}
		int m = array.length();
		for (int i = 0; i < m; i++) {
			MyFriends model = new MyFriends(array.getJSONObject(i));
			list.add(model);
		}
	}
}
