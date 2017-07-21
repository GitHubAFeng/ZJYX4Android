package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

/**
 *礼品卡列表
 * @author Administrator
 *
 */
public class CardInfo {
	public String cardnum = "";
	public String point = "";
	public String ckey= "";
	public String cmoney= "";
	public String CID= "";
	public boolean isCheck = false;
	
	
	
	public CardInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.cardnum = localJSONObject.getString("cardnum");
			this.point = localJSONObject.getString("point");
			this.ckey = localJSONObject.getString("ckey");
			this.cmoney = localJSONObject.getString("cmoney");
			this.CID = localJSONObject.getString("CID");
			
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

	public CardInfo()
	{
		
	}
	// Json转换成model
	public CardInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setCardnum(localJSONObject.getString("cardnum"));
			setPoint(localJSONObject.getString("point"));
			setCkey(localJSONObject.getString("ckey"));
			setCmoney(localJSONObject.getString("cmoney"));
			setCID(localJSONObject.getString("CID"));

		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
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

	public String getCID() {
		return CID;
	}

	public void setCID(String cID) {
		CID = cID;
	}
}
