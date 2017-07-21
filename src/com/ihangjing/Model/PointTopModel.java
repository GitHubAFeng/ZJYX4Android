package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class PointTopModel {
	int userID;//����id
	String userName;//��������
	String friendICON;//����ͷ���ַ
	
	String historyPoint;//��ʷ����
	String publicPoint;//�������
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFriendICON() {
		return friendICON;
	}
	public void setFriendICON(String friendICON) {
		this.friendICON = friendICON;
	}
	public String getHistoryPoint() {
		return historyPoint;
	}
	public void setHistoryPoint(String historyPoint) {
		this.historyPoint = historyPoint;
	}
	public String getPublicPoint() {
		return publicPoint;
	}
	public void setPublicPoint(String publicPoint) {
		this.publicPoint = publicPoint;
	}
	
	public PointTopModel(JSONObject json) throws JSONException{
		userID = json.getInt("userid");
		userName = json.getString("username");
		historyPoint = json.getString("historypoint");
		publicPoint = json.getString("publicgood");
		friendICON = json.getString("picture");
	}
}
