package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class ReciveAddressListModel {
	public List<ReciveAddressModel> list;
	public int page;
	public int total;
	//public String[] show;
	public ReciveAddressListModel(){
		list = new ArrayList<ReciveAddressModel>();
	}
	public void UpdatGourpShow(){
		int i = list.size();
		
		
	}
	public int StringToBean(String paramString){
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return 0;
		}

		try {
			
			JSONObject localJSONObject = new JSONObject(paramString);
			page = localJSONObject.getInt("page");
			total = 1;//localJSONObject.getInt("total");
			if(page == 1){
				this.list.clear();
			}
			JSONArray localJSONArray = localJSONObject.getJSONArray("list");
			int k = 0;
			int m = localJSONArray.length();
			
			ReciveAddressModel localShopBean1;
			// 循环读取所有的商家信息
			while (true) {
				
				if (k >= m) {
					break;
				}
				localShopBean1 = new ReciveAddressModel(localJSONArray.getJSONObject(k));
				
				this.list.add(localShopBean1);
				k += 1;
			}
			return m;
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
			return -1;
		}
	}
}
