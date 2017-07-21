package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

public class TagModel {
	private String dataID;
	private String imageNet;//图片网络地址
	private String info;//说明
	private float price;//优惠价格说明
	private int resID;
	private View view;
	
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public View getView() {
		return view;
	}
	public void setView(View rl) {
		this.view = rl;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
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
	public TagModel(){
		
	}
	public TagModel(JSONObject json) throws JSONException{
		imageNet = json.getString("Picture");
		info = json.getString("Title");
	}
	public void PromotionsToBean(JSONObject json) throws JSONException{
		price = (float)json.getDouble("freeSendFee");
		info = json.getString("title");
	}
}
