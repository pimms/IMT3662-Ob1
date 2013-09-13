package pimms.oblig1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.json.*;

public class CoordTrans extends AsyncTask<Void, Void, String> {
	private static final String TAG = "CoordTrans";
	
	public interface CoordTransCallback {
		void onTranslateCompleted(String address, Location location);
		void onTranslateFailed(String errorMessage);
	}
	
	public static final int TRANSLATE_FAILURE = 0;
	public static final int TRANSLATE_SUCCESS = 1;
	
	private Location mLocation;
	private CoordTransCallback mCallback;
	private Context mContext;
	private ProgressDialog mProgress;
	
	
	public CoordTrans(Location location, CoordTransCallback callback, Context context) {
		super();
		
		mLocation = location;
		mCallback = callback;
		mContext = context;
	}
	
	@Override
	protected void onPreExecute() {
		mProgress = new ProgressDialog(mContext);
		mProgress.setIndeterminate(true);
		mProgress.setTitle("Please wait");
		mProgress.setMessage("Getting address...");
		mProgress.show();
	}
	
	@Override
	protected String doInBackground(Void... params) {
		String rawJson = getRawJson();
		
		if (rawJson == null) {
			Log.e(TAG, "Failed to contact googleapis.com");
			return null;
		}
		
		return getAddress(rawJson);
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (result == null) {
			// TODO:
			// Distinguish between possible errors
			mCallback.onTranslateFailed("Unknown error");
		} else {
			mCallback.onTranslateCompleted(result, mLocation);
		}
		
		mProgress.hide();
	}
	
	
	
	private String getAddress(String rawJson) {
		JSONObject json;
		
		try {
			json = new JSONObject(rawJson);
			
			String status = (String)json.get("status");
			if (status.equals("ZERO_RESULTS")) {
				return "Uncharted Territory";
			} else if (status.equals("OK")) {
				JSONArray array = json.getJSONArray("results");
				return array.getJSONObject(0).getString("formatted_address");
			} else {
				return null;
			}
		} catch (JSONException e) {
			return null;
		}
	}
	
	private String getRawJson() {
		double lat = mLocation.getLatitude();
		double lon = mLocation.getLongitude();
		
		StringBuilder sb = new StringBuilder();
		sb.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
		sb.append(lat).append(",");
		sb.append(lon).append("&sensor=true");
		
		StringBuilder content = new StringBuilder();
		
		try {
			URL url = new URL(sb.toString());
			InputStreamReader isr = new InputStreamReader(url.openStream());
			BufferedReader br = new BufferedReader(isr);
			
			String line = br.readLine();
			while (line != null) {
				content.append(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		
		Log.i(TAG, "Successfully retrieved address information");
		return content.toString();
	}

}
