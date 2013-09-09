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
import android.content.Intent;

public class MainActivity 	extends Activity
							implements View.OnClickListener {
	
	GPSTracker mGpsTracker;
	private Button mBtnGetLocation;
	private Button mBtnHistory;
	private Handler mHandler;
	private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mGpsTracker = new GPSTracker(this);
        
        initButton();
        initHandler();
        
        mDbHelper = new DBHelper(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* 
     * Init methods
     */
    protected void initButton() {
    	mBtnHistory = (Button)findViewById(R.id.button_get_loc);
    	mBtnHistory.setOnClickListener(this);
    	
    	mBtnGetLocation = (Button)findViewById(R.id.button_see_history);
    	mBtnGetLocation.setOnClickListener(this);
    }
    
    /*
     * Handler for receival of translated
     * GPS coordinates
     */
    @SuppressLint("HandlerLeak")
	protected void initHandler() {
    	mHandler = new Handler() {
    		@Override
    		public void handleMessage(Message msg) {
    			String result = (String)msg.obj;
    			if (msg.what == CoordTrans.TRANSLATE_FAILURE) {
    				Log.e("DBG", result);
    			} else if (msg.what == CoordTrans.TRANSLATE_SUCCESS) {
    				TextView tv = (TextView)findViewById(R.id.textViewAddress);
    				tv.setText(result);
    				
    				mDbHelper.insertAddress(result);
    			}
    		}
    	};
    }

    
    /* 
     * View.OnClickListener interface implementation
     */
    public void onClick(View v) {
    	if (v == mBtnHistory) {
    		onGetAddressClick();
    	} else if (v == mBtnGetLocation) {
    		onDisplayHistory();
    	}
    }
    
    protected void onGetAddressClick() {
    	Location loc = mGpsTracker.getLocation();
    	if (loc != null) {
    		new CoordTrans().translateLocation(loc, mHandler);
    	} else {
    		displayAlert("Dunno", "I don't know where you are.");
    	}
    }
    
    protected void onDisplayHistory() {
    	Intent intent = new Intent(this, HistoryActivity.class);
    	startActivity(intent);
    }
    
    /* 
     * Error message display
     */
    private void displayAlert(String title, String message) {
    	Log.e(title, message);
    }
}
