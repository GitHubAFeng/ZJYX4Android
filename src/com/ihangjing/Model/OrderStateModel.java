package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

//{"SortID":"6413","SortName":"�����","JOrder":"1"}
public class OrderStateModel {
	private int id = 0;// ���
	private String name = "";// ����
	private String subtitle = "";
	private String addtime = "";
	//public boolean isDowload = false;//�Ƿ���Ҫ����ͼƬ
	public boolean sel = false;
	private String deliverTel;
	private double deliverLat;
	private double deliverLng;
	public OrderStateModel() {

	}
	

	public String getDeliverTel() {
		return deliverTel;
	}


	public void setDeliverTel(String deliverTel) {
		this.deliverTel = deliverTel;
	}


	public double getDeliverLat() {
		return deliverLat;
	}


	public void setDeliverLat(double deliverLat) {
		this.deliverLat = deliverLat;
	}


	public double getDeliverLng() {
		return deliverLng;
	}


	public void setDeliverLng(double deliverLng) {
		this.deliverLng = deliverLng;
	}


	// Jsonת����model
	public OrderStateModel(JSONObject json) throws JSONException {
		
			JSONObject localJSONObject = json;
			setId(localJSONObject.getInt("sId"));
			setName(localJSONObject.getString("title"));
			addtime = localJSONObject.getString("addtime");
			addtime = addtime.replace("T", " ");
			subtitle = localJSONObject.getString("subtitle");
	}

	

	public String getSubtitle() {
		return subtitle;
	}


	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}


	public String getAddtime() {
		return addtime;
	}


	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}


	public boolean isSel() {
		return sel;
	}


	public void setSel(boolean sel) {
		this.sel = sel;
	}


	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setId(int paramString) {
		this.id = paramString;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	// Jsonת����model
	//sr = 0 �������ȡ��json����1 ���ػ���
	public void jsonToBean(JSONObject localJSONObject){
		try {
			setId(localJSONObject.getInt("sId"));
			setName(localJSONObject.getString("title"));
			addtime = localJSONObject.getString("addtime");
			addtime = addtime.replace("T", " ");
			addtime = addtime.substring(0, 19);
			subtitle = localJSONObject.getString("subtitle");
			deliverTel = localJSONObject.getString("revevar1");//��ʿ�绰
			deliverLat = localJSONObject.getDouble("revevar3");//��ʿ��ǰγ��
			deliverLng = localJSONObject.getDouble("revevar2");//��ʿ��ǰ����
			//imageNetPath = localJSONObject.getString("icon");
			/*if (sr == 1) {
				imageLocalPath = localJSONObject.getString("localPath");
				isDowload = false;
			}else{
				isDowload = true;
			}*/
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
