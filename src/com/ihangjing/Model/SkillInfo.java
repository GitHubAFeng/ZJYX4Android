package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

/**
 * 秒杀实体
 * @author Administrator
 *
 */
public class SkillInfo {
	private String fID = "";
	private String foodname = "";
	private String statestr = "";
	private String openstate = "";
	private String timestr = "";
	private String togoname = "";
	private String oldprice = "";
	private String newprice = "";
	private String sqid = "";
	private String cnum = "";
	private String Foodid = "";
	private String pic = "";
	private String stardate = "";
	private String enddate = "";
	private String timestart = "";
	private String timeend = "";
	private String shopid = "";
	private String notice;
	private String sendEndTime;//派送截至时间
	private String sendStartTime;//派送开始时间
	
	public SkillInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			
			this.fID = localJSONObject.getString("fID");
			this.foodname = localJSONObject.getString("foodname");
			this.statestr = localJSONObject.getString("statestr");
			this.openstate = localJSONObject.getString("openstate");
			this.timestr = localJSONObject.getString("timestr");
			this.togoname = localJSONObject.getString("togoname");
			this.oldprice = localJSONObject.getString("oldprice");
			this.newprice = localJSONObject.getString("newprice");
			this.sqid = localJSONObject.getString("sqid");
			this.cnum = localJSONObject.getString("cnum");
			this.Foodid = localJSONObject.getString("Foodid");
			this.pic = localJSONObject.getString("pic");
			this.stardate = localJSONObject.getString("stardate");
			this.enddate = localJSONObject.getString("enddate");
			this.timestart = localJSONObject.getString("timestart");
			this.timeend = localJSONObject.getString("timeend");
			this.shopid = localJSONObject.getString("shopid");
			
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("fID", this.fID);
			localJSONObject1.put("foodname", this.foodname);
			localJSONObject1.put("statestr", this.statestr);
			localJSONObject1.put("openstate", this.openstate);
			localJSONObject1.put("timestr", this.timestr);
			localJSONObject1.put("togoname", this.togoname);
			localJSONObject1.put("oldprice", this.oldprice);
			localJSONObject1.put("newprice", this.newprice);
			localJSONObject1.put("sqid", this.sqid);
			localJSONObject1.put("cnum", this.cnum);
			localJSONObject1.put("Foodid", this.Foodid);
			localJSONObject1.put("pic", this.pic);	
			localJSONObject1.put("stardate", this.stardate);
			localJSONObject1.put("enddate", this.enddate);
			localJSONObject1.put("timestart", this.timestart);
			localJSONObject1.put("timeend", this.timeend);
			localJSONObject1.put("shopid", this.shopid);
			
		
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public SkillInfo()
	{
		
	}
	
	public String getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	// Json转换成model
	public SkillInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setfID(localJSONObject.getString("fID"));
			setFoodname(localJSONObject.getString("foodname"));
			setStatestr(localJSONObject.getString("statestr"));
			setOpenstate(localJSONObject.getString("openstate"));
			setTimestr(localJSONObject.getString("timestr"));
			setTogoname(localJSONObject.getString("togoname"));
			setOldprice(localJSONObject.getString("oldprice"));
			setNewprice(localJSONObject.getString("newprice"));
			setSqid(localJSONObject.getString("sqid"));
			setCnum(localJSONObject.getString("cnum"));
			setFoodid(localJSONObject.getString("Foodid"));
			setPic(localJSONObject.getString("pic"));
			setStardate(localJSONObject.getString("stardate"));
			setEnddate(localJSONObject.getString("enddate"));
			setTimestart(localJSONObject.getString("timestart"));
			setTimeend(localJSONObject.getString("timeend"));
			setShopid(localJSONObject.getString("shopid"));
			notice = localJSONObject.getString("ReveVar2");//说明
			sendStartTime = localJSONObject.getString("sendstart");
			sendEndTime = localJSONObject.getString("sendend");
			
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}

	public String getfID() {
		return fID;
	}

	public void setfID(String fID) {
		this.fID = fID;
	}

	public String getFoodname() {
		return foodname;
	}

	public void setFoodname(String foodname) {
		this.foodname = foodname;
	}

	public String getStatestr() {
		return statestr;
	}

	public void setStatestr(String statestr) {
		this.statestr = statestr;
	}

	public String getOpenstate() {
		return openstate;
	}

	public void setOpenstate(String openstate) {
		this.openstate = openstate;
	}

	public String getTimestr() {
		return timestr;
	}

	public void setTimestr(String timestr) {
		this.timestr = timestr;
	}

	public String getTogoname() {
		return togoname;
	}

	public void setTogoname(String togoname) {
		this.togoname = togoname;
	}

	public String getOldprice() {
		return oldprice;
	}

	public void setOldprice(String oldprice) {
		this.oldprice = oldprice;
	}

	public String getNewprice() {
		return newprice;
	}

	public void setNewprice(String newprice) {
		this.newprice = newprice;
	}

	public String getSqid() {
		return sqid;
	}

	public void setSqid(String sqid) {
		this.sqid = sqid;
	}

	public String getCnum() {
		return cnum;
	}

	public void setCnum(String cnum) {
		this.cnum = cnum;
	}

	public String getFoodid() {
		return Foodid;
	}

	public void setFoodid(String foodid) {
		Foodid = foodid;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getStardate() {
		return stardate;
	}

	public void setStardate(String stardate) {
		this.stardate = stardate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getTimestart() {
		return timestart;
	}

	public void setTimestart(String timestart) {
		this.timestart = timestart;
	}

	public String getTimeend() {
		return timeend;
	}

	public void setTimeend(String timeend) {
		this.timeend = timeend;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
}
