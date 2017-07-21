package com.ihangjing.ZJYXForAndroid;

import java.io.StringReader;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.R.drawable;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alipay.sdk.app.PayTask;
import com.ihangjing.HJControls.HJPopFoodDetailView;
import com.ihangjing.HJControls.HJScorllADV;
import com.ihangjing.HJControls.RadioGroupEx;
import com.ihangjing.Model.CommentListModel;
import com.ihangjing.Model.CommentModel;
import com.ihangjing.Model.FoodAttrModel;
import com.ihangjing.Model.FoodInOrderModel;
import com.ihangjing.Model.FoodListModel;
import com.ihangjing.Model.FoodMakeModel;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.FoodTypeListModel;
import com.ihangjing.Model.FoodTypeModel;
import com.ihangjing.Model.FsgShopDetailModel;
import com.ihangjing.Model.MyCouponModel;
import com.ihangjing.Model.MyShopCart;
import com.ihangjing.Model.RoomTypeInfo;
import com.ihangjing.Model.RoomTypeListModel;
import com.ihangjing.Model.ShopDiscountItemView;
import com.ihangjing.Model.TagModel;
import com.ihangjing.Model.UserDetail;
import com.ihangjing.alipay.Keys;
import com.ihangjing.alipay.Result;
import com.ihangjing.alipay.SignUtils;
import com.ihangjing.common.HJInteger;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;
import com.ihangjing.wxpay.Constants;
import com.ihangjing.wxpay.MD5;
import com.ihangjing.wxpay.Util;
import com.tencent.mm.sdk.modelpay.PayReq;

public class ShopDetail extends HjActivity implements HttpRequestListener {

	// private Button backButton;
	private String ShopId;
	ProgressDialog progressDialog = null;

	private static final String TAG = "ShopDetailActivity";
	private static final double EARTH_RADIUS = 6378137;// 地球半径，米;
	private static final int DATE_DIALOG_ID = 398;
	protected static final int TIME_DIALOG_ID = 397;
	private UIHandler handler;
	// private ImageView tomapImageView;
	public double shoplat = Double.parseDouble("0.0"); // 30.244683620203194;
	public double shoplng = Double.parseDouble("0.0");// 120.14622688293457;

	private FsgShopDetailModel shopmodeDetailModel;

	// private TextView gotomapTextView;
	private String dialogStr;
	HttpManager localHttpManager = null;

	private ViewPager viewSwitcher;
	private ArrayList<View> listMainViews;
	protected boolean isReadFood = false;
	private RelativeLayout downloadRelativeLayout;
	private LayoutInflater inflater;
	// private View headView;
	// private TextView netErrorTextView;
	private View footView;
	private ProgressBar more_shop_pb;
	private TextView more_food_tv;
	private ListView foodListView;
	protected boolean isMoreShopViewClick;
	protected FoodListModel[] foodListModel;
	protected int listIndex = 0;
	private int pageindex = 1;
	private String typeidString = "0";
	private FoodListViewAdapter foodListAdapter;

	// private HJGridView typeGridView;
	public float totalFoodPrice;
	public int totalFoodCount;

	private int total;
	public FoodTypeListAdapter typelistAdapter;
	// private int typeIndex = 0;
	public TextView tvShopName;

	private Button btnBack;
	private Button btnRight;
	private int index;
	private Boolean isLogin = false;
	// public FoodTypeModel foodTypeModel;
	public LoadImage mLoadImage;
	public FoodModel foodModel;
	//private FoodAttrAdapter listFoodAttrAdapter;
	private LinearLayout llFoodAttr;
	private TextView tvFoodName;
	private ImageView ivImage;
	private TextView tvFoodTypeName;
	private OnClickListener addToShopCartListener;
	private OnClickListener delFromShopCartListener;
	private TextView tvFoodPriceCurrent;
	private TextView tvFoodDis;
	private TextView tvFoodNot;
	private TextView tvFoodRev;
	private TextView tvPublicPoint;
	protected EditText nowEdit;// 记录当前编辑的edit（输入数字）
	private int selectShopIndex;
	private FoodTypeListModel foodTypeList;
	private Button btnCancel;
	private TextView tvTitle;
	// private ListView shopListView;
	private double lon;
	private double lat;
	private String shoptype = "0";
	private String shopNameString;
	private String nearFilterString = "1500";
	private int shopPageIndex = 1;
	// public LoadImage mylLoadImage;
	private Button btnFavor;
	private ImageView telButton;
	private TextView gotomapTextView;

	private ListView typeListView;
	protected int typeIndex;
	private TextView tvSendMoney;
	private TextView tvSendTime;
	private TextView tvShopAddress;
	private TextView tvSentorg;
	private TextView tvBisnesstime;

	private TextView tvDistance;
	private TextView tvDesc;
	private ImageView iconImageView;
	/*private LinearLayout llShopInfo;
	private TextView tvShopTime;
	private TextView tvShopCommand;
	private TextView tvShopStartSend;
	private TextView tvShopTeSe;*/
	//private TextView llCallTel;
	private TextView tvGoCommandList;
	private ListView lvCommand;
	public CommentListModel commentListModel;
	private CommandListViewAdapter commandAdapter;
	private boolean isReadCommand;
	private RelativeLayout rlAll;
	private HJScorllADV adv;
	private TextView tvShopGood;
	private TextView tvShopOK;
	private TextView tvShopBad;
	private boolean isCommand = false;//是否提交了评论
	private LinearLayout llAddress;
	private TextView tvShopDiscount;
	private String errMsg;
	protected int commandType;
	private TextView conditionTextView1;
	private Button btnNext;
	private RelativeLayout rlBottom;
	private TextView tvShopNews;
	private LinearLayout llICON;
	private Button btnShopCart;
	private LinearLayout llDiscount;
	private RadioGroup rgTitle;
	private RadioButton rbFootList;
	private RadioButton rbShopInfo;
	private RelativeLayout imgShop;
	private ImageView ivW;
	private ImageView ivD;
	private RadioButton rbYD;
	private Gallery gView;
	private RadioGroup rgPayType;
	private EditText etPayPassword;
	private TextView etTime;
	private EditText etName;
	private EditText etPhone;
	private EditText etRemark;
	private TextView tvPrice;
	private ImageAdapter mYDAdapter;
	protected int payType = 1;
	protected int buyType;
	private Button btnGo;
	protected int eatType;
	private int mYear;
	private int mMonth;
	private int mDay;
	private EditText etNumber;
	protected int roomType;
	protected int selectRoom = 0;
	
	private StringBuffer wxpay;
	private PayReq wxpayReq;
	private RoomTypeListModel roomTypeListModel;
	private String orderidString;
	private String errorMSG;
	public float payMoney;
	private String payID;
	private String alipayInfo;
	public boolean isPayWX;
	public Map<String, String> resultunifiedorder;
	private TextView etDTime;
	private RelativeLayout llShopCart;
	private TextView tvRemove;
	private ListView lvShopCart;
	private TextView tvPacketFee;
	private shopCartListViewAdapter mShopCardAdadpter;
	private OnClickListener addToShopCartListener1;
	private OnClickListener delFromShopCartListener1;
	private OnClickListener removeFromShopCartListener;
	private TextView tvShopCartCount;
	protected Timer timerReadFoods;
	protected boolean isReqFeedFood;
	
	public ShopDetail() {
		shopmodeDetailModel = new FsgShopDetailModel();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate()");


		handler = new UIHandler();
		mLoadImage = new LoadImage(ShopDetail.this);
		//if(app.mShop.shopIndexInShopCart == null){
			app.mShop.shopIndexInShopCart = new HJInteger();
			app.mShop.shopIndexInShopCart.value = -1;
		//}
		//shopIndex = new HJInteger();
		//shopIndex.value = -1;
		initUI();
		
		addToShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shopmodeDetailModel.getStatus() < 1 && shopmodeDetailModel.getShopType() == 1) {
					Toast.makeText(ShopDetail.this, getResources().getString(R.string.shop_detail_close_notice)/*"商家休息中，无法订单！请换其他商家！"*/, 15)
							.show();
					return;
				}
				/*if (app.shopCartModel.list.size() > 0 && app.shopCartModel.list.get(0).getShopId() != Integer.parseInt(shopmodeDetailModel.getDataID())) {
					//不是同一个商家
					app.shopCartModel.clear();
				}*/
				/*if (shopmodeDetailModel.getDistance() > shopmodeDetailModel
						.getSendDistance()) {
					Toast.makeText(ShopDetail.this,
							"您到商家的距离超出了商家的派送范围，无法下单！请换其他商家", 15).show();
					return;
				}*/
				int pt = (Integer) v.getTag();
				FoodModel model = foodListModel[index].list.get(pt);
				/*if (model.listSize.size() > 0) {
					HJPopFoodDetailView popFoodDetailView = new HJPopFoodDetailView(ShopDetail.this, 
							rlAll, RelativeLayout.LayoutParams.FILL_PARENT, rlAll.getHeight(), 
							R.id.register_title_bar, model, shopmodeDetailModel);
					popFoodDetailView.Show();
					return;
				}*/
				
				
				app.mShop.setSendMoney(shopmodeDetailModel.getSendmoney());
				app.mShop.setFullFreeMoney(shopmodeDetailModel
						.getFreeSendMoney());
				app.mShop.setStartSendMoney(shopmodeDetailModel.getMinMoney());
				app.mShop.setLatitude(shopmodeDetailModel.getlat());
				app.mShop.setLongtude(shopmodeDetailModel.getlng());
				
				app.mShop
						.setSendDistance(shopmodeDetailModel.getSendDistance());
				app.mShop.setMaxKM(shopmodeDetailModel.getMaxKM());
				app.mShop.setMinKM(shopmodeDetailModel.getMinKM());
				app.mShop
						.setSendFeeAffKM(shopmodeDetailModel.getSendFeeAffKM());
				app.mShop.setSendFeeOfKM(shopmodeDetailModel.getSendFeeOfKM());
				app.mShop
						.setStartSendFee(shopmodeDetailModel.getStartSendFee());
				app.mShop.tagListModel = shopmodeDetailModel.tagListModel;
				app.mShop.setPacketFee(shopmodeDetailModel.getPackagefree());
				nowEdit = null;
				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.addFoodAttr(
						app.mShop, model, 0, 1)));
				setCondition();
				
			}
		};

		delFromShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nowEdit = null;
				int pt = (Integer) v.getTag();
				TextView text = (TextView) v.getTag(R.id.tab_label);
				text.setText(String.valueOf(app.shopCartModel.delFoodAttr(
						app.mShop, foodListModel[index].list.get(pt), 0)));
				setCondition();
			}
		};
		
		addToShopCartListener1 = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer)v.getTag();
				FoodInOrderModel model = (FoodInOrderModel)v.getTag(R.id.tab_icon);
				int count = app.shopCartModel.getFoodCountInAttr(model.getTogoId(),
						model.getId(), 0);
				if (count >= model.getStock()) {
					Toast.makeText(ShopDetail.this, getResources().getString(R.string.shop_cart_full)/*"已经超出了库存！"*/, 15)
					  .show(); 
					 return; 
				}
				app.shopCartModel.addFoodAttr(model, pt);
				setCondition();
				
			}
		};
		
		delFromShopCartListener1 = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer)v.getTag();
				FoodInOrderModel model = (FoodInOrderModel)v.getTag(R.id.tab_icon);
				pt = app.shopCartModel.delFoodAttr(model, pt);
				setCondition();
				
				
			}
		};
		
		removeFromShopCartListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer)v.getTag();
				FoodInOrderModel model = (FoodInOrderModel)v.getTag(R.id.tab_icon);
				app.shopCartModel.removeFood(model, pt);
				//text.setText("0");
				setCondition();
			}
		};

		tvTitle = (TextView) findViewById(R.id.title_bar_content_tv);
		tvTitle.setText(app.mShop.getName());
		foodTypeList = new FoodTypeListModel();
		
	}

	

	protected void Favor() {
		// TODO Auto-generated method stub
		UserDetail userDetail = OtherManager.getUserInfo(ShopDetail.this);
		if (app.userInfo == null || app.userInfo.userId.length() < 1
				|| app.userInfo.userId.equals("0")) {
			showDialog(333);
			return;
		}
		dialogStr = getResources().getString(R.string.public_send);//提交中。。。
		showDialog(322);

		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("togoid", String.valueOf(app.mShop.getId()));
		if (shopmodeDetailModel.getIsFavor() == 1) {
			localHashMap.put("op", "-1");
		}else{
			localHashMap.put("op", "1");
		}
		
		localHashMap.put("userid", app.userInfo.userId);

		localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
				"Android/SaveCollection.aspx?", localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}

	private void initUI() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// mSchedule = new F
		setContentView(R.layout.shop_detail);
		
		
		rlAll = (RelativeLayout)findViewById(R.id.rl_all);

		btnBack = (Button) findViewById(R.id.title_bar_back_btn);
		// btnBack.setVisibility(View.GONE);
		// btnBack.setBackgroundResource(R.drawable.title_bar_menu_button);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (viewSwitcher.getCurrentItem()) {
				case 0:

					finish();
					break;
				case 1:

					viewSwitcher.setCurrentItem(0);
					break;
				case 2:
					viewSwitcher.setCurrentItem(1);
					break;
				case 3:
					viewSwitcher.setCurrentItem(2);
					break;
				default:
					break;
				}

			}
		});
		btnFavor = (Button) findViewById(R.id.btn_fav_shop);
		//btnFavor.setVisibility(View.VISIBLE);
		btnFavor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Favor();
			}
		});
		btnRight = (Button) findViewById(R.id.title_bar_right_nor_btn);
		btnShopCart = (Button) findViewById(R.id.title_bar_right_btn);
		//btnRight.setVisibility(View.GONE);
		btnRight.setVisibility(View.GONE);
		btnFavor.setVisibility(View.VISIBLE);
		btnShopCart.setVisibility(View.GONE);
		// btnRight.setBackgroundResource(R.drawable.btn_red_selector);
		// btnRight.setPadding(4, 2, 2, 2);
		// btnRight.setText("");
		btnShopCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*switch (viewSwitcher.getCurrentItem()) {
				case 0:
					// 显示商家分类
					break;
				default:
					if (app.shopCartModel.list.size() == 0) {
						Toast.makeText(ShopDetail.this, "购物车为空！", 5).show();
						return;

					}

					Intent localIntent = new Intent("com.ihangjing.common.tap3");
					app.sendBroadcast(localIntent);
					break;
				}*/
				gotoShopCart();
			}
		});

		viewSwitcher = (ViewPager) findViewById(R.id.vs_select);
		listMainViews = new ArrayList<View>();

		// 商家列表视图
		View view1 = getLayoutInflater().inflate(R.layout.shop_detail_foodlist,
				null);
		/*llShopInfo = (LinearLayout) findViewById(R.id.ll_shop_info);
		llShopInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewSwitcher.setCurrentItem(1);
			}
		});
		tvShopTime = (TextView) findViewById(R.id.tv_shoptime);
		tvShopCommand = (TextView) findViewById(R.id.tv_command);
		tvShopStartSend = (TextView) findViewById(R.id.tv_startsend);*/
		
		llShopCart = (RelativeLayout)view1.findViewById(R.id.ll_shop_cart);
		llShopCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				llShopCart.setVisibility(View.GONE);
			}
		});
		tvRemove = (TextView)view1.findViewById(R.id.tv_remove);
		tvRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.shopCartModel.removeShopAllFoodWithShopId(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				setCondition();
			}
		});
		lvShopCart = (ListView)view1.findViewById(R.id.lv_shopcart);
		mShopCardAdadpter = new shopCartListViewAdapter();
		lvShopCart.setAdapter(mShopCardAdadpter);
		tvPacketFee = (TextView)view1.findViewById(R.id.tv_packet_fee);
		
		rbYD = (RadioButton)findViewById(R.id.rb_yd);
		if(app.mShop.getShopType() > 1){
			rbYD.setVisibility(View.VISIBLE);
		}
		rgTitle = (RadioGroup)findViewById(R.id.rg_opt);
		rgTitle.setVisibility(View.VISIBLE);
		rgTitle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_unre:
					viewSwitcher.setCurrentItem(0);;
					break;
				case R.id.rb_yd:
					viewSwitcher.setCurrentItem(1);
					break;
				case R.id.rb_re:
					if (rbYD.getVisibility() == View.VISIBLE) {
						viewSwitcher.setCurrentItem(2);
						
					}else{
						viewSwitcher.setCurrentItem(1);
					}
					
					break;
				
				default:
					break;
				}
			}
		});
		rbFootList = (RadioButton)findViewById(R.id.rb_unre);
		rbShopInfo = (RadioButton)findViewById(R.id.rb_re);
		downloadRelativeLayout = (RelativeLayout) view1
				.findViewById(R.id.foodlist_loadingdata_ll);
		downloadRelativeLayout.setVisibility(View.VISIBLE);
		rlBottom = (RelativeLayout)view1.findViewById(R.id.ll_bottom);
		rlBottom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(llShopCart.getVisibility() == View.GONE){
					llShopCart.setVisibility(View.VISIBLE);
					//lvShopCart.setVisibility(View.VISIBLE);
				}else{
					llShopCart.setVisibility(View.GONE);
					//lvShopCart.setVisibility(View.GONE);
				}
			}
		});
		
		conditionTextView1 = (TextView) view1
				.findViewById(R.id.tv_cart_info);
		tvShopCartCount = (TextView)view1.findViewById(R.id.tv_shop_count);
		btnNext = (Button) view1
				.findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(shopmodeDetailModel.getShopType() == 2){
					viewSwitcher.setCurrentItem(1);
				}else{
					gotoShopCart();
				}
				
			}
		});
		inflater = LayoutInflater.from(ShopDetail.this);

		tvFoodTypeName = (TextView) view1
				.findViewById(R.id.foodlist_TextView01);

		typeListView = (ListView) view1.findViewById(R.id.lv_type);
		typeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				pageindex = 1;
				typeIndex = position;
				Log.v(TAG, "getDataFromServer 1");
				typeidString = String.valueOf(foodTypeList.list.get(position)
						.getId());
				typelistAdapter.notifyDataSetChanged();
				if(timerReadFoods == null){
					timerReadFoods = new Timer();// 定时发送gps信息
					timerReadFoods.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//new UpdateLocation().start();
							if(isReqFeedFood && !isReadFood){
								isReqFeedFood = false;
								Message msg = new Message();
								msg.what = UIHandler.START_REEDFOODS;
								handler.sendMessage(msg);
							}
						}
					}, 1000L, 1000L);
				}
				if(isReqFeedFood){
					getDataFromServer(1);
					return;
				}
				isReqFeedFood = true;
				//getDataFromServer(1);
			}
		});

		// 底部的加载更多商家的页面View
		footView = inflater.inflate(R.layout.more_food_view, null);

		more_shop_pb = (ProgressBar) footView
				.findViewById(R.id.more_food_ProgressBar);
		more_food_tv = (TextView) footView
				.findViewById(R.id.more_food_textview);
		
		foodListModel = new FoodListModel[2];
		foodListModel[0] = new FoodListModel();
		foodListModel[1] = new FoodListModel();

		foodListView = (ListView) view1.findViewById(R.id.foodlist_listView1);

		foodListView.addFooterView(footView);

		// 重要:页脚读取更多数据的点击数据
		this.footView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (!isMoreShopViewClick) {
					Log.v(TAG, "isMoreShopViewClick is false");
					return;
				}
				if (foodListModel == null) {
					Log.v(TAG, "foodListModel == null");
					return;
				}
				if (foodListModel[listIndex].getPage() >= foodListModel[listIndex]
						.getTotal()) {
					return;
				}
				more_shop_pb.setVisibility(View.VISIBLE);// 显示
				more_food_tv.setVisibility(View.GONE);

				isMoreShopViewClick = false;
				int k = foodListModel[listIndex].getPage() + 1;

				// 读取下一页数据
				isMoreShopViewClick = false;// 防止被多次点击 同一个页码
				getDataFromServer(k);

				Log.v(TAG, "FoodList读取下一页数据,页码:" + k);
			}
		});
		
		foodListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				//if (scrollState ==  SCROLL_STATE_FLING) 
				{// 停止滚动
					int first = view.getFirstVisiblePosition();
					if (first <= 1) {
						rgTitle.setVisibility(View.VISIBLE);
					} else {
						rgTitle.setVisibility(View.GONE);
					}

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				/*if (firstVisibleItem <= 1) {
					llShopInfo.setVisibility(View.VISIBLE);
				} else {
					llShopInfo.setVisibility(View.GONE);
				}*/
				if (!isReadFood && foodListModel[listIndex].getPage() < foodListModel[listIndex].getTotal() && 
						firstVisibleItem + visibleItemCount >= totalItemCount) {
					pageindex = foodListModel[listIndex].getPage() + 1;
					GetFoodList();
				}
				mLoadImage.doTask();

			}
		});

		listMainViews.add(view1);
		
		if(rbYD.getVisibility() == View.VISIBLE){
			View vYD = getLayoutInflater().inflate(R.layout.shop_detail_yuding, null);
			gView = (Gallery)vYD.findViewById(R.id.ll_g);
			gView.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if (selectRoom != position) {
						selectRoom  = position;
						mYDAdapter.notifyDataSetChanged();
					}
					mLoadImage.doTask();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
			/*gView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					selectRoom  = position;
				}
			});*/
			//gView.setOn
			//mYDAdapter = new ImageAdapter(ShopDetail.this);
			//gView.setAdapter();
			etNumber = (EditText)vYD.findViewById(R.id.et_number);
			etPayPassword = (EditText)vYD.findViewById(R.id.pay_password_et);
			rgPayType = (RadioGroup)vYD.findViewById(R.id.rg_pay_type);
			rgPayType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch (checkedId) {
					case R.id.rb_pay0://到店付款
						payType = 4;
						etPayPassword.setVisibility(View.GONE);
						break;
					case R.id.rb_pay1://支付宝
						payType = 1;
						etPayPassword.setVisibility(View.GONE);
						break;
					case R.id.rb_pay2://微信
						payType = 5;
						etPayPassword.setVisibility(View.GONE);
						break;
					case R.id.rb_pay3://账户余额
						payType = 3;
						etPayPassword.setVisibility(View.VISIBLE);
						break;
					default:
						break;
					}
				}
			});
			RadioGroup rgYType = (RadioGroup)vYD.findViewById(R.id.rg_y_type);
			rgYType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch (checkedId) {
					case R.id.rb_no_food:
						buyType = 0;
						//btnGo.setText("预订");
						//rgPayType.setVisibility(View.GONE);
						tvPrice.setText(String.format("总额：￥%.2f", 0.00f));
						break;
					case R.id.rb_food:
						buyType = 1;
						//btnGo.setText("预订结算");
						//rgPayType.setVisibility(View.VISIBLE);
						tvPrice.setText(String.format("总额：￥%.2f", totalFoodPrice));
						break;
					default:
						break;
					}
				}
			});
			
			RadioGroup rgEType = (RadioGroup)vYD.findViewById(R.id.rg_eat_type);
			rgEType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch (checkedId) {
					case R.id.rb_t1://午餐
						eatType = 0;
						break;
					case R.id.rb_t2://晚餐
						eatType = 1;
						
						break;
					default:
						break;
					}
				}
			});
			
			RadioGroup rgRoomType = (RadioGroup)vYD.findViewById(R.id.rg_type);
			rgRoomType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch (checkedId) {
					case R.id.rb_dt://大厅
						roomType = 0;
						selectRoom = 0;
						mYDAdapter.notifyDataSetChanged();
						//selectRoom = 0;
						break;
					case R.id.rb_bx://包厢
						roomType = 1;
						selectRoom = 0;
						mYDAdapter.notifyDataSetChanged();
						
						break;
					default:
						break;
					}
				}
			});
			
			etTime = (TextView)vYD.findViewById(R.id.et_time);
			
			etTime.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(DATE_DIALOG_ID);
				}
			});
			etName = (EditText)vYD.findViewById(R.id.et_name);
			etPhone = (EditText)vYD.findViewById(R.id.et_phone);
			etRemark = (EditText)vYD.findViewById(R.id.et_remark);
			etDTime = (TextView)vYD.findViewById(R.id.et_dtime);
			etDTime.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(TIME_DIALOG_ID);
				}
			});
			Date d2 = new Date(System.currentTimeMillis() + 30 * 60 * 1000);//获取当前时间
			mYear = d2.getYear() + 1900;
			mMonth = d2.getMonth();
			mDay = d2.getDate();
			mHour = d2.getHours();
			mMinute = d2.getMinutes();
			updateDateDisplay();
			updateTimeDisplay();
			btnGo = (Button)vYD.findViewById(R.id.title_bar_right_btn);
			btnGo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (app.userInfo.userId == null
							|| app.userInfo.userId.equals("") || app.userInfo.userId.equals("0")) {
						//
						Intent localIntent4 = new Intent(ShopDetail.this,
								Login.class);
						localIntent4.putExtra("isReturn", true);
						ShopDetail.this.startActivity(localIntent4);
						Toast.makeText(ShopDetail.this, "请您先登陆", 5).show();
						return;
					}
					RoomTypeInfo room;
					if (roomType == 0){ 
						if(roomTypeListModel.list1.size() == 0) {
							Toast.makeText(ShopDetail.this, "该商家无大厅数据，无法预订", 15).show();
							return;
						}
						room = roomTypeListModel.list1.get(selectRoom);
					}else{ 
						if(roomTypeListModel.list2.size() == 0){
							Toast.makeText(ShopDetail.this, "该商家无包厢数据，无法预订", 15).show();
							return;
						}
						room = roomTypeListModel.list2.get(selectRoom);
					}
					app.shopCartModel.setRoomType(roomType);
					app.shopCartModel.setRoomID(room.getSortID());
					app.shopCartModel.setRoomName(room.getSortName());
					Date d2 = new Date(System.currentTimeMillis());//获取当前时间
					if(mYear == d2.getYear() + 1900 && mMonth == d2.getMonth() && mDay == d2.getDate()){
						//判断现在的时间
						if (d2.getHours() > 14 && eatType == 0) {//不支持今天预定午餐
							Toast.makeText(ShopDetail.this, "今天的午餐预定时间已过，无法预定", 15).show();
							return;
						}else if(d2.getHours() > 22 && eatType == 1){//超过晚上八点不支持预定
							Toast.makeText(ShopDetail.this, "今天的晚餐预定时间已过，无法预定", 15).show();
							return;
						}
					}
					app.shopCartModel.setTimeType(eatType);
					app.shopCartModel.setEattime(etTime.getText().toString() + " " + etDTime.getText().toString() + ":00");
					
					if(etName.getText().toString().length() < 1){
						Toast.makeText(ShopDetail.this, "请输入您的姓名", 15).show();
						etName.setError("请输入您的姓名");
						return;
					}
					app.shopCartModel.setCustName(etName.getText().toString());
					if(!app.checkMobilePhone(etPhone.getText().toString())){
						Toast.makeText(ShopDetail.this, "请输入正确的手机号码", 15).show();
						etPhone.setError("请输入正确的手机号码");
						return;
					}
					app.shopCartModel.setPhone(etPhone.getText().toString());
					if(etNumber.getText().toString().length() < 1 || Integer.parseInt(etNumber.getText().toString()) < 1){
						Toast.makeText(ShopDetail.this, "请输入正确的就餐人数", 15).show();
						etNumber.setError("请输入正确的就餐人数");
						return;
					}
					
					app.shopCartModel.setPeople(Integer.parseInt(etNumber.getText().toString()));
					if (buyType == 1) {
						//判断是否点菜了
						if(app.shopCartModel.getFoodCountWithShop(app.mShop) == 0){
							Toast.makeText(ShopDetail.this, "您的购物车中没有菜品，请选购菜品！", 15).show();
							viewSwitcher.setCurrentItem(0);
							return;
						}
						//判断是否达到了最低消费标准
						if(totalFoodPrice < room.minfee){
							Toast.makeText(ShopDetail.this, "没有达到最低消费标准：￥" + String.valueOf(room.minfee), 15).show();
							viewSwitcher.setCurrentItem(0);
							return;
						}
						
						
					}else{//不点菜，那么就是到店付款
						if(app.shopCartModel.getFoodCountWithShop(app.mShop) == 0){
							//购物车为空
							//app.shopCartModel.setShopID(String.valueOf(app.mShop.getId()));
						}
						app.shopCartModel.setShopID(String.valueOf(app.mShop.getId()));
						app.shopCartModel.setSLat(app.mShop.getLatitude());
						app.shopCartModel.setSLng(app.mShop.getLongtude());
						//app.shopCartModel.setPayMode("4");
						//app.shopCartModel.setPayPassword("");
					}
					if(payType == 3){
						if(etPayPassword.getText().toString().length() < 1){
							Toast.makeText(ShopDetail.this, "请输入您的账户支付密码", 15).show();
							etPayPassword.setError("请输入您的账户支付密码");
							return;
						}
						
						//设置支付密码
						app.shopCartModel.setPayPassword(etPayPassword.getText().toString());
					}else{
						app.shopCartModel.setPayPassword("");
					}
					//
					app.shopCartModel.setPayMode(String.valueOf(payType));
					//app.
					app.shopCartModel.setUserid(app.userInfo.userId);
					app.shopCartModel.setLat("0.0");
					app.shopCartModel.setLng("0.0");
					app.shopCartModel.setNote(etRemark.getText().toString());
					app.shopCartModel.setBuyType(buyType);
					app.shopCartModel.setOrderType(1);//
					addOrder();
					
				}
			});
			
			tvPrice = (TextView)vYD.findViewById(R.id.tv_price);
			listMainViews.add(vYD);
			wxpayReq = new PayReq();
			wxpay=new StringBuffer();

			app.msgApi.registerApp(Constants.APP_ID);
			
		}

		View shopView = getLayoutInflater().inflate(R.layout.shop_detail_info, null);
		adv = (HJScorllADV)shopView.findViewById(R.id.iv_adv);
		ivW = (ImageView)shopView.findViewById(R.id.iv_w);
		ivD = (ImageView)shopView.findViewById(R.id.iv_d);
		imgShop = (RelativeLayout)shopView.findViewById(R.id.itemImage);
		llDiscount = (LinearLayout)shopView.findViewById(R.id.ll_discount);
		llICON = (LinearLayout)shopView.findViewById(R.id.ll_icon);
		tvSendMoney = (TextView) shopView.findViewById(R.id.shopdetail_send_money);
		TextView tvGoFoodList = (TextView)shopView.findViewById(R.id.btn_go_foodlist);
		tvGoFoodList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewSwitcher.setCurrentItem(0);
			}
		});
		
		tvGoCommandList = (TextView) shopView.findViewById(R.id.btn_go_command);
		TextView tvGoShopZ = (TextView)shopView.findViewById(R.id.btn_sz);
		tvGoShopZ.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopDetail.this, PopAdvActivity.class);
				intent.putExtra("showType", 1);
				intent.putExtra("shopID", shopmodeDetailModel.getId());
				startActivity(intent);
			}
		});
		
		llAddress = (LinearLayout)shopView.findViewById(R.id.ll_show_on_map);
		llAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(shopmodeDetailModel.getlat() == null || shopmodeDetailModel.getlat().length() == 0 ||
						shopmodeDetailModel.getlng() == null || shopmodeDetailModel.getlng().length() == 0)
				{
					Toast.makeText(ShopDetail.this, "暂时无法获取商家定位信息，无法查看！", 15).show();
					return;
				}
				//进入商家所在位置地图
				Intent intent = new Intent(ShopDetail.this, ShowOnMap.class);
				intent.putExtra("lat", Double.parseDouble(shopmodeDetailModel.getlat()));
				intent.putExtra("lng", Double.parseDouble(shopmodeDetailModel.getlng()));
				intent.putExtra("shopname", shopmodeDetailModel.getTogoName());
				startActivity(intent);
			}
		});
		tvShopName = (TextView) shopView.findViewById(R.id.tv_shopname);
		tvShopName.setText(app.mShop.getName());
		tvShopNews=(TextView)shopView.findViewById(R.id.tv_news);
		//llCallTel = (TextView) view1.findViewById(R.id.LinearLayout13);
		telButton = (ImageView) shopView.findViewById(R.id.shopdetail_tel);// 商家电话
		telButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 拨打商家电话
				AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(
						ShopDetail.this).setTitle(getResources().getString(R.string.public_notice))
						.setMessage(getResources().getString(R.string.public_call_tel) + shopmodeDetailModel.getTel());//"拨打电话：%s订餐"
				DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						Uri localUri = Uri.parse(String.format("tel:%s",
								shopmodeDetailModel.getTel()));
						Intent localIntent1 = new Intent(
								"android.intent.action.CALL", localUri);
						startActivity(localIntent1);
					}
				};

				localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok), local3)
						.setNegativeButton(getResources().getString(R.string.public_cancel), null).create();
				localBuilder1.create().show();
			}
		});
		tvShopDiscount = (TextView)shopView.findViewById(R.id.tv_discount);
		
		tvBisnesstime = (TextView) shopView
				.findViewById(R.id.shopdetail_senttime1);// 准备时间
		tvShopAddress = (TextView) shopView.findViewById(R.id.shopdetail_address);// 地址
		tvSentorg = (TextView) shopView.findViewById(R.id.shopdetail_sentorg);// 商家公告
		OnClickListener tvOnClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId())
				{
				case R.id.tv_good:
					commandType = 1;
					sendCommand("PType");
					break;
				case R.id.tv_ok:
					commandType = 2;
					sendCommand("RcvType");
					break;
				case R.id.tv_bad:
					commandType = 3;
					sendCommand("IsCallCenter");
					break;
				}
			}
		};
		tvShopGood = (TextView)shopView.findViewById(R.id.tv_good);
		tvShopGood.setOnClickListener(tvOnClick);
		tvShopOK = (TextView)shopView.findViewById(R.id.tv_ok);
		tvShopOK.setOnClickListener(tvOnClick);
		tvShopBad = (TextView)shopView.findViewById(R.id.tv_bad);
		tvShopBad.setOnClickListener(tvOnClick);
		tvSendTime = (TextView) shopView.findViewById(R.id.shopdetail_senttime);// 配送时间
		//tvShopTeSe = (TextView) view1.findViewById(R.id.tv_tese);// 商家特色
		tvDistance = (TextView) shopView.findViewById(R.id.shopdetail_distance);// 距离
		tvDesc = (TextView) shopView.findViewById(R.id.shopdetail_desc);// 商家简介
		iconImageView = (ImageView) shopView.findViewById(R.id.shopdetail_icon);// 认证商家
		//listMainViews.add(view1);
		
		// 菜品列表视图
		

		view1 = getLayoutInflater().inflate(R.layout.comment_list, null);
		lvCommand = (ListView) view1.findViewById(R.id.lv_list);
		lvCommand.addHeaderView(shopView);
		commentListModel = new CommentListModel();
		commandAdapter = new CommandListViewAdapter();
		lvCommand.setAdapter(commandAdapter);
		lvCommand.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (commentListModel.getPage() < commentListModel.getTotal() && 
						firstVisibleItem + visibleItemCount >= totalItemCount) {
					
					GetShopCommandList(commentListModel.getPage() + 1);
				}
				mLoadImage.doTask();
			}
		});
		listMainViews.add(view1);

		viewSwitcher.setAdapter(new HJPagerAdapter(listMainViews));

		viewSwitcher.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				/*if (arg0 == 1) {
					btnShopCart.setVisibility(View.GONE);
					btnFavor.setVisibility(View.VISIBLE);
				}else if (arg0 == 0) {
					btnShopCart.setVisibility(View.VISIBLE);
					btnFavor.setVisibility(View.GONE);
				}else{
					btnShopCart.setVisibility(View.GONE);
					btnFavor.setVisibility(View.GONE);
				}*/
				switch (arg0) {
				case 0:
					tvTitle.setText(shopmodeDetailModel.getname());
					rbFootList.setChecked(true);
					break;
				case 1:
					if (rbYD.getVisibility() == View.VISIBLE) {
						rbYD.setChecked(true);
					}else{
						rbShopInfo.setChecked(true);
						tvTitle.setText(getResources().getString(R.string.shop_detail_info));
						if(commentListModel.list.size() == 0){
							GetShopCommandList(1);
						}
					}
					
					
					break;
				case 2:
					rbShopInfo.setChecked(true);
					tvTitle.setText(getResources().getString(R.string.shop_detail_info));
					if(commentListModel.list.size() == 0){
						GetShopCommandList(1);
					}
					/*tvTitle.setText(getResources().getString(R.string.shop_detail_command));
					rgTitle.setVisibility(View.VISIBLE);
					if (app.mShop.isUpdateFoodList) {
						app.mShop.isUpdateFoodList = false;
						GetFoodTypeListData();
					}
					if(commentListModel.list.size() == 0){
						GetShopCommandList(1);
					}*/
					break;
				case 3:
					tvTitle.setText(getResources().getString(R.string.shop_detail_food_detail));

					if (foodModel == null) {
						if (foodListModel[index].list.size() == 0) {
							return;
						}
						foodModel = foodListModel[index].list.get(0);
						if (foodModel == null) {
							return;
						}

					}
					nowEdit = null;
					tvFoodDis.setText(foodModel.getDisc());
					tvFoodNot.setText(foodModel.getNotice());
					tvFoodName.setText(foodModel.getFoodName());
					tvPublicPoint.setText(getResources().getString(R.string.shop_detail_public_point) + foodModel.getPublicPoint());
					// ivImage.setTag(foodModel.getImageNetPath());
					if (foodModel.getImageNetPath() != null
							&& !foodModel.getImageNetPath().equals("")) {
						Bitmap bitmap = mLoadImage.getBitmap(foodModel
								.getImageNetPath());
						if (bitmap != null) {
							Drawable drawable = new BitmapDrawable(bitmap);
							ivImage.setBackgroundDrawable(drawable);
						} else {
							ivImage.setBackgroundResource(R.drawable.icon);
						}
					} else {
						ivImage.setBackgroundResource(R.drawable.icon);
					}

					//setAdapter(listFoodAttrAdapter);
					tvFoodPriceCurrent.setText(String.format(getResources().getString(R.string.public_all_price) + getResources().getString(R.string.public_moeny_0) +
							"%.2f" + getResources().getString(R.string.public_moeny),
							app.shopCartModel.getFoodPrice(app.mShop,
									foodModel.getFoodId())));
					break;
				default:
					break;
				}
				if (arg0 == 1) {

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

		app.mShop.isUpdate = false;
		getShopDetail();
		GetFoodTypeListData();

	}
	private void addOrder() {
		// 生成提交订单的json
		String ordermodelstring = "";

		ordermodelstring = app.shopCartModel.beanToStringFix();
		String ordermodelstringfix = ordermodelstring;

		ordermodelstringfix = ordermodelstring.replace("\\\"", "\"");
		ordermodelstringfix = ordermodelstringfix.replace("\\", "");
		ordermodelstringfix = ordermodelstringfix.replace("\"{", "{");
		ordermodelstringfix = ordermodelstringfix.replace("}\"", "}");
		ordermodelstringfix = ordermodelstringfix.replace("\"[", "[");
		ordermodelstringfix = ordermodelstringfix.replace("]\"", "]");

		Log.v(TAG, "ordermodelstring:" + ordermodelstringfix);
		// ordermodelstringfix = URLEncoder.encode(ordermodelstringfix,
		// "GB2312");
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		
		
		localHashMap.put("ordermodel", "[" + ordermodelstringfix + "]");
		//localHashMap.put("bid", app.mSection.SectionID);

		localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
				"Android/SubmitBookOrder.aspx?", localHashMap, 1, 9);
		localHttpManager.postRequest();

		Log.v(TAG, "[" + ordermodelstringfix + "]");
	}
	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Date d2 = new Date(System.currentTimeMillis() + 30 * 60 * 1000);//获取当前时间
			if (year < d2.getYear() + 1900) {
					//
				showDialog(DATE_DIALOG_ID);
				Toast.makeText(ShopDetail.this, "您选择的日期必须是今天或以后的日期！", 15).show();
				return;
			}else if(year == d2.getYear() + 1900){
				if (monthOfYear < d2.getMonth()) {
					showDialog(DATE_DIALOG_ID);
					Toast.makeText(ShopDetail.this, "您选择的日期必须是今天或以后的日期！", 15).show();
					return;
				}else if(monthOfYear == d2.getMonth()){
					if(dayOfMonth < d2.getDate()){
						showDialog(DATE_DIALOG_ID);
						Toast.makeText(ShopDetail.this, "您选择的日期必须是今天或以后的日期！", 15).show();
						return;
					}
				}
			}
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			updateDateDisplay();
		}

		
	};
	protected int mHour;
	protected int mMinute;
	
	private void updateDateDisplay() {
		// TODO Auto-generated method stub
		String value = (new StringBuilder().append(mYear).append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay)).toString();
		
			
		
		etTime.setText(value);
			
		
	}
	
	/**
	 * 时间控件事件
	 */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateTimeDisplay();
		}
	};
	//public HJInteger shopIndex;
	private float pacektFee;
	private float sendFee;
	public int checkSpecIndex = 0;//当前选择规格的商品
	
	/**
	 * 更新时间显示
	 */
	private void updateTimeDisplay() {
		
			String value = (new StringBuilder().append(mHour).append(":")
					.append((mMinute < 10) ? "0" + mMinute : mMinute)).toString();
			
			etDTime.setText(value);
			
		
	}
	
	protected void gotoShopCart() {
		// TODO Auto-generated method stub
		if (app.shopCartModel.list.size() == 0) {
			Toast.makeText(ShopDetail.this, getResources().getString(R.string.shop_detail_shopcart_empt), 5).show();
			return;

		}
		app.shopCartModel.setOrderType(0);
		Intent intent = new Intent(ShopDetail.this, ShopCart.class);
		startActivity(intent);
	}

	public void sendCommand(String type) {
		if(app.userInfo == null || app.userInfo.userId.length() == 0){
			Toast.makeText(ShopDetail.this, "登录后才可以点赞！", 10).show();
			return;
		}
		if (!isCommand) {
			isCommand  = true;
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("shopid", String.valueOf(app.mShop.getId()));
			localHashMap.put("Field", type);
			
			//if (app.userInfo != null && app.userInfo.userId.length() > 0) {
				localHashMap.put("userid", app.userInfo.userId);
			//}

			localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
					"Android/shopPraise.aspx?", localHashMap, 2, 7);
			localHttpManager.getRequeest();
		}else{
			Toast.makeText(ShopDetail.this, getResources().getString(R.string.com_already)/*你已经评价过了*/, 10).show();
		}
		
	}

	public void getShopDetail() {
		dialogStr = getResources().getString(R.string.public_get_data_notice);//"数据加载中...";
		showDialog(322);
		// 开始获取数据
		if (app.useLocation != null) {
			lat = app.useLocation.getLat();
			lon = app.useLocation.getLon();
		} else {
			lon = 0.0;
			lat = 0.0;
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("shopid", String.valueOf(app.mShop.getId()));
		localHashMap.put("lat", String.valueOf(lat));
		localHashMap.put("lng", String.valueOf(lon));
		if (app.userInfo != null && app.userInfo.userId.length() > 0) {
			localHashMap.put("userid", app.userInfo.userId);
		}

		localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
				"Android/GetShopDetailByShopId.aspx?", localHashMap, 2, 6);
		localHttpManager.getRequeest();
	}
	
	public void getShopYD() {
		/*dialogStr = getResources().getString(R.string.public_get_data_notice);//"数据加载中...";
		showDialog(322);
		// 开始获取数据
		
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("shopid", String.valueOf(app.mShop.getId()));
		

		localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
				"Android/GetTableList.aspx?", localHashMap, 2, 8);
		localHttpManager.getRequeest();*/
	}

	protected void getDataFromServer(int k) {
		// TODO Auto-generated method stub
		pageindex = k;
		GetFoodList();
	}
	public void GetShopCommandList(int page) {

		if(!isReadCommand){
			isReadCommand = true;
			Log.v(TAG, "连接网络获取数据开始");

			// 链接网络保存数据
			HashMap<String, String> localHashMap = new HashMap<String, String>();

			localHashMap.put("shopid", shopmodeDetailModel.getId());
			localHashMap.put("pageindex", String.valueOf(page));
			localHashMap.put("pagesize", "20");

			localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
					"Android/reviewlist.aspx?", localHashMap, 2, 5);

			localHttpManager.postRequest();

			Log.v(TAG, "tt获取第一页数据");
		}
		
		
	}

	public void GetFoodList() {
		if (!isReadFood) {
			isReadFood = true;
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("pageindex", String.valueOf(pageindex));

			localHashMap.put("shopsortid", typeidString);
			localHashMap.put("pagesize", "10");
			localHashMap.put("shopid", String.valueOf(app.mShop.getId()));
			localHashMap.put("pagesize", "10");

			localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
					"/Android/GetFoodListByShopId.aspx?", localHashMap, 2, 3);
			localHttpManager.getRequeest();
		}
		
	}

	private void GetFoodInfo(int viewID) {
		// TODO Auto-generated method stub
		HashMap<String, String> localHashMap = new HashMap<String, String>();

		localHashMap.put("id", String.valueOf(viewID));

		localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
				"/Android/Fooddetail.aspx?", localHashMap, 2, 4);
		localHttpManager.getRequeest();
	}

	public void GetFoodTypeListData() {

		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("shopid", String.valueOf(app.mShop.getId()));
		// localHashMap.put("pagesize", "30");
		// localHashMap.put("pageindex", "1");

		localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
				"/Android/GetFoodTypeListByShopId.aspx?", localHashMap, 2, 2);
		localHttpManager.getRequeest();

	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);

			DialogInterface.OnKeyListener okl = new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface paramDialogInterface,
						int paramInt, KeyEvent paramKeyEvent) {
					// 取消加载
					if ((paramInt == 4)
							&& (ShopDetail.this.localHttpManager != null)) {
						ShopDetail.this.localHttpManager.cancelHttpRequest();
					}
					return false;
				}
			};

			dialog.setOnKeyListener(okl);

			return dialog;
		}else if (paramInt == 333) {
			dialog = null;

			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)).setMessage(getResources().getString(R.string.public_login_notice));//"请先登陆");

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					Intent localIntent4 = new Intent(ShopDetail.this,
							Login.class);
					ShopDetail.this.startActivity(localIntent4);
				}
			};

			DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					removeDialog(333);
				}
			};

			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.login_label_signin), local3)
					.setNegativeButton(getResources().getString(R.string.public_cancel), local2).create();

			return dialog;
		}else if(paramInt == DATE_DIALOG_ID){
		
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}else if(paramInt == TIME_DIALOG_ID){
			return new TimePickerDialog(ShopDetail.this, mTimeSetListener, mHour, mMinute, true);
		}
		return dialog;
	}

	private class UIHandler extends Handler {
		protected static final int START_REEDFOODS = 18;
		// static final int DO_WITH_DATA = 0; // 定义消息类型
		// static final int SET_SHOP_DETAIL = 12;
		public static final int NET_ERROR = -1;
		public static final int FAVOR_SUCCES = 2;
		public static final int FAVOR_ALREADY = -2;
		public static final int FAVOR_ERROR = -3;
		public static final int GET_TYPE_SUCESS = 1;
		public static final int FOOD_DOWNLOAD_SUCCESS = 3;
		public static final int FOOD_DOWNLOAD_SUCCESS_ONLY_NOTIFY = 4;
		public static final int GET_FOOD_DETAIL = 5;
		public static final int SET_SHOP_DETAIL = 6;
		public static final int NO_COMMAND = -4;
		public static final int GET_COMMAND_SUCCESS = 7;
		public static final int COMMAND_SUCESS = 8;
		public static final int COMMAND_FAIL = -5;
		public static final int REMOVE_FAVOR_SUCCES = 9;
		public static final int GET_SHOP_ROOM = 10;
		public static final int ORDER_SUBMIT_FAILD = 11;
		public static final int ORDER_SUBMIT_SUCCESS = 12;
		protected static final int RQF_PAY = 13;
		public static final int GETPAYID_SUCCESS = 14;
		public static final int GETPAYID_FAILD = 15;
		public static final int GET_PREPAY_ID_SUCESS = 16;
		public static final int GET_PREPAY_ID_FAILD = 17;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(322);
			switch (msg.what) {
			case START_REEDFOODS:
				getDataFromServer(1);
				break;
			case GETPAYID_SUCCESS:
				
				gotoOnLinePay();
				break;
			case GETPAYID_FAILD:
				OtherManager.Toast(ShopDetail.this, getString(R.string.pay_load_id_error)/*"获取支付数据失败！"*/);
				break;
			case  GET_PREPAY_ID_FAILD:
				Toast.makeText(ShopDetail.this, errorMSG + getString(R.string.pay_wx_pay_failed)/*"微信支付失败，请稍后再试！"*/, 15).show();
				GoOrderDetail();
				clearOrderInf();
				return;
			case GET_PREPAY_ID_SUCESS:
				
				if (resultunifiedorder == null) {
					removeDialog(322);
					Toast.makeText(ShopDetail.this, getString(R.string.pay_load_wx_failed)/*"调用微信支付失败，请稍后再试！"*/, 15).show();
					GoOrderDetail();
					clearOrderInf();
					return;
				}
				genPayReq();
				sendPayReq();
				removeDialog(322);
				app.newOrderID = orderidString;
				app.showOrderType = 0;
				isPayWX = true;
				clearOrderInf();
				
				break;
			case RQF_PAY:
				Result.sResult = (String) msg.obj;
				Toast.makeText(ShopDetail.this, Result.getResult(),
						Toast.LENGTH_SHORT).show();
				GoOrderDetail();
				clearOrderInf();
				break;
			case ORDER_SUBMIT_SUCCESS: {

				Log.v(TAG, "case ORDER_SUBMIT_SUCCESS:");

				//removeDialog(322);
				/*if (payType == 1 || payType == 5) {//调用支付宝接口
					//payMoney = totalFoodPrice;
					gotoOnLinePay();
					//在线支付隐藏下面的
					
					
				}else{
					//GoOrderDetail();
					//app.shopCartModel.clear();
					//finish();
					
				}*/
				clearOrderInf();
				GoOrderDetail();
				/*app.shopCartModel.clear();//.list.clear();
				//GoOrderDetail();
				nextScrollView.setVisibility(View.GONE);
				firstView.setVisibility(View.VISIBLE);
				//btnBacke.setVisibility(View.GONE);
				foodListView.setVisibility(View.GONE);
				nextBuuton1.setVisibility(View.GONE);
				tvTotalPrice.setVisibility(View.GONE);*/
				break;
			}
			case ORDER_SUBMIT_FAILD: {
				j_showDialog(errorMSG);
				OtherManager.Toast(ShopDetail.this, getResources().getString(R.string.shop_cart_add_order_faild)/*"提交订单失败，请稍候再试！"*/);
				break;
			}
			case COMMAND_FAIL:
				Toast.makeText(ShopDetail.this, errMsg, 15).show();
				break;
			case COMMAND_SUCESS:
				switch (commandType) {
				case 1:
					shopmodeDetailModel.setGoodCount(shopmodeDetailModel.getGoodCount() + 1);
					tvShopGood.setText(String.valueOf(shopmodeDetailModel.getGoodCount()));
					break;
				case 2:
					shopmodeDetailModel.setOkCount(shopmodeDetailModel.getOkCount() + 1);
					tvShopOK.setText(String.valueOf(shopmodeDetailModel.getOkCount()));
					break;
				case 3:
					shopmodeDetailModel.setBadCount(shopmodeDetailModel.getBadCount() + 1);
					tvShopBad.setText(String.valueOf(shopmodeDetailModel.getBadCount()));
					break;
				default:
					break;
				}
				shopmodeDetailModel.UpdataAvGrade();
				/*if (shopmodeDetailModel.getAvGrade() < 0) {
					tvShopCommand.setText(getResources().getString(R.string.com_no_data));//"暂无评论");
				}else{
					tvShopCommand.setText(String.format("%.0f%%",
							shopmodeDetailModel.getAvGrade()*100));
				}*/
				break;
			case NO_COMMAND:
				Toast.makeText(ShopDetail.this, getResources().getString(R.string.com_no_data_1)/*"该商家暂无评论信息"*/, 15).show();
				break;
			case GET_SHOP_ROOM:
				if (mYDAdapter == null) {
					mYDAdapter = new ImageAdapter(ShopDetail.this);
					gView.setAdapter(mYDAdapter);
				}else{
					mYDAdapter.notifyDataSetChanged();
				}
				break;
			case GET_COMMAND_SUCCESS:
				commandAdapter.notifyDataSetChanged();
				break;
			case GET_FOOD_DETAIL:
				if (viewSwitcher.getCurrentItem() == 1) {
					tvFoodName.setText(foodModel.getFoodName());
					Drawable drawable = new BitmapDrawable(getBitmap(foodModel));
					ivImage.setBackgroundDrawable(drawable);
					//setAdapter(listFoodAttrAdapter);
				} else {
					viewSwitcher.setCurrentItem(1);
				}

				break;
			case FOOD_DOWNLOAD_SUCCESS:
				if(app.buyFoodList != null && app.buyFoodList.size() > 0){
					if (shopmodeDetailModel.getStatus() < 1 && shopmodeDetailModel.getShopType() == 1) {
						Toast.makeText(ShopDetail.this, getResources().getString(R.string.shop_detail_close_notice)/*"商家休息中，无法订单！请换其他商家！"*/, 15)
								.show();
						app.buyFoodList.clear();
					}else{
						/*if (app.shopCartModel.list.size() > 0 && app.shopCartModel.list.get(0).getShopId() != Integer.parseInt(shopmodeDetailModel.getDataID())) {
						//不是同一个商家
						app.shopCartModel.clear();
						}*/
						/*if (shopmodeDetailModel.getDistance() > shopmodeDetailModel
								.getSendDistance()) {
							Toast.makeText(ShopDetail.this,
									"您到商家的距离超出了商家的派送范围，无法下单！请换其他商家", 15).show();
							return;
						}*/
						
						/*if (model.listSize.size() > 0) {
							HJPopFoodDetailView popFoodDetailView = new HJPopFoodDetailView(ShopDetail.this, 
									rlAll, RelativeLayout.LayoutParams.FILL_PARENT, rlAll.getHeight(), 
									R.id.register_title_bar, model, shopmodeDetailModel);
							popFoodDetailView.Show();
							return;
						}*/
						if(app.mShop.shopIndexInShopCart == null){
							app.mShop.shopIndexInShopCart = new HJInteger();
						}
						if(app.shopCartModel.getFoodCountWithShopID(app.mShop.getId(), app.mShop.shopIndexInShopCart) > 0){
							app.shopCartModel.clear(app.mShop.getId(), app.mShop.shopIndexInShopCart);
						}
						app.mShop.setSendMoney(shopmodeDetailModel.getSendmoney());
						app.mShop.setFullFreeMoney(shopmodeDetailModel
								.getFreeSendMoney());
						app.mShop.setStartSendMoney(shopmodeDetailModel.getMinMoney());
						app.mShop.setLatitude(shopmodeDetailModel.getlat());
						app.mShop.setLongtude(shopmodeDetailModel.getlng());
						
						app.mShop
								.setSendDistance(shopmodeDetailModel.getSendDistance());
						app.mShop.setMaxKM(shopmodeDetailModel.getMaxKM());
						app.mShop.setMinKM(shopmodeDetailModel.getMinKM());
						app.mShop
								.setSendFeeAffKM(shopmodeDetailModel.getSendFeeAffKM());
						app.mShop.setSendFeeOfKM(shopmodeDetailModel.getSendFeeOfKM());
						app.mShop
								.setStartSendFee(shopmodeDetailModel.getStartSendFee());
						app.mShop.tagListModel = shopmodeDetailModel.tagListModel;
						app.mShop.setPacketFee(shopmodeDetailModel.getPackagefree());
						nowEdit = null;
						FoodModel model;
						String notice = "";
						for (int i = 0; i < app.buyFoodList.size(); i++) {
							model = app.buyFoodList.get(i);
							if(model.getSale() == 0){
								//商品下架，给出提示
								notice += (model.getFoodName() + ",");
							}else{
								app.shopCartModel.addFoodAttr(
										app.mShop, model, 0, model.getSale());
							}
							
						}
						
						if(notice.length() > 0){
							Toast.makeText(ShopDetail.this, notice + "商品已售完或下架！", 20).show();
						}
						
					}
					
					app.buyFoodList.clear();
					
				}
				downloadRelativeLayout.setVisibility(View.GONE);
				if (foodListView.getFooterViewsCount() <= 0) {
					foodListView.addFooterView(footView);
				}
				// mSchedule.
				// 这里对数据进行显示或做相应的处理 // 对listview进行更新 //ShowFoodListView();
				if (foodListAdapter == null) {
					foodListAdapter = new FoodListViewAdapter();
					foodListView.setAdapter(foodListAdapter);
				} else {
					foodListAdapter.notifyDataSetChanged();

				}
				listIndex = index;
				more_shop_pb.setVisibility(View.GONE);
				more_food_tv.setVisibility(View.VISIBLE);

				isMoreShopViewClick = true;
				Log.v(TAG, "对listview进行更新");

				int m = foodListModel[listIndex].getPage();
				int n = foodListModel[listIndex].getTotal();

				if ((m >= n) && (foodListView.getFooterViewsCount() > 0)) {
					foodListView.removeFooterView(footView);
				}

				isMoreShopViewClick = true;
				setCondition();
				break;
			case FOOD_DOWNLOAD_SUCCESS_ONLY_NOTIFY:
				downloadRelativeLayout.setVisibility(View.GONE);

				if (foodListView.getFooterViewsCount() <= 0) {
					foodListView.addFooterView(footView);
					Log.v(TAG, "foodListView.addFooterView");
				}

				foodListAdapter.notifyDataSetChanged();

				more_shop_pb.setVisibility(View.GONE);
				more_food_tv.setVisibility(View.VISIBLE);

				int m1 = foodListModel[listIndex].getPage();
				int n1 = foodListModel[listIndex].getTotal();

				if ((m1 >= n1) && (foodListView.getFooterViewsCount() > 0)) {
					foodListView.removeFooterView(footView);
				}
				isMoreShopViewClick = true;
				break;
			case GET_TYPE_SUCESS:

				if (typelistAdapter == null) {
					typelistAdapter = new FoodTypeListAdapter();
					typeListView.setAdapter(typelistAdapter);
				} else {
					typelistAdapter.notifyDataSetChanged();
				}

				getDataFromServer(1);
				break;
			case REMOVE_FAVOR_SUCCES:
				Toast.makeText(ShopDetail.this.getApplicationContext(), getResources().getString(R.string.fav_cancel_sucess),//"取消收藏成功",
						5).show();
				btnFavor.setBackgroundResource(R.drawable.shop_fav);
				break;
			case FAVOR_SUCCES:
				Toast.makeText(ShopDetail.this.getApplicationContext(), getResources().getString(R.string.fav_sucess),//"收藏成功",
						5).show();
				btnFavor.setBackgroundResource(R.drawable.shop_fav_press);
				break;
			case FAVOR_ALREADY:
				Toast.makeText(ShopDetail.this.getApplicationContext(),
						getResources().getString(R.string.fav_alread)/*"您已经收藏该商家"*/, 5).show();
				btnFavor.setBackgroundResource(R.drawable.shop_fav_press);
				break;
			case FAVOR_ERROR:
				Toast.makeText(ShopDetail.this.getApplicationContext(),
						getResources().getString(R.string.fav_failed)/*"收藏失败，请稍后再试"*/, 5).show();
				
				break;
			case NET_ERROR:
				Toast.makeText(ShopDetail.this, getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误！请稍候再试"*/, 5).show();
				break;
			case SET_SHOP_DETAIL:
				// 这里对数据进行显示或做相应的处理
				//adv.setReadData("shoppics.aspx", "ShopAdv" + String.valueOf(app.mShop.getId()), "shopid=" + String.valueOf(app.mShop.getId()));
				if (shopmodeDetailModel == null) {
					Log.v(TAG, "shopmodeDetailModel == null");
				} else {
					if(rbYD.getVisibility() == View.VISIBLE){
						getShopYD();
					}
					
					/*switch (shopmodeDetailModel.getShopType()) {
					case 1:
						ivW.setVisibility(View.VISIBLE);
						rbYD.setVisibility(View.GONE);
						break;
					case 2:
						ivD.setVisibility(View.VISIBLE);
						break;
					default:
						ivW.setVisibility(View.VISIBLE);
						ivD.setVisibility(View.VISIBLE);
						break;
					}*/
					tvTitle.setText(shopmodeDetailModel.getname());
					if (shopmodeDetailModel.getIsFavor() == 1) {
						//btnFavor.setText("取消收藏");
						btnFavor.setBackgroundResource(R.drawable.shop_fav_press);
					} else {
						//btnFavor.setText("收藏");
						btnFavor.setBackgroundResource(R.drawable.shop_fav);
					}
					if (shopmodeDetailModel.getShopdiscount() != 0.0f && shopmodeDetailModel.getShopdiscount() < 10.0f) {
						tvShopDiscount.setText(shopmodeDetailModel.getShopdiscount() + getResources().getString(R.string.public_zhe)/*"折"*/);
					}else{
						tvShopDiscount.setVisibility(View.GONE);
					}
					if (shopmodeDetailModel.getIco().length() > 0) {
						imgShop.setTag(shopmodeDetailModel.getIco());
						mLoadImage.addTask(shopmodeDetailModel.getIco(),
								imgShop, R.drawable.food_defalut);
					}
					tvShopGood.setText(String.valueOf(shopmodeDetailModel.getGoodCount()));
					tvShopOK.setText(String.valueOf(shopmodeDetailModel.getOkCount()));
					tvShopBad.setText(String.valueOf(shopmodeDetailModel.getBadCount()));
					//telButton.setText(shopmodeDetailModel.getTel());// 商家电话
					// tvBisnesstime.setText(shopmodeDetailModel.getTimeStart1());//准备时间
					tvShopAddress.setText(shopmodeDetailModel.getaddress());// 地址
					tvSentorg.setText(shopmodeDetailModel.getGrade());// 商家公告
					if (shopmodeDetailModel.tagListModel.list.size() > 0) {
						llDiscount.setVisibility(View.VISIBLE);
						for (int i = 0; i < shopmodeDetailModel.tagListModel.list.size(); i++) {
							TagModel model = shopmodeDetailModel.tagListModel.list.get(i);
							ShopDiscountItemView discountItemView = new ShopDiscountItemView();
							View view = model.getView();
							if (view == null) {
								view = inflater.inflate(R.layout.shop_discount_view_item, null);;
								discountItemView.tvInfo = (TextView)view.findViewById(R.id.tv_info);
								discountItemView.rlImg = (RelativeLayout)view.findViewById(R.id.rl_img);
								//model.setView(view);
							}else{
								discountItemView.tvInfo = (TextView)view.findViewById(R.id.tv_info);
								discountItemView.rlImg = (RelativeLayout)view.findViewById(R.id.rl_img);
							}
							if (model.getImageNet().length() > 16) {
								
								discountItemView.rlImg.setTag(model.getImageNet());
								mLoadImage.addTask(model.getImageNet(),
										discountItemView.rlImg, R.drawable.food_class);
							}
							llICON.addView(view);
							discountItemView.tvInfo.setText(model.getInfo());
						}
					}
					
					/*for (int i = 0; i < shopmodeDetailModel.tagListModel.list.size(); i++) {
						TagModel model = shopmodeDetailModel.tagListModel.list.get(i);
						if (model.getImageNet().length() > 16) {
							
							RelativeLayout rl = model.getView();
							if (rl == null) {
								rl = new RelativeLayout(ShopDetail.this);
								RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30
										, 30);
								rl.setLayoutParams(params);
								rl.setTag(model.getImageNet());
								rl.setTag(R.id.icon, model.getInfo());
								rl.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										//显示提示信息
									}
								});
							}else{
								model.setRl(rl);
							}
							
							llICON.addView(rl);
							mLoadImage.addTask(model.getImageNet(),
									rl, R.drawable.food_class);
							
						}
					}*/

					/*tvSendTime
							.setText(shopmodeDetailModel.getSendTime() + "分钟");// 配送时间*/
					//tvShopTeSe.setText(shopmodeDetailModel.getFeatures());// 商家特色
					tvDesc.setText(shopmodeDetailModel.getDesc());// 商家简介
					tvShopNews.setText(shopmodeDetailModel.getNews());
					if (shopmodeDetailModel.getTimeStart2().length() > 1) {
						
						tvSendTime.setText(shopmodeDetailModel.getTimeStart1() + "-" + shopmodeDetailModel.getTimeEnd1() + " "
								+ shopmodeDetailModel.getTimeStart2() + "-" + shopmodeDetailModel.getTimeEnd2());
					}else{
						
						tvSendTime.setText(shopmodeDetailModel.getTimeStart1() + "-" + shopmodeDetailModel.getTimeEnd1());
					}
					

					/*if (shopmodeDetailModel.getTogotype() != 2) {
						llCallTel.setVisibility(View.GONE);
					} else {
						llCallTel.setVisibility(View.VISIBLE);
					}*/

					if (!shopmodeDetailModel.getlat().equals("")
							&& shopmodeDetailModel.getlat() != null) {
						shoplat = Double.parseDouble(shopmodeDetailModel
								.getlat());
					}

					if (!shopmodeDetailModel.getlng().equals("")
							&& shopmodeDetailModel.getlng() != null) {
						shoplng = Double.parseDouble(shopmodeDetailModel
								.getlng());
					}

					if (shopmodeDetailModel.getDistance() == 0.0f) {
						tvDistance.setText("--");
					} else {
						tvDistance.setText(shopmodeDetailModel.getDistance()
								+ getResources().getString(R.string.public_distance));//"公里");
					}
					//String format = getResources().getString(R.string.shop_detail_send_free);
					if (shopmodeDetailModel.getFreeSendMoney() > 0.0f) {
						
						tvSendMoney.setText(String.format(getResources().getString(R.string.shop_detail_send_free),//"配送费:%.2f，满%.2f免配送费 起送金额:%.2f",
								shopmodeDetailModel.getSendmoney(),
								shopmodeDetailModel.getFreeSendMoney(), shopmodeDetailModel.getMinMoney()));
					} else {
						tvSendMoney.setText(String.format(getResources().getString(R.string.shop_detail_send_free_1),//"配送费:%.2f 起送金额:%.2f",
								shopmodeDetailModel.getSendmoney(), shopmodeDetailModel.getMinMoney()));
					}

					iconImageView.setVisibility(View.GONE);

				}
				break;

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (app.shopCartModel == null) {
			app.shopCartModel = new MyShopCart();
		}
		
		if (viewSwitcher.getCurrentItem() > 0) {
			setCondition();
			if (foodListAdapter != null) {
				foodListAdapter.notifyDataSetChanged();
			}

		}
		setCondition();
		
	}
	public void GoOrderDetail() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(ShopDetail.this, AddOrder.class);
		intent.putExtra("orderid", orderidString);
		intent.putExtra("orderType", 1);
		intent.putExtra("payType", payType);
		startActivity(intent);
	}

	public void clearOrderInf() {
		// TODO Auto-generated method stub
		app.shopCartModel.clear();
		etName.setText("");
		etPayPassword.setText("");
		etPhone.setText("");
		etNumber.setText("");
		etRemark.setText("");
		setCondition();
	}

		// 显示对话框
		private void j_showDialog(String msg) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(msg).setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}

					});
			builder.create().show();
		}	
	// TODO:记录拨打电话次数 记录到服务器
	@SuppressWarnings("unused")
	private void getPhoneCount(Context paramContext, String paramString) {
		new Thread() {
			@Override
			public void run() {
				// 商家编号 用户编号 时间
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (viewSwitcher.getCurrentItem() != 0) {
				viewSwitcher.setCurrentItem(viewSwitcher.getCurrentItem() - 1);
			} else {
				/*
				 * Intent localIntent = new Intent("com.ihangjing.common.tap0");
				 * app.sendBroadcast(localIntent);
				 */
				// gotoShopList();
				finish();
			}
		} else {
			boolean bool = true;
			bool = super.onKeyDown(keyCode, paramKeyEvent);
			return bool;
		}

		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
		// this.isActivityRun = false;
	}

	// 设置购物车的显示：一共点了几份菜 多少钱
	private void setCondition() {
		if (app.shopCartModel != null) {
			totalFoodPrice = app.shopCartModel.getShopTotalPriceWithShopId(app.mShop.getId(), app.mShop.shopIndexInShopCart);
			totalFoodCount = app.shopCartModel.getFoodCountWithShopID(app.mShop.getId(), app.mShop.shopIndexInShopCart);
			if(app.mShop.getShopType() == 2){//预定不需要打包费和配送费
				pacektFee = 0.0f;
				sendFee = 0.0f;
			}else{
				pacektFee = app.shopCartModel.getShopPacketFeeWithShopId(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				sendFee = app.shopCartModel.getSendFeeWithShopId(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				totalFoodPrice += (pacektFee + sendFee);
			}
			
			// 通过加入购物车的计算器进行计算
			String conditon = String.format(getResources().getString(R.string.order_condition) , totalFoodPrice, getResources().getString(R.string.public_moeny));///*"已点:%d份 共%.2f"*/
			if(tvPrice != null){
				if(buyType == 0){
					tvPrice.setText("总额：￥0.00");
				}else{
					tvPrice.setText(String.format("总额：￥%.2f", totalFoodPrice));
				}
				
			}
			//if (totalFoodCount > 0) {
				rlBottom.setVisibility(View.VISIBLE);
			/*}else{
				rlBottom.setVisibility(View.GONE);
				llShopCart.setVisibility(View.GONE);
			}*/
			tvPacketFee.setText(Html.fromHtml(String.format(getResources().getString(R.string.shop_detail_packet_fee), "<font color='red'><b>", getResources().getString(R.string.public_moeny_0),  pacektFee, 
					"</b></font>"
					, "<font color='red'><b>", getResources().getString(R.string.public_moeny_0),  sendFee, "</b></font>")));//打包费
			conditionTextView1.setText(conditon);
			tvShopCartCount.setText(String.valueOf(totalFoodCount));
			if (foodListAdapter != null) {
				foodListAdapter.notifyDataSetChanged();
			}
			mShopCardAdadpter.notifyDataSetChanged();
		}
	}

	public Bitmap getBitmap(FoodModel model) {
		// TODO Auto-generated method stub
		Bitmap originalImage;

		if (model.getImageLocalPath() != null
				&& !model.getImageLocalPath().equals("")) {// 本地地址不为空
			originalImage = OtherManager.getBitmap(model.getImageLocalPath());
			if (originalImage == null) {
				originalImage = BitmapFactory.decodeResource(
						ShopDetail.this.getResources(), R.drawable.icon);

			}
		} else {// 本地和网络地址都为空，使用id
			originalImage = BitmapFactory.decodeResource(
					ShopDetail.this.getResources(), R.drawable.icon);

		}

		return originalImage;
	}

	public double rad(double value) {
		return value * Math.PI / 180.0;
	}

	public double GetDistance(double lat1, double lng1, double lat2, double lng2) {// 计算两个经纬度之间的额距离
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.sin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s *= EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s / 1000;// 返回公里数
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		
		Message message = new Message();
		if (paramInt == 260) {
			String value1 = (String) paramObject;

			Log.v(TAG, value1);

			JSONObject json = null;
			JSONArray array;
			try {
				json = new JSONObject(value1);
				int state;
				switch (tag) {

				case 1:
					state = json.getInt("state");
					if (state == 1) {
						if (shopmodeDetailModel.getIsFavor() == 0) {
							message.what = UIHandler.FAVOR_SUCCES;
							shopmodeDetailModel.setIsFavor(1);
						}else{
							message.what = UIHandler.REMOVE_FAVOR_SUCCES;
							shopmodeDetailModel.setIsFavor(0);
						}
						
					} else if (state == 2) {
						message.what = UIHandler.FAVOR_ALREADY;
					} else {
						message.what = UIHandler.FAVOR_ERROR;
					}
					break;
				case 2:
					String value = (String) paramObject;
					foodTypeList.stringToBean(value, 1, getResources().getString(R.string.public_all_type));
					/*
					 * OtherManager.saveFoodTypeListToCache(ShopDetail.this,
					 * foodTypeList);
					 */
					message.what = UIHandler.GET_TYPE_SUCESS;
					break;
				case 3:
					array = json.getJSONArray("foodlist");

					this.pageindex = Integer.parseInt(json.getString("page"));
					this.total = Integer.parseInt(json.getString("total"));
					index = listIndex;
					if (pageindex == 1) {
						if (index == 1) {
							index = 0;
						} else {
							index++;
						}
						foodListModel[index].list.clear();
						message.what = UIHandler.FOOD_DOWNLOAD_SUCCESS;
					} else if (pageindex > 1) {
						message.what = UIHandler.FOOD_DOWNLOAD_SUCCESS_ONLY_NOTIFY;
					}
					foodListModel[index].setPage(pageindex);
					foodListModel[index].setTotal(total);
					FoodModel models;
					for (int i = 0; i < array.length(); i++) {
						// JSONObject model1 = array.getJSONObject(i);
						models = new FoodModel(array.getJSONObject(i));
						this.foodListModel[index].list.add(models);
					}

					break;
				case 4:
					foodModel = new FoodModel(json);
					message.what = UIHandler.GET_FOOD_DETAIL;
					break;
				case 5:
					isReadCommand = false;
					commentListModel.JSonToBean(json);
					if (commentListModel.list.size() == 0) {
						message.what = UIHandler.NO_COMMAND;
					}else{
						message.what = UIHandler.GET_COMMAND_SUCCESS;
					}
					break;
				case 6:
					shopmodeDetailModel = new FsgShopDetailModel(json);
					app.mShop.setSendTime(shopmodeDetailModel.getSendTime());
					app.mShop.setTimeEnd1(shopmodeDetailModel.getTimeEnd1());
					app.mShop.setTimeEnd2(shopmodeDetailModel.getTimeEnd2());
					app.mShop.setTimeStart1(shopmodeDetailModel.getTimeStart1());
					app.mShop.setTimeStart2(shopmodeDetailModel.getTimeStart2());
					/*
					 * shopmodeDetailModel.setDistance((float)GetDistance(lat,
					 * lon, Double.valueOf(shopmodeDetailModel.getlat()),
					 * Double.valueOf(shopmodeDetailModel.getlng())));
					 */
					message.what = UIHandler.SET_SHOP_DETAIL;
					break;
				case 7:
					state = json.getInt("state");
					if(state == 1){
						message.what = UIHandler.COMMAND_SUCESS;
					}else{
						message.what = UIHandler.COMMAND_FAIL;
						errMsg = json.getString("msg");
					}
					
					break;
				case 8:
					if(roomTypeListModel == null){
						roomTypeListModel = new RoomTypeListModel();
					}else{
						roomTypeListModel.list1.clear();
						roomTypeListModel.list2.clear();
					}
					roomTypeListModel.JSONToBin(json);
					message.what = UIHandler.GET_SHOP_ROOM;
					break;
				case 9:
					orderidString = json.getString("orderid");
					String orderstateString = json.getString("orderstate");
					app.shopCartModel.setOrderid(orderidString);

					// 提交失败
					if (orderstateString.equals("-1")) {
						// message = new Message();
						payMoney = (float)json.getDouble("totalprice");
						message.what = UIHandler.ORDER_SUBMIT_FAILD;
						errorMSG = json.getString("msg");
						// hander.sendMessage(message); // 发送消息给Handler
					} else {
						
						
						message.what = UIHandler.ORDER_SUBMIT_SUCCESS;
						errorMSG = "";

					}
					break;
				case 10:
					//json = new JSONObject(jsonString);
					int state1 = json.getInt("state");
					if (state1 == 1) {
						payID = json.getString("batch");
						message.what = UIHandler.GETPAYID_SUCCESS;
					} else {
						message.what = UIHandler.GETPAYID_FAILD;
					}
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}
		} else {
			message.what = UIHandler.NET_ERROR;
		}

		// 数据加载成功 页面更新
		switch (tag) {
		case 3:
			isReadFood = false;
			break;
		case 5:
			isReadCommand = false;
			break;
		default:
			break;
		}
		handler.sendMessage(message);
	}
	private String getAlipayOrderInfo() {
		//payMoney = 0.1f;
				// 签约合作者身份ID
				String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";

				// 签约卖家支付宝账号
				orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";

				// 商户网站唯一订单号
				orderInfo += "&out_trade_no=" + "\"" + payID + "\"";
				//getString(R.string.app_name) + getResources().getString(R.string.shop_cart_pay_title)/*"Android支付宝支付，订单编号："*/ + orderidString
				// 商品名称
				orderInfo += "&subject=" + "\"" + getString(R.string.app_name) + "Android" + getString(R.string.pay_mode_1) + ", " + getString(R.string.order_detail_id) + orderidString + ", " + getString(R.string.pay_payid)  + payID + "\"";

				// 商品详情
				orderInfo += "&body=" + "\"" + getString(R.string.app_name) + "Android" + getString(R.string.pay_mode_1) + ", " + getString(R.string.order_detail_id) + orderidString + ", " + getString(R.string.pay_payid)  + payID +"\"";

				// 商品金额
				orderInfo += "&total_fee=" + "\"" + String.format("%.2f", payMoney) + "\"";

				// 服务器异步通知页面路径
				orderInfo += "&notify_url=" + "\"" + HJAppConfig.DOMAIN + "Alipay/iosnotify.aspx"
						+ "\"";

				// 服务接口名称， 固定值
				orderInfo += "&service=\"mobile.securitypay.pay\"";

				// 支付类型， 固定值
				orderInfo += "&payment_type=\"1\"";

				// 参数编码， 固定值
				orderInfo += "&_input_charset=\"utf-8\"";

				// 设置未付款交易的超时时间
				// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
				// 取值范围：1m～15d。
				// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
				// 该参数数值不接受小数点，如1.5h，可转换为90m。
				orderInfo += "&it_b_pay=\"30m\"";

				// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
				// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

				// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
				//orderInfo += "&return_url=\"m.alipay.com\"";

				// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
				// orderInfo += "&paymethod=\"expressGateway\"";

				return orderInfo;	
	}
	public void gotoAliPay() {
		try {
			
			alipayInfo = getAlipayOrderInfo();
			String sign = SignUtils.sign(alipayInfo, Keys.PRIVATE);
			sign = URLEncoder.encode(sign, "UTF-8");
			alipayInfo += "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Result.sResult = null;
			Log.i(TAG, "info = " + alipayInfo);
			//final String orderInfo = alipayInfo;
			new Thread() {
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(ShopDetail.this);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(alipayInfo);

					Message msg = new Message();
					msg.what = UIHandler.RQF_PAY;
					msg.obj = result;
					handler.sendMessage(msg);
				}
			}.start();
		} catch (Exception ex) {
			
			ex.printStackTrace();
			Toast.makeText(ShopDetail.this, getString(R.string.pay_error),
					Toast.LENGTH_SHORT).show();
		}
	}
	public void getPayID(float moeny)
	{
		dialogStr = getString(R.string.pay_load_id)/*"获取支付数据中......"*/;
		showDialog(322);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("orderid", orderidString);
		localHashMap.put("price", String.valueOf(moeny));
		localHttpManager = new HttpManager(ShopDetail.this, ShopDetail.this,
				"Android/buildpaynum.aspx?", localHashMap, 1, 10);
		localHttpManager.postRequest();
	}
	
	private void gotoOnLinePay() {
		// TODO Auto-generated method stub
		if(payID == null || payID.length() == 0 ){
			getPayID(payMoney);
			return;
		}
		if (payType == 1) {
			gotoAliPay();
			
		}else if(payType == 5){
			gotoWXPay();
		}
	}
	//微信支付相关
		public void gotoWXPay(){
			dialogStr = getString(R.string.pay_load_wx);//"微信支付数据加载中......";
			showDialog(322);
			resultunifiedorder = null;
			GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
			getPrepayId.execute();
			
		}
		/**
		 生成签名
		 */

		private String genPackageSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append(Constants.API_KEY);
			

			String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion",packageSign);
			return packageSign;
		}
		private String genAppSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append(Constants.API_KEY);

	        wxpay.append("sign str\n"+sb.toString()+"\n\n");
			String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion",appSign);
			return appSign;
		}
		private String toXml(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();
			sb.append("<xml>");
			for (int i = 0; i < params.size(); i++) {
				sb.append("<"+params.get(i).getName()+">");


				sb.append(params.get(i).getValue());
				sb.append("</"+params.get(i).getName()+">");
			}
			sb.append("</xml>");

			Log.e("orion",sb.toString());
			return sb.toString();
		}
		
		public Map<String,String> decodeXml(String content) {

			try {
				Map<String, String> xml = new HashMap<String, String>();
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(new StringReader(content));
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {

					String nodeName=parser.getName();
					switch (event) {
						case XmlPullParser.START_DOCUMENT:

							break;
						case XmlPullParser.START_TAG:

							if("xml".equals(nodeName)==false){
								//实例化student对象
								xml.put(nodeName,parser.nextText());
							}
							break;
						case XmlPullParser.END_TAG:
							break;
					}
					event = parser.next();
				}

				return xml;
			} catch (Exception e) {
				Log.e("orion",e.toString());
			}
			return null;

		}


		private String genNonceStr() {
			Random random = new Random();
			return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
		}
		
		private long genTimeStamp() {
			return System.currentTimeMillis() / 1000;
		}

	   //
		private String genProductArgs() {
			StringBuffer xml = new StringBuffer();

			try {
				String	nonceStr = genNonceStr();


				xml.append("</xml>");
	            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
				packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
				packageParams.add(new BasicNameValuePair("body", getString(R.string.app_name) + "Android" + getString(R.string.pay_mode_3)));
				packageParams.add(new BasicNameValuePair("detail", getString(R.string.app_name) + "Android"  + getString(R.string.pay_mode_3) + ", " + getString(R.string.order_detail_id) + orderidString + ", " + getString(R.string.pay_payid)  + payID));
				packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
				packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
				packageParams.add(new BasicNameValuePair("notify_url", HJAppConfig.WEIXINPAYREQ));//接收微信支付异步通知回调地址
				packageParams.add(new BasicNameValuePair("out_trade_no",payID));
				packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
				packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(payMoney * 100))));
				packageParams.add(new BasicNameValuePair("trade_type", "APP"));


				String sign = genPackageSign(packageParams);
				packageParams.add(new BasicNameValuePair("sign", sign));


			   String xmlstring =toXml(packageParams);

				return xmlstring;

			} catch (Exception e) {
				Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
				return null;
			}
			

		}
		private void genPayReq() {

			wxpayReq.appId = Constants.APP_ID;
			wxpayReq.partnerId = Constants.MCH_ID;
			wxpayReq.prepayId = resultunifiedorder.get("prepay_id");
			wxpayReq.packageValue = "Sign=WXPay";
			wxpayReq.nonceStr = genNonceStr();
			wxpayReq.timeStamp = String.valueOf(genTimeStamp());


			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", wxpayReq.appId));
			signParams.add(new BasicNameValuePair("noncestr", wxpayReq.nonceStr));
			signParams.add(new BasicNameValuePair("package", wxpayReq.packageValue));
			signParams.add(new BasicNameValuePair("partnerid", wxpayReq.partnerId));
			signParams.add(new BasicNameValuePair("prepayid", wxpayReq.prepayId));
			signParams.add(new BasicNameValuePair("timestamp", wxpayReq.timeStamp));

			wxpayReq.sign = genAppSign(signParams);

			wxpay.append("sign\n"+wxpayReq.sign+"\n\n");

			//show.setText(wxpay.toString());

			Log.e("orion", signParams.toString());

		}
		private void sendPayReq() {
			

			app.msgApi.registerApp(Constants.APP_ID);
			app.msgApi.sendReq(wxpayReq);
		}
		
		private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {



			@Override
			protected void onPreExecute() {
				//dialog = ProgressDialog.show(ShopDetail.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
			}

			@Override
			protected void onPostExecute(Map<String,String> result) {
				
				wxpay.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
				Message message = new Message();
				String value = result.get("return_code");
				if (value.compareTo("SUCCESS") == 0) {
					resultunifiedorder=result;
					message.what = UIHandler.GET_PREPAY_ID_SUCESS;
				}else{
					message.what = UIHandler.GET_PREPAY_ID_FAILD;
					errorMSG = result.get("return_msg");
				}
				
				
				
				handler.sendMessage(message);
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
			}

			@Override
			protected Map<String,String>  doInBackground(Void... params) {

				String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
				String entity = genProductArgs();
				try {  
					entity = new String(entity.getBytes(), "ISO8859-1");  
			    } catch (Exception e) {  
			        e.printStackTrace();  
			    }
				Log.e("orion",entity);

				byte[] buf = Util.httpPost(url, entity);

				String content = new String(buf);
				Log.e("orion", content);
				Map<String,String> xml=decodeXml(content);

				return xml;
			}
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

	class FoodListHolder {
		TextView tv_foodName;
		TextView tv_foodInfo;
		RelativeLayout iv_foodImg;
		TextView tv_foodPrice;
		TextView tv_Point;
		public TextView tvNum;
		public Button btnAdd;
		public Button btnDel;
		public LinearLayout llOptM;
		public Button btnCheckSpec;
		public LinearLayout llParent;
		public RelativeLayout rlSpec;
		public TextView tvSpecNum;

		FoodListHolder() {
		}
	}

	// 菜单
	class FoodListViewAdapter extends BaseAdapter {
		FoodListViewAdapter() {
		}

		@Override
		public int getCount() {
			return ShopDetail.this.foodListModel[listIndex].list.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return foodListModel[listIndex].list.get(paramInt);
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
			ShopDetail.this.inflater = LayoutInflater.from(ShopDetail.this);

			FoodListHolder foodlistHolder;
			if (convertView == null) {
				convertView = ShopDetail.this.inflater.inflate(
						R.layout.food_item, null);

				Log.v(TAG, "convertView == null");
				foodlistHolder = new ShopDetail.FoodListHolder();
				foodlistHolder.iv_foodImg = (RelativeLayout) convertView
						.findViewById(R.id.rl_img);
				foodlistHolder.iv_foodImg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String urlString = (String)v.getTag();
						Bitmap img = mLoadImage.getBitmap(urlString);
						ImagePopViewDialog dialog = new ImagePopViewDialog(ShopDetail.this, img);
						dialog.show();
					}
				});
				foodlistHolder.llParent = (LinearLayout)convertView.findViewById(R.id.linearLayout3);
				foodlistHolder.llParent.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						checkSpecIndex = (Integer)v.getTag();
						Intent intent = new Intent(ShopDetail.this, FoodDetail.class);
						intent.putExtra("checkSpecIndex", checkSpecIndex);
						app.mFood = foodListModel[listIndex].list
								.get(checkSpecIndex);
						
						app.mShop.setSendMoney(shopmodeDetailModel.getSendmoney());
						app.mShop.setFullFreeMoney(shopmodeDetailModel
								.getFreeSendMoney());
						app.mShop.setStartSendMoney(shopmodeDetailModel.getMinMoney());
						app.mShop.setLatitude(shopmodeDetailModel.getlat());
						app.mShop.setLongtude(shopmodeDetailModel.getlng());
						
						app.mShop
								.setSendDistance(shopmodeDetailModel.getSendDistance());
						app.mShop.setMaxKM(shopmodeDetailModel.getMaxKM());
						app.mShop.setMinKM(shopmodeDetailModel.getMinKM());
						app.mShop
								.setSendFeeAffKM(shopmodeDetailModel.getSendFeeAffKM());
						app.mShop.setSendFeeOfKM(shopmodeDetailModel.getSendFeeOfKM());
						app.mShop
								.setStartSendFee(shopmodeDetailModel.getStartSendFee());
						app.mShop.tagListModel = shopmodeDetailModel.tagListModel;
						app.mShop.setPacketFee(shopmodeDetailModel.getPackagefree());
						
						startActivity(intent);
						/*FoodDetailDialog dialog = new FoodDetailDialog(
								ShopDetail.this, R.style.dialog);
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();*/
					}
				});
				foodlistHolder.rlSpec = (RelativeLayout)convertView.findViewById(R.id.rl_sec_spec);
				foodlistHolder.tv_foodInfo = (TextView) convertView
						.findViewById(R.id.tv_foodinfo);
				foodlistHolder.tv_foodName = (TextView) convertView
						.findViewById(R.id.tv_foodname);
				foodlistHolder.tv_foodPrice = (TextView) convertView
						.findViewById(R.id.tv_foodprice);

				foodlistHolder.tv_Point = (TextView) convertView
						.findViewById(R.id.tv_point);

				foodlistHolder.tvNum = (TextView) convertView
						.findViewById(R.id.tv_foodcount);
				//foodlistHolder.tvNum.setVisibility(View.GONE);
				foodlistHolder.btnAdd = (Button) convertView
						.findViewById(R.id.btn_number_plus);
				foodlistHolder.btnDel = (Button) convertView
						.findViewById(R.id.btn_number_minus);
				foodlistHolder.btnCheckSpec = (Button) convertView
						.findViewById(R.id.btn_sec_spec);
				foodlistHolder.tvSpecNum = (TextView) convertView
						.findViewById(R.id.tv_shop_count);
				foodlistHolder.btnCheckSpec.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						checkSpecIndex = (Integer)v.getTag();
						CondimentDialog dialog = new CondimentDialog(
								ShopDetail.this, R.style.dialog);
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();
					}
				});
				//foodlistHolder.btnDel.setVisibility(View.GONE);
				foodlistHolder.btnAdd.setOnClickListener(addToShopCartListener);
				foodlistHolder.btnDel
						.setOnClickListener(delFromShopCartListener);
				foodlistHolder.llOptM = (LinearLayout)convertView.findViewById(R.id.ll_opt_m);

				convertView.setTag(foodlistHolder);
			} else {
				foodlistHolder = (ShopDetail.FoodListHolder) convertView
						.getTag();
			}

			FoodModel model = foodListModel[listIndex].list.get(paramInt);

			if (model != null) {
				foodlistHolder.tv_foodName.setText(model.getFoodName());
				foodlistHolder.tv_foodInfo.setText(model.getDisc());
				foodlistHolder.tv_foodPrice.setText(getResources().getString(R.string.public_moeny_0)
						+ String.valueOf(model.getPrice()));
				foodlistHolder.tv_Point.setText(getResources().getString(R.string.shop_detail_public_point)//"公益积分："
						+ model.getPublicPoint());
				// Drawable drawable = new BitmapDrawable(getBitmap(model));
				foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
				if (model.getImageNetPath() != null
						&& model.getImageNetPath().length() > 18) {// http://*.*.*/*.jpg
					// 有网络地址，并且没有加入下载，那么加入
					// model.isDowload = true;
					foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
					mLoadImage.addTask(model.getImageNetPath(),
							foodlistHolder.iv_foodImg, R.drawable.food_defalut);
				} else {
					foodlistHolder.iv_foodImg
							.setBackgroundResource(R.drawable.food_defalut);
				}
				foodlistHolder.llParent.setTag(paramInt);
				foodlistHolder.tvNum.setTag(paramInt);
				foodlistHolder.tvNum.setTag(R.id.tab_label,
				 model.getFoodId());
				if(paramInt == 1){
					paramInt++;
					paramInt--;
				}
				
				foodlistHolder.tv_foodInfo.setText(model.getDisc());
				foodlistHolder.tv_Point.setText("月销：" + model.getSale());
				int stock = 0;
				if(model.getStock() == 0){
					foodlistHolder.rlSpec.setVisibility(View.VISIBLE);
					foodlistHolder.btnCheckSpec.setTextColor(Color.GRAY);
					foodlistHolder.btnCheckSpec.setText("已售完");
					foodlistHolder.llOptM.setVisibility(View.GONE);
					foodlistHolder.tvSpecNum.setVisibility(View.GONE);
					foodlistHolder.btnAdd.setVisibility(View.GONE);
				}else if(model.listSize.size() > 1){
					foodlistHolder.btnCheckSpec.setTextColor(getResources().getColor(R.color.topbar_bg_color));
					foodlistHolder.btnCheckSpec.setText("可选规格");
					
					stock = model.getStock() - app.shopCartModel
							.getOneFoodCount(app.mShop, model.getFoodId());
					if(stock < 0){//矫正数据
						app.shopCartModel.removeFood(app.mShop.getId(), model);
						stock = model.getStock();
						setCondition();
					}
					//foodlistHolder.tv_foodInfo.setText(String.format("库存：%d", stock));
					stock = app.shopCartModel
							.getOneFoodCount(app.mShop, model.getFoodId());
					foodlistHolder.llOptM.setVisibility(View.GONE);
					foodlistHolder.btnAdd.setVisibility(View.GONE);
					foodlistHolder.btnAdd.setVisibility(View.GONE);
					foodlistHolder.rlSpec.setVisibility(View.VISIBLE);
					if(stock == 0)
					{
						foodlistHolder.tvSpecNum.setVisibility(View.GONE);
						
					}else{
						foodlistHolder.tvSpecNum.setVisibility(View.VISIBLE);
						foodlistHolder.tvSpecNum.setText(String.format("%d", stock));
					}
				}else{
					int count = app.shopCartModel
							.getFoodCountInAttr(app.mShop, model.getFoodId(),
									model.listSize.get(0).cId);
					stock = model.getStock() - count;
					if(stock < 0){
						app.shopCartModel.removeFood(app.mShop.getId(), model, 0);
						stock = model.getStock();
						setCondition();
						count = 0;
					}
					//foodlistHolder.tv_foodInfo.setText(String.format("库存：%d", stock));
					
					foodlistHolder.tvNum.setText(String.valueOf(count));
					if (count > 0) {
						foodlistHolder.llOptM.setVisibility(View.VISIBLE);
					}else{
						foodlistHolder.llOptM.setVisibility(View.GONE);
					}
					if(stock < 1){
						foodlistHolder.btnAdd.setVisibility(View.GONE);
					}else{
						foodlistHolder.btnAdd.setVisibility(View.VISIBLE);
					}
					foodlistHolder.rlSpec.setVisibility(View.GONE);
				}
				
				foodlistHolder.btnAdd.setTag(paramInt);
				foodlistHolder.btnCheckSpec.setTag(paramInt);
				foodlistHolder.btnAdd.setTag(R.id.tab_label,
						foodlistHolder.tvNum);

				foodlistHolder.btnDel.setTag(paramInt);

				foodlistHolder.btnDel.setTag(R.id.tab_label,
						foodlistHolder.tvNum);

			} else {
				Log.v(TAG, "foodInOrderModel is null");
			}

			return convertView;
		}
	}
	
	class shopCartView {
		TextView tv_foodName;
		TextView tv_foodPrice;
		TextView tv_foodNum;
		Button btnAdd;
		Button btnDel;
		Button btnRemove;

		shopCartView() {
		}
	}

	// 菜单
	class shopCartListViewAdapter extends BaseAdapter {
		shopCartListViewAdapter() {
		}

		@Override
		public int getCount() {
			int n = 0;
			if(app.shopCartModel != null){
				n = app.shopCartModel.getShopCartListSizeWithShopID(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				//app.mShop.shopIndexInShopCart = shopIndex;
			}
			return n;
		}

		@Override
		public Object getItem(int paramInt) {
			return app.shopCartModel.getFoodInOrderModelWithShopId(app.mShop.getId(), app.mShop.shopIndexInShopCart, paramInt);
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
			shopCartView foodlistHolder;
			if (convertView == null) {
				convertView = ShopDetail.this.inflater.inflate(
						R.layout.shop_detail_shopcart_item, null);

				Log.v(TAG, "convertView == null");
				foodlistHolder = new shopCartView();
				
				foodlistHolder.tv_foodName = (TextView) convertView
						.findViewById(R.id.tv_foodname);
				foodlistHolder.tv_foodPrice = (TextView) convertView
						.findViewById(R.id.tv_foodprice);
				foodlistHolder.btnDel = (Button)convertView.findViewById(R.id.btn_number_minus);
				foodlistHolder.btnAdd = (Button)convertView.findViewById(R.id.btn_number_plus);
				foodlistHolder.btnRemove = (Button)convertView.findViewById(R.id.btn_remove);
				foodlistHolder.tv_foodNum = (TextView) convertView.findViewById(R.id.tv_foodcount);
				/*foodlistHolder.tv_foodNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						 
						if (!arg1) {
							EditText nowEdit = (EditText)arg0;
							int pt = (Integer)arg0.getTag();
							FoodInOrderModel model = (FoodInOrderModel)arg0.getTag(R.id.tab_icon);
							int count = Integer.parseInt(nowEdit.getText().toString());
							if (count > model.getStock()) {
								Toast.makeText(ShopCart.this, "已经超出了库存！", 15)
								  .show(); 
								nowEdit.setText(String.valueOf(app.shopCartModel.getFoodCountInAttr(model.getTogoId()
										, model.getId(), 0)));
								 return; 
							}
							count = app.shopCartModel.setFoodAttr(model, pt, count);
							if (count == 0) {
								mSchedule.notifyDataSetChanged();
							}
							nowEdit.setText(String.valueOf(count));
							tvTotalPrice.setText(String.format("合计：￥%.2f元", app.shopCartModel.getTotalprice()));
							//nowEdit.set
						}else{
							
						}
					}
				});*/
				
				foodlistHolder.btnAdd.setOnClickListener(addToShopCartListener1);
				foodlistHolder.btnRemove.setOnClickListener(removeFromShopCartListener);
				foodlistHolder.btnDel.setOnClickListener(delFromShopCartListener1);
				convertView.setTag(foodlistHolder);
			} else {
				foodlistHolder = (shopCartView) convertView
						.getTag();
			}
			HJInteger index = new HJInteger();
			index.value = paramInt;

			//FoodInOrderModel model = app.shopCartModel.getFoodInOrderModel(index);
			FoodInOrderModel model = app.shopCartModel.getFoodInOrderModelWithShopId(app.mShop.getId(), app.mShop.shopIndexInShopCart, index);
			//FoodInOrderModel model = app.shopCartModel.getFoodInOrderModelWithShopId(app.mShop.getId(), shopIndex, paramInt);
			
			if (model != null) {
				if(model.listSize.get(index.value).name != null && model.listSize.get(index.value).name.length() > 0){
					foodlistHolder.tv_foodName.setText(model.getName() + "_" + model.listSize.get(index.value).name);
				}else{
					foodlistHolder.tv_foodName.setText(model.getName());
				}
				int stock = model.getStock() - app.shopCartModel
						.getOneFoodCount(app.mShop, model.getId());
				if(stock < 1){
					foodlistHolder.btnAdd.setVisibility(View.GONE);
				}else{
					foodlistHolder.btnAdd.setVisibility(View.VISIBLE);
				}
				foodlistHolder.tv_foodPrice.setText(getResources().getString(R.string.public_moeny_0)/*"单价："*/
						+ String.valueOf(model.listSize.get(index.value).price));
				foodlistHolder.tv_foodNum.setTag(index.value);
				foodlistHolder.tv_foodNum.setTag(R.id.tab_icon, model);
				foodlistHolder.tv_foodNum.setText(String.valueOf(model.listSize.get(index.value).count));
				foodlistHolder.btnAdd.setTag(index.value);
				foodlistHolder.btnAdd.setTag(R.id.tab_label, foodlistHolder.tv_foodNum);
				foodlistHolder.btnAdd.setTag(R.id.tab_icon, model);
				
				
				foodlistHolder.btnDel.setTag(index.value);
				
				foodlistHolder.btnDel.setTag(R.id.tab_label, foodlistHolder.tv_foodNum);
				foodlistHolder.btnDel.setTag(R.id.tab_icon, model);
				
				foodlistHolder.btnRemove.setTag(index.value);
				
				foodlistHolder.btnRemove.setTag(R.id.tab_icon, model);
				//foodlistHolder.btnDel.setTag(R.id.tab_label, foodlistHolder.tv_foodNum);
				
			} else {
				Log.v(TAG, "foodInOrderModel is null");
			}

			return convertView;
		}
	}

	class FoodTypeListHolder {
		TextView tv_typeName;
		RelativeLayout rl_img;

		FoodTypeListHolder() {
		}
	}

	// 菜单分类
	class FoodTypeListAdapter extends BaseAdapter {
		FoodTypeListAdapter() {
		}

		@Override
		public int getCount() {

			int size = foodTypeList.list.size();
			return size;

		}

		@Override
		public Object getItem(int paramInt) {
			/*
			 * if (paramInt == 0) { return foodTypeModel; }
			 */
			return foodTypeList.list.get(paramInt);

		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int position, View convertView,
				ViewGroup paramViewGroup) {
			FoodTypeListHolder viewHolder;
			FoodTypeModel localFoodModel = new FoodTypeModel();

			Log.v(TAG, "position:" + position);

			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) ShopDetail.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.food_type_item, null);

				viewHolder = new FoodTypeListHolder();
				viewHolder.tv_typeName = (TextView) convertView
						.findViewById(R.id.typename);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (FoodTypeListHolder) convertView.getTag();
			}

			localFoodModel = foodTypeList.list.get(position);
			if (localFoodModel != null) {
				if (position == typeIndex) {
					viewHolder.tv_typeName
							.setBackgroundResource(R.drawable.type_sel);
					viewHolder.tv_typeName.setTextColor(Color.WHITE);
				} else {
					viewHolder.tv_typeName
							.setBackgroundResource(R.drawable.type_nor);
					viewHolder.tv_typeName.setTextColor(Color
							.rgb(184, 188, 184));
				}
				viewHolder.tv_typeName.setText(localFoodModel.getName());
			}
			return convertView;
		}
	}

	// 列表项包含的组件
	class CommandViewHolderEdit {
		TextView tvUserName;
		TextView tvServer;
		TextView tvOut;
		TextView tvFlavo;
		TextView tvValue;
		RatingBar rbPoint;
		ScrollView svImage;
		LinearLayout llImage;
		public RelativeLayout iv_foodImg;
	}

	// 自定义adapter
	class CommandListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		CommandListViewAdapter() {
		}

		@Override
		public int getCount() {
			return commentListModel.list.size();
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

			CommandViewHolderEdit viewHolder;
			CommentModel model;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) ShopDetail.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater
						.inflate(R.layout.comment_list_item, null);

				viewHolder = new CommandViewHolderEdit();

				viewHolder.tvUserName = (TextView) convertView
						.findViewById(R.id.tv_username);

				viewHolder.tvServer = (TextView) convertView
						.findViewById(R.id.tv_server);

				viewHolder.tvFlavo = (TextView) convertView
						.findViewById(R.id.tv_taste);
				viewHolder.tvOut = (TextView) convertView
						.findViewById(R.id.tv_out);

				viewHolder.tvValue = (TextView) convertView
						.findViewById(R.id.tv_dis);

				viewHolder.rbPoint = (RatingBar) convertView
						.findViewById(R.id.shopdetai_RatingBar02);
				viewHolder.svImage = (ScrollView)convertView
						.findViewById(R.id.sv_image);
				viewHolder.llImage = (LinearLayout)convertView
						.findViewById(R.id.ll_image);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (CommandViewHolderEdit) convertView.getTag();
			}

			model = commentListModel.list.get(position);

			if (model != null) {

				viewHolder.tvUserName.setText(model.getUserName());

				viewHolder.tvServer.setText(model.getServerG() + getResources().getString(R.string.com_d));//"分");

				viewHolder.tvOut.setText(model.getOutG() + getResources().getString(R.string.com_d));//"分");
				viewHolder.tvFlavo.setText(model.getFlavorG() + getResources().getString(R.string.com_d));//"分");
				viewHolder.tvValue.setText(String.format("[%s]:%s", model.getTime(), model.getValue()));

				// viewHolder.rbPoint.setNumStars(model.getPoint());
				viewHolder.rbPoint.setRating(model.getPoint());
				if (model.getUserpic() == null || model.getUserpic().length() < 14) {
					viewHolder.svImage.setVisibility(View.GONE);
				}else{
					viewHolder.svImage.setVisibility(View.VISIBLE);
					if (viewHolder.iv_foodImg == null) {
						viewHolder.iv_foodImg = new RelativeLayout(ShopDetail.this);
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(133, 100);
						viewHolder.iv_foodImg.setLayoutParams(params);
						viewHolder.llImage.addView(viewHolder.iv_foodImg);
						viewHolder.iv_foodImg.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String urlString = (String)v.getTag();
								Bitmap img = mLoadImage.getBitmap(urlString);
								ImagePopViewDialog dialog = new ImagePopViewDialog(ShopDetail.this, img);
								dialog.show();
							}
						});
					}
					viewHolder.iv_foodImg.setTag(model.getUserpic());
					mLoadImage.addTask(model.getUserpic(),
							viewHolder.iv_foodImg, R.drawable.food_defalut);
					
				}

			}

			return convertView;
		}
	}
	
	public class RoomItem{
		RelativeLayout rlImg;
		TextView tvName;
		LinearLayout llParent;
	}
	
	//定义继承BaseAdapter的匹配器   
    public class ImageAdapter extends BaseAdapter {   

            //Item的修饰背景   
            int mGalleryItemBackground;   

            //上下文对象   
            private Context mContext;   

            //图片数组   
            private Integer[] mImageIds = { R.drawable.start,   
                            R.drawable.start1, R.drawable.start2,   
                            R.drawable.start3 };   

            //构造方法   
            public ImageAdapter(Context c){   
                    mContext = c;   
                    //读取styleable资源   
            TypedArray a = obtainStyledAttributes(R.styleable.HelloGallery);   
            mGalleryItemBackground = a.getResourceId(   
                    R.styleable.HelloGallery_android_galleryItemBackground, 0);   
            a.recycle();   

            }   

            //返回项目数量   
            @Override  
            public int getCount() {
            	if (roomType == 0) {
					return roomTypeListModel.list1.size();
				}else{
					return roomTypeListModel.list2.size();
				}
                      
            }   

            //返回项目   
            @Override  
            public Object getItem(int position) {   
                    return position;   
            }   

            //返回项目Id   
            @Override  
            public long getItemId(int position) {   
                    return position;   
            }   

            //返回视图   
            @Override  
            public View getView(int position, View convertView, ViewGroup parent) {   
            	RoomItem roomItem;
            	if(convertView == null){
            		roomItem = new RoomItem();
            		convertView = getLayoutInflater().inflate(R.layout.rood_item,
            				null);
            		roomItem.llParent = (LinearLayout)convertView.findViewById(R.id.ll_parent);
            		roomItem.rlImg = (RelativeLayout)convertView.findViewById(R.id.rl_img);
            		roomItem.tvName = (TextView)convertView.findViewById(R.id.tv_foodname);
            		convertView.setTag(roomItem);
            	}else{
            		roomItem = (RoomItem)convertView.getTag();
            	}
            	RoomTypeInfo room;
            	if (roomType == 0) {
            		room = roomTypeListModel.list1.get(position);
				}else{
					room = roomTypeListModel.list2.get(position);
				}
            	//roomItem.rlImg = (RelativeLayout)convertView.findViewById(R.id.rl_img);
            	if (room.getSortPic() != null
						&& room.getSortPic().length() > 18) {// http://*.*.*/*.jpg
					// 有网络地址，并且没有加入下载，那么加入
					// model.isDowload = true;
            		roomItem.rlImg.setTag(room.getSortPic());
					mLoadImage.addTask(room.getSortPic(),
							roomItem.rlImg, R.drawable.food_defalut);
				} else {
					roomItem.rlImg
							.setBackgroundResource(R.drawable.food_defalut);
				}
            	if (selectRoom == position) {
					roomItem.llParent.setBackgroundColor(getResources().getColor(R.color.topbar_bg_color));
				}else{
					roomItem.llParent.setBackgroundColor(Color.WHITE);
				}
        		roomItem.tvName.setText(room.getSortName() + "(最低消费：￥" + String.valueOf(room.minfee) + "可容纳人数：" + String.valueOf(room.JOrder));
        		return convertView;
                    /*ImageView iv = new ImageView(mContext);   
                    iv.setImageResource(mImageIds[position]);   
                    //给生成的ImageView设置Id，不设置的话Id都是-1   
                    iv.setId(mImageIds[position]);   
                    iv.setLayoutParams(new Gallery.LayoutParams(150, 200));   
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);   
                    iv.setBackgroundResource(mGalleryItemBackground);   
                    return iv;  */ 
            }   

    } 
    
    public class CondimentDialog extends Dialog {
		private Button btnPlus;
		private Button btnDel;
		private Button CancelButton;
		private LinearLayout layout; // 布局 ， 可以在xml布局中获得
		private TextView tvPrice;
		private TextView tvNum;
		LinearLayout llOpt;
		
		int startID = 0x10000000;
		private RadioGroupEx specGroup; // 点选按钮组
		int id = startID;
		List<Integer> radioGoupID;
		List<Integer> checkBoxID;
		int checkBoxCount[];// 记录必选框中选中了多少个（checkBox）
		int attrIndex = 0;//当前选中的规格索引
		private FoodModel food;
		// private Drawable icCallMissed;
		public CondimentDialog(Context context, int theme) {
			super(context, R.style.no_title_dialog);
			int with = getWindowManager().getDefaultDisplay().getWidth() - 30;
			radioGoupID = new ArrayList<Integer>();
			checkBoxID = new ArrayList<Integer>();
			this.setContentView(R.layout.food_type_check);
			layout = (LinearLayout) findViewById(R.id.lin_control);

			food = foodListModel[listIndex].list
					.get(checkSpecIndex );
			int len = food.listSize.size();
			tvPrice = (TextView)findViewById(R.id.tv_price);
			tvNum = (TextView)findViewById(R.id.tv_foodcount);
			btnDel = (Button)findViewById(R.id.btn_number_minus);
			llOpt = (LinearLayout)findViewById(R.id.ll_opt_m);
			TextView titleSpec = new TextView(context);
			titleSpec.setText("请选择规格");
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					with,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.bottomMargin = 20;
			titleSpec.setLayoutParams(params);
			titleSpec.setBackgroundColor(getResources().getColor(R.color.topbar_bg_color));
			titleSpec.setTextColor(Color.rgb(255, 255, 255));
			layout.addView(titleSpec);
			titleSpec = (TextView)findViewById(R.id.tv_name);
			titleSpec.setText(food.getName());
			
			specGroup = new RadioGroupEx(ShopDetail.this);
			//specGroup.setOrientation(LinearLayout.HORIZONTAL);
			specGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					attrIndex = checkedId - (startID + 1);
					int count = app.shopCartModel.getFoodCountInAttr(app.mShop.getId(), food.getId(), food.listSize.get(attrIndex).cId);
					if(count > 0){
						llOpt.setVisibility(View.VISIBLE);
						tvNum.setText(String.valueOf(count));
					}else{
						llOpt.setVisibility(View.GONE);
						tvNum.setText("0");
					}
					tvPrice.setText(String.format("%s%.2f", getResources().getString(R.string.public_moeny_0), food.listSize.get(attrIndex).price));
				}
			});
			specGroup.setId(id);
			radioGoupID.add(id);
			id++;
			RadioButton radio = null;
			params = new RadioGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.bottomMargin = 5;
			params.leftMargin = 5;
			ColorStateList colors = getResources().getColorStateList(R.drawable.radio_botton_text_color_selector_spec);
			/*int w = 0;
			int y = 38;
			int x  = 0;
			int radioHeight = 0;*/
			for (int i = 0; i < len; i++) {
				/*if(y == 0 && radio != null){
					y = (int)radio.getY();
				}*/
				radio = creadiRadioButton(food, id, params, colors, food.listSize.get(i).name);
				//params.bottomMargin = 5;
				/*TextPaint paint = radio.getPaint();
				w = (int)paint.measureText(food.listSize.get(i).name) + 36;
				if(radioHeight == 0){
					FontMetrics fm = paint.getFontMetrics();  
					radioHeight =  (int) Math.ceil(fm.descent - fm.top) + 2 + 3;//自提高度加上3个
				}
				
				if(x + w > with){
					//w = 0;
					x = 0;
					y = 38;
					
				}*/
				//w += w1;
				
				
				
				
				
				//radio.setY(y);
				if (i == 0){
					radio.setChecked(true);
					tvPrice.setText(String.format("%s%.2f", getResources().getString(R.string.public_moeny_0), food.listSize.get(i).price));
					int count = app.shopCartModel.getFoodCountInAttr(app.mShop.getId(), food.getId(), food.listSize.get(i).cId);
					if(count > 0){
						llOpt.setVisibility(View.VISIBLE);
						tvNum.setText(String.valueOf(count));
					}
					//y += 3;
				}/*else if(x != 0){
					//y += 5;
					RadioGroup.LayoutParams params2 = new RadioGroup.LayoutParams(android.widget.RadioGroup.LayoutParams.WRAP_CONTENT, android.widget.RadioGroup.LayoutParams.WRAP_CONTENT);
					params2.setMargins(x, (y * -1), 0, 0);
					//params2.set
					radio.setLayoutParams(params2);
					if(y > radioHeight)
						y -= 3;
				}
				x += w;*/
				id++;
				specGroup.addView(radio);
			}

			layout.addView(specGroup);

			len = food.listMake.size();
			FoodMakeModel make;
			int l = 0;
			TextView titleMake;
			RadioGroup Group;
			LinearLayout lay;
			CheckBox checkBox;
			params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			int mustCheck = 0;
			for (int i = 0; i < len; i++) {
				make = food.listMake.get(i);
				titleMake = new TextView(context);
				titleMake.setLayoutParams(new LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				titleMake.setBackgroundColor(getResources().getColor(R.color.topbar_bg_color));
				titleMake.setText(make.getName());
				titleMake.setTextColor(Color.rgb(255, 255, 255));
				layout.addView(titleMake);
				if (make.getCheckType() == 0) {
					Group = new RadioGroup(context);
					Group.setOrientation(LinearLayout.HORIZONTAL);
					Group.setId(id);
					radioGoupID.add(id);
					id++;
					l = make.listAttr.size();
					for (int j = 0; j < l; j++) {
						radio = new RadioButton(context);
						radio.setTextColor(Color.rgb(0, 0, 0));
						radio.setTextSize(12.0f);
						radio.setText(make.listAttr.get(j).name/* + " ￥"
								+ String.valueOf(make.listAttr.get(j).price)*/);
						radio.setTag(make.getName());// 规格名称
						radio.setId(id);
						if (make.getCheck() == 1 && j == 0) {
							radio.setChecked(true);
						}
						id++;
						Group.addView(radio);
					}
					layout.addView(Group);
				} else {
					// 多选
					lay = new LinearLayout(context);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					l = make.listAttr.size();

					for (int j = 0; j < l; j++) {
						checkBox = new CheckBox(context);
						checkBox.setLayoutParams(params);
						checkBox.setTextColor(Color.rgb(0, 0, 0));
						checkBox.setTextSize(12.0f);
						checkBox.setText(make.listAttr.get(j).name + " ￥"
								+ String.valueOf(make.listAttr.get(j).price));
						checkBox.setTag(R.id.title_bar_content_tv, make.getName());
						checkBox.setId(id);
						// checkBox.setTag(i);
						checkBoxID.add(id);
						lay.addView(checkBox);
						id++;
						if (make.getCheck() == 1) {
							if (checkBoxCount == null) {
								checkBoxCount = new int[len];// 记录最大
							}
							checkBox.setTag(mustCheck);
							checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(
										CompoundButton arg0, boolean arg1) {
									// TODO Auto-generated method stub
									int n = (Integer) arg0.getTag();
									// int m = 0;
									if (arg1) {
										CondimentDialog.this.checkBoxCount[n]++;
									} else {
										CondimentDialog.this.checkBoxCount[n]--;
										if (CondimentDialog.this.checkBoxCount[n] == 0) {
											arg0.setChecked(true);
											CondimentDialog.this.checkBoxCount[n]++;
											Toast t = Toast.makeText(
													ShopDetail.this
															.getApplicationContext(),
													"必须要选择一个", 5);
											t.show();
										}
									}
								}

								

							});
							if (j == l - 1) {

								checkBoxCount[mustCheck] = 0;
								checkBox.setChecked(true);
								mustCheck++;
							}
							// int checkCount = 1;
							// checkBoxCount.add(checkCount);

						}
					}
					layout.addView(lay);
				}
			}

			LayoutParams params1 = getWindow().getAttributes();
			params1.height = LayoutParams.WRAP_CONTENT;
			params1.width = LayoutParams.WRAP_CONTENT;
			getWindow().setAttributes(
					(android.view.WindowManager.LayoutParams) params1);

			btnPlus = (Button) findViewById(R.id.btn_number_plus);
			btnPlus.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*int len = radioGoupID.size();
					FoodModel food = foodListModel[listIndex].list
							.get(checkSpecIndex );
					RadioGroup group;
					RadioButton radio;
					CheckBox check;
					float price = 0.0f;
					String name = food.getFoodName() + "-";
					int rid = 0;
					String value[];
					boolean bend = false;
					for (int i = 0; i < len; i++) {
						group = (RadioGroup) findViewById(radioGoupID.get(i));
						rid = group.getCheckedRadioButtonId();
						if (rid != -1) {
							radio = (RadioButton) findViewById(rid);
							if (i != 0) {
								
								name += radio.getTag() + ":";
							}
							name += radio.getText().toString();
							value = radio.getText().toString().split(" ￥");
							price += Float.parseFloat(value[1]);
							if (bend) {
								name += "#";
							}
							if (i == 0) {
								name += "《";
								bend = true;
							}
						}
					}
					len = checkBoxID.size();
					for (int i = 0; i < len; i++) {
						check = (CheckBox) findViewById(checkBoxID.get(i));
						if (check.isChecked()) {
							// 组装菜单
							name += check.getTag(R.id.title_bar_content_tv);
							name += ":" + check.getText().toString();
							value = check.getText().toString().split(" ￥");
							price += Float.parseFloat(value[1]);
							if (bend) {
								name += "#";
							}
						}
					}
					if (bend) {
						name += "》";
					}
					name = name.replace("#》", "》");
					FoodInOrderModel foodInOrderModel = new FoodInOrderModel();
					foodInOrderModel.setId(food.getFoodId());
					foodInOrderModel.setName(name);
					foodInOrderModel.setPrice(price);
					foodInOrderModel.setCount(1);

					ShopDetail.this.setCondition();
					dismiss();*/
					
					if (shopmodeDetailModel.getStatus() < 1 && shopmodeDetailModel.getShopType() == 1) {
						Toast.makeText(ShopDetail.this, getResources().getString(R.string.shop_detail_close_notice)/*"商家休息中，无法订单！请换其他商家！"*/, 15)
								.show();
						return;
					}
					/*if (app.shopCartModel.list.size() > 0 && app.shopCartModel.list.get(0).getShopId() != Integer.parseInt(shopmodeDetailModel.getDataID())) {
						//不是同一个商家
						app.shopCartModel.clear();
					}*/
					
					//int pt = (Integer) v.getTag();
					//FoodModel model = foodListModel[index].list.get(pt);
					
					
					
					app.mShop.setSendMoney(shopmodeDetailModel.getSendmoney());
					app.mShop.setFullFreeMoney(shopmodeDetailModel
							.getFreeSendMoney());
					app.mShop.setStartSendMoney(shopmodeDetailModel.getMinMoney());
					app.mShop.setLatitude(shopmodeDetailModel.getlat());
					app.mShop.setLongtude(shopmodeDetailModel.getlng());
					
					app.mShop
							.setSendDistance(shopmodeDetailModel.getSendDistance());
					app.mShop.setMaxKM(shopmodeDetailModel.getMaxKM());
					app.mShop.setMinKM(shopmodeDetailModel.getMinKM());
					app.mShop
							.setSendFeeAffKM(shopmodeDetailModel.getSendFeeAffKM());
					app.mShop.setSendFeeOfKM(shopmodeDetailModel.getSendFeeOfKM());
					app.mShop
							.setStartSendFee(shopmodeDetailModel.getStartSendFee());
					app.mShop.tagListModel = shopmodeDetailModel.tagListModel;
					app.mShop.setPacketFee(shopmodeDetailModel.getPackagefree());
					
					int count = app.shopCartModel.addFoodAttr(app.mShop, food, attrIndex, 1);
					if(count > 0){
						llOpt.setVisibility(View.VISIBLE);
						tvNum.setText(String.valueOf(count));
					}
					int stock = food.getStock() - app.shopCartModel
							.getOneFoodCount(app.mShop, food.getId());
					if(stock < 1){
						btnPlus.setVisibility(View.GONE);
					}
					setCondition();

				}
			});
			int stock = food.getStock() - app.shopCartModel
					.getOneFoodCount(app.mShop, food.getId());
			if(stock < 1){
				btnPlus.setVisibility(View.GONE);
			}else{
				btnPlus.setVisibility(View.VISIBLE);
			}
			btnDel.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int count = app.shopCartModel.delFoodAttr(
							app.mShop, food, attrIndex);
					if(count == 0){
						llOpt.setVisibility(View.GONE);
						tvNum.setText("0");
					}else{
						tvNum.setText(String.valueOf(count));
					}
					int stock = food.getStock() - app.shopCartModel
							.getOneFoodCount(app.mShop, food.getId());
					if(stock > 0){
						btnPlus.setVisibility(View.VISIBLE);
					}
					setCondition();
				}
			});
			CancelButton = (Button) findViewById(R.id.btnCancel);
			CancelButton.setOnClickListener(new Button.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();

				}
			});

		}

		protected void onStop() {
			// Log.e("dialog onStop",
			// "dialog onStop+++++++++++++++++++++++++++");
		}

	}
    
    

	public RadioButton creadiRadioButton(FoodModel food, int id,
			android.widget.LinearLayout.LayoutParams params,
			ColorStateList colors, String name) {
		// TODO Auto-generated method stub
		RadioButton radio = new RadioButton(ShopDetail.this);
		radio.setBackgroundResource(R.drawable.gray_empt_round_selector);
		radio.setButtonDrawable(R.drawable.gray_empt_round_selector);
		//radio.setTextColor(getResources().getColor(R.drawable.radio_botton_text_color_selector_spec));
		
		radio.setTextColor(colors);
		radio.setTextSize(12.0f);
		radio.setLayoutParams(params);
		radio.setText(name);
		radio.setId(id);
		return radio;
	}

}
