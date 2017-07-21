package com.ihangjing.ZJYXForAndroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;

public class FsgSearchShop extends HjActivity {

	private TextView keywordTextView;

	private TextView titleTextView;
	private Button backButton;
	private Button searchBtn = null;

	// Logcat调试信息 自定义tag
	private static final String TAG = "FsgSearchShop";

	private void initUI() {
		// 清除edit的焦点
		/*getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/

		keywordTextView = (TextView) findViewById(R.id.searchshop_keyword);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("商家搜索");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(FsgSearchShop.this, MainActivity.class);
				startActivity(intent);

				finish();
			}
		});

		searchBtn = (Button) findViewById(R.id.searchshop_btn_search);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "SearchShop oncreate()");
		
		
		setContentView(R.layout.searchshop);

		initUI();

		searchBtn.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {

				// 判断搜索框是否填写了关键字
				if (TextUtils.isEmpty(keywordTextView.getText())) {
					keywordTextView.setError("请填写商家名称");
					return;
				}

				// 跳转到商家列表页面
				Intent intent = new Intent(FsgSearchShop.this, FsgShopList.class);
				intent.putExtra("shopname", String.valueOf(keywordTextView.getText()));
				
				if(OtherManager.DEBUG)
				{
					Log.v(TAG, String.valueOf(keywordTextView.getText()));
				}
				
				startActivity(intent);
			}

		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == 4) {
			finish();
		}
		boolean bool = true;
		bool = super.onKeyDown(paramInt, paramKeyEvent);
		return bool;
	}
}