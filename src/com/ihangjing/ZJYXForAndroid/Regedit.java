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

public class Regedit extends HjActivity implements HttpRequestListener {
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
	private int state;// �ӿڷ��ص���ֵ 1 �ɹ� ���� ʧ��
	private String userid;// �ӿڷ��ص���ֵ �û����
	private String email;
	private String username;
	private String password;
	private String tel;
	private String rname;// ������
	private String cityid;// ���б��
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
	private int autoSMS = 0;

	// http://xxx/APP/Android/Regedit.aspx?email=zjfxqt@163.com&username=zjfxqt&password=123456
	// ���� state��1 ע��ɹ� -2 �����û��� -3 �����Ѿ����� -1ע��ʧ��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		setContentView(R.layout.regedit);

		hander = new UIHandler();

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText(getResources().getString(R.string.regedit_title)/*"�û�ע��"*/);
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
		cityid = OtherManager.getUserLocal(Regedit.this).cityid;
		

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

				// �ж��Ƿ�����
				// �˺Ų���Ϊ��
				// ���벻��Ϊ��
				//ע�⿪����֤���ʱ�򣬶�ȡ�ֻ�����ĵط�Ϊ������֤�밴ť���棬���Է�ֹ��ȡ����ȷ����֤������޸ĺ��������
				String in;
				in = etCode.getText().toString().toLowerCase();
				if (!in.equals(SMSCode)) {
					Toast.makeText(Regedit.this.getApplicationContext(),
							getResources().getString(R.string.regedit_smscode_error)/*"������֤���������������"*/, 5).show();
					return;
				}
				if (tel.compareTo(telEditText.getText().toString().replace(" ", "")
						.trim()) != 0) {
					Toast.makeText(Regedit.this.getApplicationContext(),
							getResources().getString(R.string.regedit_phone_change)/*"��;��������ֻ����룡���Ժ����»�ȡ��֤�룡"*/, 5).show();
					return;
				}
				
				//ʹ����֤�룬��һ��ע�͵�
				/*tel = telEditText.getText().toString().replace(" ", "")
						.trim();
				username = userNameEditText.getText().toString()
							.replace(" ", "").trim();*/
				password = passwordEditText.getText().toString().trim();
				/*email = emailEditText.getText().toString().replace(" ", "").trim();
				
				rname = rnameEditText.getText().toString().replace(" ", "")
							.trim();
					//username = tel;
				if (!OtherManager.isEmail(email) || email.length() <= 0) {
					showDialog(110);
					return;
				}*/
				if (password.length() <= 0) {
					showDialog(111);
					return;
				}
				if (!password.equals(passwordEditTexta.getText().toString())) {
					passwordEditTexta.setError(getResources().getString(R.string.regedit_password_two)/*"�����������벻һ��"*/);
					return;
				}

				/*if (username.length() <= 0) {
						
					showDialog(112);
					return;
				}
				
				if (tel.length() <= 10) {
					showDialog(113);
					return;
				}*/

				if (!checkMobilePhone(tel)) {
					showDialog(113);
					return;
				}

				progressDialog = ProgressDialog.show(Regedit.this, "",
						getResources().getString(R.string.regedit_notice)/*"ע���У����Ժ�..."*/);

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

	// ����ֻ������Ƿ�淶
	private boolean checkMobilePhone(String paramString) {
		String str = paramString.replace("+86", "");
		return Pattern.compile("^(13|15|18|17)[0-9]{9}$").matcher(str).matches();
	}

	private class UIHandler extends Handler {
		static final int LOGIN_SUCCESS = 2; // ��¼�ɹ�
		static final int LOGIN_FAILD = 3; // ��¼ʧ��
		static final int USERNAME_IS_EXIST = 4; // �û����Ѿ�����
		static final int EMAIL_IS_EXIST = 5; // �����Ѿ�����
		static final int TEL_IS_EXIST = 6; // �����Ѿ�����
		static final int RID_IS_EXIST = 7; // �����˲�����
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
				OtherManager.Toast(Regedit.this, getResources().getString(R.string.regedit_have_phone));//"���ֻ������Ѿ�ע�ᣬ������ֻ���������");
				break;
			case TIMER_OK:
				if (timerTim <= 0) {
					btnSendCode.setEnabled(true);
					timerOrder.cancel();
					
					btnSendCode.setText(getResources().getString(R.string.regedit_phone_code_send)/*"������֤��"*/);
					return;
				}
				btnSendCode.setText(String.valueOf(timerTim) + "...");
				break;
			case DATA_OK:
				nWorkMode = 1;
				//etCode.setText("");
				//if(getString(R.string.app_name).compareTo("�����Ƽ�") == 0)
				if (autoSMS == 0) {
					//��ģ����Ŀֱ������code
					etCode.setText(SMSCode);
				}
				
				etCode.setHint(getResources().getString(R.string.regedit_phone_code_notice)/*"������֤��"*/);
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
				//saveButton.setText("ע��");
				break;
			case NET_ERROR:
				OtherManager.Toast(Regedit.this, getResources().getString(R.string.network_or_connection_error)/*"����������Ժ�����"*/);
				break;
			case LOGIN_SUCCESS: {
				// ��ʾ�����б�
			
				Toast.makeText(Regedit.this, getResources().getString(R.string.regedit_reg_notice)/*"���ڻ�Ա�����޸�֧�����룡"*/, 15).show();
				finish();
				return;
			}
			case LOGIN_FAILD: {
				progressDialog.dismiss();

				// �˺Ż��������������������
				OtherManager.Toast(Regedit.this, errMsg + getResources().getString(R.string.regedit_reg_faild));//"ע��ʧ�ܣ����Ժ�����");
				return;
			}
			case USERNAME_IS_EXIST: {
				progressDialog.dismiss();

				// �˺Ż��������������������
				OtherManager.Toast(Regedit.this, getResources().getString(R.string.regedit_have_user));//"�û����Ѿ�����");
				return;
			}
			case EMAIL_IS_EXIST: {
				progressDialog.dismiss();

				// �˺Ż��������������������
				OtherManager.Toast(Regedit.this, getResources().getString(R.string.regedit_have_email));//"�����Ѿ�����");
				return;
			}
			case TEL_IS_EXIST: {
				progressDialog.dismiss();

				// �˺Ż��������������������
				OtherManager.Toast(Regedit.this, getResources().getString(R.string.regedit_have_phone));//"�ֻ������Ѿ�����");
				return;
			}
			case RID_IS_EXIST: {
				progressDialog.dismiss();

				// �˺Ż��������������������
				OtherManager.Toast(Regedit.this, getResources().getString(R.string.regedit_no_rec));//"�����˲�����");
				return;
			}
			}
			return;
		}
	}

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void DoLogin() {

		Log.v(TAG, "���������ȡ���ݿ�ʼ");
		nHttpType = 1;
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		//localHashMap.put("email", email.replace(" ", ""));
		//localHashMap.put("username", username.replace(" ", ""));
		localHashMap.put("password", password.replace(" ", ""));
		localHashMap.put("tel", tel.replace(" ", ""));
		//localHashMap.put("rname", rname);
		//localHashMap.put("cityid", cityid);
		//localHashMap.put("truename", etRecever.getText().toString());
		//localHashMap.put("address", etAddress.getText().toString());

		localHttpManager = new HttpManager(Regedit.this, Regedit.this,
				"Android/NewRegedit.aspx?", localHashMap, 2, 1);

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

			// ��ʼ�������յ���json����
			JSONObject json = null;
			try {
				json = new JSONObject(jsonString);

				if (OtherManager.DEBUG) {
					Log.v(TAG, "new JSONObject");
				}
				if (tag == 0) {
					int state = json.getInt("state");
					if (state == -1) {
						message.what = UIHandler.HAVE_USER;
					} else if (state == -2) {
						message.what = UIHandler.NET_ERROR;
					} else {
						autoSMS  = json.getInt("auto");
						SMSCode = json.getString("pwd");
						message.what = UIHandler.DATA_OK;
					}
					progressDialog.dismiss();
				} else {
					state = json.getInt("state");

					if (OtherManager.DEBUG) {
						Log.v(TAG, "���������ȡ���ݽ���1:" + userid + " username:"
								+ username);
					}
					switch (state) {
					case 1:
						app.userInfo.userId = json.getString("userid");
						app.userInfo.userName = username;
						OtherManager.saveUserInfo(Regedit.this, app.userInfo);
						message.what = UIHandler.LOGIN_SUCCESS;
						break;
					default:
						message.what = UIHandler.LOGIN_FAILD;
						errMsg = json.getString("msg");
						break;
					}
				}
				

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}
			hander.sendMessage(message); // ������Ϣ��Handler
		}

		if (OtherManager.DEBUG) {
			Log.v(TAG, "action �������ݽ���");
		}
	}

	private void SendData(String sms) {
		progressDialog = ProgressDialog.show(Regedit.this, "", getResources().getString(R.string.regedit_send_code_notice)/*"�����У����Ժ�..."*/);

		nHttpType = 0;
		String url;
		url = "Android/sendcode.aspx?type=0&tel=" + sms;

		localHttpManager = new HttpManager(Regedit.this, Regedit.this, url,
				null, 2, 0);

		localHttpManager.getRequeest();
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog;

		if (paramInt == 110) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"��ʾ"*/).setMessage(getResources().getString(R.string.regedit_email_error)/*"����д��ȷ�ĵ�������"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					Regedit.this.removeDialog(110);
					Regedit.this.emailEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"ȷ��"*/, local3).create();

			return dialog;
		}

		if (paramInt == 112) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"��ʾ"*/).setMessage(getResources().getString(R.string.regedit_name_error)/*"��Ա������Ϊ��"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					Regedit.this.removeDialog(112);
					Regedit.this.userNameEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"ȷ��"*/, local3).create();

			return dialog;
		}

		if (paramInt == 111) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"��ʾ"*/).setMessage(getResources().getString(R.string.regedit_password_error)/*"���벻��Ϊ��"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					Regedit.this.removeDialog(111);
					Regedit.this.passwordEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"ȷ��"*/, local3).create();

			return dialog;
		}

		if (paramInt == 113) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"��ʾ"*/).setMessage(getResources().getString(R.string.regedit_phone_error)/*"����д��ȷ���ֻ�����"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					Regedit.this.removeDialog(113);
					Regedit.this.telEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"ȷ��"*/, local3).create();

			return dialog;
		}

		dialog = null;
		return dialog;

	}
}
