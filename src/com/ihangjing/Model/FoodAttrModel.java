package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class FoodAttrModel {
	public int cId = 0;
	public String name = "";
	public float price = 0;
	public int count = 1;//当前规格个数
	public float oldPrice = 0.0f;//原价，市场价
	public ArrayList<FoodAttrModel> listAttr;
	//int attrDetail = 0;//
	public FoodAttrModel(JSONObject js) {
		// TODO Auto-generated constructor stub
		
		try {
			cId = js.getInt("attrid");
			name = js.getString("attrTitle");
			JSONArray array = js.getJSONArray("foodattrdetail");
			listAttr = new ArrayList<FoodAttrModel>();
			if (array.length() > 0) {
				
				FoodAttrModel model;
				for (int i = 0; i < array.length(); i++) {
					//attrDetail++;
					model = new FoodAttrModel();
					model.JsonToBean(array.getJSONObject(i));
					listAttr.add(model);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public FoodAttrModel() {
		// TODO Auto-generated constructor stub
	}
	public void JsonToBean(JSONObject js){
		try {
			cId = js.getInt("attrid");
			name = js.getString("attrTitle");
			price = (float)js.getDouble("price");
			oldPrice = price;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public Object beanToString() {
		// TODO Auto-generated method stub
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("DataId", String.valueOf(cId));
			localJSONObject1.put("Title", name);
			localJSONObject1.put("Price", String.valueOf(price));
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			return "";
		}
		
	}
}
