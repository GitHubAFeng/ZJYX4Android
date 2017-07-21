package com.ihangjing.ZJYXForAndroid;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihangjing.ZJYXForAndroid.ShopDetail.FoodListHolder;
import com.ihangjing.Model.FoodModel;
import com.ihangjing.Model.MyFriends;
import com.ihangjing.Model.MyFriendsList;
import com.ihangjing.Model.MyPhoneAddressBook;
import com.ihangjing.Model.MyPhoneBookList;
import com.ihangjing.SQLite.DBHelper;
import com.ihangjing.common.HjActivity;
import com.ihangjing.common.LoadImage;
import com.ihangjing.common.OtherManager;
import com.ihangjing.net.HttpManager;
import com.ihangjing.net.HttpRequestListener;

public class MyPhoneBookListView extends HjActivity implements
		HttpRequestListener {

	private ListView ListView;
	private MyPhoneBookList phoneBookList;

	private HttpManager localHttpManager;
	// int showType = 0;// 0��ʾ���к��ѣ�1��ʾ��������
	public LayoutInflater inflater;
	FriendsListViewAdapter listViewAdapter;
	// View moreView;
	private String dialogStr;
	private UIHandler mHandler;
	static int NET_DIALOG = 322;
	boolean isRun = true;
	boolean readFinish = false;
	public int pageindex;
	public String checkValue;
	static int LONG_STAR = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_phone_book);
		mHandler = new UIHandler();
		ListView = (ListView) findViewById(R.id.lv_data);

		ListView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

		ListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				// intputHiden();

				app.mLoadImage.doTask();
			}
		});
		phoneBookList = new MyPhoneBookList();

		listViewAdapter = new FriendsListViewAdapter();
		ListView.setAdapter(listViewAdapter);

		inflater = LayoutInflater.from(MyPhoneBookListView.this);
		Button btnBack = (Button) findViewById(R.id.title_bar_back_btn);
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRun = false;
				finish();
			}
		});

		TextView tvRight = (TextView) findViewById(R.id.title_bar_right_btn);

		tvRight.setVisibility(View.GONE);

	}

	@Override
	protected void onResume() {
		super.onResume();
		new readThread().start();
	}

	public void checkFriendsFromServer(int page, String value) {
		readFinish = false;
		HashMap<String, String> localHashMap = new HashMap<String, String>();
		localHashMap.put("pageindex", String.valueOf(page));
		localHashMap.put("phones", value);
		localHashMap.put("userid", app.userInfo.userId);
		localHttpManager = new HttpManager(MyPhoneBookListView.this,
				MyPhoneBookListView.this, "/Android/matefriend.aspx?",
				localHashMap, 2, 1);
		localHttpManager.getRequeest();
	}

	@Override
	public void action(int paramInt, Object paramObject, int tag) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		if (paramInt == 260) {
			String value = (String) paramObject;

			try {
				JSONObject json = new JSONObject(value);
				switch (tag) {
				case 1:
					int page = json.getInt("page");
					int start = (page - 1) * LONG_STAR;
					JSONArray array = json.getJSONArray("datalist");
					for (int i = 0; start < phoneBookList.list.size()
							&& i < array.length(); start++, i++) {
						MyPhoneAddressBook model = phoneBookList.list
								.get(start);
						// JSONObject json1 = array.getJSONObject(i);
						model.setFriendType(array.getJSONObject(i).getInt(
								"state"));
					}
					msg.what = UIHandler.READD_NEXT;
					readFinish = true;
					break;
				case 2:
					msg.what = UIHandler.SEARCH_SCUESS;
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

	@Override
	protected void onDestroy() {
		isRun = false;
		super.onDestroy();
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
					// ȡ������
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

	private class readThread extends Thread {
		@Override
		public void run() {
			Uri uri = ContactsContract.Contacts.CONTENT_URI;// ���е���ϵ��(contacts)
			ContentResolver contentResolver = MyPhoneBookListView.this
					.getContentResolver();
			Cursor cursor = contentResolver.query(uri, null, null, null, null);// ��ѯ������ϵ�ˣ��õ�һ���α�
			int start = 0;
			int n = 0;
			pageindex = 0;
			int j = 0;
			int count = cursor.getColumnCount();
			int phoneCount;

			while (cursor.moveToNext() && isRun) {
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID)); // �õ���ϵ�˵�Ψһ��ʶID
				String name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));// �õ���ϵ�˵�����
				// �����Ǹ�����ϵ��ID������һ�ű����ҵ����û������е绰����URI��ʵ��content://com.android.contacts/data/emails
				Cursor phones = contentResolver.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				j++;
				phoneCount = phones.getColumnCount();
				int i = 0;
				while (phones.moveToNext() && isRun) {// ��������ϵ�����еĵ绰

					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					if (phoneNumber.length() > 10) {
						MyPhoneAddressBook model = new MyPhoneAddressBook();
						model.setFriendPhone(phoneNumber);
						model.setFriendName(name);
						phoneBookList.list.add(model);
						n++;
						i++;

						if (n - start >= LONG_STAR
								|| (j + 1 >= count && i + 1 >= phoneCount)) {
							checkValue = "";
							for (; start < n; start++) {
								MyPhoneAddressBook model1 = phoneBookList.list
										.get(start);
								if (start + 1 < n) {
									checkValue += model1.getFriendPhone() + ",";
								} else {
									checkValue += model1.getFriendPhone();
								}
							}
							readFinish = false;
							pageindex++;
							Message msg = new Message();
							msg.what = UIHandler.READ_START;
							mHandler.sendMessage(msg);

							while (!readFinish) {
								SystemClock.sleep(1000);
							}

						}

					}

				}
				phones.close();
			}
			cursor.close();
		}
	}

	private class UIHandler extends Handler {
		public static final int READ_START = 2;
		public static final int NET_ERROR = -2;
		public static final int SEARCH_SCUESS = 3;
		public static final int READD_NEXT = 1;

		@Override
		public void handleMessage(Message msg) {
			removeDialog(NET_DIALOG);
			switch (msg.what) {
			case NET_ERROR:
				Toast.makeText(MyPhoneBookListView.this, "��������ݴ������Ժ����ԣ�", 10)
						.show();
				break;
			case READD_NEXT:
				listViewAdapter.notifyDataSetChanged();
				break;
			case READ_START:

				checkFriendsFromServer(pageindex, checkValue);
				break;
			case SEARCH_SCUESS:
				listViewAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}

	class FriendView {
		TextView name;
		TextView phone;
		TextView state;
	}

	class FriendsListViewAdapter extends BaseAdapter {
		FriendsListViewAdapter() {
		}

		@Override
		public int getCount() {
			return phoneBookList.list.size();

		}

		@Override
		public Object getItem(int paramInt) {
			return phoneBookList.list.get(paramInt);

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

			FriendView listHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.phone_book_item, null);
				listHolder = new FriendView();
				listHolder.name = (TextView) convertView
						.findViewById(R.id.tv_name);
				listHolder.phone = (TextView) convertView
						.findViewById(R.id.tv_phone);
				listHolder.state = (TextView) convertView
						.findViewById(R.id.tv_type);

				convertView.setTag(listHolder);
			} else {
				listHolder = (FriendView) convertView.getTag();
			}

			MyPhoneAddressBook model = phoneBookList.list.get(paramInt);

			if (model != null) {
				listHolder.name.setText(model.getFriendName());
				listHolder.phone.setText(model.getFriendPhone());
				switch (model.getFriendType()) {
				case -3:
					listHolder.state.setTextColor(Color.rgb(99, 72, 36));
					listHolder.state.setText("δע��");
					break;
				case -2:
					listHolder.state.setTextColor(Color.rgb(0, 0, 0));
					listHolder.state.setText("�Ѿ��Ǻ���");
					break;
				case -1:
					listHolder.state.setTextColor(Color.rgb(106, 81, 141));
					listHolder.state.setText("��������Լ�Ϊ����");
					break;
				case 1:
					listHolder.state.setTextColor(Color.rgb(117, 189, 49));
					listHolder.state.setText("��ӳɺ���");
					break;
				default:
					break;
				}
			}

			return convertView;
		}
	}

}
