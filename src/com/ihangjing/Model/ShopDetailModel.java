package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

//商家详细信息model
public class ShopDetailModel extends BaseBean {
	private String addr = "";
	private String area = "";
	private String averp = "";
	public Bitmap bitMap = null;
	private int cityId = 0;
	private String desc = "";
	private String details = "";
	private String end = "";
	private String icon = "";
	private String id = "";
	private String iscoll = "";
	private double latitude;
	private String lev = "";
	private double longitude;
	private String lonmon = "";
	private String name = "";
	private String note = "";
	private String ph = "";
	private String poly = "";
	private String rec = "";
	private float star = 0.0F;
	private String start = "";
	private String status = "";
	private int Hasmenupic = 0;//是否是图片菜单
	
	public ShopDetailModel()
	{
		
	}
	// Json转换成model
	public ShopDetailModel(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setId(localJSONObject.getString("id"));
			setAddr(localJSONObject.getString("addr"));
			setArea(localJSONObject.getString("area"));
			setAverp(localJSONObject.getString("averp"));
			setCityId(localJSONObject.getInt("cityId"));
			setDesc(localJSONObject.getString("desc"));
			setDetails(localJSONObject.getString("details"));
			setEnd(localJSONObject.getString("end"));
			setIcon(localJSONObject.getString("icon"));
			setIscoll(localJSONObject.getString("iscoll"));
			setLatitude(localJSONObject.getDouble("latitude"));
			setLev(localJSONObject.getString("lev"));
			setLonmon(localJSONObject.getString("lonmon"));
			setLongitude(localJSONObject.getDouble("longitude"));
			setName(localJSONObject.getString("name"));
			setNote(localJSONObject.getString("note"));
			setPh(localJSONObject.getString("ph"));
			setPoly(localJSONObject.getString("poly"));
			setRec(localJSONObject.getString("rec"));
			setStar(localJSONObject.getInt("star"));
			setStart(localJSONObject.getString("start"));
			setStatus(localJSONObject.getString("status"));
			setHasmenupic(localJSONObject.getInt("hasmenupic"));
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}
	
	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("status", this.status);
			localJSONObject1.put("desc", this.desc);
			localJSONObject1.put("name", this.name);
			localJSONObject1.put("id", this.id);
			localJSONObject1.put("lev", this.lev);
			localJSONObject1.put("icon", this.icon);
			localJSONObject1.put("star", this.star);
			localJSONObject1.put("averp", this.averp);
			localJSONObject1.put("iscoll", this.iscoll);
			localJSONObject1.put("addr", this.addr);
			localJSONObject1.put("ph", this.ph);
			localJSONObject1.put("details", this.details);
			localJSONObject1.put("rec", this.rec);
			localJSONObject1.put("latitude", this.latitude);
			localJSONObject1.put("longitude", this.longitude);
			localJSONObject1.put("start", this.start);
			localJSONObject1.put("end", this.end);
			localJSONObject1.put("lonmon", this.lonmon);
			localJSONObject1.put("area", this.area);
			localJSONObject1.put("note", this.note);
			localJSONObject1.put("cityId", this.cityId);
			localJSONObject1.put("poly", this.poly);
			localJSONObject1.put("hasmenupic", getHasmenupic());
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String getAddr() {
		return this.addr;
	}

	public String getArea() {
		return this.area;
	}

	public String getAverp() {
		return this.averp;
	}

	@Override
	public String getCacheKey() {
		if ((this.id == null) || ((this.id != null) && (this.id.equals("")))) {
			return "-1";
		}

		return this.id;
	}

	public int getCityId() {
		return this.cityId;
	}

	public String getDesc() {
		return this.desc;
	}

	public String getDetails() {
		return this.details;
	}

	public String getEnd() {
		return this.end;
	}

	public String getIcon() {
		return this.icon;
	}

	public String getId() {
		return this.id;
	}

	public String getIscoll() {
		return this.iscoll;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public String getLev() {
		return this.lev;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public String getLonmon() {
		return this.lonmon;
	}

	public String getName() {
		return this.name;
	}

	public String getNote() {
		return this.note;
	}

	public String getPh() {
		return this.ph;
	}

	public String getPoly() {
		return this.poly;
	}

	public String getRec() {
		return this.rec;
	}

	public float getStar() {
		return this.star;
	}

	public String getStart() {
		return this.start;
	}

	public String getStatus() {
		return this.status;
	}

	public void setAddr(String paramString) {
		this.addr = paramString;
	}

	public void setArea(String paramString) {
		this.area = paramString;
	}

	public void setAverp(String paramString) {
		this.averp = paramString;
	}

	public void setCityId(int paramInt) {
		this.cityId = paramInt;
	}

	public void setDesc(String paramString) {
		this.desc = paramString;
	}

	public void setDetails(String paramString) {
		this.details = paramString;
	}

	public void setEnd(String paramString) {
		this.end = paramString;
	}

	public void setIcon(String paramString) {
		this.icon = paramString;
	}

	public void setId(String paramString) {
		this.id = paramString;
	}

	public void setIscoll(String paramString) {
		this.iscoll = paramString;
	}

	public void setLatitude(double paramDouble) {
		this.latitude = paramDouble;
	}

	public void setLev(String paramString) {
		this.lev = paramString;
	}

	public void setLongitude(double paramDouble) {
		this.longitude = paramDouble;
	}

	public void setLonmon(String paramString) {
		this.lonmon = paramString;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setNote(String paramString) {
		this.note = paramString;
	}

	public void setPh(String paramString) {
		this.ph = paramString;
	}

	public void setPoly(String paramString) {
		this.poly = paramString;
	}

	public void setRec(String paramString) {
		this.rec = paramString;
	}

	public void setStar(float paramFloat) {
		this.star = paramFloat;
	}

	public void setStart(String paramString) {
		this.start = paramString;
	}

	public void setStatus(String paramString) {
		this.status = paramString;
	}

	public int getHasmenupic() {
		return this.Hasmenupic;
	}

	public void setHasmenupic(int value) {
		this.Hasmenupic = value;
	}
	
	@Override
	public ShopDetailModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}

		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.status = localJSONObject.getString("status");
			this.desc = localJSONObject.getString("desc");
			this.name = localJSONObject.getString("name");
			this.id = localJSONObject.getString("id");
			this.lev = localJSONObject.getString("lev");
			this.icon = localJSONObject.getString("icon");
			this.star = localJSONObject.getInt("star");
			this.averp = localJSONObject.getString("averp");
			this.iscoll = localJSONObject.getString("iscoll");
			this.addr = localJSONObject.getString("addr");
			this.ph = localJSONObject.getString("ph");
			this.details = localJSONObject.getString("details");
			this.rec = localJSONObject.getString("rec");
			this.latitude = localJSONObject.getDouble("latitude");
			this.longitude = localJSONObject.getDouble("longitude");
			this.start = localJSONObject.getString("start");
			this.end = localJSONObject.getString("end");
			this.lonmon = localJSONObject.getString("lonmon");
			this.area = localJSONObject.getString("area");
			this.note = localJSONObject.getString("note");
			this.cityId = localJSONObject.getInt("cityId");
			this.poly = localJSONObject.getString("poly");
			this.Hasmenupic = localJSONObject.getInt("hasmenupic");
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
