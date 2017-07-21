package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

/**
 * 城市实体
 * @author Administrator
 *
 */
public class CityInfo {
	public String cid = "";
	public String cname = "";
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
	public CityInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.cid = localJSONObject.getString("cid");
			this.cname = localJSONObject.getString("cname");
			//this.content = localJSONObject.getString("content");
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("cid", this.cid);
			localJSONObject1.put("cname", this.cname);
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public CityInfo()
	{
		
	}
	// Json转换成model
	public CityInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setCid(localJSONObject.getString("cid"));
			setCname(localJSONObject.getString("cname"));
			//setContent(localJSONObject.getString("content"));
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}
	
}
