package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class MyMessageModel {
	private String dataID;//��Ϣ���
	private String userName;//��Ա��
	private String userID;//��Ա���
	private String userICO;//��Աͷ�������ַ
	private String dateTime;//����ʱ��
	private String rrmark;//�ظ�
	private String pic;//��Ϣ��ͼƬ��ַ
	private String Speech;//��Ϣ����Ƶ��ַ
	private String video;//��Ϣ����Ƶ��ַ
	private String rDateTime;//�ظ�ʱ��
	private String comment;//��Ϣ����
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDataID() {
		return dataID;
	}
	public void setDataID(String dataID) {
		this.dataID = dataID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserICO() {
		return userICO;
	}
	public void setUserICO(String userICO) {
		this.userICO = userICO;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getRrmark() {
		return rrmark;
	}
	public void setRrmark(String rrmark) {
		this.rrmark = rrmark;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getSpeech() {
		return Speech;
	}
	public void setSpeech(String speech) {
		Speech = speech;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getrDateTime() {
		return rDateTime;
	}
	public void setrDateTime(String rDateTime) {
		this.rDateTime = rDateTime;
	}
	public MyMessageModel(JSONObject json, String userid) throws JSONException{
		dataID = json.getString("dataid");
		userID = json.getString("userid");
		userICO = json.getString("userpic");
		userName = json.getString("username");
		comment = json.getString("word");
		dateTime = json.getString("time");
		rrmark = json.getString("rremark");
		if (rrmark != null && rrmark.length() > 0) {
			rDateTime = json.getString("rtime");
		}else{
			rDateTime = "";
			rrmark = "[��δ�ظ�]";
		}
		if (userid.equals(userID)) {
			userName = "���Լ�";
		}
		pic = json.getString("picture");
		Speech = json.getString("Speech");
		video = json.getString("Video");
	}
	public MyMessageModel() {
		// TODO Auto-generated constructor stub
	}
}
