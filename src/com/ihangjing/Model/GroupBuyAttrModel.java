package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupBuyAttrModel {//�������Ʒ
	public int cId = 0;
	public String name = "";
	public float price = 0;
	public int count = 1;//��ǰ������
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
