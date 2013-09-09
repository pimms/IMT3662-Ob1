package com.example.imt3662_ob1;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DBHelper extends SQLiteOpenHelper {
	
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
	
	private SQLiteStatement mStmtInsert;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DB_CREATE);
		
		mStmtInsert = database.compileStatement(
				"INSERT INTO " + TABLE_LOCATIONS 
				+ " (" + COLUMN_ADDR + ") "
				+ "VALUES( ? );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
	}
	
	public void insertAddress(String address) {
		mStmtInsert.bindString(1, address);
		mStmtInsert.execute();
		mStmtInsert.clearBindings();
	}
	
	public ArrayList<String> getAllAddresses() {
		SQLiteDatabase db = getReadableDatabase();
		
		/*
		 * SELECT 'COLUMN_ADDR' FROM 'TABLE_LOCATIONS'
		 * ORDER BY 'COLUMN_ID'
		 */
		Cursor cursor = db.query(true, 
				TABLE_LOCATIONS, 
				new String[] { COLUMN_ADDR }, 
				null, 
				null, null, null, COLUMN_ID, null, null);
		
		ArrayList<String> results = new ArrayList<String>();
		
		while (!cursor.isAfterLast()) {
			String addr = cursor.getString(0);
			results.add(addr);
			cursor.moveToNext();
		}
		
		cursor.close();
		return results;
	}
}
