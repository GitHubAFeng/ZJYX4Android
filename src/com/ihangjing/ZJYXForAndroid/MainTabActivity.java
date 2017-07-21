/**
 * 
 */
package com.ihangjing.ZJYXForAndroid;

import com.ihangjing.ZJYXForAndroid.R;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AnalogClock;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;

/**
 * @author WuLin
 * 
 */
@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity {

	private TabHost tabHost;
	private UpdateReceiver updateReceiver;
	EasyEatApplication app;
	TabWidget tabWigetl;
	/**
	 * 
	 */
	public MainTabActivity() {
		// TODO Auto-generated constructor stub
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void createTabs() {
		 if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {  
	            Window window = getWindow();  
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS  
	                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);  
	            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  
	                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  
	                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);  
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  
	            window.setStatusBarColor(Color.TRANSPARENT);  
	            window.setNavigationBarColor(Color.TRANSPARENT);  
	        } 
		// setContentView(R.layout.main_tab);
		this.tabHost = getTabHost();
		this.tabHost.setup();
		
		TabHost.TabSpec localTabSpec1 = this.tabHost.newTabSpec("FirstTab");
		Intent localIntent1 = new Intent(this, MainActivity.class);
		localIntent1.putExtra("shopType", 1);//是否推荐
		localTabSpec1.setContent(localIntent1);
		TabView localTabView1 = new TabView(this, getResources().getString(R.string.nav_0), 0, R.drawable.rb_home_selector);
		
		localTabSpec1.setIndicator(localTabView1);
		
		this.tabHost.addTab(localTabSpec1);

		/*TabHost.TabSpec localTabSpec2 = this.tabHost.newTabSpec("TogoTab");
		Intent localIntent2 = new Intent(this, MainActivity.class);
		localIntent2.putExtra("shopType", 2);//是否推荐
		localTabSpec2.setContent(localIntent2);
		TabView localTabView2 = new TabView(this, getResources().getString(R.string.nav_1),0, R.drawable.rb_near_selector);
		localTabView2.setTag("orderTab");
		localTabSpec2.setIndicator(localTabView2);
		this.tabHost.addTab(localTabSpec2);
*/
		TabHost.TabSpec localTabSpec3 = this.tabHost.newTabSpec("FoodTab");
		Intent localIntent3 = new Intent(this, OrderList.class);
		//localIntent3.putExtra("isRem", 1);//是否推荐
		localTabSpec3.setContent(localIntent3);
		TabView localTabView3 = new TabView(this, /*getResources().getString(R.string.nav_2)*/"订单", 0, R.drawable.rb_search_selector);
		localTabView3.setTag("favoriteTab");
		localTabSpec3.setIndicator(localTabView3);
		this.tabHost.addTab(localTabSpec3);

		TabHost.TabSpec localTabSpec4 = this.tabHost.newTabSpec("FoodCarTab");
		Intent localIntent4 = new Intent(this, SystemSet.class);
		localTabSpec4.setContent(localIntent4);
		TabView localTabView4 = new TabView(this, getResources().getString(R.string.nav_3), 0, R.drawable.rb_more_selector);
		localTabSpec4.setIndicator(localTabView4);
		this.tabHost.addTab(localTabSpec4);

		/*TabHost.TabSpec localTabSpec5 = this.tabHost.newTabSpec("OrdersTab");
		Intent localIntent5 = new Intent(this, SystemSet.class);
		localTabSpec5.setContent(localIntent5);
		TabView localTabView5 = new TabView(this, "", R.drawable.tab_fifth_selector);
		localTabSpec5.setIndicator(localTabView5);
		this.tabHost.addTab(localTabSpec5);*/
		/*tabWigetl = (TabWidget)findViewById(android.R.id.tabs);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 125);
		int count = tabWigetl.getChildCount();//TabHost中有一个getTabWidget()的方法
		    for (int i = 0; i < count; i++) {
		     View view = tabWigetl.getChildTabViewAt(i); 
		     view.setLayoutParams(localLayoutParams);
		     //view.getLayoutParams().height = 120; //tabWidget.getChildAt(i)
		     //view.getLayoutParams(). = 160;
		    }*/

		this.tabHost.setCurrentTab(0);
		// MainActivity.this.
		this.tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

			}
		});
		app = (EasyEatApplication)getApplication();
		this.updateReceiver = new UpdateReceiver();
		IntentFilter localIntentFilter0 = new IntentFilter(
				"com.ihangjing.common.tap0");
		registerReceiver(this.updateReceiver, localIntentFilter0);

		IntentFilter localIntentFilter1 = new IntentFilter(
				"com.ihangjing.common.tap1");
		registerReceiver(this.updateReceiver, localIntentFilter1);

		IntentFilter localIntentFilter2 = new IntentFilter(
				"com.ihangjing.common.tap2");
		registerReceiver(this.updateReceiver, localIntentFilter2);

		IntentFilter localIntentFilter3 = new IntentFilter(
				"com.ihangjing.common.tap3");
		registerReceiver(this.updateReceiver, localIntentFilter3);

		IntentFilter localIntentFilter4 = new IntentFilter(
				"com.ihangjing.common.tap4");
		registerReceiver(this.updateReceiver, localIntentFilter4);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(updateReceiver);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.main_tab);
		createTabs();

	}

	class UpdateReceiver extends BroadcastReceiver {
		UpdateReceiver() {
		}

		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			if (paramIntent.getAction().equals("com.ihangjing.common.tap0")) {
				MainTabActivity.this.tabHost.setCurrentTab(0);

			} else if (paramIntent.getAction().equals(
					"com.ihangjing.common.tap1")) {
				MainTabActivity.this.tabHost.setCurrentTab(1);

			} else if (paramIntent.getAction().equals(
					"com.ihangjing.common.tap2")) {
				MainTabActivity.this.tabHost.setCurrentTab(2);

			} else if (paramIntent.getAction().equals(
					"com.ihangjing.common.tap3")) {
				MainTabActivity.this.tabHost.setCurrentTab(3);

			} else if (paramIntent.getAction().equals(
					"com.ihangjing.common.tap4")) {
				MainTabActivity.this.tabHost.setCurrentTab(4);

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (app.isShowSale) {
			
			tabHost.setCurrentTab(1);
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {
		//
		return true;
	}

}
