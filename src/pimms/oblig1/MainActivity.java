package pimms.oblig1;

import pimms.oblig1.R;

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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_main_list:
    		displayHistoryList();
    		break;
    		
    	case R.id.menu_main_map:
    		displayHistoryMap();
    		break;
    	}
    	
    	return false;
    }
    
    protected void displayHistoryList() {
    	Intent intent = new Intent(this, ListHistoryActivity.class);
    	startActivity(intent);
    }
    
    protected void displayHistoryMap() {
    	Intent intent = new Intent(this, MapHistoryActivity.class);
    	startActivity(intent);
    }

    /* 
     * Init methods
     */
    protected void initButton() {
    	mBtnGetLocation = (Button)findViewById(R.id.button_get_loc);
    	mBtnGetLocation.setOnClickListener(this);
    }
    
    
    /* 
     * View.OnClickListener interface implementation
     */
    public void onClick(View v) {
    	if (v == mBtnGetLocation) {
    		onGetAddressClick();
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
    
    /*
     * CoordTrans.CoordTransCallback implementation
     */
    @Override
	public void onTranslateCompleted(String result, Location location) {
		TextView tv = (TextView)findViewById(R.id.textViewAddress);
		tv.setText(result);
		
		mDbHelper.insertAddress(result, location);
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
