package pimms.oblig1;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

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
		
		if (records.size() != 0) {
			double avgLat = 0;
			double avgLon = 0;
			for (AddressRecord record : records) {
				markAddressRecord(record);
				avgLat += record.getLatitude() / records.size();
				avgLon += record.getLongitude() / records.size();
			}
			
			LatLng avgPos = new LatLng(avgLat, avgLon);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(avgPos, 15));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	
	private Marker markAddressRecord(AddressRecord record) {
		LatLng coord = record.getCoordinates();
		Marker marker = map.addMarker(new MarkerOptions().position(coord));
		marker.setTitle(record.getMarkerTitle());
		
		return marker;
	}
}
