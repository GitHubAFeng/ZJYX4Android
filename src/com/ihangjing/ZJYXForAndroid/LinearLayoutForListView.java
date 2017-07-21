package com.ihangjing.ZJYXForAndroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class LinearLayoutForListView extends LinearLayout {
	private BaseAdapter adapter = null;
	private View.OnClickListener onClickListener = null;

	public LinearLayoutForListView(Context paramContext) {
		super(paramContext);
	}

	public LinearLayoutForListView(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		setOrientation(1);
		setBaselineAligned(true);
	}

	public void fillLinearLayout() {
		removeAllViews();
		int i = this.adapter.getCount();
		int j = 0;
		while (true) {
			if (j >= i)
				return;
			View localView = this.adapter.getView(j, null, null);
			View.OnClickListener localOnClickListener = this.onClickListener;
			localView.setOnClickListener(localOnClickListener);
			addView(localView, j);
			j += 1;
		}
	}

	public BaseAdapter getAdpter() {
		return this.adapter;
	}

	public View.OnClickListener getOnClickListener() {
		return this.onClickListener;
	}

	public void setAdpter(BaseAdapter paramBaseAdapter) {
		this.adapter = paramBaseAdapter;
		fillLinearLayout();
	}

	@Override
	public void setOnClickListener(View.OnClickListener paramOnClickListener) {
		this.onClickListener = paramOnClickListener;
	}
}