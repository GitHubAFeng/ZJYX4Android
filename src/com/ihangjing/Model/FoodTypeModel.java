package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

//{"SortID":"6413","SortName":"�����","JOrder":"1"}
public class FoodTypeModel {
	private int id = 0;// ���
	private String name = "";// ����
	private String imageNetPath = "";//ͼƬ�����ַ
	private String imageLocalPath = "";//ͼƬ���ص�ַ
	//public boolean isDowload = false;//�Ƿ���Ҫ����ͼƬ
	public boolean sel = false;

	public FoodTypeModel() {

	}
	public String getImageNetPath() {
		return imageNetPath;
	}

	public void setImageNetPath(String imageNetPath) {
		this.imageNetPath = imageNetPath;
	}

	public String getImageLocalPath() {
		return imageLocalPath;
	}

	public void setImageLocalPath(String imageLocalPath) {
		this.imageLocalPath = imageLocalPath;
	}

	// Jsonת����model
	public FoodTypeModel(JSONObject json) throws JSONException {
		
			JSONObject localJSONObject = json;
			setId(localJSONObject.getInt("SortID"));
			setName(localJSONObject.getString("SortName"));
			imageNetPath = localJSONObject.getString("icon");
	}

	// ����json
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("SortID", String.valueOf(id));
			localJSONObject1.put("SortName", name);
			localJSONObject1.put("localPath", imageLocalPath);
			localJSONObject1.put("icon", imageNetPath);
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			return "";
		}
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
	public void jsonToBean(JSONObject localJSONObject, int sr){
		try {
			setId(localJSONObject.getInt("SortID"));
			setName(localJSONObject.getString("SortName"));
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
