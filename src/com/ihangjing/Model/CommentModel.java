package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

public class CommentModel {
	private int id;
	private String userID;//用户编号
	private String foodID;//食品编号
	private int point;//星级
	private String odid;//订单编号提交评论时用
	private String userName;
	private String foodName;//餐品名称提交评论时用
	private String ServerG;//服务评分
	private String FlavorG;//口感评分
	private String OutG;//外观评分
	private String time;//评价时间
	private String value;//评价内容
	private String togoID;//商家编号
	private String userpic;//用户评论图片
	
	public String getUserpic() {
		return userpic;
	}
	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getFoodID() {
		return foodID;
	}
	public void setFoodID(String foodID) {
		this.foodID = foodID;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getOdid() {
		return odid;
	}
	public void setOdid(String odid) {
		this.odid = odid;
	}
	public String getServerG() {
		return ServerG;
	}
	public void setServerG(String serverG) {
		ServerG = serverG;
	}
	public String getFlavorG() {
		return FlavorG;
	}
	public void setFlavorG(String flavorG) {
		FlavorG = flavorG;
	}
	public String getOutG() {
		return OutG;
	}
	public void setOutG(String outG) {
		OutG = outG;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTogoID() {
		return togoID;
	}
	public void setTogoID(String togoID) {
		this.togoID = togoID;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public void JSonToBean(JSONObject json) throws JSONException{
		foodID = json.getString("TogoID");
		userID = json.getString("UserID");
		value = json.getString("Comment");
		point = json.getInt("Point");
		time = json.getString("PostTime");
		ServerG = json.getString("ServiceGrade");
		FlavorG = json.getString("FlavorGrade");
		OutG = json.getString("SpeedGrade");
		userName = json.getString("UserName");
		userpic = json.getString("userpic");
	}
	
	public String BeanToJSonString(){
		JSONObject json = new JSONObject();
		try {
			json.put("UserID", userID);
			json.put("TogoID", foodID);
			json.put("Comment", value);
			json.put("ServiceGrade", ServerG);
			json.put("FlavorGrade", FlavorG);
			json.put("SpeedGrade", OutG);
			json.put("UserName", userName);
			json.put("foodname", foodName);
			json.put("orderid", odid);
			json.put("point", String.valueOf(point));
			return json.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	}
}
