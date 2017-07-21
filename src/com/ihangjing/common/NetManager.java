package com.ihangjing.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetManager {
	private static String cookie = null;
	private static NetManager instance = null;
	private Context c;

	public static NetManager getInstance(Context paramContext) {
		if (instance == null) {
			instance = new NetManager();
			NetManager localNetManager = instance;
			Context localContext = paramContext.getApplicationContext();
			localNetManager.c = localContext;
		}
		return instance;
	}

	// 判断当前网络是否可用
	public static boolean isNetworkAvailable(Context paramContext) {
		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
				.getSystemService("connectivity");
		if (localConnectivityManager == null) {
			return false;
		} else {

			// 获得所有网络
			NetworkInfo[] arrayOfNetworkInfo = localConnectivityManager
					.getAllNetworkInfo();

			if (arrayOfNetworkInfo != null) {
				for (int i = 0; i < arrayOfNetworkInfo.length; i++) {
					if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 判断wifi是否在工作
	private boolean isWiFiActive() {
		ConnectivityManager localConnectivityManager = (ConnectivityManager) this.c
				.getSystemService("connectivity");

		if (localConnectivityManager != null) {
			NetworkInfo localNetworkInfo = localConnectivityManager
					.getActiveNetworkInfo();
			if ((localNetworkInfo != null)
					&& (localNetworkInfo.getTypeName().equals("WIFI"))) {
				return true;
			}
		}
		return false;
	}

	public InputStream dogetAsInputStream(String paramString)
			throws EasyEatException.NetworkException {
		try {
			HttpURLConnection localHttpURLConnection = getURLConnection(paramString);
			localHttpURLConnection.setRequestMethod("GET");

			if (cookie != null) {
				String str1 = cookie;
				localHttpURLConnection.setRequestProperty("Cookie", str1);
			}

			InputStream localInputStream1;
			localInputStream1 = localHttpURLConnection.getInputStream();

			return localInputStream1;

		} catch (Exception localException) {
			throw new EasyEatException.NetworkException("Net Exception",
					localException);
		}
	}

	public String dogetAsString(String paramString)
			throws EasyEatException.NetworkException {
		try {
			//by jijunjian 此项目接口没有app目录，所以这里在替换掉
			paramString = paramString.replace("APP/", "");
			HttpURLConnection localHttpURLConnection = getURLConnection(paramString);
			localHttpURLConnection.setRequestMethod("GET");

			if (cookie != null) {
				String str1 = cookie;
				localHttpURLConnection.setRequestProperty("Cookie", str1);
			}

			BufferedReader localBufferedReader;

			StringBuffer localStringBuffer1 = new StringBuffer();
			InputStream localInputStream = localHttpURLConnection
					.getInputStream();
			InputStreamReader localInputStreamReader;

			localInputStreamReader = new InputStreamReader(localInputStream,
					"UTF-8");

			localBufferedReader = new BufferedReader(localInputStreamReader);

			while (true) {
				String str3 = localBufferedReader.readLine();
				if (str3 == null) {
					localInputStream.close();
					break;
				}
				localStringBuffer1.append(str3);
			}
			return localStringBuffer1.toString();
		} catch (Exception localException) {
			throw new EasyEatException.NetworkException("Net Exception",
					localException);
		}
	}

	public String dopostAsInputStream(String paramString1, String paramString2)
			throws EasyEatException.NetworkException {
		try {
			HttpURLConnection localHttpURLConnection = getURLConnection(paramString1);
			localHttpURLConnection.setRequestMethod("POST");
			if (cookie != null) {
				String str1 = cookie;
				localHttpURLConnection.setRequestProperty("Cookie", str1);
			}
			localHttpURLConnection.setDoInput(true);
			localHttpURLConnection.setDoOutput(true);
			OutputStream localOutputStream = localHttpURLConnection
					.getOutputStream();
			byte[] arrayOfByte = paramString2.getBytes();
			localOutputStream.write(arrayOfByte);
			localOutputStream.flush();
			localOutputStream.close();

			InputStream localInputStream1;

			localInputStream1 = localHttpURLConnection.getInputStream();

			BufferedReader localBufferedReader;

			StringBuffer localStringBuffer1 = new StringBuffer();
			InputStreamReader localInputStreamReader;

			localInputStreamReader = new InputStreamReader(localInputStream1,
					"GB2312");

			localBufferedReader = new BufferedReader(localInputStreamReader);
			while (true) {
				String str3 = localBufferedReader.readLine();
				if (str3 == null) {
					localInputStream1.close();
					break;
				}
				localStringBuffer1.append(str3);
			}

			return localBufferedReader.toString();

		} catch (Exception localException) {
			throw new EasyEatException.NetworkException("Net Exception",
					localException);
		}
	}

	public String dopostAsString(String paramString1, String paramString2)
			throws EasyEatException.NetworkException {
		try {
			HttpURLConnection localHttpURLConnection = getURLConnection(paramString1);
			localHttpURLConnection.setRequestMethod("POST");
			localHttpURLConnection.setDoInput(true);
			localHttpURLConnection.setDoOutput(true);
			if (cookie != null) {
				String str1 = cookie;
				localHttpURLConnection.setRequestProperty("Cookie", str1);
			}
			OutputStream localOutputStream = localHttpURLConnection
					.getOutputStream();
			byte[] arrayOfByte = paramString2.getBytes();
			localOutputStream.write(arrayOfByte);
			localOutputStream.flush();
			localOutputStream.close();

			BufferedReader localBufferedReader;
			StringBuffer localStringBuffer1;

			InputStream localInputStream = localHttpURLConnection
					.getInputStream();

			InputStreamReader localInputStreamReader = new InputStreamReader(
					localInputStream);
			localBufferedReader = new BufferedReader(localInputStreamReader);
			localStringBuffer1 = new StringBuffer();

			while (true) {
				String str3 = localBufferedReader.readLine();
				if (str3 == null) {
					localInputStream.close();
					break;
				}
				localStringBuffer1.append(str3);
			}
			return localStringBuffer1.toString();

		} catch (Exception localException) {
			throw new EasyEatException.NetworkException("Net Exception",
					localException);
		}
	}

	public HttpURLConnection getURLConnection(String paramString)
			throws Exception {
		HttpURLConnection localHttpURLConnection;
		if (isWiFiActive()) {
			localHttpURLConnection = (HttpURLConnection) new URL(paramString)
					.openConnection();

		} else {
			String str1 = android.net.Proxy.getDefaultHost();

			if ((str1 != null) && (!str1.equals(""))) {
				java.net.Proxy.Type localType = java.net.Proxy.Type.HTTP;
				String str2 = android.net.Proxy.getDefaultHost();
				int i = android.net.Proxy.getDefaultPort();
				InetSocketAddress localInetSocketAddress = new InetSocketAddress(
						str2, i);

				java.net.Proxy localProxy = new java.net.Proxy(localType,
						localInetSocketAddress);
				localHttpURLConnection = (HttpURLConnection) new URL(
						paramString).openConnection(localProxy);

			}
			localHttpURLConnection = (HttpURLConnection) new URL(paramString)
					.openConnection();
		}

		localHttpURLConnection.setConnectTimeout(4000);
		return localHttpURLConnection;
	}

}
