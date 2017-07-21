package com.ihangjing.app;

import android.graphics.Bitmap;

import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.ZJYXForAndroid.R;

public interface ImageCache {
	public static Bitmap mDefaultBitmap = ImageManager
			.drawableToBitmap(EasyEatApplication.mContext.getResources()
					.getDrawable(R.drawable.friend_default));

	public Bitmap get(String url);

	public void put(String url, Bitmap bitmap);
}
