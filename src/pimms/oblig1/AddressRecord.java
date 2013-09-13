package pimms.oblig1;

import com.google.android.gms.maps.model.LatLng;

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
	
	public LatLng getCoordinates() {
		LatLng coord = new LatLng(mLatitude, mLongitude);
		return coord;
	}
	
	/*
	 * Return a string on the following format:
	 * 		"ADDRESS (X visits)"
	 */
	public String getMarkerTitle() {
		StringBuilder sb = new StringBuilder();
		sb.append(mAddress);
		sb.append(" (");
		sb.append(mVisitCount);
		sb.append(" visits)");
		
		return sb.toString();
	}
}
