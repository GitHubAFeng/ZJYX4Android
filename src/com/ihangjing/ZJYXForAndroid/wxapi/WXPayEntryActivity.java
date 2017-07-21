package com.ihangjing.ZJYXForAndroid.wxapi;




import com.ihangjing.ZJYXForAndroid.AddOrder;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.ZJYXForAndroid.ShopCart;
import com.ihangjing.ZJYXForAndroid.R.layout;
import com.ihangjing.common.HjActivity;
import com.ihangjing.wxpay.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXPayEntryActivity extends HjActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case 0:
				//֧���ɹ�
				app.payState = 1;
				Toast.makeText(WXPayEntryActivity.this, getString(R.string.pay_sucess)/*֧���ɹ�*/, 15).show();
				break;
			case -1://���ܵ�ԭ��ǩ������δע��APPID����Ŀ����APPID����ȷ��ע���APPID�����õĲ�ƥ�䡢�����쳣��
				Toast.makeText(WXPayEntryActivity.this, getString(R.string.pay_error)/*"΢��֧���쳣�����Ժ����ԣ�"*/, 15).show();
				break;
			case -2://�û�ȡ��
				Toast.makeText(WXPayEntryActivity.this, getString(R.string.pay_cancel)/*"��ȡ����΢��֧����"*/, 15).show();
				break;
			default:
				Toast.makeText(WXPayEntryActivity.this, getString(R.string.pay_undefine)/*"δ֪���⣡"*/, 15).show();
				break;
			}
			
			
		}
		
		GoOrderDetail();
		finish();
	}
	private void GoOrderDetail() {
		/*Intent localIntent = new Intent("com.ihangjing.common.tap2");
		app.isViewOrderList = true;//
		app.sendBroadcast(localIntent);	*/
		if (app.newOrderID != null && app.newOrderID.length() > 10) {
			Intent intent = new Intent(WXPayEntryActivity.this, AddOrder.class);
			intent.putExtra("orderid", app.newOrderID);
			switch (app.showOrderType) {
			case 1:
				intent.putExtra("orderType", "0");
				break;
			case 2:
				intent.putExtra("orderType", "50");
				break;
			default:
				intent.putExtra("orderType", "0");
				break;
			}
			startActivity(intent);
		}
		
	}
}