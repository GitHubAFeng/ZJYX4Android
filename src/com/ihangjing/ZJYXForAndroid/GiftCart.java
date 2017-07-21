package com.ihangjing.ZJYXForAndroid;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

/**
 * 秒杀提交订单
 * 
 * @author Administrator
 * 
 */
public class GiftCart extends HjActivity implements HttpRequestListener {

	private UIHandler hander;
	private String dialogStr;
	public Dialog progressDialog;
	private String id;
	private String ggName;
	private String point;
	private int type = 0;//0兑换 1 抽奖
	private TextView giftName;
	private EditText etTrueName;
	private EditText etUserPhone;
	private EditText etUAddr;
	private EditText etRemark;
	private TextView giftPoint;
	private LinearLayout llAddress;
	private Button btnTime;
	private CharSequence sentTime;// = getResources().getString(R.string.gift_cart_time);//不限
	protected String[] imageItems;//  = new String[] { getResources().getString(R.string.gift_cart_time)/*"不限"*/, getResources().getString(R.string.gift_cart_time0)/*"周一-周五"*/, getResources().getString(R.string.gift_cart_time1)/*"周末"*/ };
	private Button submitBtn;
	private String sendtype = "2";
	private String gType;
	private int actionType = 1;//1 提交兑换订单 2 提交抽奖
	HttpManager localHttpManager = null;
	private String lotterID;//中奖编号
	

	private void InitUI() {
		imageItems = new String[3];
		imageItems[0] =  getResources().getString(R.string.gift_cart_time);/*"不限"*/
		imageItems[1] =  getResources().getString(R.string.gift_cart_time0);/*"周一-周五"*/
		imageItems[2] =  getResources().getString(R.string.gift_cart_time1);/*"周末"*/
		sentTime = getResources().getString(R.string.gift_cart_time);//不限
		giftName = (TextView)findViewById(R.id.tv_name_n);
		giftName.setText(ggName);
		
		giftPoint = (TextView)findViewById(R.id.tv_point_n);
		giftPoint.setText(point);
		etTrueName = (EditText)findViewById(R.id.et_uname);
		etUserPhone = (EditText)findViewById(R.id.et_uphone);
		etUAddr = (EditText)findViewById(R.id.et_uaddr);
		etRemark = (EditText)findViewById(R.id.et_uremark);
		SharedPreferences usersetting = getApplicationContext()
				.getSharedPreferences(HJAppConfig.CookieName,
						Context.MODE_PRIVATE);

		// 取出记忆中的信息
		etTrueName.setText(usersetting.getString("order_username", ""));
		etUAddr.setText(usersetting.getString("order_address", ""));
		etUserPhone.setText(usersetting.getString("order_phone", ""));
		
		LinearLayout llLayout = (LinearLayout)findViewById(R.id.ll_point);
		llAddress = (LinearLayout)findViewById(R.id.ll_address);
		if(type == 0){
			LinearLayout llRemark = (LinearLayout)findViewById(R.id.ll_remark);
			llRemark.setVisibility(View.GONE);
		}else{
			llLayout.setVisibility(View.GONE);
		}
		btnTime = (Button)findViewById(R.id.btn_time);
		btnTime.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(GiftCart.this)
				.setTitle(getResources().getString(R.string.gift_cart_time_notice))//请确选择收货时间
				.setItems(imageItems, new DialogInterface.OnClickListener() {
					

					// @Override
					public void onClick(DialogInterface dialog, int which) {
						btnTime.setText(imageItems[which]);
						sentTime = imageItems[which];
					}
				}).show();
			}
		});
		submitBtn = (Button)findViewById(R.id.btn_submit);
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (etTrueName.getText().length() < 1) {
					Toast.makeText(GiftCart.this, getResources().getString(R.string.shop_cart_reciver_notice)/*"请填写联系人姓名"*/, 5).show();
					return;
				}
				if (etUserPhone.getText().length() < 1) {
					Toast.makeText(GiftCart.this, getResources().getString(R.string.addr_detail_del_phone_error)/*"请填写联系人手机号码"*/, 5).show();
					return;
				} else {
					// 检查电话号码输入是否正确
					if (!checkMobilePhone(etUserPhone.getText().toString())) {
						Toast.makeText(GiftCart.this, getResources().getString(R.string.addr_detail_del_phone_error)/*"请输入正确的手机号码"*/, 5).show();
						return;
					}
				}
				if (sendtype.equals("2") && etUAddr.getText().length() < 1) {
					Toast.makeText(GiftCart.this, getResources().getString(R.string.shop_cart_addr_notice)/*"请填写正确的地址"*/, 5).show();
					return;
				}
				
				SharedPreferences usersetting = getApplicationContext()
						.getSharedPreferences(HJAppConfig.CookieName,
								Context.MODE_PRIVATE);
				Editor userinfoeditor = usersetting.edit();
				userinfoeditor.putString("order_phone", etUserPhone.getText()
						.toString());
				
				userinfoeditor.putString("order_username", etTrueName.getText()
						.toString());

				userinfoeditor.commit();
				if(type == 0){
					if(sendtype.equals("2")){
						userinfoeditor.putString("order_address", etUAddr.getText()
								.toString());
					}
					SubmitOrder();
				}else{
					userinfoeditor.putString("order_address", etUAddr.getText()
							.toString());
					SubmitLotter();
				}
			}
		});
		RadioGroup rg = (RadioGroup)findViewById(R.id.eattyperadioGroup);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (group.getCheckedRadioButtonId()) {
				case R.id.reattype0://送货
					sendtype = "2";
					llAddress.setVisibility(View.VISIBLE);
					break;
				case R.id.reattype11://自取
					sendtype = "1";
					llAddress.setVisibility(View.GONE);
					break;
				}
			}
		});
		

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.skillcartlayout);
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			id = extras.getString("id");
			ggName = extras.getString("gName");
			point = extras.getString("point");
			type = extras.getInt("type");//0兑换 1 抽奖
			if(type == 1){
				lotterID = extras.getString("lotterID");
			}
			gType = extras.getString("gType");
		}
		InitUI();
		hander = new UIHandler();

		// TextView shopcartTextView = (TextView) findViewById(R.id.textView6);
		// shopcartTextView.setText(str_orderlist);
	}


	// 提交订单线程
	private void SubmitOrder(){
		dialogStr = getResources().getString(R.string.shop_cart_add_order);//"订单提交中......";
		showDialog(322);
		actionType  = 1;
		String ordermodelstringfix = "";
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("Address", etUAddr.getText().toString());
			localJSONObject1.put("Cdate", "");
			localJSONObject1.put("CustId", app.userInfo.userId);
			localJSONObject1.put("UserName", app.userInfo.userName);
			localJSONObject1.put("GiftName", ggName);

			localJSONObject1.put("Date", sentTime);
			localJSONObject1.put("DetailId", "0");
			localJSONObject1.put("GiftsId", id);
			localJSONObject1.put("PayIntegral", point);
			localJSONObject1.put("Person", etTrueName.getText().toString());
			localJSONObject1.put("Phone", etUserPhone.getText().toString());
			localJSONObject1.put("State", "0");
			localJSONObject1.put("sendtype", sendtype );
			localJSONObject1.put("ReveInt1", gType);
			localJSONObject1.put("remark", etRemark.getText().toString());
			ordermodelstringfix = "[" + localJSONObject1.toString() + "]";
		}catch (Exception e) {
			// TODO: handle exception
			Message message = new Message();
			message.what = UIHandler.SUB_ERROR;
			hander.sendMessage(message);
			return;
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("ordermodel", ordermodelstringfix);

		localHttpManager = new HttpManager(GiftCart.this,
				GiftCart.this,  "/Android"
						+ "/Changegift.aspx?", localHashMap, 1, 1);
		localHttpManager.postRequest();
	}
	// 提交订单线程
		private void SubmitLotter(){
			dialogStr = getResources().getString(R.string.shop_cart_add_order);//"订单提交中......";
			showDialog(322);
			actionType  = 2;
			
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("rid", lotterID);
			localHashMap.put("Phone", etUserPhone.getText().toString());
			localHashMap.put("Person", etTrueName.getText().toString());
			localHashMap.put("Address", etUAddr.getText().toString());
			localHashMap.put("Remark", etRemark.getText().toString());

			localHttpManager = new HttpManager(GiftCart.this,
					GiftCart.this, "/Android"
							+ "/luckgift.aspx?", localHashMap, 1, 2);
			localHttpManager.postRequest();
		}

	private void GoPointOrderDetail() {
		Intent intent = null;
		//if(actionType == 1){
			intent = new Intent(GiftCart.this, GiftOrderList.class);
		//}else{
		//	intent = new Intent(GiftCart.this, LotterOrderList.class);
		//}
		startActivity(intent);
		GiftCart.this.finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private class UIHandler extends Handler {
		public static final int SUB_ERROR = -3;
		
		static final int ORDER_SUBMIT_SUCCESS = 1;
		static final int ORDER_SUBMIT_FAILD = -1;
		
		public static final int NET_ERROR = -2;

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SUB_ERROR:
				Toast.makeText(GiftCart.this, getResources().getString(R.string.gift_cart_no_sub)/*"无法提交，请稍后再试！"*/, 5).show();
				break;
			
			case ORDER_SUBMIT_SUCCESS: {

				removeDialog(322);
				showDialog(1);
				break;
			}
			case ORDER_SUBMIT_FAILD: {
				removeDialog(322);
				OtherManager.Toast(GiftCart.this, getResources().getString(R.string.shop_cart_add_order_faild)/*"提交订单失败，请稍候再试！"*/);
				break;
			}
			case NET_ERROR:
				OtherManager.Toast(GiftCart.this, getResources().getString(R.string.shop_cart_get_data_error)/*"获取数据失败"*/);
				break;
			
			case 200: {

				showDialog(111);
				break;
			}
			case 400: {
				OtherManager.Toast(GiftCart.this, getResources().getString(R.string.gift_cart_no_add)/*"暂无地址,请稍后进入地址薄添加"*/);
				break;
			}
			}
		}

	}

	final class ListHolder {
		TextView tv_foodName;
		TextView tv_foodCount;
		// TextView tv_foodDown;
		TextView tv_foodPrice;

		// TextView tv_foodUp;

		ListHolder() {
		}
	}

	// 检查手机号码是否规范
	private boolean checkMobilePhone(String paramString) {
		String str = paramString.replace("+86", "");
		return Pattern.compile("^(13|15|18|17)[0-9]{9}$").matcher(str).matches();
	}

	
	

	@Override
	public void action(int paramInt, Object paramObject, int tag) {

		Message message = new Message();
		removeDialog(322);
		if (paramInt == 260) {

			String jsonString = (String) paramObject;

			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				if(jsonObject.getInt("state") == 1){
					
					message.what = UIHandler.ORDER_SUBMIT_SUCCESS;
				}else{
					message.what = UIHandler.ORDER_SUBMIT_FAILD;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				message.what = UIHandler.NET_ERROR;
				e.printStackTrace();
			}
		} else {
			message.what = UIHandler.NET_ERROR;
		}
		hander.sendMessage(message); // 发送消息给Handler
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);
			return dialog;
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.gift_cart_sub_sucess)/*"提交成功"*/).setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							GoPointOrderDetail();
							dialog.dismiss();
						}

					});
			dialog = builder.create();
			return dialog;
		}
	}
}
