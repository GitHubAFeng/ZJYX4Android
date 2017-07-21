package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ihangjing.Model.PopAdvListModel;
import com.ihangjing.Model.PopAdvModel;
import com.ihangjing.common.GloableData;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OrderNoticeService;
import com.ihangjing.common.Preferences;
import com.ihangjing.net.HttpManager;
import com.ihangjing.util.HJAppConfig;

/**
 * ������������
 * 
 * @author zjf@ihangjing.com EasyEat for Android V 1.0
 */
public class StartApp extends HjActivity {

	Handler mHandler = null;
	HttpManager localHttpManager = null;
	private int state;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		mHandler = new UIHandler();
		SharedPreferences localSharedPreferences = StartApp.this
				.getSharedPreferences(HJAppConfig.CookieName, 0);
		state = Integer.parseInt(localSharedPreferences.getString("isNoFirst", "0"));
		// ��ʼ��ϵͳ��ʼ
		// x.postDelayed(new splashhandler(), 1500); //��ʱ
		// ......
		// ��ʼ��ϵͳ����

		// ����ͳ�ƴ��󱨸�
		// http://www.umeng.com/doc/home.html#op_con_sdkcenter/adsdk_syzn
		

		// ����IMEI��,ĿǰIMEI����Ϊ�û���IDʹ��
		GloableData.imei = Preferences.getInstance(this).getIMEI();
		
		if (GloableData.imei.equals("123456789012345678")) {
			GloableData.imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
					.getDeviceId();
			Preferences localPreferences1 = Preferences.getInstance(this);
			
			String str5 = GloableData.imei;
			localPreferences1.setIMEI(str5);

			localPreferences1.setUID("0");
			
			//TODO:ʹ��imeiע��һ���û�
		}
		
		Log.v("StartApp", "GloableData IMEI:"+ GloableData.imei);
		Log.v("StartApp", "Preferences IMEI:"+ Preferences.getInstance(this).getIMEI());
	}

	// onCreate (ONE) - onStart (ONE) - onResume(ONE)
	// onStart()��activity��һ�ζ��û��ɼ���ʱ��ᱻ���á�onResume()��activity�������û�����������ᱻ���á����������������������ͬ�����飬�����������onStart��ʼ����Ľ��棬�������onResume()�ָ����ݵȵȡ�
	@Override
	protected void onResume() {
		super.onResume();
		

		// ���������
		new checkAppUpdateThread().start();

		// Loading(this);
	}
	
	private class UIHandler extends Handler {
		static final int DO_WITH_DATA = 0; // ������Ϣ����
		static final int DO_UPDATE_APP_SUCCESS = 1;
		static final int DO_GET_LOCATION_SUCCESS = 2;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DO_WITH_DATA: {

			}
			case DO_UPDATE_APP_SUCCESS: {
					/*if (state == 0) {//������һ����������
						SharedPreferences.Editor localEditor1 = StartApp.this
								.getSharedPreferences(HJAppConfig.CookieName, 0).edit();
						localEditor1.putString("isNoFirst", "1");
						localEditor1.commit();
						app.popAdvList = new PopAdvListModel();
						app.popAdvList.popType = 1;
						PopAdvModel model = new PopAdvModel();
						model.setResID(R.drawable.start1);
						app.popAdvList.list.add(model);
						
						model = new PopAdvModel();
						model.setResID(R.drawable.start2);
						app.popAdvList.list.add(model);
						
						model = new PopAdvModel();
						model.setResID(R.drawable.start3);
						app.popAdvList.list.add(model);
						Intent intent = new Intent(StartApp.this, PopAdvActivity.class);
						startActivity(intent);
					}else{*/
						startActivity(new Intent(getApplication(), MainTabActivity.class));
						overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
					// 1.�����������startActivity()����finish()����֮�����
					// 2.��ֻ��android2.0�Լ����ϰ汾������
						
					//}
					StartApp.this.finish();
				//}
				
			}
			case DO_GET_LOCATION_SUCCESS: {

			}
			}
		}
	}

	// �������Ƿ��и���
	private class checkAppUpdateThread extends Thread {
		@Override
		public void run() {
			try {
				// String localInputStream =
				// localNetManager.dogetAsString("http://www.51tcs.com/ajax/sendpassword.aspx?phone=13750821675");
				// Log.v("StartApp", localInputStream);
				sleep(1500);
			} catch (Exception e) {
				// TODO: handle exception
			}

			Message localMessage = new Message();
			localMessage.what = 1;
			StartApp.this.mHandler.sendMessage(localMessage);

		}
	}

	class splashhandler implements Runnable {
		@Override
		public void run() {
			// ����MainActivity��Ҫ�������һ��Activity
			startActivity(new Intent(getApplication(), MainTabActivity.class));
			StartApp.this.finish();
		}
	}
}