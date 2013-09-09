package com.example.imt3662_ob1;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_LOCATIONS = "locations";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_ADDR = "address";
	
	private static final String DB_NAME = "locations.db";
	private static final int DB_VERSION = 1;
	
	private static final String DB_CREATE = 
			  "create table if not exists "
			+ TABLE_LOCATIONS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_ADDR + " text not null);";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
	}
	
	public void insertAddress(String address) {
		Log.e("DBG", "Attempting to insert: " + address);
		
		final ContentValues values = new ContentValues();
		values.put(COLUMN_ADDR, address);
		
		final SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_LOCATIONS, null, values);
		db.close();
	}
	
	public ArrayList<String> getAllAddresses() {
		SQLiteDatabase db = getReadableDatabase();
		
		/*
		 * SELECT DISTINCT 'COLUMN_ADDR' FROM 'TABLE_LOCATIONS'
		 * ORDER BY 'COLUMN_ID' DESC
		 */
		Cursor cursor = db.query(
				true, 
				TABLE_LOCATIONS, 
				new String[] { COLUMN_ADDR }, 
				null, 
				null, null, null, COLUMN_ID + " DESC", null, null);
		
		ArrayList<String> results = new ArrayList<String>();
		
		if (cursor == null || !cursor.moveToFirst()) {
			return results;
		}
		
		while (!cursor.isAfterLast()) {
			String addr = cursor.getString(0);
			results.add(addr);
			cursor.moveToNext();
		}
		
		cursor.close();
		return results;
	}
}
