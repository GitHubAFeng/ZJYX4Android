package com.ihangjing.ZJYXForAndroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabView extends LinearLayout
{
	private LayoutInflater inflater;
	  private LinearLayout innerView;
	  private ImageView iv;
	  private TextView tv;

	  public TabView(Context paramContext, String paramString, int paramInt, int paramInt1)
	  {
	    super(paramContext);
	    
	    this.inflater = (LayoutInflater)paramContext.getSystemService("layout_inflater");
	   
	    this.innerView = (LinearLayout)this.inflater.inflate(R.layout.main_tab_view, null);
	    
	    this.tv = (TextView)this.innerView.findViewById(R.id.tab_label);
	    
	    this.tv.setText(paramString);
	    //FrameLayout localLinearLayout2 =;
	    addView(this.innerView);
	    this.iv = (ImageView)this.innerView.findViewById(R.id.tab_icon);
	    
	    if (paramInt != 0) {
	    	this.iv.setImageResource(paramInt);
		}
	    
	    if (paramInt1 != 0) {
	    	this.tv.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(paramInt1), null, null);
	    	//this.tv.setBackgroundResource(paramInt1);
		}
	    setOrientation(VERTICAL);
	    setGravity(17);
	    //this.setBackgroundResource(R.drawable.qz_selector_toolbar);
	    //FrameLayout localLinearLayout3 = this.innerView;
	    //LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 100);
	    //this.innerView.setLayoutParams(localLayoutParams);
	    //setWillNotDraw(false);
	  }

	  /*protected void onDraw(Canvas paramCanvas)
	  {
	    if (isPressed())
	      this.tv.setShadowLayer(4.0F, 0.0F, 0.0F, 2130706432);
	    while (true)
	    {
	      super.onDraw(paramCanvas);
	      return;
	      
	    }
	  }*/
}
