package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GiftTypeModel {
	private String name = "";
	private String sectionId = "0";
	public String parentID = "";
	public List<GiftTypeModel> secList;
	
	public GiftTypeModel(JSONObject jsonObject) throws JSONException {
		secList = new ArrayList<GiftTypeModel>();
		name = jsonObject.getString("ClassName");
		sectionId = jsonObject.getString("ClassId");
		JSONArray localJSONArray = jsonObject.getJSONArray("subdatalist");
		int k = 0;
		int m = localJSONArray.length();
		GiftTypeModel model;
		JSONObject json;
		// 循环读取所有的菜品信息
		while (true) {

			if (k >= m) {
				break;
			}
			model = new GiftTypeModel();
			json = localJSONArray.getJSONObject(k);
			model.setName(json.getString("ClassName"));
			model.setSectionId(json.getString("ClassId"));
			this.secList.add(model);
			k++;
		}
	}
	public GiftTypeModel() {
		// TODO Auto-generated constructor stub
		secList = new ArrayList<GiftTypeModel>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String Id) {
		this.sectionId = Id;
	}
	
}
