package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.ihangjing.common.OtherManager;

/**
 *礼品卡列表
 * @author Administrator
 *
 */
public class CouponInfo {
	public String cardnum = "";
	public String point = "";//金額
	public String ckey= "";
	public String cmoney= "";//余额
	public int CID= 0;
	
	public String addDate = "";//添加日期
	
	public int type = 1;//1現金,2打折
	
	public String togoID = "";//商家編號
	public int isUsed = 0;//0沒有使用，1使用
	public int timeLimity = 1;//1沒有時間限制，0有時間限制
	public String startTime;//有時間限制的起始時間
	public String endTime;//有時間限制的結束時間
	public int moneyLimity = 1;//1表示不限制金額，0表示有金額限制
	public String moneyLine;//限制金額，最低消费
	//public boolean isCheck = false;
	private String togoName;
	
	
	
	
	public CouponInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.cardnum = localJSONObject.getString("cardnum");
			this.point = localJSONObject.getString("point");
			this.ckey = localJSONObject.getString("ckey");
			this.cmoney = localJSONObject.getString("cmoney");
			this.CID = localJSONObject.getInt("CID");
			
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			
			localJSONObject1.put("cardnum", this.cardnum);
			localJSONObject1.put("point", this.point);
			localJSONObject1.put("ckey", this.ckey);
			localJSONObject1.put("cmoney", this.cmoney);
			localJSONObject1.put("CID", this.CID);
			
			
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public CouponInfo()
	{
		
	}
	// Json转换成model
	public CouponInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setCardnum(localJSONObject.getString("cardnum"));
			setPoint(localJSONObject.getString("point"));
			setCkey(localJSONObject.getString("ckey"));
			setCmoney(localJSONObject.getString("cmoney"));
			setCID(localJSONObject.getInt("CID"));
			//type = localJSONObject.getInt("ReveInt");
			timeLimity = localJSONObject.getInt("timelimity");
			moneyLimity = localJSONObject.getInt("moneylimity");
			moneyLine = localJSONObject.getString("moneyline");
			startTime = localJSONObject.getString("starttime");
			endTime = localJSONObject.getString("endtime");
			togoName = localJSONObject.getString("TogoName");

		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}
	
	public String getTogoName() {
		return togoName;
	}

	public void setTogoName(String togoName) {
		this.togoName = togoName;
	}

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getCmoney() {
		return cmoney;
	}

	public void setCmoney(String cmoney) {
		this.cmoney = cmoney;
	}

	public int getCID() {
		return CID;
	}

	public void setCID(int cID) {
		CID = cID;
	}
}
