package com.ihangjing.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihangjing.common.HJInteger;

public class MyShopCart {
	
	public ArrayList<ShopCartModel> list;
	
	private int count = 0;// 总数量
	private int foodAttrCount = 0;// 食品属性总数（不是数量，是类型的数量）
	private float totalprice = 0.0F;// 总价格
	
	private String status = "";// 属否营业 和具体的下单时的填写时间有关所以保存在购物车

	public float sendFee = 0.0f;

	public double minMoney;
	
	
	private String sentmoney;// 配送费用
	private float packagefree;// 打包费
	private String orderid;
	
	private String lat;
	private String lng;

	private String Cityid;// 城市编号
	private String bid;// 建筑物编号
	
	private String userid;
	private String userName;//接受人
	private String CustName;// 会员名
	
	private String note;
	private String phone;
	private String address;// 地址
	private String eattime;// 配送时间
	private String addtime;// 下单时间
	private String state;// 订单状态

	private String Oorderid;// 需要代收货款,不否需要为0
	private String payType;// 支付方式:0线上支付,1线下支付
	private String PayMode;// 支付方式 1,支付宝支付;2,礼品卡支付;3,货到付款;4,到店自提;5,到店消费;
	private String ordersource = "2";// 订单来源:0表示web,2表示android,1表示ios
	private String sendtype;// 配送方式:1送货上门;2上门自提;3到店消费
	private String usersn;
	private String PayPassword;
	private String reciveAddressID;
	private int people = 1;

	private String selfStateID = "0";//自提站点

	private int OrderType = 0;//0外卖，1预定

	private String roomID = "";

	private String roomName = "";

	private int roomType = 0;

	private int timeType = 0;

	private int buyType = 1;

	private String shopID = "";

	private String SLat;

	private String SLng;
	
	public void clear() {
		// TODO Auto-generated method stub
		list.clear();
		foodAttrCount = 0;
		count = 0;
		packagefree = 0.0f;
		totalprice = 0.0f;
		sendFee = 0.0f;
		minMoney = 0.0f;
		
	}
	public void clear(int shopID, HJInteger index) {
		// TODO Auto-generated method stub
		if(index.value < 0){
			getFoodCountWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size()){
			ShopCartModel shopCartModel = list.get(index.value);
			list.get(index.value).list.clear();
			foodAttrCount -= shopCartModel.getFoodAttrCount();
			count -= shopCartModel.getCount();
			packagefree -= shopCartModel.getPackagefree();
			totalprice -= shopCartModel.getTotalprice();
			sendFee -= shopCartModel.getSendFee();
			minMoney = 0.0f;
			shopCartModel.list.clear();
			list.remove(index.value);
		}
		
	}
	public ShopCartModel getShopCarModel(int shopID, HJInteger index) {
		// TODO Auto-generated method stub
		if(index.value < 0){
			getFoodCountWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size()){
			return list.get(index.value);
			
		}
		return null;
	}
	public MyShopCart(){
		list = new ArrayList<ShopCartModel>();
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getFoodAttrCount() {
		return foodAttrCount;
	}

	public void setFoodAttrCount(int foodAttrCount) {
		this.foodAttrCount = foodAttrCount;
	}

	public float getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(float totalprice) {
		this.totalprice = totalprice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getSendFee() {
		return sendFee;
	}

	public void setSendFee(float sendFee) {
		this.sendFee = sendFee;
	}

	public double getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(double minMoney) {
		this.minMoney = minMoney;
	}

	public String getSentmoney() {
		return sentmoney;
	}

	public void setSentmoney(String sentmoney) {
		this.sentmoney = sentmoney;
	}

	public float getPackagefree() {
		return packagefree;
	}

	public void setPackagefree(float packagefree) {
		this.packagefree = packagefree;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCustName() {
		return CustName;
	}

	public void setCustName(String custName) {
		CustName = custName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEattime() {
		return eattime;
	}

	public void setEattime(String eattime) {
		this.eattime = eattime;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOorderid() {
		return Oorderid;
	}

	public void setOorderid(String oorderid) {
		Oorderid = oorderid;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayMode() {
		return PayMode;
	}

	public void setPayMode(String payMode) {
		PayMode = payMode;
	}

	public String getOrdersource() {
		return ordersource;
	}

	public void setOrdersource(String ordersource) {
		this.ordersource = ordersource;
	}

	public String getSendtype() {
		return sendtype;
	}

	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

	public String getUsersn() {
		return usersn;
	}

	public void setUsersn(String usersn) {
		this.usersn = usersn;
	}

	public String getPayPassword() {
		return PayPassword;
	}

	public void setPayPassword(String payPassword) {
		PayPassword = payPassword;
	}

	public String getReciveAddressID() {
		return reciveAddressID;
	}

	public void setReciveAddressID(String reciveAddressID) {
		this.reciveAddressID = reciveAddressID;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public String getSelfStateID() {
		return selfStateID;
	}

	public void setSelfStateID(String selfStateID) {
		this.selfStateID = selfStateID;
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
	
	

	public String beanToStringFix() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("CustomerName", this.userName);
			localJSONObject1.put("Receiver", this.CustName);
			localJSONObject1.put("Address", this.address);
			if (OrderType == 0) {
				localJSONObject1.put("Phone", this.phone);
			}else {
				localJSONObject1.put("Phone", this.CustName);//姓名
				localJSONObject1.put("deliverheaderid", roomID);//预订座位编号
				localJSONObject1.put("Sender", roomName);//预订座位名称
				localJSONObject1.put("ReveInt2", String.valueOf(OrderType));//0外卖，1堂食(订座）
				localJSONObject1.put("deliversiteid", String.valueOf(timeType));//0：:午餐，1：晚餐
				localJSONObject1.put("ReveVar1", String.valueOf(buyType));//0表示只订座不点菜，1表示订座并点菜
				localJSONObject1.put("ReveInt1", String.valueOf(this.people));//人数
				localJSONObject1.put("sendtime", this.eattime);//预订时间
				localJSONObject1.put("mealtime", this.eattime);//到店时间
			}			
			
			localJSONObject1.put("Mobilephone", this.phone);
			localJSONObject1.put("Remark", this.note);
			
			localJSONObject1.put("sendfree", this.sentmoney);
			
			localJSONObject1.put("state", "0");//用户的
			localJSONObject1.put("ulng", lng);//用户的
			localJSONObject1.put("ulat", lat);//用户的
			localJSONObject1.put("UserID", this.userid);
			localJSONObject1.put("bid", this.Cityid);
			//localJSONObject1.put("payType", this.payType);
			localJSONObject1.put("PayMode", this.PayMode);
			localJSONObject1.put("Ordersource", this.ordersource);
			localJSONObject1.put("sendtype", this.sendtype);//1.Delivery 2.pick up in the shop
			localJSONObject1.put("GainTime", this.eattime);
			
			localJSONObject1.put("isuercard", "0");////是否使用优惠券,0表示未用,1表示在用
			
			//localJSONObject1.put("addrID", reciveAddressID);//收获地址中的编号
			
			localJSONObject1.put("PayPassword", this.PayPassword);
			localJSONObject1.put("shopcardjson", "");////优惠券json(可以有多个.)；
			if (buyType == 0) {
				localJSONObject1.put("ShopList", String.format("[{\"Lng\":\"%s\",\"Lat\":\"%s\",\"TogoId\":\"%s\",\"ItemList\":[]}]", SLng, SLat, shopID));//默认获取第一个商家的编号，这样支持单个商家的订单
				
			}else{
				localJSONObject1.put("TogoId", list.get(0).getShopId());//默认获取第一个商家的编号，这样支持单个商家的订单
				JSONArray localJSONArray1 = new JSONArray();
				Iterator<ShopCartModel> localIterator = this.list.iterator();
				ShopCartModel model;
				while (true) {
					if (!localIterator.hasNext()) {
						localJSONObject1.put("ShopList", localJSONArray1);
						break;
					}
					model = localIterator.next();
					String tempString = model.beanToStringFix(OrderType);
					
					localJSONArray1.put(tempString);
				}
			}
			
			//Iterator<ShopCartModel> localIterator = this.list.iterator();
			//ShopCartModel model = localIterator.next();
			//String tempString = model.beanToStringFix(localJSONObject1);
			
			
			//localJSONObject1.put("ShopCardList", localJSONArray1);

			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}
	
	public String beanToStringFix(int shopID, HJInteger index) {
		if(index.value < 0){
			getFoodCountWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size()){
			JSONObject localJSONObject1 = new JSONObject();
			try {
				localJSONObject1.put("CustomerName", this.userName);
				localJSONObject1.put("Receiver", this.CustName);
				localJSONObject1.put("Address", this.address);
				if (OrderType == 0) {
					localJSONObject1.put("Phone", this.phone);
				}else {
					localJSONObject1.put("Phone", this.CustName);//姓名
					localJSONObject1.put("deliverheaderid", roomID);//预订座位编号
					localJSONObject1.put("Sender", roomName);//预订座位名称
					localJSONObject1.put("ReveInt2", String.valueOf(OrderType));//0外卖，1堂食(订座）
					localJSONObject1.put("deliversiteid", String.valueOf(timeType));//0：:午餐，1：晚餐
					localJSONObject1.put("ReveVar1", String.valueOf(buyType));//0表示只订座不点菜，1表示订座并点菜
					localJSONObject1.put("ReveInt1", String.valueOf(this.people));//人数
					localJSONObject1.put("sendtime", this.eattime);//预订时间
					localJSONObject1.put("mealtime", this.eattime);//到店时间
				}			
				
				localJSONObject1.put("Mobilephone", this.phone);
				localJSONObject1.put("Remark", this.note);
				
				localJSONObject1.put("sendfree", this.sentmoney);
				
				localJSONObject1.put("state", "0");//用户的
				localJSONObject1.put("ulng", lng);//用户的
				localJSONObject1.put("ulat", lat);//用户的
				localJSONObject1.put("UserID", this.userid);
				localJSONObject1.put("bid", this.Cityid);
				//localJSONObject1.put("payType", this.payType);
				localJSONObject1.put("PayMode", this.PayMode);
				localJSONObject1.put("Ordersource", this.ordersource);
				localJSONObject1.put("sendtype", this.sendtype);//1.Delivery 2.pick up in the shop
				localJSONObject1.put("GainTime", this.eattime);
				
				localJSONObject1.put("isuercard", "0");////是否使用优惠券,0表示未用,1表示在用
				
				//localJSONObject1.put("addrID", reciveAddressID);//收获地址中的编号
				
				localJSONObject1.put("PayPassword", this.PayPassword);
				localJSONObject1.put("shopcardjson", "");////优惠券json(可以有多个.)；
				if (buyType == 0) {
					localJSONObject1.put("ShopList", String.format("[{\"Lng\":\"%s\",\"Lat\":\"%s\",\"TogoId\":\"%s\",\"ItemList\":[]}]", SLng, SLat, shopID));//默认获取第一个商家的编号，这样支持单个商家的订单
					
				}else{
					localJSONObject1.put("TogoId", String.valueOf(shopID));//默认获取第一个商家的编号，这样支持单个商家的订单
					JSONArray localJSONArray1 = new JSONArray();
					ShopCartModel model = this.list.get(index.value);
					String tempString = model.beanToStringFix(OrderType);
					
					localJSONArray1.put(tempString);
					localJSONObject1.put("ShopList", localJSONArray1);
					
				}
				
				//Iterator<ShopCartModel> localIterator = this.list.iterator();
				//ShopCartModel model = localIterator.next();
				//String tempString = model.beanToStringFix(localJSONObject1);
				
				
				//localJSONObject1.put("ShopCardList", localJSONArray1);
	
				return localJSONObject1.toString();
			} catch (JSONException localJSONException) {
				return null;
			}
		}
		return null;
	}
	
	public int addFoodAttrWitchCheckAttr(ShopListItemModel mShop, FoodModel food,
			int attrIndex, FoodAttrModel attr, ArrayList<FoodAttrModel> listAttr, int Count) {
		int length = list.size();
		count++;
		
		ShopCartModel model;
		if (length > 0) {
			//totalprice += food.listSize.get(attrIndex).price;
			totalprice = add(totalprice, food.listSize.get(attrIndex).price);
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (mShop.getId() == model.getShopId()) {
					food.setTogoName(model.getShopname());
					
					foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
					sendFee = subtract(sendFee, model.getSendFee());
					
					//sendFee -= model.getSendFee();
					attrIndex = model.addFoodAttr(food, attrIndex, attr, listAttr, Count);
					foodAttrCount += model.getFoodAttrCount();////矫正属性数字
					model.CalculationSendFee();//重新计算配送费
					//sendFee += model.getSendFee();
					sendFee = add(sendFee, model.getSendFee());
					return attrIndex;

				}
			}
		}else{
			//totalprice += food.getPrice();
			totalprice = add(totalprice, food.getPrice());
		}
		model = new ShopCartModel();
		model.setShopid(mShop.getId());
		model.setShopname(mShop.getName());;
		model.setSentmoney(mShop.getSendMoney());
		model.setFullFreeMoney(mShop.getFullFreeMoney());
		model.setPackagefree(mShop.getPacketFee());
		model.setMinMoney(mShop.getStartSendMoney());
		model.setLat(mShop.getLatitude());
		model.setLng(mShop.getLongtude());
		model.setSendDistance(mShop.getSendDistance());
		food.setTogoName(model.getShopname());
		food.setTogoID(mShop.getId());
		model.addFoodAttr(food, attrIndex, attr, listAttr, Count);
		
		model.setMaxKM(mShop.getMaxKM());
		model.setMinKM(mShop.getMinKM());
		model.setSendFeeAffKM(mShop.getSendFeeAffKM());
		model.setSendFeeOfKM(mShop.getSendFeeOfKM());
		model.setStartSendFee(mShop.getStartSendFee());
		model.CalculationSendFee();
		//sendFee += model.getSendFee();
		sendFee = add(sendFee, model.getSendFee());
		//packagefree += model.getPackagefree();
		packagefree = add(packagefree, model.getPackagefree());
		list.add(model);
		foodAttrCount++;
		return 1;
	}
	
	public int addFoodAttr(ShopListItemModel mShop, FoodModel food,
			int attrIndex, int Count) {
		int length = list.size();
		count += Count;
		
		ShopCartModel model;
		if (length > 0) {
			totalprice += (food.listSize.get(attrIndex).price * Count);
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (mShop.getId() == model.getShopId()) {
					food.setTogoName(model.getShopname());
					foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
					//sendFee -= model.getSendFee();
					sendFee = subtract(sendFee, model.getSendFee());
					//packagefree -= model.getPackagefree();
					packagefree = subtract(packagefree, model.getPackagefree());
					attrIndex = model.addFoodAttr(food, attrIndex, null, null, Count);
					foodAttrCount += model.getFoodAttrCount();////矫正属性数字
					model.CalculationSendFee();
					//sendFee += model.getSendFee();
					sendFee = add(sendFee, model.getSendFee());
					//packagefree += model.getPackagefree();
					packagefree = add(packagefree, model.getPackagefree());
					return attrIndex;

				}
			}
		}else{
			//totalprice += (food.getPrice() * Count);
			totalprice = add(totalprice, multiply(food.getPrice(), Count));
		}
		model = new ShopCartModel();
		model.tagListModel = mShop.tagListModel;
		model.setShopid(mShop.getId());
		
		model.setShopname(mShop.getName());;
		model.setSentmoney(mShop.getSendMoney());
		model.setFullFreeMoney(mShop.getFullFreeMoney());
		model.setPackagefree(mShop.getPacketFee());
		model.setMinMoney(mShop.getStartSendMoney());
		model.setLat(mShop.getLatitude());
		model.setLng(mShop.getLongtude());
		model.setSendDistance(mShop.getSendDistance());
		food.setTogoName(model.getShopname());
		food.setTogoID(mShop.getId());
		model.addFoodAttr(food, attrIndex, null, null, Count);
		
		model.setMaxKM(mShop.getMaxKM());
		model.setMinKM(mShop.getMinKM());
		model.setSendFeeAffKM(mShop.getSendFeeAffKM());
		model.setSendFeeOfKM(mShop.getSendFeeOfKM());
		model.setStartSendFee(mShop.getStartSendFee());
		model.CalculationSendFee();
		//sendFee += model.getSendFee();
		sendFee = add(sendFee, model.getSendFee());
		//packagefree += model.getPackagefree();
		packagefree = add(packagefree, model.getPackagefree());
		list.add(model);
		foodAttrCount++;
		return 1;
	}

	public int delFoodAttr(ShopListItemModel mShop, FoodModel foodModel,
			int attrIndex) {
		// TODO Auto-generated method stub
		int length = list.size();
		ShopCartModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (mShop.getId() == model.getShopId()) {
					foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
					//totalprice -= model.getTotalPrice();
					totalprice = subtract(totalprice, model.getTotalPrice());
					count -= model.getCount();
					//sendFee -= model.getSendFee();
					sendFee = subtract(sendFee, model.getSendFee());
					//packagefree -= model.getPackagefree();
					packagefree = subtract(packagefree, model.getPackagefree());
					attrIndex = model.delFoodAttr(foodModel, attrIndex);
					foodAttrCount += model.getFoodAttrCount();//矫正属性数字
					//totalprice += model.getTotalPrice();
					totalprice = add(totalprice, model.getTotalPrice());
					//packagefree += model.getPackagefree();
					packagefree = add(packagefree, model.getPackagefree());
					count += model.getCount();
					model.CalculationSendFee();
					//sendFee += model.getSendFee();
					sendFee = add(sendFee, model.getSendFee());
					if (attrIndex == 0 && model.getCount() == 0) {
						//sendFee -= model.getSendFee();
						//packagefree -= model.getPackagefree();
						sendFee = subtract(sendFee, model.getSendFee());
						packagefree = subtract(packagefree, model.getPackagefree());
						list.remove(i);
					}
					return attrIndex;
				}
			}
		}
		return 0;
	}

	public float getFoodPrice(ShopListItemModel mShop, int foodId) {
		// TODO Auto-generated method stub
		int length = list.size();
		if (length > 0) {
			ShopCartModel model;
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (mShop.getId() == model.getShopId()) {
					return model.getFoodPrice(foodId);
				}
			}
		}
		return 0.0f;
	}
	
	public int getFoodCountWithShop(ShopListItemModel mShop) {
		// TODO Auto-generated method stub
		int length = list.size();
		if (length > 0) {
			ShopCartModel model;
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (mShop.getId() == model.getShopId()) {
					return model.list.size();
				}
			}
		}
		return 0;
	}
	//20160719新增
	public int getShopCartListSizeWithShopID(int shopID, HJInteger index) {
		// TODO Auto-generated method stub
		int length = list.size();
		if(index.value >= 0 && index.value < length){
			return list.get(index.value).getFoodAttrCount();
		}
		if (length > 0) {
			ShopCartModel model;
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (shopID == model.getShopId()) {
					index.value = i;
					return model.getFoodAttrCount();
				}
			}
		}
		return 0;
	}
	public int getFoodCountWithShopID(int shopID, HJInteger index) {//根据index返回食品
		// TODO Auto-generated method stub
		if(index.value < 0){
			getShopCartListSizeWithShopID(shopID, index);
		}
		int count = 0;
		if(index.value >= 0 && index.value < list.size())
		{
			if(shopID == list.get(index.value).getShopid()){
				count = list.get(index.value).getCount();
			}else{
				index.value = -1;
			}
			
		}else{
			index.value = -1;
		}
			
		return count;
	}
	public float getShopTotalPriceWithShopId(int shopID, HJInteger index) {//根据index返回食品
		// TODO Auto-generated method stub
		if(index.value < 0){
			getShopCartListSizeWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size())
			return list.get(index.value).getTotalPrice();
		return 0.0f;
	}
	
	public float getShopPacketFeeWithShopId(int shopID, HJInteger index) {//根据index返回食品
		// TODO Auto-generated method stub
		if(index.value < 0){
			getShopCartListSizeWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size())
			return list.get(index.value).getPackagefree();
		return 0.0f;
	}
	
	public float getSendFeeWithShopId(int shopID, HJInteger index) {//根据index返回食品
		// TODO Auto-generated method stub
		if(index.value < 0){
			getShopCartListSizeWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size())
			return list.get(index.value).getSendFee();
		return 0.0f;
	}
	
	public int removeShopAllFoodWithShopId(int shopID, HJInteger index) {
		// TODO Auto-generated method stub
		if(index.value < 0){
			getShopCartListSizeWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size()){
			ShopCartModel model = list.get(index.value);
			foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
			//totalprice -= model.getTotalPrice();
			totalprice = subtract(totalprice, model.getTotalPrice());
			count -= model.getCount();
			//sendFee -= model.getSendFee();
			sendFee = subtract(sendFee, model.getSendFee());
			//packagefree -= model.getPackagefree();
			packagefree = subtract(packagefree, model.getPackagefree());
			list.remove(index.value);
		}
		index.value = -1;
		return 0;
	}
	public FoodInOrderModel getFoodInOrderModel(HJInteger index) {//根据index返回食品
		// TODO Auto-generated method stub
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			count += list.get(i).getFoodAttrCount();
			if (index.value < count) {//如果在该所以呢里面
				return list.get(i).getFoodInOrderModel(index);
			}else{
				count = 0;
				index.value -= list.get(i).getFoodAttrCount();
			}
		}
		return null;
	}
	
	public FoodInOrderModel getFoodInOrderModelWithShopId(int shopID, HJInteger index, HJInteger postion) {//根据index返回食品
		// TODO Auto-generated method stub
		if(index.value < 0){
			getFoodCountWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size()){
			if(shopID == list.get(index.value).getShopid()){
				return list.get(index.value).getFoodInOrderModel(postion);
			}else{
				index.value = -1;
			}
			
		}else{
			index.value = -1;
		}
			
		return null;
	}
	
	public FoodInOrderModel getFoodInOrderModelWithShopId(int shopID, HJInteger index, int postion) {//根据index返回食品
		// TODO Auto-generated method stub
		if(index.value < 0){
			getFoodCountWithShopID(shopID, index);
		}
		if(index.value >= 0 && index.value < list.size()){
			if(shopID == list.get(index.value).getShopid()){
				return list.get(index.value).getFoodInOrderModel(postion);
			}else{
				index.value = -1;
			}
			
		}else{
			index.value = -1;
		}
			
		return null;
	}

	public int getFoodCountInAttr(ShopListItemModel mShop, int foodId,
			int cId) {
		// TODO Auto-generated method stub
		int length = list.size();
		if (length > 0) {
			ShopCartModel model;
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (mShop.getId() == model.getShopId()) {
					
					return model.getFoodCountInAttr(foodId, cId);
				}
			}
		}
		return 0;
	}
	
	public int getFoodCountInAttr(int shopID, int foodId,
			int cId) {
		// TODO Auto-generated method stub
		int length = list.size();
		if (length > 0) {
			ShopCartModel model;
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (shopID == model.getShopId()) {
					
					return model.getFoodCountInAttr(foodId, cId);
				}
			}
		}
		return 0;
	}
	
	

	

	public int addFoodAttr(FoodInOrderModel food, int pt) {
		// TODO Auto-generated method stub
		int length = list.size();

		ShopCartModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (food.getTogoId() == model.getShopId()) {
					foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
					//totalprice -= model.getTotalPrice();
					totalprice = subtract(totalprice, model.getTotalPrice());
					count -= model.getCount();
					//sendFee -= model.getSendFee();
					sendFee = subtract(sendFee, model.getSendFee());
					//packagefree -= model.getPackagefree();
					packagefree = subtract(packagefree, model.getPackagefree());
					pt = model.addFoodAttr(food.getId(), pt);
					foodAttrCount += model.getFoodAttrCount();//矫正属性数字
					model.CalculationSendFee();
					//sendFee += model.getSendFee();
					sendFee = add(sendFee, model.getSendFee());
					//totalprice += model.getTotalPrice();
					totalprice = add(totalprice, model.getTotalPrice());
					count += model.getCount();
					//packagefree += model.getPackagefree();
					packagefree = add(packagefree, model.getPackagefree());
					return pt;

				}
			}
		}

		return 0;
	}

	public int delFoodAttr(FoodInOrderModel food, int pt) {
		// TODO Auto-generated method stub
		int length = list.size();
		ShopCartModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (food.getTogoId() == model.getShopId()) {
					foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
					//totalprice -= model.getTotalPrice();
					totalprice = subtract(totalprice, model.getTotalPrice());
					count -= model.getCount();
					//sendFee -= model.getSendFee();
					sendFee = subtract(sendFee, model.getSendFee());
					//packagefree -= model.getPackagefree();
					packagefree = subtract(packagefree, model.getPackagefree());
					pt = model.delFoodAttr(food.getId(), pt);
					foodAttrCount += model.getFoodAttrCount();//矫正属性数字
					//totalprice += model.getTotalPrice();
					totalprice = add(totalprice, model.getTotalPrice());
					count += model.getCount();
					model.CalculationSendFee();
					//sendFee += model.getSendFee();
					sendFee = add(sendFee, model.getSendFee());
					//packagefree += model.getPackagefree();
					packagefree = add(packagefree, model.getPackagefree());
					if (pt == 0 && model.getCount() == 0) {
						//sendFee -= model.getSendFee();
						//packagefree -= model.getPackagefree();
						sendFee = subtract(sendFee, model.getSendFee());
						packagefree = subtract(packagefree, model.getPackagefree());
						list.remove(i);
					}
					return pt;
				}
			}
		}
		return 0;
	}

	public void removeFood(FoodInOrderModel food, int attrIndex) {
		// TODO Auto-generated method stub
		int length = list.size();
		ShopCartModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (food.getTogoId() == model.getShopId()) {
					length = model.getCount();
					if (attrIndex < length) {
						foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
						//totalprice -= model.getTotalPrice();
						totalprice = subtract(totalprice, model.getTotalPrice());
						count -= model.getCount();
						//sendFee -= model.getSendFee();
						sendFee = subtract(sendFee, model.getSendFee());
						//packagefree -= model.getPackagefree();
						packagefree = subtract(packagefree, model.getPackagefree());
						model.removeFood(food.getId(), attrIndex);
						foodAttrCount += model.getFoodAttrCount();//矫正属性数字
						//totalprice += model.getTotalPrice();
						totalprice = add(totalprice, model.getTotalPrice());
						model.CalculationSendFee();
						//sendFee += model.getSendFee();
						sendFee = add(sendFee, model.getSendFee());
						count += model.getCount();
						//packagefree += model.getPackagefree();
						packagefree = add(packagefree, model.getPackagefree());
						if (model.getCount() == 0) {
							//sendFee -= model.getSendFee();
							//packagefree -= model.getPackagefree();
							sendFee = subtract(sendFee, model.getSendFee());
							packagefree = subtract(packagefree, model.getPackagefree());
							list.remove(i);
						}
						return;
						
					}
					
				}
			}
		}
	}
	public void removeFood(int shopID, FoodModel food, int attrIndex) {
		// TODO Auto-generated method stub
		int length = list.size();
		ShopCartModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (shopID == model.getShopId()) {
					length = model.getCount();
					if (attrIndex < length) {
						foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
						//totalprice -= model.getTotalPrice();
						totalprice = subtract(totalprice, model.getTotalPrice());
						count -= model.getCount();
						//sendFee -= model.getSendFee();
						sendFee = subtract(sendFee, model.getSendFee());
						//packagefree -= model.getPackagefree();
						packagefree = subtract(packagefree, model.getPackagefree());
						model.removeFood(food.getId(), attrIndex);
						foodAttrCount += model.getFoodAttrCount();//矫正属性数字
						//totalprice += model.getTotalPrice();
						totalprice = add(totalprice, model.getTotalPrice());
						model.CalculationSendFee();
						//sendFee += model.getSendFee();
						sendFee = add(sendFee, model.getSendFee());
						count += model.getCount();
						//packagefree += model.getPackagefree();
						packagefree = add(packagefree, model.getPackagefree());
						if (model.getCount() == 0) {
							//sendFee -= model.getSendFee();
							//packagefree -= model.getPackagefree();
							sendFee = subtract(sendFee, model.getSendFee());
							packagefree = subtract(packagefree, model.getPackagefree());
							list.remove(i);
						}
						return;
						
					}
					
				}
			}
		}
	}
	
	public void removeFood(int shopID, FoodModel food) {
		// TODO Auto-generated method stub
		int length = list.size();
		ShopCartModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (shopID == model.getShopId()) {
						foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
						//totalprice -= model.getTotalPrice();
						totalprice = subtract(totalprice, model.getTotalPrice());
						count -= model.getCount();
						//sendFee -= model.getSendFee();
						sendFee = subtract(sendFee, model.getSendFee());
						//packagefree -= model.getPackagefree();
						packagefree = subtract(packagefree, model.getPackagefree());
						model.removeFood(food.getId());
						foodAttrCount += model.getFoodAttrCount();//矫正属性数字
						//totalprice += model.getTotalPrice();
						totalprice = add(totalprice, model.getTotalPrice());
						model.CalculationSendFee();
						//sendFee += model.getSendFee();
						sendFee = add(sendFee, model.getSendFee());
						count += model.getCount();
						//packagefree += model.getPackagefree();
						packagefree = add(packagefree, model.getPackagefree());
						if (model.getCount() == 0) {
							//sendFee -= model.getSendFee();
							//packagefree -= model.getPackagefree();
							sendFee = subtract(sendFee, model.getSendFee());
							packagefree = subtract(packagefree, model.getPackagefree());
							list.remove(i);
						}
						return;
					
					
				}
			}
		}
	}
	public int getOneFoodCount(ShopListItemModel mShop, int foodId) {
		// TODO Auto-generated method stub
		int length = list.size();
		if (length > 0) {
			ShopCartModel model;
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (mShop.getId() == model.getShopId()) {
					
					return model.getOneFoodCount(foodId);
				}
			}
		}
		return 0;
	}
	public int setFoodAttr(FoodInOrderModel food, int attrIndex, int ncount) {
		// TODO Auto-generated method stub
		int length = list.size();
		ShopCartModel model;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				model = list.get(i);
				if (food.getTogoId() == model.getShopId()) {
					length = model.getCount();
					if (attrIndex < length) {
						foodAttrCount -= model.getFoodAttrCount();//矫正属性数字
						//totalprice -= model.getTotalPrice();
						totalprice = subtract(totalprice, model.getTotalPrice());
						count -= model.getCount();
						//sendFee -= model.getSendFee();
						sendFee = subtract(sendFee, model.getSendFee());
						packagefree = subtract(packagefree, model.getPackagefree());
						ncount = model.setFoodAttr(food.getId(), attrIndex, ncount);
						foodAttrCount += model.getFoodAttrCount();//矫正属性数字
						//totalprice += model.getTotalPrice();
						totalprice = add(totalprice, model.getTotalPrice());
						count += model.getCount();
						model.CalculationSendFee();
						//sendFee += model.getSendFee();
						sendFee = add(sendFee, model.getSendFee());
						packagefree = add(packagefree, model.getPackagefree());
						if (ncount == 0 && model.getCount() == 0) {
							//sendFee -= model.getSendFee();
							//packagefree -= model.getPackagefree();
							sendFee = subtract(sendFee, model.getSendFee());
							packagefree = subtract(packagefree, model.getPackagefree());
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
	public void setOrderType(int i) {
		// TODO Auto-generated method stub
		OrderType  = i;
	}
	
	public void setRoomID(String id) {
		// TODO Auto-generated method stub
		roomID  = id;
	}
	
	public void setRoomName(String name) {
		// TODO Auto-generated method stub
		roomName  = name;
	}
	public void setRoomType(int Type) {
		// TODO Auto-generated method stub
		roomType  = Type;
	}
	public void setTimeType(int eatType) {
		// TODO Auto-generated method stub
		timeType = eatType;
	}
	public void setBuyType(int buy) {
		// TODO Auto-generated method stub
		buyType = buy;
	}
	public void setShopID(String valueOf) {
		// TODO Auto-generated method stub
		shopID  = valueOf;
	}
	public void setSLat(String latitude) {
		// TODO Auto-generated method stub
		SLat = latitude;
	}
	public void setSLng(String longtude) {
		// TODO Auto-generated method stub
		SLng = longtude;
	}
	

	

}
