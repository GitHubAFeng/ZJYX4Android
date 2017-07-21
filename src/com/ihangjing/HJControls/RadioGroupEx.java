package com.ihangjing.HJControls;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;


public class RadioGroupEx extends RadioGroup {
	private static final String TAG = "RadioGroupEx";  
	  
    public RadioGroupEx(Context context) {  
        super(context);  
    }  
  
    public RadioGroupEx(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
  
        //����ViewGroup�ķ�����������view  
        measureChildren(widthMeasureSpec, heightMeasureSpec);  
  
        //���Ŀ�  
        int maxWidth = 0;  
        //�ۼƵĸ�  
        int totalHeight = 0;  
  
        //��ǰ��һ�е��ۼ��п�  
        int lineWidth = 0;  
        //��ǰ���е�����и�  
        int maxLineHeight = 0;  
        //���ڼ�¼����ǰ���п���и�  
        int oldHeight;  
        int oldWidth;  
  
        int count = getChildCount();  
        //���� widthMode��heightMode����AT_MOST  
        for (int i = 0; i < count; i++) {  
            View child = getChildAt(i);  
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();  
            //�õ���һ�е����  
            oldHeight = maxLineHeight;  
            //��ǰ�����  
            oldWidth = maxWidth;  
  
            int deltaX = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;  
            if (lineWidth + deltaX + getPaddingLeft() + getPaddingRight() > widthSize) {//�������,height����  
                //��Ŀǰ���Ŀ�ȱȽ�,�õ�������ܼ��ϵ�ǰ��child�Ŀ�,�����õ���oldWidth  
                maxWidth = Math.max(lineWidth, oldWidth);  
                //���ÿ��  
                lineWidth = deltaX;  
                //�ۼӸ߶�  
                totalHeight += oldHeight;  
                //�����и�,��ǰ���View��������һ�У���˵�ǰ����и�Ϊ���child�ĸ߶ȼ���margin  
                maxLineHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;  
                Log.v(TAG, "maxHeight:" + totalHeight + "---" + "maxWidth:" + maxWidth);  
  
            } else {  
                //�����У��ۼӿ��  
                lineWidth += deltaX;  
                //�����У����������  
                int deltaY = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;  
                maxLineHeight = Math.max(maxLineHeight, deltaY);  
            }  
            if (i == count - 1) {  
                //ǰ��û�м�����һ�еĸ㣬��������һ�У���Ҫ�ٵ��������һ�е���ߵ�ֵ  
                totalHeight += maxLineHeight;  
                //�������һ�к�ǰ�������һ�бȽ�  
                maxWidth = Math.max(lineWidth, oldWidth);  
            }  
        }  
  
        //���ϵ�ǰ������paddingֵ  
        maxWidth += getPaddingLeft() + getPaddingRight();  
        totalHeight += getPaddingTop() + getPaddingBottom();  
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : maxWidth,  
                heightMode == MeasureSpec.EXACTLY ? heightSize : totalHeight);  
  
    }  
  
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {  
        int count = getChildCount();  
        //preΪǰ�����е�child����Ӻ��λ��  
        int preLeft = getPaddingLeft();  
        int preTop = getPaddingTop();  
        //��¼ÿһ�е����ֵ  
        int maxHeight = 0;  
        for (int i = 0; i < count; i++) {  
            View child = getChildAt(i);  
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();  
            //r-lΪ��ǰ�����Ŀ�ȡ������view���ۻ���ȴ���������ȣ��ͻ��С�  
            if (preLeft + params.leftMargin + child.getMeasuredWidth() + params.rightMargin + getPaddingRight() > (r - l)) {  
                //����  
                preLeft = getPaddingLeft();  
                //Ҫѡ��child��height������Ϊ����  
                preTop = preTop + maxHeight;  
                maxHeight = getChildAt(i).getMeasuredHeight() + params.topMargin + params.bottomMargin;  
            } else { //������,�������߶�  
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + params.topMargin + params.bottomMargin);  
            }  
            //left����  
            int left = preLeft + params.leftMargin;  
            //top����  
            int top = preTop + params.topMargin;  
            int right = left + child.getMeasuredWidth();  
            int bottom = top + child.getMeasuredHeight();  
            //Ϊ��view����  
            child.layout(left, top, right, bottom);  
            //���㲼�ֽ�����preLeft��ֵ  
            preLeft += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;  
  
        }  
  
    }
}
