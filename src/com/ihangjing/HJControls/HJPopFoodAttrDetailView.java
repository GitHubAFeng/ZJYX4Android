package com.ihangjing.HJControls;

import java.util.ArrayList;

import com.ihangjing.Model.FoodAttrModel;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.FsgShopDetailModel;
import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.common.HjActivity;

import android.R.integer;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HJPopFoodAttrDetailView extends HJPopViewBase {
	FoodModel foodModel;
	FoodAttrAdapter listFoodAttrAdapter;
	EasyEatApplication app;
	FsgShopDetailModel shopmodeDetailModel;
	private RadioGroup rGroup;
	int Type = 0;//0listMake 1listSpec
	private LayoutInflater inflater;
	private LinearLayout llParent;
	FoodAttrModel checkModel;
	ArrayList< FoodAttrModel> checkArrayList;
	int Wight, Height, mCount, pt;//mCount 前面输入的数量 pt选中的size的索引
	public android.widget.CompoundButton.OnCheckedChangeListener checkListener;
	public HJPopFoodAttrDetailView(Context context, RelativeLayout parent,
			int wight, int height, int belowid, FoodModel food, FsgShopDetailModel shop, int type, FoodAttrModel model, int count, int postion) {
		super(context, parent, wight, height, belowid);
		// TODO Auto-generated constructor stub
		pt = postion;
		Wight = wight;
		Height = height;
		Type = type;
		mCount = count;
		foodModel = food;
		shopmodeDetailModel = shop;
		listFoodAttrAdapter = new FoodAttrAdapter();
		checkModel = model;
		
		app = ((HjActivity)context).app;
		this.setGravity(Gravity.CENTER);
		
		this.setOrientation(VERTICAL);
		if (inflater == null) {
			inflater = LayoutInflater.from(mContext);
		}
		View view = inflater
				.inflate(R.layout.food_attr_detail, null);
		rGroup = (RadioGroup)view.findViewById(R.id.rg_parent);
		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int position = checkedId - R.id.btn_number_plus;
				//checkModel = foodModel.listMake.get(position);
				
			}
		});
		if (Type == 1) {
			checkArrayList = new ArrayList<FoodAttrModel>();
			checkListener = new android.widget.CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					int position = buttonView.getId() - R.id.btn_number_plus;
					if (isChecked) {
						//checkArrayList.add(foodModel.listMake.get(position));
					}else{
						checkArrayList.remove(foodModel.listMake.get(position));
					}
				}
			};
		}
		
		llParent = (LinearLayout)view.findViewById(R.id.ll_parent);
		Button btnOK = (Button)view.findViewById(R.id.btn_ok);
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Type == 0 && foodModel.listSpec.size() > 0) {
					HJPopFoodAttrDetailView popFoodAttrDetailView = new HJPopFoodAttrDetailView(
							mContext, parentLayout, Wight, Height
							, belowID, foodModel, shopmodeDetailModel, 1, checkModel, mCount, pt);
					popFoodAttrDetailView.Show();
					HJPopFoodAttrDetailView.this.Close();
					return;
				}
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
				
				
				
				app.shopCartModel.addFoodAttrWitchCheckAttr(
						app.mShop, foodModel, pt, checkModel, checkArrayList,  mCount);
				HJPopFoodAttrDetailView.this.Close();
			}
		});
		
		setAdapter();
		this.addView(view);
		//AttributeSet
		
		
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
			if (Type == 0) {//分类的额外项目 单选
				rGroup.addView(localView);
				llParent.setVisibility(View.GONE);
			}else{//商品的额外项目多选
				rGroup.setVisibility(View.GONE);
				llParent.addView(localView);
			}
			
			j += 1;
		}
	}
	
	class FoodAttrView {
		RadioButton btnAdd;
		CheckBox cbAdd;
	}

	class FoodAttrAdapter extends BaseAdapter {
		FoodAttrView listHolder;
		

		FoodAttrAdapter() {
		}

		@Override
		public int getCount() {
			if (foodModel == null) {
				return 0;
			}
			if (Type == 0) {//分类额外项目
				return foodModel.listMake.size();
			}else{//商品的额外项目
				return foodModel.listSpec.size();
			}
			
		}

		@Override
		public Object getItem(int paramInt) {
			if (Type == 0) {
				return foodModel.listMake.get(paramInt);
			}else{
				return foodModel.listSpec.get(paramInt);
			}
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {

			if (paramView == null) {
				if (Type == 0) {
					paramView = inflater
							.inflate(R.layout.food_attr_item, null);

					/*paramView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
						}
					});*/
					listHolder = new FoodAttrView();

					

					listHolder.btnAdd = (RadioButton) paramView
							.findViewById(R.id.btn_number_plus);
				}else{
					paramView = inflater
							.inflate(R.layout.food_attr_item_checkbox, null);

					/*paramView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
						}
					});*/
					listHolder = new FoodAttrView();

					

					listHolder.cbAdd = (CheckBox) paramView
							.findViewById(R.id.btn_number_plus);
					
					listHolder.cbAdd.setOnCheckedChangeListener(checkListener);
					
				}
				
				//rGroup.addView(listHolder.btnAdd);
				
				paramView.setTag(paramView);
			} else {
				listHolder = (FoodAttrView) paramView.getTag();
			}
			/*FoodAttrModel model;
			if (Type == 0) {
				model = foodModel.listMake.get(paramInt);
			}else{
				model = foodModel.listSpec.get(paramInt);
			}
			//FoodSizeModel model = foodModel.listSize.get(paramInt);

			if (model != null) {
				if (Type == 0) {
					listHolder.btnAdd.setId(R.id.btn_number_plus + paramInt);
					listHolder.btnAdd.setText(model.name + "  [￥" + model.price
							+ "元]");
					listHolder.btnAdd.setTag(paramInt);
				}else{
					listHolder.cbAdd.setId(R.id.btn_number_plus + paramInt);
					listHolder.cbAdd.setText(model.name + "  [￥" + model.price
							+ "元]");
					listHolder.cbAdd.setTag(paramInt);
				}
				
				

			} else {
				
			}*/

			return paramView;
		}
	}

}
