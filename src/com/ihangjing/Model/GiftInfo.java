package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

/**
 * ��ɱʵ��
 * @author Administrator
 *
 */
public class GiftInfo {
	private String gID = "";
	private String gName = "";
	private int needPoint = 0;//��Ҫ�����ٻ���
	private String price = "";//��Ʒ�۸�
	private int stocks = 0;//���
	private String pic = "";
	private int type = 0;//���ͣ�0��ͨ��Ʒ��1�ֽ�ȯ
	private String hasMoney = "0.0";//��type = 1ʱ��������ٽ��
	private int lotterPoint = 0;//�齱�������
	private String pkAddress = "";//��Ʒ��ȡ��ַ
	private String content = "";
	
	private String notice;
	
	public GiftInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			
			this.gID = localJSONObject.getString("GiftsId");
			this.gName = localJSONObject.getString("Gname");
			this.needPoint = localJSONObject.getInt("NeedIntegral");
			this.price = localJSONObject.getString("GiftsPrice");
			//this.type = localJSONObject.getInt("gifttype");
			this.pic = localJSONObject.getString("Picture");
			this.stocks = localJSONObject.getInt("stocks");
			//this.hasMoney = localJSONObject.getString("hasmoney");
			//this.lotterPoint = localJSONObject.getInt("lotterypoint");
			content = localJSONObject.getString("Content");
			
			//return this;
		} catch (JSONException localJSONException) {
			
		}
		return this;
	}

	

	public GiftInfo()
	{
		
	}
	


	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public GiftInfo(JSONObject model) {
		// TODO Auto-generated constructor stub
		try {
			//JSONObject localJSONObject = new JSONObject(paramString);
			
			this.gID = model.getString("GiftsId");
			this.gName = model.getString("Gname");
			this.needPoint = model.getInt("NeedIntegral");
			this.price = model.getString("GiftsPrice");
			//this.type = model.getInt("gifttype");
			this.pic = model.getString("Picture");
			this.stocks = model.getInt("Stocks");
			/*this.hasMoney = model.getString("hasmoney");
			this.lotterPoint = model.getInt("lotterypoint");
			this.pkAddress = model.getString("cnum");*/
		} catch (JSONException localJSONException) {
			
		}
	}



	public String getgID() {
		return gID;
	}



	public void setgID(String gID) {
		this.gID = gID;
	}



	public String getgName() {
		return gName;
	}



	public void setgName(String gName) {
		this.gName = gName;
	}



	public int getNeedPoint() {
		return needPoint;
	}



	public void setNeedPoint(int minPoint) {
		this.needPoint = minPoint;
	}



	public String getPrice() {
		return price;
	}



	public void setPrice(String price) {
		this.price = price;
	}



	public int getStocks() {
		return stocks;
	}



	public void setStocks(int stocks) {
		this.stocks = stocks;
	}



	public String getPic() {
		return pic;
	}



	public void setPic(String pic) {
		this.pic = pic;
	}



	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}



	public String getHasMoney() {
		return hasMoney;
	}



	public void setHasMoney(String hasMoney) {
		this.hasMoney = hasMoney;
	}



	public int getLotterPoint() {
		return lotterPoint;
	}



	public void setLotterPoint(int lotterPoint) {
		this.lotterPoint = lotterPoint;
	}



	public String getPkAddress() {
		return pkAddress;
	}



	public void setPkAddress(String pkAddress) {
		this.pkAddress = pkAddress;
	}



	public String getNotice() {
		return notice;
	}



	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	
}
