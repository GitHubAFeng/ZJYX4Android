package com.ihangjing.HJControls;

import java.util.Timer;
import java.util.TimerTask;

import com.ihangjing.ZJYXForAndroid.EasyEatApplication;
import com.ihangjing.ZJYXForAndroid.R;
import com.ihangjing.Model.FoodTypeListModel;
import com.ihangjing.Model.FoodTypeModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HJFoodTypeWin8 extends LinearLayout implements HttpRequestListener {
	Context mContext;
	LinearLayout linearLayoutView[];

	private UIHandler mHandler;
	private FoodTypeListModel foodTypeListModel;
	private int Row = 2;// �����������ٲ���

	private HttpManager localHttpManager;
	public int cFlowKeyDown = KeyEvent.KEYCODE_DPAD_RIGHT;
	public LayoutInflater inflater;
	public View.OnClickListener viewClickListener;
	EasyEatApplication app;

	private int ColorGrop[] = { Color.rgb(223, 125, 26),
			Color.rgb(59, 140, 220), Color.rgb(47, 0, 109),
			Color.rgb(211, 44, 26), Color.rgb(115, 0, 112),
			Color.rgb(176, 82, 156), Color.rgb(0, 156, 76),
			Color.rgb(200, 159, 0), Color.rgb(236, 153, 94) };
	private int HeightGrop[] = {180, 130, 140, 150, 170, 190, 200, 180, 130, 150, 160, 200, 130, 150, 160, 190};
	private Timer timer;
	private TimerTask updataViewTask;

	public HJFoodTypeWin8(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public HJFoodTypeWin8(Context context, AttributeSet attrs) {
		super(context, attrs);
		// this.setStaticTransformationsEnabled(true);
		mContext = context;
		app = ((HjActivity)mContext).app;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.HJScorllADV);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		this.setLayoutParams(params);
		// this.setG
		this.setOrientation(LinearLayout.HORIZONTAL);
		Row = a.getInt(R.styleable.HJScorllADV_win8Row, 2);
		linearLayoutView = new LinearLayout[Row];
		// TextView textView;
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, 0.5f);

		//params.setMargins(0, 5, 5, 0);

		for (int i = 0; i < Row; i++) {
			linearLayoutView[i] = new LinearLayout(mContext);
			linearLayoutView[i].setLayoutParams(params);
			linearLayoutView[i].setOrientation(LinearLayout.VERTICAL);

			// linearLayoutView[i].setWeightSum(0.5f);//����ò��û�ã�����

			this.addView(linearLayoutView[i]);
		}
		mHandler = new UIHandler();
		viewClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				app.foodListType = 1;
				app.viewID = (Integer)v.getTag();
				Intent localIntent = new Intent("com.ihangjing.common.tap1");
				app.sendBroadcast(localIntent);
			}
		};
		
		
		a.recycle();
		
		ReadChaceData();// ��ȡ���ػ���
		timer = new Timer();
		updataViewTask = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = UIHandler.REPLAY_ADV;
				mHandler.sendMessage(msg);

			}
		};
		timer.schedule(updataViewTask, 3000, 3000);
		
		GetData();
	}
	
	public void FlushView() {
		// TODO Auto-generated method stub
		int lenght = foodTypeListModel.list.size();
		if (lenght > 0) {
			int nView = 0;
			int nViewGroupMax = 0;//��¼���һ����ĸ������ٲ����Ĵ����������У�ֻ��һ��������
			int nViewGroupMin = Integer.MAX_VALUE;//��¼����һ����ĸ������ٲ����Ĵ����������У�ֻ��һ���������
			int rowToLeft = 0;//���ҵ���
			int rowToRight = 0;//������
			int nViewGroup;//��ǰ��ͼ��view����
			for (int i = 0; i < Row; i++) {
				nViewGroup = linearLayoutView[i].getChildCount();
				if (nViewGroup >= nViewGroupMax) {//���������ڣ���Ŀ�������
					nViewGroupMax = nViewGroup;
					rowToRight = i;//�����ͼ��������������ô�������ͼ��ʼɾ������ͼ����������
				}
				if (nViewGroup < nViewGroupMin) {//ע�⣬���ﲻ���õ��ڣ�����ǿ�������
					nViewGroupMin = nViewGroup;
					rowToLeft = i;
				}
				nView +=linearLayoutView[i].getChildCount();
			}
			nView = nView - lenght;//�������View
			LinearLayout.LayoutParams params;
			TextView textView;
			RelativeLayout image;
			LinearLayout layout;
			int colorInGourp = 0;//���λ�ȡ����ɫֵ��group�е�����
			FoodTypeModel model;
			int heightInGroup = 0;//�߶�������������
			int nStart = 0;
			//Bitmap originalImage;
			//BitmapDrawable drawable;
			if (nView > 0) {//�еĶ࣬��ôɾ�������
				for (int i = 0; i < nView; i++) {//ȥ�������
					linearLayoutView[rowToRight].removeViews(linearLayoutView[rowToRight].getChildCount() - 1, 1);
					rowToRight--;
					if (rowToRight < 0) {
						rowToRight = Row - 1;
					}
				}
				nStart = lenght;
			}else{//�����µ�view
				nView *= -1;//Ҫ�������ٸ�view
				nStart = lenght - nView;
				for (int i = nStart; i < lenght; i++) {//����������view
					model = foodTypeListModel.list.get(i);
					
					params = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT,
							HeightGrop[heightInGroup]);
					
					params.setMargins(3, 3, 3, 3);
					layout = new LinearLayout(mContext);
					
					layout.setLayoutParams(params);
					layout.setOrientation(VERTICAL);
					layout.setGravity(Gravity.CENTER);
					
					layout.setBackgroundColor(ColorGrop[colorInGourp]);
					image = new RelativeLayout(mContext);
					image.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
			//		if (model.isDowload) {// ��������
						image.setTag(model.getImageNetPath());
						app.mLoadImage.addTask(model.getImageNetPath(), image, R.drawable.default_food_type);
			//		}
					
					/*if (model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
						
					}
					if (model.getImageLocalPath() != null && !model.getImageLocalPath().equals("")) {// ���ص�ַ��Ϊ��
						originalImage = OtherManager.getBitmap(model.getImageLocalPath());
						if (originalImage == null) {
							originalImage = BitmapFactory.decodeResource(
									mContext.getResources(), R.drawable.default_food_type);
							if (!model.isDowload && model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
								//�������ַ������û�м������أ���ô����
								model.isDowload = true;
								image.setTag(model.getImageNetPath());
								app.mLoadImage.addTask(model.getImageNetPath(), image, R.drawable.default_food_type);
							}
						}
					}else{// ���غ������ַ��Ϊ�գ�ʹ��id
						originalImage = BitmapFactory.decodeResource(
								mContext.getResources(), R.drawable.default_food_type);
						if (!model.isDowload && model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {//
							//�������ַ������û�м������أ���ô����
							model.isDowload = true;
							image.setTag(model.getImageNetPath());
							app.mLoadImage.addTask(model.getImageNetPath(), image, R.drawable.default_food_type);
						}
					}
					drawable = new BitmapDrawable(originalImage);
					image.setBackgroundDrawable(drawable);*/
					layout.addView(image);
					
					textView = new TextView(mContext);
					textView.setTextColor(Color.rgb(255, 255, 255));
					textView.setText(model.getName());
					textView.setGravity(Gravity.CENTER);
					layout.addView(textView);
					linearLayoutView[rowToLeft].addView(layout);
					layout.setTag(i);
					layout.setOnClickListener(viewClickListener);
					
					if (++rowToLeft >= Row) {
						
						rowToLeft = 0;
					}
					if (++colorInGourp >= ColorGrop.length) {
						colorInGourp = 0;
					}
					
					if(++heightInGroup >= HeightGrop.length){
						heightInGroup = 0;
					}
				}
			}
			rowToLeft = 0;
			nViewGroup = 0;
			for (int i = 0; i < nStart; i++) {//������ʾ
				model = foodTypeListModel.list.get(i);
				
				layout = (LinearLayout)linearLayoutView[rowToLeft].getChildAt(nViewGroup);
				image = (RelativeLayout)layout.getChildAt(0);
				//if (model.isDowload) {// ��������
					image.setTag(model.getImageNetPath());
					app.mLoadImage.addTask(model.getImageNetPath(), image, R.drawable.default_food_type);
				//}
				/*if (model.getImageLocalPath() != null && !model.getImageLocalPath().equals("")) {// ���ص�ַ��Ϊ��
					originalImage = OtherManager.getBitmap(model.getImageLocalPath());
					if (originalImage == null) {
						originalImage = BitmapFactory.decodeResource(
								mContext.getResources(), R.drawable.default_food_type);
						if (!model.isDowload && model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {
							//�������ַ������û�м������أ���ô����
							model.isDowload = true;
							image.setTag(model.getImageNetPath());
							app.mLoadImage.addTask(model.getImageNetPath(), image, R.drawable.default_food_type);
						}
					}
				}else{// ���غ������ַ��Ϊ�գ�ʹ��id
					originalImage = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.default_food_type);
					if (!model.isDowload && model.getImageNetPath() != null && !model.getImageNetPath().equals("")) {//
						//�������ַ������û�м������أ���ô����
						model.isDowload = true;
						image.setTag(model.getImageNetPath());
						app.mLoadImage.addTask(model.getImageNetPath(), image, R.drawable.default_food_type);
					}
				}
				drawable = new BitmapDrawable(originalImage);
				image.setBackgroundDrawable(drawable);*/
				textView = (TextView)layout.getChildAt(1);
				textView.setText(model.getName());
				if (++rowToLeft >= Row) {
					rowToLeft = 0;
					nViewGroup++;
				}
			}
		}
	}

	private void ReadChaceData() {
		app.foodTypeList = OtherManager.getFoodTypeFormCaceh(mContext);
		foodTypeListModel = app.foodTypeList;
		FlushView();
	}

	private void GetData() {
		localHttpManager = new HttpManager(mContext, HJFoodTypeWin8.this,
				"APP/Android/GetShopTypeList.aspx", null, 2, 1);
		localHttpManager.getRequeest();
	}
	
	public int getColorFormGroup(int n1, int n2, int n3, int n4){
		int len = ColorGrop.length;
		int readomIndex = n1;
		for (; readomIndex == n1 || readomIndex == n2 || readomIndex == n3 || readomIndex == n4; ) {
			readomIndex = (int) (Math.random() * len);
		}
		return readomIndex;
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message message = new Message();
		if (paramInt == 260) {
			switch (tag) {
			case 1:
				try {
					String value = (String) paramObject;
					foodTypeListModel.stringToBean(value, 0, getResources().getString(R.string.public_all_type));
					message.what = UIHandler.GET_DATA_SUCCESS;
				} catch (Exception e) {
					// TODO: handle exception
					message.what = UIHandler.NET_ERROR;
				}
				break;

			default:
				break;
			}
		} else {
			message.what = UIHandler.NET_ERROR;
		}
		mHandler.sendMessage(message);
	}

	private class UIHandler extends Handler {
		public static final int GET_DATA_SUCCESS = 2;
		public static final int NET_ERROR = -1;

		protected static final int REPLAY_ADV = 1;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_DATA_SUCCESS:
				OtherManager.saveFoodTypeListToCache(mContext,
						foodTypeListModel);
				FlushView();
				break;
			case REPLAY_ADV:
				app.mLoadImage.doTask();

				/*FoodTypeModel model;
				for (int i = 0; i < foodTypeListModel.list.size(); i++) {
					model = foodTypeListModel.list.get(i);
					if (model.isDowload) {// ��Ҫ����

						if (model.getImageNetPath() != null
								&& !model.getImageNetPath().equals("")) {
							Bitmap bitmap = app.mLoadImage.memoryCache
									.getBitmapFromCache(model.getImageNetPath());
							if (bitmap != null) {// ������ɱ���ͼƬ������
								//model.setImageLocalPath(OtherManager.saveFoodTypeBitmapWithType(bitmap, model.getId()));
								model.isDowload = false;
								OtherManager.saveFoodTypeListToCache(mContext, foodTypeListModel);

							}
						}

					}
				}*/
				break;
			case NET_ERROR:
				Toast.makeText(mContext, "��������ݴ������Ժ�����", 5).show();
				break;
			default:
				break;
			}
		}
	}

	
}
