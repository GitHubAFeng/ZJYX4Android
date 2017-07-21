package com.ihangjing.ZJYXForAndroid;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class ImagePopViewDialog extends Dialog {
	int oWidth;
	int oHeight;

	public ImagePopViewDialog(Context context, Bitmap img) {
		super(context, R.style.dialog_fullscreen);
		// super(context, Android.R.style.Theme);
		setOwnerActivity((Activity) context);
		setContentView(R.layout.image_pop_view_dialog);
		setCanceledOnTouchOutside(true);// 点击非图片区域消失
		
		// WindowManager.LayoutParams lp = this.getWindow().getAttributes();

		LinearLayout rl = (LinearLayout) findViewById(R.id.rl_mage);
		if (img == null) {
			rl.setBackgroundResource(R.drawable.nopic_skill);
		} else {
			WindowManager windowManager = ((Activity) context)
					.getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			int sWidth = (int) (display.getWidth()); // 设置宽度
			int sHeight = (int) (display.getHeight()); // 设置宽度
			int width = img.getWidth();
			int height = img.getHeight();
			float bix = (float) width / (float) height;

			/*
			 * if (width > sWidth) { oWidth = width; oHeight = height; }else{
			 * 
			 * }
			 */
			oWidth = sWidth;
			oHeight = (int) (oWidth / bix);
			if (oHeight > sHeight) {
				oHeight = sHeight;
				oWidth = (int)(oHeight * bix);
			}
			LinearLayout.LayoutParams laParams = new LinearLayout.LayoutParams(
					oWidth, oHeight);
			rl.setLayoutParams(laParams);
			rl.setBackgroundDrawable(new BitmapDrawable(img));
		}
		rl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImagePopViewDialog.this.cancel();
			}
		});
		rl = (LinearLayout) findViewById(R.id.ll_parent);
		rl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImagePopViewDialog.this.cancel();
			}
		});
		/*ScrollView sv = (ScrollView) findViewById(R.id.sv_view);
		sv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImagePopViewDialog.this.cancel();
			}
		});*/
		// TODO Auto-generated constructor stub
	}

}
