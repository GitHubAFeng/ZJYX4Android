package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihangjing.ZJYXForAndroid.MyFavorShopActivity.UIHandler;
import com.ihangjing.Model.UserDetail;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class UserCenterIndex extends HjActivity implements HttpRequestListener{

	// private UserDetail user = null;
	private TextView usernameTextView;
	private boolean isShowLogin = false;
	private TextView tvEmail;
	private TextView tvPhone;
	private HttpManager localHttpManager;
	private UIHandler mHandler;
	private TextView tvGiftList;
	private TextView tvNPoint;
	private Button btnBack;
	private TextView tvTitle;
	private TextView tvHaveMoney;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_center_index);
		mHandler = new UIHandler();

		usernameTextView = (TextView) findViewById(R.id.tv_username);
		tvEmail = (TextView)findViewById(R.id.tv_email);
		tvPhone = (TextView)findViewById(R.id.tv_phone);
		tvGiftList = (TextView)findViewById(R.id.tv_giftlist);
		tvGiftList.setOnClickListener(new OnClickListener() {//礼品订单
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = null;
				intent = new Intent(UserCenterIndex.this, GiftOrderList.class);
				startActivity(intent);
			}
		});
		tvNPoint = (TextView)findViewById(R.id.tv_n_point_1);
		btnBack = (Button)findViewById(R.id.title_bar_back_btn);
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tvTitle = (TextView)findViewById(R.id.title_bar_content_tv);
		tvTitle.setText(getResources().getString(R.string.user_center_title)/*"个人中心"*/);
		UserDetail userDetail = OtherManager.getUserInfo(UserCenterIndex.this);
		tvHaveMoney = (TextView)findViewById(R.id.tv_have_money);
		usernameTextView.setText(userDetail.userName);

		LinearLayout order2RelativeLayout = (LinearLayout) findViewById(R.id.user_center_index_userinfo_rl);
		order2RelativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(UserCenterIndex.this, UserInfo.class);
				startActivity(intent);
			}
		});

		TextView resetPasswprdRelativeLayout = (TextView) findViewById(R.id.tv_password);
		resetPasswprdRelativeLayout.setOnClickListener(new OnClickListener() {//修改登陆密码
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(UserCenterIndex.this, ResetPassword.class);
				intent.putExtra("pay", false);
				startActivity(intent);
			}
		});
		
		LinearLayout resetPayPasswprdRelativeLayout = (LinearLayout) findViewById(R.id.ll_pay_password);
		resetPayPasswprdRelativeLayout.setOnClickListener(new OnClickListener() {//修改支付密码
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(UserCenterIndex.this, ResetPassword.class);
				intent.putExtra("pay", true);
				startActivity(intent);
			}
		});
		
		LinearLayout lPoint = (LinearLayout)findViewById(R.id.ll_point);
		lPoint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//积分记录
				Intent intent = new Intent(UserCenterIndex.this, MyPointListView.class);
				startActivity(intent);
			}
		});
		
		// 我的订单
		TextView order212RelativeLayout = (TextView) findViewById(R.id.tv_orderlist);
		order212RelativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(UserCenterIndex.this, OrderList.class);
				intent.putExtra("today", "0");
				intent.putExtra("ordertype", "2");
				startActivity(intent);
			}
		});
		
		TextView tvMyFriends = (TextView) findViewById(R.id.tv_myfriends);
		tvMyFriends.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(UserCenterIndex.this, FsgShopList.class);
				intent.putExtra("isRem", 7);//收藏商家
				startActivity(intent);
			}
		});
		
		TextView tvMyCoupons = (TextView) findViewById(R.id.tv_mycoupons);
		tvMyCoupons.setOnClickListener(new OnClickListener() {//我的优惠券
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(UserCenterIndex.this, MyCouponsListView.class);
				
				startActivity(intent);
			}
		});
		
		TextView tvMyMessage = (TextView) findViewById(R.id.tv_mymessage);
		tvMyMessage.setOnClickListener(new OnClickListener() {//花马巴黎互动
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(UserCenterIndex.this, MyMessageListActivity.class);
				
				startActivity(intent);
			}
		});
		
		TextView tvMyAddress = (TextView) findViewById(R.id.tv_myaddress);
		tvMyAddress.setOnClickListener(new OnClickListener() {//花马巴黎互动
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterIndex.this, UserAddressList.class);
				intent.putExtra("select", false);
				startActivity(intent);
			}
		});
		
		Button logoutButton = (Button) findViewById(R.id.user_loginout_btn);
		logoutButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				// 退出系统 清除用户信息
				app.userInfo.userId = "";
				app.userInfo.userName = "";

				OtherManager.saveUserInfo(UserCenterIndex.this, app.userInfo);
				//Intent localIntent = new Intent("com.ihangjing.common.tap0");
				//app.sendBroadcast(localIntent);
				//finish();
				finish();
				
			}
		});
	}
	
	private void GetUserInfo(){
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		
		localHashMap.put("userid", app.userInfo.userId);

		localHttpManager = new HttpManager(UserCenterIndex.this, UserCenterIndex.this,
				"/Android/GetUserInfo.aspx?", localHashMap, 2, 1);

		localHttpManager.getRequeest();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();

		} else {
			boolean bool = true;
			bool = super.onKeyDown(keyCode, paramKeyEvent);
			return bool;
		}

		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (app.userInfo == null || app.userInfo.userId.equals("0") || app.userInfo.userId.equals("")) {
			
			if (isShowLogin) {
				isShowLogin = false;
				finish();
				return;
			}
			isShowLogin  = true;
			Intent localIntent4 = new Intent(UserCenterIndex.this,
					Login.class);
			localIntent4.putExtra("isReturn", true);
			UserCenterIndex.this.startActivity(localIntent4);
			return;
		}else{
			GetUserInfo();
			usernameTextView.setText(app.userInfo.userName);
			tvEmail.setText(app.userInfo.email);
			tvPhone.setText(app.userInfo.phone);
			//tvGiftList.setText(String.format("账户余额：%.2f￥", app.userInfo.userMoney));
		}
		
		/*if (app.isViewOrderList) {
			app.isViewOrderList = false;
			Intent intent = new Intent(UserCenterIndex.this, OrderList.class);
			intent.putExtra("today", "0");
			intent.putExtra("ordertype", "2");
			startActivity(intent);
		}*/

	}
	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt == 260) {
			if (tag == 1) {
				String value = (String)paramObject;
				try {
					
					JSONObject json = new JSONObject(value);
					app.userInfo.email = json.getString("email");
					app.userInfo.QQ = json.getString("qq");
					app.userInfo.phone = json.getString("phone");
					app.userInfo.userMoney = Float.valueOf(json.getString("HaveMoney"));
					app.userInfo.trueName = json.getString("truename");
					app.userInfo.nPoint = json.getString("Point");
					app.userInfo.hPoint = json.getString("historypoint");
					app.userInfo.pPoint = json.getString("publicgood");
					app.userInfo.myICO = json.getString("pic");
					app.mLoadImage.addTask(app.userInfo.myICO, null, 0);
					
					
					OtherManager.saveUserInfo(UserCenterIndex.this, app.userInfo);
					message.what = UIHandler.GET_SUCCESS ;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message.what = UIHandler.NET_ERROR ;
				}
			}
		}else{
			message.what = UIHandler.NET_ERROR ;
		}
		mHandler.sendMessage(message);
	}
	
	private class UIHandler extends Handler {
		public final static int NET_ERROR = -1;
		static final int GET_SUCCESS = 1; //

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_SUCCESS: {
				// 显示数据列表
				app.mLoadImage.doTask();
				usernameTextView.setText(app.userInfo.userName);
				tvEmail.setText(app.userInfo.email);
				tvPhone.setText(app.userInfo.phone);
				//tvGiftList.setText(String.format("账户余额：%.2f￥", app.userInfo.userMoney));
				tvNPoint.setText(String.format("%s", app.userInfo.nPoint));
				tvHaveMoney.setText(String.format(getResources().getString(R.string.user_center_moeny)/*"余额：%.2f￥"*/, app.userInfo.userMoney, getResources().getString(R.string.public_moeny_0)));
				return;
			}
			case NET_ERROR: {
				
				OtherManager.Toast(UserCenterIndex.this, getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误，无法获取用户信息"*/);
				return;
			}
			}
			// OtherManager.Toast(AddOrder.this, "获取数据失败");
			return;
		}
	}
}