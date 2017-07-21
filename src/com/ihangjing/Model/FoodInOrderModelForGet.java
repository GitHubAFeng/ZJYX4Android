package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

//��ȡjson�����ת����model 
//��Ϊ�ӿ��е�json �� �ύ����ʱ�ύ�Ķ���json��ʽ��̫һ����
//���Դ˴�����������ͬ��model��ʵ�� ��ʱ�仹����Ҫȫ��ͳһ
public class FoodInOrderModelForGet extends BaseBean {
	private String id;
	private String name;
	private float price;
	private int count;
	private float packagefree = 0.0F;// �����
	private int isComment = 1;//0��û�����ۣ�1�Ѿ�������
	private int ActivitysType;//�Ƿ��Ź�

	@Override
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();

		return localJSONObject1.toString();
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public float getPrice() {
		return this.price;
	}

	public int getCount() {
		return this.count;
	}

	public float getPackageFree() {
		return this.packagefree;
	}
	
	public int getIsComment() {
		return isComment;
	}

	public void setIsComment(int isComment) {
		this.isComment = isComment;
	}

	public int getActivitysType() {
		return ActivitysType;
	}

	public void setActivitysType(int activitysType) {
		ActivitysType = activitysType;
	}

	//"{\"Num\":1,\"FoodID\":\"5995\",\"FoodPrice\":7.0,
	//\"foodname\":\"������˺��\",\"package\":\"0.00\"
	@Override
	public FoodInOrderModelForGet stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}
		
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			this.id = localJSONObject.getString("id");
			this.name = localJSONObject.getString("name");
			this.price = Float.parseFloat(localJSONObject.getString("price"));
			this.count = localJSONObject.getInt("count");
			/*this.packagefree = Float.parseFloat(localJSONObject
					.getString("package"));*/

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
	
	public FoodInOrderModelForGet JsonToBean(JSONObject localJSONObject) {
		if (localJSONObject == null) {
			return null;
		}
		
		try {
			//JSONObject localJSONObject = new JSONObject(paramString);
			this.id = localJSONObject.getString("FoodID");
			this.name = localJSONObject.getString("foodname");
			this.price = Float.parseFloat(localJSONObject.getString("FoodPrice"));
			this.count = localJSONObject.getInt("Num");
			ActivitysType  = 0;//Integer.parseInt(localJSONObject.getString("activetype"));
			//isComment = localJSONObject.getInt("isreview");
			/*this.packagefree = Float.parseFloat(localJSONObject
					.getString("package"));*/

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
	
	public FoodInOrderModelForGet JsonToBeanInOrderList(JSONObject localJSONObject) {
		if (localJSONObject == null) {
			return null;
		}
		
		try {
			//JSONObject localJSONObject = new JSONObject(paramString);
			this.id = localJSONObject.getString("id");
			this.name = localJSONObject.getString("name");
			this.price = Float.parseFloat(localJSONObject.getString("price"));
			this.count = localJSONObject.getInt("count");
			ActivitysType  = 0;//Integer.parseInt(localJSONObject.getString("activetype"));
			//isComment = localJSONObject.getInt("isreview");
			/*this.packagefree = Float.parseFloat(localJSONObject
					.getString("package"));*/

		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
}
