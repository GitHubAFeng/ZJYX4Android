package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//订单列表中的订单信息,只有少数的几个属性
public class SimpleOrderModel {
	//{"OrderID":"1206262045220755","TogoName":"新鼎红培",
	//"orderTime":"2012-06-26 20:45:26","TotalPrice":"34.5",
	//"State":"3","sendmoney":"8","Packagefree":"0.0"}
	private String orderid;
	private String shopname;
	private String addtime;    // 下单时间
	private String totalmoney; // 总价（注意:含配送费）
	private int state;      // 订单状态
	private String sendmoney;  // 配送费
	private String Packagefree = "0.0";// 打包费
	private String orderstr;//订单状态（中文）
	private int payMode;
	private int payStatus;
	private int SortType;
	private int sendstate;
	private int IsShopSet;
	public ArrayList<FoodInOrderModelForGet> list;
	private String togoid;
	private String togoICON;
	private int foodCount;
	private int shopCancel;
	
	public int getSendstate() {
		return sendstate;
	}

	public void setSendstate(int sendstate) {
		this.sendstate = sendstate;
	}

	public int getIsShopSet() {
		return IsShopSet;
	}

	public void setIsShopSet(int isShopSet) {
		IsShopSet = isShopSet;
	}

	public ArrayList<FoodInOrderModelForGet> getList() {
		return list;
	}

	public void setList(ArrayList<FoodInOrderModelForGet> list) {
		this.list = list;
	}

	public String getTogoid() {
		return togoid;
	}

	public void setTogoid(String togoid) {
		this.togoid = togoid;
	}

	public String getTogoICON() {
		return togoICON;
	}

	public void setTogoICON(String togoICON) {
		this.togoICON = togoICON;
	}

	public int getFoodCount() {
		return foodCount;
	}

	public void setFoodCount(int foodCount) {
		this.foodCount = foodCount;
	}

	// Json转换成model
	public SimpleOrderModel(JSONObject json, int type) throws JSONException {
		try {
			if(this.list == null){
				this.list = new ArrayList<FoodInOrderModelForGet>();
			}else{
				this.list.clear();
			}
			JSONObject localJSONObject = json;
			if (type == 1) {
				togoid = localJSONObject.getString("TogoId");
				setOrderId(localJSONObject.getString("OrderID"));
				setShopName(localJSONObject.getString("TogoName"));
				setAddtime(localJSONObject.getString("orderTime"));
				setTotalmoney(localJSONObject.getString("TotalPrice"));
				setState(localJSONObject.getInt("State"));
				togoICON = localJSONObject.getString("eattype");
				sendmoney = localJSONObject.getString("sendmoney");
				SortType = 0;//localJSONObject.getInt("OrderType");//0外卖 1预订
				sendstate = localJSONObject.getInt("sendstate");//配送状态
				IsShopSet = localJSONObject.getInt("IsShopSet");//商家状态
				shopCancel = localJSONObject.getInt("shopCancel");//申请取消订单时商家0 未处理 1同意 2拒绝
				JSONArray localJSONArray = localJSONObject.getJSONArray("list");
				foodCount = 0;
				int k = 0;
				int m = localJSONArray.length();
				// 循环读取所有的商家信息
				while (true) {
					
					if (k >= m) {
						break;
					}
					FoodInOrderModelForGet localShopBean1 = new FoodInOrderModelForGet();
					//String str3 = (String) localJSONArray.get(k);
					
					this.list.add(localShopBean1.JsonToBeanInOrderList(localJSONArray.getJSONObject(k)));
					foodCount += localShopBean1.getCount();
					k += 1;
				}
			}else{
				setOrderId(localJSONObject.getString("OrderID"));
				setShopName(localJSONObject.getString("TogoName"));//取件地址
				setAddtime(localJSONObject.getString("orderTime"));//订单时间
				setTotalmoney(localJSONObject.getString("TotalPrice"));//配送费
				setState(localJSONObject.getInt("State"));//状态
				//sendmoney = localJSONObject.getString("sendmoney");
			}
			
			//this.payMode = localJSONObject.getInt("PayMode");
			//this.payStatus = localJSONObject.getInt("paystate");
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}
	
	public int getShopCancel() {
		return shopCancel;
	}

	public void setShopCancel(int shopCancel) {
		this.shopCancel = shopCancel;
	}

	public int getSortType() {
		return SortType;
	}

	public void setSortType(int sortType) {
		SortType = sortType;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getSendmoney() {
		return sendmoney;
	}

	public void setSendmoney(String sendmoney) {
		this.sendmoney = sendmoney;
	}

	public int getPayMode() {
		return payMode;
	}

	public void setPayMode(int payMode) {
		this.payMode = payMode;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public String getTotalmoney() {
		return totalmoney;
	}

	public SimpleOrderModel() {

	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("OrderID", this.orderid);
			localJSONObject1.put("TogoName", this.shopname);
			localJSONObject1.put("orderTime", this.addtime);
			localJSONObject1.put("TotalPrice", this.totalmoney);
			localJSONObject1.put("state", this.state);
			localJSONObject1.put("orderstr", this.orderstr);
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String getOrderId() {
		return this.orderid;
	}
	
	public String getShopName() {
		return this.shopname;
	}

	public String getAddtime() {
		return this.addtime;
	}

	public String getTotalMoney() {
		return this.totalmoney;
	}

	public int getState() {
		return this.state;
	}

	public void setOrderId(String paramString) {
		this.orderid = paramString;
	}
	
	public void setShopName(String paramString) {
		this.shopname = paramString;
	}

	public void setAddtime(String paramString) {
		this.addtime = paramString;
	}

	public void setTotalmoney(String paramString) {
		this.totalmoney = paramString;
	}

	public void setState(int paramString) {
		this.state = paramString;
	}
	
	public String getSentmoney() {
		return this.sendmoney;
	}

	public void setSentmoney(String paramString) {
		this.sendmoney = paramString;
	}
	
	public String getPackagefree() {
		return this.Packagefree;
	}

	public void setPackagefree(String paramString) {
		this.Packagefree = paramString;
	}
	
	public SimpleOrderModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			setOrderId(localJSONObject.getString("OrderID"));
			setShopName(localJSONObject.getString("TogoName"));
			setAddtime(localJSONObject.getString("orderTime"));
			setTotalmoney(localJSONObject.getString("TotalPrice"));
			setState(localJSONObject.getInt("State"));
			setSentmoney(localJSONObject.getString("sendmoney"));
			setPackagefree(localJSONObject.getString("Packagefree"));
			setOrderstr(localJSONObject.getString("orderstr"));
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}

	public String getOrderstr() {
		return orderstr;
	}

	public void setOrderstr(String orderstr) {
		this.orderstr = orderstr;
	}
}
