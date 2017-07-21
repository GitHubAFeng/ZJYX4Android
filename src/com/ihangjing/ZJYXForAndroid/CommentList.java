package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.CommentListModel;
import com.ihangjing.Model.CommentModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class CommentList extends HjActivity implements HttpRequestListener,
		AdapterView.OnItemClickListener {

	// 1��ʾ��ȡ�̼����ݣ�2��ʾ��ȡ����
	int myaction = 1;
	ProgressDialog progressDialog = null;

	ListViewAdapter mSchedule = null;
	private UIHandler hander;

	// �̼��б�
	private CommentListModel commentListModel;

	// Logcat������Ϣ �Զ���tag
	private static final String TAG = "FsgShopList";

	ListView listView;

	private Button backButton;

	HttpManager localHttpManager = null;
	Thread thread = null;

	// private RelativeLayout downloadLayout;

	// private ProgressBar pb_refreshLocation;

	private String foodid = "0";

	private int indexPage = 1;
	public boolean isMoreShopViewClick = false;

	// �����̼�ͼƬ�б�
	// private HashMap<String, Bitmap> bitMapList;

	private void initUI() {

		this.commentListModel = new CommentListModel();

		hander = new UIHandler();

		setContentView(R.layout.comment_list);

		listView = (ListView) findViewById(R.id.lv_list);
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

		mSchedule = new ListViewAdapter();
		listView.setAdapter(mSchedule);

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			foodid = extras.getString("foodid");
		}
		progressDialog = ProgressDialog.show(CommentList.this, "",
				"���ݼ����У����Ժ�...");
		GetData(indexPage);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// ע��㲥���� ����locationService���͵Ķ�λ��Ϣ

	}

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetData(int page) {

		Log.v(TAG, "���������ȡ���ݿ�ʼ");

		// �������籣������
		HashMap<String, String> localHashMap = new HashMap<String, String>();

		localHashMap.put("pid", foodid);

		localHttpManager = new HttpManager(CommentList.this, CommentList.this,
				"Android/reviewlist.aspx?", localHashMap, 2, 1);

		localHttpManager.postRequest();

		Log.v(TAG, "tt��ȡ��һҳ����");
		myaction = 1;
	}

	private class UIHandler extends Handler {
		static final int SET_SHOP_LIST = 12;
		static final int LIST_DATA_UPDATE = 13;// �б������и���
		static final int SET_SHOP_LIST_ONLY_NOTIFY = 14;// �ڶ�ҳ��ʼ
		static final int NET_ERROR = -2;
		public static final int NO_DATA = -1;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case NO_DATA:
				Toast.makeText(CommentList.this, "�������ۣ�", 10).show();
				break;
			case NET_ERROR:
				Toast.makeText(CommentList.this, "��������ݴ������Ժ����ԣ�", 5).show();
				break;
			case SET_SHOP_LIST:
			case SET_SHOP_LIST_ONLY_NOTIFY: {
				// ���صڶ�ҳʱʹ�ô˶δ��룬�б�ˢ�£����ֵ�ǰλ�ã������б�ˢ����Ҫ���¿�ʼ����
				Log.v(TAG, "case SET_SHOP_LIST_ONLY_NOTIFY");

				mSchedule.notifyDataSetChanged();
				return;
			}

			case 15:
				removeDialog(322);
				showDialog(222);
				break;

			}
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		Message message = new Message();
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		if (paramInt == 260) {

			String jsonString = (String) paramObject;
			if (OtherManager.DEBUG) {
				Log.v(TAG, "0 jsonString:" + jsonString);
			}

			JSONObject json = null;
			try {
				json = new JSONObject(jsonString);

				commentListModel.JSonToBean(json);
				if (commentListModel.list.size() == 0) {
					message.what = UIHandler.NO_DATA;
				}else{
					message.what = UIHandler.SET_SHOP_LIST_ONLY_NOTIFY;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}

			if (OtherManager.DEBUG) {
				Log.v(TAG, "action �������ݽ���");
			}

		} else {
			message.what = UIHandler.NET_ERROR;
		}
		hander.sendMessage(message); // ������Ϣ��Handler

	}

	// �б�����������
	static class ViewHolderEdit {
		TextView tvUserName;
		TextView tvServer;
		TextView tvOut;
		TextView tvFlavo;
		TextView tvValue;
		RatingBar rbPoint;
		public ScrollView svImage;
	}

	// �Զ���adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		ListViewAdapter() {
		}

		@Override
		public int getCount() {
			return CommentList.this.commentListModel.list.size();
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
			CommentModel model;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) CommentList.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater
						.inflate(R.layout.comment_list_item, null);

				viewHolder = new ViewHolderEdit();

				viewHolder.tvUserName = (TextView) convertView
						.findViewById(R.id.tv_username);

				viewHolder.tvServer = (TextView) convertView
						.findViewById(R.id.tv_server);

				viewHolder.tvFlavo = (TextView) convertView
						.findViewById(R.id.tv_taste);
				viewHolder.tvOut = (TextView) convertView
						.findViewById(R.id.tv_out);

				viewHolder.tvValue = (TextView) convertView
						.findViewById(R.id.tv_dis);

				viewHolder.rbPoint = (RatingBar) convertView
						.findViewById(R.id.shopdetai_RatingBar02);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderEdit) convertView.getTag();
			}

			model = CommentList.this.commentListModel.list.get(position);

			if (model != null) {

				viewHolder.tvUserName.setText(model.getUserName());

				viewHolder.tvServer.setText(model.getServerG() + "��");

				viewHolder.tvOut.setText(model.getOutG() + "��");
				viewHolder.tvFlavo.setText(model.getFlavorG() + "��");
				viewHolder.tvValue.setText(model.getValue());

				//viewHolder.rbPoint.setNumStars(model.getPoint());
				viewHolder.rbPoint.setRating(model.getPoint());

			}

			return convertView;
		}
	}

	@Override
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {

		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}