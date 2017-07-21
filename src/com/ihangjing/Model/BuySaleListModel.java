package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuySaleListModel {
	public ArrayList<BuySaleModel> list;
	private int page;
	private int total;
	
	
	public ArrayList<BuySaleModel> getList() {
		return list;
	}

	public void setList(ArrayList<BuySaleModel> list) {
		this.list = list;
	}

	public BuySaleListModel() {
		this.list = new ArrayList<BuySaleModel>();
	}

	public int getPage() {
		return this.page;
	}

	public int getTotal() {
		return this.total;
	}

	public void setPage(int paramInt) {
		this.page = paramInt;
	}

	public void setTotal(int paramInt) {
		this.total = paramInt;
	}

	public void stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return;
		}

		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.page = localJSONObject.getInt("page");
			this.total = localJSONObject.getInt("total");
			if (page == 1) {
				list.clear();
			}
			JSONArray localJSONArray = localJSONObject.getJSONArray("datalist");
			int k = 0;

			// 循环读取所有的菜品信息
			int m = localJSONArray.length();
			while (true) {

				if (k >= m) {
					break;
				}
				BuySaleModel localShopBean1 = new BuySaleModel();
				localShopBean1.JsonToBean(localJSONArray.getJSONObject(k), 0);
				this.list.add(localShopBean1);
				k += 1;
			}
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return;
	}
}
