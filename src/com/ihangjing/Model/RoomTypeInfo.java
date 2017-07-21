package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ihangjing.common.OtherManager;

/**
 * �̼ҷ���
 * @author Administrator
 *
 */
public class RoomTypeInfo {
	public String SortID = "";
	public String SortName = "";
	public String JOrder = "";
	public String SortPic;
	public int tabtype;
	public float minfee;
	

	public RoomTypeInfo stringToBean(JSONObject localJSONObject) {
		try {
			//JSONObject localJSONObject = new JSONObject(paramString);
			this.SortID = localJSONObject.getString("TId");
			this.SortName = localJSONObject.getString("tabName");
			this.JOrder = localJSONObject.getString("seats");//����
			tabtype = localJSONObject.getInt("tabtype");//0��ʾ������1��ʾ����
			minfee = (float)localJSONObject.getDouble("minfee");//�������
			SortPic = localJSONObject.getString("picture");
			return this;
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("TId", this.SortID);
			localJSONObject1.put("tabName", this.SortName);
			localJSONObject1.put("JOrder", this.JOrder);
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			while (true)
				localJSONException.printStackTrace();
		}
	}

	public String getSortPic() {
		return SortPic;
	}

	public void setSortPic(String sortPic) {
		SortPic = sortPic;
	}

	public RoomTypeInfo()
	{
		
	}
	// Jsonת����model
	public RoomTypeInfo(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setSortID(localJSONObject.getString("SortID"));
			setSortName(localJSONObject.getString("SortName"));
			setJOrder(localJSONObject.getString("JOrder"));
			SortPic = localJSONObject.getString("SortPic");
		} catch (JSONException localJSONException) {
			if(OtherManager.DEBUG)
			{
				Log.v("NewsModel", "catch");
			}
			localJSONException.printStackTrace();
		}
	}

	public String getSortID() {
		return SortID;
	}

	public void setSortID(String sortID) {
		SortID = sortID;
	}

	public String getSortName() {
		return SortName;
	}

	public void setSortName(String sortName) {
		SortName = sortName;
	}

	public String getJOrder() {
		return JOrder;
	}

	public void setJOrder(String jOrder) {
		JOrder = jOrder;
	}
	
}
