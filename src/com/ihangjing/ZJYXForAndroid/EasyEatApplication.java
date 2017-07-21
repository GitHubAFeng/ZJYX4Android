package com.ihangjing.ZJYXForAndroid;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.ihangjing.Model.FoodListModel;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.FoodTypeListModel;
import com.ihangjing.Model.GiftTypeListModel;
import com.ihangjing.Model.MyCouponsListModel;
import com.ihangjing.Model.MyLocationModel;
import com.ihangjing.Model.MyShopCart;
import com.ihangjing.Model.PopAdvListModel;
import com.ihangjing.Model.ReciveAddressListModel;
import com.ihangjing.Model.SectionInfo;
import com.ihangjing.Model.ShopCartModel;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.Model.ShopTypeListModel;
import com.ihangjing.Model.UserDetail;
import com.ihangjing.app.LazyImageLoader;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.util.HJAppConfig;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class EasyEatApplication extends Application {
	public static final String TAG = "Application";

	// public static ImageManager mImageManager;
	public static Context mContext;
	public static SharedPreferences mPref;

	public static int networkType = 0;
	public static EasyEatAndroid mApi; // new API
	//public final static boolean DEBUG = Configuration.getDebug();

	public static final String unpayType = "00"; // 01测试系统中，00正式系统中

	private static final double EARTH_RADIUS = 6378137;//地球半径单位米;

	//static EasyEatApplication mDemoApp;

	// 授权Key http://dev.baidu.com/wiki/static/imap/key/
	public String mStrKey = "F784C10D1B1E2C0261181DC6895E4953EA1CD0E3";// getString(R.string.baidu_map_key);

	boolean m_bKeyRight = true; // 授权Key正确，验证通过
	private String mData; // 获取的数据
	// 定位时间间隔
	private int myLocationTime = 5 * 1000;
	// 是否启动了定位API
	private boolean isOpenLocation = false;
	// 经纬度
	

	private UpdateReceiver updateReceiver;

	public String version = "1.0.0";
	// 版本更新历史
	// 1.0.0 2012.4.12 正式发布版本
	// 1.0.1 2012.8.15 修复菜单接口域名错误问题
	// 1.0.2 2012.8.17 修复分享信息的错误以及关于我们的资料

	public String cid = "1";

	public int LocationType = 0;// 0 正常定位 1 位置变化后定位

	private List<Activity> activityList = new LinkedList<Activity>();

	public ShopListItemModel mShop;

	public MyShopCart shopCartModel;

	public UserDetail userInfo;

	// public SectionInfo mSearchSection;//搜索区域
	public SectionInfo mSection;// 设置区域
	public SectionInfo sSection;// 新增地址时，选择的区域

	public ReciveAddressListModel reciveAddressList;// 地址簿

	public int selectIndex = 0;// 地址簿中当前选中的地址索引

	public int foodListType = 0;// 进入餐品列表的方式0：什么也不是，1选择了分类 2:食品id；
	public int viewID;// 当foodListType为1，那么是分类索引，当foodListType为2那么是食品id

	protected int buildID;// 当前建筑物编号

	private static EasyEatApplication instance;

	public FoodTypeListModel foodTypeList;

	public boolean commentSucess = false;

	public boolean geiverCoupon = false;

	public MyCouponsListModel couponsList;// 优惠券列表，在ShopCart里面初始化，用完并清空
	public MyCouponsListModel couponsKeyList;// 选中的优惠券列表，在ShopCart里面初始化，用完并清空

	public PopAdvListModel popAdvList;

	public LoadImage mLoadImage;

	public boolean isReturn = false;

	private LocationClient mLocationClient;

	private BDLocationListener myListener = new MyLocationChangedListener();

	
	public int locationType = 0;//0使用我的地址，1使用要使用的地址

	private BroadcastReceiver mBroadcastRec;
	public BDLocation myLocation;//用户当前地址
	public MyLocationModel useLocation;//要使用的地址信息
	protected MyLocationModel searchLocation;//新增地址时搜索到的

	public boolean isViewOrderList = false;//进入会员中心是否查看订单列表界面

	public int getSaleType = 0;//0 获取买它增 1获取秒杀 2获取限量 3获取团购

	public boolean isShowSale;//是否显示优惠信息

	protected GiftTypeListModel mGiftTypeList;

	public String language = "1";
	
	public final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

	public String newOrderID;

	public int showOrderType;

	public int payState = 0;

	public ShopTypeListModel shopTypeListModel;

	public boolean isUpDataLocation;//定位成功后是否处理了这个位置的数据

	public FoodModel mFood;

	public ArrayList<FoodModel> buyFoodList;
	public MyLocationModel reciverLocation;

	public MyLocationModel userLocation;

	// 单例模式中获取唯一的MyApplication实例
	public static EasyEatApplication getInstance() {
		if (null == instance) {
			instance = new EasyEatApplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		setLocalLocation();
		if (isOpenLocation) {
			isOpenLocation = false;
			mLocationClient.unRegisterLocationListener(myListener);
			mLocationClient.stop();
		}
		unregisterReceiver(updateReceiver);
		for (Activity activity : activityList) {
			activity.finish();
			Log.v(TAG, activity.getPackageName());
		}
		System.exit(0);
	}
	public boolean checkMobilePhone(String paramString) {
		String str = paramString.replace("+86", "");
		return Pattern.compile("^(13|15|14|18|17)[0-9]{9}$").matcher(str).matches();
	}
	public void StartLocation() {
		Log.i(TAG, "StartLocation S");

		mLocationClient = new LocationClient(getApplicationContext());
		try {
			if (!isOpenLocation) // 如果没有打开
			{
				LocationClientOption option = new LocationClientOption();
				option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
				option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
				option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
				option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
				option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向;
				option.setOpenGps(true);// 设置是否打开gps，使用gps前提是用户硬件打开gps。默认是不打开gps的
				option.setProdName(HJAppConfig.CookieName + "Lo");// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
				mLocationClient.setLocOption(option);

				mLocationClient.registerLocationListener(myListener);// 注册监听函数

				isOpenLocation = true; // 标识为已经打开了定位
				mLocationClient.start();

				Log.i(TAG, "StartLocation Success");
			}
		} catch (Exception e) {
			Log.i(TAG, "打开定位异常" + e.toString());
		}

		Log.i(TAG, "StartLocation E");
	}
	private void updateLanguage(Locale locale) {
		Log.d("ANDROID_LAB", locale.toString());
		try {
			Object objIActMag, objActMagNative;
			Class clzIActMag = Class.forName("android.app.IActivityManager");
			Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
			Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
			// IActivityManager iActMag = ActivityManagerNative.getDefault();
			objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
			// Configuration config = iActMag.getConfiguration();
			Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
			Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
			config.locale = locale;
			// iActMag.updateConfiguration(config);
			// 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
			// 会重新调用 onCreate();
			Class[] clzParams = { Configuration.class };
			Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod(
					"updateConfiguration", clzParams);
			mtdIActMag$updateConfiguration.invoke(objIActMag, config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void changeAppLanguage(String lanAtr) {
		//Resources resources = getResources();
		//Configuration config = resources.getConfiguration();
       // DisplayMetrics dm = resources.getDisplayMetrics();
        if (lanAtr.equals("WY")) {
            //config.locale = new Locale("ar", "rEG");
            updateLanguage(new Locale("ar", "rEG"));
            language = "2";
        }else {
            //config.locale = new Locale("zh", "rCN");
            updateLanguage(new Locale("zh", "rCN"));
            language = "1";
        }
        //resources.updateConfiguration(config, dm);
        
    }
	@Override
	public void onCreate() {

		PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        //SharedPreferences localSharedPreferences = getSharedPreferences(HJAppConfig.CookieName, 0);
        //String language = localSharedPreferences.getString("language", "CN");
        //changeAppLanguage(language);
        //language = getResources().getConfiguration().locale.getCountry().equals("CN");
        if(getResources().getConfiguration().locale.getCountry().equals("CN")){
        	language = "1";
        }else{
        	language = "2";
        }
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(),0);
			version = packInfo.versionCode + "." + packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mLoadImage = new LoadImage(this);
		myLocation = getLocalLocation();//获取保存在本地的经纬度
		//MyLocation = new double[2];
		//测试打开
		//MyLocation[1] = 120.391042; MyLocation[0] = 30.312514;
		/*EasyEatApplication.this.myLocation = new BDLocation();
		useLocation = new MyLocationModel();
		useLocation.setLat(48.043317);
		useLocation.setLon(125.881984);
		useLocation.setAddressDetail("凤起路储蓄银行");
		useLocation.setStreet("凤起路储蓄银行");
		useLocation.setCityName("杭州市");
		EasyEatApplication.this.myLocation.setLatitude(useLocation.getLat());
		EasyEatApplication.this.myLocation.setLongitude(useLocation.getLon());*/
		 
		//addressString = "浙江省杭州市江干区下沙学正街541号";
		
		//测试打开 end
		
		//x86 测试关闭
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		// iFilter.addAction(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE);
		mBroadcastRec = new SDKReceiver();
		registerReceiver(mBroadcastRec, iFilter);
		StartLocation();
		SDKInitializer.initialize(getApplicationContext());
		Log.v(TAG, "EasyEatApplication oncreate");
		//x86 测试关闭 end
		
		Log.v(TAG, this.mStrKey);

		mApi = new EasyEatAndroid();
		mSection = OtherManager.GetSection(this);
		mContext = this.getApplicationContext();
		// mImageManager = new ImageManager(this);

		// 为cmwap用户设置代理上网
		String type = getNetworkType();
		if (null != type && type.equalsIgnoreCase("cmwap")) {
			Toast.makeText(this, "您当前正在使用cmwap网络上网.", Toast.LENGTH_SHORT);
			mApi.getHttpClient().setProxy("10.0.0.172", 80, "http");
		}

		this.updateReceiver = new UpdateReceiver();

		IntentFilter localIntentFilter = new IntentFilter(
				"com.ihangjing.common.reloadMyLocation");
		registerReceiver(updateReceiver, localIntentFilter);

		Log.v("EasyEatApplication", "registerReceiver");

		// DoGetLocation();
	}

	public void DoGetLocation() {
		Log.v(TAG, "DoGetLocation S");

		/*
		 * 异步获取当前位置。可以理解为发起一次定位，而且是异步的，所以立即返回，
		 * 不会引起阻塞。定位结果在ReceiveListener的方法OnReceive方法的参数中返回。
		 * 但是如果定位失败，OnReceive方法的参数为null。定位失败的原因可能有网络异常，
		 * 获取定位依据偶然失效等，这时可以根据需要再次调用getlocation发起一次定位。
		 */

		Log.v(TAG, "DoGetLocation E");
	}

	public String getNetworkType() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		// NetworkInfo mobNetInfo = connectivityManager
		// .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (activeNetInfo != null) {
			return activeNetInfo.getExtraInfo(); // 接入点名称: 此名称可被用户任意更改 如: cmwap,
													// cmnet,
													// internet ...
		} else {
			return null;
		}
	}

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub

		super.onTerminate();
	}

	private double stringToDouble(String str) {
		double v = 0.0;
		try {
			if (str != null && !str.trim().equals("")) {
				v = Double.parseDouble(str.trim());
			}
		} catch (NumberFormatException e) {
			Log.i(TAG, "string转换成double 失败 ");
		}

		return v;
	}

	

	/***
	 * 将经纬度保存在本地
	 * 
	 * @return
	 */
	private int setLocalLocation() {

		int r = 0;
		try {
			// 获取活动的 preferences 对象
			SharedPreferences usersetting = getApplicationContext()
					.getSharedPreferences(HJAppConfig.CookieName,
							Context.MODE_PRIVATE);
			// 获取编辑对象
			Editor userinfoeditor = usersetting.edit();
			userinfoeditor.putString("Location_longitude", String.valueOf(useLocation.getLon()));// 经度
			userinfoeditor.putString("Location_latitude", String.valueOf(useLocation.getLat())); // 纬度
			userinfoeditor.putString("Location_address", useLocation.getAddressDetail());// 地址
			userinfoeditor.putString("Location_city", useLocation.getCityName());// 地址
			userinfoeditor.commit();

			if (OtherManager.DEBUG) {
				Log.v(TAG, "我的地址：" + useLocation.getAddressDetail());
			}

			return 1;

		} catch (Exception e) {
			Log.i(TAG, "  保存经纬度，到本地失败" + e.toString());
			r = 0;
		}
		return r;
	}

	/***
	 * 获取本地的经纬度
	 * 
	 * @return
	 */
	private BDLocation getLocalLocation() {
		
		double[] myLocation = new double[] { 0, 0 };
		try {
			// 获取活动的 preferences 对象
			SharedPreferences usersetting = getApplicationContext()
					.getSharedPreferences(HJAppConfig.CookieName,
							Context.MODE_PRIVATE);
			double longitude = Double.parseDouble(usersetting.getString(
					"Location_longitude", "0")); // 经度
			double latitude = Double.parseDouble(usersetting.getString(
					"Location_latitude", "0")); // 纬度
			myLocation[0] = longitude;
			myLocation[1] = latitude;
		} catch (NumberFormatException e) {
			//myLocation = new double[] { 0, 0 };
			Log.i(TAG, "空值异常" + e.toString());
		} catch (Exception e) {
			//myLocation = new double[] { 0, 0 };
			Log.i(TAG, "获取异常" + e.toString());
		}
		BDLocation loc = new BDLocation();
		loc.setLatitude(myLocation[0]);
		loc.setLongitude(myLocation[1]);
		return loc;
	}

	class UpdateReceiver extends BroadcastReceiver {
		UpdateReceiver() {
		}

		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			String str1 = paramIntent.getAction();
			if (!str1.equals("com.ihangjing.common.reloadMyLocation")) {
				return;
			}
			Log.v("EasyEatApplication", "onReceive");

			// 重新进行定位
			if (mLocationClient != null) {
				if (mLocationClient.isStarted()) {
					mLocationClient.requestLocation();
				} else {
					mLocationClient.start();
					mLocationClient.requestLocation();
				}
			} else {
				StartLocation();
			}
			Log.v("EasyEatApplication", "BroadcastReceiver StartLocation() E");

			Log.v("EasyEatApplication", "BroadcastReceiver getLocation() E");
		}
	}

	public void setShop(ShopListItemModel localShopModel) {
		// TODO Auto-generated method stub
		mShop = localShopModel;
		mShop.isUpdate = true;
		mShop.isUpdateFoodList = true;
	}
	
	public class SDKReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action
					.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				// key 验证失败，示例显示提示框
				Toast.makeText(getApplicationContext(), "Key 验证失败", 15).show();
			} else if (action
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				// network error
				Toast.makeText(getApplicationContext(), "网络错误", 15).show();
			} else if (action
					.equals(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE)) {
				// key 验证失败，Intent中附加信息错误码键
				Toast.makeText(getApplicationContext(), "Key 验证失败1", 15).show();
			}
		}
	}
	public double rad(double value)
	{
	    return value * Math.PI / 180.0;
	}

	public double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{
	    double radLat1 = rad(lat1);
	    double radLat2 = rad(lat2);
	    double a = radLat1 - radLat2;
	    double b = rad(lng1) - rad(lng2);
	    double s = 2 * Math.sin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
	    s *= EARTH_RADIUS ;
	    s = Math.round(s * 10000) / 10000;
	    return s;
	}
	// 方法onLocationChanged表示当前位置改变。当定位SDK探测到移动设备位置改变时，会调用接口类的方法。
	public class MyLocationChangedListener implements BDLocationListener {
		/*
		 * @Override public void onLocationChanged() { LocationType = 1; //
		 * 在此添加，位置改变触发的功能。 // 重新进行定位 // 通过下面的onReceive 接收到位置信息后更新到相应的页面中 //
		 * 重新获取定位信息并广播新位置 // 重新进行定位 // 打开百度地图定位API 注意不同于百度地图API DoGetLocation();
		 * }
		 */

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null)
				return;
			
			  

			EasyEatApplication.this.myLocation = location;

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			if(useLocation == null ){
				useLocation = new MyLocationModel();
			}
			if (/*GetDistance(location.getLatitude(), location.getLongitude(), useLocation.getLat(), useLocation.getLon()) > 300*/
					GetDistance(location.getLatitude(), location.getLongitude(), 0.0, 0.0) < 1000 || 
					useLocation.getAddressDetail() == null || useLocation.getAddressDetail().length() == 0) {
					useLocation.setLocation(location);
						Intent localIntent = new Intent(
							HJAppConfig.UPDATELOCATION);
						EasyEatApplication.this.sendBroadcast(localIntent);
			}
			/*if (useLocation == null || useLocation.getAddressDetail() == null || useLocation.getAddressDetail().length() == 0) {
				useLocation = new MyLocationModel(location);
				Intent localIntent = new Intent(
						HJAppConfig.UPDATELOCATION);
				EasyEatApplication.this.sendBroadcast(localIntent);
			}*/
			
			/*if (!isUpDataLocation) {
				if (useLocation == null) {
					useLocation = new MyLocationModel(location);
				}else{
					useLocation.setLocation(location);
				}
				Intent localIntent = new Intent(
						HJAppConfig.UPDATELOCATION);
				EasyEatApplication.this.sendBroadcast(localIntent);
			}*/
			//setLocalLocation();

			// 定位成功 后广播 在MainActivity中接收到消息

			// if (location.getLocType() == 0) {
			
		}

	}
}
