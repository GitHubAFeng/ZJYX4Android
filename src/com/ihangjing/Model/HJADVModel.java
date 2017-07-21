package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class HJADVModel {
	private int DataID;//编号
	private int advType;//广告类型 0商品 ，1·公告
	private String ImageNetPath;//图片网络路径
	private String ImageLocalPath="";//图片本地路径
	private String title="";//标题
	//public boolean isDowload = false;//是否需要下载图片
	public int getDataID() {
		return DataID;
	}
	public void setDataID(int dataID) {
		DataID = dataID;
	}
	public int getAdvType() {
		return advType;
	}
	public void setAdvType(int advType) {
		this.advType = advType;
	}
	public String getImageNetPath() {
		return ImageNetPath;
	}
	public void setImageNetPath(String imageNetPath) {
		ImageNetPath = imageNetPath;
	}
	public String getImageLocalPath() {
		return ImageLocalPath;
	}
	public void setImageLocalPath(String imageLocalPath) {
		ImageLocalPath = imageLocalPath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void JsonToBean(JSONObject json, int sr, int type){
		try {
			if (type == 0) {
				//DataID = json.getInt("SortID");
				ImageNetPath = json.getString("SortName");
				title = json.getString("SortID");
				if (title.length() >= 18) {//http://a.a/a.a?a=a
					if (title.indexOf("shopreview.aspx") >= 0) {//点评过来的
						advType = 2;//进入商家点评界面
					}else if (title.indexOf("shop.aspx") >= 0) {//商家详情
						advType = 3;//进入商家界面，直接进入商品界面
					}
					int index = title.indexOf("id=");
					if (index >= 0) {
						int postion = title.indexOf("&");
						if (postion >= 0) {
							if (postion < index) {//在id=前面有其他参数
								title = title.substring(index);
								postion = title.indexOf("&");//检查后面是否还有&
								if (postion > 0) {
									title = title.substring(0, postion);
								}
							}else{
								title = title.substring(index, postion);
							}
						}else{//只有一个参数
							title = title.substring(index);
						}
						String[] valueStrings = title.split("=");
						DataID = Integer.parseInt(valueStrings[1]);
					}
					
				}
				
				
			}else{
				DataID = json.getInt("dataid");
				ImageNetPath = json.getString("icon");
				
				advType = -1;
			}
			
			/*if (sr == 1) {
				ImageLocalPath = json.getString("locaPath");
				isDowload = false;
			}else{
				isDowload = true;
			}*/
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String BeanToJsonString(){
		JSONObject json = new JSONObject();
		try{
			json.put("dataid", String.valueOf(DataID));
			json.put("SortName", ImageNetPath);
			json.put("locaPath", ImageLocalPath);
			json.put("title", title);
			json.put("dataname", String.valueOf(advType));
		}catch (Exception e) {
			// TODO: handle exception
			return "";
		}
		return json.toString();
	}
}
