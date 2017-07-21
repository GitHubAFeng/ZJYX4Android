package com.ihangjing.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ResizePicture {
	private float new_heigth = 0.0F;
	private float new_width = 0.0F;
	private String picPath = "";

	public ResizePicture(String paramString, float paramFloat1,
			float paramFloat2) {
		this.picPath = paramString;
		this.new_width = paramFloat1;
		this.new_heigth = paramFloat2;
	}

	public Drawable getDrawable() {
		Bitmap localBitmap1 = BitmapFactory.decodeFile(this.picPath);
		if (localBitmap1 == null) {
			return null;
		}

		BitmapDrawable localBitmapDrawable;

		int i = localBitmap1.getWidth();
		int j = localBitmap1.getHeight();
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;

		Bitmap localBitmap2 = Bitmap.createBitmap(i, j, localConfig);

		Canvas localCanvas = new Canvas(localBitmap2);
		Paint localPaint = new Paint();
		int k = localBitmap1.getWidth();
		int m = localBitmap1.getHeight();
		Rect localRect = new Rect(0, 0, k, m);
		RectF localRectF = new RectF(localRect);
		localPaint.setAntiAlias(true);
		localCanvas.drawARGB(0, 0, 0, 0);
		localPaint.setColor(-12434878);
		localCanvas.drawRoundRect(localRectF, 5.0F, 5.0F, localPaint);

		PorterDuff.Mode localMode = PorterDuff.Mode.SRC_IN;
		PorterDuffXfermode localPorterDuffXfermode = new PorterDuffXfermode(
				localMode);
		localPaint.setXfermode(localPorterDuffXfermode);
		localCanvas.drawBitmap(localBitmap1, localRect, localRect, localPaint);
		localBitmapDrawable = new BitmapDrawable(localBitmap2);

		localBitmap1.recycle();

		return localBitmapDrawable;
	}

	public Bitmap resize() {
		Bitmap localBitmap1 = BitmapFactory.decodeFile(this.picPath);
		int i;
		int j;
		Bitmap.Config localConfig;
		
		i = (int) this.new_width;
		j = (int) this.new_heigth;
		localConfig = Bitmap.Config.ARGB_8888;

		Bitmap localBitmap2 = Bitmap.createBitmap(i, j, localConfig);

		int k;
		int m;
		Matrix localMatrix;
		int n;
		k = localBitmap1.getWidth();
		m = localBitmap1.getHeight();
		float f1 = this.new_width;
		float f2 = k;
		float f3 = f1 / f2;
		float f4 = this.new_heigth;
		float f5 = m;
		float f6 = f4 / f5;
		localMatrix = new Matrix();
		localMatrix.postScale(f3, f6);
		n = 0;

		localBitmap2 = Bitmap.createBitmap(localBitmap1, 0, n, k, m,
				localMatrix, true);

		return localBitmap2;

	}
}