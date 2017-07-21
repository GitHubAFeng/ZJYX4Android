package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

//������Ϣ���ύ����ʱ�Ͳ鿴����ʱʹ��
public class OrderModel extends BaseBean {

	public ArrayList<FoodInOrderModel> list;
	public ArrayList<ShopCartItemModel> listfix;
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
	private String sentmoney;// ���ͷ���
	private String totalmoney;// �ܼۣ��������ͷѣ�
	private String state;// ����״̬
	private int people = 1;
	private String orderid;
	private String packagefree;// �����
	private String lat;
	private String lng;

	private String Oorderid;// ��Ҫ���ջ���,������ҪΪ0
	private String Cityid;// ���б��
	private String bid;// ��������
	private String payType;// ֧����ʽ:0����֧��,1����֧��
	private String PayMode;// ֧����ʽ 1,֧����֧��;2,��Ʒ��֧��;3,��������;4,��������;5,��������;
	private String cardpay;// ��Ʒ��֧�����
	private String OrderType;// �������ͣ�0����ͨ���� 1���Ź����� 2:��ɱ���� 3:���ֶһ�����
	private String ordersource;// ������Դ:0��ʾweb,1��ʾandroid,2��ʾios
	private String sendtype;// ���ͷ�ʽ:1�ͻ�����;2��������;3��������
	private String usersn;
	private String shopsn;
	private CouponListModel CID;//��Ʒ��
	private String CIDStringx;//����ȯ�������
	private String PayPassword;
	private String reciveAddressID;

	public OrderModel() {
		this.list = new ArrayList<FoodInOrderModel>();
		this.listfix = new ArrayList<ShopCartItemModel>();
	}

	public String getCIDStringx() {
		return CIDStringx;
	}

	public void setCIDStringx(String cIDStringx) {
		CIDStringx = cIDStringx;
	}

	public String getOrderid() {
		return this.orderid;
	}

	public void setOrderid(String paramInt) {
		this.orderid = paramInt;
	}

	public int getPeople() {
		return this.people;
	}

	public void setPeople(int paramInt) {
		this.people = paramInt;
	}

	public String getPackagefree() {
		return this.packagefree;
	}

	public void setPackagefree(String paramInt) {
		this.packagefree = paramInt;
	}

	public String getLat() {
		return this.lat;
	}

	public void setLat(String paramInt) {
		this.lat = paramInt;
	}

	public String getLng() {
		return this.lng;
	}

	public void setLng(String paramInt) {
		this.lng = paramInt;
	}

	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {

			localJSONObject1.put("TotalPrice", this.totalmoney);
			localJSONObject1.put("Tel", this.phone);
			localJSONObject1.put("UserID", this.userid);

			localJSONObject1.put("TogoId", this.shopid);
			localJSONObject1.put("orderTime", this.addtime);
			localJSONObject1.put("UserName", this.username);
			localJSONObject1.put("Address", this.address);
			localJSONObject1.put("sendmoney", this.sentmoney);
			localJSONObject1.put("CustomerName", this.realname);
			localJSONObject1.put("Remark", this.note);
			localJSONObject1.put("SentTime", this.eattime);
			localJSONObject1.put("Inve2", "1");

			localJSONObject1.put("latlng", "\"{\"ulat\":\"" + this.lat
					+ "\",\"ulng\":\"" + this.lng
					+ "\",\"slat\":\"_shoplat\",\"slng\":\"_shoplng\"}\"");
			JSONArray localJSONArray1 = new JSONArray();
			Iterator<FoodInOrderModel> localIterator = this.list.iterator();

			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("cartlist", localJSONArray1);
					return localJSONObject1.toString();
				}
				localJSONArray1.put(localIterator.next().beanToString());
			}
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToStringFix() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("UserName", this.username);
			localJSONObject1.put("Address", this.address);
			localJSONObject1.put("Phone", "");
			localJSONObject1.put("Tel", this.phone);
			localJSONObject1.put("Remark", this.note);
			localJSONObject1.put("Oorderid", "0");
			localJSONObject1.put("tempCode", "");
			localJSONObject1.put("sendfree", this.sentmoney);
			localJSONObject1.put("bid", this.bid);
			//localJSONObject1.put("UserName", this.realname);
			localJSONObject1.put("UserID", this.userid);
			localJSONObject1.put("cityid", this.Cityid);
			localJSONObject1.put("payType", this.payType);
			localJSONObject1.put("PayMode", this.PayMode);
			localJSONObject1.put("cardpay", this.cardpay);
			localJSONObject1.put("OrderType", this.OrderType);
			localJSONObject1.put("orderSources", this.ordersource);
			localJSONObject1.put("sendtype", this.sendtype);
			localJSONObject1.put("SentTime", this.eattime);
			localJSONObject1.put("usersn", this.usersn);
			localJSONObject1.put("shopsn", this.shopsn);
			localJSONObject1.put("TogoId", this.shopid);
			localJSONObject1.put("addrID", reciveAddressID);
			
			localJSONObject1.put("ShopCardIDs", this.CIDStringx);
			localJSONObject1.put("PayPassword", this.PayPassword);
			

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<FoodInOrderModel> localIterator = this.list.iterator();

			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("cartlist", localJSONArray1);
					break;
				}
				String tempString = localIterator.next().beanToStringFix(0);
				localJSONArray1.put(tempString);
			}
			
			localJSONArray1 = new JSONArray();
			JSONObject localJSONObject2;
			for(int i = 0; CID != null && i < CID.list.size(); i++){
				localJSONObject2 = new JSONObject();
				localJSONObject2.put("cardnum", CID.list.get(i).getCardnum());
				
				localJSONArray1.put(localJSONObject2.toString());
			}
			localJSONObject1.put("ShopCardList", localJSONArray1);

			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
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

	public String getSentmoney() {
		return this.sentmoney;
	}

	public String getTotalMoney() {
		return this.totalmoney;
	}

	public String getState() {
		return this.state;
	}

	public void setFoodinorderString(String paramString) {
		this.foodinorderString = paramString;
	}

	public void setShopId(String paramString) {
		this.shopid = paramString;
	}

	public void setShopName(String paramString) {
		this.shopname = paramString;
	}

	public void setUserid(String paramString) {
		this.userid = paramString;
	}

	public void setUsername(String paramString) {
		this.username = paramString;
	}

	public void setRealname(String paramString) {
		this.realname = paramString;
	}

	public void setNote(String paramString) {
		this.note = paramString;
	}

	public void setPhone(String paramString) {
		this.phone = paramString;
	}

	public void setEattime(String paramString) {
		this.eattime = paramString;
	}

	public void setAddtime(String paramString) {
		this.addtime = paramString;
	}

	public void setSentmoney(String paramString) {
		this.sentmoney = paramString;
	}

	public void setTotalmoney(String paramString) {
		this.totalmoney = paramString;
	}

	public void setState(String paramString) {
		this.state = paramString;
	}

	public void setAddress(String paramString) {
		this.address = paramString;
	}

	@Override
	public OrderModel stringToBean(String paramString) {

		return this;
	}

	public String getOorderid() {
		return Oorderid;
	}

	public void setOorderid(String oorderid) {
		Oorderid = oorderid;
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

	public String getCardpay() {
		return cardpay;
	}

	public void setCardpay(String cardpay) {
		this.cardpay = cardpay;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
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

	public String getShopsn() {
		return shopsn;
	}

	public void setShopsn(String shopsn) {
		this.shopsn = shopsn;
	}

	public CouponListModel getCID() {
		return CID;
	}

	public void setCID(CouponListModel cID) {
		this.CID = cID;
	}

	public String getPayPassword() {
		return PayPassword;
	}

	public void setPayPassword(String payPassword) {
		this.PayPassword = payPassword;
	}

	public void setAddressID(String addressID) {
		// TODO Auto-generated method stub
		reciveAddressID = addressID;
	}
}
