package com.example.imt3662_ob1;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.support.v4.app.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHistoryActivity extends Activity {
	
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_history);
		
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

		DBHelper dbHelper = new DBHelper(this);
		List<AddressRecord> records = dbHelper.getAddressRecords();
		
		Marker dest = null;
		for (AddressRecord record : records) {
			dest = markAddressRecord(record);
		}
		
		if (dest != null) {
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(dest.getPosition(), 15));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	
	private Marker markAddressRecord(AddressRecord record) {
		LatLng coord = addressRecordToLatLng(record);
		Marker marker = map.addMarker(new MarkerOptions().position(coord));
		
		return marker;
	}
	
	private LatLng addressRecordToLatLng(AddressRecord record) {
		LatLng coord = new LatLng(record.getLatitude(), record.getLongitude());
		return coord;
	}
}
