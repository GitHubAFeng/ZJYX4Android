package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

//�����б��еĶ�����Ϣ,ֻ�������ļ�������
public class GiftDetailOrderModel {
	// {"OrderID":"1206262045220755","TogoName":"�¶�����",
	// "orderTime":"2012-06-26 20:45:26","TotalPrice":"34.5",
	// "State":"3","sendmoney":"8","Packagefree":"0.0"}
	private String orderid;
	private String giftName;
	private String addtime; // �ύʱ��
	private String usePoint; // /��������
	private int sendType = 2; // 1���ᣬ2�ͻ�
	private int type = 0; // 0��ͨ��Ʒ 1 �ֽ�ȯ
	private int state;// 0 δ��� 1 ���ͨ�� 2 ���δͨ��
	private String uAddress = "";// �ͻ���ַ
	private String pAddressString = "";// ȡ����ַ
	private String recName = "";// �ջ���
	private String recPhone = "";// �ջ��绰
	private String recData = "";// �ͻ�ʱ��Ҫ��

	// Jsonת����model
	public GiftDetailOrderModel(JSONObject json) throws JSONException {

		JSONObject localJSONObject = json;

		// orderid = localJSONObject.getString("IntegralId");
		giftName = localJSONObject.getString("GiftName");
		usePoint = localJSONObject.getString("PayIntegral");// ��������
		addtime = localJSONObject.getString("Cdate");// �ύʱ��
		//sendType = localJSONObject.getInt("sendtype");// 1���ᣬ2�ͻ�
		//type = localJSONObject.getInt("ReveInt1");// 0��ͨ��Ʒ 1 �ֽ�ȯ
		state = localJSONObject.getInt("State");// ������0 δ��� 1 ���ͨ�� 2 ���δͨ��
		uAddress = localJSONObject.getString("Address");// �ͻ���ַ
		//pAddressString = localJSONObject.getString("remark");// ȡ����ַ
		recName = localJSONObject.getString("Person");// �ջ���
		recPhone = localJSONObject.getString("Phone");// �ջ��绰
		recData = localJSONObject.getString("Date");// �ͻ�ʱ��Ҫ��

	}

	public GiftDetailOrderModel() {

	}

	public String getuAddress() {
		return uAddress;
	}

	public void setuAddress(String uAddress) {
		this.uAddress = uAddress;
	}

	public String getpAddressString() {
		return pAddressString;
	}

	public void setpAddressString(String pAddressString) {
		this.pAddressString = pAddressString;
	}

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	public String getRecPhone() {
		return recPhone;
	}

	public void setRecPhone(String recPhone) {
		this.recPhone = recPhone;
	}

	public String getRecData() {
		return recData;
	}

	public void setRecData(String recData) {
		this.recData = recData;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getUsePoint() {
		return usePoint;
	}

	public void setUsePoint(String usePoint) {
		this.usePoint = usePoint;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public GiftDetailOrderModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			orderid = localJSONObject.getString("IntegralId");
			giftName = localJSONObject.getString("GiftName");
			usePoint = localJSONObject.getString("PayIntegral");// ��������
			addtime = localJSONObject.getString("Cdate");// �ύʱ��
			sendType = localJSONObject.getInt("sendtype");// 1���ᣬ2�ͻ�
			type = localJSONObject.getInt("ReveInt1");// 0��ͨ��Ʒ 1 �ֽ�ȯ
			state = localJSONObject.getInt("State");// ������0 δ��� 1 ���ͨ�� 2 ���δͨ��
			uAddress = localJSONObject.getString("Address");// �ͻ���ַ
			pAddressString = localJSONObject.getString("remark");// ȡ����ַ
			recName = localJSONObject.getString("Person");// �ջ���
			recPhone = localJSONObject.getString("Phone");// �ջ��绰
			recData = localJSONObject.getString("Date");// �ͻ�ʱ��Ҫ��

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
