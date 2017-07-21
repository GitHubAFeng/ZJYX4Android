package com.ihangjing.ZJYXForAndroid;

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
import android.widget.ListView;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;

public class ShopTypeList extends HjActivity {
	private static final String TAG = "ShopTypeList";
	ProgressDialog progressDialog = null;
	ListView areaListView = null;

	ListViewAdapter mSchedule = null;

	private UIHandler hander = new UIHandler();

	private int pageindex = 1;

	private TextView titleTextView;
	private Button backButton;

	private void initUI() {
		setContentView(R.layout.shoptypelist);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("商家分类");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		areaListView = (ListView) ShopTypeList.this
				.findViewById(R.id.shoptype_listview);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();


		// 加载数据 开始加载页码为1
		getDataFromServer(this, pageindex, true, true);

		// 添加listview的点击事件 点击后进入详细页面 或者执行相应的操作
		areaListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = new Intent(ShopTypeList.this, FsgShopList.class);
				intent.putExtra("shoptype", String.valueOf(position));
				startActivity(intent);

			}
		});
	}

	private class UIHandler extends Handler {
		static final int DO_WITH_DATA = 0; // 定义消息类型
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

				return;
			}
			case DO_WITH_DATA: {
				OtherManager.Toast(ShopTypeList.this, "获取数据成功");
				return;
			}
			}
		}
	}

	// paramInt 是页码
	private void getDataFromServer(Context paramContext, int paramInt,
			boolean paramBoolean1, boolean paramBoolean2) {

		// 初始化数据
		Message message = new Message();
		message.what = UIHandler.SET_AREA_LIST;
		hander.sendMessage(message);

		Log.v(TAG, "hander.sendMessage(SET_SHOPTYPE_LIST)");
	}

	// 列表项包含的组件
	static class ViewHolderEdit {
		TextView shopnameTextView;
	}

	// 自定义adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		@Override
		public int getCount() {
			return 5;
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

			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) ShopTypeList.this
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

			String typeString = "";
			if (position == 0) {
				typeString = "推荐商家";
			} 
			else if (position == 1) {
				typeString = "商务套餐";
			} else if (position == 2) {
				typeString = "中式炒菜";
			} else if (position == 3) {
				typeString = "异国风味";
			} else if (position == 4) {
				typeString = "甜品小吃";
			} 
			
			viewHolder.shopnameTextView.setText(typeString);

			return convertView;
		}
	}
}
