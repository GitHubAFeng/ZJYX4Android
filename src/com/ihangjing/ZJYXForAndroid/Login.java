package com.ihangjing.ZJYXForAndroid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;















import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;
import com.ihangjing.wxpay.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

public class Login extends HjActivity implements HttpRequestListener, PlatformActionListener, Callback{

	private static final String TAG = "Login";
	ProgressDialog progressDialog = null;
	private EditText userNameEditText;
	private EditText passwordEditText;
	private UIHandler hander;
	private int state;// �ӿڷ��ص���ֵ 1 �ɹ� ���� ʧ��
	//private String userid;// �ӿڷ��ص���ֵ �û����
	private String username;
	private String password;
	private String mytype="0";//,ShopId,ShopName,skillid;
	
	HttpManager localHttpManager = null;
	private Platform qq;
	protected Platform weixin;
	protected int loginType = 0;//0��ͨ��¼ 1����qq��¼��ť 2΢�ŵ�¼ 3QQ��¼������
	

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {  
	            Window window = getWindow();  
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS  
	                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);  
	            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  
	                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  
	                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);  
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  
	            window.setStatusBarColor(Color.TRANSPARENT);  
	            window.setNavigationBarColor(Color.TRANSPARENT);  
	        } 


		setContentView(R.layout.login);
		hander = new UIHandler();
		
		Button btnBack = (Button)findViewById(R.id.title_bar_back_btn);
		btnBack.setVisibility(View.GONE);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView tvFindPassword = (TextView)findViewById(R.id.tv_findpassword);
		tvFindPassword.setText(Html.fromHtml("<u>"+getResources().getString(R.string.login_find_password)+"</u>"));
		tvFindPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = null;
				intent = new Intent(Login.this, FindPassword.class);
				startActivity(intent);
			}
		});
		TextView title = (TextView)findViewById(R.id.title_bar_content_tv);
		
		title.setText(getResources().getString(R.string.login_label_signin)/*"��½"*/);
		userNameEditText = (EditText) findViewById(R.id.login_account_et);
		passwordEditText = (EditText) findViewById(R.id.login_password_et);
		
		Bundle extras = getIntent().getExtras();
		mytype= OtherManager.GetQueryString(extras, "mytype", "0");
		//ShopId = OtherManager.GetQueryString(extras, "ShopId", "");
		//ShopName = OtherManager.GetQueryString(extras, "ShopName", "");
		//skillid = OtherManager.GetQueryString(extras, "skillid", "0");

		final Button cancelButton = (Button) findViewById(R.id.login_register_btn);
		final Button loginButton = (Button) findViewById(R.id.login_btn);

		loginButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				// �ж��Ƿ�����
				// �˺Ų���Ϊ��
				// ���벻��Ϊ��
				username = userNameEditText.getText().toString().trim();
				password = passwordEditText.getText().toString().trim();

				if (username.length() <= 0) {
					showDialog(110);
					return;
				}
				if (password.length() <= 0) {
					showDialog(111);
					return;
				}

				progressDialog = ProgressDialog.show(Login.this, "",
						getResources().getString(R.string.login_status_logging_in)/*"��¼�У����Ժ�..."*/);

				try {
					DoLogin();
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		cancelButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = null;
				intent = new Intent(Login.this, Regedit.class);
				startActivity(intent);

				finish();
			}
		});
		
		Button btnQQLogin = (Button)findViewById(R.id.btn_qq_login);
		btnQQLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(Login.this, "",
						getResources().getString(R.string.login_status_logging_in)/*"��¼�У����Ժ�..."*/);
				qq = ShareSDK.getPlatform(QQ.NAME);
				qq.SSOSetting(false);  //����false��ʾʹ��SSO��Ȩ��ʽ
				qq.setPlatformActionListener(Login.this); // ���÷����¼��ص�
				qq.authorize();
				loginType  = 1;
			}
		});
		Button btnWXLogin = (Button)findViewById(R.id.btn_wx_login);
		btnWXLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(Login.this, "",
						getResources().getString(R.string.login_status_logging_in)/*"��¼�У����Ժ�..."*/);
				weixin = ShareSDK.getPlatform(Wechat.NAME);
				weixin.SSOSetting(false);  //����false��ʾʹ��SSO��Ȩ��ʽ
				weixin.setPlatformActionListener(Login.this); // ���÷����¼��ص�
				weixin.authorize();
				loginType = 2;
			}
		});
		ShareSDK.initSDK(Login.this);
		
	}
	
	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog;

		if (paramInt == 110) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"��ʾ"*/).setMessage(getResources().getString(R.string.login_status_null_username_or_password)/*"�û�������Ϊ��"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					Login.this.removeDialog(110);
					Login.this.userNameEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"ȷ��"*/, local3).create();

			return dialog;
		}

		if (paramInt == 111) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"��ʾ"*/).setMessage(getResources().getString(R.string.login_status_invalid_username_or_password)/*"���벻��Ϊ��"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					Login.this.removeDialog(111);
					Login.this.passwordEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"ȷ��"*/, local3).create();

			return dialog;
		}

		dialog = null;
		return dialog;

	}
	@Override
	protected void onPause() {
		super.onPause();
		
		/*if(progressDialog != null){
			progressDialog.dismiss();
			
		}*/
		//MobclickAgent.onPause(this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		
		}
	}
	@Override
	public void onResume(){
		super.onResume();
		
			//�ж�ָ��ƽ̨�Ƿ��Ѿ������Ȩ
			
	}
	
	private class UIHandler extends Handler {
		static final int LOGIN_SUCCESS = 2; // ��¼�ɹ�
		static final int LOGIN_FAILD = 3; // ��¼ʧ��
		public static final int QQ_START_LOGIN = 1;
		public static final int WX_START_LOGIN = 4;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WX_START_LOGIN:
				if(weixin.isAuthValid()) {
					 String userId = weixin.getDb().getUserId();
					 String name = weixin.getDb().getUserName();
					 app.userInfo.myICO = weixin.getDb().getUserIcon();
					 //btShopZ1 = getBitmap(weixin.getDb().getUserIcon());
					 if(userId != null && userId.length() > 0){
						 OtherLogin(userId, name, "WeiXin");
					 }else{
						 if(progressDialog != null){
								progressDialog.dismiss();
								
						}
						 Toast.makeText(Login.this, "��ȡ�û���Ϣʧ��", 15).show();
					}
					 
				}else{
					if(progressDialog != null){
						progressDialog.dismiss();
						
					}
					Toast.makeText(Login.this, "��ȡ��Ȩʧ��", 15).show();
				}
				weixin = null;
				loginType = 0;
				break;
			case QQ_START_LOGIN:
				if(qq.isAuthValid()) {
					 String userId = qq.getDb().getUserId();
					 String name = qq.getDb().getUserName();
					 app.userInfo.myICO = qq.getDb().getUserIcon();
					 //btShopZ1 = getBitmap(qq.getDb().getUserIcon());
					 if(userId != null && userId.length() > 0){
						 OtherLogin(userId, name, "QQ");
					 }else{
						 if(progressDialog != null){
								progressDialog.dismiss();
								
						}
						 Toast.makeText(Login.this, "��ȡ�û���Ϣʧ��", 15).show();
					}
					 
				}else{
					if(progressDialog != null){
						progressDialog.dismiss();
						
					}
					Toast.makeText(Login.this, "��ȡ��Ȩʧ��", 15).show();
				}
				qq = null;
				loginType = 0;
				break;
			case LOGIN_SUCCESS: {
				// ��ʾ�����б�
				/*if(updageUserICON == 1){
					updateImage();
					return;
				}*/
				progressDialog.dismiss();
				finish();
				return;
			}
			case LOGIN_FAILD: {
				progressDialog.dismiss();

				// �˺Ż��������������������
				OtherManager.Toast(Login.this, getResources().getString(R.string.login_status_failure_0)/*"�˺Ż��������������������"*/);
				return;
			}
			}
			// OtherManager.Toast(AddOrder.this, "��ȡ����ʧ��");
			return;
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		Message message = new Message();
		if(paramInt == 260){
			String jsonString = (String) paramObject;
	
			if (OtherManager.DEBUG) {
				Log.v(TAG, "0 jsonString:" + jsonString);
			}
	
			JSONObject json = null;
			try {
				json = new JSONObject(jsonString);
	
				if (OtherManager.DEBUG) {
					Log.v(TAG, "new JSONObject");
				}
				switch (tag) {
				case 1:
					state = json.getInt("state");
					
					if (state == 1) {
						app.userInfo.userId = json.getString("userid");
						app.userInfo.userName = username;
						//updageUserICON = 1;
						// �����û������û����
						//app.userInfo.userName = json.getString("username");
						OtherManager.saveUserInfo(Login.this, app.userInfo);
						
						message.what = UIHandler.LOGIN_SUCCESS;
						Log.v(TAG, "hander.sendMessage(LOGIN_SUCCESS)");
					} else {
						message.what = UIHandler.LOGIN_FAILD;
						Log.v(TAG, "hander.sendMessage(LOGIN_FAILD)");
					}
					break;
				case 3:
					state = json.getInt("state");
					//updageUserICON = 0;
					message.what = UIHandler.LOGIN_SUCCESS;
					break;
				default:
					break;
				}
				// ���յ��̼��б�json
				// ֪ͨactivity�����б���°�
				
			
				
	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.LOGIN_FAILD;
			}
	
			if (OtherManager.DEBUG) {
				Log.v(TAG, "action �������ݽ���");
			}
		}else{
			message.what = UIHandler.LOGIN_FAILD;
		}
		hander.sendMessage(message); // ������Ϣ��Handler
	}

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void DoLogin() throws HttpException, JSONException {

		Log.v(TAG, "���������ȡ���ݿ�ʼ");

		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("username", username.replace(" ", ""));
		localHashMap.put("password", password.replace(" ", ""));

		localHttpManager = new HttpManager(Login.this, Login.this,
				"/Android/Login.aspx?", localHashMap, 2, 1);

		localHttpManager.getRequeest();

	}
	
	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void OtherLogin(String openid, String name, String type) {

			Log.v(TAG, "���������ȡ���ݿ�ʼ");

			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("uid", openid);
			localHashMap.put("uname", name);
			localHashMap.put("utype", type);
			localHashMap.put("uway", "1");

			localHttpManager = new HttpManager(Login.this, Login.this,
					HJAppConfig.DOMAIN + "/account/ThirdpartyLogin.aspx?", localHashMap, 2, 1);

			localHttpManager.getRequeest();

	}
	
	

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		if (weixin != null && arg0 == weixin) {
			Message msg = new Message();
			msg.what = UIHandler.WX_START_LOGIN;
			hander.sendMessage(msg);
			
		}
		if (qq != null && arg0 == qq) {
			Message msg = new Message();
			msg.what = UIHandler.QQ_START_LOGIN;
			hander.sendMessage(msg);
			
		}
		
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
}
