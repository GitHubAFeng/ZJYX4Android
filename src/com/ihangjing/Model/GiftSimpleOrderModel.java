package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

//�����б��еĶ�����Ϣ,ֻ�������ļ�������
public class GiftSimpleOrderModel {
	//{"OrderID":"1206262045220755","TogoName":"�¶�����",
	//"orderTime":"2012-06-26 20:45:26","TotalPrice":"34.5",
	//"State":"3","sendmoney":"8","Packagefree":"0.0"}
	private String orderid;
	private String giftName;
	private String addtime;    // �µ�ʱ��
	private String usePoint; // �ܼۣ�ע��:�����ͷѣ�
	private int sendType = 2;      // ����״̬
	private int type = 0;  // ���ͷ�
	private int state;// �����
	
	// Jsonת����model
	public GiftSimpleOrderModel(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			
			orderid = localJSONObject.getString("IntegralId");
			giftName = localJSONObject.getString("GiftName");
			usePoint = localJSONObject.getString("PayIntegral");//��������
			addtime = localJSONObject.getString("Cdate");//�ύʱ��
			sendType = localJSONObject.getInt("sendtype");//1���ᣬ2�ͻ�
			type = localJSONObject.getInt("ReveInt1");//0��ͨ��Ʒ 1 �ֽ�ȯ
			state = localJSONObject.getInt("State");//������0 δ��� 1 ���ͨ�� 2 ���δͨ��
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}
	
	public GiftSimpleOrderModel() {

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

	public GiftSimpleOrderModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			orderid = localJSONObject.getString("IntegralId");
			giftName = localJSONObject.getString("GiftName");
			usePoint = localJSONObject.getString("PayIntegral");//��������
			addtime = localJSONObject.getString("Cdate");//�ύʱ��
			sendType = localJSONObject.getInt("sendtype");//1���ᣬ2�ͻ�
			type = localJSONObject.getInt("ReveInt1");//0��ͨ��Ʒ 1 �ֽ�ȯ
			state = localJSONObject.getInt("State");//������0 δ��� 1 ���ͨ�� 2 ���δͨ��
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
