package pimms.oblig1;

public class AddressRecord {
	private String mAddress;
	private int mVisitCount;
	private double mLatitude;
	private double mLongitude;
	
	public AddressRecord(String address, int visitCount, double lat, double lon) {
		mAddress = address;
		mVisitCount = visitCount;
		mLatitude = lat;
		mLongitude = lon;
	}
	
	public String getAddress() {
		return mAddress;
	}
	
	public int getVisitCount() {
		return mVisitCount;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	public double getLongitude() {
		return mLongitude;
	}
}
