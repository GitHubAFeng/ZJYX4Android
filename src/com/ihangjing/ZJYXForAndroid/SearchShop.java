package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.FoodTypeModel;
import com.ihangjing.Model.ShopTypeInfo;
import com.ihangjing.Model.ShopTypeListModel;
import com.ihangjing.ZJYXForAndroid.ShopDetail.FoodTypeListHolder;
import com.ihangjing.common.HjActivity;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class SearchShop extends HjActivity implements HttpRequestListener{
	
	private static final int GET_SHOP_TYPE = 1;
	private TextView tvClose;
	private TextView tvOpen;
	private EditText etSearch;
	private Button btnSearch;
	private GridView gvShopType;
	private HttpManager localHttpManager;
	//private ShopTypeListModel shopTypeListModel;
	UIHandler handler = new UIHandler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchshop);
		OnClickListener viewClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent localIntent1;
				switch (v.getId()) {
				case R.id.tv_close:
					localIntent1 = new Intent(SearchShop.this, FsgShopList.class);
					localIntent1.putExtra("isRem", 4);//查看休息中的商家
					break;
				case R.id.tv_open:
					localIntent1 = new Intent(SearchShop.this, FsgShopList.class);
					localIntent1.putExtra("isRem", 5);//查看营业中的商家
					break;
				case R.id.searchshop_btn_search:
					
				default:
					if (etSearch.getText().toString().length() < 1) {
						etSearch.setError(getResources().getString(R.string.search_notice)/*"请输入搜索条件"*/);
						Toast.makeText(SearchShop.this, getResources().getString(R.string.search_notice)/*"请输入搜索条件！"*/, 15).show();
						return;
					}
					localIntent1 = new Intent(SearchShop.this, FsgShopList.class);
					localIntent1.putExtra("isRem", 3);//搜索商家
					localIntent1.putExtra("searchKey", etSearch.getText().toString());//是否推荐
					
					break;
					
				}
				startActivity(localIntent1);
			}
		};
		Button btnBack = (Button)findViewById(R.id.title_bar_back_btn);
		btnBack.setVisibility(View.GONE);
		TextView tvTitle = (TextView)findViewById(R.id.title_bar_content_tv);
		tvTitle.setText(getResources().getString(R.string.search_title));//"搜索商家");
		tvClose = (TextView)findViewById(R.id.tv_close);
		tvClose.setOnClickListener(viewClick);
		tvOpen = (TextView)findViewById(R.id.tv_open);
		tvOpen.setOnClickListener(viewClick);
		etSearch = (EditText)findViewById(R.id.searchshop_keyword);
		btnSearch = (Button)findViewById(R.id.searchshop_btn_search);
		btnSearch.setOnClickListener(viewClick);
		gvShopType = (GridView)findViewById(R.id.gridview);
		gvShopType.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ShopTypeInfo models = app.shopTypeListModel.list.get(position);
				Intent localIntent1 = new Intent(SearchShop.this, FsgShopList.class);
				localIntent1.putExtra("isRem", 6);//分类商家
				localIntent1.putExtra("shopTypeID", models.SortID);
				localIntent1.putExtra("shopTypeName", models.SortName);
				startActivity(localIntent1);
			}
		});
		//shopTypeListModel = new ShopTypeListModel();
		if (app.shopTypeListModel.list.size() == 0) {
			getshoptype();
		}else{
			Message message = new Message();
			message.what = UIHandler.OVER_GET_SHOPTYPE;
			handler.sendMessage(message);
		}
		
	}
	@Override
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK) {
			
			Intent localIntent = new Intent("com.ihangjing.common.tap0");
			app.sendBroadcast(localIntent);

			
			
			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}
	private void getshoptype() {
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pid", "0");
		localHashMap.put("languageType", app.language);

		localHttpManager = new HttpManager(SearchShop.this,
				SearchShop.this, "Android/GetShopTypeList.aspx?",
				localHashMap, 1, GET_SHOP_TYPE);
		localHttpManager.postRequest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt == 260) {
			try {
			switch (tag) {
			case GET_SHOP_TYPE:// 获取分类数据
				String jsonString = (String) paramObject;
				
				// 开始解析接收到的json数据
				JSONArray array = new JSONArray();

				JSONObject json = null;
				
					json = new JSONObject(jsonString);

					

					array = json.getJSONArray("datalist");

					ShopTypeInfo models;// = new ShopTypeInfo[array.length()];

					/*models = new ShopTypeInfo();
					models.SortName = getResources().getString(R.string.public_all_type);//"全部分类";
					models.SortID = "0";
					app.shopTypeListModel.list.add(models);*/
					int j = 0;
					for (int i = 0; i < array.length(); i++) {
						JSONObject model = array.getJSONObject(i);
						models = new ShopTypeInfo(model);
						app.shopTypeListModel.list.add(models);
					}
					message.what = UIHandler.OVER_GET_SHOPTYPE;
				

				// 接收到商家列表json
				// 通知activity进行列表更新绑定

				break;
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}
		}
		handler.sendMessage(message);
		
	}
	private class UIHandler extends Handler {
		static final int OVER_GET_SHOPTYPE = 1;// 获取分类完成
		static final int NET_ERROR = -2;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NET_ERROR:
				Toast.makeText(SearchShop.this, getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误，请稍后再试！"*/, 15).show();
				break;
			case OVER_GET_SHOPTYPE:
				gvShopType.setAdapter(new ShopTypeListAdapter());
				break;
			default:
				break;
			}
		}
	}
	
	class ShopTypeListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		ShopTypeListAdapter() {
		}

		@Override
		public int getCount() {

			int size = app.shopTypeListModel.list.size();
			return size;

		}

		@Override
		public Object getItem(int paramInt) {
			/*
			 * if (paramInt == 0) { return foodTypeModel; }
			 */
			return null;//app.shopTypeListModel.list.get(paramInt);

		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int position, View convertView,
				ViewGroup paramViewGroup) {
			FoodTypeListHolder viewHolder;
			FoodTypeModel localFoodModel = new FoodTypeModel();

			TextView tvtypeName;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) SearchShop.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.search_shop_type_tem, null);

				
				tvtypeName = (TextView) convertView
						.findViewById(R.id.tv_typename);

				convertView.setTag(tvtypeName);
			} else {
				tvtypeName = (TextView) convertView.getTag();
			}
			ShopTypeInfo models = app.shopTypeListModel.list.get(position);
			tvtypeName.setText(models.getSortName());
			
			return convertView;
		}
	}
}
