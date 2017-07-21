package com.ihangjing.ZJYXForAndroid;

import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.alipay.sdk.app.PayTask;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.ihangjing.Model.FoodInOrderModelForGet;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.OrderModelForGet;
import com.ihangjing.Model.OrderStateListModel;
import com.ihangjing.Model.OrderStateModel;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.Model.TagModel;
import com.ihangjing.alipay.Keys;
import com.ihangjing.alipay.Result;
import com.ihangjing.alipay.SignUtils;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.NetManager;
import com.ihangjing.common.OtherManager;
import com.ihangjing.http.HttpException;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;
import com.ihangjing.wxpay.Constants;
import com.ihangjing.wxpay.MD5;
import com.ihangjing.wxpay.Util;
import com.tencent.mm.sdk.modelpay.PayReq;

//�����ύ�ɹ������ʾҳ��
public class AddOrder extends HjActivity implements HttpRequestListener{

	// {\"PNum\":\"1\",\"PId\":\"8009\",\"PPrice\":\"16\",\"PName\":\"��ͷ������\",\"Foodcurrentprice\":\"8\",\"owername\":\"1.6\"}
	private UIHandler hander;// = new UIHandler();
	private TextView shopnameTextView;
	private TextView orderidTextView;
	private TextView totalpriceTextView;
	private TextView addressTextView;
	//private TextView usernameTextView;
	//private TextView phoneTextView;
	private TextView orderaddtimeTextView;
	//private TextView senttimeTextView;
	private TextView sentmoneyTextView;
	private TextView stateshowImageView;
	private TextView showshopTextView;
	private TextView ordernoTextView;
	private TextView allpriceTextView;
	private TextView tvCardPay;
	private TextView tvCouponPay;
	private TextView tvUnpay;
	ProgressDialog progressDialog = null;
	private String shopidString;
	private String orderidString;
	private String orderstateString;

	private LinearLayout ll_food_list;

	private OrderModelForGet mOrderModel;

	private ListFoodAdapter listFoodAdapter;

	private static final String TAG = "AddOrder";


	
	private HttpManager localHttpManager;
	private TextView tvShopTel;
	private OnClickListener commentClickListener;
	protected Integer commentFoodIndex = 0;
	private Button btnCommand;
	private TextView tvSendTime;
	private TextView tvPayMode;
	private LinearLayout llPayStatus;
	private TextView tvPayStatus;
	private LinearLayout llPayMoney;
	private TextView tvPayMoney;
	private TextView tvPayTime;
	
	public float payMoney;
	
	private StringBuffer wxpay;
	private PayReq wxpayReq;
	public Map<String, String> resultunifiedorder;
	public String errorMSG;
	private String dialogStr;
	private String alipayInfo;
	private String payID;
	private Button btnGoPay;
	private TextView tvPromotion;
	private LinearLayout llICON;
	private ViewPager viewSwitcher;
	private ArrayList<View> listMainViews;
	private ListView lvState;
	private OrderStateListModel orderStateListMode;
	private RadioButton rbStateList;
	private RadioButton rbOrderInfo;
	private TextView tvPacketFee;
	private TextView tvSendTimeName;
	private TextView tvAddressName;
	private TextView tvOtherInfo;
	private int orderType;
	private int payType;
	public BaiduMap mBaiduMap;
	//public MapView mapView;
	public LatLng orderLatLng;
	public Button btnDeliver;
	public InfoWindow mInfoWindow;
	private int orderMode;//1���� 2����
	private RelativeLayout rlBtnOpt;
	private Button btnGoCommand;
	private Button btnCancel;
	private Button btnGoQuick;
	//public MapView mapView;
	private SnapshotReadyCallback mapViewSnap;
	protected Bitmap mapViewBitMap;
	public stateListAdapter mStateAdapter = new stateListAdapter();
	public ImageView imgMapView;
	private TextView tvTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extern = getIntent().getExtras();
		setContentView(R.layout.addorder);
		String msg = null;
		if (extern != null) {
			orderidString = /*"17020415132055550";//*/extern.getString("orderid", "");
			orderMode = extern.getInt("orderMode", 1);
			orderType = extern.getInt("orderType", 0);
			msg = extern.getString("msg");
			payType  = extern.getInt("payType", 4);
		}
		if(msg != null && !msg.equals("")){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(msg).setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.public_ok), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
						}

					});
			builder.create().show();
		}
		
		hander = new UIHandler();
		Button back = (Button)findViewById(R.id.title_bar_back_btn);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		OnClickListener callClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddOrder.this);
				builder.setMessage(getResources().getString(R.string.public_call_tel) +  mOrderModel.getTogoPhone())
						.setPositiveButton(getResources().getString(R.string.public_ok),
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										Uri localUri = Uri.parse(String.format("tel:%s", mOrderModel.getTogoPhone()));
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
			}
		};
		commentClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mOrderModel.getSendstate() == 3 && mOrderModel.getState() != 3) {
					AlertDialog.Builder builder = new AlertDialog.Builder(AddOrder.this);  

					 builder.setMessage(getResources().getString(R.string.order_detail_re_food))  

					        .setCancelable(false)  

					        .setPositiveButton(getResources().getString(R.string.public_ok), new DialogInterface.OnClickListener() {  

					            public void onClick(DialogInterface dialog, int id) {  

					            	ReOrderOK();  

					            }

								

					        })  

					        .setNegativeButton(getResources().getString(R.string.public_cancel), new DialogInterface.OnClickListener() {  

					            public void onClick(DialogInterface dialog, int id) {  

					                 dialog.cancel();  

					            }  

					        });  

					 AlertDialog alert = builder.create(); 
					 alert.show();
					
				}else{
					Intent intent = new Intent(AddOrder.this, CommentDetail.class);
					commentFoodIndex = (Integer)v.getTag();
					intent.putExtra("foodid", mOrderModel.getShopId());//mOrderModel.list.get(commentFoodIndex).getId());
					intent.putExtra("foodName", (String)v.getTag(R.id.tab_icon));
					intent.putExtra("OrderID", orderidString);
					intent.putExtra("ShopID", mOrderModel.getShopId());
					startActivity(intent);
				}
			}
		};
		RadioGroup rgTitle = (RadioGroup)findViewById(R.id.rg_opt);
		
		if (orderMode == 2) {
			rgTitle.setVisibility(View.GONE);
		}else{
			rgTitle.setVisibility(View.VISIBLE);
		}
		
		rgTitle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_unre:
					viewSwitcher.setCurrentItem(0);;
					break;
				case R.id.rb_re:
					viewSwitcher.setCurrentItem(1);
					break;
				
				default:
					break;
				}
			}
		});
		rbStateList = (RadioButton)findViewById(R.id.rb_unre);
		rbOrderInfo = (RadioButton)findViewById(R.id.rb_re);
		viewSwitcher = (ViewPager) findViewById(R.id.vs_select);
		viewSwitcher.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					/*if(mapView != null)
					{
						mapView.setVisibility(View.VISIBLE);
						mapView.onResume();
					}*/
						
					rbStateList.setChecked(true);
					break;

				default:
					/*if(mapView != null)
					{
						mapView.setVisibility(View.INVISIBLE);
						mapView.onPause();
					}*/
						
					rbOrderInfo.setChecked(true);
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		listMainViews = new ArrayList<View>();
		Button right = (Button)findViewById(R.id.title_bar_right_btn);
		right.setOnClickListener(callClickListener );
		if (orderMode == 1) {
			View vList = getLayoutInflater().inflate(R.layout.order_detail_state_list, null);
			listMainViews.add(vList);
			
			lvState = (ListView)vList.findViewById(R.id.lv_content);
			rlBtnOpt = (RelativeLayout)vList.findViewById(R.id.rl_btn);
			btnCancel = (Button)vList.findViewById(R.id.btn_cancel);
			btnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String info;
					if(mOrderModel.getShopState() == 1){
						info = "ȷ������ȡ�����ύ����ɹ����벦���̼ҵ绰��ͨ��";
					}else{
						info = getResources().getString(R.string.order_detail_cancel_notice);
					}
					//ȡ����������������ȡ������
					AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(
							AddOrder.this).setTitle(getResources().getString(R.string.public_notice)).setMessage(info);

					DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface,
								int paramInt) {

							
							// ����״̬��������
							CancelOrder();
						}
					};

					localBuilder1.setPositiveButton(getResources().getString(R.string.public_ok), local3);
					localBuilder1.setNeutralButton(getResources().getString(R.string.public_cancel), null).create().show();
				}
			});
			btnGoQuick = (Button)vList.findViewById(R.id.btn_quck);
			btnGoQuick.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						Date   curDate    =   new    Date(System.currentTimeMillis());
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date d1 = df.parse(mOrderModel.getAddtime());
						if(curDate.getTime() - d1.getTime() < mOrderModel.getSitename()){
							Toast.makeText(AddOrder.this, "δ�ﵽ�ߵ�ʱ�䣬�����ĵȴ���", 15).show();
							return;
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//�ߵ�
					QuickOrder();
				}
			});
			btnGoCommand = (Button)vList.findViewById(R.id.btn_go_command);
			btnGoCommand.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//ȷ���ջ�
					FinishOrder();
				}
			});
			/*mapViewSnap = new SnapshotReadyCallback() {
				
				@Override
				public void onSnapshotReady(Bitmap arg0) {
					// TODO Auto-generated method stub
					imgMapView.setImageBitmap(arg0);
					//mapView.setVisibility(View.GONE);
					mapViewBitMap = arg0;
					mStateAdapter.notifyDataSetChanged();
				}
			};
			mapView = (com.baidu.mapapi.map.MapView)vList.findViewById(R.id.bmap_view);
			
			mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
				
				@Override
				public void onMapStatusChangeStart(MapStatus arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onMapStatusChangeFinish(MapStatus arg0) {
					// TODO Auto-generated method stub
					LatLngBounds latlng = arg0.bound;
					double lat = latlng.getCenter().latitude;
					double lng = latlng.getCenter().longitude;
				}
				
				@Override
				public void onMapStatusChange(MapStatus arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
				
				@Override
				public void onMapLoaded() {
					// TODO Auto-generated method stub
					int a = 0;
					a++;
				}
			});
			mBaiduMap = mapView.getMap(); 
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(15);// ���õ�ͼ���ű�����17��100��
			mBaiduMap.setMapStatus(ms);*/
		}
		
		View vInfo = getLayoutInflater().inflate(R.layout.order_detail_info, null);
		listMainViews.add(vInfo);
		
		LinearLayout llShopName = (LinearLayout)vInfo.findViewById(R.id.linearLayout7);
		llShopName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShopListItemModel localShopModel = new ShopListItemModel();
				
				localShopModel.setName(mOrderModel.getShopname());
				localShopModel.setId(Integer.parseInt(mOrderModel.getShopid()));
				// TODO:���������¼
				app.setShop(localShopModel);
				Intent localIntent = new Intent(AddOrder.this,
						ShopDetail.class);
				startActivity(localIntent);
			}
		});
		
		btnGoPay = (Button)vInfo.findViewById(R.id.btn_gopay);
		btnGoPay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				payMoney = subtract(subtract(subtract(subtract(mOrderModel.getTotalMoney(), mOrderModel.getCardPay()), mOrderModel.getCouponPay()), mOrderModel.getPromotionmoney()), mOrderModel.getPayMoney());
				gotoOnLinePay();
			}
		});
		if(orderMode == 1){
			LinearLayout llBuy =  (LinearLayout)vInfo.findViewById(R.id.rl_buyagain);
			llBuy.setVisibility(View.VISIBLE);
			Button btnBuyAgain = (Button)vInfo.findViewById(R.id.btn_buy_again);
			btnBuyAgain.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBuyAgain(mOrderModel.getOrderid());
				}
			});
		}
		
		btnCommand = (Button)vInfo.findViewById(R.id.btn_command);
		btnCommand.setOnClickListener(commentClickListener);
		btnCommand.setTag(R.id.tab_icon, "");
		tvSendTime = (TextView)vInfo.findViewById(R.id.addorder_sendtime);
		shopnameTextView = (TextView) vInfo.findViewById(R.id.addorder_shopname);
		totalpriceTextView = (TextView) vInfo.findViewById(R.id.addorder_totalprice);
		addressTextView = (TextView) vInfo.findViewById(R.id.addorder_address);
		//usernameTextView = (TextView) findViewById(R.id.addorder_username);
		//phoneTextView = (TextView) findViewById(R.id.addorder_userphone);
		orderaddtimeTextView = (TextView) vInfo.findViewById(R.id.addorder_addtime);
		//senttimeTextView = (TextView) findViewById(R.id.addorder_senttime);
		// peopleTextView = (TextView) findViewById(R.id.addorder_people);
		orderidTextView = (TextView) vInfo.findViewById(R.id.addorder_orderid);
		stateshowImageView = (TextView) vInfo.findViewById(R.id.addorder_ordericon);
		showshopTextView = (TextView) vInfo.findViewById(R.id.addorder_tv_showshop);
		ordernoTextView = (TextView) vInfo.findViewById(R.id.addorder_tv_ordernote);
		sentmoneyTextView = (TextView) vInfo.findViewById(R.id.addorder_sentmoney);
		tvPacketFee = (TextView)vInfo.findViewById(R.id.tv_packetfee);
		tvSendTimeName = (TextView)vInfo.findViewById(R.id.tv_sendtime_name);
		tvAddressName = (TextView)vInfo.findViewById(R.id.textView27);
		tvOtherInfo = (TextView)vInfo.findViewById(R.id.tv_other_info);
		allpriceTextView = (TextView) vInfo.findViewById(R.id.addorder_allprice);
		tvPayMode = (TextView)vInfo.findViewById(R.id.addorder_paymode);
		llPayStatus = (LinearLayout)vInfo.findViewById(R.id.ll_paystatus);
		tvPayStatus = (TextView)vInfo.findViewById(R.id.addorder_paystatus);
		llPayMoney = (LinearLayout)vInfo.findViewById(R.id.ll_paymoney);
		tvPayMoney = (TextView)vInfo.findViewById(R.id.addorder_paymoney);
		tvPayTime = (TextView)vInfo.findViewById(R.id.addorder_paytime);
		tvPromotion = (TextView)vInfo.findViewById(R.id.tv_promotion);//�����Żݽ��
		llICON = (LinearLayout)vInfo.findViewById(R.id.ll_icon);//�μӵĻ�б�
		tvCardPay = (TextView) vInfo.findViewById(R.id.tv_card_pay);
		tvCouponPay = (TextView) vInfo.findViewById(R.id.addorder_couponprice);
		tvUnpay = (TextView) vInfo.findViewById(R.id.addorder_unpayprice);
		tvShopTel = (TextView)vInfo.findViewById(R.id.addorder_senttime);
		tvShopTel.setOnClickListener(callClickListener);
		ll_food_list = (LinearLayout) vInfo.findViewById(R.id.addorder_ll_foodlist);
		tvTitle = (TextView)findViewById(R.id.textView1);
		if(orderMode == 2){
			//tvGetAddrInfo = (TextView)vInfo.findViewById(R.id.tv_get_addr);
			llShopName.setVisibility(View.GONE);
			
			LinearLayout llView = (LinearLayout)vInfo.findViewById(R.id.ll_food_list);//��Ʒ��������
			llView.setVisibility(View.GONE);
			llView = (LinearLayout)vInfo.findViewById(R.id.ll_card_info);//�Ż���Ϣ
			llView.setVisibility(View.GONE);
			llView = (LinearLayout)vInfo.findViewById(R.id.ll_moeny_info);//���,֧����Ϣ
			llView.setVisibility(View.GONE);
			TextView tvView =  (TextView)vInfo.findViewById(R.id.tv_order_title);//��ȡ����Ϣ
			tvView.setText(getResources().getString(R.string.showorder_order_get_info));
			
			tvView = (TextView)vInfo.findViewById(R.id.textView11);//�ĳ�������Ʒ��
			tvView.setText(getResources().getString(R.string.shop_addorder_send_goods));
			
			tvView = (TextView)vInfo.findViewById(R.id.tv_packfee_title);//�����Ϣ�ĳ�ȡ����Ϣ
			tvView.setText(getResources().getString(R.string.showorder_order_get_info_m));
			
			//RelativeLayout rlView = (RelativeLayout)vInfo.findViewById(R.id.linearLayout11);//���ͷ���Ϣ
			//rlView.setVisibility(View.GONE);
			//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.textView10);
			//params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
			totalpriceTextView.setGravity(Gravity.LEFT);
			//params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.tv_packfee_title);
			//params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
			tvPacketFee.setGravity(Gravity.LEFT);
			/*llView = (LinearLayout)vInfo.findViewById(R.id.ll_get_info);
			llView.setVisibility(View.VISIBLE);
			llView = (LinearLayout)vInfo.findViewById(R.id.ll_state);
			llView.setVisibility(View.VISIBLE);*/
		}
		// ��ȡ��������
		// �������:�Ӷ����ύҳ������ҳ��
		// 1. �����ύ�ɹ������
		// 2. TODO: �����ݿ��л�ȡ������Ϣ(�ύ�����󱾵ؽ��д洢,�����������������ȡ)
		// �������ݿ��� ����ʾ���صĶ�����Ϣ��������� ��������

		// OrderModelForGet orderModel = new OrderModelForGet();
		// String ordermodelString = getIntent().getStringExtra("ordermodel");

		mOrderModel = new OrderModelForGet();
		
		
		
		

		// �����ȡ������ϸ��Ϣ //���߱��ػ����ȡ������Ϣ
		// ��������
		
		
		

		

		showshopTextView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = null;
				intent = new Intent(AddOrder.this, ShopDetail.class);
				intent.putExtra("shopid", shopidString);
				startActivity(intent);
			}
		});
		
		progressDialog = ProgressDialog.show(AddOrder.this, "", getResources().getString(R.string.public_get_data_notice));
		
		
		wxpayReq = new PayReq();
		wxpay=new StringBuffer();

		app.msgApi.registerApp(Constants.APP_ID);
		
		viewSwitcher.setAdapter(new HJPagerAdapter(listMainViews));

		

		Log.v(TAG, "AddOrder onCreate() end");
	}
	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
		public void CheckBuyAgain(String odid){
					Log.v(TAG, "���������ȡ���ݿ�ʼ");
					progressDialog = ProgressDialog.show(AddOrder.this, "",
							"���������У����Ժ�...");
					// ���ò���
					// http://www.dianyifen.com/AndroidAPI/GetShopListByLoaction.aspx?pageindex=3
					HashMap<String, String> localHashMap = new HashMap<String, String>();
					
					localHashMap.put("orderid", odid);//�̼ҷ���
					
					localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
							"Android/GetAgainOrder.aspx?", localHashMap, 2, 9);

					localHttpManager.postRequest();
					
				
		}
	public void CancelOrder(){

		Log.v(TAG, "���������ȡ���ݿ�ʼ");
		
		Log.v(TAG, "����׼�����");
		progressDialog = ProgressDialog.show(AddOrder.this, "",
				getResources().getString(R.string.public_up_data)/*"���������У����Ժ�..."*/);
		
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("orderid", mOrderModel.getOrderid());
		if(mOrderModel.getShopState() == 0){
			localHashMap.put("status", "5");
		}else{
			localHashMap.put("status", "8");
		}
		
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
				"APP/Android/UpdateOrderStatus.aspx?", localHashMap, 2, 4);

		localHttpManager.postRequest();

		
	}
	public void FinishOrder(){

		Log.v(TAG, "���������ȡ���ݿ�ʼ");
		
		Log.v(TAG, "����׼�����");
		progressDialog = ProgressDialog.show(AddOrder.this, "",
				getResources().getString(R.string.public_up_data)/*"���������У����Ժ�..."*/);
		
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("orderid", mOrderModel.getOrderid());
		
		localHashMap.put("status", "3");
		
		
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
				"APP/Android/UpdateOrderStatus.aspx?", localHashMap, 2, 2);

		localHttpManager.postRequest();

		
	}
	public void QuickOrder(){

		Log.v(TAG, "���������ȡ���ݿ�ʼ");
		
		Log.v(TAG, "����׼�����");
		progressDialog = ProgressDialog.show(AddOrder.this, "",
				getResources().getString(R.string.public_up_data)/*"���������У����Ժ�..."*/);
		
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("orderid", mOrderModel.getOrderid());
		
		
		
		
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
				"APP/Android/GetHurry.aspx?", localHashMap, 2, 5);

		localHttpManager.getRequeest();

		
	}
	private void ReOrderOK() {
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(AddOrder.this, "",
				getResources().getString(R.string.public_up_data));
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("orderid", orderidString);
		
		localHashMap.put("status", "3");
		localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
				"Android/UpdateOrderStatus.aspx?", localHashMap, 2, 2);
		localHttpManager.getRequeest();
	}  
	@Override
	protected void onPause() {
		super.onPause();
		/*if(mapView != null)
			mapView.onPause();*/
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*if(mapView != null)
			mapView.onDestroy();*/
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		/*if(mapView != null)
			mapView.onResume();*/
		
		if (app.payState == 1) {
			mOrderModel.setPayStatus(1);
			UpdateOrderInfo();
			app.payState = 0;
		}
		if (app.commentSucess) {
			app.commentSucess = false;
			//mOrderModel.list.get(commentFoodIndex).setIsComment(1);
			if (orderMode == 1) {
			setAdapter(listFoodAdapter);
			}
		}
		
		GetData();
	}
	private void setOrderState() {
		if(mOrderModel.getState() == 3){
			//"���������";
			rlBtnOpt.setVisibility(View.GONE);
		}else if(mOrderModel.getState() == 4){
			rlBtnOpt.setVisibility(View.GONE);
			//"����ʧ��";;
		}else if(mOrderModel.getState() == 5){
			rlBtnOpt.setVisibility(View.GONE);//"����ʧЧ");
		}else if(mOrderModel.getState() == 6){
			rlBtnOpt.setVisibility(View.GONE);//"����ʧ��");
		}else if(mOrderModel.getState() == 8){
			//�ȴ�ƽ̨ȷ��ȡ��
			if(mOrderModel.getShopCancel() == 1){//�̼�ͬ���˿�
				rlBtnOpt.setVisibility(View.GONE);
				//value += "δ֪";
			}else{
				rlBtnOpt.setVisibility(View.VISIBLE);
			}
			
			btnCancel.setText(getResources().getString(R.string.order_detail_cancel_btn1)/*"����ȡ������"*/);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoCommand.setText(getResources().getString(R.string.ordercontent_sureaccept)/*"ȷ���ʹ�"*/);
		}else if(mOrderModel.getSendstate() == 0 && mOrderModel.getShopState() == 0){
			rlBtnOpt.setVisibility(View.VISIBLE);
			btnCancel.setText(getResources().getString(R.string.order_detail_cancel_btn)/*"ȡ������"*/);
			btnGoCommand.setVisibility(View.GONE);
			btnGoQuick.setVisibility(View.GONE);
			//value += "�������ύ"; �����̼�δ�ӵ�
		}else if(mOrderModel.getSendstate() == 1){
			rlBtnOpt.setVisibility(View.VISIBLE);
			btnCancel.setText(getResources().getString(R.string.order_detail_cancel_btn1)/*"����ȡ������"*/);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoQuick.setVisibility(View.VISIBLE);
			btnGoCommand.setText(getResources().getString(R.string.ordercontent_sureaccept)/*"ȷ���ʹ�"*/);
			//value += "��ʿ�������̼�";
		}else if(mOrderModel.getSendstate() == 2){
			rlBtnOpt.setVisibility(View.VISIBLE);
			btnCancel.setText(getResources().getString(R.string.order_detail_cancel_btn1)/*"����ȡ������"*/);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoQuick.setVisibility(View.VISIBLE);
			btnGoCommand.setText(getResources().getString(R.string.ordercontent_sureaccept)/*"ȷ���ʹ�"*/);
			//value += "��ʿ��ȡ��";
		}else if(mOrderModel.getSendstate() == 3){
			rlBtnOpt.setVisibility(View.VISIBLE);
			btnCancel.setText(getResources().getString(R.string.order_detail_cancel_btn1)/*"����ȡ������"*/);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoQuick.setVisibility(View.VISIBLE);
			btnGoCommand.setText(getResources().getString(R.string.ordercontent_sureaccept)/*"ȷ���ʹ�"*/);
			//value += "���ʹ�";
		}else if(mOrderModel.getSendstate() == 4){
			rlBtnOpt.setVisibility(View.VISIBLE);
			btnCancel.setText(getResources().getString(R.string.order_detail_cancel_btn1)/*"����ȡ������"*/);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoQuick.setVisibility(View.VISIBLE);
			btnGoCommand.setText(getResources().getString(R.string.ordercontent_sureaccept)/*"ȷ���ʹ�"*/);
			//value += "��ʿ��ȡ��";
		}else if(mOrderModel.getShopState() == 1){
			rlBtnOpt.setVisibility(View.VISIBLE);
			btnCancel.setText(getResources().getString(R.string.order_detail_cancel_btn1)/*"����ȡ������"*/);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoCommand.setVisibility(View.VISIBLE);
			btnGoQuick.setVisibility(View.VISIBLE);
			btnGoCommand.setText(getResources().getString(R.string.ordercontent_sureaccept)/*"ȷ���ʹ�"*/);
			//value += "�̼��ѽӵ�";
		}else if(mOrderModel.getShopState() == 2){
			rlBtnOpt.setVisibility(View.GONE);
			//value += "�������̼Ҿܾ�";
		}else{
			rlBtnOpt.setVisibility(View.GONE);
			//value += "δ֪";
		}
	}
	private void UpdateOrderInfo() {
		String m = getResources().getString(R.string.public_moeny_0);
		if(orderMode == 1){
			shopnameTextView.setText(mOrderModel.getShopName());
			totalpriceTextView.setText(String.format(m + "%.2f", mOrderModel.getTotalMoney()/*foodMoney*/));
			tvPacketFee.setText(String.format(m + "%.2f", mOrderModel.getPackagefree()));
			sentmoneyTextView.setText(String.format(m + "%.2f", mOrderModel.getSentmoney()));
			setOrderState();
			tvTitle.setText(mOrderModel.getShopname());
		}else{
			sentmoneyTextView.setText(mOrderModel.getGoods());
			tvPacketFee.setText(mOrderModel.getShopName() + " " + mOrderModel.getTogoPhone() + " \r\n" + mOrderModel.getTogoAddress());
			
		}
		
		

		addressTextView.setText(mOrderModel.getUserName() + " " + mOrderModel.getPhone() + " \r\n" + mOrderModel.getAddress());
		//phoneTextView.setText(mOrderModel.getPhone());
		//usernameTextView.setText(mOrderModel.getUserName());
		orderaddtimeTextView.setText(mOrderModel.getAddtime());
		tvSendTime.setText(mOrderModel.getEattime());
		orderidTextView.setText(mOrderModel.getOrderid());
		ordernoTextView.setText(mOrderModel.getNote());
		
		tvPromotion.setText(String.format(m + "%.2f", mOrderModel.getPromotionmoney()));
		if (mOrderModel.getPromotionmoney() > 0.0f) {
			llICON.setVisibility(View.VISIBLE);
			TextView text;
			TagModel model;
			for (int i = 0; i < mOrderModel.Promotions.size(); i++) {
				model = mOrderModel.Promotions.get(i);
				text = new TextView(AddOrder.this);
				text.setText(model.getInfo());
				llICON.addView(text);
			}
		}
		tvCardPay.setText(String.format(m + "%.2f", mOrderModel.getCardPay()));
		
		
		
		tvCouponPay.setText(String.format(m + "%.2f", mOrderModel.getCouponPay()));
		tvUnpay.setText(String.format(m + "%.2f", mOrderModel.getUnpay()));
		tvShopTel.setText(mOrderModel.getTogoPhone());

		// �����Ʒ�ܼ�
		float foodMoney = mOrderModel.getTotalMoney() - mOrderModel.getSentmoney() - mOrderModel.getPackagefree();
		
		allpriceTextView.setText(String.format(/*formatPrice*/m + "%.2f", mOrderModel.getTotalMoney()  - mOrderModel.getPromotionmoney() 
				- mOrderModel.getCardPay()/* - mOrderModel.getPayMoney(), foodMoney, mOrderModel.getPackagefree(), 
			mOrderModel.getSentmoney(), mOrderModel.getPayMoney(), mOrderModel.getPromotionmoney(), mOrderModel.getCardPay()*/));

		if (mOrderModel.getPayMode() == 4) {
			if (mOrderModel.getSortType() == 0) {
				tvPayMode.setText(getResources().getString(R.string.pay_mode_0));
			}else{
				tvPayMode.setText("���긶��");
			}
			
			llPayStatus.setVisibility(View.GONE);
			llPayMoney.setVisibility(View.GONE);
		}else{
			if (mOrderModel.getPayMode() == 1) {
				tvPayMode.setText(getResources().getString(R.string.pay_mode_1));
			}else if(mOrderModel.getPayMode() == 3){
				tvPayMode.setText(getResources().getString(R.string.pay_mode_2));
			}else{
				tvPayMode.setText(getResources().getString(R.string.pay_mode_3));
			}
			if (mOrderModel.getPayStatus() == 1) {
				tvPayStatus.setText(getResources().getString(R.string.pay_state_0));
				tvPayMoney.setText(m + mOrderModel.getPayMoney());
				tvPayTime.setText(mOrderModel.getPayTime());
				btnGoPay.setVisibility(View.GONE);
			}else{
				tvPayStatus.setText(getResources().getString(R.string.pay_state_1));
				llPayMoney.setVisibility(View.GONE);
				btnGoPay.setVisibility(View.VISIBLE);
			}
		}
		
		

		//stateshowImageView.setText(mOrderModel.getOrderstr());
		//stateshowImageView.setText(mOrderModel.getState());
		
		String value = "";
		switch (mOrderModel.getSendstate()) {
		case 0:
			value = getResources().getString(R.string.order_send_state_0);
			
			break;
		case 1:
			value = getResources().getString(R.string.order_send_state_1);
			break;
		case 2:
			value = getResources().getString(R.string.order_send_state_2);
			break;
		case 3:
			value = getResources().getString(R.string.order_send_state_3);
			btnCommand.setText(getResources().getString(R.string.ordercontent_sureaccept));
			btnCommand.setVisibility(View.VISIBLE);
			break;
		case 4:
			value = getResources().getString(R.string.order_send_state_4);
			break;
		}
		switch (mOrderModel.getState()) {
		case 0:
			value += "-" + getResources().getString(R.string.order_state_0);

			break;
		case 1:
			value += "-" + getResources().getString(R.string.order_state_1);

			break;
		case 2:
			value += "-" + getResources().getString(R.string.order_state_2);
			break;
		case 3:
			btnCommand.setText(getResources().getString(R.string.com_add));
			value += "-" + getResources().getString(R.string.order_state_3);
			btnCommand.setVisibility(View.VISIBLE);
			break;
		case 4:
			value += "-" + getResources().getString(R.string.order_state_4);
			break;
		case 5:
			value += "-" + getResources().getString(R.string.order_state_5);
			break;
		case 6:
			value += "-" + getResources().getString(R.string.order_state_6);
			break;
		case 7:
			value += "-" + getResources().getString(R.string.order_state_7);
			break;
		default:
			value += "-" + getResources().getString(R.string.order_state_8);
			break;
		}
		stateshowImageView.setText(value);
		listFoodAdapter = new ListFoodAdapter();

		setAdapter(listFoodAdapter);

		Log.v(TAG, "setAdapter()");
	}
	
	private String getAlipayOrderInfo() {
		//payMoney = 0.1f;
				// ǩԼ���������ID
				String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";

				// ǩԼ����֧�����˺�
				orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";

				// �̻���վΨһ������
				orderInfo += "&out_trade_no=" + "\"" + payID + "\"";
				//getString(R.string.app_name) + getResources().getString(R.string.shop_cart_pay_title)/*"Android֧����֧����������ţ�"*/ + orderidString
				// ��Ʒ����
				orderInfo += "&subject=" + "\"" + getString(R.string.app_name) + "Android" + getString(R.string.pay_mode_1) + ", " + getString(R.string.order_detail_id) + orderidString + ", " + getString(R.string.pay_payid)  + payID + "\"";

				// ��Ʒ����
				orderInfo += "&body=" + "\"" + getString(R.string.app_name) + "Android" + getString(R.string.pay_mode_1) + ", " + getString(R.string.order_detail_id) + orderidString + ", " + getString(R.string.pay_payid)  + payID +"\"";

				// ��Ʒ���
				orderInfo += "&total_fee=" + "\"" + String.format("%.2f", payMoney) + "\"";

				// �������첽֪ͨҳ��·��
				orderInfo += "&notify_url=" + "\"" + HJAppConfig.DOMAIN + "Alipay/iosnotify.aspx"
						+ "\"";

				// ����ӿ����ƣ� �̶�ֵ
				orderInfo += "&service=\"mobile.securitypay.pay\"";

				// ֧�����ͣ� �̶�ֵ
				orderInfo += "&payment_type=\"1\"";

				// �������룬 �̶�ֵ
				orderInfo += "&_input_charset=\"utf-8\"";

				// ����δ����׵ĳ�ʱʱ��
				// Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
				// ȡֵ��Χ��1m��15d��
				// m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
				// �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
				orderInfo += "&it_b_pay=\"30m\"";

				// extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
				// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

				// ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
				//orderInfo += "&return_url=\"m.alipay.com\"";

				// �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
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
					// ����PayTask ����
					PayTask alipay = new PayTask(AddOrder.this);
					// ����֧���ӿڣ���ȡ֧�����
					String result = alipay.pay(alipayInfo);

					Message msg = new Message();
					msg.what = UIHandler.RQF_PAY;
					msg.obj = result;
					hander.sendMessage(msg);
				}
			}.start();
		} catch (Exception ex) {
			
			ex.printStackTrace();
			Toast.makeText(AddOrder.this, getString(R.string.pay_error),
					Toast.LENGTH_SHORT).show();
		}
	}
	public void getPayID(float moeny)
	{
		dialogStr = getString(R.string.pay_load_id)/*"��ȡ֧��������......"*/;
		showDialog(322);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("orderid", orderidString);
		localHashMap.put("price", String.valueOf(moeny));
		localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
				"Android/buildpaynum.aspx?", localHashMap, 1, 3);
		localHttpManager.postRequest();
	}
	
	private void gotoOnLinePay() {
		// TODO Auto-generated method stub
		if(payID == null || payID.length() == 0 ){
			getPayID(payMoney);
			return;
		}
		if (mOrderModel.getPayMode() == 1) {
			gotoAliPay();
			
		}else if(mOrderModel.getPayMode() == 5){
			gotoWXPay();
		}
	}
	//΢��֧�����
		public void gotoWXPay(){
			dialogStr = getString(R.string.pay_load_wx);//"΢��֧�����ݼ�����......";
			showDialog(322);
			resultunifiedorder = null;
			GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
			getPrepayId.execute();
			
		}
		/**
		 ����ǩ��
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
								//ʵ����student����
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
				packageParams.add(new BasicNameValuePair("detail", getString(R.string.app_name) + "Android"  + getString(R.string.pay_mode_3) + ", " + getString(R.string.order_detail_id) + orderidString + ", " + getString(R.string.pay_payid)  + payID));
				packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
				packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
				packageParams.add(new BasicNameValuePair("notify_url", HJAppConfig.WEIXINPAYREQ));//����΢��֧���첽֪ͨ�ص���ַ
				packageParams.add(new BasicNameValuePair("out_trade_no",payID));
				packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));//APP����ҳ֧���ύ�û���ip��Native֧�������΢��֧��API�Ļ���IP��
				packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(payMoney * 100))));
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

		@Override
		protected Dialog onCreateDialog(int paramInt) {
			Dialog dialog = null;
			Log.v(TAG, "onCreateDialog:" + paramInt);
			switch (paramInt) {
			case 322:
				dialog = OtherManager.createProgressDialog(this, this.dialogStr);
				return dialog;
			
			}
			return null;
		}
		public BigDecimal fToB(float a)
		{
			return new BigDecimal(Float.toString(a));//����ʹ��toString���ͣ��������ȲŻ���ȷ
		}
		public float add(float a, float b){//��
			BigDecimal b1 = fToB(a);
			BigDecimal b2 = fToB(b);
			return b1.add(b2).floatValue();
		}
		
		public float subtract(float a, float b){//��
			BigDecimal b1 = fToB(a);
			BigDecimal b2 = fToB(b);
			return b1.subtract(b2).floatValue();
		}
		
		public float multiply(float a, float b){//ʣ
			BigDecimal b1 = fToB(a);
			BigDecimal b2 = fToB(b);
			return b1.multiply(b2).floatValue();
		}
		
		public float divide(float a, float b){//��
			BigDecimal b1 = fToB(a);
			BigDecimal b2 = fToB(b);
			return b1.divide(b2, 2).floatValue();
		}
	private class UIHandler extends Handler {
		static final int SET_ORDER_LIST = 1; // �������ݳɹ�
		static final int UPDATE_ORDER_SUCCESS = 2;// ���¶����ɹ�
		public static final int NET_ERROR = -1;
		public static final int UPDATE_ORDER_FAILED = -2;
		protected static final int RQF_PAY = 9;
		public static final int GETPAYID_SUCCESS = 8;
		public static final int GETPAYID_FAILD = -8;
		public static final int GET_PREPAY_ID_FAILD = 17;
		public static final int GET_PREPAY_ID_SUCESS = 13;
		public static final int GET_STATE_SUCESS = 10;
		public static final int CANCEL_ORDER_SUCCESS = 11;
		public static final int CANCEL_ORDER_FAILD = 12;
		public static final int QUICK_ORDER_FAILD = 14;
		public static final int QUICK_ORDER_SUCCESS = 15;
		public static final int BUY_AGAIN = 7;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BUY_AGAIN:
				ShopListItemModel shopModel = new ShopListItemModel();
				shopModel.setId(app.buyFoodList.get(0).getTogoID());
				app.setShop(shopModel);
				Intent localIntent = new Intent(AddOrder.this,
						ShopDetail.class);
				startActivity(localIntent);
				break;
			case QUICK_ORDER_SUCCESS:
				Toast.makeText(AddOrder.this, errorMSG, 15).show();
				break;
			case QUICK_ORDER_FAILD:
				Toast.makeText(AddOrder.this, errorMSG, 15).show();
				break;
			case CANCEL_ORDER_FAILD:
				Toast.makeText(AddOrder.this, errorMSG, 15).show();
				break;
			case CANCEL_ORDER_SUCCESS:
				if(mOrderModel.getShopState() == 0){
					Toast.makeText(AddOrder.this, getResources().getString(R.string.order_detail_cancel_sucess_notice)/*"ȡ�������ɹ�����ȴ���̨��ˣ�"*/, 15).show();
					mOrderModel.setState(5);
				}else{
					Uri localUri = Uri.parse(String.format("tel:%s", mOrderModel.getTogoPhone()));
					Intent localIntent1 = new Intent(
							"android.intent.action.CALL", localUri);
					startActivity(localIntent1);
					Toast.makeText(AddOrder.this, getResources().getString(R.string.order_detail_cancel_sucess_notice1)/*"����ȡ�������ɹ�����ȴ���̨��ˣ�"*/, 15).show();
					mOrderModel.setState(8);
				}
				setOrderState();
				break;
			case GETPAYID_SUCCESS:
				
				gotoOnLinePay();
				break;
			case GETPAYID_FAILD:
				OtherManager.Toast(AddOrder.this, getString(R.string.pay_load_id_error)/*"��ȡ֧������ʧ�ܣ�"*/);
				break;
			case  GET_PREPAY_ID_FAILD:
				Toast.makeText(AddOrder.this, errorMSG + getString(R.string.pay_wx_pay_failed)/*"΢��֧��ʧ�ܣ����Ժ����ԣ�"*/, 15).show();
				
				return;
			case GET_PREPAY_ID_SUCESS:
				
				if (resultunifiedorder == null) {
					removeDialog(322);
					Toast.makeText(AddOrder.this, getString(R.string.pay_load_wx_failed)/*"����΢��֧��ʧ�ܣ����Ժ����ԣ�"*/, 15).show();
					
					return;
				}
				genPayReq();
				sendPayReq();
				removeDialog(322);
				
				break;
			case GET_STATE_SUCESS:
				lvState.setAdapter(mStateAdapter );
				break;
			case RQF_PAY:
				Result.sResult = (String) msg.obj;
				if (Result.sResult == null || Result.sResult.length() == 0) {
					Toast.makeText(AddOrder.this, getResources().getString(R.string.pay_sucess),
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(AddOrder.this, Result.getResult(),
							Toast.LENGTH_SHORT).show();
				}
				GetData();
				break;
			case NET_ERROR:
				Toast.makeText(AddOrder.this, getResources().getString(R.string.public_net_or_data_error), 5).show();
				break;
			case SET_ORDER_LIST: {
				// ��ʾ�����б�
				if (orderMode == 1) {
					GetOrderStateList();
				}

				shopidString = mOrderModel.getShopId();

				UpdateOrderInfo();

				// ��ȡ��������Ϣ��ʾ
				orderidTextView.setText(orderidString);
				if(payType == 1 || payType == 5){
					payType = 4;
					//֧������΢��֧��
					payMoney = subtract(subtract(subtract(subtract(mOrderModel.getTotalMoney(), mOrderModel.getCardPay()), mOrderModel.getCouponPay()), mOrderModel.getPromotionmoney()), mOrderModel.getPayMoney());
					if (payMoney > 0.0f) {
						gotoOnLinePay();
					}
				}
				return;
			}
			case UPDATE_ORDER_SUCCESS: {
				// ��ʾ�����б�
				
				setOrderState();

				/*String value = "";
				switch (mOrderModel.getSendstate()) {
				case 0:
					value = getResources().getString(R.string.order_send_state_0);
					
					break;
				case 1:
					value = getResources().getString(R.string.order_send_state_1);
					break;
				case 2:
					value = getResources().getString(R.string.order_send_state_2);
					break;
				case 3:
					value = getResources().getString(R.string.order_send_state_3);
					btnCommand.setText(getResources().getString(R.string.ordercontent_sureaccept));
					btnCommand.setVisibility(View.VISIBLE);
					break;
				case 4:
					value = getResources().getString(R.string.order_send_state_4);
					break;
				}
				switch (mOrderModel.getState()) {
				case 0:
					value += "-" + getResources().getString(R.string.order_state_0);

					break;
				case 1:
					value += "-" + getResources().getString(R.string.order_state_1);

					break;
				case 2:
					value += "-" + getResources().getString(R.string.order_state_2);
					break;
				case 3:
					btnCommand.setText(getResources().getString(R.string.com_add));
					value += "-" + getResources().getString(R.string.order_state_3);
					btnCommand.setVisibility(View.VISIBLE);
					break;
				case 4:
					value += "-" + getResources().getString(R.string.order_state_4);
					break;
				case 5:
					value += "-" + getResources().getString(R.string.order_state_5);
					break;
				case 6:
					value += "-" + getResources().getString(R.string.order_state_6);
					break;
				case 7:
					value += "-" + getResources().getString(R.string.order_state_7);
					break;
				default:
					value += "-" + getResources().getString(R.string.order_state_8);
					break;
				}
				stateshowImageView.setText(value);*/

				return;
			}
			case UPDATE_ORDER_FAILED:
				Toast.makeText(AddOrder.this, getResources().getString(R.string.order_detail_up_falied), 15).show();
				break;
			
			}

			return;
		}
	}

	

	

	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetData() {
		
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("orderid", orderidString);
		localHashMap.put("isios", "0");
		if (orderType == 0) {
			if (orderMode == 1) {//����
				localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
						"Android/GetOrderDetailByOrderId.aspx?", localHashMap, 2, 1);
			}else{//����
				localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
						"Android/GetRunOrderDetailByOrderId.aspx?", localHashMap, 2, 1);
			}
		}else{//Ԥ��
			localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
					"Android/GetBookOrderDetailByOrderId.aspx?", localHashMap, 2, 1);
		}
		
		localHttpManager.getRequeest();
	}
	
	// ͨ�������ȡ���� ��ȡ��ɺ�����Ϣ
	public void GetOrderStateList() {
			
			HashMap<String, String> localHashMap = new HashMap<String, String>();
			localHashMap.put("orderid", orderidString);

			localHttpManager = new HttpManager(AddOrder.this, AddOrder.this,
					"/OrderStep?", localHashMap, 2, 8);
			localHttpManager.getApi();
	}

	

	
	class stateHoder {
		TextView tvState;
		TextView tvDiscry;
		TextView tvTime;
		
		ImageView ivUp;
		ImageView ivDow;
		ImageView ivState;
		public RelativeLayout mapView;
		stateHoder() {
		}
	}
	class stateListAdapter extends BaseAdapter {
		stateHoder listHolder;
		LayoutInflater inflater;

		stateListAdapter() {
		}

		@Override
		public int getCount() {
			int n = orderStateListMode.list.size();
			return n;
		}

		@Override
		public Object getItem(int paramInt) {
			return AddOrder.this.orderStateListMode.list.get(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			LayoutInflater localLayoutInflater = LayoutInflater
					.from(AddOrder.this);

			this.inflater = localLayoutInflater;

			if (paramView == null) {
				paramView = this.inflater.inflate(
						R.layout.order_state_list_item, null);

				Log.v(TAG, "convertView == null");

				listHolder = new stateHoder();

				listHolder.tvState = (TextView) paramView
						.findViewById(R.id.tv_state);
				listHolder.mapView = (RelativeLayout)paramView.findViewById(R.id.bmapView);
				listHolder.mapView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//ȥ������ʿ
						Intent intent = new Intent(AddOrder.this, ShowOnMap.class);
						intent.putExtra("isSearch", false);
						intent.putExtra("getType", 1);//1��ȡ��ʿGPS
						intent.putExtra("lat", orderLatLng.latitude);//1��ȡ��ʿGPS
						intent.putExtra("lng", orderLatLng.longitude);//1��ȡ��ʿGPS
						intent.putExtra("deliverID", mOrderModel.getDeliverID());//1��ȡ��ʿGPS
						startActivity(intent);
					}
				});
				listHolder.tvDiscry = (TextView) paramView
						.findViewById(R.id.tv_title);
				listHolder.tvTime = (TextView) paramView
						.findViewById(R.id.tv_time);
				
				listHolder.ivDow = (ImageView) paramView
						.findViewById(R.id.iv_dow);
				listHolder.ivState = (ImageView) paramView
						.findViewById(R.id.iv_state);
				listHolder.ivUp = (ImageView) paramView
						.findViewById(R.id.iv_up);
				
				

				paramView.setTag(listHolder);
			} else {
				listHolder = (stateHoder) paramView.getTag();
			}

			OrderStateModel foodInOrderModel = orderStateListMode.list
					.get(paramInt);

			if (foodInOrderModel != null) {
				
				
				if (paramInt == orderStateListMode.list.size() - 1 && mOrderModel.getState() > 2 && mOrderModel.getState() != 7 ) {
					listHolder.ivDow.setVisibility(View.GONE);
				}else{
					listHolder.ivDow.setVisibility(View.VISIBLE);
				}
				
				listHolder.tvState.setText(foodInOrderModel
						.getName());
				listHolder.tvTime.setText(foodInOrderModel
						.getAddtime());
				if(mOrderModel.getState() != 3 && mOrderModel.getState() != 4 && mOrderModel.getState() != 5 && 
						mOrderModel.getState() != 6 && foodInOrderModel.getDeliverLat() > 1.0 && foodInOrderModel.getDeliverLng() > 1.0){
					//listHolder.mapView.onResume();
					listHolder.mapView.setVisibility(View.VISIBLE);
					orderLatLng = new LatLng(foodInOrderModel.getDeliverLat(), foodInOrderModel.getDeliverLng());
					String url = String.format("http://api.map.baidu.com/staticimage/v2?ak=%s&width=500&height=200&zoom=17&markers=%f,%f", 
							HJAppConfig.BAIDU_STATIC_KEY,foodInOrderModel.getDeliverLng(), foodInOrderModel.getDeliverLat());
					listHolder.mapView.setTag(url);
					app.mLoadImage.addTask(url
							,
									listHolder.mapView, R.drawable.default_map);
					app.mLoadImage.doTask();
					//imgMapView = listHolder.mapView;
					//if(mapViewBitMap == null)
					{
						//mapView.setVisibility(View.VISIBLE);
						//if(mapView == null)
						{
							
							/*mBaiduMap = listHolder.mapView.getMap(); 
							mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
							MapStatusUpdate ms = MapStatusUpdateFactory.zoomTo(15);// ���õ�ͼ���ű�����17��100��
							mBaiduMap.setMapStatus(ms);
							
							orderLatLng = new LatLng(foodInOrderModel.getDeliverLat(), foodInOrderModel.getDeliverLng());
							
							btnDeliver = new Button(getApplicationContext());
							btnDeliver.setBackgroundResource(R.drawable.bottom_deliver_ico);
							
							//button.setText("��ʿλ��");
							btnDeliver.setTextColor(Color.WHITE);
							btnDeliver.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									//finish();
									//ȥ����ʿ��λ��
									Intent intent = new Intent(AddOrder.this, ShowOnMap.class);
									intent.putExtra("isSearch", false);
									intent.putExtra("getType", 1);//1��ȡ��ʿGPS
									intent.putExtra("lat", orderLatLng.latitude);//1��ȡ��ʿGPS
									intent.putExtra("lng", orderLatLng.longitude);//1��ȡ��ʿGPS
									intent.putExtra("deliverID", mOrderModel.getDeliverID());//1��ȡ��ʿGPS
									startActivity(intent);
								}
							});
							
							// ����������ʾ��InfoWindow�������
							// LatLng pt = new LatLng(result.getLocation(), 116.397428);
							// ����InfoWindow�ĵ���¼�������
							
							// ����InfoWindow
							mInfoWindow = new InfoWindow(btnDeliver,
									orderLatLng, 0);
							// ��ʾInfoWindow
							mBaiduMap.showInfoWindow(mInfoWindow);
							mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(orderLatLng));
							//��ͼ
							Rect rect = new Rect(0, 0, 300, 300);// ��xy ��xy 
							mBaiduMap.snapshotScope(null, mapViewSnap);*/
						}
					}/*else{
						listHolder.mapView.setImageBitmap(mapViewBitMap);
					}*/
					
					
					
					
				}else{
					listHolder.mapView.setVisibility(View.INVISIBLE);
					//listHolder.mapView.onPause();
					listHolder.mapView.setVisibility(View.GONE);
				}
				
				if(paramInt != 0){
					
					if(foodInOrderModel.getDeliverTel().length() == 0){
						listHolder.tvDiscry.setText(foodInOrderModel.getSubtitle());
					}else{
						String html = String.format("<font color='black'>%s</font><font color='#0000ff'><big><i>%s</i></big></font><p>",
								"��ʿ�绰��", 
								foodInOrderModel.getDeliverTel());
						listHolder.tvDiscry.setText(Html.fromHtml(html));
						listHolder.tvDiscry.setTag(foodInOrderModel.getDeliverTel());
						listHolder.tvDiscry.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String tel = (String)v.getTag();
								if(tel.length() > 0){
									Uri localUri = Uri.parse(String.format("tel:%s", tel));
									Intent localIntent1 = new Intent(
											"android.intent.action.CALL", localUri);
									startActivity(localIntent1);
								}
							}
						});
					}
					
				}else{
					listHolder.tvDiscry.setText("�����ĵȴ��̼�ȷ��");
				}
				
			} else {
				Log.v(TAG, "foodInOrderModel is null");
			}

			Log.v(TAG, "return convertView");
			return paramView;
		}
	}

	class Hoder {
		TextView tv_foodCount;
		TextView tv_foodName;
		TextView tv_foodPrice;
		Hoder() {
		}
	}

	class ListFoodAdapter extends BaseAdapter {
		Hoder listHolder;
		LayoutInflater inflater;

		ListFoodAdapter() {
		}

		@Override
		public int getCount() {
			return AddOrder.this.mOrderModel.list.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return AddOrder.this.mOrderModel.list.get(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			LayoutInflater localLayoutInflater = LayoutInflater
					.from(AddOrder.this);

			this.inflater = localLayoutInflater;

			if (paramView == null) {
				paramView = this.inflater.inflate(
						R.layout.addorder_foodlistitem, null);

				Log.v(TAG, "convertView == null");

				listHolder = new Hoder();

				listHolder.tv_foodCount = (TextView) paramView
						.findViewById(R.id.addorder_foodcount);
				listHolder.tv_foodName = (TextView) paramView
						.findViewById(R.id.addorder_foodname);
				listHolder.tv_foodPrice = (TextView) paramView
						.findViewById(R.id.addorder_foodprice);

				paramView.setTag(paramView);
			} else {
				listHolder = (AddOrder.Hoder) paramView.getTag();
			}

			FoodInOrderModelForGet foodInOrderModel = AddOrder.this.mOrderModel.list
					.get(paramInt);

			if (foodInOrderModel != null) {

				
				listHolder.tv_foodCount.setText("X" + foodInOrderModel
						.getCount());
				listHolder.tv_foodPrice.setText(getResources().getString(R.string.public_moeny_0) + String.valueOf(foodInOrderModel
						.getPrice()));
				listHolder.tv_foodName.setText(foodInOrderModel.getName());
			} else {
				Log.v(TAG, "foodInOrderModel is null");
			}

			Log.v(TAG, "return convertView");
			return paramView;
		}
	}

	private void setAdapter(ListFoodAdapter listFoodAdapter) {
		Log.v(TAG, "setAdapter");
		ll_food_list.removeAllViews();
		int i = this.listFoodAdapter.getCount();
		int j = 0;

		Log.v(TAG, "listFoodAdapter.getcount:" + i);

		while (true) {
			if (j >= i) {
				Log.v(TAG, "j >= i setAdapter end");
				return;
			}
			View localView = this.listFoodAdapter.getView(j, null, null);
			ll_food_list.addView(localView);
			j += 1;
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		
		if (paramInt == 260) {
			
			String isonsString = (String)paramObject;
			
			try {
				JSONObject json = new JSONObject(isonsString);
				int state;
			switch (tag) {
			case 1:
				isonsString = isonsString.replace("\\\"", "\"");
				isonsString = isonsString.replace("\"{", "{");
				isonsString = isonsString.replace("}\"", "}");
				mOrderModel = new OrderModelForGet();
				if(orderMode == 2){
					mOrderModel.JSONToBean(isonsString, 3);
				}else{
					mOrderModel.JSONToBean(isonsString, orderType);
				}
				
				message.what = UIHandler.SET_ORDER_LIST;
				break;
			case 2:
				
					state = json.getInt("state");
					if (state == 1) {
						//message.what = UIHandler.
						mOrderModel.setState(3);
						message.what = UIHandler.UPDATE_ORDER_SUCCESS;
					}else{
						message.what = UIHandler.UPDATE_ORDER_FAILED;
					}
				
				
				break;
			case 3:
				
					int state1 = json.getInt("state");
					if (state1 == 1) {
						payID = json.getString("batch");
						message.what = UIHandler.GETPAYID_SUCCESS;
					} else {
						message.what = UIHandler.GETPAYID_FAILD;
					}
				
				break;
			case 4:
			{
					state = json.getInt("state");
					if (state == 1) {
						message.what = UIHandler.CANCEL_ORDER_SUCCESS;
					}else{
						errorMSG = json.getString("msg");
						message.what = UIHandler.CANCEL_ORDER_FAILD;
					}
				
			}
				break;
			case 5:
			{
					state = json.getInt("state");
					errorMSG = json.getString("msg");
					if (state == 1) {
						message.what = UIHandler.QUICK_ORDER_SUCCESS;
					}else{
						message.what = UIHandler.QUICK_ORDER_FAILD;
					}
				
			}
				break;
			case 7:
			{
					
					state = json.getInt("state");
					if (state == 1) {
						payID = json.getString("batch");
						message.what = UIHandler.GETPAYID_SUCCESS;
					} else {
						message.what = UIHandler.GETPAYID_FAILD;
					}
			}
				break;
			case 8:
				if (orderStateListMode == null) {
					orderStateListMode = new OrderStateListModel();
				}
					
				orderStateListMode.stringToBean(isonsString);
				message.what = UIHandler.GET_STATE_SUCESS;
				break;
			case 9:
			{
				JSONArray array = new JSONArray();
				array = json.getJSONArray("foodlist");
				if(app.buyFoodList == null){
					app.buyFoodList = new ArrayList<FoodModel>();
				}else{
					app.buyFoodList.clear();
				}
				FoodModel model;
				int togoID = json.getInt("TogoId");
				for (int i = 0; i < array.length(); i++) {
					json = array.getJSONObject(i);
					model = new FoodModel();
					model.BuyAgainJson(json, togoID);
					app.buyFoodList.add(model);
				}
				message.what = UIHandler.BUY_AGAIN;
			}	
				break;
			default:
				message.what = UIHandler.NET_ERROR;
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
		
		if (tag == 1) {
			if(orderMode != 1){
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
			}
		}else if(tag == 8){
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}else if(tag == 3){
			removeDialog(322);
		}else{
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
		hander.sendMessage(message); // ������Ϣ��Handler
	}
	
	public class HJPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public HJPagerAdapter(List<View> listViews) {
			// TODO Auto-generated constructor stub
			this.mListViews = listViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}


}
