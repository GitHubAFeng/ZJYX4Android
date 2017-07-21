package com.ihangjing.ZJYXForAndroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;

public class RegeditAgreement extends HjActivity {

	private TextView titleTextView;
	private Button backButton;
	private TextView regeditAggreementTextView; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regedit_agreement);
		
		
		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("饭是钢注册协议");

		regeditAggreementTextView = (TextView) findViewById(R.id.regedit_agreement);
		regeditAggreementTextView.setText("饭是钢注册协议...");
		
		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				intent = new Intent(RegeditAgreement.this, Regedit.class);
				startActivity(intent);

				finish();
			}
		});
	}
}
