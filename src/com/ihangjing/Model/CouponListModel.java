package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CouponListModel {
	private String cacheKey = "";
	public ArrayList<CouponInfo> list;
	private int page = 0;
	private int total = 0;

	public CouponListModel() {
		this.list = new ArrayList<CouponInfo>();
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			localJSONObject1.put("page", this.page);

			localJSONObject1.put("total", this.total);

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<CouponInfo> localIterator = this.list.iterator();

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
	
	public int CheckModeIsIn(CouponInfo coupon){
		if(list == null || list.size() == 0){
			return -1;
		}else{
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).getCID() == coupon.CID){
					return i;
				}
			}
			return -1;
		}
	}

	public CouponListModel stringToBean(String paramString) {
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
				CouponInfo localShopBean1 = new CouponInfo();
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
