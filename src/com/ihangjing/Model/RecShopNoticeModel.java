package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;


public class RecShopNoticeModel {
	public String id = "";
	public String recommendTime = "";
	public String shopName = "";
	public int state = 0;//
	public String point = "0";//获得的积分
	
	
	public int getState() {
		return state;
	}



	public void setState(int state) {
		this.state = state;
	}



	public String getPoint() {
		return point;
	}



	public void setPoint(String point) {
		this.point = point;
	}



	


	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getRecommendTime() {
		return recommendTime;
	}



	public void setRecommendTime(String recommendTime) {
		this.recommendTime = recommendTime;
	}



	public String getShopName() {
		return shopName;
	}



	public void setShopName(String shopName) {
		this.shopName = shopName;
	}



	public RecShopNoticeModel() {
		// TODO Auto-generated constructor stub
	}


	public RecShopNoticeModel(JSONObject model) {
		// TODO Auto-generated constructor stub
		try {
			
			id = model.getString("id");
			recommendTime = model.getString("RecommendTime");
			state = model.getInt("State");
			point = model.getString("point");
			shopName = model.getString("shopname");
			//return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}



	public RecShopNoticeModel stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			id = localJSONObject.getString("id");
			recommendTime = localJSONObject.getString("RecommendTime");
			state = localJSONObject.getInt("State");
			point = localJSONObject.getString("point");
			shopName = localJSONObject.getString("shopname");
			
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	
}
