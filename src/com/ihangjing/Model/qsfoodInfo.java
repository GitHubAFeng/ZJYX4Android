package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

public class qsfoodInfo {
	public String fID = "";
	public String foodname = "";
	public String statestr = "";
	public String openstate = "";
	public String timestr = "";
	public String togoname = "";
	public String oldprice = "";
	public String newprice = "";
	public String sqid = "";
	public String cnum = "";
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
	
	public qsfoodInfo stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.fID= localJSONObject.getString("fID");
			this.foodname = localJSONObject.getString("foodname");
			this.statestr = localJSONObject.getString("statestr");
			this.openstate = localJSONObject.getString("openstate");
			this.timestr = localJSONObject.getString("timestr");
			this.togoname = localJSONObject.getString("togoname");
			this.oldprice = localJSONObject.getString("oldprice");
			this.newprice = localJSONObject.getString("newprice");
			this.sqid = localJSONObject.getString("sqid");
			this.cnum = localJSONObject.getString("cnum");
			
			
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
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public qsfoodInfo()
	{
		
	}
	// Json×ª»»³Émodel
	public qsfoodInfo(JSONObject json) throws JSONException {
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
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}
}
