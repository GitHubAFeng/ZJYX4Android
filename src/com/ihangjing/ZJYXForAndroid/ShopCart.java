package com.ihangjing.ZJYXForAndroid;

import java.io.StringReader;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.alipay.sdk.app.PayTask;
import com.ihangjing.Model.CouponListModel;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.auth.NTCredentials;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ihangjing.Model.CardListModel;
import com.ihangjing.Model.FoodInOrderModel;
import com.ihangjing.Model.FoodInOrderModelForGet;
import com.ihangjing.Model.MyCouponModel;
import com.ihangjing.Model.MyCouponsListModel;
import com.ihangjing.Model.ReciveAddressListModel;
import com.ihangjing.Model.ReciveAddressModel;
import com.ihangjing.Model.SelfStateListModel;
import com.ihangjing.Model.ShopCartModel;
import com.ihangjing.Model.ShopDiscountItemView;
import com.ihangjing.Model.TagModel;
import com.ihangjing.alipay.Keys;
import com.ihangjing.alipay.Result;
import com.ihangjing.alipay.SignUtils;
import com.ihangjing.common.HJInteger;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.common.LoadImage;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;
import com.ihangjing.wxpay.Constants;
import com.ihangjing.wxpay.MD5;
import com.ihangjing.wxpay.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.unionpay.UPPayAssistEx;

/*
 * zjf@ihangjing.com 2011.10.28
 * 购物车 菜单页面传输过来商品的编号列表
 * 此页面保存编号 
 */
public class ShopCart extends HjActivity implements HttpRequestListener {
	// implements View.OnClickListener
	// private Button forwordButton1;
	private Button nextBuuton1;
	public static final String R_FAIL = "fail";
	public static final String R_SUCCESS = "success";
	public static final String R_CANCEL = "cancel";
	private ScrollView nextScrollView;// 下一步后输入联系方式
	private RelativeLayout firstView;
	private String addressID;

	private UIHandler hander;

	// private LinearLayout shopcartLinearLayout;// 显示购物车的LinearLayout
	private MyLinearLayout mylinear;
	public LayoutInflater inflater;

	private static final String TAG = "ShopCart";
	

	// 提交订单按钮
	private Button submitshopcarButton;
	int hour;
	int minute;
	//private EditText etAddress;
	//private EditText etUsername;
	//private EditText etPhone;
	private EditText etNote;
	// 送货上门
	private EditText etSentDate1;// 送餐日期
	private EditText etSentTime1;// 送餐时间

	//private OrderModel orderModel;// 订单信息

	private String orderidString;

	FoodListViewAdapter mSchedule = null;
	private String dialogStr;

	private int actionTag = 0;
	private float SendFee = 0.0f;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	private static final int SHOW_DATAPICK = 10;
	private static final int DATE_DIALOG_ID = 11;
	private static final int SHOW_TIMEPICK = 12;
	private static final int TIME_DIALOG_ID = 13;
	private static final double EARTH_RADIUS = 6378137;//地球半径，米;

	
	private int sendtype = 1;// 0:送货上门；1：自取；
	/*
	 * LinearLayout _getsendtime1; LinearLayout _getsendtime2; LinearLayout
	 * _getsendtime3;
	 */
	/**
	 * 支付方式整体:线上(0)，线下(1)
	 */
	private int paytype = -1;
	private int paydetailtype = 3;
	//LinearLayout _s_pay_offline;
	EditText etPayPassword;

	
	TextView gf_sendfree;
	TextView gf_foodmoney;
	TextView gf_cardpay;
	TextView gf_allmoney;
	// 输入礼品卡密码，提交
	Button check_giftcard_bt_;
	// 取消当前礼品卡按钮
	Button Cancel_giftcard_bt_;
	ProgressDialog progressDialog = null;
	String giftcardpwdString = "";// 礼品卡密码
	String shopCouponpwd = "";// 店铺店铺券密码

	EditText tbpwd1_;
	EditText tbpwd2_;
	EditText tbpwd3_;
	EditText tbpwd4_;

	private CardListModel Card;
	private CouponListModel Coupon;

	private String statusString;
	// 礼品卡验证结果
	private String cardmsgString;
	// 礼品卡余额
	private double Card_have_moeny = 0;
	TextView gf_havemoney_TV;

	int CIDStringx = 0;
	String paypasswordString = "";
	
	private float totalFoodPrice;
	private String tel;
	private String Grade = "1";
	// private Button btnSelectAddress;
	// private float sendMoney = 0.0f;
	// private float freeSendMoney = 0.0f;
	protected Builder builder;
	
	private UserAddressDedail dailog;
	
	private boolean isSelectAddr = false;// 是否从选择地址页面返回
	//private LinearLayout llTime;
	
	private ListView foodListView;
	private OnClickListener addToShopCartListener;
	private OnClickListener delFromShopCartListener;
	private OnClickListener removeFromShopCartListener;
	private TextView tvTotalPrice;
	private Button btnBacke;
	private LinearLayout llSelfState;
	private EditText etSelfState;
	private SelfStateListModel selfStateList;
	private LinearLayout llCoupon;
	protected int userCoupon;
	private boolean isSelectCoupon;
	private CouponListFoodAdapter couponListFoodAdapter;
	public View moreView;
	protected TextView tvMore;
	OnClickListener delCoupon;
	//private EditText nowEdit;
	// 备注选项
	public String alipayInfo;
	private int addrSelectIndex;//记录选择的地址第几个
	private LinearLayout llNewAddr;
	//private LinearLayout llOldAddr;
	private EditText etNewUser;
	private EditText etNewPhone;
	private EditText etNewAddr;
	private EditText etGPSAddr;
	private RadioGroup rgAdd;
	private Button btnAddNewAddress;
	private boolean userGPS = true;//默认使用GPS地址
	private ReciveAddressModel addrModel;
	public float payMoney;
	
	private StringBuffer wxpay;
	private PayReq wxpayReq;
	public Map<String, String> resultunifiedorder;
	private LinearLayout llICON;
	public boolean isPayWX = false;
	private int totalFoodCount;
	private float pacektFee;
	private float sendFee;
	private RadioGroup rgTime;
	private RadioButton rbPayType0;
	private RadioButton rbPayType1;
	private RadioButton rbPayType2;
	private RadioButton rbPayType3;
	private RadioGroup paytypegGroup;

	private void InitUI() {
		setContentView(R.layout.shop_cart);
		inflater = LayoutInflater.from(ShopCart.this);
		nextScrollView = (ScrollView) findViewById(R.id.shopcart_scrollView1);
		firstView = (RelativeLayout) findViewById(R.id.s_linearLayout8);
		tvTotalPrice = (TextView)findViewById(R.id.tv_price);
		llICON = (LinearLayout)findViewById(R.id.ll_icon);
		if (app.shopCartModel.list.get(0).tagListModel.list.size() > 0) {
			
			for (int i = 0; i < app.shopCartModel.list.get(0).tagListModel.list.size(); i++) {
				TagModel model = app.shopCartModel.list.get(0).tagListModel.list.get(i);
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
					app.mLoadImage.addTask(model.getImageNet(),
							discountItemView.rlImg, R.drawable.food_class);
				}
				llICON.addView(view);
				discountItemView.tvInfo.setText(model.getInfo());
			}
		}
		etNote = (EditText)findViewById(R.id.et_remark);
		btnBacke = (Button)findViewById(R.id.title_bar_back_btn);
		btnBacke.setVisibility(View.VISIBLE);
		btnBacke.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Back();
				//btnBacke.setVisibility(View.GONE);
			}
		});		

		
		foodListView = (ListView) findViewById(R.id.foodlist_listView1);
		mSchedule = new FoodListViewAdapter();
		
		btnAddNewAddress = (Button)findViewById(R.id.btn_reminders);
		btnAddNewAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddNewAddress();
			}
		});
		foodListView.setAdapter(mSchedule);
		
		
			
		rgTime = (RadioGroup)findViewById(R.id.rg_sendtime);
		
		etSentDate1 = (EditText) findViewById(R.id.etSentDate);
		etSentTime1 = (EditText) findViewById(R.id.etSentTime);

		// 取出记忆中的信息

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		submitshopcarButton = (Button) findViewById(R.id.btn_submit);
		submitshopcarButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				// 获得各类信息今天提交并进入订单成功页面/错误提示信息
				// 1. 检查输入框是否合法输入
				

				
				if (!userGPS && addrModel == null) {
					Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_s_addr_notice)/*"请选择地址"*/, 15).show();
					return;
				}
				String address;
				app.shopCartModel.setAddtime("");
				//if (rgAdd.getCheckedRadioButtonId() == R.id.rb_new) {
					if (etNewUser.getText().length() < 1) {
						etNewUser.setError(getResources().getString(R.string.shop_cart_reciver_notice)/*"请输入收货人姓名！"*/);
						Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_reciver_notice)/*"请输入收货人姓名！"*/, 15).show();
						return;
					}
					if (etNewPhone.getText().length() < 7) {
						etNewPhone.setError(getResources().getString(R.string.shop_cart_phone_notice)/*"请输入正确的联系方式！"*/);
						Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_phone_notice)/*"请输入正确的联系方式！"*/, 15).show();
						return;
					}
					if (etGPSAddr.getText().length() < 1) {
						etGPSAddr.setError(getResources().getString(R.string.shop_cart_addr_notice)/*"请输入正确的地址！"*/);
						Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_addr_notice)/*"请输入正确的地址！"*/, 15).show();
						return;
					}
					if (paytype == 3 && etPayPassword.getText().length() < 6) {//账户余额支付
						etPayPassword.setError(getResources().getString(R.string.shop_cart_pay_password_notice)/*"请输入支付密码！"*/);
						Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_pay_password_info)/*"请输入支付密码！如果您没有设置过支付密码，请到用户中心设置！"*/, 15).show();
						return;
					}
					if (userGPS) {
						address = etGPSAddr.getText().toString() + etNewAddr.getText().toString();
						SharedPreferences usersetting = getApplicationContext()
								.getSharedPreferences(HJAppConfig.CookieName,
										Context.MODE_PRIVATE);
						Editor userinfoeditor = usersetting.edit();
						userinfoeditor.putString("order_phone", etNewPhone.getText()
								.toString());
						userinfoeditor.putString("order_username", etNewUser.getText()
								.toString());
						userinfoeditor.putString("order_address", etNewAddr.getText().toString());
						userinfoeditor.commit();
						app.shopCartModel.setLat(String.valueOf(app.useLocation.getLat()));
						app.shopCartModel.setLng(String.valueOf(app.useLocation.getLon()));
						
					}else{
						address = etNewAddr.getText().toString();
						app.shopCartModel.setLat(String.valueOf(addrModel.getLat()));
						app.shopCartModel.setLng(String.valueOf(addrModel.getLon()));
					}
					
					app.shopCartModel.setPhone(etNewPhone.getText().toString());
					
					
					app.shopCartModel.setCustName(etNewUser.getText().toString());
					
					app.shopCartModel.setAddress(address);
				/*}else{
					if (app.reciveAddressList.list.get(app.selectIndex).getPhone().length() < 1) {
						etNewPhone.setError("请填写联系人手机号码");
						Toast.makeText(ShopCart.this, "请填写联系人手机号码!", 15).show();
						return;
					} else {
						// 检查电话号码输入是否正确
						if (!checkMobilePhone(app.reciveAddressList.list.get(app.selectIndex).getPhone())) {
							etNewPhone.setError("请输入正确的手机号码");
							Toast.makeText(ShopCart.this, "请填写联系人手机号码!", 15).show();
							return;
						}
					}
					
					if (app.reciveAddressList.list.get(app.selectIndex).getReciver().length() < 2) {
						etNewUser.setError("请填写正确的收货人");
						Toast.makeText(ShopCart.this, "请填写正确的收货人!", 15).show();
					}
					
					if (app.reciveAddressList.list.get(app.selectIndex).getAddres().length() < 3) {
						etNewAddr.setError("请填写联系地址");
						Toast.makeText(ShopCart.this, "请填写联系地址!", 15).show();
						return;
					}
					
					if (userCoupon == 1 && app.couponsKeyList.list.size() == 0) {
						Toast.makeText(ShopCart.this, "您没有选择任何优惠券，无法提交!", 15).show();
						return;
					}
					
					app.shopCartModel.setPhone(app.reciveAddressList.list.get(app.selectIndex).getPhone());

					
					app.shopCartModel.setCustName(app.reciveAddressList.list.get(app.selectIndex).getReciver());
					address = app.reciveAddressList.list.get(app.selectIndex).getAddres();
					app.shopCartModel.setAddress(address);
					app.shopCartModel.setLat(String.valueOf(app.reciveAddressList.list.get(app.selectIndex).getLat()));
					app.shopCartModel.setLng(String.valueOf(app.reciveAddressList.list.get(app.selectIndex).getLon()));
				}*/
				app.shopCartModel.setCityid(app.mSection.SectionID);
				app.shopCartModel.setPayMode(String.valueOf(paytype));
				totalFoodPrice = app.shopCartModel.getTotalprice();

				// 通过加入购物车的计算器进行计算
				/*for (int i = 0; i < app.shopCartModel.list.size(); i++) {
					totalFoodPrice += app.shopCartModel.list.get(i).getCount()
							* app.shopCartModel.list.get(i).getPrice();
				}*/

				totalFoodPrice = totalFoodPrice + SendFee;

				

				
				app.shopCartModel.setNote(etNote.getText().toString());
				app.shopCartModel.setUserName(app.userInfo.userName);
				app.shopCartModel.setUserid(app.userInfo.userId);
				app.shopCartModel.setSentmoney(String.format("%.0f", SendFee));
				app.shopCartModel.setState("0");// 未审核
				//app.shopCartModel.setOrderType("0");// 普通订单
				app.shopCartModel.setOrdersource("2");// Android订单

				String paypasswordString = "";

				//app.shopCartModel.setCID(null);// 默认值为空代表不用礼品卡
				//app.shopCartModel.setCIDStringx("");// 默认值为空代表不用店铺券
				app.shopCartModel.setBid(OtherManager.getUserLocal(ShopCart.this).buildid
						+ "");

				
				paypasswordString = etPayPassword.getText().toString();
				app.shopCartModel.setPayPassword(paypasswordString);
				app.shopCartModel.setSendtype(sendtype + "");
				app.shopCartModel.setPayType(paytype + "");
				/*if (paytype == 5) {

					if (Coupon == null || Coupon.list.size() == 0
							&& Card == null && Card.list.size() == 0) {
						j_showDialog(getResources().getString(R.string.shop_cart_s_coupon_notice));//"请先选择兑换券！");
						return;
					} else {

						//app.shopCartModel.setCID(Coupon);// 默认值为空代表不用礼品卡
						// orderModel.setCIDStringx(Coupon.cardnum);//
						// 默认值为空代表不用店铺券

					}
				}*/

				
				
				Date curDate = new Date(System.currentTimeMillis());//获取当前时间
				SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
				if(rgTime.getCheckedRadioButtonId() == R.id.rb_time1){
					//尽快送达
					
					
					String str = formatter.format(curDate);
					app.shopCartModel.setEattime(str);
				}else{
					//配送时间
					String time = etSentDate1.getText().toString()
							+ " " + etSentTime1.getText().toString() + ":00";
					//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						Date d1 = formatter.parse(time);//用户选择的时间
						if(curDate.getTime() + (app.mShop.getSendTime() * 60 * 1000) > d1.getTime()){
							curDate.setTime(curDate.getTime() + (60 * 1000));
							Toast.makeText(ShopCart.this, "配送时间不能早于" + formatter.format(curDate), 15).show();
							return;
						}
						Date dS1 = formatter.parse(etSentDate1.getText().toString() + " " + app.mShop.getTimeStart1() + ":00");//用户选择的时间
						Date dE1 = formatter.parse(etSentDate1.getText().toString() + " " + app.mShop.getTimeEnd1() + ":00");//用户选择的时间
						Date dS2 = formatter.parse(etSentDate1.getText().toString() + " " + app.mShop.getTimeStart2() + ":00");//用户选择的时间
						
						Date dE2 = formatter.parse(etSentDate1.getText().toString() + " " + app.mShop.getTimeEnd2() + ":00");//用户选择的时间
						if ((d1.getTime() < dS1.getTime() || d1.getTime() > dE1.getTime()) 
								&& (d1.getTime() < dS2.getTime() || d1.getTime() > dE2.getTime())) {
							Toast.makeText(ShopCart.this, "商家的配送时间范围：" + app.mShop.getTimeStart1() + "-" + app.mShop.getTimeEnd1() 
									+ "及" + app.mShop.getTimeStart2() + "-" + app.mShop.getTimeEnd2(), 15).show();
							return;
						}
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(ShopCart.this, "商家的配送时间范围：" + app.mShop.getTimeStart1() + "-" + app.mShop.getTimeEnd1() 
								+ "及" + app.mShop.getTimeStart2() + "-" + app.mShop.getTimeEnd2(), 15).show();
						return;
					}
					
					
					app.shopCartModel.setEattime(time);
				}
				
				app.shopCartModel.setReciveAddressID(addressID);

				
				

				// 提醒用户再次确定提交订单
				AlertDialog.Builder confirmDeletionDialog = new AlertDialog.Builder(
						ShopCart.this);

				DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {

						dialogStr = getResources().getString(R.string.shop_cart_add_order);//"订单提交中......";
						showDialog(322);

						addOrder();
					}
				};
				float sendMoney = 0.0f;
				/*String format = "(不含配送费)\r\n配送费合计：%.2f = ";
				for (int i = 0; i < app.shopCartModel.list.size(); i++) {
					sendMoney += app.shopCartModel.list.get(i).getSendFee();
					if (i == app.shopCartModel.list.size() - 1) {
						format += String.format("\r\n%s:%.2f;\r\n", app.shopCartModel.list.get(i).getShopname(), 
										app.shopCartModel.list.get(i).getSendFee());
					}else{
						format += String.format("\r\n%s:%.2f +", app.shopCartModel.list.get(i).getShopname(), 
								app.shopCartModel.list.get(i).getSendFee());
					}
				}
				String sendFee = String.format(format, sendMoney);*/
				confirmDeletionDialog.setTitle(getResources().getString(R.string.shop_cart_add_notice));
				confirmDeletionDialog.setMessage(String.format(getResources().getString(R.string.shop_cart_add_info), totalFoodPrice + sendFee + pacektFee, address));/*"订单总金额："
						+ String.valueOf(totalFoodPrice) + "(不含配送费)\r\n送货地址："
						+ address + "\r\n确定提交订单?");*/
				confirmDeletionDialog.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local3);
				confirmDeletionDialog.setNegativeButton(getResources().getString(R.string.public_cancel)/*"取消"*/, null);
				confirmDeletionDialog.create().show();
			}
		});

		// 下一步
		nextBuuton1 = (Button) findViewById(R.id.btn_add);
		nextBuuton1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShopCartModel shopCartModel = app.shopCartModel.getShopCarModel(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				if (shopCartModel == null || shopCartModel.list.size() == 0) {
					Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_emp)/*"购物车为空，无法提交订单"*/, 5).show();
					return;
				}
				
				//计算每个商家是否满足配送金额，配送费是否免掉
				//for (int i = 0; i < app.shopCartModel.list.size(); i++) {
					//ShopCartModel model = app.shopCartModel.list.get(i);
					if (shopCartModel.getTotalPrice() < shopCartModel.getMinMoney()) {
						Toast.makeText(ShopCart.this, String.format(getResources().getString(R.string.shop_cart_can_buy_notice)/*"无法提交订单，商家%s的金额未达到最低起送金额：%.2f!"*/, shopCartModel.getShopname(), shopCartModel.getMinMoney()), 18).show();
						return;
					}
					if (shopCartModel.getFullFreeMoney() > 0.0f && shopCartModel.getTotalPrice() >= shopCartModel.getFullFreeMoney()) {
						shopCartModel.setSendFee(0.0f);
					}else{
						shopCartModel.setSendFee(shopCartModel.getSentmoney());
					}
				//}
				
				if (app.userInfo == null || app.userInfo.userId == null
						|| app.userInfo.userId.equals("") || app.userInfo.userId.equals("0")) {
					//
					Intent localIntent4 = new Intent(ShopCart.this,
							Login.class);
					localIntent4.putExtra("isReturn", true);
					ShopCart.this.startActivity(localIntent4);
					Toast.makeText(ShopCart.this, getResources().getString(R.string.public_login_notice)/*"请您先登陆"*/, 5).show();
					return;
				}
				
				Log.v("ShopCart", "nextBuuton");
				// 显示下一步的组件0 �示
				btnBacke.setVisibility(View.VISIBLE);
				firstView.setVisibility(View.GONE);
				nextScrollView.setVisibility(View.VISIBLE);
			}
		});

		// 日期控件

		etSentDate1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (v.isFocused()) {
					Message msg = new Message();
					msg.what = ShopCart.SHOW_DATAPICK;
					ShopCart.this.hander.sendMessage(msg);
				}
			}
		});

		etSentDate1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = ShopCart.SHOW_DATAPICK;
				ShopCart.this.hander.sendMessage(msg);
			}
		});

		// 时间控件

		etSentTime1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (v.isFocused()) {
					Message msg = new Message();
					msg.what = ShopCart.SHOW_TIMEPICK;
					ShopCart.this.hander.sendMessage(msg);
				}
			}
		});

		etSentTime1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = ShopCart.SHOW_TIMEPICK;
				ShopCart.this.hander.sendMessage(msg);
			}
		});
		View.OnClickListener tvOnClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goToSelectAddress();
				
				
			}
		};

		View.OnFocusChangeListener btnFocus = new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					goToSelectAddress();
				}
			}

		};
		
		llNewAddr = (LinearLayout)findViewById(R.id.ll_new_addr);
		//llOldAddr = (LinearLayout)findViewById(R.id.ll_select_addr);
		//llNewAddr.setVisibility(View.GONE);
		//llOldAddr.setVisibility(View.VISIBLE);
		etNewUser = (EditText)findViewById(R.id.etUserName);
		etNewUser.setOnClickListener(tvOnClickListener);
		etNewUser.setOnFocusChangeListener(btnFocus);
		etNewPhone = (EditText)findViewById(R.id.etPhone);
		etNewUser.setOnClickListener(tvOnClickListener);
		etNewUser.setOnFocusChangeListener(btnFocus);
		etNewAddr = (EditText)findViewById(R.id.etAddress);
		etNewAddr.setOnClickListener(tvOnClickListener);
		etNewAddr.setOnFocusChangeListener(btnFocus);
		etGPSAddr = (EditText)findViewById(R.id.et_gps_addr);
		etGPSAddr.setText(app.useLocation.getAddressDetail());
		
		SharedPreferences usersetting = getApplicationContext()
				.getSharedPreferences(HJAppConfig.CookieName,
						Context.MODE_PRIVATE);
		etNewUser.setText(usersetting.getString("order_username", ""));
		etNewPhone.setText(usersetting.getString("order_phone", ""));
		etNewAddr.setText(usersetting.getString("order_address", ""));
		rgAdd = (RadioGroup)findViewById(R.id.rg_address);
		rgAdd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (group.getCheckedRadioButtonId()) {
				case R.id.rb_new:
					//llNewAddr.setVisibility(View.VISIBLE);
					etGPSAddr.setText(app.useLocation.getAddressDetail());
					etNewAddr.setText("");
					btnAddNewAddress.setVisibility(View.GONE);
					userGPS = true;
					break;
				case R.id.rb_address:
					//llNewAddr.setVisibility(View.GONE);
					
					btnAddNewAddress.setVisibility(View.VISIBLE);
					userGPS = false;
					goToSelectAddress();
					break;
				default:
					break;
				}
			}
		});

		
		// 支付方式(线上（0），线下(1))
		paytypegGroup = (RadioGroup) this
				.findViewById(R.id.pay_typeradioGroup);
		/*etPayPassword = (EditText) findViewById(R.id.pay_password_et);
		rbPayType0 = (RadioButton)findViewById(R.id.pay_type0);
		rbPayType1 = (RadioButton)findViewById(R.id.pay_type0);
		rbPayType2 = (RadioButton)findViewById(R.id.pay_type0);
		rbPayType3 = (RadioButton)findViewById(R.id.pay_type0);*/
		// 绑定一个匿名监听器
		paytypegGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case 100000://支付宝
					if (etPayPassword != null) {
						etPayPassword.setVisibility(View.GONE);
					}
					
					paytype = 1;
					break;
				case 100001://余额支付
					if (etPayPassword != null) {
						etPayPassword.setVisibility(View.VISIBLE);
					}
					
					paytype = 3;
					break;
				case 100002://货到付款
					if (etPayPassword != null) {
						etPayPassword.setVisibility(View.GONE);
					}
					
					paytype = 4;
					break;
				case 100003://微信支付
					if (etPayPassword != null) {
						etPayPassword.setVisibility(View.GONE);
					}
					
					paytype = 5;
					break;
				default:
					break;
				}
				/*if (arg0.getCheckedRadioButtonId() == R.id.pay_type0) {//货到付款
					etPayPassword.setVisibility(View.GONE);
					paytype = 4;
				} else if (arg0.getCheckedRadioButtonId() == R.id.pay_type1) {//支付宝
					etPayPassword.setVisibility(View.GONE);
					paytype = 1;
				}else if (arg0.getCheckedRadioButtonId() == R.id.pay_type3) {//微信支付
					etPayPassword.setVisibility(View.GONE);
					paytype = 5;
				} else if (arg0.getCheckedRadioButtonId() == R.id.pay_type2) {//余额
					etPayPassword.setVisibility(View.VISIBLE);
					paytype = 3;
				} else {
				}*/
			}
		});
		//优惠券
		RadioGroup couponGroup = (RadioGroup) this
				.findViewById(R.id.coupon_radioGroup);
		llCoupon = (LinearLayout)findViewById(R.id.ll_couponlist);
		// 绑定一个匿名监听器
		couponGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub

				if (arg0.getCheckedRadioButtonId() == R.id.coupon_type0) {
					llCoupon.setVisibility(View.GONE);
					userCoupon = 0;
				} else if (arg0.getCheckedRadioButtonId() == R.id.coupon_type1) {
					llCoupon.setVisibility(View.VISIBLE);
					userCoupon = 1;
					if (app.couponsList == null) {
						app.couponsList = new MyCouponsListModel();
						app.couponsKeyList = new MyCouponsListModel();
						couponListFoodAdapter = new CouponListFoodAdapter();
						moreView = inflater.inflate(R.layout.more_friends_from_net, null);
						tvMore = (TextView) moreView
								.findViewById(R.id.tv_more_friends);
						tvMore.setText(getResources().getString(R.string.shop_cart_add_card_title)/*"添加优惠券"*/);
						tvMore.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if (app.couponsKeyList.list.size() >= 4) {
									Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_add_card_notice)/*"一次消费最多只能使用四张优惠券"*/, 15).show();
									return;
								}
								goToMyCoupon();
							}
						});
						
						delCoupon = new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								int index = (Integer)v.getTag();
								MyCouponModel model = app.couponsKeyList.list.get(index);
								model.setIsSelect(0);
								app.couponsKeyList.list.remove(index);
								setCouponAdapter();
							}
						};
					}
					if (app.couponsKeyList.list.size() == 0) {
						goToMyCoupon();
					}
				}  else {
				}
			}
		});

		// 支付方式线上方式：支付宝支付(1),礼品卡支付(2)
		RadioGroup rSendType = (RadioGroup) this
				.findViewById(R.id.eattyperadioGroup);

		llSelfState = (LinearLayout)findViewById(R.id.ll_self_state);
		etSelfState = (EditText)findViewById(R.id.et_self_state);
		etSelfState.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowSelfState();
			}
		});
		etSelfState.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				ShowSelfState();
			}
		});
		
		// 绑定一个匿名监听器
		rSendType
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub

						if (arg0.getCheckedRadioButtonId() == R.id.reattype0) {
							sendtype = 0;
							llSelfState.setVisibility(View.GONE);
						} else if (arg0.getCheckedRadioButtonId() == R.id.reattype1) {
							sendtype = 1;
							llSelfState.setVisibility(View.VISIBLE);
						} else {
						}
					}
				});

		if (OtherManager.DEBUG) {
			Log.v(TAG, "InitUI() Success");
		}

	}
	
	protected void goToSelectAddress() {
		// TODO Auto-generated method stub
		if (!userGPS ) {
			if (app.reciveAddressList.list.size() == 0) {
				AddNewAddress();
			} else {
				SelectAddress();
			}
		}
		
	}

	protected void Back() {
		// TODO Auto-generated method stub
		if(firstView.getVisibility() == View.VISIBLE)
		{
			finish();
		}else{
			nextScrollView.setVisibility(View.GONE);
			firstView.setVisibility(View.VISIBLE);
		}
	}

	public double rad(double value)
	{
	    return value * Math.PI / 180.0;
	}
	
	public double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{//计算两个经纬度之间的额距离
	    double radLat1 = rad(lat1);
	    double radLat2 = rad(lat2);
	    double a = radLat1 - radLat2;
	    double b = rad(lng1) - rad(lng2);
	    double s = 2 * Math.sin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
	    s *= EARTH_RADIUS ;
	    s = Math.round(s * 10000) / 10000;
	    return s / 1000;//返回公里数
	}

	protected void goToMyCoupon() {
		// TODO Auto-generated method stub
		isSelectCoupon = true;
		Intent intent = new Intent(ShopCart.this, MyCouponsListView.class);
		intent.putExtra("money", app.shopCartModel.getTotalprice());
		startActivity(intent);
	}

	protected void SelectAddress() {
		// TODO Auto-generated method stub
		isSelectAddr = true;
		Intent intent = null;
		intent = new Intent(ShopCart.this, UserAddressList.class);
		intent.putExtra("select", true);
		startActivity(intent);
	}

	protected void AddNewAddress() {
		// TODO Auto-generated method stub
		dailog = new UserAddressDedail(ShopCart.this, null, true,
				new UserAddressDedail.UpdateAddress() {

					@Override
					public void updateAddress(ReciveAddressModel model) {
						// TODO Auto-generated method stub
						//app.reciveAddressList.list.add(model);
						//app.reciveAddressList.UpdatGourpShow();
						//app.selectIndex = app.reciveAddressList.list.size() - 1;
						// addressID = model.getBuildID();
						//CheckBuidID();
						GetAddressList();
					}

					@Override
					public void Cancel() {
						// TODO Auto-generated method stub
						
					}

				}, app.userInfo.userId, app);
		dailog.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	private void GetAddressList() {
		if (app.userInfo == null || app.userInfo.userId == null || app.userInfo.userId.length() < 1 || 
				app.userInfo.userId.equals("0")) {
			return;
		}
		actionTag = 4;
		dialogStr = getResources().getString(R.string.shop_cart_inition_notice)/*"获取初始化数据......"*/;
		showDialog(322);

		localHttpManager = new HttpManager(
				ShopCart.this,
				ShopCart.this,
				"Android/GetUserAddressList.aspx?userid=" + app.userInfo.userId,
				null, 1, 4);
		localHttpManager.getRequeest();
	}

	private void CheckBuidID() {
		actionTag = 5;
		dialogStr = getResources().getString(R.string.shop_cart_check)/*"验证数据中......"*/;
		showDialog(322);

		localHttpManager = new HttpManager(ShopCart.this, ShopCart.this,
				"Android/checkbuilding.aspx?tid="
						+ app.mShop.getId()
						+ "&bid="
						+ app.reciveAddressList.list.get(app.selectIndex)
								.getBuildID(), null, 1, 6);
		localHttpManager.getRequeest();

	}

	private void addOrder() {
		// 生成提交订单的json
		String ordermodelstring = "";

		ordermodelstring = app.shopCartModel.beanToStringFix(app.mShop.getId(), app.mShop.shopIndexInShopCart);
		String ordermodelstringfix = ordermodelstring;

		ordermodelstringfix = ordermodelstring.replace("\\\"", "\"");
		ordermodelstringfix = ordermodelstringfix.replace("\\", "");
		ordermodelstringfix = ordermodelstringfix.replace("\"{", "{");
		ordermodelstringfix = ordermodelstringfix.replace("}\"", "}");

		Log.v(TAG, "ordermodelstring:" + ordermodelstringfix);
		// ordermodelstringfix = URLEncoder.encode(ordermodelstringfix,
		// "GB2312");
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		if (userCoupon == 1) {
			String coupon = "";
			MyCouponModel model;
			for (int i = 0; i < app.couponsKeyList.list.size(); i++) {
				model = app.couponsKeyList.list.get(i);
				if (i == 0) {
					coupon += String.format("{\"CID\":\"%d\",\"Point\":\"%s\",\"cardnum\":\"\",\"ckey\":\"%s\"}", model.getDataID(),
							model.getCValue(), model.getCKey());
				}else{
					coupon += String.format(",{\"CID\":\"%d\",\"Point\":\"%s\",\"cardnum\":\"\",\"ckey\":\"%s\"}", model.getDataID(),
							model.getCValue(), model.getCKey());
				}
			}
			localHashMap.put("Cardjson", "[" + coupon + "]");
		}
		actionTag = 1;
		
		localHashMap.put("ordermodel", "[" + ordermodelstringfix + "]");
		//localHashMap.put("bid", app.mSection.SectionID);

		localHttpManager = new HttpManager(ShopCart.this, ShopCart.this,
				"Android/SubmitOrder.aspx?", localHashMap, 1, 1);
		localHttpManager.postRequest();

		Log.v(TAG, "[" + ordermodelstringfix + "]");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Log.v(TAG, "0000000000:" + statusString);
		hander = new UIHandler();
		if (app.reciveAddressList == null) {
			app.reciveAddressList = new ReciveAddressListModel();
		}

		InitUI();
		
		addToShopCartListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer)v.getTag();
				FoodInOrderModel model = (FoodInOrderModel)v.getTag(R.id.tab_icon);
				int count = app.shopCartModel.getFoodCountInAttr(model.getTogoId(),
						model.getId(), 0);
				if (count >= model.getStock()) {
					Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_full)/*"已经超出了库存！"*/, 15)
					  .show(); 
					 return; 
				}
				TextView text = (TextView)v.getTag(R.id.tab_label);
				text.setText(String.valueOf(app.shopCartModel.addFoodAttr(model, pt)));
				/*String m = getResources().getString(R.string.public_moeny_0);
				tvTotalPrice.setText(String.format(getResources().getString(R.string.shop_cart_format),
						m, app.shopCartModel.getTotalprice(), m, app.shopCartModel.getPackagefree(),  
						m, app.shopCartModel.getSendFee(), 						 
						m,  app.shopCartModel.getTotalprice() + app.shopCartModel.getSendFee() + app.shopCartModel.getPackagefree()));
				mSchedule.notifyDataSetChanged();*/
				setCondition();
			}
		};
		
		delFromShopCartListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer)v.getTag();
				TextView text = (TextView)v.getTag(R.id.tab_label);
				FoodInOrderModel model = (FoodInOrderModel)v.getTag(R.id.tab_icon);
				pt = app.shopCartModel.delFoodAttr(model, pt);
				if (pt == 0) {
					mSchedule.notifyDataSetChanged();
				}else{
					text.setText(String.valueOf(pt));
				}
				/*String m = getResources().getString(R.string.public_moeny_0);
				tvTotalPrice.setText(String.format(String.format(getResources().getString(R.string.shop_cart_format),
						m, app.shopCartModel.getTotalprice(), m, app.shopCartModel.getPackagefree(),  
						m, app.shopCartModel.getSendFee(), 						 
						m,  app.shopCartModel.getTotalprice() + app.shopCartModel.getSendFee() + app.shopCartModel.getPackagefree())));
				if (app.shopCartModel.getCount() == 0) {
					foodListView.setVisibility(View.GONE);
					nextBuuton1.setVisibility(View.GONE);
					tvTotalPrice.setVisibility(View.GONE);
				}
				mSchedule.notifyDataSetChanged();*/
				setCondition();
			}
		};
		
		removeFromShopCartListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer)v.getTag();
				//TextView text = (TextView)v.getTag(R.id.tab_label);
				FoodInOrderModel model = (FoodInOrderModel)v.getTag(R.id.tab_icon);
				app.shopCartModel.removeFood(model, pt);
				//text.setText("0");
				/*mSchedule.notifyDataSetChanged();
				String m = getResources().getString(R.string.public_moeny_0);
				tvTotalPrice.setText(String.format(getResources().getString(R.string.shop_cart_format),
						m, app.shopCartModel.getTotalprice(), m, app.shopCartModel.getPackagefree(),  
						m, app.shopCartModel.getSendFee(), 						 
						m,  app.shopCartModel.getTotalprice() + app.shopCartModel.getSendFee() + app.shopCartModel.getPackagefree()));
				if (app.shopCartModel.getCount() == 0) {
					foodListView.setVisibility(View.GONE);
					nextBuuton1.setVisibility(View.GONE);
					tvTotalPrice.setVisibility(View.GONE);
				}*/
				if (totalFoodCount == 0) {
					foodListView.setVisibility(View.GONE);
					nextBuuton1.setVisibility(View.GONE);
					tvTotalPrice.setVisibility(View.GONE);
				}
				mSchedule.notifyDataSetChanged();
			}
		};
		wxpayReq = new PayReq();
		wxpay=new StringBuffer();

		app.msgApi.registerApp(Constants.APP_ID);
		getPayMode();
	}
	
	void GetState(){//获取配送站点
		dialogStr = "获取站点数据......";
		showDialog(322);

		localHttpManager = new HttpManager(
				ShopCart.this,
				ShopCart.this,
				"Android/GetDeliverySiteListByShopId.aspx",
				null, 1, 5);
		localHttpManager.getRequeest();
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
					//totalFoodPrice += (pacektFee + sendFee);
				}
				String m = getResources().getString(R.string.public_moeny_0);
				tvTotalPrice.setText(String.format(getResources().getString(R.string.shop_cart_format)/*"商品：￥%.2f元，配送费：￥%.2f，合计：￥%.2f"*/,
						m, totalFoodPrice, m, pacektFee,  
						m, sendFee, 						 
						m,  totalFoodPrice + sendFee + pacektFee));
				if (totalFoodCount == 0) {
					foodListView.setVisibility(View.GONE);
					nextBuuton1.setVisibility(View.GONE);
					tvTotalPrice.setVisibility(View.GONE);
				}
				mSchedule.notifyDataSetChanged();
				//totalFoodPrice = app.shopCartModel.getTotalprice();
				
				
			}
			
			
		}
	@Override
	protected void onResume() {
		super.onResume();
		if (isPayWX) {
			GoOrderDetail();
			finish();
			return;
		}
		if (isSelectCoupon) {
			isSelectCoupon = false;
			setCouponAdapter();
			return;
		}
		if (dailog != null && dailog.isShowing()) {
			dailog.updateView();
			return;
		}

		if (isSelectAddr) {
			
			addrSelectIndex = app.selectIndex;
			addrModel = app.reciveAddressList.list.get(app.selectIndex);
			//etAddress.setText(model.getAddres() + "," + model.getReciver() + "," + model.getPhone());
			etNewUser.setText(addrModel.getReciver());
			etNewPhone.setText(addrModel.getPhone());
			etGPSAddr.setText(addrModel.getAddres());
			etNewAddr.setText(addrModel.getAddres());
			isSelectAddr = false;
		} else {
			if (app.shopCartModel != null && app.shopCartModel.list.size() > 0) {
				Log.v(TAG, "ShowFoodListView S");
				//nowEdit = null;
				foodListView.setVisibility(View.VISIBLE);
				nextBuuton1.setVisibility(View.VISIBLE);
				tvTotalPrice.setVisibility(View.VISIBLE);
				
				setCondition();
				//btnBacke.setVisibility(View.GONE);

				// 通过加入购物车的计算器进行计算
				/*for (int i = 0; i < app.shopCartModel.list.size(); i++) {
					totalFoodPrice += app.shopCartModel.list.get(i).getCount()
							* app.shopCartModel.list.get(i).getPrice();
				}*/

				
				Log.v(TAG, "ShowFoodListView E");

				// shopcartTextView.setText(app.mStrShopCart);

				// 取出记忆中的信息
				/*SharedPreferences usersetting = getApplicationContext()
						.getSharedPreferences(HJAppConfig.CookieName,
								Context.MODE_PRIVATE);
				etAddress.setText(usersetting.getString("order_address", ""));
				etPhone.setText(usersetting.getString("order_phone", ""));
				etUsername.setText(usersetting.getString("order_username", ""));*/

				final Calendar c = Calendar.getInstance();
				c.add(Calendar.MINUTE, app.mShop.getSendTime() + 3);
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);

				mHour = c.get(Calendar.HOUR_OF_DAY);
				mMinute = c.get(Calendar.MINUTE);

				// forwordButton1.setVisibility(View.INVISIBLE);
				Log.v("ShopCart", "shopcart_btn_forword2");
				// 显示下一步的组件0 �示

				nextScrollView.setVisibility(4);

				firstView.setVisibility(0);

				// 商家营业状态 打烊则时间顺延到第二天
				statusString = app.shopCartModel.getStatus();

				setDateTime();

				setTimeOfDay();

			} else {
				foodListView.setVisibility(View.GONE);
				nextBuuton1.setVisibility(View.GONE);
				tvTotalPrice.setVisibility(View.GONE);
				return;
			}
			GetAddressList();
		}
		

	}

	// 验证礼品卡密码
	private class checkgiftcardpwd extends Thread {
		@Override
		public void run() {

			if (OtherManager.DEBUG) {
				Log.v(TAG, "checkgiftcardpwd 向服务器请求数据...");
			}

			// 访问网络提交订单
			try {

				actionTag = 2;
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("pwd", "" + giftcardpwdString + "");
				localHashMap.put("uid",
						"" + OtherManager.getUserInfo(ShopCart.this).userId
								+ "");

				localHttpManager = new HttpManager(ShopCart.this,
						ShopCart.this, "Android/getcard.aspx?", localHashMap,
						1, 2);
				localHttpManager.postRequest();

				Log.v(TAG, "pwd=" + giftcardpwdString + "");

			} catch (Exception ex) {
				Log.e(TAG, ex.getMessage());
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("uppay", "onActivityResult() +++");

		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase(R_SUCCESS)) {
			removeDialog(322);
			errorMSG = "银联支付成功";
			GoOrderDetail();
			// showResultDialog(" 支付成功！ ");
		} else if (str.equalsIgnoreCase(R_FAIL)) {
			removeDialog(322);
			AlertDialog.Builder confirmDeletionDialog = new AlertDialog.Builder(
					ShopCart.this);

			DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					errorMSG = "银联支付失败";
					GoOrderDetail();
				}
			};

			confirmDeletionDialog.setTitle("支付失败");
			confirmDeletionDialog.setMessage("支付失败！将跳转到订单详细页面！");
			confirmDeletionDialog.setPositiveButton("确定", ok);
			confirmDeletionDialog.setNegativeButton(null, null);
			confirmDeletionDialog.create().show();
			// GoOrderDetail();
			// showResultDialog(" 支付失败！ ");
		} else if (str.equalsIgnoreCase(R_CANCEL)) {
			removeDialog(322);
			AlertDialog.Builder confirmDeletionDialog = new AlertDialog.Builder(
					ShopCart.this);

			DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					errorMSG = "您取消了银联支付";
					GoOrderDetail();
				}
			};

			confirmDeletionDialog.setTitle("取消支付");
			confirmDeletionDialog.setMessage("您取消了支付！将跳转到订单详细页面！");
			confirmDeletionDialog.setPositiveButton("确定", ok);
			confirmDeletionDialog.setNegativeButton(null, null);
			confirmDeletionDialog.create().show();

			// showResultDialog(" 你已取消了本次订单的支付！ ");
		}

		Log.e("uppay", "onActivityResult() ---");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			Back();
		} else {
			boolean bool = true;
			bool = super.onKeyDown(keyCode, paramKeyEvent);
			return bool;
		}

		return true;
	}

	private void GoOrderDetail() {
		/*Intent localIntent = new Intent("com.ihangjing.common.tap2");
		app.isViewOrderList = true;//
		app.sendBroadcast(localIntent);	*/
		
		Intent intent = new Intent(ShopCart.this, AddOrder.class);
		intent.putExtra("orderid", orderidString);
		intent.putExtra("payType", paytype);
		startActivity(intent);
	}

	

	private class UIHandler extends Handler {
		static final int FOOD_DOWNLOAD_SUCCESS = 3; // 定义消息类型
		static final int ORDER_SUBMIT_SUCCESS = 1;
		static final int ORDER_SUBMIT_FAILD = -1;
		static final int CEHCK_CARD_SUCCESS = 14;
		static final int CEHCK_CARD_FAILE = 15;
		static final int ORDER_SUBMIT_FAILE = 16;
		public static final int NET_ERROR = -2;
		public static final int START_PAY = 2;
		public static final int GET_REC_ADDR_SUCCESS = 6;
		public static final int NO_REC_ADDR = -4;
		public static final int CHECK_BID_SUCCESS = 7;
		public static final int CHECK_BID_FAILD = -7;
		public static final int GET_STATE_SUCESS = 4;
		protected static final int RQF_PAY = 9;
		public static final int GETPAYID_SUCCESS = 8;
		public static final int GETPAYID_FAILD = -8;
		public static final int GET_PREPAY_ID_FAILD = 17;
		public static final int GET_PREPAY_ID_SUCESS = 13;
		public static final int GET_PAYMODE_SUCESS = 18;

		@Override
		public void handleMessage(Message msg) {

			if (OtherManager.DEBUG) {
				Log.v(TAG, "message.what:" + msg.what);
			}
			removeDialog(322);
			switch (msg.what) {
			case GET_PAYMODE_SUCESS:
				JSONObject json;
				for (int i = 0; i < suportAry.length(); i++) {
					try {
						json = suportAry.getJSONObject(i);
						RadioButton rbPay;
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						switch (json.getInt("status")) {
						case 1://支付宝
							rbPay = new RadioButton(ShopCart.this);
							rbPay.setId(100000);
							rbPay.setTextColor(Color.BLACK);
							rbPay.setLayoutParams(params);
							rbPay.setText(R.string.pay_mode_1);
							paytypegGroup.addView(rbPay);
							if (paytype == -1) {
								paytype = 1;
								rbPay.setChecked(true);
							}
							break;
						case 3://余额支付
							rbPay = new RadioButton(ShopCart.this);
							rbPay.setId(100001);
							rbPay.setTextColor(Color.BLACK);
							rbPay.setLayoutParams(params);
							rbPay.setText(R.string.pay_mode_2);
							paytypegGroup.addView(rbPay);
							etPayPassword = new EditText(ShopCart.this);
							etPayPassword.setHint(R.string.pay_password);
							etPayPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
							paytypegGroup.addView(etPayPassword);
							if (paytype == -1) {
								paytype = 3;
								rbPay.setChecked(true);
							}else{
								etPayPassword.setVisibility(View.GONE);
							}
							break;
						case 4://货到付款
							rbPay = new RadioButton(ShopCart.this);
							rbPay.setId(100002);
							rbPay.setTextColor(Color.BLACK);
							rbPay.setLayoutParams(params);
							rbPay.setText(R.string.pay_mode_0);
							paytypegGroup.addView(rbPay);
							if (paytype == -1) {
								paytype = 4;
								rbPay.setChecked(true);
							}
							break;
						case 5://微信支付
							rbPay = new RadioButton(ShopCart.this);
							rbPay.setId(100003);
							rbPay.setTextColor(Color.BLACK);
							rbPay.setLayoutParams(params);
							rbPay.setText(R.string.pay_mode_3);
							paytypegGroup.addView(rbPay);
							if (paytype == -1) {
								paytype = 5;
								rbPay.setChecked(true);
							}
							break;
						default:
							break;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				break;
			case CHECK_BID_SUCCESS:
				/*etUsername.setText(app.reciveAddressList.list.get(
						app.selectIndex).getReciver());
				etPhone.setText(app.reciveAddressList.list.get(app.selectIndex)
						.getMobilePhone());
				etAddress.setText(app.reciveAddressList.list.get(
						app.selectIndex).getAddres());
				
				addressID = app.reciveAddressList.list.get(app.selectIndex)
						.getId();*/
				break;
			case CHECK_BID_FAILD:
				/*Toast.makeText(ShopCart.this, "商家不配送您所选的区域，您可以更换相关区域下面的商家！", 10)
						.show();
				
				etAddress.setText("");
				etPhone.setText("");
				etUsername.setText("");*/
				break;
			case GET_STATE_SUCESS:
				ShowSelfState();
				break;
			case GET_REC_ADDR_SUCCESS:
				//默认是定位或地图搜索的地址
				// addressID = app.reciveAddressList.list.get(0).getId();
				
				//app.selectIndex = 0;
				//CheckBuidID();
				//ReciveAddressModel model = app.reciveAddressList.list.get(app.selectIndex);
				//etAddress.setText(model.getAddres() + "," + model.getReciver() + "," + model.getPhone());
				break;
			case NO_REC_ADDR:
				//removeDialog(322);
				break;
			case FOOD_DOWNLOAD_SUCCESS: {
				break;
			}
			case GETPAYID_SUCCESS:
				
				gotoOnLinePay();
				break;
			case GETPAYID_FAILD:
				OtherManager.Toast(ShopCart.this, getString(R.string.pay_load_id_error)/*"获取支付数据失败！"*/);
				break;
			case  GET_PREPAY_ID_FAILD:
				Toast.makeText(ShopCart.this, errorMSG + getString(R.string.pay_wx_pay_failed)/*"微信支付失败，请稍后再试！"*/, 15).show();
				GoOrderDetail();
				app.shopCartModel.clear(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				finish();
				return;
			case GET_PREPAY_ID_SUCESS:
				
				if (resultunifiedorder == null) {
					removeDialog(322);
					Toast.makeText(ShopCart.this, getString(R.string.pay_load_wx_failed)/*"调用微信支付失败，请稍后再试！"*/, 15).show();
					GoOrderDetail();
					app.shopCartModel.clear(app.mShop.getId(), app.mShop.shopIndexInShopCart);
					finish();
					return;
				}
				genPayReq();
				sendPayReq();
				removeDialog(322);
				app.newOrderID = orderidString;
				app.showOrderType = 0;
				isPayWX = true;
				app.shopCartModel.clear(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				Back();
				break;
			case RQF_PAY:
				Result.sResult = (String) msg.obj;
				Toast.makeText(ShopCart.this, Result.getResult(),
						Toast.LENGTH_SHORT).show();
				GoOrderDetail();
				app.shopCartModel.clear(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				finish();
				break;
			case ORDER_SUBMIT_SUCCESS: {

				Log.v(TAG, "case ORDER_SUBMIT_SUCCESS:");

				//removeDialog(322);
				if (paytype == 1 || paytype == 5) {//调用支付宝接口 现在支付统一到订单详情去支付
					//payMoney = app.shopCartModel.getTotalprice() + app.shopCartModel.getSendFee() + app.shopCartModel.getPackagefree();
					//gotoOnLinePay();
					//在线支付隐藏下面的
					/*GoOrderDetail();
					app.shopCartModel.clear();
					finish();*/
					
				}else{
					
				}
				GoOrderDetail();
				app.shopCartModel.clear(app.mShop.getId(), app.mShop.shopIndexInShopCart);
				finish();
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
				OtherManager.Toast(ShopCart.this, getResources().getString(R.string.shop_cart_add_order_faild)/*"提交订单失败，请稍候再试！"*/);
				break;
			}
			case NET_ERROR:
				OtherManager.Toast(ShopCart.this, getResources().getString(R.string.shop_cart_get_data_error)/*"获取数据失败"*/);
				break;
			case START_PAY:
				UIONPay();

				break;
			case 200: {

				showDialog(111);
				break;
			}
			case 400: {
				OtherManager.Toast(ShopCart.this, getResources().getString(R.string.shop_cart_no_addr)/*"暂无地址,请稍后进入地址薄添加"*/);
				break;
			}
			case ShopCart.SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			case ShopCart.SHOW_TIMEPICK:
				showDialog(TIME_DIALOG_ID);
				break;
			case CEHCK_CARD_SUCCESS:
				progressDialog.dismiss();
				// Cancel_giftcard_bt_.setVisibility(0);
				gf_havemoney_TV.setText(getResources().getString(R.string.shop_cart_card_have_money) + Card_have_moeny + getResources().getString(R.string.public_moeny_0));

				break;
			case CEHCK_CARD_FAILE:
				progressDialog.dismiss();
				OtherManager.Toast(ShopCart.this, cardmsgString);
				break;
			case ORDER_SUBMIT_FAILE:
				removeDialog(322);
				OtherManager.Toast(ShopCart.this, getResources().getString(R.string.shop_cart_pay_password_error)/*"支付密码错误，请重新输入"*/);
				break;
			}
		}
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
					PayTask alipay = new PayTask(ShopCart.this);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(alipayInfo);

					Message msg = new Message();
					msg.what = UIHandler.RQF_PAY;
					msg.obj = result;
					hander.sendMessage(msg);
				}
			}.start();
		} catch (Exception ex) {
			
			ex.printStackTrace();
			Toast.makeText(ShopCart.this, getString(R.string.pay_error),
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
		localHttpManager = new HttpManager(ShopCart.this, ShopCart.this,
				"Android/buildpaynum.aspx?", localHashMap, 1, 7);
		localHttpManager.postRequest();
	}
	
	public void getPayMode()
	{
		dialogStr = getString(R.string.pay_load_id)/*"获取支付数据中......"*/;
		showDialog(322);
		localHttpManager = new HttpManager(ShopCart.this, ShopCart.this,
				"Android/GetPayStyleList.aspx", null, 1, 8);
		localHttpManager.getRequeest();
	}
	
	private void gotoOnLinePay() {
		// TODO Auto-generated method stub
		if(payID == null || payID.length() == 0 ){
			getPayID(payMoney);
			return;
		}
		if (paytype == 1) {
			gotoAliPay();
			
		}else if(paytype == 5){
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
				//dialog = ProgressDialog.show(ShopCart.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
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
				
				
				
				hander.sendMessage(message);
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
	
	

	private void UIONPay() {
		int ret = UPPayAssistEx.startPay(ShopCart.this, null, null,
				UNIONPayXML, EasyEatApplication.unpayType);// 01测试系统中，00正式系统中
		if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
			AlertDialog.Builder confirmDeletionDialog = new AlertDialog.Builder(
					ShopCart.this);

			DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					UIONPay();
				}
			};
			DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					removeDialog(322);
					errorMSG = "您取消了银联支付";
					GoOrderDetail();
				}
			};
			confirmDeletionDialog.setTitle("银联支付插件安装");
			confirmDeletionDialog.setMessage("已经安装完成银联支付插件？");
			confirmDeletionDialog.setPositiveButton("安装完成", ok);
			confirmDeletionDialog.setNegativeButton("取消", cancel);
			confirmDeletionDialog.create().show();
			UPPayAssistEx.installUPPayPlugin(ShopCart.this);
		}
	}
	
	public void ShowSelfState() {
		// TODO Auto-generated method stub
		if (selfStateList == null || selfStateList.list.size() == 0) {
			//获取数据
			GetState();
		}else{
			//显示
			AlertDialog.Builder showsort = new AlertDialog.Builder(ShopCart.this);
			showsort.setTitle(getResources().getString(R.string.public_select)/*"请选择"*/).setSingleChoiceItems(selfStateList.stateName, -1,
					new DialogInterface.OnClickListener() {
						

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 获取选择的分类的编号
							etSelfState.setText(selfStateList.stateName[which]);
							app.shopCartModel.setSelfStateID(String.valueOf(selfStateList.list.get(which).getId()));
							dialog.cancel();

						}
					}).create().show();
		}
	}

	public Bitmap getBitmap(FoodInOrderModel model) {
		// TODO Auto-generated method stub
		Bitmap originalImage;
		
		if (model.getImageLocalPath() != null && model.getImageLocalPath().length() > 18) {// 本地地址不为空
			originalImage = app.mLoadImage.getBitmap(model.getImageLocalPath());
			if (originalImage == null) {
				originalImage = BitmapFactory.decodeResource(
						ShopCart.this.getResources(), R.drawable.icon);
				
			}
		}else{// 本地和网络地址都为空，使用id
			originalImage = BitmapFactory.decodeResource(
					ShopCart.this.getResources(), R.drawable.icon);
			
		}
		
		return originalImage;
	}
	
	class Hoder {
		TextView tvKey;
		
		Button btnDel;
		
	}

	class CouponListFoodAdapter extends BaseAdapter {
		AddOrder.Hoder hoder;

		CouponListFoodAdapter() {
			
		}

		@Override
		public int getCount() {
			return app.couponsKeyList.list.size() + 1;
		}

		@Override
		public Object getItem(int paramInt) {
			return app.couponsKeyList.list.get(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			if(paramInt == app.couponsKeyList.list.size()){
				if (app.couponsKeyList.list.size() >= 4) {
					tvMore.setText(getResources().getString(R.string.shop_cart_add_card_more)/*"一次消费最多只能使用四张优惠券"*/);
				}else{
					tvMore.setText(getResources().getString(R.string.shop_cart_add_card)/*"添加优惠券"*/);
				}
				return moreView;
			}
			Hoder listHolder = new Hoder();
			if (paramView == null || paramView == moreView) {
				paramView = inflater.inflate(
						R.layout.coupon_user_item, null);

				Log.v(TAG, "convertView == null");
				listHolder.tvKey = (TextView) paramView
						.findViewById(R.id.tv_key);
				listHolder.btnDel = (Button)paramView.findViewById(R.id.btn_del);
				listHolder.btnDel.setOnClickListener(delCoupon);
				paramView.setTag(paramView);
			} else {
				listHolder = (Hoder) paramView.getTag();
			}

			MyCouponModel model = app.couponsKeyList.list.get(paramInt);

			if (model != null) {

				
				listHolder.tvKey.setText(model.getCKey());
				listHolder.btnDel.setTag(paramInt);
				
				
			} else {
				Log.v(TAG, "foodInOrderModel is null");
			}

			Log.v(TAG, "return convertView");
			return paramView;
		}
	}

	private void setCouponAdapter() {
		Log.v(TAG, "setAdapter");
		llCoupon.removeAllViews();
		int i = this.couponListFoodAdapter.getCount();
		int j = 0;

		Log.v(TAG, "listFoodAdapter.getcount:" + i);

		while (true) {
			if (j >= i) {
				Log.v(TAG, "j >= i setAdapter end");
				return;
			}
			View localView = this.couponListFoodAdapter.getView(j, null, null);
			llCoupon.addView(localView);
			j += 1;
		}
	}

	final class FoodListHolder {
		TextView tv_foodName;
		TextView tv_foodAttr;
		RelativeLayout iv_foodImg;
		TextView tv_foodPrice;
		TextView tv_foodNum;
		Button btnAdd;
		Button btnDel;
		Button btnRemove;

		FoodListHolder() {
		}
	}

	// 菜单
	class FoodListViewAdapter extends BaseAdapter {
		FoodListViewAdapter() {
		}

		@Override
		public int getCount() {
			if (app.shopCartModel == null) {
				return 0;
			}
			return app.shopCartModel.getShopCartListSizeWithShopID(app.mShop.getId(), app.mShop.shopIndexInShopCart);//app.shopCartModel.getFoodAttrCount();
		}

		@Override
		public Object getItem(int paramInt) {
			HJInteger index = new HJInteger();
			index.value = paramInt;
			return app.shopCartModel.getFoodInOrderModelWithShopId(app.mShop.getId(), app.mShop.shopIndexInShopCart, index);
			//return app.shopCartModel.getFoodInOrderModel(index);
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
		public View getView(int paramInt, View convertView, ViewGroup paramViewGroup) {
			

			FoodListHolder foodlistHolder;
			if (convertView == null) {
				convertView = ShopCart.this.inflater.inflate(
						R.layout.shopcart_item, null);

				Log.v(TAG, "convertView == null");
				foodlistHolder = new ShopCart.FoodListHolder();
				foodlistHolder.iv_foodImg = (RelativeLayout) convertView
						.findViewById(R.id.rl_img);
				foodlistHolder.tv_foodAttr = (TextView) convertView
						.findViewById(R.id.tv_foodinfo);
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
				
				foodlistHolder.btnAdd.setOnClickListener(addToShopCartListener);
				foodlistHolder.btnRemove.setOnClickListener(removeFromShopCartListener);
				foodlistHolder.btnDel.setOnClickListener(delFromShopCartListener);
				convertView.setTag(foodlistHolder);
			} else {
				foodlistHolder = (ShopCart.FoodListHolder) convertView
						.getTag();
			}
			HJInteger index = new HJInteger();
			index.value = paramInt;

			//FoodInOrderModel model = app.shopCartModel.getFoodInOrderModel(index);
			FoodInOrderModel model = app.shopCartModel.getFoodInOrderModelWithShopId(app.mShop.getId(), app.mShop.shopIndexInShopCart, index);
			if (model != null) {
				if(model.listSize.get(index.value).name != null && model.listSize.get(index.value).name.length() > 0){
					foodlistHolder.tv_foodName.setText(model.getName() + "_" + model.listSize.get(index.value).name);
				}else{
					foodlistHolder.tv_foodName.setText(model.getName());
				}
				foodlistHolder.tv_foodAttr.setText(getResources().getString(R.string.shop_cart_shop_name)/*"商家："*/ + model.getTogoName());
				foodlistHolder.tv_foodPrice.setText(getResources().getString(R.string.public_price)/*"单价："*/
						+ String.valueOf(model.listSize.get(index.value).price));
				Drawable drawable = new BitmapDrawable(getBitmap(model));
				foodlistHolder.iv_foodImg.setBackgroundDrawable(drawable);
				foodlistHolder.tv_foodNum.setTag(index.value);
				foodlistHolder.tv_foodNum.setTag(R.id.tab_icon, model);
				foodlistHolder.tv_foodNum.setText(String.valueOf(model.listSize.get(index.value).count));
				int stock = model.getStock() - app.shopCartModel
						.getOneFoodCount(app.mShop, model.getId());
				if(/*model.listSize.get(index.value).count >= model.getStock()*/stock < 1){
					foodlistHolder.btnAdd.setVisibility(View.GONE);
				}else{
					foodlistHolder.btnAdd.setVisibility(View.VISIBLE);
				}
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

	final class ListHolder {
		TextView tv_foodName;
		TextView tv_foodCount;
		TextView tv_foodDown;
		TextView tv_foodPrice;
		TextView tv_foodUp;

		ListHolder() {
		}
	}

	

	// 修改页面显示数据
	/*private void setChange() {
		totalFoodPrice = 0.0F;
		// 通过加入购物车的计算器进行计算
		for (int i = 0; i < app.shopCartModel.list.size(); i++) {
			totalFoodPrice += app.shopCartModel.list.get(i).getCount()
					* app.shopCartModel.list.get(i).getPrice();
		}

		String totalpriceString = String.valueOf(totalFoodPrice
				+ SendFee);

		if ((totalpriceString.equals("-.0")) || (totalpriceString.equals(".0"))
				|| (totalpriceString.equals("0.0"))) {
			totalpriceString = "0";
		}

		String str2 = "￥" + totalpriceString;// + "(含配送费)";

		
	}*/

	// 检查手机号码是否规范
	private boolean checkMobilePhone(String paramString) {
		String str = paramString.replace("+86", "");
		return Pattern.compile("^(13|15|18|17)[0-9]{9}$").matcher(str).matches();
	}

	HttpManager localHttpManager = null;
	private String UNIONPayXML;
	private String errorMSG;
	private String payID;
	private JSONArray suportAry;

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		Message message = new Message();
		removeDialog(322);
		if (paramInt == 260) {

			String jsonString = (String) paramObject;

			if (OtherManager.DEBUG) {
				Log.e(TAG, "jsonString:" + jsonString);
			}
			try {
				JSONObject json;
				switch (tag) {
				case 1:
					

					json = new JSONObject(jsonString);

					orderidString = json.getString("orderid");
					String orderstateString = json.getString("orderstate");
					app.shopCartModel.setOrderid(orderidString);

					// 提交失败
					if (orderstateString.equals("-1")) {
						// message = new Message();
						message.what = UIHandler.ORDER_SUBMIT_FAILD;
						errorMSG = json.getString("msg");
						// hander.sendMessage(message); // 发送消息给Handler
					} else {
						if (app.couponsKeyList != null) {
							app.couponsKeyList.list.clear();
							app.couponsList.list.clear();
						}
						
						message.what = UIHandler.ORDER_SUBMIT_SUCCESS;
						errorMSG = "";

					}
					break;
				case 2:
					json = new JSONObject(jsonString);
					int rcode = Integer.parseInt(json.getString("code"));
					if (rcode != 200) {
						cardmsgString = json.getString("msg");
						// message = new Message();
						message.what = UIHandler.CEHCK_CARD_FAILE;
						// hander.sendMessage(message); // 发送消息给Handler
					} else {
						Card_have_moeny = Double.parseDouble(json
								.getString("cmoney"));
						CIDStringx = Integer.parseInt(json.getString("CID"));
						// message = new Message();
						message.what = UIHandler.CEHCK_CARD_SUCCESS;
						// hander.sendMessage(message); // 发送消息给Handler
					}
					break;
				case 3:
					UNIONPayXML = jsonString;
					message = new Message();
					message.what = UIHandler.START_PAY;
					break;
				case 4:
					app.reciveAddressList.list.clear();
					if (app.reciveAddressList.StringToBean(jsonString) > 0) {
						message.what = UIHandler.GET_REC_ADDR_SUCCESS;
					} else {
						message.what = UIHandler.NO_REC_ADDR;
					}
					break;
				case 5://提货点
					selfStateList = new SelfStateListModel();
					selfStateList.stringToBean(jsonString);
					message.what = UIHandler.GET_STATE_SUCESS;
					break;
				case 6:
					json = new JSONObject(jsonString);
					int state = json.getInt("state");
					if (state == 1) {
						message.what = UIHandler.CHECK_BID_SUCCESS;
					} else {
						message.what = UIHandler.CHECK_BID_FAILD;
					}
					break;
				case 7:
					json = new JSONObject(jsonString);
					int state1 = json.getInt("state");
					if (state1 == 1) {
						payID = json.getString("batch");
						message.what = UIHandler.GETPAYID_SUCCESS;
					} else {
						message.what = UIHandler.GETPAYID_FAILD;
					}
					break;
				case 8:
					json = new JSONObject(jsonString);
					suportAry = json.getJSONArray("datalist");
					message.what = UIHandler.GET_PAYMODE_SUCESS;
					break;
				default:
					message.what = UIHandler.NET_ERROR;
					break;
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				message.what = UIHandler.NET_ERROR;
			}
		} else {
			message.what = UIHandler.NET_ERROR;
		}
		hander.sendMessage(message); // 发送消息给Handler
	}

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			updateDateDisplay(true);
		}
	};

	/**
	 * 时间控件事件
	 */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateTimeDisplay(true);
		}
	};

	/**
	 * 设置日期
	 */
	private void setDateTime() {
		Calendar c = Calendar.getInstance();

		if (statusString.equals("0")) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}

		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDateDisplay(false);
	}

	/**
	 * 设置时间
	 */
	private void GetNewTime() {
		Calendar c = Calendar.getInstance();

		c.add(Calendar.MINUTE, 30);

		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

	private void setTimeOfDay() {
		GetNewTime();
		updateTimeDisplay(false);
	}

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay(Boolean check) {
		
		try {
			String value = (new StringBuilder().append(mYear).append("-")
					.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
					.append("-").append((mDay < 10) ? "0" + mDay : mDay)).toString();
			if (check) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = sdf.parse(value + " " + etSentTime1.getText().toString() + ":00");
				Date d2 = new Date(System.currentTimeMillis() + 30 * 60 * 1000);//获取当前时间
				if (d1.getTime() < d2.getTime()) {//给出提示不符合要求
					Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_s_time_notice)/*"您选择的时间必须在半小时后！"*/, 15).show();
					return;
				}
			}
			etSentDate1.setText(value);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 更新时间显示
	 */
	private void updateTimeDisplay(Boolean check) {
		try {
			String value = (new StringBuilder().append(mHour).append(":")
					.append((mMinute < 10) ? "0" + mMinute : mMinute)).toString();
			if (check) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = sdf.parse(etSentDate1.getText().toString() + " " + value + ":00");
				Date d2 = new Date(System.currentTimeMillis() + 30 * 60 * 1000);//获取当前时间
				if (d1.getTime() < d2.getTime()) {//给出提示不符合要求
					Toast.makeText(ShopCart.this, getResources().getString(R.string.shop_cart_s_time_notice)/*"您选择的时间必须在半小时后！"*/, 15).show();
					return;
				}
			}
			etSentTime1.setText(value);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		case TIME_DIALOG_ID:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		Log.v(TAG, "onCreateDialog:" + paramInt);
		switch (paramInt) {
		case 322:
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);
			return dialog;
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			TimePickerDialog time = new TimePickerDialog(ShopCart.this, mTimeSetListener, mHour, mMinute, true);
			time.show();
		}
		return null;
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
}
