package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.FsgShopDetailModel;
import com.ihangjing.Model.GiftInfo;
import com.ihangjing.Model.UserDetail;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

public class GiftDedail extends HjActivity implements HttpRequestListener {

	// private Button backButton;
	private int skillid;
	ProgressDialog progressDialog = null;

	private static final String TAG = "showskill";
	private UIHandler handler;
	private GiftInfo shopmodeDetailModel;

	private Button backButton;
	private String dialogStr;
	HttpManager localHttpManager = null;

	private TextView titleTextView;
	private Button orderButton;
	private Button lotterButton;
	Activity mActivity;
	private int action = 0;//0获取详情，1抽奖
	private int point = -1;
	protected int selectType = 0;//0兑换，1抽奖
	private String lotterID = "";

	public GiftDedail() {
		//shopmodeDetailModel = new GiftInfo();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		handler = new UIHandler();

		initUI();

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);
		titleTextView.setText(getResources().getString(R.string.gift_detail_title)/*"积分兑换详情"*/);
		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 获取传输过来的商家编号
		Bundle extras = getIntent().getExtras();
		skillid = Integer.parseInt(extras.getString("id"));
		OnClickListener btnOnClickListener = new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//app.userInfo
				if (app.userInfo.userId.equals("0") || app.userInfo.userId.length() < 1) {
				    dialogStr = getResources().getString(R.string.public_login_notice);///*"请登录";
					showDialog(333);
					return;
				}
				switch (v.getId()) {
				case R.id.shopdetail_btn_lotter:
					if(point == -1){
						selectType  = 1;
						GetUserInfo();
					}else{
						LotterData();
					}
					
					break;
				case R.id.shopdetail_btn_order:
					if(point == -1){
						selectType  = 0;
						GetUserInfo();
					}else{
						GotoCart();
					}
					
					break;
				default:
					break;
				}
				
			}
		};
		lotterButton = (Button) findViewById(R.id.shopdetail_btn_lotter);
		lotterButton.setOnClickListener(btnOnClickListener);
		orderButton = (Button) findViewById(R.id.shopdetail_btn_order);

		orderButton.setOnClickListener(btnOnClickListener);
		action = 0;
		dialogStr = getResources().getString(R.string.public_get_data_notice);//"数据加载中...";
		showDialog(322);

		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("id", String.valueOf(skillid));

		localHttpManager = new HttpManager(GiftDedail.this, GiftDedail.this,
				"Android/GetGiftDetail.aspx?", localHashMap, 2, 0);
		localHttpManager.getRequeest();
	}
	
	private void GotoCart(){
		if(point < shopmodeDetailModel.getNeedPoint()){
			Toast.makeText(GiftDedail.this, getResources().getString(R.string.gift_detail_no_point)/*"您的积分不够，无法参与兑换！"*/, 5).show();
			return;
		}else{
			point -= shopmodeDetailModel.getNeedPoint();
			
		}
		Intent intent = new Intent(GiftDedail.this, GiftCart.class);
		intent.putExtra("id", String.valueOf(skillid));
		intent.putExtra("gName", shopmodeDetailModel.getgName());
		intent.putExtra("point", String.valueOf(shopmodeDetailModel.getNeedPoint()));
		intent.putExtra("gType", String.valueOf(shopmodeDetailModel.getType()));
		intent.putExtra("type", 0);
		startActivity(intent);
	}
	private void LotterData(){
		if(point < shopmodeDetailModel.getLotterPoint()){
			Toast.makeText(GiftDedail.this, getResources().getString(R.string.gift_detail_no_point_0)/*"您的积分不够，无法参与抽奖！"*/, 5).show();
			return;
		}else{
			
			
		}
		action = 1;
		dialogStr = getResources().getString(R.string.gift_detail_chang)/*"抽奖中..."*/;
		showDialog(322);

		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("userid", app.userInfo.userId);
		localHashMap.put("pid", String.valueOf(skillid));
		localHttpManager = new HttpManager(GiftDedail.this, GiftDedail.this,
				"App/Android/GetPrizeRecord.aspx?", localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}
	private void GetUserInfo() {
		this.dialogStr = getResources().getString(R.string.public_get_data_notice)/*"获取数据中..."*/;
		showDialog(322);
		action = 2;

		// 链接网络保存数据
		HashMap<String, String> localHashMap = new HashMap<String, String>();

		localHashMap.put("userid",
				OtherManager.getUserInfo(GiftDedail.this).userId);

		localHttpManager = new HttpManager(GiftDedail.this, GiftDedail.this,
				"/Android" + "/GetUserInfo.aspx?", localHashMap, 1, 2);
		localHttpManager.postRequest();
	}
	
	private void initUI() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.showskilllayout);
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		switch (paramInt) {
		case UIHandler.LOTTER_FAILD:
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this)
			.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.gift_detail_c_failed)/*"抽奖失败"*/);

			DialogInterface.OnClickListener local = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
		
					removeDialog(UIHandler.LOTTER_FAILD);
				}
			};
			dialog = localBuilder.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local).create();
			break;
		case UIHandler.LOTTER_NONE:
			AlertDialog.Builder localBuilder0 = new AlertDialog.Builder(this)
			.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.gift_detail_c_failed_0)/*"您没有抽到该奖品！"*/);

			DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
		
					removeDialog(UIHandler.LOTTER_NONE);
				}
			};
			dialog = localBuilder0.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local1).create();
			break;
		case UIHandler.LOTTER_SUCCESS:
			AlertDialog.Builder localBuilder2 = new AlertDialog.Builder(this)
			.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.gift_detail_c_sucess)/*"恭喜您抽到该奖品"*/);

			DialogInterface.OnClickListener local4 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					//跳出填写地址界面
					Intent intent = new Intent(GiftDedail.this, GiftCart.class);
					intent.putExtra("id", String.valueOf(skillid));
					intent.putExtra("gName", shopmodeDetailModel.getgName());
					intent.putExtra("point", String.valueOf(shopmodeDetailModel.getLotterPoint()));
					intent.putExtra("gType", String.valueOf(shopmodeDetailModel.getType()));
					intent.putExtra("lotterID", lotterID);
					intent.putExtra("type", 1);
					startActivity(intent);
					
					removeDialog(UIHandler.LOTTER_SUCCESS);
				}
			};
			dialog = localBuilder2.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local4).create();
			break;
		case 322:
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);

			DialogInterface.OnKeyListener okl = new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface paramDialogInterface,
						int paramInt, KeyEvent paramKeyEvent) {
					// 取消加载
					if ((paramInt == 4)
							&& (GiftDedail.this.localHttpManager != null)) {
						GiftDedail.this.localHttpManager.cancelHttpRequest();
					}
					return false;
				}
			};

			dialog.setOnKeyListener(okl);
			break;
		case 333:
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
			.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.public_login_notice)/*"请登录后再兑换"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
		
					Intent localIntent4 = new Intent(GiftDedail.this,
							Login.class);
					
					localIntent4.putExtra("mytype", "2");
					
					mActivity.startActivity(localIntent4);
				}
			};
		
			DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
		
					removeDialog(333);
				}
			};
		
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"登录"*/, local3)
					.setNegativeButton(getResources().getString(R.string.public_return)/*"返回"*/, local2).create();
			break;
		default:
			break;
		}
		return dialog;
	}

	private class UIHandler extends Handler {
		static final int NET_ERROR = -1; // 定义消息类型
		static final int SET_SHOP_DETAIL = 1;
		public static final int GET_POINT = 2;
		public static final int LOTTER_FAILD = 3;
		public static final int LOTTER_NONE = 4;
		public static final int LOTTER_SUCCESS = 5;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(322);
			switch (msg.what) {
			case LOTTER_FAILD:
				
				showDialog(LOTTER_FAILD);
				break;
			case LOTTER_NONE:
				point -= shopmodeDetailModel.getLotterPoint();
				showDialog(LOTTER_NONE);
				break;
			case LOTTER_SUCCESS:
				point -= shopmodeDetailModel.getLotterPoint();
				showDialog(LOTTER_SUCCESS);
				break;
			case GET_POINT:
				if(selectType == 0){
					GotoCart();
					
				}else{
					LotterData();
					
				}
				break;
			case NET_ERROR: {
				//removeDialog(322);
				Toast.makeText(GiftDedail.this, getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误，请稍后再试！"*/, 5).show();
				break;
			}
			case SET_SHOP_DETAIL: {
				// 这里对数据进行显示或做相应的处理
				if (shopmodeDetailModel == null) {
					Log.v(TAG, "shopmodeDetailModel == null");
				} else {

					TextView foodnametv_ = (TextView) findViewById(R.id.foodnametv);
					foodnametv_.setText(shopmodeDetailModel.getgName());

					TextView oldpricetv_ = (TextView) findViewById(R.id.oldpricetv);
					oldpricetv_
							.setText(String.valueOf(shopmodeDetailModel.getNeedPoint()));
					/*oldpricetv_.getPaint()
							.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);*/

					TextView newpricetv_ = (TextView) findViewById(R.id.newpricetv);
					newpricetv_
							.setText(String.valueOf(shopmodeDetailModel.getLotterPoint()));

					TextView sendfreetv_ = (TextView) findViewById(R.id.sendfreetv);
					sendfreetv_.setText("￥" + shopmodeDetailModel.getPrice());

					TextView cnumtv_ = (TextView) findViewById(R.id.cnumtv);
					cnumtv_.setText(String.valueOf(shopmodeDetailModel.getStocks()));
					TextView shopnametv_ = (TextView) findViewById(R.id.textView6);
					TextView addr = (TextView)findViewById(R.id.shopnametv);
					if(shopmodeDetailModel.getType() == 0){
						
						shopnametv_.setText(getResources().getString(R.string.gift_detail_gift_info)/*"礼品介绍："*/);
						addr.setText(shopmodeDetailModel.getContent());
					}else{
						shopnametv_.setText(getResources().getString(R.string.gift_detail_gift_value)/*"面额："*/);
						addr.setText(shopmodeDetailModel.getHasMoney());
						TextView tvNotice = (TextView)findViewById(R.id.tv_gift_type);
						tvNotice.setText(shopmodeDetailModel.getNotice());
					}

					//removeDialog(322);
				}
				break;
			}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
		// this.isActivityRun = false;
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		Message message = new Message();
		if(paramInt == 260){
			String jsonString = (String) paramObject;
			Log.v(TAG, jsonString);
			try {
				
				if(tag == 0){
					shopmodeDetailModel = new GiftInfo();
					shopmodeDetailModel.stringToBean(jsonString);
					message.what = UIHandler.SET_SHOP_DETAIL;
				}else {
					JSONObject json = null;
					json = new JSONObject(jsonString);
					if(action == 1){
					
						switch (json.getInt("state")) {
						case -1:
							message.what = UIHandler.LOTTER_FAILD;
							break;
						case 0:
							message.what = UIHandler.LOTTER_NONE;
							break;
						case 1:
							lotterID  = json.getString("rid");//中奖编号
							message.what = UIHandler.LOTTER_SUCCESS;
							break;
						default:
							break;
						}
					}else{
						point  = json.getInt("Point");
						message.what = UIHandler.GET_POINT;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}
		}else{
			
		}

		// 数据加载成功 页面更新
		
		
		handler.sendMessage(message);
	}
}
