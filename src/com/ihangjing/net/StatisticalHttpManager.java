package com.ihangjing.net;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.ihangjing.common.OtherManager;
import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.util.HJAppConfig;

public class StatisticalHttpManager implements Runnable {
	private static final String page = "AndroidApi/stats/android.json";
	private EasyEatApplication app = null;
	private HttpURLConnection conn = null;
	private int connectTimeout = 0;
	private Context context;
	private String data = "";
	private StringBuilder sb = null;

	public StatisticalHttpManager(Context paramContext,
			EasyEatApplication paramFanTongApplication, String paramString) {
		
		this.context = paramContext;
		this.app = paramFanTongApplication;
		if (this.app == null)
			return;
		
		this.sb = new StringBuilder();
		this.sb.append("s=");

		this.data = sb.toString();
		new Thread(this).start();
	}

	private void sendPostRequest(String paramString, boolean paramBoolean) {
		try {
			String str1 = String.valueOf(paramString);
			StringBuilder localStringBuilder = new StringBuilder(str1)
					.append("?");
			String str2 = this.data;
			String str3 = str2;
			HttpURLConnection localHttpURLConnection1 = (HttpURLConnection) new URL(
					str3).openConnection();
			this.conn = localHttpURLConnection1;
			HttpURLConnection localHttpURLConnection2 = this.conn;
			int i = this.connectTimeout;
			localHttpURLConnection2.setConnectTimeout(i);
			this.conn.setUseCaches(false);
			this.conn.setRequestProperty("Charset", "UTF-8");
			if (paramBoolean)
				this.conn
						.setRequestProperty("X-Online-Host", "api.fantong.com");
			this.conn.setRequestMethod("GET");
			int j = this.conn.getResponseCode();
			return;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	@Override
	public void run() {
		int i = OtherManager.isNetworkAvailable(this.context);
		if (i == 0)
		{
			return;
		}
		
		if ((i == 1) || (i == 3)) {
			this.connectTimeout = 8000;
			sendPostRequest(HJAppConfig.URL+"/AndroidApi/stats/android.aspx",false);
			return;
		}
		
		if (i != 2)
		{	
			return;
		}
		this.connectTimeout = 10000;
		sendPostRequest("http://"+HJAppConfig.PROXY+"/AndroidApi/stats/android.aspx", true);
	}
}