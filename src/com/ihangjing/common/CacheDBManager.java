package com.ihangjing.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheDBManager {
	private static final String DATABASE_NAME = "easyeat4android2cache.db";
	// private static final int DATABASE_VERSION = 1;
	private static DatabaseHelper DBHelper = null;
	private static SQLiteDatabase db = null;
	private static CacheDBManager dbManager = null;
	private final Context context;

	private CacheDBManager(Context paramContext) {
		this.context = paramContext;
		Context localContext = this.context;
		DBHelper = new DatabaseHelper(localContext);
	}

	public static CacheDBManager open(Context paramContext) {
		CacheDBManager localCacheDBManager = null;

		try {
			if (dbManager == null) {
				dbManager = new CacheDBManager(paramContext);
				db = DBHelper.getWritableDatabase();
			}
			localCacheDBManager = dbManager;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return localCacheDBManager;
	}

	public void close() {
		if (db != null)
			db.close();
		if (DBHelper != null)
			DBHelper.close();
		if (dbManager == null)
			return;
		dbManager = null;
	}

	public void deleteAllCache() {
		db.delete("historyCache", null, null);
	}

	public void deleteCache(String paramString) {
		SQLiteDatabase localSQLiteDatabase = db;
		String[] arrayOfString = new String[1];
		arrayOfString[0] = paramString;
		localSQLiteDatabase.delete("historyCache", "IdKey LIKE ? ",
				arrayOfString);
	}

	public void insertToCache(String paramString1, String paramString2,
			long paramLong) {
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("IdKey", paramString1);
		localContentValues.put("content", paramString2);
		Long localLong = Long.valueOf(paramLong);
		localContentValues.put("date", localLong);
		db.insert("historyCache", null, localContentValues);
	}

	public void updateToCache(String paramString1, String paramString2,
			long paramLong) {
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("IdKey", paramString1);
		localContentValues.put("content", paramString2);
		Long localLong = Long.valueOf(paramLong);
		localContentValues.put("date", localLong);
		SQLiteDatabase localSQLiteDatabase = db;
		String[] arrayOfString = new String[1];
		arrayOfString[0] = paramString1;
		localSQLiteDatabase.update("historyCache", localContentValues,
				"IdKey LIKE ? ", arrayOfString);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context paramContext) {
			super(paramContext, DATABASE_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
			paramSQLiteDatabase
					.execSQL("create table historyCache(id integer primary key autoincrement, IdKey text not null , content text not null,date long not null );");
		}

		@Override
		public void onUpgrade(SQLiteDatabase paramSQLiteDatabase,
				int paramInt1, int paramInt2) {
			paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS historyCache");
			onCreate(paramSQLiteDatabase);
		}
	}

	static class historyCache {
		public static final String Create_table = "create table historyCache(id integer primary key autoincrement, IdKey text not null , content text not null,date long not null );";
		public static final String KEY_IdKey = "IdKey";
		public static final String KEY_ROWId = "id";
		public static final String KEY_content = "content";
		public static final String KEY_date = "date";
		public static final String TableName = "historyCache";
		public static final String[] columns;

		static {
			String[] arrayOfString = new String[4];
			arrayOfString[0] = "id";
			arrayOfString[1] = "IdKey";
			arrayOfString[2] = "content";
			arrayOfString[3] = "date";
			columns = arrayOfString;
		}
	}
}