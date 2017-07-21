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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class UserInfo extends HjActivity implements HttpRequestListener {

	private static final String TAG = "UserInfo";

	private TextView titleTextView;
	private Button backButton;

	private EditText truenameEditText;
	private EditText usernameEditText;
	private EditText emailEditText;
	private EditText qqEditText;
	private EditText phoneEditText;

	private String usernameString;
	private String truenameString;
	private String emailString;
	private String qqString;
	private String phoneString;

	private Button saveButton;

	private UIHandler hander = new UIHandler();

	private String dialogStr;
	private String buttonStr;
	HttpManager localHttpManager = null;
	private int op = 1; // 1 获取资料 2更新资料

	private EditText etPoint;

	private String point;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_info);


		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText(getResources().getString(R.string.user_title)/*"个人资料"*/);

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});

		truenameEditText = (EditText) findViewById(R.id.userinfo_truename_et);
		usernameEditText = (EditText) findViewById(R.id.userinfo_username_et);
		emailEditText = (EditText) findViewById(R.id.userinfo_email_et);
		qqEditText = (EditText) findViewById(R.id.userinfo_qq_et);
		phoneEditText = (EditText) findViewById(R.id.userinfo_phone_et);
		etPoint = (EditText)findViewById(R.id.userinfo_point_et);

		saveButton = (Button) findViewById(R.id.userinfo_save_button);

		saveButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 保存密码
				Save();
			}
		});

		// 获取个人资料
		GetUserInfo();
	}

	private class UIHandler extends Handler {
		static final int SET_AREA_LIST = 1; // 下载数据成功

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_AREA_LIST: {

				return;
			}
			case 322: {
				showDialog(322);
				return;
			}
			case 333: {
				app.userInfo.trueName = truenameEditText.getText().toString();
				app.userInfo.QQ = qqEditText.getText().toString();
				app.userInfo.email = emailEditText.getText().toString();
				app.userInfo.phone = phoneEditText.getText().toString();
				OtherManager.saveUserInfo(UserInfo.this, app.userInfo);
				showDialog(333);
				return;
			}
			case 200: {
				SetUserInfo();
				return;
			}
			}
		}
	}

	private void SetUserInfo() {

		truenameEditText.setText(truenameString);
		usernameEditText.setText(usernameString);
		emailEditText.setText(emailString);
		qqEditText.setText(qqString);
		phoneEditText.setText(phoneString);
		etPoint.setText(point);
	}

	private void GetUserInfo() {
		this.dialogStr = getResources().getString(R.string.public_get_data_notice);//"获取数据中...";
		showDialog(322);
		op = 1;

		// 链接网络保存数据
		HashMap<String, String> localHashMap = new HashMap<String, String>();

		localHashMap.put("userid", app.userInfo.userId);

		localHttpManager = new HttpManager(UserInfo.this, UserInfo.this,
				"Android/GetUserInfo.aspx?", localHashMap, 1, 1);
		localHttpManager.postRequest();
	}

	private void Save() {
		op = 2;
		
		// 判断是否填写了
		if (!CheckNull()) {
			return;
		}

		this.dialogStr = getResources().getString(R.string.public_send);//"保存数据中...";
		showDialog(322);

		// 链接网络保存数据
		HashMap<String, String> localHashMap = new HashMap<String, String>();

		localHashMap.put("userid",
				OtherManager.getUserInfo(UserInfo.this).userId);
		localHashMap.put("username", usernameEditText.getText().toString()
				.trim());
		localHashMap.put("truename", truenameEditText.getText().toString()
				.trim());
		localHashMap.put("qq", qqEditText.getText().toString().trim());
		localHashMap.put("email", emailEditText.getText().toString().trim());
		localHashMap.put("phone", phoneEditText.getText().toString().trim());

		localHttpManager = new HttpManager(UserInfo.this, UserInfo.this,
				"Android/SaveUserInfo.aspx?", localHashMap, 1, 2);
		localHttpManager.postRequest();
	}

	private boolean CheckNull() {
		if (usernameEditText.getText().toString().trim().length() < 1) {
			showDialog(1);
			return false;
		}

		return true;
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		String jsonString = (String) paramObject;
		Message message = new Message();
		JSONObject json = null;
		String state = "";

		if (OtherManager.DEBUG) {
			Log.e(TAG, "jsonString:" + jsonString);
		}

		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (tag == 2) {
			try {
				state = json.getString("state");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 0会员名已经被使用 1修改成功 -1 失败
			removeDialog(322);

			// 更新地址
			if (state.equals("0")) {
				dialogStr = "会员名已经被使用，请修改会员名";
				buttonStr = "返回";
				message.what = 333;
				hander.sendMessage(message);
			} else if (state.equals("1")) {
				dialogStr = getResources().getString(R.string.user_edit_sucess)/*"修改成功"*/;
				buttonStr = getResources().getString(R.string.public_ok)/*"确定"*/;
				
				message.what = 333;
				
				hander.sendMessage(message);
			} else if (state.equals("-1")) {
				dialogStr = getResources().getString(R.string.user_edit_failed)/*"修改失败，请联系我们的客服"*/;
				buttonStr = getResources().getString(R.string.public_ok)/*"确定"*/;
				message.what = 333;
				hander.sendMessage(message);
			}
		} else {
			// 获取到个人资料
			try {
				truenameString = json.getString("truename");
				usernameString = json.getString("username");
				qqString = json.getString("qq");
				emailString = json.getString("email");
				phoneString = json.getString("phone");
				//point = json.getString("point");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			removeDialog(322);
			
			message.what = 200;
			hander.sendMessage(message);
		}
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		Log.v(TAG, "onCreateDialog:" + paramInt);

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);
			return dialog;
		}

		if (paramInt == 1) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle("提示").setMessage("会员名不能为空");

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					UserInfo.this.removeDialog(1);
					UserInfo.this.usernameEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton("确定", local3).create();

			return dialog;
		}

		if (paramInt == 333) {
			dialog = null;

			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(dialogStr);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					removeDialog(333);
				}
			};
			dialog = localBuilder1.setPositiveButton(buttonStr, local3)
					.create();
		}

		return dialog;

	}
}
