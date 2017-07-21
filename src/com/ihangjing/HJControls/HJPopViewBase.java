package com.ihangjing.HJControls;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HJPopViewBase extends LinearLayout {

	RelativeLayout parentLayout;
	int wight;
	int height;
	int belowID;
	protected Context mContext;
	onItemClick icallBack = null;
	public interface onItemClick{  
        public void onClickItem(int position);  
    }
	public void setOnItemClick(onItemClick back){
		icallBack = back;
	}
	public HJPopViewBase(Context context, RelativeLayout parent, int wight, int height, int belowid) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		parentLayout = parent;
		this.wight = wight;
		this.height = height;
		belowID = belowid;
		this.setBackgroundColor(Color.rgb(0, 0, 0));
		this.getBackground().setAlpha(85);
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_Close(-1);
			}
		});
		
	}
	protected void _Close(int position)
	{
		if (icallBack != null) {
			icallBack.onClickItem(position);
		}
		parentLayout.removeView(this);
	}
	public void Close()
	{
		_Close(-1);
	}
	public void Show()
	{
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				wight,
				wight);
		params.addRule(
				RelativeLayout.BELOW,
				belowID);
		
		this.setLayoutParams(params);
		parentLayout.addView(this);
	}

}
