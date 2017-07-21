package com.ihangjing.ZJYXForAndroid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.ZJYXForAndroid.MyMessageListActivity.ListViewAdapter;
import com.ihangjing.Model.MyMessageListModel;
import com.ihangjing.Model.MyMessageModel;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class MyMessageListActivity extends HjActivity implements HttpRequestListener{

	private static final int NET_DIALOG = 111;
	private ListView listView;
	private EditText etMessage;
	private Button btnSend;
	UIHandler handler;
	private HttpManager localHttpManager;
	private int pageindex;
	private MyMessageListModel myMessageList;
	public View moreView;
	public LayoutInflater inflater;
	private ListViewAdapter listViewAdapter;
	protected int scrollPos;
	protected int scrollTop;
	private String dialogStr;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.my_message_list_view);
		Button btnBack = (Button)findViewById(R.id.title_bar_back_btn);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		myMessageList = new MyMessageListModel();
		listView = (ListView)findViewById(R.id.lv_message);
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    // ListPos记录当前可见的List顶端的一行的位置
                   scrollPos = listView.getFirstVisiblePosition();
				} 
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem == 0 && myMessageList.page < myMessageList.total) {
					pageindex++;
					GetMessageList();
				}
			}
		});
		listViewAdapter = new ListViewAdapter();
		listView.setAdapter(listViewAdapter);
		etMessage = (EditText)findViewById(R.id.tv_message);
		btnSend = (Button)findViewById(R.id.btn_send);
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (etMessage.getText().toString().length() < 1) {
					Toast.makeText(MyMessageListActivity.this, "请输入要发送的消息", 5).show();
					return;
				}
				SendMessage();
			}
		});
		handler = new UIHandler();
		inflater = (LayoutInflater) MyMessageListActivity.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		moreView = inflater.inflate(R.layout.more_build_view, null);
		pageindex = 1;
		
		GetMessageList();
	}
	
	private void GetMessageList(){
		if (pageindex == 1) {
			dialogStr = "加载中......";
			showDialog(NET_DIALOG);
		}
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(pageindex));
		localHashMap.put("pagesize", "5");
		localHashMap.put("userid", app.userInfo.userId);
		

		localHttpManager = new HttpManager(MyMessageListActivity.this, MyMessageListActivity.this,
				"Android/Messagelist.aspx?", localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}
	
	private void SendMessage(){
		dialogStr = "发表中......";
		showDialog(NET_DIALOG);
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("username", app.userInfo.userName);
		localHashMap.put("msg", etMessage.getText().toString());
		localHashMap.put("userid", app.userInfo.userId);

		localHttpManager = new HttpManager(MyMessageListActivity.this, MyMessageListActivity.this,
				"Android/addmessage.aspx?", localHashMap, 2, 2);
		localHttpManager.getRequeest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			try {
				JSONObject json = new JSONObject((String)paramObject);
				switch (tag) {
				case 1:
					myMessageList.JSonToBean(json, app.userInfo.userId);
					msg.what = UIHandler.GET_MESSAGE_OK;
					break;
				case 2:
					int state = json.getInt("state");
					if (state > 0) {
						msg.what = UIHandler.SEND_MESSAGE_SUCESS;
						MyMessageModel model = new MyMessageModel();
						model.setDataID(String.valueOf(state));
						model.setComment(etMessage.getText().toString());
						Date curDate = new Date(System.currentTimeMillis());//获取当前时间
						//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						model.setUserICO(app.userInfo.myICO);
						model.setDateTime(String.format("%d-%d-%d %d:%d:%d", curDate.getYear() + 1900, curDate.getMonth() + 1, curDate.getDate(), curDate.getHours(), curDate.getMinutes(), curDate.getSeconds()));
						model.setrDateTime("");
						model.setUserID(app.userInfo.userId);
						model.setUserName("我自己");
						model.setRrmark("[暂未回复]");
						myMessageList.list.add(0, model);
					}else{
						msg.what = UIHandler.SEND_MESSAGE_ERROR;
					}
					break;
				default:
					msg.what = 0;
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what = UIHandler.NET_ERROR;
			}
		}else{
			msg.what = UIHandler.NET_ERROR;
		}
		handler.sendEmptyMessage(tag);
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
	class UIHandler extends Handler{

		public static final int SEND_MESSAGE_ERROR = -2;
		public static final int SEND_MESSAGE_SUCESS = 2;
		public static final int GET_MESSAGE_OK = 1;
		public static final int NET_ERROR = -1;
		@Override
		public void handleMessage(Message msg) {
			removeDialog(NET_DIALOG);
			switch (msg.what) {
			case NET_ERROR:
				Toast.makeText(MyMessageListActivity.this, "网络或数据错误！请稍后再试", 5).show();
				break;
			case GET_MESSAGE_OK:
				listViewAdapter.notifyDataSetChanged();
				if (myMessageList.page == 1 && myMessageList.list.size() > 0) {
					
					listView.setSelection(myMessageList.list.size() * 2);
				}else{
					listView.setSelection(10);
					if (myMessageList.page >= myMessageList.total) {
						moreView.setVisibility(View.GONE);
					}
				}
				break;
			case SEND_MESSAGE_ERROR:
				Toast.makeText(MyMessageListActivity.this, "发送消息失败！请稍后再试", 5).show();
				break;
			case SEND_MESSAGE_SUCESS:
				etMessage.setText("");
				listViewAdapter.notifyDataSetChanged();
				listView.setSelection(myMessageList.list.size() * 2);
				break;
			default:
				break;
			}
		}
		
	}
	class MyMessageItem{
		TextView tvUserName;
		TextView tvDateTime;
		TextView tvComment;
		ImageView ivImage;
	}
	class ListViewAdapter extends BaseAdapter {
		MyMessageItem myMessageItem;
		ListViewAdapter() {
			myMessageItem = new MyMessageItem();
			
		}

		@Override
		public int getCount() {
			int count = myMessageList.list.size();
			if (count > 0) {
				count *= 2;
				count++;
			}
			return count;
		}

		@Override
		public Object getItem(int paramInt) {
			return myMessageList.list.get(paramInt / 2);
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		

		@Override
		public View getView(int paramInt, View convertView,
				ViewGroup paramViewGroup) {
			if (paramInt == 0) {
				return moreView;
			}
			paramInt--;
			//if (convertView == null) {
			if (paramInt % 2 == 0) {
				convertView = inflater.inflate(R.layout.my_message_send_item, null);
			}else{
				convertView = inflater.inflate(R.layout.my_message_reciver_item, null);
			}
				
			myMessageItem.tvComment = (TextView)convertView.findViewById(R.id.messagedetail_row_text);
			myMessageItem.tvDateTime = (TextView)convertView.findViewById(R.id.messagedetail_row_date);
			myMessageItem.tvUserName = (TextView)convertView.findViewById(R.id.messagedetail_row_name);
			myMessageItem.ivImage = (ImageView)convertView.findViewById(R.id.messagegedetail_rov_icon);
			
			
			int count = (myMessageList.list.size() * 2) - paramInt - 1;
			count /= 2;
			MyMessageModel model = myMessageList.list.get(count);
			if (model != null) {
				if (paramInt % 2 == 0) {//发送端
					Bitmap bitmap = app.mLoadImage.getBitmap(model.getUserICO());
					if (bitmap != null) {
						Drawable drawable = new BitmapDrawable(bitmap);
						myMessageItem.ivImage.setBackgroundDrawable(drawable);
					}else{
						myMessageItem.ivImage.setBackgroundResource(R.drawable.friend_default);
					}
					
					myMessageItem.tvUserName.setText("[" + model.getUserName() + "]");
					myMessageItem.tvDateTime.setText("发表于:" + model.getDateTime());
					myMessageItem.tvComment.setText(model.getComment());
				}else{//接收端
					myMessageItem.ivImage.setBackgroundResource(R.drawable.icon);
					myMessageItem.tvUserName.setText("[花马巴黎]");
					myMessageItem.tvDateTime.setText("回复于:" + model.getrDateTime());
					myMessageItem.tvComment.setText(model.getRrmark());
				}
			}
			return convertView;
		}
	}
}
