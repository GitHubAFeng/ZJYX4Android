package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TagListModel {
	int page;
	int total;
	public int popType = 0;//0获取广告弹出，1资源中图片弹出
	public ArrayList<TagModel> list;
	public TagListModel(){
		list = new ArrayList<TagModel>();
	}
	public void JsonToBean(JSONObject json) throws JSONException{
		page  = 1;//json.getInt("page");
		total = 1;//json.getInt("total");
		JSONArray array = json.getJSONArray("taglist");
		int m = array.length();
		for (int i = 0; i < m; i++) {
			TagModel model = new TagModel(array.getJSONObject(i));
			list.add(model);
		}
	}
}
