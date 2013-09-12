package com.example.imt3662_ob1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {
	private static final String TAG = "DBHelper";
	
	public static final String TABLE_LOCATIONS = "locations";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_ADDR = "address";
	public static final String COLUMN_LAT = "latitude";
	public static final String COLUMN_LON = "longitude";
	
	private static final String DB_NAME = "locations.db";
	private static final int DB_VERSION = 1;
	
	private static final String DB_CREATE = 
			  "create table if not exists "
			+ TABLE_LOCATIONS + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_LAT + " REAL, "
			+ COLUMN_LON + " REAL, "
			+ COLUMN_ADDR + " text not null "
			+ ");";

	
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
	
	public long insertAddress(String address, Location location) {
		Log.i(TAG, "Attempting to insert address: " + address);
		
		final ContentValues values = new ContentValues();
		values.put(COLUMN_ADDR, address);
		values.put(COLUMN_LAT, location.getLatitude());
		values.put(COLUMN_LON, location.getLongitude());
		
		final SQLiteDatabase db = getWritableDatabase();
		long insertId = db.insert(TABLE_LOCATIONS, null, values);
		db.close();
		
		if (insertId == -1) {
			Log.e(TAG, "Failed to insert to database!");
		}
		
		return insertId;
	}
	
	public List<AddressRecord> getAddressRecords() {
		SQLiteDatabase db = getReadableDatabase();
		
		/*
		 * SELECT 'COLUMN_ADDR', 'COLUMN_LAT', 'COLUMN_LON', COUNT(*) 
		 * FROM 'TABLE_LOCATIONS'
		 * GROUP BY 'COLUMN_ADDR' 
		 */
		Cursor cursor = db.query(
				false, 
				TABLE_LOCATIONS, 
				new String[] { COLUMN_ADDR, COLUMN_LAT, COLUMN_LON, "COUNT(*)" }, 
				null, 
				null, 
				COLUMN_ADDR, 
				null, 
				COLUMN_ID + " DESC", 
				null, 
				null);
		
		List<AddressRecord> results = new ArrayList<AddressRecord>();
		
		if (cursor == null || !cursor.moveToFirst()) {
			return results;
		}
		
		while (!cursor.isAfterLast()) {
			String address = cursor.getString(0);
			double lat = cursor.getDouble(1);
			double lon = cursor.getDouble(2);
			int count = cursor.getInt(3);
			
			AddressRecord record = new AddressRecord(address, count, lat, lon);
			results.add(record);
			
			cursor.moveToNext();
		}
		
		cursor.close();
		return results;
	}
}
