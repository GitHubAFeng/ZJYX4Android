package com.ihangjing.ZJYXForAndroid;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.HJControls.HJListView;
import com.ihangjing.HJControls.HJMoveRelativeLayout;
import com.ihangjing.Model.ReciveAddressListModel;
import com.ihangjing.Model.ReciveAddressModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

public class UserAddressList extends HjActivity implements HttpRequestListener {
	private static final String TAG = "ShopTypeList";
	ProgressDialog progressDialog = null;
	HJListView smsListView = null;

	ListViewAdapter mSchedule = null;

	private UIHandler hander = new UIHandler();

	// private int pageindex = 1;

	private TextView titleTextView;
	private Button backButton;
	// private int action = 1;// 1表示获取一级区域，2表示获取二级区域，建筑物
	//ReciveAddressListModel app.reciveAddressList;//
	String userID;
	int nSelectIndex = 0;

	private HttpManager localHttpManager;
	private String[] menu;// = { getResources().getString(R.string.addr_list_edit)/*"编辑"*/, getResources().getString(R.string.addr_list_del)/*"删除"*/ };
	private Button btnAddress;
	private int actionTag;
	protected float p1x;
	protected float p1y;
	protected long time1;
	
	//protected boolean moveLeftEnd = true;
	//private boolean moveRightEnd = false;
	private Animation Animation_toLeft;
	private Animation Animation_toRight;
	//private Animation AnimationZoom;
	//protected int left;
	//protected int top;
	//protected int with;
	//protected int heigth;
	protected UserAddressDedail dailog;
	private boolean isSelect;
	private int scrollPos;
	private int scrollTop;
	// private String areaID = "0";// 当前选中的区域Id
	protected boolean isStopRight = false;
	protected boolean isStopLeft = false;
	protected boolean isInLeft = false;
	protected boolean isInRight = true;
	
	private void initUI() {
		setContentView(R.layout.user_address_list);

		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		titleTextView.setText(getResources().getString(R.string.addr_list_title)/*"我的地址"*/);

		backButton = (Button) findViewById(R.id.title_bar_back_btn);
		OnClickListener btnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.title_bar_back_btn) {
					finish();
				} else if (v.getId() == R.id.title_bar_right_btn) {
					
					dailog = new UserAddressDedail(
							UserAddressList.this, null, true,
							new UserAddressDedail.UpdateAddress() {

								@Override
								public void updateAddress(
										ReciveAddressModel model) {
									// TODO Auto-generated method stub
									
									app.reciveAddressList.list.add(model);
									mSchedule.notifyDataSetChanged();
								}

								@Override
								public void Cancel() {
									// TODO Auto-generated method stub
									
								}

							}, userID, app);
					dailog.show();
				}

			}
		};
		backButton.setOnClickListener(btnClick);
		btnAddress = (Button) findViewById(R.id.title_bar_right_btn);
		btnAddress.setText(getResources().getString(R.string.addr_list_add)/*"新增"*/);
		btnAddress.setOnClickListener(btnClick);

		
		smsListView = (HJListView) UserAddressList.this
				.findViewById(R.id.shoptype_listview);

		
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menu = new String[2];//{ getResources().getString(R.string.addr_list_edit)/*"编辑"*/, getResources().getString(R.string.addr_list_del)/*"删除"*/ };
		menu[0] = getResources().getString(R.string.addr_list_edit);/*"编辑"*/
		menu[1] = getResources().getString(R.string.addr_list_del);/*"删除"*/		
		initUI();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isSelect = extras.getBoolean("select");
		}else{
			isSelect = false;
		}
		
		userID = OtherManager.getUserInfo(UserAddressList.this).userId;

		// 加载数据 开始加载页码为1
		// po = new SectionListModel();

		// 添加listview的点击事件 点击后进入详细页面 或者执行相应的操作
		smsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (isSelect) {
					app.selectIndex = arg2;
					finish();
				}

			}
		});

		smsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (totalItemCount != 0
						&& totalItemCount == firstVisibleItem
								+ visibleItemCount) {
					if (app.reciveAddressList.page < app.reciveAddressList.total) {
						getAddressListData(++app.reciveAddressList.page);
					}

				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

		});
		if (app.reciveAddressList == null) {
			app.reciveAddressList = new ReciveAddressListModel();
		}
		
		mSchedule = new ListViewAdapter();
		smsListView.setAdapter(mSchedule);
		
		
		if (!isSelect) {
			progressDialog = ProgressDialog.show(UserAddressList.this, "",
					getResources().getString(R.string.public_get_data_notice)/*"加载中，请稍后..."*/);
			getAddressListData(1);
		}else{
			btnAddress.setVisibility(View.GONE);
		}
		
	}

	private class UIHandler extends Handler {
		public static final int NET_ERROR = -1;
		public static final int NO_DATA = -2;
		public static final int GET_REC_ADDR_SUCCESS = 1;
		public static final int DELETE_ADDR_ERROR = -3;
		public static final int DELETE_ADDR_OK = 2;

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case DELETE_ADDR_ERROR:
				
				Toast.makeText(UserAddressList.this.getApplicationContext(),
						getResources().getString(R.string.addr_list_del_failed)/*"删除失败，请稍后再试！"*/, 5).show();
				break;
			case DELETE_ADDR_OK:
				Toast.makeText(UserAddressList.this.getApplicationContext(),
						getResources().getString(R.string.addr_list_del_sucess)/*"删除成功！"*/, 5).show();
				if (app.selectIndex == nSelectIndex) {
					app.selectIndex = 0;
				}
				app.reciveAddressList.list.remove(nSelectIndex);
				//smsListView.removeViewAt(nSelectIndex);
				//delListView.removeViewAt(nSelectIndex);
				mSchedule.notifyDataSetChanged();
				break;
			case GET_REC_ADDR_SUCCESS: {
				SharedPreferences localSharedPreferences = getSharedPreferences(HJAppConfig.CookieName, 0);
				int re = localSharedPreferences.getInt("isPopNotice", 0);
				if (re == 0) {
					Intent intent2 = new Intent(UserAddressList.this, NoticePopActivity.class);
					startActivity(intent2);
					SharedPreferences.Editor localEditor1 = getSharedPreferences(HJAppConfig.CookieName, 0).edit();
					
					localEditor1.putInt("isPopNotice",1);
					localEditor1.commit();
				}
				mSchedule.notifyDataSetChanged();
				if (OtherManager.DEBUG) {
					Log.v(TAG, "handleMessage DO_WITH_DATA 对listview进行更新");
				}

				return;
			}

			case NO_DATA:
				Toast.makeText(UserAddressList.this.getApplicationContext(),
						getResources().getString(R.string.public_net_no_data)/*"无相关数据"*/, 5).show();
				break;

			case NET_ERROR:
				Toast.makeText(UserAddressList.this.getApplicationContext(),
						getResources().getString(R.string.public_net_or_data_error)/*"网络或数据出错无法获取数据"*/, 5).show();
				break;
			}
		}
	}

	// paramInt 是页码
	private void getAddressListData(int index) {
		
		String url;
		actionTag = 0;
		url = "/Android/GetUserAddressList.aspx?pageindex="
				+ String.valueOf(index) + "&userid=" + userID;
		localHttpManager = new HttpManager(UserAddressList.this,
				UserAddressList.this, url, null, 2, 0);
		localHttpManager.getRequeest();
	}

	private void DelAddress() {
		progressDialog = ProgressDialog.show(UserAddressList.this, "",
				getResources().getString(R.string.addr_list_del_int)/*"删除中，请稍后..."*/);
		String url;
		actionTag = 1;
		url = "/Android/SaveUserAddress.aspx?dataid="
				+ app.reciveAddressList.list.get(nSelectIndex).getId() + "&op=-1";
		localHttpManager = new HttpManager(UserAddressList.this,
				UserAddressList.this, url, null, 2, 1);
		localHttpManager.getRequeest();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		System.gc();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (dailog != null && dailog.isShowing()) {
			dailog.updateView();
		}
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			String strJson = (String) paramObject;
			if (tag == 0) {
				if (app.reciveAddressList.StringToBean(strJson) > 0) {
					msg.what = UIHandler.GET_REC_ADDR_SUCCESS;
				} else {
					msg.what = UIHandler.NO_DATA;
				}
			} else {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(strJson);
					int type = jsonObject.getInt("state");

					if (type == 1) {
						msg.what = UIHandler.DELETE_ADDR_OK;
					} else {
						msg.what = UIHandler.DELETE_ADDR_ERROR;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = UIHandler.NET_ERROR;
				}

			}
		} else {
			msg.what = UIHandler.NET_ERROR;
		}
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		
		hander.sendMessage(msg);
	}

	// 列表项包含的组件
	static class ViewHolderEdit {
		TextView addr;
		TextView reciver;
		TextView phone;
		public HJMoveRelativeLayout rlParent;
		public RelativeLayout rlContent;
		public LinearLayout llOpt;
		public Button btnEdit;
		public Button btnDel;
		public LinearLayout llContent;
	}

	

	

	// 自定义adapter
	class ListViewAdapter extends BaseAdapter {

		LayoutInflater inflater = null;
		protected float dowX;
		protected float dowY;
		protected long dowTime;
		protected int max;
		@Override
		public int getCount() {
			return app.reciveAddressList.list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		public void toggle(int position) {
			this.notifyDataSetChanged();// date changed and we should refresh
										// the view
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolderEdit viewHolder;

			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) UserAddressList.this
							.getSystemService(LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.address_list_item, null);

				viewHolder = new ViewHolderEdit();
				viewHolder.llContent = (LinearLayout) convertView
						.findViewById(R.id.LinearLayout012);
				viewHolder.llContent.requestDisallowInterceptTouchEvent(false);

				viewHolder.llContent.setOnTouchListener(new OnTouchListener() {

					

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							dowTime = event.getDownTime();
							dowX = event.getX();
							dowY = event.getY();
							return true;
						}else if (event.getAction() == MotionEvent.ACTION_UP) {
							float x = dowX - event.getX();
							float y = dowY - event.getY();
							if (x == 0.0f) {
								if (isSelect) {
									app.selectIndex = (Integer)v.getTag();
									finish();
								}
							}
							else if ((x > 5.0f || x < -5.0f)) {
								ViewHolderEdit viewHolder = (ViewHolderEdit)((View)v.getParent().getParent()).getTag();
								
								
								max = (int) viewHolder.btnEdit.getX()
												+ (int) viewHolder.btnEdit.getWidth()
												- (int) viewHolder.btnDel.getX() + 10;
								
								
								

								viewHolder.rlParent.setMoveMaxXY(max * -1, 0);
								
								if (x > 0) {
									((HJMoveRelativeLayout) v.getParent()
											.getParent()).moveToMax();
								} else {
									((HJMoveRelativeLayout) v.getParent()
											.getParent()).moveBack();
								}
								return true;
							}
						}
						return false;
					}
				});
				viewHolder.rlParent = (HJMoveRelativeLayout) convertView
						.findViewById(R.id.rl_parent);
				viewHolder.rlContent = (RelativeLayout) convertView
						.findViewById(R.id.rl_content);
				viewHolder.llOpt = (LinearLayout) convertView
						.findViewById(R.id.ll_opt);
				viewHolder.addr = (TextView) convertView
						.findViewById(R.id.tv_name);
				viewHolder.reciver = (TextView) convertView
						.findViewById(R.id.tv_number);
				viewHolder.phone = (TextView) convertView
						.findViewById(R.id.tv_total);
				viewHolder.rlParent.setMoveView(viewHolder.rlContent);
				OnClickListener btnClick = new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						nSelectIndex = (Integer) arg0.getTag();
						if (arg0.getId() == R.id.btn_del) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									UserAddressList.this);
							builder.setMessage(getResources().getString(R.string.addr_list_del_notice)/*"您确定删除该收货地址？"*/)
									.setPositiveButton(getResources().getString(R.string.public_ok)/*"确定"*/,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog, int id) {
													DelAddress();
													dialog.cancel();
												}
											})
									.setNegativeButton(getResources().getString(R.string.public_cancel)/*"取消"*/,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog, int id) {
													dialog.cancel();
												}
											}).create().show();
						} else if (arg0.getId() == R.id.btn_edit) {
							dailog = new UserAddressDedail(
									UserAddressList.this,
									app.reciveAddressList.list.get(nSelectIndex), false,
									new UserAddressDedail.UpdateAddress() {

										@Override
										public void updateAddress(
												ReciveAddressModel model) {
											// TODO Auto-generated
											// method stub
											app.reciveAddressList.list.get(nSelectIndex)
													.setNewModel(model);
											mSchedule.notifyDataSetChanged();
										}

										@Override
										public void Cancel() {
											// TODO Auto-generated method stub
											
										}

									}, userID, app);
							dailog.show();
						} else {
						}
						
					}
				};
				viewHolder.btnEdit = (Button) convertView
						.findViewById(R.id.btn_edit);
				viewHolder.btnEdit.setOnClickListener(btnClick);
				viewHolder.btnDel = (Button) convertView
						.findViewById(R.id.btn_del);
				viewHolder.btnDel.setOnClickListener(btnClick);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderEdit) convertView.getTag();
			}
			viewHolder.llContent.setTag(position);
			viewHolder.btnEdit.setTag(position);
			viewHolder.btnDel.setTag(position);
			viewHolder.addr.setText(getResources().getString(R.string.addr_list_addr)/*"收货地址："*/
					+ app.reciveAddressList.list.get(position).getAddres());
			viewHolder.reciver.setText(getResources().getString(R.string.order_detail_reciver)/*"收货人："*/
					+ app.reciveAddressList.list.get(position).getReciver());
			viewHolder.phone.setText(getResources().getString(R.string.ordercontent_ph)/*"联系电话："*/
					+ app.reciveAddressList.list.get(position).getMobilePhone());
			// convertView.layout(convertView.getLeft(), convertView.getTop(),
			// convertView.getWidth(), 41);
			return convertView;
		}
	}

}
