package com.ihangjing.HJControls;

import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.common.HjActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HJPopListView extends HJPopViewBase{
	
	
	ListView lv;
	LayoutInflater mInflater;
	
	String[] list;
	
	public HJPopListView(Context context, String[] l, RelativeLayout parent, int wight, int height, int belowid)
	{
		super(context,parent,wight,height,belowid);
		
		list = l;
		mInflater = ((HjActivity) context).getLayoutInflater();
		View view = mInflater.inflate(R.layout.pop_view_list, null);
		
		lv = (ListView)view.findViewById(R.id.lv_data);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				_Close(position);
			}
		});
		
		lv.setAdapter(new ListViewAdapter());
		ImageView iv = (ImageView)view.findViewById(R.id.iv_bottom);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		this.addView(view);
	}
	
	
	
	public class contextView{
		TextView tv;
	}
	public class ListViewAdapter extends BaseAdapter {

		

		ListViewAdapter() {
		}

		@Override
		public int getCount() {
			if (list != null) {
				return list.length;
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		public void toggle(int position) {
			this.notifyDataSetChanged();// date changed and we should refresh
										// the view
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			contextView viewHolder = new contextView();
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.pop_view_list_item, null);
				viewHolder.tv = (TextView)convertView.findViewById(R.id.tv_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (contextView) convertView.getTag();
			}
			viewHolder.tv.setText(list[position]);
			

			return convertView;
		}
	}
	

}
