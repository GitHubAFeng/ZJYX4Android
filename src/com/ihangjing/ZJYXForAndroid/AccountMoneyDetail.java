package com.ihangjing.ZJYXForAndroid;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ihangjing.alipay.Keys;
import com.ihangjing.alipay.Result;
import com.ihangjing.alipay.SignUtils;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;
import com.ihangjing.wxpay.Constants;
import com.ihangjing.wxpay.MD5;
import com.ihangjing.wxpay.Util;
import com.tencent.mm.sdk.modelpay.PayReq;

public class AccountMoneyDetail extends HjActivity implements HttpRequestListener {

	private static final String TAG = "CashMoneyDetail";

	private TextView tvCanUserMoney;
	private Button backButton;

	private EditText truenameEditText;
	

	

	private Button saveButton;

	private UIHandler hander = new UIHandler();

	private String dialogStr;
	private String buttonStr;
	HttpManager localHttpManager = null;
	private int op = 1; // 1 获取资料 2更新资料

	private double canUseMoney = 0.0;

	protected double useMoney;

	private String errMsg;

	private double allMoeny;

	private double noUMoeny;

	private double usemoney;

	private ProgressDialog progressDialog;

	private TextView tvTitle;

	private String strEndData;

	private String strStartData;

	protected int payMode = 2;

	private String alipayInfo;

	private PayReq wxpayReq;

	private StringBuffer wxpay;

	private Map<String, String> resultunifiedorder;

	public String errorMSG;

	private String payID;
	
	@Override
	public void onResume()
	{
		super.onResume();
		//GetHaveMoney();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.account_money_detail);

		//EasyEatApplication.getInstance().addActivity(this);
		Bundle extern = getIntent().getExtras();
		if (extern != null) {
			strEndData = extern.getString("strEndData");
			strStartData = extern.getString("strStartData");
		}
		tvTitle = (TextView) findViewById(R.id.title_bar_content_tv);

		tvTitle.setText(getResources().getString(R.string.acount_input_title));

		backButton = (Button) findViewById(R.id.title_bar_back_btn);

		backButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				

				finish();
			}
		});
		
		RadioGroup rgGroup = (RadioGroup)findViewById(R.id.pay_typeradioGroup);
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.pay_type0://支付宝
					payMode  = 2;
					break;
				case R.id.pay_type1://微信
					payMode = 5;
					break;
				default:
					break;
				}
			}
		});
		
		
		//tvCanUserMoney = (TextView)findViewById(R.id.tv_can_user_money);
		//tvCanUserMoney.setText(String.format("可提现金额：￥%.2f", canUseMoney));
		truenameEditText = (EditText) findViewById(R.id.userinfo_truename_et);
		

		saveButton = (Button) findViewById(R.id.userinfo_save_button);

		saveButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 保存密码
				String strUseMoney = truenameEditText.getText().toString().replace(" ", "");
				if (strUseMoney.length() < 1) {
					Toast.makeText(AccountMoneyDetail.this, getResources().getString(R.string.acount_input_notice), 15).show();
					truenameEditText.requestFocus();
					return;
				}
				useMoney = Float.parseFloat(strUseMoney);
				if (useMoney == 0.0f) {
					Toast.makeText(AccountMoneyDetail.this, getResources().getString(R.string.acount_input_notice0), 15).show();
					truenameEditText.requestFocus();
					return;
				}
				/*if (useMoney > canUseMoney) {
					Toast.makeText(AccountMoneyDetail.this, String.format("提现金额不能超过：￥%.2f", canUseMoney), 15).show();
					truenameEditText.requestFocus();
					return;
				}*/
				Save();
			}
		});
		wxpayReq = new PayReq();
		wxpay=new StringBuffer();

		app.msgApi.registerApp(Constants.APP_ID);
	}
	public void GetHaveMoney()
	{
		progressDialog = ProgressDialog.show(AccountMoneyDetail.this, "",
				"获取数据中，请稍后...");
		HashMap<String, String> localHashMap = new HashMap<String, String>();

		localHashMap.put("shopid",
				OtherManager.getUserInfo(AccountMoneyDetail.this).userId);
		localHashMap.put("starttime", strStartData);
		localHashMap.put("endtime", strEndData);
		localHttpManager = new HttpManager(AccountMoneyDetail.this, AccountMoneyDetail.this,
				"APP/shop/cashoutmoney.aspx?", localHashMap, 1, 1);
		localHttpManager.postRequest();
	}
	private class UIHandler extends Handler {
		static final int SET_AREA_LIST = 1; // 下载数据成功
		public static final int CASHMONEY_SUCESS = 1;
		public static final int CASHMONEY_FAILD = -2;
		public static final int NET_ERROR = -1;
		public static final int GET_MONEY_OK = 2;
		protected static final int RQF_PAY = 9;
		public static final int GET_PREPAY_ID_FAILD = 17;
		public static final int GET_PREPAY_ID_SUCESS = 13;

		@Override
		public void handleMessage(Message msg) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
			switch (msg.what) {
			case GET_PREPAY_ID_SUCESS:
				
				if (resultunifiedorder == null) {
					removeDialog(322);
					Toast.makeText(AccountMoneyDetail.this, getString(R.string.pay_load_wx_failed)/*"调用微信支付失败，请稍后再试！"*/, 15).show();
					
					return;
				}
				genPayReq();
				sendPayReq();
				removeDialog(322);
				
				break;
			case  GET_PREPAY_ID_FAILD:
				Toast.makeText(AccountMoneyDetail.this, errorMSG + getString(R.string.pay_wx_pay_failed)/*"微信支付失败，请稍后再试！"*/, 15).show();
				
				return;
			case RQF_PAY:
				Result.sResult = (String) msg.obj;
				if (Result.sResult == null || Result.sResult.length() == 0) {
					Toast.makeText(AccountMoneyDetail.this, getResources().getString(R.string.pay_sucess),
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(AccountMoneyDetail.this, Result.getResult(),
							Toast.LENGTH_SHORT).show();
				}
				break;
			case GET_MONEY_OK:
				tvCanUserMoney.setText(String.format("可提现金额：￥%.2f", canUseMoney));
				break;
			case CASHMONEY_SUCESS:
				//tvCanUserMoney.setText(String.format("可提现金额：￥%.2f", canUseMoney));
				//truenameEditText.setText("");
				//Toast.makeText(AccountMoneyDetail.this, "提现申请成功，请等待审核通过！", 15).show();
				gotoOnLinePay();
				break;
			case CASHMONEY_FAILD:
				Toast.makeText(AccountMoneyDetail.this, errMsg, 15).show();
				break;
			case NET_ERROR: 
				Toast.makeText(AccountMoneyDetail.this, "网络或数据错误，请稍后再试！", 15).show();
				break;
			}
		}
	}

	private String getAlipayOrderInfo() {
		//payMoney = 0.1f;
				// 签约合作者身份ID
				String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";

				// 签约卖家支付宝账号
				orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";

				// 商户网站唯一订单号
				orderInfo += "&out_trade_no=" + "\"" + payID + "\"";
				//getString(R.string.app_name) + getResources().getString(R.string.shop_cart_pay_title)/*"Android支付宝支付，订单编号："*/ + orderidString
				// 商品名称
				orderInfo += "&subject=" + "\"" + getString(R.string.app_name) + "Android" + getString(R.string.pay_mode_1) + getString(R.string.acount_input_title) + ", " + getString(R.string.pay_payid)  + payID + "\"";

				// 商品详情
				orderInfo += "&body=" + "\"" + getString(R.string.app_name) + "Android" + getString(R.string.pay_mode_1) + getString(R.string.acount_input_title) + ", " + getString(R.string.pay_payid)  + payID +"\"";

				// 商品金额
				orderInfo += "&total_fee=" + "\"" + String.format("%.2f", useMoney) + "\"";

				// 服务器异步通知页面路径
				orderInfo += "&notify_url=" + "\"" + HJAppConfig.DOMAIN + "Alipay/iosnotify.aspx"
						+ "\"";

				// 服务接口名称， 固定值
				orderInfo += "&service=\"mobile.securitypay.pay\"";

				// 支付类型， 固定值
				orderInfo += "&payment_type=\"1\"";

				// 参数编码， 固定值
				orderInfo += "&_input_charset=\"utf-8\"";

				// 设置未付款交易的超时时间
				// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
				// 取值范围：1m～15d。
				// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
				// 该参数数值不接受小数点，如1.5h，可转换为90m。
				orderInfo += "&it_b_pay=\"30m\"";

				// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
				// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

				// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
				//orderInfo += "&return_url=\"m.alipay.com\"";

				// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
				// orderInfo += "&paymethod=\"expressGateway\"";

				return orderInfo;	
	}
	public void gotoAliPay() {
		try {
			
			alipayInfo = getAlipayOrderInfo();
			String sign = SignUtils.sign(alipayInfo, Keys.PRIVATE);
			sign = URLEncoder.encode(sign, "UTF-8");
			alipayInfo += "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Result.sResult = null;
			Log.i(TAG, "info = " + alipayInfo);
			//final String orderInfo = alipayInfo;
			new Thread() {
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(AccountMoneyDetail.this);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(alipayInfo);

					Message msg = new Message();
					msg.what = UIHandler.RQF_PAY;
					msg.obj = result;
					hander.sendMessage(msg);
				}
			}.start();
		} catch (Exception ex) {
			
			ex.printStackTrace();
			Toast.makeText(AccountMoneyDetail.this, getString(R.string.pay_error),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void gotoOnLinePay() {
		// TODO Auto-generated method stub
		
		if (payMode == 2) {
			gotoAliPay();
			
		}else if(payMode == 5){
			gotoWXPay();
		}
	}
	//微信支付相关
		public void gotoWXPay(){
			dialogStr = getString(R.string.pay_load_wx);//"微信支付数据加载中......";
			showDialog(322);
			resultunifiedorder = null;
			GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
			getPrepayId.execute();
			
		}
		/**
		 生成签名
		 */

		private String genPackageSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append(Constants.API_KEY);
			

			String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion",packageSign);
			return packageSign;
		}
		private String genAppSign(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i).getName());
				sb.append('=');
				sb.append(params.get(i).getValue());
				sb.append('&');
			}
			sb.append("key=");
			sb.append(Constants.API_KEY);

	        wxpay.append("sign str\n"+sb.toString()+"\n\n");
			String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
			Log.e("orion",appSign);
			return appSign;
		}
		private String toXml(List<NameValuePair> params) {
			StringBuilder sb = new StringBuilder();
			sb.append("<xml>");
			for (int i = 0; i < params.size(); i++) {
				sb.append("<"+params.get(i).getName()+">");


				sb.append(params.get(i).getValue());
				sb.append("</"+params.get(i).getName()+">");
			}
			sb.append("</xml>");

			Log.e("orion",sb.toString());
			return sb.toString();
		}
		
		public Map<String,String> decodeXml(String content) {

			try {
				Map<String, String> xml = new HashMap<String, String>();
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(new StringReader(content));
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {

					String nodeName=parser.getName();
					switch (event) {
						case XmlPullParser.START_DOCUMENT:

							break;
						case XmlPullParser.START_TAG:

							if("xml".equals(nodeName)==false){
								//实例化student对象
								xml.put(nodeName,parser.nextText());
							}
							break;
						case XmlPullParser.END_TAG:
							break;
					}
					event = parser.next();
				}

				return xml;
			} catch (Exception e) {
				Log.e("orion",e.toString());
			}
			return null;

		}


		private String genNonceStr() {
			Random random = new Random();
			return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
		}
		
		private long genTimeStamp() {
			return System.currentTimeMillis() / 1000;
		}

	   //
		private String genProductArgs() {
			StringBuffer xml = new StringBuffer();

			try {
				String	nonceStr = genNonceStr();


				xml.append("</xml>");
	            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
				packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
				packageParams.add(new BasicNameValuePair("body", getString(R.string.app_name) + "Android" + getString(R.string.pay_mode_3)));
				packageParams.add(new BasicNameValuePair("detail", getString(R.string.app_name) + "Android"  + getString(R.string.pay_mode_3) + getString(R.string.order_detail_id) + getString(R.string.pay_payid)  + payID));
				packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
				packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
				packageParams.add(new BasicNameValuePair("notify_url", HJAppConfig.WEIXINPAYREQ));//接收微信支付异步通知回调地址
				packageParams.add(new BasicNameValuePair("out_trade_no",payID));
				packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
				packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(useMoney * 100))));
				packageParams.add(new BasicNameValuePair("trade_type", "APP"));


				String sign = genPackageSign(packageParams);
				packageParams.add(new BasicNameValuePair("sign", sign));


			   String xmlstring =toXml(packageParams);

				return xmlstring;

			} catch (Exception e) {
				Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
				return null;
			}
			

		}
		private void genPayReq() {

			wxpayReq.appId = Constants.APP_ID;
			wxpayReq.partnerId = Constants.MCH_ID;
			wxpayReq.prepayId = resultunifiedorder.get("prepay_id");
			wxpayReq.packageValue = "Sign=WXPay";
			wxpayReq.nonceStr = genNonceStr();
			wxpayReq.timeStamp = String.valueOf(genTimeStamp());


			List<NameValuePair> signParams = new LinkedList<NameValuePair>();
			signParams.add(new BasicNameValuePair("appid", wxpayReq.appId));
			signParams.add(new BasicNameValuePair("noncestr", wxpayReq.nonceStr));
			signParams.add(new BasicNameValuePair("package", wxpayReq.packageValue));
			signParams.add(new BasicNameValuePair("partnerid", wxpayReq.partnerId));
			signParams.add(new BasicNameValuePair("prepayid", wxpayReq.prepayId));
			signParams.add(new BasicNameValuePair("timestamp", wxpayReq.timeStamp));

			wxpayReq.sign = genAppSign(signParams);

			wxpay.append("sign\n"+wxpayReq.sign+"\n\n");

			//show.setText(wxpay.toString());

			Log.e("orion", signParams.toString());

		}
		private void sendPayReq() {
			

			app.msgApi.registerApp(Constants.APP_ID);
			app.msgApi.sendReq(wxpayReq);
		}
		
		private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {



			@Override
			protected void onPreExecute() {
				//dialog = ProgressDialog.show(ShopCart.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
			}

			@Override
			protected void onPostExecute(Map<String,String> result) {
				
				wxpay.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
				Message message = new Message();
				String value = result.get("return_code");
				if (value.compareTo("SUCCESS") == 0) {
					resultunifiedorder=result;
					message.what = UIHandler.GET_PREPAY_ID_SUCESS;
				}else{
					message.what = UIHandler.GET_PREPAY_ID_FAILD;
					errorMSG = result.get("return_msg");
				}
				
				
				
				hander.sendMessage(message);
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
			}

			@Override
			protected Map<String,String>  doInBackground(Void... params) {

				String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
				String entity = genProductArgs();
				try {  
					entity = new String(entity.getBytes(), "ISO8859-1");  
			    } catch (Exception e) {  
			        e.printStackTrace();  
			    }
				Log.e("orion",entity);

				byte[] buf = Util.httpPost(url, entity);

				String content = new String(buf);
				Log.e("orion", content);
				Map<String,String> xml=decodeXml(content);

				return xml;
			}
		}

	private void Save() {
		this.dialogStr = "提交中...";
		showDialog(322);

		// 链接网络保存数据
		HashMap<String, String> localHashMap = new HashMap<String, String>();

		localHashMap.put("userid", app.userInfo.userId);
		
		localHashMap.put("AddMoney", String.format("%.2f", useMoney));
		localHashMap.put("paymodel", String.valueOf(payMode));

		localHttpManager = new HttpManager(AccountMoneyDetail.this, AccountMoneyDetail.this,
				"APP/Android/Recharge.aspx?", localHashMap, 1, 2);
		localHttpManager.getRequeest();
	}

	
	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		String jsonString = (String) paramObject;
		Message message = new Message();
		JSONObject json = null;
		int state = 0;
		removeDialog(322);
		if (paramInt == 260) {
			try {
				json = new JSONObject(jsonString);
				switch (tag) {
				case 1:
					allMoeny = json.getDouble("AllMoney");//当前总余额
					noUMoeny = json.getDouble("nousemoney");//冻结金额（提现后，未处理前，这部分金额为冻结金额）
					canUseMoney = json.getDouble("usemoney");//当前要提现金额
					message.what = UIHandler.GET_MONEY_OK;
					break;
				case 2:
					state = json.getInt("state");
					if (state == 1) {
						message.what = UIHandler.CASHMONEY_SUCESS;
						payID = json.getString("payorderid");
					}else{
						errMsg = json.getString("msg");
						message.what = UIHandler.CASHMONEY_FAILD;
					}
					break;
				default:
					break;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message.what = UIHandler.NET_ERROR;
			}
		}else{
			message.what = UIHandler.NET_ERROR;
		}
		hander.sendMessage(message);
		
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		Log.v(TAG, "onCreateDialog:" + paramInt);

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);
			return dialog;
		}

		if (paramInt == 1) {
			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle("提示").setMessage("提现金额不能为空，并且不能超过");

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {
				}
			};
			dialog = localBuilder1.setPositiveButton("确定", local3).create();

			return dialog;
		}

		if (paramInt == 333) {
			dialog = null;

			AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this)
					.setTitle("提示").setMessage(dialogStr);

			DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface paramDialogInterface,
						int paramInt) {

					removeDialog(333);
				}
			};
			dialog = localBuilder1.setPositiveButton(buttonStr, local3)
					.create();
		}

		return dialog;

	}
}
