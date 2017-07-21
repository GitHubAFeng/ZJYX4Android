package com.ihangjing.ZJYXForAndroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;

public class About extends HjActivity {

	private TextView titleTextView;
	private Button backButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		//添加该Activity到MyApplication对象实例容器中
		//需要结束所有Activity的时候调用exit方法 退出所有的activity
		
		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("关于我们");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				

				finish();
			}
		});
	}
}
