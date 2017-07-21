package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

//获取json后进行转换的model 
//因为接口中的json 和 提交订单时提交的订单json格式不太一样，
//所以此处用了两个不同的model来实现 有时间还是需要全部统一
public class FoodInOrderModelForGet extends BaseBean {
	private String id;
	private String name;
	private float price;
	private int count;
	private float packagefree = 0.0F;// 打包费
	private int isComment = 1;//0还没有评论，1已经评论了
	private int ActivitysType;//是否团购

	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();

		return localJSONObject1.toString();
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public float getPrice() {
		return this.price;
	}

	public int getCount() {
		return this.count;
	}

	public float getPackageFree() {
		return this.packagefree;
	}
	
	public int getIsComment() {
		return isComment;
	}

	public void setIsComment(int isComment) {
		this.isComment = isComment;
	}

	public int getActivitysType() {
		return ActivitysType;
	}

	public void setActivitysType(int activitysType) {
		ActivitysType = activitysType;
	}

	//"{\"Num\":1,\"FoodID\":\"5995\",\"FoodPrice\":7.0,
	//\"foodname\":\"金牌手撕包\",\"package\":\"0.00\"
	@Override
	public FoodInOrderModelForGet stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.id = localJSONObject.getString("id");
			this.name = localJSONObject.getString("name");
			this.price = Float.parseFloat(localJSONObject.getString("price"));
			this.count = localJSONObject.getInt("count");
			/*this.packagefree = Float.parseFloat(localJSONObject
					.getString("package"));*/

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
	
	public FoodInOrderModelForGet JsonToBean(JSONObject localJSONObject) {
		if (localJSONObject == null) {
			return null;
		}
		
		try {
			//JSONObject localJSONObject = new JSONObject(paramString);
			this.id = localJSONObject.getString("FoodID");
			this.name = localJSONObject.getString("foodname");
			this.price = Float.parseFloat(localJSONObject.getString("FoodPrice"));
			this.count = localJSONObject.getInt("Num");
			ActivitysType  = 0;//Integer.parseInt(localJSONObject.getString("activetype"));
			//isComment = localJSONObject.getInt("isreview");
			/*this.packagefree = Float.parseFloat(localJSONObject
					.getString("package"));*/

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
	
	public FoodInOrderModelForGet JsonToBeanInOrderList(JSONObject localJSONObject) {
		if (localJSONObject == null) {
			return null;
		}
		
		try {
			//JSONObject localJSONObject = new JSONObject(paramString);
			this.id = localJSONObject.getString("id");
			this.name = localJSONObject.getString("name");
			this.price = Float.parseFloat(localJSONObject.getString("price"));
			this.count = localJSONObject.getInt("count");
			ActivitysType  = 0;//Integer.parseInt(localJSONObject.getString("activetype"));
			//isComment = localJSONObject.getInt("isreview");
			/*this.packagefree = Float.parseFloat(localJSONObject
					.getString("package"));*/

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
