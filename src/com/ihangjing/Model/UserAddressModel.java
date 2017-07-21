package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

/*
 * 程序节（Bean） 可重用软件组件。将它们组合起来使用就可创建出应用程序。 
 */
public class UserAddressModel extends BaseBean {

	public String tag = "";//标签
	public String receiverName = "";
	public String buildingid = "";//区域
	public String address = "";
	public String phone = "";
	public String tel = "";
	public String isdefault = "";
	public String dataid = "";
	public double lat = 0.0D;
	public double lon = 0.0D;
	  
	public UserAddressModel() {

	}

	// Json转换成model
	public UserAddressModel(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setDataid(localJSONObject.getString("dataid"));
			setReceiver(localJSONObject.getString("receiver"));
			setBuildingid(localJSONObject.getString("buildingid"));
			setPhone(localJSONObject.getString("mobilephone"));
			setIsdefault(localJSONObject.getString("isdefault"));
			setAddress(localJSONObject.getString("address"));
			
		} catch (JSONException localJSONException) {
			if (OtherManager.DEBUG) {
				Log.v("HotAreaModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}

	public String getDataid() {
		return this.dataid;
	}

	public String getReceiverName() {
		return this.receiverName;
	}

	public String getBuildingid() {
		return this.buildingid;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getAddress() {
		return this.address;
	}

	public String getIsdefault() {
		return this.isdefault;
	}

	public String getTag() {
		return this.tag;
	}

	public void setDataid(String paramString) {
		this.dataid = paramString;
	}

	public void setReceiver(String paramString) {
		this.receiverName = paramString;
	}

	public void setBuildingid(String paramString) {
		this.buildingid = paramString;
	}

	public void setPhone(String paramString) {
		this.phone = paramString;
	}

	public void setAddress(String paramString) {
		this.address = paramString;
	}

	public void setIsdefault(String paramString) {
		this.isdefault = paramString;
	}

	public void setTag(String paramString) {
		this.tag = paramString;
	}

	public String getTel() {
		return this.tel;
	}
	
	public void setTel(String paramString) {
		this.tel = paramString;
	}
	
	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("dataid", this.dataid);
			localJSONObject1.put("tag", this.tag);
			localJSONObject1.put("receiver", this.receiverName);
			localJSONObject1.put("buildingid", this.buildingid);
			localJSONObject1.put("address", this.address);
			localJSONObject1.put("phone", this.phone);
			localJSONObject1.put("isdefault", this.isdefault);
			localJSONObject1.put("tel", this.tel);
			localJSONObject1.put("lat", this.lat);
			localJSONObject1.put("lon", this.lon);
			
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public void reset() {
		this.receiverName = "";
		this.tag = "";
		this.buildingid = "";
		this.address = "";
		this.phone = "";
		this.tel = "";
		this.isdefault = "";
		this.lat = 0.0D;
		this.lon = 0.0D;
	}

	// 保存地址
	@Override
	public UserAddressModel stringToBean(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			
			this.tag = localJSONObject.getString("tag");
			this.receiverName = localJSONObject.getString("receiverName");
			this.buildingid = localJSONObject.getString("buildingid");
			this.address = localJSONObject.getString("address");
			this.phone = localJSONObject.getString("mobilephone");
			this.tel = localJSONObject.getString("tel");
			this.isdefault = localJSONObject.getString("isdefault");
			this.lat = Double.parseDouble(localJSONObject.getString("lat"));
			this.lon = Double.parseDouble(localJSONObject.getString("lon"));
			
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return String.valueOf(this.receiverName + "/" + this.address);
	}
}
