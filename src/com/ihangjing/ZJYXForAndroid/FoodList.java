package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.ZJYXForAndroid.FoodList.FoodListHolder;
import com.ihangjing.Model.FoodAttrModel;
import com.ihangjing.Model.FoodListModel;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.MyShopCart;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class FoodList extends HjActivity implements HttpRequestListener {

	private Button btnHot;
	private Button btnNew;
	private HttpManager localHttpManager;
	private String type = "0";// 0 热卖， 1促销，2 新品
	private int pageindex = 1;
	private UIHandler mHandler = new UIHandler();
	private int total;
	private int listIndex = 0;
	private FoodListModel[] foodListModel;
	private int index;
	private GridView gvView;
	public LayoutInflater inflater;
	private OnClickListener btnClickListener;
	FoodListViewAdapter listViewAdapter;
	private Button btnRight;
	private TextView tvTitle;
	private Button btnBack;
	private ViewPager viewSwitcher;
	private ArrayList<View> listMainViews;
	private LinearLayout llFoodAttr;
	private TextView tvFoodName;
	private RelativeLayout rlFoodImage;
	private TextView tvFoodPriceCurrent;
	private TextView tvPra;
	private TextView tvShowShop;
	private TextView tvFoodDes;
	private TextView tvFoodPriceO;
	private TextView tvFoodPriceN;
	private TextView tvFoodBuyCount;
	protected FoodModel foodModel;
	private Button btnFoodFav;
	private TextView tvFoodPriceName;
	private ImageView ivFoodType;
	public OnClickListener addToShopCartListener;
	public OnClickListener delFromShopCartListener;
	private FoodAttrAdapter listFoodAttrAdapter;

	@Override
	protected void onResume() {
		super.onResume();
		if (app.shopCartModel == null) {
			app.shopCartModel = new MyShopCart();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	private void setAdapter() {
		llFoodAttr.removeAllViews();
		int i = this.listFoodAttrAdapter.getCount();
		int j = 0;


		while (true) {
			if (j >= i) {
				return;
			}
			View localView = this.listFoodAttrAdapter.getView(j, null, null);
			llFoodAttr.addView(localView);
			j += 1;
		}
	}
	public void InitFoodUI() {
		// TODO Auto-generated method stub
		rlFoodImage.setTag(foodModel.getImageNetPath());
		if (foodModel.getImageNetPath() != null
				&& foodModel.getImageNetPath().length() > 18) {// http://*.*.*/*.jpg
			// 有网络地址，并且没有加入下载，那么加入
			// model.isDowload = true;
			rlFoodImage.setTag(foodModel.getImageNetPath());
			app.mLoadImage.addTask(foodModel.getImageNetPath(),
					rlFoodImage, R.drawable.nopic_shop);
		} else {
			rlFoodImage
					.setBackgroundResource(R.drawable.nopic_shop);
		}
		tvFoodName.setText(foodModel.getFoodName());
		tvPra.setText(String.valueOf(foodModel.getIstuan()));

		if (foodModel.getJoinnum() == 1) {
			tvFoodPriceO.setText(String.format("市场价  ￥%.2f", foodModel.listSpec.get(0).oldPrice));//市场价
			tvFoodPriceO.setVisibility(View.VISIBLE);
			tvFoodPriceName.setText("促销价  ");//修改当前价格名称
		}else{
			tvFoodPriceO.setVisibility(View.GONE);
			tvFoodPriceName.setText("销售价  ");//修改当前价格名称
		}
		
		
		tvFoodPriceN.setText(String.format("￥%.2f", foodModel.listSpec.get(0).price));//促销价		
		tvFoodBuyCount.setText(String.format("已出售  %d件", foodModel.getSale()));//出售数量
		
		//ivFoodType;//限时、特惠等图标
		if (foodModel.getIscollect() == 1) {
			btnFoodFav.setText("取消收藏");
		}else{
			btnFoodFav.setText("收藏");
		}
		
		
		setAdapter();
		
	}

	private void InitUI() {
		tvTitle = (TextView) findViewById(R.id.title_bar_content_tv);
		tvTitle.setText("爱逛街");
		btnBack = (Button) findViewById(R.id.title_bar_back_btn);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (viewSwitcher.getCurrentItem()) {
				case 0:

					finish();
					break;
				case 1:
					viewSwitcher.setCurrentItem(0);
					break;
				case 2:
					viewSwitcher.setCurrentItem(1);
					break;
				case 3:
					viewSwitcher.setCurrentItem(2);
					break;
				default:
					break;
				}

			}
		});
		btnRight = (Button) findViewById(R.id.title_bar_right_btn);
		btnRight.setVisibility(View.GONE);

		viewSwitcher = (ViewPager) findViewById(R.id.vs_select);
		listMainViews = new ArrayList<View>();

		// 商家列表视图
		View view1 = getLayoutInflater().inflate(R.layout.foodlist_foodlist,
				null);

		btnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_1:
					btnHot.setBackgroundResource(R.drawable.left_btn_pressed);
					btnNew.setBackgroundResource(R.drawable.right_btn_normal);
					type = "0";
					pageindex = 1;
					GetFoodList();
					break;
				case R.id.btn_2:
					btnHot.setBackgroundResource(R.drawable.left_btn_normal);
					btnNew.setBackgroundResource(R.drawable.right_btn_pressed);
					type = "2";
					pageindex = 1;
					GetFoodList();
					break;
				case R.id.btn_go:
					int index = (Integer)v.getTag();
					foodModel = foodListModel[listIndex].list.get(index);
					if (app.mShop == null) {
						app.mShop = new ShopListItemModel();
						
						app.mShop.setStatus(1);
					}
					app.mShop.setId(foodModel.getTogoID());
					
					viewSwitcher.setCurrentItem(1);
					break;
				default:
					break;
				}
			}

			
		};

		btnHot = (Button) view1.findViewById(R.id.btn_1);
		btnHot.setOnClickListener(btnClickListener);
		btnNew = (Button) view1.findViewById(R.id.btn_2);
		btnNew.setOnClickListener(btnClickListener);

		gvView = (GridView) view1.findViewById(R.id.gv_food);
		gvView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount >= totalItemCount
						&& pageindex < total) {
					pageindex++;
					GetFoodList();
				}
				if (app.mLoadImage != null) {
					app.mLoadImage.doTask();
				}
			}
		});
		listMainViews.add(view1);

		// 食品详情

		view1 = getLayoutInflater().inflate(R.layout.foodlist_foodinfo, null);
		llFoodAttr = (LinearLayout) view1.findViewById(R.id.lv_attr);
		tvFoodName = (TextView) view1.findViewById(R.id.tv_name);
		rlFoodImage = (RelativeLayout) view1.findViewById(R.id.iv_img);

		tvFoodPriceCurrent = (TextView) view1.findViewById(R.id.tv_sub_total);

		tvPra = (TextView) view1.findViewById(R.id.tv_poit);// 赞
		tvPra.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {// 赞
				// TODO Auto-generated method stub
				// AlertDialog dialog = new AlertDialog();

			}
		});

		tvShowShop = (TextView) view1.findViewById(R.id.tv_pro_rev);
		tvShowShop.setOnClickListener(new OnClickListener() {// 评论

					@Override
					public void onClick(View v) {// 查看想家详情
						// TODO Auto-generated method stub

					}
				});

		tvFoodDes = (TextView) view1.findViewById(R.id.tv_pro_des);
		tvFoodDes.setOnClickListener(new OnClickListener() {// 菜品详情

					@Override
					public void onClick(View v) {// 菜品详情
						// TODO Auto-generated method stub

					}
				});
		
		tvFoodPriceO = (TextView)view1.findViewById(R.id.tv_price_o);//市场价
		tvFoodPriceN = (TextView)view1.findViewById(R.id.tv_price_n);//促销价		
		tvFoodBuyCount = (TextView)view1.findViewById(R.id.tv_buy_count);//出售数量
		ivFoodType = (ImageView)view1.findViewById(R.id.iv_type);//限时、特惠等图标
		tvFoodPriceName = (TextView)view1.findViewById(R.id.tv_price_name);//修改当前价格名称
		btnFoodFav = (Button)view1.findViewById(R.id.btn_fav);//菜品收藏
		btnFoodFav.setOnClickListener(new OnClickListener() {////菜品收藏
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		listMainViews.add(view1);
		// 商家详情视图
		view1 = getLayoutInflater().inflate(R.layout.foodlist_shopdetail, null);

		listMainViews.add(view1);

		viewSwitcher.setAdapter(new HJPagerAdapter(listMainViews));

		viewSwitcher.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					tvTitle.setText("爱逛街");

					btnRight.setVisibility(View.GONE);
					break;
				case 1:
					tvTitle.setText("商品详情");
					btnRight.setVisibility(View.VISIBLE);
					if (foodModel == null) {
						foodModel = foodListModel[listIndex].list.get(0);
					}
					InitFoodUI();
					break;
				case 2:
					tvTitle.setText("商家详情");
					btnRight.setVisibility(View.GONE);
					
					break;
				
				default:
					break;
				}
				if (arg0 == 1) {

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
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent paramKeyEvent) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (viewSwitcher.getCurrentItem() != 0) {
				viewSwitcher.setCurrentItem(viewSwitcher.getCurrentItem() - 1);
			} else {
				
				finish();
			}
		} else {
			boolean bool = true;
			bool = super.onKeyDown(keyCode, paramKeyEvent);
			return bool;
		}

		return true;
	}

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.foodlist);
		InitUI();
		
		addToShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if (shopmodeDetailModel.getStatus() < 1) {
					Toast.makeText(ShopDetail.this, "商家休息中，无法订单！请换其他商家！", 15).show();
					return;
				}
				if (shopmodeDetailModel.getDistance() > shopmodeDetailModel.getSendDistance()) {
					Toast.makeText(ShopDetail.this, "您到商家的距离超出了商家的派送范围，无法下单！请换其他商家", 15).show();
					return;
				}
				app.mShop.setSendMoney(shopmodeDetailModel.getSendmoney());
				app.mShop.setFullFreeMoney(shopmodeDetailModel.getFreeSendMoney());
				app.mShop.setStartSendMoney(shopmodeDetailModel.getMinMoney());
				app.mShop.setLatitude(shopmodeDetailModel.getlat());
				app.mShop.setLongtude(shopmodeDetailModel.getlng());
				app.mShop.setSendDistance(shopmodeDetailModel.getSendDistance());
				app.mShop.setMaxKM(shopmodeDetailModel.getMaxKM());
				app.mShop.setMinKM(shopmodeDetailModel.getMinKM());
				app.mShop.setSendFeeAffKM(shopmodeDetailModel.getSendFeeAffKM());
				app.mShop.setSendFeeOfKM(shopmodeDetailModel.getSendFeeOfKM());
				app.mShop.setStartSendFee(shopmodeDetailModel.getStartSendFee());*/
				//int pt = (Integer) v.getTag();
				TextView text = (TextView) v.getTag(R.id.tab_label);
				
				text.setText(String.valueOf(app.shopCartModel.addFoodAttr(app.mShop, 
						foodModel, 0, Integer.parseInt(text.getText().toString()))));
			}
		};

		delFromShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//int pt = (Integer) v.getTag();
				TextView text = (TextView) v.getTag(R.id.tab_label);
				text.setText(String.valueOf(app.shopCartModel.delFoodAttr(app.mShop, 
						foodModel, 0)));
			}
		};
		
		foodListModel = new FoodListModel[2];
		foodListModel[0] = new FoodListModel();
		foodListModel[1] = new FoodListModel();
		listFoodAttrAdapter = new FoodAttrAdapter();

		GetFoodList();
	}

	public void GetFoodList() {
		if (pageindex == 1) {
			showDialog(322);
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(pageindex));
		if (app.userInfo != null && app.userInfo.userId.length() > 0) {
			localHashMap.put("userid", app.userInfo.userId);
		}
		localHashMap.put("type", type);
		localHashMap.put("pagesize", "20");
		localHashMap.put("isbuy", "0");// 0表示线上商品，1表示线下商品

		localHttpManager = new HttpManager(FoodList.this, FoodList.this,
				"/Android/GetFoodListByShopId.aspx?", localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, "加载中。。。，请稍后！");

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

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			JSONObject json = null;
			JSONArray array;
			String value1 = (String) paramObject;
			try {
				json = new JSONObject(value1);
				switch (tag) {
				case 1:

					array = json.getJSONArray("foodlist");

					this.pageindex = Integer.parseInt(json.getString("page"));
					this.total = Integer.parseInt(json.getString("total"));
					index = listIndex;
					if (pageindex == 1) {
						if (index == 1) {
							index = 0;
						} else {
							index++;
						}
						foodListModel[index].list.clear();

					}
					foodListModel[index].setPage(pageindex);
					foodListModel[index].setTotal(total);
					FoodModel models;
					for (int i = 0; i < array.length(); i++) {
						models = new FoodModel(array.getJSONObject(i));
						foodListModel[index].list.add(models);
					}
					msg.what = UIHandler.FOOD_DOWNLOAD_SUCCESS;
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

	private class UIHandler extends Handler {
		public static final int NET_ERROR = -1;
		public static final int FOOD_DOWNLOAD_SUCCESS = 1;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(322);
			switch (msg.what) {
			case NET_ERROR:
				Toast.makeText(FoodList.this, "网络或数据错误，请稍后再试！", 15).show();
				break;
			case FOOD_DOWNLOAD_SUCCESS:
				listIndex = index;
				if (listViewAdapter == null) {
					listViewAdapter = new FoodListViewAdapter();
					gvView.setAdapter(listViewAdapter);
					// listViewAdapter.notify();
				} else {
					listViewAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
	}

	class FoodListHolder {
		TextView tv_foodName;
		TextView tv_foodInfo;
		RelativeLayout iv_foodImg;
		TextView tv_foodPrice;

		Button btnGo;

		FoodListHolder() {
		}
	}

	// 菜单
	class FoodListViewAdapter extends BaseAdapter {
		FoodListViewAdapter() {
		}

		@Override
		public int getCount() {
			return foodListModel[listIndex].list.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return foodListModel[listIndex].list.get(paramInt);
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
			if (inflater == null) {
				inflater = LayoutInflater.from(FoodList.this);
			}

			FoodListHolder foodlistHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.food_item_for_girdview,
						null);

				foodlistHolder = new FoodListHolder();
				foodlistHolder.iv_foodImg = (RelativeLayout) convertView
						.findViewById(R.id.rl_img);
				foodlistHolder.tv_foodInfo = (TextView) convertView
						.findViewById(R.id.tv_dis);
				foodlistHolder.tv_foodName = (TextView) convertView
						.findViewById(R.id.tv_name);
				foodlistHolder.tv_foodPrice = (TextView) convertView
						.findViewById(R.id.tv_price);

				foodlistHolder.btnGo = (Button) convertView
						.findViewById(R.id.btn_go);
				foodlistHolder.btnGo.setOnClickListener(btnClickListener);
				convertView.setTag(foodlistHolder);
			} else {
				foodlistHolder = (FoodListHolder) convertView.getTag();
			}

			FoodModel model = foodListModel[listIndex].list.get(paramInt);

			if (model != null) {
				foodlistHolder.btnGo.setTag(paramInt);
				foodlistHolder.tv_foodName.setText(model.getFoodName());
				foodlistHolder.tv_foodInfo.setText(model.getDisc());
				foodlistHolder.tv_foodPrice.setText(String.valueOf(model
						.getPrice()) + "￥");

				foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
				if (model.getImageNetPath() != null
						&& model.getImageNetPath().length() > 18) {// http://*.*.*/*.jpg
					// 有网络地址，并且没有加入下载，那么加入
					// model.isDowload = true;
					foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
					app.mLoadImage.addTask(model.getImageNetPath(),
							foodlistHolder.iv_foodImg, R.drawable.nopic_shop);
				} else {
					foodlistHolder.iv_foodImg
							.setBackgroundResource(R.drawable.nopic_shop);
				}

			} else {
			}

			return convertView;
		}
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
	
	
	class FoodAttrView {
		TextView tvAttr;
		EditText tvNum;
		Button btnAdd;
		Button btnDel;
	}

	class FoodAttrAdapter extends BaseAdapter {
		FoodAttrView listHolder;
		LayoutInflater inflater;

		FoodAttrAdapter() {
		}

		@Override
		public int getCount() {
			if (foodModel == null) {
				return 0;
			}
			return foodModel.listSpec.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return foodModel.listSpec.get(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {

			if (paramView == null) {
				if (inflater == null) {
					inflater = LayoutInflater.from(FoodList.this);
				}
				paramView = this.inflater
						.inflate(R.layout.food_attr_item, null);

				listHolder = new FoodAttrView();

				listHolder.tvAttr = (TextView) paramView
						.findViewById(R.id.addorder_foodname);
				listHolder.tvNum = (EditText) paramView
						.findViewById(R.id.tv_foodcount);
				listHolder.tvNum.setEnabled(false);
						/*.setOnFocusChangeListener(new View.OnFocusChangeListener() {

							@Override
							public void onFocusChange(View arg0, boolean arg1) {
								// TODO Auto-generated method stub

								
							}
						});
				listHolder.tvNum.addTextChangedListener(new TextWatcher() {
					Boolean isSetValue = false;

					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub
						if (nowEdit != null) {
							if (arg0.length() > 0 && !isSetValue) {
								isSetValue = true;
								int paramInt = (Integer) nowEdit.getTag();

								int count = Integer.parseInt(arg0.toString());
								nowEdit.setText(String
										.valueOf(app.shopCartModel.setFoodAttr(
												foodModel, paramInt, count)));
								tvFoodPriceCurrent.setText(String.format(
										"合计：￥%.2f元", app.shopCartModel
												.getFoodPrice(foodModel
														.getFoodId())));
								setCondition();
							} else {
								isSetValue = false;
								nowEdit.setSelection(arg0.length(),
										arg0.length());// 设置光标位置
							}
						}

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub
					}

					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub

					}
				});*/

				listHolder.btnAdd = (Button) paramView
						.findViewById(R.id.btn_number_plus);
				listHolder.btnDel = (Button) paramView
						.findViewById(R.id.btn_number_minus);
				listHolder.btnAdd.setOnClickListener(addToShopCartListener);
				listHolder.btnDel.setOnClickListener(delFromShopCartListener);
				paramView.setTag(paramView);
			} else {
				listHolder = (FoodAttrView) paramView.getTag();
			}

			FoodAttrModel model = foodModel.listSpec.get(paramInt);

			if (model != null) {

				listHolder.tvNum.setTag(paramInt);
				listHolder.tvAttr.setText("选择数量");
				listHolder.tvNum.setText(String.valueOf(app.shopCartModel
						.getFoodCountInAttr(app.mShop, foodModel.getFoodId(), model.cId)));
				listHolder.btnAdd.setTag(paramInt);
				listHolder.btnAdd.setTag(R.id.tab_label, listHolder.tvNum);

				listHolder.btnDel.setTag(paramInt);

				listHolder.btnDel.setTag(R.id.tab_label, listHolder.tvNum);

			} else {
			}

			return paramView;
		}
	}
}
