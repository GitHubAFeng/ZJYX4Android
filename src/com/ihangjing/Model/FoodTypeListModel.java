package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class FoodTypeListModel {
	public ArrayList<FoodTypeModel> list;

	public FoodTypeListModel() {
		this.list = new ArrayList<FoodTypeModel>();
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<FoodTypeModel> localIterator = this.list.iterator();

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
				localJSONArray1.put(localIterator.next()
						.beanToString());
			}
		} catch (JSONException localJSONException) {
			return "";
		}
	}

	public void stringToBean(String paramString, int sr, String allType) {
		if (paramString == null || paramString.equals("")) {
			return;
		}
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			
			JSONArray localJSONArray = localJSONObject.getJSONArray("foodtypelist");
			
			int m = localJSONArray.length();
			FoodTypeModel model;
			if (sr == 1) {
				this.list.clear();
				model = new FoodTypeModel();
				model.setId(0);
				model.setName(allType/*"全部分类"*/);
				list.add(model);
				for (int i = 0; i < m; i++) {
					model = new FoodTypeModel();
					model.jsonToBean(localJSONArray.getJSONObject(i), sr);
					list.add(model);
				}
				//return;
			}else{
				ArrayList<FoodTypeModel> listCache = new ArrayList<FoodTypeModel>();
				for (int i = 0; i < m; i++) {
					model = new FoodTypeModel();
					model.jsonToBean(localJSONArray.getJSONObject(i), sr);
					listCache.add(model);
				}
				m = listCache.size();
				int n = list.size();
				for (int i = 0; i < m; i++) {
					for (int j = 0; j < n; j++) {
						if (listCache.get(i).getId() == list.get(j).getId()){// 本地有缓存
							listCache.get(i).setImageLocalPath(list.get(j).getImageLocalPath());
							if (listCache.get(i).getImageNetPath().equals("")
									|| listCache.get(i).getImageNetPath()
											.equals(list.get(j).getImageNetPath())) {// 新的下载地址为空，或者和旧地址相同，那不需要下载
								//listCache.get(i).isDowload = false;
							}
							//要不要删除已经比对过的呢？删除了是否印象UI的闪退问题
							break;
						}
					}
				}
				list = listCache;
			}
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		//return this;
	}
}
