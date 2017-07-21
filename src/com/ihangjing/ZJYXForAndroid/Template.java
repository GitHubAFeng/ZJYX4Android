package com.ihangjing.ZJYXForAndroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ihangjing.common.HjActivity;
//import com.umeng.analytics.MobclickAgent;
public class Template extends HjActivity {
	
	Handler mHandler = null;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        //初始化系统开始
        mHandler = new UIHandler();
        
        //x.postDelayed(new splashhandler(), 1500); //延时
        //......
        //初始化系统结束
        
        //友盟统计错误报告 
        //http://www.umeng.com/doc/home.html#op_con_sdkcenter/adsdk_syzn
        //MobclickAgent.onError(this);
    }
    
	private class UIHandler extends Handler {
		static final int DO_WITH_DATA = 0; // 定义消息类型
		//static final int DO_UPDATE_APP_SUCCESS = 1;
		//static final int DO_GET_LOCATION_SUCCESS = 2;
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DO_WITH_DATA: {

			}
			}
		}
	}
	
    class splashhandler implements Runnable{
        @Override
		public void run() {
        	//其中MainActivity是要进入的下一个Activity
            //startActivity(new Intent(getApplication(),MainActivity.class));  
            //StartApp.this.finish(); 
        } 
    }
}