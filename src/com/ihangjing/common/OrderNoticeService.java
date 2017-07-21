package com.ihangjing.common;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

//检测订单状态 
//订单状态发生改变了会及时通知客户
public class OrderNoticeService extends Service {
	Handler handler;
	//private boolean isSoftwareRunning = true;
	MyReceiver myReceiver;
	//private GetOrderStatusBean orderStatusBean = null;
	TimerTask task;
	private Timer timer = null;

	public OrderNoticeService() {
		task = new TimerTask() {
			@Override
			public void run() {
				new CheckIsAnyOrderStatusChanged().start();
			}
		};

		handler = new Handler() {
			@Override
			public void handleMessage(Message paramMessage) {
				switch (paramMessage.what) {
				case 1: {

				}
				}
				//OrderNoticeService.this.isSoftwareRunning = false;
			}
		};
	}

	private class CheckIsAnyOrderStatusChanged extends Thread {
		@Override
		public void run() {
			/*
			Hashtable localHashtable = new Hashtable();
			Object localObject1 = localHashtable.put("rtp", "GetOrderStatus");
			String str1 = GloableData.imei;
			Object localObject2 = localHashtable.put("imei", str1);
			String str2 = GloableData.uid;
			Object localObject3 = localHashtable.put("uid", str2);
			Object localObject4 = localHashtable.put("gzip", "1");
			Context localContext = OrderNoticeService.this.getApplicationContext();//......
			GetOrderStatusParser localGetOrderStatusParser = new GetOrderStatusParser(localContext);
			try {
				OrderNoticeService localOrderNoticeService = OrderNoticeService.this;
				Handler localHandler = OrderNoticeService.this.handler;
				GetOrderStatusBean localGetOrderStatusBean = localGetOrderStatusParser
						.getMsgFromNetwork(localHandler, "", localHashtable);
				//localOrderNoticeService.orderStatusBean = localGetOrderStatusBean;
				boolean bool = OrderNoticeService.this.handler
						.sendEmptyMessage(3);
				return;
			} catch (Exception localException) {
				while (true)
					localException.printStackTrace();
			}
			*/
		}
	}

	@Override
	public IBinder onBind(Intent paramIntent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.myReceiver = new MyReceiver();
		//String str = GloableData.softwareHaveExit_br;
		//IntentFilter localIntentFilter = new IntentFilter(str);

		//Intent localIntent = registerReceiver(this.myReceiver,localIntentFilter);

		if (this.timer != null)
			return;
		this.timer = new Timer();
		Timer localTimer = this.timer;
		TimerTask localTimerTask = this.task;
		long l = 40000L;
		localTimer.schedule(localTimerTask, 40000L, l);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (this.timer != null)
			this.timer.cancel();
		if (this.myReceiver == null)
			return;

		unregisterReceiver(this.myReceiver);
	}

	@Override
	public void onStart(Intent paramIntent, int paramInt) {
		super.onStart(paramIntent, paramInt);
	}

	class MyReceiver extends BroadcastReceiver {
		MyReceiver() {
		}

		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			String str1 = paramIntent.getAction();
			String str2 = GloableData.softwareHaveExit_br;
			if (!str1.equals(str2))
				return;

			Message localMessage = new Message();
			localMessage.what = 4;
			OrderNoticeService.this.handler.sendMessage(localMessage);
		}
	}
}
