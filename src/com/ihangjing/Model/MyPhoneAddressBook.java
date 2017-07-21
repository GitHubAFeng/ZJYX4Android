package com.ihangjing.Model;

import org.json.JSONObject;

public class MyPhoneAddressBook {
	int dataID;
	String friendName;
	String friendPhone;
	int friendType;//-1表示不能加自己为好友,-2表示已经是好友,-3用户未注册,1表示添加成好友
	public MyPhoneAddressBook() {
		// TODO Auto-generated constructor stub
		
	}
	public int getDataID() {
		return dataID;
	}
	public void setDataID(int dataID) {
		this.dataID = dataID;
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
	public int getFriendType() {
		return friendType;
	}
	public void setFriendType(int friendType) {
		this.friendType = friendType;
	}
	
}
