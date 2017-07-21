package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.Model.ShopListModel;
import com.ihangjing.Model.UserDetail;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;
import com.ihangjing.util.HJAppConfig;

public class MyFavorShopActivity extends HjActivity implements
		HttpRequestListener {
	private Button btnReturn;
	private TextView tvTitle;
	private ListView listView;
	public ShopListModel shopListModel;
	private HttpManager localHttpManager;
	ListViewAdapter listAdapter;
	private int pageindex;
	private int total;
	UIHandler hander;
	// private LoadImage app.mLoadImage;
	private ProgressDialog progressDialog;
	private String[] menu = { "查看商家", "取消收藏" };
	int nSelectShop;// 当前单击Item时选中的商家
	private int actionType;
	protected int type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_favor_ui);
		tvTitle = (TextView) findViewById(R.id.title_bar_content_tv);
		Bundle bundle = getIntent().getExtras();
		
		if (bundle != null) {
			type = bundle.getInt("type");
		}
		if (type == 0) {
			tvTitle.setText("店铺收藏");
		} else {
			tvTitle.setText("最近使用的店铺");
		}
		btnReturn = (Button) findViewById(R.id.title_bar_back_btn);
		
		//tvTitle.setText("店铺收藏");
		OnClickListener btnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.title_bar_back_btn) {
					MyFavorShopActivity.this.finish();
				}
			}

		};
		btnReturn.setOnClickListener(btnClick);
		listView = (ListView) findViewById(R.id.content_list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// 得到被点击的商家的model
				
				nSelectShop = arg2;
				if (type == 1) {
					GoToShopDedail();
					return;
				}
				Builder builder = new AlertDialog.Builder(
						MyFavorShopActivity.this);
				builder.setTitle("请选择操作").setItems(menu,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									GoToShopDedail();
									break;
								case 1:
									AlertDialog.Builder builder = new AlertDialog.Builder(
											MyFavorShopActivity.this);
									builder.setMessage("您确定取消收藏？")
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															DeleteFave();
															dialog.cancel();
														}
													})
											.setNegativeButton(
													"取消",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															dialog.cancel();
														}
													}).create().show();
									
									break;
								}
							}
						});
				AlertDialog ad = builder.create();
				ad.show();
				// TODO:保存浏览记录

			}

		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (totalItemCount != 0
						&& firstVisibleItem + visibleItemCount == totalItemCount) {
					if (shopListModel.getPage() < shopListModel.getTotal()) {
						GetData(shopListModel.getPage() + 1, false);
					}
				}
				if (app.mLoadImage != null) {
					app.mLoadImage.doTask();
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

		});
		listAdapter = new ListViewAdapter();
		hander = new UIHandler();
		shopListModel = new ShopListModel();
		GetData(1, true);

	}
	protected void GoToShopDedail() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ShopDetail.class);
		intent.putExtra("shopid", shopListModel.list.get(nSelectShop).getId());
		intent.putExtra("shopname", shopListModel.list.get(nSelectShop)
				.getName());
		startActivity(intent);

	}
	void DeleteFave() {

		progressDialog = ProgressDialog.show(MyFavorShopActivity.this, "",
				"提交数据中，请稍后...");
		// 开始获取数据
		actionType = 1;
		UserDetail userDetail = OtherManager
				.getUserInfo(MyFavorShopActivity.this);
		String path = "APP/Android/SaveCollection.aspx?userid="
				+ userDetail.userId
				+ "&op=-1"
				+ "&togoid="
				+ MyFavorShopActivity.this.shopListModel.list.get(nSelectShop)
						.getId();
		localHttpManager = new HttpManager(MyFavorShopActivity.this,
				MyFavorShopActivity.this, path, null, 1, 1);

		localHttpManager.getRequeest();
	}

	void GetData(int index, boolean bShowPro) {
		actionType = 0;
		
		if (bShowPro) {
			progressDialog = ProgressDialog.show(MyFavorShopActivity.this, "",
					"获取数据中，请稍后...");
		}
		// 开始获取数据
		double lon;
		double lat;
		if (app.myLocation != null) {
			lon = app.myLocation.getLongitude();
			lat = app.myLocation.getLatitude();
		}else{
			lon = 0.0;
			lat = 0.0;
		}
		UserDetail userDetail = OtherManager
				.getUserInfo(MyFavorShopActivity.this);
		String path;
		if(type == 0){
			path = "APP/Android/GetCollectionListByUserId.aspx?userid=";
		}else{
			path = "APP/Android/GetShopListByUserOrder.aspx?userid=";
		}
		path += userDetail.userId + "&lat=" + String.valueOf(lat) + "&lng="
				+ String.valueOf(lon) + "&pageindex=" + String.valueOf(index);
		localHttpManager = new HttpManager(MyFavorShopActivity.this,
				MyFavorShopActivity.this, path, null, 1, 0);

		localHttpManager.getRequeest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		progressDialog.dismiss();
		if (paramInt == 260) {
			String jsonString = (String) paramObject;

			// 开始解析接收到的json数据
			JSONArray array = new JSONArray();

			JSONObject json = null;
			try {
				json = new JSONObject(jsonString);
				if (tag == 0) {
					this.pageindex = Integer.parseInt(json.getString("page"));
					this.total = Integer.parseInt(json.getString("total"));
					if (total == 0) {
						message.what = UIHandler.NO_DATA;
					} else {
						array = json.getJSONArray("list");

						shopListModel.setPage(pageindex);
						shopListModel.setTotal(total);

						ShopListItemModel models;// = new ShopListItemModel[array.length()];
						for (int i = 0; i < array.length(); i++) {
							JSONObject model = array.getJSONObject(i);
							models = new ShopListItemModel(model);
							this.shopListModel.list.add(models);

						}

						if (pageindex == 1) {
							message.what = UIHandler.GET_LIST;
						} else if (pageindex > 1) {
							// 如果是第二页数据则
							message.what = UIHandler.SET_SHOP_LIST_ONLY_NOTIFY;
						}

					}
				}else{
					int state = json.getInt("state");
					if(state == 1){
						message.what = UIHandler.REMOVE_FAVOR_SUCCESS;
					}else{
						message.what = UIHandler.REMOVE_FAVOR_ERROR;
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				message.what = UIHandler.NET_ERROR;
				e.printStackTrace();
			}

		} else {
			message.what = UIHandler.NET_ERROR;
		}

		hander.sendMessage(message); // 发送消息给Handler
	}

	public class UIHandler extends Handler {
		public static final int REMOVE_FAVOR_ERROR = -2;
		public static final int REMOVE_FAVOR_SUCCESS = 4;
		public static final int NET_ERROR = -1;
		public static final int GET_LIST = 1;
		public static final int SET_SHOP_LIST_ONLY_NOTIFY = 2;
		public static final int NO_DATA = 3;

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case REMOVE_FAVOR_ERROR:
				Toast.makeText(
						MyFavorShopActivity.this.getApplicationContext(),
						"取消收藏失败！请稍后再试", 5).show();
				break;
			case REMOVE_FAVOR_SUCCESS:
				Toast.makeText(
						MyFavorShopActivity.this.getApplicationContext(),
						"取消收藏成功", 5).show();
				shopListModel.list.remove(nSelectShop);
				listAdapter.notifyDataSetChanged();
				break;
			case NET_ERROR:
				Toast.makeText(
						MyFavorShopActivity.this.getApplicationContext(),
						"网络或数据出错无法获取数据", 5).show();
				break;
			case GET_LIST:
				listView.setAdapter(listAdapter);
				break;
			case SET_SHOP_LIST_ONLY_NOTIFY:
				listAdapter.notifyDataSetChanged();
				break;
			case NO_DATA:
				Toast.makeText(
						MyFavorShopActivity.this.getApplicationContext(),
						"没有相关数据", 5).show();
				break;
			}
		}
	}

	// 列表项包含的组件
	// 列表项包含的组件
		static class ViewHolderEdit {
			TextView shopnameTextView;
			TextView telTextView;
			TextView addressTextView;
			TextView starTextView;
			RelativeLayout iconImageView;
			TextView ordertimeTextView;
			TextView tvTypeTextView;
			TextView tvSendTime;
		}

		// 自定义adapter
		class ListViewAdapter extends BaseAdapter {

			LayoutInflater inflater = null;
			

			ListViewAdapter() {
			}

			@Override
			public int getCount() {
				return MyFavorShopActivity.this.shopListModel.list.size();
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
				ShopListItemModel localShoplist;
				if (convertView == null) {
					if (inflater == null) {
						inflater = (LayoutInflater) MyFavorShopActivity.this
								.getSystemService(LAYOUT_INFLATER_SERVICE);
					}
					convertView = inflater.inflate(R.layout.shopitem, null);

					viewHolder = new ViewHolderEdit();

					viewHolder.shopnameTextView = (TextView) convertView
							.findViewById(R.id.itemShopName);

					viewHolder.telTextView = (TextView) convertView
							.findViewById(R.id.itemTel);

					viewHolder.addressTextView = (TextView) convertView
							.findViewById(R.id.itemShopAddress);

					viewHolder.ordertimeTextView = (TextView) convertView
							.findViewById(R.id.itemOrderTime);

					viewHolder.iconImageView = (RelativeLayout) convertView
							.findViewById(R.id.itemImage);
					viewHolder.tvTypeTextView = (TextView)convertView.findViewById(R.id.tv_notic);
					viewHolder.tvSendTime = (TextView)convertView.findViewById(R.id.tv_send_time);

					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolderEdit) convertView.getTag();
				}

				localShoplist = MyFavorShopActivity.this.shopListModel.list.get(position);

				if (localShoplist != null) {

					viewHolder.shopnameTextView.setText(localShoplist.getName());
					
					viewHolder.telTextView.setText("起送价："
							+ localShoplist.getSendMoney());

					viewHolder.addressTextView.setText(localShoplist.getAdress());

					if (localShoplist.getTimeStart2().length() > 2
							&& localShoplist.getTimeEnd2().length() > 2) {
						viewHolder.ordertimeTextView.setText(localShoplist
								.getTimeStart1()
								+ "-"
								+ localShoplist.getTimeEnd1()
								+ "  "
								+ localShoplist.getTimeStart2()
								+ "-"
								+ localShoplist.getTimeEnd2());
					} else {
						viewHolder.ordertimeTextView.setText(localShoplist
								.getTimeStart1()
								+ "-"
								+ localShoplist.getTimeEnd1());
					}
					
					viewHolder.tvSendTime.setText("送达时间：" + localShoplist.getSendTime() + "分钟");
					
					if(localShoplist.getStatus() == 0){
						viewHolder.tvTypeTextView.setText("休息");
					}else{
						viewHolder.tvTypeTextView.setText("营业");
					}

					// 绘制边框 显示商家logo
					
					viewHolder.iconImageView.setTag(localShoplist.getIcon());
					if (!(localShoplist.getIcon().trim().length() == 0)) {
						
						app.mLoadImage.addTask(localShoplist.getIcon(), viewHolder.iconImageView,
								R.drawable.nopic_shop);
					} else {
						viewHolder.iconImageView
								.setBackgroundResource(R.drawable.nopic_shop);
					}
					
					/*String str1 = localShoplist.getIcon();
					int i = localShoplist.getIcon().lastIndexOf("/") + 1;
					String str2 = str1.substring(i);
					

					if (!bitMapList.containsKey(str2) || str2.length() == 0) {
						// 没有则要显示暂无图片
						 viewHolder.iconImageView.setImageResource(R.drawable.nopic_shop);
						Log.v(TAG,
								"if (!ShopList.this.bitMapList.containsKey(str2))");
					} else {
						Log.v(TAG, str2);
						Bitmap localBitmap = (Bitmap) FsgShopList.this.bitMapList
								.get(str2);
						BitmapDrawable localBitmapDrawable = new BitmapDrawable(
								localBitmap);
						viewHolder.iconImageView
								.setImageDrawable(localBitmapDrawable);
					}*/

				}

				return convertView;
			}
		}

}
