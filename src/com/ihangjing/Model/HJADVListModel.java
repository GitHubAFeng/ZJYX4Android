package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class HJADVListModel {
	public ArrayList<HJADVModel> list;
	public int page;// 当前页面
	public int total;// 总页面

	public HJADVListModel() {
		list = new ArrayList<HJADVModel>();

	}

	// sr 0来自网络，1来自本地缓存
	public void JSonToBean(JSONObject json, int type) throws JSONException {
		

		ArrayList<HJADVModel> listCache = new ArrayList<HJADVModel>();
		if (type == 0) {
			total = 1;//json.getInt("total");
			page = 1;//json.getInt("page");
		}else{
			total = 1;
			page = 1;
		}
		
		JSONArray array = json.getJSONArray("foodtypelist");
		int m = array.length();
		HJADVModel model;

		for (int i = 0; i < m; i++) {
			model = new HJADVModel();
			model.JsonToBean(array.getJSONObject(i), 0, type);
			listCache.add(model);
		}
		m = listCache.size();
		int n = list.size();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (listCache.get(i).getDataID() == list.get(j).getDataID()
						&& listCache.get(i).getAdvType() == list.get(j)
								.getAdvType()) {// 本地有缓存
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

	public String BeanToJsonString() {
		JSONObject json = new JSONObject();
		String value = "";
		try {
			json.put("page", String.valueOf(page));
			json.put("total", String.valueOf(total));
			JSONArray localJSONArray1 = new JSONArray();
			Iterator<HJADVModel> localIterator = this.list.iterator();

			while (true) {
				if (!localIterator.hasNext()) {
					json.put("datalist", localJSONArray1);
					break;
				}
				localJSONArray1.put(localIterator.next().BeanToJsonString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		value = json.toString();
		value = value.replace("\\\"", "\"");
		value = value.replace("\"{", "{");
		value = value.replace("}\"", "}");
		value = value.replace("\\", "");
		return value;
	}

	public void JSonToBeanCache(JSONObject json, int type) throws JSONException {
		// TODO Auto-generated method stub
		page = json.getInt("page");
		if (page == 1) {
			list.clear();
		}
		total = json.getInt("total");
		JSONArray array = json.getJSONArray("datalist");
		int m = array.length();
		HJADVModel model;

		for (int i = 0; i < m; i++) {
			model = new HJADVModel();
			model.JsonToBean(array.getJSONObject(i), 1, type);
			list.add(model);
		}
	}
}
