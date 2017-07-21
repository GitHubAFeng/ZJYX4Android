package com.ihangjing.ZJYXForAndroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.GiftTypeListModel;
import com.ihangjing.Model.SectionListModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class GiftTypeList extends Dialog implements HttpRequestListener {

	private static final String TAG = "ShopTypeList";
	ProgressDialog progressDialog = null;
	ListView areaListView = null;

	ListViewAdapter mSchedule = null;

	private UIHandler hander = new UIHandler();

	// private int pageindex = 1;

	private TextView titleTextView;
	private Button backButton;
	private int action = 1;// 1表示获取一级区域，2表示获取二级区域，建筑物

	private HttpManager localHttpManager;
	boolean isScroll = false;
	GiftTypeListModel sectionListModel;
	Context context;
	GetID getID;
	int pIndex = 0;// 父类当前选中

	public GiftTypeList(Context context, GiftTypeListModel se, GetID getID) {
		super(context, R.style.no_title_dialog);
		sectionListModel = se;
		if (sectionListModel == null) {
			sectionListModel = new GiftTypeListModel();
		}
		this.context = context;
		this.getID = getID;
		// TODO Auto-generated constructor stub
	}

	public interface GetID {
		public void getID(String pID, String cID);
	}

	private void initUI() {
		setContentView(R.layout.area_build);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("礼品分类");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);
		Button btnOK = (Button) findViewById(R.id.title_bar_right_btn);
		btnOK.setVisibility(View.GONE);
		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (action == 1) {
					cancel();

				} else {

					action = 1;
					mSchedule.notifyDataSetChanged();
				}
			}
		});

		areaListView = (ListView) GiftTypeList.this
				.findViewById(R.id.shoptype_listview);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();

		// 加载数据 开始加载页码为1
		// sectionList = new SectionListModel();
		// 添加listview的点击事件 点击后进入详细页面 或者执行相应的操作
		areaListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (action == 1) {
					if(position == 0){
						if (getID != null) {
							getID.getID("0", "0");
						}
						cancel();
					}else{
						action = 2;
						mSchedule.notifyDataSetChanged();
						pIndex = position;
					}
				} else {
					if (getID != null) {
						getID.getID(sectionListModel.list.get(pIndex)
								.getSectionId(), sectionListModel.list
								.get(pIndex).secList.get(position)
								.getSectionId());
					}
					cancel();
				}

			}
		});
		areaListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (!isScroll
						&& totalItemCount != 0
						&& totalItemCount == firstVisibleItem
								+ visibleItemCount) {
					isScroll = true;
					if (action == 1) {

					} else {

					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

		});
		mSchedule = new ListViewAdapter();
		areaListView.setAdapter(mSchedule);
		if(sectionListModel.list.size() == 0){
			getAreaData(1);
		}
	}

	private class UIHandler extends Handler {
		public static final int NET_ERROR = -1;
		public static final int NO_DATA = -2;
		public static final int GET_LIST = 1;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_LIST: {
				mSchedule.notifyDataSetChanged();
				// areaListView.setAdapter(mSchedule);

				if (OtherManager.DEBUG) {
					Log.v(TAG, "handleMessage DO_WITH_DATA 对listview进行更新");
				}

				return;
			}

			case NO_DATA:
				Toast.makeText(context, "无相关数据", 5).show();
				break;

			case NET_ERROR:
				Toast.makeText(context, "网络或数据出错无法获取数据", 5).show();
				break;
			}
		}
	}

	// paramInt 是页码
	private void getAreaData(int index) {

		action = 1;
		String url;

		url = "/Android/GetGiftClassList.aspx";
		localHttpManager = new HttpManager(context, GiftTypeList.this, url,
				null, 2, 1);
		localHttpManager.getRequeest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			String strJson = (String) paramObject;
			JSONObject json = null;
			try {
				json = new JSONObject(strJson);
				// JSONArray array;
				// //array = json.getJSONArray("datalist");
				msg.what = UIHandler.GET_LIST;

				sectionListModel.page = json.getInt("page");
				sectionListModel.total = json.getInt("total");
				sectionListModel.stringToBean(json);
				if (sectionListModel.list.size() == 0) {
					msg.what = UIHandler.NO_DATA;
				}

			} catch (JSONException e) {
				msg.what = UIHandler.NET_ERROR;
			}
		} else {
			msg.what = UIHandler.NET_ERROR;
		}
		isScroll = false;
		hander.sendMessage(msg);
	}

	// 列表项包含的组件
	static class ViewHolderEdit {
		TextView shopnameTextView;
	}

	// 自定义adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		@Override
		public int getCount() {
			if (action == 1) {
				return sectionListModel.list.size();
			} else {
				//if()
				return sectionListModel.list.get(pIndex).secList.size();
			}
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

			ViewHolderEdit viewHolder;

			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) context
							.getSystemService("layout_inflater");
				}
				convertView = inflater.inflate(R.layout.textview, null);

				viewHolder = new ViewHolderEdit();

				viewHolder.shopnameTextView = (TextView) convertView
						.findViewById(R.id.textview);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderEdit) convertView.getTag();
			}

			if (action == 1) {
				viewHolder.shopnameTextView.setText(sectionListModel.list.get(
						position).getName());
			} else {
				viewHolder.shopnameTextView.setText(sectionListModel.list.get(pIndex).secList.get(
						position).getName());
			}
			return convertView;
		}
	}

}
