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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ihangjing.Model.NewBuildingInfo;
import com.ihangjing.Model.NewBuildingListModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.util.HJAppConfig;

public class SelBuilding extends HjActivity {
	private static final String TAG = "citylist";
	ProgressDialog progressDialog = null;
	ListView cityListView = null;
	ListViewAdapter mSchedule = null;
	private NewBuildingListModel hotareaListModel;
	private UIHandler hander = new UIHandler();
	private int pageindex = 1;
	private int pagesize = 20;
	private int total;
	// ��Ȧ
	private String mycircleid;
	private TextView titleTextView;
	private Button backButton;
	// ���صĿؼ�
	// �Ƿ��һҳ���ݣ�1��ʾ�ǣ�2��ʾ�ǣ�
	private int actionstate = 1;
	private LayoutInflater inflater;
	private View footView;// ҳ�� ���ظ������ݵ�View
	private void initUI() {
		setContentView(R.layout.j_building);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("ѡ��ر�");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		cityListView = (ListView) SelBuilding.this
				.findViewById(R.id.building_listview);
		
		inflater = LayoutInflater.from(SelBuilding.this);
		// �ײ��ļ��ظ����̼ҵ�ҳ��View
		footView = inflater.inflate(R.layout.more_build_view, null);
		cityListView.addFooterView(footView);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();

		Bundle extras = getIntent().getExtras();
		mycircleid = extras.getString("circleid");


		hotareaListModel = new NewBuildingListModel();

		// ��������
		progressDialog = ProgressDialog.show(SelBuilding.this, "",
				"���ݼ����У����Ժ�...");
		progressDialog.setCancelable(true);

		// �������� ��ʼ����ҳ��Ϊ1
		getDataFromServer(this, pageindex, true, true);

		// ���listview�ĵ���¼� ����������ϸҳ�� ����ִ����Ӧ�Ĳ���
		cityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				
				if (hotareaListModel == null) {
					return;
				}

				// �õ���������ȵ������model
				NewBuildingInfo localShopModel = null;

				if ((hotareaListModel.list.size() > 0)
						&& (hotareaListModel.list != null)) {
					ArrayList<NewBuildingInfo> localArrayList = hotareaListModel.list;
					localShopModel = localArrayList.get(position);
				}
				// �����û����ڵر�:
				OtherManager.saveUserLocalInfo(SelBuilding.this, "buildname",
						localShopModel.getName());
				OtherManager.saveUserLocalInfo(SelBuilding.this, "buildid",
						localShopModel.getDataid());
				// ��ת����ϸҳ�� ���붩����ż���
				//Intent intent = new Intent(SelBuilding.this, MainActivity.class);

				//startActivity(intent);
				finish();
			}
		});

		cityListView.setOnScrollListener(listener);
	}

	private class UIHandler extends Handler {
		static final int DO_WITH_DATA = 0; // ������Ϣ����
		static final int SET_AREA_LIST = 1; // �������ݳɹ�
		static final int SET_MORE_AREA_LIST = 2; // ���ظ������ݳɹ�

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_AREA_LIST: {
				// ���ؼ��ؿؼ�
				
				mSchedule = new ListViewAdapter();
				cityListView.setAdapter(mSchedule);
				
				progressDialog.dismiss();
				footView.setVisibility(View.GONE);
				return;
			}
			case SET_MORE_AREA_LIST: {
				// ���ؼ��ؿؼ�
				footView.setVisibility(View.GONE);
				mSchedule.notifyDataSetChanged();

				return;
			}
			case DO_WITH_DATA: {
				OtherManager.Toast(SelBuilding.this, "��ȡ���ݳɹ�");
				return;
			}

			}
		}
	}

	// paramInt ��ҳ��
	private void getDataFromServer(Context paramContext, int paramInt,
			boolean paramBoolean1, boolean paramBoolean2) {

		pageindex = paramInt;

		new LoadDataThread().start();
	}

	private class LoadDataThread extends Thread {
		@Override
		public void run() {
			try {
				GetData();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = actionstate;
			hander.sendMessage(message); // ������Ϣ��Handler
		}
	}

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetData() throws HttpException, JSONException {
		NetManager localNetManager = NetManager.getInstance(SelBuilding.this
				.getApplicationContext());

		String isonsString = "";
		String url = HJAppConfig.URL + "/Android/GetBuildingBycirID.aspx?cid="
				+ mycircleid;
		url += "&pagesize=" + pagesize;
		url += "&pageindex=" + pageindex;

		try {
			isonsString = localNetManager.dogetAsString(url);
			JSONObject json = null;
			json = new JSONObject(isonsString);
			JSONArray array = new JSONArray();

			this.pageindex = Integer.parseInt(json.getString("page"));
			this.total = Integer.parseInt(json.getString("total"));

			
			array = json.getJSONArray("datalist");

			hotareaListModel.setPage(pageindex);
			hotareaListModel.setTotal(total);

			NewBuildingInfo models;// = new NewBuildingInfo[array.length()];

			for (int i = 0; i < array.length(); i++) {
				JSONObject model = array.getJSONObject(i);
				models = new NewBuildingInfo(model);
				this.hotareaListModel.list.add(models);
			}
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
			return SelBuilding.this.hotareaListModel.list.size();
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
			NewBuildingInfo localSimpleOrderModel;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) SelBuilding.this
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

			localSimpleOrderModel = SelBuilding.this.hotareaListModel.list
					.get(position);

			viewHolder.shopnameTextView
					.setText(localSimpleOrderModel.getName());

			return convertView;
		}
	}

	/**
	 * listview��������һҳ��������
	 */
	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (view.getLastVisiblePosition() == view.getCount() - 1) {
				footView.setVisibility(View.VISIBLE);
				// ����������һҳ,�ͻ�ȡ��һҳ����,��ʾ���ؿؼ�
				if (total > pageindex) {
					pageindex = pageindex+1;
					// ��ʾ�Ѿ��ǵ�һҳ��ļ�������
					actionstate = 2;
					new LoadDataThread().start();
					
				}
				else {
					//�����һҳ����
					footView.setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};
}
