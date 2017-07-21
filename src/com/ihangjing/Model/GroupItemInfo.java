package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

public class GroupItemInfo {
	private String gId = "";
	private String Title = "";
	private String nowdis = "";
	private String Inve3 = "";
	private String price = "";
	private String Discount = "";
	private String InUser = "";
	private String togoname = "";
	private String Inve4 = "";
	private String Inve2 = "";
	private String Introduce = "";
	private String Picture = "";
	private String StartDate = "";
	private String EndDate = "";
	private String SupplyerId = "";
	private String timestr="";
	
	public GroupItemInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			
			this.gId = localJSONObject.getString("gId");
			this.Title = localJSONObject.getString("Title");
			this.nowdis = localJSONObject.getString("nowdis");
			this.Inve3 = localJSONObject.getString("Inve3");
			this.price = localJSONObject.getString("price");
			this.Discount = localJSONObject.getString("Discount");
			this.InUser = localJSONObject.getString("InUser");
			this.togoname = localJSONObject.getString("togoname");
			this.Inve4 = localJSONObject.getString("Inve4");
			this.Inve2 = localJSONObject.getString("Inve2");
			this.Introduce = localJSONObject.getString("Introduce");
			this.Picture = localJSONObject.getString("Picture");
			this.StartDate = localJSONObject.getString("StartDate");
			this.EndDate = localJSONObject.getString("EndDate");
			this.SupplyerId = localJSONObject.getString("SupplyerId");
			this.timestr = localJSONObject.getString("timestr");
			
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("gId", this.gId);
			localJSONObject1.put("Title", this.Title);
			localJSONObject1.put("nowdis", this.nowdis);
			localJSONObject1.put("Inve3", this.Inve3);
			localJSONObject1.put("price", this.price);
			localJSONObject1.put("InUser", this.InUser);
			localJSONObject1.put("Discount", this.Discount);
			localJSONObject1.put("togoname", this.togoname);
			localJSONObject1.put("Inve4", this.Inve4);
			localJSONObject1.put("Inve2", this.Inve2);
			localJSONObject1.put("Introduce", this.Introduce);
			localJSONObject1.put("Picture", this.Picture);	
			localJSONObject1.put("StartDate", this.StartDate);
			localJSONObject1.put("EndDate", this.EndDate);
			localJSONObject1.put("SupplyerId", this.SupplyerId);
			localJSONObject1.put("timestr", this.timestr);
			
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public GroupItemInfo()
	{	
	}
	// Json转换成model
	public GroupItemInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setgId(localJSONObject.getString("gId"));
			setTitle(localJSONObject.getString("Title"));
			setNowdis(localJSONObject.getString("nowdis"));
			setInve3(localJSONObject.getString("Inve3"));
			setPrice(localJSONObject.getString("price"));
			setDiscount(localJSONObject.getString("Discount"));
			setInUser(localJSONObject.getString("InUser"));
			setTogoname(localJSONObject.getString("togoname"));
			setInve4(localJSONObject.getString("Inve4"));
			setInve2(localJSONObject.getString("Inve2"));
			setIntroduce(localJSONObject.getString("Introduce"));
			setPicture(localJSONObject.getString("Picture"));
			setStartDate(localJSONObject.getString("StartDate"));
			setEndDate(localJSONObject.getString("EndDate"));
			setSupplyerId(localJSONObject.getString("SupplyerId"));
			setTimestr(localJSONObject.getString("timestr"));
			
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getNowdis() {
		return nowdis;
	}

	public void setNowdis(String nowdis) {
		this.nowdis = nowdis;
	}

	/**
	 * 特别提示
	 * @return
	 */
	public String getInve3() {
		return Inve3;
	}

	public void setInve3(String inve3) {
		Inve3 = inve3;
	}
	
	/**
	 * 原价
	 * @return
	 */
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * 现价
	 * @return
	 */
	public String getDiscount() {
		return Discount;
	}

	public void setDiscount(String discount) {
		Discount = discount;
	}

	public String getInUser() {
		return InUser;
	}

	public void setInUser(String inUser) {
		InUser = inUser;
	}

	public String getTogoname() {
		return togoname;
	}

	public void setTogoname(String togoname) {
		this.togoname = togoname;
	}

	/**
	 * 地址
	 * @return
	 */
	public String getInve4() {
		return Inve4;
	}

	public void setInve4(String inve4) {
		Inve4 = inve4;
	}

	/**
	 * 配送费
	 * @return
	 */
	public String getInve2() {
		return Inve2;
	}

	public void setInve2(String inve2) {
		Inve2 = inve2;
	}

	/**
	 *介绍
	 * @return
	 */
	public String getIntroduce() {
		return Introduce;
	}

	public void setIntroduce(String introduce) {
		Introduce = introduce;
	}

	public String getPicture() {
		return Picture;
	}

	public void setPicture(String picture) {
		Picture = picture;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	/**
	 * 商家编号
	 * @return
	 */
	public String getSupplyerId() {
		return SupplyerId;
	}

	public void setSupplyerId(String supplyerId) {
		SupplyerId = supplyerId;
	}

	public String getTimestr() {
		return timestr;
	}

	public void setTimestr(String timestr) {
		this.timestr = timestr;
	}

}
