package com.ihangjing.HJControls;


import com.ihangjing.Model.FoodTypeListModel;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class HJFoodTypeList extends ListView implements HttpRequestListener {
	Context mContext;
	LinearLayout linearLayoutView[];

	private UIHandler mHandler;
	private FoodTypeListModel foodTypeListModel;
	

	private HttpManager localHttpManager;
	public int cFlowKeyDown = KeyEvent.KEYCODE_DPAD_RIGHT;
	public LayoutInflater inflater;
	public View.OnClickListener viewClickListener;

	public HJFoodTypeList(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public HJFoodTypeList(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;

		viewClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		};
		
		
		ReadChaceData();// 读取本地缓存
		
		
		GetData();
	}
	
	

	private void ReadChaceData() {
		if (((HjActivity)mContext).app.foodTypeList == null) {
			((HjActivity)mContext).app.foodTypeList = OtherManager.getFoodTypeFormCaceh(mContext);
			foodTypeListModel = ((HjActivity)mContext).app.foodTypeList;
			if (foodTypeListModel.list.size() == 0) {
				GetData();
			}else{
				
			}
		}
		
		
		
	}

	private void GetData() {
		localHttpManager = new HttpManager(mContext, HJFoodTypeList.this,
				"APP/Android/GetFoodTypeListByShopId.aspx", null, 2, 1);
		localHttpManager.getRequeest();
	}
	
	

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (mHandler == null) {
			mHandler = new UIHandler();
		}
		if (paramInt == 260) {
			switch (tag) {
			case 1:
				try {
					
					String value = (String) paramObject;
					foodTypeListModel.stringToBean(value, 0, getResources().getString(R.string.public_all_type));
					message.what = UIHandler.GET_DATA_SUCCESS;
				} catch (Exception e) {
					// TODO: handle exception
					message.what = UIHandler.NET_ERROR;
				}
				break;

			default:
				break;
			}
		} else {
			message.what = UIHandler.NET_ERROR;
		}
		mHandler.sendMessage(message);
	}

	private class UIHandler extends Handler {
		public static final int GET_DATA_SUCCESS = 2;
		public static final int NET_ERROR = -1;

		protected static final int REPLAY_ADV = 1;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_DATA_SUCCESS:
				OtherManager.saveFoodTypeListToCache(mContext,
						foodTypeListModel);
				
				break;
			
			case NET_ERROR:
				Toast.makeText(mContext, "网络活数据错误！请稍后再试", 5).show();
				break;
			default:
				break;
			}
		}
	}

	
}
