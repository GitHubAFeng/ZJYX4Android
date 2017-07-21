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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ihangjing.Model.CityInfo;
import com.ihangjing.Model.CityListModel;
import com.ihangjing.Model.SectionInfo;
import com.ihangjing.Model.SectionListModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.util.HJAppConfig;

/**
 * ѡ������
 * @author Administrator
 *
 */
public class SelSection extends HjActivity {
	private static final String TAG = "citylist";
	ProgressDialog progressDialog = null;
	ListView cityListView = null;

	ListViewAdapter mSchedule = null;

	private SectionListModel hotareaListModel;

	private UIHandler hander = new UIHandler();

	private int pageindex = 1;
	private int total;
	private String mycityid;

	private TextView titleTextView;
	private Button backButton;
	private boolean isReturn;
	private boolean search;

	private void initUI() {
		setContentView(R.layout.j_section);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("ѡ������");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		cityListView = (ListView) SelSection.this.findViewById(R.id.section_listview);
	}
	@Override 
	protected void onResume()
	{
		super.onResume();
		if (app.isReturn) {
			finish();
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		
		Bundle extras = getIntent().getExtras();
		mycityid = extras.getString("cityid");
		isReturn = extras.getBoolean("isReturn");
		search = extras.getBoolean("search");
		
		hotareaListModel = new SectionListModel();
		
		if (OtherManager.DEBUG) {
			Log.v(TAG, "initUI end ");
		}

		// ��������
		progressDialog = ProgressDialog.show(SelSection.this, "",
				"���ݼ����У����Ժ�...");
		progressDialog.setCancelable(true);
		
		// �������� ��ʼ����ҳ��Ϊ1
		getDataFromServer(this, pageindex, true, true);

		// ���listview�ĵ���¼� ����������ϸҳ�� ����ִ����Ӧ�Ĳ���
		cityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Log.v(TAG, "setOnItemClickListener position:" + position);

				if (hotareaListModel == null) {
					return;
				}
				
				// �õ���������ȵ������model
				SectionInfo localShopModel = null;
				
				if ((hotareaListModel.list.size() > 0)
						&& (hotareaListModel.list != null)) {
					ArrayList<SectionInfo> localArrayList = hotareaListModel.list; 
					localShopModel = localArrayList
							.get(position);
				}
				if (isReturn) {
					if (app.sSection == null) {
						app.sSection = new SectionInfo();
					}
					app.useLocation.setAreaID(localShopModel.SectionID);
					app.useLocation.setAreaName(localShopModel.SectionName);
					app.sSection.SectionID = localShopModel.SectionID;
					
					app.sSection.SectionName = localShopModel.SectionName;
				}else{
					if (app.mSection == null) {
						app.mSection = new SectionInfo();
					}
					app.mSection.SectionID = localShopModel.SectionID;
					
					app.mSection.SectionName = localShopModel.SectionName;
					OtherManager.SaveSection(app.mSection, SelSection.this);
					
					
				}
				//app.isReturn = true;
				finish();
				// ��ת����ϸҳ�� ���붩����ż���
				
				
				/*
				Intent intent = new Intent(SelSection.this,  SelCircle.class);
				intent.putExtra("cityid", localShopModel.getCityid());
				intent.putExtra("sectionid", localShopModel.getSectionID());
				intent.putExtra("isReturn", isReturn);
				intent.putExtra("search", search);
				startActivity(intent);*/
				//
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
				
				cityListView.setAdapter(mSchedule);

				if (OtherManager.DEBUG) {
					Log.v(TAG, "handleMessage DO_WITH_DATA ��listview���и���");
				}
				progressDialog.dismiss();
				
				return;
			}
			case DO_WITH_DATA: {
				OtherManager.Toast(SelSection.this, "��ȡ���ݳɹ�");
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

		NetManager localNetManager = NetManager.getInstance(SelSection.this
				.getApplicationContext());

		String isonsString = "";
		String url = HJAppConfig.URL+"/Android/GetSectionByCityid.aspx?id="+mycityid;

		if (OtherManager.DEBUG) {
			Log.v(TAG, "����׼�����:" + url);
		}

		try {
			isonsString = localNetManager.dogetAsString(url);
			JSONObject json = null;

			json = new JSONObject(isonsString);

			JSONArray array = new JSONArray();
			Log.v(TAG, "new JSONObject");

			this.pageindex = 1;//Integer.parseInt(json.getString("page"));
			this.total = 1;//Integer.parseInt(json.getString("total"));

			
			array = json.getJSONArray("SectionList");
			

			hotareaListModel.setPage(pageindex);
			hotareaListModel.setTotal(total);
			
			SectionInfo models;// = new SectionInfo[array.length()];
			models = new SectionInfo();
			models.SectionName = "��������";
			models.SectionID = "0";
			this.hotareaListModel.list.add(models);
			for (int i = 0; i < array.length(); i++) {
				JSONObject model = array.getJSONObject(i);
				models = new SectionInfo(model);
				this.hotareaListModel.list.add(models);
				
				Log.v(TAG, "for i="+i);
			}
			Log.v(TAG, isonsString);
		} catch (Exception e) {
			Log.v(TAG, e.getMessage());
			throw new HttpException(isonsString,
					e);
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
			return SelSection.this.hotareaListModel.list.size();
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
			SectionInfo localSimpleOrderModel;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) SelSection.this
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

			localSimpleOrderModel = SelSection.this.hotareaListModel.list
					.get(position);

			viewHolder.shopnameTextView
					.setText(localSimpleOrderModel.getSectionName());

			return convertView;
		}
	}
}