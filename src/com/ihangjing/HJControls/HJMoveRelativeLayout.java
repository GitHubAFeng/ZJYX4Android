package com.ihangjing.HJControls;

import com.ihangjing.ZJYXForAndroid.R;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class HJMoveRelativeLayout extends RelativeLayout {
	
	private ViewDragHelper mDragHelper;
	//public View mMenuView;
	public View mMoveView;
	boolean isMoveToLeft = true;
	protected int mLeft = 0;
	ViewDragHelper.Callback call = new ViewDragHelper.Callback(){
		@Override
		public boolean tryCaptureView(View arg0, int arg1) {
			// TODO Auto-generated method stub
			return arg0 == mMoveView;
			
		}
		@Override
        public int clampViewPositionHorizontal(View child, int left, int dx)
        {
			
			if (mMaxMoveX >= 0) {
				if (left > mMaxMoveX) {
					return mMaxMoveX;
				}else if (left < 0) {
					return 0;
				}
	            return left;
			}else{
				if (left < mMaxMoveX) {
					mLeft = mMaxMoveX;
					return mMaxMoveX;
				}else if (left > 0) {
					mLeft = 0;
					return 0;
				}
				isMoveToLeft = left > mLeft;
				mLeft = left;
	            return left;
			}
			
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy)
        {
        	if (top > mMaxMoveY) {
				return mMaxMoveY;
			}else if(top < 0){
				return 0;
			}
        	
            return top;
        }
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {//位置花生改变是调用
           /* mTop = top;
            mDragOffset = (float) top / mDragRange;
            rlList.setPivotX(rlList.getWidth());
            rlList.setPivotY(rlList.getHeight());
            rlList.setScaleX(1 - mDragOffset / 2);
            rlList.setScaleY(1 - mDragOffset / 2);
            mDescView.setAlpha(1 - mDragOffset);
            requestLayout();*/
        }
       
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
        	super.onViewReleased(releasedChild, xvel, yvel);
        	
        	if (releasedChild == mMoveView) {
        		if (mMaxMoveX > 0) {
        			if (xvel < 0 || yvel < 0) {
            			mDragHelper.settleCapturedViewAt(0, 0);
    				}else{
    					mDragHelper.settleCapturedViewAt(mMaxMoveX, mMaxMoveY);
    				}
				}else{
					if (!isMoveToLeft) {
						mDragHelper.settleCapturedViewAt(mMaxMoveX, mMaxMoveY);
	        			
					}else{
						mDragHelper.settleCapturedViewAt(0, 0);
					}
				}
        		
        		/*
        		if (xvel < 0) {
        			mDragHelper.settleCapturedViewAt(0, 0);
				}else if(xvel == 0){
					if (yvel < 0) {
	        			mDragHelper.settleCapturedViewAt(0, 0);
					}else if(yvel > 0){
						mDragHelper.settleCapturedViewAt(mMaxMoveX, mMaxMoveY);
					}else{
						mDragHelper.settleCapturedViewAt(0, 0);
					}
				}else{
					mDragHelper.settleCapturedViewAt(mMaxMoveX, mMaxMoveY);
				}
        		*/
        		
        		invalidate();
			}
            //int top = getPaddingTop();
            /*if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                top += mDragRange;
            }   */
            
        }
	};
	private int mMaxMoveX;
	private int mMaxMoveY;
	/* @Override  
	 protected void onFinishInflate() {  
		 mMenuView = findViewById(R.id.ll_menu);  
	     mMoveView = findViewById(R.id.rl_list);  
	 }*/
	public void setMoveView(View view){
		mMoveView = view;
	}
	 public HJMoveRelativeLayout(Context context){
		 super(context);
		 if (isInEditMode()) { return; } 
		 mDragHelper = ViewDragHelper.create(this, 1.0f, call);
	 }
	 public HJMoveRelativeLayout(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			if (isInEditMode()) { return; } 
			mDragHelper = ViewDragHelper.create(this, 1.0f, call);
		}
	 public HJMoveRelativeLayout(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			if (isInEditMode()) { return; } 
			mDragHelper = ViewDragHelper.create(this, 1.0f, call);
		}
	 
	 public void setMoveMaxXY(int x, int y){
		 mMaxMoveX = x;
		 mMaxMoveY = y;
	 }
	 @Override
	 public void computeScroll() {//这个非常重要，如果没有这个mDragHelper.settleCapturedViewAt无效
	     super.computeScroll();
	     
	     if(mDragHelper.continueSettling(true)) {
	        ViewCompat.postInvalidateOnAnimation(this);
	     }
	 }
	 public void moveTo(int finalLeft, int finalTop){
		 if (mMaxMoveX > 0) {
			if (finalLeft > mMaxMoveX) {
				finalLeft = mMaxMoveX;
			}
		}else{
			if (finalLeft < mMaxMoveX) {
				finalLeft = mMaxMoveX;
			}
		}
		if (mMaxMoveY > 0) {
			if (finalTop > mMaxMoveY) {
				finalTop = mMaxMoveY;
			}
		}else{
			if (finalTop < mMaxMoveY) {
				finalTop = mMaxMoveY;
			}
		}
		 if(mDragHelper.smoothSlideViewTo(mMoveView, finalLeft, finalTop))
		 {
			 ViewCompat.postInvalidateOnAnimation(this);
		 }
		 
	 }
	 
	 public void moveToMax(){
		if( mDragHelper.smoothSlideViewTo(mMoveView, mMaxMoveX, mMaxMoveY))
		 {
			 ViewCompat.postInvalidateOnAnimation(this);
		 }
	 }
	 public void moveBack(){
		 if(mDragHelper.smoothSlideViewTo(mMoveView, 0, 0))
		 {
			 ViewCompat.postInvalidateOnAnimation(this);
		 }
	 }
	 @Override  
	 public boolean onTouchEvent(MotionEvent ev) {  
	   mDragHelper.processTouchEvent(ev);
	   return true;  
	 }
	 @Override  
	 public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mDragHelper.shouldInterceptTouchEvent(ev);  
		 
	 }

}
