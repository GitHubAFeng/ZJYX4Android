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
	private int count = 0;// ������
	private int foodAttrCount = 0;// ʳƷ�������������������������͵�������
	private float totalprice = 0.0F;// �ܼ۸�
	public TagListModel tagListModel;
	private String status = "";// ����Ӫҵ �;�����µ�ʱ����дʱ���й����Ա����ڹ��ﳵ

	public float sendFee = 0.0f;//ʵ�ʼ����е����ͷѣ��п��ܵ���sentmoneyҲ�п���Ϊ0.0�����������������ͷѣ�

	public float minMoney;//���ͽ��
	private float fullFreeMoney = 0.0f;//���������ͷ�
	
	
	private float sentmoney;// ���ͷ���
	private float packagefree;// �����
	
	private String orderid;
	
	private String lat;
	private String lng;

	private String Cityid;// ���б��
	private String bid;// ��������
	
	private String cardpay;// ��Ʒ��֧�����
	
	private CouponListModel CID;//��Ʒ��
	private String CIDStringx;//����ȯ�������

	private int people;
	
	//����������ͷ����
	private float sendDistance;//���;���
	private float startSendFee = 0.0f;//�𲽼�
	private float SendFeeOfKM = 0.0f;//ÿ����Ӽ�
	private float minKM = 0.0f;//�������ٹ��￪ʼ�Ӽ�
	private float maxKM = 0.0f;//�������ٹ�����õڶ��۸�
	private float SendFeeAffKM = 0.0f;//�������ٹ���ڶ��۸�

	private float distance;//��������ַȷ������
	
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
		return new BigDecimal(Float.toString(a));//����ʹ��toString���ͣ��������ȲŻ���ȷ
	}
	public float add(float a, float b){//��
		BigDecimal b1 = fToB(a);
		BigDecimal b2 = fToB(b);
		
		return b1.add(b2).floatValue();
	}
	
	public float subtract(float a, float b){//��
		BigDecimal b1 = fToB(a);
		BigDecimal b2 = fToB(b);
		return b1.subtract(b2).floatValue();
	}
	
	public float multiply(float a, float b){//ʣ
		BigDecimal b1 = fToB(a);
		BigDecimal b2 = fToB(b);
		return b1.multiply(b2).floatValue();
	}
	
	public float divide(float a, float b){//��
		BigDecimal b1 = fToB(a);
		BigDecimal b2 = fToB(b);
		return b1.divide(b2, 2).floatValue();
	}
	public boolean isFreeSend(){//�Ƿ������ͷ�
		if ((fullFreeMoney > 0.0f && totalprice >= fullFreeMoney) || totalprice == 0.0f) {//�ﵽ�������ͷѽ��������ͷ�
			return true;
		}
		return false;
	}

	public void CalculationSendFee(){//�������ͷ�
		if (isFreeSend()) {
			sendFee = 0.0f;
		}else{
			if (startSendFee == 0.0f) {//δ�������ͷѼ��㷽ʽ����ôĬ�Ͼ�����վ�̶������ͷ�
				sendFee = sentmoney;
				return;
			}
			sendFee = startSendFee;
			if (maxKM > minKM && distance > maxKM) {//maxKM�������minKM ����û����
				//sendFee += ((maxKM - minKM) * SendFeeOfKM + (distance - maxKM) * SendFeeAffKM);
				sendFee = add(sendFee, add(multiply(subtract(maxKM, minKM), SendFeeOfKM), multiply(subtract(distance, minKM), SendFeeAffKM)));
			}else if(minKM > 0.0 && distance > minKM){//�����������
				//sendFee += (distance - minKM) * SendFeeOfKM;
				sendFee = add(sendFee, multiply(subtract(distance, minKM), SendFeeOfKM));
			}else{//���𲽼ۣ�����ÿ�������Ǯ
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

	// ��������
	public void setAddCount(int paramInt) {
		this.count = this.count + paramInt;
	}

	// �����ܼ�
	public void setAddTotalPrice(float paramFloat) {
		this.totalprice = this.totalprice + paramFloat;
	}

	// ���ٿ��
	public void setRemCount(int paramInt) {
		this.count = this.count - paramInt;
	}

	// �����ܼ�
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
	//20160719 ����
	public FoodInOrderModel getFoodInOrderModel(int postion) {
		int count = 0;
		if(postion < list.size()){
			return list.get(postion);
		}
		
		return null;
	}

	// ������ӦʳƷĳһ�����Ƿ��Ѿ����ڣ���������Ӧ�ĸ���
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
	// ������ӦʳƷĳһ�����Ƿ��Ѿ����ڣ���������Ӧ�ĸ���
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
		if (attr == null && listAttr == null) {//�д���������Ŀ��ֱ���½���
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
			// ѭ����ȡ���еĲ˵���Ϣ
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
