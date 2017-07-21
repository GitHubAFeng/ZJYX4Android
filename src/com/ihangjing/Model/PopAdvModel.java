package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class PopAdvModel {
	private String dataID;
	private String imageNet;//Í¼Æ¬ÍøÂçµØÖ·
	private int resID;
	
	public int getResID() {
		return resID;
	}
	public void setResID(int resID) {
		this.resID = resID;
	}
	public String getDataID() {
		return dataID;
	}
	public void setDataID(String dataID) {
		this.dataID = dataID;
	}
	public String getImageNet() {
		return imageNet;
	}
	public void setImageNet(String imageNet) {
		this.imageNet = imageNet;
	}
	public PopAdvModel(){
		
	}
	public PopAdvModel(JSONObject json) throws JSONException{
		imageNet = json.getString("SortName");
	}
	
	public void ShopZJSONToBean(JSONObject json) throws JSONException{
		imageNet = json.getString("SortPic");
	}
}
