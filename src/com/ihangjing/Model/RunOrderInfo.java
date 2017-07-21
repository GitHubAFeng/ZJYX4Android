package com.ihangjing.Model;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RunOrderInfo {
	
	private String userName;
	private String userPhone;
	private String userAddress;
	private String remark;
	private String userID;
	private int count;
	private float money;
	private String sendTime;
	private String addTime;
	private String uLat;
	private String uLng;
	private int direc;//方向
	private String senderName;
	private String senderPhone;
	private String senderAddr;
	private String senderLat;
	private String senderLng;
	private String cityID;
	
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	private String comName;//商品名称
	private String[] de = {"东", "南", "西", "北"};
	private String payPassword;
	private String payType;
	
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String shopID) {
		this.userID = shopID;
	}
	public String getSenderAddr() {
		return senderAddr;
	}
	
	public String getSenderLat() {
		return senderLat;
	}
	public void setSenderLat(String senderLat) {
		this.senderLat = senderLat;
	}
	public String getSenderLng() {
		return senderLng;
	}
	public void setSenderLng(String senderLng) {
		this.senderLng = senderLng;
	}
	public void setSenderAddr(String shopAddr) {
		this.senderAddr = shopAddr;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String[] getDe() {
		return de;
	}
	public void setDe(String[] de) {
		this.de = de;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public float getMoney() {
		return money;
	}
	public void setMoney(float money) {
		this.money = money;
	}
	
	public String getuLat() {
		return uLat;
	}
	public void setuLat(String uLat) {
		this.uLat = uLat;
	}
	public String getuLng() {
		return uLng;
	}
	public void setuLng(String uLng) {
		this.uLng = uLng;
	}
	public int getDirec() {
		return direc;
	}
	public void setDirec(int direc) {
		this.direc = direc;
	}
	
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String beanToString(){
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("Inve2", this.comName);//物品名称
			localJSONObject1.put("callmsg", this.userName);//收货人
			localJSONObject1.put("Remark", remark/*String.format("[%s][%d份餐]%s", de[direc], count, remark)*/);
			localJSONObject1.put("Oorderid", this.userAddress);//收货地址
			localJSONObject1.put("appShopAddress", this.senderAddr);
			localJSONObject1.put("ReveVar", this.userPhone);//收件人电话
			localJSONObject1.put("OrderSums", String.format("%.2f", money));//配送费
			localJSONObject1.put("SentTime", this.sendTime);
			localJSONObject1.put("P2Sign", de[direc]);
			//localJSONObject1.put("PayMode", "4");
			localJSONObject1.put("ulat", uLat);
			localJSONObject1.put("ulng", uLng);
			localJSONObject1.put("shoplat", senderLat);
			localJSONObject1.put("shoplng", senderLng);
			localJSONObject1.put("ordersource", "2");
			localJSONObject1.put("PayPassword", payPassword);
			localJSONObject1.put("PayMode", payType);
			localJSONObject1.put("Cityid", cityID);
			localJSONObject1.put("UserName", senderName);
			localJSONObject1.put("Tel", senderPhone);
			localJSONObject1.put("Address", senderAddr);
			localJSONObject1.put("UserID", userID);

			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			return null;
		}
		
	}

}
