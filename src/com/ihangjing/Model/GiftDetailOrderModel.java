package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

//订单列表中的订单信息,只有少数的几个属性
public class GiftDetailOrderModel {
	// {"OrderID":"1206262045220755","TogoName":"新鼎红培",
	// "orderTime":"2012-06-26 20:45:26","TotalPrice":"34.5",
	// "State":"3","sendmoney":"8","Packagefree":"0.0"}
	private String orderid;
	private String giftName;
	private String addtime; // 提交时间
	private String usePoint; // /所花积分
	private int sendType = 2; // 1自提，2送货
	private int type = 0; // 0普通礼品 1 现金券
	private int state;// 0 未审核 1 审核通过 2 审核未通过
	private String uAddress = "";// 送货地址
	private String pAddressString = "";// 取货地址
	private String recName = "";// 收货人
	private String recPhone = "";// 收货电话
	private String recData = "";// 送货时间要求

	// Json转换成model
	public GiftDetailOrderModel(JSONObject json) throws JSONException {

		JSONObject localJSONObject = json;

		// orderid = localJSONObject.getString("IntegralId");
		giftName = localJSONObject.getString("GiftName");
		usePoint = localJSONObject.getString("PayIntegral");// 所花积分
		addtime = localJSONObject.getString("Cdate");// 提交时间
		//sendType = localJSONObject.getInt("sendtype");// 1自提，2送货
		//type = localJSONObject.getInt("ReveInt1");// 0普通礼品 1 现金券
		state = localJSONObject.getInt("State");// 　　　0 未审核 1 审核通过 2 审核未通过
		uAddress = localJSONObject.getString("Address");// 送货地址
		//pAddressString = localJSONObject.getString("remark");// 取货地址
		recName = localJSONObject.getString("Person");// 收货人
		recPhone = localJSONObject.getString("Phone");// 收货电话
		recData = localJSONObject.getString("Date");// 送货时间要求

	}

	public GiftDetailOrderModel() {

	}

	public String getuAddress() {
		return uAddress;
	}

	public void setuAddress(String uAddress) {
		this.uAddress = uAddress;
	}

	public String getpAddressString() {
		return pAddressString;
	}

	public void setpAddressString(String pAddressString) {
		this.pAddressString = pAddressString;
	}

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	public String getRecPhone() {
		return recPhone;
	}

	public void setRecPhone(String recPhone) {
		this.recPhone = recPhone;
	}

	public String getRecData() {
		return recData;
	}

	public void setRecData(String recData) {
		this.recData = recData;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getUsePoint() {
		return usePoint;
	}

	public void setUsePoint(String usePoint) {
		this.usePoint = usePoint;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public GiftDetailOrderModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			orderid = localJSONObject.getString("IntegralId");
			giftName = localJSONObject.getString("GiftName");
			usePoint = localJSONObject.getString("PayIntegral");// 所花积分
			addtime = localJSONObject.getString("Cdate");// 提交时间
			sendType = localJSONObject.getInt("sendtype");// 1自提，2送货
			type = localJSONObject.getInt("ReveInt1");// 0普通礼品 1 现金券
			state = localJSONObject.getInt("State");// 　　　0 未审核 1 审核通过 2 审核未通过
			uAddress = localJSONObject.getString("Address");// 送货地址
			pAddressString = localJSONObject.getString("remark");// 取货地址
			recName = localJSONObject.getString("Person");// 收货人
			recPhone = localJSONObject.getString("Phone");// 收货电话
			recData = localJSONObject.getString("Date");// 送货时间要求

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
