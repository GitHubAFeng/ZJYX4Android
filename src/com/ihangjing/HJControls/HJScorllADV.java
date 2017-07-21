package com.ihangjing.HJControls;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.ZJYXForAndroid.MainActivity;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.ZJYXForAndroid.ShopDetail;
import com.ihangjing.ZJYXForAndroid.ShowNews;
import com.ihangjing.Model.HJADVListModel;
import com.ihangjing.Model.HJADVModel;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HJScorllADV extends RelativeLayout implements HttpRequestListener{
	private String PageName = null;
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
	private HJADVListModel advList;
	private ImageAdapter imageAdapter;
	private int backgroundSrc;
	private HttpManager localHttpManager;
	public int cFlowKeyDown = KeyEvent.KEYCODE_DPAD_RIGHT;
	public LayoutInflater inflater;
	private OnClickListener advOnClickListener;
	private ArrayList<View> listViews[];
	public int userIndex;
	public int nowIndex;
	EasyEatApplication app;
	public int nowIndexPage = 0;
	public boolean addOrDown;
	private LayoutInflater mInflater;
	private LinearLayout llViewDots;//下面显示当前页面指示器视图
	private ArrayList<ImageView> listDots;//指示器，和listViews数量相同
	private boolean isShowDots;
	Drawable dotsBackground;
	float dotsBgAlpha;
	private int readData;
	private String typeID;
	private String Parameters;
	public HJScorllADV(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	public LoadImage getLoadImage(){
		return app.mLoadImage;
	}
	public HJScorllADV(Context context, AttributeSet attrs) {
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
		isShowDots = a.getBoolean(R.styleable.HJScorllADV_isShowDots, true);
		leftBtnSrc = a.getResourceId(R.styleable.HJScorllADV_leftButtonSrc,
				R.drawable.adv_leftbtn_default_selector);
		rightBtnSrc = a.getResourceId(R.styleable.HJScorllADV_rightButtonSrc,
				R.drawable.adv_rightbtn_default_selector);
		backgroundSrc = a.getResourceId(R.styleable.HJScorllADV_backgoundSrc,
				R.drawable.mindex);
		readData = a.getInt(R.styleable.HJScorllADV_readData, 0);
		dotsBackground = a.getDrawable(R.styleable.HJScorllADV_dotsBackground);
		dotsBgAlpha = a.getFloat(R.styleable.HJScorllADV_dotsBgAlpha, 0.0f);
		LinearLayout.LayoutParams params;
		

		imageView = new HJViewPager(context, attrs);
		/*imageView.measure(MeasureSpec.makeMeasureSpec(LayoutParams.FILL_PARENT, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(LayoutParams.FILL_PARENT, MeasureSpec.EXACTLY));*/
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		//params.setMargins(100, 0, 100, 0);
		// params.addRule(RelativeLayout.CENTER_IN_PARENT);
		mInflater = ((HjActivity)mContext).getLayoutInflater();
		listViews = new ArrayList[2];
		listViews[0] = new ArrayList<View>();
		listViews[1] = new ArrayList<View>();
		imageView.setLayoutParams(params);
		
		//imageList = new ArrayList<HJScorllADV.NetImage>();
		this.addView(imageView);
		
		imageView.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (llViewDots != null) {
					ImageView image;
					if (arg0 + 1 < listDots.size()) {
						image = listDots.get(arg0 + 1);
						image.setBackgroundResource(R.drawable.index_ico_03);
					}
					if (arg0 - 1 >= 0) {
						image = listDots.get(arg0 - 1);
						image.setBackgroundResource(R.drawable.index_ico_03);
					}
					image = listDots.get(arg0);
					image.setBackgroundResource(R.drawable.index_ico_hov_03);
					
				}
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

		if (isShowButton) {// 创建按钮
			View.OnClickListener click = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*if (!imageView.getScanScroll()) {
						return;
					}*/
					nowIndexPage = imageView.getCurrentItem();
					if (listViews[userIndex].size() > 0) {
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

							
							if (nowIndexPage < listViews[userIndex].size() - 1) {
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
		
		if (isShowDots) {//创建下面的指示器
			llViewDots = new LinearLayout(context);
			llViewDots.setGravity(Gravity.CENTER);
			listDots = new ArrayList<ImageView>();
			if (dotsBackground != null) {
				dotsBackground.setAlpha((int)(dotsBgAlpha * 255));
				llViewDots.setBackgroundDrawable(dotsBackground);
			}
			RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		            40);
			params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
			this.addView(llViewDots, params1);
			
			
			
		}

		
		//advList = new HJADVListModel();
		imageAdapter = new ImageAdapter();
		if (readData == 0) {
			ReadChaceData();//读取本地缓存
		}
		
		
		imageView.setAdapter(imageAdapter);
		advOnClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (readData == 0) {
					int id = (Integer)v.getTag();
					HJADVModel netImage = advList.list.get(id);
					if (netImage.getAdvType() == 2) {//点评
						ShopListItemModel localShopModel = new ShopListItemModel();
						localShopModel.setId(netImage.getDataID());
						localShopModel.setName("");
						app.setShop(localShopModel);
						Intent intent = new Intent(mContext, ShopDetail.class);
						intent.putExtra("isRem", 0);//超市
						mContext.startActivity(intent);
					}else if (netImage.getAdvType() == 3) {//商家
						ShopListItemModel localShopModel = new ShopListItemModel();
						localShopModel.setId(netImage.getDataID());
						localShopModel.setName("");
						app.setShop(localShopModel);
						Intent intent = new Intent(mContext, ShopDetail.class);
						intent.putExtra("isRem", 8);//超市
						mContext.startActivity(intent);
					}
					/*int type = (Integer)v.getTag(R.id.tab_icon);
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
					}*/
				}
				
			}
		};
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
		
		
		if (readData == 0) {
			GetData();//读取本地缓存
		}
	}
	
	public void stopADV(){
		if (updataViewTask != null) {
			updataViewTask.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
	}

	public void setReadData(String page, String id, String para)
	{
		if (PageName == null || PageName.length() < 1) {
			PageName = page;
			typeID = id;
			Parameters = para;
			ReadChaceData();
			GetData();
		}
		
		
	}
	
	private void ReadChaceData(){
		
		if (readData == 0) {
			advList = OtherManager.getAdvListFormCache(mContext, "ADVListCache");
		}else{
			advList = OtherManager.getAdvListFormCache(mContext, typeID);
		}
		
		initView();
		
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		View view;
		ImageView iv;
		nowIndex = userIndex;
		nowIndex--;
		nowIndex *= -1;
		listViews[nowIndex].clear();
		if (llViewDots != null) {
			listDots.clear();
			llViewDots.removeAllViews();
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 0, 0);
		for (int i = 0; i < advList.list.size(); i++) {
			view = (View)mInflater.inflate(R.layout.hj_adv_adv, null);
			listViews[nowIndex].add(view);
			HJADVModel netImage = advList.list.get(i);
			RelativeLayout image = (RelativeLayout)view.findViewById(R.id.iv_adv);
			image.setTag(netImage.getImageNetPath());
			if (netImage.getImageNetPath() != null && !netImage.getImageNetPath().equals("")) {
				
				
					app.mLoadImage.addTask(netImage.getImageNetPath(), image, 0);
					
				
			}
			if (llViewDots != null) {
				iv = new ImageView(getContext());
				if (i != 0) {
					iv.setBackgroundResource(R.drawable.index_ico_03);
				}else{
					iv.setBackgroundResource(R.drawable.index_ico_hov_03);
				}
				iv.setLayoutParams(params);
				listDots.add(iv);
				llViewDots.addView(iv);
			}
			
			
		}
		//imageView.setScanScroll(true);
		userIndex = nowIndex;
		imageAdapter.notifyDataSetChanged();
		
	}

	private void GetData(){
		if (readData == 0) {
			localHttpManager = new HttpManager(mContext,
					HJScorllADV.this, "APP/Android/specialad.aspx",
					null, 2, 1);
		}else{
			localHttpManager = new HttpManager(mContext,
					HJScorllADV.this, "APP/Android/" + PageName + "?" + Parameters,
					null, 2, 1);
		}
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
					advList.JSonToBean(new JSONObject(value), readData);
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
				//int lenght = advList.list.size();
				//int len = lenght - listViews.size();
				
				if (readData == 0) {
					OtherManager.saveAdvListToCache(mContext, advList, "ADVListCache");
				}else{
					OtherManager.saveAdvListToCache(mContext, advList, typeID);
				}
				//imageView.setScanScroll(false);
				initView();
				/*if (lenght > 0) {
					if (len > 0) {
						View view;
						for (; len > 0; len--) {
							view = (View)mInflater.inflate(R.layout.hj_adv_adv, null);
							listViews.add(view);
							if (llViewDots != null) {
								if (llViewDots != null) {
									ImageView iv = new ImageView(getContext());
									iv.setBackgroundResource(R.drawable.index_ico_03);
									
									
									listDots.add(iv);
									llViewDots.addView(iv);
								}
							}
						}
					}else{
						len *= -1;
						
						for (; len > 0; len--) {
							imageView.removeViewAt(0);
							listViews.remove(0);
							if (llViewDots != null) {
								listDots.remove(0);
								llViewDots.removeViewAt(0);
							}
							
						}
						
					}
					HJADVModel model;
					for (int i = 0; i < lenght; i++) {
						model = advList.list.get(i);
						//if (model.isDowload) {//按需下载
							app.mLoadImage.addTask(model.getImageNetPath(), null, 0);
						//}
					}
					//imageAdapter.notifyDataSetChanged();
				}else{
					listViews.clear();
					imageView.removeAllViews();
					if (llViewDots != null) {
						listDots.clear();
						llViewDots.removeAllViews();
					}
					//HJScorllADV.this.setBackgroundResource(R.drawable.nopic_shop);
					
				}
				
				imageAdapter.notifyDataSetChanged();
				userIndex = nowIndex;*/
				break;
			case REPLAY_ADV:
					if (app.mLoadImage != null) {
						app.mLoadImage.doTask();
					}
					if (advList == null) {
						return;
					}
					if (advList.list.size() > 1) {
						//nowIndexPage = imageView.getCurrentItem();
						
						if (nowIndexPage == 0) {// 大于总数。往左

							cFlowKeyDown = KeyEvent.KEYCODE_DPAD_RIGHT;
							addOrDown = true;
							
						} else if(nowIndexPage == listViews[userIndex].size() - 1){
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
				
				
				/*HJADVModel model;
				for (int i = 0; i < advList.list.size(); i++) {
					model = advList.list.get(i);
					//if (model.isDowload) {// 需要下载

						if (model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
							Bitmap bitmap = app.mLoadImage.memoryCache
									.getBitmapFromCache(model.getImageNetPath());
							if (bitmap != null) {//下载完成保存图片到本地
								
								OtherManager.saveAdvListToCache(mContext, advList);
								imageAdapter.notifyDataSetChanged();
							}
						}

					//} 
				}*/
				break;
			case NET_ERROR:
				Toast.makeText(mContext, mContext.getResources().getString(R.string.public_net_or_data_error), 5).show();//"网络或数据错误！请稍后再试"
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

	public class ImageAdapter extends PagerAdapter {
		

		public ImageAdapter() {
			
		}
		@Override
		public int getCount() {//返回页卡的数量
			int count  = 0;
			if (listViews[userIndex] == null) {
				return count;
			}
			count = listViews[userIndex].size();
			return count;
		}

		

		/*@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView image;
			if (convertView == null) {
				image = new ImageView(mContext);
				
			} else {
				image = (ImageView) convertView;
			}
			
			
			Bitmap originalImage;
			HJADVModel netImage = advList.list.get(position);
			if (netImage.getImageLocalPath() != null && !netImage.getImageLocalPath().equals("")) {// 本地地址不为空
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
			}
			
			
			image.setAdjustViewBounds(true);
			image.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			image.setScaleType(ScaleType.FIT_CENTER);
			image.setImageBitmap(originalImage);
			
			return image;
		}*/

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			if (arg1 >= listViews[userIndex].size()) {
				return;
			}
			((ViewGroup)arg0).removeView((View)arg2/*listViews[userIndex].get(arg1)*/);//删除页卡 
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}
		
	     // 跳转到每个页面都要执行的方法
	     
	    @Override
	    public void setPrimaryItem(View container, int position, Object object) {
	    	if (position >= listViews[userIndex].size()) {
				return;
			}
	    	View view = (View)listViews[userIndex].get(position);
	    	//HJADVModel netImage = advList.list.get(position);
	    	/*ImageView image = (ImageView)view.findViewById(R.id.iv_adv);
			Bitmap originalImage;
			
			if (netImage.getImageNetPath() != null && !netImage.getImageNetPath().equals("")) {
				originalImage = app.mLoadImage.getBitmapFromCache(netImage.getImageNetPath());
				if (originalImage == null) {
					//netImage.isDowload = true;
					//app.mLoadImage.addTask(netImage.getImageNetPath(), null, 0);
					originalImage = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.mindex);
				}
			}else{
				originalImage = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.mindex);
			}
			
			Drawable drawable = new BitmapDrawable(originalImage);
			image.setBackgroundDrawable(drawable);*/
			view.setTag(position);//netImage.getDataID());
			//view.setTag(R.id.tab_icon, netImage.getAdvType());
			view.setOnClickListener(advOnClickListener);
	    }
	    @Override
	    public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;//
	    }
	    
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			//这个方法用来实例化页卡
			
			((ViewGroup)arg0).addView(listViews[userIndex].get(arg1), 0);//添加页卡  
            return listViews[userIndex].get(arg1); 
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

	}

	
}
