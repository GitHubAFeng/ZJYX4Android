package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

/**
 * 商家分类
 * @author Administrator
 *
 */
public class ShopTypeInfo {
	public String SortID = "";
	public String SortName = "";
	public String JOrder = "";
	private String SortPic;
	

	public ShopTypeInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.SortID = localJSONObject.getString("SortID");
			this.SortName = localJSONObject.getString("SortName");
			this.JOrder = localJSONObject.getString("JOrder");
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("SortID", this.SortID);
			localJSONObject1.put("SortName", this.SortName);
			localJSONObject1.put("JOrder", this.JOrder);
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String getSortPic() {
		return SortPic;
	}

	public void setSortPic(String sortPic) {
		SortPic = sortPic;
	}

	public ShopTypeInfo()
	{
		
	}
	// Json转换成model
	public ShopTypeInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setSortID(localJSONObject.getString("SortID"));
			setSortName(localJSONObject.getString("SortName"));
			setJOrder(localJSONObject.getString("JOrder"));
			SortPic = localJSONObject.getString("SortPic");
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}

	public String getSortID() {
		return SortID;
	}

	public void setSortID(String sortID) {
		SortID = sortID;
	}

	public String getSortName() {
		return SortName;
	}

	public void setSortName(String sortName) {
		SortName = sortName;
	}

	public String getJOrder() {
		return JOrder;
	}

	public void setJOrder(String jOrder) {
		JOrder = jOrder;
	}
	
}
