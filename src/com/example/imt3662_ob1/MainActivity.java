package com.example.imt3662_ob1;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;

public class MainActivity 	extends Activity
							implements  View.OnClickListener,
										CoordTrans.CoordTransCallback {
	
	GPSTracker mGpsTracker;
	private Button mBtnGetLocation;
	private Button mBtnHistory;
	private Handler mHandler;
	private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initButton();
        
        mGpsTracker = new GPSTracker(this);
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
    		CoordTrans coordinateTranslator = new CoordTrans(loc, this, this);
    		coordinateTranslator.execute();
    	} else {
    		displayAlert("Dunno", "Unable to retrieve your GPS coordinates");
    	}
    }
    
    protected void onDisplayHistory() {
    	Intent intent = new Intent(this, HistoryActivity.class);
    	startActivity(intent);
    }
    
    /*
     * CoordTrans.CoordTransCallback implementation
     */
    @Override
	public void onTranslateCompleted(String result) {
		TextView tv = (TextView)findViewById(R.id.textViewAddress);
		tv.setText(result);
		
		mDbHelper.insertAddress(result);
	}

	@Override
	public void onTranslateFailed(String errorMessage) {
		displayAlert("Something went wrong", errorMessage);
	}
    
    /* 
     * Error message display
     */
    private void displayAlert(String title, String message) {
    	new AlertDialog.Builder(this)
    		.setTitle(title)
    		.setMessage(message)
    		.setPositiveButton("Ok", null)
    		.show();
    		
    }
    
	
}
