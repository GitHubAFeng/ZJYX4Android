package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

/*
{"DataID":"119","TogoName":"�͵���","address":"�촺·118�żε¹㳡һ¥",
"sendmoney":"��8","lat":"30.266256","lng":"120.180555",
"Time1Start":"10:00","Time1End":"20:00","Time2Start":"10:00","Time2End":"20:00",
"Star":"1","desc":"���ﴨ�����͵�Ʒ�ƣ����ﴨ�Ĳ�Ʒ��ɫ�����������ձ������Ļ�����Ļ�����,�Ƴ�һϵ�к����й�����ʳϰ�ߵ��淹��Сʳ�;�ˮ��Ʒ��ζ���������������ۣ���������ŵľͲͻ�����Ϊ��˵����ֵ��һ�ԡ�",
"distance":"5.7����",}
 */
//�̼���ϸ��Ϣmodel
public class FsgShopDetailModel extends BaseBean {
	private String DataID = ""; 
	private String TogoName = "";
	private String address = "";
	private float sendmoney = 0.0f;//���ͷ�
	private float minMoney = 0.0f;//������ͽ��
	private float freeSendMoney = 0.0f;//������Ǯ�����

	//����������ͷ����
	private float startSendFee = 0.0f;//�𲽼�
	private float SendFeeOfKM = 0.0f;//ÿ����Ӽ�
	private float minKM = 0.0f;//�������ٹ��￪ʼ�Ӽ�
	private float maxKM = 0.0f;//�������ٹ�����õڶ��۸�
	private float SendFeeAffKM = 0.0f;//�������ٹ���ڶ��۸�
	public TagListModel tagListModel;
	private String lat = "";
	private String lng = "";
	
	private String TimeStart1 = "";
	private String TimeEnd1 = "";
	private String TimeStart2 = "";
	private String TimeEnd2 = "";
	private int Star = 0;
	private String desc = "";//�̼Ҽ��
	private String ico = "";//�̼�ͼƬ
	private float distance = 0.0f;//�̼Ҿ���
	private float SendDistance = 0.0f;//���;���
	private int status = 0;//-1��Ϣ�� 0��Ϣ��Ԥ�� 1Ӫҵ
	
	private String tel = "";
	private String sendtype = "";
	private int sendTime;
	private String sendTimeDes;//�ʹ�ʱ��˵��
	private String Grade;
	private String News;//�̼ҹ���
	private float FlavorGrade;
	private float ServiceGrade;
	private float SpeedGrade;
	private float avGrade;
	private String features;
	private int togotype = 1;
	private int isFavor;//�Ƿ��ղ� 0δ�ղ� 1�ղ�
	private int goodCount;
	private int okCount;
	private int badCount;
	private float shopdiscount;
	private float Packagefree = 0.0f;//�����
	private String shopInfo;
	private int shopType;
	private String Ship1Start;
	private String Ship1End;
	private String Ship2Start;
	private String Ship2End;
	
	
	
	public TagListModel getTagListModel() {
		return tagListModel;
	}

	public void setTagListModel(TagListModel tagListModel) {
		this.tagListModel = tagListModel;
	}

	public float getPackagefree() {
		return Packagefree;
	}

	public void setPackagefree(float packagefree) {
		Packagefree = packagefree;
	}

	public String getDataID() {
		return DataID;
	}

	public void setDataID(String dataID) {
		DataID = dataID;
	}

	public String getTogoName() {
		return TogoName;
	}

	public void setTogoName(String togoName) {
		TogoName = togoName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public float getAvGrade() {
		return avGrade;
	}

	public void setAvGrade(float avGrade) {
		this.avGrade = avGrade;
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

	public float CalculationSendFee(){//�������ͷ�
		float sendFee = startSendFee;
		if (maxKM > minKM && distance > maxKM) {//maxKM�������minKM ����û����
			sendFee += ((maxKM - minKM) * SendFeeOfKM + (distance - maxKM) * SendFeeAffKM);
		}else if(minKM > 0.0 && distance > minKM){//�����������
			sendFee += (distance - minKM) * SendFeeOfKM;
		}else{//���𲽼ۣ�����ÿ�������Ǯ
			sendFee += distance * SendFeeOfKM;
		}
		return sendFee;
	}
	
	public float getSendDistance() {
		return SendDistance;
	}

	public void setSendDistance(float sendDistance) {
		SendDistance = sendDistance;
	}

	public String getNews() {
		return News;
	}

	public void setNews(String news) {
		News = news;
	}

	public String getSendTimeDes() {
		return sendTimeDes;
	}

	public void setSendTimeDes(String sendTimeDes) {
		this.sendTimeDes = sendTimeDes;
	}

	

	public FsgShopDetailModel()
	{
		
	}
	
	public int getSendTime() {
		return sendTime;
	}

	public void setSendTime(int sendTime) {
		this.sendTime = sendTime;
	}
	
	

	public void setGrade(String grade) {
		Grade = grade;
	}
	
	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public int getTogotype() {
		return togotype;
	}

	public void setTogotype(int togotype) {
		this.togotype = togotype;
	}

	public int getIsFavor() {
		return isFavor;
	}

	public void setIsFavor(int isFavor) {
		this.isFavor = isFavor;
	}
	

	public String getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(String shopInfo) {
		this.shopInfo = shopInfo;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public int getShopType() {
		return shopType;
	}

	public void setShopType(int shopType) {
		this.shopType = shopType;
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

	// Jsonת����model
	public FsgShopDetailModel(JSONObject json){
		try {
			JSONObject localJSONObject = json;
			
			setId(localJSONObject.getString("DataID"));
			setname(localJSONObject.getString("TogoName"));
			setaddress(localJSONObject.getString("address"));
			goodCount = localJSONObject.getInt("PType");//��������
			okCount = localJSONObject.getInt("RcvType");//һ������
			badCount = localJSONObject.getInt("IsCallCenter");//��������
			sendmoney = (float)localJSONObject.getDouble("sendmoney");
			minMoney = (float)localJSONObject.getDouble("minmoney");
			togotype = 1;//localJSONObject.getInt("togotype");////1���� 2�绰
			isFavor = localJSONObject.getInt("iscollected");
			freeSendMoney = (float)localJSONObject.getDouble("ptimes");
			tagListModel = new TagListModel();
			tagListModel.JsonToBean(localJSONObject);
			ico = localJSONObject.getString("icon");
			sendTime = localJSONObject.getInt("startsendtime");//����ʱ��
			shopType = 1;//localJSONObject.getInt("Types");//1��ʾ�����̼ң�2��ʾ�����̼ң�3��ʾ��֧��
			
			//Grade = localJSONObject.getString("Grade");
			//News = localJSONObject.getString("news");
			//FlavorGrade = (float)localJSONObject.getDouble("FlavorGrade");//ϲ������
			//ServiceGrade = (float)localJSONObject.getDouble("ServiceGrade");//��������
			//SpeedGrade = (float)localJSONObject.getDouble("SpeedGrade");//�ٶ�����
			UpdataAvGrade();
			
			setlat(localJSONObject.getString("lat"));
			setlng(localJSONObject.getString("lng"));
			//sendTimeDes = localJSONObject.getString("BussinessTime");
			setTimeStart1(localJSONObject.getString("Time1Start"));//Ӫҵʱ��
			setTimeStart2(localJSONObject.getString("Time2Start"));
			setTimeEnd1(localJSONObject.getString("Time1End"));
			setTimeEnd2(localJSONObject.getString("Time2End"));
			shopdiscount = ((float)localJSONObject.getInt("shopdiscount"))/10.0f;//�̼��ۿ�
			tel = localJSONObject.getString("Comm");
			
			//features = localJSONObject.getString("features");//�̼���ɫ
			setDesc(localJSONObject.getString("desc"));//�̼��Ż���Ϣ
			setDistance((float)localJSONObject.getDouble("distance"));
			//SendDistance = (float)localJSONObject.getDouble("shopdistance");
			//SendFeeAffKM = (float)localJSONObject.getDouble("everyfee");
			startSendFee = sendmoney;//(float)localJSONObject.getDouble("basemoney");
			//minKM = (float)localJSONObject.getDouble("basedistance");
			
			setStatus(localJSONObject.getInt("status"));
			//sendTime = localJSONObject.getString("SendTime");
			//setTel(localJSONObject.getString("tel"));
			//setSendtype(localJSONObject.getString("sendtype"));
			shopInfo = localJSONObject.getString("desc");//�̼Ҽ��
			setNews(localJSONObject.getString("desc"));//�̼ҹ���
			
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}

	public int getGoodCount() {
		return goodCount;
	}

	public void setGoodCount(int goodCount) {
		this.goodCount = goodCount;
	}

	public int getOkCount() {
		return okCount;
	}

	public void setOkCount(int okCount) {
		this.okCount = okCount;
	}

	public int getBadCount() {
		return badCount;
	}

	public void setBadCount(int badCount) {
		this.badCount = badCount;
	}

	public float getShopdiscount() {
		return shopdiscount;
	}

	public void setShopdiscount(float shopdiscount) {
		this.shopdiscount = shopdiscount;
	}

	public float getSendmoney() {
		return sendmoney;
	}

	public void setSendmoney(float sendmoney) {
		this.sendmoney = sendmoney;
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
	
	

	public void setId(String paramString) {
		this.DataID = paramString;
	}
	
	public String getId() {
		return this.DataID;
	}
	
	public void setname(String paramString) {
		this.TogoName = paramString;
	}
	
	public String getname() {
		return this.TogoName;
	}
	
	public void setaddress(String paramString) {
		this.address = paramString;
	}
	
	public String getaddress() {
		return this.address;
	}

	
	
	
	     
	public int getStar() {
		return Star;
	}

	public void setStar(int star) {
		Star = star;
	}

	public String getGrade() {
		return Grade;
	}

	public void setlat(String paramString) {
		this.lat = paramString;
	}
	
	public String getlat() {
		return this.lat;
	}
	
	public void setlng(String paramString) {
		this.lng = paramString;
	}
	
	public String getlng() {
		return this.lng;
	}
	
	public void setDesc(String paramString) {
		this.desc = paramString;
	}
	
	public String getDesc() {
		return this.desc;
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
	
	public void setDistance(float paramString) {
		this.distance = paramString;
	}
	
	public float getDistance() {
		return this.distance;
	}
	
	public void setStatus(int paramString) {
		this.status = paramString;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	@Override
	public String getCacheKey() {
		if ((this.DataID == null) || ((this.DataID != null) && (this.DataID.equals("")))) {
			return "-1";
		}

		return this.DataID;
	}

	@Override
	public String beanToString() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BaseBean stringToBean(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getSendtype() {
		return sendtype;
	}
	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

	public void UpdataAvGrade() {
		// TODO Auto-generated method stub
		if (goodCount + okCount + badCount == 0) {
			avGrade = -1.0f;
		}else{
			avGrade = (float)(goodCount + okCount)/(float)(goodCount + okCount + badCount);//������
		}
	}

	
}
