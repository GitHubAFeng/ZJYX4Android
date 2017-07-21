package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihangjing.common.OtherManager;

import android.R.integer;

//{"FoodID":"8521","Name":"飘香猪扒饭","Price":"15.0","Discount":"10.0","PackageFree":"0.0"}
public class SeckillModel {// 买搭增
	private int ActivitiesID = 0;// 活动编号
	private int id = 0; // 编号
	private String name = ""; // 名称
	private float price = 0.0F; // 价格
	private float oPrice = 0.0f;//原价
	private float discount = 0.0F; // 折扣
	private float packagefree = 0.0F; // 打包费
	private String Disc = "";// 简介描述
	private String notice = "";// 提示
	private int Stock = 0; // 库存
	private boolean selected = false;// 是否被选中 列表中选中未选中状态的标识
	private int ordernum = 0; // 购买的数量 在订购时
	private int foodStock = -1;
	public List<SeckillAttrModel> listSpec;// 规格
	private String ImageNetPath;// 图片网络路径
	private String ImageLocalPath;// 图片本地路径
	private String publicPoint;// 公益积分
	private int togoID;
	private String togoName;// 商家名称
	// public boolean isDowload = ;//是否需要下载图片
	private String lat;
	private String lng;

	// 下面计算配送费相关
	private float sendDistance;// 配送距离
	private float startSendFee = 0.0f;// 起步价
	private float SendFeeOfKM = 0.0f;// 每公里加价
	private float minKM = 0.0f;// 超过多少公里开始加价
	private float maxKM = 0.0f;// 超过多少公里采用第二价格
	private float SendFeeAffKM = 0.0f;// 超过多少公里第二价格
	private float distance;// 距离最后地址确定计算
	private float minMoney;// 起送金额
	private float freeSendMoney;// 满多少免配送费
	private float sendMoney;//配送费
	private int shopStatus = 0;//shopstatus
	private String startTime;
	private String endTime;

	public float CalculationSendFee() {// 计算配送费
		float sendFee = startSendFee;
		if (maxKM > minKM && distance > maxKM) {// maxKM必须大于minKM 否则没意义
			sendFee += ((maxKM - minKM) * SendFeeOfKM + (distance - maxKM)
					* SendFeeAffKM);
		} else if (minKM > 0.0 && distance > minKM) {// 无最大公里限制
			sendFee += (distance - minKM) * SendFeeOfKM;
		} else {// 无起步价，就是每公里多少钱
			sendFee += distance * SendFeeOfKM;
		}
		return sendFee;
	}
	
	public float getSendMoney() {
		return sendMoney;
	}

	public void setSendMoney(float sendMoney) {
		this.sendMoney = sendMoney;
	}

	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public float getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(float minMoney) {
		this.minMoney = minMoney;
	}

	public float getFreeSendMoney() {
		return freeSendMoney;
	}

	public void setFreeSendMoney(float freeSendMoney) {
		this.freeSendMoney = freeSendMoney;
	}

	public int getActivitiesID() {
		return ActivitiesID;
	}

	public void setActivitiesID(int activitiesID) {
		ActivitiesID = activitiesID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPackagefree() {
		return packagefree;
	}

	public void setPackagefree(float packagefree) {
		this.packagefree = packagefree;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public List<SeckillAttrModel> getListSpec() {
		return listSpec;
	}

	public void setListSpec(List<SeckillAttrModel> listSpec) {
		this.listSpec = listSpec;
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

	public float getSendDistance() {
		return sendDistance;
	}

	public void setSendDistance(float sendDistance) {
		this.sendDistance = sendDistance;
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

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public SeckillModel() {

	}

	public String getTogoName() {
		return togoName;
	}

	public void setTogoName(String togoName) {
		this.togoName = togoName;
	}

	public String getPublicPoint() {
		return publicPoint;
	}

	public void setPublicPoint(String publicPoint) {
		this.publicPoint = publicPoint;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getDisc() {
		return Disc;
	}

	public void setDisc(String disc) {
		Disc = disc;
	}

	public String getImageNetPath() {
		return ImageNetPath;
	}

	public void setImageNetPath(String imageNetPath) {
		ImageNetPath = imageNetPath;
	}

	public String getImageLocalPath() {
		return ImageLocalPath;
	}

	public void setImageLocalPath(String imageLocalPath) {
		ImageLocalPath = imageLocalPath;
	}

	public int getTogoID() {
		return togoID;
	}

	public void setTogoID(int togoID) {
		this.togoID = togoID;
	}
	
	public float getoPrice() {
		return oPrice;
	}

	public void setoPrice(float oPrice) {
		this.oPrice = oPrice;
	}

	public int getFoodStock() {
		return foodStock;
	}

	public void setFoodStock(int foodStock) {
		this.foodStock = foodStock;
	}

	

	public int getFoodId() {
		return this.id;
	}

	public String getFoodName() {
		return this.name;
	}

	public float getPrice() {
		return this.price;
	}

	public float getDiscount() {
		return this.discount;
	}

	public float getPackageFree() {
		return this.packagefree;
	}

	

	public boolean isSelected() {
		return this.selected;
	}

	public int getOrderNum() {
		return this.ordernum;
	}

	public void setSelected(boolean paramBoolean) {
		this.selected = paramBoolean;
	}

	public void setFoodId(int paramString) {
		this.id = paramString;
	}

	public void setFoodName(String paramString) {
		this.name = paramString;
	}

	public void setPrice(float paramString) {
		this.price = paramString;
	}

	
	public void setOrderNum(int paramString) {
		this.ordernum = paramString;
	}

	public void setDiscount(float paramString) {
		this.discount = paramString;
	}

	public void setPackageFree(float paramString) {
		this.packagefree = paramString;
	}

	
	public int getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(int shopStatus) {
		this.shopStatus = shopStatus;
	}
	
	public int getStock() {
		return Stock;
	}

	public void setStock(int stock) {
		Stock = stock;
	}

	// Json转换成model
	// sr = 0,网络获取 sr=1本地缓存
	public void JsonToBean(JSONObject localJSONObject, int sr) {

		try {
			ActivitiesID = localJSONObject.getInt("fID");
			setFoodId(localJSONObject.getInt("Foodid"));
			setFoodName(localJSONObject.getString("foodname"));
			Disc = localJSONObject.getString("timestr");
			price = (float)localJSONObject.getDouble("newprice");//商品现价
			Stock = localJSONObject.getInt("cnum");
			setOrderNum(0);
			ImageLocalPath = OtherManager.getFoodImageLocalPath(0, id);
			oPrice = (float)localJSONObject.getDouble("oldprice");
			//notice = localJSONObject.getString("note");

			setPackageFree(0.0f);
			ImageNetPath = localJSONObject.getString("pic");

			sendDistance = (float) localJSONObject.getDouble("limitdistance");
			SendFeeAffKM = (float) localJSONObject.getDouble("everyfee");
			startSendFee = (float) localJSONObject.getDouble("basemoney");
			minKM = (float) localJSONObject.getDouble("basedistance");
			minMoney = (float) localJSONObject.getDouble("limitfee");
			freeSendMoney = (float) localJSONObject.getDouble("freefee");
			distance = (float) localJSONObject.getDouble("distance");
			togoID = localJSONObject.getInt("shopid");
			togoName = localJSONObject.getString("togoname");
			
			
			sendMoney = CalculationSendFee();

			lat = localJSONObject.getString("shoplat");
			lng = localJSONObject.getString("shoplng");

			listSpec = new ArrayList<SeckillAttrModel>();
			SeckillAttrModel mode = new SeckillAttrModel();
			mode.cId = 0;
			mode.name = getFoodName();//赠品名称
			mode.price = this.price;//赠品价格
			listSpec.add(mode);
			
			startTime = String.format("%s %s",  localJSONObject.getString("stardate"),
					 localJSONObject.getString("timestart"));
			
			endTime = String.format("%s %s",  localJSONObject.getString("enddate"),
					 localJSONObject.getString("timeend"));
			shopStatus = localJSONObject.getInt("shopstatus");

			

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}
}
