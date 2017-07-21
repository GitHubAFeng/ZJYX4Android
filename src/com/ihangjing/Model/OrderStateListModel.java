package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class OrderStateListModel {
	public ArrayList<OrderStateModel> list;

	public OrderStateListModel() {
		this.list = new ArrayList<OrderStateModel>();
	}

	

	public void stringToBean(String paramString) {
		if (paramString == null || paramString.equals("")) {
			return;
		}
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			
			JSONArray localJSONArray = localJSONObject.getJSONArray("data");
			
			int m = localJSONArray.length();
			OrderStateModel model;
			
				this.list.clear();
				
				for (int i = 0; i < m; i++) {
					model = new OrderStateModel();
					model.jsonToBean(localJSONArray.getJSONObject(i));
					list.add(model);
				}
				//return;
			
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		//return this;
	}
}
