package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

//订单列表中的订单信息,只有少数的几个属性
public class GiftSimpleOrderModel {
	//{"OrderID":"1206262045220755","TogoName":"新鼎红培",
	//"orderTime":"2012-06-26 20:45:26","TotalPrice":"34.5",
	//"State":"3","sendmoney":"8","Packagefree":"0.0"}
	private String orderid;
	private String giftName;
	private String addtime;    // 下单时间
	private String usePoint; // 总价（注意:含配送费）
	private int sendType = 2;      // 订单状态
	private int type = 0;  // 配送费
	private int state;// 打包费
	
	// Json转换成model
	public GiftSimpleOrderModel(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			
			orderid = localJSONObject.getString("IntegralId");
			giftName = localJSONObject.getString("GiftName");
			usePoint = localJSONObject.getString("PayIntegral");//所花积分
			addtime = localJSONObject.getString("Cdate");//提交时间
			sendType = localJSONObject.getInt("sendtype");//1自提，2送货
			type = localJSONObject.getInt("ReveInt1");//0普通礼品 1 现金券
			state = localJSONObject.getInt("State");//　　　0 未审核 1 审核通过 2 审核未通过
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}
	
	public GiftSimpleOrderModel() {

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

	public GiftSimpleOrderModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			orderid = localJSONObject.getString("IntegralId");
			giftName = localJSONObject.getString("GiftName");
			usePoint = localJSONObject.getString("PayIntegral");//所花积分
			addtime = localJSONObject.getString("Cdate");//提交时间
			sendType = localJSONObject.getInt("sendtype");//1自提，2送货
			type = localJSONObject.getInt("ReveInt1");//0普通礼品 1 现金券
			state = localJSONObject.getInt("State");//　　　0 未审核 1 审核通过 2 审核未通过
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
