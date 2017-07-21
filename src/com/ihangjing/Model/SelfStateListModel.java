package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class SelfStateListModel {
	public ArrayList<SelfStateModel> list;
	public String stateName[];
	public SelfStateListModel() {
		this.list = new ArrayList<SelfStateModel>();
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<SelfStateModel> localIterator = this.list.iterator();

			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("foodtypelist", localJSONArray1);
					String value = localJSONObject1.toString();
					value = value.replace("\\\"", "\"");
					value = value.replace("\"{", "{");
					value = value.replace("}\"", "}");
					value = value.replace("\\", "");
					return value;
				}
				localJSONArray1.put(localIterator.next().beanToString());
			}
		} catch (JSONException localJSONException) {
			return "";
		}
	}

	public void stringToBean(String paramString) throws JSONException {

		JSONObject localJSONObject = new JSONObject(paramString);

		JSONArray localJSONArray = localJSONObject.getJSONArray("foodtypelist");

		int m = localJSONArray.length();
		SelfStateModel model;
		this.list.clear();
		stateName = new String[m];
		for (int i = 0; i < m; i++) {
			model = new SelfStateModel();
			model.jsonToBean(localJSONArray.getJSONObject(i));
			stateName[i] = model.getName();
			list.add(model);
		}

	}
}
