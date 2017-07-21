package com.ihangjing.SQLite;

import java.util.ArrayList;


import com.ihangjing.Model.MyFriends;
import com.ihangjing.util.HJAppConfig;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	//public String CreateTabel;

	//private SQLiteDatabase db;
	final static String DataBaseName = HJAppConfig.CookieName + ".plist";
	final static int version = 2;

	public DBHelper(Context context) {
		super(context, DataBaseName, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("create table if not exists MyFriends(id integer primary key autoincrement, friendID integeer, name text, icon text, phone varchar(15))");
		arg0.execSQL("create table if not exists MyPhoneAddressBook(id integer primary key autoincrement, name text, phone varchar(15), type integet)");

	}

	

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
	
	
	
	public void cleanFriendsTable(){
		getWritableDatabase().delete("MyFriends", null, null);//ɾ����������
		ContentValues values = new ContentValues();
		values.put("seq", 0);
		String[] sqlStrings = new String[1];
		sqlStrings[0] = "MyFriends";
		
		getWritableDatabase().update("sqlite_sequence", values, "name=?", sqlStrings);//���ô�1��ʼ����
		
	}

	public int delFriend(MyFriends model){
		if (model == null) {
			return 0;
		}
		return getWritableDatabase().delete("MyFriends", "friendID=", new String[]{String.valueOf(model.getFriendID())});//ɾ����������
	}
	public boolean checkFriend(MyFriends model){
		if (model != null) {
			Cursor c = getReadableDatabase().rawQuery("select * from MyFriends where friendID=?", new String[]{String.valueOf(model.getFriendID())});//query("HaveAddress", null, null, null, null, null, "id desc", )//execSQL("select top 1 * from HaveAddress orderby id desc");
			if(c.getCount() != 0){
				return true;
			}
		}
		return false;
	}
	
	public int updateFriend(MyFriends model){
		if (model != null) {
			ContentValues values = new ContentValues();
			values.put("name", model.getFriendName());
			values.put("icon", model.getFriendICON());
			values.put("phone", model.getFriendPhone());
			
			return getWritableDatabase().update("MyFriends", values, "friendID=?", new String[]{String.valueOf(model.getFriendID())});//���ô�1��ʼ����
		}
		return 0;
	}
	
	public long insertMyFriend(MyFriends add) {
		ContentValues cv = new ContentValues();//ʵ����һ��ContentValues����װ�ش����������
		cv.put("friendID", add.getDataID());
		cv.put("name",add.getFriendName()); //
		cv.put("icon", add.getFriendICON());
		cv.put("phone", add.getFriendPhone());
		return this.getWritableDatabase().insert("MyFriends",null,cv);//ִ�в������
	}
	
	public int insertMyFriendWichCheck(MyFriends model){
		if (checkFriend(model)) {
			//����
			return updateFriend(model);
		}else{
			//����
			if (insertMyFriend(model) != -1) {
				return 1;
			}
		}
		return 0;
	}
	

	public ArrayList<MyFriends> getAllFriends() {
		// ��ȡ�����������
		ArrayList<MyFriends> list = new ArrayList<MyFriends>();
		Cursor c = getReadableDatabase().rawQuery("select * from MyFriends", null);//query("HaveAddress", null, null, null, null, null, "id desc", )//execSQL("select top 1 * from HaveAddress orderby id desc");
		if(c.getCount() == 0){
			return list;
		}
		else
		{
			
			while(c.moveToNext())
			{
				MyFriends addr = new MyFriends();
				addr.setDataID(c.getInt(c.getColumnIndex("id")));
				addr.setFriendID(c.getInt(c.getColumnIndex("friendID")));
				addr.setFriendName(c.getString(c.getColumnIndex("name")));
				addr.setFriendICON(c.getString(c.getColumnIndex("icon")));
				
				addr.setFriendPhone(c.getString(c.getColumnIndex("phone")));
				list.add(addr);
			}
			return list;
		}
	}
	
	public ArrayList<MyFriends> getFriends(String value) {
		// ��ȡ�����������
		value = value.replace("'", "");
		ArrayList<MyFriends> list = new ArrayList<MyFriends>();
		Cursor c = getReadableDatabase().rawQuery("select * from MyFriends where name like '%" + value + "%' or phone like '%" + value + "%'", null);//query("HaveAddress", null, null, null, null, null, "id desc", )//execSQL("select top 1 * from HaveAddress orderby id desc");
		if(c.getCount() == 0){
			return list;
		}
		else
		{
			
			while(c.moveToNext())
			{
				MyFriends addr = new MyFriends();
				addr.setDataID(c.getInt(c.getColumnIndex("id")));
				addr.setFriendID(c.getInt(c.getColumnIndex("friendID")));
				addr.setFriendName(c.getString(c.getColumnIndex("name")));
				addr.setFriendICON(c.getString(c.getColumnIndex("icon")));
				
				addr.setFriendPhone(c.getString(c.getColumnIndex("phone")));
				list.add(addr);
			}
			return list;
		}
	}

	public boolean deleteData(Context context) {
		// getReadableDatabase()..deleteDatabase(DataBaseName);
		return context.deleteDatabase(DataBaseName);
	}

}
