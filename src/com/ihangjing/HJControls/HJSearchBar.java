package com.ihangjing.HJControls;

import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.common.HjActivity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class HJSearchBar extends LinearLayout {
	
	public interface onSearchClickButton{  
        public void onClickButton(String value);  
    }
	
	Context mContext;
	Button btnClear;
	Button btnSearch;
	EditText etSearch;
	private LayoutInflater mInflater;
	onSearchClickButton icallBack = null;
	public void setOnSearchClickButton(onSearchClickButton back){
		icallBack = back;
	}
	
	public HJSearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		mInflater = ((HjActivity) context).getLayoutInflater();
		View view = mInflater.inflate(R.layout.search_edit_view, null);
		
		this.addView(view);
		btnClear = (Button)view.findViewById(R.id.btn_clear);
		btnSearch = (Button)view.findViewById(R.id.btn_search);
		etSearch = (EditText)view.findViewById(R.id.et_search);
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					btnSearch.setVisibility(View.VISIBLE);
				}else{
					btnSearch.setVisibility(View.GONE);
				}
			}
		});
		
		btnClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etSearch.setText("");
			}
		});
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (icallBack != null) {
					icallBack.onClickButton(etSearch.getText().toString());
				}
			}
		});
	}
}
