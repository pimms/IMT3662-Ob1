package com.example.imt3662_ob1;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.app.*;
import android.content.Context;

public class MainActivity extends Activity {
	
	GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        gpsTracker = new GPSTracker(this);
        
        initButton();
    }
    
    protected void initButton() {
    	final Button button = (Button)findViewById(R.id.button_get_loc);
    	final TextView tvLat = (TextView)findViewById(R.id.textViewLatitude);
    	final TextView tvLon = (TextView)findViewById(R.id.textViewLongitude);
    	
    	button.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Location location = gpsTracker.getLocation();
    			if (location == null) {
    				return;
    			}
    			
    			StringBuilder str = new StringBuilder();
    			str.append(location.getLatitude());
    			tvLat.setText(str.toString());
    			
    			str.delete(0, 100);
    			str.append(location.getLongitude());
    			tvLon.setText(str.toString());
    		}
    	});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
