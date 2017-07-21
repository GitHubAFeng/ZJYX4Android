package com.ihangjing.HJControls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class HJGridView extends GridView {//�����ʵ�֣���Ҫ���GridView�ĸ߶�����Ӧwrap_content
	public HJGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	//��дGridView��onMeasure���� ���Խ��GridView�ĸ߶�����Ӧwrap_content
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }

}
