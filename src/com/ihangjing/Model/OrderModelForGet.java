package com.ihangjing.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//������Ϣ���ύ����ʱ�Ͳ鿴����ʱʹ��
public class OrderModelForGet extends BaseBean {

	public ArrayList<FoodInOrderModelForGet> list;
	public String foodinorderString;// תΪstring�Ĳ�Ʒ�б���Ϣ
	private String shopid;
	private String shopname;
	private String userid;
	private String username;// �û���
	private String realname;// �ջ���
	private String note;
	private String phone;
	private String address;// ��ַ
	private String eattime;// ����ʱ��
	private String addtime;// �µ�ʱ��
	private float sentmoney;// ���ͷ���
	private float totalmoney;// �ܼۣ��������ͷѣ�
	private int state;// ����״̬
	private String orderid;
	private float packagefree = 0.0f;// �����
	private String orderstr;//����״̬�����ģ�
	private float cardPay;//��Ʒ��֧��
	private float couponPay;//����ȯ
	private float unpay;//����
	private String togoPhone;
	private int payMode;
	private int payStatus;
	private float payMoney;
	private String payTime;
	private int sendstate;
	private int shopState;
	private float promotionmoney;
	public ArrayList<TagModel> Promotions;//�Ż��б�
	private int SortType;
	private String deliverID;
	private String togoAddress;
	private String goods;
	private int sitename;
	private int shopCancel;
	private String Cancelreason;
	public String getOrderstr() {
		return orderstr;
	}

	public void setOrderstr(String orderstr) {
		this.orderstr = orderstr;
	}

	public OrderModelForGet() {
		this.list = new ArrayList<FoodInOrderModelForGet>();
	}

	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		
		return localJSONObject1.toString();
	}

	public String getFoodinorderString() {
		return this.foodinorderString;
	}

	public String getShopId() {
		return this.shopid;
	}

	public String getShopName() {
		return this.shopname;
	}

	public String getUserId() {
		return this.userid;
	}

	public String getUserName() {
		return this.username;
	}

	public String getRealname() {
		return this.realname;
	}

	public String getNote() {
		return this.note;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getAddress() {
		return this.address;
	}

	public String getEattime() {
		return this.eattime;
	}

	public String getAddtime() {
		return this.addtime;
	}

	public float getSentmoney() {
		return this.sentmoney;
	}
	
	public float getCardPay() {
		return cardPay;
	}

	public void setCardPay(float cardPay) {
		this.cardPay = cardPay;
	}

	public float getCouponPay() {
		return couponPay;
	}

	public void setCouponPay(float couponPay) {
		this.couponPay = couponPay;
	}

	public float getUnpay() {
		return unpay;
	}

	public void setUnpay(float unpay) {
		this.unpay = unpay;
	}

	public float getTotalMoney() {
		return this.totalmoney;
	}

	public int getState() {
		return this.state;
	}
	
	

	public String getTogoPhone() {
		return togoPhone;
	}

	public void setTogoPhone(String togoPhone) {
		this.togoPhone = togoPhone;
	}

	public String getOrderid() {
		return this.orderid;
	}

	public float getPackagefree() {
		return this.packagefree;
	}
	
	public ArrayList<FoodInOrderModelForGet> getList() {
		return list;
	}

	public void setList(ArrayList<FoodInOrderModelForGet> list) {
		this.list = list;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public float getTotalmoney() {
		return totalmoney;
	}

	public void setTotalmoney(float totalmoney) {
		this.totalmoney = totalmoney;
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

	public float getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(float payMoney) {
		this.payMoney = payMoney;
	}

	public void setFoodinorderString(String foodinorderString) {
		this.foodinorderString = foodinorderString;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEattime(String eattime) {
		this.eattime = eattime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public void setSentmoney(float sentmoney) {
		this.sentmoney = sentmoney;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public void setPackagefree(float packagefree) {
		this.packagefree = packagefree;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public int getSendstate() {
		return sendstate;
	}

	public void setSendstate(int sendstate) {
		this.sendstate = sendstate;
	}

	public int getShopState() {
		return shopState;
	}

	public void setShopState(int shopState) {
		this.shopState = shopState;
	}

	/*
	 * {"TotalPrice":"34.5","UserName":"���Զ���","Tel":"13750821675","State":"3",
	 * "UserID":"38356", "foodlist":[
	 * "{\"Num\":1,\"FoodID\":\"5995\",\"FoodPrice\":7.0,\"foodname\":\"������˺��\",\"package\":\"0.00\"}"
	 * ,
	 * "{\"Num\":1,\"FoodID\":\"5990\",\"FoodPrice\":3.0,\"foodname\":\"��̢(����)/��\",\"package\":\"0.00\"}"
	 * ,
	 * "{\"Num\":1,\"FoodID\":\"5981\",\"FoodPrice\":7.0,\"foodname\":\"��������Ƭ\",\"package\":\"0.00\"}"
	 * ,
	 * "{\"Num\":1,\"FoodID\":\"5977\",\"FoodPrice\":13.0,\"foodname\":\"��������˾\",\"package\":\"0.00\"}"
	 * ,
	 * "{\"Num\":1,\"FoodID\":\"5973\",\"FoodPrice\":4.5,\"foodname\":\"�������\",\"package\":\"0.00\"}"
	 * ], "TogoName":"�¶�����","orderTime":"2012-06-26 20:45:22","TogoId":"79",
	 * "Address":"ͨ�߹㳡","sendmoney":"8","OrderID":"1206262045220755",
	 * "SentTime":"2012-6-26 21:00","Remark":"���Զ�����Ҫ����"}
	 */
	
	@Override
	public OrderModelForGet stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}

		try {
			this.list.clear();
			JSONObject localJSONObject = new JSONObject(paramString);
			SortType = localJSONObject.getInt("OrderType");//1���� 2Ԥ��
			if (SortType == 1) {
				this.address = localJSONObject.getString("Address");
				this.eattime = localJSONObject.getString("SentTime");
			}else{
				this.address = localJSONObject.getString("Orderseats");
				this.eattime = localJSONObject.getString("OrderDate");
				if (localJSONObject.getInt("OrderTime") == 0) {
					this.address += "-���";
				}else{
					this.address += "-���";
				}
			}
			this.totalmoney = (float)localJSONObject.getDouble("TotalPrice");
			
			this.phone = localJSONObject.getString("Tel");
			this.userid = localJSONObject.getString("UserID");
			this.username = localJSONObject.getString("UserName");
			this.addtime = localJSONObject.getString("orderTime");
			this.sentmoney = (float)localJSONObject.getDouble("sendmoney");
			
			this.orderid = localJSONObject.getString("OrderID");
			this.shopname = localJSONObject.getString("TogoName");
			this.shopid = localJSONObject.getString("TogoId");
			this.note = localJSONObject.getString("Remark");
			this.state = localJSONObject.getInt("State");
			this.payMode = localJSONObject.getInt("paymodeint");
			this.payStatus = localJSONObject.getInt("paystateint");
			this.payMoney = (float)localJSONObject.getDouble("paymoney");
			this.payTime = localJSONObject.getString("paytime");
			sendstate = localJSONObject.getInt("sendstate");//����״̬
			shopState = localJSONObject.getInt("IsShopSet");//�̼��Ƿ����
			//ActivitysType  = Integer.parseInt(localJSONObject.getString("activetype"));
			//togoPhone = localJSONObject.getString("shoptel");
			this.packagefree = (float)localJSONObject.getDouble("Packagefree");
			//this.orderstr = localJSONObject.getString("orderstr");
			
			
			//this.couponPay = localJSONObject.getString("paymoney");
			//this.unpay = localJSONObject.getString("alipaymoney");
			this.cardPay = (float)localJSONObject.getDouble("cardpay");//�Ż�ȯ֧�����
			promotionmoney = (float)localJSONObject.getDouble("promotionmoney");//�����Żݽ��
			JSONArray localJSONArray;
			if (promotionmoney > 0.0f) {
				localJSONArray = localJSONObject.getJSONArray("Promotions");
				TagModel model;
				Promotions = new ArrayList<TagModel>();
				for (int i = 0; i < localJSONArray.length(); i++) {
					model = new TagModel();
					model.PromotionsToBean(localJSONArray.getJSONObject(i));
					Promotions.add(model);
				}
			}
			localJSONArray = localJSONObject.getJSONArray("foodlist");
			int k = 0;
			int m = localJSONArray.length();
			// ѭ����ȡ���е��̼���Ϣ
			while (true) {
				
				if (k >= m) {
					break;
				}
				FoodInOrderModelForGet localShopBean1 = new FoodInOrderModelForGet();
				//String str3 = (String) localJSONArray.get(k);
				this.list.add(localShopBean1.JsonToBean(localJSONArray.getJSONObject(k)));
				k += 1;
			}
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}

	public String getDeliverID() {
		return deliverID;
	}

	public void setDeliverID(String deliverID) {
		this.deliverID = deliverID;
	}

	public String getTogoAddress() {
		return togoAddress;
	}

	public void setTogoAddress(String togoAddress) {
		this.togoAddress = togoAddress;
	}

	

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public int getSitename() {
		return sitename;
	}

	public void setSitename(int sitename) {
		this.sitename = sitename;
	}

	public void JSONToBean(String paramString, int type) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return;
		}

		try {
			this.list.clear();
			JSONObject localJSONObject = new JSONObject(paramString);
			SortType = type;//1���� 2Ԥ��
			
			if(SortType < 3){
				this.addtime = localJSONObject.getString("orderTime");
				if (SortType == 0) {
					this.address = localJSONObject.getString("Address");
					this.eattime = localJSONObject.getString("SentTime");
					this.sentmoney = (float)localJSONObject.getDouble("sendmoney");
					this.payMode = localJSONObject.getInt("paymodeint");
					sendstate = localJSONObject.getInt("sendstate");//����״̬
					this.packagefree = (float)localJSONObject.getDouble("Packagefree");
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date d1 = df.parse(addtime);
					df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date d2 = df.parse(eattime);
					long diff = d2.getTime() - d1.getTime();//�����õ��Ĳ�ֵ��΢�뼶��
					if (diff < 1800000) {//
						eattime = "�����ʹ�";
					}
				}else if(SortType == 2){
					this.address = localJSONObject.getString("Orderseats");
					this.eattime = localJSONObject.getString("MealTime");
					this.eattime = this.eattime.split(" ")[0];
					this.payMode = localJSONObject.getInt("PayMode");
					if (localJSONObject.getInt("OrderTime") == 0) {
						this.eattime += " ���";
					}else{
						this.eattime += " ���";
					}
				}
				String value = localJSONObject.getString("sitename");
				if(value.length() > 0){
					sitename = Integer.parseInt(value) * 60000;//�̼ҵ�����ʱ�䣬��λ����
				}
				
				this.totalmoney = (float)localJSONObject.getDouble("TotalPrice");
				this.payTime = localJSONObject.getString("paytime");
				this.phone = localJSONObject.getString("Tel");
				this.userid = localJSONObject.getString("UserID");
				this.username = localJSONObject.getString("UserName");
				this.addtime = localJSONObject.getString("orderTime");
				
				shopCancel = localJSONObject.getInt("shopCancel");//����ȡ������ʱ�̼�0 δ���� 1ͬ�� 2�ܾ�
				Cancelreason = localJSONObject.getString("Cancelreason");//�ܽ�����
				this.orderid = localJSONObject.getString("OrderID");
				this.shopname = localJSONObject.getString("TogoName");
				this.shopid = localJSONObject.getString("TogoId");
				this.note = localJSONObject.getString("Remark");
				this.state = localJSONObject.getInt("State");
				deliverID = localJSONObject.getString("deliverid");
				this.payStatus = localJSONObject.getInt("paystateint");
				this.payMoney = (float)localJSONObject.getDouble("paymoney");
				
				
				shopState = localJSONObject.getInt("IsShopSet");//�̼��Ƿ����
				//ActivitysType  = Integer.parseInt(localJSONObject.getString("activetype"));
				togoPhone = localJSONObject.getString("shoptel");
				
				//this.orderstr = localJSONObject.getString("orderstr");
				
				
				//this.couponPay = localJSONObject.getString("paymoney");
				//this.unpay = localJSONObject.getString("alipaymoney");
				this.cardPay = (float)localJSONObject.getDouble("cardpay");//�Ż�ȯ֧�����
				promotionmoney = (float)localJSONObject.getDouble("promotionmoney");//�����Żݽ��
				JSONArray localJSONArray;
				if (promotionmoney > 0.0f) {
					localJSONArray = localJSONObject.getJSONArray("Promotions");
					TagModel model;
					Promotions = new ArrayList<TagModel>();
					for (int i = 0; i < localJSONArray.length(); i++) {
						model = new TagModel();
						model.PromotionsToBean(localJSONArray.getJSONObject(i));
						Promotions.add(model);
					}
				}
				localJSONArray = localJSONObject.getJSONArray("foodlist");
				int k = 0;
				int m = localJSONArray.length();
				// ѭ����ȡ���е��̼���Ϣ
				while (true) {
					
					if (k >= m) {
						break;
					}
					FoodInOrderModelForGet localShopBean1 = new FoodInOrderModelForGet();
					//String str3 = (String) localJSONArray.get(k);
					this.list.add(localShopBean1.JsonToBean(localJSONArray.getJSONObject(k)));
					k += 1;
				}
			}else{
				//paydelivertip = (float)localJSONObject.getDouble("paydelivertip");//С��
				//this.totalmoney = localJSONObject.getDouble("totalmoney");//�ܼۣ�������Ʒ�۸���˷�
				this.address = localJSONObject.getString("toaddress");//�ռ�����ϵ��ַ
				this.phone =localJSONObject.getString("totel");//�ռ�����ϵ��ʽ
				//this.userid = localJSONObject.getString("userid");
				this.username = localJSONObject.getString("toname");//�ռ���
				this.addtime = localJSONObject.getString("addtime");//�µ�ʱ��
				this.sentmoney = (float)localJSONObject.getDouble("sendmoney");//���ͷ�
				this.eattime = localJSONObject.getString("sendtime");//����ʱ��
				this.shopid = localJSONObject.getString("senddistance");//���;���
				//this.people = localJSONObject.getInt("people");
				this.orderid = localJSONObject.getString("orderid");
				this.shopname = localJSONObject.getString("fromname");//������
				this.note = localJSONObject.getString("Remark");//��ע
				sendstate = localJSONObject.getInt("state");
				this.state = localJSONObject.getInt("state");//0 ���������ӵ���;1�ѵ���; 2ȡ����; 4 ������;3 �ɹ� ;5 ȡ��,6��ʧ��
				this.togoAddress = localJSONObject.getString("fromaddress");//��������ϵ��ַ
				this.togoPhone = localJSONObject.getString("Fromtel");//��������ϵ��ʽ
				//this.realname = localJSONObject.getString("callmsg");
				goods = localJSONObject.getString("goods");//��Ʒ����
				this.payMode = localJSONObject.getInt("PayMode");
				//paydeliverfee = (float)localJSONObject.getDouble("paydeliverfee");//����Ա�յ������ͷ�
				//paydelivertip = (float)localJSONObject.getDouble("paydelivertip");//����Ա�յ���С��
				/*String value = localJSONObject.getString("ulat");
				if (value.length() > 0) {
					uLat = Double.valueOf(value);
				}else{
					uLat = 0.0;
				}
				value = localJSONObject.getString("ulng");
				if (value.length() > 0) {
					uLon = Double.valueOf(value);
				}else{
					uLon = 0.0;
				}
				value = localJSONObject.getString("shoplat");
				if (value.length() > 0) {
					sLat = Double.valueOf(value);
				}else{
					sLat = 0.0;
				}
				value = localJSONObject.getString("shoplng");
				if (value.length() > 0) {
					sLon = Double.valueOf(value);
				}else{
					sLon = 0.0;
				}*/
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = df.parse(addtime);
				
				Date d2 = df.parse(eattime);
				long diff = d2.getTime() - d1.getTime();//�����õ��Ĳ�ֵ��΢�뼶��
				if (diff < 1800000) {//
					eattime = "��������";
				}
			}
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public int getShopCancel() {
		return shopCancel;
	}

	public void setShopCancel(int shopCancel) {
		this.shopCancel = shopCancel;
	}

	public String getCancelreason() {
		return Cancelreason;
	}

	public void setCancelreason(String cancelreason) {
		Cancelreason = cancelreason;
	}

	public ArrayList<TagModel> getPromotions() {
		return Promotions;
	}

	public void setPromotions(ArrayList<TagModel> promotions) {
		Promotions = promotions;
	}

	public int getSortType() {
		return SortType;
	}

	public void setSortType(int sortType) {
		SortType = sortType;
	}

	public float getPromotionmoney() {
		return promotionmoney;
	}

	public void setPromotionmoney(float promotionmoney) {
		this.promotionmoney = promotionmoney;
	}
}