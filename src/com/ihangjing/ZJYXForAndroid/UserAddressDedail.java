package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.ihangjing.Model.ReciveAddressModel;
import com.ihangjing.Model.SectionInfo;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

import android.R.integer;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserAddressDedail extends Dialog implements HttpRequestListener {
	private TextView titleTextView;
	private Button backButton;
	private Button btnOK;
	private EditText etAddress;
	private EditText etReciver;
	private EditText etPhone;
	ReciveAddressModel AddressModel;
	UpdateAddress update;
	private HttpManager localHttpManager;
	Context context;
	UIHandler handler;
	boolean isNew;
	String userID;
	char[] input = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private ProgressDialog progressDialog;
	private Button btnArea;
	private SectionInfo section;
	EasyEatApplication app;
	//private SectionInfo section;
	public UserAddressDedail(Context context, ReciveAddressModel model,
			boolean isnew, UpdateAddress up, String userID, EasyEatApplication application) {
		super(context, R.style.no_title_dialog);
		// TODO Auto-generated constructor stub
		AddressModel = model;
		this.update = up;
		this.context = context;
		isNew = isnew;
		this.userID = userID;
		app = application;
	}

	public interface UpdateAddress {
		void updateAddress(ReciveAddressModel model);
		void Cancel();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reciver_address_dedail);
		handler = new UIHandler();
		View.OnClickListener btnClick = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.title_bar_back_btn:
					UserAddressDedail.this.cancel();
					break;

				case R.id.title_bar_right_btn:
					if (btnArea.getText().toString().equals("") || (AddressModel.getLat() < 1.0 &&
							AddressModel.getLon() < 1.0)) {
						Toast.makeText(context, context.getResources().getString(R.string.addr_detail_gps_notice)/*"请现在地图上定位"*/, 5).show();
						GoToArea();
						return;
					}
					if (etAddress.getText().toString().equals("")) {
						Toast.makeText(context, context.getResources().getString(R.string.addr_detail_addr_empt)/*"收货地址不能为空"*/, 5).show();
						return;
					}
					if (etReciver.getText().toString().equals("")) {
						Toast.makeText(context, context.getResources().getString(R.string.addr_detail_reciver_empt)/*"收货人不能为空"*/, 5).show();
						return;
					}
					if (etPhone.getText().toString().equals("")) {
						Toast.makeText(context, context.getResources().getString(R.string.addr_detail_del_phone_empt)/*"联系电话不能为空"*/, 5).show();
						return;
					}
					if(!checkMobilePhone(etPhone.getText().toString())){
						Toast.makeText(context, context.getResources().getString(R.string.addr_detail_del_phone_error)/*"请填写正确的电话号码"*/, 5).show();
						return;
					}
					if (section != null) {//区域未编辑
						AddressModel.setBuildID(section.SectionID);
						AddressModel.setBuildName(section.SectionName);
					}
					
					AddressModel.setAddres(etAddress.getText().toString());
					AddressModel.setMobilePhone(etPhone.getText().toString());
					AddressModel.setReciver(etReciver.getText().toString());
					UpdateAddress();
					break;
				}
			}
		};
		titleTextView = (TextView) findViewById(R.id.title_bar_content_tv);

		backButton = (Button) findViewById(R.id.title_bar_back_btn);
		backButton.setOnClickListener(btnClick);
		btnOK = (Button) findViewById(R.id.title_bar_right_btn);
		etAddress = (EditText) findViewById(R.id.et_address);
		etReciver = (EditText) findViewById(R.id.et_reciver);
		etPhone = (EditText) findViewById(R.id.et_phone);
		btnArea = (Button) findViewById(R.id.btn_area);
		btnArea.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GoToArea();
			}
		});
		if (isNew) {
			app.searchLocation = null;
			titleTextView.setText(context.getResources().getString(R.string.addr_detail_title)/*"新增收货地址"*/);
			btnOK.setText(context.getResources().getString(R.string.public_ok)/*"确定"*/);
			if (AddressModel != null) {
				etPhone.setText(AddressModel.getMobilePhone());
			}else{
				AddressModel = new ReciveAddressModel();
			}
		} else {
			titleTextView.setText(context.getResources().getString(R.string.addr_detail_title1)/*"编辑收货地址"*/);
			btnOK.setText(context.getResources().getString(R.string.public_ok)/*"确定"*/);
			btnArea.setText(AddressModel.getAddres());
			etAddress.setText(AddressModel.getAddres());
			etReciver.setText(AddressModel.getReciver());
			etPhone.setText(AddressModel.getMobilePhone());
			//btnArea.setText(AddressModel.getBuildName());
		}
		btnOK.setOnClickListener(btnClick);
		etPhone.setKeyListener(new NumberKeyListener() {

			@Override
			public int getInputType() {
				// TODO Auto-generated method stub
				return 3;
			}

			@Override
			protected char[] getAcceptedChars() {
				// TODO Auto-generated method stub
				return input;
			}

		});
		

	}
	@Override
	public void cancel() {
		if (update != null) {
			update.Cancel();
		}
		super.cancel();
	}
	
	public void GoToArea(){
		//((HjActivity) context).app.mSection = null;
		Intent localIntent1 = new Intent(context, SearchOnMap.class);
		localIntent1.putExtra("isSearch", true);
		if (AddressModel.getLat() > 0.0 && AddressModel.getLon() > 0.0) {
			localIntent1.putExtra("lat", AddressModel.getLat());
			localIntent1.putExtra("lng", AddressModel.getLon());
			localIntent1.putExtra("Address", AddressModel.getAddres());
		}
		//localIntent1.putExtra("cityid", app.userLocalInfo.cityid);
		context.startActivity(localIntent1);
	}

	// 检查手机号码是否规范
	private boolean checkMobilePhone(String paramString) {
		if (paramString == null || paramString.equals("")) {
			return false;
		}
		String str = paramString.replace("+86", "");
		return Pattern.compile("^(13|15|18)[0-9]{9}$").matcher(str).matches();
	}
	
	public void updateView() {
		// TODO Auto-generated method stub
		
			//section = ((HjActivity) context).app.mSection;
			if (app.searchLocation == null || (app.searchLocation.getLat() < 1.0 &&
					app.searchLocation.getLon() < 1.0) || 
					app.searchLocation.getAddressDetail().length() < 1) {
				Toast.makeText(context, context.getResources().getString(R.string.addr_detail_del_gps_error)/*"未选择正确的定位地址，无法添加地址"*/, 10).show();
				return;
			}
			if (isNew) {
				etAddress.setText(app.searchLocation.getAddressDetail());
				AddressModel.setAddres(app.searchLocation.getAddressDetail());
			}
			btnArea.setText(app.searchLocation.getAddressDetail());
			AddressModel.setLat(app.searchLocation.getLat());
			AddressModel.setLon(app.searchLocation.getLon());
			
		
	}

	private void UpdateAddress() {
		progressDialog = ProgressDialog.show(UserAddressDedail.this.context, "",
				context.getResources().getString(R.string.public_send)/*"更新数据中，请稍后..."*/);
		String url;
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("receiver", AddressModel.getReciver());
		localHashMap.put("address", AddressModel.getAddres());
		localHashMap.put("mobilephone", AddressModel.getMobilePhone());
		localHashMap.put("lat", String.valueOf(AddressModel.getLat()));
		localHashMap.put("lng", String.valueOf(AddressModel.getLon()));
		localHashMap.put("userid", userID);
		localHashMap.put("buildingid", "0");
		localHashMap.put("BuildingName", "");
		
		if (isNew) {
			localHashMap.put("op","1");
			localHashMap.put("phone", " ");
		} else {
			localHashMap.put("op","0");
			localHashMap.put("phone", AddressModel.getPhone());
			localHashMap.put("dataid", AddressModel.getId());
		}
		url = "Android/SaveUserAddress.aspx?";
		
		localHttpManager = new HttpManager(context, UserAddressDedail.this,
				url, localHashMap, 2,  0);
		localHttpManager.postRequest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			String strJson = (String) paramObject;
			try {
				JSONObject jsonObject = new JSONObject(strJson);
				int type = jsonObject.getInt("state");

				if (type > 0) {
					msg.what = UIHandler.UPDATE_OK;
					if(isNew){
						AddressModel.setId(String.valueOf(type));
					}
				} else {
					msg.what = UIHandler.UPDATE_ERROR;
				}

			} catch (JSONException localJSONException) {
				msg.what = UIHandler.NET_ERROR;
			}

		} else {
			msg.what = UIHandler.NET_ERROR;
		}
		handler.sendMessage(msg);

	}

	private class UIHandler extends Handler {
		public static final int UPDATE_OK = 1;
		public static final int NET_ERROR = -1;
		public static final int UPDATE_ERROR = -2;

		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			switch (msg.what) {
			case UPDATE_ERROR:
				Toast.makeText(context, context.getResources().getString(R.string.public_up_data_failed)/*"更新数据失败"*/, 5).show();
				break;
			case UPDATE_OK:
				if (update != null) {
					update.updateAddress(AddressModel);
				}
				UserAddressDedail.this.cancel();
				break;
			case NET_ERROR:
				Toast.makeText(context, context.getResources().getString(R.string.public_net_or_data_error)/*"网络或数据出错无法获取数据"*/, 5).show();
				break;
			}
		}
	}

}
