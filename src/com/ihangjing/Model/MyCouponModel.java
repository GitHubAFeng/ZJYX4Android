package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class MyCouponModel {
	private String CKey;// 券号
	private int CType;// 券类型 1现金券 2折扣
	private String CValue;// 券值
	private String geiverPerson;// 赠送人
	private int CTimeLimity;// 是否时间限制 0有限制，1无限制
	private String StartTime;// 时间限制的起始时间
	private String EndTime;// 时间限制的结束时间
	private int CMoneyLine;// 是否金额限制 0 有限制 1无限制
	private float MoneyLine = 0.0f;// 限制金额
	private float CMoney = 0.0f;// 剩余金额
	private int CActivity = 0;// 是否激活 0没有 1激活
	private int dataID;// 编号
	private int isUser = 0;// 是否使用 0未用，1使用过
	private int isSelect = 0;// 是否选中，0未选中 1选中
	private int indexSelect;// 选中队列中的位置（购物使用优惠券时用到）

	public String getCKey() {
		return CKey;
	}

	public void setCKey(String cKey) {
		CKey = cKey;
	}

	public int getCType() {
		return CType;
	}

	public void setCType(int cType) {
		CType = cType;
	}

	public String getCValue() {
		return CValue;
	}

	public void setCValue(String cValue) {
		CValue = cValue;
	}

	public String getGeiverPerson() {
		return geiverPerson;
	}

	public void setGeiverPerson(String geiverPerson) {
		this.geiverPerson = geiverPerson;
	}

	public int getCTimeLimity() {
		return CTimeLimity;
	}

	public void setCTimeLimity(int cTimeLimity) {
		CTimeLimity = cTimeLimity;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public int getCMoneyLine() {
		return CMoneyLine;
	}

	public void setCMoneyLine(int cMoneyLine) {
		CMoneyLine = cMoneyLine;
	}

	public float getMoneyLine() {
		return MoneyLine;
	}

	public void setMoneyLine(float moneyLine) {
		MoneyLine = moneyLine;
	}

	public float getCMoney() {
		return CMoney;
	}

	public void setCMoney(float cMoney) {
		CMoney = cMoney;
	}

	public int getCActivity() {
		return CActivity;
	}

	public void setCActivity(int cActivity) {
		CActivity = cActivity;
	}

	public int getDataID() {
		return dataID;
	}

	public void setDataID(int dataID) {
		this.dataID = dataID;
	}

	public int getIsUser() {
		return isUser;
	}

	public void setIsUser(int isUser) {
		this.isUser = isUser;
	}

	public int getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public int getIndexSelect() {
		return indexSelect;
	}

	public void setIndexSelect(int indexSelect) {
		this.indexSelect = indexSelect;
	}
	public MyCouponModel(){
		
	}
	public MyCouponModel(JSONObject json) throws JSONException{
		dataID = json.getInt("CID");
		CValue = json.getString("point");
		CType = json.getInt("ReveInt");
		geiverPerson = json.getString("geivecardperson");
		CTimeLimity = json.getInt("timelimity");
		StartTime = json.getString("starttime");
		EndTime = json.getString("endtime");
		CMoneyLine = json.getInt("moneylimity");
		MoneyLine = (float)json.getDouble("moneyline");
		CMoney = (float)json.getDouble("cmoney");
		CActivity = json.getInt("Inve2");
		isUser = json.getInt("isused");
		CKey = json.getString("ckey");
	}
}
