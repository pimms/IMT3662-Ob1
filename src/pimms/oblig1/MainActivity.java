package pimms.oblig1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    		displayGpsSettingsPrompt();
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
    
	private void displayGpsSettingsPrompt() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		
		alertDialog.setTitle("GPS is disabled");
		alertDialog.setMessage("GPS or Mobile Network must be enabled.");
		
		alertDialog.setPositiveButton("Settings" , new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		});
		
		alertDialog.setNegativeButton("Cancel", null);
		
		alertDialog.show();
	}
}
