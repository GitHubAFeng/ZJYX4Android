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

	public static final String unpayType = "00"; // 01����ϵͳ�У�00��ʽϵͳ��

	private static final double EARTH_RADIUS = 6378137;//����뾶��λ��;

	//static EasyEatApplication mDemoApp;

	// ��ȨKey http://dev.baidu.com/wiki/static/imap/key/
	public String mStrKey = "F784C10D1B1E2C0261181DC6895E4953EA1CD0E3";// getString(R.string.baidu_map_key);

	boolean m_bKeyRight = true; // ��ȨKey��ȷ����֤ͨ��
	private String mData; // ��ȡ������
	// ��λʱ����
	private int myLocationTime = 5 * 1000;
	// �Ƿ������˶�λAPI
	private boolean isOpenLocation = false;
	// ��γ��
	

	private UpdateReceiver updateReceiver;

	public String version = "1.0.0";
	// �汾������ʷ
	// 1.0.0 2012.4.12 ��ʽ�����汾
	// 1.0.1 2012.8.15 �޸��˵��ӿ�������������
	// 1.0.2 2012.8.17 �޸�������Ϣ�Ĵ����Լ��������ǵ�����

	public String cid = "1";

	public int LocationType = 0;// 0 ������λ 1 λ�ñ仯��λ

	private List<Activity> activityList = new LinkedList<Activity>();

	public ShopListItemModel mShop;

	public MyShopCart shopCartModel;

	public UserDetail userInfo;

	// public SectionInfo mSearchSection;//��������
	public SectionInfo mSection;// ��������
	public SectionInfo sSection;// ������ַʱ��ѡ�������

	public ReciveAddressListModel reciveAddressList;// ��ַ��

	public int selectIndex = 0;// ��ַ���е�ǰѡ�еĵ�ַ����

	public int foodListType = 0;// �����Ʒ�б�ķ�ʽ0��ʲôҲ���ǣ�1ѡ���˷��� 2:ʳƷid��
	public int viewID;// ��foodListTypeΪ1����ô�Ƿ�����������foodListTypeΪ2��ô��ʳƷid

	protected int buildID;// ��ǰ��������

	private static EasyEatApplication instance;

	public FoodTypeListModel foodTypeList;

	public boolean commentSucess = false;

	public boolean geiverCoupon = false;

	public MyCouponsListModel couponsList;// �Ż�ȯ�б���ShopCart�����ʼ�������겢���
	public MyCouponsListModel couponsKeyList;// ѡ�е��Ż�ȯ�б���ShopCart�����ʼ�������겢���

	public PopAdvListModel popAdvList;

	public LoadImage mLoadImage;

	public boolean isReturn = false;

	private LocationClient mLocationClient;

	private BDLocationListener myListener = new MyLocationChangedListener();

	
	public int locationType = 0;//0ʹ���ҵĵ�ַ��1ʹ��Ҫʹ�õĵ�ַ

	private BroadcastReceiver mBroadcastRec;
	public BDLocation myLocation;//�û���ǰ��ַ
	public MyLocationModel useLocation;//Ҫʹ�õĵ�ַ��Ϣ
	protected MyLocationModel searchLocation;//������ַʱ��������

	public boolean isViewOrderList = false;//�����Ա�����Ƿ�鿴�����б����

	public int getSaleType = 0;//0 ��ȡ������ 1��ȡ��ɱ 2��ȡ���� 3��ȡ�Ź�

	public boolean isShowSale;//�Ƿ���ʾ�Ż���Ϣ

	protected GiftTypeListModel mGiftTypeList;

	public String language = "1";
	
	public final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

	public String newOrderID;

	public int showOrderType;

	public int payState = 0;

	public ShopTypeListModel shopTypeListModel;

	public boolean isUpDataLocation;//��λ�ɹ����Ƿ��������λ�õ�����

	public FoodModel mFood;

	public ArrayList<FoodModel> buyFoodList;
	public MyLocationModel reciverLocation;

	public MyLocationModel userLocation;

	// ����ģʽ�л�ȡΨһ��MyApplicationʵ��
	public static EasyEatApplication getInstance() {
		if (null == instance) {
			instance = new EasyEatApplication();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
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
			if (!isOpenLocation) // ���û�д�
			{
				LocationClientOption option = new LocationClientOption();
				option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
				option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
				option.setScanSpan(5000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
				option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
				option.setNeedDeviceDirect(true);// ���صĶ�λ��������ֻ���ͷ�ķ���;
				option.setOpenGps(true);// �����Ƿ��gps��ʹ��gpsǰ�����û�Ӳ����gps��Ĭ���ǲ���gps��
				option.setProdName(HJAppConfig.CookieName + "Lo");// ���ò�Ʒ�����ơ�ǿ�ҽ�����ʹ���Զ���Ĳ�Ʒ�����ƣ����������Ժ�Ϊ���ṩ����Ч׼ȷ�Ķ�λ����
				mLocationClient.setLocOption(option);

				mLocationClient.registerLocationListener(myListener);// ע���������

				isOpenLocation = true; // ��ʶΪ�Ѿ����˶�λ
				mLocationClient.start();

				Log.i(TAG, "StartLocation Success");
			}
		} catch (Exception e) {
			Log.i(TAG, "�򿪶�λ�쳣" + e.toString());
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
			// �˴���Ҫ����Ȩ��:android.permission.CHANGE_CONFIGURATION
			// �����µ��� onCreate();
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
        // getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
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
		myLocation = getLocalLocation();//��ȡ�����ڱ��صľ�γ��
		//MyLocation = new double[2];
		//���Դ�
		//MyLocation[1] = 120.391042; MyLocation[0] = 30.312514;
		/*EasyEatApplication.this.myLocation = new BDLocation();
		useLocation = new MyLocationModel();
		useLocation.setLat(48.043317);
		useLocation.setLon(125.881984);
		useLocation.setAddressDetail("����·��������");
		useLocation.setStreet("����·��������");
		useLocation.setCityName("������");
		EasyEatApplication.this.myLocation.setLatitude(useLocation.getLat());
		EasyEatApplication.this.myLocation.setLongitude(useLocation.getLon());*/
		 
		//addressString = "�㽭ʡ�����н�������ɳѧ����541��";
		
		//���Դ� end
		
		//x86 ���Թر�
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		// iFilter.addAction(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE);
		mBroadcastRec = new SDKReceiver();
		registerReceiver(mBroadcastRec, iFilter);
		StartLocation();
		SDKInitializer.initialize(getApplicationContext());
		Log.v(TAG, "EasyEatApplication oncreate");
		//x86 ���Թر� end
		
		Log.v(TAG, this.mStrKey);

		mApi = new EasyEatAndroid();
		mSection = OtherManager.GetSection(this);
		mContext = this.getApplicationContext();
		// mImageManager = new ImageManager(this);

		// Ϊcmwap�û����ô�������
		String type = getNetworkType();
		if (null != type && type.equalsIgnoreCase("cmwap")) {
			Toast.makeText(this, "����ǰ����ʹ��cmwap��������.", Toast.LENGTH_SHORT);
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
		 * �첽��ȡ��ǰλ�á��������Ϊ����һ�ζ�λ���������첽�ģ������������أ�
		 * ����������������λ�����ReceiveListener�ķ���OnReceive�����Ĳ����з��ء�
		 * ���������λʧ�ܣ�OnReceive�����Ĳ���Ϊnull����λʧ�ܵ�ԭ������������쳣��
		 * ��ȡ��λ����żȻʧЧ�ȣ���ʱ���Ը�����Ҫ�ٴε���getlocation����һ�ζ�λ��
		 */

		Log.v(TAG, "DoGetLocation E");
	}

	public String getNetworkType() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		// NetworkInfo mobNetInfo = connectivityManager
		// .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (activeNetInfo != null) {
			return activeNetInfo.getExtraInfo(); // ���������: �����ƿɱ��û�������� ��: cmwap,
													// cmnet,
													// internet ...
		} else {
			return null;
		}
	}

	@Override
	// ��������app���˳�֮ǰ����mapadpi��destroy()�����������ظ���ʼ��������ʱ������
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
			Log.i(TAG, "stringת����double ʧ�� ");
		}

		return v;
	}

	

	/***
	 * ����γ�ȱ����ڱ���
	 * 
	 * @return
	 */
	private int setLocalLocation() {

		int r = 0;
		try {
			// ��ȡ��� preferences ����
			SharedPreferences usersetting = getApplicationContext()
					.getSharedPreferences(HJAppConfig.CookieName,
							Context.MODE_PRIVATE);
			// ��ȡ�༭����
			Editor userinfoeditor = usersetting.edit();
			userinfoeditor.putString("Location_longitude", String.valueOf(useLocation.getLon()));// ����
			userinfoeditor.putString("Location_latitude", String.valueOf(useLocation.getLat())); // γ��
			userinfoeditor.putString("Location_address", useLocation.getAddressDetail());// ��ַ
			userinfoeditor.putString("Location_city", useLocation.getCityName());// ��ַ
			userinfoeditor.commit();

			if (OtherManager.DEBUG) {
				Log.v(TAG, "�ҵĵ�ַ��" + useLocation.getAddressDetail());
			}

			return 1;

		} catch (Exception e) {
			Log.i(TAG, "  ���澭γ�ȣ�������ʧ��" + e.toString());
			r = 0;
		}
		return r;
	}

	/***
	 * ��ȡ���صľ�γ��
	 * 
	 * @return
	 */
	private BDLocation getLocalLocation() {
		
		double[] myLocation = new double[] { 0, 0 };
		try {
			// ��ȡ��� preferences ����
			SharedPreferences usersetting = getApplicationContext()
					.getSharedPreferences(HJAppConfig.CookieName,
							Context.MODE_PRIVATE);
			double longitude = Double.parseDouble(usersetting.getString(
					"Location_longitude", "0")); // ����
			double latitude = Double.parseDouble(usersetting.getString(
					"Location_latitude", "0")); // γ��
			myLocation[0] = longitude;
			myLocation[1] = latitude;
		} catch (NumberFormatException e) {
			//myLocation = new double[] { 0, 0 };
			Log.i(TAG, "��ֵ�쳣" + e.toString());
		} catch (Exception e) {
			//myLocation = new double[] { 0, 0 };
			Log.i(TAG, "��ȡ�쳣" + e.toString());
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

			// ���½��ж�λ
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
				// key ��֤ʧ�ܣ�ʾ����ʾ��ʾ��
				Toast.makeText(getApplicationContext(), "Key ��֤ʧ��", 15).show();
			} else if (action
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				// network error
				Toast.makeText(getApplicationContext(), "�������", 15).show();
			} else if (action
					.equals(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE)) {
				// key ��֤ʧ�ܣ�Intent�и�����Ϣ�������
				Toast.makeText(getApplicationContext(), "Key ��֤ʧ��1", 15).show();
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
	// ����onLocationChanged��ʾ��ǰλ�øı䡣����λSDK̽�⵽�ƶ��豸λ�øı�ʱ������ýӿ���ķ�����
	public class MyLocationChangedListener implements BDLocationListener {
		/*
		 * @Override public void onLocationChanged() { LocationType = 1; //
		 * �ڴ���ӣ�λ�øı䴥���Ĺ��ܡ� // ���½��ж�λ // ͨ�������onReceive ���յ�λ����Ϣ����µ���Ӧ��ҳ���� //
		 * ���»�ȡ��λ��Ϣ���㲥��λ�� // ���½��ж�λ // �򿪰ٶȵ�ͼ��λAPI ע�ⲻͬ�ڰٶȵ�ͼAPI DoGetLocation();
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

			// ��λ�ɹ� ��㲥 ��MainActivity�н��յ���Ϣ

			// if (location.getLocType() == 0) {
			
		}

	}
}
