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
import com.ihangjing.ZJYXForAndroid.CommentList.ViewHolderEdit;
import com.ihangjing.ZJYXForAndroid.ShopCart.FoodListHolder;
import com.ihangjing.ZJYXForAndroid.ShopDetail.shopCartListViewAdapter;
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

public class FoodDetail extends HjActivity implements HttpRequestListener {

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


	// private TextView gotomapTextView;
	private String dialogStr;
	HttpManager localHttpManager = null;

	private ArrayList<View> listMainViews;
	protected boolean isReadFood = false;
	private LayoutInflater inflater;
	// private View headView;
	// private TextView netErrorTextView;
	private View footView;
	private ProgressBar more_shop_pb;
	private TextView more_food_tv;
	protected boolean isMoreShopViewClick;
	protected int listIndex = 0;
	private int pageindex = 1;
	private String typeidString = "0";

	// private HJGridView typeGridView;
	public float totalFoodPrice;
	public int totalFoodCount;

	private int total;
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
	protected EditText nowEdit;// 记录当前编辑的edit（输入数字）
	
	private TextView tvShopGood;
	private TextView tvShopOK;
	private TextView tvShopBad;
	
	private String errMsg;
	protected int commandType;
	private TextView conditionTextView1;
	private Button btnNext;
	private RelativeLayout rlBottom;
	
	
	protected int payType = 1;
	protected int buyType;
	protected int eatType;
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
	private RelativeLayout foodImgView;
	private ArrayList<Integer> radioGoupID;
	private ArrayList<Integer> checkBoxID;
	private LinearLayout layout;
	private TextView tvNum;
	private Button btnDel;
	private LinearLayout llOpt;
	private RadioGroupEx specGroup;
	private Button btnPlus;
	private Button CancelButton;
	int startID = 0x10000000;
	int id = startID;
	int checkBoxCount[];// 记录必选框中选中了多少个（checkBox）
	int attrIndex = 0;//当前选中的规格索引
	private TextView tvShowPrice;
	private TextView tvShopCartCount;
	
	public FoodDetail() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate()");

		
		handler = new UIHandler();
		mLoadImage = new LoadImage(FoodDetail.this);
		shopIndex = new HJInteger();
		shopIndex.value = -1;
		initUI();
		
		

		
		
	}

	

	

	private void initUI() {
		this.setContentView(R.layout.food_detail_dialog);
		
		foodImgView = (RelativeLayout)findViewById(R.id.rl_mage);
		foodImgView.setTag(app.mFood.getImageNetPath());
		
		TextView titleSpec = (TextView)findViewById(R.id.tv_name);
		titleSpec.setText(app.mFood.getName());
		
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int sWidth = (int) (display.getWidth()); // 设置宽度
		int sHeight = (int) (display.getHeight()); // 设置宽度
		
		
		Bitmap img = mLoadImage.getBitmap(app.mFood.getImageNetPath());
		if(img != null){
			int width = img.getWidth();
			int height = img.getHeight();
			float bix = (float) width / (float) height;
			int oWidth = sWidth;
			int oHeight = (int) (oWidth / bix);
			if (oHeight > sHeight) {
				oHeight = sHeight;
				oWidth = (int)(oHeight * bix);
			}
			LinearLayout.LayoutParams laParams = new LinearLayout.LayoutParams(
					oWidth, oHeight);
			foodImgView.setLayoutParams(laParams);
			foodImgView.setBackgroundDrawable(new BitmapDrawable(img));
		}else{
			
			int oWidth = sWidth;
			int oHeight = (int) (oWidth / 1.3333333);
			
			LinearLayout.LayoutParams laParams = new LinearLayout.LayoutParams(
					oWidth, oHeight);
			foodImgView.setLayoutParams(laParams);
			if (app.mFood.getImageNetPath() != null
					&& app.mFood.getImageNetPath().length() > 18) {// http://*.*.*/*.jpg
				// 有网络地址，并且没有加入下载，那么加入
				// model.isDowload = true;
				foodImgView.setTag(app.mFood.getImageNetPath());
				mLoadImage.addTask(app.mFood.getImageNetPath(),
						foodImgView, R.drawable.food_defalut);
			} else {
				foodImgView
						.setBackgroundResource(R.drawable.food_defalut);
			}
		}
		
		int with = sWidth;
		radioGoupID = new ArrayList<Integer>();
		checkBoxID = new ArrayList<Integer>();
		
		layout = (LinearLayout) findViewById(R.id.lin_control);

		
		int len = app.mFood.listSize.size();
		tvShowPrice = (TextView)findViewById(R.id.tv_price);
		tvNum = (TextView)findViewById(R.id.tv_foodcount);
		btnDel = (Button)findViewById(R.id.btn_number_minus);
		llOpt = (LinearLayout)findViewById(R.id.ll_opt_m);
		titleSpec = new TextView(FoodDetail.this);
		titleSpec.setText("请选择规格");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				with,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = 20;
		titleSpec.setLayoutParams(params);
		titleSpec.setBackgroundColor(getResources().getColor(R.color.topbar_bg_color));
		titleSpec.setTextColor(Color.rgb(255, 255, 255));
		layout.addView(titleSpec);
		
		titleSpec = (TextView)findViewById(R.id.tv_pro_des_content);
		titleSpec.setText(app.mFood.getDisc());
		specGroup = new RadioGroupEx(FoodDetail.this);
		//specGroup.setOrientation(LinearLayout.HORIZONTAL);
		specGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				attrIndex = checkedId - (startID + 1);
				setCount();
				
			}
		});
		specGroup.setId(id);
		radioGoupID.add(id);
		id++;
		RadioButton radio;
		params = new RadioGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = 5;
		params.leftMargin = 5;
		ColorStateList colors = getResources().getColorStateList(R.drawable.radio_botton_text_color_selector_spec);
		String name;
		/*int w = 0;
		int y = 38;
		int x  = 0;*/
		int radioHeight = 0;
		for (int i = 0; i < len; i++) {
			
			if(app.mFood.listSize.get(i).name != null && app.mFood.listSize.get(i).name.length() > 0){
				name = app.mFood.listSize.get(i).name;
			}else{
				name = app.mFood.getFoodName();
			}
			radio = creadiRadioButton(app.mFood, id, params, colors, name);
			/*TextPaint paint = radio.getPaint();
			w = (int)paint.measureText(name) + 36;
			if(radioHeight == 0){
				FontMetrics fm = paint.getFontMetrics();  
				radioHeight =  (int) Math.ceil(fm.descent - fm.top) + 2 + 3;//自提高度加上3个
			}
			if(x + w > with){
				//w = 0;
				x = 0;
				y = 38;
			}*/
			if (i == 0){
				radio.setChecked(true);
				tvShowPrice.setText(String.format("%s%.2f", getResources().getString(R.string.public_moeny_0), app.mFood.listSize.get(i).price));
				int count = app.shopCartModel.getFoodCountInAttr(app.mShop.getId(), app.mFood.getId(), app.mFood.listSize.get(i).cId);
				if(count > 0){
					llOpt.setVisibility(View.VISIBLE);
					tvNum.setText(String.valueOf(count));
				}
			}/*else if(x != 0){
				RadioGroup.LayoutParams params2 = new RadioGroup.LayoutParams(android.widget.RadioGroup.LayoutParams.WRAP_CONTENT, android.widget.RadioGroup.LayoutParams.WRAP_CONTENT);
				params2.setMargins(x, (y * -1), 0, 0);
				radio.setLayoutParams(params2);
				if(y > radioHeight)
					y -= 3;
			}
			x += w;*/
			id++;
			specGroup.addView(radio);
		}

		layout.addView(specGroup);

		len = app.mFood.listMake.size();
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
			make = app.mFood.listMake.get(i);
			titleMake = new TextView(FoodDetail.this);
			titleMake.setLayoutParams(new LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			titleMake.setBackgroundColor(getResources().getColor(R.color.topbar_bg_color));
			titleMake.setText(make.getName());
			titleMake.setTextColor(Color.rgb(255, 255, 255));
			layout.addView(titleMake);
			if (make.getCheckType() == 0) {
				Group = new RadioGroup(FoodDetail.this);
				Group.setOrientation(LinearLayout.HORIZONTAL);
				Group.setId(id);
				radioGoupID.add(id);
				id++;
				l = make.listAttr.size();
				for (int j = 0; j < l; j++) {
					radio = new RadioButton(FoodDetail.this);
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
				lay = new LinearLayout(FoodDetail.this);
				lay.setOrientation(LinearLayout.HORIZONTAL);
				l = make.listAttr.size();

				for (int j = 0; j < l; j++) {
					checkBox = new CheckBox(FoodDetail.this);
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
									checkBoxCount[n]++;
								} else {
									checkBoxCount[n]--;
									if (checkBoxCount[n] == 0) {
										arg0.setChecked(true);
										checkBoxCount[n]++;
										Toast t = Toast.makeText(
												FoodDetail.this
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
				String name = app.mFood.getFoodName() + "-";
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
				foodInOrderModel.setId(app.mFood.getFoodId());
				foodInOrderModel.setName(name);
				foodInOrderModel.setPrice(price);
				foodInOrderModel.setCount(1);

				ShopDetail.this.setCondition();
				dismiss();*/
				
				if (app.mShop.getStatus() < 1 && app.mShop.getShopType() == 1) {
					Toast.makeText(FoodDetail.this, getResources().getString(R.string.shop_detail_close_notice)/*"商家休息中，无法订单！请换其他商家！"*/, 15)
							.show();
					return;
				}
				/*if (app.shopCartModel.list.size() > 0 && app.shopCartModel.list.get(0).getShopId() != app.mShop.getId()) {
					//不是同一个商家
					app.shopCartModel.clear();
				}
				*/
				//int pt = (Integer) v.getTag();
				//FoodModel model = foodListModel[index].list.get(pt);
				
				
				
				/*app.mShop.setSendMoney(shopmodeDetailModel.getSendmoney());
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
				app.mShop.setPacketFee(shopmodeDetailModel.getPackagefree());*/
				
				int count = app.shopCartModel.addFoodAttr(app.mShop, app.mFood, attrIndex, 1);
				if(count > 0){
					llOpt.setVisibility(View.VISIBLE);
					tvNum.setText(String.valueOf(count));
				}
				int stock = app.mFood.getStock() - app.shopCartModel
						.getOneFoodCount(app.mShop, app.mFood.getId());
				if(stock < 1){
					btnPlus.setVisibility(View.GONE);
				}
				setCondition();;

			}
		});
		int stock = app.mFood.getStock() - app.shopCartModel
				.getOneFoodCount(app.mShop, app.mFood.getId());
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
						app.mShop, app.mFood, attrIndex);
				if(count == 0){
					llOpt.setVisibility(View.GONE);
					tvNum.setText("0");
				}else{
					tvNum.setText(String.valueOf(count));
				}
				int stock = app.mFood.getStock() - app.shopCartModel
						.getOneFoodCount(app.mShop, app.mFood.getId());
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
				finish();

			}
		});
		llShopCart = (RelativeLayout)findViewById(R.id.ll_shop_cart);
		llShopCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				llShopCart.setVisibility(View.GONE);
			}
		});
		tvRemove = (TextView)findViewById(R.id.tv_remove);
		tvRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.shopCartModel.removeShopAllFoodWithShopId(app.mShop.getId(), shopIndex);
				llOpt.setVisibility(View.GONE);
				tvNum.setText("0");
				setCondition();
			}
		});
		lvShopCart = (ListView)findViewById(R.id.lv_shopcart);
		mShopCardAdadpter = new shopCartListViewAdapter();
		lvShopCart.setAdapter(mShopCardAdadpter);
		tvPacketFee = (TextView)findViewById(R.id.tv_packet_fee);
		
		
		
		
		rlBottom = (RelativeLayout)findViewById(R.id.ll_bottom);
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
		
		conditionTextView1 = (TextView) findViewById(R.id.tv_cart_info);
		tvShopCartCount = (TextView)findViewById(R.id.tv_shop_count);
		btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					gotoShopCart();
				
				
			}
		});

	}
	
	
	
	public HJInteger shopIndex;
	private float pacektFee;
	private float sendFee;
	public int checkSpecIndex = 0;//当前选择规格的商品
	
	
	
	protected void gotoShopCart() {
		// TODO Auto-generated method stub
		if (app.shopCartModel.list.size() == 0) {
			Toast.makeText(FoodDetail.this, getResources().getString(R.string.shop_detail_shopcart_empt), 5).show();
			return;

		}
		app.shopCartModel.setOrderType(0);
		Intent intent = new Intent(FoodDetail.this, ShopCart.class);
		startActivity(intent);
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
							&& (FoodDetail.this.localHttpManager != null)) {
						FoodDetail.this.localHttpManager.cancelHttpRequest();
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

					Intent localIntent4 = new Intent(FoodDetail.this,
							Login.class);
					FoodDetail.this.startActivity(localIntent4);
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
		}
		return dialog;
	}

	private class UIHandler extends Handler {
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
			case ORDER_SUBMIT_FAILD: {
				j_showDialog(errorMSG);
				OtherManager.Toast(FoodDetail.this, getResources().getString(R.string.shop_cart_add_order_faild)/*"提交订单失败，请稍候再试！"*/);
				break;
			}
			case COMMAND_FAIL:
				Toast.makeText(FoodDetail.this, errMsg, 15).show();
				break;
			
			case NO_COMMAND:
				Toast.makeText(FoodDetail.this, getResources().getString(R.string.com_no_data_1)/*"该商家暂无评论信息"*/, 15).show();
				break;
			
			
			
			
			case NET_ERROR:
				Toast.makeText(FoodDetail.this, getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误！请稍候再试"*/, 5).show();
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
	

	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
		// this.isActivityRun = false;
	}

	// 设置购物车的显示：一共点了几份菜 多少钱
	private void setCondition() {
		if (app.shopCartModel != null) {
			totalFoodPrice = app.shopCartModel.getShopTotalPriceWithShopId(app.mShop.getId(), shopIndex);
			totalFoodCount = app.shopCartModel.getFoodCountWithShopID(app.mShop.getId(), shopIndex);
			if(app.mShop.getShopType() == 2){//预定不需要打包费和配送费
				pacektFee = 0.0f;
				sendFee = 0.0f;
			}else{
				pacektFee = app.shopCartModel.getShopPacketFeeWithShopId(app.mShop.getId(), shopIndex);
				sendFee = app.shopCartModel.getSendFeeWithShopId(app.mShop.getId(), shopIndex);
				totalFoodPrice += (pacektFee + sendFee);
			}
			
			// 通过加入购物车的计算器进行计算
			String conditon = String.format(getResources().getString(R.string.order_condition) , totalFoodPrice, getResources().getString(R.string.public_moeny));///*"已点:%d份 共%.2f"*/
			
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
			//mShopCardAdadpter.notifyDataSetChanged();
		}
		mShopCardAdadpter.notifyDataSetChanged();
		setCount();
		
	}
	public void setCount()
	{
		int count = app.shopCartModel.getFoodCountInAttr(app.mShop.getId(), app.mFood.getId(), app.mFood.listSize.get(attrIndex).cId);
		if(count > 0){
			llOpt.setVisibility(View.VISIBLE);
			tvNum.setText(String.valueOf(count));
		}else{
			llOpt.setVisibility(View.GONE);
			tvNum.setText("0");
		}
		tvShowPrice.setText(String.format("%s%.2f", getResources().getString(R.string.public_moeny_0), app.mFood.listSize.get(attrIndex).price));
	}
	public Bitmap getBitmap(FoodModel model) {
		// TODO Auto-generated method stub
		Bitmap originalImage;

		if (model.getImageLocalPath() != null
				&& !model.getImageLocalPath().equals("")) {// 本地地址不为空
			originalImage = OtherManager.getBitmap(model.getImageLocalPath());
			if (originalImage == null) {
				originalImage = BitmapFactory.decodeResource(
						FoodDetail.this.getResources(), R.drawable.icon);

			}
		} else {// 本地和网络地址都为空，使用id
			originalImage = BitmapFactory.decodeResource(
					FoodDetail.this.getResources(), R.drawable.icon);

		}

		return originalImage;
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

				
				case 4:
					foodModel = new FoodModel(json);
					message.what = UIHandler.GET_FOOD_DETAIL;
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
		
		default:
			break;
		}
		handler.sendMessage(message);
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
				n = app.shopCartModel.getShopCartListSizeWithShopID(app.mShop.getId(), shopIndex);
			}
			return n;
		}

		@Override
		public Object getItem(int paramInt) {
			return app.shopCartModel.getFoodInOrderModelWithShopId(app.mShop.getId(), shopIndex, paramInt);
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
				if (inflater == null) {
					inflater = (LayoutInflater) FoodDetail.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(
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

	

	
    
    

	public RadioButton creadiRadioButton(FoodModel food, int id,
			android.widget.LinearLayout.LayoutParams params,
			ColorStateList colors, String name) {
		// TODO Auto-generated method stub
		RadioButton radio = new RadioButton(FoodDetail.this);
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
