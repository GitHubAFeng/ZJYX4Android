package com.ihangjing.ZJYXForAndroid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.ihangjing.alipay.Keys;
import com.ihangjing.alipay.Result;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.common.Util;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

public class SystemSet extends HjActivity implements HttpRequestListener{

	private TextView abouTextView;
	private TextView disclaimerTextView;
	private TextView shareTextView;
	//private TextView mapTextView;
	private TextView titleTextView;
	private Button backButton;
	private TextView checkupdateTextView;
	private LinearLayout llLogin;
	private LinearLayout llUserInfo;
	private TextView tvUserName;
	private TextView tvPhone;
	private Button btnLogin;
	private HttpManager localHttpManager;
	private UIHandler mHandler = new UIHandler();
	String langs[];// = {getResources().getString(R.string.mor_language_wy), getResources().getString(R.string.mor_language_cn)};
	private Button btnExit;
	private TextView tvMyPoint;
	private TextView tvMyMoeny;
	private RelativeLayout rlMyIcon;
	private TextView tvTitle;
	private float payMoney;
	private String payID = "";
	private String alipayInfo;
	private ProgressDialog progressDialog;
	
	private static final int IMAGE_REQUEST_CODE = 0;// 设置本地选择照片的标志
	private static final int CAMERA_REQUEST_CODE = 1; // 设置拍照操作的标志
	public static final String SDCARD_ROOT_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();// 路径
	public static final String SAVE_PATH_IN_SDCARD = "/DCIM/"; // 图片及其他数据保存文件夹
	public String IMAGE_CAPTURE_NAME = "cameraTmp.jpg"; // 照片名称
	private String[] imageItems;// = new String[] { "手机相册", "手机拍照" };
	private static final int RESULT_REQUEST_CODE = 2;
	
	protected RelativeLayout nowEditRl;
	private String imgExt;

	private Bitmap btNow;

	private Bitmap btShopZ1;

	private Bitmap btShopZ2;
	private String dialogStr;
	protected boolean gotoSelectImg;
	
	public void saveLanguage(String lanAtr)
	{
		SharedPreferences.Editor localEditor1 = getSharedPreferences(HJAppConfig.CookieName, 0).edit();
        localEditor1.putString("language", lanAtr);
		localEditor1.commit();
	}
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(getResources().getString(R.string.mor_share_info));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl(HJAppConfig.DOMAIN+ "/mobile_phone.aspx");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText(getResources().getString(R.string.mor_share_info)//您好,我在使用杭景科技手机版App，手机上直接可以点外卖很方便不妨你也试用一下：
					+HJAppConfig.DOMAIN+ "/mobile_phone.aspx");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl(HJAppConfig.DOMAIN+ "/mobile_phone.aspx");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment(getResources().getString(R.string.mor_share_info));
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl(HJAppConfig.DOMAIN+ "/mobile_phone.aspx");
		 
		// 启动分享GUI
		 oks.show(this);
		 }
	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);
			return dialog;
		}
		return dialog;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.systemset);
		langs = new String[2];
		langs[0] = getResources().getString(R.string.mor_language_wy);
		langs[1] = getResources().getString(R.string.mor_language_cn);
		initUI();
		
		/*ShareSDK.initSDK(this);
		shareSDKWechat = ShareSDK.getPlatform(this, Wechat.NAME);
		shareSDKWechatMoments = ShareSDK.getPlatform(this, WechatMoments.NAME);
		shareSDKSinaWeiBo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
		shareSDKQQ = ShareSDK.getPlatform(this, QQ.NAME);
		PlatformActionListener platListener = new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				//arg1++;
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				//arg1++;
			}
		};
		shareSDKWechat.setPlatformActionListener(platListener);
		shareSDKWechatMoments.setPlatformActionListener(platListener);
		shareSDKSinaWeiBo.setPlatformActionListener(platListener);
		shareSDKQQ.setPlatformActionListener(platListener);*/
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent localIntent = new Intent("com.ihangjing.common.tap0");
			app.sendBroadcast(localIntent);

		} else {
			boolean bool = true;
			bool = super.onKeyDown(keyCode, paramKeyEvent);
			return bool;
		}

		return true;
	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (app.userInfo == null || app.userInfo.userId.equals("0") || app.userInfo.userId.equals("")) {
			llLogin.setVisibility(View.VISIBLE);
			llUserInfo.setVisibility(View.GONE);
			tvUserName.setText("");
			btnExit.setVisibility(View.GONE);
		}else{
			llUserInfo.setVisibility(View.VISIBLE);
			llLogin.setVisibility(View.GONE);
			tvUserName.setText(app.userInfo.userName);
			btnExit.setVisibility(View.VISIBLE);
			if(!gotoSelectImg){
				GetUserInfo();
			}
			
		}
		gotoSelectImg = false;
	}
	private Boolean checkLogin(){
		if (app.userInfo == null || app.userInfo.userId.equals("0") || app.userInfo.userId.equals("")) {
			
			
			Intent localIntent4 = new Intent(SystemSet.this,
					Login.class);
			localIntent4.putExtra("isReturn", true);
			SystemSet.this.startActivity(localIntent4);
			return false;
		}
		return true;
	}
	public void getPayID(float moeny)
	{
		progressDialog = ProgressDialog.show(SystemSet.this, "", "获取充值数据中，请稍后...");
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("userid", app.userInfo.userId);
		localHashMap.put("AddMoney", String.valueOf(moeny));
		localHashMap.put("paymodel", "1");
		localHttpManager = new HttpManager(SystemSet.this, SystemSet.this,
				"Android/recharege.aspx?", localHashMap, 1, 2);
		localHttpManager.postRequest();
	}
	
	public void gotoAliPay() {
		// TODO Auto-generated method stub
		/*
		try {
			//payMoney = mOrderModel.getTotalmoney() - mOrderModel.getCardPay();
			//payMoney = 0.1f;//
			if(payID .length() == 0 ){
				getPayID(payMoney);
				return;
			}
			alipayInfo = getAlipayOrderInfo();
			String sign = Rsa.sign(alipayInfo, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			alipayInfo += "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Result.sResult = null;
			final String orderInfo = alipayInfo;
			new Thread() {
				public void run() {
					String result = new AliPay(SystemSet.this, mHandler)
					.pay(orderInfo);

					Message msg = new Message();
					msg.what = UIHandler.RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();
		} catch (Exception ex) {
			
			ex.printStackTrace();
			Toast.makeText(SystemSet.this, "支付异常",
					Toast.LENGTH_SHORT).show();
		}*/
	}
	private String getAlipayOrderInfo() {
		StringBuilder sb = new StringBuilder();
		
		
		sb.append("partner=\"");//合作者身份
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");//订单编号
		sb.append(payID);
		sb.append("\"&subject=\"");//商品的标题/交易标题/订单标 题/订单关键字等。该参数最长为 128 个汉字
		sb.append(getString(R.string.app_name) + "Android支付宝账号充值，充值编号：" + payID);//
		sb.append("\"&body=\"");//对一笔交易的具体描述信息。如 果是多种商品,请将商品描述字 符串累加传给 body。
		sb.append(getString(R.string.app_name) + "Android支付宝账号充值，充值编号：" + payID);
		sb.append("\"&total_fee=\"");//该笔订单的资金总额,单位为 RMB-Yuan。取值范围为[0.01, 100000000.00],精确到小数点 后两位。
		sb.append(String.format("%.2f", payMoney));
		sb.append("\"&notify_url=\"");//支付宝服务器主动通知商户网 站里指定的页面 http 路径。

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(HJAppConfig.DOMAIN + "/Alipay/iosnotify.aspx"));
		sb.append("\"&service=\"mobile.securitypay.pay");//接口名称(固定值)
		sb.append("\"&_input_charset=\"UTF-8");//编码格式,固定值 
		sb.append("\"&return_url=\"");//支付宝处理完请求后,当前页面 自动跳转到商户网站里指定页 面的 http 路径。可为空
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");//默认值为:1(商品购买)。
		sb.append("\"&seller_id=\"");//卖家支付宝账号对应的支付宝 唯一用户号。以 2088 开头的纯 16 位数字。
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");
		payID = "";
		return new String(sb);
	}
	public void updateImage() {

		if (btShopZ1 == null && btShopZ2 == null) {
			Message msg = new Message();
			msg.what = UIHandler.UPDATA_IMAGE_FINISH;
			mHandler.sendMessage(msg);
			return;
		}

		if (btShopZ1 != null) {

			dialogStr = getResources().getString(R.string.user_pro0_u_notice)/*"上传身份证，请稍后..."*/;
			showDialog(322);

			HashMap<String, String> localHashMap = new HashMap<String, String>();
			// / type=1 表示上传商家图片 id 表示商家编号
			// / type=2 表示上传菜品图片 id 表示菜品编号
			// / ext=jpg 表示后缀名
			localHashMap.put("id", app.userInfo.userId);
			localHashMap.put("type", "5");
			localHashMap.put("ext", imgExt);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			btShopZ1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] img = baos.toByteArray();
			localHttpManager = new HttpManager(SystemSet.this, SystemSet.this,
					"APP/Android/androidupload.ashx", localHashMap,
					img, 3);

			localHttpManager.sendImage();

			btShopZ1 = null;
			return;
		}

		if (btShopZ2 != null) {

			dialogStr = getResources().getString(R.string.user_pro1_u_notice)/*"上传健康证，请稍后..."*/;
			showDialog(322);

			HashMap<String, String> localHashMap = new HashMap<String, String>();
			// / type=1 表示上传商家图片 id 表示商家编号
			// / type=2 表示上传菜品图片 id 表示菜品编号
			// / ext=jpg 表示后缀名
			localHashMap.put("id", app.userInfo.userId);
			localHashMap.put("type", "5");
			localHashMap.put("ext", imgExt);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			btShopZ2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] img = baos.toByteArray();
			localHttpManager = new HttpManager(SystemSet.this, SystemSet.this,
					"APP/Android/Deliver/androidupload.ashx", localHashMap,
					img, 3);

			localHttpManager.sendImage();

			btShopZ2 = null;
			return;
		}

	}
	
	public void startPhotoZoom(Uri uri) {

		try {// 获取图片扩展名
				// BitmapFactory.Options opts = new BitmapFactory.Options();
				// opts.inJustDecodeBounds = true; // 确保图片不加载到内存
			Rect outPadding = null;
			ContentResolver cr = this.getContentResolver();
			// BitmapFactory.decodeStream(cr.openInputStream(uri), outPadding,
			// opts);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			btNow = BitmapFactory.decodeStream(cr.openInputStream(uri),
					outPadding, options); // 此时返回bm为空
			options.inJustDecodeBounds = false;
			// 缩放比
			int be = (int) (options.outHeight / (float) 215);
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;
			options.inSampleSize = Util.computeSampleSize(options, -1,
					1024 * 768);
			// options.inJustDecodeBounds = true;
			// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
			btNow = BitmapFactory.decodeStream(cr.openInputStream(uri),
					outPadding, options); // 此时返回bm为空
			/*btNow = watermarkBitmap(btNow, BitmapFactory.decodeResource(
					getResources(), R.drawable.icon), null);*/
			imgExt = options.outMimeType;
			if (imgExt != null) {
				imgExt = imgExt.split("/")[1];
			}
			nowEditRl.setBackgroundDrawable(new BitmapDrawable(btNow));
			if (nowEditRl == rlMyIcon) {
				btShopZ1 = btNow;
			} else {
				btShopZ2 = btNow;
			}
			updateImage();
			// imageUri = uri;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			imgExt = "jpg";
		}
	}
	
	public void SelectImageFrom() {
		if(imageItems == null || imageItems.length == 0){
			imageItems = new String[2];// { "手机相册", "手机拍照" };
			imageItems[0] = getResources().getString(R.string.public_img_phone);/*手机相册*/
			imageItems[1] = getResources().getString(R.string.public_img_camare);/*手机拍照*/	
		}
		new AlertDialog.Builder(SystemSet.this)
				.setTitle(getResources().getString(R.string.public_img_notice)/*"请选择图片"*/)
				.setItems(imageItems, new DialogInterface.OnClickListener() {
					// @Override
					public void onClick(DialogInterface dialog, int which) {
						gotoSelectImg = true;
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							/* 开启Pictures画面Type设定为image */
							intentFromGallery.setType("image/*");

							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							/* 取得相片后返回本画面 */
							SystemSet.this.startActivityForResult(
									intentFromGallery, IMAGE_REQUEST_CODE);
							break;
						case 1:
							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (android.os.Environment
									.getExternalStorageState()
									.equals(android.os.Environment.MEDIA_MOUNTED)) {

								SimpleDateFormat DateFormat = new SimpleDateFormat(
										"yyyyMMddHHmmss");
								Date curDate = new Date(System
										.currentTimeMillis());
								IMAGE_CAPTURE_NAME = DateFormat.format(curDate)
										+ ".jpg";
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(SDCARD_ROOT_PATH
												+ SAVE_PATH_IN_SDCARD,
												IMAGE_CAPTURE_NAME)));
							}
							SystemSet.this.startActivityForResult(
									intentFromCapture, CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton(getResources().getString(R.string.public_cancel)/*"取消"*/, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			Uri uri = null;
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:

				uri = data.getData();
				Log.e("uri", uri.toString());
				startPhotoZoom(uri);
				break;
			case CAMERA_REQUEST_CODE:
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ SAVE_PATH_IN_SDCARD + IMAGE_CAPTURE_NAME);
				uri = Uri.fromFile(tempFile);
				startPhotoZoom(uri);
				break;
			case RESULT_REQUEST_CODE:
				// uri = data.getData();
				btNow = data.getExtras().getParcelable("data");
				if (btNow != null) {

					/*
					 * progressDialog = ProgressDialog.show(FoodList.this, "",
					 * "正在上传图片，请稍后..."); HashMap<String, String> localHashMap =
					 * new HashMap<String, String>(); // / type=1 表示上传商家图片 id
					 * 表示商家编号 // / type=2 表示上传菜品图片 id 表示菜品编号 // / ext=jpg 表示后缀名
					 * localHashMap.put("id", commentDataID);
					 * localHashMap.put("type", "3"); localHashMap.put("ext",
					 * imgExt); ByteArrayOutputStream baos = new
					 * ByteArrayOutputStream();
					 * btLogo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					 * byte[] img = baos.toByteArray(); localHttpManager = new
					 * HttpManager(FoodList.this, FoodList.this,
					 * "Android/togo/androidupload.ashx", localHashMap, img, 2);
					 * 
					 * localHttpManager.sendImage();
					 */

				}
				break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void initUI() {
		
		View.OnClickListener onClickListener1 = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (checkLogin()) {
					Intent intent;
					switch (v.getId()) {
					case R.id.tv_password:
						intent = new Intent(SystemSet.this, ResetPassword.class);
						intent.putExtra("pay", false);
						startActivity(intent);
						break;
					case R.id.tv_pay_password:
						intent = new Intent(SystemSet.this, ResetPassword.class);
						intent.putExtra("pay", true);
						startActivity(intent);
						break;
					case R.id.tv_n_point:
						intent = new Intent(SystemSet.this, MyPointListView.class);
						startActivity(intent);
						break;
					case R.id.tv_orderlist:
						intent = new Intent(SystemSet.this, OrderList.class);
						intent.putExtra("ordertype", "2");
						intent.putExtra("orderMode", 2);//跑腿订单
						startActivity(intent);
						break;
					case R.id.tv_myfriends:
						intent = new Intent(SystemSet.this, FsgShopList.class);
						intent.putExtra("isRem", 7);//收藏商家
						startActivity(intent);
						break;
					case R.id.tv_myaddress:
						intent = new Intent(SystemSet.this, UserAddressList.class);
						intent.putExtra("select", false);
						startActivity(intent);
						break;
					case R.id.tv_giftlist:
						intent = new Intent(SystemSet.this, GiftOrderList.class);
						startActivity(intent);
						break;
					case R.id.tv_n_money:
						InputDialogActivity input = new InputDialogActivity(
								SystemSet.this, SystemSet.this.getResources().getString(R.string.mor_acount_input) /*"账户充值"*/, "", "", 255,
								1, true,
								new InputDialogActivity.OnClickOKListener() {

									

									@Override
									public void getInput(String value1, String value2) {
										
										
										payMoney = Float.parseFloat(value1);
										gotoAliPay();
										
									}

								});
						input.show();
						break;
					case R.id.ll_account://充值记录
						intent = new Intent(SystemSet.this, MyAccountListView.class);
						startActivity(intent);
						break;
					default:
						break;
					}
				}
				
			}
		};
		LinearLayout llLayout = (LinearLayout)findViewById(R.id.ll_account);
		llLayout.setOnClickListener(onClickListener1);
		
		TextView tv = (TextView) findViewById(R.id.tv_password);
		tv.setOnClickListener(onClickListener1);
		
		tv = (TextView)findViewById(R.id.tv_giftlist);
		tv.setOnClickListener(onClickListener1);
		
		tv = (TextView) findViewById(R.id.tv_pay_password);
		tv.setOnClickListener(onClickListener1);
		
		tv = (TextView)findViewById(R.id.tv_n_point);
		tv.setOnClickListener(onClickListener1);
		
		// 我的订单
		tv = (TextView) findViewById(R.id.tv_orderlist);
		tv.setOnClickListener(onClickListener1);
		
		tv = (TextView) findViewById(R.id.tv_myfriends);
		tv.setOnClickListener(onClickListener1);
		
		tv = (TextView) findViewById(R.id.tv_myaddress);
		tv.setOnClickListener(onClickListener1);
		
		tv = (TextView) findViewById(R.id.tv_n_money);
		tv.setOnClickListener(onClickListener1);
		
		btnExit = (Button)findViewById(R.id.user_loginout_btn);
		btnExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.userInfo.userId = "";
				app.userInfo.userName = "";
				app.userInfo.myICO = "";
				btnExit.setVisibility(View.GONE);
				OtherManager.saveUserInfo(SystemSet.this, app.userInfo);
				llLogin.setVisibility(View.VISIBLE);
				rlMyIcon.setTag(app.userInfo.myICO);
				rlMyIcon.setBackgroundResource(R.drawable.user_icon);
				llUserInfo.setVisibility(View.GONE);
				tvUserName.setText("");
				tvMyMoeny.setText("￥0.00");
				tvMyPoint.setText("0");
				
			}
		});
		tvMyPoint = (TextView)findViewById(R.id.tv_point);
		tvMyMoeny = (TextView)findViewById(R.id.tv_money);
		rlMyIcon = (RelativeLayout)findViewById(R.id.rl_my_icon);
		rlMyIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nowEditRl = rlMyIcon;
				SelectImageFrom();
			}
		});
		/*linearLayout = (TextView) findViewById(R.id.tv_n_money);
		linearLayout.setOnClickListener(onClickListener1);*/
		

		View.OnClickListener onClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.ll_about) {
					Intent aintent = null;
					aintent = new Intent(SystemSet.this, About.class);
					startActivity(aintent);
				} else if (v.getId() == R.id.ll_opinin) {//语言切换
					//final CharSequence[] items = {"Red", "Green", "Blue"};  

					AlertDialog.Builder builder = new AlertDialog.Builder(SystemSet.this);  

					builder.setTitle(getString(R.string.mor_language));  

					builder.setItems(langs, new DialogInterface.OnClickListener() {  

					    public void onClick(DialogInterface dialog, int item) {  

					       // Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();  
					    	switch (item) {
							case 0:
								app.changeAppLanguage("WY");
								saveLanguage("WY");
								break;
								
							default:
								app.changeAppLanguage("CN");
								saveLanguage("CN");
								break;
							}
					    }  

					});  

					AlertDialog alert = builder.create(); 
					alert.show();
				}/* else if (v.getId() == R.id.ll_join) {//加入我们
					
				} */else if(v.getId() == R.id.ll_share){
					/*StringBuffer localStringBuffer1 = new StringBuffer();
					String str1 = getResources().getString(R.string.mor_share_info)//您好,我在使用杭景科技手机版App，手机上直接可以点外卖很方便不妨你也试用一下：
							+HJAppConfig.URL+ "/" + HJAppConfig.APKNAME;
					localStringBuffer1.append(str1);

					try {
						Intent localIntent1 = new Intent(
								"android.intent.action.SEND");
						localIntent1.setType("text/plain");
						String str2 = localStringBuffer1.toString();
						localIntent1.putExtra("android.intent.extra.TEXT", str2);
						startActivity(localIntent1);
						return;
					} catch (Exception localException) {
						OtherManager.Toast(SystemSet.this, getResources().getString(R.string.mor_share_notice));//您的手机目前不支持该分享
					}*/
					showShare();
				}else if (v.getId() == R.id.ll_connect) {//联系我们
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SystemSet.this);
					builder.setMessage(getResources().getString(R.string.mor_con))//"联系我们：0571-28879706")
							.setPositiveButton(getResources().getString(R.string.public_ok),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											Uri localUri = Uri.parse(String.format("tel:%s", getResources().getString(R.string.mor_tel)));
											Intent localIntent1 = new Intent(
													"android.intent.action.CALL", localUri);
											startActivity(localIntent1);
											dialog.cancel();
										}
									})
							.setNegativeButton(getResources().getString(R.string.public_cancel),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									}).create().show();
				} else if (v.getId() == R.id.ll_point) {//积分规则
				} else if (v.getId() == R.id.ll_activity) {//积分兑换
					/*if (app.userInfo != null && app.userInfo.userId != null && !app.userInfo.userId.equals("0") && !app.userInfo.userId.equals("")) {
						Toast.makeText(SystemSet.this, "您已登陆订我吧，无需注册", 5).show();
					}else{
						Intent intent = null;
						intent = new Intent(SystemSet.this, Regedit.class);
						startActivity(intent);
					}*/
					Intent intent = null;
					intent = new Intent(SystemSet.this, GiftList.class);
					startActivity(intent);
				} else if (v.getId() == R.id.ll_update) {
					Intent cintent = null;
					cintent = new Intent(SystemSet.this, CheckUpdate.class);
					startActivity(cintent);
				} else if(v.getId() == R.id.logined_container){
					Intent intent = new Intent(SystemSet.this, UserInfo.class);
					startActivity(intent);
				}
			}
		};
		TextView linearLayout = (TextView)findViewById(R.id.ll_about);
		linearLayout.setOnClickListener(onClickListener);
		
		/*linearLayout = (LinearLayout)findViewById(R.id.ll_opinin);
		linearLayout.setOnClickListener(onClickListener);
		
		linearLayout = (LinearLayout)findViewById(R.id.ll_join);
		linearLayout.setOnClickListener(onClickListener);*/
		
		linearLayout = (TextView)findViewById(R.id.ll_share);
		linearLayout.setOnClickListener(onClickListener);
		
		linearLayout = (TextView)findViewById(R.id.ll_connect);
		linearLayout.setOnClickListener(onClickListener);
		
		/*linearLayout = (LinearLayout)findViewById(R.id.ll_point);
		linearLayout.setOnClickListener(onClickListener);*/
		
		linearLayout = (TextView)findViewById(R.id.ll_activity);
		linearLayout.setOnClickListener(onClickListener);
		tvTitle = (TextView)findViewById(R.id.title_bar_content_tv);
		tvTitle.setText(getResources().getString(R.string.nav_3));
		
		linearLayout = (TextView)findViewById(R.id.ll_update);
		linearLayout.setOnClickListener(onClickListener);
		
		llLogin = (LinearLayout)findViewById(R.id.has_not_login_container);
		llUserInfo = (LinearLayout)findViewById(R.id.logined_container);
		llUserInfo.setOnClickListener(onClickListener);
		tvUserName = (TextView)findViewById(R.id.username);
		tvPhone = (TextView)findViewById(R.id.integral);
		btnLogin = (Button)findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent localIntent4 = new Intent(SystemSet.this,
						Login.class);
				localIntent4.putExtra("isReturn", true);
				startActivity(localIntent4);
			}
		});
	}
	
	private void GetUserInfo(){
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		
		localHashMap.put("userid", app.userInfo.userId);

		localHttpManager = new HttpManager(SystemSet.this, SystemSet.this,
				"/Android/GetUserInfo.aspx?", localHashMap, 2, 1);

		localHttpManager.getRequeest();
	}
	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt == 260) {
			
				String value = (String)paramObject;
				try {
					JSONObject json = new JSONObject(value);
					switch (tag) {
					case 1:
						app.userInfo.userName = json.getString("username");
						app.userInfo.email = json.getString("email");
						app.userInfo.QQ = json.getString("qq");
						app.userInfo.phone = json.getString("phone");
						app.userInfo.userMoney = Float.valueOf(json.getString("HaveMoney"));
						app.userInfo.trueName = json.getString("truename");
						app.userInfo.nPoint = json.getString("Point");
						app.userInfo.hPoint = json.getString("historypoint");
						app.userInfo.pPoint = json.getString("publicgood");
						value = json.getString("pic");
						if (value.length() != 0) {
							app.userInfo.myICO = value;
						}
						//app.userInfo.myICO = json.getString("pic");
						
						
						OtherManager.saveUserInfo(SystemSet.this, app.userInfo);
						message.what = UIHandler.GET_SUCCESS ;
						break;
					case 2:
						
						break;
					case 3://上传头像
						int status = 0;
						
						status = json.getInt("state");
						
						removeDialog(322);
						if (status == 1) {
							app.userInfo.myICO = json.getString("pic");
							message.what = UIHandler.UPDATA_IMAGE_SUCESS;
							// shopDetailModel.setPic(json.getString("pic"));

						} else {
							message.what = UIHandler.UPDATA_IMAGE_FAILED;
						}
						break;
					default:
						break;
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message.what = UIHandler.NET_ERROR ;
				}
			
		}else{
			message.what = UIHandler.NET_ERROR ;
		}
		mHandler .sendMessage(message);
	}
	
	private class UIHandler extends Handler {
		public static final int UPDATA_IMAGE_FINISH = 2;
		public final static int NET_ERROR = -1;
		static final int GET_SUCCESS = 1; //
		public static final int UPDATA_IMAGE_FAILED = 3;
		public static final int UPDATA_IMAGE_SUCESS = 4;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATA_IMAGE_FAILED:
				Toast.makeText(SystemSet.this, getResources().getString(R.string.public_img_up_faild)/*"图片上传失败！请稍后再试！"*/, 15).show();
				break;
			case UPDATA_IMAGE_SUCESS:
				// SetUserInfo();
				updateImage();

				break;
			case UPDATA_IMAGE_FINISH:
				/*if (app.userInfo.myICO.length() > 0) {
					rlMyIcon.setTag(app.userInfo.myICO);
					app.mLoadImage.addTask(app.userInfo.myICO,
							rlMyIcon, R.drawable.user_icon);
					app.mLoadImage.doTask();
				}*/
				Toast.makeText(SystemSet.this, getResources().getString(R.string.public_img_up_s)/*"图片上传成功！"*/, 15).show();
				break;
			case GET_SUCCESS: {
				// 显示数据列表
				if (app.userInfo.myICO.length() > 0) {
					rlMyIcon.setTag(app.userInfo.myICO);
					app.mLoadImage.addTask(app.userInfo.myICO,
							rlMyIcon, R.drawable.user_icon);
					app.mLoadImage.doTask();
				}
				tvUserName.setText(app.userInfo.userName);
				tvPhone.setText(app.userInfo.phone);
				tvMyPoint.setText(String.valueOf(app.userInfo.nPoint));
				tvMyMoeny.setText(String.format("￥%.2f", app.userInfo.userMoney));
				
				return;
			}
			case NET_ERROR: {
				
				OtherManager.Toast(SystemSet.this, getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误，无法获取用户信息"*/);
				return;
			}
			}
			// OtherManager.Toast(AddOrder.this, "获取数据失败");
			return;
		}
	}
}
