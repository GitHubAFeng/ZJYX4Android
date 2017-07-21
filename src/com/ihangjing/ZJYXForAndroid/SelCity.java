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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ihangjing.Model.CityInfo;
import com.ihangjing.Model.CityListModel;
import com.ihangjing.Model.SectionInfo;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;
import com.theme.app.ThemeStateUtil;

/**
 * ѡ�����
 * 
 */
public class SelCity extends HjActivity implements HttpRequestListener{
	private static final String TAG = "citylist";
	ProgressDialog progressDialog = null;
	ListView cityListView = null;

	ListViewAdapter mSchedule = null;

	private CityListModel hotareaListModel;

	private UIHandler hander = new UIHandler();

	private int pageindex = 1;
	private int total;

	private TextView titleTextView;
	private Button backButton;
	private boolean isReturn;
	private boolean isSearch;
	private HttpManager localHttpManager;

	private void initUI() {
		setContentView(R.layout.citylist);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("ѡ�����");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		cityListView = (ListView) SelCity.this
				.findViewById(R.id.city_listview);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		initUI();
		app.isReturn = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isReturn = extras.getBoolean("isReturn");
			isSearch = extras.getBoolean("isSearch");
		}else{
			isReturn = false;
		}
		
		//EasyEatApplication.getInstance().addActivity(this);
		
		hotareaListModel = new CityListModel();
		
		if (OtherManager.DEBUG) {
			Log.v(TAG, "initUI end ");
		}

		// ��������
		progressDialog = ProgressDialog.show(SelCity.this, "",
				"���ݼ����У����Ժ�...");
		progressDialog.setCancelable(true);
		
		// �������� ��ʼ����ҳ��Ϊ1
		GetData();

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
				CityInfo localShopModel = null;
				
				if ((hotareaListModel.list.size() > 0)
						&& (hotareaListModel.list != null)) {
					ArrayList<CityInfo> localArrayList = hotareaListModel.list; 
					localShopModel = localArrayList
							.get(position);
				}
				// ѡ����к�ֱ�ӵ���ҳ����ѡ�����������Ź�����ɱ�����ж���û��ѡ������
				//Intent intent = new Intent(SelCity.this, MainActivity.class);
				OtherManager.saveUserLocalInfo(SelCity.this, "cityidname", localShopModel.getCname());
				OtherManager.saveUserLocalInfo(SelCity.this, "cityid", localShopModel.getCid());
				if (isSearch) {
					app.searchLocation.setCityID(localShopModel.getCid());
					app.searchLocation.setCityLat(0.0);//localShopModel.getLat());
					app.searchLocation.setCityLon(0.0);//localShopModel.getLon());
					app.searchLocation.setCityName(localShopModel.getCname());
				}else{
					app.useLocation.setCityID(localShopModel.getCid());
					app.useLocation.setCityLat(0.0);//localShopModel.getLat());
					app.useLocation.setCityLon(0.0);//localShopModel.getLon());
					app.useLocation.setCityName(localShopModel.getCname());
				}
				if (isReturn) {
					if (app.sSection == null) {
						app.sSection = new SectionInfo();
					}
					app.sSection.cityid = localShopModel.cid;
					app.sSection.cityName = localShopModel.cname;
					
					app.sSection.Parentid = localShopModel.cid;
				}else{
					if (app.mSection == null) {
						app.mSection = new SectionInfo();
					}
					app.mSection.cityid = localShopModel.cid;
					
					app.mSection.Parentid = localShopModel.cid;
					Intent localIntent0 = new Intent(SelCity.this, SelSection.class);
					
					localIntent0.putExtra("cityid", localShopModel.cid);
					localIntent0.putExtra("isReturn", isReturn);
					startActivity(localIntent0);
				}
				
				
				
				finish();
				
				
				//finish();
				//startActivity(intent);
			}
		});
	}
	@Override 
	protected void onResume()
	{
		super.onResume();
		if (app.isReturn) {
			finish();
		}
	}
	private class UIHandler extends Handler {
		static final int DO_WITH_DATA = 0;  // ������Ϣ����
		static final int SET_AREA_LIST = 1; // �������ݳɹ�
		public static final int NET_ERROR = -1;

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
			case NET_ERROR:
				Toast.makeText(SelCity.this, "��������ݴ������Ժ����ԣ�", 15).show();
				break;
			case DO_WITH_DATA: {
				OtherManager.Toast(SelCity.this, "��ȡ���ݳɹ�");
				return;
			}

			}
		}
	}

	
	
	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
		public void GetData(){

			Log.v(TAG, "���������ȡ���ݿ�ʼ");
			
			localHttpManager = new HttpManager(SelCity.this,
					SelCity.this, "/Android/GetCityList.aspx",
					null, 2, 1);

			localHttpManager.getRequeest();

			

			
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
			return SelCity.this.hotareaListModel.list.size();
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
			CityInfo localSimpleOrderModel;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) SelCity.this
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

			localSimpleOrderModel = SelCity.this.hotareaListModel.list
					.get(position);

			viewHolder.shopnameTextView
					.setText(localSimpleOrderModel.getCname());

			return convertView;
		}
	}



	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt == 260) {
			
			try {
				String isonsString = (String)paramObject;
				JSONObject json = null;

				json = new JSONObject(isonsString);
				switch (tag) {
				case 1:
					

					JSONArray array = new JSONArray();
					Log.v(TAG, "new JSONObject");

					this.pageindex = Integer.parseInt(json.getString("page"));
					this.total = Integer.parseInt(json.getString("total"));

					
					array = json.getJSONArray("datalist");
					
					hotareaListModel.setPage(pageindex);
					hotareaListModel.setTotal(total);
					
					CityInfo models;// = new CityInfo[array.length()];
					
					for (int i = 0; i < array.length(); i++) {
						JSONObject model = array.getJSONObject(i);
						models = new CityInfo(model);
						this.hotareaListModel.list.add(models);
						
						Log.v(TAG, "for i="+i);
					}
					
					message.what = UIHandler.SET_AREA_LIST;
					break;

				default:
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				message.what = UIHandler.NET_ERROR;
			}
			
			
		}else{
			message.what = UIHandler.NET_ERROR;
		}
		hander.sendMessage(message); 
	}
}
