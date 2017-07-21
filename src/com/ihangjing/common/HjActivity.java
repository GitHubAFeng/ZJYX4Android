package com.ihangjing.common;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.ihangjing.Model.UserDetail;
import com.ihangjing.systembartint.SystemBarTintManager;
import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.ZJYXForAndroid.Login;
import com.ihangjing.ZJYXForAndroid.MainActivity;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.ZJYXForAndroid.UserCenterIndex;

/*
 * zjf@ihangjing.com 2011.10.27
 * ���������ͳ�ƹ��� ����ÿ��activity����Ҫ����ͳ�ƴ���
 * �������http://www.umeng.com/doc/home.html#op_con_sdkcenter/adsdk_syzn
 */
public class HjActivity extends Activity {

	public EasyEatApplication app = null;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// ���ô������չ���ԡ�������Window���ж���ĳ�����
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//ֻ��������
		requestWindowFeature(1);
		
		Thread.setDefaultUncaughtExceptionHandler(new MyCustomExceptionHandler(
				this));

		this.app = (EasyEatApplication) getApplication();
		app.addActivity(this);
		
		// ����״̬���Ĺ���ʵ��  
	    SystemBarTintManager tintManager = new SystemBarTintManager(this);  
	    // ����״̬������  
	    tintManager.setStatusBarTintEnabled(true);  
	    // �����������  
	   // tintManager.setNavigationBarTintEnabled(true); 
	 // ����һ����ɫ��ϵͳ��  
	    tintManager.setStatusBarTintColor(getResources().getColor(R.color.topbar_bg_color));  
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//MobclickAgent.onResume(this);
	}

	public boolean onCreateOptionsMenu(Menu paramMenu) {

		boolean bool = super.onCreateOptionsMenu(paramMenu);

		/*int i = "home".hashCode();

		paramMenu.add(0, i, 0, "��ҳ").setIcon(R.drawable.ic_menu_home);

		// ���δ��¼ϵͳ�������ʾ
		UserDetail userDetail = OtherManager.getUserInfo(HjActivity.this);

		if (userDetail.userId.equals("0")) {
			i = "login".hashCode();
			paramMenu.add(0, i, 1, "��¼").setIcon(R.drawable.ic_menu_login);
		} else {
			i = "usercenter".hashCode();
			paramMenu.add(0, i, 2, "��Ա����").setIcon(R.drawable.ic_menu_sendsms);
		}*/

		return bool;
	}

	public boolean onPrepareOptionsMenu(Menu paramMenu) {
		// 1. ����ҳʱ������ʵ�ص���ҳ��ѡ��
		int i = "home".hashCode();
		MenuItem localMenuItem1 = paramMenu.findItem(i);
		boolean bool1;

		if ((this instanceof MainActivity)) {
			bool1 = false;
			localMenuItem1.setVisible(bool1);
		}

		return super.onPrepareOptionsMenu(paramMenu);
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {

		int i = paramMenuItem.getItemId();
		int j1 = "home".hashCode();
		int j2 = "login".hashCode();
		int j3 = "usercenter".hashCode();

		if (i == j1) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}

		if (i == j2) {
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
		}

		if (i == j3) {
			Intent intent = new Intent(this, UserCenterIndex.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(paramMenuItem);
	}
}
