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
import android.widget.Toast;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class ResetPassword extends HjActivity implements 
		HttpRequestListener {

	private static final String TAG = "ResetPassword";

	private TextView titleTextView;
	private Button backButton;

	private EditText oldpasswordEditText;
	private EditText newpasswordEditText;
	private EditText againpasswordEditText;

	private Button saveButton;

	private UIHandler hander = new UIHandler();

	private String dialogStr;
	private String buttonStr;
	HttpManager localHttpManager = null;
	boolean isPay = false;
	String oldPassword = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.reset_password);
		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null){
			isPay = bundle.getBoolean("pay");
		}

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);
		if(isPay){
			titleTextView.setText(getResources().getString(R.string.rp_title1));//"修改支付密码");
			TextView tView = (TextView)findViewById(R.id.tv_notice);
			tView.setVisibility(View.VISIBLE);
			
		}else{
			titleTextView.setText(getResources().getString(R.string.rp_title));//"修改登陆密码");
		}

		backButton = (Button) findViewById(R.id.title_bar_back_btn);
		
		

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				

				finish();
			}
		});

		oldpasswordEditText = (EditText) findViewById(R.id.resetpassword_old_et);
		newpasswordEditText = (EditText) findViewById(R.id.resetpassword_new_et);
		againpasswordEditText = (EditText) findViewById(R.id.resetpassword_again_et);

		saveButton = (Button) findViewById(R.id.resetpassword_save_button);

		saveButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 保存密码
				Save();
			}
		});
	}

	private class UIHandler extends Handler {
		static final int SET_AREA_LIST = 1; // 下载数据成功
		public static final int NET_ERROR = -1;
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
			case 200: {
				showDialog(200);
				return;
			}
			case NET_ERROR:
				Toast.makeText(ResetPassword.this, getResources().getString(R.string.public_net_or_data_error)/*"数据或网络错误"*/, 5).show();
				break;
			}
		}
	}

	private void Save() {
		// 判断是否填写了
		if (!CheckNull()) {
			return;
		}

		this.dialogStr = getResources().getString(R.string.public_send)/*"保存数据中..."*/;

		showDialog(322);

		// 链接网络保存数据
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("userid",
				OtherManager.getUserInfo(ResetPassword.this).userId);
		localHashMap.put("oldpassword", oldPassword);
		localHashMap.put("newpassword", newpasswordEditText.getText().toString().trim());
		if(isPay){
			localHttpManager = new HttpManager(ResetPassword.this,
					ResetPassword.this, "Android/setpaypwd.aspx?",
					localHashMap, 1, 0);
		}else{
			localHttpManager = new HttpManager(ResetPassword.this,
				ResetPassword.this, "Android/ResetPassword.aspx?",
				localHashMap, 1, 1);
		}
		localHttpManager.postRequest();

	}

	private boolean CheckNull() {
		
		if (oldpasswordEditText.getText().toString().trim().length() < 1) {
			if(isPay){
				oldPassword = "";
			}else{
				showDialog(1);
				return false;
			}
		}
		oldPassword = oldpasswordEditText.getText().toString();
		if (newpasswordEditText.getText().toString().trim().length() < 1) {
			showDialog(2);
			return false;
		}
		
		if (againpasswordEditText.getText().toString().trim().length() < 1) {
			showDialog(3);
			return false;
		}
		
		if (!againpasswordEditText.getText().toString().trim().equals(newpasswordEditText.getText().toString().trim())) {
			showDialog(4);
			return false;
		}
		
		return true;
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {

		String jsonString = (String) paramObject;
		Message message = new Message();
		if(paramInt == 260){
		JSONObject json = null;
		String state = "";
		
		if (OtherManager.DEBUG) {
			Log.e(TAG, "jsonString:" + jsonString);
		}
		
		try {
			json = new JSONObject(jsonString);
			state = json.getString("state");
			if (state.equals("0")) {
				dialogStr = getResources().getString(R.string.rp_o_error);//"旧密码不正确";
				buttonStr = getResources().getString(R.string.public_return);//"返回";
				message.what = 200;
			} else if (state.equals("1")) {
				dialogStr = getResources().getString(R.string.rp_c_sucess);//"修改密码成功";
				buttonStr = getResources().getString(R.string.public_ok);//"确定";
				message.what = 200;
			}
			else if (state.equals("-1")) {
				dialogStr = getResources().getString(R.string.rp_c_failed);//"修改密码失败，请联系我们的客服";
				buttonStr = getResources().getString(R.string.public_return);//"返回";
				message.what = 200;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.what = UIHandler.NET_ERROR;
		}
		}else{
			message.what = UIHandler.NET_ERROR;
		}
		hander.sendMessage(message);
		
		
		removeDialog(322);
		
		// 0旧密码不正确  1修改成功  -1 失败
		// 更新地址
		
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
					.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.rp_o_empt)/*"旧密码不能为空"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					ResetPassword.this.removeDialog(1);
					ResetPassword.this.oldpasswordEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local3).create();

			return dialog;
		}

		if (paramInt == 2) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.rp_n_empt)/*"新密码不能为空"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					ResetPassword.this.removeDialog(2);
					ResetPassword.this.newpasswordEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local3).create();

			return dialog;
		}
		
		if (paramInt == 3 || paramInt == 4) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(getResources().getString(R.string.regedit_password_two)/*"两次输入的密码不同，请重新输入新密码"*/);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
					ResetPassword.this.removeDialog(4);
					ResetPassword.this.againpasswordEditText.requestFocus();
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/, local3).create();

			return dialog;
		}
		
		if (paramInt == 200) {
			dialog = null;

			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/*"提示"*/).setMessage(dialogStr);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					removeDialog(200);
				}
			};
			dialog = localBuilder1.setPositiveButton(buttonStr, local3)
					.create();
		}
		
		return dialog;
	}
}
