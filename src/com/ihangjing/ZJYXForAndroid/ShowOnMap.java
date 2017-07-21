package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

/*
 * zjf@ihangjing.com
 * �̼��ڵ�ͼ����ʾ��λ��Ϣ  ʹ�ðٶȵ�ͼ(�ȸ��ͼ���³����ڻ����ϰ�װ���ɹ�)
 * TODO���û�λ����ʾ
 */
public class ShowOnMap extends HjActivity implements HttpRequestListener{

	private double mlat;
	private double mlng;
	private String mshopname;

	private static final String TAG = "ShowOnMap";
	private TextView titleTextView;
	private Button backButton;
	private MapView mapView;
	private BaiduMap mBaiduMap;
	private BitmapDescriptor mCurrentMarker;
	private int getType;
	private HttpManager localHttpManager;
	private String deliverID;
	public ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(1);
		
		setContentView(R.layout.show_on_map);
		
		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Button btnRight = (Button)findViewById(R.id.title_bar_right_btn);
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDeliverGPS();
			}
		});
		Bundle extras = getIntent().getExtras();
		getType = extras.getInt("getType", 0);
		mlat = extras.getDouble("lat");
		mlng = extras.getDouble("lng");
		if(getType == 0){
			
			mshopname = extras.getString("shopname");
			titleTextView.setText("�̼�λ��");
			btnRight.setVisibility(View.GONE);
		}else{
			deliverID = extras.getString("deliverID");
			titleTextView.setText("��ʿλ��");
			btnRight.setText("ˢ��");
			btnRight.setVisibility(View.VISIBLE);
		}
		

		mapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mapView.getMap();  
		//��ͨ��ͼ  
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(14);// ���õ�ͼ���ű�����17��100��
		mBaiduMap.setMapStatus(ms);
		LatLng latLng = new LatLng(mlat, mlng);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
		
		/*mBaiduMap.setMyLocationEnabled(true);
		
		// ���춨λ����
		MyLocationData locData = new MyLocationData.Builder()  
		    .accuracy(app.location.getRadius())  
		    // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360  
		    .direction(100).latitude(app.location.getLatitude())  
		    .longitude(app.location.getLongitude()).build();  
		// ���ö�λ����  
		mBaiduMap.setMyLocationData(locData);  
		// ���ö�λͼ������ã���λģʽ���Ƿ���������Ϣ���û��Զ��嶨λͼ�꣩  
		mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.iconmarka);
		MyLocationConfigeration config = new MyLocationConfigeration(LocationMode.FOLLOWING, true, mCurrentMarker);  
		mBaiduMap.setMyLocationConfigeration(config);
		
		
		
		BaiduMapOptions mapOptions = new BaiduMapOptions();
		mapOptions.compassEnabled(true);
		mapOptions.zoomControlsEnabled(true);
		mapOptions.zoomGesturesEnabled(true);
		MapStatus status = new MapStatus(0.0, 0.0, new LatLng(arg0, arg1));
		mapView.setBuiltInZoomControls(true);

		/*MapController mMapController = mapView.getController(); // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		
		GeoPoint ptCenter = new GeoPoint((int) (mlat * 1E6), (int) (mlng * 1E6));

		mMapController.setCenter(ptCenter); // ���õ�ͼ���ĵ�

		Drawable marker = getResources().getDrawable(R.drawable.iconmarka); // �õ���Ҫ���ڵ�ͼ�ϵ���Դ
		
		Log.v(TAG, "mapView.getOverlays().add(new OverItemT(marker, this))");
		mapView.getOverlays().add(new OverItemT(marker, this)); // ���ItemizedOverlayʵ����mMapView

		mMapController.setZoom(16); // ���õ�ͼzoom����*/
	}
	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
		public void getDeliverGPS() {

			Log.v(TAG, "���������ȡ���ݿ�ʼ");
			progressDialog = ProgressDialog.show(ShowOnMap.this, "",
					getResources().getString(R.string.user_center_get)/*"ע���У����Ժ�..."*/);
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("did", deliverID);
			localHttpManager = new HttpManager(ShowOnMap.this, ShowOnMap.this,
					"Android/Getgpsrecord.aspx?", localHashMap, 2, 1);

			localHttpManager.postRequest();

		}
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(getType == 1)
			getDeliverGPS();
		mapView.onResume();
		LatLng point = new LatLng(mlat, mlng);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.iconmarka);
		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
		mBaiduMap.addOverlay(option);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if(paramInt == 260){
			String value = (String)paramObject;
			JSONObject json;
			try {
				json = new JSONObject(value);
				switch (tag) {
				case 1:
					JSONArray ary = json.getJSONArray("datalist");
					json = ary.getJSONObject(0);
					mlat = json.getDouble("JH3");
					mlng = json.getDouble("JH2");
					msg.what = UIHandler.LOGIN_SUCCESS;
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
		mHandler.sendMessage(msg);
	}
	UIHandler mHandler = new UIHandler();
	private class UIHandler extends Handler {
		static final int LOGIN_SUCCESS = 2; // ��¼�ɹ�
		static final int LOGIN_FAILD = 3; // ��¼ʧ��
		static final int USERNAME_IS_EXIST = 4; // �û����Ѿ�����
		static final int EMAIL_IS_EXIST = 5; // �����Ѿ�����
		static final int TEL_IS_EXIST = 6; // �����Ѿ�����
		static final int RID_IS_EXIST = 7; // �����˲�����
		public static final int NET_ERROR = -1;
		public static final int HAVE_USER = 1;
		public static final int DATA_OK = -2;
		protected static final int TIMER_OK = 8;

		@Override
		public void handleMessage(Message msg) {
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case LOGIN_SUCCESS:
				LatLng point = new LatLng(mlat, mlng);
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.iconmarka);
				OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
				mBaiduMap.addOverlay(option);
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
				break;
			case NET_ERROR:
				OtherManager.Toast(ShowOnMap.this, getResources().getString(R.string.network_or_connection_error)/*"����������Ժ�����"*/);
				break;
			}
		}
	}
	

	
}
