package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class MyFriends {
	int dataID;
	int friendID;
	String friendName;
	String friendPhone;
	String friendICON;
	String imageLocalPath;
	int isFriend = 1;//0不是朋友，1是朋友
	public MyFriends(){
		
	}
	public MyFriends(JSONObject json){
		try {
			friendID = json.getInt("friendid");
			friendName = json.getString("friendname");
			friendICON = json.getString("friendpicture");
			friendPhone = json.getString("friendtell");
			isFriend = json.getInt("isfriend");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public MyFriends(int did, int fid, String name, String phone, String icon){
		dataID = did;
		friendID = fid;
		friendName = name;
		friendPhone = phone;
		friendICON = icon;
		isFriend = 1;//从本地数据库读取的都是好友，故为1
	}
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
	public String getImageLocalPath() {
		return imageLocalPath;
	}
	public void setImageLocalPath(String imageLocalPath) {
		this.imageLocalPath = imageLocalPath;
	}
	public int getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(int isFriend) {
		this.isFriend = isFriend;
	}
	
	
}
