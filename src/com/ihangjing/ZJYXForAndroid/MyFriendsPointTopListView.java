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
import com.ihangjing.Model.MyPoint;
import com.ihangjing.Model.MyPointsListModel;
import com.ihangjing.Model.PointTopModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class MyFriendsPointTopListView extends HjActivity implements
		HttpRequestListener {

	protected Animation Animation_toLeft = null;
	protected Animation Animation_toRight = null;
	private ListView ListView;
	private ArrayList<PointTopModel> pointList;
	
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
		
		pointList = new ArrayList<PointTopModel>();
		

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
		


		inflater = LayoutInflater.from(MyFriendsPointTopListView.this);

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
		getPointsFromServer();
		
	}

	

	@Override
	protected void onResume() {
		super.onResume();
		
		
	}

	
	
	public void getPointsFromServer() {
		dialogStr = "加载中......";
		showDialog(NET_DIALOG);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("type", String.valueOf(type));
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyFriendsPointTopListView.this,
				MyFriendsPointTopListView.this, "/Android/Gethistorypoinpoint.aspx?",
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
					JSONArray array = json.getJSONArray("list");
					int m = array.length();
					for (int i = 0; i < m; i++) {
						PointTopModel model = new PointTopModel(array.getJSONObject(i));
						pointList.add(model);
					}
					
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
				Toast.makeText(MyFriendsPointTopListView.this, errMsg, 15).show();
				break;
			case ADD_SCUESS:
				Toast.makeText(MyFriendsPointTopListView.this, "添加好友成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				break;
			case BIND_COUPON_SCUESS:
				Toast.makeText(MyFriendsPointTopListView.this, "绑定优惠券成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				break;
			case BIND_COUPON_FAILD:
				Toast.makeText(MyFriendsPointTopListView.this, errMsg, 15).show();
				break;
			case NET_ERROR:
				Toast.makeText(MyFriendsPointTopListView.this, "网络或数据错误，请稍后再试！", 10)
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
		
		TextView tvTop;
		RelativeLayout rlImag;
		TextView tvValue;
	}

	class PointTopListViewAdapter extends BaseAdapter {
		PointTopListViewAdapter() {
		}

		@Override
		public int getCount() {
			int len = pointList.size();
			
			return len;
		}

		@Override
		public Object getItem(int paramInt) {
			return pointList.get(paramInt);
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

			PointTopView listHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.pont_top_item, null);
				listHolder = new PointTopView();
				listHolder.tvTop = (TextView)convertView.findViewById(R.id.tv_top);
				listHolder.tvValue = (TextView)convertView.findViewById(R.id.tv_value);
				listHolder.rlImag = (RelativeLayout)convertView.findViewById(R.id.rl_icon);

				convertView.setTag(listHolder);
			} else {
				listHolder = (PointTopView) convertView.getTag();
			}

			PointTopModel model = pointList.get(paramInt);
			

			if (model != null) {
				listHolder.tvTop.setText(String.format("TOP%d", paramInt + 1));
				if (type == 0) {
					listHolder.tvValue.setText(String.format("花马币记录：%s", model.getHistoryPoint()));
				}else{
					listHolder.tvValue.setText(String.format("公益积分：%s", model.getPublicPoint()));
				}
				listHolder.rlImag.setTag(model.getFriendICON());
				if (model.getFriendICON() != null
						&& !model.getFriendICON().equals("")) {
					app.mLoadImage.addTask(model.getFriendICON(), listHolder.rlImag,
							R.drawable.friend_default);
				} else {
					listHolder.rlImag
							.setBackgroundResource(R.drawable.friend_default);
				}
			}

			return convertView;
		}
	}

}
