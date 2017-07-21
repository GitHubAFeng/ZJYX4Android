package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.ReciveAddressModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class FindPassword extends HjActivity implements HttpRequestListener {
	private TextView titleTextView;
	private Button backButton;
	private Button saveButton;

	private static final String TAG = "Login";
	ProgressDialog progressDialog = null;
	private EditText emailEditText;
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText telEditText;
	private EditText rnameEditText;

	private UIHandler hander;
	private int state;// 接口返回的数值 1 成功 其他 失败
	private String userid;// 接口返回的数值 用户编号
	private String email;
	private String username;
	private String password;
	private String tel;
	private String rname;// 引荐人
	private String cityid;// 城市编号
	//private TextView tvCode;
	private EditText etCode;
	HttpManager localHttpManager = null;
	String[] code = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
			"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "o",
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B",
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private int nHttpType;
	private String SMSCode;
	public int nWorkMode = 0;
	private EditText passwordEditTexta;
	private Button btnSendCode;
	public Timer timerOrder;
	public int timerTim;
	private String errMsg;
	private int autoSMS;

	// http://xxx/APP/Android/Regedit.aspx?email=zjfxqt@163.com&username=zjfxqt&password=123456
	// 返回 state：1 注册成功 -2 存在用户名 -3 邮箱已经存在 -1注册失败
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		setContentView(R.layout.find_password);

		hander = new UIHandler();

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("忘记密码");
		/*tvCode = (TextView) findViewById(R.id.tv_code);
		tvCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Rand();
			}
		});*/
		etCode = (EditText) findViewById(R.id.et_code);
		userNameEditText = (EditText) findViewById(R.id.register_name_et);
		passwordEditText = (EditText) findViewById(R.id.register_password_et);
		passwordEditTexta = (EditText)findViewById(R.id.register_password_et_a);
		emailEditText = (EditText) findViewById(R.id.register_email_et);
		telEditText = (EditText) findViewById(R.id.register_tel_et);
		rnameEditText = (EditText) findViewById(R.id.register_friend_et);
		cityid = OtherManager.getUserLocal(FindPassword.this).cityid;
		

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});
		btnSendCode = (Button)findViewById(R.id.btn_sendcode);
		btnSendCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tel = telEditText.getText().toString().replace(" ", "")
						.trim();
				if (!checkMobilePhone(tel)) {
					showDialog(113);
					return;
				}
				SendData(telEditText.getText().toString());
			}
		});

		saveButton = (Button) findViewById(R.id.register_btn);

		saveButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				// 判断是否输入
				// 账号不能为空
				// 密码不能为空
				String in;
				in = etCode.getText().toString().toLowerCase();
				
				if (!in.equals(SMSCode)) {
						Toast.makeText(FindPassword.this.getApplicationContext(),
								getResources().getString(R.string.regedit_smscode_error)/*"短信验证码错误，请重新输入"*/, 5).show();
						return;
				}
				if (tel.compareTo(telEditText.getText().toString().replace(" ", "")
						.trim()) != 0) {
					Toast.makeText(FindPassword.this.getApplicationContext(),
							getResources().getString(R.string.regedit_phone_change)/*"中途你更换了手机号码！请稍后重新获取验证码！"*/, 5).show();
					return;
				}
				/*username = userNameEditText.getText().toString()
							.replace(" ", "").trim();*/
				password = passwordEditText.getText().toString().trim();
				email = "";//emailEditText.getText().toString().replace(" ", "").trim();
				//tel = telEditText.getText().toString().replace(" ", "").trim();//放在获取验证码的时候读取，可以防止输入验证码之后在更换手机号码注册
				rname = "";//rnameEditText.getText().toString().replace(" ", "")
							//.trim();
				username = tel;
				/*if (!OtherManager.isEmail(email) || email.length() <= 0) {
					showDialog(110);
					return;
				}
				if (!password.equals(passwordEditTexta.getText().toString())) {
					passwordEditTexta.setError("两次密码输入不一致");
					return;
				}*/

				if (username.length() <= 0) {
						
					showDialog(112);
					return;
				}
				if (password.length() <= 0) {
					showDialog(111);
					return;
				}
				if (tel.length() <= 10) {
					showDialog(113);
					return;
				}

				if (!checkMobilePhone(tel)) {
					showDialog(113);
					return;
				}

				progressDialog = ProgressDialog.show(FindPassword.this, "",
							"注册中，请稍后...");

				DoLogin();
				
			}
		
		});
		//Rand();
	}

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
		return Pattern.compile("^(13|15|18|17)[0-9]{9}$").matcher(str).matches();
	}

	private class UIHandler extends Handler {
		static final int LOGIN_SUCCESS = 2; // 登录成功
		static final int LOGIN_FAILD = 3; // 登录失败
		static final int USERNAME_IS_EXIST = 4; // 用户名已经存在
		static final int EMAIL_IS_EXIST = 5; // 邮箱已经存在
		static final int TEL_IS_EXIST = 6; // 邮箱已经存在
		static final int RID_IS_EXIST = 7; // 引荐人不存在
		public static final int NET_ERROR = -1;
		public static final int HAVE_USER = 1;
		public static final int DATA_OK = -2;
		protected static final int TIMER_OK = 8;

		@Override
		public void handleMessage(Message msg) {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case HAVE_USER:
				OtherManager.Toast(FindPassword.this, getResources().getString(R.string.login_no_reg));//该号码没有注册，请确认手机号码再试！
				break;
			case TIMER_OK:
				if (timerTim <= 0) {
					btnSendCode.setEnabled(true);
					timerOrder.cancel();
					
					btnSendCode.setText(getResources().getString(R.string.regedit_phone_code_send));
					return;
				}
				btnSendCode.setText(String.valueOf(timerTim) + "...");
				break;
			case DATA_OK:
				nWorkMode = 1;
				//etCode.setText("");
				if(autoSMS == 0){
					//是模版项目直接设置code
					etCode.setText(SMSCode);
				}
				etCode.setHint(getResources().getString(R.string.regedit_phone_code_notice));
				timerOrder = new Timer();
				timerTim = 60;
				btnSendCode.setEnabled(false);
				btnSendCode.setText("60...");
				timerOrder.schedule(new TimerTask() {
					@Override
					public void run() {
						timerTim--;
						Message msg = new Message();
						msg.what = UIHandler.TIMER_OK;
						hander.sendMessage(msg);
					}
				}, 1000L, 1000L);
				/*tvCode.setVisibility(View.GONE);
				TextView tvNotice = (TextView) findViewById(R.id.tv_flush);
				tvNotice.setVisibility(View.GONE);*/
				//LinearLayout ll = (LinearLayout)findViewById(R.id.ll_code);
				//ll.setVisibility(View.VISIBLE);
				//saveButton.setText("注册");
				break;
			case NET_ERROR:
				OtherManager.Toast(FindPassword.this, getResources().getString(R.string.network_or_connection_error)/*"网络错误，请稍后再试"*/);
				break;
			case LOGIN_SUCCESS: {
				// 显示数据列表
			
				Toast.makeText(FindPassword.this, getResources().getString(R.string.login_setp_sucess)/*"重置密码成功，请用新的密码登陆！"*/, 15).show();
				finish();
				return;
			}
			case LOGIN_FAILD: {
				progressDialog.dismiss();

				// 账号或者密码错误，请重新输入
				OtherManager.Toast(FindPassword.this, errMsg);
				return;
			}
			
			}
			return;
		}
	}

	// 通过网络获取数据 获取完成后发送消息
	public void DoLogin() {

		Log.v(TAG, "连接网络获取数据开始");
		nHttpType = 1;
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("newpassword", password.replace(" ", ""));
		localHashMap.put("tel", tel.replace(" ", ""));
		

		localHttpManager = new HttpManager(FindPassword.this, FindPassword.this,
				"Android/setpassword.aspx?", localHashMap, 2, 1);

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

			// 开始解析接收到的json数据
			JSONObject json = null;
			try {
				json = new JSONObject(jsonString);

				if (OtherManager.DEBUG) {
					Log.v(TAG, "new JSONObject");
				}
				if (tag == 0) {
					int state = json.getInt("state");
					if (state == 1) {
						autoSMS  = json.getInt("auto");
						SMSCode = json.getString("pwd");
						message.what = UIHandler.DATA_OK;
						
					} else if (state == -2) {
						message.what = UIHandler.HAVE_USER;
					} else {
						message.what = UIHandler.NET_ERROR;
					}
					progressDialog.dismiss();
				} else {
					state = json.getInt("state");

					if (OtherManager.DEBUG) {
						Log.v(TAG, "连接网络获取数据结束1:" + userid + " username:"
								+ username);
					}
					if (state == 1) {
						
						message.what = UIHandler.LOGIN_SUCCESS;
					}else{
						errMsg = json.getString("msg");
						message.what = UIHandler.LOGIN_FAILD;
					}
					/*switch (state) {
					case 1:
						
						break;
					case -1:
						message.what = UIHandler.LOGIN_FAILD;
						break;
					case -2:
						message.what = UIHandler.USERNAME_IS_EXIST;
						break;
					case -3:
						message.what = UIHandler.TEL_IS_EXIST;
						break;
					case -4:
						message.what = UIHandler.EMAIL_IS_EXIST;
						break;
					case -5:
						message.what = UIHandler.RID_IS_EXIST;
						break;
					default:
						break;
					}*/
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

	private void SendData(String sms) {
		progressDialog = ProgressDialog.show(FindPassword.this, "", "发送中，请稍后...");

		nHttpType = 0;
		String url;
		url = "Android/sendcode.aspx?type=1&tel=" + sms;

		localHttpManager = new HttpManager(FindPassword.this, FindPassword.this, url,
				null, 2, 0);

		localHttpManager.getRequeest();
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog;

		

		if (paramInt == 111) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
			.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.regedit_password_error)/*"密码不能为空"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					FindPassword.this.removeDialog(111);
					FindPassword.this.passwordEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local3).create();
		
			return dialog;
		}

		if (paramInt == 113) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
			.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.regedit_phone_error)/*"请填写正确的手机号码"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					FindPassword.this.removeDialog(113);
					FindPassword.this.telEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local3).create();
		
			return dialog;
		}

		dialog = null;
		return dialog;

	}
}
