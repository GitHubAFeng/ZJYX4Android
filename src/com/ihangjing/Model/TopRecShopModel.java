package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

public class TopRecShopModel {
	public String userid = "";
	public String count = "";
	public String userName = "";

	
	public String getUserid() {
		return userid;
	}



	public void setUserid(String userid) {
		this.userid = userid;
	}



	public String getCount() {
		return count;
	}



	public void setCount(String count) {
		this.count = count;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}


	
	public TopRecShopModel() {
		// TODO Auto-generated constructor stub
	}


	public TopRecShopModel(JSONObject model) {
		// TODO Auto-generated constructor stub
		try {
			
			userid = model.getString("userid");
			count = model.getString("tuijianCount");
			userName = model.getString("tuijianName");
			//return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}



	public TopRecShopModel stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			userid = localJSONObject.getString("userid");
			count = localJSONObject.getString("tuijianCount");
			userName = localJSONObject.getString("tuijianName");
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	
}
