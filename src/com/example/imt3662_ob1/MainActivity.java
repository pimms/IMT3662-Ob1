package com.example.imt3662_ob1;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.app.*;
import android.content.Context;

public class MainActivity 	extends Activity
							implements View.OnClickListener {
	
	GPSTracker gpsTracker;
	private Button btnGetLocation;
	private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        gpsTracker = new GPSTracker(this);
        
        initButton();
        initHandler();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* Init methods
     */
    protected void initButton() {
    	btnGetLocation = (Button)findViewById(R.id.button_get_loc);
    	btnGetLocation.setOnClickListener(this);
    }
    
	protected void initHandler() {
    	handler = new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			String result = (String)msg.obj;
    			if (msg.what == 0) {
    				Log.e("DBG", result);
    			} else if (msg.what == 1) {
    				TextView tv = (TextView)findViewById(R.id.textViewAddress);
    				tv.setText(result);
    			}
    		}
    	};
    }

    
    /* View.OnClickListener interface implementation
     */
    public void onClick(View v) {
    	if (v == btnGetLocation) {
    		onGetAddressClick();
    	}
    }
    
    public void onGetAddressClick() {
    	Location loc = gpsTracker.getLocation();
    	if (loc != null) {
    		new CoordTrans().translateLocation(loc, handler);
    	} else {
    		displayAlert("Dunno", "I don't know where you are.");
    	}
    }
    
    /* Error message display
     */
    private void displayAlert(String title, String message) {
    	Log.e(title, message);
    }
}
