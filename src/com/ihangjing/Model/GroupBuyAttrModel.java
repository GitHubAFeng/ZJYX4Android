package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupBuyAttrModel {//买搭增赠品
	public int cId = 0;
	public String name = "";
	public float price = 0;
	public int count = 1;//当前规格个数
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
