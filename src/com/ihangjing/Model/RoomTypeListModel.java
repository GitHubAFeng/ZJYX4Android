package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoomTypeListModel {
	private String cacheKey = "";
	public ArrayList<RoomTypeInfo> list2;
	public ArrayList<RoomTypeInfo> list1;
	private int page;
	private int total;

	public RoomTypeListModel() {
		this.list2 = new ArrayList<RoomTypeInfo>();
		this.list1 = new ArrayList<RoomTypeInfo>();
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			localJSONObject1.put("page", this.page);

			localJSONObject1.put("total", this.total);

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<RoomTypeInfo> localIterator = this.list2.iterator();

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

	public RoomTypeListModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		try {
			this.list2.clear();
			JSONObject localJSONObject = new JSONObject(paramString);
			int i = localJSONObject.getInt("page");
			this.page = i;
			int j = localJSONObject.getInt("total");
			this.total = j;
			JSONArray localJSONArray = localJSONObject.getJSONArray("datalist");
			RoomTypeInfo localShopBean1;
			int m = localJSONArray.length();
			int k = 0;
			// 循环读取所有的商家信息
			while (true) {
				
				if (k >= m) {
					break;
				}
				localShopBean1 = new RoomTypeInfo();
				//String str3 = (String) localJSONArray.get(k);
				localShopBean1.stringToBean(localJSONArray.getJSONObject(k));
				if (localShopBean1.tabtype == 0) {
					this.list1.add(localShopBean1);
				}else{
					this.list2.add(localShopBean1);
				}
				
				k += 1;
			}
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
	
	public void JSONToBin(JSONObject localJSONObject) throws JSONException{
		this.list2.clear();
		
		int i = localJSONObject.getInt("page");
		this.page = i;
		int j = localJSONObject.getInt("total");
		this.total = j;
		JSONArray localJSONArray = localJSONObject.getJSONArray("datalist");
		int k = 0;
		RoomTypeInfo localShopBean1;
		int m = localJSONArray.length();
		// 循环读取所有的商家信息
		while (true) {
			
			if (k >= m) {
				break;
			}
			localShopBean1 = new RoomTypeInfo();
			//String str3 = (String) localJSONArray.get(k);
			localShopBean1.stringToBean(localJSONArray.getJSONObject(k));
			if (localShopBean1.tabtype == 0) {
				this.list1.add(localShopBean1);
			}else{
				this.list2.add(localShopBean1);
			}
			
			k += 1;
		}
	}
}