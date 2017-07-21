package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMyLocationClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.ihangjing.Model.MyLocationModel;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.Model.ShopListModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.util.HJAppConfig;

public class SearchOnMap extends HjActivity{
	private double mlat;
	private double mlng;
	public ShopListModel shopListModel;
	private static final String TAG = "SearchOnMap";
	private static final double EARTH_RADIUS = 6378137;//����뾶����
	private UIHandler hander;
	ProgressDialog progressDialog = null;
	static MapView mapView = null;
	public View mPopView = null; // ���markʱ����������View

	Dialog dialog = null;
	

	private TextView titleTextView;
	private Button backButton;
	//private LinearLayout filter_panel;
	//private Button nearFilter;
	//private ListView nearListView;
	//private String nearFilterString = "1500";
	//private ArrayList<String> nearlist;
	//private ArrayAdapter<String> nearAdapter;
	private UpdateReceiver updateReceiver;
	private BaiduMap mBaiduMap;
	private BitmapDescriptor mCurrentMarker;
	public MyOverLayManager overlayManager;
	private PoiSearch mPoiSearch;
	private Button btnSearch;
	private EditText etKey;
	protected String dialogStr;
	protected boolean canShowLocation = false;
	protected ReverseGeoCodeResult pointPoi;
	private GeoCoder mPointPioSearch;
	public PoiInfo searchPoi;
	private Button rightButton;
	private boolean isSearch;
	private double Lat;
	private double Lon;
	private String address;
	private boolean isSelectCity;

	// 30.250763535948515 120.15386581420898
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// ���ô������չ���ԡ�������Window���ж���ĳ�����
		//requestWindowFeature(1);

		// SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.search_on_map);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isSearch = extras.getBoolean("isSearch");
			Lat = extras.getDouble("lat");
			Lon = extras.getDouble("lng");
			address = extras.getString("Address");
			
			
		}
		this.updateReceiver = new UpdateReceiver();
		IntentFilter localIntentFilter1 = new IntentFilter(
				HJAppConfig.UPDATELOCATION);
		registerReceiver(this.updateReceiver, localIntentFilter1);
		// handle
		hander = new UIHandler();
		this.shopListModel = new ShopListModel();

		mapView = (MapView) findViewById(R.id.bmap_view);
		mBaiduMap = mapView.getMap();
		
		// ��ͨ��ͼ
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(14);// ���õ�ͼ���ű�����17��100��
		mBaiduMap.setMapStatus(ms);
		if (isSearch && app.searchLocation == null) {
			app.searchLocation = new MyLocationModel();
			app.searchLocation.setCityName(app.useLocation.getCityName());
		}
		if (isSearch && Lat > 0.0f && Lon > 0.0) {
			
			Button button = new Button(getApplicationContext());
			button.setBackgroundResource(R.drawable.addr_bg);
			if (address.length() < 1) {
				address = getResources().getString(R.string.search_map_now)/*"���ڵ�ַ"*/;
			}
			app.searchLocation.setLat(Lat);
			app.searchLocation.setLon(Lon);
			app.searchLocation.setAddressDetail(address);
			button.setText(address);
			button.setTextColor(Color.WHITE);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			LatLng latLng = new LatLng(Lat, Lon);
			// ����������ʾ��InfoWindow�������
			// LatLng pt = new LatLng(result.getLocation(), 116.397428);
			// ����InfoWindow�ĵ���¼�������
			
			// ����InfoWindow
			InfoWindow mInfoWindow = new InfoWindow(button,
					latLng, 0);
			// ��ʾInfoWindow
			mBaiduMap.showInfoWindow(mInfoWindow);
			mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
		}else{
			mBaiduMap.setMyLocationEnabled(true);
			
			if (app.myLocation != null) {
				LatLng latLng = new LatLng(app.myLocation.getLatitude(), app.myLocation.getLongitude());
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
			}
		}
		
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			// ��ͼ�����¼��ص����� @param point ����ĵ�������

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				mPointPioSearch.reverseGeoCode(new ReverseGeoCodeOption()
				.location(arg0));
		        dialogStr = getResources().getString(R.string.public_get_data_notice)/*"��ȡ��ַ��Ϣ��...���Ժ�"*/;
				showDialog(322);
				// Toast.makeText(SearchOnMap.this, "onMap", 5).show();
			}

			// ��ͼ�� Poi �����¼��ص����� @param poi ����� poi ��Ϣ

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				//Toast.makeText(SearchOnMap.this, "onMapPoiClick", 5).show();
				mPointPioSearch.reverseGeoCode(new ReverseGeoCodeOption()
				.location(arg0.getPosition()));
		        dialogStr = getResources().getString(R.string.public_get_data_notice)/*"��ȡ��ַ��Ϣ��...���Ժ�"*/;
		        showDialog(322);
				
				return false;
			}

		});
		mBaiduMap.setOnMyLocationClickListener(new OnMyLocationClickListener() {

			@Override
			public boolean onMyLocationClick() {// ��λͼ��
				// TODO Auto-generated method stub
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.addr_bg);
				button.setText(app.myLocation.getAddrStr());
				button.setTextColor(Color.WHITE);
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (isSearch) {
							app.searchLocation = new MyLocationModel(app.myLocation);
						}else{
							app.useLocation = new MyLocationModel(app.myLocation);
							Intent localIntent = new Intent(
									HJAppConfig.UPDATELOCATION);
							SearchOnMap.this.sendBroadcast(localIntent);
						}
						
						finish();
					}
				});
				// ����������ʾ��InfoWindow�������
				// LatLng pt = new LatLng(result.getLocation(), 116.397428);
				// ����InfoWindow�ĵ���¼�������
				
				// ����InfoWindow
				InfoWindow mInfoWindow = new InfoWindow(button,
						new LatLng(app.myLocation.getLatitude(), app.myLocation.getLongitude()) , 0);
				// ��ʾInfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				return false;
			}
		});
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				// TODO Auto-generated method stub
				canShowLocation  = true;
			}
		});
		
		mPoiSearch = PoiSearch.newInstance();
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
			@Override
			public void onGetPoiResult(PoiResult result) {
				// ��ȡPOI�������
				removeDialog(322);
				// mPoiSearch.destroy();
				if (result == null
						|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
					Toast.makeText(SearchOnMap.this, getResources().getString(R.string.search_map_no_key_info)/*"δ�����������Ϣ������������ؼ���"*/, 15)
							.show();
					return;
				}

				if (result.error == SearchResult.ERRORNO.NO_ERROR) {
					// mBaiduMap.setMyLocationEnabled(false);
					mBaiduMap.clear();
					// ����PoiOverlay
					PoiOverlay overlay = new SearchPoiOverlay(mBaiduMap);
					// ����overlay���Դ����ע����¼�
					mBaiduMap.setOnMarkerClickListener(overlay);
					// ����PoiOverlay����
					overlay.setData(result);
					// ���PoiOverlay����ͼ��
					overlay.addToMap();
					overlay.zoomToSpan();
					return;
				} else {
					Toast.makeText(SearchOnMap.this, getResources().getString(R.string.search_map_search_error)/*"�����������Ժ����ԣ�"*/, 15).show();
				}
			}

			@Override
			public void onGetPoiDetailResult(PoiDetailResult result) {
				// ��ȡPlace����ҳ�������
			}

			@Override
			public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

		mPointPioSearch = GeoCoder.newInstance();
		OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					// û�м��������
				}
				// ��ȡ���������
			}

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				// mPointPioSearch.destroy();
				removeDialog(322);
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					// û���ҵ��������
					Toast.makeText(SearchOnMap.this, getResources().getString(R.string.search_map_no_key_info)/*"δ�ѻ�ȡ����ַ��Ϣ������������ؼ���"*/, 15)
							.show();
				}
				if (result.error == SearchResult.ERRORNO.NO_ERROR) {
					// ����InfoWindowչʾ��view
					pointPoi = result;
					Button button = new Button(getApplicationContext());
					button.setBackgroundResource(R.drawable.addr_bg);
					button.setText(result.getAddress());
					button.setTextColor(Color.WHITE);
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (isSearch) {
								app.searchLocation.setResult(pointPoi);
							}else{
								app.useLocation.setResult(pointPoi);
							Intent localIntent = new Intent(
									HJAppConfig.UPDATELOCATION);
							SearchOnMap.this.sendBroadcast(localIntent);
							}
							
							
							finish();
						}
					});
					// ����������ʾ��InfoWindow�������
					// LatLng pt = new LatLng(result.getLocation(), 116.397428);
					// ����InfoWindow�ĵ���¼�������
					
					// ����InfoWindow
					InfoWindow mInfoWindow = new InfoWindow(button,
							result.getLocation(), 0);
					// ��ʾInfoWindow
					mBaiduMap.showInfoWindow(mInfoWindow);
				} else {
					Toast.makeText(SearchOnMap.this, getResources().getString(R.string.search_map_search_error)/*"��ȡ��ַ�������Ժ����ԣ�"*/, 15)
							.show();
				}
				// ��ȡ������������
			}
		};
		mPointPioSearch.setOnGetGeoCodeResultListener(listener);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText(getResources().getString(R.string.search_map_title)/*"ѡ���ַ"*/);

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = null;
				// intent = new Intent(ShopList.this, SystemSet.class);
				// startActivity(intent);

				finish();
			}
		});
		
		rightButton = (Button)findViewById(R.id.title_bar_right_btn);
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				gotoCity();
			}
		});
		
		btnSearch = (Button) findViewById(R.id.searchshop_btn_search);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String city;
				if (isSearch) {
					city = app.searchLocation.getCityName();
				}else{
					city = app.useLocation.getCityName();
				}
				if (etKey.getText().length() < 1) {
					etKey.setError(getResources().getString(R.string.search_map_input_notict)/*"������Ҫ�����ĵ�ַ����"*/);
					return;
				}else if(city == null || city.length() < 1){
					gotoCity();
					return;
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etKey.getWindowToken(), 0);
				
				mPoiSearch.searchInCity((new PoiCitySearchOption())
						.city(city)
						.keyword(etKey.getText().toString()).pageNum(0));
				dialogStr = getResources().getString(R.string.public_get_data_notice)/*"������...�����Ժ�"*/;
				showDialog(322);
			}
		});
		etKey = (EditText) findViewById(R.id.main_et_search);
	}

	

	protected void gotoCity() {
		// TODO Auto-generated method stub
		isSelectCity = true;
		Intent intent = new Intent(SearchOnMap.this, SelCity.class);
		intent.putExtra("isSearch", isSearch);
		intent.putExtra("isReturn", true);
		startActivity(intent);
	}



	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);			
		}
		return dialog;
	}

	// ���ܹ㲥��Ϣ
	class UpdateReceiver extends BroadcastReceiver {
		UpdateReceiver() {
		}

		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals(
					HJAppConfig.UPDATELOCATION)) {
				Message localMessage1 = new Message();
				localMessage1.what = UIHandler.DO_GET_LOCATION_SUCCESS;
				hander.sendMessage(localMessage1);

			}
		}
	}
	
	
	
	private class SearchPoiOverlay extends PoiOverlay {
		public SearchPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			
			PoiInfo info = this.getPoiResult().getAllPoi().get(index);
			searchPoi = info;
			Button button = new Button(getApplicationContext());
			button.setBackgroundResource(R.drawable.addr_bg);
			button.setText(info.address);
			button.setTextColor(Color.WHITE);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isSearch) {
						app.searchLocation = new MyLocationModel(searchPoi);
					}else{
						app.useLocation = new MyLocationModel(searchPoi);
						Intent localIntent = new Intent(
								HJAppConfig.UPDATELOCATION);
						SearchOnMap.this.sendBroadcast(localIntent);
					}
					
					finish();
				}
			});
			// ����������ʾ��InfoWindow�������
			// LatLng pt = new LatLng(result.getLocation(), 116.397428);
			// ����InfoWindow�ĵ���¼�������
			
			// ����InfoWindow
			InfoWindow mInfoWindow = new InfoWindow(button,
					info.location, 0);
			// ��ʾInfoWindow
			mBaiduMap.showInfoWindow(mInfoWindow);
			return true;
		}
	}

	private class UIHandler extends Handler {

		public static final int DO_GET_LOCATION_SUCCESS = 1;
		static final int SET_SHOP_LIST = 12;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case DO_GET_LOCATION_SUCCESS:
				// ���춨λ����
				if (canShowLocation && mBaiduMap.isMyLocationEnabled()) {
						MyLocationData locData = new MyLocationData.Builder()
						.accuracy(app.myLocation.getRadius())
						// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
						.direction(app.myLocation.getDirection()).latitude(app.myLocation.getLatitude())
						.longitude(app.myLocation.getLongitude()).build();
						// ���ö�λ����
						mBaiduMap.setMyLocationData(locData);

						

						// ���ö�λͼ������ã���λģʽ���Ƿ���������Ϣ���û��Զ��嶨λͼ�꣩
						/*mCurrentMarker = BitmapDescriptorFactory
								.fromResource(R.drawable.iconmarka);*/
						MyLocationConfiguration config = new MyLocationConfiguration(
								LocationMode.NORMAL, true, null);
						mBaiduMap.setMyLocationConfigeration(config);					
				}
				break;
			case SET_SHOP_LIST: {// ��������ʧ�� ��ʾ��Ӧ����ʾ

				// progressDialog.dismiss();
				/*Drawable marker = getResources().getDrawable(
						R.drawable.iconmarka); // �õ���Ҫ���ڵ�ͼ�ϵ���Դ
				marker.setBounds(0, 0, marker.getIntrinsicWidth(),
						marker.getIntrinsicHeight());*/

				if (overlayManager == null) {
					overlayManager = new MyOverLayManager(mBaiduMap);
					overlayManager.SetShopList(shopListModel, app, SearchOnMap.this);
					overlayManager.addToMap();
					mBaiduMap.setOnMarkerClickListener(overlayManager);
				} else {
					overlayManager.removeFromMap();
					overlayManager.addToMap();
				}

				dialog.dismiss();
				// OtherManager.Toast(SearchOnMap.this,
				// "������ʾ�汾�̼�Ŀǰֻλ�ں���,��ͼ��λ�ں���,��ʽ�汾������ֻ��ĵ�ǰλ�ö�λ����ʾ�ܱ߿����͵��̼�");
			}
			}
		}
	}

	

	

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		if (isSearch) {
			rightButton.setText(app.searchLocation.getCityName());
			if (isSelectCity) {
				isSelectCity = false;
				if (app.searchLocation.getCityLat() > 0.0 && app.searchLocation.getCityLon() > 0.0) {
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(
							app.searchLocation.getCityLat(), app.searchLocation.getCityLon())));
				}
			}
		}else{
			rightButton.setText(app.useLocation.getCityName());
			if (isSelectCity) {
				isSelectCity = false;
				if (app.useLocation.getCityLat() > 0.0 && app.useLocation.getCityLon() > 0.0) {
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(
							app.useLocation.getCityLat(), app.useLocation.getCityLon())));
				}
			}
		}
		
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(updateReceiver);
		mPointPioSearch.destroy();
		mPoiSearch.destroy();
		mapView.onDestroy();
		
	}

	
}

class MyOverLayManager extends OverlayManager implements
		BaiduMap.OnMarkerClickListener {
	ShopListModel shopList;
	BitmapDescriptor bitmap;
	EasyEatApplication app;
	SearchOnMap activity;

	public MyOverLayManager(BaiduMap arg0) {
		super(arg0);
		bitmap = BitmapDescriptorFactory.fromResource(R.drawable.iconmarka);
		// TODO Auto-generated constructor stub
	}

	

	public void SetShopList(ShopListModel list, EasyEatApplication ap, SearchOnMap sm) {
		shopList = list;
		app = ap;
		activity = sm;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.getZIndex();
		//ShopListItemModel localShopModel = new ShopListItemModel();
		 //localShopModel.setId(overlayItem.getSnippet());
		 app.setShop(shopList.list.get(arg0.getZIndex())); 
		 Intent localIntent = new
		 Intent("com.ihangjing.common.tap1"); 
		 app.sendBroadcast(localIntent);
		 activity.finish();
		return false;
	}

	@Override
	public List<OverlayOptions> getOverlayOptions() {
		// TODO Auto-generated method stub
		if (shopList != null && shopList.list.size() != 0) {
			List<OverlayOptions> list = new ArrayList<OverlayOptions>();
			int length = shopList.list.size();
			ShopListItemModel model;
			OverlayOptions option;
			OverlayOptions textOptions;
			LatLng point;// = new LatLng(39.963175, 116.400244);
			for (int i = 0; i < length; i++) {
				model = shopList.list.get(i);
				point = new LatLng(Double.parseDouble(model.getLatitude()),
						Double.parseDouble(model.getLongtude()));
				//option = new MarkerOptions().position(point).icon(bitmap).title(model.getName());
				option = new MarkerOptions().position(point).title(model.getName())
						.icon(bitmap).zIndex(i);
				textOptions = new TextOptions()  
			    .bgColor(0xAAFFFF00)  
			    .fontSize(16)  
			    .fontColor(0xFFFF00FF)  
			    .text(model.getName())  
			    .rotate(0)  
			    .position(point).zIndex(i); 
				
				list.add(option);
				list.add(textOptions);
			}
			return list;
		}
		return null;

	}



	@Override
	public boolean onPolylineClick(Polyline arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
