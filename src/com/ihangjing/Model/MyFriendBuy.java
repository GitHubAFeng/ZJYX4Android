package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFriendBuy {
	int dataID;
	
	int friendID;
	String friendName;
	String friendPhone;
	String friendICON;
	String frImageLocalPath;
	
	int foodID;
	String foodName;
	String foodICON;
	String foImageLocalPath;
	
	String sendTime;//发布时间

	public int getDataID() {
		return dataID;
	}

	public void setDataID(int dataID) {
		this.dataID = dataID;
	}

	public int getFriendID() {
		return friendID;
	}

	public void setFriendID(int friendID) {
		this.friendID = friendID;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getFriendPhone() {
		return friendPhone;
	}

	public void setFriendPhone(String friendPhone) {
		this.friendPhone = friendPhone;
	}

	public String getFriendICON() {
		return friendICON;
	}

	public void setFriendICON(String friendICON) {
		this.friendICON = friendICON;
	}

	public String getFrImageLocalPath() {
		return frImageLocalPath;
	}

	public void setFrImageLocalPath(String frImageLocalPath) {
		this.frImageLocalPath = frImageLocalPath;
	}

	public int getFoodID() {
		return foodID;
	}

	public void setFoodID(int foodID) {
		this.foodID = foodID;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getFoodICON() {
		return foodICON;
	}

	public void setFoodICON(String foodICON) {
		this.foodICON = foodICON;
	}

	public String getFoImageLocalPath() {
		return foImageLocalPath;
	}

	public void setFoImageLocalPath(String foImageLocalPath) {
		this.foImageLocalPath = foImageLocalPath;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	public MyFriendBuy(JSONObject json) throws JSONException{
		friendID = json.getInt("userid");
		friendICON = json.getString("userpic");
		friendName = json.getString("username");
		foodID = json.getInt("foodid");
		foodName = json.getString("foodname");
		foodICON = json.getString("foodpic");
		sendTime = json.getString("issuetime");
	}
}
