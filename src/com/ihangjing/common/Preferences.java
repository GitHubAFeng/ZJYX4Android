package com.ihangjing.common;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
	private static Preferences instance;
	private String DB_NAME = "easyeatv1.db";
	private Context context;
	SharedPreferences.Editor editor;
	SharedPreferences settings;

	private Preferences(Context paramContext) {
		this.context = paramContext;
		Context localContext = this.context;
		String str = this.DB_NAME;

		// 获取到作用域是本应用程序的preference
		this.settings = localContext.getSharedPreferences(str, 0);

		// 让setting处于编辑状态
		this.editor = this.settings.edit();
	}

	public static Preferences getInstance(Context paramContext) {
		if (instance == null) {
			instance = new Preferences(paramContext);
		}
		return instance;
	}

	public boolean getAutoDownPic() {
		return this.settings.getBoolean("isAuto", true);
	}

	public String getCityId() {
		return this.settings.getString("cityId", "");
	}

	public String getCityName() {
		return this.settings.getString("cityName", "");
	}

	public String getIMEI() {
		return this.settings.getString("imei", "123456789012345678");
	}

	public boolean getIsFirstRun() {
		return this.settings.getBoolean("IsFirstRun", true);
	}

	public boolean getIsGridView() {
		return this.settings.getBoolean("isGridView", false);
	}

	public boolean getIsNeedLoadData() {
		return this.settings.getBoolean("IsNeedLoadData_orderContenctActivity",
				false);
	}

	public boolean getIsNeedUpdateLocation() {
		return this.settings.getBoolean("IsNeedUpdateLocation", true);
	}

	public String getJumpOrderId() {
		return this.settings.getString("JumpOrderId_orderContenctActivity", "");
	}

	public String getLastPositionAndAddress() {
		return this.settings.getString("LastPositionAndAddress", null);
	}

	public long getLocationUpdateTime() {
		return this.settings.getLong("location_update_time", 30000L);
	}

	public int getMaxMyShopId() {
		return this.settings.getInt("maxMyShopId", 0);
	}

	public String getOneOrderBeanString() {
		return this.settings.getString(
				"OneOrderBeanString_orderContenctActivity", "");
	}

	public String getProvinceName() {
		return this.settings.getString("provinceName", "");
	}

	public String getUID() {
		return this.settings.getString("UID", "");
	}

	public boolean getViewHistory() {
		return this.settings.getBoolean("isStoreViewHistory", false);
	}

	public void setAutoDownPic(boolean paramBoolean) {
		this.editor.putBoolean("isAuto", paramBoolean);
		this.editor.commit();
	}

	public void setCity(String paramString1, String paramString2,
			String paramString3) {
		this.editor.putString("cityId", paramString1);
		this.editor.putString("cityName", paramString2);
		this.editor.putString("provinceName", paramString3);
		this.editor.commit();
	}

	public void setIMEI(String paramString) {
		if (this.settings.contains("imei")) {
			this.editor.remove("imei");
		}
		this.editor.putString("imei", paramString);
		this.editor.commit();
	}

	public void setIsFirstRun(boolean paramBoolean) {
		this.editor.putBoolean("IsFirstRun", paramBoolean);
		this.editor.commit();
	}

	public void setIsGridView(boolean paramBoolean) {
		this.editor.putBoolean("isGridView", paramBoolean);
		this.editor.commit();
	}

	public void setIsNeedLoadData(boolean paramBoolean) {
		this.editor.putBoolean("IsNeedLoadData_orderContenctActivity",
				paramBoolean);
		this.editor.commit();
	}

	public void setIsNeedUpdateLocation(boolean paramBoolean) {
		this.editor.putBoolean("IsNeedUpdateLocation", paramBoolean);
		this.editor.commit();
	}

	public void setJumpOrderId(String paramString) {
		this.editor.putString("JumpOrderId_orderContenctActivity", paramString);
		this.editor.commit();
	}

	public void setLastPositionAndAddress(String paramString) {
		this.editor.putString("LastPositionAndAddress", paramString);
		this.editor.commit();
	}

	public void setLocationUpdateTime(long paramLong) {
		this.editor.putLong("location_update_time", paramLong);
		this.editor.commit();
	}

	public void setMaxMyShopId(int paramInt) {
		this.editor.putInt("maxMyShopId", paramInt);
	}

	public void setOneOrderBeanString(String paramString) {
		this.editor.putString("OneOrderBeanString_orderContenctActivity",
				paramString);
		this.editor.commit();
	}

	public void setUID(String paramString) {
		if (this.settings.contains("UID")) {
			this.editor.remove("UID");
		}
		this.editor.putString("UID", paramString);
		this.editor.commit();
	}

	public void setViewHistory(boolean paramBoolean) {
		if (this.settings.contains("isStoreViewHistory")) {
			this.editor.remove("isStoreViewHistory");
		}
		this.editor.putBoolean("isStoreViewHistory", paramBoolean);
		this.editor.commit();
	}
}