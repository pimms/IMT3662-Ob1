package com.example.imt3662_ob1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
	String mGpsProvider;
	
	LocationManager mLocationManager;
	Location mLocation;
	
	private Context mContext;
	private boolean mLocSubscription = false;
	
	public GPSTracker(Context ctx) {
		mContext = ctx;
		mLocationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
		
		if (servicesAvailable()) {
			subscribeLocation();
		} else {
			Log.d("DBG", "GPS services are not available :(\n");
		}
	}
	
	public boolean servicesAvailable() {
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mGpsProvider = LocationManager.GPS_PROVIDER;
		} else if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			mGpsProvider = LocationManager.NETWORK_PROVIDER;
		} else {
			return false;
		}
		
		return true;
	}
	
	public Location getLocation() {
		if (!servicesAvailable()) {
			Log.e("DBG", "GPS Services unavailable :(");
			return null;
		} 
		
		if (!mLocSubscription) {
			subscribeLocation();
			return null;
		}
		
		mLocation = mLocationManager.getLastKnownLocation(mGpsProvider);
		return mLocation;
	}
	
	
	private void subscribeLocation() {
		mLocationManager.requestLocationUpdates(mGpsProvider, 10000, 10, this);
		mLocSubscription = true;
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
