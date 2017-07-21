package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.OrderListModel;
import com.ihangjing.Model.GiftOrderListModel;
import com.ihangjing.Model.GiftSimpleOrderModel;
import com.ihangjing.Model.SimpleOrderModel;
import com.ihangjing.Model.UserDetail;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

public class GiftOrderList extends HjActivity implements HttpRequestListener {
	ProgressDialog progressDialog = null;
	ListView orderListView = null;

	ListViewAdapter mSchedule = null;
	private UIHandler hander = new UIHandler();
	private Button refreshBtn = null;

	private View footView;// ҳ�� ���ظ������ݵ�View
	private LayoutInflater inflater;
	private TextView more_order_tv;
	private ProgressBar more_order_pb;

	private int pageindex = 1;
	private int total;
	private String State = "-1";
	private String todayString = "0";

	private boolean isMoreOrderViewClick;// ��ֹ����ε�� ͬһ��ҳ�� �����ڵ��һ��ʱ������δ�����û��ٴε��

	private GiftOrderListModel orderListModel;

	// Logcat������Ϣ �Զ���tag
	private static final String TAG = "OrderList";

	// HttpManager localHttpManager = null;

	private RelativeLayout downloadRelativeLayout;
	private Thread thread;
	private HttpManager localHttpManager;
	private Button backButton;

	private void initUI() {
		setContentView(R.layout.gift_order_list);
		backButton = (Button) findViewById(R.id.title_bar_back_btn);
		
		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		downloadRelativeLayout = (RelativeLayout) findViewById(R.id.orderlist_loadingdata_ll);

		orderListModel = new GiftOrderListModel();

		inflater = LayoutInflater.from(GiftOrderList.this);

		// �ײ��ļ��ظ��ඩ����ҳ��View
		footView = inflater.inflate(R.layout.more_order_view, null);

		more_order_pb = (ProgressBar) footView
				.findViewById(R.id.more_order_ProgressBar);
		more_order_tv = (TextView) footView
				.findViewById(R.id.more_order_textview);

		orderListView = (ListView) findViewById(R.id.order_listview);

		orderListView.addFooterView(footView);

		// ��Ҫ:ҳ�Ŷ�ȡ�������ݵĵ������
		OnClickListener oclMoreBt = new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				if (!GiftOrderList.this.isMoreOrderViewClick) {
					Log.v(TAG, "isMoreOrderViewClick is false");
					return;
				}
				if (GiftOrderList.this.orderListModel == null) {
					return;
				}
				if (GiftOrderList.this.orderListModel.getPage() >= GiftOrderList.this.orderListModel
						.getTotal()) {
					return;
				}
				GiftOrderList.this.more_order_pb.setVisibility(0);// ��ʾ
				GiftOrderList.this.more_order_tv.setVisibility(8);

				GiftOrderList.this.isMoreOrderViewClick = false;
				pageindex = GiftOrderList.this.orderListModel.getPage() + 1;

				// ��ȡ��һҳ����
				GiftOrderList.this.isMoreOrderViewClick = false;// ��ֹ����ε��
																	// ͬһ��ҳ��
				GetData();

			}
		};

		this.footView.setOnClickListener(oclMoreBt);

		refreshBtn = (Button) findViewById(R.id.orderlist_btn_refresh);

		Log.e(TAG, "������");

		// ˢ��
		refreshBtn.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {

				// ��յ�ǰ����
				orderListModel.list.clear();// = new OrderListModel();
				downloadRelativeLayout.setVisibility(View.VISIBLE);

				pageindex = 1;

				// �������� ��ʼ����ҳ��Ϊ1
				GetData();
			}
		});

		downloadRelativeLayout.setVisibility(View.VISIBLE);
	}

	public GiftOrderList() {
		this.isMoreOrderViewClick = true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// �ж��û��Ƿ��¼
		UserDetail userDetail = OtherManager.getUserInfo(GiftOrderList.this);

		if (userDetail.userId.equals("0")) {
			Intent localIntent1 = new Intent(GiftOrderList.this, Login.class);
			startActivity(localIntent1);

			finish();
		}

		initUI();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			State = extras.getString("state");
		}
		if (State.equals("70"))// ���ն���
		{
			todayString = "1";
		}

		

		// �������� ��ʼ����ҳ��Ϊ1
		GetData();

		// ���listview�ĵ���¼� ����������ϸҳ�� ����ִ����Ӧ�Ĳ���
		orderListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// if (position == 0)
				// return;
				Log.v(TAG, "setOnItemClickListener position:" + position);

				if (GiftOrderList.this.orderListModel == null) {
					return;
				}

				// �õ���������̼ҵ�model
				GiftSimpleOrderModel localShopModel = GiftOrderList.this.orderListModel.list
						.get(position);
				GiftOrderDetail dialog = new GiftOrderDetail(
						GiftOrderList.this, localShopModel.getOrderid(),
						localShopModel.getSendType(), localShopModel.getType());
				dialog.show();
			}
		});
	}

	private class UIHandler extends Handler {
		static final int SET_ORDER_LIST = 1; // �������ݳɹ�
		static final int SET_ORDER_LIST_ONLY = 2; // �ǵ�һҳ ���ݳɹ�
		public static final int NET_ERROR = -2;
		public static final int NO_DATA = -1;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NO_DATA: {
				Toast.makeText(GiftOrderList.this, getResources().getString(R.string.public_net_no_data)/*"û���������"*/, 5).show();

				downloadRelativeLayout.setVisibility(View.GONE);
				break;
				// progressDialog.dismiss();
			}
			case NET_ERROR: {
				Toast.makeText(GiftOrderList.this, getResources().getString(R.string.public_net_no_data)/*"û���������"*/, 5).show();

				downloadRelativeLayout.setVisibility(View.GONE);
				// progressDialog.dismiss();
			}
				break;
			case SET_ORDER_LIST: {
				// ��ʾ�����б�
				// progressDialog.dismiss();
				downloadRelativeLayout.setVisibility(View.GONE);
				Log.v(TAG, "ShopList.this.progressDialog.cancel()");
				if (GiftOrderList.this.orderListModel != null) {
					Log.v(TAG, "ShopList.this.shopListModel != null");

					// �����ж��Ƿ������Ҫ���ظ���������Ҫ����ʾ"���ظ�������"
					if (GiftOrderList.this.orderListView.getFooterViewsCount() <= 0) {
						GiftOrderList.this.orderListView
								.addFooterView(GiftOrderList.this.footView);
						Log.v(TAG, "shopListView.addFooterView");
					}

					mSchedule = new GiftOrderList.ListViewAdapter();
					orderListView.setAdapter(mSchedule);

					GiftOrderList.this.more_order_pb.setVisibility(8);
					GiftOrderList.this.more_order_tv.setVisibility(0);

					// ÿ��ȡһ��Ҫ����һ��page(ҳ��) ������ظ���ʱ��Ҫʹ��
					int m = GiftOrderList.this.orderListModel.getPage();
					int n = GiftOrderList.this.orderListModel.getTotal();

					// ������ظ�������ʱ�Ƴ����ظ���
					Log.v(TAG,
							"m:"
									+ m
									+ "n:"
									+ n
									+ ","
									+ GiftOrderList.this.orderListView
											.getFooterViewsCount());

					// �����ж��Ƿ������Ҫ���ظ������ݲ���Ҫ���Ƴ�"���ظ�������"
					if ((m >= n)
							&& (GiftOrderList.this.orderListView
									.getFooterViewsCount() > 0)) {
						orderListView
								.removeFooterView(GiftOrderList.this.footView);
						Log.v(TAG, "shopListView.removeFooterView");
					}

					if (GiftOrderList.this.orderListModel.list.size() <= 0) {
						GiftOrderList.this.orderListView.setVisibility(8);
						return;
					}
					GiftOrderList.this.orderListView.setVisibility(0);

					// ���ݶ�ȡ��� �������ݼ��ذ�ť�ظ����Ե��״̬ ��ֹ����ε�� ͬһ��ҳ��
					GiftOrderList.this.isMoreOrderViewClick = true;

					Log.v(TAG, "case SET_SHOP_LIST end");
					return;
				}

				OtherManager.Toast(GiftOrderList.this, getResources().getString(R.string.public_net_or_data_failed)/*"��ȡ����ʧ��"*/);
				return;
			}
			case SET_ORDER_LIST_ONLY: {
				// ��ʾ�����б�
				// progressDialog.dismiss();
				downloadRelativeLayout.setVisibility(View.GONE);
				Log.v(TAG, "ShopList.this.progressDialog.cancel()");
				if (GiftOrderList.this.orderListModel != null) {
					Log.v(TAG, "ShopList.this.shopListModel != null");

					// �����ж��Ƿ������Ҫ���ظ���������Ҫ����ʾ"���ظ�������"
					if (GiftOrderList.this.orderListView.getFooterViewsCount() <= 0) {
						GiftOrderList.this.orderListView
								.addFooterView(GiftOrderList.this.footView);
						Log.v(TAG, "shopListView.addFooterView");
					}

					mSchedule.notifyDataSetChanged();

					GiftOrderList.this.more_order_pb.setVisibility(8);
					GiftOrderList.this.more_order_tv.setVisibility(0);

					// ÿ��ȡһ��Ҫ����һ��page(ҳ��) ������ظ���ʱ��Ҫʹ��
					int m = GiftOrderList.this.orderListModel.getPage();
					int n = GiftOrderList.this.orderListModel.getTotal();

					// ������ظ�������ʱ�Ƴ����ظ���
					Log.v(TAG,
							"m:"
									+ m
									+ "n:"
									+ n
									+ ","
									+ GiftOrderList.this.orderListView
											.getFooterViewsCount());

					// �����ж��Ƿ������Ҫ���ظ������ݲ���Ҫ���Ƴ�"���ظ�������"
					if ((m >= n)
							&& (GiftOrderList.this.orderListView
									.getFooterViewsCount() > 0)) {
						orderListView
								.removeFooterView(GiftOrderList.this.footView);
						Log.v(TAG, "shopListView.removeFooterView");
					}

					if (GiftOrderList.this.orderListModel.list.size() <= 0) {
						GiftOrderList.this.orderListView.setVisibility(8);
						return;
					}
					GiftOrderList.this.orderListView.setVisibility(0);

					// ���ݶ�ȡ��� �������ݼ��ذ�ť�ظ����Ե��״̬ ��ֹ����ε�� ͬһ��ҳ��
					GiftOrderList.this.isMoreOrderViewClick = true;

					Log.v(TAG, "case SET_SHOP_LIST end");
					return;
				}

				OtherManager.Toast(GiftOrderList.this, getResources().getString(R.string.public_net_or_data_failed)/*"��ȡ����ʧ��"*/);
				return;
			}
			}
		}
	}

	@Override
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == 4) {
			// finish();
			if ((paramInt == 4) && (GiftOrderList.this.thread != null)) {
				//thread.stop();
			}
		}
		boolean bool = true;
		bool = super.onKeyDown(paramInt, paramKeyEvent);
		return bool;
	}

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetData() {

		Log.v(TAG, "���������ȡ���ݿ�ʼ");

		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("userid", app.userInfo.userId);
		localHashMap.put("pageindex", String.valueOf(pageindex));
		localHashMap.put("Pagesize", "8");

		localHttpManager = new HttpManager(GiftOrderList.this,
				GiftOrderList.this, "/Android"
						+ "/GetIntegralList.aspx?", localHashMap, 1, 0);
		localHttpManager.getRequeest();

	}

	// �б�����������
	static class ViewHolderEdit {
		TextView giftNmae;
		TextView userPoint;
		TextView addTime;
		TextView typeView;
		int dataidView;
	}

	// �Զ���adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		@Override
		public int getCount() {
			return GiftOrderList.this.orderListModel.list.size();
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
			GiftSimpleOrderModel localSimpleOrderModel;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) GiftOrderList.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.gift_order_list_item,
						null);

				viewHolder = new ViewHolderEdit();

				viewHolder.giftNmae = (TextView) convertView
						.findViewById(R.id.orderitem_g_name);

				viewHolder.userPoint = (TextView) convertView
						.findViewById(R.id.orderitem_point);

				viewHolder.addTime = (TextView) convertView
						.findViewById(R.id.orderitem_time);

				viewHolder.typeView = (TextView) convertView
						.findViewById(R.id.orderitem_type);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderEdit) convertView.getTag();
			}

			localSimpleOrderModel = GiftOrderList.this.orderListModel.list
					.get(position);

			viewHolder.giftNmae.setText(localSimpleOrderModel.getGiftName());
			viewHolder.userPoint.setText(localSimpleOrderModel.getUsePoint());
			viewHolder.addTime.setText(localSimpleOrderModel.getAddtime());
			switch (localSimpleOrderModel.getState()) {
			case 0:
				viewHolder.typeView.setText(getResources().getString(R.string.check_state_0));//"δ���");
				break;
			case 1:
				viewHolder.typeView.setText(getResources().getString(R.string.check_state_1));//"���ͨ��");
				break;
			case 2:
				viewHolder.typeView.setText(getResources().getString(R.string.check_state_2));//"���ʧ��");
				break;
			default:
				viewHolder.typeView.setText(getResources().getString(R.string.check_state_0));//"δ���");
				break;
			}
			return convertView;
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt == 260) {
			try {
				JSONObject json = null;

				json = new JSONObject(String.valueOf(paramObject));

				JSONArray array = new JSONArray();
				Log.v(TAG, "new JSONObject");

				this.pageindex = Integer.parseInt(json.getString("page"));
				this.total = Integer.parseInt(json.getString("total"));

				array = json.getJSONArray("datalist");

				orderListModel.setPage(pageindex);
				orderListModel.setTotal(total);

				GiftSimpleOrderModel models;// / = new
												// SimpleOrderModel[array.length()];
				for (int i = 0; i < array.length(); i++) {
					JSONObject model = array.getJSONObject(i);
					models = new GiftSimpleOrderModel(model);
					this.orderListModel.list.add(models);
				}
				if (pageindex == 1) {
					if (orderListModel.list.size() == 0) {
						message.what = UIHandler.NO_DATA;
					} else {
						message.what = UIHandler.SET_ORDER_LIST;
						if (OtherManager.DEBUG) {
							Log.v(TAG, "hander.sendMessage:SET_ORDER_LIST");
						}
					}
				} else if (pageindex > 1) {
					// ����ǵڶ�ҳ������
					message.what = UIHandler.SET_ORDER_LIST_ONLY;
					if (OtherManager.DEBUG) {
						Log.v(TAG, "hander.sendMessage:SET_ORDER_LIST_ONLY");
					}
				}
			} catch (JSONException jsone) {
				message.what = UIHandler.NET_ERROR;
			}
		} else {
			message.what = UIHandler.NET_ERROR;
		}
		hander.sendMessage(message);
	}
}
