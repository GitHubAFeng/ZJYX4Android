package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.ihangjing.common.OtherManager;

/**
 *��Ʒ���б�
 * @author Administrator
 *
 */
public class CouponInfo {
	public String cardnum = "";
	public String point = "";//���~
	public String ckey= "";
	public String cmoney= "";//���
	public int CID= 0;
	
	public String addDate = "";//�������
	
	public int type = 1;//1�F��,2����
	
	public String togoID = "";//�̼Ҿ�̖
	public int isUsed = 0;//0�]��ʹ�ã�1ʹ��
	public int timeLimity = 1;//1�]�Еr�g���ƣ�0�Еr�g����
	public String startTime;//�Еr�g���Ƶ���ʼ�r�g
	public String endTime;//�Еr�g���ƵĽY���r�g
	public int moneyLimity = 1;//1��ʾ�����ƽ��~��0��ʾ�н��~����
	public String moneyLine;//���ƽ��~���������
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
	// Jsonת����model
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
