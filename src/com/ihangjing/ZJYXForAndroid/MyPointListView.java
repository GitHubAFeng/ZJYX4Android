package com.ihangjing.ZJYXForAndroid;

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
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class MyPointListView extends HjActivity implements
		HttpRequestListener {

	protected Animation Animation_toLeft = null;
	protected Animation Animation_toRight = null;
	private ListView ListView;
	private Button btnLookFriendsBuy;
	private MyPointsListModel pointList;
	
	int pageindex;
	private HttpManager localHttpManager;
	public LayoutInflater inflater;
	PointListViewAdapter listViewAdapter;
	View moreView;
	private String dialogStr;
	private UIHandler mHandler;
	private boolean isShowInput;
	static int NET_DIALOG = 322;
	
	private String errMsg;
	
	protected Integer OptIndex;
	private float cartMoney = 0.0f;//当从确定订单界面过来时该值不为0.0f

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_coupons);
		app.geiverCoupon = false;
		mHandler = new UIHandler();
		Bundle extern = getIntent().getExtras();
		if (extern != null) {
			cartMoney = extern.getFloat("money");//
		}
		ListView = (ListView) findViewById(R.id.lv_data);
		
		
		btnLookFriendsBuy = (Button) findViewById(R.id.btn_view_friends_buy);
		
		
		
		
		pointList = new MyPointsListModel();
		

		listViewAdapter = new PointListViewAdapter();
		ListView.setAdapter(listViewAdapter);

		
		TextView tvTitle = (TextView)findViewById(R.id.title_bar_content_tv);
		tvTitle.setText(getResources().getString(R.string.point_list_title)/*"我的积分记录"*/);


		inflater = LayoutInflater.from(MyPointListView.this);
		
		
		
		moreView = inflater.inflate(R.layout.more_friends_from_net, null);
		TextView tvMore = (TextView) moreView
				.findViewById(R.id.tv_more_friends);
		tvMore.setText(getResources().getString(R.string.public_get_more_data_notice)/*"加载更多数据。。。"*/);
		tvMore.setTextColor(Color.rgb(0, 0, 0));
		tvMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {//加载更多数据
				// TODO Auto-generated method stub
				pageindex++;
				getPointsFromServer();
			}
		});

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
		if (pointList.list.size() == 0) {
			pageindex = 1;
			getPointsFromServer();
		}
	}

	

	@Override
	protected void onResume() {
		super.onResume();
		
		
	}

	
	
	public void getPointsFromServer() {
		dialogStr = getResources().getString(R.string.public_get_data_notice);///*"加载中......"*、;
		showDialog(NET_DIALOG);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(pageindex));
		localHashMap.put("pagesize", "30");
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyPointListView.this,
				MyPointListView.this, "/Android/pointrecordlist.aspx?",
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
					
					pointList.JSonToBean(json);
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
				Toast.makeText(MyPointListView.this, errMsg, 15).show();
				break;
			case ADD_SCUESS:
				Toast.makeText(MyPointListView.this, "添加好友成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				break;
			case BIND_COUPON_SCUESS:
				Toast.makeText(MyPointListView.this, "绑定优惠券成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				break;
			case BIND_COUPON_FAILD:
				Toast.makeText(MyPointListView.this, errMsg, 15).show();
				break;
			case NET_ERROR:
				Toast.makeText(MyPointListView.this, "网络或数据错误，请稍后再试！", 10)
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

	class CouponView {
		
		TextView tvPoint;
		TextView tvState;
		
		TextView tvComment;
		TextView tvTime;
	}

	class PointListViewAdapter extends BaseAdapter {
		PointListViewAdapter() {
		}

		@Override
		public int getCount() {
			int len = pointList.list.size();
			if (len == 0) {
				return len;
			}
			if (pointList.page < pointList.total) {
				len++;
			}
			return len;
		}

		@Override
		public Object getItem(int paramInt) {
			
			if (paramInt < pointList.list.size()) {
				return pointList.list.get(paramInt);
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

			CouponView listHolder;
			if (paramInt == pointList.list.size()) {
				return moreView;
			}
			if (convertView == null || convertView == moreView) {
				convertView = inflater.inflate(R.layout.point_item, null);
				listHolder = new CouponView();
				listHolder.tvPoint = (TextView)convertView.findViewById(R.id.tv_key);
				listHolder.tvState = (TextView)convertView.findViewById(R.id.tv_state);
				listHolder.tvComment = (TextView)convertView.findViewById(R.id.tv_time);
				listHolder.tvTime = (TextView)convertView.findViewById(R.id.tv_min);

				convertView.setTag(listHolder);
			} else {
				listHolder = (CouponView) convertView.getTag();
			}

			MyPoint model = pointList.list.get(paramInt);
			

			if (model != null) {
				listHolder.tvPoint.setText(model.getPoint());
				listHolder.tvComment.setText(model.getComment());
				listHolder.tvTime.setText(model.getDataTime());
			}

			return convertView;
		}
	}

}
