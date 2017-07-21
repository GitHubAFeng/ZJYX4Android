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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

//APP/Android/CheckUpdate.aspx?version=1
//{"state":"1","url":"http:\/\/www.dianyifen.com\/upload\/app\/fanshigang.apk"}
public class CheckUpdate extends HjActivity implements HttpRequestListener {

	private TextView titleTextView;
	private Button backButton;
	private Button checkButton;
	private static final String TAG = "CheckUpdate";
	private UIHandler hander;
	private String dialogStr;
	HttpManager localHttpManager = null;
	private UpdateManager mUpdateManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkupdate);
		
		
		hander = new UIHandler();

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText(getResources().getString(R.string.check_title));
		
		TextView tvVersion = (TextView)findViewById(R.id.more_check_update_version_tv);
		tvVersion.setText(getResources().getString(R.string.version_name) + app.version);

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				

				finish();
			}
		});

		checkButton = (Button) findViewById(R.id.check_update_btn);

		checkButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// ������ ���޸�������ʾ�Ѿ������°汾��
				// showDialog(dialog_checking);
				// ���и�������Ҫ���س���
				Log.v(TAG, "onClick 1");

				dialogStr = getResources().getString(R.string.check_check);
				showDialog(322);

				HashMap<String, String> localHashMap = new HashMap<String, String>();
				localHashMap.put("version", app.version);

				localHttpManager = new HttpManager(CheckUpdate.this,
						CheckUpdate.this, "Android/CheckUpdate.aspx?",
						localHashMap, 2, 0);

				localHttpManager.getRequeest();

				Log.v(TAG, "onClick 2");
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);

			DialogInterface.OnKeyListener okl = new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface paramDialogInterface,
						int paramInt, KeyEvent paramKeyEvent) {
					// ȡ�����
					if ((paramInt == 4)
							&& (CheckUpdate.this.localHttpManager != null)) {
						CheckUpdate.this.localHttpManager.cancelHttpRequest();
					}
					return false;
				}
			};

			dialog.setOnKeyListener(okl);
			
			return dialog;
		}

		if (paramInt == 323) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)).setMessage(getResources().getString(R.string.check_have_new));
			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					// ��ʼ��������
					DownLoad();
				}
			};

			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok), local3)
					.setNegativeButton(getResources().getString(R.string.public_cancel), null).create();

			return dialog;
		}

		if (paramInt == 333) {
			dialog = null;

			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)).setMessage(getResources().getString(R.string.check_no_new));

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					removeDialog(333);
				}
			};
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_return), local3).create();
		}

		return dialog;
	}
	
	private void DownLoad() {
		CheckUpdate.this.removeDialog(323);
		//dialogStr = "�����������°�ϵͳ...";
		//showDialog(322);
		
		//���������汾�Ƿ���Ҫ����
		mUpdateManager = new UpdateManager(this);
		mUpdateManager.checkUpdateInfo();
	}

	private class UIHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 333: {
				showDialog(333);
				return;
			}
			case 323: {
				showDialog(323);
				return;
			}
			}
			// OtherManager.Toast(AddOrder.this, "��ȡ����ʧ��");
			return;
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {

		// dismissDialog()�����������رնԻ���ģ�removeDialog()�����������Ի����Activity���й����Ƴ���������Ѿ��Ƴ��ĶԻ������½��е���showDialog
		// ����öԻ��򽫽������´�����
		removeDialog(322);
		String jsonString = (String) paramObject;

		Log.v(TAG, jsonString);

		JSONObject json = null;
		String state = "";
		//String url = "";

		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			//url = json.getString("url");
			state = json.getString("state");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �˴�֮ǰ����һ��bug��ֱ�ӵ��� showdialog��322���ǻᱨ���
		// Ŀǰ�޸ĳ�ʹ��message��ʽ������������ ����ԭ����δ����
		Message message = new Message();
		if (state.equals("1")) {
			message.what = 323;
		} else {
			message.what = 333;
		}
		hander.sendMessage(message); // ������Ϣ��Handler

		Log.v("DDDDDDDDDDD:", jsonString);

	}
}