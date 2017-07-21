package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ReciveAddressModel {
	String id;
	String phone;
	String mobilePhone;
	String addres;
	String reciver;
	String buildID;
	String buildName;
	double lat;
	double lon;
	
	public ReciveAddressModel(JSONObject json) throws JSONException {
		id = json.getString("dataid");
		phone = json.getString("mobilephone");
		mobilePhone = json.getString("mobilephone");
		addres = json.getString("address");
		reciver = json.getString("receiver");
		buildID = json.getString("buildingid");
		//buildName = json.getString("BuildingName");
		String value = json.getString("lat");
		if (value != null && value.length() > 0) {
			lat = Double.parseDouble(value);
		}
		value = json.getString("lng");
		if (value != null && value.length() > 0) {
			lon = Double.parseDouble(value);
		}
	}
	public ReciveAddressModel() {
		// TODO Auto-generated constructor stub
	}
	
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getBuildID() {
		return buildID;
	}
	public void setBuildID(String buildID) {
		this.buildID = buildID;
	}
	public String getBuildName() {
		return buildName;
	}
	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getAddres() {
		return addres;
	}
	public void setAddres(String addres) {
		this.addres = addres;
	}
	public String getReciver() {
		return reciver;
	}
	public void setReciver(String reciver) {
		this.reciver = reciver;
	}
	public void setNewModel(ReciveAddressModel model) {
		// TODO Auto-generated method stub
		id = model.getId();
		phone = model.getPhone();
		mobilePhone = model.getMobilePhone();
		addres = model.getAddres();
		reciver = model.getReciver();
	}
}
