package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

public class NewBuildingInfo {
	private String dataid = "";
	private String Name = "";
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	public NewBuildingInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.dataid = localJSONObject.getString("dataid");
			this.Name = localJSONObject.getString("Name");
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("dataid", this.dataid);
			localJSONObject1.put("Name", this.Name);
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public NewBuildingInfo()
	{
		
	}
	// Json×ª»»³Émodel
	public NewBuildingInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setDataid(localJSONObject.getString("dataid"));
			setName(localJSONObject.getString("Name"));
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
