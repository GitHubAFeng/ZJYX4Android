package com.ihangjing.common;

import android.content.Context;

import com.ihangjing.Model.AddressBean;

public class GloableData
{
  public static String GpsCityName;
  public static String cityid;
  public static String cityname;
  public static String closeDishActivity;
  public static Context ctx;
  public static String imei;
  public static boolean isCommitSuccess;
  public static boolean isMyShopRefresh;
  public static AddressBean nowAddressBean;
  public static String pictureBasePath = "/sdcard/easyeat/.pic/";//图片保存地址 保存到SD卡中
  public static String provincename;
  public static String reloadMyLocation;
  public static String softwareHaveExit_br;
  public static String uid;
  public static String updateMyLocation_br = "com.ihangjing.common.updateMyLocation";

  static
  {
    closeDishActivity = "com.ihangjing.common.closeDishActivity";
    softwareHaveExit_br = "com.ihangjing.common.softwareHaveExit";
    reloadMyLocation = "com.ihangjing.common.reloadMyLocation";
    GpsCityName = "";
    imei = "";
    nowAddressBean = new AddressBean();
    cityid = "";
    cityname = "";
    provincename = "";
    uid = "";
    ctx = null;
    isCommitSuccess = false;
    isMyShopRefresh = false;
  }
}
