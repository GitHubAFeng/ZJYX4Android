package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ihangjing.Model.GiftInfo;
import com.ihangjing.Model.GiftListModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class GiftList extends HjActivity implements HttpRequestListener {
	private static final String TAG = "Skill";
	ProgressDialog progressDialog = null;
	ListView cityListView = null;
	ListViewAdapter mSchedule = null;
	private GiftListModel hotareaListModel;
	private UIHandler hander = new UIHandler();
	private int pageindex = 1;
	private int pagesize = 20;
	private int total;
	// 楼宇编号
	// private String mycircleid;
	private TextView titleTextView;
	private Button backButton;
	private TextView rButton;
	// 加载的控件
	// private View footView;// 页脚 加载更多数据的View
	private String sortid = "0";
	private String subsortid = "0";
	private HttpManager localHttpManager;
	protected boolean isReadData;

	private void initUI() {
		setContentView(R.layout.skilllayout);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText(getResources().getString(R.string.gift_list_title)/*"礼品兑换"*/);

		backButton = (Button) findViewById(R.id.title_bar_back_btn);
		//backButton.setVisibility(View.GONE);
		rButton = (Button) findViewById(R.id.title_bar_right_btn);
		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		/*rButton = (TextView) findViewById(R.id.title_bar_right_btn);
		rButton.setText("分类");
		rButton.setVisibility(View.GONE);
		rButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				GiftTypeList dialog = new GiftTypeList(GiftList.this,
						app.mGiftTypeList, new GiftTypeList.GetID() {
							@Override
							public void getID(String pID, String cID) {
								// TODO Auto-generated method stub
								sortid = pID;
								subsortid = cID;
								pageindex = 1;
								GetData();
							}
						});
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
		});*/
		cityListView = (ListView) GiftList.this
				.findViewById(R.id.building_listview);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();



		hotareaListModel = new GiftListModel();

		// 加载数据 开始加载页码为1
		// /getDataFromServer(this, pageindex, true, true);

		// 添加listview的点击事件 点击后进入详细页面 或者执行相应的操作
		cityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (hotareaListModel == null) {
					return;
				}

				GiftInfo localShopModel = hotareaListModel.list.get(position);

				Intent intent = new Intent(GiftList.this, GiftDedail.class);
				intent.putExtra("id", localShopModel.getgID());

				startActivity(intent);
			}
		});

		cityListView.setOnScrollListener(listener);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		/*if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent localIntent = new Intent("com.ihangjing.common.tap0");
			app.sendBroadcast(localIntent);

		} else {
			boolean bool = true;
			bool = super.onKeyDown(keyCode, paramKeyEvent);
			return bool;
		}*/

		return super.onKeyDown(keyCode, paramKeyEvent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 加载数据
		if(hotareaListModel.list.size() == 0){
			progressDialog = ProgressDialog.show(GiftList.this, "", getResources().getString(R.string.public_get_data_notice)/*"数据加载中，请稍后..."*/);
			progressDialog.setCancelable(true);
			pageindex = 1;
			GetData();
		}
		
	}

	private class UIHandler extends Handler {
		static final int NET_ERROR = -1; // 定义消息类型
		static final int SET_AREA_LIST = 1; // 下载数据成功

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_AREA_LIST: {
				// 隐藏加载控件
				if (mSchedule == null) {
					mSchedule = new ListViewAdapter();
					cityListView.setAdapter(mSchedule);
				}
				mSchedule.notifyDataSetChanged();
				progressDialog.dismiss();

				return;
			}

			case NET_ERROR: {
				OtherManager.Toast(GiftList.this, getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误，请稍后再试！"*/);
				return;
			}

			}
		}
	}

	// 通过网络获取数据 获取完成后发送消息
	public void GetData() {
		isReadData = true;
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pagesize", String.valueOf(pagesize));
		localHashMap.put("pageindex", String.valueOf(pageindex));
		localHashMap.put("sortid", sortid);
		localHashMap.put("subsortid", subsortid);

		localHttpManager = new HttpManager(GiftList.this, GiftList.this,
				"Android/GetGiftList.aspx?", localHashMap, 2, 0);
		localHttpManager.getRequeest();

	}

	// 列表项包含的组件
	static class ViewHolderEdit {
		TextView shopnameTextView;
		RelativeLayout ImageView;// 图片
		// TextView timestrTextView;// 时间串
		TextView statestrTextView;// 状态字串，burronn中的内容
		// TextView foodnameTextView;// 菜品名称

		TextView giftType;

		TextView oldpriceTextView;// 原价
		TextView newpriceTextView;// 现价
		TextView sendfreeTextView;// 配送费
		// TextView cnumTextView;// 剩余数量

	}

	// 自定义adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		@Override
		public int getCount() {
			return GiftList.this.hotareaListModel.list.size();
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
			GiftInfo localSimpleOrderModel;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) GiftList.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.skillitem, null);

				viewHolder = new ViewHolderEdit();

				viewHolder.shopnameTextView = (TextView) convertView
						.findViewById(R.id.timestr_tv);
				/*viewHolder.giftType = (TextView) convertView
						.findViewById(R.id.tv_gift_type);*/

				viewHolder.ImageView = (RelativeLayout) convertView
						.findViewById(R.id.itemImage);
				/*
				 * viewHolder.ImageView.setOnClickListener(new OnClickListener()
				 * {
				 * 
				 * @Override public void onClick(View v) { // TODO
				 * Auto-generated method stub String urlString =
				 * (String)v.getTag(); Bitmap img =
				 * app.mLoadImage.getBitmap(urlString); ImagePopViewDialog dialog
				 * = new ImagePopViewDialog(GiftList.this, img); dialog.show();
				 * } });
				 */
				viewHolder.oldpriceTextView = (TextView) convertView
						.findViewById(R.id.oldpricetv);
				viewHolder.newpriceTextView = (TextView) convertView
						.findViewById(R.id.newpricetv);

				viewHolder.sendfreeTextView = (TextView) convertView
						.findViewById(R.id.sendfreetv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderEdit) convertView.getTag();
			}

			localSimpleOrderModel = GiftList.this.hotareaListModel.list
					.get(position);

			viewHolder.shopnameTextView.setText(localSimpleOrderModel
					.getgName());
			/*if (localSimpleOrderModel.getType() == 1) {
				viewHolder.giftType.setText("现金券");
			} else {
				viewHolder.giftType.setText("");
			}*/
			viewHolder.oldpriceTextView.setText(getResources().getString(R.string.gift_list_req_point)//"兑换积分："
					+ localSimpleOrderModel.getNeedPoint());

			viewHolder.newpriceTextView.setText(getResources().getString(R.string.gift_list_count)//"剩余数量："
					+ localSimpleOrderModel.getStocks());
			viewHolder.sendfreeTextView.setText(getResources().getString(R.string.public_moeny_0)
					+ localSimpleOrderModel.getPrice());

			viewHolder.ImageView.setTag(localSimpleOrderModel.getPic());
			if (!(localSimpleOrderModel.getPic().trim().length() == 0)) {
				app.mLoadImage.addTask(localSimpleOrderModel.getPic(),
						viewHolder.ImageView, R.drawable.nopic_skill);
			}

			return convertView;
		}
	}

	/**
	 * listview滚动到最一页加载数据
	 */
	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			/*
			 * if (view.getLastVisiblePosition() == view.getCount() - 1) {
			 * 
			 * // 如果不是最后一页,就获取下一页数据,显示加载控件 if (total > pageindex) { pageindex =
			 * pageindex + 1; // 表示已经是第一页后的加载数据 GetData();
			 * 
			 * } else { // 到最后一页隐藏
			 * 
			 * } }
			 */
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem + visibleItemCount >= totalItemCount) {
				if (!isReadData && total > pageindex) {
					isReadData = true;
					pageindex = pageindex + 1;
					// 表示已经是第一页后的加载数据
					GetData();

				}
			}
			// 滚动完成后调用
			app.mLoadImage.doTask();
		}
	};

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt == 260) {
			JSONObject json = null;
			try {
				json = new JSONObject((String) paramObject);
				JSONArray array;// = new JSONArray();

				this.pageindex = Integer.parseInt(json.getString("page"));
				if (pageindex == 1) {
					this.hotareaListModel.list.clear();
				}
				this.total = Integer.parseInt(json.getString("total"));

				array = json.getJSONArray("datalist");

				hotareaListModel.setPage(pageindex);
				hotareaListModel.setTotal(total);

				GiftInfo models;// = new GiftInfo[array.length()];

				for (int i = 0; i < array.length(); i++) {
					JSONObject model = array.getJSONObject(i);
					models = new GiftInfo(model);
					this.hotareaListModel.list.add(models);
				}
				message.what = UIHandler.SET_AREA_LIST;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}

		} else {
			message.what = UIHandler.NET_ERROR;
		}
		isReadData = false;
		hander.sendMessage(message);
	}
}
