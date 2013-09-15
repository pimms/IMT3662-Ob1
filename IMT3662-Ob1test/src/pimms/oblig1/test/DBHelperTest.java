package pimms.oblig1.test;

import java.util.List;

import pimms.oblig1.AddressRecord;
import pimms.oblig1.DBHelper;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.test.AndroidTestCase;
import android.util.Log;

public class DBHelperTest extends AndroidTestCase {
	private DBHelper mDatabase;

	private final String DUMMY_ADDRESS = "hubba bubba";
	private final double DUMMY_LATITUDE = 34.25;
	private final double DUMMY_LONGITUDE = 15.3;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mDatabase = new DBHelper(getContext());
		
		// Force creation of table "locations"
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		mDatabase.onCreate(db);
		db.close();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testInsertAddress() throws Exception {
		long result = insertOneAddress();
		assert(result != -1);
	}
	
	public void testGetAddressRecords() throws Exception {
		// Insert a row to the database
		assert(insertOneAddress() != -1);
		
		// Ensure that exactly one element was inserted
		List<AddressRecord> records = mDatabase.getAddressRecords();
		Log.d("DBG", "Number of records: " + records.size());
		assertEquals(records.size(), 1);
		
		AddressRecord record = records.get(0);
		
		assertEquals(record.getAddress(), DUMMY_ADDRESS);
		assertEquals(record.getLatitude(), DUMMY_LATITUDE);
		assertEquals(record.getLongitude(), DUMMY_LONGITUDE);
		assert(record.getVisitCount() > 0);
	}
	
	
	private long insertOneAddress() throws Exception {
		String address = DUMMY_ADDRESS;
		Location loc = new Location("gps");
		loc.setLatitude(DUMMY_LATITUDE);
		loc.setLongitude(DUMMY_LONGITUDE);
		
		return mDatabase.insertAddress(address, loc);
	}
}
