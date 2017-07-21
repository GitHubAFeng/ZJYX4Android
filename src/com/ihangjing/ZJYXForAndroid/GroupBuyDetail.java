package com.ihangjing.ZJYXForAndroid;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.Model.FoodAttrModel;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.GroupBuyAttrModel;
import com.ihangjing.Model.GroupBuyModel;
import com.ihangjing.Model.ShopListItemModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class GroupBuyDetail extends HjActivity implements HttpRequestListener{
	
	private double lat;
	private double lng;
	private int activityID;
	private HttpManager localHttpManager;
	private TextView tvTitle;
	private LinearLayout llFoodAttr;
	private TextView tvFoodName;
	private ImageView ivImage;
	private TextView tvFoodPriceCurrent;
	private TextView tvFoodDis;
	private TextView tvFoodNot;
	
	UIHandler mHandler = new UIHandler();
	private GroupBuyModel groupBuyModel;
	private OnClickListener delFromShopCartListener;
	private OnClickListener addToShopCartListener;
	private GroupBuyAttrAdapter groupBuyAttrListAdapter;
	public ShopListItemModel mShop;

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.group_buy_detail);
		Bundle extern = getIntent().getExtras();
		if (extern != null) {
			activityID = extern.getInt("ID");
		}
		groupBuyAttrListAdapter = new GroupBuyAttrAdapter();
		Button btnBack = (Button)findViewById(R.id.title_bar_back_btn);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Button btnGotoShopCart = (Button)findViewById(R.id.title_bar_right_btn);
		btnGotoShopCart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent localIntent = new Intent("com.ihangjing.common.tap3");
				app.sendBroadcast(localIntent);	
				finish();
			}
		});
		
		tvTitle = (TextView)findViewById(R.id.title_bar_content_tv);
		tvTitle.setText("活动详情");
		
		llFoodAttr = (LinearLayout) findViewById(R.id.lv_attr);
		tvFoodName = (TextView) findViewById(R.id.tv_name);
		ivImage = (ImageView) findViewById(R.id.iv_img);

		tvFoodPriceCurrent = (TextView) findViewById(R.id.tv_sub_total);

		tvFoodDis = (TextView) findViewById(R.id.tv_pro_des_content);

		tvFoodNot = (TextView) findViewById(R.id.tv_pro_notice_content);
		
		addToShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if (groupBuyModel.getShopStatus() < 1) {
					 Toast.makeText(GroupBuyDetail.this, "商家休息中，无法订单！请换其他商家！", 15)
					  .show(); 
					 return; 
				}
				
				
				 if (app.shopCartModel
							.getFoodCountInAttr(groupBuyModel.getTogoID(),
									groupBuyModel.getFoodId(), 0) >= groupBuyModel.getStock()) {
						Toast.makeText(GroupBuyDetail.this, "已经超出了库存，无法继续团购＄1�7", 15)
						  .show(); 
						 return; 
					}
				
				 
				 
				if (groupBuyModel.getDistance() > groupBuyModel
						.getSendDistance()) {
					Toast.makeText(GroupBuyDetail.this,
							"您到商家的距离超出了商家的派送范围，无法下单！请换其他商宄1�7", 15).show();
					return;
				}
				FoodModel model = new FoodModel();
				model.listSpec = new ArrayList<FoodAttrModel>();

				model.setFoodId(groupBuyModel.getFoodId());
				model.setFoodName(groupBuyModel.getFoodName());
				model.setPrice(groupBuyModel.getPrice());
				model.setActivitysType(1);
				model.setActivitysID(groupBuyModel.getActivitiesID());

				FoodAttrModel attr = new FoodAttrModel();
				attr.cId = 0;
				attr.count = 1;
				attr.name = model.getFoodName();
				attr.price = model.getPrice();
				model.listSpec.add(attr);

				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.addFoodAttr(
						mShop, model, 0, Integer.parseInt(text.getText().toString()))));
				tvFoodPriceCurrent.setText(String.format("合计：￥%.2f兄1�7",
						app.shopCartModel.getFoodPrice(mShop, groupBuyModel
								.getFoodId())));
				// setCondition();

			}
		};

		delFromShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int pt = (Integer) v.getTag();
				
				
				FoodModel model = new FoodModel();
				model.listSpec = new ArrayList<FoodAttrModel>();

				model.setFoodId(groupBuyModel.getFoodId());
				model.setFoodName(groupBuyModel.getFoodName());
				model.setPrice(groupBuyModel.getPrice());
				model.setActivitysType(4);
				model.setActivitysID(groupBuyModel.getActivitiesID());
				FoodAttrModel attr = new FoodAttrModel();
				attr.cId = 0;
				attr.count = 1;
				attr.name = model.getFoodName();
				attr.price = model.getPrice();
				model.listSpec.add(attr);

				TextView text = (TextView) v.getTag(R.id.tab_label);

				text.setText(String.valueOf(app.shopCartModel.delFoodAttr(
						mShop, model, 0)));
				tvFoodPriceCurrent.setText(String.format("合计：￥%.2f兄1�7",
						app.shopCartModel.getFoodPrice(mShop, groupBuyModel
								.getFoodId())));
			}
		};
		
		getGroupBuyDetail();
	}
	
	public void getGroupBuyDetail() {// 获取买撘赠团贄1�7
		showDialog(322);
		if (app.useLocation == null) {
			lat = 0.0;
			lng = 0.0;
		} else {
			lat = app.useLocation.getLat();
			lng = app.useLocation.getLon();
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("cityid", app.mSection.SectionID);
		localHashMap.put("lat", String.valueOf(lat));

		localHashMap.put("lng", String.valueOf(lng));
		localHashMap.put("dataid", String.valueOf(activityID));

		localHttpManager = new HttpManager(GroupBuyDetail.this,
				GroupBuyDetail.this, "/Android/GetGroupDetail.aspx?", localHashMap,
				2, 1);
		localHttpManager.getRequeest();
	}
	
	private void setAdapter() {
		llFoodAttr.removeAllViews();
		int i = groupBuyAttrListAdapter.getCount();
		int j = 0;


		while (true) {
			if (j >= i) {
				return;
			}
			View localView = this.groupBuyAttrListAdapter.getView(j, null, null);
			llFoodAttr.addView(localView);
			j += 1;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int paramInt) {
		Dialog dialog = null;

		if (paramInt == 322) {
			dialog = OtherManager.createProgressDialog(this, "记在数据中，请稍各1�7...");

			DialogInterface.OnKeyListener okl = new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface paramDialogInterface,
						int paramInt, KeyEvent paramKeyEvent) {
					// 取消加载
					if ((paramInt == 4)
							&& (GroupBuyDetail.this.localHttpManager != null)) {
						GroupBuyDetail.this.localHttpManager.cancelHttpRequest();
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
			switch (tag) {
			case 1:
				groupBuyModel = new GroupBuyModel();
				
				try {
					JSONObject jsonObject = new JSONObject((String)paramObject);
					groupBuyModel.JsonToBean(jsonObject, 1);
					
					mShop = new ShopListItemModel();
					mShop.setStatus(groupBuyModel.getShopStatus());
					mShop.setId(groupBuyModel.getTogoID());
					mShop.setName(groupBuyModel.getTogoName());
					mShop.setSendMoney(groupBuyModel.getSendMoney());
					mShop.setFullFreeMoney(groupBuyModel.getFreeSendMoney());
					mShop.setStartSendMoney(groupBuyModel.getMinMoney());
					mShop.setLatitude(groupBuyModel.getLat());
					mShop.setLongtude(groupBuyModel.getLng());
					mShop.setSendDistance(groupBuyModel.getSendDistance());
					mShop.setMaxKM(groupBuyModel.getMaxKM());
					mShop.setMinKM(groupBuyModel.getMinKM());
					mShop.setSendFeeAffKM(groupBuyModel.getSendFeeAffKM());
					mShop.setSendFeeOfKM(groupBuyModel.getSendFeeOfKM());
					mShop.setStartSendFee(groupBuyModel.getStartSendFee());
					
					msg.what = UIHandler.GET_DETAIL_SUCESS;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					msg.what = UIHandler.NET_ERROR;
					e.printStackTrace();
				}
				
				break;

			default:
				break;
			}
		}else{
			msg.what = UIHandler.NET_ERROR;
		}
		mHandler.sendMessage(msg);
	}
	
	private class UIHandler extends Handler {
		
		public static final int GET_DETAIL_SUCESS = 1;
		public static final int NET_ERROR = -1;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(322);
			switch (msg.what) {
			case NET_ERROR:
				Toast.makeText(GroupBuyDetail.this, "网络或数据错误，请稍后再试！", 15).show();
				break;
			case GET_DETAIL_SUCESS:
				tvFoodDis.setText(groupBuyModel.getDisc());
				tvFoodNot.setText(groupBuyModel.getNotice());
				tvFoodName.setText(groupBuyModel.getFoodName());
				// ivImage.setTag(foodModel.getImageNetPath());
				if (groupBuyModel.getImageNetPath() != null
						&& !groupBuyModel.getImageNetPath().equals("")) {
					Bitmap bitmap = app.mLoadImage.getBitmap(groupBuyModel
							.getImageNetPath());
					if (bitmap != null) {
						Drawable drawable = new BitmapDrawable(bitmap);
						ivImage.setBackgroundDrawable(drawable);
					} else {
						ivImage.setBackgroundResource(R.drawable.icon);
					}
				} else {
					ivImage.setBackgroundResource(R.drawable.icon);
				}

				setAdapter();
				tvFoodPriceCurrent.setText(String.format("合计：￥%.2f兄1�7",
						app.shopCartModel.getFoodPrice(mShop, groupBuyModel
								.getFoodId())));
				break;
			}
		}
	}
	
	class GroupBuyAttrView {
		TextView tvAttr;
		TextView tvNum;
		Button btnAdd;
		Button btnDel;
	}

	class GroupBuyAttrAdapter extends BaseAdapter {
		GroupBuyAttrView listHolder;
		LayoutInflater inflater;

		GroupBuyAttrAdapter() {
		}

		@Override
		public int getCount() {
			if (groupBuyModel == null) {
				return 0;
			}
			return groupBuyModel.listSpec.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return groupBuyModel.listSpec.get(paramInt);
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
					inflater = LayoutInflater.from(GroupBuyDetail.this);
				}
				paramView = this.inflater
						.inflate(R.layout.group_buy_attr_item, null);

				listHolder = new GroupBuyAttrView();

				listHolder.tvAttr = (TextView) paramView
						.findViewById(R.id.addorder_foodname);
				listHolder.tvNum = (TextView) paramView
						.findViewById(R.id.tv_foodcount);
				/*listHolder.tvNum
						.setOnFocusChangeListener(new View.OnFocusChangeListener() {

							@Override
							public void onFocusChange(View arg0, boolean arg1) {
								// TODO Auto-generated method stub

								if (arg1) {
									nowEdit = (EditText) arg0;
								}
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
										"合计：￥%.2f兄1�7", app.shopCartModel
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
				listHolder = (GroupBuyAttrView) paramView.getTag();
			}

			GroupBuyAttrModel model = groupBuyModel.listSpec.get(paramInt);

			if (model != null) {

				listHolder.tvNum.setTag(paramInt);
				listHolder.tvAttr.setText(model.name + "  [ￄ1�7" + model.price
						+ "元]");
				listHolder.tvNum.setText(String.valueOf(app.shopCartModel
						.getFoodCountInAttr(mShop, groupBuyModel.getFoodId(), model.cId)));
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
