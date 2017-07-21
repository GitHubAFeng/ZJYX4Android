package com.ihangjing.ZJYXForAndroid;

import java.nio.channels.CancelledKeyException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.ZJYXForAndroid.MyFriendsListView.delFriendsListViewAdapter;
import com.ihangjing.ZJYXForAndroid.ShopDetail.FoodListHolder;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.MyFriends;
import com.ihangjing.Model.MyFriendsList;
import com.ihangjing.SQLite.DBHelper;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class MyFriendsListView extends HjActivity implements
		HttpRequestListener {

	protected Animation Animation_toLeft = null;
	protected Animation Animation_toRight = null;
	private ListView ListView;
	private EditText etSearch;
	private Button btnCancle;
	private Button btnLookFriendsBuy;
	private ListView listViewDel;
	private MyFriendsList friendsList;
	private MyFriendsList searchList;
	private DBHelper db;
	int pageindex;
	private HttpManager localHttpManager;
	int showType = 0;// 0显示所有好友，1显示搜索好友
	public LayoutInflater inflater;
	FriendsListViewAdapter listViewAdapter;
	View moreView;
	private String dialogStr;
	private UIHandler mHandler;
	private boolean isShowInput;
	private OnClickListener addFriendListener;
	static int NET_DIALOG = 322;
	int indexOpt = 0;
	private String errMsg;
	protected long time1;
	protected float p1x;
	protected float p1y;
	protected boolean moveLeftEnd = true;
	protected boolean moveRightEnd = false;
	protected boolean isStopRight = false;
	protected boolean isStopLeft = false;
	protected boolean isInLeft = false;
	protected boolean isInRight = true;
	private delFriendsListViewAdapter delListViewAdapter;
	protected int scrollPos;
	protected int scrollTop;
	protected long time2;
	protected float p2x;
	protected float p2y;
	private int CDataID = 0;
	private int CheckIndex = -1;
	private Button btnPointTop;
	private Button btnPublicPointTop;
	private Button btnNotic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_friends);
		
		Bundle extern = getIntent().getExtras();
		if (extern != null) {
			CDataID  = extern.getInt("cid");
		}
		mHandler = new UIHandler();
		ListView = (ListView) findViewById(R.id.lv_data);
		listViewDel = (ListView) findViewById(R.id.lv_del);
		etSearch = (EditText) findViewById(R.id.main_et_search);
		etSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isShowInput = true;
				btnCancle.setVisibility(View.VISIBLE);

			}
		});
		etSearch.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				isShowInput = true;
				btnCancle.setVisibility(View.VISIBLE);
			}
		});

		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String value = String.valueOf(s);

				if (value.length() > 0) {
					if (db != null) {
						searchList.list = db.getFriends(value);
						showType = 1;
						listViewAdapter.notifyDataSetChanged();
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		btnCancle = (Button) findViewById(R.id.searchshop_btn_search);
		btnCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnCancle.setVisibility(View.GONE);
				etSearch.setText("");
				intputHiden();
				if (showType == 1) {
					showType = 0;
					listViewAdapter.notifyDataSetChanged();
				}

			}
		});
		btnPointTop = (Button)findViewById(R.id.btn_point_top);
		btnPublicPointTop = (Button)findViewById(R.id.btn_public_point_top);
		btnLookFriendsBuy = (Button) findViewById(R.id.btn_view_friends_buy);
		btnNotic = (Button)findViewById(R.id.btn_view_friends_not);
		if (CDataID != 0) {
			btnNotic.setText("请选择您要转赠的好友");
			btnNotic.setVisibility(View.VISIBLE);
			btnLookFriendsBuy.setVisibility(View.GONE);
			btnPointTop.setVisibility(View.GONE);
			btnPublicPointTop.setVisibility(View.GONE);
		}else{
			OnClickListener btnOnClickListener = new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent;
					switch (v.getId()) {
					case R.id.btn_view_friends_buy:
						// 查看好友购买记录
						intent = new Intent(MyFriendsListView.this, MyFriendsBuysListView.class);
						break;
					case R.id.btn_point_top:
						intent = new Intent(MyFriendsListView.this, MyFriendsPointTopListView.class);
						intent.putExtra("type", 0);
						break;
					case R.id.btn_public_point_top:
						intent = new Intent(MyFriendsListView.this, MyFriendsPointTopListView.class);
						intent.putExtra("type", 1);
						break;
					default:
						intent = null;
						break;
					}
					if (intent != null) {
						MyFriendsListView.this.startActivity(intent);
					}
					
				}
			};
			btnLookFriendsBuy.setOnClickListener(btnOnClickListener);
			btnPointTop.setOnClickListener(btnOnClickListener);
			btnPublicPointTop.setOnClickListener(btnOnClickListener);
		}
		ListView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (CDataID != 0) {
					return false;
				}
				intputHiden();
				if (showType == 1) {
					return false;
				}
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
							
			                if (friendsList.list != null) {  
			                	scrollPos = ListView.getFirstVisiblePosition();
			                       View v1=ListView .getChildAt(0);
			                       scrollTop=(v==null)?0:v1.getTop();
			                       listViewDel.setSelectionFromTop(scrollPos, scrollTop);
			                 }  
							
							ListView.startAnimation(Animation_toLeft);
							//Animation_toLeft.start();
							return true;
						} else if (x < -20.0f && isInLeft) {
							isStopRight = false;
							isInLeft = false;
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

		ListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				/*if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    // ListPos记录当前可见的List顶端的一行的位置
                    
				} */
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				// intputHiden();

				app.mLoadImage.doTask();
			}
		});
		ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (CDataID != 0) {
					if (CheckIndex != -1) {
						View view = ListView.getChildAt(CheckIndex);
						if (view != null) {
							view.setBackgroundColor(Color.rgb(255, 255, 255));
						}
					}
					
					CheckIndex = arg2;
					arg1.setBackgroundColor(Color.rgb(246, 251, 197));
				}
				
			}
		});
		
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
		

		friendsList = new MyFriendsList();

		searchList = new MyFriendsList();

		listViewAdapter = new FriendsListViewAdapter();
		ListView.setAdapter(listViewAdapter);

		delListViewAdapter = new delFriendsListViewAdapter();
		listViewDel.setAdapter(delListViewAdapter);

		addFriendListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				indexOpt = (Integer) v.getTag();
				AddFriend();
			}
		};

		db = new DBHelper(MyFriendsListView.this);

		inflater = LayoutInflater.from(MyFriendsListView.this);
		moreView = inflater.inflate(R.layout.more_friends_from_net, null);
		TextView tvMore = (TextView) moreView
				.findViewById(R.id.tv_more_friends);
		tvMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String value = etSearch.getText().toString();
				value = value.replace("'", "");
				searchFriendsFromServer(value);
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
		if (CDataID != 0) {
			tvRight.setText("确定选择");
		}else{
			tvRight.setText("通讯录匹配");
		}
		
		tvRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CDataID != 0) {
					geiveCoupon();
				}else{
					Intent intent = new Intent(MyFriendsListView.this,
							MyPhoneBookListView.class);
					startActivity(intent);
				}
			}
		});

	}

	protected void hiden() {
		// TODO Auto-generated method stub
		if (isInLeft) {
			isStopRight = false;
			isInLeft = false;
			ListView.startAnimation(Animation_toRight);

		}
	}
	
	private void geiveCoupon() {
		if (CheckIndex == -1) {
			Toast.makeText(MyFriendsListView.this, "请先选择好友！", 15).show();
			return;
		}
		dialogStr = "转赠中，请稍后...";
		MyFriends model;
		if (showType == 0) {
			model = friendsList.list.get(CheckIndex);
		}else{
			model = searchList.list.get(CheckIndex);
		}
		showDialog(NET_DIALOG);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("cid", String.valueOf(CDataID));
		localHashMap.put("Uidold", app.userInfo.userId);
		localHashMap.put("Unameold", app.userInfo.userName);
		localHashMap.put("Uid", String.valueOf(model.getFriendID()));
		localHashMap.put("Uname", model.getFriendName());
		localHttpManager = new HttpManager(MyFriendsListView.this,
				MyFriendsListView.this, "/Android/GiveCardToPerson.aspx?",
				localHashMap, 2, 4);
		localHttpManager.getRequeest();
	}

	private void AddFriend() {
		dialogStr = "请求中，请稍后...";
		showDialog(NET_DIALOG);
		MyFriends model = searchList.list.get(indexOpt);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("friendname", model.getFriendName());
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyFriendsListView.this,
				MyFriendsListView.this, "/Android/addfriend.aspx?",
				localHashMap, 2, 3);
		localHttpManager.getRequeest();
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
		
		if (friendsList.list.size() == 0) {
			if (db != null) {
				friendsList.list = db.getAllFriends();
				listViewAdapter.notifyDataSetChanged();
				delListViewAdapter.notifyDataSetChanged();
				pageindex = 1;
				getAllFriendsFromServer();
			}
		}
	}

	public void getAllFriendsFromServer() {
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(pageindex));
		localHashMap.put("pagesize", "30");
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyFriendsListView.this,
				MyFriendsListView.this, "/Android/friendlist.aspx?",
				localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}

	public void searchFriendsFromServer(String value) {
		dialogStr = "搜索中，请稍后...";
		showDialog(NET_DIALOG);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("friendname", value);
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyFriendsListView.this,
				MyFriendsListView.this, "/Android/searchfriend.aspx?",
				localHashMap, 2, 2);
		localHttpManager.getRequeest();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (showType == 1) {
				btnCancle.setVisibility(View.GONE);
				etSearch.setText("");
				if (isShowInput) {
					isShowInput = false;
				}
				showType = 0;
				listViewAdapter.notifyDataSetChanged();
				return true;
			} else {
				return super.onKeyDown(keyCode, paramKeyEvent);
			}
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
				int state;
				switch (tag) {
				case 1:
					// MyFriendsList list =new MyFriendsList();
					int page = json.getInt("page");
					int total = json.getInt("total");
					if (page == 1) {
						if (db != null) {
							db.cleanFriendsTable();
						}
					}
					JSONArray array = json.getJSONArray("datalist");
					int m = array.length();
					for (int i = 0; i < m; i++) {
						MyFriends model = new MyFriends(array.getJSONObject(i));
						if (db != null) {
							db.insertMyFriend(model);
						}
					}
					if (page < total) {
						msg.what = UIHandler.READD_NEXT;
					} else {
						msg.what = UIHandler.READ_FULL;
					}
					break;
				case 2:
					searchList.jsonToBean(json);
					msg.what = UIHandler.SEARCH_SCUESS;
					break;
				case 3:
					state = json.getInt("state");
					if (state == 1) {
						msg.what = UIHandler.ADD_SCUESS;
						if (db != null) {
							MyFriends model = searchList.list.get(indexOpt);
							model.setIsFriend(1);
							db.insertMyFriendWichCheck(model);
							friendsList.list.add(model);
						}
					} else {
						msg.what = UIHandler.ADD_FAILED;
						errMsg = json.getString("msg");
					}
					break;
				case 4:
					state = json.getInt("state");
					if (state == 1) {
						msg.what = UIHandler.GEIVER_SUCESS;
					}else{
						errMsg = json.getString("msg");
						msg.what = UIHandler.GEIVER_FAILD;
					}
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
		public static final int GEIVER_FAILD = -4;
		public static final int GEIVER_SUCESS = 5;
		public static final int ADD_FAILED = -3;
		public static final int ADD_SCUESS = 4;
		public static final int NET_ERROR = -2;
		public static final int SEARCH_SCUESS = 3;
		public static final int READ_FULL = 2;
		public static final int READD_NEXT = 1;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(NET_DIALOG);
			switch (msg.what) {
			
			case GEIVER_FAILD:
				Toast.makeText(MyFriendsListView.this, errMsg, 15).show();
				break;
			case GEIVER_SUCESS:
				Toast.makeText(MyFriendsListView.this, "转正成功！", 15).show();
				app.geiverCoupon = true;
				finish();
				break;
			case ADD_FAILED:
				Toast.makeText(MyFriendsListView.this, errMsg, 15).show();
				break;
			case ADD_SCUESS:
				Toast.makeText(MyFriendsListView.this, "添加好友成功", 10).show();
				listViewAdapter.notifyDataSetChanged();
				delListViewAdapter.notifyDataSetChanged();
				break;
			case NET_ERROR:
				Toast.makeText(MyFriendsListView.this, "网络或数据错误，请稍后再试！", 10)
						.show();
				break;
			case READD_NEXT:
				pageindex++;
				getAllFriendsFromServer();
				break;
			case READ_FULL:
				showType = 0;
				if (db != null) {
					friendsList.list = db.getAllFriends();
				}
				listViewAdapter.notifyDataSetChanged();
				delListViewAdapter.notifyDataSetChanged();
				break;
			case SEARCH_SCUESS:
				showType = 1;
				listViewAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}

	class FriendView {
		RelativeLayout icon;
		TextView name;
		Button btnOpt;
	}

	class FriendsListViewAdapter extends BaseAdapter {
		FriendsListViewAdapter() {
		}

		@Override
		public int getCount() {
			if (showType == 0) {
				return friendsList.list.size();
			}
			return searchList.list.size() + 1;
		}

		@Override
		public Object getItem(int paramInt) {
			if (showType == 0) {
				return friendsList.list.get(paramInt);
			} else {
				if (paramInt < searchList.list.size()) {
					return searchList.list.get(paramInt);
				}
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

			FriendView listHolder;
			if (showType == 1 && paramInt == searchList.list.size()) {
				return moreView;
			}
			if (convertView == null || convertView == moreView) {
				convertView = inflater.inflate(R.layout.friends_item, null);
				listHolder = new FriendView();
				listHolder.icon = (RelativeLayout) convertView
						.findViewById(R.id.rl_icon);
				listHolder.name = (TextView) convertView
						.findViewById(R.id.tv_name);
				listHolder.btnOpt = (Button) convertView
						.findViewById(R.id.btn_opt);
				listHolder.btnOpt.setOnClickListener(addFriendListener);

				convertView.setTag(listHolder);
			} else {
				listHolder = (FriendView) convertView.getTag();
			}

			MyFriends model;
			if (showType == 0) {
				model = friendsList.list.get(paramInt);
			} else {
				model = searchList.list.get(paramInt);
			}

			if (model != null) {
				listHolder.name.setText(model.getFriendName());
				listHolder.icon.setTag(model.getFriendICON());
				if (model.getFriendICON() != null
						&& !model.getFriendICON().equals("")) {
					app.mLoadImage.addTask(model.getFriendICON(), listHolder.icon,
							R.drawable.friend_default);
				} else {
					listHolder.icon
							.setBackgroundResource(R.drawable.friend_default);
				}
				listHolder.btnOpt.setVisibility(View.GONE);
				listHolder.btnOpt.setTag(paramInt);
				if (model.getIsFriend() == 0) {
					listHolder.btnOpt.setVisibility(View.VISIBLE);
				}

			} else {

			}

			return convertView;
		}
	}

	class delFriendView {
		Button btnOpt;
		LinearLayout llLayout;
	}

	class delFriendsListViewAdapter extends BaseAdapter {
		delFriendsListViewAdapter() {
		}

		@Override
		public int getCount() {

			return friendsList.list.size();

		}

		@Override
		public Object getItem(int paramInt) {
			return friendsList.list.get(paramInt);

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

			if (convertView == null || convertView == moreView) {
				convertView = inflater.inflate(R.layout.del_friend_item, null);
				listHolder = new delFriendView();
				listHolder.btnOpt = (Button) convertView
						.findViewById(R.id.btn_opt);
				listHolder.btnOpt.setOnClickListener(addFriendListener);
				listHolder.llLayout = (LinearLayout)convertView.findViewById(R.id.ll_item);
				convertView.setTag(listHolder);
			} else {
				listHolder = (delFriendView) convertView.getTag();
			}

			MyFriends model;
			model = friendsList.list.get(paramInt);

			if (model != null) {
				View view = ListView.getChildAt(paramInt);
				
				if (view != null) {
					int height = 0;
					height = view.getHeight();
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                           1, height);
					listHolder.llLayout.setLayoutParams(lp);
				}
				listHolder.btnOpt.setTag(paramInt);

			} else {

			}

			return convertView;
		}
	}

}
