package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.MyCouponModel;
import com.ihangjing.Model.MyCouponsListModel;
import com.ihangjing.Model.MyFriendBuy;
import com.ihangjing.Model.MyFriendBuyListModel;
import com.ihangjing.Model.MyPoint;
import com.ihangjing.Model.MyPointsListModel;
import com.ihangjing.Model.PointTopModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class MyFriendsBuysListView extends HjActivity implements
		HttpRequestListener {

	protected Animation Animation_toLeft = null;
	protected Animation Animation_toRight = null;
	private ListView ListView;
	private MyFriendBuyListModel buyList;
	
	int pageindex;
	private HttpManager localHttpManager;
	public LayoutInflater inflater;
	PointTopListViewAdapter listViewAdapter;
	
	private String dialogStr;
	private UIHandler mHandler;
	static int NET_DIALOG = 322;
	
	private String errMsg;
	
	protected Integer OptIndex;
	private int type;
	public View moreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_coupons);
		app.geiverCoupon = false;
		mHandler = new UIHandler();
		ListView = (ListView) findViewById(R.id.lv_data);
		
		Bundle extern = getIntent().getExtras();
		if (extern != null) {
			type = extern.getInt("type");
		}
		
		buyList = new MyFriendBuyListModel();
		

		listViewAdapter = new PointTopListViewAdapter();
		ListView.setAdapter(listViewAdapter);

		
		ListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (app.mLoadImage != null) {
					app.mLoadImage.doTask();
				}
				
			}
		});
		inflater = LayoutInflater.from(MyFriendsBuysListView.this);
		moreView = inflater.inflate(R.layout.more_friends_from_net, null);
		TextView tvMore = (TextView) moreView
				.findViewById(R.id.tv_more_friends);
		tvMore.setText("加载更多数据。。。");
		tvMore.setTextColor(Color.rgb(0, 0, 0));
		tvMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {//加载更多数据
				// TODO Auto-generated method stub
				pageindex++;
				getFriendBuyFromServer();
			}
		});

		inflater = LayoutInflater.from(MyFriendsBuysListView.this);

		Button btnBack = (Button) findViewById(R.id.title_bar_back_btn);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView tvRight = (TextView) findViewById(R.id.title_bar_right_btn);
		
		
		tvRight.setVisibility(View.GONE);
		pageindex = 1;
		getFriendBuyFromServer();
		
	}

	

	@Override
	protected void onResume() {
		super.onResume();
		
		
	}

	
	
	public void getFriendBuyFromServer() {
		dialogStr = "加载中......";
		showDialog(NET_DIALOG);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(pageindex));
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyFriendsBuysListView.this,
				MyFriendsBuysListView.this, "/Android/mycircle.aspx?",
				localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}

	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
		}
		return super.onKeyDown(keyCode, paramKeyEvent);
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			String value = (String) paramObject;

			try {
				JSONObject json = new JSONObject(value);
				switch (tag) {
				case 1:
					// MyFriendsList list =new MyFriendsList();
					buyList.JSonToBean(json);
					
					msg.what = UIHandler.READ_DATA_OK;
					break;
				case 2:
					
					break;
				case 3:
					
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what = UIHandler.NET_ERROR;
			}
		} else {
			msg.what = UIHandler.NET_ERROR;
		}
		mHandler.sendMessage(msg);
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		
		if (paramInt == NET_DIALOG) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);

			DialogInterface.OnKeyListener okl = new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface paramDialogInterface,
						int paramInt, KeyEvent paramKeyEvent) {
					// 取消加载
					if ((paramInt == 4) && (localHttpManager != null)) {
						localHttpManager.cancelHttpRequest();
					}
					return false;
				}
			};

			dialog.setOnKeyListener(okl);

			return dialog;
		}
		return dialog;
	}

	private class UIHandler extends Handler {
		public static final int BIND_COUPON_FAILD = -1;
		public static final int BIND_COUPON_SCUESS = 1;
		public static final int ADD_FAILED = -3;
		public static final int ADD_SCUESS = 4;
		public static final int NET_ERROR = -2;
		public static final int SEARCH_SCUESS = 3;
		public static final int READ_DATA_OK = 2;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(NET_DIALOG);
			switch (msg.what) {
			case ADD_FAILED:
				Toast.makeText(MyFriendsBuysListView.this, errMsg, 15).show();
				break;
			case ADD_SCUESS:
				Toast.makeText(MyFriendsBuysListView.this, "添加好友成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				break;
			case BIND_COUPON_SCUESS:
				Toast.makeText(MyFriendsBuysListView.this, "绑定优惠券成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				break;
			case BIND_COUPON_FAILD:
				Toast.makeText(MyFriendsBuysListView.this, errMsg, 15).show();
				break;
			case NET_ERROR:
				Toast.makeText(MyFriendsBuysListView.this, "网络或数据错误，请稍后再试！", 10)
						.show();
				break;
			case READ_DATA_OK:
				
				listViewAdapter.notifyDataSetChanged();
				break;
			case SEARCH_SCUESS:
				listViewAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}

	class PointTopView {
		
		TextView tvFrName;
		RelativeLayout rlFrImag;
		RelativeLayout rlFoImag;
		TextView tvFoName;
		TextView tvSendTime;
	}

	class PointTopListViewAdapter extends BaseAdapter {
		PointTopListViewAdapter() {
		}

		@Override
		public int getCount() {
			int len = buyList.list.size();
			if (len == 0) {
				return len;
			}
			if (buyList.page < buyList.total) {
				len++;
			}
			return len;
		}

		@Override
		public Object getItem(int paramInt) {
			if (paramInt < buyList.list.size()) {
				return buyList.list.get(paramInt);
			}
			return null;
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			if (position < 4) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public View getView(int paramInt, View convertView,
				ViewGroup paramViewGroup) {
			if (paramInt == buyList.list.size()) {
				return moreView;
			}
			PointTopView listHolder;
			if (convertView == null || convertView == moreView) {
				convertView = inflater.inflate(R.layout.friends_buy_item, null);
				listHolder = new PointTopView();
				listHolder.tvFrName = (TextView)convertView.findViewById(R.id.tv_value);
				listHolder.tvFoName = (TextView)convertView.findViewById(R.id.tv_food_name);
				listHolder.rlFrImag = (RelativeLayout)convertView.findViewById(R.id.rl_icon);
				listHolder.rlFoImag = (RelativeLayout)convertView.findViewById(R.id.rl_foodicon);
				listHolder.tvSendTime = (TextView)convertView.findViewById(R.id.tv_sendtime);

				convertView.setTag(listHolder);
			} else {
				listHolder = (PointTopView) convertView.getTag();
			}

			MyFriendBuy model = buyList.list.get(paramInt);
			

			if (model != null) {
				listHolder.tvFrName.setText(model.getFriendName());
				listHolder.tvFoName.setText(model.getFoodName());
				listHolder.tvSendTime.setText(model.getSendTime());
				listHolder.rlFrImag.setTag(model.getFriendICON());
				if (model.getFriendICON() != null
						&& !model.getFriendICON().equals("")) {
					app.mLoadImage.addTask(model.getFriendICON(), listHolder.rlFrImag,
							R.drawable.friend_default);
				} else {
					listHolder.rlFrImag
							.setBackgroundResource(R.drawable.friend_default);
				}
				
				listHolder.rlFoImag.setTag(model.getFoodICON());
				if (model.getFriendICON() != null
						&& !model.getFoodICON().equals("")) {
					app.mLoadImage.addTask(model.getFoodICON(), listHolder.rlFoImag,
							R.drawable.icon);
				} else {
					listHolder.rlFrImag
							.setBackgroundResource(R.drawable.icon);
				}
			}

			return convertView;
		}
	}

}
