package com.ihangjing.util;

import android.content.Intent;

/*
 * 
 * ������Ҫ�Ǳ���������ã�������
 */
public class HJAppConfig {
	
	public static String DM = "daojia999.com";
	//����ͷ
	public static String HOST = "www." + DM;//www.365takeaway.ie
	//ip
	public static String PROXY = "";
	
	public static String DOMAIN = "http://" + HOST + "/";
	public static String WEIXINPAYREQ = "http://weixin." + DM + "/weixinpay/payNotifyUrl.aspx";
	
	//�ӿ���������
	public static String URL = DOMAIN + "App/";
	
	public static String CookieName = "ZJYXForAndroid";
	
	public static String APKNAME = "ZJYXForAndroid.apk";
	
	public static String PRONAME = "ZJYXForAndroid";
	
	public static final String UPDATELOCATION = "com.ihangjing." + CookieName +".updateMyLocation";
	public static final String BAIDU_STATIC_KEY = "IBnmfL24l7K15Id6VC5eNfe3hMxkOzcu";
	
}
