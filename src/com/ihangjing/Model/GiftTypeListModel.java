package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GiftTypeListModel {
	public List<GiftTypeModel> list;
	
	public int page = 1;
	public int total = 1;	
	
	public GiftTypeListModel(){
		list = new ArrayList<GiftTypeModel>();
	}
	public int FindCityWitchName(String name){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getName().contains(name)){
				return i;
			}
		}
		return -1;
	}
	public void stringToBean(JSONObject json) throws JSONException {

		JSONArray localJSONArray = json.getJSONArray("datalist");
		int k = 0;
		int m = localJSONArray.length();
		if(m > 0){
			GiftTypeModel model = new GiftTypeModel();
			model.setName("全部");
			model.setSectionId("0");
			this.list.add(model);
			// 循环读取所有的菜品信息
			while (true) {
	
				if (k >= m) {
					break;
				}
				model = new GiftTypeModel(
						localJSONArray.getJSONObject(k));
				this.list.add(model);
				k++;
			}
		}
		//return this;
	}
}
