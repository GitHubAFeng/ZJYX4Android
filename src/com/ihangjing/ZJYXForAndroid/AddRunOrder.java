package com.ihangjing.ZJYXForAndroid;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.ihangjing.Model.RunOrderInfo;
import com.ihangjing.Model.MyLocationModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class AddRunOrder extends HjActivity implements HttpRequestListener {
	private TextView titleTextView;
	private Button backButton;
	private Button saveButton;

	private static final String TAG = "Login";
	ProgressDialog progressDialog = null;
	private EditText telEditText;

	private UIHandler hander;
	private int state;// 接口返回的数值 1 成功 其他 失败
	
	//private TextView tvCode;
	HttpManager localHttpManager = null;
	String[] code = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
			"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "o",
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B",
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	
	public int nWorkMode = 0;
	private Button btnSendCode;
	public Timer timerOrder;
	public int timerTim;
	private EditText etAddr;
	private EditText etName;
	private String errMsg;
	private EditText etGPSAddr;
	private Button btnDirec;
	protected String[] items = {"东", "南", "西", "北"};
	private int nDirec = 0;
	private EditText etCount;
	private EditText etMoney;
	private TextView etDate;
	private TextView etTime;
	private EditText etRemark;
	private int isGotoMap = 0;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	
	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			etDate.setText(String.format("%d-%d-%d", mYear, mMonth, mDay));
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
			etTime.setText(String.format("%d:%d", mHour, mMinute));
		}
	};
	protected RunOrderInfo addOrderModel;
	
	private EditText etSenderGPS;
	private EditText etSenderAddr;
	private EditText etSendMoney;
	private EditText etSendDistance;
	private Button btnGetSendMoeny;
	private String sendMoeny;
	private String sendDistance;
	private EditText etComName;
	protected int timer;
	private LinearLayout llTime;
	private AutoCompleteTextView recAutoCompleteTextView;
	private PoiSearch mPoiSearch;
	private String city;
	protected PoiResult searchResult;
	protected int checkIndex;
	protected boolean results;
	protected boolean isSetAddr;
	private EditText etSender;
	private EditText etSenderPhone;
	private AutoCompleteTextView sendAutoCompleteTextView;
	private EditText nowEt;
	private TextView tvCity;
	private boolean isSelectCity;
	private EditText etPayPassword;
	protected int paytype = 2;


	// http://xxx/APP/Android/Regedit.aspx?email=zjfxqt@163.com&username=zjfxqt&password=123456
	// 返回 state：1 注册成功 -2 存在用户名 -3 邮箱已经存在 -1注册失败
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		setContentView(R.layout.add_run_order);

		hander = new UIHandler();

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText(getResources().getString(R.string.shop_addorder_title)/*"门店下单"*/);
		
		etAddr = (EditText)findViewById(R.id.et_addr);
		etAddr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (etGPSAddr.getText().toString().length() < 1) {
					GoToMap(2);
				}
			}
		});
		// 支付方式(线上（0），线下(1))
		RadioGroup paytypegGroup = (RadioGroup) this
				.findViewById(R.id.pay_typeradioGroup);
		etPayPassword = (EditText) findViewById(R.id.pay_password_et);

		// 绑定一个匿名监听器
		paytypegGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub

				if (arg0.getCheckedRadioButtonId() == R.id.pay_type0) {//我现付
					etPayPassword.setVisibility(View.GONE);
					paytype = 2;
				} else if (arg0.getCheckedRadioButtonId() == R.id.pay_type1) {//对方现付
					etPayPassword.setVisibility(View.GONE);
					paytype = 4;
				}else if (arg0.getCheckedRadioButtonId() == R.id.pay_type2) {//余额
					etPayPassword.setVisibility(View.VISIBLE);
					paytype = 3;
				} else {
				}
			}
		});		
		etComName = (EditText)findViewById(R.id.et_com_name);
		tvCity = (TextView)findViewById(R.id.tv_city);
		mPoiSearch = PoiSearch.newInstance();
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
			@Override
			public void onGetPoiResult(PoiResult result) {
				// 获取POI检索结果
				

				if (result.error == SearchResult.ERRORNO.NO_ERROR) {
					// mBaiduMap.setMyLocationEnabled(false);
					searchResult = result;
					Message msg = new Message();
					msg.what = UIHandler.SEARCH_SUCESS;
					hander.sendMessage(msg);
					return;
				}else{
					searchResult = null;
					if (nowEt == etGPSAddr) {
						if(app.reciverLocation == null){
							app.reciverLocation = new MyLocationModel();
						}
						app.reciverLocation.setAddressDetail("");
						app.reciverLocation.setLat(0.0);
						app.reciverLocation.setLon(0.0);
					}else{
						if(app.userLocation == null){
							app.userLocation = new MyLocationModel();
						}
						app.userLocation.setAddressDetail("");
						app.userLocation.setLat(0.0);
						app.userLocation.setLon(0.0);
					}
					
				}
			}

			@Override
			public void onGetPoiDetailResult(PoiDetailResult result) {
				// 获取Place详情页检索结果
			}

			@Override
			public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
		sendAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.act_list_1);
		sendAutoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (searchResult.getAllPoi().size() > 0) {
					checkIndex = position;
					nowEt = etSenderGPS;
					setSearchAddr();
				}
				
				
			}
		});
		sendAutoCompleteTextView.setAdapter(new AutoCompleteAdapter());
		
		recAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.act_list);
		recAutoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (searchResult.getAllPoi().size() > 0) {
					checkIndex = position;
					nowEt = etGPSAddr;
					setSearchAddr();
				}
				
				
			}
		});
		recAutoCompleteTextView.setAdapter(new AutoCompleteAdapter());
		city = app.useLocation.getCityName();
		
		etGPSAddr = (EditText)findViewById(R.id.et_gps);
		etGPSAddr.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				nowEt = etGPSAddr;
				if (!hasFocus && checkIndex == 0 && searchResult != null) {
					isSetAddr = true;
					
					setSearchAddr();
				}
			}
		});
		etGPSAddr.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				nowEt = etGPSAddr;
				if (!isSetAddr && etGPSAddr.getText().toString().length() > 0) {
					checkIndex = 0;
					
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.city(city)
							.keyword(etGPSAddr.getText().toString()).pageNum(0));
				}
				isSetAddr = false;
				
			}
		});
		
		etName = (EditText)findViewById(R.id.et_name);
		etSendDistance = (EditText)findViewById(R.id.et_send_distance);
		btnGetSendMoeny = (Button)findViewById(R.id.btn_get_sendmoeny);
		btnGetSendMoeny.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getSendMoeny();
			}
		});
		etSendMoney = (EditText)findViewById(R.id.et_send_money);
		TextView tvSendFeeNotice = (TextView)findViewById(R.id.tv_sendfee_notice);
		tvSendFeeNotice.setText(Html.fromHtml("跑腿费用：<font color='red'><b>3公里内12元，每超出一公里加3元</b></font>"));
		telEditText = (EditText) findViewById(R.id.et_phone);
		btnDirec = (Button)findViewById(R.id.btn_direc);
		btnDirec.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(AddRunOrder.this);  

				builder.setTitle("Pick a color");  

				builder.setItems(items , new DialogInterface.OnClickListener() {  

				    

					public void onClick(DialogInterface dialog, int item) {  

				        btnDirec.setText(items[item]);  
				        nDirec = item;
				    }  

				});  

				AlertDialog alert = builder.create();  
				alert.show();
			}
		});
		//cityid = OtherManager.getUserLocal(Regedit.this).cityid;
		
		
		Button btnGoMap1 = (Button)findViewById(R.id.btn_go_map_1);
		btnGoMap1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GoToMap(1);//
			}
		});
		
		Button btnGoMap = (Button)findViewById(R.id.btn_go_map);
		btnGoMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GoToMap(2);//
			}
		});
		
		
		etSenderGPS = (EditText)findViewById(R.id.et_shop_gps);
		etSenderGPS.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				nowEt = etSenderGPS;
				if (!hasFocus && checkIndex == 0 && searchResult != null) {
					isSetAddr = true;
					
					setSearchAddr();
				}
			}
		});
		etSenderGPS.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				nowEt = etSenderGPS;
				if (!isSetAddr && etSenderGPS.getText().toString().length() > 0) {
					checkIndex = 0;
					
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.city(city)
							.keyword(etSenderGPS.getText().toString()).pageNum(0));
				}
				isSetAddr = false;
				
			}
		});
		
		
		
		etSenderAddr = (EditText)findViewById(R.id.et_shop_addr);
		etSenderAddr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (etSenderGPS.getText().toString().length() < 1) {
					GoToMap(1);
				}
				
			}
		});
	
		etSender = (EditText)findViewById(R.id.et_sender);
		etSenderPhone = (EditText)findViewById(R.id.et_sender_phone);
		
		llTime = (LinearLayout)findViewById(R.id.ll_time);
		RadioGroup rgTime = (RadioGroup)findViewById(R.id.rg_time);
		rgTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_time1:
					timer = 0;
					llTime.setVisibility(View.GONE);
					break;
				case R.id.rb_time2:
					timer = 1;
					llTime.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}
		});
		etCount = (EditText)findViewById(R.id.et_count);
		etMoney = (EditText)findViewById(R.id.et_money);
		etDate = (TextView)findViewById(R.id.et_date);
		etDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog dialog = new DatePickerDialog(AddRunOrder.this, mDateSetListener, mYear, mMonth - 1,
						mDay);
				dialog.show();
			}
		});
		etTime = (TextView)findViewById(R.id.et_time);
		etTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimePickerDialog time = new TimePickerDialog(AddRunOrder.this, mTimeSetListener, mHour, mMinute, true);
				time.show();
			}
		});
		setDateTime(1800000);//半小时后
		etDate.setText(String.format("%d-%d-%d", mYear, mMonth, mDay));
		
		etTime.setText(String.format("%d:%d", mHour, mMinute));
		
		etRemark = (EditText)findViewById(R.id.et_remark);

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});
		

		saveButton = (Button) findViewById(R.id.register_btn);

		saveButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				
				if(addOrderModel == null){
					addOrderModel = new RunOrderInfo();
				}
				if (etSender.getText().toString().length() == 0) {
					Toast.makeText(AddRunOrder.this, "请输入发货人姓名！", 15).show();
					etSender.setError("请输入发货人姓名");
					return;
				}
				addOrderModel.setSenderName(etSender.getText().toString()
							.replace(" ", "").trim());
				if (etSenderPhone.getText().toString().length() == 0) {
					Toast.makeText(AddRunOrder.this, "请输入发货人手机！", 15).show();
					etSenderPhone.setError("请输入发货人手机");
					return;
				}
				addOrderModel.setSenderPhone(telEditText.getText().toString().replace(" ", "")
						.trim());
				if (etComName.getText().toString().length() < 1) {
					Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addrorder_no_gn)/*"请输入配送物品名称"*/, 15).show();
					etComName.setError(getResources().getString(R.string.shop_addrorder_no_gn)/*"请输入配送物品名称"*/);
					return;
				}
				addOrderModel.setComName(etComName.getText().toString());
				String value;
				value = etSenderGPS.getText().toString();
				if (value.length() < 1) {
					Toast.makeText(AddRunOrder.this, "填写的取货地址没有定位成功！请重新输入", 15).show();
					GoToMap(1);
					return;
				}
				if (etSenderAddr.getText().toString().length() < 2) {
					Toast.makeText(AddRunOrder.this, "请输入发货地址门牌号，且不少于2个字", 15).show();
					etSenderAddr.setError("请输入发货地址门牌号，且不少于2个字");
					return;
				}
				addOrderModel.setSenderAddr(etSenderGPS.getText().toString() + "-" + etSenderAddr.getText().toString());	
				
				if (etName.getText().toString().length() == 0) {
					Toast.makeText(AddRunOrder.this, "请输入收货人姓名！", 15).show();
					etName.setError("");
					return;
				}
				addOrderModel.setUserName(etName.getText().toString()
							.replace(" ", "").trim());
				if (etName.getText().toString().length() == 0) {
					Toast.makeText(AddRunOrder.this, "请输入收货人姓名！", 15).show();
					etName.setError("");
					return;
				}
				addOrderModel.setUserPhone(telEditText.getText().toString().replace(" ", "")
						.trim());
					//username = tel;
				
				
				/*if (searchResult== null) {
					Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addorder_raddr_n_g), 15).show();//"填写的地址没有定位成功！请重新输入"
					return;
				}*/
				value = etGPSAddr.getText().toString();
				if (value.length() < 1) {
					Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addorder_raddr_n_g)/*"请先给送货地址定位"*/, 15).show();
					GoToMap(2);
					return;
				}
				
				if (etAddr.getText().toString().length() < 2) {
					Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addorder_raddr_n_i)/*"请输入送货的详细地址"*/, 15).show();
					etAddr.setError(getResources().getString(R.string.shop_addorder_raddr_n_i)/*"请输入用户的详细地址"*/);
					return;
				}
				addOrderModel.setUserAddress(/*searchResult.getAllPoi().get(checkIndex).address + */ etGPSAddr.getText().toString() + "-" + etAddr.getText().toString());
				/*value = etCount.getText().toString();
				if (value.length() < 1) {
					Toast.makeText(ShopAddOrder.this, "请输入份数", 15).show();
					etCount.setError("请输入份数");
					return;
				}*/
				value = "0.0";//etSendMoney.getText().toString();
				if (value.length() < 1) {
					Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addorder_sf_n)/*"请输入配送费"*/, 15).show();
					etSendMoney.setError(getResources().getString(R.string.shop_addorder_sf_n)/*"请输入配送费"*/);
					return;
				}else{
					float sendM = Float.parseFloat(value);
					/*if (app.userInfo.loginType == 5) {//用户班配送费要大于10元
						if (sendM < 10.0f) {
							Toast.makeText(ShopAddOrder.this, "配送费不能低于10元", 15).show();
							etSendMoney.setError("配送费不能低于10元");
							return;
						}
					}else{
						if (sendM < 6.0f) {
							Toast.makeText(ShopAddOrder.this, "配送费不能低于6元", 15).show();
							etSendMoney.setError("配送费不能低于10元");
							return;
						}
					}*/
					addOrderModel.setMoney(sendM);
				}
				/*addOrderModel.setCount(Integer.parseInt(value));
				if (addOrderModel.getCount() < 1) {
					Toast.makeText(ShopAddOrder.this, "份数不能为0", 15).show();
					etCount.setError("份数不能为0");
					return;
				}
				value = etMoney.getText().toString();
				
				if (value.length() < 1) {
					Toast.makeText(ShopAddOrder.this, "请输入应收款", 15).show();
					etMoney.setError("请输入应收款");
					return;
				}
				addOrderModel.setMoney(Float.parseFloat(value));*/
				if (paytype == 3) {
					if (etPayPassword.getText().toString().length() == 0) {
						Toast.makeText(AddRunOrder.this, "请输入支付密码", 15).show();
						etPayPassword.setError("请输入支付密码");
						return;
					}
					addOrderModel.setPayPassword(etPayPassword.getText().toString());
				}else{
					addOrderModel.setPayPassword("");
				}
				addOrderModel.setPayType(String.valueOf(paytype));
				String timeString;
				if (timer == 0) {
					//获取现在的时间
					setDateTime(0);//半小时后
					timeString = String.format("%d-%d-%d %d:%d:00", mYear, mMonth, mDay, mHour, mMinute);
				}else{
					timeString = etDate.getText().toString() + " " + etTime.getText().toString() + ":00";
					if (timeString.length() < 15) {
						Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addorder_time_n)/*"请选择配送时间"*/, 15).show();
						etDate.setError(getResources().getString(R.string.shop_addorder_time_n)/*"请选择配送时间"*/);
						return;
					}
				}
				
				addOrderModel.setUserID(app.userInfo.userId);
				addOrderModel.setSendTime(timeString);
				addOrderModel.setRemark(etRemark.getText().toString());
				addOrderModel.setuLat(String.valueOf(app.reciverLocation.getLat()));
				addOrderModel.setDirec(nDirec);
				addOrderModel.setuLng(String.valueOf(app.reciverLocation.getLon()));
				addOrderModel.setSenderLat(String.valueOf(app.userLocation.getLat()));
				addOrderModel.setSenderLng(String.valueOf(app.userLocation.getLon()));

				

				addOrder();
				
			}
		
		});
		//Rand();
	}
	
	protected void setSearchAddr() {
		// TODO Auto-generated method stub
		isSetAddr = true;
		if (nowEt != null) {
			nowEt.setText(searchResult.getAllPoi().get(checkIndex).address + searchResult.getAllPoi().get(checkIndex).name);
			if (nowEt == etGPSAddr) {
				if (app.reciverLocation == null) {
					app.reciverLocation = new MyLocationModel();
				}
				app.reciverLocation.setAddressDetail(searchResult.getAllPoi().get(checkIndex).address + searchResult.getAllPoi().get(checkIndex).name);
				app.reciverLocation.setLat(searchResult.getAllPoi().get(checkIndex).location.latitude);
				app.reciverLocation.setLon(searchResult.getAllPoi().get(checkIndex).location.longitude);
			}else{
				if (app.userLocation == null) {
					app.userLocation = new MyLocationModel();
				}
				app.userLocation.setAddressDetail(searchResult.getAllPoi().get(checkIndex).address + searchResult.getAllPoi().get(checkIndex).name);
				app.userLocation.setLat(searchResult.getAllPoi().get(checkIndex).location.latitude);
				app.userLocation.setLon(searchResult.getAllPoi().get(checkIndex).location.longitude);
			}
			
			getSendMoeny();
		}
		
	}

	public void setDateTime(int sec){
		Date  curDate   =   new   Date(System.currentTimeMillis() + sec);
		//Calendar c = Calendar.getInstance();
		
		mYear = curDate.getYear() + 1900;
		mMonth = curDate.getMonth() + 1;
		mDay = curDate.getDate();
		mHour = curDate.getHours();
		mMinute = curDate.getMinutes();
	}

	protected void GoToMap(int type) {
		// TODO Auto-generated method stub
		isGotoMap = type;
		Intent intent = new Intent(AddRunOrder.this, SearchOnMap.class);
		if (type == 1) {
			if (app.userLocation != null && app.useLocation.getLat() > 0.0 && app.useLocation.getLon() > 0.0) {
				intent.putExtra("lat", app.useLocation.getLat());
				intent.putExtra("lng", app.useLocation.getLon());
				intent.putExtra("lng", app.useLocation.getAddressDetail());
			}
			
		}else{
			if (app.reciverLocation != null && app.reciverLocation.getLat() > 0.0 && app.reciverLocation.getLon() > 0.0) {
				intent.putExtra("lat", app.reciverLocation.getLat());
				intent.putExtra("lng", app.reciverLocation.getLon());
				intent.putExtra("lng", app.reciverLocation.getAddressDetail());
			}
		}
		intent.putExtra("getType", type);
		intent.putExtra("isSearch", true);
		startActivity(intent);
	}
	protected void gotoCity() {
		// TODO Auto-generated method stub
		isSelectCity = true;
		Intent intent = new Intent(AddRunOrder.this, SelCity.class);
		if (isSelectCity) {
			
		}
		intent.putExtra("isSearch", true);
		intent.putExtra("isReturn", true);
		startActivity(intent);
	}
	public void onResume()
	{
		super.onResume();
		/*if (tvCity.getText().toString().length() == 0) {
			if(isSelectCity){
				isSelectCity = false;
				if (app.searchLocation == null || app.searchLocation.getCityName().length() == 0) {
					finish();
					return;
				}
				if(addOrderModel == null){
					addOrderModel = new RunOrderInfo();
				}
				addOrderModel.setCityID(app.searchLocation.getCityID());
				tvCity.setText(app.searchLocation.getCityName());
			}else{
				gotoCity();
			}
			
		}*/
		if (isGotoMap != 0) {
			
			if(app.searchLocation == null || app.searchLocation.getLat() == 0.0 || app.searchLocation.getLon() == 0.0){
				isGotoMap = 0;
				return;
			}
			if (isGotoMap == 2) {
				if (app.reciverLocation == null) {
					app.reciverLocation = new MyLocationModel(app.searchLocation);
					
				}else{
					app.reciverLocation.setMyLocation(app.searchLocation);
				}
				if (app.reciverLocation.getLat() > 0.0 && app.reciverLocation.getLon() > 0.0 && app.reciverLocation.getAddressDetail().length() > 0) {
					isSetAddr = true;
					etGPSAddr.setText(app.reciverLocation.getAddressDetail());
					getSendMoeny();
				}
			}else{
				if (app.userLocation == null) {
					app.userLocation = new MyLocationModel(app.searchLocation);
					
				}else{
					app.userLocation.setMyLocation(app.searchLocation);
				}
				if (app.userLocation.getLat() > 0.0 && app.userLocation.getLon() > 0.0 && app.userLocation.getAddressDetail().length() > 0) {
					isSetAddr = true;
					etSenderGPS.setText(app.userLocation.getAddressDetail());
				}
			}
			isGotoMap = 0;
			
			
			
		}
		/*if (app.userInfo.cityid != null && app.userInfo.cityName != null  && app.userInfo.cityid.length() > 0
				 && app.userInfo.cityName.length() > 0) {
			etCityName.setText(app.userInfo.cityName);
		}else{
			GotoCity();
		}*/
	}
	
	private void getSendMoeny() {
		// TODO Auto-generated method stub
		//String value = etDate.getText().toString() + " " + etTime.getText().toString() + ":00";
		if (etSenderGPS.hasFocus() && checkIndex == 0 && searchResult != null) {//还处于焦点，直接点击了其他地方
			etSenderGPS.clearFocus();
			return;
		}
		if (etGPSAddr.hasFocus() && checkIndex == 0 && searchResult != null) {//还处于焦点，直接点击了其他地方
			etGPSAddr.clearFocus();
			return;
		}
		
		String value = etSenderGPS.getText().toString();
		if (value.length() < 1 || app.userLocation == null || app.userLocation.getLat() == 0.0 || app.userLocation.getLon() == 0.0) {
			Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addorder_raddr_n_s)/*"送货地址没有定位成功，请先给送货地址定位"*/, 15).show();
			nowEt = null;
			GoToMap(1);
			return;
		}
		value = etGPSAddr.getText().toString();
		if (value.length() < 1 || app.reciverLocation == null || app.reciverLocation.getLat() == 0.0 || app.reciverLocation.getLon() == 0.0) {
			Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addorder_raddr_n_g)/*"送货地址没有定位成功，请先给送货地址定位"*/, 15).show();
			nowEt = null;
			GoToMap(2);
			return;
		}
		if (timer == 0) {
			setDateTime(0);//半小时后
			value = String.format("%d-%d-%d %d:%d:00", mYear, mMonth, mDay, mHour, mMinute);
		}else{
			value = etDate.getText().toString() + " " + etTime.getText().toString() + ":00";
		}
		
		if (value.length() < 15) {
			Toast.makeText(AddRunOrder.this, getResources().getString(R.string.shop_addorder_time_n)/*"请选择配送时间"*/, 15).show();
			etDate.setError(getResources().getString(R.string.shop_addorder_time_n)/*"请选择配送时间"*/);
			return;
		}
		progressDialog = ProgressDialog.show(AddRunOrder.this, "",
				getResources().getString(R.string.shop_addorder_get_send_fee)/*"获取配送费中，请稍后..."*/);
		Log.v(TAG, "连接网络获取数据开始");
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("did", app.userInfo.userId);
			
		
		
		localHashMap.put("ulat", String.valueOf(app.reciverLocation.getLat()));
		localHashMap.put("ulng", String.valueOf(app.reciverLocation.getLon()));
		localHashMap.put("fromlat", String.valueOf(app.userLocation.getLat()));
		localHashMap.put("fromlng", String.valueOf(app.userLocation.getLon()));
		
		localHashMap.put("senttime", value);

		/*localHttpManager = new HttpManager(AddRunOrder.this, AddRunOrder.this,
				"/Android/expressfee.aspx?", localHashMap, 2, 2);

		localHttpManager.postRequest();*/
	}

	/*public void GotoCity(){
		Intent intent = new Intent(Regedit.this, SelCity.class);
		intent.putExtra("isSearch", false);
		intent.putExtra("isReturn", true);
		startActivity(intent);
	}*/
	
	private void Rand() {

		String a = "";

		int len = code.length;
		int readomWordIndex;
		for (int i = 0; i < 4; i++) {
			readomWordIndex = (int) (Math.random() * len);
			a += code[readomWordIndex];
		}
		//tvCode.setText(a);

	}

	// 检查手机号码是否规范
	private boolean checkMobilePhone(String paramString) {
		String str = paramString.replace("+86", "");
		return Pattern.compile("^(13|15|18)[0-9]{9}$").matcher(str).matches();
	}

	private class UIHandler extends Handler {
		
		protected static final int SEARCH_SUCESS = 3;
		public static final int NET_ERROR = -1;
		public static final int ADD_ORDER_OK = 1;
		public static final int ADD_ORDER_FAILD = -2;
		public static final int GET_SEND_MOENY_SUCESS = 2;
		public static final int GET_SEND_MOENY_FAILD = -3;

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case ADD_ORDER_OK:
				Intent intent = new Intent(AddRunOrder.this, OrderList.class);
				intent.putExtra("today", "0");
				intent.putExtra("ordertype", "2");
				intent.putExtra("orderMode", 2);//跑腿订单
				startActivity(intent);
				OtherManager.Toast(AddRunOrder.this, getResources().getString(R.string.shop_addorder_addorder_s)/*"提交订单成功"*/);
				etAddr.setText("");
				etName.setText("");
				telEditText.setText("");
				etGPSAddr.setText("");
				etCount.setText("1");
				etRemark.setText("");
				etMoney.setText("");
				etSenderGPS.setText("");
				etSenderAddr.setText("");
				etSendDistance.setText("");
				etSendMoney.setText("");
				etComName.setText("");
				finish();
				break;
			case ADD_ORDER_FAILD:
				OtherManager.Toast(AddRunOrder.this, getResources().getString(R.string.shop_addorder_addorder_f)/*"提交订单失败，请稍候再试！"*/);
				break;
			
			case NET_ERROR:
				OtherManager.Toast(AddRunOrder.this, getResources().getString(R.string.public_net_or_data_error)/*"网络错误，请稍后再试"*/);
				break;
			case GET_SEND_MOENY_FAILD:
				OtherManager.Toast(AddRunOrder.this, getResources().getString(R.string.shop_addorder_get_send_fee_f)/*"计算配送费失败！请稍后再试！"*/ + errMsg);
				break;
			case GET_SEND_MOENY_SUCESS:
				etSendMoney.setText(sendMoeny);
				etSendDistance.setText(sendDistance + getResources().getString(R.string.public_distance)/*"公里"*/);
				break;
			case SEARCH_SUCESS:
				if (nowEt == etGPSAddr) {
					if (!etGPSAddr.hasFocus()) {//已经不是焦点了 设置一个默认的地址
						checkIndex = 0;
						setSearchAddr();
						return;
						
					}
					recAutoCompleteTextView.setText(etGPSAddr.getText());
					recAutoCompleteTextView.showDropDown();
				}else{
					if (!etSenderGPS.hasFocus()) {//已经不是焦点了 设置一个默认的地址
						checkIndex = 0;
						setSearchAddr();
						return;
						
					}
					sendAutoCompleteTextView.setText(etSenderGPS.getText());
					sendAutoCompleteTextView.showDropDown();
				}
				
				break;
			}
			return;
		}
	}

	// 通过网络获取数据 获取完成后发送消息
	public void addOrder() {
		progressDialog = ProgressDialog.show(AddRunOrder.this, "",
				getResources().getString(R.string.public_send)/*"提交订单中，请稍后..."*/);
		Log.v(TAG, "连接网络获取数据开始");
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		/*if (app.userInfo.loginType == 2) {
			localHashMap.put("shopid", app.userInfo.userId);
			localHashMap.put("userid", "0");
		}else{
			localHashMap.put("userid", app.userInfo.userId);
			localHashMap.put("shopid", "0");
		}
		
		localHashMap.put("ulat", addOrderModel.getuLat());
		localHashMap.put("ulng", addOrderModel.getuLng());*/
		
		localHashMap.put("ordermodel", addOrderModel.beanToString());

		localHttpManager = new HttpManager(AddRunOrder.this, AddRunOrder.this,
				"/Android/express.aspx?", localHashMap, 2, 1);

		localHttpManager.postRequest();

	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		if (paramInt == 260) {
			Message message = new Message();
			String jsonString = (String) paramObject;

			if (OtherManager.DEBUG) {
				Log.v(TAG, "0 jsonString:" + jsonString);
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}

			// 开始解析接收到的json数据
			JSONObject json = null;
			try {
				json = new JSONObject(jsonString);

				if (OtherManager.DEBUG) {
					Log.v(TAG, "new JSONObject");
				}
				if (tag == 1) {
					int state = json.getInt("state");
					if (state == 1) {
						message.what = UIHandler.ADD_ORDER_OK;
					}else{
						message.what = UIHandler.ADD_ORDER_FAILD;
					}
					
				} else {
					
					state = json.getInt("state");

					if (state == 1) {
						
						json = json.getJSONObject("data");
						
					
							sendMoeny = json.getString("sendmoney");
							sendDistance = json.getString("Distance");
							message.what = UIHandler.GET_SEND_MOENY_SUCESS;
						
						
					}else{
						message.what = UIHandler.GET_SEND_MOENY_FAILD;
						errMsg = json.getString("msg");
					}
				}
				

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}
			hander.sendMessage(message); // 发送消息给Handler
		}

		if (OtherManager.DEBUG) {
			Log.v(TAG, "action 解析数据结束");
		}
	}
	class ViewHolder {
		TextView tv;
	}
	public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
		private class ArrayFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {//该方法不可少，否则可能不显示
				// TODO Auto-generated method stub
				if (searchResult == null || searchResult.getAllPoi().size() == 0) {
					return null;
				}//
				FilterResults results = new FilterResults();
				String values[] = new String[searchResult.getAllPoi().size()];
				
				for (int i = 0; i < values.length; i++) {
					values[i] = searchResult.getAllPoi().get(i).name;
				}
				results.values = values;
				results.count = searchResult.getAllPoi().size();
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Auto-generated method stub
				if (results != null) {
					if (results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
				
			}

			

		}

		private ArrayFilter mFilter;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (searchResult == null || searchResult.getAllPoi().size() == 0) {
				return 0;
			}
			return searchResult.getAllPoi().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return searchResult.getAllPoi().get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) AddRunOrder.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.simple_list_item_for_autocomplete, null);
				holder.tv = (TextView) convertView
						.findViewById(R.id.simple_item_0);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv.setText(searchResult.getAllPoi().get(position).name);
			return convertView;
		}

		@Override
		public Filter getFilter() {
			// TODO Auto-generated method stub
			if (mFilter == null) {
				mFilter = new ArrayFilter();
			}

			return mFilter;
		}

	}
	
}
