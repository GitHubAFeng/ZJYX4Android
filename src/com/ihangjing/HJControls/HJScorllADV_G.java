package com.ihangjing.HJControls;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.ZJYXForAndroid.MainActivity;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.ZJYXForAndroid.ShowNews;
import com.ihangjing.Model.HJADVListModel;
import com.ihangjing.Model.HJADVModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HJScorllADV_G extends RelativeLayout implements HttpRequestListener{
	Context mContext;
	HJGallery imageView;
	private int time;
	private boolean isShowButton;
	private int leftBtnSrc;
	private int rightBtnSrc;
	private Button btnLeft;
	private Button btnRight;
	private Timer timer;
	private TimerTask updataViewTask;
	private UIHandler mHandler;
	private HJADVListModel advList;
	private ImageAdapter imageAdapter;
	private int backgroundSrc;
	private HttpManager localHttpManager;
	public int cFlowKeyDown = KeyEvent.KEYCODE_DPAD_RIGHT;
	public LayoutInflater inflater;
	private OnClickListener advOnClickListener;
	private ArrayList<View> listViews;
	EasyEatApplication app;
	public int nowIndexPage;
	public boolean addOrDown;
	public HJScorllADV_G(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public HJScorllADV_G(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.setStaticTransformationsEnabled(true);
		mContext = context;
		app = ((HjActivity)mContext).app;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.HJScorllADV);
		time = a.getInt(R.styleable.HJScorllADV_scorllTime, 5);
		time *= 1000;
		isShowButton = a
				.getBoolean(R.styleable.HJScorllADV_isShowButton, false);
		leftBtnSrc = a.getResourceId(R.styleable.HJScorllADV_leftButtonSrc,
				R.drawable.adv_leftbtn_default_selector);
		rightBtnSrc = a.getResourceId(R.styleable.HJScorllADV_rightButtonSrc,
				R.drawable.adv_rightbtn_default_selector);
		backgroundSrc = a.getResourceId(R.styleable.HJScorllADV_backgoundSrc,
				R.drawable.mindex);
		LinearLayout.LayoutParams params;

		imageView = new HJGallery(context, attrs);
		/*imageView.measure(MeasureSpec.makeMeasureSpec(LayoutParams.FILL_PARENT, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(LayoutParams.FILL_PARENT, MeasureSpec.EXACTLY));*/
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		params.setMargins(100, 0, 100, 0);
		// params.addRule(RelativeLayout.CENTER_IN_PARENT);
		imageView.setLayoutParams(params);
		imageView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int id = advList.list.get(arg2).getDataID();
				int type = advList.list.get(arg2).getAdvType();
				if (type == 0) {//商品
					app.foodListType = 2;
					app.viewID = id;
					Intent localIntent = new Intent("com.ihangjing.common.tap1");
					app.sendBroadcast(localIntent);
				}else{//公告
					Intent intent = new Intent(mContext, ShowNews.class);
					intent.putExtra("dataid", id);
					intent.putExtra("type", 0);
					mContext.startActivity(intent);
				}
			}

		});
		//imageList = new ArrayList<HJScorllADV.NetImage>();
		this.addView(imageView);

		if (isShowButton) {// 创建按钮
			View.OnClickListener click = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (advList.list.size() > 0) {
						switch ((Integer) v.getTag()) {
						case 1:
							// 上一副

							imageView.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT,
									null);
							break;
						case 2:
							// 下一副

							imageView.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT,
									null);
							break;
						default:
							break;
						}
					}

				}
			};

			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(60,
					60);
			params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params1.addRule(RelativeLayout.CENTER_VERTICAL);
			params1.setMargins(20, 0, 0, 0);
			btnLeft = new Button(context);
			btnLeft.setBackgroundResource(leftBtnSrc);
			btnLeft.setLayoutParams(params1);
			btnLeft.setOnClickListener(click);
			btnLeft.setTag(1);

			this.addView(btnLeft);

			params1 = new RelativeLayout.LayoutParams(60,
					60);
			params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params1.addRule(RelativeLayout.CENTER_VERTICAL);
			params1.setMargins(0, 0, 20, 0);
			btnRight = new Button(context);
			btnRight.setBackgroundResource(rightBtnSrc);
			btnRight.setLayoutParams(params1);
			btnRight.setOnClickListener(click);
			btnRight.setTag(2);
			this.addView(btnRight);
		}

		
		//advList = new HJADVListModel();
		ReadChaceData();//读取本地缓存
		imageAdapter = new ImageAdapter(mContext);
		imageView.setAdapter(imageAdapter);
		/*imageView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
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
		});*/

		mHandler = new UIHandler();
		timer = new Timer();
		updataViewTask = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = UIHandler.REPLAY_ADV;
				mHandler.sendMessage(msg);

			}
		};
		timer.schedule(updataViewTask, time, time);

		a.recycle();
		
		
		
		GetData();
	}

	
	
	private void ReadChaceData(){
		//advList = OtherManager.getAdvListFormCache(mContext);
	}
	
	private void GetData(){
		localHttpManager = new HttpManager(mContext,
				HJScorllADV_G.this, "APP/Android/GetAdList.aspx",
				null, 2, 1);
		localHttpManager.getRequeest();
	}
	
	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt ==260) {
			switch (tag) {
			case 1:
				try {
					String value = (String)paramObject;
					//advList.JSonToBean(new JSONObject(value));
					message.what = UIHandler.GET_DATA_SUCCESS;
				} catch (Exception e) {
					// TODO: handle exception
					message.what = UIHandler.NET_ERROR;
				}
				break;

			default:
				break;
			}
		}else{
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
				int lenght = advList.list.size();
				//OtherManager.saveAdvListToCache(mContext, advList);
				if (lenght > 0) {
					
					HJADVModel model;
					for (int i = 0; i < lenght; i++) {
						model = advList.list.get(i);
						//if (model.isDowload) {//按需下载
							app.mLoadImage.addTask(model.getImageNetPath(), null, 0);
						//}
					}
					imageAdapter.notifyDataSetChanged();
				}
				break;
			case REPLAY_ADV:
				app.mLoadImage.doTask();
				if (advList == null) {
					return;
				}
				if (advList.list.size() > 0) {
					nowIndexPage = imageView.getSelectedItemPosition();
					
					if (nowIndexPage == 0) {// 大于总数。往左

						cFlowKeyDown = KeyEvent.KEYCODE_DPAD_RIGHT;
						addOrDown = true;
						
					} else if(nowIndexPage == advList.list.size() - 1){
						cFlowKeyDown = KeyEvent.KEYCODE_DPAD_LEFT;
						addOrDown = false;
						//nowIndexPage = n;
					}
					imageView.onKeyDown(cFlowKeyDown, null);
					//imageView.setCurrentItem(nowIndexPage);
					/*if (addOrDown) {
						nowIndexPage++;
					}else{
						nowIndexPage--;
					}*/
				}
				/*HJADVModel model;
				for (int i = 0; i < advList.list.size(); i++) {
					model = advList.list.get(i);
					//if (model.isDowload) {// 需要下载

						if (model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
							Bitmap bitmap = app.mLoadImage.memoryCache
									.getBitmapFromCache(model.getImageNetPath());
							if (bitmap != null) {//下载完成保存图片到本地
								//model.setImageLocalPath(OtherManager.saveBitmapWithType(bitmap, "ADV", model.getAdvType(), model.getDataID()));
								//model.isDowload = false;
								OtherManager.saveAdvListToCache(mContext, advList);
								imageAdapter.notifyDataSetChanged();
							}
						}

					//} 
				}*/
				break;
			case NET_ERROR:
				Toast.makeText(mContext, "网络活数据错误！请稍后再试", 5).show();
				break;
			default:
				break;
			}
		}
	}

	public class NetImage {
		public String netPath;// 网络路径
		public String localPath = "";// 本地路径
		public int resID = R.drawable.mindex;// 资源id
		public int dataID = 0;// 在业务逻辑中的编号，也即文件名称，用数字表示
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			if (advList == null) {
				return 0;
			}
			return advList.list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView image;
			if (convertView == null) {
				image = new ImageView(mContext);
				
			} else {
				image = (ImageView) convertView;
			}
			
			
			Bitmap originalImage;
			HJADVModel netImage = advList.list.get(position);
			if (netImage.getImageNetPath() != null && !netImage.getImageNetPath().equals("")) {
				originalImage = app.mLoadImage.getBitmapFromCache(netImage.getImageNetPath());
				if (originalImage == null) {
					//netImage.isDowload = true;
					app.mLoadImage.addTask(netImage.getImageNetPath(), null, 0);
					originalImage = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.mindex);
				}
			}else{
				originalImage = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.mindex);
			}
			/*if (netImage.getImageLocalPath() != null && !netImage.getImageLocalPath().equals("")) {// 本地地址不为空
				originalImage = OtherManager.getBitmap(netImage.getImageLocalPath());
				if (originalImage == null) {
					originalImage = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.mindex);
					if (!netImage.isDowload && netImage.getImageNetPath() != null && !netImage.getImageNetPath().equals("")) {
						//有网络地址，并且没有加入下载，那么加入
						netImage.isDowload = true;
						app.mLoadImage.addTask(netImage.getImageNetPath(), null, 0);
					}
				}
			}else{// 本地和网络地址都为空，使用id
				originalImage = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.mindex);
				if (!netImage.isDowload && netImage.getImageNetPath() != null && !netImage.getImageNetPath().equals("")) {//
					//有网络地址，并且没有加入下载，那么加入
					netImage.isDowload = true;
					app.mLoadImage.addTask(netImage.getImageNetPath(), null, 0);
				}
			}*/
			
			
			image.setAdjustViewBounds(true);
			image.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			image.setScaleType(ScaleType.FIT_CENTER);
			image.setImageBitmap(originalImage);
			
			return image;
		}

		/*@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
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
			
		}*/

	}

	
}
