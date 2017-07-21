package com.ihangjing.ZJYXForAndroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;

public class Disclaimer extends HjActivity {
	
	private TextView titleTextView;
	private Button backButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disclaimer);
		
		
		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);
		
		titleTextView.setText("√‚‘…Í√˜");
		
		backButton = (Button)findViewById(R.id.title_bar_back_btn);
		
		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});
	}
}