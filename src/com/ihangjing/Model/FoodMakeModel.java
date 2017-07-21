package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class FoodMakeModel {//商品做法
	private String name = "";
	private int checkType = 0;//0单选 1 多选
	private int check = 0;//0必选 1非必选
	public List<FoodAttrModel> listAttr;
	public FoodMakeModel(JSONObject json) throws JSONException {
		try{
			listAttr = new ArrayList<FoodAttrModel>();
			JSONObject localJSONObject = json;
			name = localJSONObject.getString("Title");
			checkType = localJSONObject.getInt("tempattrlist");
			check = localJSONObject.getInt("Inve1");
			String makeAttr = localJSONObject.getString("Attributes");
			makeAttr = makeAttr.replace("?", "-");
			String make[] = makeAttr.split("#");
			String V;
			for(int i = 0; i < make.length; i++){
				FoodAttrModel mode = new FoodAttrModel();
				V = make[i];
				String value[] = V.split("-");
				mode.name = value[0];
				mode.price = Float.parseFloat(value[1]);
				listAttr.add(mode);
			}
		}catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		
	}
	public FoodMakeModel(String name, int checktype, int check){
		this.name = name;
		checkType = checktype;
		this.check = check;
		listAttr = new ArrayList<FoodAttrModel>();
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCheckType() {
		return checkType;
	}
	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}
	public int getCheck() {
		return check;
	}
	public void setCheck(int check) {
		this.check = check;
	}

}
