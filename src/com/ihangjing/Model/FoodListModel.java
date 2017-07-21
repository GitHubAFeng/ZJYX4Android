package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FoodListModel {
	public ArrayList<FoodModel> list;
	private int page;
	private int total;

	public FoodListModel() {
		this.list = new ArrayList<FoodModel>();
	}

	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("page", String.valueOf(this.page));

			localJSONObject1.put("total", String.valueOf(this.total));

			JSONArray localJSONArray1 = new JSONArray();
			Iterator<FoodModel> localIterator = this.list.iterator();

			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("foodlist", localJSONArray1);
					String value = localJSONObject1.toString();
					value = value.replace("\\\"", "\"");
					value = value.replace("\"{", "{");
					value = value.replace("}\"", "}");
					value = value.replace("\\", "");
					return value;
				}
				localJSONArray1.put(localIterator.next()
						.beanToString());
			}
		} catch (JSONException localJSONException) {
			return "";
		}
	}


	public int getPage() {
		return this.page;
	}

	public int getTotal() {
		return this.total;
	}

	public void setPage(int paramInt) {
		this.page = paramInt;
	}

	public void setTotal(int paramInt) {
		this.total = paramInt;
	}

	public FoodListModel stringToBean(String paramString) {
		if ((paramString == null)
				|| ((paramString != null) && (paramString.equals("")))) {
			return null;
		}

		try {
			this.list.clear();
			JSONObject localJSONObject = new JSONObject(paramString);
			this.page = localJSONObject.getInt("page");
			this.total = localJSONObject.getInt("total");
			
			JSONArray localJSONArray = localJSONObject.getJSONArray("foodlist");
			int k = 0;
			
			//ѭ����ȡ���еĲ�Ʒ��Ϣ
			int m = localJSONArray.length();
			while (true) {
				
				if (k >= m) {
					break;
				}
				FoodModel localShopBean1 = new FoodModel();
				localShopBean1.JsonToBean(localJSONArray.getJSONObject(k), 0);
				this.list.add(localShopBean1);
				k += 1;
			}
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this;
	}
	//cache ��ȡ��jsonר��
	public void cacheStringToBean(String value) throws JSONException{
		JSONObject localJSONObject = new JSONObject(value);
		this.page = localJSONObject.getInt("page");
		this.total = localJSONObject.getInt("total");
		
		JSONArray localJSONArray = localJSONObject.getJSONArray("foodlist");
		int k = 0;
		
		//ѭ����ȡ���еĲ�Ʒ��Ϣ
		int m = localJSONArray.length();
		while (true) {
			
			if (k >= m) {
				break;
			}
			FoodModel localShopBean1 = new FoodModel();
			localShopBean1.JsonToBean(localJSONArray.getJSONObject(k), 1);
			this.list.add(localShopBean1);
			k += 1;
		}
	}
	//HJScorllFood����ר��
	public void JSonToBean(JSONObject localJSONObject) throws JSONException {
		// TODO Auto-generated method stub
		ArrayList<FoodModel> listCache = new ArrayList<FoodModel>();
		this.page = localJSONObject.getInt("page");
		this.total = localJSONObject.getInt("total");
		
		JSONArray localJSONArray = localJSONObject.getJSONArray("foodlist");
		int k = 0;
		int m = localJSONArray.length();
		//ѭ����ȡ���еĲ�Ʒ��Ϣ
		while (true) {
			if (k >= m) {
				break;
			}
			FoodModel localShopBean1 = new FoodModel();
			localShopBean1.JsonToBean(localJSONArray.getJSONObject(k), 0);
			listCache.add(localShopBean1);
			k += 1;
		}
		
		m = listCache.size();
		int n = list.size();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (listCache.get(i).getFoodId() == list.get(j).getFoodId()) {// �����л���
					listCache.get(i).setImageLocalPath(list.get(j).getImageLocalPath());
					if (listCache.get(i).getImageNetPath().equals("")
							|| listCache.get(i).getImageNetPath()
									.equals(list.get(j).getImageNetPath())) {// �µ����ص�ַΪ�գ����ߺ;ɵ�ַ��ͬ���ǲ���Ҫ����
						//listCache.get(i).isDowload = false;
					}
					//Ҫ��Ҫɾ���Ѿ��ȶԹ����أ�ɾ�����Ƿ�ӡ��UI����������
					break;
				}
			}
			
		}
		list = listCache;
	}
}
