package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class CommentListModel {
	public ArrayList<CommentModel> list;
	public int page = 1;
	public int total = 1;
	public CommentListModel(){
		list = new ArrayList<CommentModel>();
		
	}
	
	public void JSonToBean(JSONObject json) throws JSONException{
		page = json.getInt("page");
		if (page == 1) {
			list.clear();
		}
		total = json.getInt("total");
		JSONArray array = json.getJSONArray("datalist");
		int len = array.length();
		CommentModel model;
		for (int i = 0; i < len; i++) {
			model = new CommentModel();
			model.JSonToBean(array.getJSONObject(i));
			list.add(model);
		}
	}

	public int getPage() {
		// TODO Auto-generated method stub
		return page;
	}

	public int getTotal() {
		// TODO Auto-generated method stub
		return total;
	}
}
