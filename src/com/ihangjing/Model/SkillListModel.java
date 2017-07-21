package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SkillListModel {
	private String cacheKey = "";
	public ArrayList<SkillInfo> list;
	private int page;
	private int total;

	public SkillListModel() {
		this.list = new ArrayList<SkillInfo>();
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			localJSONObject1.put("page", this.page);

			localJSONObject1.put("total", this.total);

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<SkillInfo> localIterator = this.list.iterator();

			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("datalist", localJSONArray1);
					return localJSONObject1.toString();
				}
				localJSONArray1.put(localIterator.next()
						.beanToString());
			}
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
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

	public SkillListModel stringToBean(String paramString) {
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
				SkillInfo localShopBean1 = new SkillInfo();
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
