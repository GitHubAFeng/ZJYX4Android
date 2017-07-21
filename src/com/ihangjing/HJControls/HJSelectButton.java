package com.ihangjing.HJControls;


import com.ihangjing.ZJYXForAndroid.R;

import android.R.integer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class HJSelectButton extends TextView {
	int type = 0;//
	private Drawable mArrow_up = getResources().getDrawable(
			R.drawable.arrow_asc);
	private Drawable mTransparent = getResources().getDrawable(
			R.drawable.icon_transparent);
	private Drawable mArrow_dow = getResources().getDrawable(
			R.drawable.arrow_desc);
	private Drawable mArrow;
	public HJSelectButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mArrow = getCompoundDrawables()[0];
		// TODO Auto-generated constructor stub
	}
	public void setUpDowDraw(int up, int dow)
	{
		mArrow_up = getResources().getDrawable(
				up);
		mArrow_dow = getResources().getDrawable(
				dow);
	}
	@Override
	public void setSelected(boolean selected) {
		
		if (selected) {
			if (type != 1) {
				type = 1;
				setCompoundDrawablesWithIntrinsicBounds(mArrow, null,
						mArrow_dow, null);
				
			}else{
				type = 2;
				setCompoundDrawablesWithIntrinsicBounds(mArrow, null,
						mArrow_up, null);
			}
			//setSelected(true);
		}else{
			type = 0;
			//setSelected(false);
			setCompoundDrawablesWithIntrinsicBounds(mArrow, null,
					mTransparent, null);
		}
	}
	public int getType(){
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
