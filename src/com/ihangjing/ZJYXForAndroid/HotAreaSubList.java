package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ihangjing.Model.HotAreaModel;
import com.ihangjing.Model.HotAreaModelList;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;

//APP/Android/GetAreaSubList.aspx?aid=101000000000

//CopyRight (c) 2009-2012 HangJing Teconology. All Rights Reserved.
//zjf@ihangjing.com
//2012-02-14
//获取贰级区域 回龙观 天通苑 ...

public class HotAreaSubList extends HjActivity  {
	private static final String TAG = "HotAreaSubList";
	ProgressDialog progressDialog = null;
	ListView areaListView = null;

	ListViewAdapter mSchedule = null;

	private HotAreaModelList hotareaListModel;

	private UIHandler hander = new UIHandler();

	private int pageindex = 1;
	private int total;

	private TextView titleTextView;
	private Button backButton;

	private String AId;
	
	private void initUI() {
		setContentView(R.layout.hotarea_list);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("热点区域");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		areaListView = (ListView) HotAreaSubList.this
				.findViewById(R.id.hotarea_listview);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		
		
		hotareaListModel = new HotAreaModelList();
		
		if (OtherManager.DEBUG) {
			Log.v(TAG, "initUI end ");
		}

		// 获取传输过来的商家编号
		Bundle extras = getIntent().getExtras();
	    AId = extras.getString("aid");
		Log.v(TAG, String.valueOf(AId));
		
		// 加载数据
		progressDialog = ProgressDialog.show(HotAreaSubList.this, "",
				"数据加载中，请稍后...");
		progressDialog.setCancelable(true);
		
		// 加载数据 开始加载页码为1
		getDataFromServer(this, pageindex, true, true);

		// 添加listview的点击事件 点击后进入详细页面 或者执行相应的操作
		areaListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// if (position == 0) // return;
				Log.v(TAG, "setOnItemClickListener position:" + position);

				if (HotAreaSubList.this.hotareaListModel == null) {
					return;
				}

				// 得到被点击的热点区域的model
				HotAreaModel localShopModel = null;
				if ((HotAreaSubList.this.hotareaListModel.list.size() > 0)
						&& (HotAreaSubList.this.hotareaListModel.list != null)) {
					ArrayList<HotAreaModel> localArrayList = HotAreaSubList.this.hotareaListModel.list; // int																		// 1;
					localShopModel = localArrayList
							.get(position);
				}

				// 跳转到商家列表页面
				Intent intent = new Intent(HotAreaSubList.this, FsgShopList.class);
				intent.putExtra("aid", localShopModel.getUnid());//直接传输unid
				startActivity(intent);
			}
		});
	}

	private class UIHandler extends Handler {
		static final int DO_WITH_DATA = 0;  // 定义消息类型
		static final int SET_AREA_LIST = 1; // 下载数据成功

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_AREA_LIST: {
				
				mSchedule = new ListViewAdapter();
				
				areaListView.setAdapter(mSchedule);

				if (OtherManager.DEBUG) {
					Log.v(TAG, "handleMessage DO_WITH_DATA 对listview进行更新");
				}
				progressDialog.dismiss();
				
				return;
			}
			case DO_WITH_DATA: {
				OtherManager.Toast(HotAreaSubList.this, "获取数据成功");
				return;
			}

			}
		}
	}

	// paramInt 是页码
	private void getDataFromServer(Context paramContext, int paramInt,
			boolean paramBoolean1, boolean paramBoolean2) {

		pageindex = paramInt;
		if (OtherManager.DEBUG) {
			// 创建线程并启动
			Log.v(TAG, "new LoadDataThread().start()");
		}

		new LoadDataThread().start();
	}

	private class LoadDataThread extends Thread {
		@Override
		public void run() {

			Log.v(TAG, "向服务器请求数据...");

			try {
				GetData();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.v(TAG, "请求数据成功");

			Message message = new Message();
			message.what = UIHandler.SET_AREA_LIST;
			hander.sendMessage(message); // 发送消息给Handler
			Log.v(TAG, "hander.sendMessage(SET_ORDER_LIST)");
		}
	}

	// 通过网络获取数据 获取完成后发送消息
	public void GetData() throws HttpException, JSONException {

		Log.v(TAG, "连接网络获取数据开始");

		NetManager localNetManager = NetManager.getInstance(HotAreaSubList.this
				.getApplicationContext());

		String isonsString = "";
		String url = "http://www.fanshigang.com/APP/Android/GetAreaSubList.aspx?aid="+AId;

		if (OtherManager.DEBUG) {
			Log.v(TAG, "参数准备完毕:" + url);
		}

		try {
			isonsString = localNetManager.dogetAsString(url);
			Log.v(TAG, isonsString);
		} catch (Exception e) {
			Log.v(TAG, e.getMessage());
		}

		JSONObject json = null;

		json = new JSONObject(isonsString);

		JSONArray array = new JSONArray();
		Log.v(TAG, "new JSONObject");

		this.pageindex = Integer.parseInt(json.getString("page"));
		this.total = Integer.parseInt(json.getString("total"));

		try {
			array = json.getJSONArray("arealist");
		} catch (JSONException jsone) {
			throw new HttpException(jsone.getMessage() + "::" + isonsString,
					jsone);
		}
		
		hotareaListModel.setPage(pageindex);
		hotareaListModel.setTotal(total);
		
		HotAreaModel[] models = new HotAreaModel[array.length()];
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject model = array.getJSONObject(i);
			models[i] = new HotAreaModel(model);
			this.hotareaListModel.list.add(models[i]);
		}
	}

	// 列表项包含的组件
	static class ViewHolderEdit {
		TextView shopnameTextView;
		TextView addressTextView;
		TextView orderidTextView;
		TextView ordertimeTextView;
		ImageView orderstatusImageView;
		int dataidView;
	}

	// 自定义adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		@Override
		public int getCount() {
			return HotAreaSubList.this.hotareaListModel.list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		public void toggle(int position) {
			this.notifyDataSetChanged();// date changed and we should refresh
										// the view
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolderEdit viewHolder;
			HotAreaModel localSimpleOrderModel;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) HotAreaSubList.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.textview, null);

				viewHolder = new ViewHolderEdit();

				viewHolder.shopnameTextView = (TextView) convertView
						.findViewById(R.id.textview);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderEdit) convertView.getTag();
			}

			localSimpleOrderModel = HotAreaSubList.this.hotareaListModel.list
					.get(position);

			viewHolder.shopnameTextView
					.setText(localSimpleOrderModel.getName());

			return convertView;
		}
	}
}
