package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//商家列表
public class ShopListModel {
	private String cacheKey = "";
	private String desc = "";
	public ArrayList<ShopListItemModel> list;
	private int page;
	private String status = "";
	private int total;

	public ShopListModel() {
		this.list = new ArrayList<ShopListItemModel>();
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			localJSONObject1.put("status", this.status);

			localJSONObject1.put("desc", this.desc);

			localJSONObject1.put("page", this.page);

			localJSONObject1.put("total", this.total);

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<ShopListItemModel> localIterator = this.list.iterator();

			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("list", localJSONArray1);
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

	public String getDesc() {
		return this.desc;
	}

	public int getPage() {
		return this.page;
	}

	public String getStatus() {
		return this.status;
	}

	public int getTotal() {
		return this.total;
	}

	public void setCacheKey(String paramString) {
		this.cacheKey = paramString;
	}

	public void setDesc(String paramString) {
		this.desc = paramString;
	}

	public void setPage(int paramInt) {
		this.page = paramInt;
	}

	public void setStatus(String paramString) {
		this.status = paramString;
	}

	public void setTotal(int paramInt) {
		this.total = paramInt;
	}

	public ShopListModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}

		try {
			this.list.clear();
			JSONObject localJSONObject = new JSONObject(paramString);
			String str1 = localJSONObject.getString("status");
			this.status = str1;
			String str2 = localJSONObject.getString("desc");
			this.desc = str2;
			int i = localJSONObject.getInt("page");
			this.page = i;
			int j = localJSONObject.getInt("total");
			this.total = j;
			JSONArray localJSONArray = localJSONObject.getJSONArray("list");
			int k = 0;
			
			//循环读取所有的商家信息
			while (true) {
				int m = localJSONArray.length();
				if (k >= m) {
					break;
				}
				ShopListItemModel localShopBean1 = new ShopListItemModel();
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
