package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ihangjing.Model.HotAreaModel;
import com.ihangjing.Model.HotAreaModelList;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;

//APP/Android/GetAreaSubList.aspx?aid=101000000000

//CopyRight (c) 2009-2012 HangJing Teconology. All Rights Reserved.
//zjf@ihangjing.com
//2012-02-14
//��ȡ�������� ������ ��ͨԷ ...

public class HotAreaSubList extends HjActivity  {
	private static final String TAG = "HotAreaSubList";
	ProgressDialog progressDialog = null;
	ListView areaListView = null;

	ListViewAdapter mSchedule = null;

	private HotAreaModelList hotareaListModel;

	private UIHandler hander = new UIHandler();

	private int pageindex = 1;
	private int total;

	private TextView titleTextView;
	private Button backButton;

	private String AId;
	
	private void initUI() {
		setContentView(R.layout.hotarea_list);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("�ȵ�����");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		areaListView = (ListView) HotAreaSubList.this
				.findViewById(R.id.hotarea_listview);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		
		
		hotareaListModel = new HotAreaModelList();
		
		if (OtherManager.DEBUG) {
			Log.v(TAG, "initUI end ");
		}

		// ��ȡ����������̼ұ��
		Bundle extras = getIntent().getExtras();
	    AId = extras.getString("aid");
		Log.v(TAG, String.valueOf(AId));
		
		// ��������
		progressDialog = ProgressDialog.show(HotAreaSubList.this, "",
				"���ݼ����У����Ժ�...");
		progressDialog.setCancelable(true);
		
		// �������� ��ʼ����ҳ��Ϊ1
		getDataFromServer(this, pageindex, true, true);

		// ���listview�ĵ���¼� ����������ϸҳ�� ����ִ����Ӧ�Ĳ���
		areaListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// if (position == 0) // return;
				Log.v(TAG, "setOnItemClickListener position:" + position);

				if (HotAreaSubList.this.hotareaListModel == null) {
					return;
				}

				// �õ���������ȵ������model
				HotAreaModel localShopModel = null;
				if ((HotAreaSubList.this.hotareaListModel.list.size() > 0)
						&& (HotAreaSubList.this.hotareaListModel.list != null)) {
					ArrayList<HotAreaModel> localArrayList = HotAreaSubList.this.hotareaListModel.list; // int																		// 1;
					localShopModel = localArrayList
							.get(position);
				}

				// ��ת���̼��б�ҳ��
				Intent intent = new Intent(HotAreaSubList.this, FsgShopList.class);
				intent.putExtra("aid", localShopModel.getUnid());//ֱ�Ӵ���unid
				startActivity(intent);
			}
		});
	}

	private class UIHandler extends Handler {
		static final int DO_WITH_DATA = 0;  // ������Ϣ����
		static final int SET_AREA_LIST = 1; // �������ݳɹ�

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_AREA_LIST: {
				
				mSchedule = new ListViewAdapter();
				
				areaListView.setAdapter(mSchedule);

				if (OtherManager.DEBUG) {
					Log.v(TAG, "handleMessage DO_WITH_DATA ��listview���и���");
				}
				progressDialog.dismiss();
				
				return;
			}
			case DO_WITH_DATA: {
				OtherManager.Toast(HotAreaSubList.this, "��ȡ���ݳɹ�");
				return;
			}

			}
		}
	}

	// paramInt ��ҳ��
	private void getDataFromServer(Context paramContext, int paramInt,
			boolean paramBoolean1, boolean paramBoolean2) {

		pageindex = paramInt;
		if (OtherManager.DEBUG) {
			// �����̲߳�����
			Log.v(TAG, "new LoadDataThread().start()");
		}

		new LoadDataThread().start();
	}

	private class LoadDataThread extends Thread {
		@Override
		public void run() {

			Log.v(TAG, "���������������...");

			try {
				GetData();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.v(TAG, "�������ݳɹ�");

			Message message = new Message();
			message.what = UIHandler.SET_AREA_LIST;
			hander.sendMessage(message); // ������Ϣ��Handler
			Log.v(TAG, "hander.sendMessage(SET_ORDER_LIST)");
		}
	}

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetData() throws HttpException, JSONException {

		Log.v(TAG, "���������ȡ���ݿ�ʼ");

		NetManager localNetManager = NetManager.getInstance(HotAreaSubList.this
				.getApplicationContext());

		String isonsString = "";
		String url = "http://www.fanshigang.com/APP/Android/GetAreaSubList.aspx?aid="+AId;

		if (OtherManager.DEBUG) {
			Log.v(TAG, "����׼�����:" + url);
		}

		try {
			isonsString = localNetManager.dogetAsString(url);
			Log.v(TAG, isonsString);
		} catch (Exception e) {
			Log.v(TAG, e.getMessage());
		}

		JSONObject json = null;

		json = new JSONObject(isonsString);

		JSONArray array = new JSONArray();
		Log.v(TAG, "new JSONObject");

		this.pageindex = Integer.parseInt(json.getString("page"));
		this.total = Integer.parseInt(json.getString("total"));

		try {
			array = json.getJSONArray("arealist");
		} catch (JSONException jsone) {
			throw new HttpException(jsone.getMessage() + "::" + isonsString,
					jsone);
		}
		
		hotareaListModel.setPage(pageindex);
		hotareaListModel.setTotal(total);
		
		HotAreaModel[] models = new HotAreaModel[array.length()];
		
		for (int i = 0; i < array.length(); i++) {
			JSONObject model = array.getJSONObject(i);
			models[i] = new HotAreaModel(model);
			this.hotareaListModel.list.add(models[i]);
		}
	}

	// �б�����������
	static class ViewHolderEdit {
		TextView shopnameTextView;
		TextView addressTextView;
		TextView orderidTextView;
		TextView ordertimeTextView;
		ImageView orderstatusImageView;
		int dataidView;
	}

	// �Զ���adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		@Override
		public int getCount() {
			return HotAreaSubList.this.hotareaListModel.list.size();
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
			HotAreaModel localSimpleOrderModel;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) HotAreaSubList.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.textview, null);

				viewHolder = new ViewHolderEdit();

				viewHolder.shopnameTextView = (TextView) convertView
						.findViewById(R.id.textview);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderEdit) convertView.getTag();
			}

			localSimpleOrderModel = HotAreaSubList.this.hotareaListModel.list
					.get(position);

			viewHolder.shopnameTextView
					.setText(localSimpleOrderModel.getName());

			return convertView;
		}
	}
}
