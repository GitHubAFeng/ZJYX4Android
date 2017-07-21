package com.ihangjing.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPoint {
	private int type;//����
	private String comment;//˵��
	private String point;//����ֵ
	private String dataTime;//ʱ��
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getDataTime() {
		return dataTime;
	}
	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}
	public MyPoint(JSONObject json) throws JSONException{
		//type = json.getInt("");
		point = json.getString("Point");
		dataTime = json.getString("PostTime");
		comment = json.getString("Comment");
	}
}
