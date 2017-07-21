package com.ihangjing.HJControls;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.ZJYXForAndroid.MainActivity;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.ZJYXForAndroid.ShowNews;
import com.ihangjing.Model.FoodListModel;
import com.ihangjing.Model.FoodModel;
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
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HJScorllFood extends RelativeLayout implements HttpRequestListener{
	Context mContext;
	HJViewPager imageView;
	private int time;
	private boolean isShowButton;
	private int leftBtnSrc;
	private int rightBtnSrc;
	private Button btnLeft;
	private Button btnRight;
	private Timer timer;
	private TimerTask updataViewTask;
	private UIHandler mHandler;
	//private ArrayList<NetImage> imageList;
	private FoodListModel foodListModel;
	private ImageAdapter imageAdapter;
	private int backgroundSrc;
	private HttpManager localHttpManager;
	EasyEatApplication app;
	public int cFlowKeyDown = KeyEvent.KEYCODE_DPAD_RIGHT;
	private OnClickListener foodOnClickListener;
	private LayoutInflater mInflater;
	protected int nowIndexPage = 0;
	private ArrayList<View> listViews;
	public boolean addOrDown;
	public HJScorllFood(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public HJScorllFood(Context context, AttributeSet attrs) {
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
				R.drawable.adv_food_leftbtn_default_selector);
		rightBtnSrc = a.getResourceId(R.styleable.HJScorllADV_rightButtonSrc,
				R.drawable.adv_food_rightbtn_default_selector);
		backgroundSrc = a.getResourceId(R.styleable.HJScorllADV_backgoundSrc,
				R.drawable.mindex);
		RelativeLayout.LayoutParams params;

		imageView = new HJViewPager(context);

		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		// params.addRule(RelativeLayout.CENTER_IN_PARENT);
		imageView.setLayoutParams(params);
		foodOnClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = (Integer)v.getTag();
					app.foodListType = 2;
					app.viewID = id;
					Intent localIntent = new Intent("com.ihangjing.common.tap1");
					app.sendBroadcast(localIntent);
				
			}
		};
		/*imageView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				
				app.foodListType = 2;
				app.viewID = foodListModel.list.get(arg2).getFoodId();
				Intent localIntent = new Intent("com.ihangjing.common.tap1");
				app.sendBroadcast(localIntent);
			}

		});*/
		mInflater = ((HjActivity)mContext).getLayoutInflater();
		foodListModel = new FoodListModel();
		this.addView(imageView);
		View view = new View(context);
		view.setDrawingCacheEnabled(true);
		if (isShowButton) {// 创建按钮
			View.OnClickListener click = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!imageView.getScanScroll()) {
						return;
					}
					nowIndexPage  = imageView.getCurrentItem();
					if (listViews.size() > 0) {
						switch ((Integer) v.getTag()) {
						case 1:
							// 上一副

							
							if (nowIndexPage > 0) {
								nowIndexPage--;
								imageView.setCurrentItem(nowIndexPage);
							}
							break;
						case 2:
							// 下一副

							
							if (nowIndexPage < listViews.size() - 1) {
								nowIndexPage++;
								imageView.setCurrentItem(nowIndexPage);
							}
							break;
						default:
							break;
						}
					}

				}
			};

			params = new RelativeLayout.LayoutParams(35,
					35);
			params.setMargins(20, 0, 0, 0);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			
			btnLeft = new Button(context);
			btnLeft.setLayoutParams(params);
			btnLeft.setBackgroundResource(leftBtnSrc);
			
			btnLeft.setOnClickListener(click);
			btnLeft.setTag(1);

			this.addView(btnLeft);

			params = new RelativeLayout.LayoutParams(35,
					35);
			params.setMargins(0, 0, 20, 0);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			
			btnRight = new Button(context);
			btnRight.setLayoutParams(params);
			btnRight.setBackgroundResource(rightBtnSrc);
			
			btnRight.setOnClickListener(click);
			btnRight.setTag(2);
			this.addView(btnRight);
		}
		a.recycle();
		listViews = new ArrayList<View>();
		ReadChaceData();
		imageAdapter = new ImageAdapter();
		imageView.setAdapter(imageAdapter);

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
		
		
		// BitmapFactory.d
		// Bitmap originalImage = Bitmap.createBitmap(imageView.getWidth(),
		// imageView.getHeight(), Bitmap.Config.ARGB_8888);
		
		GetData();
	}
	
	private void ReadChaceData() {
		foodListModel = OtherManager.getFoodListFormCaceh(mContext);
		View view;
		for (int i = 0; i < foodListModel.list.size(); i++) {
			view = (View)mInflater.inflate(R.layout.hj_adv_food, null);
			listViews.add(view);
		}
	}

	private void GetData() {
		localHttpManager = new HttpManager(mContext, HJScorllFood.this,
				"/Android/GetFoodListByShopId.aspx?pageindex=1&Pagesize=6", null, 2, 1);
		localHttpManager.getRequeest();
	}

	private class UIHandler extends Handler {
		
		protected static final int REPLAY_ADV = 1;
		public static final int GET_DATA_SUCCESS = 2;
		public static final int NET_ERROR = -1;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_DATA_SUCCESS:
				int lenght = foodListModel.list.size();
				int len = lenght - listViews.size();
				imageView.setScanScroll(false);
				OtherManager.saveFoodListToCache(mContext, foodListModel);
				if (lenght > 0) {
					if (len > 0) {
						View view;
						for (; len > 0; len--) {
							view = (View)mInflater.inflate(R.layout.hj_adv_food, null);
							listViews.add(view);
						}
					}else{
						len *= -1;
						
						for (; len > 0; len--) {
							imageView.removeViewAt(0);
							listViews.remove(0);
						}
						
					}
					FoodModel model;
					for (int i = 0; i < lenght; i++) {
						model = foodListModel.list.get(i);
						//if (model.isDowload) {//按需下载
							app.mLoadImage.addTask(model.getImageNetPath(), null, 0);
						//}
					}
					//imageAdapter.notifyDataSetChanged();
				}else{
					listViews.clear();
					imageView.removeAllViews();
					
				}
				imageView.setScanScroll(true);
				imageAdapter.notifyDataSetChanged();
				break;
			case NET_ERROR:
				Toast.makeText(mContext, "网络活数据错误！请稍后再试", 5).show();
				break;
			case REPLAY_ADV:
				if (app.mLoadImage != null) {
					app.mLoadImage.doTask();
				}
				if (foodListModel == null) {
					return;
				}
				if (foodListModel.list.size() > 0) {
					if (nowIndexPage == 0) {// 大于总数。往左

						cFlowKeyDown = KeyEvent.KEYCODE_DPAD_RIGHT;
						addOrDown = true;
						
					} else if(nowIndexPage == listViews.size() - 1){
						cFlowKeyDown = KeyEvent.KEYCODE_DPAD_LEFT;
						addOrDown = false;
						//nowIndexPage = n;
					}
					//imageView.onKeyDown(cFlowKeyDown, null);
					imageView.setCurrentItem(nowIndexPage);
					if (addOrDown) {
						nowIndexPage++;
					}else{
						nowIndexPage--;
					}
				}
				/*FoodModel model;
				for (int i = 0; i < foodListModel.list.size(); i++) {
					model = foodListModel.list.get(i);
					//if (model.isDowload) {// 需要下载

						if (model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
							Bitmap bitmap = app.mLoadImage.memoryCache
									.getBitmapFromCache(model.getImageNetPath());
							if (bitmap != null) {//下载完成保存图片到本地
								//model.setImageLocalPath(OtherManager.saveFooodBitmapWithType(bitmap, model.getFoodId()));
								//model.isDowload = false;
								OtherManager.saveFoodListToCache(mContext, foodListModel);
								imageAdapter.notifyDataSetChanged();
							}
						}

					//} 
				}*/
				break;

			default:
				break;
			}
		}
	}

	
	public class viewInfo{
		public ImageView image;
		public TextView name;
		public TextView price;
	}
	public class ImageAdapter extends PagerAdapter {
		private viewInfo info;
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewGroup)arg0).removeView(listViews.get(arg1));//删除页卡
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}
		 @Override
		public void setPrimaryItem(View container, int position, Object object) {
			 if(info == null){
				 info = new viewInfo();
			 }
			 if (position >= listViews.size()) {
				return;
			}
			 View view = listViews.get(position);
			 info.image = (ImageView)view.findViewById(R.id.rl_img);
				info.name = (TextView)view.findViewById(R.id.tv_name);
				info.price = (TextView)view.findViewById(R.id.tv_price);
				//赋值
				FoodModel model = foodListModel.list.get(position);
				info.name.setText(model.getFoodName());
				info.price.setText(String.valueOf(model.getPrice()) + "￥");
				Bitmap originalImage;
				if (model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
					originalImage = app.mLoadImage.getBitmapFromCache(model.getImageNetPath());
					if (originalImage == null) {
						app.mLoadImage.addTask(model.getImageNetPath(), null, 0);
						originalImage = BitmapFactory.decodeResource(
								mContext.getResources(), R.drawable.icon);
					}
				}else{
					originalImage = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.icon);
				}
				/*if (model.getImageLocalPath() != null && !model.getImageLocalPath().equals("")) {// 本地地址不为空
					originalImage = OtherManager.getBitmap(model.getImageLocalPath());
					if (originalImage == null) {
						originalImage = BitmapFactory.decodeResource(
								mContext.getResources(), R.drawable.icon);
						if (!model.isDowload && model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
							//有网络地址，并且没有加入下载，那么加入
							model.isDowload = true;
							app.mLoadImage.addTask(model.getImageNetPath(), null, 0);
						}
					}
				}else{// 本地和网络地址都为空，使用id
					originalImage = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.icon);
					if (!model.isDowload && model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {//
						//有网络地址，并且没有加入下载，那么加入
						model.isDowload = true;
						app.mLoadImage.addTask(model.getImageNetPath(), null, 0);
					}
				}*/
				Drawable drawable = new BitmapDrawable(originalImage);
				info.image.setBackgroundDrawable(drawable);
				view.setTag(model.getFoodId());
				view.setOnClickListener(foodOnClickListener);
		 }
		 
		 @Override
		public int getItemPosition(Object object) {
	            // TODO Auto-generated method stub
	            return POSITION_NONE;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (listViews == null) {
				return 0;
			}
			return listViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewGroup)arg0).addView(listViews.get(arg1), 0);//添加页卡  
            return listViews.get(arg1); 
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;//官方提示这样写
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
		/*private Context mContext;
		private LayoutInflater inflater;
		private viewInfo info;

		public ImageAdapter(Context context) {
			mContext = context;
			info = new viewInfo();
		}

		public int getCount() {
			if (foodListModel == null) {
				return 0;
			}
			return foodListModel.list.size();
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
				if (inflater == null) {
					inflater = (LayoutInflater) mContext
							.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				}
			} else {
				image = (ImageView) convertView;
			}

			
			
			if (imageView.getWidth() == 0 || imageView.getHeight() == 0) {
				return image;
			}
			View view = inflater.inflate(R.layout.hj_adv_food, null);
			view.setDrawingCacheEnabled(true);
			info.image = (ImageView)view.findViewById(R.id.rl_img);
			info.name = (TextView)view.findViewById(R.id.tv_name);
			info.price = (TextView)view.findViewById(R.id.tv_price);
			//赋值
			FoodModel model = foodListModel.list.get(position);
			info.name.setText(model.getFoodName());
			info.price.setText(String.valueOf(model.getPrice()) + "￥");
			Bitmap originalImage;
			if (model.getImageLocalPath() != null && !model.getImageLocalPath().equals("")) {// 本地地址不为空
				originalImage = OtherManager.getBitmap(model.getImageLocalPath());
				if (originalImage == null) {
					originalImage = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.icon);
					if (!model.isDowload && model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
						//有网络地址，并且没有加入下载，那么加入
						model.isDowload = true;
						app.mLoadImage.addTask(model.getImageNetPath(), null, 0);
					}
				}
			}else{// 本地和网络地址都为空，使用id
				originalImage = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.icon);
				if (!model.isDowload && model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {//
					//有网络地址，并且没有加入下载，那么加入
					model.isDowload = true;
					app.mLoadImage.addTask(model.getImageNetPath(), null, 0);
				}
			}
			
			info.image.setImageBitmap(originalImage);
			
			view.measure(MeasureSpec.makeMeasureSpec(imageView.getWidth(), MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(imageView.getHeight(), MeasureSpec.EXACTLY));//计算
			view.layout(0, 0, imageView.getWidth(), imageView.getHeight());//布局
			view.requestLayout();
			//方法1：
			//Bitmap originalImage = Bitmap.createBitmap(view.getWidth(),
			//		view.getHeight(), Config.ARGB_8888);
			//originalImage = 
			//Canvas canvas = new Canvas(originalImage);
			// canvas.translate(-view.getScrollX(), -view.getScrollY());
			//view.draw(canvas);
			//方法2：
			view.buildDrawingCache();
			
			Bitmap cacheBitmap = view.getDrawingCache();
			Bitmap viewBitmap = cacheBitmap.copy(Config.ARGB_8888, true);
			view.destroyDrawingCache();

			
			image.setAdjustViewBounds(true);
			image.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			image.setImageBitmap(viewBitmap);
			
			return image;
		}*/

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
					foodListModel.JSonToBean(new JSONObject(value));
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
}
