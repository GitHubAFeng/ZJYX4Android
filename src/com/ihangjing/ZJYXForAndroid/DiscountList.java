package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
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

import com.ihangjing.Model.BuySaleListModel;
import com.ihangjing.Model.BuySaleModel;
import com.ihangjing.Model.FoodAttrModel;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.GroupBuyListModel;
import com.ihangjing.Model.GroupBuyModel;
import com.ihangjing.Model.LimitedListModel;
import com.ihangjing.Model.LimitedModel;
import com.ihangjing.Model.SeckillListModel;
import com.ihangjing.Model.SeckillModel;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class DiscountList extends HjActivity implements HttpRequestListener {
	private ViewPager viewSwitcher;
	private ArrayList<View> listMainViews;
	private Button btnOne;
	private Button btnTwo;
	private Button btnThe;
	private Button btnFor;
	private ListView oneListView;
	private ListView forListView;
	private ListView twoListView;
	private ListView theListView;
	private double lat;
	private double lng;

	private HttpManager localHttpManager;
	private int seckillPageIndex = 1;
	private int limitPageIndex = 1;
	private int groupBuyPageIndex = 1;
	private int buySalePageIndex = 1;
	private String dialogStr = "加载数据中！请稍各1�7...";
	private UIHandler mHandler;
	private Button btnRight;
	BuySaleListModel buySaleListModel;
	private LayoutInflater inflater;
	private OnClickListener addToShopCartListener;
	private OnClickListener delFromShopCartListener;
	

	BuySaleListAdapter buySaleListAdapter;
	private SeckillListModel seckillListModel;
	public SeckillListAdapter seckillListAdapter;
	private OnClickListener delFromShopCartListenerForSeckill;
	private OnClickListener addToShopCartListenerForSeckill;
	private LimitedListModel limitedListModel;
	public LimitedListAdapter limitedListAdapter;
	private OnClickListener addToShopCartListenerForLimit;
	private OnClickListener delFromShopCartListenerForLimit;
	private GroupBuyListModel groupBuyListModel;
	public GroupBuyListAdapter groupBuyListAdapter;
	private int groupBuySelectIndex = 0;//当前点击的团购第几个索引

	@Override
	protected void onResume() {
		super.onResume();
		app.isShowSale = false;
		switch (app.getSaleType) {
		case 0:
			if (buySaleListModel == null || buySaleListModel.list.size() == 0) {
				showDialog(322);
				getBuySaleList();
			}else{
				buySaleListAdapter.notifyDataSetChanged();
			}

			break;
		case 1:
			if (seckillListModel != null && seckillListModel.list.size() != 0) {
				seckillListAdapter.notifyDataSetChanged();
			}
			
			break;
		case 2:
			if (limitedListModel != null && limitedListModel.list.size() != 0) {
				limitedListAdapter.notifyDataSetChanged();
			}else{
				
			}
			break;
		case 3:
			if (groupBuyListModel != null && groupBuyListModel.list.size() != 0) {
				groupBuyListAdapter.notifyDataSetChanged();
			}else{
				
			}
			
			break;
		default:
			app.getSaleType = 0;
			if (buySaleListModel == null || buySaleListModel.list.size() == 0) {
				showDialog(322);
				getBuySaleList();
			}else{
				buySaleListAdapter.notifyDataSetChanged();
			}
			break;
		}
		viewSwitcher.setCurrentItem(app.getSaleType);

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.discount_list);
		mHandler = new UIHandler();
		OnClickListener btnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_1:
					viewSwitcher.setCurrentItem(0);
					break;
				case R.id.btn_2:
					viewSwitcher.setCurrentItem(1);
					break;
				case R.id.btn_3:
					viewSwitcher.setCurrentItem(2);
					break;
				case R.id.btn_4:
					viewSwitcher.setCurrentItem(3);
					break;
				case R.id.title_bar_right_btn:
					if (app.shopCartModel.list.size() == 0) {
						Toast.makeText(DiscountList.this, "购物车为空！", 5).show();
						return;
					}
					Intent localIntent = new Intent("com.ihangjing.common.tap3");
					app.sendBroadcast(localIntent);
					break;
				default:
					break;
				}
			}
		};

		btnOne = (Button) findViewById(R.id.btn_1);
		btnOne.setOnClickListener(btnClickListener);
		btnTwo = (Button) findViewById(R.id.btn_2);
		btnTwo.setOnClickListener(btnClickListener);
		btnThe = (Button) findViewById(R.id.btn_3);
		btnThe.setOnClickListener(btnClickListener);
		btnFor = (Button) findViewById(R.id.btn_4);
		btnFor.setOnClickListener(btnClickListener);

		btnRight = (Button) findViewById(R.id.title_bar_right_btn);
		btnRight.setOnClickListener(btnClickListener);

		viewSwitcher = (ViewPager) findViewById(R.id.vs_select);
		viewSwitcher.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					btnOne.setBackgroundResource(R.drawable.left_btn_pressed);
					btnTwo.setBackgroundResource(R.drawable.middle_btn_normal);
					btnThe.setBackgroundResource(R.drawable.middle_btn_normal);
					btnFor.setBackgroundResource(R.drawable.right_btn_normal);
					if (buySaleListModel == null || buySaleListModel.list.size() == 0) {
						getBuySaleList();
					}
					break;
				case 1:
					btnOne.setBackgroundResource(R.drawable.left_btn_normal);
					btnTwo.setBackgroundResource(R.drawable.middle_btn_pressed);
					btnThe.setBackgroundResource(R.drawable.middle_btn_normal);
					btnFor.setBackgroundResource(R.drawable.right_btn_normal);
					if (seckillListModel == null || seckillListModel.list.size() == 0) {
						showDialog(322);
						getSeckillList();
					}
					break;
				case 2:
					btnOne.setBackgroundResource(R.drawable.left_btn_normal);
					btnTwo.setBackgroundResource(R.drawable.middle_btn_normal);
					btnThe.setBackgroundResource(R.drawable.middle_btn_pressed);
					btnFor.setBackgroundResource(R.drawable.right_btn_normal);
					if (limitedListModel == null || limitedListModel.list.size() == 0) {
						showDialog(322);
						getLimitedList();
					}
					break;
				case 3:
					btnOne.setBackgroundResource(R.drawable.left_btn_normal);
					btnTwo.setBackgroundResource(R.drawable.middle_btn_normal);
					btnThe.setBackgroundResource(R.drawable.middle_btn_normal);
					btnFor.setBackgroundResource(R.drawable.right_btn_pressed);
					if (groupBuyListModel == null || groupBuyListModel.list.size() == 0) {
						showDialog(322);
						getGroupBuyList();
					}
					break;
				default:
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
		inflater = LayoutInflater.from(DiscountList.this);
		View view1 = inflater.inflate(R.layout.discount_detail_list, null);
		oneListView = (ListView) view1.findViewById(R.id.foodlist_listView1);
		oneListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (buySaleListModel != null && firstVisibleItem + visibleItemCount >= totalItemCount && buySalePageIndex < buySaleListModel.getTotal()) {
					buySalePageIndex++;
					getBuySaleList();
				}
			}
		});
		listMainViews.add(view1);

		view1 = inflater.inflate(R.layout.discount_detail_list, null);
		twoListView = (ListView) view1.findViewById(R.id.foodlist_listView1);
		twoListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (seckillListModel != null && firstVisibleItem + visibleItemCount >= totalItemCount &&
						seckillListModel.getPage() < seckillListModel.getTotal()) {
					seckillPageIndex++;
					getSeckillList();
				}
			}
		});
		listMainViews.add(view1);

		view1 = inflater.inflate(R.layout.discount_detail_list, null);
		theListView = (ListView) view1.findViewById(R.id.foodlist_listView1);
		theListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (limitedListModel != null && firstVisibleItem + visibleItemCount >= totalItemCount &&
						limitedListModel.getPage() < limitedListModel.getTotal()) {
					limitPageIndex++;
					getLimitedList();
				}
			}
		});
		listMainViews.add(view1);

		view1 = inflater.inflate(R.layout.discount_detail_list, null);
		forListView = (ListView) view1.findViewById(R.id.foodlist_listView1);
		forListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (groupBuyListModel != null && firstVisibleItem + visibleItemCount >= totalItemCount &&
						groupBuyListModel.getPage() < groupBuyListModel.getTotal()) {
					groupBuyPageIndex++;
					getGroupBuyList();
				}
			}
		});
		forListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				groupBuySelectIndex = position;
				Intent intent = new Intent(DiscountList.this, GroupBuyDetail.class);
				intent.putExtra("ID", groupBuyListModel.list.get(position).getActivitiesID());
				startActivity(intent);
			}
		});
		listMainViews.add(view1);

		viewSwitcher.setAdapter(new HJPagerAdapter(listMainViews));

		addToShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer) v.getTag();
				
				BuySaleModel buySaleModel = buySaleListModel.list
						.get(pt);
				 if (buySaleModel.getShopStatus() < 1) {
					 Toast.makeText(DiscountList.this, "商家休息中，无法订单！请换其他商家！", 15)
					  .show(); 
					 return; 
				}
				ShopListItemModel mShop = new ShopListItemModel();
				mShop.setStatus(buySaleModel.getShopStatus());
				mShop.setId(buySaleModel.getTogoID());
				mShop.setName(buySaleModel.getTogoName());
				mShop.setSendMoney(buySaleModel.getSendMoney());
				mShop.setFullFreeMoney(buySaleModel.getFreeSendMoney());
				mShop.setStartSendMoney(buySaleModel.getMinMoney());
				mShop.setLatitude(buySaleModel.getLat());
				mShop.setLongtude(buySaleModel.getLng());
				mShop.setSendDistance(buySaleModel.getSendDistance());
				mShop.setMaxKM(buySaleModel.getMaxKM());
				mShop.setMinKM(buySaleModel.getMinKM());
				mShop.setSendFeeAffKM(buySaleModel.getSendFeeAffKM());
				mShop.setSendFeeOfKM(buySaleModel.getSendFeeOfKM());
				mShop.setStartSendFee(buySaleModel.getStartSendFee());
				

				
				 
				 
				if (buySaleModel.getDistance() > buySaleModel
						.getSendDistance()) {
					Toast.makeText(DiscountList.this,
							"您到商家的距离超出了商家的派送范围，无法下单！请换其他商宄1�7", 15).show();
					return;
				}
				FoodModel model = new FoodModel();
				model.listSpec = new ArrayList<FoodAttrModel>();

				model.setFoodId(buySaleModel.getFoodId());
				model.setFoodName(buySaleModel.getFoodName() + "(资1�7" + buySaleModel.listSpec.get(0).name + ")");
				model.setPrice(buySaleModel.getPrice());
				model.setActivitysType(4);
				model.setActivitysID(buySaleModel.getActivitiesID());

				FoodAttrModel attr = new FoodAttrModel();
				attr.cId = 0;
				attr.count = 1;
				attr.name = model.getFoodName();
				attr.price = model.getPrice();
				model.listSpec.add(attr);

				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.addFoodAttr(
						mShop, model, 0, Integer.parseInt(text.getText().toString()))));
				// setCondition();

			}
		};

		delFromShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int pt = (Integer) v.getTag();
				ShopListItemModel mShop = new ShopListItemModel();
				BuySaleModel buySaleModel = buySaleListModel.list
						.get(pt);
				mShop.setId(buySaleModel.getTogoID());
				mShop.setName(buySaleModel.getTogoName());
				mShop.setSendMoney(buySaleModel.getSendMoney());
				mShop.setFullFreeMoney(buySaleModel.getFreeSendMoney());
				mShop.setStartSendMoney(buySaleModel.getMinMoney());
				mShop.setLatitude(buySaleModel.getLat());
				mShop.setLongtude(buySaleModel.getLng());
				mShop.setSendDistance(buySaleModel.getSendDistance());
				mShop.setMaxKM(buySaleModel.getMaxKM());
				mShop.setMinKM(buySaleModel.getMinKM());
				mShop.setSendFeeAffKM(buySaleModel.getSendFeeAffKM());
				mShop.setSendFeeOfKM(buySaleModel.getSendFeeOfKM());
				mShop.setStartSendFee(buySaleModel.getStartSendFee());
				
				FoodModel model = new FoodModel();
				model.listSpec = new ArrayList<FoodAttrModel>();

				model.setFoodId(buySaleModel.getFoodId());
				model.setFoodName(buySaleModel.getFoodName());
				model.setPrice(buySaleModel.getPrice());
				model.setActivitysType(4);
				model.setActivitysID(buySaleModel.getActivitiesID());
				FoodAttrModel attr = new FoodAttrModel();
				attr.cId = 0;
				attr.count = 1;
				attr.name = model.getFoodName();
				attr.price = model.getPrice();
				model.listSpec.add(attr);

				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.delFoodAttr(
						mShop, model, 0)));
			}
		};
		
		addToShopCartListenerForSeckill = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer) v.getTag();
				
				SeckillModel buySaleModel = seckillListModel.list
						.get(pt);
				 if (buySaleModel.getShopStatus() < 1) {
					 Toast.makeText(DiscountList.this, "商家休息中，无法订单！请换其他商家！", 15)
					  .show(); 
					 return; 
				}
				if (app.shopCartModel
						.getFoodCountInAttr(buySaleModel.getTogoID(),
								buySaleModel.getFoodId(), 0) >= buySaleModel.getStock()) {
					Toast.makeText(DiscountList.this, "已经超出了库存，无法继续秒杀＄1�7", 15)
					  .show(); 
					 return; 
				}
				ShopListItemModel mShop = new ShopListItemModel();
				mShop.setStatus(buySaleModel.getShopStatus());
				mShop.setId(buySaleModel.getTogoID());
				mShop.setName(buySaleModel.getTogoName());
				mShop.setSendMoney(buySaleModel.getSendMoney());
				mShop.setFullFreeMoney(buySaleModel.getFreeSendMoney());
				mShop.setStartSendMoney(buySaleModel.getMinMoney());
				mShop.setLatitude(buySaleModel.getLat());
				mShop.setLongtude(buySaleModel.getLng());
				mShop.setSendDistance(buySaleModel.getSendDistance());
				mShop.setMaxKM(buySaleModel.getMaxKM());
				mShop.setMinKM(buySaleModel.getMinKM());
				mShop.setSendFeeAffKM(buySaleModel.getSendFeeAffKM());
				mShop.setSendFeeOfKM(buySaleModel.getSendFeeOfKM());
				mShop.setStartSendFee(buySaleModel.getStartSendFee());
				

				
				 
				 
				if (buySaleModel.getDistance() > buySaleModel
						.getSendDistance()) {
					Toast.makeText(DiscountList.this,
							"您到商家的距离超出了商家的派送范围，无法下单！请换其他商宄1�7", 15).show();
					return;
				}
				FoodModel model = new FoodModel();
				model.listSpec = new ArrayList<FoodAttrModel>();
				model.setStock(buySaleModel.getStock());
				model.setFoodId(buySaleModel.getFoodId());
				model.setFoodName(buySaleModel.getFoodName());
				model.setPrice(buySaleModel.getPrice());
				model.setActivitysType(2);
				model.setActivitysID(buySaleModel.getActivitiesID());

				FoodAttrModel attr = new FoodAttrModel();
				attr.cId = 0;
				attr.count = 1;
				attr.name = model.getFoodName();
				attr.price = model.getPrice();
				model.listSpec.add(attr);

				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.addFoodAttr(
						mShop, model, 0, Integer.parseInt(text.getText().toString()))));
				// setCondition();

			}
		};

		delFromShopCartListenerForSeckill = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int pt = (Integer) v.getTag();
				ShopListItemModel mShop = new ShopListItemModel();
				SeckillModel buySaleModel = seckillListModel.list
						.get(pt);
				mShop.setId(buySaleModel.getTogoID());
				mShop.setName(buySaleModel.getTogoName());
				mShop.setSendMoney(buySaleModel.getSendMoney());
				mShop.setFullFreeMoney(buySaleModel.getFreeSendMoney());
				mShop.setStartSendMoney(buySaleModel.getMinMoney());
				mShop.setLatitude(buySaleModel.getLat());
				mShop.setLongtude(buySaleModel.getLng());
				mShop.setSendDistance(buySaleModel.getSendDistance());
				mShop.setMaxKM(buySaleModel.getMaxKM());
				mShop.setMinKM(buySaleModel.getMinKM());
				mShop.setSendFeeAffKM(buySaleModel.getSendFeeAffKM());
				mShop.setSendFeeOfKM(buySaleModel.getSendFeeOfKM());
				mShop.setStartSendFee(buySaleModel.getStartSendFee());
				
				FoodModel model = new FoodModel();
				model.listSpec = new ArrayList<FoodAttrModel>();

				model.setFoodId(buySaleModel.getFoodId());
				model.setFoodName(buySaleModel.getFoodName());
				model.setPrice(buySaleModel.getPrice());
				model.setActivitysType(4);
				model.setActivitysID(buySaleModel.getActivitiesID());
				FoodAttrModel attr = new FoodAttrModel();
				attr.cId = 0;
				attr.count = 1;
				attr.name = model.getFoodName();
				attr.price = model.getPrice();
				model.listSpec.add(attr);

				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.delFoodAttr(
						mShop, model, 0)));
			}
		};
		
		
		addToShopCartListenerForLimit = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pt = (Integer) v.getTag();
				
				LimitedModel buySaleModel = limitedListModel.list
						.get(pt);
				 if (buySaleModel.getShopStatus() < 1) {
					 Toast.makeText(DiscountList.this, "商家休息中，无法订单！请换其他商家！", 15)
					  .show(); 
					 return; 
				}
				if (app.shopCartModel
						.getFoodCountInAttr(buySaleModel.getTogoID(),
								buySaleModel.getFoodId(), 0) >= buySaleModel.getStock()) {
					Toast.makeText(DiscountList.this, "已经超出了库存，无法继续限量购买＄1�7", 15)
					  .show(); 
					 return; 
				}
				ShopListItemModel mShop = new ShopListItemModel();
				mShop.setStatus(buySaleModel.getShopStatus());
				mShop.setId(buySaleModel.getTogoID());
				mShop.setName(buySaleModel.getTogoName());
				mShop.setSendMoney(buySaleModel.getSendMoney());
				mShop.setFullFreeMoney(buySaleModel.getFreeSendMoney());
				mShop.setStartSendMoney(buySaleModel.getMinMoney());
				mShop.setLatitude(buySaleModel.getLat());
				mShop.setLongtude(buySaleModel.getLng());
				mShop.setSendDistance(buySaleModel.getSendDistance());
				mShop.setMaxKM(buySaleModel.getMaxKM());
				mShop.setMinKM(buySaleModel.getMinKM());
				mShop.setSendFeeAffKM(buySaleModel.getSendFeeAffKM());
				mShop.setSendFeeOfKM(buySaleModel.getSendFeeOfKM());
				mShop.setStartSendFee(buySaleModel.getStartSendFee());
				

				
				 
				 
				if (buySaleModel.getDistance() > buySaleModel
						.getSendDistance()) {
					Toast.makeText(DiscountList.this,
							"您到商家的距离超出了商家的派送范围，无法下单！请换其他商宄1�7", 15).show();
					return;
				}
				FoodModel model = new FoodModel();
				model.listSpec = new ArrayList<FoodAttrModel>();
				model.setStock(buySaleModel.getStock());
				model.setFoodId(buySaleModel.getFoodId());
				model.setFoodName(buySaleModel.getFoodName());
				model.setPrice(buySaleModel.getPrice());
				model.setActivitysType(3);
				model.setActivitysID(buySaleModel.getActivitiesID());

				FoodAttrModel attr = new FoodAttrModel();
				attr.cId = 0;
				attr.count = 1;
				attr.name = model.getFoodName();
				attr.price = model.getPrice();
				model.listSpec.add(attr);

				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.addFoodAttr(
						mShop, model, 0, Integer.parseInt(text.getText().toString()))));
				// setCondition();

			}
		};

		delFromShopCartListenerForLimit = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int pt = (Integer) v.getTag();
				ShopListItemModel mShop = new ShopListItemModel();
				LimitedModel buySaleModel = limitedListModel.list
						.get(pt);
				mShop.setId(buySaleModel.getTogoID());
				mShop.setName(buySaleModel.getTogoName());
				mShop.setSendMoney(buySaleModel.getSendMoney());
				mShop.setFullFreeMoney(buySaleModel.getFreeSendMoney());
				mShop.setStartSendMoney(buySaleModel.getMinMoney());
				mShop.setLatitude(buySaleModel.getLat());
				mShop.setLongtude(buySaleModel.getLng());
				mShop.setSendDistance(buySaleModel.getSendDistance());
				mShop.setMaxKM(buySaleModel.getMaxKM());
				mShop.setMinKM(buySaleModel.getMinKM());
				mShop.setSendFeeAffKM(buySaleModel.getSendFeeAffKM());
				mShop.setSendFeeOfKM(buySaleModel.getSendFeeOfKM());
				mShop.setStartSendFee(buySaleModel.getStartSendFee());
				
				FoodModel model = new FoodModel();
				model.listSpec = new ArrayList<FoodAttrModel>();

				model.setFoodId(buySaleModel.getFoodId());
				model.setFoodName(buySaleModel.getFoodName());
				model.setPrice(buySaleModel.getPrice());
				model.setActivitysType(4);
				model.setActivitysID(buySaleModel.getActivitiesID());
				FoodAttrModel attr = new FoodAttrModel();
				attr.cId = 0;
				attr.count = 1;
				attr.name = model.getFoodName();
				attr.price = model.getPrice();
				model.listSpec.add(attr);

				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.delFoodAttr(
						mShop, model, 0)));
			}
		};

	}

	public void getBuySaleList() {// 获取买撘资1�7
		if (app.useLocation == null) {
			lat = 0.0;
			lng = 0.0;
		} else {
			lat = app.useLocation.getLat();
			lng = app.useLocation.getLon();
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(buySalePageIndex));
		localHashMap.put("cityid", app.mSection.SectionID);
		localHashMap.put("lat", String.valueOf(lat));

		localHashMap.put("lng", String.valueOf(lng));
		localHashMap.put("pagesize", "10");

		localHttpManager = new HttpManager(DiscountList.this,
				DiscountList.this, "/Android/buysale.aspx?", localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}

	public void getSeckillList() {// 获取秒杀
		if (app.useLocation == null) {
			lat = 0.0;
			lng = 0.0;
		} else {
			lat = app.useLocation.getLat();
			lng = app.useLocation.getLon();
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(seckillPageIndex));
		localHashMap.put("cityid", app.mSection.SectionID);
		localHashMap.put("lat", String.valueOf(lat));

		localHashMap.put("lng", String.valueOf(lng));
		localHashMap.put("pagesize", "10");

		localHttpManager = new HttpManager(DiscountList.this,
				DiscountList.this, "/Android/GetSeckillList.aspx?",
				localHashMap, 2, 2);
		localHttpManager.getRequeest();
	}

	public void getLimitedList() {// 获取限量数据
		if (app.useLocation == null) {
			lat = 0.0;
			lng = 0.0;
		} else {
			lat = app.useLocation.getLat();
			lng = app.useLocation.getLon();
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(limitPageIndex));
		localHashMap.put("cityid", app.mSection.SectionID);
		localHashMap.put("lat", String.valueOf(lat));

		localHashMap.put("lng", String.valueOf(lng));
		localHashMap.put("pagesize", "10");

		localHttpManager = new HttpManager(DiscountList.this,
				DiscountList.this, "/Android/GetLimitList.aspx?", localHashMap,
				2, 3);
		localHttpManager.getRequeest();
	}

	public void getGroupBuyList() {// 获取买撘赠团贄1�7
		if (app.useLocation == null) {
			lat = 0.0;
			lng = 0.0;
		} else {
			lat = app.useLocation.getLat();
			lng = app.useLocation.getLon();
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(groupBuyPageIndex));
		localHashMap.put("cityid", app.mSection.SectionID);
		localHashMap.put("lat", String.valueOf(lat));

		localHashMap.put("lng", String.valueOf(lng));
		localHashMap.put("pagesize", "10");

		localHttpManager = new HttpManager(DiscountList.this,
				DiscountList.this, "/Android/GetGroupList.aspx?", localHashMap,
				2, 4);
		localHttpManager.getRequeest();
	}
	
	

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			switch (tag) {
			case 1:
				if (buySaleListModel == null) {
					buySaleListModel = new BuySaleListModel();
				}
				buySaleListModel.stringToBean((String) paramObject);
				if (buySaleListModel.list.size() > 0) {
					msg.what = UIHandler.GET_BUY_SALE_SUCESS;
				}else{
					msg.what = UIHandler.NO_BUY_SALE;
				}
				
				break;
			case 2:
				if (seckillListModel == null) {
					seckillListModel = new SeckillListModel();
				}
				seckillListModel.stringToBean((String) paramObject);
				if (seckillListModel.list.size() > 0) {
					msg.what = UIHandler.GET_BUY_SECKILL_SUCESS;
				}else{
					msg.what = UIHandler.NO_SECKILL;
				}
				break;
			case 3:
				if (limitedListModel == null) {
					limitedListModel = new LimitedListModel();
				}
				limitedListModel.stringToBean((String) paramObject);
				if (limitedListModel.list.size() > 0) {
					msg.what = UIHandler.GET_BUY_LIMITED_SUCESS;
				}else{
					msg.what = UIHandler.NO_LIMITED;
				}
				break;
			case 4:
				if (groupBuyListModel == null) {
					groupBuyListModel = new GroupBuyListModel();
				}
				groupBuyListModel.stringToBean((String) paramObject);
				if (groupBuyListModel.list.size() > 0) {
					msg.what = UIHandler.GET_GROUP_BUY_SUCESS;
				}else{
					msg.what = UIHandler.NO_GROUP_BUY;
				}
				break;
			default:
				break;
			}

		} else {
			msg.what = UIHandler.NET_ERROR;
		}
		mHandler.sendMessage(msg);
	}

	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, this.dialogStr);

			DialogInterface.OnKeyListener okl = new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface paramDialogInterface,
						int paramInt, KeyEvent paramKeyEvent) {
					// 取消加载
					if ((paramInt == 4)
							&& (DiscountList.this.localHttpManager != null)) {
						DiscountList.this.localHttpManager.cancelHttpRequest();
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
		public static final int NO_GROUP_BUY = -5;
		public static final int GET_GROUP_BUY_SUCESS = 5;
		public static final int NO_BUY_SALE = -2;
		public static final int NO_SECKILL = -3;
		public static final int NO_LIMITED = -4;
		public static final int GET_BUY_LIMITED_SUCESS = 3;
		public static final int GET_BUY_SECKILL_SUCESS = 2;
		public static final int GET_BUY_SALE_SUCESS = 1;
		public static final int NET_ERROR = -1;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(322);
			switch (msg.what) {
			case NET_ERROR:
				break;
			case GET_BUY_SALE_SUCESS:
				if (buySaleListAdapter == null) {
					buySaleListAdapter = new BuySaleListAdapter();
					oneListView.setAdapter(buySaleListAdapter);
					// buySaleListAdapter.notify();
				} else {
					buySaleListAdapter.notifyDataSetChanged();
				}
				break;
			case NO_BUY_SALE:
				Toast.makeText(DiscountList.this, "附近没有买搭增活动！敬请期待＄1�7", 15).show();
				break;
			case GET_BUY_SECKILL_SUCESS:
				if (seckillListAdapter == null) {
					seckillListAdapter = new SeckillListAdapter();
					twoListView.setAdapter(seckillListAdapter);
					// buySaleListAdapter.notify();
				} else {
					seckillListAdapter.notifyDataSetChanged();
				}
				break;
			case NO_SECKILL:
				Toast.makeText(DiscountList.this, "附近没有秒杀活动！敬请期待！", 15).show();
				break;
			case GET_BUY_LIMITED_SUCESS:
				if (limitedListAdapter == null) {
					limitedListAdapter = new LimitedListAdapter();
					theListView.setAdapter(limitedListAdapter);
					// buySaleListAdapter.notify();
				} else {
					limitedListAdapter.notifyDataSetChanged();
				}
				break;
			case NO_LIMITED:
				Toast.makeText(DiscountList.this, "附近没有限量活动！敬请期待！", 15).show();
				break;
			case GET_GROUP_BUY_SUCESS:
				if (groupBuyListAdapter == null) {
					groupBuyListAdapter = new GroupBuyListAdapter();
					forListView.setAdapter(groupBuyListAdapter);
					// buySaleListAdapter.notify();
				} else {
					groupBuyListAdapter.notifyDataSetChanged();
				}
				break;
			case NO_GROUP_BUY:
				Toast.makeText(DiscountList.this, "附近没有团购活动！敬请期待！", 15).show();
				break;
			}
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

	class BuySaleListHolder {
		TextView tv_foodName;
		TextView tv_foodInfo;
		RelativeLayout iv_foodImg;
		TextView tv_foodPrice;
		TextView tv_Point;
		public TextView tvNum;
		public Button btnAdd;
		public Button btnDel;

		BuySaleListHolder() {
		}
	}

	// 买搭墄1�7
	class BuySaleListAdapter extends BaseAdapter {
		BuySaleListAdapter() {
		}

		@Override
		public int getCount() {
			return buySaleListModel.list.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return buySaleListModel.list.get(paramInt);
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

			BuySaleListHolder foodlistHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.sale_item, null);
				foodlistHolder = new BuySaleListHolder();
				foodlistHolder.iv_foodImg = (RelativeLayout) convertView
						.findViewById(R.id.rl_img);
				foodlistHolder.tv_foodInfo = (TextView) convertView
						.findViewById(R.id.tv_foodinfo);
				foodlistHolder.tv_foodInfo.setVisibility(View.VISIBLE);
				foodlistHolder.tv_foodName = (TextView) convertView
						.findViewById(R.id.tv_foodname);
				foodlistHolder.tv_foodPrice = (TextView) convertView
						.findViewById(R.id.tv_foodprice);

				foodlistHolder.tv_Point = (TextView) convertView
						.findViewById(R.id.tv_point);
				// foodlistHolder.tv_Point.setVisibility(View.VISIBLE);

				foodlistHolder.tvNum = (TextView) convertView
						.findViewById(R.id.tv_foodcount);

				foodlistHolder.btnAdd = (Button) convertView
						.findViewById(R.id.btn_number_plus);
				foodlistHolder.btnDel = (Button) convertView
						.findViewById(R.id.btn_number_minus);
				foodlistHolder.btnAdd.setOnClickListener(addToShopCartListener);
				foodlistHolder.btnDel
						.setOnClickListener(delFromShopCartListener);

				convertView.setTag(foodlistHolder);
			} else {
				foodlistHolder = (BuySaleListHolder) convertView.getTag();
			}

			BuySaleModel model = buySaleListModel.list.get(paramInt);

			if (model != null) {
				foodlistHolder.tv_foodName.setText(model.getFoodName());
				foodlistHolder.tv_foodInfo.setText(model.getDisc() + "\r\n赠品＄1�7"
						+ model.listSpec.get(0).name + "-赠品价格＄1�7"
						+ model.listSpec.get(0).price);
				foodlistHolder.tv_foodPrice.setText("价格＄1�7"
						+ String.valueOf(model.getPrice()));

				foodlistHolder.tv_Point.setText(model.getStartTime() + "资1�7"
						+ model.getEndTime() + "歄1�7");

				// Drawable drawable = new BitmapDrawable(getBitmap(model));
				foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
				if (model.getImageNetPath() != null
						&& model.getImageNetPath().length() > 18) {// http://*.*.*/*.jpg
					// 有网络地坄1�7，并且没有加入下载，那么加入
					// model.isDowload = true;
					foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
					app.mLoadImage.addTask(model.getImageNetPath(),
							foodlistHolder.iv_foodImg, R.drawable.icon);
				} else {
					foodlistHolder.iv_foodImg
							.setBackgroundResource(R.drawable.icon);
				}

				foodlistHolder.tvNum.setTag(paramInt);

				foodlistHolder.tvNum.setText(String.valueOf(app.shopCartModel
						.getFoodCountInAttr(model.getTogoID(),
								model.getFoodId(), 0)));

				foodlistHolder.btnAdd.setTag(paramInt);
				foodlistHolder.btnAdd.setTag(R.id.tab_label,
						foodlistHolder.tvNum);

				foodlistHolder.btnDel.setTag(paramInt);

				foodlistHolder.btnDel.setTag(R.id.tab_label,
						foodlistHolder.tvNum);

			} else {
			}

			return convertView;
		}
	}
	
	class SeckillListHolder {
		TextView tv_foodName;
		TextView tv_foodInfo;
		RelativeLayout iv_foodImg;
		TextView tv_foodPrice;
		TextView tv_Point;
		public TextView tvNum;
		public Button btnAdd;
		public Button btnDel;

		SeckillListHolder() {
		}
	}

	// 买搭墄1�7
	class SeckillListAdapter extends BaseAdapter {
		SeckillListAdapter() {
		}

		@Override
		public int getCount() {
			return seckillListModel.list.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return seckillListModel.list.get(paramInt);
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

			SeckillListHolder foodlistHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.sale_item, null);
				foodlistHolder = new SeckillListHolder();
				foodlistHolder.iv_foodImg = (RelativeLayout) convertView
						.findViewById(R.id.rl_img);
				foodlistHolder.tv_foodInfo = (TextView) convertView
						.findViewById(R.id.tv_foodinfo);
				foodlistHolder.tv_foodInfo.setVisibility(View.VISIBLE);
				foodlistHolder.tv_foodName = (TextView) convertView
						.findViewById(R.id.tv_foodname);
				foodlistHolder.tv_foodPrice = (TextView) convertView
						.findViewById(R.id.tv_foodprice);

				foodlistHolder.tv_Point = (TextView) convertView
						.findViewById(R.id.tv_point);

				foodlistHolder.tvNum = (TextView) convertView
						.findViewById(R.id.tv_foodcount);

				foodlistHolder.btnAdd = (Button) convertView
						.findViewById(R.id.btn_number_plus);
				foodlistHolder.btnDel = (Button) convertView
						.findViewById(R.id.btn_number_minus);
				foodlistHolder.btnAdd.setOnClickListener(addToShopCartListenerForSeckill);
				foodlistHolder.btnDel
						.setOnClickListener(delFromShopCartListenerForSeckill);

				convertView.setTag(foodlistHolder);
			} else {
				foodlistHolder = (SeckillListHolder) convertView.getTag();
			}

			SeckillModel model = seckillListModel.list.get(paramInt);

			if (model != null) {
				foodlistHolder.tv_foodName.setText(model.getFoodName());
				foodlistHolder.tv_foodInfo.setText(String.format("原价＄1�7%.2f\r\n当前库存＄1�7%d", 
						model.getoPrice(), model.getStock()));
				foodlistHolder.tv_foodPrice.setText("秒杀价："
						+ String.valueOf(model.getPrice()));

				foodlistHolder.tv_Point.setText(model.getStartTime() + "资1�7"
						+ model.getEndTime() + "歄1�7");

				// Drawable drawable = new BitmapDrawable(getBitmap(model));
				foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
				if (model.getImageNetPath() != null
						&& model.getImageNetPath().length() > 18) {// http://*.*.*/*.jpg
					// 有网络地坄1�7，并且没有加入下载，那么加入
					// model.isDowload = true;
					foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
					app.mLoadImage.addTask(model.getImageNetPath(),
							foodlistHolder.iv_foodImg, R.drawable.icon);
				} else {
					foodlistHolder.iv_foodImg
							.setBackgroundResource(R.drawable.icon);
				}

				foodlistHolder.tvNum.setTag(paramInt);

				foodlistHolder.tvNum.setText(String.valueOf(app.shopCartModel
						.getFoodCountInAttr(model.getTogoID(),
								model.getFoodId(), 0)));

				foodlistHolder.btnAdd.setTag(paramInt);
				foodlistHolder.btnAdd.setTag(R.id.tab_label,
						foodlistHolder.tvNum);

				foodlistHolder.btnDel.setTag(paramInt);

				foodlistHolder.btnDel.setTag(R.id.tab_label,
						foodlistHolder.tvNum);

			} else {
			}

			return convertView;
		}
	}
	
	class LimitedListHolder {
		TextView tv_foodName;
		TextView tv_foodInfo;
		RelativeLayout iv_foodImg;
		TextView tv_foodPrice;
		TextView tv_Point;
		public TextView tvNum;
		public Button btnAdd;
		public Button btnDel;

		LimitedListHolder() {
		}
	}

	// 买搭墄1�7
	class LimitedListAdapter extends BaseAdapter {
		LimitedListAdapter() {
		}

		@Override
		public int getCount() {
			return limitedListModel.list.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return limitedListModel.list.get(paramInt);
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

			LimitedListHolder foodlistHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.sale_item, null);
				foodlistHolder = new LimitedListHolder();
				foodlistHolder.iv_foodImg = (RelativeLayout) convertView
						.findViewById(R.id.rl_img);
				foodlistHolder.tv_foodInfo = (TextView) convertView
						.findViewById(R.id.tv_foodinfo);
				foodlistHolder.tv_foodInfo.setVisibility(View.VISIBLE);
				foodlistHolder.tv_foodName = (TextView) convertView
						.findViewById(R.id.tv_foodname);
				foodlistHolder.tv_foodPrice = (TextView) convertView
						.findViewById(R.id.tv_foodprice);

				foodlistHolder.tv_Point = (TextView) convertView
						.findViewById(R.id.tv_point);
				// foodlistHolder.tv_Point.setVisibility(View.VISIBLE);

				foodlistHolder.tvNum = (TextView) convertView
						.findViewById(R.id.tv_foodcount);

				foodlistHolder.btnAdd = (Button) convertView
						.findViewById(R.id.btn_number_plus);
				foodlistHolder.btnDel = (Button) convertView
						.findViewById(R.id.btn_number_minus);
				foodlistHolder.btnAdd.setOnClickListener(addToShopCartListenerForLimit);
				foodlistHolder.btnDel
						.setOnClickListener(delFromShopCartListenerForLimit);

				convertView.setTag(foodlistHolder);
			} else {
				foodlistHolder = (LimitedListHolder) convertView.getTag();
			}

			LimitedModel model = limitedListModel.list.get(paramInt);

			if (model != null) {
				foodlistHolder.tv_foodName.setText(model.getFoodName());
				foodlistHolder.tv_foodInfo.setText(String.format("原价＄1�7%.2f\r\n当前库存＄1�7%d", 
						model.getoPrice(), model.getStock()));
				foodlistHolder.tv_foodPrice.setText("限量价："
						+ String.valueOf(model.getPrice()));

				foodlistHolder.tv_Point.setText(model.getStartTime() + "资1�7"
						+ model.getEndTime() + "歄1�7");

				// Drawable drawable = new BitmapDrawable(getBitmap(model));
				foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
				if (model.getImageNetPath() != null
						&& model.getImageNetPath().length() > 18) {// http://*.*.*/*.jpg
					// 有网络地坄1�7，并且没有加入下载，那么加入
					// model.isDowload = true;
					foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
					app.mLoadImage.addTask(model.getImageNetPath(),
							foodlistHolder.iv_foodImg, R.drawable.icon);
				} else {
					foodlistHolder.iv_foodImg
							.setBackgroundResource(R.drawable.icon);
				}

				foodlistHolder.tvNum.setTag(paramInt);

				foodlistHolder.tvNum.setText(String.valueOf(app.shopCartModel
						.getFoodCountInAttr(model.getTogoID(),
								model.getFoodId(), 0)));

				foodlistHolder.btnAdd.setTag(paramInt);
				foodlistHolder.btnAdd.setTag(R.id.tab_label,
						foodlistHolder.tvNum);

				foodlistHolder.btnDel.setTag(paramInt);

				foodlistHolder.btnDel.setTag(R.id.tab_label,
						foodlistHolder.tvNum);

			} else {
			}

			return convertView;
		}
	}
	
	class GroupBuyListHolder {
		TextView tv_foodName;
		TextView tv_foodInfo;
		RelativeLayout iv_foodImg;
		TextView tv_foodPrice;
		TextView tv_Point;
		

		GroupBuyListHolder() {
		}
	}

	// 买搭墄1�7
	class GroupBuyListAdapter extends BaseAdapter {
		GroupBuyListAdapter() {
		}

		@Override
		public int getCount() {
			return groupBuyListModel.list.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return groupBuyListModel.list.get(paramInt);
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

			GroupBuyListHolder foodlistHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.group_buy_item, null);
				foodlistHolder = new GroupBuyListHolder();
				foodlistHolder.iv_foodImg = (RelativeLayout) convertView
						.findViewById(R.id.rl_img);
				foodlistHolder.tv_foodInfo = (TextView) convertView
						.findViewById(R.id.tv_foodinfo);
				foodlistHolder.tv_foodInfo.setVisibility(View.VISIBLE);
				foodlistHolder.tv_foodName = (TextView) convertView
						.findViewById(R.id.tv_foodname);
				foodlistHolder.tv_foodPrice = (TextView) convertView
						.findViewById(R.id.tv_foodprice);

				foodlistHolder.tv_Point = (TextView) convertView
						.findViewById(R.id.tv_point);
				// foodlistHolder.tv_Point.setVisibility(View.VISIBLE);

				

				convertView.setTag(foodlistHolder);
			} else {
				foodlistHolder = (GroupBuyListHolder) convertView.getTag();
			}

			GroupBuyModel model = groupBuyListModel.list.get(paramInt);

			if (model != null) {
				foodlistHolder.tv_foodName.setText(model.getFoodName());
				foodlistHolder.tv_foodInfo.setText(String.format("原价＄1�7%.2f\r\n已参加人数：%d", 
						model.getoPrice(), model.getJoinNum()));
				foodlistHolder.tv_foodPrice.setText("团购价："
						+ String.valueOf(model.getPrice()));

				foodlistHolder.tv_Point.setText(model.getStartTime() + "资1�7"
						+ model.getEndTime() + "歄1�7");

				// Drawable drawable = new BitmapDrawable(getBitmap(model));
				foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
				if (model.getImageNetPath() != null
						&& model.getImageNetPath().length() > 18) {// http://*.*.*/*.jpg
					// 有网络地坄1�7，并且没有加入下载，那么加入
					// model.isDowload = true;
					foodlistHolder.iv_foodImg.setTag(model.getImageNetPath());
					app.mLoadImage.addTask(model.getImageNetPath(),
							foodlistHolder.iv_foodImg, R.drawable.icon);
				} else {
					foodlistHolder.iv_foodImg
							.setBackgroundResource(R.drawable.icon);
				}

				

			} else {
			}

			return convertView;
		}
	}

}
