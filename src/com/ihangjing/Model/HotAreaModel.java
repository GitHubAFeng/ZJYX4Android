package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

public class HotAreaModel {
	private String unid = "";// 编号
	private String aid = ""; // 编号
	private String name = "";// 名称
	private int level = 1;   // 第几级
	private String lat = ""; // 
	private String lng = ""; // 

	public HotAreaModel() {

	}

	// Json转换成model
	public HotAreaModel(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setUnid(localJSONObject.getString("unid"));
			setAid(localJSONObject.getString("aid"));
			setName(localJSONObject.getString("areaname"));
			setLevel(0);
			setLat("");
			setLng("");
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("HotAreaModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}

	// 生成json
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("unid", getUnid());
			localJSONObject1.put("aid", getAid());
			localJSONObject1.put("name", getName());
			localJSONObject1.put("level", getLevel());
			localJSONObject1.put("lat", getLat());
			localJSONObject1.put("lng", getLng());
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String getUnid() {
		return this.unid;
	}

	public String getAid() {
		return this.aid;
	}
	
	public String getName() {
		return this.name;
	}

	public int getLevel() {
		return this.level;
	}

	public String getLat() {
		return this.lat;
	}

	public String getLng() {
		return this.lng;
	}
	
	public void setUnid(String paramString) {
		this.unid = paramString;
	}
	
	public void setAid(String paramString) {
		this.aid = paramString;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setLevel(int paramString) {
		this.level = paramString;
	}

	public void setLat(String paramString) {
		this.lat = paramString;
	}
	
	public void setLng(String paramString) {
		this.lng = paramString;
	}

	// Json转换成model
	public HotAreaModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}

		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			
			setUnid(localJSONObject.getString("unid"));
			setAid(localJSONObject.getString("aid"));
			setName(localJSONObject.getString("areaname"));
			
			setLevel(0);//Integer.parseInt(localJSONObject.getString("level"))
			setLat("");//localJSONObject.getString("lat")
			setLat("");//localJSONObject.getString("lat")

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}