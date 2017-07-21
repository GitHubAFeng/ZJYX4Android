package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

public class BuildingModel extends BaseBean {
	public String unid = "";
	public String aid = "";
	public String aname = "";
	public String mapx = "";
	public String stype = "";

	public BuildingModel()
	{
		
	}
	// Json转换成model
	public BuildingModel(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setUnid(localJSONObject.getString("unid"));
			setAid(localJSONObject.getString("aid"));
			setAname(localJSONObject.getString("areaname"));
			setMapx(localJSONObject.getString("mapx")); 
			setStype(localJSONObject.getString("stype"));
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("BuildingModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}
	
	public String getUnid() {
		return this.unid;
	}

	public String getAid() {
		return this.aid;
	}
	
	public String getAname() {
		return this.aname;
	}

	public String getMapx() {
		return this.mapx;
	}

	public String getStype() {
		return this.stype;
	}
	
	public void setUnid(String paramString) {
		this.unid = paramString;
	}
	
	public void setAname(String paramString) {
		this.aname = paramString;
	}

	public void setMapx(String paramString) {
		this.mapx = paramString;
	}

	public void setStype(String paramString) {
		this.stype = paramString;
	}

	public void setAid(String paramString) {
		this.aid = paramString;
	}
	
	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("unid", this.unid);
			localJSONObject1.put("aid", this.aid);
			localJSONObject1.put("areaname", this.aname);
			localJSONObject1.put("mapx", this.mapx);
			localJSONObject1.put("stype", this.stype);

			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public void reset() {
		this.unid = "";
		this.aid = "";
		this.aname = "";
		this.mapx = "";
		this.stype = "";
	}

	// 保存地址
	@Override
	public BuildingModel stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.unid = localJSONObject.getString("unid");
			this.aid = localJSONObject.getString("aid");
			this.aname = localJSONObject.getString("areaname");
			this.mapx = localJSONObject.getString("mapx");
			this.stype = localJSONObject.getString("stype");
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return String.valueOf(this.aname);
	}
}
