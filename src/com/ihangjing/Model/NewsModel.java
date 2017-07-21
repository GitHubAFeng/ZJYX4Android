package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

public class NewsModel extends BaseBean {
	public String dataid = "";
	public String title = "";
	public String content = "";

	public NewsModel()
	{
		
	}
	// Json转换成model
	public NewsModel(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setDataid(localJSONObject.getString("id"));
			setTitle(localJSONObject.getString("title"));
			//setContent(localJSONObject.getString("content"));
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}
	
	public String getDataid() {
		return this.dataid;
	}

	public String getTitle() {
		return this.title;
	}
	
	public String getContent() {
		return this.content;
	}

	public void setDataid(String paramString) {
		this.dataid = paramString;
	}
	
	public void setTitle(String paramString) {
		this.title = paramString;
	}

	public void setContent(String paramString) {
		this.content = paramString;
	}
	
	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("id", this.dataid);
			localJSONObject1.put("title", this.title);
			localJSONObject1.put("content", this.content);

			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public void reset() {
		this.dataid = "";
		this.title = "";
		this.content = "";
	}

	// 保存地址
	@Override
	public NewsModel stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.dataid = localJSONObject.getString("id");
			this.title = localJSONObject.getString("title");
			//this.content = localJSONObject.getString("content");
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return String.valueOf(this.title);
	}
}
