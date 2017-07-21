package com.ihangjing.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.FoodListModel;
import com.ihangjing.Model.FoodTypeListModel;
import com.ihangjing.Model.HJADVListModel;
import com.ihangjing.Model.SectionInfo;
import com.ihangjing.Model.UserDetail;
import com.ihangjing.Model.UserLocalInfo;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.util.HJAppConfig;

public class OtherManager {
	private static final int BUFFER_SIZE = 1024;
	public static final String SDCARD_ROOT_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();
	private static Toast toast = null;
	public static boolean DEBUG = true;// 标志系统是否调试状态

	public static boolean isEmail(String paramString) {
		return paramString
				.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}

	public static Drawable BitmapToDrawable(Bitmap paramBitmap) {
		return new BitmapDrawable(paramBitmap);
	}

	public static Bitmap Drawable2Bitmap(Drawable paramDrawable) {
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inJustDecodeBounds = false;
		localOptions.inSampleSize = 10;
		int i = paramDrawable.getIntrinsicWidth();
		int j = paramDrawable.getIntrinsicHeight();
		if (paramDrawable.getOpacity() != -1) {
			return null;
		}
		Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;

		Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
		Canvas localCanvas = new Canvas(localBitmap);
		int k = paramDrawable.getIntrinsicWidth();
		int m = paramDrawable.getIntrinsicHeight();
		paramDrawable.setBounds(0, 0, k, m);
		paramDrawable.draw(localCanvas);
		return localBitmap;
	}
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
	public static void Toast(Context paramContext, int paramInt) {
		if (toast != null) {
			Toast localToast = toast;
			String str1 = paramContext.getResources().getString(paramInt);
			localToast.setText(str1);
			toast.setDuration(0);
		}

		String str2 = paramContext.getResources().getString(paramInt);
		toast = Toast.makeText(paramContext, str2, 0);
		toast.show();
		return;

	}

	public static void Toast(Context paramContext, String paramString) {
		if (toast != null) {
			toast.setText(paramString);
			toast.setDuration(0);
		}

		toast = Toast.makeText(paramContext, paramString, 0);
		toast.show();
		return;

	}

	public static void ToastWithColor(Context paramContext, String paramString) {
		Spanned localSpanned = Html.fromHtml("<font color='#B3EE3A'>"
				+ paramString + "</font>");
		if (toast != null) {
			toast.setText(localSpanned);
			toast.setDuration(1);
		}

		toast = Toast.makeText(paramContext, localSpanned, 1);
		toast.show();
		return;

	}

	public static void print(String paramString) {
	}

	public static Dialog createProgressDialog(Activity paramActivity,
			String paramString) {

		View localView = LayoutInflater.from(paramActivity).inflate(
				R.layout.loadingdata, null);

		((TextView) localView.findViewById(R.id.load_data_tv))
				.setText(paramString);

		Dialog localDialog = new Dialog(paramActivity, R.style.dialog);
		localDialog.setContentView(localView);
		localDialog.setCancelable(true);
		return localDialog;

	}

	public static int isNetworkAvailable(Context paramContext) {
		int i = 0;
		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
				.getSystemService("connectivity");

		if (localConnectivityManager == null) {
			return i;
		}

		NetworkInfo[] arrayOfNetworkInfo;
		arrayOfNetworkInfo = localConnectivityManager.getAllNetworkInfo();

		if (arrayOfNetworkInfo == null) {
			return 0;
		}

		int j = 0;
		int type = -1;
		while (true) {
			int k = arrayOfNetworkInfo.length;
			if (j >= k) {
				break;
			}

			if (arrayOfNetworkInfo[j].getState() == NetworkInfo.State.CONNECTED) {
				NetworkInfo localNetworkInfo = arrayOfNetworkInfo[j];
				type = localNetworkInfo.getType();
				if (type == 1 || type == 9) {// 1 wifi
												// ConnectivityManager.TYPE_WIFI
					i = 1;
					break;
				}
				if (type == 0) {// 0 3G
								// ConnectivityManager.TYPE_MOBILE
					String str = localNetworkInfo.getExtraInfo();
					if (("cmwap".equalsIgnoreCase(str))
							|| ("cmwap:gsm".equalsIgnoreCase(str))) {
						i = 2;
						break;
					}
					i = 3;
					break;
				}
			}
			j += 1;
		}

		return i;
	}

	public static String getLocalIpAddress(Context paramContext, int paramInt) {
		Object localObject = "127.0.0.1";

		if (paramInt == 1) {// wifi
			int i = ((WifiManager) paramContext.getSystemService("wifi"))
					.getConnectionInfo().getIpAddress();

			return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
					+ ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		}

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						localObject = localObject + ";"
								+ inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}

		return localObject.toString();
	}

	// 保存用户名和密码
	public static void saveUserIDAndName(Context paramContext,
			String paramString1, String paramString2) {
		SharedPreferences.Editor localEditor1 = paramContext
				.getSharedPreferences(HJAppConfig.CookieName, 0).edit();
		localEditor1.putString("userName", paramString2);
		localEditor1.putString("userId", paramString1);
		localEditor1.commit();
	}

	// 获得用户信息
	public static UserDetail getUserInfo(Context paramContext) {
		SharedPreferences localSharedPreferences = paramContext
				.getSharedPreferences(HJAppConfig.CookieName, 0);

		String str1 = localSharedPreferences.getString("userId", "0");
		String str2 = localSharedPreferences.getString("userName", "");
		String str3 = localSharedPreferences.getString("password", "");
		String str4 = localSharedPreferences.getString("email", "");
		String str5 = localSharedPreferences.getString("trueName", "");
		String str6 = localSharedPreferences.getString("tell", "");
		String str7 = localSharedPreferences.getString("phone", "");
		int str8 = localSharedPreferences.getInt("point", 0);
		float str9 = localSharedPreferences.getFloat("userMoney",
				Float.parseFloat("0.0"));
		String qq = localSharedPreferences.getString("QQ", "");
		String icon = localSharedPreferences.getString("myICO", "");

		return new UserDetail(str1, str2, str3, str4, str5, str6, str7, str8,
				str9, qq, icon);
	}

	/**
	 * 获取用户城市和建筑物信息
	 * 
	 * @param _conContext
	 * @return
	 */
	public static UserLocalInfo getUserLocal(Context _conContext) {
		SharedPreferences localSharedPreferences = _conContext
				.getSharedPreferences(HJAppConfig.CookieName, 0);

		UserLocalInfo mInfo = new UserLocalInfo();
		mInfo.cityid = localSharedPreferences.getString("cityid", "0");
		mInfo.cityidname = localSharedPreferences.getString("cityidname", "");
		mInfo.buildid = Integer.parseInt(localSharedPreferences.getString(
				"buildid", "0"));
		mInfo.buildname = localSharedPreferences.getString("buildname", "");
		return mInfo;
	}

	/**
	 * 保存用户位置信息（城市和地标）
	 * 
	 * @param paramContext
	 * @param paramKay
	 *            键
	 * @param paramValue
	 *            值
	 */
	public static void saveUserLocalInfo(Context paramContext, String paramKay,
			String paramValue) {
		SharedPreferences.Editor localEditor1 = paramContext
				.getSharedPreferences(HJAppConfig.CookieName, 0).edit();
		localEditor1.putString(paramKay, paramValue);
		localEditor1.commit();
	}

	/**
	 * 获取参数keystrName表示参数名，defaut表示没有时，返回的值(int)
	 * 
	 * @param extras
	 * @param keystrName
	 * @param defaut
	 * @return
	 */
	public static String GetQueryString(Bundle extras, String keystrName,
			String defaut) {
		String rString = "";
		if (extras != null) {
			if (extras.getString(keystrName) != null) {
				rString = extras.getString(keystrName);
			}
		} else {
			rString = defaut;
		}
		return rString;
	}

	/**
	 * 获取参数keystrName表示参数名，defaut表示没有时，返回的值
	 * 
	 * @param extras
	 * @param keystrName
	 * @param defaut
	 * @return
	 */
	public static int GetQueryInt(Bundle extras, String keystrName, int defaut) {
		int rString = 0;
		if (extras != null) {
			/*
			 * if (extras.getString(keystrName) != null) { rString =
			 * Integer.parseInt(extras.getString(keystrName)); }
			 */
			return extras.getInt(keystrName);
		} else {
			rString = defaut;
		}
		return rString;
	}

	public static void saveUserInfo(Context paramContext, UserDetail user) {
		SharedPreferences.Editor localEditor1 = paramContext
				.getSharedPreferences(HJAppConfig.CookieName, 0).edit();
		localEditor1.putString("userName", user.userName);
		localEditor1.putString("userId", user.userId);
		localEditor1.putString("password", user.password);
		localEditor1.putString("email", user.email);
		localEditor1.putString("trueName", user.trueName);
		localEditor1.putString("tell", user.tell);
		localEditor1.putString("phone", user.phone);
		localEditor1.putInt("point", user.point);
		localEditor1.putFloat("userMoney", user.userMoney);
		localEditor1.putString("QQ", user.QQ);
		localEditor1.putString("myICO", user.myICO);
		localEditor1.commit();
	}

	public static SectionInfo GetSection(Context paramContext) {
		// TODO Auto-generated method stub
		SharedPreferences localSharedPreferences = paramContext
				.getSharedPreferences(HJAppConfig.CookieName, 0);
		String name = localSharedPreferences.getString("AreaName", "");
		String id = localSharedPreferences.getString("AreaID", "0");
		String parentid = localSharedPreferences.getString("AreaParentID", "");
		SectionInfo model = new SectionInfo();
		model.setSectionName(name);
		model.setSectionID(id);
		model.setParentid(parentid);
		return model;
	}

	public static void SaveSection(SectionInfo sectionModel,
			Context paramContext) {
		// TODO Auto-generated method stub
		SharedPreferences.Editor localEditor1 = paramContext
				.getSharedPreferences(HJAppConfig.CookieName, 0).edit();
		localEditor1.putString("AreaName", sectionModel.getSectionName());
		localEditor1.putString("AreaID", sectionModel.getSectionID());
		localEditor1.putString("AreaParentID", sectionModel.getParentid());
		localEditor1.commit();
		return;
	}

	public static String saveBitmapWithType(Bitmap bitmap, String filed,
			int type, int id) {
		String path = SDCARD_ROOT_PATH + "/" + HJAppConfig.CookieName + "/";
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		path += filed + "/";
		dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		path +=  String.valueOf(type) + "/";
		/*if (!dirFile.exists()) {
			dirFile.mkdir();
		}*/
		String name = String.valueOf(id);
		saveBitmapDrawable(bitmap, path, name);
		return path + name;
	}

	public static void saveBitmapDrawable(Bitmap bitmap, String path,
			String name) {
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(path + name);
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Bitmap getBitmap(String path) {
		// String path = "";
		// 打开文件
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		byte[] bt = new byte[(int) file.length()];

		// 得到文件的输入流
		InputStream in;
		try {
			in = new FileInputStream(file);
			// 将文件读出到输出流中
			int readLength = in.read(bt);
			while (readLength != -1) {
				outStream.write(bt, 0, readLength);
				readLength = in.read(bt);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// 转换成byte 后 再格式化成位图
		byte[] data = outStream.toByteArray();
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图

		return bitmap;
	}

	public static BitmapDrawable getBitmapDrawable(String path) {
		// String path = "";
		// 打开文件
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] bt = new byte[BUFFER_SIZE];

		// 得到文件的输入流
		InputStream in;
		try {
			in = new FileInputStream(file);
			// 将文件读出到输出流中
			int readLength = in.read(bt);
			while (readLength != -1) {
				outStream.write(bt, 0, readLength);
				readLength = in.read(bt);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// 转换成byte 后 再格式化成位图
		byte[] data = outStream.toByteArray();
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		return bd;
	}
	@SuppressLint("InlinedApi")
	public static HJADVListModel getAdvListFormCache(Context mContext, String name) {
		// TODO Auto-generated method stub
		HJADVListModel listModel = new HJADVListModel();
		SharedPreferences localSharedPreferences = mContext
				.getSharedPreferences(HJAppConfig.CookieName, 0);
		String value = localSharedPreferences.getString(name, "");
		if (value != null && !value.equals("")) {
			try {
				value = new String(android.util.Base64.decode(value.getBytes(), Base64.DEFAULT), "UTF-8");
				listModel.JSonToBeanCache(new JSONObject(value), 0);
			} catch (Exception e) {
				// TODO: handle exception
				value = "";
			}
		}
		
		return listModel;
	}
	
	@SuppressLint("InlinedApi")
	public static void saveAdvListToCache(Context mContext, HJADVListModel list, String name){
		
		
		String value = list.BeanToJsonString();
		if (value != null && !value.equals("")) {
			value = android.util.Base64.encodeToString(value.getBytes(), Base64.DEFAULT);
			SharedPreferences.Editor localEditor1 = mContext
					.getSharedPreferences(HJAppConfig.CookieName, 0).edit();
			localEditor1.putString(name, value);
			localEditor1.commit();
		}
		
	}

	@SuppressLint("InlinedApi")
	public static FoodTypeListModel getFoodTypeFormCaceh(Context mContext) {
		// TODO Auto-generated method stub
		FoodTypeListModel listModel = new FoodTypeListModel();
		SharedPreferences localSharedPreferences = mContext
				.getSharedPreferences(HJAppConfig.CookieName, 0);
		String value = localSharedPreferences.getString("FoodTypeListCache", "");
		if (value != null && !value.equals("")) {
			try {
				value = new String(android.util.Base64.decode(value.getBytes(), Base64.DEFAULT), "UTF-8");
				listModel.stringToBean(value, 1, mContext.getResources().getString(R.string.public_all_type));
			} catch (Exception e) {
				//value = "";
			}
		}
		
		return listModel;
	}

	@SuppressLint("InlinedApi")
	public static void saveFoodTypeListToCache(Context mContext,
			FoodTypeListModel foodTypeListModel) {
		// TODO Auto-generated method stub
		String value = foodTypeListModel.beanToString();
		if (value != null && !value.equals("")) {
			value = android.util.Base64.encodeToString(value.getBytes(), Base64.DEFAULT);
			SharedPreferences.Editor localEditor1 = mContext
					.getSharedPreferences(HJAppConfig.CookieName, 0).edit();
			localEditor1.putString("FoodTypeListCache", value);
			localEditor1.commit();
		}
	}

	@SuppressLint("InlinedApi")
	public static FoodListModel getFoodListFormCaceh(Context mContext) {
		// TODO Auto-generated method stub
		FoodListModel listModel = new FoodListModel();
		SharedPreferences localSharedPreferences = mContext
				.getSharedPreferences(HJAppConfig.CookieName, 0);
		String value = localSharedPreferences.getString("FoodListCache", "");
		if (value != null && !value.equals("")) {
			try {
				value = new String(android.util.Base64.decode(value.getBytes(), Base64.DEFAULT), "UTF-8");
				listModel.cacheStringToBean(value);
			} catch (Exception e) {
				//value = "";
			}
		}
		
		return listModel;
	}
	@SuppressLint("InlinedApi")
	public static void saveFoodListToCache(Context mContext,
			FoodListModel foodListModel) {
		// TODO Auto-generated method stub
		String value = foodListModel.beanToString();
		if (value != null && !value.equals("")) {
			value = android.util.Base64.encodeToString(value.getBytes(), Base64.DEFAULT);
			SharedPreferences.Editor localEditor1 = mContext
					.getSharedPreferences(HJAppConfig.CookieName, 0).edit();
			localEditor1.putString("FoodListCache", value);
			localEditor1.commit();
		}
	}

	public static String getFoodImageLocalPath(int i, int id) {
		// TODO Auto-generated method stub
		//String path = 
		return SDCARD_ROOT_PATH + "/" + HJAppConfig.CookieName + "/Food/0/" + String.valueOf(id);
	}

	public static String saveFooodBitmapWithType(Bitmap bitmap, int foodId) {
		// TODO Auto-generated method stub
		return saveBitmapWithType(bitmap, "Food", 0, foodId);
	}

	public static String saveFoodTypeBitmapWithType(Bitmap bitmap, int id) {
		// TODO Auto-generated method stub
		return saveBitmapWithType(bitmap, "FoodType", 0, id);
	}

	public static Dialog createProgressDialog(Context context,
			String paramString) {
		// TODO Auto-generated method stub
		View localView = LayoutInflater.from(context).inflate(
				R.layout.loadingdata, null);

		((TextView) localView.findViewById(R.id.load_data_tv))
				.setText(paramString);

		Dialog localDialog = new Dialog(context, R.style.dialog);
		localDialog.setContentView(localView);
		localDialog.setCancelable(true);
		return localDialog;
	}
}
