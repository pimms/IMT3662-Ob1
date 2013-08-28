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
	
	boolean gpsEnabled = false;
	boolean netEnabled = false;
	
	LocationManager locationManager;
	Location location;
	
	private Context context;
	private boolean locSubscription = false;
	
	public GPSTracker(Context ctx) {
		context = ctx;
		locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
		
		if (servicesAvailable()) {
			subscribeLocation();
		}
	}
	
	public boolean servicesAvailable() {
		gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		netEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		return (gpsEnabled && netEnabled);
	}
	
	public Location getLocation() {
		if (!servicesAvailable()) {
			Log.e("DBG", "GPS Services unavailable :(");
			return null;
		} 
		
		if (!locSubscription) {
			subscribeLocation();
			return null;
		}
		
		return location;
	}
	
	
	private void subscribeLocation() {
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
		locSubscription = true;
	}
	
	
	@Override
	public void onLocationChanged(Location arg0) {
		location = arg0;
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
