package com.ihangjing.ZJYXForAndroid;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ihangjing.common.HjActivity;

public class NoticePopActivity extends HjActivity {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.notice_pop_view);
		
		ImageView iv = (ImageView)findViewById(R.id.im_click);
				iv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		} else {
			return super.onKeyDown(keyCode, paramKeyEvent);
			
		}
	}
}
