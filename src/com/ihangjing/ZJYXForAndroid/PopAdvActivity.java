package com.ihangjing.ZJYXForAndroid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.renderscript.Type;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.ihangjing.Model.PopAdvListModel;
import com.ihangjing.Model.PopAdvModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

import cz.msebera.android.httpclient.HttpRequest;

public class PopAdvActivity extends HjActivity implements HttpRequestListener{
	private ArrayList<View> listViews;
	private ViewPager viewPager;
	private FixedSpeedScroller mScroller;
	private Timer timer;
	private TimerTask updataViewTask;
	protected UIHandler mHandler;
	private int showType = 0;//0普通 1商家资质
	private String shopID;
	PopAdvListModel popAdvList;
	private HttpManager localHttpManager;
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.pop_adv);
		
		Bundle extern = getIntent().getExtras();
		if(extern != null){
			showType = extern.getInt("showType");
			shopID = extern.getString("shopID");
		}
		mHandler = new UIHandler();
		listViews = new ArrayList<View>();

		viewPager = (ViewPager) findViewById(R.id.vs_select);
		/* 主要代码段 */
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			// 设置加速度
			// ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
			mScroller = new FixedSpeedScroller(viewPager.getContext(),
					new AccelerateInterpolator());
			mField.set(viewPager, mScroller);
			mScroller.setmDuration(800);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		

		

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		timer = new Timer();
		updataViewTask = new TimerTask() {
			@Override
			public void run() {
				// mSchedule.notifyDataSetChanged();
				Message msg = new Message();
				msg.what = UIHandler.REPLAY_ADV;
				mHandler.sendMessage(msg);

			}
		};
		if (showType == 0) {
			timer.schedule(updataViewTask, 5000L, 5000L);
		}else{
			timer.schedule(updataViewTask, 1000L, 1000L);
		}
		
		if(showType == 0){
			popAdvList = app.popAdvList;
			setView();
			
		}else{
			getData();
			Button btnClose = (Button)findViewById(R.id.btnCancel);
			btnClose.setVisibility(View.VISIBLE);
			btnClose.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			
		}
		
		

	}
	@Override
	public void onDestroy()
	{
		timer.cancel();
		super.onDestroy();
	}
	// 通过网络获取数据 获取完成后发送消息
		public void getData() {

			showDialog(111);
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("shopid", shopID);

			localHttpManager = new HttpManager(PopAdvActivity.this, PopAdvActivity.this,
					"/Android/GetShopLicence.aspx?", localHashMap, 2, 1);

			localHttpManager.getRequeest();

		}
		@Override
		protected Dialog onCreateDialog(int paramInt) {
			Dialog dialog = null;

			if (paramInt == 111) {
				dialog = OtherManager.createProgressDialog(PopAdvActivity.this,
						getResources().getString(R.string.user_center_get));
			}

			return dialog;
		}
	public class HJPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public HJPagerAdapter(List<View> listViews) {
			// TODO Auto-generated constructor stub
			this.mListViews = listViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}
	public class FixedSpeedScroller extends Scroller {
		private int mDuration = 1500;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
				int duration) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setmDuration(int time) {
			mDuration = time;
		}

		public int getmDuration() {
			return mDuration;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(showType == 1){
				finish();
			}
			return false;
		} else {
			return super.onKeyDown(keyCode, paramKeyEvent);
			
		}
	}
	private class UIHandler extends Handler {
		static final int REPLAY_ADV = 4;
		static final int DO_GET_LOCATION_SUCCESS = 2;
		public static final int NET_ERROR = 1;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NET_ERROR:
				Toast.makeText(PopAdvActivity.this, getResources().getString(R.string.public_net_or_data_error), 15).show();
				break;
			case DO_GET_LOCATION_SUCCESS: {
				setView();
			}
				break;
			case REPLAY_ADV:
				app.mLoadImage.doTask();
				if(showType == 0){
					int cur = viewPager.getCurrentItem();
					cur++;
					if (cur > popAdvList.list.size() - 1) {
						timer.cancel();
						if (popAdvList.popType == 1) {
							startActivity(new Intent(getApplication(), MainTabActivity.class));
							overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
						}
						PopAdvActivity.this.finish();
					} else {
						viewPager.setCurrentItem(cur);
					}
				}
				
				break;
			}
		}
	}
	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if(paramInt == 260){
			String value = (String)paramObject;
			try {
				JSONObject json = new JSONObject(value);
				switch (tag) {
				case 1:
					popAdvList = new PopAdvListModel();
					popAdvList.ShopZJSonToBean(json);
					msg.what = UIHandler.DO_GET_LOCATION_SUCCESS;
					break;

				default:
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				msg.what = UIHandler.NET_ERROR;
			}
			
		}else{
			msg.what = UIHandler.NET_ERROR;
		}
		removeDialog(111);
		mHandler.sendMessage(msg);
	}
	public void setView() {
		// TODO Auto-generated method stub
		View view;// = getLayoutInflater().inflate(R.layout.start, null);
		if(popAdvList.list.size() > 0){
			for (int i = 0; i < popAdvList.list.size(); i++) {
				view = getLayoutInflater().inflate(R.layout.adv_view, null);
				RelativeLayout ivImag = (RelativeLayout)view.findViewById(R.id.iv_img);
				PopAdvModel model = popAdvList.list.get(i);
				ivImag.setTag(model.getImageNet());
				if (popAdvList.popType == 0 && model.getImageNet() != null && model.getImageNet().length() > 0) {
					if (showType != 0) {
						model.setResID(R.drawable.loadimging);
					}
					
					app.mLoadImage.addTask(model.getImageNet(), ivImag, model.getResID());
					
				}else{
					if (showType == 0) {
						ivImag.setBackgroundResource(model.getResID());
					}else{
						ivImag.setBackgroundResource(R.drawable.nopic_skill);
					}
					
				}
				listViews.add(view);
			}
			
		}else{
			view = getLayoutInflater().inflate(R.layout.adv_view, null);
			RelativeLayout ivImag = (RelativeLayout)view.findViewById(R.id.iv_img);
			if (showType == 0) {
				ivImag.setBackgroundResource(R.drawable.start);
			}else{
				ivImag.setBackgroundResource(R.drawable.nopic_skill);
			}
			listViews.add(view);
		}
		
		viewPager.setAdapter(new HJPagerAdapter(listViews));
	}
}
