package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class FoodSizeModel {
	public int cId = 0;
	public int foodId = 0;
	public String name = "";
	public float price = 0;
	public int count = 1;//��ǰ������
	public float oldPrice = 0.0f;//ԭ�ۣ��г���
	public FoodSizeModel(JSONObject js) {
		// TODO Auto-generated constructor stub
		try {
			cId = js.getInt("DataId");
			foodId = js.getInt("FoodtId");
			name = js.getString("Title");//������ƣ���Ϊ�գ�ֻ��һ�����ʱ��
			price = (float)js.getDouble("Price");
			oldPrice = price;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public FoodSizeModel() {
		// TODO Auto-generated constructor stub
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
