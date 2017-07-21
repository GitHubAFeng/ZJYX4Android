package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GiftListModel {
	private String cacheKey = "";
	public ArrayList<GiftInfo> list;
	private int page;
	private int total;

	public GiftListModel() {
		this.list = new ArrayList<GiftInfo>();
	}

	public String getCacheKey() {
		return this.cacheKey;
	}

	public int getPage() {
		return this.page;
	}
	
	public int getTotal() {
		return this.total;
	}

	public void setCacheKey(String paramString) {
		this.cacheKey = paramString;
	}

	public void setPage(int paramInt) {
		this.page = paramInt;
	}

	public void setTotal(int paramInt) {
		this.total = paramInt;
	}

	public GiftListModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		try {
			this.list.clear();
			JSONObject localJSONObject = new JSONObject(paramString);
			int i = localJSONObject.getInt("page");
			this.page = i;
			int j = localJSONObject.getInt("total");
			this.total = j;
			JSONArray localJSONArray = localJSONObject.getJSONArray("datalist");
			int k = 0;

			// 循环读取所有的商家信息
			while (true) {
				int m = localJSONArray.length();
				if (k >= m) {
					break;
				}
				GiftInfo localShopBean1 = new GiftInfo();
				String str3 = (String) localJSONArray.get(k);
				this.list.add(localShopBean1.stringToBean(str3));
				k += 1;
			}
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
