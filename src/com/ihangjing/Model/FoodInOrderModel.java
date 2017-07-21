package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FoodInOrderModel extends BaseBean {
	private int id;
	private String name;
	private float price;//总价
	private int count;//总数
	private int foodStock = -1;//餐品限量∂
	public List<FoodMakeModel> listMake;// 做法
	public List<FoodSizeModel> listSize;// 规格
	private String ImageLocalPath;//图片本地路径
	private float packagefree = 0.0F;// 打包费
	private int togoId;//商家编号
	private String togoName;//商家名称
	private int ActivitysID = 0;//活动编号
	private int ActivitysType = 0;//活动类型 0普通 1.团购 2.秒杀 3.限量 4.买撘赠
	private int stock = 0;//库存
	
	private String pName;//额外项目名称
	private float pPrice;//额外项目价格总和
	
	
	
	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public float getpPrice() {
		return pPrice;
	}

	public void setpPrice(float pPrice) {
		this.pPrice = pPrice;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getActivitysID() {
		return ActivitysID;
	}

	public void setActivitysID(int activitysID) {
		ActivitysID = activitysID;
	}

	public int getActivitysType() {
		return ActivitysType;
	}

	public void setActivitysType(int activitysType) {
		ActivitysType = activitysType;
	}
	public String getTogoName() {
		return togoName;
	}
	public void setTogoName(String togoName) {
		this.togoName = togoName;
	}
	public FoodInOrderModel(){
		listSize = new ArrayList<FoodSizeModel>();
	}
	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			localJSONObject1.put("id", this.id);
			localJSONObject1.put("name", this.name);
			localJSONObject1.put("price", this.price);
			localJSONObject1.put("count", this.count);
			localJSONObject1.put("packagefree", this.packagefree);
			
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}
	
	
	public int getTogoId() {
		return togoId;
	}
	public void setTogoId(int togoId) {
		this.togoId = togoId;
	}
	public String beanToStringFix(int type) {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			String value = "";
			int size = listSize.size();
			for (int i = 0; i < size;) {
				if (type == 0) {//外卖
					localJSONObject1.put("owername", String.valueOf(this.packagefree));
					localJSONObject1.put("ReveInt1", String.valueOf(ActivitysID));//商品活动所属编号
					localJSONObject1.put("ReveInt2", String.valueOf(ActivitysType));//商品活动类型：0普通 1.团购 2.秒杀 3.限量 4.买撘赠
					localJSONObject1.put("addprice", String.valueOf(this.pPrice));
					localJSONObject1.put("Material", pName);
					localJSONObject1.put("sid", String.valueOf(listSize.get(i).cId));
					localJSONObject1.put("sname", String.valueOf(listSize.get(i).name));
				}
				localJSONObject1.put("PId", String.valueOf(this.id));
				if(listSize.get(i).name != null && listSize.get(i).name.length() > 0){
					localJSONObject1.put("PName", this.name + "_" + listSize.get(i).name);
					
				}else{
					localJSONObject1.put("PName", this.name/* + "(" + listSpec.get(i).name + ")"*/);
				}
				
				localJSONObject1.put("PPrice", String.valueOf(listSize.get(i).price));
				localJSONObject1.put("Currentprice", listSize.get(i).price);
				localJSONObject1.put("PNum", String.valueOf(this.count));
				
				localJSONObject1.put("TogoId", String.valueOf(togoId));
				
				value += localJSONObject1.toString();
				i++;
				if (i == size) {
					break;
				}else{
					value += ",";
				}
			}
			return value;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	

	public String getImageLocalPath() {
		return ImageLocalPath;
	}

	public void setImageLocalPath(String imageLocalPath) {
		ImageLocalPath = imageLocalPath;
	}

	public float getPackagefree() {
		return packagefree;
	}

	public void setPackagefree(float packagefree) {
		this.packagefree = packagefree;
	}

	public int getFoodStock() {
		return foodStock;
	}

	public void setFoodStock(int foodStock) {
		this.foodStock = foodStock;
	}

	public int getId() {
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

	public void setId(int paramString) {
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
	public FoodInOrderModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		Log.v("FoodInOrderModel", "stringToBean:" + paramString);

		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.id = localJSONObject.getInt("id");
			this.name = localJSONObject.getString("name");
			this.price = Float.parseFloat(localJSONObject.getString("price"));
			this.count = localJSONObject.getInt("count");
			this.packagefree = Float.parseFloat(localJSONObject.getString("packagefree"));
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
