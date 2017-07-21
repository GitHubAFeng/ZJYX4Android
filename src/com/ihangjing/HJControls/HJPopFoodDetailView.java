package com.ihangjing.HJControls;

import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.FoodSizeModel;
import com.ihangjing.Model.FsgShopDetailModel;
import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.common.HjActivity;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HJPopFoodDetailView extends HJPopViewBase {
	FoodModel foodModel;
	FoodAttrAdapter listFoodAttrAdapter;
	EasyEatApplication app;
	FsgShopDetailModel shopmodeDetailModel;
	private OnClickListener addToShopCartListener;
	private OnClickListener delFromShopCartListener;
	public HJPopFoodDetailView(Context context, RelativeLayout parent,
			int wight, int height, int belowid, FoodModel food, FsgShopDetailModel shop) {
		super(context, parent, wight, height, belowid);
		// TODO Auto-generated constructor stub
		foodModel = food;
		shopmodeDetailModel = shop;
		listFoodAttrAdapter = new FoodAttrAdapter();
		addToShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shopmodeDetailModel.getStatus() < 1) {
					Toast.makeText(mContext, "商家休息中，无法订单！请换其他商家！", 15)
							.show();
					return;
				}
				if (shopmodeDetailModel.getDistance() > shopmodeDetailModel
						.getSendDistance()) {
					Toast.makeText(mContext,
							"您到商家的距离超出了商家的派送范围，无法下单！请换其他商家", 15).show();
					return;
				}
				TextView text = (TextView) v.getTag(R.id.tab_label);
				int pt = (Integer) v.getTag();
				if (foodModel.listMake.size() > 0) {//分类的额外项目 单选
					HJPopFoodAttrDetailView popFoodAttrDetailView = new HJPopFoodAttrDetailView(
							mContext, parentLayout, HJPopFoodDetailView.this.wight, HJPopFoodDetailView.this.height
							, belowID, foodModel, shopmodeDetailModel, 0, null, 
							Integer.parseInt(text.getText().toString()), pt);
					popFoodAttrDetailView.Show();
					HJPopFoodDetailView.this.Close();
					return;
				}else if(foodModel.listSpec.size() > 0){//商品的额外项目多选
					HJPopFoodAttrDetailView popFoodAttrDetailView = new HJPopFoodAttrDetailView(
							mContext, parentLayout, HJPopFoodDetailView.this.wight, HJPopFoodDetailView.this.height
							, belowID, foodModel, shopmodeDetailModel, 1, null, 
							Integer.parseInt(text.getText().toString()), pt);
					popFoodAttrDetailView.Show();
					HJPopFoodDetailView.this.Close();
					return;
				}
				//只有规格大小
				app.mShop.setSendMoney(shopmodeDetailModel.getSendmoney());
				app.mShop.setFullFreeMoney(shopmodeDetailModel
						.getFreeSendMoney());
				app.mShop.setStartSendMoney(shopmodeDetailModel.getMinMoney());
				app.mShop.setLatitude(shopmodeDetailModel.getlat());
				app.mShop.setLongtude(shopmodeDetailModel.getlng());
				app.mShop
						.setSendDistance(shopmodeDetailModel.getSendDistance());
				app.mShop.setMaxKM(shopmodeDetailModel.getMaxKM());
				app.mShop.setMinKM(shopmodeDetailModel.getMinKM());
				app.mShop
						.setSendFeeAffKM(shopmodeDetailModel.getSendFeeAffKM());
				app.mShop.setSendFeeOfKM(shopmodeDetailModel.getSendFeeOfKM());
				app.mShop
						.setStartSendFee(shopmodeDetailModel.getStartSendFee());
				
				
				
				app.shopCartModel.addFoodAttr(app.mShop, foodModel, pt,  Integer.parseInt(text.getText().toString()));
				//text.setText(String.valueOf(app.shopCartModel.addFoodAttr(app.mShop, foodModel, pt)));
				HJPopFoodDetailView.this.Close();
				//setCondition();
			}
		};

		delFromShopCartListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//nowEdit = null;
				int pt = (Integer) v.getTag();
				TextView text = (TextView) v.getTag(R.id.tab_label);
				/*text.setText(String.valueOf(app.shopCartModel.delFoodAttr(
						app.mShop, foodListModel[index].list.get(pt), 0)));*/
				//setCondition();
			}
		};
		app = ((HjActivity)context).app;
		this.setGravity(Gravity.CENTER);
		this.setOrientation(VERTICAL);
		setAdapter();
	}
	
	private void setAdapter() {
		
		this.removeAllViews();
		int i = this.listFoodAttrAdapter.getCount();
		int j = 0;


		while (true) {
			if (j >= i) {
				return;
			}
			View localView = this.listFoodAttrAdapter.getView(j, null, null);
			this.addView(localView);
			j += 1;
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
			return foodModel.listSize.size();
		}

		@Override
		public Object getItem(int paramInt) {
			return foodModel.listSize.get(paramInt);
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
					inflater = LayoutInflater.from(mContext);
				}
				paramView = this.inflater
						.inflate(R.layout.food_size_item, null);

				paramView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
				listHolder = new FoodAttrView();

				listHolder.tvAttr = (TextView) paramView
						.findViewById(R.id.addorder_foodname);
				listHolder.tvNum = (EditText) paramView
						.findViewById(R.id.tv_foodcount);
				listHolder.tvNum
						.setOnFocusChangeListener(new View.OnFocusChangeListener() {

							@Override
							public void onFocusChange(View arg0, boolean arg1) {
								// TODO Auto-generated method stub

								if (arg1) {
									//nowEdit = (EditText) arg0;
								}
							}
						});
				

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

			FoodSizeModel model = foodModel.listSize.get(paramInt);

			if (model != null) {

				listHolder.tvNum.setTag(paramInt);
				listHolder.tvAttr.setText(model.name + "  [￥" + model.price
						+ "元]");
				listHolder.tvNum.setText("1");
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
