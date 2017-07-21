package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

//{"SortID":"6413","SortName":"商务餐","JOrder":"1"}
public class FoodTypeModel {
	private int id = 0;// 编号
	private String name = "";// 名称
	private String imageNetPath = "";//图片网络地址
	private String imageLocalPath = "";//图片本地地址
	//public boolean isDowload = false;//是否需要下载图片
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

	// Json转换成model
	public FoodTypeModel(JSONObject json) throws JSONException {
		
			JSONObject localJSONObject = json;
			setId(localJSONObject.getInt("SortID"));
			setName(localJSONObject.getString("SortName"));
			imageNetPath = localJSONObject.getString("icon");
	}

	// 生成json
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

	// Json转换成model
	//sr = 0 从网络读取的json，＝1 本地缓存
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
