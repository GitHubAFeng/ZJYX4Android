package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.r;
import com.ihangjing.HJControls.HJPopListView;
import com.ihangjing.HJControls.HJPopViewBase.onItemClick;
import com.ihangjing.HJControls.HJSelectButton;
import com.ihangjing.Model.ShopDiscountItemView;
import com.ihangjing.Model.ShopItemView;
import com.ihangjing.Model.MyShopCart;
import com.ihangjing.Model.PopAdvListModel;
import com.ihangjing.Model.PopAdvModel;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.Model.ShopListModel;
import com.ihangjing.Model.ShopTypeInfo;
import com.ihangjing.Model.ShopTypeListModel;
import com.ihangjing.Model.TagModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

public class FsgShopList extends HjActivity implements HttpRequestListener {

	// 1��ʾ��ȡ�̼����ݣ�2��ʾ��ȡ����
	int myaction = 1;
	// ProgressDialog progressDialog = null;

	ListViewAdapter mSchedule = null;
	private UIHandler hander;
	private boolean isActivityRun;
	private boolean isMoreShopViewClick;// ��ֹ����ε�� ͬһ��ҳ�� �����ڵ��һ��ʱ������δ�����û��ٴε��

	// �̼��б�
	private ShopListModel shopListModel[];
	public int userIndex;
	public int nowIndex;

	// private LinearLayout ll_NoData;
	private TextView netErrorTextView;
	private View moreView;// ҳ�� ���ظ������ݵ�View
	private LinearLayout loadView;// ҳ�� ���ظ������ݵ�View
	private LinearLayout errorView;
	private LayoutInflater inflater;
	private TextView more_shop_tv;
	private ProgressBar more_shop_pb;
	// private TextView tvNoData;

	private double lat;
	private double lon;
	private int pageindex;
	private int total;

	// Logcat������Ϣ �Զ���tag
	private static final String TAG = "FsgShopList";

	private static final int GET_SHOP = 1;

	private static final int GET_SHOP_TYPE = 2;

	private static final int SHOW_LOCATION = 313;

	private static final int GET_POP_ADV = 3;
	private static final int CHECK_UPDATE = 4;

	ListView shopListView;

	// �ӿڲ��� location �����ȡ
	private String op = "location";
	private String shopNameString = ""; // �����ؼ���
	private String istuan = "0";// �̼ҷ���
	private String shoptype = "0";// �̼�һ������

	HttpManager localHttpManager = null;
	Thread thread = null;

	// private RelativeLayout downloadLayout;
	private String[] strsSortName;
	private String[] strsOrderBy;
	private String[] strsActivitys;

	/**
	 * gettype =0��ʾ������ =1��ʾ�������
	 */
	int gettype = 0;

	/**
	 * ��������.û����0,�����ݽ��������Ϊ0
	 */
	int bid = 0;
	String dialogStr;

	UpdateReceiver updateReceiver;
	private ArrayList<String> nearlist;
	private LinearLayout llCity;
	private RelativeLayout RlParent;
	protected HJPopListView activitysView;
	protected HJPopListView orderByView;
	protected HJPopListView shopTypeView;
	public boolean isShowShopType = false;
	private String orderByID = "0";
	// �����̼�ͼƬ�б�
	// private HashMap<String, Bitmap> bitMapList;
	private HJSelectButton btnShopType;
	private HJSelectButton btnOrderBy;
	private HJSelectButton btnShopTop;
	private long time1 = 0;
	private TextView tv_myLocation;

	protected String sortname = "SortNum";// �������� SortNum Ĭ�� pop���� InTime����
											// grade���� Distance����

	protected int sortflag = 1;// 0��������1��������

	private int isRem = 0;

	private LinearLayout llBtnOpt;

	private Button btnBack;

	private Timer timer;

	private TimerTask updataViewTask;

	public boolean isPopADV;

	private UpdateManager mUpdateManager;
	
	private ImageView ivDataNotice;

	private TextView tvCheckAgain;

	private TextView tvMessage;
	private boolean isReadData;
	protected int lastPosition = 0;//��¼��һ������ʹ�õ�����һ��

	private int isPromotion = 0;
	private String shoptypeName;

	private int shopType;

	private void initUI() {
		app.userInfo = OtherManager.getUserInfo(this);
		strsOrderBy = new String[5];
		strsOrderBy[0] = getResources().getString(R.string.shop_list_default);//"Ĭ������";
		strsOrderBy[1] = getResources().getString(R.string.shop_list_distance);//����;
		strsOrderBy[2] = getResources().getString(R.string.shop_list_show);//����;
		strsOrderBy[3] = getResources().getString(R.string.shop_list_com);//����;
		strsOrderBy[4] = getResources().getString(R.string.shop_list_new);//������ס;
		/*strsOrderBy[4] = "service";
		strsOrderBy[5] = "speed";
		strsOrderBy[6] = "price";*/
		strsActivitys = new String[3];
		strsActivitys[0] = getResources().getString(R.string.shop_list_all);//"�����̼�";
		strsActivitys[1] = getResources().getString(R.string.shop_list_activity);//�л�̼�;
		strsActivitys[2] = getResources().getString(R.string.shop_list_n_activity);//�޻�̼�;
		//strsActivitys[3] = "����+Ԥ��";
		
		this.isActivityRun = true;
		this.isMoreShopViewClick = true;

		this.shopListModel = new ShopListModel[2];
		shopListModel[0] = new ShopListModel();
		shopListModel[1] = new ShopListModel();

		setContentView(R.layout.fsg_shoplist);
		ivDataNotice = (ImageView)findViewById(R.id.iv_data_notice);
		tvCheckAgain = (TextView)findViewById(R.id.tv_check_again);
		tvMessage = (TextView)findViewById(R.id.message);
		btnBack = (Button) findViewById(R.id.title_bar_back_btn);
		tv_myLocation = (TextView) findViewById(R.id.main_location);
		RlParent = (RelativeLayout) findViewById(R.id.rl_parent);
		llCity = (LinearLayout) findViewById(R.id.LinearLayout04);

		llCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent localIntent0 = new Intent(FsgShopList.this,
						SearchOnMap.class);
				startActivity(localIntent0);
			}
		});
		llBtnOpt = (LinearLayout) findViewById(R.id.ll_btn_opt);
		TextView tvTitle = (TextView)findViewById(R.id.tv_title);
		if (isRem != 0) {
			if (isRem == 1 || isRem == 7) {
				llBtnOpt.setVisibility(View.GONE);
				if (isRem == 7) {
					tvTitle.setVisibility(View.VISIBLE);
					tvTitle.setText(getResources().getString(R.string.user_center_shop));
				}
			}
			llCity.setVisibility(View.GONE);

		}
		/*
		 * HJSearchBar searchBar = (HJSearchBar)findViewById(R.id.search_bar);
		 * searchBar.setOnSearchClickButton(new onSearchClickButton() {
		 * 
		 * @Override public void onClickButton(String value) { // TODO
		 * Auto-generated method stub
		 * 
		 * } });
		 */

		btnOrderBy = (HJSelectButton) findViewById(R.id.btn_orderby);
		if (shoptypeName != null && shoptypeName.length() > 0) {
			btnOrderBy.setText(shoptypeName);
		}
		btnOrderBy.setUpDowDraw(R.drawable.ic_arrow_down, R.drawable.ic_arrow_up);
		btnOrderBy.setSelected(true);
		btnOrderBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*btnOrderBy.setSelected(true);
				btnShopType.setSelected(false);
				btnShopTop.setSelected(false);
				sortname = "Distance";
				sortflag = btnOrderBy.getType() - 1;
				pageindex = 1;
				GetData(1);*/
				ShowShopType();
			}
		});
		btnShopType = (HJSelectButton) findViewById(R.id.btn_shoptype);
		btnShopType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*btnOrderBy.setSelected(false);
				btnShopType.setSelected(true);
				btnShopTop.setSelected(false);
				// TODO Auto-generated method stub
				sortname = "pop";
				sortflag = btnShopType.getType() - 1;
				pageindex = 1;
				GetData(1);*/
				ShowOrerBy();
			}
		});

		btnShopTop = (HJSelectButton) findViewById(R.id.btn_shoptop);
		btnShopTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*btnOrderBy.setSelected(false);
				btnShopType.setSelected(false);
				btnShopTop.setSelected(true);
				// TODO Auto-generated method stub
				sortname = "grade";
				sortflag = btnShopTop.getType() - 1;
				pageindex = 1;
				GetData(1);*/
				ShowActivitys();
			}
		});

		inflater = LayoutInflater.from(FsgShopList.this);

		// �ײ��ļ��ظ����̼ҵ�ҳ��View
		moreView = inflater.inflate(R.layout.more_shop_view, null);
		moreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		errorView = (LinearLayout) findViewById(R.id.error);
		errorView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetData(1);
			}
		});
		loadView = (LinearLayout) findViewById(R.id.fullscreen_loading_layout);
		more_shop_pb = (ProgressBar) moreView
				.findViewById(R.id.more_shop_ProgressBar);
		more_shop_tv = (TextView) moreView
				.findViewById(R.id.more_shop_textview);

		shopListView = (ListView) findViewById(R.id.shoplist_shoplist);
		shopListView.addFooterView(moreView);
		moreView.setVisibility(View.GONE);
		mSchedule = new FsgShopList.ListViewAdapter();
		shopListView.setAdapter(mSchedule);

		

	}
	
	public void ShowOrerBy()
	{
		if (orderByView == null) {
			if (shopTypeView != null) {
				shopTypeView.Close();
			}
			if (activitysView != null) {
				activitysView.Close();
			}
			orderByView = new HJPopListView(FsgShopList.this, strsOrderBy, RlParent, RelativeLayout.LayoutParams.FILL_PARENT, 
					RlParent.getHeight(), R.id.ll_btn_opt);
			orderByView.setOnItemClick(new onItemClick() {
				
				

				@Override
				public void onClickItem(int position) {
					// TODO Auto-generated method stub
					if (position > -1) {
						btnShopType.setText(strsOrderBy[position]);
						if (position == 0) {
							sortname = "SortNum";
							sortflag = 1;
							btnShopType.setSelected(false);
						}else{
							if (lastPosition != position) {//�ϴβ��������������
								lastPosition  = position;
								btnShopType.setType(1);
							}
							switch (position) {
							case 1:
								sortname = "Distance";
								break;
							case 2:
								sortname = "pop";
								break;
							case 3:
								sortname = "grade";
								break;
							default:
								sortname = "InTime";
								break;
							}
							
							
							btnShopType.setSelected(true);
							sortflag = btnShopType.getType() - 1;
						}
						
						
						
						//orderByID = String.valueOf((int)Math.pow(2, position - 1));
						
						
						//pageindex = 1;
						GetData(1);
					}
					orderByView = null;
				}
			});
			orderByView.Show();
		}else{
			orderByView.Close();
		}
	}
	
	public void ShowActivitys()
	{
		if (activitysView == null) {
			if (shopTypeView != null) {
				shopTypeView.Close();
			}
			if (orderByView != null) {
				orderByView.Close();
			}
			activitysView = new HJPopListView(FsgShopList.this, strsActivitys, RlParent, RelativeLayout.LayoutParams.FILL_PARENT, 
					RlParent.getHeight(), R.id.ll_btn_opt);
			activitysView.setOnItemClick(new onItemClick() {
				
				

				@Override
				public void onClickItem(int position) {
					// TODO Auto-generated method stub
					if (position > -1) {
						isPromotion = position - 1;
						btnShopTop.setText(strsActivitys[position]);
						
						GetData(1);
					}
					activitysView = null;
				}
			});
			activitysView.Show();
		}else{
			activitysView.Close();
		}
	}

	void ShowShopType() {
		btnOrderBy.setSelected(true);
		if (shopTypeView == null) {
			if (orderByView != null) {
				orderByView.Close();
			}
			if (activitysView != null) {
				activitysView.Close();
			}
			if (strsSortName == null || strsSortName.length < 1) {
				/*Toast.makeText(FsgShopList.this, "���ݼ����С��������Ժ�", 15).show();
				isShowShopType = true;
				return;*/
				strsSortName = new String[app.shopTypeListModel.list.size() + 1];
				strsSortName[0] = getResources().getString(R.string.public_all_type)/*"�����̼�"*/;
				for (int i = 0; i < app.shopTypeListModel.list.size(); i++) {
					strsSortName[i + 1] = app.shopTypeListModel.list.get(i).SortName;
				}
			}
			shopTypeView = new HJPopListView(FsgShopList.this, strsSortName,
					RlParent, RelativeLayout.LayoutParams.FILL_PARENT,
					RlParent.getHeight(), R.id.ll_btn_opt);

			shopTypeView.setOnItemClick(new onItemClick() {

				@Override
				public void onClickItem(int position) {
					// TODO Auto-generated method stub
					if (position > -1) {
						if (position == 0) {
							shoptype = "0";
							btnOrderBy.setText(getResources().getString(R.string.public_all_type)); 
						}else{
							if (app.shopTypeListModel.list.get(position - 1)
									.getSortID().compareTo("23") == 0) {
								Intent intent = new Intent(FsgShopList.this, AddRunOrder.class);
								startActivity(intent);
								btnOrderBy.setSelected(true);
								shopTypeView = null;
								return;
							}
							shoptype = app.shopTypeListModel.list.get(position - 1)
									.getSortID();
							btnOrderBy.setText(app.shopTypeListModel.list
									.get(position - 1).getSortName()); 
						}
						
						pageindex = 1;
						GetData(1);
					}
					btnOrderBy.setSelected(true);
					shopTypeView = null;
				}
			});
			shopTypeView.Show();
			
			// RlParent.addView(shopTypeView);
		} else {
			btnOrderBy.setSelected(true);
			shopTypeView.Close();
		}
	}

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hander = new UIHandler();
		
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


		// bitMapList = new HashMap<String, Bitmap>();
		this.updateReceiver = new UpdateReceiver();
		IntentFilter localIntentFilter1 = new IntentFilter(
				HJAppConfig.UPDATELOCATION);
		registerReceiver(this.updateReceiver, localIntentFilter1);

		IntentFilter localIntentFilter2 = new IntentFilter(
				"com.ihangjing.common.ReShowRefreshPicture");
		registerReceiver(this.updateReceiver, localIntentFilter2);

		IntentFilter localIntentFilter3 = new IntentFilter(
				"com.ihangjing.common.ChangeupdateMyLocation");
		registerReceiver(this.updateReceiver, localIntentFilter3);

		Log.v("MainActivity", "registerReceiver()");

		pageindex = 1;
		// 1����ȡ�������з�����ȡ����
		// getshoptype();
		Bundle externs = getIntent().getExtras();
		if (externs != null) {
			isRem = externs.getInt("isRem");
			shopType = externs.getInt("shopType");
			if (isRem == 3) {
				shopNameString = externs.getString("searchKey");
				shoptype = "0";
			} else {
				if (isRem == 6) {
					shoptype = externs.getString("shopTypeID");
					shoptypeName = externs.getString("shopTypeName");
				} else {
					shoptype = "0";
					shoptypeName = "";
				}
				shopNameString = "";
			}
		}
		initUI();
		if (isRem > 1) {
			btnBack.setVisibility(View.VISIBLE);
			btnBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
		if(app.shopCartModel == null)
			app.shopCartModel = new MyShopCart();
		shopListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount >= totalItemCount
						&& shopListModel[userIndex].getPage() < shopListModel[userIndex].getTotal()) {
					GetData(shopListModel[userIndex].getPage() + 1);
				}
				if (app.mLoadImage != null) {
					app.mLoadImage.doTask();
				}
			}
		});

		// 2�����ݶ�λ��Ϣ��ȡ����
		// ���̣��ȼ���Ƿ���ڶ�λ��Ϣ��������ֱ�Ӹ��ݴ���������Ϣ��ȡ����
		// ��һ������������ַ���Ѿ���ȡ������ȷ��ַ��
		// �û�����ˢ�µ�ǰ��λ��Ϣ�������ݵĻ�ȡ,�����ˢ��ʱ �����ǰ�б� ->���ж�λ ->�յ���λ�ɹ�����Ϣ���ٿ�ʼ��ȡ����

		loadView.setVisibility(View.VISIBLE);
		// ���listview�ĵ���¼� ����������ϸҳ�� ����ִ����Ӧ�Ĳ���
		shopListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// �õ���������̼ҵ�model
				ShopListItemModel localShopModel = shopListModel[userIndex].list
						.get(position);
				if (localShopModel == null) {
					Log.v(TAG, "localShopModel == null");
					return;
				}
				
				// TODO:���������¼
				app.setShop(localShopModel);
				Intent localIntent = new Intent(FsgShopList.this,
						ShopDetail.class);
				startActivity(localIntent);
			}
		});
		if (app.useLocation == null) {
			showDialog(SHOW_LOCATION);
		} else {// һ�������Ѿ���λ����
			Message localMessage1 = new Message();
			localMessage1.what = UIHandler.DO_GET_LOCATION_SUCCESS;
			hander.sendMessage(localMessage1);
		}
		//GetPopADV();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSchedule.notifyDataSetChanged();
		if(shopType == 2){
			//if()
			removeDialog(SHOW_LOCATION);
		}
		// ע��㲥���� ����locationService���͵Ķ�λ��Ϣ
		if (app.useLocation != null) {
			tv_myLocation.setText(app.useLocation.getStreet() + ">");
		}
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		Log.v(TAG, "onCreateDialog:" + paramInt);

		if (paramInt == 111) {
			// ���������ȡ����
			AlertDialog.Builder localBuilder = new AlertDialog.Builder(this)
					.setTitle("��ʾ").setMessage("λ�øı�,��ȡ������?");
			DialogInterface.OnClickListener noOnClick = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					pageindex = 1;
					// ��������б�
					//shopListView.removeAllViewsInLayout();

					// ���»�ȡ����

					loadView.setVisibility(View.VISIBLE);
					Log.v(TAG, "onCreateDialog GetData");
					GetData(1);

				}
			};

			dialog = localBuilder.setPositiveButton("ȷ��", noOnClick)
					.setNegativeButton("ȡ��", null).create();
		} else if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);
			return dialog;
		} else if (paramInt == SHOW_LOCATION) {
			dialog = OtherManager.createProgressDialog(FsgShopList.this,
					"��λ�У����Ժ�");
		}else if (paramInt == 323) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
			.setTitle(getResources().getString(R.string.public_notice)/*"��ʾ"*/).setMessage(getResources().getString(R.string.check_have_new)/*"���°汾���Ը��£�ȷ���������°汾?"*/);
			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
		
					// ��ʼ��������
					DownLoad();
				}
			};
		
			dialog = localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok)/*"ȷ��"*/, local3)
					.setNegativeButton(getResources().getString(R.string.public_cancel)/*"ȡ��"*/, null).create();
		}
		return dialog;
	}
	
	private void DownLoad() {
		FsgShopList.this.removeDialog(323);
		// dialogStr = "�����������°�ϵͳ...";
		// showDialog(322);

		// ���������汾�Ƿ���Ҫ����
		mUpdateManager = new UpdateManager(this);
		mUpdateManager.checkUpdateInfo();
	}

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetPopADV() {// ��ȡ��������õĵ������

		DisplayMetrics dm = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int width = dm.widthPixels;

		int height = dm.heightPixels;
		String urlString = String.format(
				"APP/Android/Specialad.aspx?size=%d*%d", width, height);
		localHttpManager = new HttpManager(FsgShopList.this,
				FsgShopList.this, urlString, null, 2, GET_POP_ADV);
		localHttpManager.getRequeest();

	}
	// �������������������Ƿ���Ҫ����
		public void CheckUpdate() {
		

			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("version", app.version);

			localHttpManager = new HttpManager(FsgShopList.this,
					FsgShopList.this, "APP/Android/CheckUpdate.aspx?",
					localHashMap, 2, CHECK_UPDATE);

			localHttpManager.getRequeest();
		}

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetData(int page) {

			// ��ʾ���ݼ�����
			// downloadLayout.setVisibility(View.VISIBLE);
		if (!isReadData) {
			isReadData = true;
			if (app.useLocation != null) {
				lon = app.useLocation.getLon();
				lat = app.useLocation.getLat();
			} else {
				lon = 0.0;
				lat = 0.0;
			}
			if (page == 1) {
				//shopListModel.list.clear();
				//mSchedule.notifyDataSetChanged();
				loadView.setVisibility(View.VISIBLE);
				errorView.setVisibility(View.GONE);
				//shopListView.setVisibility(View.GONE);
				// moreView.setVisibility(View.GONE);
			}
			// �������籣������
			HashMap<String, String> localHashMap = new HashMap<String, String>();

			switch (isRem) {
			case 1:
				localHashMap.put("isrem", "1");
				break;
			case 4:
				localHashMap.put("isonline", "0");
				break;
			case 5:
				localHashMap.put("isonline", "1");
				break;
			case 7:
				localHashMap.put("userid", app.userInfo.userId);
				localHashMap.put("iscollected", "1");
				break;
			
			default:
				break;
			}
			/*if (isPromotion  >= 0) {
				localHashMap.put("ispromotion", String.valueOf(isPromotion));
			}*/
			localHashMap.put("types", String.valueOf(shopType));// �̼ҷ���
			if(shopType == 2){
				localHashMap.put("cityID", app.useLocation.getCityID());
				localHashMap.put("areaID", app.useLocation.getAreaID()); 
			}
			localHashMap.put("languageType", app.language);
			localHashMap.put("lat", String.valueOf(this.lat));
			localHashMap.put("lng", String.valueOf(this.lon));
			localHashMap.put("pageindex", String.valueOf(page));
			localHashMap.put("Order", orderByID);
			localHashMap.put("sortname", sortname);
			localHashMap.put("sortflag", String.valueOf(sortflag));
			localHashMap.put("shoptype", shoptype);// �̼ҷ���
			if (shopNameString != null && !shopNameString.equals("")) {
				localHashMap.put("shopname", shopNameString);
			} else {

				if (bid < 1) {
					// localHashMap.put("nearFilter", nearFilterString);
				}
			}

			localHttpManager = new HttpManager(FsgShopList.this,
					FsgShopList.this, "Android/GetShopListByLocation.aspx?",
					localHashMap, 2, GET_SHOP);

			localHttpManager.postRequest();

			Log.v(TAG, "tt��ȡ��һҳ����");
			myaction = 1;
		}

	}

	// ��ȡ�����߳�
	private void getshoptype() {
		myaction = 2;
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pid", "0");

		localHttpManager = new HttpManager(FsgShopList.this, FsgShopList.this,
				"Android/GetShopTypeList.aspx?", localHashMap, 1, GET_SHOP_TYPE);
		localHttpManager.postRequest();
	}

	// http://www.ctolive.com/space-126-do-blog-id-1594.html
	// ����ֱ���ڶ��߳��ж�Activity��ListView���в���������취����ʹ��Handler�������߳���Activity֮���ͨ������
	private class UIHandler extends Handler {
		static final int LIST_DATA_UPDATE = 13;// �б������и���
		static final int SET_SHOP_LIST_ONLY_NOTIFY = 14;// �ڶ�ҳ��ʼ
		static final int DO_GET_LOCATION_SUCCESS = 2;
		static final int OVER_GET_SHOPTYPE = 15;// ��ȡ�������
		static final int LIST_DATA_UPDATE_BYTYPE = 16;// ѡ����������б�
		static final int NET_ERROR = -2;
		public static final int NO_SHOP_DATA = -3;
		public static final int GPS_UPDATA = 3;
		protected static final int CHECK_ADV = 1;
		public static final int CHECK_UPDATE_M = 17;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (!FsgShopList.this.isActivityRun) {
				return;
			}
			switch (msg.what) {
			case CHECK_UPDATE_M:
				showDialog(323);
				break;
			case CHECK_ADV:
				for (int i = 0; i < app.popAdvList.list.size(); i++) {
					app.mLoadImage.doTask();
					PopAdvModel model = app.popAdvList.list.get(i);
					Bitmap bitmap = app.mLoadImage.getBitmap(model.getImageNet());
					if (bitmap == null) {
						return;
					}
				}
				if (!isPopADV) {
					isPopADV = true;
					timer.cancel();
					app.mLoadImage = app.mLoadImage;
					Intent intent = new Intent(FsgShopList.this,
							PopAdvActivity.class);
					startActivity(intent);
				}

				break;
			case NET_ERROR:
				Toast.makeText(FsgShopList.this, getResources().getString(R.string.public_net_or_data_error), 15).show();
				if (msg.arg1 == GET_SHOP_TYPE) {
					//shopListView.setVisibility(View.GONE);
					loadView.setVisibility(View.GONE);
					errorView.setVisibility(View.VISIBLE);
					ivDataNotice.setBackgroundResource(R.drawable.bg_wifi);
					tvCheckAgain.setVisibility(View.VISIBLE);
					tvMessage.setVisibility(View.VISIBLE);
				}
				break;
			case NO_SHOP_DATA:
				//shopListView.setVisibility(View.GONE);
				loadView.setVisibility(View.GONE);
				errorView.setVisibility(View.VISIBLE);
				ivDataNotice.setBackgroundResource(R.drawable.have_no_shop);
				tvCheckAgain.setVisibility(View.GONE);
				tvMessage.setVisibility(View.GONE);
				break;
			case SET_SHOP_LIST_ONLY_NOTIFY: {
				// ���صڶ�ҳʱʹ�ô˶δ��룬�б�ˢ�£����ֵ�ǰλ�ã������б�ˢ����Ҫ���¿�ʼ����
				Log.v(TAG, "case SET_SHOP_LIST_ONLY_NOTIFY");

				loadView.setVisibility(View.GONE);
				errorView.setVisibility(View.GONE);
				//shopListView.setVisibility(View.VISIBLE);
				if (FsgShopList.this.shopListModel != null) {
					Log.v(TAG, "ShopList.this.shopListModel != null");
					

					// ������ص㣬��ˢ�������б�ֱ�Ӹ����б�����
					userIndex = nowIndex;
					mSchedule.notifyDataSetChanged();
					

					more_shop_pb.setVisibility(View.GONE);
					more_shop_tv.setVisibility(View.VISIBLE);

					// ÿ��ȡһ��Ҫ����һ��page(ҳ��) ������ظ���ʱ��Ҫʹ��
					int m = FsgShopList.this.shopListModel[userIndex].getPage();
					int n = FsgShopList.this.shopListModel[userIndex].getTotal();

					// �����ж��Ƿ������Ҫ���ظ������ݲ���Ҫ���Ƴ�"���ظ�������"
					if ((m < n)) {
						moreView.setVisibility(View.VISIBLE);
					} else {
						moreView.setVisibility(View.GONE);
					}
					return;
				}

				OtherManager.Toast(FsgShopList.this, getResources().getString(R.string.public_net_no_data));
				return;
			}
			case LIST_DATA_UPDATE: {// �б������и���,�̼�ͼƬ���سɹ�
				Log.v(TAG, "LIST_DATA_UPDATE S");
				mSchedule.notifyDataSetChanged();
				Log.v(TAG, "LIST_DATA_UPDATE E");
				break;
			}
			case DO_GET_LOCATION_SUCCESS: {

				// ��ȡ��ַ�ɹ� ���µ�ǰλ�� GPS��ȡ��ַ������
				Log.v(TAG, "���µ�ǰλ�� GPS��ȡ��ַ������");
				Log.v(TAG, "���µ�ǰ��ַ");
				// ��ʼ��ȡ����
				// downloadLayout.setVisibility(View.VISIBLE);
				Log.v(TAG, "DO_GET_LOCATION_SUCCESS GetData");
				//
				removeDialog(SHOW_LOCATION);
				app.isUpDataLocation = true;
				tv_myLocation.setText(app.useLocation.getStreet() + ">");
				GetData(1);
				break;
			}
			case GPS_UPDATA: {

				// ��ȡ��ַ�ɹ� ���µ�ǰλ�� GPS��ȡ��ַ������
				Log.v(TAG, "���µ�ǰλ�� GPS��ȡ��ַ������");
				SharedPreferences usersetting = getApplicationContext()
						.getSharedPreferences(HJAppConfig.CookieName,
								Context.MODE_PRIVATE);

				Log.v(TAG, "���µ�ǰ��ַ");

				// ���»�ȡ���ݶԻ���
				showDialog(111);
				break;
			}
			case OVER_GET_SHOPTYPE:
				if (isShowShopType) {
					isShowShopType = false;
					ShowShopType();
				}
				break;
			case LIST_DATA_UPDATE_BYTYPE:
				// ������ص㣬��ˢ�������б�ֱ�Ӹ����б�����
				loadView.setVisibility(View.VISIBLE);
				//shopListView.setVisibility(View.GONE);
				//shopListModel[userIndex].list.clear();
				// bitMapList.clear();
				//mSchedule.notifyDataSetChanged();

				GetData(1);

				break;
			}
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		Message message = new Message();
		if (paramInt == 260) {
			JSONArray array;
			JSONObject json;
			String jsonString = (String) paramObject;
			
			if (OtherManager.DEBUG) {
				Log.v(TAG, "0 jsonString:" + jsonString);
			}

			try {
				json = new JSONObject(jsonString);

				switch (tag) {
				case GET_SHOP: 
					this.pageindex = Integer.parseInt(json.getString("page"));
					this.total = Integer.parseInt(json.getString("total"));
					nowIndex = userIndex;
					if (pageindex == 1) {
						nowIndex--;
						nowIndex *= -1;
						shopListModel[nowIndex].list.clear();
					}
					array = json.getJSONArray("list");

					shopListModel[nowIndex].setPage(pageindex);
					shopListModel[nowIndex].setTotal(total);
					if (array.length() > 0) {
						ShopListItemModel models;
						for (int i = 0; i < array.length(); i++) {
							models = new ShopListItemModel(
									array.getJSONObject(i));
							this.shopListModel[nowIndex].list.add(models);
						}
						message.what = UIHandler.SET_SHOP_LIST_ONLY_NOTIFY;
					} else {
						message.what = UIHandler.NO_SHOP_DATA;
					}

				
					break;
				
				case GET_POP_ADV:
					array = json.getJSONArray("foodtypelist");
					int m = array.length();
					if (m > 0) {
						if (app.popAdvList == null) {
							app.popAdvList = new PopAdvListModel();
						} else {
							app.popAdvList.list.clear();
						}
						app.popAdvList.popType = 0;
						
					}
					for (int i = 0; i < m; i++) {
						PopAdvModel model = new PopAdvModel(
								array.getJSONObject(i));
						app.mLoadImage.addTask(model.getImageNet(), null, 0);
						app.popAdvList.list.add(model);
					}
					if (m > 0) {

						timer = new Timer();
						updataViewTask = new TimerTask() {
							@Override
							public void run() {
								// mSchedule.notifyDataSetChanged();
								Message msg = new Message();
								msg.what = UIHandler.CHECK_ADV;
								hander.sendMessage(msg);

							}
						};
						timer.schedule(updataViewTask, 1000L, 1000L);
					}
					break;
				case CHECK_UPDATE:
					int state = json.getInt("state");
					if (state == 1) {
						message.what = UIHandler.CHECK_UPDATE_M;

					}
					break;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}	

				
		} else {
			message.arg1 = tag;
			message.what = UIHandler.NET_ERROR;
		}
		if (tag == GET_SHOP) {
			isReadData = false;
		}
		hander.sendMessage(message); // ������Ϣ��Handler

	}

	
	// �Զ���adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;

		ListViewAdapter() {
		}

		@Override
		public int getCount() {
			return FsgShopList.this.shopListModel[userIndex].list.size();
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

			ShopItemView viewHolder;
			ShopListItemModel localShoplist;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) FsgShopList.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
					
				}
				viewHolder = new ShopItemView();
				if (isRem == 1) {
					convertView = inflater.inflate(R.layout.topshopitem, null);
					viewHolder.iconImageView = (RelativeLayout) convertView
							.findViewById(R.id.itemImage);
					float with = shopListView.getWidth();
					
					float height = with / 1.3333333333f;
					int h = (int)height;
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, (int)height);
					viewHolder.iconImageView.setLayoutParams(params);
				} else {
					convertView = inflater.inflate(R.layout.shopitem, null);
					viewHolder.ivOpenStatus = (ImageView) convertView
							.findViewById(R.id.iv_open);
					viewHolder.iconImageView = (RelativeLayout) convertView
							.findViewById(R.id.itemImage);
				}
				viewHolder.tvDistance = (TextView)convertView.findViewById(R.id.tv_dis_sale);
				viewHolder.shopnameTextView = (TextView) convertView
						.findViewById(R.id.itemShopName);

				viewHolder.telTextView = (TextView) convertView
						.findViewById(R.id.itemTel);

				viewHolder.addressTextView = (TextView) convertView
						.findViewById(R.id.itemShopAddress);
				viewHolder.tvShopDiscount = (TextView) convertView
						.findViewById(R.id.tv_discount);

				viewHolder.ordertimeTextView = (TextView) convertView
						.findViewById(R.id.itemOrderTime);
				viewHolder.tvShopCount = (TextView)convertView.findViewById(R.id.tv_shop_count);

				viewHolder.ivLine = (ImageView)convertView.findViewById(R.id.iv_line);
				/*viewHolder.iconImageView
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String urlString = (String) v.getTag();
								Bitmap img = app.mLoadImage.getBitmap(urlString);
								ImagePopViewDialog dialog = new ImagePopViewDialog(
										FsgShopList.this, img);
								dialog.show();
							}
						});*/
				viewHolder.tvTypeTextView = (TextView) convertView
						.findViewById(R.id.tv_notic);
				viewHolder.tvSendTime = (TextView) convertView
						.findViewById(R.id.tv_send_time);
				viewHolder.llICON = (LinearLayout)convertView.findViewById(R.id.ll_icon);
				viewHolder.ivW = (ImageView)convertView.findViewById(R.id.iv_w);
				viewHolder.ivD = (ImageView)convertView.findViewById(R.id.iv_d);
				viewHolder.ivLine = (ImageView)convertView.findViewById(R.id.iv_line);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ShopItemView) convertView.getTag();
			}

			localShoplist = FsgShopList.this.shopListModel[userIndex].list.get(position);

			if (localShoplist != null) {

				viewHolder.shopnameTextView.setText(localShoplist.getName());
				if(app.shopCartModel != null){
					int count =  app.shopCartModel.getFoodCountWithShopID(localShoplist.getId(), localShoplist.shopIndexInShopCart);
					if(count > 0){
						viewHolder.tvShopCount.setVisibility(View.VISIBLE);
						viewHolder.tvShopCount.setText(String.valueOf(count));
					}else{
						viewHolder.tvShopCount.setVisibility(View.GONE);
					}
				}else{
					viewHolder.tvShopCount.setVisibility(View.GONE);
				}
				if (isRem == 1) {// �Ƽ�����
					viewHolder.telTextView.setText(Html.fromHtml("<font color='red'><b>[�Ƽ�����]</b></font>" + localShoplist.getDesc()));
					switch (localShoplist.getStatus()) {
					case 1:
						viewHolder.tvTypeTextView.setText(getResources().getString(R.string.shop_list_open));//"Ӫҵ��");
						// convertView.setBackgroundColor(Color.WHITE);
						break;
					case 0:
						viewHolder.tvTypeTextView.setText(getResources().getString(R.string.shop_list_close));//"��Ϣ��");
						// convertView.setBackgroundColor(Color.rgb(220, 220,
						// 220));
						break;
					default:
						break;
					}
				} else {
					String m = getResources().getString(R.string.public_moeny_0);//Զ
					viewHolder.tvDistance.setText(Html.fromHtml("���룺<font color='red'><b>" + localShoplist.getDis() + "KM</b></font> ������<font color='red'><b>" + localShoplist.getSales() + "</b></font>"));
					viewHolder.telTextView.setText(Html.fromHtml(getResources().getString(R.string.shop_list_send_fee)//"���ͷ�:"
							+ "<font color='red'><b>" + m + localShoplist.getSendMoney() + "</b></font> " + getResources().getString(R.string.shop_list_send_start) //���ͽ��
							+ "<font color='red'><b>" + m + localShoplist.getMinmoney() + "</b></font>"));
					// viewHolder.ivOpenStatus.setVisibility(View.GONE);
					if (localShoplist.getTimeStart2().length() > 2
							&& localShoplist.getTimeEnd2().length() > 2) {
						viewHolder.ordertimeTextView.setText(localShoplist
								.getTimeStart1()
								+ "-"
								+ localShoplist.getTimeEnd1()
								+ "  "
								+ localShoplist.getTimeStart2()
								+ "-"
								+ localShoplist.getTimeEnd2());
					} else {
						viewHolder.ordertimeTextView.setText(localShoplist
								.getTimeStart1()
								+ "-"
								+ localShoplist.getTimeEnd1());
					}
					if(shopType == 2){
						if(localShoplist.getGrade() > 0){
							viewHolder.telTextView.setText(String.format("%d%%����", localShoplist.getGrade()));
							
						}else{
							viewHolder.telTextView.setText("��������");
						}
						//viewHolder.telTextView.setVisibility(View.GONE);
						//viewHolder.ordertimeTextView.setVisibility(View.GONE);
						viewHolder.ordertimeTextView.setCompoundDrawables(null, null, null, null);
						viewHolder.ordertimeTextView.setText(String.format("������%s", localShoplist.getSales()));
					}
					viewHolder.tvTypeTextView.setVisibility(View.GONE);
					switch (localShoplist.getStatus()) {
					case 1:
						viewHolder.ivOpenStatus.setVisibility(View.VISIBLE);
						// convertView.setBackgroundColor(Color.WHITE);
						break;
					case 0:
						viewHolder.ivOpenStatus.setVisibility(View.GONE);
						// convertView.setBackgroundColor(Color.rgb(220, 220,
						// 220));
						break;
					default:
						break;
					}
				}
				/*switch (localShoplist.getShopType()) {
				case 1:
					viewHolder.ivW.setVisibility(View.VISIBLE);
					break;
				case 2:
					viewHolder.ivD.setVisibility(View.VISIBLE);
					break;
				default:
					viewHolder.ivW.setVisibility(View.VISIBLE);
					viewHolder.ivD.setVisibility(View.VISIBLE);
					break;
				}*/
				viewHolder.addressTextView.setText(localShoplist.getAdress());
				viewHolder.tvSendTime.setText(localShoplist.getDesc());
				
				if (localShoplist.getShopDiscount() != 0.0f
						&& localShoplist.getShopDiscount() < 10.0f) {
					viewHolder.tvShopDiscount.setVisibility(View.VISIBLE);
					viewHolder.tvShopDiscount.setText(localShoplist
							.getShopDiscount() + getResources().getString(R.string.public_zhe));//"��");
				} else {
					viewHolder.tvShopDiscount.setVisibility(View.GONE);
				}

				/*viewHolder.tvSendTime.setText("<" + localShoplist.getDis()
						+ getResources().getString(R.string.public_distance));*///"����");

				// ���Ʊ߿� ��ʾ�̼�logo

				if (localShoplist.getIcon().length() > 16) {
					viewHolder.iconImageView.setTag(localShoplist.getIcon());
					app.mLoadImage.addTask(localShoplist.getIcon(),
							viewHolder.iconImageView, R.drawable.nopic_shop);
				} else {
					viewHolder.iconImageView
							.setBackgroundResource(R.drawable.nopic_shop);
				}
				//if (viewHolder.llICON.getChildCount() > 0) {
					viewHolder.llICON.removeAllViews();
				//}
					if (localShoplist.tagListModel.list.size() > 0) {
						viewHolder.ivLine.setVisibility(View.VISIBLE);
						for (int i = 0; i < localShoplist.tagListModel.list.size(); i++) {
							TagModel model = localShoplist.tagListModel.list.get(i);
							ShopDiscountItemView discountItemView = new ShopDiscountItemView();
							View view = model.getView();
							if (view == null) {
								view = inflater.inflate(R.layout.shop_discount_view_item, null);;
								discountItemView.tvInfo = (TextView)view.findViewById(R.id.tv_info);
								discountItemView.rlImg = (RelativeLayout)view.findViewById(R.id.rl_img);
								model.setView(view);
							}else{
								discountItemView.tvInfo = (TextView)view.findViewById(R.id.tv_info);
								discountItemView.rlImg = (RelativeLayout)view.findViewById(R.id.rl_img);
							}
							if (model.getImageNet().length() > 16) {
								
								discountItemView.rlImg.setTag(model.getImageNet());
								app.mLoadImage.addTask(model.getImageNet(),
										discountItemView.rlImg, R.drawable.food_class);
							}
							viewHolder.llICON.addView(view);
							discountItemView.tvInfo.setText(model.getInfo());
						}
					}else{
						viewHolder.ivLine.setVisibility(View.GONE);
					}
					
				}

			return convertView;
		}
	}

	@Override
	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if (paramInt == KeyEvent.KEYCODE_BACK && isRem <= 1) {
			
			Intent localIntent = new Intent("com.ihangjing.common.tap0");
			app.sendBroadcast(localIntent);

			
			
			return true;
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (app.mLoadImage != null) {
			app.mLoadImage.stopTask();
		}

		unregisterReceiver(updateReceiver);
		this.isActivityRun = false;

	}

	// ��item�еĸ�view����������android:descendantFocusability="blocksDescendants"����
	// ���ܹ㲥��Ϣ
	class UpdateReceiver extends BroadcastReceiver {
		UpdateReceiver() {
		}

		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals(
					HJAppConfig.UPDATELOCATION)) {

				// ��λ�ɹ����ȡ����
				// ��ʾ�Ի��� �û�ȷ�Ϻ��ٴλ�ȡ����
				//paramIntent.removeExtra(name);
				Message localMessage1 = new Message();
				localMessage1.what = UIHandler.DO_GET_LOCATION_SUCCESS;
				hander.sendMessage(localMessage1);
			} else if (paramIntent.getAction().equals(
					"com.ihangjing.common.ReShowRefreshPicture")) {

				Log.v(TAG,
						"��λ�쳣�����Ժ�ˢ�� onReceive:com.ihangjing.common.ReShowRefreshPicture");
			} else if (paramIntent.getAction().equals(
					"com.ihangjing.common.ChangeupdateMyLocation")) {
				// λ����Ϊʹ�����ƶ�����ɵ����»�ȡ���� ������ʾ
				// ��ʾ�Ի��� �û�ȷ�Ϻ��ٴλ�ȡ����
				if (op.equals("location")) {
					Message localMessage1 = new Message();
					localMessage1.what = UIHandler.GPS_UPDATA;
					FsgShopList.this.hander.sendMessage(localMessage1);
					Log.v(TAG,
							"onReceive:com.ihangjing.common.ChangeupdateMyLocation");

				}
			}
		}
	}

}