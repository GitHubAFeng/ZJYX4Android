package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class MyMessageModel {
	private String dataID;//消息编号
	private String userName;//会员名
	private String userID;//会员编号
	private String userICO;//会员头像网络地址
	private String dateTime;//发布时间
	private String rrmark;//回复
	private String pic;//消息中图片地址
	private String Speech;//消息中音频地址
	private String video;//消息中视频地址
	private String rDateTime;//回复时间
	private String comment;//消息内容
	
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
			rrmark = "[暂未回复]";
		}
		if (userid.equals(userID)) {
			userName = "我自己";
		}
		pic = json.getString("picture");
		Speech = json.getString("Speech");
		video = json.getString("Video");
	}
	public MyMessageModel() {
		// TODO Auto-generated constructor stub
	}
}
