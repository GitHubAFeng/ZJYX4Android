package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.FoodInOrderModelForGet;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.OrderListModel;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.Model.SimpleOrderModel;
import com.ihangjing.Model.UserDetail;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

public class OrderList extends HjActivity implements HttpRequestListener{
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
	private String State = "";
	private String todayString = "0";
	private String ordertype = "";

	private boolean isMoreOrderViewClick;// ��ֹ����ε�� ͬһ��ҳ�� �����ڵ��һ��ʱ������δ�����û��ٴε��

	private OrderListModel orderListModel[];
	public int userIndex;
	public int nowIndex;
	protected SimpleOrderModel editOrderModel;
	// Logcat������Ϣ �Զ���tag
	private static final String TAG = "OrderList";

	// HttpManager localHttpManager = null;

	private RelativeLayout downloadRelativeLayout;
	private Thread thread;
	private String phone;
	private boolean isShowLogin = false;
	private HttpManager localHttpManager;
	private boolean isReadData;
	private int orderMode = 1;//1���� 2����

	private void initUI() {
		setContentView(R.layout.orderlist);
	}

	public OrderList() {
		this.isMoreOrderViewClick = true;
	}

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {  
	            Window window = getWindow();  
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS  
	                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);  
	            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  
	                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  
	                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);  
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  
	            window.setStatusBarColor(Color.TRANSPARENT);  
	            window.setNavigationBarColor(Color.TRANSPARENT);  
	        } 


		// �ж��û��Ƿ��¼
		/*UserDetail userDetail = OtherManager.getUserInfo(OrderList.this);

		if (userDetail.userId.equals("0")) {
			Intent localIntent1 = new Intent(OrderList.this, Login.class);
			startActivity(localIntent1);

			finish();
		}*/

		initUI();
		
		Button back = (Button)findViewById(R.id.title_bar_back_btn);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OrderList.this.finish();
			}
		});
		TextView tvTitle = (TextView)findViewById(R.id.textView1);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			State = OtherManager.GetQueryString(extras, "state", "");
			phone = OtherManager.GetQueryString(extras, "phone", "");
			ordertype = OtherManager.GetQueryString(extras, "ordertype", "0");
			orderMode = extras.getInt("orderMode", 1);
		}
		if(orderMode == 2){
			back.setVisibility(View.VISIBLE);
			tvTitle.setText(getResources().getString(R.string.user_center_run_order));
		}
		if (State.equals("70"))// ���ն���
		{
			todayString = "1";
		}
		ordertype = OtherManager.GetQueryString(extras, "ordertype", "0");

		downloadRelativeLayout = (RelativeLayout) findViewById(R.id.orderlist_loadingdata_ll);

		orderListModel = new OrderListModel[2];
		orderListModel[0] = new OrderListModel();
		orderListModel[1] = new OrderListModel();

		inflater = LayoutInflater.from(OrderList.this);

		// �ײ��ļ��ظ��ඩ����ҳ��View
		footView = inflater.inflate(R.layout.more_order_view, null);

		more_order_pb = (ProgressBar) footView
				.findViewById(R.id.more_order_ProgressBar);
		more_order_tv = (TextView) footView
				.findViewById(R.id.more_order_textview);

		orderListView = (ListView) findViewById(R.id.order_listview);

		orderListView.addFooterView(footView);
		orderListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (orderListModel[userIndex].getPage() < orderListModel[userIndex].getTotal() && 
						firstVisibleItem + visibleItemCount >= totalItemCount) {
					GetData(orderListModel[userIndex].getPage() + 1);
				}
			}
		});
		
		mSchedule = new OrderList.ListViewAdapter();
		orderListView.setAdapter(mSchedule);

		// ��Ҫ:ҳ�Ŷ�ȡ�������ݵĵ������
		OnClickListener oclMoreBt = new View.OnClickListener() {
			@Override
			public void onClick(View paramView) {
				if (!OrderList.this.isMoreOrderViewClick) {
					Log.v(TAG, "isMoreOrderViewClick is false");
					return;
				}
				
				if (OrderList.this.orderListModel[userIndex].getPage() >= OrderList.this.orderListModel
						[userIndex].getTotal()) {
					return;
				}
				OrderList.this.more_order_pb.setVisibility(0);// ��ʾ
				OrderList.this.more_order_tv.setVisibility(8);

				OrderList.this.isMoreOrderViewClick = false;
				int k = OrderList.this.orderListModel[userIndex].getPage() + 1;

				// ��ȡ��һҳ����
				OrderList.this.isMoreOrderViewClick = false;// ��ֹ����ε�� ͬһ��ҳ��
				GetData(k);

				Log.v(TAG, "��ȡ��һҳ����,ҳ��:" + k);
			}
		};

		this.footView.setOnClickListener(oclMoreBt);

		refreshBtn = (Button) findViewById(R.id.orderlist_btn_refresh);

		Log.e(TAG, "������");

		// ˢ��
		refreshBtn.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				orderListModel[userIndex].list.clear();
				mSchedule.notifyDataSetChanged();
				// ��յ�ǰ����
				downloadRelativeLayout.setVisibility(View.VISIBLE);

				pageindex = 1;

				// �������� ��ʼ����ҳ��Ϊ1
				GetData(pageindex);
			}
		});

		downloadRelativeLayout.setVisibility(View.VISIBLE);

		// �������� ��ʼ����ҳ��Ϊ1
		

		// ���listview�ĵ���¼� ����������ϸҳ�� ����ִ����Ӧ�Ĳ���
		orderListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// if (position == 0)
				// return;
				Log.v(TAG, "setOnItemClickListener position:" + position);

				if (OrderList.this.orderListModel == null) {
					return;
				}

				// �õ���������̼ҵ�model
				SimpleOrderModel localShopModel = null;
				if ((OrderList.this.orderListModel[userIndex].list.size() > 0)
						&& (OrderList.this.orderListModel[userIndex].list != null)) {
					ArrayList<SimpleOrderModel> localArrayList = OrderList.this.orderListModel[userIndex].list;
					// int i = position - 1;
					localShopModel = localArrayList.get(position);
				}

				// ��ת����ϸҳ�� ���붩����ż���
				Intent intent = new Intent(OrderList.this, AddOrder.class);
				intent.putExtra("orderid", localShopModel.getOrderId());
				intent.putExtra("orderType", localShopModel.getSortType());
				intent.putExtra("orderMode", orderMode);
				startActivity(intent);
			}
		});
	}

	private class UIHandler extends Handler {
		static final int SET_ORDER_LIST_ONLY = 1; // �ǵ�һҳ ���ݳɹ�
		public static final int NET_ERROR = -1;
		public static final int UPDATE_ORDER_SUCCESS = 2;
		public static final int UPDATE_FAILD = 3;
		public static final int BUY_AGAIN = 4;

		@Override
		public void handleMessage(Message msg) {
			if(progressDialog != null){
				progressDialog.dismiss();
				progressDialog = null;
			}
			switch (msg.what) {
			case BUY_AGAIN:
				ShopListItemModel shopModel = new ShopListItemModel();
				shopModel.setId(app.buyFoodList.get(0).getTogoID());
				app.setShop(shopModel);
				Intent localIntent = new Intent(OrderList.this,
						ShopDetail.class);
				startActivity(localIntent);
				break;
			case NET_ERROR:
				Toast.makeText(OrderList.this, getResources().getString(R.string.public_net_or_data_error)/*"��������ݴ������Ժ����ԣ�"*/, 15).show();
				break;
			case UPDATE_FAILD:
				Toast.makeText(OrderList.this, "����ȡ������ʧ�ܣ����Ժ�����", 5).show();
				break;
			case UPDATE_ORDER_SUCCESS:
				editOrderModel.setState(8);
				mSchedule.notifyDataSetChanged();
				Toast.makeText(OrderList.this, "����ȡ�������ɹ�����ȴ���̨��ˣ�", 15).show();
				break;
			case SET_ORDER_LIST_ONLY: {
				// ��ʾ�����б�
				// progressDialog.dismiss();
				downloadRelativeLayout.setVisibility(View.GONE);
				Log.v(TAG, "ShopList.this.progressDialog.cancel()");
				if (OrderList.this.orderListModel != null) {
					Log.v(TAG, "ShopList.this.shopListModel != null");

					

					mSchedule.notifyDataSetChanged();
					userIndex = nowIndex;

					OrderList.this.more_order_pb.setVisibility(8);
					OrderList.this.more_order_tv.setVisibility(0);

					// ÿ��ȡһ��Ҫ����һ��page(ҳ��) ������ظ���ʱ��Ҫʹ��
					int m = OrderList.this.orderListModel[userIndex].getPage();
					int n = OrderList.this.orderListModel[userIndex].getTotal();

					// ������ظ�������ʱ�Ƴ����ظ���
					Log.v(TAG,
							"m:"
									+ m
									+ "n:"
									+ n
									+ ","
									+ OrderList.this.orderListView
											.getFooterViewsCount());

					// �����ж��Ƿ������Ҫ���ظ������ݲ���Ҫ���Ƴ�"���ظ�������"
					if (m >= n) {
						OrderList.this.footView.setVisibility(View.GONE);;
						Log.v(TAG, "shopListView.removeFooterView");
					}else{
						footView.setVisibility(View.VISIBLE);
					}


					// ���ݶ�ȡ��� �������ݼ��ذ�ť�ظ����Ե��״̬ ��ֹ����ε�� ͬһ��ҳ��
					OrderList.this.isMoreOrderViewClick = true;

					Log.v(TAG, "case SET_SHOP_LIST end");
					return;
				}

				OtherManager.Toast(OrderList.this, getResources().getString(R.string.public_net_or_data_failed)/*"��ȡ����ʧ��"*/);
				return;
			}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		if (orderMode != 2 && keyCode == KeyEvent.KEYCODE_BACK) {
			Intent localIntent = new Intent("com.ihangjing.common.tap0");
			app.sendBroadcast(localIntent);

		} else {
			boolean bool = true;
			bool = super.onKeyDown(keyCode, paramKeyEvent);
			return bool;
		}

		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (app.userInfo == null || app.userInfo.userId.equals("0") || app.userInfo.userId.equals("")) {
			
			if (isShowLogin) {
				isShowLogin = false;
				Intent localIntent = new Intent("com.ihangjing.common.tap0");
				app.sendBroadcast(localIntent);
				return;
			}
			isShowLogin  = true;
			Intent localIntent4 = new Intent(OrderList.this,
					Login.class);
			localIntent4.putExtra("isReturn", true);
			OrderList.this.startActivity(localIntent4);
			return;
		}else if(pageindex == 1){
			GetData(pageindex);
		}

	}

	

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetData(int paramInt){
		if (!isReadData) {
			isReadData = true;
			pageindex = paramInt;
			Log.v(TAG, "���������ȡ���ݿ�ʼ");

			// ���ò���
			// http://www.dianyifen.com/AndroidAPI/GetShopListByLoaction.aspx?pageindex=3
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("userid", OtherManager.getUserInfo(OrderList.this).userId);
			localHashMap.put("pageindex", String.valueOf(pageindex));
			localHashMap.put("pagesize", "20");
			localHashMap.put("state", State);
			localHashMap.put("today", todayString);
			localHashMap.put("ordertype", ordertype);//�̼ҷ���
			if (phone != null && phone.length() > 5) {
				localHashMap.put("phone", phone);//�̼ҷ���
			}
			if (orderMode == 1) {
				localHttpManager = new HttpManager(OrderList.this, OrderList.this,
						"Android/GetOrderListByUserId.aspx?", localHashMap, 2, 1);
			}else{
				localHttpManager = new HttpManager(OrderList.this, OrderList.this,
						"Android/ExpressOrderList.aspx?", localHashMap, 2, 1);
			}

			localHttpManager.postRequest();
			

			

			Log.v(TAG, "���������ȡ���ݽ���");
		}
		
	}
	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void CheckBuyAgain(String odid){
				Log.v(TAG, "���������ȡ���ݿ�ʼ");
				progressDialog = ProgressDialog.show(OrderList.this, "",
						"���������У����Ժ�...");
				// ���ò���
				// http://www.dianyifen.com/AndroidAPI/GetShopListByLoaction.aspx?pageindex=3
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				
				localHashMap.put("orderid", odid);//�̼ҷ���
				
				localHttpManager = new HttpManager(OrderList.this, OrderList.this,
						"Android/GetAgainOrder.aspx?", localHashMap, 2, 3);

				localHttpManager.postRequest();
				
			
	}
	public void CancelOrder(){

		Log.v(TAG, "���������ȡ���ݿ�ʼ");
		
		Log.v(TAG, "����׼�����");
		progressDialog = ProgressDialog.show(OrderList.this, "",
				"���������У����Ժ�...");
		
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("orderid", editOrderModel.getOrderid());
		
		localHashMap.put("status", "8");
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(OrderList.this, OrderList.this,
				"APP/Android/UpdateOrderStatus.aspx?", localHashMap, 2, 2);

		localHttpManager.postRequest();

		
	}

	// �б�����������
	static class ViewHolderEdit {
		TextView shopnameTextView;
		TextView addressTextView;
		TextView orderidTextView;
		TextView ordertimeTextView;
		TextView orderstatusImageView;
		int dataidView;
		public TextView tvCancel;
		public RelativeLayout rlShopImg;
		public RelativeLayout rlGotoShop;
		public LinearLayout llFoodList;
		public Button btnBuy;
		public Button btnCommand;
		public LinearLayout llBtn;
		public LinearLayout llParent;
	}
	class FoodList {
		TextView tvName;
		TextView tvCount;
		
	}

	// �Զ���adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;
		

		@Override
		public int getCount() {
			return orderListModel[userIndex].list.size();
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
			SimpleOrderModel localSimpleOrderModel;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) OrderList.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.orderlistitem, null);

				viewHolder = new ViewHolderEdit();
				viewHolder.llParent = (LinearLayout)convertView.findViewById(R.id.ll_parent);
				viewHolder.rlShopImg = (RelativeLayout)convertView.findViewById(R.id.rl_shop_ico);
				if(orderMode == 1){
					viewHolder.llParent.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							int postion = (Integer)v.getTag(R.id.act_list);
							SimpleOrderModel localShopModel = OrderList.this.orderListModel[userIndex].list.get(postion);
							

							// ��ת����ϸҳ�� ���붩����ż���
							Intent intent = new Intent(OrderList.this, AddOrder.class);
							intent.putExtra("orderid", localShopModel.getOrderId());
							intent.putExtra("orderType", localShopModel.getSortType());
							intent.putExtra("orderMode", orderMode);
							startActivity(intent);
						}
					});
					viewHolder.rlGotoShop = (RelativeLayout)convertView.findViewById(R.id.rl_go_to);
					viewHolder.rlGotoShop.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//ȥ�̻�����
							int position = (Integer)v.getTag();
							SimpleOrderModel localSimpleOrderModel = OrderList.this.orderListModel[userIndex].list
									.get(position);
							ShopListItemModel localShopModel = new ShopListItemModel();
							
							localShopModel.setName(localSimpleOrderModel.getShopname());
							localShopModel.setId(Integer.parseInt(localSimpleOrderModel.getTogoid()));
							// TODO:���������¼
							app.setShop(localShopModel);
							Intent localIntent = new Intent(OrderList.this,
									ShopDetail.class);
							startActivity(localIntent);
						}
					});
				}else{
					viewHolder.rlShopImg.setVisibility(View.GONE);
				}
					
				
				viewHolder.llBtn = (LinearLayout)convertView.findViewById(R.id.ll_opt);
				viewHolder.btnBuy = (Button)convertView.findViewById(R.id.btn_buy_again);
				viewHolder.btnBuy.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int index = (Integer)v.getTag();
						SimpleOrderModel localSimpleOrderModel = OrderList.this.orderListModel[userIndex].list
								.get(index);
						CheckBuyAgain(localSimpleOrderModel.getOrderid());
					}
				});
				viewHolder.btnCommand = (Button)convertView.findViewById(R.id.btn_go_command);
				viewHolder.btnCommand.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
				
				viewHolder.llFoodList = (LinearLayout)convertView.findViewById(R.id.ll_food_list);
				
				viewHolder.shopnameTextView = (TextView) convertView
						.findViewById(R.id.orderitem_shopname);

				viewHolder.addressTextView = (TextView) convertView
						.findViewById(R.id.orderitem_address);

				viewHolder.ordertimeTextView = (TextView) convertView
						.findViewById(R.id.orderitem_ordertime);

				viewHolder.orderidTextView = (TextView) convertView
						.findViewById(R.id.orderitem_orderid);

				viewHolder.orderstatusImageView = (TextView) convertView
						.findViewById(R.id.orderIcon);
				viewHolder.tvCancel = (TextView)convertView.findViewById(R.id.tv_cancel);
				viewHolder.tvCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int postion = (Integer)v.getTag();
						editOrderModel = OrderList.this.orderListModel[userIndex].list
								.get(postion);
						AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(
								OrderList.this).setTitle("��ʾ").setMessage("��ȷ��ȡ������");

						DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface paramDialogInterface,
									int paramInt) {

								
								// ����״̬��������
								CancelOrder();
							}
						};

						localBuilder1.setPositiveButton("ȷ��", local3);
						localBuilder1.setNeutralButton("ȡ��", null).create().show();
					}
				});
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderEdit) convertView.getTag();
			}

			localSimpleOrderModel = OrderList.this.orderListModel[userIndex].list
					.get(position);

			viewHolder.shopnameTextView.setText(localSimpleOrderModel
					.getShopName());
			

			viewHolder.addressTextView.setText(getResources().getString(R.string.public_moeny_0) + localSimpleOrderModel.getTotalMoney());

			viewHolder.orderidTextView.setText(getResources().getString(R.string.order_detail_show_detail)/*"������ţ�"*/
					+ localSimpleOrderModel.getOrderId());

			/*viewHolder.orderstatusImageView.setText(localSimpleOrderModel
					.getOrderstr());*/

			viewHolder.ordertimeTextView.setText(localSimpleOrderModel
					.getAddtime());
			if (orderMode == 1) {
				viewHolder.llFoodList.removeAllViews();
				FoodInOrderModelForGet localShopBean1;
				int size = localSimpleOrderModel.list.size();
				if(size > 3)
					size = 3;
				FoodList foodList = new FoodList();
				View foodView;// = inflater.inflate(R.layout.order_list_food_list, null);
				for (int i = 0; i < size; i++) {
					foodView = inflater.inflate(R.layout.order_list_food_list, null);
					localShopBean1 = localSimpleOrderModel.list.get(i);
					foodList.tvCount = (TextView)foodView.findViewById(R.id.tv_count);
					foodList.tvName = (TextView)foodView.findViewById(R.id.textview);
					foodList.tvName.setText(localShopBean1.getName());
					foodList.tvCount.setText("X" + localShopBean1.getCount());
					viewHolder.llFoodList.addView(foodView);
				}
				foodView = inflater.inflate(R.layout.order_list_food_list, null);
				foodList.tvCount = (TextView)foodView.findViewById(R.id.tv_count);
				foodList.tvName = (TextView)foodView.findViewById(R.id.textview);
				foodList.tvName.setText(String.format("��%d����Ʒ��ʵ����%s", localSimpleOrderModel.getFoodCount(), localSimpleOrderModel.getTotalmoney()));
				viewHolder.llFoodList.addView(foodView);
				viewHolder.rlGotoShop.setTag(position);
				
				if(localSimpleOrderModel.getTogoICON().length() > 16)//http://*.*.*/*.*
				{
					viewHolder.rlShopImg.setTag(localSimpleOrderModel.getTogoICON());
					app.mLoadImage.addTask(localSimpleOrderModel.getTogoICON(), viewHolder.rlShopImg, R.drawable.icon);
					
					
				}
				String value = "";
				/*switch (localSimpleOrderModel.getSortType()) {
				case 0:
					value = "����-";
					break;
				case 1:
					value = "Ԥ��-";
					break;
				default:
					break;
				}*/
				// 1�������� 2�µ��ɹ� 3�Ѿ����� 4 �������� 5����ɹ� 0�Ѿ�ȡ��
				viewHolder.llBtn.setVisibility(View.VISIBLE);
				if(localSimpleOrderModel.getState() == 3){
					value += getResources().getString(R.string.order_state_3);//"���������";
					//viewHolder.llBtn.setVisibility(View.VISIBLE);
				}else if(localSimpleOrderModel.getState() == 4){
					value += getResources().getString(R.string.order_state_4);//"����ʧ��");;
					//viewHolder.llBtn.setVisibility(View.VISIBLE);
				}else if(localSimpleOrderModel.getState() == 5){
					value += getResources().getString(R.string.order_state_5);//"����ʧЧ");
					//viewHolder.llBtn.setVisibility(View.VISIBLE);
				}else if(localSimpleOrderModel.getState() == 6){
					value += getResources().getString(R.string.order_state_6);//"����ʧ��");
					//viewHolder.llBtn.setVisibility(View.VISIBLE);
				}else if(localSimpleOrderModel.getState() == 8){
					if(localSimpleOrderModel.getShopCancel() == 1){
						value += "�̼�ͬ���˿�";
					}else if(localSimpleOrderModel.getShopCancel() == 2){
						value += "�̼Ҿܾ��˿�";
					}else{
						value += getResources().getString(R.string.order_state_8);//"����ʧ��");
					}
				}else if(localSimpleOrderModel.getSendstate() == 0 && localSimpleOrderModel.getIsShopSet() == 0){
					value += getResources().getString(R.string.order_state_10);//"�������ύ";
				}else if(localSimpleOrderModel.getSendstate() == 1){
					value += getResources().getString(R.string.order_state_11);//"��ʿ�������̼�";
				}else if(localSimpleOrderModel.getSendstate() == 2){
					value += getResources().getString(R.string.order_state_13);//"��ʿ�������̼�";
				}else if(localSimpleOrderModel.getSendstate() == 3){
					value += getResources().getString(R.string.order_state_12);//"���ʹ�";
				}else if(localSimpleOrderModel.getSendstate() == 4){
					value += getResources().getString(R.string.order_state_13);//"��ʿ��ȡ��";
				}else if(localSimpleOrderModel.getIsShopSet() == 1){
					value += getResources().getString(R.string.order_state_14);//"�̼��ѽӵ�";
				}else if(localSimpleOrderModel.getIsShopSet() == 2){
					value += getResources().getString(R.string.order_state_15);//"�������̼Ҿܾ�";
				}else{
					value += "δ֪";
				}
				viewHolder.orderstatusImageView.setText(value);
			}else{
				// 1�������� 2�µ��ɹ� 3�Ѿ����� 4 �������� 5����ɹ� 0�Ѿ�ȡ��
				switch (localSimpleOrderModel.getState()) {
				case 0:
					viewHolder.orderstatusImageView.setText(getResources().getString(R.string.order_state_0));//"��������");

					break;
				case 1:
					viewHolder.orderstatusImageView.setText(getResources().getString(R.string.order_state_9));//"�ȴ����");

					break;
				case 2:
					viewHolder.orderstatusImageView.setText(getResources().getString(R.string.order_state_7));//"���ͨ��");
					break;
				case 3:
					viewHolder.orderstatusImageView.setText(getResources().getString(R.string.order_state_3));//"����ɹ�");
					break;
				
				case 6:
					viewHolder.orderstatusImageView.setText(getResources().getString(R.string.order_state_6));//"����ʧ��");
					break;
				
				default:
					viewHolder.orderstatusImageView.setText(getResources().getString(R.string.order_state_8));//"δ֪");
					break;
				}
			}
			
			
			viewHolder.tvCancel.setVisibility(View.GONE);
			viewHolder.tvCancel.setTag(position);
			viewHolder.llParent.setTag(R.id.act_list, position);
			viewHolder.btnBuy.setTag(position);
			viewHolder.btnCommand.setTag(position);
			
			/*switch (localSimpleOrderModel.getState()) {
			case 0:
				value += getResources().getString(R.string.order_state_0);//"��������");
				//viewHolder.tvCancel.setVisibility(View.VISIBLE);
				break;
			case 1:
				value += getResources().getString(R.string.order_state_1);//"�ȴ����");
				//viewHolder.tvCancel.setVisibility(View.VISIBLE);
				break;
			case 2:
				value += getResources().getString(R.string.order_state_2);//"���ͨ��");
				//viewHolder.tvCancel.setVisibility(View.VISIBLE);
				break;
			case 3:
				value += getResources().getString(R.string.order_state_3);//"����ɹ�");
				break;
			case 4:
				value += getResources().getString(R.string.order_state_4);//"����ʧ��");
				break;
			case 5:
				value += getResources().getString(R.string.order_state_5);//"����ʧЧ");
				break;
			case 6:
				value += getResources().getString(R.string.order_state_6);//"����ʧ��");
				break;
			case 7:
				value += getResources().getString(R.string.order_state_7);//"�Ѿ�����");
				//viewHolder.tvCancel.setVisibility(View.VISIBLE);
				break;
			default:
				value += getResources().getString(R.string.order_state_8);//"δ֪");
				
				break;
			}*/
			
			return convertView;
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		isReadData = false;
		if (paramInt == 260) {
			String value = (String)paramObject;
			value = value.replace("\\\"", "\"");
			value = value.replace("\"{", "{");
			value = value.replace("}\"", "}");
			JSONObject json = null;

			try {
				json = new JSONObject(value);
			
				switch (tag) {
				case 1:
				{
					JSONArray array = new JSONArray();
					Log.v(TAG, "new JSONObject");
		
					this.pageindex = Integer.parseInt(json.getString("page"));
					this.total = Integer.parseInt(json.getString("total"));
		
					
					array = json.getJSONArray("orderlist");
					nowIndex = userIndex;
					if (pageindex == 1) {
						nowIndex--;
						nowIndex *= -1;
						orderListModel[nowIndex].list.clear();
					}
					orderListModel[nowIndex].setPage(pageindex);
					orderListModel[nowIndex].setTotal(total);
		
					SimpleOrderModel models;// = new SimpleOrderModel[array.length()];
					for (int i = 0; i < array.length(); i++) {
						JSONObject model = array.getJSONObject(i);
						models = new SimpleOrderModel(model, orderMode);
						this.orderListModel[nowIndex].list.add(models);
					}
					msg.what = UIHandler.SET_ORDER_LIST_ONLY;
					break;
				}
				case 2:
				{
					int state = json.getInt("state");
					if (state == 1) {
						msg.what = UIHandler.UPDATE_ORDER_SUCCESS;
					}else{
						msg.what = UIHandler.UPDATE_FAILD;
					}
				}
					break;
				case 3:
				{
					JSONArray array = new JSONArray();
					array = json.getJSONArray("foodlist");
					if(app.buyFoodList == null){
						app.buyFoodList = new ArrayList<FoodModel>();
					}else{
						app.buyFoodList.clear();
					}
					FoodModel model;
					int togoID = json.getInt("TogoId");
					for (int i = 0; i < array.length(); i++) {
						json = array.getJSONObject(i);
						model = new FoodModel();
						model.BuyAgainJson(json, togoID);
						app.buyFoodList.add(model);
					}
					msg.what = UIHandler.BUY_AGAIN;
				}	
					break;
				default:
					break;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what = UIHandler.NET_ERROR;
			}
		}else{
			msg.what = UIHandler.NET_ERROR;
		}
		hander.sendMessage(msg);
	}
}
