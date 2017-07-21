package com.ihangjing.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihangjing.common.HJInteger;

import android.R.integer;
import android.util.Log;

public class ShopCartModel {

	public ArrayList<FoodInOrderModel> list;

	private int shopid;
	private String shopname = "";
	private int count = 0;// 总数量
	private int foodAttrCount = 0;// 食品属性总数（不是数量，是类型的数量）
	private float totalprice = 0.0F;// 总价格
	public TagListModel tagListModel;
	private String status = "";// 属否营业 和具体的下单时的填写时间有关所以保存在购物车

	public float sendFee = 0.0f;//实际计算中的配送费，有可能等于sentmoney也有可能为0.0（满足满多少免配送费）

	public float minMoney;//起送金额
	private float fullFreeMoney = 0.0f;//满多少免送费
	
	
	private float sentmoney;// 配送费用
	private float packagefree;// 打包费
	
	private String orderid;
	
	private String lat;
	private String lng;

	private String Cityid;// 城市编号
	private String bid;// 建筑物编号
	
	private String cardpay;// 礼品卡支付金额
	
	private CouponListModel CID;//礼品卡
	private String CIDStringx;//店铺券自增编号

	private int people;
	
	//下面计算配送费相关
	private float sendDistance;//配送距离
	private float startSendFee = 0.0f;//起步价
	private float SendFeeOfKM = 0.0f;//每公里加价
	private float minKM = 0.0f;//超过多少公里开始加价
	private float maxKM = 0.0f;//超过多少公里采用第二价格
	private float SendFeeAffKM = 0.0f;//超过多少公里第二价格

	private float distance;//距离最后地址确定计算
	
	private String Ship1Start;
	private String Ship1End;
	private String Ship2Start;
	private String Ship2End;
	
	public float getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(float totalprice) {
		this.totalprice = totalprice;
	}

	public TagListModel getTagListModel() {
		return tagListModel;
	}

	public void setTagListModel(TagListModel tagListModel) {
		this.tagListModel = tagListModel;
	}

	public String getShip1Start() {
		return Ship1Start;
	}

	public void setShip1Start(String ship1Start) {
		Ship1Start = ship1Start;
	}

	public String getShip1End() {
		return Ship1End;
	}

	public void setShip1End(String ship1End) {
		Ship1End = ship1End;
	}

	public String getShip2Start() {
		return Ship2Start;
	}

	public void setShip2Start(String ship2Start) {
		Ship2Start = ship2Start;
	}

	public String getShip2End() {
		return Ship2End;
	}

	public void setShip2End(String ship2End) {
		Ship2End = ship2End;
	}

	public int getShopid() {
		return shopid;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getStartSendFee() {
		return startSendFee;
	}

	public void setStartSendFee(float startSendFee) {
		this.startSendFee = startSendFee;
	}

	public float getSendFeeOfKM() {
		return SendFeeOfKM;
	}

	public void setSendFeeOfKM(float sendFeeOfKM) {
		SendFeeOfKM = sendFeeOfKM;
	}

	public float getMinKM() {
		return minKM;
	}

	public void setMinKM(float minKM) {
		this.minKM = minKM;
	}

	public float getMaxKM() {
		return maxKM;
	}

	public void setMaxKM(float maxKM) {
		this.maxKM = maxKM;
	}

	public float getSendFeeAffKM() {
		return SendFeeAffKM;
	}

	public void setSendFeeAffKM(float sendFeeAffKM) {
		SendFeeAffKM = sendFeeAffKM;
	}
	public BigDecimal fToB(float a)
	{
		return new BigDecimal(Float.toString(a));//必须使用toString类型，这样精度才会正确
	}
	public float add(float a, float b){//加
		BigDecimal b1 = fToB(a);
		BigDecimal b2 = fToB(b);
		
		return b1.add(b2).floatValue();
	}
	
	public float subtract(float a, float b){//减
		BigDecimal b1 = fToB(a);
		BigDecimal b2 = fToB(b);
		return b1.subtract(b2).floatValue();
	}
	
	public float multiply(float a, float b){//剩
		BigDecimal b1 = fToB(a);
		BigDecimal b2 = fToB(b);
		return b1.multiply(b2).floatValue();
	}
	
	public float divide(float a, float b){//除
		BigDecimal b1 = fToB(a);
		BigDecimal b2 = fToB(b);
		return b1.divide(b2, 2).floatValue();
	}
	public boolean isFreeSend(){//是否面配送费
		if ((fullFreeMoney > 0.0f && totalprice >= fullFreeMoney) || totalprice == 0.0f) {//达到了面配送费金额剪掉配送费
			return true;
		}
		return false;
	}

	public void CalculationSendFee(){//计算配送费
		if (isFreeSend()) {
			sendFee = 0.0f;
		}else{
			if (startSendFee == 0.0f) {//未设置配送费计算方式，那么默认就用网站固定的配送费
				sendFee = sentmoney;
				return;
			}
			sendFee = startSendFee;
			if (maxKM > minKM && distance > maxKM) {//maxKM必须大于minKM 否则没意义
				//sendFee += ((maxKM - minKM) * SendFeeOfKM + (distance - maxKM) * SendFeeAffKM);
				sendFee = add(sendFee, add(multiply(subtract(maxKM, minKM), SendFeeOfKM), multiply(subtract(distance, minKM), SendFeeAffKM)));
			}else if(minKM > 0.0 && distance > minKM){//无最大公里限制
				//sendFee += (distance - minKM) * SendFeeOfKM;
				sendFee = add(sendFee, multiply(subtract(distance, minKM), SendFeeOfKM));
			}else{//无起步价，就是每公里多少钱
				sendFee = add(sendFee, multiply(distance, SendFeeOfKM));
				//sendFee += distance * SendFeeOfKM;
			}
		}
		
	}
	

	public float getSendDistance() {
		return sendDistance;
	}



	public void setSendDistance(float sendDistance) {
		this.sendDistance = sendDistance;
	}



	public float getSendFee() {
		return sendFee;
	}



	public void setSendFee(float sendFee) {
		this.sendFee = sendFee;
	}



	public float getFullFreeMoney() {
		return fullFreeMoney;
	}



	public void setFullFreeMoney(float fullFreeMoney) {
		this.fullFreeMoney = fullFreeMoney;
	}



	public ArrayList<FoodInOrderModel> getList() {
		return list;
	}
	
	

	public void setList(ArrayList<FoodInOrderModel> list) {
		this.list = list;
	}

	public int getPeople() {
		return people;
	}



	public void setPeople(int people) {
		this.people = people;
	}



	

	

	public double getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(float minMoney) {
		this.minMoney = minMoney;
	}

	

	public float getSentmoney() {
		return sentmoney;
	}

	public void setSentmoney(float sentmoney) {
		this.sentmoney = sentmoney;
	}

	



	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public float getPackagefree() {
		return packagefree;
	}

	public void setPackagefree(float packagefree) {
		this.packagefree = packagefree;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	

	public String getCityid() {
		return Cityid;
	}

	public void setCityid(String cityid) {
		Cityid = cityid;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	

	public String getCardpay() {
		return cardpay;
	}

	public void setCardpay(String cardpay) {
		this.cardpay = cardpay;
	}
	public CouponListModel getCID() {
		return CID;
	}

	public void setCID(CouponListModel cID) {
		CID = cID;
	}

	public String getCIDStringx() {
		return CIDStringx;
	}

	public void setCIDStringx(String cIDStringx) {
		CIDStringx = cIDStringx;
	}

	

	

	public void setFoodAttrCount(int foodAttrCount) {
		this.foodAttrCount = foodAttrCount;
	}

	public ShopCartModel() {
		this.list = new ArrayList<FoodInOrderModel>();
	}

	public int getShopId() {
		return this.shopid;
	}

	public int getFoodAttrCount() {

		return foodAttrCount;
	}

	public String getShopname() {
		return this.shopname;
	}

	public void setShopid(int paramString) {
		this.shopid = paramString;
	}

	public void setShopname(String paramString) {
		this.shopname = paramString;
	}

	public float getTotalPrice() {
		return this.totalprice;
	}

	public int getCount() {
		return this.count;
	}

	public void setTotalPrice(float paramFloat) {
		this.totalprice = paramFloat;
	}

	public void setCount(int paramInt) {
		this.count = paramInt;
	}

	// 增加数量
	public void setAddCount(int paramInt) {
		this.count = this.count + paramInt;
	}

	// 增加总价
	public void setAddTotalPrice(float paramFloat) {
		this.totalprice = this.totalprice + paramFloat;
	}

	// 减少库存
	public void setRemCount(int paramInt) {
		this.count = this.count - paramInt;
	}

	// 减少总价
	public void setRemTotalPrice(float paramFloat) {
		this.totalprice = this.totalprice - paramFloat;
	}

	public void setStatus(String paramString) {
		this.status = paramString;
	}

	public String getStatus() {
		return this.status;
	}
	
	

	public FoodInOrderModel getFoodInOrderModel(HJInteger index) {
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			count += list.get(i).listSize.size();
			if (index.value < count) {
				count -= list.get(i).listSize.size();
				index.value -= count;
				return list.get(i);
			}
		}
		return null;
	}
	//20160719 新增
	public FoodInOrderModel getFoodInOrderModel(int postion) {
		int count = 0;
		if(postion < list.size()){
			return list.get(postion);
		}
		
		return null;
	}

	// 返回相应食品某一属性是否已经存在，并返回相应的个数
	public int getFoodCountInAttr(int foodId, int attrId) {
		int length = list.size();
		if (length > 0) {
			FoodInOrderModel model;
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (foodId == model.getId()) {
					length = model.listSize.size();
					if (length > 0) {
						for (int j = 0; j < length; j++) {
							if (attrId == model.listSize.get(j).cId) {
								return model.listSize.get(j).count;
							}
						}
					}
					return 0;
				}
			}
		}
		return 0;
	}
	// 返回相应食品某一属性是否已经存在，并返回相应的个数
		public int getOneFoodCount(int foodId) {
				int length = list.size();
				if (length > 0) {
					FoodInOrderModel model;
					
					for (int i = 0; i < length; i++) {
						model = list.get(i);
						if (foodId == model.getId()) {
							length = model.listSize.size();
							if (length > 0) {
								int count = 0;
								for (int j = 0; j < length; j++) {
									count += model.listSize.get(j).count;
									
								}
								return count;
							}
							return 0;
						}
					}
				}
				return 0;
			}
	public float getFoodPrice(int foodId) {
		int length = list.size();
		if (length > 0) {
			FoodInOrderModel model;
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (foodId == model.getId()) {

					return model.getPrice();
				}
			}
		}
		return 0.00f;
	}
	
	public int setFoodAttr(int foodid, int attrIndex, int ncount) {
		int length = list.size();
		float price = 0.0f;
		FoodInOrderModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (foodid == model.getId()) {
					length = model.listSize.size();
					if (attrIndex < length) {
						ncount -= model.listSize.get(attrIndex).count;
						model.listSize.get(attrIndex).count += ncount;
						//price = ncount * model.listSize.get(attrIndex).price;
						price = multiply(ncount, model.listSize.get(attrIndex).price);
						model.setCount(model.getCount() + ncount);
						model.setPrice(add(model.getPrice(), price));
						count += ncount;
						//totalprice += price;
						totalprice = add(totalprice, price);
						ncount = model.listSize.get(attrIndex).count;
						if (ncount == 0) {
							model.listSize.remove(attrIndex);
							foodAttrCount--;
						}
						if (model.getCount() == 0) {
							list.remove(i);
						}
						return ncount;

					}
					return 0;

				}
			}
		}

		return 0;
	}

	//
	public int addFoodAttr(int foodid, int attrIndex) {
		int length = list.size();

		FoodInOrderModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (foodid == model.getId()) {
					length = model.listSize.size();
					if (attrIndex < length) {

						model.listSize.get(attrIndex).count++;
						model.setCount(model.getCount() + 1);
						model.setPrice(add(model.getPrice()
								, model.listSize.get(attrIndex).price));
						count++;
						//totalprice += model.listSize.get(attrIndex).price;
						totalprice = add(totalprice, model.listSize.get(attrIndex).price);
						//packagefree += model.getPackagefree();
						packagefree = add(packagefree, model.getPackagefree());
						return model.listSize.get(attrIndex).count;

					}
					return 0;

				}
			}
		}

		return 0;
	}
	
	public int setFoodAttr(FoodModel food, int attrIndex, int ncount) {
		int length = list.size();
		float nprice = 0.0f;
		FoodInOrderModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (food.getFoodId() == model.getId()) {
					length = model.listSize.size();
					if (length > 0) {
						for (int j = 0; j < length; j++) {
							if (food.listSize.get(attrIndex).cId == model.listSize
									.get(j).cId) {
								ncount -= model.listSize.get(j).count;
								//nprice = food.listSize.get(attrIndex).price * ncount;
								nprice = multiply(food.listSize.get(attrIndex).price, ncount);
								model.listSize.get(j).count += ncount;
								model.setCount(model.getCount() + ncount);
								model.setPrice(add(model.getPrice()
										, nprice));
								ncount = model.listSize.get(j).count;
								if (ncount == 0) {
									model.listSize.remove(j);
								}
								if (model.getCount() == 0) {
									list.remove(i);
								}
								return ncount;
							}
						}
					}
					if (ncount > 0) {
						foodAttrCount++;
						model.setCount(model.getCount() + ncount);
						food.listSize.get(attrIndex).count = ncount;
						//nprice = food.listSize.get(attrIndex).price * ncount;
						nprice = multiply(food.listSize.get(attrIndex).price, ncount);
						model.setPrice(add(model.getPrice()
								, nprice));
						model.setImageLocalPath(food.getImageNetPath());
						model.listSize.add(food.listSize.get(attrIndex));
						count += ncount;
						//totalprice += nprice;
						totalprice = add(totalprice, nprice);
					}
					
					
					return ncount;

				}
			}
		}
		if (ncount > 0) {
			model = new FoodInOrderModel();
			model.setId(food.getFoodId());
			model.setTogoId(food.getTogoID());
			model.setName(food.getFoodName());
			model.setCount(ncount);
			//nprice = food.listSize.get(attrIndex).price * ncount;
			nprice = multiply(food.listSize.get(attrIndex).price, ncount);
			model.setPrice(nprice);
			model.setImageLocalPath(food.getImageNetPath());
			food.listSize.get(attrIndex).count = ncount;
			model.listSize.add(food.listSize.get(attrIndex));
			list.add(model);
			foodAttrCount++;
			count += ncount;
			//totalprice += nprice;
			totalprice = add(totalprice, nprice);
		}
		
		return ncount;
	}

	public int addFoodAttr(FoodModel food, int attrIndex, FoodAttrModel attr, ArrayList<FoodAttrModel> listAttr, int Count) {
		int length = list.size();
		count += Count;
		//totalprice += food.listSize.get(attrIndex).price;
		totalprice = add(totalprice, food.listSize.get(attrIndex).price);
		FoodInOrderModel model;
		String pString = "";
		float pprice = 0.0f;
		if (attr == null && listAttr == null) {//有传入配料项目，直接新建，
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					model = list.get(i);
					if (food.getFoodId() == model.getId()) {
						length = model.listSize.size();
						if (length > 0) {
							for (int j = 0; j < length; j++) {
								if (food.listSize.get(attrIndex).cId == model.listSize
										.get(j).cId) {
									model.listSize.get(j).count += Count;
									model.setCount(model.getCount() + 1);
									model.setPrice(add(model.getPrice()
											, multiply(food.listSize.get(attrIndex).price, Count)));
									//packagefree += (model.getPackagefree() * Count);
									packagefree = add(packagefree, multiply(model.getPackagefree(), Count));
									return model.listSize.get(j).count;
								}
							}
						}
						foodAttrCount++;
						model.setpName(pString);
						model.setpPrice(pprice);
						model.setCount(model.getCount() + 1);
						food.listSize.get(attrIndex).count = Count;
						model.setPrice(add(model.getPrice()
								, multiply(food.listSize.get(attrIndex).price, Count)));
						model.setImageLocalPath(food.getImageNetPath());
						model.listSize.add(food.listSize.get(attrIndex));
						//packagefree += (model.getPackagefree() * Count);
						packagefree = add(packagefree, multiply(model.getPackagefree(), Count));
						return Count;

					}
				}
			}
		}else{
			if (attr != null) {
				pString += attr.name;
				//pprice += attr.price;
				pprice = add(pprice, attr.price);
			}
			for (int i = 0; listAttr != null && i < listAttr.size(); i++) {
				pString += ("," + listAttr.get(i).name);
				//pprice += listAttr.get(i).price;
				pprice = add(pprice, listAttr.get(i).price);
			}
		}
		
		model = new FoodInOrderModel();
		
		model.setpName(pString);
		model.setpPrice(pprice);
		model.setPackagefree(food.getPackagefree());
		model.setActivitysID(food.getActivitysID());
		model.setActivitysType(food.getActivitysType());
		model.setTogoName(food.getTogoName());
		model.setId(food.getFoodId());
		model.setTogoId(food.getTogoID());
		model.setName(food.getFoodName());
		model.setCount(Count);
		model.setStock(food.getStock());
		model.setPrice(multiply(food.listSize.get(attrIndex).price, Count));
		model.setImageLocalPath(food.getImageNetPath());
		food.listSize.get(attrIndex).count = Count;
		model.listSize.add(food.listSize.get(attrIndex));
		
		list.add(model);
		//packagefree += (model.getPackagefree() * Count);
		packagefree = add(packagefree, multiply(model.getPackagefree(), Count));
		foodAttrCount++;
		return Count;
	}
	
	public void removeFood(int foodid, int attrIndex) {
		int length = list.size();
		FoodInOrderModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (foodid == model.getId()) {
					length = model.listSize.size();
					if (attrIndex < length) {
						foodAttrCount--;
						model.setCount(model.getCount() - model.listSize.get(attrIndex).count);
						
						float price = multiply(model.listSize.get(attrIndex).count, model.listSize.get(attrIndex).price);
						
						model.setPrice(subtract(model.getPrice(), price));
						//this.totalprice -= price;
						totalprice = subtract(totalprice, price);
						this.count -= model.listSize.get(attrIndex).count;
						model.listSize.remove(attrIndex);
						if (model.getCount() == 0) {
							list.remove(i);
						}
						return;
						
					}
					
				}
			}
		}
	}

	public int delFoodAttr(int foodid, int attrIndex) {
		int length = list.size();
		FoodInOrderModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (foodid == model.getId()) {
					length = model.listSize.size();
					if (attrIndex < length) {

						model.listSize.get(attrIndex).count--;
						model.setPrice(subtract(model.getPrice()
								, model.listSize.get(attrIndex).price));
						count--;
						//totalprice -= model.listSize.get(attrIndex).price;
						totalprice = subtract(totalprice, model.listSize.get(attrIndex).price);
						//packagefree -= model.getPackagefree();
						packagefree = subtract(packagefree, model.getPackagefree());
						length = model.listSize.get(attrIndex).count;
						if (model.listSize.get(attrIndex).count == 0) {
							model.listSize.remove(attrIndex);
							foodAttrCount--;
							length = 0;
						}
						model.setCount(model.getCount() - 1);
						
						if (model.getCount() == 0) {
							list.remove(i);

							return 0;
						}
						return length;

					}
					return 0;
				}
			}
		}
		return 0;
	}
	
	public void removeFood(int foodid) {
		int length = list.size();
		FoodInOrderModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				length = model.listSize.size();
				if (foodid == model.getId()) {
					for(; length > 0; length--){
						foodAttrCount--;
						model.setCount(model.getCount() - model.listSize.get(0).count);
						
						float price = multiply(model.listSize.get(0).count, model.listSize.get(0).price);
						
						model.setPrice(subtract(model.getPrice(), price));
						//this.totalprice -= price;
						totalprice = subtract(totalprice, price);
						this.count -= model.listSize.get(0).count;
						model.listSize.remove(0);
						if (model.getCount() == 0) {
							list.remove(i);
						}
						
					}
					return;
					
				}
			}
		}
	}

	public int delFoodAttr(FoodModel food, int attrIndex) {
		int length = list.size();
		FoodInOrderModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (food.getFoodId() == model.getId()) {
					length = model.listSize.size();
					if (length > 0) {
						for (int j = 0; j < length; j++) {
							if (food.listSize.get(attrIndex).cId == model.listSize
									.get(j).cId) {
								model.listSize.get(j).count--;
								
								model.setPrice(subtract(model.getPrice(), food.listSize.get(attrIndex).price));
								count--;
								//totalprice -= food.listSize.get(attrIndex).price;
								totalprice = subtract(totalprice, food.listSize.get(attrIndex).price);
								//packagefree -= food.getPackagefree();
								packagefree = subtract(packagefree, food.getPackagefree());
								length = model.listSize.get(j).count;
								if (model.listSize.get(j).count == 0) {
									model.listSize.remove(j);
									foodAttrCount--;
								}
								model.setCount(model.getCount() - 1);
								
								if (model.getCount() == 0) {
									list.remove(i);

									return 0;
								}
								return length;
							}
						}
					}
					return 0;
				}
			}
		}
		return 0;
	}

	

	public ShopCartModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}

		try {
			this.list.clear();
			JSONObject localJSONObject = new JSONObject(paramString);

			this.shopid = localJSONObject.getInt("shopid");
			this.shopname = localJSONObject.getString("shopname");
			this.status = localJSONObject.getString("status");

			this.totalprice = Float.parseFloat(localJSONObject
					.getString("totalprice"));

			Log.v("ShopCartModel",
					"stringToBean: " + String.valueOf(this.totalprice));

			this.people = localJSONObject.getInt("people");

			JSONArray localJSONArray = localJSONObject.getJSONArray("list");
			int k = 0;
			// 循环读取所有的菜单信息
			while (true) {
				int m = localJSONArray.length();
				if (k >= m) {
					break;
				}
				FoodInOrderModel localShopBean1 = new FoodInOrderModel();
				String str3 = (String) localJSONArray.get(k);
				this.list.add(localShopBean1.stringToBean(str3));
				k += 1;
			}
		} catch (JSONException localJSONException) {
			Log.v("ShopCartModel", "catch (JSONException localJSONException)");
			localJSONException.printStackTrace();
		}
		return this;
	}

	public String beanToStringFix(int type) {
		// TODO Auto-generated method stub
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("TogoId", String.valueOf(shopid));
			
			localJSONObject1.put("Lat", lat);
			localJSONObject1.put("Lng", lng);
				

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<FoodInOrderModel> localIterator = this.list.iterator();
			
			FoodInOrderModel model;
			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("ItemList", localJSONArray1);
					break;
				}
				model = localIterator.next();
				String tempString = model.beanToStringFix(type);
				
				localJSONArray1.put(tempString);
			}
			

			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			
			localJSONException.printStackTrace();
		}
		return null;
	}
	public String beanToStringFix(JSONObject localJSONObject1) {
		// TODO Auto-generated method stub
		
		try {
			
			//JSONObject localJSONObject1 = new JSONObject();
			localJSONObject1.put("TogoId", String.valueOf(shopid));
			
			localJSONObject1.put("Lat", lat);
			localJSONObject1.put("Lng", lng);
				

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<FoodInOrderModel> localIterator = this.list.iterator();
			
			FoodInOrderModel model;
			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("cartlist", localJSONArray1);
					break;
				}
				model = localIterator.next();
				String tempString = model.beanToStringFix(0);
				
				localJSONArray1.put(tempString);
			}
			

			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			
			localJSONException.printStackTrace();
		}
		return null;
	}
}
