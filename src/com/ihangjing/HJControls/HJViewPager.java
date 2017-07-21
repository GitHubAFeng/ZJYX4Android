package com.ihangjing.HJControls;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class HJViewPager extends ViewPager {
	private boolean isCanScroll = true;  
	  
    public HJViewPager(Context context) {  
        super(context);  
    }  
  
    public HJViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public void setScanScroll(boolean isCanScroll){  
        this.isCanScroll = isCanScroll;  
    }  
    public boolean getScanScroll(){
    	return isCanScroll;
    }
  
    @Override  
    public void scrollTo(int x, int y){  
        if (isCanScroll){  
            super.scrollTo(x, y);  
        } 
    }
}
