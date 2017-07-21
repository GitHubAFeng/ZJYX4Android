package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCouponsListModel {
	public int page;
	public int total;
	public ArrayList<MyCouponModel> list;
	public MyCouponsListModel(){
		list = new ArrayList<MyCouponModel>();
	}
	
	public void JsonToBean(JSONObject json) throws JSONException{
		page = json.getInt("page");
		if (page == 1) {
			list.clear();
		}
		total = json.getInt("total");
		JSONArray arry = json.getJSONArray("datalist");
		int m = arry.length();
		for (int i = 0; i < m; i++) {
			MyCouponModel model = new MyCouponModel(arry.getJSONObject(i));
			list.add(model);
		}
	}
}
