package com.ihangjing.ZJYXForAndroid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.CommentModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.Util;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class CommentDetail extends HjActivity implements HttpRequestListener {

	private String foodid;
	private String orderid;
	private TextView tvServer;
	private TextView tvTaste;
	private TextView tvOut;
	private EditText etValue;
	private RatingBar rbPoint;
	private String foodName;
	private TextView tvFoodName;
	private Button btnAdd;
	CommentModel model;
	int taste = 5;// 口味
	int out = 5;// 外观
	int server = 5;// 服务
	private HttpManager localHttpManager;
	UIHandler mHandler;
	String items[];// = { getResources().getString(R.string.com_default_0), getResources().getString(R.string.com_default_1), getResources().getString(R.string.com_default_2), getResources().getString(R.string.com_default_3), getResources().getString(R.string.com_default) };
	private ImageView ivImage;
	
	private static final int IMAGE_REQUEST_CODE = 0;// 设置本地选择照片的标志
	private static final int CAMERA_REQUEST_CODE = 1; // 设置拍照操作的标志
	public static final String SDCARD_ROOT_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();// 路径
	public static final String SAVE_PATH_IN_SDCARD = "/DCIM/"; // 图片及其他数据保存文件夹
	public String IMAGE_CAPTURE_NAME = "cameraTmp.jpg"; // 照片名称
	private String[] imageItems;// = new String[] { getResources().getString(R.string.public_img_phone), getResources().getString(R.string.public_img_camare) };
	private static final int RESULT_REQUEST_CODE = 2;
	private Bitmap btLogo;
	private String imgExt = "jpg";
	private ProgressDialog progressDialog;
	private String commentDataID;
	//boolean bSelectImg = false;// false,表示Dialog选择的图片， true表示冲本界面弹出的选择的图片
	//private Uri imageUri = null;
	private String shopID;
	private String strMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		items = new String[5];//{ getResources().getString(R.string.com_default_0), getResources().getString(R.string.com_default_1), getResources().getString(R.string.com_default_2), getResources().getString(R.string.com_default_3), getResources().getString(R.string.com_default) };
		items[0] = getResources().getString(R.string.com_default_0);
		items[1] = getResources().getString(R.string.com_default_1);
		items[2] = getResources().getString(R.string.com_default_2);
		items[3] = getResources().getString(R.string.com_default_3);
		items[4] = getResources().getString(R.string.com_default);
		
		imageItems = new String[2];
		imageItems[0] = getResources().getString(R.string.public_img_phone);
		imageItems[1] = getResources().getString(R.string.public_img_camare);
		/*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable ex) {
			//任意一个线程异常后统一的处理
				System.out.println(ex.getLocalizedMessage());

				finish();
			}
		});*/
		model = new CommentModel();
		mHandler = new UIHandler();
		setContentView(R.layout.comment_detail);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			foodid = extras.getString("foodid");
			orderid = extras.getString("OrderID");
			foodName = extras.getString("foodName");
			shopID = extras.getString("ShopID");
		}
		TextView tvTitle = (TextView)findViewById(R.id.title_bar_content_tv);
		tvTitle.setText(getResources().getString(R.string.com_add));
		Button btnBack = (Button)findViewById(R.id.title_bar_back_btn);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		tvFoodName = (TextView) findViewById(R.id.show_news_title);
		tvFoodName.setText(foodName);
		
		ivImage = (ImageView)findViewById(R.id.iv_image);
		ivImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SelectImageFrom();
			}
		});
		
		tvServer = (TextView) findViewById(R.id.tv_server);
		tvServer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(CommentDetail.this);

				builder.setTitle(getResources().getString(R.string.com_s_notice));

				builder.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int item) {
						tvServer.setText(items[item]);
						server = item + 1;
					}

				});
				
				AlertDialog alert = builder.create();
				
				alert.show();
			}
		});
		tvTaste = (TextView) findViewById(R.id.tv_taste);
		tvTaste.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(CommentDetail.this);

				builder.setTitle(getResources().getString(R.string.com_s_notice));

				builder.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int item) {
						tvTaste.setText(items[item]);
						taste = item + 1;
					}

				});
				
				AlertDialog alert = builder.create();
				
				alert.show();
			}
		});
		tvOut = (TextView) findViewById(R.id.tv_out);
		tvOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(CommentDetail.this);

				builder.setTitle(getResources().getString(R.string.com_s_notice));

				builder.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int item) {
						tvOut.setText(items[item]);
						out = item + 1;
					}

				});
				
				AlertDialog alert = builder.create();
				
				alert.show();
			}
		});
		etValue = (EditText) findViewById(R.id.et_dis);
		rbPoint = (RatingBar) findViewById(R.id.shopdetai_RatingBar02);
		rbPoint.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				float x = event.getX();
				// float y = event.getY();
				float x_x = v.getWidth() / 5;
				int p = (int) (x / x_x) + 1;
				rbPoint.setRating(p);
				return false;
			}
		});

		btnAdd = (Button) findViewById(R.id.btn_submit);
		btnAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				model.setFoodID(foodid);
				model.setFoodName(foodName);
				model.setFlavorG(String.valueOf(taste));
				model.setServerG(String.valueOf(server));
				model.setOutG(String.valueOf(out));
				model.setOdid(orderid);
				model.setValue(etValue.getText().toString());
				model.setUserID(app.userInfo.userId);
				model.setUserName(app.userInfo.userName);
				model.setPoint((int) rbPoint.getRating());
				UpdataComment();
			}
		});

	}

	public void SelectImageFrom(){
		new AlertDialog.Builder(CommentDetail.this)
		.setTitle(getResources().getString(R.string.public_img_notice))
		.setItems(imageItems,
				new DialogInterface.OnClickListener() {
					// @Override
					public void onClick(DialogInterface dialog,
							int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							/* 开启Pictures画面Type设定为image */
							intentFromGallery.setType("image/*");

							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							/* 取得相片后返回本画面 */
							CommentDetail.this
									.startActivityForResult(
											intentFromGallery,
											IMAGE_REQUEST_CODE);
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
								IMAGE_CAPTURE_NAME = DateFormat
										.format(curDate) + ".jpg";
								intentFromCapture
										.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(new File(
														SDCARD_ROOT_PATH
																+ SAVE_PATH_IN_SDCARD,
														IMAGE_CAPTURE_NAME)));
							}
							CommentDetail.this
									.startActivityForResult(
											intentFromCapture,
											CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
		.setNegativeButton(getResources().getString(R.string.public_cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
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
				//uri = data.getData();
				btLogo = data.getExtras().getParcelable("data");
				if (btLogo != null) {
					
						/*progressDialog = ProgressDialog.show(CommentDetail.this,
								"", "正在上传图片，请稍后...");
						HashMap<String, String> localHashMap = new HashMap<String, String>();
						// / type=1 表示上传商家图片 id 表示商家编号
						// / type=2 表示上传菜品图片 id 表示菜品编号
						// / ext=jpg 表示后缀名
						localHashMap.put("id", commentDataID);
						localHashMap.put("type", "3");
						localHashMap.put("ext", imgExt);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						btLogo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						byte[] img = baos.toByteArray();
						localHttpManager = new HttpManager(CommentDetail.this,
								CommentDetail.this,
								"Android/togo/androidupload.ashx",
								localHashMap, img, 2);

						localHttpManager.sendImage();*/
					
				}
				break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void updateImage()
	{
		if (btLogo != null) {
			
			//try {// 获取图片扩展名
				/*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                ContentResolver cr = this.getContentResolver();
                Rect outPadding = null;
                btLogo  = BitmapFactory.decodeStream(cr.openInputStream(imageUri), outPadding,
                		options); //此时返回bm为空
                options.inJustDecodeBounds = false;
               //缩放比
                int be = (int)(options.outHeight / (float)215);
                 if (be <= 0)
                     be = 1;
                options.inSampleSize = be;
                options.inSampleSize = Util.computeSampleSize(options, -1, 1024*768); 
                options.inJustDecodeBounds = true;
                 //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
                btLogo  = BitmapFactory.decodeStream(cr.openInputStream(imageUri), outPadding,
                		options); //此时返回bm为空
                
				*/
				
				HashMap<String, String> localHashMap = new HashMap<String, String>();
				// / type=1 表示上传商家图片 id 表示商家编号
				// / type=2 表示上传菜品图片 id 表示菜品编号
				// / ext=jpg 表示后缀名
				localHashMap.put("id", commentDataID);
				localHashMap.put("type", "3");
				localHashMap.put("ext", imgExt);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				btLogo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] img = baos.toByteArray();
				localHttpManager = new HttpManager(CommentDetail.this,
						CommentDetail.this,
						"Android/androidupload.ashx",
						localHashMap, img, 2);
	
				localHttpManager.sendImage();
			/*}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
		}else{
			Message msg = new Message();
			msg.what = UIHandler.UPDATA_IMAGE_SUCESS;
			mHandler.sendMessage(msg);
		}
		
	}
	
	public void startPhotoZoom(Uri uri) {

		try {// 获取图片扩展名
			//BitmapFactory.Options opts = new BitmapFactory.Options();
			//opts.inJustDecodeBounds = true; // 确保图片不加载到内存
			Rect outPadding = null;
			ContentResolver cr = this.getContentResolver();
			//BitmapFactory.decodeStream(cr.openInputStream(uri), outPadding,
					//opts);
			
			BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            btLogo  = BitmapFactory.decodeStream(cr.openInputStream(uri), outPadding,
            		options); //此时返回bm为空
            options.inJustDecodeBounds = false;
           //缩放比
            int be = (int)(options.outHeight / (float)215);
             if (be <= 0)
                 be = 1;
            options.inSampleSize = be;
            options.inSampleSize = Util.computeSampleSize(options, -1, 1024*768); 
            //options.inJustDecodeBounds = true;
             //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
            btLogo  = BitmapFactory.decodeStream(cr.openInputStream(uri), outPadding,
            		options); //此时返回bm为空
			imgExt = options.outMimeType;
			if (imgExt != null) {
				imgExt = imgExt.split("/")[1];
			}
			ivImage.setImageBitmap(btLogo);
			//imageUri = uri;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			imgExt = "jpg";
		}
		
		/*Intent intent = new Intent("com.android.camera.action.CROP");
		
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 1024);
		intent.putExtra("outputY", 768);
		intent.putExtra("return-data", true);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(intent, RESULT_REQUEST_CODE);*/
	}
	private void UpdataComment() {
		progressDialog = ProgressDialog.show(CommentDetail.this,
				"", getResources().getString(R.string.com_add_notice));//"提交评论中，请稍后...");
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("ordermodel", model.BeanToJSonString());
		localHashMap.put("shopid", shopID);
		localHashMap.put("username", model.getUserName());
		localHashMap.put("userid", model.getUserID());
		localHttpManager = new HttpManager(CommentDetail.this,
				CommentDetail.this, "Android/Submitreview.aspx?",
				localHashMap, 2, 1);
		localHttpManager.postRequest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt == 260) {
			String value = (String) paramObject;
			try {
				JSONObject jsonObject = new JSONObject(value);
				int state = 0;
				state = jsonObject.getInt("state");
				switch (tag) {
				case 1:
					
					if (state == 1) {
						message.what = UIHandler.UPDATA_SUCESS;
						commentDataID = jsonObject.getString("dataid");
					} else {
						message.what = UIHandler.UPDATA_FAILED;
						strMsg = jsonObject.getString("msg");
					}
					break;
				case 2:
					//{"pic":"","state":"-1"}
					if (state == 1) {
						message.what = UIHandler.UPDATA_IMAGE_SUCESS;
					}else{
						message.what = UIHandler.UPDATA_IMAGE_FAILED;
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
		} else {
			message.what = UIHandler.NET_ERROR;
		}
		mHandler.sendMessage(message);
	}

	private class UIHandler extends Handler {
		public static final int UPDATA_IMAGE_FAILED = -3;
		public static final int UPDATA_IMAGE_SUCESS = 2;
		public static final int UPDATA_FAILED = -2;
		static final int UPDATA_SUCESS = 1;// 第二页开始
		static final int NET_ERROR = -1;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what != UPDATA_SUCESS) {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}
			switch (msg.what) {
			case UPDATA_FAILED:
				Toast.makeText(CommentDetail.this, strMsg, 15).show();
				
				break;
			case NET_ERROR:
				Toast.makeText(CommentDetail.this, getResources().getString(R.string.public_net_or_data_error)/*"网络或数据错误，请稍后再试"*/, 15).show();
				
				
				break;
			case UPDATA_SUCESS:
				app.commentSucess = true;
				updateImage();
				
				break;
			case UPDATA_IMAGE_SUCESS:
				Toast.makeText(app, getResources().getString(R.string.com_add_sucess),//"评论成功，感谢您对我们的支持！",
						15).show();
				
				finish();
				break;
			case UPDATA_IMAGE_FAILED:
				Toast.makeText(app, getResources().getString(R.string.com_add_faild) + getResources().getString(R.string.public_img_up_faild),//"评论成功，图片上传失败！",
						15).show();
				finish();
				break;
			default:
				break;
			}
		}
	}
}
