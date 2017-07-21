package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.ihangjing.Model.GiftDetailOrderModel;
import com.ihangjing.Model.ShopCartModel;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GiftOrderDetail extends Dialog implements HttpRequestListener{

	private Button btnReturn;
	private TextView tvTitle;
	

	public Context context;

	int index = 0;//
	String orderID;
	int sendType = 2;//1自提 2送货
	int type = 0;//0普通，1现金券
	private HttpManager localHttpManager;
	private Dialog processDialog;
	private UIHandler handler;
	GiftDetailOrderModel pointDetailOrderModel;
	public GiftOrderDetail(Context context, String orderID,
			int sendType, int type) {
		super(context, R.style.no_title_dialog);
		this.orderID = orderID;
		this.context = context;
		this.sendType = sendType;
		this.type = type;
		

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_order_dedail);
		btnReturn = (Button) findViewById(R.id.title_bar_back_btn);
		btnReturn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				cancel();
			}
		});

		tvTitle = (TextView) findViewById(R.id.title_bar_content_tv);
		
		tvTitle.setText(context.getResources().getString(R.string.gift_order_detail_title)/*"礼品订单"*/);
		if(sendType == 1){
			TextView view = (TextView)findViewById(R.id.tv_uaddr);
			view.setText(context.getResources().getString(R.string.gift_order_detail_get_addr)/*"取货地址："*/);
		}
		processDialog = OtherManager.createProgressDialog(context, context.getResources().getString(R.string.public_get_data_notice)/*"获取数据中..."*/);
		handler = new UIHandler(); 
		GetData();
	}
	
	private void GetData(){
		processDialog.show();
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("id", orderID);
		

		localHttpManager = new HttpManager(context,
				GiftOrderDetail.this, "/Android"
						+ "/GetIntegralDetail.aspx?", localHashMap, 1, 0);
		localHttpManager.getRequeest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		processDialog.dismiss();
		Message message = new Message();
		if(paramInt == 260){
			
			try {
				//JSONObject json = new JSONObject(String.valueOf(paramObject));
				pointDetailOrderModel = new GiftDetailOrderModel(new JSONObject(String.valueOf(paramObject)));
				message.what = UIHandler.SHOW_GIFT;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}
 		}else{
			message.what = UIHandler.NET_ERROR;
		}
		handler.sendMessage(message);
		
	}
	
	private class UIHandler extends Handler {
		public static final int NET_ERROR = -1;
		static final int SHOW_GIFT = 1; // 下载数据成功

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NET_ERROR:
				Toast.makeText(context, context.getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误！请稍后再试"*/, 5).show();
				break;
			case SHOW_GIFT: {
				TextView view = (TextView)findViewById(R.id.tv_name_n);
				view.setText(pointDetailOrderModel.getGiftName());
				view = (TextView)findViewById(R.id.tv_state);
				switch (pointDetailOrderModel.getState()) {
				case 0:
					view.setText(context.getResources().getString(R.string.check_state_0)/*"未审核"*/);
					break;
				case 1:
					view.setText(context.getResources().getString(R.string.check_state_1)/*"审核通过"*/);
					break;
				case 2:
					view.setText(context.getResources().getString(R.string.check_state_2)/*"审核未通过"*/);
					break;
				default:
					view.setText(context.getResources().getString(R.string.check_state_0)/*"未审核"*/);
					break;
				}
				
				view = (TextView)findViewById(R.id.tv_point_n);
				view.setText(pointDetailOrderModel.getUsePoint());
				view = (TextView)findViewById(R.id.et_uname);
				view.setText(pointDetailOrderModel.getRecName());
				view = (TextView)findViewById(R.id.et_uphone);
				view.setText(pointDetailOrderModel.getRecPhone());
				view = (TextView)findViewById(R.id.tv_add_time_n);
				view.setText(pointDetailOrderModel.getAddtime());
				view = (TextView)findViewById(R.id.et_uaddr);
				
				if(sendType == 2){
					view.setText(pointDetailOrderModel.getuAddress());
				}else{
					view.setText(pointDetailOrderModel.getpAddressString());
				}
				view = (TextView)findViewById(R.id.tv_time_n);
				view.setText(pointDetailOrderModel.getRecData());
				return;
			}
			
			}
		}
	}

	

}
