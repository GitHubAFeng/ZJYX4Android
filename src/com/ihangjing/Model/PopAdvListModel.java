package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PopAdvListModel {
	int page;
	int total;
	public int popType = 0;//0获取广告弹出，1资源中图片弹出
	public ArrayList<PopAdvModel> list;
	public PopAdvListModel(){
		list = new ArrayList<PopAdvModel>();
	}
	public void JsonToBean(JSONObject json) throws JSONException{
		page  = 1;//json.getInt("page");
		total = 1;//json.getInt("total");
		JSONArray array = json.getJSONArray("foodtypelist");
		int m = array.length();
		for (int i = 0; i < m; i++) {
			PopAdvModel model = new PopAdvModel(array.getJSONObject(i));
			list.add(model);
		}
	}
	
	public void ShopZJSonToBean(JSONObject json) throws JSONException{
		page  = 1;//json.getInt("page");
		total = 1;//json.getInt("total");
		JSONArray array = json.getJSONArray("datalist");
		int m = array.length();
		for (int i = 0; i < m; i++) {
			PopAdvModel model = new PopAdvModel();
			model.ShopZJSONToBean(array.getJSONObject(i));
			list.add(model);
		}
	}
}
