package com.ihangjing.ZJYXForAndroid;

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
import android.widget.ListView;
import android.widget.TextView;

import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;

public class ShopTypeList extends HjActivity {
	private static final String TAG = "ShopTypeList";
	ProgressDialog progressDialog = null;
	ListView areaListView = null;

	ListViewAdapter mSchedule = null;

	private UIHandler hander = new UIHandler();

	private int pageindex = 1;

	private TextView titleTextView;
	private Button backButton;

	private void initUI() {
		setContentView(R.layout.shoptypelist);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText("�̼ҷ���");

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		areaListView = (ListView) ShopTypeList.this
				.findViewById(R.id.shoptype_listview);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();


		// �������� ��ʼ����ҳ��Ϊ1
		getDataFromServer(this, pageindex, true, true);

		// ���listview�ĵ���¼� ����������ϸҳ�� ����ִ����Ӧ�Ĳ���
		areaListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = new Intent(ShopTypeList.this, FsgShopList.class);
				intent.putExtra("shoptype", String.valueOf(position));
				startActivity(intent);

			}
		});
	}

	private class UIHandler extends Handler {
		static final int DO_WITH_DATA = 0; // ������Ϣ����
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

				return;
			}
			case DO_WITH_DATA: {
				OtherManager.Toast(ShopTypeList.this, "��ȡ���ݳɹ�");
				return;
			}
			}
		}
	}

	// paramInt ��ҳ��
	private void getDataFromServer(Context paramContext, int paramInt,
			boolean paramBoolean1, boolean paramBoolean2) {

		// ��ʼ������
		Message message = new Message();
		message.what = UIHandler.SET_AREA_LIST;
		hander.sendMessage(message);

		Log.v(TAG, "hander.sendMessage(SET_SHOPTYPE_LIST)");
	}

	// �б�����������
	static class ViewHolderEdit {
		TextView shopnameTextView;
	}

	// �Զ���adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		@Override
		public int getCount() {
			return 5;
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
					inflater = (LayoutInflater) ShopTypeList.this
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

			String typeString = "";
			if (position == 0) {
				typeString = "�Ƽ��̼�";
			} 
			else if (position == 1) {
				typeString = "�����ײ�";
			} else if (position == 2) {
				typeString = "��ʽ����";
			} else if (position == 3) {
				typeString = "�����ζ";
			} else if (position == 4) {
				typeString = "��ƷС��";
			} 
			
			viewHolder.shopnameTextView.setText(typeString);

			return convertView;
		}
	}
}
