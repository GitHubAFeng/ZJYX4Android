package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class ShowNews extends HjActivity implements HttpRequestListener {
	// {"dataid":"0","title":"该公告已经不存在可能已经被删除","content":"该公告已经不存在可能已经被删除"}

	private UIHandler hander;
	private String dialogStr;
	private String dataIdString = "0";
	JSONObject jsonObject = null;
	
	private String TAG = "ShowNews";
	//private int actionTag = 0;// 不同的联网操作使用不同的标志来区分不同的请求返回的数据

	HttpManager localHttpManager = null;
	
	private TextView titleTextView;
	private Button backButton;
	private TextView newsTitleTextView;
	private TextView newsContentTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_news);
		
		
		if (OtherManager.DEBUG) {
			Log.v(TAG, "onCreate");
		}
		
		hander = new UIHandler();
		
		newsTitleTextView = (TextView) findViewById(R.id.show_news_title);
		newsContentTextView = (TextView) findViewById(R.id.show_news_content);
		
		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);
		titleTextView.setText("公告");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);
		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String temp = extras.getString("dataid");
			if (temp != null) {
				dataIdString = temp;
				if (OtherManager.DEBUG) {
					Log.v(TAG, "dataIdString:" + dataIdString);
				}
			}
		}
		
		if (dataIdString.equals("0")) {
			newsTitleTextView.setText(extras.getString("name"));
			newsContentTextView.setText(extras.getString("info"));
		}else{
			dialogStr = "数据加载中...";
			showDialog(322);
			
			getData();
		}
	}
	
	private void getData() {
		//actionTag = 3;
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("dataid", dataIdString);

		localHttpManager = new HttpManager(ShowNews.this, ShowNews.this,
				"/Android/GetNewsDetail.aspx?", localHashMap, 2, 0);

		localHttpManager.getRequeest();
	}
	
	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		Log.v(TAG, "onCreateDialog:" + paramInt);

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);
			return dialog;
		}

		return dialog;
	}
	
	private class UIHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (OtherManager.DEBUG) {
				Log.v(TAG, "handleMessage:" + msg.what);
			}

			switch (msg.what) {
			case 200: {
				//获取数据
				
				try {
					newsTitleTextView.setText(jsonObject.getString("title"));
					newsContentTextView.setText(jsonObject.getString("content"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return;
			}
			}

			// OtherManager.Toast(AddOrder.this, "获取数据失败");
			return;
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		String jsonString = (String) paramObject;
		Message message = new Message();

		removeDialog(322);
		
		if (OtherManager.DEBUG) {
			Log.e(TAG, "jsonString:" + jsonString);
		}
		
		try {
			jsonString = (String) paramObject;
			jsonObject = new JSONObject(jsonString);

			message.what = 200;
			hander.sendMessage(message);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
