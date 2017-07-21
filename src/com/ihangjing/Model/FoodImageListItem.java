package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class FoodImageListItem extends BaseBean{
	private String pid = "";
	private String pic = "";
	private String togoid = "";
	
	public FoodImageListItem()
	{
		
	}
	// Json×ª»»³Émodel
	public FoodImageListItem(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setPid(localJSONObject.getString("pid"));
			setPic(localJSONObject.getString("pic"));
			setTogoid(localJSONObject.getString("togoid"));

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}
	
	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("pid", this.pid);
			localJSONObject1.put("pic", this.pic);
			localJSONObject1.put("togoid", this.togoid);
			
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String getPid() {
		return this.pid;
	}
	
	public void setPid(String paramString) {
		this.pid = paramString;
	}
	
	public String getPic() {
		return this.pic;
	}
	
	public void setPic(String paramString) {
		this.pic = paramString;
	}
	
	public String getTogoid() {
		return this.togoid;
	}
	
	public void setTogoid(String paramString) {
		this.togoid = paramString;
	}
	
	@Override
	public FoodImageListItem stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}

		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.pid = localJSONObject.getString("pid");
			this.pic = localJSONObject.getString("pic");
			this.togoid = localJSONObject.getString("togoid");
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
