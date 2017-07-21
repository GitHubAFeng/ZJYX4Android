package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class MyCouponModel {
	private String CKey;// ȯ��
	private int CType;// ȯ���� 1�ֽ�ȯ 2�ۿ�
	private String CValue;// ȯֵ
	private String geiverPerson;// ������
	private int CTimeLimity;// �Ƿ�ʱ������ 0�����ƣ�1������
	private String StartTime;// ʱ�����Ƶ���ʼʱ��
	private String EndTime;// ʱ�����ƵĽ���ʱ��
	private int CMoneyLine;// �Ƿ������� 0 ������ 1������
	private float MoneyLine = 0.0f;// ���ƽ��
	private float CMoney = 0.0f;// ʣ����
	private int CActivity = 0;// �Ƿ񼤻� 0û�� 1����
	private int dataID;// ���
	private int isUser = 0;// �Ƿ�ʹ�� 0δ�ã�1ʹ�ù�
	private int isSelect = 0;// �Ƿ�ѡ�У�0δѡ�� 1ѡ��
	private int indexSelect;// ѡ�ж����е�λ�ã�����ʹ���Ż�ȯʱ�õ���

	public String getCKey() {
		return CKey;
	}

	public void setCKey(String cKey) {
		CKey = cKey;
	}

	public int getCType() {
		return CType;
	}

	public void setCType(int cType) {
		CType = cType;
	}

	public String getCValue() {
		return CValue;
	}

	public void setCValue(String cValue) {
		CValue = cValue;
	}

	public String getGeiverPerson() {
		return geiverPerson;
	}

	public void setGeiverPerson(String geiverPerson) {
		this.geiverPerson = geiverPerson;
	}

	public int getCTimeLimity() {
		return CTimeLimity;
	}

	public void setCTimeLimity(int cTimeLimity) {
		CTimeLimity = cTimeLimity;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public int getCMoneyLine() {
		return CMoneyLine;
	}

	public void setCMoneyLine(int cMoneyLine) {
		CMoneyLine = cMoneyLine;
	}

	public float getMoneyLine() {
		return MoneyLine;
	}

	public void setMoneyLine(float moneyLine) {
		MoneyLine = moneyLine;
	}

	public float getCMoney() {
		return CMoney;
	}

	public void setCMoney(float cMoney) {
		CMoney = cMoney;
	}

	public int getCActivity() {
		return CActivity;
	}

	public void setCActivity(int cActivity) {
		CActivity = cActivity;
	}

	public int getDataID() {
		return dataID;
	}

	public void setDataID(int dataID) {
		this.dataID = dataID;
	}

	public int getIsUser() {
		return isUser;
	}

	public void setIsUser(int isUser) {
		this.isUser = isUser;
	}

	public int getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public int getIndexSelect() {
		return indexSelect;
	}

	public void setIndexSelect(int indexSelect) {
		this.indexSelect = indexSelect;
	}
	public MyCouponModel(){
		
	}
	public MyCouponModel(JSONObject json) throws JSONException{
		dataID = json.getInt("CID");
		CValue = json.getString("point");
		CType = json.getInt("ReveInt");
		geiverPerson = json.getString("geivecardperson");
		CTimeLimity = json.getInt("timelimity");
		StartTime = json.getString("starttime");
		EndTime = json.getString("endtime");
		CMoneyLine = json.getInt("moneylimity");
		MoneyLine = (float)json.getDouble("moneyline");
		CMoney = (float)json.getDouble("cmoney");
		CActivity = json.getInt("Inve2");
		isUser = json.getInt("isused");
		CKey = json.getString("ckey");
	}
}
