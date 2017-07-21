package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.HJControls.HJListView;
import com.ihangjing.HJControls.HJPopListView;
import com.ihangjing.HJControls.HJScorllADV;
import com.ihangjing.HJControls.HJSelectButton;
import com.ihangjing.HJControls.HJPopViewBase.onItemClick;
import com.ihangjing.Model.NewsModelList;
import com.ihangjing.Model.ShopDiscountItemView;
import com.ihangjing.Model.ShopItemView;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.Model.ShopListModel;
import com.ihangjing.Model.ShopTypeInfo;
import com.ihangjing.Model.ShopTypeListModel;
import com.ihangjing.Model.TagModel;
import com.ihangjing.Model.UserLocalInfo;
import com.ihangjing.common.HJInteger;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.common.Preferences;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;
import com.theme.app.ThemeStateUtil;
import com.theme.app.ThemeUtil;

/*
 * zjf@Ihangjing.com 2011.10.27 
 * 程序主页面
 */
public class MainActivity extends HjActivity implements HttpRequestListener {
	Handler mHandler;
	// private ImageView loation_refreshPicture;
	// private ProgressBar pb_refreshLocation;
	Preferences prefrence;
	// private TextView tv_myLocation;

	Activity mActivity;

	UpdateReceiver updateReceiver;

	final private String LOG_TAG = "MainActivity ";

	// 生成动态数组，并且转入数据
	ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

	HttpManager localHttpManager = null;
	private static final String TAG = "MainActivity";
	private static final int GET_SHOP_TYPE = 1;
	private static final int CHECK_VERSION = 2;
	private static final int GET_SHOP = 3;
	private static final int SHOW_LOCATION = 100;
	private static final int GET_CITY_ID = 4;
	private static final int GET_AREA_LIST = 5;

	RelativeLayout downloadRelativeLayout;
	private int action = 1;// 1 读取新闻 2 读取检测信息
	private UpdateManager mUpdateManager;
	TextView localbar;
	String mybuildname;
	private NewsModelList newsListModel;
	public ShopListViewAdapter mSchedule;

	private Timer timer;
	private TimerTask updataViewTask;
	public boolean isPopADV = false;

	protected boolean canShowLocation = false;

	public ArrayList<View> shopTypeViews;
	private String dialogStr;
	private TextView tv_myLocation;
	private HJScorllADV adv;
	public LayoutInflater inflater;
	private ViewPager vpShopType;
	public int typeSize = 8;
	private HJListView lvContent;
	private boolean isReadData;
	private int pageindex = 1;
	private int total = 1;
	private ShopListModel shopListModel[];
	public int userIndex;
	public int nowIndex;
	public ShopTypeListAdapter mShopTypeAdapter;
	private Long time1 = 0l;

	private View heardView;
	private LinearLayout llOpt;
	// private LinearLayout llOpt1;
	private View secHeardView;
	private HJSelectButton btnOrderBy;
	private HJSelectButton btnShopType;
	private HJSelectButton btnShopTop;
	private HJPopListView shopTypeView;
	private HJPopListView activitysView;
	private HJPopListView orderByView;
	private String shoptype = "0";// 商家一级分类
	protected int sortflag = 1;// 0降序排序，1升序排序
	protected String sortname = "SortNum";// 排序类型 SortNum 默认 pop销量 InTime最新
	// grade评价 Distance距离
	protected int lastPosition;
	private String[] strsSortName;
	private String[] strsOrderBy;
	private String[] strsActivitys;
	protected int isPromotion = 0;
	protected int shopType = 0;
	private RelativeLayout RlParent;
	private HJSelectButton btnOrderBy1;
	private HJSelectButton btnShopTop1;
	private HJSelectButton btnShopType1;
	private EditText etSearchKey;
	private LinearLayout llCity;
	private boolean isSelectCity;
	private ImageView imgLocation;
	private Button btnLeft;
	private boolean isSelectArea;
	private LinearLayout llViewDots;
	private ArrayList<ImageView> listDots;
	private RelativeLayout rlTypePT;

	public MainActivity() {
		// this.startActivity_requestCode_changeMyLocation = 1;
		mActivity = this;
		mHandler = new UIHandler();
	}

	// 判断之前选择的建筑物是否删除了
	public void CheckBuild(String bid) {
		action = 3;
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("id", bid);
		localHttpManager = new HttpManager(MainActivity.this,
				MainActivity.this, "APP/Android/checkbuild.aspx?",
				localHashMap, 2, 3);
		localHttpManager.getRequeest();
	}

	/**
	 * 进入本区外卖和团购，秒杀前判断有没有选择城市，没有先选择城郭，有，判断有没有选择楼宇，有直接转,没有到到选择区域，再到楼宇.
	 * 
	 * @return 返回1表示正常，0跳转
	 */
	public int checklocal() {
		UserLocalInfo mInfo = OtherManager.getUserLocal(MainActivity.this);
		int rs = 0;
		if (mInfo.cityid.equalsIgnoreCase("0")) {
			// 选择城市
			/*
			 * Intent localIntent0 = new Intent(MainActivity.this,
			 * SelCity.class); mActivity.startActivity(localIntent0); rs = 0;
			 */
		} else {
			if (mInfo.buildid == 0) {
				// 选择区域->商圈->楼宇
				Intent localIntent0 = new Intent(MainActivity.this,
						SelSection.class);
				localIntent0.putExtra("cityid", mInfo.cityid);
				mActivity.startActivity(localIntent0);
				rs = 0;
			} else {
				rs = 1;
			}
		}

		return rs;
	}

	// 所有事情做完后检测程序是否需要更新
	public void CheckUpdate() {
		action = 2;

		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("version", app.version);

		localHttpManager = new HttpManager(MainActivity.this,
				MainActivity.this, "APP/Android/CheckUpdate.aspx?",
				localHashMap, 2, CHECK_VERSION);

		localHttpManager.getRequeest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		Message message = new Message();
		if (paramInt == 260) {
			String jsonString = (String) paramObject;

			// 开始解析接收到的json数据
			// JSONArray array = new JSONArray();

			JSONObject json = null;
			try {
				json = new JSONObject(jsonString);

				switch (tag) {
				case CHECK_VERSION:
					int state = json.getInt("state");
					if (json.getInt("state") == 1) {
						message.what = UIHandler.HAVE_NEW_VERSION;
						// mHandler.sendMessage(message); // 发送消息给Handler
					}

					break;
				case GET_SHOP:
					this.pageindex = Integer.parseInt(json.getString("page"));
					this.total = Integer.parseInt(json.getString("total"));

					nowIndex = userIndex;
					if (pageindex == 1) {
						nowIndex--;
						nowIndex *= -1;
						shopListModel[nowIndex].list.clear();
					}
					JSONArray array1 = json.getJSONArray("list");

					shopListModel[nowIndex].setPage(pageindex);
					shopListModel[nowIndex].setTotal(total);
					if (array1.length() > 0) {
						ShopListItemModel models;
						for (int i = 0; i < array1.length(); i++) {
							models = new ShopListItemModel(
									array1.getJSONObject(i));
							this.shopListModel[nowIndex].list.add(models);
						}
						message.what = UIHandler.SET_SHOP_LIST_ONLY_NOTIFY;
					} else {
						message.what = UIHandler.NO_SHOP_DATA;
					}

					break;
				case GET_CITY_ID:
					int cityid = json.getInt("cityid");
					if (cityid > 0) {
						app.useLocation.setCityID(String.valueOf(cityid));
						message.what = UIHandler.GET_CITYID_SUCESS;
					} else {
						message.what = UIHandler.GET_CITYID_FAILD;
					}
					break;
				case GET_AREA_LIST:
					break;
				case GET_SHOP_TYPE:
					JSONArray array = json.getJSONArray("datalist");

					ShopTypeInfo models;// = new ShopTypeInfo[array.length()];

					/*
					 * models = new ShopTypeInfo(); models.SortName =
					 * getResources
					 * ().getString(R.string.public_all_type);//"全部分类";
					 * models.SortID = "0"; shopTypeListModel.list.add(models);
					 */
					int j = 0;
					for (int i = 0; i < array.length(); i++) {
						JSONObject model = array.getJSONObject(i);
						models = new ShopTypeInfo(model);
						app.shopTypeListModel.list.add(models);
					}
					message.what = UIHandler.OVER_GET_SHOPTYPE;
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}

		} else {
			message.what = UIHandler.NET_ERROR;
		}
		if (tag == GET_SHOP) {
			isReadData = false;
		}
		mHandler.sendMessage(message);
	}

	// http://dev.baidu.com/wiki/geolocation/index.php?title=AndroidAPI
	private class UIHandler extends Handler {
		public static final int GET_CITYID_FAILD = 8;
		public static final int GET_CITYID_SUCESS = 9;
		public static final int NO_SHOP_DATA = 4;
		public static final int SET_SHOP_LIST_ONLY_NOTIFY = 3;
		public static final int OVER_GET_SHOPTYPE = 7;
		public static final int NET_ERROR = -1;
		static final int DO_UPDATE_APP_SUCCESS = 1;
		static final int DO_GET_LOCATION_SUCCESS = 2;
		private static final int HAVE_NEW_VERSION = 6;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_CITYID_SUCESS:
				GetNearShop(1);
				break;
			case GET_CITYID_FAILD:
				Toast.makeText(
						MainActivity.this,
						String.format("您当前的城市“%s”没有开通服务！请选择城市",
								app.useLocation.getCityName()), 15).show();
				gotoCity();
				break;
			case NO_SHOP_DATA:
			case SET_SHOP_LIST_ONLY_NOTIFY:
				userIndex = nowIndex;
				mSchedule.notifyDataSetChanged();
				break;
			case OVER_GET_SHOPTYPE:
				int count = app.shopTypeListModel.list.size() / typeSize;
				if (app.shopTypeListModel.list.size() % typeSize != 0) {
					count++;
				}
				if (llViewDots != null) {
					listDots.clear();
					llViewDots.removeAllViews();
				}
				ImageView iv;
				shopTypeViews = new ArrayList<View>();
				View convertView;
				GridView gv;
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(10, 0, 0, 0);
				for (int i = 0; i < count; i++) {

					convertView = inflater.inflate(R.layout.shop_type_view,
							null);
					gv = (GridView) convertView.findViewById(R.id.gridview);
					gv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub

							int page = vpShopType.getCurrentItem();
							position += page * typeSize;
							ShopTypeInfo models = app.shopTypeListModel.list
									.get(position);
							if (models.getSortID().compareTo("23") == 0) {
								Intent intent = new Intent(MainActivity.this,
										AddRunOrder.class);
								startActivity(intent);
							} else {
								Intent localIntent1 = new Intent(
										MainActivity.this, FsgShopList.class);
								localIntent1.putExtra("isRem", 6);// 分类商家
								localIntent1.putExtra("shopTypeID",
										models.SortID);
								localIntent1.putExtra("shopTypeName",
										models.SortName);
								startActivity(localIntent1);
							}
						}
					});

					gv.setAdapter(new ShopTypeListAdapter(i));
					shopTypeViews.add(convertView);

					if (llViewDots != null) {
						iv = new ImageView(MainActivity.this);
						if (i != 0) {
							iv.setBackgroundResource(R.drawable.index_ico_03);
						} else {
							iv.setBackgroundResource(R.drawable.index_ico_hov_03);
						}
						iv.setLayoutParams(params);
						listDots.add(iv);
						llViewDots.addView(iv);
					}
				}
				vpShopType.setAdapter(new HJPagerAdapter(shopTypeViews));
				break;
			case DO_UPDATE_APP_SUCCESS: {
				break;
			}
			case DO_GET_LOCATION_SUCCESS: {
				// 更新当前位置 GPS获取地址并更新

				if (shopType == 1) {
					Log.v("MainActivity", "更新当前位置 GPS获取地址并更新");
					removeDialog(SHOW_LOCATION);
					tv_myLocation.setText(app.useLocation.getStreet() + ">");
					app.isUpDataLocation = true;
					// if (pageindex == 0) {
					GetNearShop(1);
					// }
				} else {
					if (app.useLocation.getCityID() == null
							|| app.useLocation.getCityID().length() == 0
							|| app.useLocation.getCityID().compareTo("0") == 0) {
						getCityID(app.useLocation.getCityName());
					}

					tv_myLocation.setText("所有区域");
				}

				break;
			}

			case 201: {// 检查之前选择的建筑物是否存在
				// localbar.setText("我在："+mybuildname);
				break;
			}
			case HAVE_NEW_VERSION: {

				showDialog(323);
				break;
			}
			}// switch end
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.main_activity);

		// 穿透activity状态栏
		ThemeStateUtil themeStateUtil = new ThemeStateUtil(MainActivity.this);

		// ThemeUtil theme = new ThemeUtil();
		// theme.theme(MainActivity.this);

		Bundle extern = getIntent().getExtras();
		if (extern != null) {
			shopType = extern.getInt("shopType");
		}
		// AudioManager.STREAM_RING

		// adv.setVisibility(View.GONE);
		// app.mLoadImage = new LoadImage(MainActivity.this);
		shopListModel = new ShopListModel[2];
		shopListModel[0] = new ShopListModel();
		shopListModel[1] = new ShopListModel();
		app.shopTypeListModel = new ShopTypeListModel();
		// 初始化界面
		strsOrderBy = new String[5];
		strsOrderBy[0] = getResources().getString(R.string.shop_list_default);// "默认排序";
		strsOrderBy[1] = getResources().getString(R.string.shop_list_distance);// 距离;
		strsOrderBy[2] = getResources().getString(R.string.shop_list_show);// 销量;
		strsOrderBy[3] = getResources().getString(R.string.shop_list_com);// 评价;
		strsOrderBy[4] = getResources().getString(R.string.shop_list_new);// 最新入住;
		/*
		 * strsOrderBy[4] = "service"; strsOrderBy[5] = "speed"; strsOrderBy[6]
		 * = "price";
		 */
		strsActivitys = new String[3];
		strsActivitys[0] = getResources().getString(R.string.shop_list_all);// "所有商家";
		strsActivitys[1] = getResources()
				.getString(R.string.shop_list_activity);// 有活动商家;
		strsActivitys[2] = getResources().getString(
				R.string.shop_list_n_activity);// 无活动商家;
		// strsActivitys[3] = "外卖+预定";
		Init();

		app.userInfo = OtherManager.getUserInfo(this);

		this.updateReceiver = new UpdateReceiver();
		IntentFilter localIntentFilter1 = new IntentFilter(
				HJAppConfig.UPDATELOCATION);
		registerReceiver(this.updateReceiver, localIntentFilter1);

		IntentFilter localIntentFilter2 = new IntentFilter(
				"com.ihangjing.common.ReShowRefreshPicture");
		registerReceiver(this.updateReceiver, localIntentFilter2);

		Log.v("MainActivity", "registerReceiver()");

		this.prefrence = Preferences.getInstance(this);

		getshoptype();

		// GetPopADV();
		CheckUpdate();
		if (app.useLocation == null
				|| app.useLocation.getAddressDetail() == null
				|| app.useLocation.getAddressDetail().length() < 1) {
			showDialog(SHOW_LOCATION);
		} else {// 一进来就已经定位好了
			Message localMessage1 = new Message();
			localMessage1.what = UIHandler.DO_GET_LOCATION_SUCCESS;
			mHandler.sendMessage(localMessage1);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(updateReceiver);

	}

	protected void gotoCity() {
		// TODO Auto-generated method stub
		isSelectCity = true;
		Intent intent = new Intent(MainActivity.this, SelCity.class);
		intent.putExtra("isSearch", false);
		intent.putExtra("isReturn", true);
		startActivity(intent);
	}

	protected void gotoArea() {
		// TODO Auto-generated method stub
		isSelectArea = true;
		Intent intent = new Intent(MainActivity.this, SelSection.class);

		intent.putExtra("cityid", app.useLocation.getCityID());
		intent.putExtra("isReturn", true);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSchedule.notifyDataSetChanged();
		if (shopType == 2) {
			// if()
			removeDialog(SHOW_LOCATION);
			imgLocation.setVisibility(View.GONE);
			tv_myLocation.setGravity(Gravity.CENTER);
			;
			btnLeft.setVisibility(View.VISIBLE);
			if (isSelectCity || isSelectArea) {
				if (app.useLocation.getCityID() != null
						&& app.useLocation.getCityID().length() > 0) {
					isSelectCity = false;
					isSelectArea = false;
					GetNearShop(1);
				} else {
					gotoCity();
					return;
				}

			}
			tv_myLocation.setText(app.useLocation.getAreaName());
			btnLeft.setText(app.useLocation.getCityName());
		}
	}

	public void ShowOrerBy() {
		if (orderByView == null) {
			if (shopTypeView != null) {
				shopTypeView.Close();
			}
			if (activitysView != null) {
				activitysView.Close();
			}
			orderByView = new HJPopListView(MainActivity.this, strsOrderBy,
					RlParent, RelativeLayout.LayoutParams.FILL_PARENT,
					RlParent.getHeight(), R.id.ll_btn_opt);
			orderByView.setOnItemClick(new onItemClick() {

				@Override
				public void onClickItem(int position) {
					// TODO Auto-generated method stub
					if (position > -1) {
						btnShopType.setText(strsOrderBy[position]);
						btnShopType1.setText(strsOrderBy[position]);
						if (position == 0) {
							sortname = "SortNum";
							sortflag = 1;
							btnShopType.setSelected(false);
							btnShopType1.setSelected(false);
						} else {
							if (lastPosition != position) {// 上次不是用了这个排序
								lastPosition = position;
								btnShopType.setType(1);
								btnShopType1.setType(1);
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
							btnShopType1.setSelected(true);
							sortflag = btnShopType.getType() - 1;
						}

						// orderByID = String.valueOf((int)Math.pow(2, position
						// - 1));

						// pageindex = 1;
						GetNearShop(1);
					}
					orderByView = null;
				}
			});
			orderByView.Show();
		} else {
			orderByView.Close();
		}
	}

	public void ShowActivitys() {
		if (activitysView == null) {
			if (shopTypeView != null) {
				shopTypeView.Close();
			}
			if (orderByView != null) {
				orderByView.Close();
			}
			activitysView = new HJPopListView(MainActivity.this, strsActivitys,
					RlParent, RelativeLayout.LayoutParams.FILL_PARENT,
					RlParent.getHeight(), R.id.ll_btn_opt);
			activitysView.setOnItemClick(new onItemClick() {

				@Override
				public void onClickItem(int position) {
					// TODO Auto-generated method stub
					if (position > -1) {
						isPromotion = position;
						btnShopTop.setText(strsActivitys[position]);
						btnShopTop1.setText(strsActivitys[position]);

						GetNearShop(1);
					}
					activitysView = null;
				}
			});
			activitysView.Show();
		} else {
			activitysView.Close();
		}
	}

	void ShowShopType() {
		btnOrderBy.setSelected(true);
		btnOrderBy1.setSelected(true);
		if (shopTypeView == null) {
			if (orderByView != null) {
				orderByView.Close();
			}
			if (activitysView != null) {
				activitysView.Close();
			}
			if (strsSortName == null || strsSortName.length < 2) {
				/*
				 * Toast.makeText(FsgShopList.this, "数据记载中。。。请稍后！", 15).show();
				 * isShowShopType = true; return;
				 */
				strsSortName = new String[app.shopTypeListModel.list.size() + 1];
				strsSortName[0] = getResources().getString(
						R.string.public_all_type)/* "所有商家" */;
				for (int i = 0; i < app.shopTypeListModel.list.size(); i++) {
					strsSortName[i + 1] = app.shopTypeListModel.list.get(i).SortName;
				}
			}
			shopTypeView = new HJPopListView(MainActivity.this, strsSortName,
					RlParent, RelativeLayout.LayoutParams.FILL_PARENT,
					RlParent.getHeight(), R.id.ll_btn_opt);

			shopTypeView.setOnItemClick(new onItemClick() {

				@Override
				public void onClickItem(int position) {
					// TODO Auto-generated method stub
					if (position > -1) {
						if (position == 0) {
							shoptype = "0";
							btnOrderBy.setText(getResources().getString(
									R.string.public_all_type));
							btnOrderBy1.setText(getResources().getString(
									R.string.public_all_type));
						} else {
							if (app.shopTypeListModel.list.get(position - 1)
									.getSortID().compareTo("23") == 0) {
								Intent intent = new Intent(MainActivity.this,
										AddRunOrder.class);
								startActivity(intent);
								btnOrderBy.setSelected(true);
								btnOrderBy1.setSelected(true);
								shopTypeView = null;
								return;
							}
							shoptype = app.shopTypeListModel.list.get(
									position - 1).getSortID();
							btnOrderBy.setText(app.shopTypeListModel.list.get(
									position - 1).getSortName());
							btnOrderBy1.setText(app.shopTypeListModel.list.get(
									position - 1).getSortName());
						}

						pageindex = 1;
						GetNearShop(1);
					}
					btnOrderBy.setSelected(true);
					btnOrderBy1.setSelected(true);
					shopTypeView = null;
				}
			});
			shopTypeView.Show();

			// RlParent.addView(shopTypeView);
		} else {
			btnOrderBy.setSelected(true);
			btnOrderBy1.setSelected(true);
			shopTypeView.Close();
		}
	}

	private void Init() {

		llCity = (LinearLayout) findViewById(R.id.LinearLayout04);

		llCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent localIntent0 = new Intent(MainActivity.this,
						SearchOnMap.class);
				startActivity(localIntent0);
			}
		});
		imgLocation = (ImageView) findViewById(R.id.main_location_refresh);
		etSearchKey = (EditText) findViewById(R.id.searchshop_keyword);
		btnLeft = (Button) findViewById(R.id.title_bar_left_btn);
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoCity();
			}
		});
		Button btnSearch = (Button) findViewById(R.id.title_bar_right_btn);

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				if (etSearchKey.getVisibility() == View.GONE) {

				} else {

					llCity.setVisibility(View.VISIBLE);
					if (shopType == 2) {
						btnLeft.setVisibility(View.VISIBLE);
					}
					if (etSearchKey.getText().toString().length() < 1) {
						// etSearchKey.setError(getResources().getString(R.string.search_notice)/*"请输入搜索条件"*/);
						// Toast.makeText(MainActivity.this,
						// getResources().getString(R.string.search_notice)/*"请输入搜索条件！"*/,
						// 15).show();
						return;
					}

					Intent localIntent1 = new Intent(MainActivity.this,
							FsgShopList.class);
					localIntent1.putExtra("isRem", 3);// 搜索商家
					localIntent1.putExtra("shopType", shopType);
					localIntent1.putExtra("searchKey", etSearchKey.getText()
							.toString());// 是否推荐
					startActivity(localIntent1);
				}
			}
		});

		RlParent = (RelativeLayout) findViewById(R.id.rl_parent);
		llOpt = (LinearLayout) findViewById(R.id.ll_btn_opt);
		btnOrderBy = (HJSelectButton) findViewById(R.id.btn_orderby);
		btnOrderBy.setUpDowDraw(R.drawable.ic_arrow_down,
				R.drawable.ic_arrow_up);
		btnOrderBy.setSelected(true);
		btnOrderBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowShopType();
			}
		});
		btnShopType = (HJSelectButton) findViewById(R.id.btn_shoptype);
		btnShopType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowOrerBy();
			}
		});

		btnShopTop = (HJSelectButton) findViewById(R.id.btn_shoptop);
		btnShopTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowActivitys();
			}
		});
		LinearLayout llCity = (LinearLayout) findViewById(R.id.LinearLayout04);

		llCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (shopType == 1) {
					Intent localIntent0 = new Intent(MainActivity.this,
							SearchOnMap.class);
					startActivity(localIntent0);
				} else {
					gotoArea();
					;
				}

			}
		});

		lvContent = (HJListView) findViewById(R.id.lv_content);
		lvContent.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem >= 1) {
					llOpt.setVisibility(View.VISIBLE);
				} else {
					llOpt.setVisibility(View.GONE);
				}
				if (!isReadData
						&& pageindex > 0
						&& firstVisibleItem + visibleItemCount >= totalItemCount
						&& pageindex < total) {
					GetNearShop(pageindex + 1);
				}
				if (app.mLoadImage != null) {
					app.mLoadImage.doTask();
				}

			}
		});
		lvContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				position -= 2;// 有个头试图，必须减
				if (position < 0)
					return;// 必须啊，要不然可能闪退，点击分类的时候
				ShopListItemModel localShopModel = shopListModel[userIndex].list
						.get(position);
				if (localShopModel == null) {
					Log.v(TAG, "localShopModel == null");
					return;
				}

				// TODO:保存浏览记录
				app.setShop(localShopModel);
				Intent localIntent = new Intent(MainActivity.this,
						ShopDetail.class);
				startActivity(localIntent);
			}
		});

		inflater = (LayoutInflater) MainActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		heardView = inflater.inflate(R.layout.main_view_top, null);
		secHeardView = inflater.inflate(R.layout.shop_list_section, null);
		btnOrderBy1 = (HJSelectButton) secHeardView
				.findViewById(R.id.btn_orderby);
		btnOrderBy1.setUpDowDraw(R.drawable.ic_arrow_down,
				R.drawable.ic_arrow_up);
		btnOrderBy1.setSelected(true);
		btnOrderBy1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// lvContent.scrollTo((int)lvContent.getChildAt(1).getX(),
				// (int)lvContent.getChildAt(1).getY());
				lvContent.setSelection(1);
				llOpt.setVisibility(View.VISIBLE);
				ShowShopType();
			}
		});
		btnShopType1 = (HJSelectButton) secHeardView
				.findViewById(R.id.btn_shoptype);
		btnShopType1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// lvContent.scrollTo((int)lvContent.getChildAt(1).getX(),
				// (int)lvContent.getChildAt(1).getY());
				lvContent.setSelection(1);
				llOpt.setVisibility(View.VISIBLE);
				ShowOrerBy();
			}
		});

		btnShopTop1 = (HJSelectButton) secHeardView
				.findViewById(R.id.btn_shoptop);
		btnShopTop1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// lvContent.scrollTo((int)lvContent.getChildAt(1).getX(),
				// (int)lvContent.getChildAt(1).getY());
				lvContent.setSelection(1);
				llOpt.setVisibility(View.VISIBLE);
				ShowActivitys();
			}
		});
		// llOpt1 = (LinearLayout)findViewById(R.id.ll_btn_opt_1);
		adv = (HJScorllADV) heardView.findViewById(R.id.iv_adv);
		// app.mLoadImage = adv.getLoadImage();

		WindowManager wm = this.getWindowManager();
		float with = wm.getDefaultDisplay().getWidth();

		float height = with / 2.0f;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, (int) height);
		adv.setLayoutParams(params);

		vpShopType = (ViewPager) heardView.findViewById(R.id.vp_shop_type);
		rlTypePT = (RelativeLayout) heardView.findViewById(R.id.rl_type_pt);
		// 添加下面的指示器视图
		llViewDots = new LinearLayout(MainActivity.this);
		llViewDots.setGravity(Gravity.CENTER);
		listDots = new ArrayList<ImageView>();
		/*
		 * if (dotsBackground != null) {
		 * dotsBackground.setAlpha((int)(dotsBgAlpha * 255));
		 * llViewDots.setBackgroundDrawable(dotsBackground); }
		 */
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 30);
		params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		rlTypePT.addView(llViewDots, params1);
		/*
		 * View convertView = inflater.inflate(R.layout.shop_type_view, null);
		 * GridView gv = (GridView)convertView.findViewById(R.id.gridview);
		 * gv.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { // TODO Auto-generated method stub
		 * 
		 * } }); mShopTypeAdapter = new ShopTypeListAdapter(0);
		 * gv.setAdapter(mShopTypeAdapter); vpShopType.addView(convertView);
		 */
		vpShopType.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (llViewDots != null) {
					ImageView image;
					if (arg0 + 1 < listDots.size()) {
						image = listDots.get(arg0 + 1);
						image.setBackgroundResource(R.drawable.index_ico_03);
					}
					if (arg0 - 1 >= 0) {
						image = listDots.get(arg0 - 1);
						image.setBackgroundResource(R.drawable.index_ico_03);
					}
					image = listDots.get(arg0);
					image.setBackgroundResource(R.drawable.index_ico_hov_03);

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				arg0++;
			}
		});
		lvContent.addHeaderView(heardView);
		lvContent.addHeaderView(secHeardView);
		tv_myLocation = (TextView) findViewById(R.id.main_location);

		mSchedule = new ShopListViewAdapter();

		lvContent.setAdapter(mSchedule);
		// CheckUpdate();
	}

	private void getshoptype() {
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pid", "0");
		localHashMap.put("indexpage", "1");
		localHashMap.put("pagesize", "100");
		localHashMap.put("languageType", app.language);

		localHttpManager = new HttpManager(MainActivity.this,
				MainActivity.this, "Android/GetShopTypeList.aspx?",
				localHashMap, 1, GET_SHOP_TYPE);
		localHttpManager.postRequest();
	}

	private void getCityID(String name) {
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("name", name);
		localHashMap.put("languageType", app.language);

		localHttpManager = new HttpManager(MainActivity.this,
				MainActivity.this, "Android/GetCityidByCityname.aspx?",
				localHashMap, 1, GET_CITY_ID);
		localHttpManager.postRequest();
	}

	private void getAreaList(String name) {
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("name", name);
		localHashMap.put("id", "0");
		localHashMap.put("indexpage", "1");
		localHashMap.put("pagesize", "100");
		localHashMap.put("languageType", app.language);

		localHttpManager = new HttpManager(MainActivity.this,
				MainActivity.this, "Android/GetSectionByCityid.aspx?",
				localHashMap, 1, GET_AREA_LIST);
		localHttpManager.postRequest();
	}

	// 通过网络获取数据 获取完成后发送消息
	public void GetNearShop(int page) {

		// 显示数据加载中
		// downloadLayout.setVisibility(View.VISIBLE);
		if (!isReadData) {
			isReadData = true;
			double lon;
			double lat;
			if (app.useLocation != null) {
				lon = app.useLocation.getLon();
				lat = app.useLocation.getLat();
			} else {
				lon = 0.0;
				lat = 0.0;
			}

			// 链接网络保存数据
			HashMap<String, String> localHashMap = new HashMap<String, String>();

			localHashMap.put("languageType", app.language);
			localHashMap.put("lat", String.valueOf(lat));
			localHashMap.put("lng", String.valueOf(lon));
			localHashMap.put("pageindex", String.valueOf(page));
			localHashMap.put("pagesize", "8");
			localHashMap.put("Order", "0");
			if (shopType == 2) {
				localHashMap.put("cityID", app.useLocation.getCityID());
				localHashMap.put("areaID", app.useLocation.getAreaID());
			}
			localHashMap.put("sortname", sortname);
			localHashMap.put("sortflag", String.valueOf(sortflag));
			localHashMap.put("shoptype", shoptype);// 商家分类
			/*
			 * if (isPromotion == 0) { localHashMap.put("types", "3");// 商家分类
			 * }else{
			 */
			localHashMap.put("types", String.valueOf(shopType));// 商家分类
			// }

			localHttpManager = new HttpManager(MainActivity.this,
					MainActivity.this, "Android/GetShopListByLocation.aspx?",
					localHashMap, 2, GET_SHOP);

			localHttpManager.postRequest();

		}

	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;

		if (paramInt == 323) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.public_notice)/* "提示" */)
					.setMessage(
							getResources().getString(R.string.check_have_new)/* "有新版本可以更新，确定下载最新版本?" */);
			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					// 开始下载数据
					DownLoad();
				}
			};

			dialog = localBuilder1
					.setPositiveButton(
							getResources().getString(R.string.public_ok)/* "确定" */,
							local3)
					.setNegativeButton(
							getResources().getString(R.string.public_cancel)/* "取消" */,
							null).create();

			return dialog;
		} else if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);

		} else if (paramInt == SHOW_LOCATION) {
			dialog = OtherManager.createProgressDialog(MainActivity.this,
					getResources().getString(R.string.public_location_notice));
		}

		return dialog;
	}

	private void DownLoad() {
		MainActivity.this.removeDialog(323);
		// dialogStr = "正在下载最新版系统...";
		// showDialog(322);

		// 这里来检测版本是否需要更新
		mUpdateManager = new UpdateManager(this);
		mUpdateManager.checkUpdateInfo();
	}

	// 接受广播消息
	class UpdateReceiver extends BroadcastReceiver {
		UpdateReceiver() {
		}

		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals(HJAppConfig.UPDATELOCATION)) {
				Message localMessage1 = new Message();
				localMessage1.what = UIHandler.DO_GET_LOCATION_SUCCESS;
				MainActivity.this.mHandler.sendMessage(localMessage1);
				Log.v(LOG_TAG,
						"onReceive:com.ihangjing.common.updateMyLocation");
			} else if (paramIntent.getAction().equals(
					"com.ihangjing.common.ReShowRefreshPicture")) {

			}
		}
	}

	// 点击返回按钮提示退出系统
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Long time = System.currentTimeMillis();
			if (time - time1 > 1000) {
				time1 = time;
				Toast.makeText(MainActivity.this,
						getResources().getString(R.string.shop_list_exit), 5)
						.show();
				return true;
			}
			app.exit();
		}

		return true;
	}

	// 显示对话框
	private void j_showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}

				});
		builder.create().show();
	}

	// 列表项包含的组件
	static class ShopTypeView {
		RelativeLayout rlImg;
		TextView titleTextView;
	}

	class ShopTypeListAdapter extends BaseAdapter {

		// LayoutInflater inflater = null;
		int page;

		ShopTypeListAdapter() {
		}

		ShopTypeListAdapter(int n) {
			page = n;
		}

		@Override
		public int getCount() {
			int n = 0;
			if (app.shopTypeListModel.list.size() == 0) {
				n = 0;
			} else if ((page + 1) * typeSize <= app.shopTypeListModel.list
					.size()) {
				n = typeSize;
			} else {

				n = app.shopTypeListModel.list.size() % typeSize;
			}
			return n;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		public void toggle(int position) {
			this.notifyDataSetChanged();// date changed and we should refresh
										// the view
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ShopTypeView viewHolder;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.shop_type_view_item,
						null);

				viewHolder = new ShopTypeView();

				viewHolder.titleTextView = (TextView) convertView
						.findViewById(R.id.tv_name);
				viewHolder.rlImg = (RelativeLayout) convertView
						.findViewById(R.id.rl_img);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ShopTypeView) convertView.getTag();
			}
			position += page * typeSize;
			ShopTypeInfo modelInfo = app.shopTypeListModel.list.get(position);
			viewHolder.titleTextView.setText(modelInfo.getSortName());
			if (modelInfo.getSortPic() != null
					&& modelInfo.getSortPic().length() > 14) {// http://*.*/*.*
				viewHolder.rlImg.setTag(modelInfo.getSortPic());
				app.mLoadImage.addTask(modelInfo.getSortPic(),
						viewHolder.rlImg, R.drawable.nopic_shop);
			}
			// app.mLoadImage.doTask();
			return convertView;
		}
	}

	// 自定义adapter
	class ShopListViewAdapter extends BaseAdapter {

		ShopListViewAdapter() {
		}

		@Override
		public int getCount() {
			return shopListModel[userIndex].list.size();
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

				viewHolder = new ShopItemView();

				convertView = inflater.inflate(R.layout.shopitem, null);
				viewHolder.ivOpenStatus = (ImageView) convertView
						.findViewById(R.id.iv_open);
				viewHolder.iconImageView = (RelativeLayout) convertView
						.findViewById(R.id.itemImage);

				viewHolder.shopnameTextView = (TextView) convertView
						.findViewById(R.id.itemShopName);

				viewHolder.telTextView = (TextView) convertView
						.findViewById(R.id.itemTel);

				viewHolder.addressTextView = (TextView) convertView
						.findViewById(R.id.itemShopAddress);
				viewHolder.tvShopDiscount = (TextView) convertView
						.findViewById(R.id.tv_discount);

				viewHolder.tvDistance = (TextView) convertView
						.findViewById(R.id.tv_dis_sale);

				viewHolder.ordertimeTextView = (TextView) convertView
						.findViewById(R.id.itemOrderTime);
				viewHolder.tvShopCount = (TextView) convertView
						.findViewById(R.id.tv_shop_count);
				viewHolder.ivW = (ImageView) convertView
						.findViewById(R.id.iv_w);
				viewHolder.ivD = (ImageView) convertView
						.findViewById(R.id.iv_d);
				/*
				 * viewHolder.iconImageView .setOnClickListener(new
				 * OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { // TODO
				 * Auto-generated method stub String urlString = (String)
				 * v.getTag(); Bitmap img = app.mLoadImage.getBitmap(urlString);
				 * ImagePopViewDialog dialog = new ImagePopViewDialog(
				 * MainActivity.this, img); dialog.show(); } });
				 */
				viewHolder.tvTypeTextView = (TextView) convertView
						.findViewById(R.id.tv_notic);
				viewHolder.tvSendTime = (TextView) convertView
						.findViewById(R.id.tv_send_time);
				viewHolder.llICON = (LinearLayout) convertView
						.findViewById(R.id.ll_icon);
				viewHolder.ivLine = (ImageView) convertView
						.findViewById(R.id.iv_line);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ShopItemView) convertView.getTag();
			}

			localShoplist = shopListModel[userIndex].list.get(position);

			if (localShoplist != null) {

				viewHolder.shopnameTextView.setText(localShoplist.getName());
				if (app.shopCartModel != null) {
					int count = app.shopCartModel.getFoodCountWithShopID(
							localShoplist.getId(),
							localShoplist.shopIndexInShopCart);
					if (count > 0) {
						viewHolder.tvShopCount.setVisibility(View.VISIBLE);
						viewHolder.tvShopCount.setText(String.valueOf(count));
					} else {
						viewHolder.tvShopCount.setVisibility(View.GONE);
					}
				} else {
					viewHolder.tvShopCount.setVisibility(View.GONE);
				}
				String m = getResources().getString(R.string.public_moeny_0);// 远
				viewHolder.tvDistance.setText(Html
						.fromHtml("距离：<font color='red'><b>"
								+ localShoplist.getDis()
								+ "KM</b></font> 销量：<font color='red'><b>"
								+ localShoplist.getSales() + "</b></font>"));
				viewHolder.telTextView.setText(Html.fromHtml(getResources()
						.getString(R.string.shop_list_send_fee)// "配送费:"
						+ "<font color='red'><b>"
						+ m
						+ localShoplist.getSendMoney()
						+ "</b></font> "
						+ getResources().getString(
								R.string.shop_list_send_start) // 起送金额
						+ "<font color='red'><b>"
						+ m
						+ localShoplist.getMinmoney() + "</b></font>"));
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
				// viewHolder.ivOpenStatus.setVisibility(View.GONE);
				if (shopType == 2) {
					if (localShoplist.getGrade() > 0) {
						viewHolder.telTextView.setText(String.format("%d%%好评",
								localShoplist.getGrade()));

					} else {
						viewHolder.telTextView.setText("暂无评论");
					}
					// viewHolder.telTextView.setVisibility(View.GONE);
					// viewHolder.ordertimeTextView.setVisibility(View.GONE);
					viewHolder.ordertimeTextView.setCompoundDrawables(null,
							null, null, null);
					viewHolder.ordertimeTextView.setText(String.format("销量：%s",
							localShoplist.getSales()));
				}
				viewHolder.tvTypeTextView.setVisibility(View.GONE);
				if (shopType != 2 && localShoplist.getStatus() == 1) {
					viewHolder.ivOpenStatus.setVisibility(View.VISIBLE);
				} else {
					viewHolder.ivOpenStatus.setVisibility(View.GONE);
				}

				/*
				 * switch (localShoplist.getShopType()) { case 1:
				 * viewHolder.ivW.setVisibility(View.VISIBLE); break; case 2:
				 * viewHolder.ivD.setVisibility(View.VISIBLE); break; default:
				 * viewHolder.ivW.setVisibility(View.VISIBLE);
				 * viewHolder.ivD.setVisibility(View.VISIBLE); break; }
				 */

				viewHolder.addressTextView.setText(localShoplist.getAdress());
				viewHolder.tvSendTime.setText(localShoplist.getDesc());

				if (localShoplist.getShopDiscount() != 0.0f
						&& localShoplist.getShopDiscount() < 10.0f) {
					viewHolder.tvShopDiscount.setVisibility(View.VISIBLE);
					viewHolder.tvShopDiscount.setText(localShoplist
							.getShopDiscount()
							+ getResources().getString(R.string.public_zhe));// "折");
				} else {
					viewHolder.tvShopDiscount.setVisibility(View.GONE);
				}

				/*
				 * viewHolder.tvSendTime.setText("<" + localShoplist.getDis() +
				 * getResources().getString(R.string.public_distance));
				 */// "公里");

				// 绘制边框 显示商家logo

				if (localShoplist.getIcon().length() > 16) {
					viewHolder.iconImageView.setTag(localShoplist.getIcon());
					app.mLoadImage.addTask(localShoplist.getIcon(),
							viewHolder.iconImageView, R.drawable.nopic_shop);
				} else {
					viewHolder.iconImageView
							.setBackgroundResource(R.drawable.nopic_shop);
				}
				viewHolder.llICON.removeAllViews();
				if (localShoplist.tagListModel.list.size() > 0) {
					viewHolder.ivLine.setVisibility(View.VISIBLE);
					for (int i = 0; i < localShoplist.tagListModel.list.size(); i++) {
						TagModel model = localShoplist.tagListModel.list.get(i);
						ShopDiscountItemView discountItemView = new ShopDiscountItemView();
						View view = model.getView();
						if (view == null) {
							view = inflater.inflate(
									R.layout.shop_discount_view_item, null);
							;
							discountItemView.tvInfo = (TextView) view
									.findViewById(R.id.tv_info);
							discountItemView.rlImg = (RelativeLayout) view
									.findViewById(R.id.rl_img);
							// model.setView(view);//首页开启了这个会报错，不知道什么原因
						} else {
							discountItemView.tvInfo = (TextView) view
									.findViewById(R.id.tv_info);
							discountItemView.rlImg = (RelativeLayout) view
									.findViewById(R.id.rl_img);
						}
						if (model.getImageNet().length() > 16) {

							discountItemView.rlImg.setTag(model.getImageNet());
							app.mLoadImage.addTask(model.getImageNet(),
									discountItemView.rlImg,
									R.drawable.food_class);
						}
						viewHolder.llICON.addView(view);
						discountItemView.tvInfo.setText(model.getInfo());
					}
				} else {
					viewHolder.ivLine.setVisibility(View.GONE);
				}
			}

			return convertView;
		}
	}

	public class HJPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public HJPagerAdapter(List<View> listViews) {
			// TODO Auto-generated constructor stub
			this.mListViews = listViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}

}
