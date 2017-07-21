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
 * 添加了友盟统计功能 避免每个activity都需要增加统计代码
 * 友盟相关http://www.umeng.com/doc/home.html#op_con_sdkcenter/adsdk_syzn
 */
public class HjActivity extends Activity {

	public EasyEatApplication app = null;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// 启用窗体的扩展特性。参数是Window类中定义的常量。
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//只允许竖屏
		requestWindowFeature(1);
		
		Thread.setDefaultUncaughtExceptionHandler(new MyCustomExceptionHandler(
				this));

		this.app = (EasyEatApplication) getApplication();
		app.addActivity(this);
		
		// 创建状态栏的管理实例  
	    SystemBarTintManager tintManager = new SystemBarTintManager(this);  
	    // 激活状态栏设置  
	    tintManager.setStatusBarTintEnabled(true);  
	    // 激活导航栏设置  
	   // tintManager.setNavigationBarTintEnabled(true); 
	 // 设置一个颜色给系统栏  
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

		paramMenu.add(0, i, 0, "首页").setIcon(R.drawable.ic_menu_home);

		// 如果未登录系统则进行提示
		UserDetail userDetail = OtherManager.getUserInfo(HjActivity.this);

		if (userDetail.userId.equals("0")) {
			i = "login".hashCode();
			paramMenu.add(0, i, 1, "登录").setIcon(R.drawable.ic_menu_login);
		} else {
			i = "usercenter".hashCode();
			paramMenu.add(0, i, 2, "会员中心").setIcon(R.drawable.ic_menu_sendsms);
		}*/

		return bool;
	}

	public boolean onPrepareOptionsMenu(Menu paramMenu) {
		// 1. 在首页时不再现实回到首页的选项
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
