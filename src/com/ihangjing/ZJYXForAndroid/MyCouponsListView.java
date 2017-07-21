package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.MyCouponModel;
import com.ihangjing.Model.MyCouponsListModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class MyCouponsListView extends HjActivity implements
		HttpRequestListener {

	protected Animation Animation_toLeft = null;
	protected Animation Animation_toRight = null;
	private ListView ListView;
	private Button btnLookFriendsBuy;
	private ListView listViewDel;
	private MyCouponsListModel couposList;
	
	int pageindex;
	private HttpManager localHttpManager;
	public LayoutInflater inflater;
	CouponsListViewAdapter listViewAdapter;
	View moreView;
	private String dialogStr;
	private UIHandler mHandler;
	private boolean isShowInput;
	static int NET_DIALOG = 322;
	int indexOpt = 0;
	private String errMsg;
	protected long time1;
	protected float p1x;
	protected float p1y;
	//protected boolean moveLeftEnd = true;
	//protected boolean moveRightEnd = false;
	protected boolean isStopRight = false;
	protected boolean isStopLeft = false;
	protected boolean isInLeft = false;
	protected boolean isInRight = true;
	private geiverCouponListViewAdapter delListViewAdapter;
	protected int scrollPos;
	protected int scrollTop;
	protected long time2;
	protected float p2x;
	protected float p2y;
	public OnClickListener geiverCouponListener;
	private View moreView1;
	protected Integer OptIndex;
	private float cartMoney = 0.0f;//当从确定订单界面过来时该值不为0.0f

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_coupons);
		app.geiverCoupon = false;
		mHandler = new UIHandler();
		Bundle extern = getIntent().getExtras();
		if (extern != null) {
			cartMoney = extern.getFloat("money");//
		}
		ListView = (ListView) findViewById(R.id.lv_data);
		listViewDel = (ListView) findViewById(R.id.lv_del);
		
		btnLookFriendsBuy = (Button) findViewById(R.id.btn_view_friends_buy);
		
		ListView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (cartMoney > 0.0f) {
					return false;
				}
				intputHiden();
				if (event.getDownTime() - time1 > 1000) {
					time1 = event.getDownTime();
					p1x = event.getX();
					p1y = event.getY();
				} else {
					float x = p1x - event.getX();
					float y = p1y - event.getY();
					if (y < 10.0f && y > -10.0f) {
						if (Animation_toRight == null) {
							Animation_toRight = new TranslateAnimation(0, listViewDel.getWidth(),
									0, 0);
							Animation_toRight.setDuration(500);

							Animation_toRight.setAnimationListener(new AnimationListener() {

								// AttributeSet attri;// = new
								// AttributeSet();

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub
									listViewDel.setSelectionFromTop(scrollPos, scrollTop);
								}

								@Override
								public void onAnimationRepeat(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									ListView.clearAnimation();// 不调用该方案可能会导致闪动问题
									if (!isStopRight) {
										isStopRight = true;
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
										params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
										//listViewDel.setSelectionFromTop(scrollPos, scrollTop);
										ListView.setLayoutParams(params);
										isInLeft = false;
										isInRight = true;
									}

								}
							});

							Animation_toLeft = new TranslateAnimation(0, -listViewDel.getWidth(),
									0, 0);
							Animation_toLeft.setDuration(1000);
							// Animation_toLeft.setRepeatMode(Animation.REVERSE);
							Animation_toLeft.setAnimationListener(new AnimationListener() {

								// AttributeSet attri;// = new
								// AttributeSet();

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub
									listViewDel.setSelectionFromTop(scrollPos, scrollTop);
								}

								@Override
								public void onAnimationRepeat(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									ListView.clearAnimation();// 不调用该方案可能会导致闪动问题
									if (!isStopLeft) {
										isStopLeft = true;
										
										
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
												LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
										params.addRule(RelativeLayout.LEFT_OF, R.id.lv_del);
										
										
										ListView.setLayoutParams(params);
										isInLeft = true;
										isInRight = false;
									}

								}
							});
						}
						if (x > 20.0f && isInRight) {// 左移
							isStopLeft = false;
							isInRight = false;
							delListViewAdapter.notifyDataSetChanged();
			                if (couposList.list != null) {  
			                	scrollPos = ListView.getFirstVisiblePosition();
			                       View v1=ListView .getChildAt(0);
			                       scrollTop=(v==null)?0:v1.getTop();
			                       
			                 }  
							
							ListView.startAnimation(Animation_toLeft);
							//Animation_toLeft.start();
							return true;
						} else if (x < -20.0f && isInLeft) {
							isStopRight = false;
							isInLeft = false;
							delListViewAdapter.notifyDataSetChanged();
							ListView.startAnimation(Animation_toRight);
							//Animation_toRight.start();
							return true;

						}
					} else {// 上下左右移动
						hiden();

					}

				}
				return false;
			}
		});
		
		ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (cartMoney > 0.0f) {
					MyCouponModel model = couposList.list.get(arg2);
					if (model.getIsSelect() == 0) {
						//加入
						if (model.getIsUser() == 1) {
							Toast.makeText(MyCouponsListView.this, "该优惠券已经使用过，无法再次使用", 15).show();
							return;
						}
						if (cartMoney < model.getMoneyLine()) {
							Toast.makeText(MyCouponsListView.this, String.format("您的订单金额%.2f￥未打到券的使用最低消费%.2f￥，无法使用", cartMoney, model.getMoneyLine()), 15).show();
							return;
						}
						if (app.couponsKeyList.list.size() >= 4) {
							Toast.makeText(MyCouponsListView.this, "一次消费最多只能使用四张优惠券!", 15).show();
							return;
						}
						model.setIsSelect(1);
						model.setIndexSelect(app.couponsKeyList.list.size());
						app.couponsKeyList.list.add(model);
						arg1.setBackgroundColor(Color.rgb(246, 251, 197));
					}else{
						//删除
						
						model.setIsSelect(0);
						app.couponsKeyList.list.remove(model.getIndexSelect());
						arg1.setBackgroundColor(Color.rgb(255, 255, 255));
					}
					listViewAdapter.notifyDataSetChanged();
				}
				
			}
		});

		geiverCouponListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//赠予好友
				OptIndex = (Integer) v.getTag();
				hiden();
				MyCouponModel model = couposList.list.get(OptIndex);
				Intent intent = new Intent(MyCouponsListView.this, MyFriendsListView.class);
				intent.putExtra("cid", model.getDataID());
				MyCouponsListView.this.startActivity(intent);
			}
		};
		
		listViewDel.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				intputHiden();
				
				if (event.getDownTime() - time2 > 1000) {
					time2 = event.getDownTime();
					p2x = event.getX();
					p2y = event.getY();
				} else {
					hiden();
				}
				return false;
			}
		});
		
		listViewDel.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				//hiden();
				
			}
		});
		
		if (cartMoney > 0.0f) {
			couposList = app.couponsList;
			
			btnLookFriendsBuy.setText(String.format("您当前的订单总金额为：%.2f￥", cartMoney));
			btnLookFriendsBuy.setVisibility(View.VISIBLE);
		}else{
			couposList = new MyCouponsListModel();
		}

		listViewAdapter = new CouponsListViewAdapter();
		ListView.setAdapter(listViewAdapter);

		delListViewAdapter = new geiverCouponListViewAdapter();
		listViewDel.setAdapter(delListViewAdapter);

		


		inflater = LayoutInflater.from(MyCouponsListView.this);
		
		moreView1 = inflater.inflate(R.layout.more_friends_from_net, null);
		TextView tvMore = (TextView) moreView1
				.findViewById(R.id.tv_more_friends);
		tvMore.setText("");
		
		moreView = inflater.inflate(R.layout.more_friends_from_net, null);
		tvMore = (TextView) moreView
				.findViewById(R.id.tv_more_friends);
		tvMore.setText("加载更多数据。。。");
		tvMore.setTextColor(Color.rgb(0, 0, 0));
		tvMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {//加载更多数据
				// TODO Auto-generated method stub
				pageindex++;
				getCouponsFromServer();
			}
		});

		Button btnBack = (Button) findViewById(R.id.title_bar_back_btn);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView tvRight = (TextView) findViewById(R.id.title_bar_right_btn);
		tvRight.setText("绑定优惠券");
		/*if (cartMoney > 0.0f) {
			tvRight.setText("确定选择");
		}else{
			
		}*/
		
		tvRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if (cartMoney > 0.0f) {
					if (app.couponsKeyList.list.size() == 0) {
						Toast.makeText(MyCouponsListView.this, "您没有选择任何优惠券", 15).show();
						return ;
					}
					finish();
				}else{*/
					CouponInputDialogActivity input = new CouponInputDialogActivity(
							MyCouponsListView.this, "", "", "", 255, 1, true,
							new CouponInputDialogActivity.OnClickOKListener() {

								@Override
								public void getInput(String value1, String value2) {
									
									bindCouponsFromServer(value2);
								}

							});
					input.show();
				}
			//}
		});
		if (couposList.list.size() == 0) {
			pageindex = 1;
			getCouponsFromServer();
		}
	}

	protected void hiden() {
		// TODO Auto-generated method stub
		if (isInLeft) {
			isStopRight = false;
			isInLeft = false;
			ListView.startAnimation(Animation_toRight);

		}
	}

	

	private void intputHiden() {
		if (isShowInput) {
			isShowInput = false;
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			// if(imm.isActive()){
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			// }
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (app.geiverCoupon) {
			app.geiverCoupon = false;
			couposList.list.remove(indexOpt);
			listViewAdapter.notifyDataSetChanged();
		}
		
	}

	public void bindCouponsFromServer(String key) {
		dialogStr = "绑定中......";
		showDialog(NET_DIALOG);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("cardnum", key);
		localHashMap.put("uname", app.userInfo.userName);
		localHashMap.put("uid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyCouponsListView.this,
				MyCouponsListView.this, "/Android/bindcard.aspx?",
				localHashMap, 2, 2);
		localHttpManager.getRequeest();
	}
	
	public void getCouponsFromServer() {
		dialogStr = "加载中......";
		showDialog(NET_DIALOG);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(pageindex));
		localHashMap.put("pagesize", "30");
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyCouponsListView.this,
				MyCouponsListView.this, "/Android/GetMyCardList.aspx?",
				localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}

	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
		}
		return super.onKeyDown(keyCode, paramKeyEvent);
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			String value = (String) paramObject;

			try {
				JSONObject json = new JSONObject(value);
				switch (tag) {
				case 1:
					// MyFriendsList list =new MyFriendsList();
					
					couposList.JsonToBean(json);
					msg.what = UIHandler.READ_DATA_OK;
					break;
				case 2:
					int state = json.getInt("code");
					if (state == 200) {
						
						MyCouponModel model = new MyCouponModel();
						model.setIsUser(0);
						model.setCKey(json.getString("ckey"));
						model.setDataID(json.getInt("CID"));
						model.setCValue(json.getString("point"));
						model.setMoneyLine((float)json.getDouble("moneyline"));
						if (model.getMoneyLine() == 0.0f) {
							model.setCMoneyLine(1);
						}
						model.setCTimeLimity(1);
						model.setCActivity(1);
						model.setCType(json.getInt("ReveInt"));
						couposList.list.add(model);
						msg.what = UIHandler.BIND_COUPON_SCUESS;
					}else{
						errMsg = json.getString("msg");
						msg.what = UIHandler.BIND_COUPON_FAILD;
					}
					break;
				case 3:
					
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what = UIHandler.NET_ERROR;
			}
		} else {
			msg.what = UIHandler.NET_ERROR;
		}
		mHandler.sendMessage(msg);
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;
		
		if (paramInt == NET_DIALOG) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);

			DialogInterface.OnKeyListener okl = new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface paramDialogInterface,
						int paramInt, KeyEvent paramKeyEvent) {
					// 取消加载
					if ((paramInt == 4) && (localHttpManager != null)) {
						localHttpManager.cancelHttpRequest();
					}
					return false;
				}
			};

			dialog.setOnKeyListener(okl);

			return dialog;
		}
		return dialog;
	}

	private class UIHandler extends Handler {
		public static final int BIND_COUPON_FAILD = -1;
		public static final int BIND_COUPON_SCUESS = 1;
		public static final int ADD_FAILED = -3;
		public static final int ADD_SCUESS = 4;
		public static final int NET_ERROR = -2;
		public static final int SEARCH_SCUESS = 3;
		public static final int READ_DATA_OK = 2;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(NET_DIALOG);
			switch (msg.what) {
			case ADD_FAILED:
				Toast.makeText(MyCouponsListView.this, errMsg, 15).show();
				break;
			case ADD_SCUESS:
				Toast.makeText(MyCouponsListView.this, "添加好友成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				break;
			case BIND_COUPON_SCUESS:
				Toast.makeText(MyCouponsListView.this, "绑定优惠券成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				break;
			case BIND_COUPON_FAILD:
				Toast.makeText(MyCouponsListView.this, errMsg, 15).show();
				break;
			case NET_ERROR:
				Toast.makeText(MyCouponsListView.this, "网络或数据错误，请稍后再试！", 10)
						.show();
				break;
			case READ_DATA_OK:
				
				listViewAdapter.notifyDataSetChanged();
				//delListViewAdapter.notifyDataSetChanged();
				break;
			case SEARCH_SCUESS:
				listViewAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}

	class CouponView {
		
		TextView tvKey;
		TextView tvState;
		TextView tvType;
		TextView tvHasValue;
		TextView tvValueName;
		TextView tvValue;
		TextView tvGever;
		TextView tvTime;
		TextView tvMin;
		LinearLayout llHas;
		RelativeLayout rlTime;
		RelativeLayout rlMin;
	}

	class CouponsListViewAdapter extends BaseAdapter {
		CouponsListViewAdapter() {
		}

		@Override
		public int getCount() {
			int len = couposList.list.size();
			if (len == 0) {
				return len;
			}
			if (couposList.page < couposList.total) {
				len++;
			}
			return len;
		}

		@Override
		public Object getItem(int paramInt) {
			
			if (paramInt < couposList.list.size()) {
				return couposList.list.get(paramInt);
			}
			
			return null;
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			if (position < 4) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public View getView(int paramInt, View convertView,
				ViewGroup paramViewGroup) {

			CouponView listHolder;
			if (paramInt == couposList.list.size()) {
				return moreView;
			}
			if (convertView == null || convertView == moreView) {
				convertView = inflater.inflate(R.layout.coupon_item, null);
				listHolder = new CouponView();
				listHolder.tvKey = (TextView)convertView.findViewById(R.id.tv_key);
				listHolder.tvState = (TextView)convertView.findViewById(R.id.tv_state);
				listHolder.tvType = (TextView)convertView.findViewById(R.id.tv_type);
				listHolder.tvHasValue = (TextView)convertView.findViewById(R.id.tv_has_value);
				listHolder.tvValueName = (TextView)convertView.findViewById(R.id.tv_nvalue);
				listHolder.tvValue = (TextView)convertView.findViewById(R.id.tv_value);
				listHolder.tvGever = (TextView)convertView.findViewById(R.id.tv_gever);
				listHolder.tvTime = (TextView)convertView.findViewById(R.id.tv_time);
				listHolder.tvMin = (TextView)convertView.findViewById(R.id.tv_min);
				listHolder.llHas = (LinearLayout)convertView.findViewById(R.id.ll_has);
				listHolder.rlTime = (RelativeLayout)convertView.findViewById(R.id.rl_time);
				listHolder.rlMin = (RelativeLayout)convertView.findViewById(R.id.rl_min);

				convertView.setTag(listHolder);
			} else {
				listHolder = (CouponView) convertView.getTag();
			}

			MyCouponModel model = couposList.list.get(paramInt);
			

			if (model != null) {
				listHolder.tvKey.setText(model.getCKey());
				if (model.getCType() == 1) {
					listHolder.tvType.setText("现金券");
					listHolder.tvValueName.setText("面额：");
					listHolder.tvValue.setText(String.format("%s￥", model.getCValue()));
				}else{
					listHolder.tvType.setText("折扣券");
					listHolder.tvValueName.setText("折扣：");
					listHolder.tvValue.setText(model.getCValue());
				}
				if (model.getCActivity() == 0) {
					listHolder.tvState.setText("未激活");
					listHolder.tvState.setTextColor(Color.rgb(242, 177, 23));
				}else{
					if (model.getIsUser() == 0) {
						listHolder.tvState.setText("未使用");
						listHolder.tvState.setTextColor(Color.rgb(86, 74, 138));
					}else{
						listHolder.tvState.setText("使用过");
						listHolder.tvState.setTextColor(Color.rgb(197, 77, 132));
					}
				}
				if (model.getGeiverPerson() == null || model.getGeiverPerson().length() < 1) {
					listHolder.tvGever.setText("");
				}else{
					listHolder.tvGever.setText(String.format("由好友[%s]赠送", model.getGeiverPerson()));
				}
				
				listHolder.llHas.setVisibility(View.GONE);//暂时不用
				
				if (model.getCTimeLimity() == 0) {
					listHolder.rlTime.setVisibility(View.VISIBLE);
					listHolder.tvTime.setText(String.format("%s到%s", model.getStartTime(), model.getEndTime()));
				}else{
					listHolder.rlTime.setVisibility(View.GONE);
				}
				
				if (model.getCMoneyLine() == 0) {
					listHolder.rlMin.setVisibility(View.VISIBLE);
					listHolder.tvMin.setText(String.format("%.2f￥", model.getMoneyLine()));
				}else{
					listHolder.rlMin.setVisibility(View.GONE);
				}
				
				if (model.getIsSelect() == 1) {
					convertView.setBackgroundColor(Color.rgb(246, 251, 197));
				}else{
					convertView.setBackgroundColor(Color.rgb(255, 255, 255));
				}
				
			}

			return convertView;
		}
	}

	class delFriendView {
		Button btnOpt;
		LinearLayout llLayout;
	}

	class geiverCouponListViewAdapter extends BaseAdapter {
		geiverCouponListViewAdapter() {
		}

		@Override
		public int getCount() {

			return couposList.list.size();

		}

		@Override
		public Object getItem(int paramInt) {
			return couposList.list.get(paramInt);

		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			if (position < 4) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public View getView(int paramInt, View convertView,
				ViewGroup paramViewGroup) {

			delFriendView listHolder;
			if (paramInt == couposList.list.size()) {
				return moreView1;
			}
			if (convertView == null || convertView == moreView) {
				convertView = inflater.inflate(R.layout.del_friend_item, null);
				listHolder = new delFriendView();
				listHolder.btnOpt = (Button) convertView
						.findViewById(R.id.btn_opt);
				listHolder.btnOpt.setOnClickListener(geiverCouponListener);
				listHolder.btnOpt.setText("赠予好友");
				listHolder.llLayout = (LinearLayout)convertView.findViewById(R.id.ll_item);
				convertView.setTag(listHolder);
			} else {
				listHolder = (delFriendView) convertView.getTag();
			}

			MyCouponModel model = couposList.list.get(paramInt);

			if (model != null) {
				View view = ListView.getChildAt(paramInt);
				
				if (view != null) {
					int height = 0;
					height = view.getHeight();
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                           1, height);
					listHolder.llLayout.setLayoutParams(lp);
				}
				if (model.getIsUser() == 0) {
					listHolder.btnOpt.setVisibility(View.VISIBLE);
				}else{
					listHolder.btnOpt.setVisibility(View.INVISIBLE);
				}
				listHolder.btnOpt.setTag(paramInt);

			} 

			return convertView;
		}
	}

}
