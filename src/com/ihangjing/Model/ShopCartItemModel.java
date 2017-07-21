package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopCartItemModel extends BaseBean {
	private String id;
	private String name;
	private float price;
	private int count;
	private float packagefree = 0.0F;// 打包费

	//{\"PNum\":\"1\",\"PId\":\"8009\",\"PPrice\":\"16\",
	//\"PName\":\"猴头菇鸡汤\",\"Foodcurrentprice\":\"8\",\"owername\":\"1.6\"}
	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			localJSONObject1.put("PId", this.id);
			localJSONObject1.put("PName", this.name);
			localJSONObject1.put("PPrice", String.valueOf(this.price));
			localJSONObject1.put("Foodcurrentprice", String.valueOf(this.price));
			localJSONObject1.put("PNum", String.valueOf(this.count));
			localJSONObject1.put("owername", String.valueOf(this.packagefree));

			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
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

	public void setCount(int paramString) {
		this.count = paramString;
	}

	public void setId(String paramString) {
		this.id = paramString;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setPrice(float paramString) {
		this.price = paramString;
	}

	public float getPackageFree() {
		return this.packagefree;
	}

	public void setPackageFree(float paramFloat) {
		this.packagefree = paramFloat;
	}

	// 增加数量
	public void setAddCount(int paramInt) {
		this.count = this.count + paramInt;
	}

	// 减少数量
	public void setRemCount(int paramInt) {
		this.count = this.count - paramInt;
	}

	@Override
	public BaseBean stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.id = localJSONObject.getString("FoodID");
			this.name = localJSONObject.getString("foodname");
			this.price = Float.parseFloat(localJSONObject.getString("FoodPrice"));
			this.count = localJSONObject.getInt("Num");
			this.packagefree = Float.parseFloat(localJSONObject
					.getString("package"));

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		
		return this;
	}
}
