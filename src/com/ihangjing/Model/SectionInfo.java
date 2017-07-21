package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

public class SectionInfo {
	public String SectionID = "";
	public String SectionName = "";
	public String cityid = "";
	public String cityName = "";
	public String Parentid = "";
	

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getSectionID() {
		return SectionID;
	}

	public void setSectionID(String sectionID) {
		SectionID = sectionID;
	}

	public String getSectionName() {
		return SectionName;
	}

	public void setSectionName(String sectionName) {
		SectionName = sectionName;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getParentid() {
		return Parentid;
	}

	public void setParentid(String parentid) {
		Parentid = parentid;
	}

	public SectionInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.SectionID = localJSONObject.getString("id");
			this.SectionName = localJSONObject.getString("name");
			this.cityid = localJSONObject.getString("id");
			this.Parentid = localJSONObject.getString("id");
			
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("id", this.SectionID);
			localJSONObject1.put("name", this.SectionName);
			localJSONObject1.put("id", this.cityid);
			localJSONObject1.put("id", this.Parentid);
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public SectionInfo()
	{
		
	}
	// Json×ª»»³Émodel
	public SectionInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setSectionID(localJSONObject.getString("SortID"));
			setSectionName(localJSONObject.getString("SortName"));
			//setCityid(localJSONObject.getString("cityid"));
			//setParentid(localJSONObject.getString("Parentid"));
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}
	
	
	
	public void SetBuild(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setSectionID(localJSONObject.getString("DataId"));
			setSectionName(localJSONObject.getString("Name"));
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}
	
}
