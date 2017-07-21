package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import com.ihangjing.common.HJInteger;

import android.R.bool;
import android.net.wifi.WifiConfiguration.Status;

//商家列表中商家的信息
public class ShopListItemModel {
	// {"DataID":"1373", "TogoName":"碗碗香面庄","sortname":"商务简餐",
	// "address":"石鼓路大榕树旁","Time1Start":"09:00","Time1End":"21:00",
	// "Time2Start":"09:00","Time2End":"21:00","distance":"0","Star":"1",
	// "sendmoney":"0"}
	private int id;// 编号
	private String icon = "";// logo
	private String name = "";// 名称
	private String tel = "";// 电话
	private String adress = "";// 商家地址
	private String dis = "";// 距离
	private String ordertime = "";// 订购时间（营业时间）
	private String latitude;// 地图坐标
	private String longtude;// 地图坐标

	private String TimeStart1 = "";
	private String TimeEnd1 = "";
	private String TimeStart2 = "";
	private String TimeEnd2 = "";
	private float SendDistance;// 配送距离
	private int isBrandString = 0;// 是否在线点餐
	private int Status = 0;// 1表示营业中，0表示可以预定 -1表示休息中不可预定
	private int sendTime = 0;
	private int Rblstart;
	public boolean isUpdate = false;// 更新商家信息
	public boolean isUpdateFoodList = false;// 是否更新商品列表
	private float FlavorGrade;
	private float ServiceGrade;
	private float SpeedGrade;
	public TagListModel tagListModel;

	private int Grade = 0;
	private float SendMoney = 0.0f;// 派送费
	private float PacketFee = 0.0f;// 打包费
	private float startSendMoney = 0.0f;// 起送金额
	private float fullFreeMoney = 0.0f;// 满多少免配送费 0.0表示不眠

	// 下面计算配送费相关
	private float startSendFee = 0.0f;// 起步价
	private float SendFeeOfKM = 0.0f;// 每公里加价
	private float minKM = 0.0f;// 超过多少公里开始加价
	private float maxKM = 0.0f;// 超过多少公里采用第二价格
	private float SendFeeAffKM = 0.0f;// 超过多少公里第二价格
	private float shopDiscount;
	private String desc;
	private float minmoney;
	private int PType;
	private int shopType;
	private String sales;
	public HJInteger shopIndexInShopCart;//记录在购物车中的索引
	

	public float getPacketFee() {
		return PacketFee;
	}

	public void setPacketFee(float packetFee) {
		PacketFee = packetFee;
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

	public float getSendDistance() {
		return SendDistance;
	}

	public void setSendDistance(float sendDistance) {
		SendDistance = sendDistance;
	}

	public float getStartSendMoney() {
		return startSendMoney;
	}

	public void setStartSendMoney(float startSendMoney) {
		this.startSendMoney = startSendMoney;
	}

	public float getFullFreeMoney() {
		return fullFreeMoney;
	}

	public void setFullFreeMoney(float fullFreeMoney) {
		this.fullFreeMoney = fullFreeMoney;
	}

	

	// private String togoID;

	public int getStatus() {
		return Status;
	}

	public int getRblstart() {
		return Rblstart;
	}

	public void setRblstart(int rblstart) {
		Rblstart = rblstart;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public ShopListItemModel() {

	}

	public int getSendTime() {
		return sendTime;
	}

	public void setSendTime(int sendTime) {
		this.sendTime = sendTime;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public int getIsBrandString() {
		return isBrandString;
	}

	public void setIsBrandString(int isBrandString) {
		this.isBrandString = isBrandString;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public boolean isUpdateFoodList() {
		return isUpdateFoodList;
	}

	public void setUpdateFoodList(boolean isUpdateFoodList) {
		this.isUpdateFoodList = isUpdateFoodList;
	}

	public float getFlavorGrade() {
		return FlavorGrade;
	}

	public void setFlavorGrade(float flavorGrade) {
		FlavorGrade = flavorGrade;
	}

	public float getServiceGrade() {
		return ServiceGrade;
	}

	public void setServiceGrade(float serviceGrade) {
		ServiceGrade = serviceGrade;
	}

	public float getSpeedGrade() {
		return SpeedGrade;
	}

	public void setSpeedGrade(float speedGrade) {
		SpeedGrade = speedGrade;
	}

	public float getShopDiscount() {
		return shopDiscount;
	}

	public void setShopDiscount(float shopDiscount) {
		this.shopDiscount = shopDiscount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getMinmoney() {
		return minmoney;
	}

	public void setMinmoney(float minmoney) {
		this.minmoney = minmoney;
	}

	public int getPType() {
		return PType;
	}

	public void setPType(int pType) {
		PType = pType;
	}

	public TagListModel getTagListModel() {
		return tagListModel;
	}

	public void setTagListModel(TagListModel tagListModel) {
		this.tagListModel = tagListModel;
	}

	public int getShopType() {
		return shopType;
	}

	public void setShopType(int shopType) {
		this.shopType = shopType;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	// Json转换成model
	public ShopListItemModel(JSONObject json) throws JSONException {
		try {
			shopIndexInShopCart = new HJInteger();
			shopIndexInShopCart.value = -1;
			tagListModel = new TagListModel();
			JSONObject localJSONObject = json;
			setId(localJSONObject.getInt("DataID"));
			setName(localJSONObject.getString("TogoName"));
			setAdress(localJSONObject.getString("address"));
			setDis(localJSONObject.getString("distance"));
			shopDiscount = ((float)localJSONObject.getInt("shopdiscount"))/10.0f;//商家折扣
			setTimeStart1(localJSONObject.getString("Time1Start"));
			setTimeStart2(localJSONObject.getString("Time2Start"));
			setTimeEnd1(localJSONObject.getString("Time1End"));
			setTimeEnd2(localJSONObject.getString("Time2End"));
			shopType = 1;//localJSONObject.getInt("Types");//1表示外卖商家，2表示订座商家，3表示都支持
			PType = 1;//localJSONObject.getInt("PType");//商家类型 位标识 1.外卖;2.商家订座;4.团购预定
			// togoID = localJSONObject.getString("togoid");
			// Rblstart = localJSONObject.getInt("rblstart");
			//FlavorGrade = (float)localJSONObject.getDouble("FlavorGrade");//喜好评分
			//ServiceGrade = (float)localJSONObject.getDouble("ServiceGrade");//服务评分
			//SpeedGrade = (float)localJSONObject.getDouble("SpeedGrade");//速度评分
			Grade = localJSONObject.getInt("Grade");//星级
			desc = localJSONObject.getString("desc");//推荐理由
			sales = localJSONObject.getString("sales");//销量
			Status = localJSONObject.getInt("status");
			// sendTime = localJSONObject.getString("SendTime");
			setLatitude(localJSONObject.getString("lat"));// 30
			setLongtude(localJSONObject.getString("lng"));// 120

			setSendMoney((float) localJSONObject.getDouble("sendmoney"));//配送费
			minmoney = (float) localJSONObject.getDouble("minmoney");//起送金额
			// setGrade(localJSONObject.getInt("Star"));

			setIcon(localJSONObject.getString("icon"));
			tagListModel.JsonToBean(localJSONObject);

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}

	// 生成json
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("DataID", getId());
			localJSONObject1.put("TogoName", getName());
			localJSONObject1.put("address", getAdress());
			localJSONObject1.put("distance", getDis());

			localJSONObject1.put("TimeStart1", getTimeStart1());
			localJSONObject1.put("TimeStart2", getTimeStart2());
			localJSONObject1.put("TimeEnd1", getTimeEnd1());
			localJSONObject1.put("TimeEnd2", getTimeEnd2());

			localJSONObject1.put("Star", getGrade());
			localJSONObject1.put("sendmoney", getSendMoney());
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public int getId() {
		return this.id;
	}

	public String getIcon() {
		return this.icon;
	}

	public String getName() {
		return this.name;
	}

	public String getTel() {
		return this.tel;
	}

	public String getAdress() {
		return this.adress;
	}

	public String getDis() {
		return this.dis;
	}

	public String getOrderTime() {
		return this.ordertime;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public String getLongtude() {
		return this.longtude;
	}

	public float getSendMoney() {
		return this.SendMoney;
	}

	public void setId(int paramString) {
		this.id = paramString;
	}

	public void setIcon(String paramString) {
		this.icon = paramString;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setTel(String paramString) {
		this.tel = paramString;
	}

	public void setAdress(String paramString) {
		this.adress = paramString;
	}

	public void setDis(String paramString) {
		this.dis = paramString;
	}

	public void setOrderTime(String paramString) {
		this.ordertime = paramString;
	}

	public void setLatitude(String paramDouble) {
		this.latitude = paramDouble;
	}

	public void setLongtude(String paramDouble) {
		this.longtude = paramDouble;
	}

	public void setTimeStart1(String paramString) {
		this.TimeStart1 = paramString;
	}

	public String getTimeStart1() {
		return this.TimeStart1;
	}

	public void setTimeEnd1(String paramString) {
		this.TimeEnd1 = paramString;
	}

	public String getTimeEnd1() {
		return this.TimeEnd1;
	}

	public void setTimeStart2(String paramString) {
		this.TimeStart2 = paramString;
	}

	public String getTimeStart2() {
		return this.TimeStart2;
	}

	public void setTimeEnd2(String paramString) {
		this.TimeEnd2 = paramString;
	}

	public String getTimeEnd2() {
		return this.TimeEnd2;
	}

	public void setGrade(int paramString) {
		this.Grade = paramString;
	}

	public int getGrade() {
		return this.Grade;
	}

	public void setSendMoney(float paramString) {
		this.SendMoney = paramString;
	}

	public int getIsBrand() {
		return this.isBrandString;
	}

	public void SetIsBrand(int paramString) {
		this.isBrandString = paramString;
	}

	// Json转换成model
	public ShopListItemModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}

		try {
			JSONObject localJSONObject = new JSONObject(paramString);

			setId(localJSONObject.getInt("DataID"));
			setName(localJSONObject.getString("TogoName"));
			setAdress(localJSONObject.getString("address"));
			setDis(localJSONObject.getString("distance"));
			setTimeStart1(localJSONObject.getString("Time1Start"));
			setTimeStart2(localJSONObject.getString("Time2Start"));
			setTimeEnd1(localJSONObject.getString("Time1End"));
			setTimeEnd2(localJSONObject.getString("Time2End"));
			setGrade(localJSONObject.getInt("Star"));
			setSendMoney((float) localJSONObject.getDouble("sendmoney"));
			SetIsBrand(localJSONObject.getInt("Grade"));
			setLatitude(localJSONObject.getString("lat"));// 30
			setLongtude(localJSONObject.getString("lng"));// 120

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
