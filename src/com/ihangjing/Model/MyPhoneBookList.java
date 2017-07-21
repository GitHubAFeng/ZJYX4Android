package com.ihangjing.Model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPhoneBookList {
	public ArrayList<MyPhoneAddressBook> list;
	public int page;
	public int total;
	public MyPhoneBookList(){
		list = new ArrayList<MyPhoneAddressBook>();
	}
	
	public void jsonToBean(JSONObject json) throws JSONException{
		page = json.getInt("page");
		total = json.getInt("total");
		JSONArray array = json.getJSONArray("datalist");
		if (page == 1) {
			list.clear();
		}
		int m = array.length();
		
	}
}
